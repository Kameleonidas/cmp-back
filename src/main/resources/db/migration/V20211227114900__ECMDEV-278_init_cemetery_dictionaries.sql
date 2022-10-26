insert into cemetery_types_dictionary (id, code, name) values (nextval('cemetery_types_dictionary_id_seq'), 'COMMUNAL', 'komunalny');
insert into cemetery_types_dictionary (id, code, name) values (nextval('cemetery_types_dictionary_id_seq'), 'RELIGIOUS', 'wyznaniowy');
insert into cemetery_types_dictionary (id, code, name) values (nextval('cemetery_types_dictionary_id_seq'), 'WAR', 'wojenny');
insert into cemetery_types_dictionary (id, code, name) values (nextval('cemetery_types_dictionary_id_seq'), 'OTHER', 'inny');

insert into cemetery_facility_types_dictionary (id, code, name) values (nextval('cemetery_facility_types_dictionary_id_seq'), 'CEMETERY', 'cmentarz');
insert into cemetery_facility_types_dictionary (id, code, name) values (nextval('cemetery_facility_types_dictionary_id_seq'), 'RELIGIOUS_BUILDING', 'budynek kultu religijnego');
insert into cemetery_facility_types_dictionary (id, code, name) values (nextval('cemetery_facility_types_dictionary_id_seq'), 'BURIAL_PLACE', 'miejsce spoczynku');
insert into cemetery_facility_types_dictionary (id, code, name) values (nextval('cemetery_facility_types_dictionary_id_seq'), 'OTHER', 'inny budynek');
insert into cemetery_facility_types_dictionary (id, code, name) values (nextval('cemetery_facility_types_dictionary_id_seq'), 'BELONGING_TO_RELIGIOUS_BUILDING', 'teren przynależny do budynku kultu religijnego, jeżeli nie spełnia wymogów ustawowych dla cmentarza');

insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'ROMAN_CATHOLIC', 'rzymsko-katolicki');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'EVANGELICAL', 'ewangelicki');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'EVANGELICAL_AUGSBURG', 'ewangelicko-augsburski');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'EVANGELICAL_REFORMED', 'ewangelicko-reformowany');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'ORTHODOX', 'prawosławny');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'PROTESTANT', 'protestancki');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'JEWISH', 'żydowski');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'MARIAVITE', 'mariawicki');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'MUSLIM', 'muzułmański');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'KARAIM', 'karaimski');
insert into cemetery_religions_dictionary (id, code, name) values (nextval('cemetery_religions_dictionary_id_seq'), 'OTHER', 'inny');

insert into cemetery_sources_dictionary (id, code, name) values (nextval('cemetery_sources_dictionary_id_seq'), 'CMP', 'pełne');
insert into cemetery_sources_dictionary (id, code, name) values (nextval('cemetery_sources_dictionary_id_seq'), 'GUGIK', 'uproszczone');

insert into cemetery_staff_functions_dictionary (id, code, name) values (nextval('cemetery_staff_functions_dictionary_id_seq'), 'OWNER', 'właściciel cmentarza');
insert into cemetery_staff_functions_dictionary (id, code, name) values (nextval('cemetery_staff_functions_dictionary_id_seq'), 'OWNER_REPRESENTATIVE', 'reprezentant właściciela cmentarza');
insert into cemetery_staff_functions_dictionary (id, code, name) values (nextval('cemetery_staff_functions_dictionary_id_seq'), 'MANAGER', 'zarządca cmentarza');
insert into cemetery_staff_functions_dictionary (id, code, name) values (nextval('cemetery_staff_functions_dictionary_id_seq'), 'MANAGER_REPRESENTATIVE', 'reprezentant zarządcy cmentarza');
insert into cemetery_staff_functions_dictionary (id, code, name) values (nextval('cemetery_staff_functions_dictionary_id_seq'), 'APLICANT', 'osoba składająca wniosek');
