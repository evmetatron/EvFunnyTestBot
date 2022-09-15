create sequence if not exists test_question_replace_id_seq;

create table if not exists test_question_replace
(
    id             bigint not null default nextval('test_question_replace_id_seq'),
    test_id        bigint not null references test (id),
    question       varchar not null,
    num            int not null,
    constraint pk_test_question_replace primary key (id)
);

alter sequence test_question_replace_id_seq
owned by test_question_replace.id;