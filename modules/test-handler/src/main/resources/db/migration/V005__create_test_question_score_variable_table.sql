create sequence if not exists test_question_score_variable_id_seq;

create table if not exists test_question_score_variable
(
    id          bigint  not null default nextval('test_question_score_variable_id_seq'),
    question_id bigint  not null references test_question_score (id),
    variable    varchar not null,
    is_true     bool    not null,
    constraint pk_test_question_score_variable primary key (id)
);

alter sequence test_question_score_variable_id_seq
    owned by test_question_score_variable.id;

create unique index up_test_question_score_variable_is_true_variable ON test_question_score_variable (is_true, question_id)
    where is_true is true;