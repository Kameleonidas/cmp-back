create type cemetery_status as enum ('ACTIVE', 'CLOSED', 'FOR_LIQUIDATION', 'LIQUIDATED');
create type availability_event_type as enum ('OPENING', 'CLOSING');
create type availability_event_date_type as enum ('YEAR', 'MONTH', 'DATE', 'YEARS_RANGE');
create type staff_type as enum ('NATURAL_PERSON', 'INSTITUTION');
create type cemetery_item_type as enum ('CEMETERY_SURFACE', 'BURIAL_SURFACE');
create type geometry_type as enum ('POINT', 'LINESTRING', 'POLYGON', 'MULTIPOINT', 'MULTILINESTRING', 'MULTIPOLYGON', 'GEOMETRYCOLLECTION');

create table application_cemeteries (
    id bigserial primary key,
    name varchar(200) not null,
    status cemetery_status not null,
    create_date date not null,
    update_date date,
    email varchar(100) not null,
    phone_number varchar(50),
    other_type varchar(200),
    other_religion varchar(200),
    facility_type_id bigint,
    location_address_id bigint not null,
    contact_address_id bigint,
    user_account_id bigint
);

create table application_cemetery_availability (
    id bigserial primary key,
    event_type availability_event_type not null,
    event_date_type availability_event_date_type not null,
    date_from date not null,
    date_to date,
    application_id bigint not null
);

create table application_cemetery_types_mapping (
    application_id bigint not null,
    type_id bigint not null
);

create table application_cemetery_religions_mapping (
    application_id bigint not null,
    religion_id bigint not null
);

create table application_cemetery_staff (
    id bigserial primary key,
    type staff_type not null,
    name varchar(100) not null,
    surname varchar(100),
    nip varchar(50),
    regon varchar(50),
    email varchar(100) not null,
    phone_number varchar(50),
    function_id bigint not null,
    representative_id bigint,
    user_account_id bigint
);

create table application_cemetery_staff_mapping (
    application_id bigint not null,
    staff_id bigint not null
);

create table cemeteries (
    id bigserial primary key,
    name varchar(200) not null,
    status cemetery_status not null,
    create_date date not null,
    registration_number varchar(100) not null,
    liquidation_date date,
    planned_liquidation_date date,
    email varchar(100) not null,
    phone_number varchar(50),
    other_type varchar(200),
    other_religion varchar(200),
    description varchar(200),
    published boolean default false not null,
    source_id bigint,
    facility_type_id bigint,
    location_address_id bigint not null,
    contact_address_id bigint,
    user_account_id bigint
);

create table cemetery_availability (
    id bigserial primary key,
    event_type availability_event_type not null,
    event_date_type availability_event_date_type not null,
    date_from date not null,
    date_to date,
    cemetery_id bigint not null
);

create table cemetery_types_mapping (
    cemetery_id bigint not null,
    type_id bigint not null
);

create table cemetery_religions_mapping (
    cemetery_id bigint not null,
    religion_id bigint not null
);

create table cemetery_staff (
    id bigserial primary key,
    type staff_type not null,
    name varchar(100) not null,
    surname varchar(100),
    nip varchar(50),
    regon varchar(50),
    email varchar(100) not null,
    phone_number varchar(50),
    function_id bigint not null,
    representative_id bigint,
    user_account_id bigint
);

create table cemetery_staff_mapping (
    cemetery_id bigint not null,
    staff_id bigint not null
);

create table cemetery_geometries (
    id bigserial primary key,
    name varchar(200),
    type cemetery_item_type not null,
    code varchar,
    create_date date,
    update_date date,
    description varchar(200),
    geometry_type geometry_type not null,
    geometry geometry not null,
    cemetery_id bigint not null
);

create table gugik_cemeteries (
    id bigserial primary key,
    name varchar(200),
    description varchar(200),
    import_date date
);

create table gugik_cemetery_geometries (
    id bigserial primary key,
    id_iip varchar not null,
    name varchar(200),
    type cemetery_item_type not null,
    code varchar,
    create_date date not null,
    update_date date,
    description varchar(200),
    geometry_type geometry_type not null,
    geometry geometry not null,
    cemetery_id bigint not null,
    address_id bigint not null
);


create table addresses (
    id bigserial primary key,
    terc_code char(7) not null,
    name varchar(100),
    place varchar(100),
    street varchar(100),
    number varchar(50),
    zip_code varchar(50),
    post_name varchar(100)
);

