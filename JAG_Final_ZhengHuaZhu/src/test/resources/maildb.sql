DROP DATABASE IF EXISTS MAILDB;

CREATE DATABASE MAILDB;

USE MAILDB;


DROP TABLE IF EXISTS FOLDER;

CREATE TABLE FOLDER (
  FOLDERID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  NAME varchar(30) NOT NULL DEFAULT ''
) ENGINE=InnoDB;

DROP TABLE IF EXISTS MAIL;

CREATE TABLE MAIL (
  MAILID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  FROMFIELD varchar(50) NOT NULL DEFAULT '',
  SUBJECT varchar(100) NOT NULL DEFAULT '',
  TEXT MEDIUMTEXT,
  HTML MEDIUMTEXT,
  FOLDERID int NOT NULL DEFAULT 0,
  SENTTIME timestamp DEFAULT 0,
  RECEIVEDTIME timestamp DEFAULT 0,
  MAILSTATUS int NOT NULL DEFAULT 0,
  FOREIGN KEY (FOLDERID) REFERENCES FOLDER(FOLDERID) ON DELETE CASCADE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS RECIPIENT;

CREATE TABLE RECIPIENT (
  RECIPIENTID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  TOFIELD varchar(50) NOT NULL DEFAULT '',
  CC varchar(50),
  BCC varchar(50)
) ENGINE=InnoDB;

DROP TABLE IF EXISTS ATTACHMENT;

CREATE TABLE ATTACHMENT (
  ATTACHMENTID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  MAILID int NOT NULL,
  NAME varchar(100) NOT NULL,
  CID varchar(50),
  SIZE int DEFAULT 0,
  CONTENT MEDIUMBLOB,
  FOREIGN KEY (MAILID) REFERENCES MAIL (MAILID) ON DELETE CASCADE
) ENGINE=InnoDB;

DROP TABLE IF EXISTS MAIL_RECIPIENT;

CREATE TABLE MAIL_RECIPIENT (
  ID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
  MAILID int NOT NULL,
  RECIPIENTID int NOT NULL,
  FOREIGN KEY (MAILID) REFERENCES MAIL(MAILID) ON DELETE CASCADE,
  FOREIGN KEY (RECIPIENTID) REFERENCES RECIPIENT(RECIPIENTID) ON DELETE CASCADE
) ENGINE=InnoDB;

INSERT INTO FOLDER(NAME) values
("inbox"),
("sent");

INSERT INTO MAIL (FROMFIELD, SUBJECT, TEXT, HTML, FOLDERID, SENTTIME, RECEIVEDTIME, MAILSTATUS) values 
("zzh.517.00001@gmail.com","Bruce","Asthma",null,1,"2014-01-23 9:00:00","2014-02-21 11:00:00",0),
("zzh.517.00001@gmail.com","Barry","Kidney Stones",null,1,"2014-02-18 9:00:00","2014-02-21 18:00:00",1),
("zzh.517.00001@gmail.com","Clark","Tonsilitis","<html><body><h1>Here is my photograph embedded in this email.</h1><img src='cid:FreeFall.jpg'><img src='cid:WindsorKen180.jpg'><h2>I'm flying!</h2></body></html>",1,"2014-05-02 9:00:00","2014-05-07 03:00:00",0),
("zzh.517.00001@gmail.com","Tony","Appendicitis",null,2,"2014-07-14 9:00:00","2014-07-15 11:00:00",0),
("zzh.517.00001@gmail.com","Bruce","Gall Bladder Disease",null,2,"2014-11-09 9:00:00","2014-11-12 13:00:00",1);

INSERT INTO RECIPIENT(TOFIELD,CC,BCC) values
("zzh.517.00002@gmail.com", null, null),
("zzh.517.00002@gmail.com", "zzh.517.cctestacct@gmail.com", null),
("zzh.517.00002@gmail.com", "zzh.517.cctestacct@gmail.com", "zzh.517.bcctestacct1@gmail.com"),
("zzh.517.00003@gmail.com", "zzh.517.cctestacct2@gmail.com", "zzh.517.bcctestacct2@gmail.com"),
("zzh.517.00004@gmail.com", "zzh.517.cctestacct3@gmail.com", "zzh.517.bcctestacct3@gmail.com"),
("zzh.517.00005@gmail.com", "zzh.517.cctestacct21@gmail.com", "zzh.517.bcctestacct21@gmail.com"),
("zzh.517.00006@gmail.com", "zzh.517.cctestacct22@gmail.com", "zzh.517.bcctestacct22@gmail.com"),
("zzh.517.00007@gmail.com", "zzh.517.cctestacct23@gmail.com", "zzh.517.bcctestacct23@gmail.com"),
("zzh.517.00008@gmail.com", "zzh.517.cctestacct24@gmail.com", "zzh.517.bcctestacct24@gmail.com"),
("zzh.517.000021@gmail.com", "zzh.517.cctestacct25@gmail.com", "zzh.517.bcctestacct25@gmail.com"),
("zzh.517.000022@gmail.com", "zzh.517.cctestacct26@gmail.com", "zzh.517.bcctestacct26@gmail.com"),
("zzh.517.000023@gmail.com", "zzh.517.cctestacct27@gmail.com", "zzh.517.bcctestacct2@gmail.com");

INSERT INTO MAIL_RECIPIENT(MAILID,RECIPIENTID) VALUES
(1, 1),
(2, 2),
(3, 3),
(4, 4),
(4, 5),
(5, 6),
(5, 7),
(5, 8),
(5, 9),
(5, 10),
(5, 11),
(5, 12);

INSERT INTO ATTACHMENT(MAILID,NAME,CID,SIZE,CONTENT) values
(4, "FreeFall.jpg", null, 2000, 96060),
(4, "p1.jpg", null, 10000, 48630),
(5, "p1.jpg", "CID", 10000, 48630);