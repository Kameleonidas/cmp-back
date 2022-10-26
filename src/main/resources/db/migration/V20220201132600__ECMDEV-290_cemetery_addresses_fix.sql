alter table gugik_addresses
    drop column plot_number;

create temp table addresses_to_remove as
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
    from addresses_to_remove
);

create temp table cemetery_addresses_to_remove as
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
    from cemetery_addresses_to_remove
);

update system_processes
set state = 'WAS_NOT_RUN'
where code = 'CEMETERIES_ADDRESSES_FILLER';