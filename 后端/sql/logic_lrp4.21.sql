/*
 Navicat Premium Data Transfer

 Source Server         : 阿里云服务器
 Source Server Type    : MySQL
 Source Server Version : 80026
 Source Host           : 8.137.57.246:3306
 Source Schema         : logic_lrp

 Target Server Type    : MySQL
 Target Server Version : 80026
 File Encoding         : 65001

 Date: 21/04/2024 14:45:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for atomic
-- ----------------------------
DROP TABLE IF EXISTS `atomic`;
CREATE TABLE `atomic`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `word` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '原子语句',
  `tags` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '标签',
  `sentence_id` int UNSIGNED NULL DEFAULT NULL COMMENT '文章分类ID',
  `create_user` int UNSIGNED NOT NULL COMMENT '创建人ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_atomic_sentence`(`sentence_id` ASC) USING BTREE,
  CONSTRAINT `fk_atomic_sentence` FOREIGN KEY (`sentence_id`) REFERENCES `sentence` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 238 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for sentence
-- ----------------------------
DROP TABLE IF EXISTS `sentence`;
CREATE TABLE `sentence`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `sentence` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '字符串/语句',
  `sentence_type` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '分类名',
  `create_user` int UNSIGNED NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `state` varchar(3) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '失败' COMMENT '状态: 只能是[失败] 或者 [成功]',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_sentence_user`(`create_user` ASC) USING BTREE,
  CONSTRAINT `fk_sentence_user` FOREIGN KEY (`create_user`) REFERENCES `user` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 130 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` int UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `username` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户名',
  `password` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '密码',
  `nickname` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '昵称',
  `email` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '邮箱',
  `user_pic` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '头像',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
