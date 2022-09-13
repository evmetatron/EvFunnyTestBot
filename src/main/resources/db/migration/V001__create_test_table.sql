create table if not exists test
(
    id             uuid    not null,
    name           varchar not null,
    description    text    not null,
    type           varchar not null,
    constraint pk_test primary key (id)
);