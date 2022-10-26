create or replace function import_gugik_files_into_temporary_tables()
returns boolean as $result$
begin
    raise notice 'Begin importing gugik data to temporary tables ...';
    -- create temporary tables for gugik data import
    create table gugik_cemeteries_original (
        b_nazwa varchar null,
        b_rodzaj varchar null,
        x_aktual_1 date null,
        x_datautwo date null,
        x_informdo varchar null,
        x_kod varchar null,
        id_iip varchar null
    );
    PERFORM AddGeometryColumn ('public','gugik_cemeteries_original','wkb_geometry',2180,'MULTIPOLYGON',2);

    create table gugik_cemetery_objects_original (
        b_rodzaj varchar null,
        b_wyznanie varchar null,
        x_aktual_1 date null,
        x_datautwo date null,
        x_informdo varchar null,
        x_kod varchar null,
        id_iip varchar null
    );
    PERFORM AddGeometryColumn ('public','gugik_cemetery_objects_original','wkb_geometry',2180,'MULTIPOLYGON',2);

    -- conversion from geojson's to tables
    -- cemetery surfaces
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

    -- burial surfaces
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

    -- drop tables with geojson files
    drop table cemetery_data_import;
    drop table cemetery_object_data_import;

    raise notice 'Gugik data imported to temporary tables.';
    return true;
end;
$result$ language plpgsql;

------------------------------------------------------------------------------------------------------------------------
create or replace function import_gugik_data_from_temp_tables()
returns boolean as $result$
declare
    cemetery_temp record;
begin
    -- clean old data
    truncate table gugik_cemeteries cascade;
    truncate table gugik_cemetery_geometries cascade;
    truncate table gugik_addresses cascade;

    -- import from temp into main tables
    -- first cemeteries into gugik_cemeteries and gugik_cemetery_geometries with foreign key
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

   	-- next burial surfaces into gugik_cemetery_geometries
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

    -- drop temporary tables
    drop table if exists gugik_cemeteries_original;
    drop table if exists gugik_cemetery_objects_original;

    raise notice 'Cemeteries imported to original tables.';
    return true;
end;
$result$ language plpgsql;

------------------------------------------------------------------------------------------------------------------------
create or replace function connect_burial_surfaces_with_cemeteries()
returns boolean as $result$
declare
    cemetery_geometry record;
    connections_count integer;
    cemetery_id_temp integer;
begin

    raise notice 'Begin connecting cemeteries objects with geometries ...';

    for cemetery_geometry in select * from gugik_cemetery_geometries where type = 'BURIAL_SURFACE' and cemetery_id is null
    loop

        select count(*) into connections_count
        from gugik_cemeteries gc
        where ST_Contains(gc.geometry_temp, cemetery_geometry.geometry);

        if connections_count = 1 then
            select id into cemetery_id_temp
            from gugik_cemeteries gc
            where ST_Contains(gc.geometry_temp, cemetery_geometry.geometry);

            update gugik_cemetery_geometries
            set cemetery_id = cemetery_id_temp
            where id=cemetery_geometry.id;
        else
            select count(*) into connections_count
            from gugik_cemeteries gc
            where ST_Overlaps(gc.geometry_temp, cemetery_geometry.geometry);

            if connections_count = 1 then
                select id into cemetery_id_temp
                from gugik_cemeteries gc
                where ST_Overlaps(gc.geometry_temp, cemetery_geometry.geometry);

                update gugik_cemetery_geometries
                set cemetery_id = cemetery_id_temp
                where id=cemetery_geometry.id;
            else
                select count(*) into connections_count
                from gugik_cemeteries gc
                where ST_Contains(cemetery_geometry.geometry, gc.geometry_temp);

                if connections_count = 1 then
                    select id into cemetery_id_temp
                    from gugik_cemeteries gc
                    where ST_Contains(cemetery_geometry.geometry, gc.geometry_temp);

                    update gugik_cemetery_geometries
                    set cemetery_id = cemetery_id_temp
                    where id=cemetery_geometry.id;
                end if;
            end if;
        end if;
    end loop;
    raise notice 'Cemetery objects connected.';

    return true;
end;
$result$ language plpgsql;

------------------------------------------------------------------------------------------------------------------------
create or replace function reimport_gugik_data()
returns boolean as $result$
begin

    raise notice 'Begin reimporting GUGIK data ...';

    PERFORM import_gugik_files_into_temporary_tables();
    PERFORM import_gugik_data_from_temp_tables();
    PERFORM connect_burial_surfaces_with_cemeteries();

    raise notice 'GUGIK data imported.';
    return true;
end;
$result$ language plpgsql;
