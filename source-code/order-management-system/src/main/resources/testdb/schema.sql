drop table T_PRODUCT if exists;

create table T_PRODUCT (ID bigint identity primary key, 
                        NAME varchar(50) not null, PRODUCT_TYPE varchar(50) not null, PRICE decimal(8,2));

ALTER TABLE T_PRODUCT ALTER COLUMN PRICE SET DEFAULT 0.0;
ALTER TABLE T_PRODUCT ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE T_PRODUCT ADD CONSTRAINT UNIQUE_PRODUCT UNIQUE(NAME);

drop table T_TAX if exists;

create table T_TAX (ID bigint identity primary key, 
                        PRODUCT_TYPE varchar(50) not null, LOCATION varchar(50) not null, TAX_NAME varchar(50) not null, TAX_PERCENT decimal(8,2));

ALTER TABLE T_TAX ALTER COLUMN TAX_PERCENT SET DEFAULT 0.0;
ALTER TABLE T_TAX ALTER COLUMN ID RESTART WITH 1;
ALTER TABLE T_TAX ADD CONSTRAINT UNIQUE_PRODUCT_TYPE_LOC UNIQUE(PRODUCT_TYPE, LOCATION, TAX_NAME);

drop table T_ORDER if exists;

create table T_ORDER (ID bigint identity primary key, 
                        CREATED_DATE TIMESTAMP not null);

ALTER TABLE T_ORDER ALTER COLUMN ID RESTART WITH 1;                        
                        
drop table T_ORDER_DETAILS if exists;

create table T_ORDER_DETAILS (ORDER_ID bigint not null, 
                        PRODUCT_ID bigint not null, QUANTITY int not null, PRIMARY KEY(ORDER_ID, PRODUCT_ID));
                        
drop table T_BILLING if exists;

create table T_BILLING (ID bigint identity primary key, 
						ORDER_ID bigint not null, BILLING_ADDRESS varchar(50) not null, LOCATION varchar(50) not null,
                        CUSTOMER_NAME varchar(50) not null, EMAIL_ID varchar(50) not null, CREATED_DATE TIMESTAMP not null);

ALTER TABLE T_BILLING ALTER COLUMN ID RESTART WITH 1;
                        
                        
