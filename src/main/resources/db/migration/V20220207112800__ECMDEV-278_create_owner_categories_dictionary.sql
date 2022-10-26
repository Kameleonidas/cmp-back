create table cemetery_owner_categories_dictionary (
    id bigserial primary key,
    code varchar(50) not null,
    name varchar(100) not null
);

insert into cemetery_owner_categories_dictionary (id, code, name) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'LOCAL_GOVERNMENT_UNIT', 'Jednostka samorządu terytorialnego');
insert into cemetery_owner_categories_dictionary (id, code, name) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'UNION_OF_LOCAL_GOVERNMENT_UNIT', 'Związek jednostek samorządu terytorialnego');
insert into cemetery_owner_categories_dictionary (id, code, name) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'CHURCH', 'Kościół');
insert into cemetery_owner_categories_dictionary (id, code, name) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'RELIGIOUS_UNION', 'Związek wyznaniowy');
insert into cemetery_owner_categories_dictionary (id, code, name) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'STATE_TREASURY', 'Skarb Państwa');
insert into cemetery_owner_categories_dictionary (id, code, name) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'LEGAL_PERSON', 'Osoba prawna');
insert into cemetery_owner_categories_dictionary (id, code, name) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'NATURAL_PERSON', 'Osoba fizyczna');
insert into cemetery_owner_categories_dictionary (id, code, name) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'OWNER_UNKNOWN', 'Właściciel nieustalony');
insert into cemetery_owner_categories_dictionary (id, code, name) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'UNIT_WITHOUT_LEGAL_PERSONALITY', 'Jednostka organizacyjna nieposiadająca osobowości prawnej');
