-- MySQL dump 10.13  Distrib 5.7.19, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: r_pan
-- ------------------------------------------------------
-- Server version	5.7.19

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for r_pan_file
-- ----------------------------
DROP TABLE IF EXISTS `r_pan_file`;
CREATE TABLE `r_pan_file`
(
    `id`          int(11)                                                NOT NULL AUTO_INCREMENT COMMENT '主键',
    `file_id`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件记录ID',
    `parent_id`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '上级文件夹ID,顶级文件夹为\'TOP\'',
    `user_id`     int(11)                                                NOT NULL COMMENT '用户ID',
    `file_name`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件名',
    `real_path`   varchar(700) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件真实存放路径',
    `show_path`   varchar(700) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件展示路径',
    `file_size`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '文件大小',
    `type`        int(2)                                                 NOT NULL DEFAULT 0 COMMENT '文件类型（0 文件夹 1 文件 2 压缩文件 3 excel 4 word 5 pdf 6 txt 7 图片 8 音频 9 视频 10 ppt 11 源码文件）',
    `create_user` int(11)                                                NOT NULL COMMENT '创建人',
    `create_time` timestamp(0)                                           NOT NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_user` int(11)                                                NULL     DEFAULT NULL COMMENT '更新人',
    `update_time` timestamp(0)                                           NULL     DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `r_pan_file_ file_id_uindex` (`file_id`) USING BTREE
) ENGINE = InnoDB
  AUTO_INCREMENT = 432
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for r_pan_user
-- ----------------------------
DROP TABLE IF EXISTS `r_pan_user`;
CREATE TABLE `r_pan_user`
(
    `id`          int(11)                                                NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '用户名',
    `password`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '密码',
    `salt`        varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '随机盐值',
    `question`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '密保问题',
    `answer`      varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL DEFAULT '' COMMENT '密保答案',
    `create_time` datetime(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                                            NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_index_username` (`username`) USING BTREE COMMENT '用户名称唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 58
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_bin
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
