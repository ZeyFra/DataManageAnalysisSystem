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
  `contest_id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '????????????',
  `contest_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????????',
  `contest_time` date NOT NULL COMMENT '????????????',
  `contest_site` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????????',
  `contest_team_num` int(11) NOT NULL COMMENT '??????????????????',
  `contest_ak_num` int(11) NOT NULL COMMENT '??????AK????????????',
  `created_time` datetime NULL DEFAULT NULL COMMENT '????????????',
  `update_time` datetime NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`contest_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 14 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '?????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for contest_member
-- ----------------------------
DROP TABLE IF EXISTS `contest_member`;
CREATE TABLE `contest_member`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '??????',
  `contest_id` int(10) UNSIGNED NOT NULL COMMENT '????????????',
  `member_id` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????????',
  `rank` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'rank',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `user_name`(`member_id`) USING BTREE,
  INDEX `contest_user_ibfk_1`(`contest_id`) USING BTREE,
  CONSTRAINT `contest_member_ibfk_1` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `contest_member_ibfk_2` FOREIGN KEY (`member_id`) REFERENCES `member` (`member_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 200 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '?????????????????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for contest_problem
-- ----------------------------
DROP TABLE IF EXISTS `contest_problem`;
CREATE TABLE `contest_problem`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '??????',
  `contest_id` int(10) UNSIGNED NOT NULL COMMENT '????????????',
  `problem_id` int(10) UNSIGNED NOT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `contest_id`(`contest_id`) USING BTREE,
  INDEX `problem_id`(`problem_id`) USING BTREE,
  CONSTRAINT `contest_problem_ibfk_1` FOREIGN KEY (`problem_id`) REFERENCES `problem` (`problem_id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `contest_problem_ibfk_2` FOREIGN KEY (`contest_id`) REFERENCES `contest` (`contest_id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '?????????????????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for member
-- ----------------------------
DROP TABLE IF EXISTS `member`;
CREATE TABLE `member`  (
  `member_id` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '??????id(??????)',
  `member_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????????',
  `gender` tinyint(1) UNSIGNED NOT NULL COMMENT '??????',
  `telephone` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????????',
  `qq` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'QQ???',
  `is_retire` tinyint(1) NOT NULL DEFAULT 0 COMMENT '?????????',
  `now_coder_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '?????????id',
  `created_time` datetime NULL DEFAULT NULL COMMENT '????????????',
  `update_time` datetime NULL DEFAULT NULL COMMENT '????????????',
  PRIMARY KEY (`member_id`) USING BTREE,
  INDEX `user_name`(`member_name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '?????????' ROW_FORMAT = Dynamic;

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
  `problem_id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '????????????',
  `problem_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????????',
  `problem_submit_num` int(11) NOT NULL COMMENT '????????????????????????',
  `problem_pass_num` int(11) NOT NULL COMMENT '?????????pass???',
  PRIMARY KEY (`problem_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 136 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '?????????' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for problem_member
-- ----------------------------
DROP TABLE IF EXISTS `problem_member`;
CREATE TABLE `problem_member`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '??????',
  `problem_id` int(11) UNSIGNED NOT NULL COMMENT '????????????',
  `member_id` varchar(8) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????????(??????)',
  `result` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '????????????',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `problem_user_ibfk_1`(`problem_id`) USING BTREE,
  INDEX `problem_user_ibfk_2`(`member_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1669 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '?????????????????????' ROW_FORMAT = Dynamic;

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
