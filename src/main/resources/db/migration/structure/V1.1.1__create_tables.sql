call ExecuteStatementIfTableDoesNotExists('User', 'CREATE TABLE User (
                      DTYPE              VarChar(31) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                      id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                      created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      created_date       DateTime,
                      last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      last_modified_date DateTime,
                      email              VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                      firstName          VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      lastName           VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      phone              VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      bio                VarChar(3000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      headline           VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      picture            LongBLOB,
                      twitter            VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      bsky               VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      companyName        VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      companyWebsite     VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      description        VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      logo               LongBLOB,
                      sponsorPackage     VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      featured           Bit(1),
                      branch             VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      password           VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                      active             Bit(1),
                      accepted           Bit(1),
                      PRIMARY KEY (
                                   id
                          )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('Article', 'CREATE TABLE Article (
                         id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                         created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         created_date       DateTime,
                         last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         last_modified_date DateTime,
                         description        VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         `text`             Text CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                         title              VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                         author             BigInt(20),
                         published          Bit(1) NOT NULL,
                         PRIMARY KEY (
                                      id
                             )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');


call ExecuteStatementIfTableDoesNotExists('BackgroundJob', 'CREATE TABLE BackgroundJob (
                               jobId       VarChar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                               completed   DateTime,
                               description VarChar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                               log         LongText CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                               status      VarChar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                               PRIMARY KEY (
                                            jobId
                                   )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('Branch', 'CREATE TABLE Branch (
                        label            VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                        createdBy        VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                        createdDate      DateTime,
                        lastModifiedBy   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                        lastModifiedDate DateTime,
                        agendaPublished  Bit(1) NOT NULL,
                        cfpCloseDate     DateTime,
                        cfpOpenDate      DateTime,
                        currentBranch    Bit(1) NOT NULL,
                        duration         Decimal(21, 0),
                        soldOut          Bit(1) NOT NULL,
                        soldOutPackages  VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                        startDate        DateTime,
                        branch_year      Integer(11),
                        PRIMARY KEY (
                                     label
                            )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('Partner', 'CREATE TABLE Partner (
                         id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                         created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         created_date       DateTime,
                         last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         last_modified_date DateTime,
                         companyName        VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         companyWebsite     VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         description        VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         logo               LongBLOB,
                         active             Bit(1),
                         partnerPackage     VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         PRIMARY KEY (
                                      id
                             )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('Registrant', 'CREATE TABLE Registrant (
                            id                            BigInt(20) NOT NULL AUTO_INCREMENT,
                            created_by                    VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            created_date                  DateTime,
                            last_modified_by              VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            last_modified_date            DateTime,
                            address                       VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            email                         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            epayInvoiceNumber             BigInt(20),
                            borikaAuthorizationCode       VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            date                          DateTime,
                            epayAnswer                    VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            isValid                       Bit(1),
                            status                        VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            transactionNumberIfPaidByCard VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            isCompany                     Bit(1) NOT NULL,
                            mol                           VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            `name`                        VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            realInvoiceNumber             BigInt(20),
                            vatNumber                     VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            eik                           VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            paymentType                   Integer(11),
                            proformaInvoiceNumber         BigInt(20),
                            branch                        VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            isStudent                     Bit(1) NOT NULL,
                            PRIMARY KEY (
                                         id
                                )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('ResetPasswordToken', 'CREATE TABLE ResetPasswordToken (
                                    id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                                    created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                                    created_date       DateTime,
                                    last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                                    last_modified_date DateTime,
                                    tokenId            VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                                    used               Bit(1) NOT NULL,
                                    userId             BigInt(20),
                                    PRIMARY KEY (
                                                 id
                                        )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('Tag', 'CREATE TABLE Tag (
                     id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                     created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                     created_date       DateTime,
                     last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                     last_modified_date DateTime,
                     `name`             VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                     PRIMARY KEY (
                                  id
                         )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('tags_articles', 'CREATE TABLE tags_articles (
                               article_pk BigInt(20) NOT NULL,
                               tag_pk     BigInt(20) NOT NULL
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');


call ExecuteStatementIfTableDoesNotExists('Submission', 'CREATE TABLE Submission (
                            id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                            created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            created_date       DateTime,
                            last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            last_modified_date DateTime,
                            description        VarChar(10000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            level              VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            status             VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            title              VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
                            speaker            BigInt(20) NOT NULL,
                            branch             VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            coSpeaker          BigInt(20),
                            `type`             VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                            featured           Bit(1),
                            PRIMARY KEY (
                                         id
                                )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('TicketPrice', 'CREATE TABLE TicketPrice (
                             id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                             created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             created_date       DateTime,
                             last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             last_modified_date DateTime,
                             price              Decimal(38, 2),
                             ticketType         Enum(''EARLY_BIRD'', ''REGULAR'', ''STUDENT'') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             validFrom          DateTime,
                             validUntil         DateTime,
                             branch             VarChar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             PRIMARY KEY (
                                          id
                                 )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('VenueHall', 'CREATE TABLE VenueHall (
                           id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                           created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           created_date       DateTime,
                           last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           last_modified_date DateTime,
                           description        VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           map                LongBLOB,
                           `name`             VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                           PRIMARY KEY (
                                        id
                               )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('Visitor', 'CREATE TABLE Visitor (
                         id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                         created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         created_date       DateTime,
                         last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         last_modified_date DateTime,
                         email              VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         `name`             VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         registrant         BigInt(20) NOT NULL,
                         company            VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         status             VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         isPresent          Bit(1) NOT NULL,
                         isRegistered       Bit(1) NOT NULL,
                         ticket             VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         PRIMARY KEY (
                                      id
                             )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('VisitorJPro', 'CREATE TABLE VisitorJPro (
                             id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                             created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             created_date       DateTime,
                             last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             last_modified_date DateTime,
                             company            VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             email              VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             isPresent          Bit(1) NOT NULL,
                             `name`             VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             status             VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                             PRIMARY KEY (
                                          id
                                 )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');

call ExecuteStatementIfTableDoesNotExists('Session', 'CREATE TABLE Session (
                         id                 BigInt(20) NOT NULL AUTO_INCREMENT,
                         created_by         VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         created_date       DateTime,
                         last_modified_by   VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         last_modified_date DateTime,
                         end_time           DateTime,
                         start_time         DateTime,
                         hall               BigInt(20),
                         submission         BigInt(20),
                         title              VarChar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci,
                         PRIMARY KEY (
                                      id
                             )
) DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci');
