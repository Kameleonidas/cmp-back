-- tabele tymczasowe dla danych z geojson'ów
create table gugik_cemeteries_original (
    b_nazwa varchar null,
    b_rodzaj varchar null,
    x_aktual_1 date null,
    x_datautwo date null,
    x_informdo varchar null,
    x_kod varchar null,
    id_iip varchar null
);
SELECT AddGeometryColumn ('public','gugik_cemeteries_original','wkb_geometry',2180,'MULTIPOLYGON',2);

create table gugik_cemetery_objects_original (
    b_rodzaj varchar null,
    b_wyznanie varchar null,
    x_aktual_1 date null,
    x_datautwo date null,
    x_informdo varchar null,
    x_kod varchar null,
    id_iip varchar null
);
SELECT AddGeometryColumn ('public','gugik_cemetery_objects_original','wkb_geometry',2180,'MULTIPOLYGON',2);

-- konwersja z jsonów (tabele tymczasowe) na konkretne dane
--powierzchnie cmentarne
insert into gugik_cemeteries_original
select
    properties->>'B_NAZWA' as b_nazwa,
    properties->>'B_RODZAJ' as b_rodzaj,
    to_date(properties->>'X_AKTUAL_1', 'YYYY/MM/DD') as x_aktual_1,
    to_date(properties->>'X_DATAUTWO', 'YYYY/MM/DD') as x_DATAUTWO,
    properties->>'X_INFORMDO' as x_informdo,
    properties->>'X_KOD' as x_kod,
    properties->>'ID_IIP' as id_iip,
    st_setsrid(st_geomfromgeojson(geometry), 2180) as geom
from (
    select
        json_extract_path(values, 'properties') as properties,
        json_extract_path(values, 'geometry') as geometry
    from (
        select
            json_array_elements(json_extract_path(doc::json, 'features')) as values
        from
            cemetery_data_import
        ) features
    ) flat_data;

-- powierzchnie grzebalne
insert into gugik_cemetery_objects_original
select
    properties->>'B_RODZAJ' as b_rodzaj,
    properties->>'B_WYZNANIE' as b_wyznanie,
    to_date(properties->>'X_AKTUAL_1', 'YYYY/MM/DD') as x_aktual_1,
    to_date(properties->>'X_DATAUTWO', 'YYYY/MM/DD') as x_DATAUTWO,
    properties->>'X_INFORMDO' as x_informdo,
    properties->>'X_KOD' as x_kod,
    properties->>'ID_IIP' as id_iip,
    st_setsrid(st_geomfromgeojson(geometry), 2180) as geom
from (
    select
        json_extract_path(values, 'properties') as properties,
        json_extract_path(values, 'geometry') as geometry
    from (
        select
            json_array_elements(json_extract_path(doc::json, 'features')) as values
        from
            cemetery_object_data_import
        ) features
    ) flat_data;

-- przygotowanie tabel na import
alter table gugik_cemeteries
    add column geometry_type_temp geometry_type;

SELECT AddGeometryColumn ('public','gugik_cemeteries','geometry_temp',2180,'MULTIPOLYGON',2);

alter table gugik_cemetery_geometries
	add column kind varchar(10);

alter table gugik_cemetery_geometries
    alter column description type varchar(400);

alter table gugik_cemetery_geometries
    alter column cemetery_id drop not null;

alter table gugik_cemetery_geometries
    alter column address_id drop not null;

-- import do głównych tabel
-- cmentarze od razu do dwóch tabel, rekord po rekordzie, żeby zrobić powiązania (bez użycia ST_Intersects na współrzędnych)
do
$do$
declare
    cemetery_temp record;
begin
    raise notice 'Begin importing cemeteries to original tables ...';

    for cemetery_temp in select * from gugik_cemeteries_original
    loop

        insert into gugik_cemeteries
        select
            nextval('gugik_cemeteries_id_seq'),
            cemetery_temp.b_nazwa,
            cemetery_temp.x_informdo,
            current_date,
            geometrytype(cemetery_temp.wkb_geometry)::geometry_type,
            cemetery_temp.wkb_geometry;

        insert into gugik_cemetery_geometries
        select
            nextval('gugik_cemetery_geometries_id_seq'),
            cemetery_temp.id_iip,
            cemetery_temp.b_nazwa,
            'CEMETERY_SURFACE',
            cemetery_temp.x_kod,
            cemetery_temp.x_datautwo,
            cemetery_temp.x_aktual_1,
            cemetery_temp.x_informdo,
            geometrytype(cemetery_temp.wkb_geometry)::geometry_type,
            cemetery_temp.wkb_geometry,
            currval('gugik_cemeteries_id_seq'),
            null,
           cemetery_temp.b_rodzaj;

    end loop;
    raise notice 'Cemeteries imported.';
end;
$do$;

-- cemetery objects into gugik_cemetery_geometries
insert into gugik_cemetery_geometries
select
    nextval('gugik_cemetery_geometries_id_seq'),
    id_iip,
    null,
    'BURIAL_SURFACE',
    x_kod,
    x_datautwo,
    x_aktual_1,
    x_informdo,
    geometrytype(wkb_geometry)::geometry_type,
    wkb_geometry,
    null,
    null,
    b_rodzaj
from gugik_cemetery_objects_original
where b_rodzaj != 'Zwr';

-- czyszczenie
drop table cemetery_data_import;
drop table cemetery_object_data_import;
drop table gugik_cemeteries_original ;
drop table gugik_cemetery_objects_original;
