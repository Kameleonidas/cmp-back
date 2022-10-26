create table invitations (
     id bigserial primary key,
     name varchar(200) not null,
     email varchar(100) not null,
     request_identifier varchar(50) not null,
     status varchar(20) not null,
     create_date timestamp not null,
     modification_date timestamp not null,
     institution_type varchar(20),
     institution_id bigint
);

create unique index invitations_request_identifier_unique_idx on invitations (request_identifier);
create unique index invitations_email_entity_unique_idx on invitations (email, institution_type, institution_id);

alter table history_operations
    alter column type type varchar(50);

drop type history_operation_type;