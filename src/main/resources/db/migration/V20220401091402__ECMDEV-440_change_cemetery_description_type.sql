alter table application_cemeteries alter column object_description type varchar (4000);
alter table application_cemeteries alter column object_description set not null;