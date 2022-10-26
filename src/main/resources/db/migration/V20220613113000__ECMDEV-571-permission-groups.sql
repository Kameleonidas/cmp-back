SET SESSION CHARACTERISTICS AS TRANSACTION READ WRITE;

DO
$$
DECLARE
    permission_group_1_id BIGINT := nextval('permission_groups_id_seq');
    permission_group_2_id BIGINT := nextval('permission_groups_id_seq');
    permission_group_3_id BIGINT := nextval('permission_groups_id_seq');
    permission_group_4_id BIGINT := nextval('permission_groups_id_seq');
    permission_group_5_id BIGINT := nextval('permission_groups_id_seq');
    permission_group_6_id BIGINT := nextval('permission_groups_id_seq');
    permission_1_id BIGINT := nextval('permissions_id_seq');
    permission_2_id BIGINT := nextval('permissions_id_seq');
    permission_3_id BIGINT := nextval('permissions_id_seq');
    permission_4_id BIGINT := nextval('permissions_id_seq');
    permission_5_id BIGINT := nextval('permissions_id_seq');
    permission_6_id BIGINT := nextval('permissions_id_seq');
    permission_7_id BIGINT := nextval('permissions_id_seq');
    permission_8_id BIGINT := nextval('permissions_id_seq');
    permission_9_id BIGINT := nextval('permissions_id_seq');
    permission_10_id BIGINT := nextval('permissions_id_seq');
    permission_11_id BIGINT := nextval('permissions_id_seq');
    permission_12_id BIGINT := nextval('permissions_id_seq');
    permission_13_id BIGINT := nextval('permissions_id_seq');
    permission_14_id BIGINT := nextval('permissions_id_seq');
    permission_15_id BIGINT := nextval('permissions_id_seq');
    permission_16_id BIGINT := nextval('permissions_id_seq');
