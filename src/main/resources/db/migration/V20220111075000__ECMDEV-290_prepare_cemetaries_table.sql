alter table cemetery_geometries
    add column id_iip_identifier varchar(200) not null;

alter table cemetery_geometries
    add column id_iip_version varchar(30) not null;

alter table cemeteries
    alter column email drop not null;

alter table cemeteries
    alter column name drop not null;

alter table cemeteries
    alter column registration_number drop not null;

alter table cemeteries
    alter column location_address_id drop not null;

alter table gugik_addresses
    drop column place_simc_code;

alter table gugik_addresses
    drop column street;

ALTER TABLE gugik_addresses
    RENAME COLUMN place TO area;

ALTER TABLE gugik_addresses
    RENAME COLUMN number TO plot_number;