alter table cemeteries add column if not exists  object_name varchar(255);
alter table cemeteries add column if not exists  open_term_type term_type;
alter table cemeteries add column if not exists  close_term_type term_type;
alter table cemeteries add column if not exists  open_date date;
alter table cemeteries add column if not exists  close_date date;
alter table cemeteries add column if not exists  substitute_performance boolean;
alter table cemeteries add column if not exists  perpetual_use boolean;
alter table cemeteries add column if not exists  church_perpetual_user boolean;
alter table cemeteries add column if not exists  church_owner boolean;
alter table cemeteries add column if not exists  church_regulated_by_law boolean;
alter table cemeteries add column if not exists  manager_exists boolean;
alter table cemeteries add column if not exists  user_admin_exists boolean;
alter table cemeteries add column if not exists  cemetery_type_id bigint;
alter table cemeteries add column if not exists  cemetery_type_id bigint;

create table if not exists cemetery_perpetual_users (
    id bigserial primary key,
    type perpetual_use_type not null,
    name varchar(100),
    first_name varchar(100),
    last_name varchar(100),
    nip varchar(50),
    regon varchar(50),
    email varchar(100),
    religion_id bigint,
    representative_id bigint
);

alter table cemeteries add column if not exists cemetery_perpetual_users_id bigint;
alter table cemeteries add foreign key (cemetery_perpetual_users_id) references cemetery_perpetual_users (id);

create table if not exists cemetery_owners (
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
    representative_id bigint
);

alter table cemeteries add column if not exists cemetery_owners_id bigint;
alter table cemeteries add foreign key (cemetery_owners_id) references cemetery_owners (id);

create table if not exists cemetery_managers (
     id bigserial primary key,
     type legal_form_type not null,
     name varchar(100),
     first_name varchar(100),
     last_name varchar(100),
     nip varchar(50),
     regon varchar(50),
     email varchar(100),
     representative_id bigint
);

alter table cemeteries add column if not exists cemetery_managers_id bigint;
alter table cemeteries add foreign key (cemetery_managers_id) references cemetery_managers (id);

create table if not exists cemetery_representatives (
     id bigserial primary key,
     type representative_type not null,
     first_name varchar(100),
     last_name varchar(100),
     email varchar(100)
);

alter table cemeteries add column if not exists cemetery_representatives_id bigint;
alter table cemeteries add foreign key (cemetery_representatives_id) references cemetery_representatives (id);

create table if not exists cemetery_user_admins (
    id bigserial primary key,
    first_name varchar(100),
    last_name varchar(100),
    email varchar(100)
);


alter table cemeteries add column if not exists cemetery_user_admins_id bigint;
alter table cemeteries add foreign key (cemetery_user_admins_id) references cemetery_user_admins (id);

create table if not exists cemetery_attachment
(
    id bigserial primary key,
    application_id bigint NOT NULL,
    file_hashed_name character varying(255) NOT NULL,
    size integer NOT NULL
);

alter table cemeteries add column if not exists cemetery_attachment_id bigint;
alter table cemeteries add foreign key (cemetery_attachment_id) references cemetery_attachment (id);


alter table applications add column if not exists application_cemetery_id bigint;
alter table applications add foreign key (application_cemetery_id) references application_cemeteries(id);