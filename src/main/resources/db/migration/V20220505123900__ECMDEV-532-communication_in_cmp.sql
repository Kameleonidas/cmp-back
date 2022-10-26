create type message_status as enum ('NEW', 'SENT', 'CONFIRMED');

CREATE SEQUENCE IF NOT EXISTS messages_id_seq
INCREMENT 1
START 1;

create table messages (
    id bigserial primary key,
    template_name varchar(50),
    status message_status,
    create_date timestamp not null,
    send_date timestamp,
    confirm_date timestamp,
    email_sender_id bigint,
    user_account_to_subject_id bigint
);
CREATE SEQUENCE IF NOT EXISTS email_senders_id_seq
INCREMENT 1
START 1;

create table email_senders (
    id bigserial primary key,
    email_from varchar(50),
    email_to varchar(50)
);

alter table messages add foreign key (email_sender_id) references email_senders (id);

alter table invitations add column massage_id bigint;

alter table invitations add foreign key (massage_id) references messages (id);

alter table invitations drop column name;

alter table invitations drop column email;

alter table invitations drop column status;

alter table invitations drop column create_date;

alter table invitations drop column send_date;

alter table invitations drop column confirm_date;

CREATE SEQUENCE IF NOT EXISTS message_parameters_id_seq
INCREMENT 1
START 1;

create table message_parameters (
    id bigserial primary key,
    "key" varchar(100) not null,
    "value" varchar(200) not null,
    massage_id bigint
);

alter table message_parameters add foreign key (massage_id) references messages (id);
