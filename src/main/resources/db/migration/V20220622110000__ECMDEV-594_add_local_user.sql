alter table user_accounts add column if not exists local_password varchar(100);
alter table user_accounts add column if not exists local_id bigint;