create sequence if not exists test_id_seq;

-- Телеграм не поддерживает строки в api кнопок, размер которых более 64 символов.
-- Поэтому id сделан как bigint, а не uuid

create table if not exists test
(
    id             bigint not null default nextval('test_id_seq'),
    name           varchar not null,
    description    text    not null,
    type           varchar not null,
    allow_gender   varchar not null,
    constraint pk_test primary key (id)
);

alter sequence test_id_seq
owned by test.id;