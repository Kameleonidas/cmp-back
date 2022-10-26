alter table application_addresses add column if not exists street_code varchar(250);

alter table application_cemetery_perpetual_users add column if not exists perpetual_church_regulated_by_law_id bigint;
alter table application_cemetery_perpetual_users add column if not exists perpetual_church_not_regulated_by_law_id bigint;
alter table application_cemetery_perpetual_users add column if not exists perpetual_churches_regulated_by_law_or_no varchar(250);
alter table application_cemetery_perpetual_users add column if not exists name_of_parish_perpetual_use varchar(250);

alter table application_cemetery_owners add column if not exists community_name_id bigint;

create table if not exists application_community_name (
    id bigserial,
    name character varying(255),
    source character varying(255),
    code_simc character varying(255),
    level_name character varying(255),
    constraint community_name_pkey primary key (id)
);

CREATE SEQUENCE IF NOT EXISTS application_community_name_id_seq
INCREMENT 1
START 1;

alter table application_cemetery_owners add foreign key (community_name_id) references application_community_name (id);

alter table application_cemetery_owners add column if not exists application_is_owner boolean;
alter table application_cemetery_owners add column if not exists user_instead_owner_or_perpetual_user character varying(255);
alter table application_cemetery_owners add column if not exists unit_without_legal_personality_name character varying(255);
alter table application_cemetery_owners add column if not exists name_association_local_government character varying(255);
alter table application_cemetery_owners add column if not exists church_not_regulated_by_law_id bigint;
alter table application_cemetery_owners add column if not exists name_of_parish character varying(255);
alter table application_cemetery_owners add column if not exists perpetual_owner_type_id bigint;
alter table application_cemetery_owners add column if not exists church_regulated_by_law_id bigint;
alter table application_cemetery_owners add column if not exists owner_company_name character varying(255);
alter table application_cemetery_owners add column if not exists name_local_government_unit_id bigint;

create table if not exists application_name_local_government_unit
(
    id bigserial,
    name character varying(255),
    source character varying(255),
    code_simc character varying(255),
    level_name character varying(255),
    constraint application_name_local_government_unit_pkey primary key (id)
);

CREATE SEQUENCE IF NOT EXISTS application_name_local_government_unit_id_seq
INCREMENT 1
START 1;

alter table application_cemetery_owners add foreign key (name_local_government_unit_id) references application_name_local_government_unit (id);

alter table application_cemetery_owners add column if not exists owner_community_name_id bigint;

create table if not exists application_owner_community_name
(
    id bigserial,
    name character varying(255),
    source character varying(255),
    code_simc character varying(255),
    level_name character varying(255),
    constraint application_owner_community_name_pkey primary key (id)
);

CREATE SEQUENCE IF NOT EXISTS application_owner_community_name_id_seq
INCREMENT 1
START 1;

alter table application_cemetery_owners add foreign key (owner_community_name_id) references application_owner_community_name (id);
alter table application_cemetery_owners add column if not exists work_email_institution_object_owner character varying(255);

alter table application_cemetery_managers add column if not exists manager_data_the_same_as_perpetual_user_or_object_owner boolean;
alter table application_cemetery_representatives add column if not exists name_representation_person_object_manager_legal_person character varying(255);
alter table application_cemetery_representatives add column if not exists surname_representation_person_object_manager_Legal_person character varying(255);
alter table application_cemetery_representatives add column if not exists email_representation_person_object_manager_Legal_person character varying(255);

alter table application_cemetery_user_admins add column if not exists admin_data_the_same_as_obj_manager_or_perp_user_or_obj_owner boolean;
