SET NAMES utf8;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for r_pan_user
-- ----------------------------
DROP TABLE IF EXISTS `r_pan_user`;
CREATE TABLE `r_pan_user`
(
    `id`          int(11)                         NOT NULL AUTO_INCREMENT COMMENT '主键',
    `username`    varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '用户名',
    `password`    varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '密码',
    `salt`        varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL DEFAULT '' COMMENT '随机盐值',
    `create_time` datetime(0)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
    `update_time` datetime(0)                     NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE,
    UNIQUE INDEX `unique_index_username` (`username`) USING BTREE COMMENT '用户名称唯一索引'
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  CHARACTER SET = utf8
  ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;