alter table applications alter column create_date type timestamptz using create_date::timestamptz;

alter table applications alter column update_date type timestamptz using update_date::timestamptz;

alter table cemeteries alter column create_date type timestamptz using create_date::timestamptz;

alter table cemeteries add update_date timestamptz;

alter table application_cemetery_drafts alter column create_date type timestamptz using create_date::timestamptz;

alter table application_cemetery_drafts alter column update_date type timestamptz using update_date::timestamptz;
