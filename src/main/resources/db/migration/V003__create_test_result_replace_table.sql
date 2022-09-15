create sequence if not exists test_result_replace_id_seq;

create table if not exists test_result_replace
(
    id             bigint not null default nextval('test_result_replace_id_seq'),
    test_id        bigint not null references test (id),
    gender         varchar default null,
    result         varchar not null,
    constraint pk_test_result_replace primary key (id)
);

alter sequence test_result_replace_id_seq
owned by test_result_replace.id;