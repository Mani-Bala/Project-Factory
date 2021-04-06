/*
SQLyog Ultimate v12.4.1 (64 bit)
MySQL - 8.0.17 : Database - project_factory
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`project_factory` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `project_factory`;

/*Table structure for table `categories` */

DROP TABLE IF EXISTS `categories`;

CREATE TABLE `categories` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(200) NOT NULL,
  `DESCRIPTION` longtext,
  `IS_ACTIVE` tinyint(1) NOT NULL DEFAULT '1',
  `CREATED_BY` bigint(20) NOT NULL,
  `CREATED_ON` datetime NOT NULL,
  `UPDATED_BY` bigint(20) DEFAULT NULL,
  `UPDATED_ON` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `active` (`IS_ACTIVE`),
  KEY `CATEGORIES_FK1` (`CREATED_BY`),
  KEY `CATEGORIES_FK2` (`UPDATED_BY`),
  CONSTRAINT `CATEGORIES_FK1` FOREIGN KEY (`CREATED_BY`) REFERENCES `employees` (`ID`),
  CONSTRAINT `CATEGORIES_FK2` FOREIGN KEY (`UPDATED_BY`) REFERENCES `employees` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `categories` */

insert  into `categories`(`ID`,`NAME`,`DESCRIPTION`,`IS_ACTIVE`,`CREATED_BY`,`CREATED_ON`,`UPDATED_BY`,`UPDATED_ON`) values
(1,'Java','Java 11',1,1,'2020-11-27 16:15:47',NULL,NULL),
(2,'.NET','.net',1,1,'2020-11-27 16:16:12',NULL,NULL),
(3,'DevOps','Cloud technologies, CI/CD',1,1,'2020-11-28 02:30:34',NULL,NULL),
(4,'ASP.NET',NULL,1,1,'2020-11-28 02:31:27',NULL,NULL),
(5,'AWS','Web services',1,1,'2020-11-28 02:32:01',NULL,NULL),
(6,'HTML','Web page',1,1,'2020-11-28 02:32:31',NULL,NULL),
(7,'C#',NULL,1,1,'2020-11-29 02:32:49',NULL,NULL),
(8,'Spring Cloud','Spring Framework',1,1,'2020-11-29 02:33:26',NULL,NULL);

/*Table structure for table `employees` */

DROP TABLE IF EXISTS `employees`;

CREATE TABLE `employees` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `FIRST_NAME` varchar(200) NOT NULL,
  `LAST_NAME` varchar(200) NOT NULL,
  `EMAIL_ID` varchar(200) NOT NULL,
  `PASSWORD_ENCRYPT` longblob,
  `PASSWORD_SALT` longblob,
  `IS_ACTIVE` tinyint(1) NOT NULL DEFAULT '1',
  `CREATED_BY` bigint(20) NOT NULL,
  `CREATED_ON` datetime NOT NULL,
  `UPDATED_BY` bigint(20) DEFAULT NULL,
  `UPDATED_ON` datetime DEFAULT NULL,
  `PASSWORD_RESET_REQUEST` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ID`),
  UNIQUE KEY `EMPLOYEES_UK_EMAIL_ID` (`EMAIL_ID`),
  KEY `active` (`IS_ACTIVE`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `employees` */

insert  into `employees`(`ID`,`FIRST_NAME`,`LAST_NAME`,`EMAIL_ID`,`PASSWORD_ENCRYPT`,`PASSWORD_SALT`,`IS_ACTIVE`,`CREATED_BY`,`CREATED_ON`,`UPDATED_BY`,`UPDATED_ON`) values
(1,'Admin','User','user@yopmail.com','N¿Öœ2xò]èj9¢Žu`\n §HV','T+3sòá',1,1,'2020-11-25 12:47:05',NULL,NULL);


/*Table structure for table `project_skills` */

DROP TABLE IF EXISTS `project_skills`;

CREATE TABLE `project_skills` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `PROJECT_ID` bigint(20) NOT NULL,
  `SKILL_ID` bigint(20) DEFAULT NULL,
  `SCORE` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `PRO_SKILLS_FK1` (`PROJECT_ID`),
  KEY `PRO_SKILLS_FK2` (`SKILL_ID`),
  CONSTRAINT `PRO_SKILLS_FK1` FOREIGN KEY (`PROJECT_ID`) REFERENCES `projects` (`ID`),
  CONSTRAINT `PRO_SKILLS_FK2` FOREIGN KEY (`SKILL_ID`) REFERENCES `skills` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `project_skills` */

