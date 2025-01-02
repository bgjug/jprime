call RemoveForeignKeyIfExists('Article', 'FKq5rn5c7ebu09xymsu67rfwajr');
call RemoveForeignKeyIfExists('Article', 'FK_qlnlcf382ifhvj2iyrkcjs3e3');

call RemoveForeignKeyIfExists('ResetPasswordToken', 'FKf1cyquxpakjrbu7gam24ersla');
call RemoveForeignKeyIfExists('ResetPasswordToken', 'FK_eb9ovnn3wnw4b5s2xo10b8gdd');

call RemoveForeignKeyIfExists('Submission', 'FKd189ymi4jxqgyngkx5gn22fq2');
call RemoveForeignKeyIfExists('Submission', 'FKd5hka52chga52npqnkk7m98f3');
call RemoveForeignKeyIfExists('Submission', 'FK_7h10vns4otncwv7qtesrl2j44');
call RemoveForeignKeyIfExists('Submission', 'FK_shyp8uqmh5t9526u983e3y0i5');

call RemoveForeignKeyIfExists('Session', 'FK3johuxuxvqlr70ppsvj1rqgs0');
call RemoveForeignKeyIfExists('Session', 'FKf6lj7gyp4ih08ayvt9a2y7sfd');
call RemoveForeignKeyIfExists('Session', 'FK_ccg4a9421kvl1cdu0c0ufut6q');
call RemoveForeignKeyIfExists('Session', 'FK_lbsdck6wuoavhk28w7q6i4cf3');

call RemoveIndexIfExists('User', 'UK_e6gkqunxajvyxl5uctpl2vl2p');

call RemoveForeignKeyIfExists('Visitor', 'FK3g9u04q7hetbykqb4kjewayl6');
call RemoveForeignKeyIfExists('Visitor', 'FK_jubbesbawcgr888bv51k5ndni');

call RemoveForeignKeyIfExists('tags_articles', 'FK84246owy4yj4t2pjqi2rhiymo');
call RemoveForeignKeyIfExists('tags_articles', 'FK_6dglpfm30fc8dro1gu1gxxjf');
call RemoveForeignKeyIfExists('tags_articles', 'FK_cts2q6qb21eacyeuu4c9h0fl3');
call RemoveForeignKeyIfExists('tags_articles', 'FKopq97s4f99lb1ooep2if5vy7n');
