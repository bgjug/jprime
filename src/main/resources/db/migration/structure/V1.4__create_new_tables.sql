create table Branch
(
    label            varchar(16) not null,
    createdBy        varchar(255),
    createdDate      datetime(6),
    lastModifiedBy   varchar(255),
    lastModifiedDate datetime(6),
    agendaPublished  bit         not null,
    cfpCloseDate     datetime(6),
    cfpOpenDate      datetime(6),
    currentBranch    bit         not null,
    duration         decimal(21, 0),
    soldOut          bit         not null,
    soldOutPackages  varchar(255),
    startDate        datetime(6),
    branch_year      integer,
    primary key (label)
) engine=InnoDB;

create table TicketPrice
(
    id                 bigint not null auto_increment,
    created_by         varchar(255),
    created_date       datetime(6),
    last_modified_by   varchar(255),
    last_modified_date datetime(6),
    price              decimal(38, 2),
    ticketType         enum ('EARLY_BIRD','REGULAR','STUDENT'),
    validFrom          datetime(6),
    validUntil         datetime(6),
    branch             varchar(16),
    primary key (id)
) engine=InnoDB;

alter table Branch
    add constraint UNQ_YEAR unique (branch_year);

alter table TicketPrice
    add constraint fk_ticket_price_branch
        foreign key (branch)
            references Branch (label);