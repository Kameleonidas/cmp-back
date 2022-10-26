alter table gugik_addresses
    RENAME COLUMN area TO place;

alter table gugik_addresses
    add COLUMN place_simc_code char(7);

create index cemetery_geometries_id_iip_identifier_idx on cemetery_geometries (id_iip_identifier);

create temp table gugik_addresses_to_remove as
select address_id
from gugik_cemetery_geometries
where address_id is not null;

update gugik_cemetery_geometries
set address_id = null
where address_id is not null;

delete
from gugik_addresses
where id in (
    select address_id
    from gugik_addresses_to_remove
);

create temp table gugik_cemetery_addresses_to_remove as
select location_address_id
from cemeteries
where location_address_id is not null;

update cemeteries
set location_address_id = null
where location_address_id is not null;

delete
from cemetery_addresses
where id in (
    select location_address_id
    from gugik_cemetery_addresses_to_remove
);

update system_processes
set state = 'WAS_NOT_RUN'
where code = 'CEMETERIES_ADDRESSES_FILLER';