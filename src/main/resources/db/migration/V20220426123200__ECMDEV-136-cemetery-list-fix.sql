alter table cemetery_attachment rename column application_id to cemetery_id;
alter table cemetery_attachment add column if not exists file_name character varying(255);