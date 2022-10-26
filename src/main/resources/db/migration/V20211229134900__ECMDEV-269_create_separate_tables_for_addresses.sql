drop table if exists addresses cascade;

create table gugik_addresses (
    id bigserial primary key,
    voivodeship_terc_code char(2) not null,
    district_terc_code char(4),
    commune_terc_code char(7),
    place_simc_code char(7),
    voivodeship varchar(100),
    district varchar(100),
    commune varchar(100),
    place varchar(100),
    street varchar(100),
    number varchar(50)
);

create table application_addresses (
    id bigserial primary key,
    voivodeship_terc_code char(2) not null,
    district_terc_code char(4),
    commune_terc_code char(7),
    place_simc_code char(7),
    voivodeship varchar(100),
    district varchar(100),
    commune varchar(100),
    place varchar(100),
    street varchar(100),
    number varchar(50),
    zip_code varchar(50),
    post_name varchar(100)
);

create table cemetery_addresses (
    id bigserial primary key,
    voivodeship_terc_code char(2) not null,
    district_terc_code char(4),
    commune_terc_code char(7),
    place_simc_code char(7),
    voivodeship varchar(100),
    district varchar(100),
    commune varchar(100),
    place varchar(100),
    street varchar(100),
    number varchar(50),
    zip_code varchar(50),
    post_name varchar(100)
);

alter table gugik_cemetery_geometries add foreign key (address_id) references gugik_addresses (id);
alter table application_cemeteries add foreign key (location_address_id) references application_addresses (id);
alter table application_cemeteries add foreign key (contact_address_id) references application_addresses (id);
alter table cemeteries add foreign key (location_address_id) references cemetery_addresses (id);
alter table cemeteries add foreign key (contact_address_id) references cemetery_addresses (id);

