create type object_category as enum (
'CMP',
'CEMETERY',
'CREMATORIUM',
'VOIVODSHIP_OFFICE',
'IPN',
'KPRM'
);

alter table user_account_to_subjects add column if not exists category object_category;
