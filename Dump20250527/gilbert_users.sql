-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: gilbert
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `gender` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
  `role` enum('ADMIN','NORMAL') NOT NULL DEFAULT 'NORMAL',
  `password` varchar(255) NOT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_business` tinyint(1) NOT NULL DEFAULT '0',
  `rating` decimal(3,2) DEFAULT NULL,
  `is_verified` tinyint(1) NOT NULL DEFAULT '0',
  `username` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Thomas','Admin','24875755','thomas@gmail.com',NULL,'ADMIN','$2a$10$H3tA/azojJ3kd.j2X.iLIuKS6/hrUZM7K7VmFAsQ.kIuSUqOE.5oO','2025-05-12 12:59:16',0,NULL,0,'thomas4342'),(2,'Lars','Larsen','24875755','lars@gmail.com',NULL,'NORMAL','$2a$10$N1e4PrDGoqvLUE8UWbp/5u5e/UOnNAOCpNIIFSKGW.s007esQi3p6','2025-05-14 18:47:27',0,NULL,0,'lars4321'),(3,'Jesper','Jørgensen','24875755','jesper@gmail.com',NULL,'NORMAL','$2a$10$F8Vn34gMhYT.FIlJ7RrVJ.A1iLaHAv6Ia2tl1JYlei0bs5tRmXEfq','2025-05-15 21:11:24',0,NULL,0,'jesper1111'),(4,'Hedwig','Hedwigsen','24875755','hedwig@gmail.com',NULL,'NORMAL','$2a$10$ZiVYhdZzmwqDetrk3deXO.RCzMJtev9DrWGCua7.V8svtWqgDu19e','2025-05-21 22:36:01',0,NULL,0,'hedwig2426');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-05-27 13:09:58
