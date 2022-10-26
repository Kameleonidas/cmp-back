ALTER TABLE cemetery_owner_categories_dictionary ADD COLUMN IF NOT EXISTS perpetual_use boolean;

delete from cemetery_owner_categories_dictionary where code = 'CHURCH';
delete from cemetery_owner_categories_dictionary where code = 'RELIGIOUS_UNION';

ALTER TABLE cemetery_owner_categories_dictionary ADD COLUMN IF NOT EXISTS parent_id bigint;

insert into cemetery_owner_categories_dictionary (id, code, name, perpetual_use, parent_id) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'NATIONAL_FORESTS', 'Lasy Państwowe', false, 5);
insert into cemetery_owner_categories_dictionary (id, code, name, perpetual_use, parent_id) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'NATIONAL_AGRICULTURAL_SUPPORT_CENTER', 'Krajowy Ośrodek Wsparcia Rolnictwa', false, 5);
insert into cemetery_owner_categories_dictionary (id, code, name, perpetual_use, parent_id) values (nextval('cemetery_owner_categories_dictionary_id_seq'), 'POLISH_WATER', 'Wody Polskie', false, 5);

UPDATE cemetery_owner_categories_dictionary set perpetual_use = false;
UPDATE cemetery_owner_categories_dictionary set perpetual_use = true where code = 'LOCAL_GOVERNMENT_UNIT';
UPDATE cemetery_owner_categories_dictionary set perpetual_use = true where code = 'UNION_OF_LOCAL_GOVERNMENT_UNIT';
UPDATE cemetery_owner_categories_dictionary set perpetual_use = true where code = 'STATE_TREASURY';





