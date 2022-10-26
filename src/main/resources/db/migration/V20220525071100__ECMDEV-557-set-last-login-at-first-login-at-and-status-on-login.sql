alter table user_accounts add column if not exists first_login_at timestamp with time zone;
alter table user_accounts add column if not exists last_login_at timestamp with time zone;