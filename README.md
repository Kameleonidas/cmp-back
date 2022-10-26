# CMP-BACK

## Opis modułu
TODO

## Import danych z GUGIK

### Import danych do tabel GUGIK

Dane importujemy do tabel `gugik_cemeteries` oraz `gugik_cemetery_geometries`.
Tabele z geometriami jest połączona z główną kluczem obcym `cemetery_id`.
Tabela `gugik_cemeteries` jest to tabela grupująca powierzchnie grzebalne i cmentarne danego cmentarza.
Natomiast `gugik_cemetery_geometries` zawiera ww. powierzchnie ze wskazaniem na cmentarz.

Kroki importu:
* import całych plików do tabel "tymczasowych" jako jedna krotka
* transformata podanych krotek na kolejne tabele tymczasowe z poprawnymi kolumnami (pola w geojson = kolumny w tabelach)
* kopiowanie danych do głównych tabel
  * kopiowanie jest realizowane wiersz po wierszu w pętli dlatego dla powierzchni cmentarnych od razu możemy uzupełnić klucz obcy
  * powierzchnie cmentarne kopiujemy do `gugik_cemeteries` oraz `gugik_cemetery_geometries` (mając id rekordu w `gugik_cemeteries` 
możemy uzupełnić `cemetery_id` w `gugik_cemetery_geometries`)
  * powierzchnie grzebalne tylko do tabeli `gugik_cemetery_geometries`
 
### Połączenie powierzchni grzebalnych z cmentarzmi 

Aby połączenie było możliwe do tabeli `gugik_cemeteries` została dodana kolumna `geometry_temp`. Zawiera ona koordynaty 
cmentarza i jest kopią powierzchni cmentarnej danego cmentarza.

Połączenie realizowane jest za pomocą funkcji POSTGisa:
* ST_Contains(geometryA, geometryB) - funkcja która zwraca true, jeżeli geometria B zawiera w całości w geometrii A
* ST_Overlaps(geometryA, geometryB) - funkcja ta zwraca true, jeżeli geometrie posiadają część wspólną, 
* ale jedna nie zawiera się całkowicie w drugiej

Algorytm łączenia danych:
* z tabeli `gugik_cemetery_geometries` pobierz wszystkie powierzchnie grzebalne które nie mają uzupełnionego klucz obcego `cemetery_id`
* dla każdego rekordu z zapytania powyżej wykonaj:
  * jezeli istnieje dokładnie jeden cmentarz (tabela `gugik_cemeteries`) wewnątrz którego powierzchni powierzchnia aktualnej 
powierzchni grzebalnej zawiera się w całości to ustaw pole `cemetery_id`
  * w przeciwnym wypadku sprawdź czy istnieje dokładnie jeden cmentarz (tabela `gugik_cemeteries`)  w którego powierzchni 
zawiera się częściowo aktualna powierzchnia grzebalna jeżeli tak to ustaw pole `cemetery_id`
  * w przeciwnym wypadku sprawdź czy istnieje dokładnie jeden cmentarz (tabela `gugik_cemeteries`) który zawiera się 
w aktualnej powierzchni grzebalnej jeżeli tak to ustaw pole `cemetery_id`

Jeżeli po tych krokach nie mogliśmy wyznaczyć pola `cemetery_id` to go nie uzupełniamy. Taki rekord należy na końcu 
procesu przeanalizować.

