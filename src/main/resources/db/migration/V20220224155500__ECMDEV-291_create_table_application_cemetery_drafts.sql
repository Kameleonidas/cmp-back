create table application_cemetery_drafts (
   id bigserial primary key,
   create_date timestamp not null,
   update_date timestamp,
   draft jsonb,
   user_account_id bigint
);

alter table application_cemetery_drafts add foreign key (user_account_id) references user_accounts (id);
