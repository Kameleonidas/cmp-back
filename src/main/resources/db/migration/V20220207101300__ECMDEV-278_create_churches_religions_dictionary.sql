create table churches_religions_dictionary (
    id bigserial primary key,
    code varchar(50) not null,
    name varchar(100) not null,
    regulated_by_law boolean default true not null
);

insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'CATHOLIC_CHURCH', 'Kościół Katolicki w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'AUTOCEPHALOUS_ORTHODOX_CHURCH', 'Polski Autokefaliczny Kościół Prawosławny');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'EVANGELICAL_AUGSBURG_CHURCH', 'Kościół Ewangelicko-Augsburski w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'EVANGELICAL_REFORMED_CHURCH', 'Kościół Ewangelicko-Reformowany w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'EVANGELICAL_METHODIST_CHURCH', 'Kościół Ewangelicko-Metodystyczny w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'BAPTIST_CHRISTIAN_CHURCH', 'Kościół Chrześcijan Baptystów w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'SEVENTH_DAY_ADVENTIST_CHURCH', 'Kościół Adwentystów Dnia Siódmego w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'POLISH_CATHOLIC_CHURCH', 'Kościół Polskokatolicki w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'JEWISH_RELIGIOUS_COMMUNITIES', 'Gminy wyznaniowe żydowskie tworzące Związek Gmin Wyznaniowych Żydowskich w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'CATHOLIC_MARIAVITE_CHURCH', 'Kościół Katolicki Mariawitów w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'OLD_CATHOLIC_MARIAVITE_CHURCH', 'Kościół Starokatolicki Mariawitów w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'PENTECOSTAL_CHURCH', 'Kościół Zielonoświątkowy w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'EASTERN_OLD_BELIEVERS_CHURCH', 'Wschodni Kościół Staroobrzędowy, nie posiadający hierarchii');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'MUSLIM_RELIGIOUS_UNION', 'Muzułmański Związek Religijny w Rzeczypospolitej Polskiej');
insert into churches_religions_dictionary (id, code, name) values (nextval('churches_religions_dictionary_id_seq'), 'KARAIM_RELIGIOUS_UNION', 'Karaimski Związek Religijny w Rzeczypospolitej Polskiej');
