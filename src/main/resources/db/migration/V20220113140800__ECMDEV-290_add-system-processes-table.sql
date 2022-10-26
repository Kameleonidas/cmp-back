create table system_processes (
    id bigserial primary key,
    code varchar(50) not null,
    description varchar(200) not null,
    run_date timestamp,
    state varchar(50) not null
);

create unique index system_processes_code_unique_idx on system_processes (code);