*UWAGA: procedura importu/reimportu opisana jest [tutaj](#cmp-back). Zawiera ona ww. kroki, tj. 
import danych jak i połączenie tabel kluczem obcym.*

### Przeniesienie danych z tabel GUGIK'owych do tabeli cmentarzy (dane uproszczone)

Kopiowane są dane z tabeli `gugik_cemetery_geometries` do tabel `cemeteries` oraz `cemetery_geometries`.  
Dane źródłowe to wszystkie powierzchnie cmentarne plus dodatkowo powierzchnie grzebalne niepołączone z żadnym cmentarzem
z wyłączeniem problematycznych danych.
* do tabeli `cemeteries` wędruje tylko nazwa cmentarza i opis, natomiast pola `status`, `published`, `source_id` oraz `facility_type_id`
ustawiane są zawsze na: `ACTIVE`, `true`, `2 - uproszczone` oraz `1 - cmentarz`
* do tabeli `cemetery_geometries` trafiają prawie wszystkie dane plus dodatkowo pole `id_iip` jest rozbijane na dwa pola:
  * `id_iip_identifier` - identyfikator za pomocą którego możemy zidentyfikować powierzchnię
  * `id_iip_version` - wersja danego obiektu
  * pole `id_iip` zawiera identyfikator GUGIK'owy oraz wersję obiektu. Rozdzielając je po znaku `_` otrzymamy trzy człony. 
Dwa pierwsze traktujemy jako identyfikator trzeci jako wersję.

### Uzupełnienie nazw i opisów cmentarzy

Nazwy i opisy cmentarzy przygotowuje zespół analityczny. Przekazaliśmy zespołowi zapytanie które można wykonać na bazie
i na podstawie jego zostały przygotowane opisy.  
Opis taki zawierał pole `id_iip` powierzchni nazwę oraz opis i na tej podstawie należało uzupełnić dane w bazie. 

### Uruchomienie procesu uzupełnienia adresów

Z tego względu że adresy pobieramy usługą REST'ową nie mogliśmy napisać skryptu z SQL'u. Został przygotowany mechanizm 
w aplikacji, który uruchamia się w osobnym wątku przy starcie aplikacji.

Kroki mechanizmu uzupełniania danych adresowych:
* sprawdzenie czy mechanizm był już  uruchomiony
  * nie ma wpisu w tabeli `system_processes` (dla `code='CEMETERIES_ADDRESSES_FILLER'`) albo jest wpis, ale ma status `WAS_NOT_RUN`
* jeżeli nie był uruchomiony to pobieramy z tabeli `gugik_cemetery_geometries` powierzchnie cmentarne bez przypisanych adresów
plus powierzchnie grzebalne bez przypisanych cmentarzy i bez przypisanych adresów
* dla każdego rekordu pobierz adres, przypisz go do aktualnego rekordu oraz dla tożsamego rekordu z tabeli `cemeteries`
  * w pierwszym kroku pobierane są adresy w kilku wątkach, przypisywane do mapy cmentarz -> adres
    * jeżeli dany cmentarz nie ma przypisanego centroidu (`address_point`) to jest wyznaczany na podstawie powierzchni
    * pobieramy adres z usługi `https://uldk.gugik.gov.pl/?request=GetRegionByXY&result=teryt,voivodeship,county,commune`, 
dodając parametr `xy`, który jest uzupełniany centroidem
    * usługa zwróci nam kod TERC oraz nazwy województwa, powiatu, gminy, które to dane ustawiamy w obiekcie tymczasowym 
`AddressDto` (kod TERC przetwarzamy odpowiednio na województwo, powiat i gminę)
    * następnie na postawie centroidu wywołujemy następną usługę, dzięki której dostaniemy nazwę miejscowości oraz jej kod SIMC
(usługa: `https://services.gugik.gov.pl/uug/?request=GetAddressReverse&srid=2180&radius=5000`, parametr do którego wrzucamy
koordynaty: `location`)
    * miejscowość oraz jej kod SIMC dodajemy do obiektu `AddressDto`
  * w następnym  kroku rekord Dto z adresem jest transformowany na poprawny adres i ustawiany dla aktualnie procesowanego
rekordu  `gugik_cemetery_geometries`
  * w ostatnim kroku na postawie aktualnego rekordu `gugik_cemetery_geometries` i jego pola `id_iip` pobierany jest rekord z
`cemetery_geometries`, a na jego podstawie rekord `cemeteries`
    * adres jest transformowany do poprawnej encji i trafia do wyznaczonego rekordu 
* sprawdź czy istnieje rekord w tabeli `cemeteries` bez przypisanego adresu `location_address_id`
  * dla każdego rekordu  pobierz adres a następnie przypisz go do aktualnego rekordu
    * z tego względu że relacja `cemeteries<---cemetery_geometries` jest 1:1 to pobieramy pierwszą encje `cemetery_geometries`
która jest połączona kluczem obcym z `cemeteries` 
    * mając encję geometrii na podstawie wartości z `id_iip_identifier` pobieramy wartość z `gugik_cemetery_geometries`
    * mapujemy adres z encji GUGIK'owej do obiektu `AddressDto`
    * następnie mapujemy do odpowiedniej encji i ustawiamy w aktualnym rekordzie `cemeteries`

## Reimport danych z GUGIK

Aby zrobić kolejny import danych z GUGIK wystarczy stworzyć plik z migracją sql'owa.
Plik ten musi składać się z trzech bloków.
* import pliku geojson z powierzchniami cmentarnymi do tabeli tymczasowej
* import pliku geojson z powierzchniami grzebalnymi do tabeli tymczasowej
* uruchomienie funkcji reimportującej

Szkielet migracji:
```roomsql
create table cemetery_data_import (doc text);
insert into cemetery_data_import values('<dane_cmentarzy_z_pliku_geojson>');


create  table cemetery_object_data_import (doc text);
insert into cemetery_object_data_import values('<dane_pow_grzebalnych_z_pliku_geojson>');

select reimport_gugik_data();
```
Poprawny reimport jest w pliku **V20220126083800__ECMDEV-339_reimport-GUGIK-data.sql**.
<br/>
**UWAGA:** Funkcja reimportująca wykonuje połączenie powierzchni grzebalnych z cmentarzami - jednak nie do końca można 
to zrobić automatycznie ze względu na niepoprawne dane itp. Dlatego po wykonaniu tej funkcji należy zweryfikować, czy są
jakieś powierzchnie grzebalne z `cemetery_id is null`. Jeżeli tak należy przeanalizować te przypadki i spróbować ręcznie  
połączyć te obiekty.

