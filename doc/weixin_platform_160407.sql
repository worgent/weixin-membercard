/*
Navicat MySQL Data Transfer

Source Server         : 192.168.1.113
Source Server Version : 50625
Source Host           : 192.168.1.113:3306
Source Database       : weixin_platform

Target Server Type    : MYSQL
Target Server Version : 50625
File Encoding         : 65001

Date: 2016-04-07 09:18:43
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '注册账户表',
  `account_name` varchar(255) DEFAULT NULL COMMENT '注册账户名称',
  `password` varchar(100) DEFAULT NULL COMMENT '注册账户密码',
  `phone` varchar(255) DEFAULT NULL COMMENT '注册账户手机号',
  `email` varchar(255) DEFAULT NULL COMMENT '邮箱',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=114 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `key_value`
-- ----------------------------
DROP TABLE IF EXISTS `key_value`;
CREATE TABLE `key_value` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key_str` varchar(255) DEFAULT NULL,
  `value_str` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `vip_business`
-- ----------------------------
DROP TABLE IF EXISTS `vip_business`;
CREATE TABLE `vip_business` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '商户ID',
  `account_id` int(11) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '商户名称',
  `tel` varchar(15) DEFAULT NULL COMMENT '电话',
  `city` varchar(255) DEFAULT NULL,
  `addr` varchar(100) DEFAULT NULL COMMENT '地址',
  `type` int(11) DEFAULT '0' COMMENT '0为未绑定公众号，1为已授权绑定公众号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `vip_member`
-- ----------------------------
DROP TABLE IF EXISTS `vip_member`;
CREATE TABLE `vip_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '会员信息ID',
  `open_id` varchar(255) DEFAULT NULL,
  `business_id` int(11) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL COMMENT '会员名称',
  `addr_province` varchar(255) DEFAULT NULL,
  `addr_city` varchar(255) DEFAULT NULL,
  `addr_area` varchar(255) DEFAULT NULL,
  `addr_detail` varchar(255) DEFAULT NULL COMMENT '会员地址',
  `tel` varchar(15) DEFAULT NULL COMMENT '会员电话',
  `account` varchar(100) DEFAULT NULL COMMENT '会员登陆账号',
  `pwd` varchar(100) DEFAULT '000000' COMMENT '登录密码',
  `type` int(11) DEFAULT NULL COMMENT '会员类型（普通会员，高级会员等',
  `sex` int(11) DEFAULT NULL COMMENT '性别1男 0女',
  `birthday` varchar(255) DEFAULT NULL COMMENT '出生日期',
  `money_input` int(11) DEFAULT '0',
  `money_output` int(11) DEFAULT '0',
  `money_lock` int(11) DEFAULT '0',
  `status` smallint(6) DEFAULT '1' COMMENT '会员状态1有效0无效',
  `create_time` datetime DEFAULT '2015-09-29 00:00:00' COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `vip_message`
-- ----------------------------
DROP TABLE IF EXISTS `vip_message`;
CREATE TABLE `vip_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '消息ID',
  `member_id` int(11) DEFAULT NULL COMMENT '会员ID',
  `title` varchar(100) DEFAULT NULL COMMENT '消息标题',
  `content` varchar(255) DEFAULT NULL COMMENT '消息内容',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `read_time` datetime DEFAULT NULL COMMENT '阅读时间',
  `type` int(11) DEFAULT '1' COMMENT '消息类型1公注册成功，2-充值消息，3-消费信息',
  `status` int(11) DEFAULT '0' COMMENT '1-删除',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_10` (`member_id`),
  CONSTRAINT `FK_Reference_10` FOREIGN KEY (`member_id`) REFERENCES `vip_member` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `vip_pay_order`
-- ----------------------------
DROP TABLE IF EXISTS `vip_pay_order`;
CREATE TABLE `vip_pay_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '支付订单ID',
  `member_id` int(11) DEFAULT NULL COMMENT '会员ID',
  `business_id` int(11) DEFAULT NULL COMMENT '商户ID',
  `out_trade_no` varchar(255) DEFAULT NULL,
  `pay_type` int(11) DEFAULT '1' COMMENT '支付方式1微支付2余额支付3现金付款(暂无)',
  `fee` int(11) DEFAULT NULL COMMENT '支付金额',
  `coupons_type` int(11) DEFAULT '1' COMMENT '优惠类型1无优惠2代金卷3优惠劵',
  `create_time` datetime DEFAULT NULL COMMENT '下单时间',
  `finish_time` datetime DEFAULT NULL COMMENT '订单结束时间',
  `status` smallint(6) DEFAULT '0' COMMENT '订单状态1已支付0未支付',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_5` (`member_id`),
  KEY `FK_Reference_6` (`business_id`),
  CONSTRAINT `FK_Reference_5` FOREIGN KEY (`member_id`) REFERENCES `vip_member` (`id`),
  CONSTRAINT `FK_Reference_6` FOREIGN KEY (`business_id`) REFERENCES `vip_business` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `vip_recharge_order`
-- ----------------------------
DROP TABLE IF EXISTS `vip_recharge_order`;
CREATE TABLE `vip_recharge_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '充值订单ID',
  `out_trade_no` varchar(255) DEFAULT NULL,
  `member_id` int(11) DEFAULT NULL COMMENT '会员ID',
  `business_id` int(11) DEFAULT NULL COMMENT '商户ID',
  `type` int(11) DEFAULT NULL COMMENT '充值方式1微支付（目前只有微支付',
  `create_time` datetime DEFAULT NULL COMMENT '下单时间',
  `finish_time` datetime DEFAULT NULL COMMENT '订单完成时间',
  `fee` int(11) DEFAULT NULL COMMENT '充值金额',
  `status` smallint(6) DEFAULT '0' COMMENT '订单状态0未支付,1-支付成功，2-支付失败',
  PRIMARY KEY (`id`),
  KEY `FK_Reference_11` (`member_id`),
  KEY `FK_Reference_12` (`business_id`),
  CONSTRAINT `FK_Reference_11` FOREIGN KEY (`member_id`) REFERENCES `vip_member` (`id`),
  CONSTRAINT `FK_Reference_12` FOREIGN KEY (`business_id`) REFERENCES `vip_business` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=97 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `weixin_gzh`
-- ----------------------------
DROP TABLE IF EXISTS `weixin_gzh`;
CREATE TABLE `weixin_gzh` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `business_id` int(11) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `alias` varchar(255) DEFAULT NULL,
  `origin_id` varchar(255) DEFAULT NULL,
  `head_img` varchar(255) DEFAULT NULL,
  `qrcode_url` varchar(255) DEFAULT NULL,
  `service_type_info` int(11) DEFAULT NULL,
  `verify_type_info` int(11) DEFAULT NULL,
  `func_info` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `weixin_gzh_authorizer`
-- ----------------------------
DROP TABLE IF EXISTS `weixin_gzh_authorizer`;
CREATE TABLE `weixin_gzh_authorizer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `weixin_gzh_id` int(11) DEFAULT NULL,
  `app_id` varchar(255) DEFAULT NULL,
  `app_secret` varchar(255) DEFAULT NULL,
  `authorization_code` varchar(255) DEFAULT NULL,
  `code_expire_time` datetime DEFAULT NULL,
  `authorizer_refresh_token` varchar(255) DEFAULT NULL,
  `authorizer_access_token` varchar(255) DEFAULT NULL,
  `token_expire_time` datetime DEFAULT NULL,
  `jsapi_ticket` varchar(255) DEFAULT NULL,
  `ticket_expire_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT NULL COMMENT '0-授权，1-取消授权',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `weixin_gzh_pay`
-- ----------------------------
DROP TABLE IF EXISTS `weixin_gzh_pay`;
CREATE TABLE `weixin_gzh_pay` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `weixin_gzh_id` int(11) DEFAULT NULL,
  `mch_id` varchar(255) DEFAULT NULL,
  `mch_key` varchar(255) DEFAULT NULL,
  `notify_url` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for `weixin_user`
-- ----------------------------
DROP TABLE IF EXISTS `weixin_user`;
CREATE TABLE `weixin_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `weixin_gzh_id` int(11) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `nickname` varchar(255) DEFAULT NULL,
  `sex` int(11) DEFAULT NULL,
  `province` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `head_img_url` varchar(255) DEFAULT NULL,
  `privilege` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weixin_user
-- ----------------------------

-- ----------------------------
-- Table structure for `weixin_user_authorizer`
-- ----------------------------
DROP TABLE IF EXISTS `weixin_user_authorizer`;
CREATE TABLE `weixin_user_authorizer` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `weixin_user_id` int(11) DEFAULT NULL,
  `open_id` varchar(255) DEFAULT NULL,
  `union_id` varchar(255) DEFAULT NULL,
  `code` varchar(255) DEFAULT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `access_token` varchar(255) DEFAULT NULL,
  `token_expire_time` datetime DEFAULT NULL,
  `refresh_token` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of weixin_user_authorizer
-- ----------------------------