create table user_accounts (
    id bigserial primary key,
    wk_id varchar(100) not null,
    name varchar(100) not null,
    surname varchar(100) not null,
    birth_date date,
    email varchar(100) not null,
    phone_number varchar(50),
    role_id bigint not null
);

create table user_roles (
    id bigserial primary key,
    code varchar(50) not null,
    name varchar(100) not null
);

create table cemetery_types_dictionary (
    id bigserial primary key,
    code varchar(50) not null,
    name varchar(100) not null
);

create table cemetery_facility_types_dictionary (
    id bigserial primary key,
    code varchar(50) not null,
    name varchar(100) not null
);

create table cemetery_religions_dictionary (
    id bigserial primary key,
    code varchar(50) not null,
    name varchar(100) not null
);

create table cemetery_staff_functions_dictionary (
    id bigserial primary key,
    code varchar(50) not null,
    name varchar(100) not null
);

create table cemetery_sources_dictionary (
    id bigserial primary key,
    code varchar(50) not null,
    name varchar(100) not null
);

create table teryt_dictionary (
    code char(7),
    voivodeship char(2) not null,
    district char(2),
    community char(2),
    category char(1),
    name varchar(100) not null,
    type varchar(50) not null,
    valid_on date not null,
    constraint "teryt_dictionary_pk" primary key (code, valid_on)
);

alter table application_cemeteries add foreign key (facility_type_id) references cemetery_facility_types_dictionary (id);
alter table application_cemeteries add foreign key (location_address_id) references addresses (id);
alter table application_cemeteries add foreign key (contact_address_id) references addresses (id);
alter table application_cemeteries add foreign key (user_account_id) references user_accounts (id);
alter table application_cemetery_availability add foreign key (application_id) references application_cemeteries (id);
alter table application_cemetery_types_mapping add foreign key (application_id) references application_cemeteries (id);
alter table application_cemetery_types_mapping add foreign key (type_id) references cemetery_types_dictionary (id);
alter table application_cemetery_religions_mapping add foreign key (application_id) references application_cemeteries (id);
alter table application_cemetery_religions_mapping add foreign key (religion_id) references cemetery_religions_dictionary (id);
alter table application_cemetery_staff add foreign key (function_id) references cemetery_staff_functions_dictionary (id);
alter table application_cemetery_staff add foreign key (representative_id) references application_cemetery_staff (id);
alter table application_cemetery_staff add foreign key (user_account_id) references user_accounts (id);
alter table application_cemetery_staff_mapping add foreign key (application_id) references application_cemeteries (id);
alter table application_cemetery_staff_mapping add foreign key (staff_id) references application_cemetery_staff (id);
alter table cemeteries add foreign key (source_id) references cemetery_sources_dictionary (id);
alter table cemeteries add foreign key (facility_type_id) references cemetery_facility_types_dictionary (id);
alter table cemeteries add foreign key (location_address_id) references addresses (id);
alter table cemeteries add foreign key (contact_address_id) references addresses (id);
alter table cemetery_availability add foreign key (cemetery_id) references cemeteries (id);
alter table cemetery_types_mapping add foreign key (cemetery_id) references cemeteries (id);
alter table cemetery_types_mapping add foreign key (type_id) references cemetery_types_dictionary (id);
alter table cemetery_religions_mapping add foreign key (cemetery_id) references cemeteries (id);
alter table cemetery_religions_mapping add foreign key (religion_id) references cemetery_religions_dictionary (id);
alter table cemetery_staff add foreign key (function_id) references cemetery_staff_functions_dictionary (id);
alter table cemetery_staff add foreign key (representative_id) references cemetery_staff (id);
alter table cemetery_staff add foreign key (user_account_id) references user_accounts (id);
alter table cemetery_staff_mapping add foreign key (cemetery_id) references cemeteries (id);
alter table cemetery_staff_mapping add foreign key (staff_id) references cemetery_staff (id);
alter table cemetery_geometries add foreign key (cemetery_id) references cemeteries (id);
alter table gugik_cemetery_geometries add foreign key (cemetery_id) references gugik_cemeteries (id);
alter table gugik_cemetery_geometries add foreign key (address_id) references addresses (id);
alter table user_accounts add foreign key (role_id) references user_roles (id);
