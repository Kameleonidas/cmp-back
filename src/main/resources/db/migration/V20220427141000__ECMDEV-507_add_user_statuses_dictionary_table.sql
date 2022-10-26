CREATE SEQUENCE IF NOT EXISTS user_account_statuses_dictionary_id_seq
INCREMENT 1
START 1;

create table user_account_statuses_dictionary (
    id bigserial primary key,
    code varchar(50) not null,
    name varchar(100) not null
);

insert into user_account_statuses_dictionary (id, code, name) values (nextval('user_account_statuses_dictionary_id_seq'), 'NEW', 'Nowy');
insert into user_account_statuses_dictionary (id, code, name) values (nextval('user_account_statuses_dictionary_id_seq'), 'ACTIVE', 'Aktywny');
insert into user_account_statuses_dictionary (id, code, name) values (nextval('user_account_statuses_dictionary_id_seq'), 'LOCKED', 'Zablokowany');
insert into user_account_statuses_dictionary (id, code, name) values (nextval('user_account_statuses_dictionary_id_seq'), 'REJECTED', 'Odrzucony');
