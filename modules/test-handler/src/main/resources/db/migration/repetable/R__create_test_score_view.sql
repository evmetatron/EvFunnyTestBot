drop view if exists test_score_view;

create view test_score_view as
select *
from test
where type = 'SCORE';