drop table if exists application_cemetery_availability cascade;
drop table if exists cemetery_availability cascade;
drop table if exists cemetery_religions_dictionary cascade;
drop table if exists application_cemeteries cascade;
drop type if exists availability_event_date_type;
drop type if exists availability_event_type;
drop type if exists term_type;

create type term_type as enum ('YEAR', 'MONTH', 'DATE', 'YEARS_RANGE');

create table application_cemeteries (
    id bigserial primary key,
    cemetery_status cemetery_status not null,
    object_name varchar(100),
    object_description varchar(200),
    contact_email varchar(100),
    contact_phone_number varchar(50),
    open_term_type term_type,
    close_term_type term_type,
    open_date date,
    close_date date,
    open_term varchar(50),
    close_term varchar(50),
    other_type varchar(200),
    other_religion varchar(200),
    substitute_performance boolean default false,
    perpetual_use boolean default false,
    church_perpetual_user boolean default false,
    church_owner boolean default false,
    church_regulated_by_law boolean default false,
    manager_exists boolean default false,
    user_admin_exists boolean default false,
    cemetery_type_id bigint,
    facility_type_id bigint,
    religion_id bigint,
    location_address_id bigint,
    contact_address_id bigint,
    application_id bigint
);

alter table application_cemeteries add foreign key (cemetery_type_id) references cemetery_types_dictionary (id);
alter table application_cemeteries add foreign key (facility_type_id) references cmp.public.cemetery_facility_types_dictionary (id);
alter table application_cemeteries add foreign key (religion_id) references churches_religions_dictionary (id);
alter table application_cemeteries add foreign key (location_address_id) references application_addresses (id);
alter table application_cemeteries add foreign key (contact_address_id) references application_addresses (id);
alter table application_cemeteries add foreign key (application_id) references applications (id);
