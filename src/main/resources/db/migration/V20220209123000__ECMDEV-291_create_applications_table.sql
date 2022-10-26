create type application_type as enum ('CEMETERY_REGISTRATION', 'VETERAN_REGISTRATION');
create type application_status as enum ('DRAFT', 'SENT', 'TO_BE_COMPLETED', 'COMPLETED', 'ACCEPTED', 'REJECTED');

create table applications (
    id bigserial primary key,
    app_number varchar(100) not null,
    app_type application_type not null,
    app_status application_status not null,
    object_name varchar(100),
    create_date date not null,
    update_date date,
    user_account_id bigint
);

alter table applications add foreign key (user_account_id) references user_accounts (id);
