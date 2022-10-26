create table if not exists application_attachment
(
    id bigserial primary key,
    application_id bigint NOT NULL,
    file_hashed_name character varying(255) NOT NULL,
    size integer NOT NULL
);


create table if not exists application_cemetery_attachment
(
    id bigserial primary key,
    cemetery_id bigint,
    file_hashed_name character varying(255) NOT NULL,
    size integer NOT NULL
);

create table if not exists application_cemetery_image
(
    id bigserial primary key,
    cemetery_id bigint NOT NULL,
    file_hashed_name character varying(255) NOT NULL,
    size integer NOT NULL
);