BEGIN
  --Wnioski - cmentarze
  insert into permission_groups(id, name, description) values
  (permission_group_1_id, 'Wnioski - cmentarze', 'Grupa uprawnień przeznaczona dla modułu administracyjnego systemu CMP w zakresie przeglądania i procedowania wniosków o wpis cmentarzy do rejesteru cmentarzy.');

  insert into permission_group_institution_types (permission_group_id, institution_type) values
  (permission_group_1_id, 'CMP');

  insert into permissions (id, name, description) values
  (permission_1_id, 'Odczyt wniosku o wpis cmentarza', 'Uprawnienie umożliwia przeglądanie danych wniosków o wpis cmentarza do rejestru, dostęp do listy wszystkich wniosków, oraz przeglądaniu szczegółów wniosku.'),
  (permission_2_id, 'Procedowanie wniosku', 'Uprawnienie umożliwia procedowanie wniosku o wpis cmentarza do rejestru (akceptację, odrzucenie, przekazanie do uzupełniania).');

  insert into permission_groups_permissions(permission_group_id, permission_id) values
  (permission_group_1_id, permission_1_id),
  (permission_group_1_id, permission_2_id);

  --Dane instytucji
  insert into permission_groups(id, name, description) values
  (permission_group_2_id, 'Instytucja dane ogólne - odczyt', 'Grupa uprawnień przeznaczona dla właścicieli / zarządców / pracowników instytucji, w której są zatrudnieni. Uprawnienie umożliwia na dostęp do szczegółowych danych instytucji w trybie odczytu danych (szczegółowe dane rejestracyjne cmentarza, załączniki instytucji ze statusem nieopublikowane, pełnego widoku instytucji.');

  insert into permission_group_institution_types (permission_group_id, institution_type) values
  (permission_group_2_id, 'CMP'),
  (permission_group_2_id, 'CEMETERY');

  insert into permissions (id, name, description) values
  (permission_3_id, 'Szczegóły cmentarza', 'Uprawnienie umożliwia dostęp do szczegółowych danych instytucji.'),
  (permission_4_id, 'Mapa cmentarza', 'Uprawnienie umożliwia podgląd mapy cmentarza.'),
  (permission_5_id, 'Pliki cmentarza', 'Uprawnienie umożliwia przeglądanie wszystkich plików cmentarza (również tych nieopublikowanych).');

  insert into permission_groups_permissions(permission_group_id, permission_id) values
  (permission_group_2_id, permission_3_id),
  (permission_group_2_id, permission_4_id),
  (permission_group_2_id, permission_5_id);

  --(rozszeżenie uprawień "Instytucja dane ogólne - odczyt" )
  insert into permission_groups(id, name, description) values
  (permission_group_3_id, 'Instytucja dane ogólne - zarządzanie', 'Grupa uprawnień przeznaczona dla właścicieli / zarządców instytucji. Uprawnienia umożliwiają edycję danych instytucji, dodawanie zdjęć / załączników oraz publikowanie ich, przeglądanie historii zmian.');

  insert into permission_group_institution_types (permission_group_id, institution_type) values
  (permission_group_3_id, 'CEMETERY');

  insert into permissions (id, name, description) values
  (permission_6_id, 'Zarządzanie instytucją', 'Uprawnienie umożliwia edycję danych instytucji.'),
  (permission_7_id, 'Zarządzanie plikami', 'Uprawnienie umożliwia dodawanie i usuwanie plików z galerii i załączników instytucji, oraz publikowanie lub blokowanie ich publikacji.'),
  (permission_8_id, 'Edycja mapy cmentarza', 'Uprawnienie umożliwia edycje powierzchni i obrysu cmentarza.'),
  (permission_9_id, 'Historia zmiany danych', 'Uprawnienie umożliwia przeglądanie historii zmian danych instytucji.');

  insert into permission_groups_permissions(permission_group_id, permission_id) values
  (permission_group_3_id, permission_6_id),
  (permission_group_3_id, permission_7_id),
  (permission_group_3_id, permission_8_id),
  (permission_group_3_id, permission_9_id);

  --Użytkownicy
  insert into permission_groups(id, name, description) values
  (permission_group_4_id, 'Użytkownik instytucji - odczyt', 'Grupa uprawnień przeznaczona dla administratorów użytkowników / właścicieli / zarządców. Umożliwia na dostęp w systemie CMP do sekcji Użytkownicy. Umożliwia również na dostęp do listy i szczegółów użytkownika w trybie podglądu.');

  insert into permission_group_institution_types (permission_group_id, institution_type) values
  (permission_group_4_id, 'CEMETERY'),
  (permission_group_4_id, 'CREMATORIUM'),
  (permission_group_4_id, 'VOIVODSHIP_OFFICE'),
  (permission_group_4_id, 'IPN');

  insert into permissions (id, name, description) values
  (permission_10_id, 'Lista użytkowników', 'Uprawnienie umożliwia podgląd listy użytkowników w instytucji.'),
  (permission_11_id, 'Szczegóły uzytkownika', 'Uprawnienie umożliwia podgląd szczegółów każdego z użytkowników instytucji.');

  insert into permission_groups_permissions(permission_group_id, permission_id) values
  (permission_group_4_id, permission_10_id),
  (permission_group_4_id, permission_11_id);

  --(rozszeżenie uprawień "Użytkownicy" )
  insert into permission_groups(id, name, description) values
  (permission_group_5_id, 'Użytkownik instytucji - odczyt', 'Grupa uprawnień rozszerza uprawnienia administratora użytkowników o możliwość zarządzania użytkownikami instytucji (dodanie użytkownika do instytucji, zmiany uprawnień i edycji danych w instytucji).');

  insert into permission_group_institution_types (permission_group_id, institution_type) values
  (permission_group_5_id, 'CEMETERY'),
  (permission_group_5_id, 'CREMATORIUM'),
  (permission_group_5_id, 'VOIVODSHIP_OFFICE'),
  (permission_group_5_id, 'IPN');

  insert into permissions (id, name, description) values
  (permission_12_id, 'Wysłanie zaproszenia', 'Uprawnienie umożliwia na wysłanie zaproszenia do pracownika instytucji w celu zaproszenia go do założenia konta użytkownika w systemie CMP i nadania uprawnień w instytucji.'),
  (permission_13_id, 'Edycja uprawnień użytkownika', 'Uprawnienie umożliwia zarządanie uprawnieniami użytkownika w instytucji.'),
  (permission_14_id, 'Edycja danych użytkownika', 'Uprawnienia umożliwia edycje danych użytkownika w instytucji.');

  insert into permission_groups_permissions(permission_group_id, permission_id) values
  (permission_group_5_id, permission_12_id),
  (permission_group_5_id, permission_13_id),
  (permission_group_5_id, permission_14_id);

  --Użytkownicy systemu
  insert into permission_groups(id, name, description) values
  (permission_group_6_id, 'Użytkownik CMP - zarządzanie', 'Grupa uprawnień przeznaczona dla administratora systemu CMP do zarządzania użytkownikami na poziomie centralnym aplikacji.');

  insert into permission_group_institution_types (permission_group_id, institution_type) values
  (permission_group_6_id, 'CMP');

  insert into permissions (id, name, description) values
  (permission_15_id, 'Lista użytkowników systemu', 'Lista użytkowników systemu'),
  (permission_16_id, 'Blokada użytkowika', 'Uprawnienie umożliwia zablokowanie / odblokowanie użytkownika w systemie.');

  insert into permission_groups_permissions(permission_group_id, permission_id) values
  (permission_group_6_id, permission_12_id),
  (permission_group_6_id, permission_13_id),
  (permission_group_6_id, permission_14_id),
  (permission_group_6_id, permission_15_id),
  (permission_group_6_id, permission_16_id);
END;
$$ LANGUAGE plpgsql;