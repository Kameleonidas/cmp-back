create type user_account_status as enum ('NEW', 'ACTIVE', 'REJECTED', 'LOCKED');

alter table user_accounts add column if not exists status user_account_status;

CREATE SEQUENCE IF NOT EXISTS user_account_to_subjects_id_seq
INCREMENT 1
START 1;

create table user_account_to_subjects (
    id bigserial primary key,
    cemetery_id bigint,
    ipn_id bigint,
    voivodship_id bigint,
    crematorium_id bigint,
    active_employee boolean default false,
    email varchar(100) not null,
    phone_number varchar(50),
    user_account_id bigint
);

alter table user_account_to_subjects add foreign key (cemetery_id) references cemeteries (id);
alter table user_account_to_subjects add foreign key (user_account_id) references user_accounts (id);

CREATE SEQUENCE IF NOT EXISTS permissions_id_seq
INCREMENT 1
START 1;

create table permissions (
    id bigserial primary key,
    name varchar(100) not null,
    description varchar(200) not null,
    subject_id bigint
);

alter table permissions add foreign key (subject_id) references user_account_to_subjects (id);

CREATE SEQUENCE IF NOT EXISTS user_account_activity_periods_id_seq
INCREMENT 1
START 1;

create table user_account_activity_periods (
    id bigserial primary key,
    employed_from date,
    employed_to date,
    subject_id bigint
);

alter table user_account_activity_periods add foreign key (subject_id) references user_account_to_subjects (id);
