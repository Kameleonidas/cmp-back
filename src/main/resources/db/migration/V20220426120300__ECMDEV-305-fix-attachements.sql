alter table cemeteries drop column if exists cemetery_attachment_id;
alter table application_attachment add column cemetery_id bigint;
alter table application_attachment add foreign key (cemetery_id) references cemeteries (id);