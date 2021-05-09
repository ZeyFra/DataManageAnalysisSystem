/*
 Navicat Premium Data Transfer

 Source Server         : ZeyFra
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : dmas

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 06/05/2021 20:54:16
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for award_record
-- ----------------------------
DROP TABLE IF EXISTS `award_record`;
CREATE TABLE `award_record`  (
  `award_id` int(8) UNSIGNED NOT NULL AUTO_INCREMENT,
  `organizational_unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `award_year` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `award_time` datetime NULL DEFAULT NULL,
  `award_type` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `award_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `award_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `certificate_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `the_school_recognizes_the_award_level` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `awarding_unit` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`award_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 162 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for award_record_member
-- ----------------------------
DROP TABLE IF EXISTS `award_record_member`;
CREATE TABLE `award_record_member`  (
  `record_id` int(8) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `member_id` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `award_id` int(8) UNSIGNED NOT NULL,
  PRIMARY KEY (`record_id`) USING BTREE,
  INDEX `member_id`(`member_id`) USING BTREE,
  INDEX `award_id`(`award_id`) USING BTREE,
  CONSTRAINT `award_record_member_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `award_record_member_ibfk_2` FOREIGN KEY (`award_id`) REFERENCES `award_record` (`award_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 190 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for contest
-- ----------------------------
DROP TABLE IF EXISTS `contest`;
CREATE TABLE `contest`  (
  `contest_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '比赛编号',
  `contest_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '比赛名称',
  `contest_time` date NOT NULL COMMENT '比赛日期',
  `contest_site` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '比赛地点',
  `contest_team_num` int(11) NOT NULL COMMENT '比赛队伍数量',
  `contest_ak_num` int(11) NOT NULL COMMENT '比赛AK队伍个数',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建数据',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`contest_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '比赛表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for contest_member
-- ----------------------------
DROP TABLE IF EXISTS `contest_member`;
CREATE TABLE `contest_member`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
  `contest_id` int(10) UNSIGNED NOT NULL COMMENT '比赛编号',
  `member_id` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户编号',
  `rank` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'rank',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_name`(`member_id`) USING BTREE,
  INDEX `contest_user_ibfk_1`(`contest_id`) USING BTREE,
  CONSTRAINT `contest_member_ibfk_1` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `contest_member_ibfk_2` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '比赛队员关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for contest_problem
-- ----------------------------
DROP TABLE IF EXISTS `contest_problem`;
CREATE TABLE `contest_problem`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
  `contest_id` int(10) UNSIGNED NOT NULL COMMENT '比赛编号',
  `problem_id` int(10) UNSIGNED NOT NULL COMMENT '题目编号',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `contest_id`(`contest_id`) USING BTREE,
  INDEX `problem_id`(`problem_id`) USING BTREE,
  CONSTRAINT `contest_problem_ibfk_1` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`problem_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `contest_problem_ibfk_2` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '比赛题目关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `member_id` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '队员id(学号)',
  `member_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '队员姓名',
  `gender` tinyint(1) UNSIGNED NOT NULL COMMENT '性别',
  `telephone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '电话号码',
  `qq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'QQ号',
  `is_retire` tinyint(1) NOT NULL DEFAULT 0 COMMENT '已退休',
  `now_coder_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '牛客网id',
  `created_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`member_id`) USING BTREE,
  INDEX `user_name`(`member_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for now_coder_record
-- ----------------------------
DROP TABLE IF EXISTS `now_coder_record`;
CREATE TABLE `now_coder_record`  (
  `now_coder_record_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT,
  `contest_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `contest_id` int(11) NULL DEFAULT NULL,
  `logo_url` varchar(2083) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `team_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `member_id` int(11) NULL DEFAULT NULL,
  `problem_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `accept_count` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `rank` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `user_count` int(11) NULL DEFAULT NULL,
  `start_time` datetime NULL DEFAULT NULL,
  `end_time` datetime NULL DEFAULT NULL,
  `contest_duration` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`now_coder_record_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3990 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for problem
-- ----------------------------
DROP TABLE IF EXISTS `problem`;
CREATE TABLE `problem`  (
  `problem_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '题目编号',
  `problem_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '题目名称',
  `problem_submit_num` int(11) NOT NULL COMMENT '题目的总提交次数',
  `problem_pass_num` int(11) NOT NULL COMMENT '题目的pass数',
  PRIMARY KEY (`problem_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '题目表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for problem_member
-- ----------------------------
DROP TABLE IF EXISTS `problem_member`;
CREATE TABLE `problem_member`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '编号',
  `problem_id` int(11) UNSIGNED NOT NULL COMMENT '题目编号',
  `member_id` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '队员编号(学号)',
  `result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '答题情况',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `problem_user_ibfk_1`(`problem_id`) USING BTREE,
  INDEX `problem_user_ibfk_2`(`member_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1669 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '题目队员关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `user_id` int(11) NOT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
