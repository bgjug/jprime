ALTER TABLE Article DROP FOREIGN KEY FKq5rn5c7ebu09xymsu67rfwajr;
ALTER TABLE Article DROP FOREIGN KEY FK_qlnlcf382ifhvj2iyrkcjs3e3;

ALTER TABLE ResetPasswordToken DROP FOREIGN KEY FKf1cyquxpakjrbu7gam24ersla;
ALTER TABLE ResetPasswordToken DROP FOREIGN KEY FK_eb9ovnn3wnw4b5s2xo10b8gdd;

ALTER TABLE Submission DROP FOREIGN KEY FKd189ymi4jxqgyngkx5gn22fq2;
ALTER TABLE Submission DROP FOREIGN KEY FKd5hka52chga52npqnkk7m98f3;
ALTER TABLE Submission DROP FOREIGN KEY FK_7h10vns4otncwv7qtesrl2j44;
ALTER TABLE Submission DROP FOREIGN KEY FK_shyp8uqmh5t9526u983e3y0i5;

ALTER TABLE Session DROP FOREIGN KEY FK3johuxuxvqlr70ppsvj1rqgs0;
ALTER TABLE Session DROP FOREIGN KEY FKf6lj7gyp4ih08ayvt9a2y7sfd;
ALTER TABLE Session DROP FOREIGN KEY FK_ccg4a9421kvl1cdu0c0ufut6q;
ALTER TABLE Session DROP FOREIGN KEY FK_lbsdck6wuoavhk28w7q6i4cf3;

ALTER TABLE User DROP KEY UK_e6gkqunxajvyxl5uctpl2vl2p;

ALTER TABLE Visitor DROP FOREIGN KEY FK3g9u04q7hetbykqb4kjewayl6;
ALTER TABLE Visitor DROP FOREIGN KEY FK_jubbesbawcgr888bv51k5ndni;

alter table tags_articles drop foreign key FK84246owy4yj4t2pjqi2rhiymo;
alter table tags_articles drop foreign key FK_6dglpfm30fc8dro1gu1gxxjf;
alter table tags_articles drop foreign key FK_cts2q6qb21eacyeuu4c9h0fl3;
alter table tags_articles drop foreign key FKopq97s4f99lb1ooep2if5vy7n;