insert  into `project_skills`(`ID`,`PROJECT_ID`,`SKILL_ID`,`SCORE`) values
(3,6,1,5),
(4,6,5,10),
(5,6,6,20),
(6,7,7,15),
(7,7,3,5),
(8,8,5,10),
(9,8,2,4),
(10,8,11,8);

/*Table structure for table `projects` */

DROP TABLE IF EXISTS `projects`;

CREATE TABLE `projects` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(200) NOT NULL,
  `DESCRIPTION` longtext,
  `CATEGORY_ID` bigint(20) NOT NULL,
  `IS_ACTIVE` tinyint(1) NOT NULL DEFAULT '1',
  `CREATED_BY` bigint(20) NOT NULL,
  `CREATED_ON` datetime NOT NULL,
  `UPDATED_BY` bigint(20) DEFAULT NULL,
  `UPDATED_ON` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `active` (`IS_ACTIVE`),
  KEY `PROJECTS_FK1` (`CREATED_BY`),
  KEY `PROJECTS_FK2` (`UPDATED_BY`),
  KEY `PROJECTS_FK3` (`CATEGORY_ID`),
  CONSTRAINT `PROJECTS_FK1` FOREIGN KEY (`CREATED_BY`) REFERENCES `employees` (`ID`),
  CONSTRAINT `PROJECTS_FK2` FOREIGN KEY (`UPDATED_BY`) REFERENCES `employees` (`ID`),
  CONSTRAINT `PROJECTS_FK3` FOREIGN KEY (`CATEGORY_ID`) REFERENCES `categories` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `projects` */

insert  into `projects`(`ID`,`NAME`,`DESCRIPTION`,`CATEGORY_ID`,`IS_ACTIVE`,`CREATED_BY`,`CREATED_ON`,`UPDATED_BY`,`UPDATED_ON`) values
(6,'Grading app','Standalone application',1,1,1,'2020-11-28 21:45:51',NULL,NULL),
(7,'Charity','Helping hand',8,1,1,'2020-11-28 21:53:59',NULL,NULL),
(8,'Pubhub','pubhub model',5,1,1,'2020-11-28 21:54:47',NULL,NULL);

/*Table structure for table `skills` */

DROP TABLE IF EXISTS `skills`;

CREATE TABLE `skills` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(200) NOT NULL,
  `SCORE` int(11) NOT NULL,
  `IS_ACTIVE` tinyint(1) NOT NULL DEFAULT '1',
  `CREATED_BY` bigint(20) NOT NULL,
  `CREATED_ON` datetime NOT NULL,
  `UPDATED_BY` bigint(20) DEFAULT NULL,
  `UPDATED_ON` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `active` (`IS_ACTIVE`),
  KEY `SKILLS_FK1` (`CREATED_BY`),
  KEY `SKILLS_FK2` (`UPDATED_BY`),
  CONSTRAINT `SKILLS_FK1` FOREIGN KEY (`CREATED_BY`) REFERENCES `employees` (`ID`),
  CONSTRAINT `SKILLS_FK2` FOREIGN KEY (`UPDATED_BY`) REFERENCES `employees` (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `skills` */

insert  into `skills`(`ID`,`NAME`,`SCORE`,`IS_ACTIVE`,`CREATED_BY`,`CREATED_ON`,`UPDATED_BY`,`UPDATED_ON`) values
(1,'Core Java',5,1,1,'2020-11-27 16:17:09',NULL,NULL),
(2,'JDBC',4,1,1,'2020-11-27 16:17:27',NULL,NULL),
(3,'Hibernate',5,1,1,'2020-11-27 16:17:42',NULL,NULL),
(4,'Mysql',3,1,1,'2020-11-27 16:18:01',NULL,NULL),
(5,'JavaScript',10,1,1,'2020-11-28 02:25:09',NULL,NULL),
(6,'Microservices',20,1,1,'2020-11-28 02:27:22',NULL,NULL),
(7,'AngularJS',15,1,1,'2020-11-28 02:27:43',NULL,NULL),
(8,'Servlet',6,1,1,'2020-11-28 02:28:04',NULL,NULL),
(9,'Angular',20,1,1,'2020-11-28 02:28:39',NULL,NULL),
(10,'React',25,1,1,'2020-11-28 02:28:55',NULL,NULL),
(11,'Docker',8,1,1,'2020-11-29 02:29:28',NULL,NULL);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;