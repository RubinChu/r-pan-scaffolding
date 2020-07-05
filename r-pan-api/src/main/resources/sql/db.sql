-- MySQL dump 10.13  Distrib 5.7.19, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: r_pan
-- ------------------------------------------------------
-- Server version	5.7.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

--
-- Table structure for table `r_pan_file`
--

DROP TABLE IF EXISTS `r_pan_file`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `r_pan_file`
(
    `id`          int(11)                                                       NOT NULL AUTO_INCREMENT COMMENT '主键',
    `file_id`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件记录ID',
    `parent_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '上级文件夹ID,顶级文件夹为\'TOP\'',
    `user_id`     int(11)                                                       NOT NULL COMMENT '用户ID',
    `file_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名',
    `real_path`   varchar(700) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件真实存放路径',
    `show_path`   varchar(700) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件展示路径',
    `file_size`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件大小',
    `type`        int(2)                                                        NOT NULL COMMENT '文件类型（0 文件夹 1 文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件）',
    `create_user` int(11)                                                       NOT NULL COMMENT '创建人',
    `create_time` timestamp(0)                                                  NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_user` int(11)                                                       NULL     DEFAULT NULL COMMENT '更新人',
    `update_time` timestamp(0)                                                  NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `r_pan_file_ file_id_uindex` (`file_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = Dynamic;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `r_pan_file`
--

LOCK TABLES `r_pan_file` WRITE;
/*!40000 ALTER TABLE `r_pan_file`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `r_pan_file`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `r_pan_user`
--

DROP TABLE IF EXISTS `r_pan_user`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `r_pan_user`
(
    `id`          int(11)                                          NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '用户名',
    `password`    varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '密码',
    `salt`        varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '随机盐值',
    `question`    varchar(255)                                     NOT NULL DEFAULT '' COMMENT '密保问题',
    `answer`      varchar(255)                                     NOT NULL DEFAULT '' COMMENT '密保答案',
    `create_time` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` datetime                                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE KEY `unique_index_username` (`username`) USING BTREE COMMENT '用户名称唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8
  ROW_FORMAT = DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `r_pan_user`
--

LOCK TABLES `r_pan_user` WRITE;
/*!40000 ALTER TABLE `r_pan_user`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `r_pan_user`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2020-06-12 21:31:35
