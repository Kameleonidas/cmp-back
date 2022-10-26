alter table application_attachment add column if not exists file_name character varying(255);
alter table application_cemetery_attachment add column if not exists file_name character varying(255);