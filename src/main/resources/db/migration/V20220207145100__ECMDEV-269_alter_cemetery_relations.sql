drop table if exists application_cemetery_types_mapping cascade;
drop table if exists application_cemetery_religions_mapping cascade;
drop table if exists cemetery_types_mapping cascade;
drop table if exists cemetery_religions_mapping cascade;

alter table application_cemeteries add column type_id bigint;
alter table application_cemeteries add column religion_id bigint;
alter table application_cemeteries add column owner_category_id bigint;
alter table cemeteries add column type_id bigint;
alter table cemeteries add column religion_id bigint;
alter table cemeteries add column owner_category_id bigint;

alter table application_cemeteries add foreign key (type_id) references cemetery_types_dictionary (id);
alter table application_cemeteries add foreign key (religion_id) references churches_religions_dictionary (id);
alter table application_cemeteries add foreign key (owner_category_id) references cemetery_owner_categories_dictionary (id);
alter table cemeteries add foreign key (type_id) references cemetery_types_dictionary (id);
alter table cemeteries add foreign key (religion_id) references churches_religions_dictionary (id);
alter table cemeteries add foreign key (owner_category_id) references cemetery_owner_categories_dictionary (id);
