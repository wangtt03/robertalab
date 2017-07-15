create table USER (
  ID INTEGER not null AUTO_INCREMENT,
  ACCOUNT varchar(255) not null,
  PASSWORD  varchar(255) not null,
  EMAIL varchar(255),
  ROLE varchar(32) not null,
  CREATED timestamp not null,
  LAST_LOGIN timestamp not null,
  TAGS text, -- e.g. HERDER-GYMNASIUM KÖLN Q1 ED_SHEERAN
  USER_NAME varchar(255),
  YOUNGER_THAN_14 bool default false not null,
  ACTIVATED bool default false not null,
  primary key (ID)
);

create unique index accountIdx on USER(ACCOUNT);

create table LOST_PASSWORD (
    ID INTEGER not null AUTO_INCREMENT,
    USER_ID INTEGER not null,
    URL_POSTFIX varchar(255),
    CREATED timestamp not null,

    primary key (ID),
    foreign key (USER_ID) references USER(ID) ON DELETE CASCADE
);

create table PENDING_EMAIL_CONFIRMATIONS (
    ID INTEGER not null AUTO_INCREMENT,
    USER_ID INTEGER not null,
    URL_POSTFIX varchar(255),
    CREATED timestamp not null,

    primary key (ID),
    foreign key (USER_ID) references USER(ID) ON DELETE CASCADE
);

create table ROBOT (
  ID INTEGER not null AUTO_INCREMENT,
  NAME varchar(255) not null,
  CREATED timestamp not null,
  TAGS text,
  ICON_NUMBER integer not null,

  primary key (ID)
);

create unique index typeIdx on ROBOT(NAME);

create table PROGRAM (
  ID INTEGER not null AUTO_INCREMENT,
  NAME varchar(255) not null,
  OWNER_ID INTEGER not null,
  ROBOT_ID INTEGER not null,
  PROGRAM_TEXT text,
  CREATED timestamp not null,
  LAST_CHANGED timestamp not null,
  LAST_CHECKED timestamp,
  LAST_ERRORFREE timestamp,
  NUMBER_OF_BLOCKS INTEGER,
  TAGS text, -- e.g. CAR AUTONOMOUS COOL 3WHEELS
  ICON_NUMBER integer not null,

  primary key (ID),
  foreign key (OWNER_ID) references USER(ID) ON DELETE CASCADE,
  foreign key (ROBOT_ID) references ROBOT(ID)
);

create unique index progNameOwnerRobotIdx on PROGRAM(NAME, OWNER_ID, ROBOT_ID);

CREATE TABLE LESSON (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(255) NOT NULL,
  `DOCURL` VARCHAR(255) NULL,
  `LEVEL` VARCHAR(255) NULL,
  `THUMBNAIL` VARCHAR(255) NULL,
  `PRGURL` VARCHAR(255) NULL,
  `DEVICETYPE` VARCHAR(255) NULL,
  PRIMARY KEY (`ID`)
);


CREATE TABLE DEVICE (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `DEVICE_NAME` VARCHAR(255) NOT NULL,
  `TYPE` VARCHAR(255) NULL,
  `TOKEN` VARCHAR(255) NULL,
  `FIRMWARE_NAME` VARCHAR(255) NULL,
  `MENU_VERSION` VARCHAR(255) NULL,
  `BATTERY` VARCHAR(255) NULL,
  `FIRMWARE_VERSION` VARCHAR(255) NULL,
  `BRICK_NAME` VARCHAR(255) NULL,
  `MACADDR` VARCHAR(255) NULL,
  PRIMARY KEY (`ID`));

create table USER_PROGRAM (
  ID INTEGER not null AUTO_INCREMENT,
  USER_ID INTEGER not null,
  PROGRAM_ID INTEGER not null,
  RELATION varchar(32) not null, -- 1 READ access, 2 WRITE access, 4 DELETE right, (really? not yet used) 8 PROMOTE_READ right, 16 PROMOTE_WRITE right

  primary key (ID),
  foreign key (USER_ID) references USER(ID) ON DELETE CASCADE,
  foreign key (PROGRAM_ID) references PROGRAM(ID) ON DELETE CASCADE
);

create table TOOLBOX (
  ID INTEGER not null AUTO_INCREMENT,
  NAME varchar(255) not null,
  OWNER_ID INTEGER,
  ROBOT_ID INTEGER not null,
  TOOLBOX_TEXT text,
  CREATED timestamp not null,
  LAST_CHANGED timestamp not null,
  LAST_CHECKED timestamp,
  LAST_ERRORFREE timestamp,
  TAGS text, -- e.g. CAR AUTONOMOUS COOL 3WHEELS
  ICON_NUMBER integer not null,

  primary key (ID),
  foreign key (OWNER_ID) references USER(ID) ON DELETE CASCADE,
  foreign key (ROBOT_ID) references ROBOT(ID)
);

create unique index toolNameOwnerIdx on TOOLBOX(NAME, OWNER_ID, ROBOT_ID);

create table CONFIGURATION (
  ID INTEGER not null AUTO_INCREMENT,
  NAME varchar(255) not null,
  OWNER_ID INTEGER,
  ROBOT_ID INTEGER not null,
  CONFIGURATION_TEXT text,
  CREATED timestamp not null,
  LAST_CHANGED timestamp not null,
  LAST_CHECKED timestamp,
  LAST_ERRORFREE timestamp,
  TAGS text, -- e.g. CAR AUTONOMOUS COOL 3WHEELS
  ICON_NUMBER integer not null,

  primary key (ID),
  foreign key (OWNER_ID) references USER(ID) ON DELETE CASCADE,
  foreign key (ROBOT_ID) references ROBOT(ID)
);

CREATE TABLE USER_DEVICE_RELATION (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `ACCOUNT_NAME` VARCHAR(255) NOT NULL,
  `DEVICE_NAME` VARCHAR(255) NULL,
  PRIMARY KEY (`ID`));

insert into ROBOT
( NAME, CREATED, TAGS, ICON_NUMBER )
values('ev3',
now(),
 '', 0
 );
commit;

insert into USER
(ACCOUNT, PASSWORD, EMAIL, ROLE, CREATED, LAST_LOGIN, TAGS, USER_NAME)
values ('Roberta','f17a0084220e822e:313c4eda282166163f78cd0b13da3b66f5ed6a0e','','TEACHER',now() ,now() ,'','Roberta Roboter'
);
commit;

insert into USER
(ACCOUNT, PASSWORD, EMAIL, ROLE, CREATED, LAST_LOGIN, TAGS, USER_NAME)
values ('Gallery','f17a0084220e822e:313c4eda282166163f78cd0b13da3b66f5ed6a0e','','TEACHER',now() ,now() ,'','The Gallery'
);
commit;

INSERT INTO LESSON VALUES(1,'lesson1','../guide/lesson-1/index.html',NULL,'https://www.baidu.com/img/bd_logo1.png','../guide/lesson-1/index.html', 'ev3dev');
commit;
INSERT INTO LESSON VALUES(2,'lesson2','../guide/index.html',NULL,'https://www.baidu.com/img/bd_logo1.png','../guide/index.html', 'ev3dev');
commit;
INSERT INTO LESSON VALUES(3,'lesson3','../guide/index.html',NULL,'https://www.baidu.com/img/bd_logo1.png','../guide/index.html', 'ev3dev');
commit;
