create table mapub_factorofinv (
id char(20) NOT NULL,
rowno varchar(50) NULL,
pk_factor varchar(20) default '~' NULL,
pk_allocfac varchar(20) default '~' NULL,
pk_group varchar(20) NOT NULL,
pk_org varchar(20) default '~' NULL,
pk_org_v varchar(20) default '~' NULL,
creator varchar(20) default '~' NULL,
creationtime char(19) NULL,
modifier varchar(20) default '~' NULL,
modifiedtime char(19) NULL,
def1 varchar(100) NULL,
def2 varchar(100) NULL,
def3 varchar(100) NULL,
def4 varchar(100) NULL,
def5 varchar(100) NULL,
def6 varchar(100) NULL,
def7 varchar(100) NULL,
def8 varchar(100) NULL,
def9 varchar(100) NULL,
def10 varchar(100) NULL,
def11 varchar(100) NULL,
def12 varchar(100) NULL,
def13 varchar(100) NULL,
def14 varchar(100) NULL,
def15 varchar(100) NULL,
def16 varchar(100) NULL,
def17 varchar(100) NULL,
def18 varchar(100) NULL,
def19 varchar(100) NULL,
def20 varchar(100) NULL,
CONSTRAINT PK_PUB_FACTOROFINV PRIMARY KEY (id),
ts char(19) NULL,
dr smallint default 0 
)


