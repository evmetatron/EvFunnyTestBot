drop view if exists test_replace_view;

create view test_replace_view as
select *
from test
where type = 'REPLACE';