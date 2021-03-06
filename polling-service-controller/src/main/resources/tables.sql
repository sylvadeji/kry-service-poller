CREATE TABLE `user_management` (
  `USER_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(100) NOT NULL,
  `USER_ROLE` varchar(50) NOT NULL,
  `DATE_CREATED` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `services` (
  `SERVICE_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SERVICE_URL` varchar(100) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `DATE_CREATED` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `LAST_DATE_MODIFIED` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `USER_ID` bigint(20) DEFAULT NULL,
  `STATUS` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`SERVICE_ID`),
  KEY `FK8moq8qlwg81wecumjl78jrus7` (`USER_ID`),
  CONSTRAINT `FK8moq8qlwg81wecumjl78jrus7` FOREIGN KEY (`USER_ID`) REFERENCES `user_management` (`USER_ID`),
  CONSTRAINT `services_ibfk_1` FOREIGN KEY (`USER_ID`) REFERENCES `user_management` (`USER_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `service_pool` (
  `POOL_ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `SERVICE_ID` bigint(20) NOT NULL,
  `POOL_RESULT` varchar(4) NOT NULL,
  `POOL_TIME` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`POOL_ID`),
  KEY `FKkst6p0fajwolsg0n9aic9yhap` (`SERVICE_ID`),
  CONSTRAINT `FKkst6p0fajwolsg0n9aic9yhap` FOREIGN KEY (`SERVICE_ID`) REFERENCES `services` (`SERVICE_ID`),
  CONSTRAINT `service_pool_ibfk_1` FOREIGN KEY (`SERVICE_ID`) REFERENCES `services` (`SERVICE_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
