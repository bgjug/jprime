-- Migration script
-- FROM
-- commit 615e0412773657e6a8690e37e33a055c296d1ec4
-- Author: Martin <marto8808@gmail.com>
-- Date:   Wed Mar 28 00:50:31 2018 +0300
-- TO
-- commit 9e18c667aede81dddc040fd0231f724861b90a82
-- Author: Nayden Gochev <gochev@gmail.com>
-- Date:   Fri Apr 20 18:20:50 2018 +0300
select a.id, a.title
from Article a
inner join tags_articles ta on ta.article_pk=a.id
inner join Tag t on t.id=ta.tag_pk
where t.name in ('Speakers', 'Agenda');
-- The triple delete fails on a weird-double-constraint. Dirty fix that works: (don't do this at home, kids)
SET FOREIGN_KEY_CHECKS=0;
delete ta, a, t
from Tag t
inner join tags_articles ta on t.id=ta.tag_pk
inner join Article a on ta.article_pk=a.id
where t.name in ('Speakers', 'Agenda');
SET FOREIGN_KEY_CHECKS=1;

-- Migration script for 2018 - delete visitors and registrants from 2017
SET FOREIGN_KEY_CHECKS=0;
delete r, v
from Registrant r
left outer join Visitor v on v.registrant=r.id
where r.branch='YEAR_2017';
SET FOREIGN_KEY_CHECKS=1;
