/*
Navicat MySQL Data Transfer

Source Server         : mysql
Source Server Version : 50552
Source Host           : localhost:3306
Source Database       : tale

Target Server Type    : MYSQL
Target Server Version : 50552
File Encoding         : 65001

Date: 2017-11-17 11:48:49
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_attach`
-- ----------------------------
DROP TABLE IF EXISTS `t_attach`;
CREATE TABLE `t_attach` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `fname` varchar(100) NOT NULL DEFAULT '',
  `ftype` varchar(50) DEFAULT '',
  `fkey` varchar(100) NOT NULL DEFAULT '',
  `author_id` int(10) DEFAULT NULL,
  `created` int(10) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=28 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_attach
-- ----------------------------
INSERT INTO `t_attach` VALUES ('14', 'bg_3_2.gif', 'image', 'http://oyeve9iiu.bkt.clouddn.com/FtOr-mYJlWhNMV4p4jg2-swYrBFU', '1', '1509946968');
INSERT INTO `t_attach` VALUES ('2', 'baidu.html', 'file', '/upload/2017/10/vravfduebij9pqsaskncsluqk1.html', '1', '1509446071');
INSERT INTO `t_attach` VALUES ('15', 'msa_chuan.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/Fq8zdWgXMCa2UuLiyFmbwZIa_A0B', '1', '1509946996');
INSERT INTO `t_attach` VALUES ('4', 'baidu.html', 'file', '/upload/2017/11/r6qgt8fkpmgbtr5fs7au3f8vt5.html', '1', '1509699748');
INSERT INTO `t_attach` VALUES ('5', 'CoreServlet.java', 'file', '/upload/2017/11/7ltj5718mciulq3mov5g8f6368.java', '1', '1509699783');
INSERT INTO `t_attach` VALUES ('16', 'msa_chuan_1.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/FqKx338jZkGdgdDkhMNnEcHRHtPq', '1', '1509946997');
INSERT INTO `t_attach` VALUES ('17', 'msa_chuan_2.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/Fk76aw6bj6tV4DEhM4bC37o-BZr4', '1', '1509946997');
INSERT INTO `t_attach` VALUES ('27', 'Penguins.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/Ft976dxPRnGHeDrKaMfOmOTfIXLQ', '1', '1510887509');
INSERT INTO `t_attach` VALUES ('25', '9cfe3f5.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/Fj4BYkUMIkM2QQmTYjQbk-2qnv_y', '1', '1510102533');
INSERT INTO `t_attach` VALUES ('18', 'msa_chuan_3.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/FkOxdHiQdFdXjGCTDNrxcGnyOxxD', '1', '1509946999');
INSERT INTO `t_attach` VALUES ('19', 'msa_chuan_4.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/Flm5UQYf1R5tn9lZiTmcVQdtu9jZ', '1', '1509947001');
INSERT INTO `t_attach` VALUES ('20', 'msa_chuan_5.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/FhTXWMNOYANe5PhKByTlGCcRH6nD', '1', '1509947002');
INSERT INTO `t_attach` VALUES ('21', 'msa_chuan_6.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/Fs9CwvdU6A4HBHbCnCIHx45gSWzZ', '1', '1509947003');
INSERT INTO `t_attach` VALUES ('22', 'msa_chuan_7.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/Fkq__CycEQLsaZyxSxsyNIUKANaA', '1', '1509947004');
INSERT INTO `t_attach` VALUES ('23', 'msa_chuan_8.jpg', 'image', 'http://oyeve9iiu.bkt.clouddn.com/Fiw1hBy6oL_4JPgZI8NQiq15dqfQ', '1', '1509947006');

-- ----------------------------
-- Table structure for `t_comments`
-- ----------------------------
DROP TABLE IF EXISTS `t_comments`;
CREATE TABLE `t_comments` (
  `coid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'comment表主键',
  `cid` int(10) unsigned DEFAULT '0' COMMENT 'post表主键,关联字段',
  `created` int(10) unsigned DEFAULT '0' COMMENT '评论生成时的GMT unix时间戳',
  `author` varchar(200) DEFAULT NULL COMMENT '评论作者',
  `author_id` int(10) unsigned DEFAULT '0' COMMENT '评论所属用户id',
  `owner_id` int(10) unsigned DEFAULT '0' COMMENT '评论所属内容作者id',
  `mail` varchar(200) DEFAULT NULL COMMENT '评论者邮件',
  `url` varchar(200) DEFAULT NULL COMMENT '评论者网址',
  `ip` varchar(64) DEFAULT NULL COMMENT '评论者ip地址',
  `agent` varchar(200) DEFAULT NULL COMMENT '评论者客户端',
  `content` text COMMENT '评论内容',
  `type` varchar(16) DEFAULT 'comment' COMMENT '评论类型',
  `status` varchar(16) DEFAULT 'approved' COMMENT '评论状态',
  `parent` int(10) unsigned DEFAULT '0' COMMENT '父级评论',
  PRIMARY KEY (`coid`),
  KEY `cid` (`cid`),
  KEY `created` (`created`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='评论';

-- ----------------------------
-- Records of t_comments
-- ----------------------------
INSERT INTO `t_comments` VALUES ('1', '2', '1509503472', '尼古拉丁', '0', '1', '1903373681@qq.com', 'https://wxn.qq.com/cmsid/WXN2017110100196000', '0:0:0:0:0:0:0:1', null, '第一条评论', 'comment', 'approved', '0');
INSERT INTO `t_comments` VALUES ('2', '2', '1509503576', '尼古拉丁', '0', '1', '1903373681@qq.com', 'https://wxn.qq.com/cmsid/WXN2017110100196000', '0:0:0:0:0:0:0:1', null, '啊萨达但是', 'comment', 'approved', '0');
INSERT INTO `t_comments` VALUES ('3', '2', '1509503680', '可爱宝宝', '0', '1', '975104693@qq.com', 'http://www.baidu.com', '0:0:0:0:0:0:0:1', null, '你很不错啊', 'comment', 'approved', '2');
INSERT INTO `t_comments` VALUES ('4', '2', '1509503774', '阿萨德', '0', '1', '12839138@qq.com', 'http://zl190.tunnel.2bdata.com/WeixinAuthorizeServlet/wxcfigServlet?method=wxCfgEntrance', '0:0:0:0:0:0:0:1', null, '不错不错哦', 'comment', 'approved', '3');
INSERT INTO `t_comments` VALUES ('5', '2', '1510821142', 'ad', null, '1', 'ad@qq.com', 'http://www.weixueyuan.net', '10.10.0.150', null, 'http://www.weixueyuan.net', null, null, '2');
INSERT INTO `t_comments` VALUES ('6', '2', '1510821221', 'ad', null, '1', 'ad@qq.com', 'http://www.weixueyuan.net', '10.10.0.150', null, 'asdasdasda', null, null, '2');
INSERT INTO `t_comments` VALUES ('7', '2', '1510821380', 'adaaaaas', null, '1', 'ad@qq.com', 'http://www.weixueyuan.net', '10.10.0.150', null, 'asdadasdasdadsads', null, null, null);
INSERT INTO `t_comments` VALUES ('8', '2', '1510821544', 'adaaaaas', null, '1', 'ad@qq.com', 'http://www.weixueyuan.net', '10.10.0.150', null, 'asdasd', null, null, null);

-- ----------------------------
-- Table structure for `t_contents`
-- ----------------------------
DROP TABLE IF EXISTS `t_contents`;
CREATE TABLE `t_contents` (
  `cid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'post表主键',
  `title` varchar(200) DEFAULT NULL COMMENT '内容标题',
  `slug` varchar(200) DEFAULT NULL COMMENT '内容缩略名',
  `created` int(10) unsigned DEFAULT '0' COMMENT '内容生成时的GMT unix时间戳',
  `modified` int(10) unsigned DEFAULT '0' COMMENT '内容更改时的GMT unix时间戳',
  `content` text COMMENT '内容文字',
  `author_id` int(10) unsigned DEFAULT '0' COMMENT '内容所属用户id',
  `type` varchar(16) DEFAULT 'post' COMMENT '内容类别',
  `status` varchar(16) DEFAULT 'publish' COMMENT '内容状态',
  `tags` varchar(200) DEFAULT NULL COMMENT '标签列表',
  `categories` varchar(200) DEFAULT NULL COMMENT '分类列表',
  `hits` int(10) unsigned DEFAULT '0' COMMENT '点击次数',
  `comments_num` int(10) unsigned DEFAULT '0' COMMENT '内容所属评论数',
  `allow_comment` tinyint(1) DEFAULT '1' COMMENT '是否允许评论',
  `allow_ping` tinyint(1) DEFAULT '1' COMMENT '是否允许ping',
  `allow_feed` tinyint(1) DEFAULT '1' COMMENT '允许出现在聚合中',
  PRIMARY KEY (`cid`),
  UNIQUE KEY `slug` (`slug`),
  KEY `created` (`created`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='内容表';

-- ----------------------------
-- Records of t_contents
-- ----------------------------
INSERT INTO `t_contents` VALUES ('1', '关于', 'about', '1487853610', '1487872488', '### Hello World\r\n\r\n这是我的关于页面\r\n\r\n### 当然还有其他\r\n\r\n具体你来写点什么吧', '1', 'page', 'publish', null, null, '0', '0', '1', '1', '1');
INSERT INTO `t_contents` VALUES ('2', '第一篇文章', null, '1487861184', '1487872798', '## Hello  World.\r\n\r\n> 第一篇文章总得写点儿什么?...\r\n\r\n----------\r\n\r\n\r\n<!--more-->\r\n\r\n```java\r\npublic static void main(String[] args){\r\n    System.out.println(\"Hello Tale.\");\r\n}\r\n```', '1', 'post', 'publish', '', '默认分类', '40', '8', '1', '1', '1');

-- ----------------------------
-- Table structure for `t_logs`
-- ----------------------------
DROP TABLE IF EXISTS `t_logs`;
CREATE TABLE `t_logs` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `action` varchar(100) DEFAULT NULL COMMENT '产生的动作',
  `data` varchar(2000) DEFAULT NULL COMMENT '产生的数据',
  `author_id` int(10) DEFAULT NULL COMMENT '发生人id',
  `ip` varchar(20) DEFAULT NULL COMMENT '日志产生的ip',
  `created` int(10) DEFAULT NULL COMMENT '日志创建时间',
  PRIMARY KEY (`id`)
) ENGINE=MyISAM AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_logs
-- ----------------------------
INSERT INTO `t_logs` VALUES ('1', '初始化站点', null, '1', null, '1509428678');
INSERT INTO `t_logs` VALUES ('2', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509428692');
INSERT INTO `t_logs` VALUES ('3', '系统备份', null, '1', '0:0:0:0:0:0:0:1', '1509428714');
INSERT INTO `t_logs` VALUES ('4', '系统备份', null, '1', '0:0:0:0:0:0:0:1', '1509535011');
INSERT INTO `t_logs` VALUES ('5', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509683177');
INSERT INTO `t_logs` VALUES ('6', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509684374');
INSERT INTO `t_logs` VALUES ('7', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509685027');
INSERT INTO `t_logs` VALUES ('8', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509685128');
INSERT INTO `t_logs` VALUES ('9', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509685215');
INSERT INTO `t_logs` VALUES ('10', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509685345');
INSERT INTO `t_logs` VALUES ('11', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509685796');
INSERT INTO `t_logs` VALUES ('12', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509686034');
INSERT INTO `t_logs` VALUES ('13', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509686429');
INSERT INTO `t_logs` VALUES ('14', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509687717');
INSERT INTO `t_logs` VALUES ('15', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509687763');
INSERT INTO `t_logs` VALUES ('16', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509691258');
INSERT INTO `t_logs` VALUES ('17', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509691951');
INSERT INTO `t_logs` VALUES ('18', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509692063');
INSERT INTO `t_logs` VALUES ('19', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509697551');
INSERT INTO `t_logs` VALUES ('20', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509699660');
INSERT INTO `t_logs` VALUES ('21', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509699726');
INSERT INTO `t_logs` VALUES ('22', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509700745');
INSERT INTO `t_logs` VALUES ('23', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509932949');
INSERT INTO `t_logs` VALUES ('24', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509933142');
INSERT INTO `t_logs` VALUES ('25', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509933429');
INSERT INTO `t_logs` VALUES ('26', '删除文章', '/upload/2017/11/q8fuh5kqb6gu7rm9u4pldb1bqo.jpg', '1', '0:0:0:0:0:0:0:1', '1509933601');
INSERT INTO `t_logs` VALUES ('27', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509934053');
INSERT INTO `t_logs` VALUES ('28', '删除文章', '/upload/2017/11/uk54uqtrbmgu9r2kmc536aeqjr.jpg', '1', '10.10.0.196', '1509934150');
INSERT INTO `t_logs` VALUES ('29', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509934785');
INSERT INTO `t_logs` VALUES ('30', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509934882');
INSERT INTO `t_logs` VALUES ('31', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509935121');
INSERT INTO `t_logs` VALUES ('32', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509935532');
INSERT INTO `t_logs` VALUES ('33', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509935710');
INSERT INTO `t_logs` VALUES ('34', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509935831');
INSERT INTO `t_logs` VALUES ('35', '删除文章', '/upload/2017/11/4ah2hfop14jhhpl8i0r4s744og.jpg', '1', '10.10.0.196', '1509935875');
INSERT INTO `t_logs` VALUES ('36', '删除文章', '/upload/2017/11/7vj81m361mif5ocmrsn88ommpf.jpg', '1', '10.10.0.196', '1509935879');
INSERT INTO `t_logs` VALUES ('37', '删除文章', '/upload/2017/11/36fkclj1i0irvovccpjg5kk8oo.jpg', '1', '10.10.0.196', '1509935882');
INSERT INTO `t_logs` VALUES ('38', '删除文章', '/upload/2017/11/umh9m9qjmuimhopv8m9f6ep9m2.png', '1', '10.10.0.196', '1509935887');
INSERT INTO `t_logs` VALUES ('39', '删除文章', '/upload/2017/10/q60jr6jc0qj18rvqta66i2t3uf.png', '1', '10.10.0.196', '1509935896');
INSERT INTO `t_logs` VALUES ('40', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509946905');
INSERT INTO `t_logs` VALUES ('41', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509946929');
INSERT INTO `t_logs` VALUES ('42', '删除文章', '/upload/2017/11/e542556f-d78c-4942-8184-0b00b61e9639.jpg', '1', '10.10.0.196', '1509947059');
INSERT INTO `t_logs` VALUES ('43', '删除文章', '/upload/2017/11/c5ea44f6-c440-437d-929d-26ec0709a018.jpg', '1', '10.10.0.196', '1509947064');
INSERT INTO `t_logs` VALUES ('44', '删除文章', '/upload/2017/11/a7381ce9-a2e8-4a68-9eff-1dc623a1d4d6.jpg', '1', '10.10.0.196', '1509947068');
INSERT INTO `t_logs` VALUES ('45', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509947438');
INSERT INTO `t_logs` VALUES ('46', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509947878');
INSERT INTO `t_logs` VALUES ('47', '系统备份', null, '1', '0:0:0:0:0:0:0:1', '1509948758');
INSERT INTO `t_logs` VALUES ('48', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509950880');
INSERT INTO `t_logs` VALUES ('49', '登录后台', null, '1', '127.0.0.1', '1509952870');
INSERT INTO `t_logs` VALUES ('50', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1509953042');
INSERT INTO `t_logs` VALUES ('51', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510018756');
INSERT INTO `t_logs` VALUES ('52', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510102519');
INSERT INTO `t_logs` VALUES ('53', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510809845');
INSERT INTO `t_logs` VALUES ('54', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510810276');
INSERT INTO `t_logs` VALUES ('55', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510810716');
INSERT INTO `t_logs` VALUES ('56', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510810883');
INSERT INTO `t_logs` VALUES ('57', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510811112');
INSERT INTO `t_logs` VALUES ('58', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510811372');
INSERT INTO `t_logs` VALUES ('59', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510811552');
INSERT INTO `t_logs` VALUES ('60', '系统备份', null, '1', '10.10.0.150', '1510811578');
INSERT INTO `t_logs` VALUES ('61', '系统备份', null, '1', '10.10.0.150', '1510811580');
INSERT INTO `t_logs` VALUES ('62', '系统备份', null, '1', '10.10.0.150', '1510811581');
INSERT INTO `t_logs` VALUES ('63', '系统备份', null, '1', '10.10.0.150', '1510811581');
INSERT INTO `t_logs` VALUES ('64', '系统备份', null, '1', '10.10.0.150', '1510811581');
INSERT INTO `t_logs` VALUES ('65', '系统备份', null, '1', '10.10.0.150', '1510811581');
INSERT INTO `t_logs` VALUES ('66', '系统备份', null, '1', '10.10.0.150', '1510811583');
INSERT INTO `t_logs` VALUES ('67', '系统备份', null, '1', '10.10.0.150', '1510811583');
INSERT INTO `t_logs` VALUES ('68', '系统备份', null, '1', '10.10.0.150', '1510811583');
INSERT INTO `t_logs` VALUES ('69', '系统备份', null, '1', '10.10.0.150', '1510811586');
INSERT INTO `t_logs` VALUES ('70', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510811775');
INSERT INTO `t_logs` VALUES ('71', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510812048');
INSERT INTO `t_logs` VALUES ('72', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510812404');
INSERT INTO `t_logs` VALUES ('73', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510812699');
INSERT INTO `t_logs` VALUES ('74', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510813747');
INSERT INTO `t_logs` VALUES ('75', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510813905');
INSERT INTO `t_logs` VALUES ('76', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510814605');
INSERT INTO `t_logs` VALUES ('77', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510817386');
INSERT INTO `t_logs` VALUES ('78', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510817513');
INSERT INTO `t_logs` VALUES ('79', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510817713');
INSERT INTO `t_logs` VALUES ('80', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510817925');
INSERT INTO `t_logs` VALUES ('81', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510818157');
INSERT INTO `t_logs` VALUES ('82', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510820481');
INSERT INTO `t_logs` VALUES ('83', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510820545');
INSERT INTO `t_logs` VALUES ('84', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510820939');
INSERT INTO `t_logs` VALUES ('85', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510821885');
INSERT INTO `t_logs` VALUES ('86', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510822391');
INSERT INTO `t_logs` VALUES ('87', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510822984');
INSERT INTO `t_logs` VALUES ('88', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510823040');
INSERT INTO `t_logs` VALUES ('89', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510823409');
INSERT INTO `t_logs` VALUES ('90', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510823770');
INSERT INTO `t_logs` VALUES ('91', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510824003');
INSERT INTO `t_logs` VALUES ('92', '系统备份', null, '1', '10.10.0.150', '1510824367');
INSERT INTO `t_logs` VALUES ('93', '系统备份', null, '1', '10.10.0.150', '1510824392');
INSERT INTO `t_logs` VALUES ('94', '登录后台', 'http://oyeve9iiu.bkt.clouddn.com/FvX4rSaBmkcTGNJGMfpQVQNnEqh-', '1', '10.10.0.150', '1510824541');
INSERT INTO `t_logs` VALUES ('95', '登录后台', 'http://oyeve9iiu.bkt.clouddn.com/lrlWd86NVBihTySk4XpZ_2hs1d9s', '1', '10.10.0.150', '1510824553');
INSERT INTO `t_logs` VALUES ('96', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510825440');
INSERT INTO `t_logs` VALUES ('97', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510826050');
INSERT INTO `t_logs` VALUES ('98', '保存系统设置', '{\"site_keywords\":\"博客系统,Blade框架,Tale\",\"site_description\":\"博客系统,Blade框架,Tale\",\"allow_qiniu\":\"1\",\"site_title\":\"www.esmart.com\",\"site_theme\":\"default\",\"allow_install\":\"0\"}', '1', '10.10.0.150', '1510826069');
INSERT INTO `t_logs` VALUES ('99', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510826555');
INSERT INTO `t_logs` VALUES ('100', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510881648');
INSERT INTO `t_logs` VALUES ('101', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510881706');
INSERT INTO `t_logs` VALUES ('102', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510881798');
INSERT INTO `t_logs` VALUES ('103', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510881842');
INSERT INTO `t_logs` VALUES ('104', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510882165');
INSERT INTO `t_logs` VALUES ('105', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510884195');
INSERT INTO `t_logs` VALUES ('106', '主题设置', '变更主题为greyshade', '1', '10.10.0.150', '1510884205');
INSERT INTO `t_logs` VALUES ('107', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510887444');
INSERT INTO `t_logs` VALUES ('108', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510889156');
INSERT INTO `t_logs` VALUES ('109', '登录后台', null, '1', '0:0:0:0:0:0:0:1', '1510890140');

-- ----------------------------
-- Table structure for `t_metas`
-- ----------------------------
DROP TABLE IF EXISTS `t_metas`;
CREATE TABLE `t_metas` (
  `mid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT '项目主键',
  `name` varchar(200) DEFAULT NULL COMMENT '名称',
  `slug` varchar(200) DEFAULT NULL COMMENT '项目缩略名',
  `type` varchar(32) NOT NULL DEFAULT '' COMMENT '项目类型',
  `description` varchar(200) DEFAULT NULL COMMENT '选项描述',
  `sort` int(10) unsigned DEFAULT '0' COMMENT '项目排序',
  `parent` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`mid`),
  KEY `slug` (`slug`)
) ENGINE=MyISAM AUTO_INCREMENT=9 DEFAULT CHARSET=utf8 COMMENT='项目';

-- ----------------------------
-- Records of t_metas
-- ----------------------------
INSERT INTO `t_metas` VALUES ('1', '默认分类', null, 'category', null, '0', '0');
INSERT INTO `t_metas` VALUES ('6', '王爵的技术博客', 'http://biezhi.me', 'link', null, '0', '0');
INSERT INTO `t_metas` VALUES ('7', 'java', null, 'category', null, null, null);
INSERT INTO `t_metas` VALUES ('8', '可爱宝宝', 'http://www.weixueyuan.net', 'link', '', '0', null);

-- ----------------------------
-- Table structure for `t_options`
-- ----------------------------
DROP TABLE IF EXISTS `t_options`;
CREATE TABLE `t_options` (
  `name` varchar(32) NOT NULL DEFAULT '' COMMENT '配置名称',
  `value` varchar(1000) DEFAULT '' COMMENT '配置值',
  `description` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='配置表';

-- ----------------------------
-- Records of t_options
-- ----------------------------
INSERT INTO `t_options` VALUES ('site_title', 'www.esmart.com', '');
INSERT INTO `t_options` VALUES ('social_weibo', '', null);
INSERT INTO `t_options` VALUES ('social_zhihu', '', null);
INSERT INTO `t_options` VALUES ('social_github', '', null);
INSERT INTO `t_options` VALUES ('social_twitter', '', null);
INSERT INTO `t_options` VALUES ('allow_install', '0', '是否允许重新安装博客');
INSERT INTO `t_options` VALUES ('site_theme', 'greyshade', null);
INSERT INTO `t_options` VALUES ('site_keywords', '博客系统,Blade框架,Tale', null);
INSERT INTO `t_options` VALUES ('site_description', '博客系统,Blade框架,Tale', null);
INSERT INTO `t_options` VALUES ('site_url', 'http://localhost:8080/tale-smart', null);
INSERT INTO `t_options` VALUES ('site_is_install', '1', null);
INSERT INTO `t_options` VALUES ('qiniu_isopen', '1', '是否打开七牛上传');
INSERT INTO `t_options` VALUES ('allow_qiniu', '1', null);
INSERT INTO `t_options` VALUES ('site_block_ips', '123.12.12.1', null);

-- ----------------------------
-- Table structure for `t_relationships`
-- ----------------------------
DROP TABLE IF EXISTS `t_relationships`;
CREATE TABLE `t_relationships` (
  `cid` int(10) unsigned NOT NULL COMMENT '内容主键',
  `mid` int(10) unsigned NOT NULL COMMENT '项目主键',
  PRIMARY KEY (`cid`,`mid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='关联表';

-- ----------------------------
-- Records of t_relationships
-- ----------------------------
INSERT INTO `t_relationships` VALUES ('2', '1');

-- ----------------------------
-- Table structure for `t_users`
-- ----------------------------
DROP TABLE IF EXISTS `t_users`;
CREATE TABLE `t_users` (
  `uid` int(10) unsigned NOT NULL AUTO_INCREMENT COMMENT 'user表主键',
  `username` varchar(32) DEFAULT NULL COMMENT '用户名称',
  `password` varchar(64) DEFAULT NULL COMMENT '用户密码',
  `email` varchar(200) DEFAULT NULL COMMENT '用户的邮箱',
  `home_url` varchar(200) DEFAULT NULL COMMENT '用户的主页',
  `screen_name` varchar(32) DEFAULT NULL COMMENT '用户显示的名称',
  `created` int(10) unsigned DEFAULT '0' COMMENT '用户注册时的GMT unix时间戳',
  `activated` int(10) unsigned DEFAULT '0' COMMENT '最后活动时间',
  `logged` int(10) unsigned DEFAULT '0' COMMENT '上次登录最后活跃时间',
  `group_name` varchar(16) DEFAULT 'visitor' COMMENT '用户组',
  PRIMARY KEY (`uid`),
  UNIQUE KEY `name` (`username`),
  UNIQUE KEY `mail` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_users
-- ----------------------------
INSERT INTO `t_users` VALUES ('1', 'admin', '3a4ebf16a4795ad258e5408bae7be341', '1903373681@qq.com', null, 'admin', '1509419259', null, '1510890140', null);
INSERT INTO `t_users` VALUES ('2', 'root', 'root', '123@qq.com', 'http://localhost:8080/tale-smart/main/show/1', null, '1509436032', null, null, null);
