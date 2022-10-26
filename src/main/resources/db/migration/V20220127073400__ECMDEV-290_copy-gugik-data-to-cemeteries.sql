alter table cemetery_geometries
    drop column type;


-- truncate tables
truncate table cemeteries cascade;
truncate table cemetery_geometries cascade;
truncate table cemetery_addresses cascade;

--fix problematic data
update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.337.BDOT10k_2B016A15-000D-946C-E053-CC2BA8C0BCFA%')
where id_iip like 'PL.PZGiK.337.BDOT10k_1f74936c-2d3b-426f-a7f5-9cf1b291f903%';

update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.333.BDOT10k_2881935E-974F-1086-E053-CC2BA8C0249D%')
where id_iip like 'PL.PZGiK.333.BDOT10k_7a744f4b-31ac-4619-9fa4-2f9eed7340aa%';

update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.335.BDOT10k_2BE4B432-4273-1BF0-E053-CC2BA8C0D140%')
where id_iip like 'PL.PZGiK.335.BDOT10k_2BE4B431-CBD2-1BF0-E053-CC2BA8C0D140%';

update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.333.BDOT10k_2881935E-973F-1086-E053-CC2BA8C0249D%')
where id_iip like 'PL.PZGiK.333.BDOT10k_2881935E-414A-1086-E053-CC2BA8C0249D%';

update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.238.BDOT10k_30FA02A8-300A-8845-E053-CA2BA8C07C9A%')
where id_iip like 'PL.PZGiK.238.BDOT10k_30FA02A7-AF82-8845-E053-CA2BA8C07C9A%';

update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.340.BDOT10k_2F3F1AF9-2682-68EB-E053-CA2BA8C0BE99%')
where id_iip like 'PL.PZGiK.340.BDOT10k_2F3F1AF8-64E4-68EB-E053-CA2BA8C0BE99%';

update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.335.BDOT10k_2ED380A9-19AA-5AC4-E053-CA2BA8C0AB34%')
where id_iip like 'PL.PZGiK.335.BDOT10k_2ED380A8-B704-5AC4-E053-CA2BA8C0AB34%';

update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.335.BDOT10k_2BE4B432-4266-1BF0-E053-CC2BA8C0D140%')
where id_iip like 'PL.PZGiK.335.BDOT10k_2BE4B431-CBDC-1BF0-E053-CC2BA8C0D140%';

update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.238.BDOT10k_314DE349-76F0-D9E0-E053-CA2BA8C04748%')
where id_iip like 'PL.PZGiK.238.BDOT10k_314DE349-3A07-D9E0-E053-CA2BA8C04748%';

update gugik_cemetery_geometries
set cemetery_id = (select cemetery_id 
                    from gugik_cemetery_geometries gcg
                    where id_iip like 'PL.PZGiK.994.BDOT10k_b0a848cc-6638-495b-9c5d-3334a57a22ee%')
where id_iip like 'PL.PZGiK.994.BDOT10k_2D2066FD-867B-B891-E053-CC2BA8C09FBE%';

-- copy records
do
$do$
declare
    cemetery_geometry record;
    address_id_temp integer;
begin
    raise notice 'Filling cemeteries table ...';

    -- copy cemeteries and burials without cemetery link and without problematic records
    for cemetery_geometry in
        select *
        from gugik_cemetery_geometries
        where
            type = 'CEMETERY_SURFACE'
            or (type = 'BURIAL_SURFACE' and cemetery_id is null and id not in(
                select distinct gcg.id
                from gugik_cemetery_geometries gcg
                join gugik_cemeteries gc on ST_Intersects(gc.geometry_temp, gcg.geometry)
                where gcg.type = 'BURIAL_SURFACE' and gcg.cemetery_id is null
            ))
    loop
        insert into cemeteries
        select
            nextval('cemeteries_id_seq'),
            cemetery_geometry.name,
            'ACTIVE',
            cemetery_geometry.create_date,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            cemetery_geometry.description,
            true,
            2,
            1,
            null,
            null,
            null;

        insert into cemetery_geometries
        select
            nextval('cemetery_geometries_id_seq'),
            cemetery_geometry.name,
            cemetery_geometry.code,
            cemetery_geometry.create_date,
            cemetery_geometry.update_date,
            cemetery_geometry.description,
            cemetery_geometry.geometry_type,
            cemetery_geometry.geometry,
            currval('cemeteries_id_seq'),
            split_part(cemetery_geometry.id_iip, '_', 1) || '_' || split_part(cemetery_geometry.id_iip, '_', 2),
            split_part(cemetery_geometry.id_iip, '_', 3);

    end loop;
    raise notice 'Cemeteries table filled.';
end;
$do$;

----update system_processes table  - app should run address filler once again
update system_processes
set state='WAS_NOT_RUN'
where code='CEMETERIES_ADDRESSES_FILLER';