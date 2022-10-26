alter table application_cemeteries add column bound_cemetery_id bigint;
alter table application_cemeteries add foreign key (bound_cemetery_id) references cemeteries (id);
