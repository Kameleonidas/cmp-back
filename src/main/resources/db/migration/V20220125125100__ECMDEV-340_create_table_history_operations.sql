create type history_operation_type as enum (
    'APPLICATION_CREATED',
    'APPLICATION_SENT_TO_BE_COMPLETED',
    'APPLICATION_COMPLETED',
    'APPLICATION_REJECTED',
    'APPLICATION_AUTO_REJECTED',
    'APPLICATION_APPROVED',
    'CEMETERY_REGISTERED',
    'CEMETERY_DATA_CHANGED',
    'CEMETERY_FILE_PUBLISHED',
    'CEMETERY_FILE_UNPUBLISHED',
    'CEMETERY_FILE_ATTACHED',
    'CEMETERY_FILE_DELETED',
    'GRAVE_PAID_UP_TO_DATE'
);

create table history_operations (
    id bigserial primary key,
    type history_operation_type not null,
    description varchar(300),
    operation_date timestamp not null,
    application_id bigint,
    cemetery_id bigint,
    grave_id bigint,
    user_account_id bigint,
    application_cemetery_staff_id bigint,
    cemetery_staff_id bigint
);

alter table history_operations add foreign key (application_id) references application_cemeteries (id);
alter table history_operations add foreign key (cemetery_id) references cemeteries (id);
alter table history_operations add foreign key (user_account_id) references user_accounts (id);
alter table history_operations add foreign key (application_cemetery_staff_id) references application_cemetery_staff (id);
alter table history_operations add foreign key (cemetery_staff_id) references cemetery_staff (id);
