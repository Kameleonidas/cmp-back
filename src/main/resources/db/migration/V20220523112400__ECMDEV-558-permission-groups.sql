create table if not exists permission_groups (
    id serial primary key,
    name varchar(255) not null,
    description VARCHAR(2000) not null
);

create table if not exists permission_group_institution_types(
    permission_group_id bigint not null references permission_groups(id),
    institution_type VARCHAR(64) not null
);

alter table permission_group_institution_types add constraint
permission_group_institution_types_unique unique(permission_group_id, institution_type);

create table if not exists permission_groups_permissions(
    permission_group_id bigint not null references permission_groups(id),
    permission_id bigint not null references permissions(id)
);

alter table permission_groups_permissions add constraint
permission_groups_permissions_unique unique(permission_group_id, permission_id);

create table if not exists user_account_to_subject_permission_group(
    user_account_to_subject_id bigint not null references user_account_to_subjects(id),
    permission_group_id bigint not null references permission_groups(id)
);

alter table user_account_to_subject_permission_group add constraint
user_account_to_subject_permission_group_unique unique(user_account_to_subject_id, permission_group_id);

alter table permissions drop column if exists subject_id;