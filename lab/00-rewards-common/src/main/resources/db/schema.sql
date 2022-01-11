drop table T_ACCOUNT_BENEFICIARY if exists;
drop table T_ACCOUNT_CREDIT_CARD if exists;
drop table T_ACCOUNT if exists;
drop table T_ROLE if exists;
drop table T_ACCOUNT_ROLE if exists;
drop table T_RESTAURANT if exists;
drop table T_REWARD if exists;
drop sequence S_REWARD_CONFIRMATION_NUMBER if exists;
drop table DUAL_REWARD_CONFIRMATION_NUMBER if exists;

create table T_ACCOUNT (ID integer identity primary key, NUMBER varchar(9), NAME varchar(50) not null, USERNAME varchar(25) unique, PASSWORD varchar(100), LAST_LOGIN timestamp, unique(NUMBER));
create table T_ROLE (NAME varchar(50) primary key);
create table T_ACCOUNT_ROLE (ACCOUNT_ID integer, ROLE_NAME varchar(50));
create table T_ACCOUNT_CREDIT_CARD (ID integer identity primary key, ACCOUNT_ID integer, NUMBER varchar(16), unique(ACCOUNT_ID, NUMBER));
create table T_ACCOUNT_BENEFICIARY (ID integer identity primary key, ACCOUNT_ID integer, NAME varchar(50), ALLOCATION_PERCENTAGE decimal(3,2) not null, SAVINGS decimal(8,2) not null, unique(ACCOUNT_ID, NAME));
create table T_RESTAURANT (ID integer identity primary key, MERCHANT_NUMBER varchar(10) not null, NAME varchar(80) not null, BENEFIT_PERCENTAGE decimal(3,2) not null, LOCATION varchar(50), BENEFIT_AVAILABILITY_POLICY varchar(1) not null, OWNER_ID integer, CREATED_BY_ID integer, CREATED_DATE timestamp, LAST_MODIFIED_BY_ID integer, LAST_MODIFIED_DATE timestamp, unique(MERCHANT_NUMBER));
create table T_REWARD (ID integer identity primary key, CONFIRMATION_NUMBER varchar(25) not null, REWARD_AMOUNT decimal(8,2) not null, REWARD_DATE date not null, ACCOUNT_NUMBER varchar(9) not null, DINING_AMOUNT decimal not null, DINING_MERCHANT_NUMBER varchar(10) not null, DINING_DATE date not null, unique(CONFIRMATION_NUMBER));

create sequence S_REWARD_CONFIRMATION_NUMBER start with 1;
create table DUAL_REWARD_CONFIRMATION_NUMBER (ZERO integer);
insert into DUAL_REWARD_CONFIRMATION_NUMBER values (0);
   
alter table T_ACCOUNT_ROLE add constraint FK_ACCOUNT_ROLE_ACCOUNT_ID foreign key (ACCOUNT_ID) references T_ACCOUNT(ID) on delete cascade;
alter table T_ACCOUNT_ROLE add constraint FK_ACCOUNT_ROLE_ROLE_NAME foreign key (ROLE_NAME) references T_ROLE(NAME) on delete cascade;
alter table T_ACCOUNT_CREDIT_CARD add constraint FK_ACCOUNT_CREDIT_CARD foreign key (ACCOUNT_ID) references T_ACCOUNT(ID) on delete cascade;
alter table T_ACCOUNT_BENEFICIARY add constraint FK_ACCOUNT_BENEFICIARY foreign key (ACCOUNT_ID) references T_ACCOUNT(ID) on delete cascade;