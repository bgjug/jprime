Migration script
FROM
commit 615e0412773657e6a8690e37e33a055c296d1ec4
Author: Martin <marto8808@gmail.com>
Date:   Wed Mar 28 00:50:31 2018 +0300
TO
commit 9e18c667aede81dddc040fd0231f724861b90a82
Author: Nayden Gochev <gochev@gmail.com>
Date:   Fri Apr 20 18:20:50 2018 +0300

select a.id, a.title
from Article a
inner join tags_articles ta on ta.article_pk=a.id
inner join Tag t on t.id=ta.tag_pk
where t.name in ('Speakers', 'Agenda');

delete ta.*, a.*, t.*
from Article a
inner join tags_articles ta on ta.article_pk=a.id
inner join Tag t on t.id=ta.tag_pk where t.name in ('Speakers', 'Agenda');
