alter table user_accounts
    alter email drop not null,
    alter role_id drop not null;

alter table user_accounts rename column name to first_name;
alter table user_accounts rename column surname to last_name;
