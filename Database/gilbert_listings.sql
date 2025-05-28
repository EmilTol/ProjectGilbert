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
-- Table structure for table `listings`
--

DROP TABLE IF EXISTS `listings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `listings` (
  `listing_id` bigint NOT NULL AUTO_INCREMENT,
  `seller_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  `item_type` varchar(50) NOT NULL,
  `model` varchar(100) DEFAULT NULL,
  `brand` varchar(100) NOT NULL,
  `description` text NOT NULL,
  `conditions` enum('NEW','LIKE_NEW','GOOD','FAIR','POOR') NOT NULL,
  `materials` varchar(255) DEFAULT NULL,
  `price` decimal(10,2) NOT NULL,
  `max_discount_percent` decimal(5,2) DEFAULT NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` enum('PENDING','APPROVED','SOLD','CANCELLED','REMOVED') NOT NULL DEFAULT 'PENDING',
  `is_fair_trade` tinyint(1) NOT NULL DEFAULT '0',
  `is_validated` tinyint(1) NOT NULL DEFAULT '0',
  `color` varchar(50) DEFAULT NULL,
  `size_id` bigint DEFAULT NULL,
  `image_file_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`listing_id`),
  KEY `seller_id` (`seller_id`),
  KEY `category_id` (`category_id`),
  KEY `fk_size_id` (`size_id`),
  CONSTRAINT `fk_size_id` FOREIGN KEY (`size_id`) REFERENCES `sizes` (`size_id`) ON DELETE SET NULL,
  CONSTRAINT `listings_ibfk_1` FOREIGN KEY (`seller_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE,
  CONSTRAINT `listings_ibfk_2` FOREIGN KEY (`category_id`) REFERENCES `categories` (`category_id`) ON DELETE RESTRICT
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `listings`
--

LOCK TABLES `listings` WRITE;
/*!40000 ALTER TABLE `listings` DISABLE KEYS */;
INSERT INTO `listings` VALUES (10,1,1,'T-Shirt','Living','Haute','a very cool black shirt','GOOD','Cotton',400000.00,NULL,'2025-05-27 11:51:58','APPROVED',0,0,'Black',3,'af454c62-2028-4f9d-8a67-1059078e1541.jpg'),(11,1,1,'Handbag','Cheval','Ladies fashion','A pretty handbag, barely used.','NEW','Leather',10000.00,NULL,'2025-05-27 11:56:44','APPROVED',0,0,'Black',1,'fb5d3336-1765-4cd0-a701-857839dccb76.jpeg'),(12,2,2,'Pants','Long','Charles Tyrwhitt','Fancy pants, for fancy people.','LIKE_NEW','Cotton',12500.00,NULL,'2025-05-27 12:00:12','APPROVED',0,0,'Black',4,'4708d98d-84bf-4e79-a3bf-46c97243a95b.jpg'),(13,2,2,'Shoes','Chaussures','John Lobb','A fancy pair of shoes for all the fancy parties','LIKE_NEW','Leather',3000.00,NULL,'2025-05-27 12:03:54','APPROVED',0,0,'Brown',4,'7c71e543-034c-4325-81fe-4f797e2b7085.webp'),(14,4,2,'Cap','Nashville','47','A cool cap for a cool guy','POOR','Cotton',350.00,NULL,'2025-05-27 12:06:30','SOLD',0,0,'Black',1,'ab851594-f01c-4ccd-8592-02b94e4112d0.jpg'),(15,4,2,'Cap','Pizza','47','Who dosent love pizza?','GOOD','Cotton',500.00,NULL,'2025-05-27 12:08:58','APPROVED',0,0,'Green',1,'009a21b1-c9c7-4979-80ad-3edfa914e92c.jpg'),(16,4,2,'Cap',' Raton laveur','Goorin bros','A little personality never hurt anyone','NEW','Cotton',1000.00,NULL,'2025-05-27 12:10:27','APPROVED',0,0,'Black',1,'c933c347-5202-47e5-8983-ed8d25293ebc.webp'),(17,3,2,'Coat','Manteau','Samsø','A glorious coat for a handsome man','NEW','Wool',5600.00,NULL,'2025-05-27 12:15:38','APPROVED',0,0,'Grey',4,'81506c26-045c-4506-82d2-93021d328d0a.jpg'),(18,3,1,'High heels','Glamour','Gucci','My wife dont want these anymore... :(','FAIR','Plastic',30000.00,NULL,'2025-05-27 12:18:38','PENDING',0,0,'Black',NULL,'47f4dba5-3278-46b0-9a76-939247ec1be3.jpg'),(19,3,1,'Sweater','Swet','Homemade','My wife made this herself!','NEW','Wool',750.00,NULL,'2025-05-27 12:27:48','PENDING',0,0,'Blue',3,'2eb26917-33bc-4643-871a-aa6645f0c22f.webp'),(20,1,2,'T-shirt','Year Zero','Ghost band','My favorite shirt! :D','FAIR','Cotton',10000050.00,NULL,'2025-05-27 12:36:59','PENDING',0,0,'black',4,'019886a1-2490-4a81-801a-493c163875d2.webp');
/*!40000 ALTER TABLE `listings` ENABLE KEYS */;
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
