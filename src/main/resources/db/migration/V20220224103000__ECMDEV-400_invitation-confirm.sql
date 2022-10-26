alter table invitations
    rename column modification_date to send_date;

alter table invitations
    alter column send_date drop not null;

alter table invitations
    add column confirm_date timestamp;