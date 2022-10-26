drop table if exists application_cemetery_staff cascade;
drop table if exists application_cemetery_staff_mapping cascade;
drop table if exists cemetery_staff cascade;
drop table if exists cemetery_staff_mapping cascade;
drop table if exists application_cemetery_staff_functions_mapping cascade;
drop table if exists cemetery_staff_functions_dictionary cascade;
drop type if exists perpetual_use_category;
drop type if exists perpetual_use_type;
drop type if exists legal_form_type;
drop type if exists territorial_unit_type;
drop type if exists staff_type;

create type perpetual_use_type as enum ('UNIT_UNREGULATED_BY_LAW', 'NATURAL_PERSON', 'LEGAL_PERSON');
create type legal_form_type as enum ('NATURAL_PERSON', 'INSTITUTION');
create type territorial_unit_type as enum ('VOIVODESHIP', 'DISTRICT', 'COMMUNITY');
create type representative_type as enum ('OWNER_REPRESENTATIVE', 'MANAGER_REPRESENTATIVE', 'PERPETUAL_USER_REPRESENTATIVE');

create table application_cemetery_applicants (
    id bigserial primary key,
    first_name varchar(100),
    last_name varchar(100),
    email varchar(100),
    phone_number varchar(50),
    application_id bigint
);

create table application_cemetery_perpetual_users (
    id bigserial primary key,
    type perpetual_use_type not null,
    name varchar(100),
    first_name varchar(100),
    last_name varchar(100),
    nip varchar(50),
    regon varchar(50),
    email varchar(100),
    religion_id bigint,
    representative_id bigint,
    application_id bigint
);

create table application_cemetery_owners (
    id bigserial primary key,
    name varchar(100),
    first_name varchar(100),
    last_name varchar(100),
    nip varchar(50),
    regon varchar(50),
    email varchar(100),
    territorial_unit_type territorial_unit_type,
    owner_category_id bigint,
    owner_subcategory_id bigint,
    address_id bigint,
    religion_id bigint,
    representative_id bigint,
    application_id bigint
);

create table application_cemetery_managers (
     id bigserial primary key,
     type legal_form_type not null,
     name varchar(100),
     first_name varchar(100),
     last_name varchar(100),
     nip varchar(50),
     regon varchar(50),
     email varchar(100),
     representative_id bigint,
     application_id bigint
);

create table application_cemetery_representatives (
     id bigserial primary key,
     type representative_type not null,
     first_name varchar(100),
     last_name varchar(100),
     email varchar(100),
     application_id bigint
);

create table application_cemetery_user_admins (
    id bigserial primary key,
    first_name varchar(100),
    last_name varchar(100),
    email varchar(100),
    application_id bigint
);

alter table application_cemetery_applicants add foreign key (application_id) references application_cemeteries (id);
alter table application_cemetery_perpetual_users add foreign key (religion_id) references churches_religions_dictionary (id);
alter table application_cemetery_perpetual_users add foreign key (representative_id) references application_cemetery_representatives (id);
alter table application_cemetery_perpetual_users add foreign key (application_id) references application_cemeteries (id);
alter table application_cemetery_owners add foreign key (owner_category_id) references cemetery_owner_categories_dictionary (id);
alter table application_cemetery_owners add foreign key (owner_subcategory_id) references cemetery_owner_categories_dictionary (id);
alter table application_cemetery_owners add foreign key (address_id) references application_addresses (id);
alter table application_cemetery_owners add foreign key (religion_id) references churches_religions_dictionary (id);
alter table application_cemetery_owners add foreign key (representative_id) references application_cemetery_representatives (id);
alter table application_cemetery_owners add foreign key (application_id) references application_cemeteries (id);
alter table application_cemetery_managers add foreign key (representative_id) references application_cemetery_representatives (id);
alter table application_cemetery_managers add foreign key (application_id) references application_cemeteries (id);
alter table application_cemetery_representatives add foreign key (application_id) references application_cemeteries (id);
alter table application_cemetery_user_admins add foreign key (application_id) references application_cemeteries (id);
