/*
Navicat MySQL Data Transfer

Source Server         : charging
Source Server Version : 50537
Source Host           : 172.16.15.55:3306
Source Database       : bj_test

Target Server Type    : MYSQL
Target Server Version : 50537
File Encoding         : 65001

Date: 2018-03-20 10:01:15
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for app_calctask
-- ----------------------------
DROP TABLE IF EXISTS `app_calctask`;
CREATE TABLE `app_calctask` (
  `TASKID` int(11) NOT NULL,
  `TASKTYPE` smallint(2) NOT NULL DEFAULT '0' COMMENT '0：自动，1：手动',
  `OBJTYPE` smallint(2) NOT NULL,
  `OBJID` int(11) NOT NULL,
  `CIRCLETYPE` smallint(1) NOT NULL COMMENT '0：15分钟，1：小时，2：日，3：月，4：季，6：周，7：年',
  `DATATIME` datetime NOT NULL,
  `CALCTYPE` smallint(1) NOT NULL COMMENT '结算、充电点评分等',
  `CREATOR` varchar(20) NOT NULL,
  `CREATETIME` datetime NOT NULL,
  `STARTTIME` datetime DEFAULT NULL,
  `ENDTIME` datetime DEFAULT NULL,
  `STATUS` smallint(1) NOT NULL COMMENT '1：等待中，2：运行，3：完成，4：失败',
  PRIMARY KEY (`TASKID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bbs_circle_friends
-- ----------------------------
DROP TABLE IF EXISTS `bbs_circle_friends`;
CREATE TABLE `bbs_circle_friends` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `CONTENT` text,
  `ICON1` varchar(50) DEFAULT NULL,
  `ICON2` varchar(50) DEFAULT NULL,
  `ICON3` varchar(50) DEFAULT NULL,
  `ICON4` varchar(50) DEFAULT NULL,
  `PID` int(11) DEFAULT '0' COMMENT '0.顶级',
  `LEVEL` smallint(1) NOT NULL DEFAULT '0' COMMENT '0.顶级1.一级回复',
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bbs_follow_user
-- ----------------------------
DROP TABLE IF EXISTS `bbs_follow_user`;
CREATE TABLE `bbs_follow_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ABOUT_USER` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `STATUS` smallint(1) DEFAULT '1' COMMENT '1.是2.否',
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bbs_private_msg
-- ----------------------------
DROP TABLE IF EXISTS `bbs_private_msg`;
CREATE TABLE `bbs_private_msg` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `TO_USER` int(11) NOT NULL,
  `STATUS` smallint(1) DEFAULT NULL COMMENT '1,成功2.失败',
  `CONTENT` varchar(255) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bbs_user_credit
-- ----------------------------
DROP TABLE IF EXISTS `bbs_user_credit`;
CREATE TABLE `bbs_user_credit` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `RESOURCE` varchar(50) DEFAULT NULL,
  `CREDIT` double(20,2) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_account
-- ----------------------------
DROP TABLE IF EXISTS `bus_account`;
CREATE TABLE `bus_account` (
  `USER_ID` int(11) NOT NULL,
  `STATUS` smallint(1) DEFAULT '1' COMMENT '1.激活2.冻结',
  `TOTAL_MONEY` decimal(10,3) DEFAULT '0.000',
  `USABLE_MONEY` decimal(10,3) DEFAULT '0.000',
  `FREEZE_MONEY` decimal(10,3) DEFAULT '0.000',
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_account_log
-- ----------------------------
DROP TABLE IF EXISTS `bus_account_log`;
CREATE TABLE `bus_account_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `RECORD_ID` int(11) NOT NULL COMMENT '对应各业务ID（充值记录ID,预约记录ID，充电缴费记录ID，提现记录ID...）',
  `ACCOUNT_INFO` varchar(100) DEFAULT NULL,
  `TYPE` smallint(1) NOT NULL COMMENT '1.充值2.平台账户充电3.非平台账户充电4.平台账户预约5.非平台账户预约6.充点卡充值7.充电卡充电',
  `DIRECTION` smallint(1) NOT NULL COMMENT '1.收入2.支出',
  `TOTAL_MONEY` decimal(10,3) NOT NULL,
  `OPERATE_MONEY` decimal(10,3) NOT NULL,
  `USABLE_MONEY` decimal(10,3) NOT NULL,
  `FREEZE_MONEY` decimal(10,3) NOT NULL,
  `REMARK` varchar(250) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  `ADD_IP` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=83 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_appointment
-- ----------------------------
DROP TABLE IF EXISTS `bus_appointment`;
CREATE TABLE `bus_appointment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `PILE_ID` int(11) NOT NULL,
  `APP_NO` varchar(50) NOT NULL,
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `APP_LEN` int(4) DEFAULT NULL,
  `APP_FEE` decimal(10,3) DEFAULT '0.000',
  `APP_STATUS` smallint(1) DEFAULT '1' COMMENT '1.预约中2.履约3.取消4.过时',
  `PAY_WAY` smallint(1) DEFAULT NULL COMMENT '1.资金账户2.支付宝3.微信4.银联5.信用卡',
  `ACCOUNT_INFO` varchar(512) DEFAULT NULL,
  `PAY_STATUS` smallint(1) DEFAULT NULL COMMENT '1.支付中2.已支付3.未支付',
  `IS_BILL` smallint(1) DEFAULT '2' COMMENT '1.是2.否',
  `ADD_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `addtime_index` (`ADD_TIME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_bill
-- ----------------------------
DROP TABLE IF EXISTS `bus_bill`;
CREATE TABLE `bus_bill` (
  `USER_ID` int(11) NOT NULL,
  `DATA_TIME` date NOT NULL,
  `RECHARGE` decimal(10,3) DEFAULT NULL,
  `APP` decimal(10,3) DEFAULT NULL,
  `CHARGE` decimal(10,3) DEFAULT NULL,
  `APP_IN` decimal(10,3) DEFAULT NULL,
  `RECHARGE_IN` decimal(10,3) DEFAULT NULL,
  `CASH` decimal(10,3) DEFAULT NULL,
  `IS_BILL` smallint(1) DEFAULT '2' COMMENT '1.是2.否',
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`USER_ID`,`DATA_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_bills
-- ----------------------------
DROP TABLE IF EXISTS `bus_bills`;
CREATE TABLE `bus_bills` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL COMMENT '用户ID',
  `CHECK_CYCLE` varchar(50) NOT NULL COMMENT '结算周期',
  `TOTAL_FEE` decimal(10,3) DEFAULT '0.000' COMMENT '收入总额（未结算）',
  `APP_FEE_IN` decimal(10,3) DEFAULT '0.000' COMMENT '预约收入（结算后）',
  `CHA_FEE_IN` decimal(10,3) DEFAULT '0.000' COMMENT '充电收入（结算后）',
  `SERVICE_FEE_IN` decimal(10,3) DEFAULT '0.000' COMMENT '服务费收入',
  `PARK_FEE_IN` decimal(10,3) DEFAULT '0.000' COMMENT '停车费收入',
  `RECHARGE_FEE` decimal(10,3) DEFAULT '0.000' COMMENT '充值金额',
  `CASH_FEE` decimal(10,3) DEFAULT '0.000' COMMENT '提现金额',
  `APP_FEE_OUT` decimal(10,3) DEFAULT '0.000' COMMENT '预约费支出',
  `CHA_FEE_OUT` decimal(10,3) DEFAULT '0.000' COMMENT '充电费支出',
  `SERVICE_FEE_OUT` decimal(10,3) DEFAULT '0.000' COMMENT '服务费支出',
  `PARK_FEE_OUT` decimal(10,3) DEFAULT '0.000' COMMENT '停车费支出',
  `CHECK_TYPE` smallint(1) NOT NULL COMMENT '结算类型1.按月',
  `RECEIPT_ID` int(11) DEFAULT NULL COMMENT '开票ID',
  `ADD_TIME` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`ID`),
  KEY `user_cycle_index` (`USER_ID`,`CHECK_CYCLE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_bills_detail
-- ----------------------------
DROP TABLE IF EXISTS `bus_bills_detail`;
CREATE TABLE `bus_bills_detail` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `RECORD_ID` int(11) NOT NULL COMMENT '对应记录ID（预约记录，充电记录）',
  `USER_ID` int(11) NOT NULL,
  `CHECK_CYCLE` varchar(50) NOT NULL,
  `TOTAL_FEE` decimal(10,3) DEFAULT '0.000' COMMENT '总额（未结算）',
  `APP_FEE_IN` decimal(10,3) DEFAULT '0.000' COMMENT '预约费（结算后）',
  `CHA_FEE_IN` decimal(10,3) DEFAULT '0.000' COMMENT '充电费（结算后）',
  `SERVICE_FEE_IN` decimal(10,3) DEFAULT '0.000' COMMENT '服务费（结算后）',
  `PARK_FEE_IN` decimal(10,3) DEFAULT '0.000' COMMENT '停车费（结算后）',
  `CHECK_MARK` smallint(1) NOT NULL COMMENT '1.充电2.预约',
  `ADD_TIME` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`ID`),
  KEY `record_index` (`RECORD_ID`) USING BTREE,
  KEY `user_index` (`USER_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_bussiness_info
-- ----------------------------
DROP TABLE IF EXISTS `bus_bussiness_info`;
CREATE TABLE `bus_bussiness_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BUS_NAME` varchar(100) DEFAULT NULL,
  `LICENCE` varchar(100) DEFAULT NULL COMMENT '营业执照图片路径',
  `ACC_REAL_NAME` varchar(20) DEFAULT NULL COMMENT '银行开户名',
  `BANK_NAME` varchar(100) DEFAULT NULL COMMENT '开户行名称',
  `BANK_ACCOUNT` varchar(50) DEFAULT NULL,
  `LIMIT_YEAR` varchar(50) DEFAULT NULL,
  `SCALE` varchar(50) DEFAULT NULL,
  `BUS_DOMAIN` varchar(50) DEFAULT NULL,
  `DOMAIN` varchar(50) DEFAULT NULL,
  `COUNTRY` varchar(50) DEFAULT NULL,
  `PROVINCE` int(11) DEFAULT NULL,
  `CITY` int(11) DEFAULT NULL,
  `ADDRESS` varchar(100) DEFAULT NULL,
  `TEL` varchar(50) DEFAULT NULL,
  `LICENCE_IMG` varchar(100) DEFAULT NULL,
  `CORPORATE_IMG` varchar(100) DEFAULT NULL,
  `TRANSATOR_IMG` varchar(100) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_bussiness_real
-- ----------------------------
DROP TABLE IF EXISTS `bus_bussiness_real`;
CREATE TABLE `bus_bussiness_real` (
  `BUS_INFO_ID` int(11) NOT NULL,
  `ADD_TIME` datetime NOT NULL COMMENT '添加时间',
  `BUS_NAME` varchar(100) DEFAULT NULL COMMENT '公司名称',
  `ACC_REAL_NAME` varchar(20) DEFAULT NULL COMMENT '企业银行开户名',
  `BANK_ACCOUNT` varchar(50) DEFAULT NULL COMMENT '企业对公账户',
  `BANK_NAME` varchar(100) DEFAULT NULL COMMENT '开户银行名称',
  `LICENCE_IMG` varchar(100) DEFAULT NULL,
  `CORPORATE_IMG` varchar(100) DEFAULT NULL,
  `TRANSATOR_IMG` varchar(100) DEFAULT NULL,
  `VALID_STATUS` smallint(1) DEFAULT '1' COMMENT '1.未审核2.已审核待验证3.审核失败4.验证成功5.验证失败',
  `VALID_MONEY` decimal(10,2) DEFAULT NULL,
  `VALID_CODE` varchar(16) DEFAULT NULL,
  `REMARK` varchar(250) DEFAULT NULL,
  `USER_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`BUS_INFO_ID`,`ADD_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_card_recharge
-- ----------------------------
DROP TABLE IF EXISTS `bus_card_recharge`;
CREATE TABLE `bus_card_recharge` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TRADE_NO` varchar(100) NOT NULL COMMENT '交易订单号',
  `CARD_INFO_ID` int(11) NOT NULL COMMENT '充电卡信息ID',
  `MONEY` decimal(10,3) NOT NULL COMMENT '充值金额',
  `WORKER` int(11) NOT NULL COMMENT '操作员ID',
  `ADD_TIME` datetime NOT NULL COMMENT '添加时间',
  `ADD_IP` varchar(50) DEFAULT NULL COMMENT 'IP地址',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_cash
-- ----------------------------
DROP TABLE IF EXISTS `bus_cash`;
CREATE TABLE `bus_cash` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `MONEY` decimal(10,3) NOT NULL,
  `REMARK` varchar(250) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  `VALID_TIME` datetime DEFAULT NULL,
  `VALID_REMARK` varchar(250) DEFAULT NULL,
  `VALID_STATUS` smallint(1) DEFAULT '1' COMMENT '1.审核中2.审核通过3.审核未通过',
  `CASH_WAY` smallint(1) DEFAULT '2' COMMENT '1.银行转账2...',
  `ACCOUNT_INFO` varchar(100) DEFAULT NULL,
  `CASH_STATUS` smallint(1) DEFAULT NULL COMMENT '1.提现中2.提现成功3.提现失败',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_charge_card
-- ----------------------------
DROP TABLE IF EXISTS `bus_charge_card`;
CREATE TABLE `bus_charge_card` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `BUS_NO` varchar(50) NOT NULL COMMENT '发卡运营商编号',
  `CARD_NO` varchar(50) NOT NULL COMMENT '充电卡卡号',
  `APPLICATION_TYPE` smallint(1) NOT NULL COMMENT '应用类型标识',
  `START_TIME` datetime NOT NULL COMMENT '充电卡启用日期',
  `END_TIME` datetime NOT NULL COMMENT '有效日期',
  `PASSWORD` varchar(30) NOT NULL COMMENT '充电卡密码',
  `CARD_TYPE` smallint(1) NOT NULL COMMENT '充电卡类型标识:1.用户卡2.用户管理卡',
  `WORKER` int(11) DEFAULT NULL COMMENT '职工标识',
  `BAD_RECORD` varchar(100) DEFAULT NULL COMMENT '灰色记录例:[21，22]',
  `USABLE_MONEY` decimal(10,3) DEFAULT '0.000' COMMENT '可用金额',
  `FREEZE_MONEY` decimal(10,3) DEFAULT '0.000' COMMENT '冻结金额',
  `UPDATE_TIME` datetime DEFAULT NULL COMMENT '最后更新时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_charge_rule
-- ----------------------------
DROP TABLE IF EXISTS `bus_charge_rule`;
CREATE TABLE `bus_charge_rule` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(30) NOT NULL COMMENT '规则名称',
  `JIAN_FEE` decimal(10,3) NOT NULL COMMENT '尖费率',
  `FENG_FEE` decimal(10,3) NOT NULL COMMENT '峰费率',
  `PING_FEE` decimal(10,3) NOT NULL COMMENT '平费率',
  `GU_FEE` decimal(10,3) NOT NULL COMMENT '谷费率',
  `REMARK` varchar(250) DEFAULT NULL,
  `ADD_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_favorites
-- ----------------------------
DROP TABLE IF EXISTS `bus_favorites`;
CREATE TABLE `bus_favorites` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `STATION_ID` int(11) NOT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  `IS_SHOW` smallint(1) NOT NULL DEFAULT '1' COMMENT '1.是2.否',
  PRIMARY KEY (`ID`),
  KEY `userId_stationId_index` (`USER_ID`,`STATION_ID`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_group_info
-- ----------------------------
DROP TABLE IF EXISTS `bus_group_info`;
CREATE TABLE `bus_group_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `GROUP_NAME` varchar(100) DEFAULT NULL COMMENT '集团名称',
  `SCALE` varchar(50) DEFAULT NULL COMMENT '集团规模',
  `DOMAIN` varchar(250) DEFAULT NULL COMMENT '主营业务',
  `PROVINCE` int(11) DEFAULT NULL COMMENT '省份',
  `CITY` int(11) DEFAULT NULL COMMENT '市区',
  `ADDRESS` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `PHONE` varchar(50) DEFAULT NULL,
  `LICENCE_IMG` varchar(100) DEFAULT NULL COMMENT '运营资质图片上传（预留）',
  `ADD_TIME` datetime DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_payment
-- ----------------------------
DROP TABLE IF EXISTS `bus_payment`;
CREATE TABLE `bus_payment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `PILE_ID` int(11) NOT NULL,
  `TRADE_NO` varchar(50) NOT NULL COMMENT '对应采集模块充电记录流水号',
  `CHA_MODE` smallint(1) DEFAULT NULL,
  `DEAL_STATUS` smallint(1) DEFAULT '1' COMMENT '1.交易中2.成功3.失败',
  `PARK_FEE` decimal(12,3) DEFAULT '0.000',
  `CHA_FEE` decimal(12,3) DEFAULT '0.000',
  `SERVICE_FEE` decimal(12,3) DEFAULT '0.000',
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `CHA_LEN` int(8) DEFAULT NULL,
  `CHA_POWER` double(12,2) DEFAULT NULL,
  `SHOULD_MONEY` decimal(10,3) DEFAULT NULL,
  `ACTUAL_MONEY` decimal(10,3) DEFAULT NULL,
  `PAY_WAY` smallint(1) DEFAULT NULL COMMENT '1.资金账户2.支付宝3.微信4.银联5.信用卡',
  `ACCOUNT_INFO` varchar(512) DEFAULT NULL,
  `PAY_STATUS` smallint(1) DEFAULT NULL COMMENT '1.未缴费2.缴费中3.缴费成功4.缴费失败',
  `PLATE_NUM` varchar(50) DEFAULT NULL COMMENT '车牌号',
  `IS_BILL` smallint(1) DEFAULT '2' COMMENT '1.是2.否',
  `UPDATE_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `Trade_NO_Unique_Index` (`TRADE_NO`)
) ENGINE=InnoDB AUTO_INCREMENT=15657 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_pile_apply
-- ----------------------------
DROP TABLE IF EXISTS `bus_pile_apply`;
CREATE TABLE `bus_pile_apply` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REAL_NAME` varchar(10) NOT NULL,
  `PHONE` varchar(20) NOT NULL,
  `ADDRESS` varchar(200) NOT NULL,
  `REMARK` varchar(250) DEFAULT NULL,
  `USER_ID` int(11) NOT NULL,
  `ADDTIME` datetime DEFAULT NULL,
  `VALID_STATUS` smallint(1) DEFAULT NULL,
  `VALID_REMARK` varchar(250) DEFAULT NULL,
  `VALID_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_pile_chargerule
-- ----------------------------
DROP TABLE IF EXISTS `bus_pile_chargerule`;
CREATE TABLE `bus_pile_chargerule` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PILE_ID` int(11) NOT NULL COMMENT '桩表ID',
  `CHARGERULE_ID` int(11) NOT NULL COMMENT '规则表ID',
  `CHARGE_FEE` decimal(6,3) NOT NULL DEFAULT '0.000' COMMENT '单一费率金额',
  `PARK_FEE` decimal(6,3) NOT NULL DEFAULT '0.000' COMMENT '停车费',
  `SERVICE_FEE` decimal(6,3) NOT NULL DEFAULT '0.000' COMMENT '服务费',
  `ACTIVE_TIME` datetime NOT NULL COMMENT '生效时间',
  `STATUS` smallint(1) DEFAULT '1' COMMENT '1.生效2.失效',
  `ADD_TIME` datetime NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=317 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_pile_comment
-- ----------------------------
DROP TABLE IF EXISTS `bus_pile_comment`;
CREATE TABLE `bus_pile_comment` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `PILE_ID` int(11) NOT NULL,
  `CHARGERECORD_ID` int(11) NOT NULL,
  `CONTENT` varchar(255) DEFAULT NULL,
  `PATH` varchar(512) DEFAULT NULL COMMENT 'xxx,xxx,xxx',
  `PID` int(11) NOT NULL DEFAULT '0' COMMENT '0.顶级',
  `ADD_TIME` datetime NOT NULL,
  `SCORE` smallint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_pile_model
-- ----------------------------
DROP TABLE IF EXISTS `bus_pile_model`;
CREATE TABLE `bus_pile_model` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `BRAND` varchar(50) DEFAULT NULL,
  `CHA_WAY` smallint(1) NOT NULL COMMENT '1.交流2.直流',
  `CHA_TYPE` smallint(1) NOT NULL COMMENT '1.快充2.慢充3.超速',
  `NUM` varchar(50) DEFAULT NULL,
  `IS_INTELLIGENT` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `STANDARD` smallint(1) DEFAULT NULL,
  `IN_V` varchar(50) DEFAULT NULL,
  `OUT_V` varchar(50) DEFAULT NULL,
  `MAX_P` varchar(50) DEFAULT NULL,
  `RAT_P` varchar(50) DEFAULT NULL,
  `HULL` varchar(50) DEFAULT NULL,
  `SIZE` varchar(50) DEFAULT NULL,
  `PRO_LEVEL` varchar(50) DEFAULT NULL,
  `LINE_LEN` varchar(50) DEFAULT NULL,
  `RATE` varchar(50) DEFAULT NULL,
  `MEA_ACC` varchar(50) DEFAULT NULL,
  `WEIGHT` varchar(50) DEFAULT NULL,
  `WINDOW` varchar(50) DEFAULT NULL,
  `WORK_TEM` varchar(50) DEFAULT NULL,
  `RELA_HUM` varchar(50) DEFAULT NULL,
  `ALTITUDE` varchar(50) DEFAULT NULL,
  `STATUS` smallint(1) DEFAULT NULL COMMENT '1:有效，2：无效',
  `INS_METHOD` varchar(50) DEFAULT NULL,
  `WORK_STA` varchar(50) DEFAULT NULL,
  `IDENTIFY` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_pow_coin_log
-- ----------------------------
DROP TABLE IF EXISTS `bus_pow_coin_log`;
CREATE TABLE `bus_pow_coin_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `RECORD_ID` int(11) NOT NULL,
  `ACCOUNT_INFO` varchar(100) DEFAULT NULL,
  `TYPE` smallint(1) NOT NULL,
  `DIRECTION` smallint(1) NOT NULL COMMENT '1.收入2.支出',
  `TOTAL_COIN` decimal(10,0) NOT NULL COMMENT '账户总能量币数',
  `OPERATE_COIN` decimal(10,0) NOT NULL COMMENT '操作能量币数',
  `REMARK` varchar(250) DEFAULT NULL COMMENT '备注',
  `ADD_TIME` datetime DEFAULT NULL COMMENT '添加时间',
  `ADD_IP` varchar(100) DEFAULT NULL COMMENT '添加IP',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_recharge
-- ----------------------------
DROP TABLE IF EXISTS `bus_recharge`;
CREATE TABLE `bus_recharge` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TRADE_NO` varchar(100) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  `STATUS` smallint(1) NOT NULL DEFAULT '1' COMMENT '1.充值中2.成功3.失败',
  `MONEY` decimal(10,3) DEFAULT NULL,
  `PAY_WAY` smallint(1) DEFAULT NULL COMMENT '1.资金账户2.支付宝3.微信4.银联5.信用卡',
  `ACCOUNT_INFO` varchar(512) DEFAULT NULL,
  `FEE` decimal(10,3) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  `ADD_IP` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=42 DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (ID)
(PARTITION data01 VALUES LESS THAN (1000000) ENGINE = InnoDB,
 PARTITION data02 VALUES LESS THAN (2000000) ENGINE = InnoDB) */;

-- ----------------------------
-- Table structure for bus_repair_point
-- ----------------------------
DROP TABLE IF EXISTS `bus_repair_point`;
CREATE TABLE `bus_repair_point` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `ADDRESS` varchar(250) NOT NULL,
  `IS_SHOW` smallint(1) NOT NULL,
  `LNG` varchar(20) NOT NULL,
  `LAT` varchar(20) NOT NULL,
  `PHONE` varchar(20) DEFAULT NULL,
  `TEL` varchar(20) DEFAULT NULL,
  `WORK_TIME` varchar(50) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_statement
-- ----------------------------
DROP TABLE IF EXISTS `bus_statement`;
CREATE TABLE `bus_statement` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `INFO_ID` int(11) NOT NULL,
  `BEFORE_FEE` decimal(10,3) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_suggestion
-- ----------------------------
DROP TABLE IF EXISTS `bus_suggestion`;
CREATE TABLE `bus_suggestion` (
  `USER_ID` int(11) NOT NULL,
  `ADD_TIME` datetime NOT NULL,
  `CONTENT` varchar(1024) NOT NULL,
  `PIC` varchar(512) DEFAULT NULL COMMENT '多张图格式:xxxx.jpg,xxxx.jpg,xxxx.jpg...',
  `STATUS` smallint(1) DEFAULT '0' COMMENT '0：未处理，1：已处理',
  PRIMARY KEY (`USER_ID`,`ADD_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_user
-- ----------------------------
DROP TABLE IF EXISTS `bus_user`;
CREATE TABLE `bus_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USERNAME` varchar(50) NOT NULL,
  `PASSWORD` varchar(50) NOT NULL,
  `PAY_PASSWORD` varchar(50) NOT NULL,
  `HEAD_IMG` varchar(100) DEFAULT NULL,
  `IS_LOCK` smallint(1) DEFAULT '2' COMMENT '1.是2.否',
  `REAL_STATUS` smallint(1) DEFAULT '1' COMMENT '1.未通过2.通过',
  `EMAIL` varchar(50) DEFAULT NULL,
  `EMAIL_STATUS` smallint(1) DEFAULT '1' COMMENT '1.未通过2.通过',
  `PHONE` varchar(20) DEFAULT NULL,
  `PHONE_STATUS` smallint(1) DEFAULT '1' COMMENT '1.未通过2.通过',
  `USER_TYPE` smallint(1) DEFAULT NULL COMMENT '1.平台用户2.企业用户3.个人用户',
  `INFO_ID` int(11) DEFAULT NULL,
  `GROUP_ID` int(11) DEFAULT NULL COMMENT '集团子账户对应集团管理员ID',
  `REGIST_TIME` datetime DEFAULT NULL,
  `REGIST_IP` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `username_index` (`USERNAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_user_info
-- ----------------------------
DROP TABLE IF EXISTS `bus_user_info`;
CREATE TABLE `bus_user_info` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `FRONT` varchar(50) DEFAULT NULL,
  `CON` varchar(50) DEFAULT NULL,
  `CARD_NO` varchar(20) DEFAULT NULL,
  `REAL_NAME` varchar(10) DEFAULT NULL,
  `BIRTHDAY` date DEFAULT NULL,
  `SEX` smallint(1) DEFAULT '1' COMMENT '1.男2.女',
  `SIGN` varchar(250) DEFAULT NULL,
  `QQ` varchar(20) DEFAULT NULL,
  `PROVINCE` int(11) DEFAULT NULL,
  `CITY` int(11) DEFAULT NULL,
  `BRAND` int(11) DEFAULT NULL COMMENT '关联字典表',
  `MODEL` int(11) DEFAULT NULL COMMENT '关联字典表',
  `VIN` varchar(40) DEFAULT NULL COMMENT '车架号',
  `PLATE_NO` varchar(50) DEFAULT NULL,
  `PIC` varchar(250) DEFAULT NULL COMMENT '驾驶证图片路径',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_user_real
-- ----------------------------
DROP TABLE IF EXISTS `bus_user_real`;
CREATE TABLE `bus_user_real` (
  `USER_ID` int(11) NOT NULL,
  `ADD_TIME` datetime NOT NULL,
  `CARD_NUM` varchar(20) NOT NULL,
  `FRONT` varchar(100) NOT NULL,
  `CON` varchar(100) DEFAULT NULL,
  `REAL_NAME` varchar(20) NOT NULL,
  `STATUS` smallint(1) NOT NULL DEFAULT '1' COMMENT '1.审核中2.审核通过3.审核失败',
  `REMARK` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`,`ADD_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_user_receipt
-- ----------------------------
DROP TABLE IF EXISTS `bus_user_receipt`;
CREATE TABLE `bus_user_receipt` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `RECIPIENT` varchar(20) NOT NULL,
  `PHONE` varchar(20) NOT NULL,
  `EXPRESS_NUM` varchar(50) DEFAULT NULL,
  `EXPRESS_COMPANY` varchar(50) DEFAULT NULL,
  `BILL_TYPE` smallint(1) NOT NULL DEFAULT '1' COMMENT '1.普通发票2.增值发票',
  `BILL_HEAD` varchar(50) NOT NULL,
  `MONEY` decimal(10,3) NOT NULL,
  `STATUS` smallint(1) NOT NULL DEFAULT '1' COMMENT '1.审核中2.开票成功3.开票失败',
  `ADDRESS` varchar(250) NOT NULL,
  `TIME` varchar(100) NOT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  `VALID_REMARK` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for bus_user_token
-- ----------------------------
DROP TABLE IF EXISTS `bus_user_token`;
CREATE TABLE `bus_user_token` (
  `USERID` int(11) NOT NULL DEFAULT '0',
  `TOKEN` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dcs_alarm_events
-- ----------------------------
DROP TABLE IF EXISTS `dcs_alarm_events`;
CREATE TABLE `dcs_alarm_events` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PILE_ID` int(11) NOT NULL,
  `EVENT_ID` char(8) NOT NULL COMMENT '枚举定义',
  `DESCRIBED` varchar(250) DEFAULT NULL,
  `LEVEL` smallint(1) DEFAULT NULL COMMENT '1.普通告警2.严重告警3.未知告警',
  `EVENT_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dcs_basedatatype
-- ----------------------------
DROP TABLE IF EXISTS `dcs_basedatatype`;
CREATE TABLE `dcs_basedatatype` (
  `DATATYPE1` int(11) NOT NULL AUTO_INCREMENT,
  `TYPECLASS` int(1) NOT NULL,
  `NAME` varchar(16) NOT NULL,
  `UNIT` varchar(16) NOT NULL,
  `CODE1` char(8) DEFAULT NULL,
  `CODE2` char(8) DEFAULT NULL,
  PRIMARY KEY (`DATATYPE1`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dcs_chargerecord
-- ----------------------------
DROP TABLE IF EXISTS `dcs_chargerecord`;
CREATE TABLE `dcs_chargerecord` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CHARGEID` int(11) DEFAULT NULL,
  `CHARGE_TYPE` smallint(1) DEFAULT NULL COMMENT '0.APP1.本地刷卡',
  `PILE_NUM` smallint(1) DEFAULT NULL COMMENT '充电桩为一桩多充时用来标记接口号，一桩一充时此项为0。多个接口时顺序对每个接口进行编号',
  `TRADE_NO` varchar(32) DEFAULT NULL,
  `PAY_NO` varchar(64) DEFAULT NULL,
  `CARD_NO` varchar(64) DEFAULT NULL,
  `IS_TIMES` smallint(1) DEFAULT NULL COMMENT '0.分时1.不分时',
  `START_TIME` datetime DEFAULT NULL,
  `END_TIME` datetime DEFAULT NULL,
  `BMJ1` double(12,4) DEFAULT NULL,
  `BMJ2` double(12,4) DEFAULT NULL,
  `BMF1` double(12,4) DEFAULT NULL,
  `BMF2` double(12,4) DEFAULT NULL,
  `BMP1` double(12,4) DEFAULT NULL,
  `BMP2` double(12,4) DEFAULT NULL,
  `BMG1` double(12,4) DEFAULT NULL,
  `BMG2` double(12,4) DEFAULT NULL,
  `MET_TYPE` double(10,4) DEFAULT NULL,
  `BMZ1` double(12,4) DEFAULT NULL,
  `BMZ2` double(20,4) DEFAULT NULL,
  `DJJ` decimal(10,3) DEFAULT NULL,
  `DLJ` double(12,4) DEFAULT NULL,
  `JEJ` decimal(10,3) DEFAULT NULL,
  `DJF` decimal(10,3) DEFAULT NULL,
  `DLF` double(12,4) DEFAULT NULL,
  `JEF` decimal(12,3) DEFAULT NULL,
  `DJP` decimal(10,3) DEFAULT NULL,
  `DLP` double(10,4) DEFAULT NULL,
  `JEP` decimal(10,3) DEFAULT NULL,
  `DJG` decimal(10,3) DEFAULT NULL,
  `DLG` double(10,4) DEFAULT NULL,
  `JEG` decimal(10,3) DEFAULT NULL,
  `DLZ` double(12,4) DEFAULT NULL,
  `BUS_TYPE` smallint(1) DEFAULT NULL COMMENT '1.充电',
  `AFTER_MONEY` decimal(10,3) DEFAULT NULL,
  `XFDJ` decimal(10,3) DEFAULT NULL,
  `XFJE` decimal(10,3) DEFAULT NULL,
  `MARK` varchar(50) DEFAULT NULL,
  `PAY_STATUS` smallint(1) DEFAULT NULL COMMENT '0.CPU卡成功1.CPU卡失败2.M1卡成功3.M1卡失败',
  `CLIENT_NO` varchar(50) DEFAULT NULL,
  `BEFORE_MONEY` decimal(10,3) DEFAULT NULL,
  `BAG_NO` varchar(50) DEFAULT NULL,
  `PAY_MONEY` decimal(12,3) DEFAULT NULL,
  `DATE_TIME` varchar(50) DEFAULT NULL,
  `RADOM_NO` varchar(50) DEFAULT NULL,
  `PAY_TYPE` smallint(1) DEFAULT NULL,
  `TAC` varchar(50) DEFAULT NULL,
  `KEY_VERSION` varchar(50) DEFAULT NULL,
  `TERMINAL_NO` varchar(50) DEFAULT NULL,
  `CHA_PRICE` decimal(10,3) DEFAULT NULL,
  `CHA_MONEY` decimal(10,3) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `TRADE_NO_Unique_index` (`TRADE_NO`)
) ENGINE=InnoDB AUTO_INCREMENT=23432 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dcs_hisya
-- ----------------------------
DROP TABLE IF EXISTS `dcs_hisya`;
CREATE TABLE `dcs_hisya` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CHARGEID` int(11) DEFAULT NULL,
  `DATATYPE` int(6) DEFAULT NULL,
  `DATATIME` datetime DEFAULT NULL,
  `VALUE` double(10,4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `index1` (`CHARGEID`,`DATATYPE`),
  KEY `indexData` (`CHARGEID`,`DATATYPE`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dcs_hisyc
-- ----------------------------
DROP TABLE IF EXISTS `dcs_hisyc`;
CREATE TABLE `dcs_hisyc` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CHARGEID` int(11) NOT NULL,
  `DATATYPE` int(6) NOT NULL,
  `DATATIME` datetime NOT NULL,
  `VALUE` double(10,4) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `index` (`CHARGEID`,`DATATYPE`,`DATATIME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10346687 DEFAULT CHARSET=utf8
/*!50100 PARTITION BY RANGE (ID)
(PARTITION p0 VALUES LESS THAN (1000000) ENGINE = InnoDB,
 PARTITION p1 VALUES LESS THAN (2000000) ENGINE = InnoDB,
 PARTITION p2 VALUES LESS THAN (3000000) ENGINE = InnoDB,
 PARTITION p3 VALUES LESS THAN (4000000) ENGINE = InnoDB,
 PARTITION p4 VALUES LESS THAN (5000000) ENGINE = InnoDB,
 PARTITION p5 VALUES LESS THAN (6000000) ENGINE = InnoDB,
 PARTITION p6 VALUES LESS THAN (7000000) ENGINE = InnoDB,
 PARTITION p7 VALUES LESS THAN (8000000) ENGINE = InnoDB,
 PARTITION p8 VALUES LESS THAN (9000000) ENGINE = InnoDB,
 PARTITION p9 VALUES LESS THAN (10000000) ENGINE = InnoDB,
 PARTITION p10 VALUES LESS THAN (11000000) ENGINE = InnoDB,
 PARTITION p11 VALUES LESS THAN (12000000) ENGINE = InnoDB,
 PARTITION p12 VALUES LESS THAN (13000000) ENGINE = InnoDB,
 PARTITION p13 VALUES LESS THAN (14000000) ENGINE = InnoDB,
 PARTITION p14 VALUES LESS THAN (15000000) ENGINE = InnoDB,
 PARTITION pmax VALUES LESS THAN MAXVALUE ENGINE = InnoDB) */;

-- ----------------------------
-- Table structure for dcs_hisyx
-- ----------------------------
DROP TABLE IF EXISTS `dcs_hisyx`;
CREATE TABLE `dcs_hisyx` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CHARGEID` int(11) NOT NULL,
  `DATATYPE` int(6) NOT NULL,
  `DATATIME` datetime NOT NULL,
  `VALUE` int(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for dcs_monitor
-- ----------------------------
DROP TABLE IF EXISTS `dcs_monitor`;
CREATE TABLE `dcs_monitor` (
  `PILE_ID` int(11) NOT NULL,
  `CHA_LEN` int(8) DEFAULT NULL,
  `CHA_POWER` double(5,2) DEFAULT NULL,
  `MONEY` decimal(10,3) DEFAULT NULL,
  `OUT_V` varchar(50) DEFAULT NULL,
  `OUT_I` varchar(50) DEFAULT NULL,
  `STATUS` smallint(1) DEFAULT NULL COMMENT '1.正常2.故障3.维护...',
  `IS_ONLINE` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_CHARGING` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`PILE_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pob_charging_pile
-- ----------------------------
DROP TABLE IF EXISTS `pob_charging_pile`;
CREATE TABLE `pob_charging_pile` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PILE_CODE` varchar(100) NOT NULL,
  `PILE_NAME` varchar(100) NOT NULL,
  `STATION_ID` int(11) NOT NULL,
  `STATUS` smallint(1) DEFAULT '1' COMMENT '1.正常2.故障3.维护',
  `PILE_TYPE` smallint(1) DEFAULT NULL COMMENT '1.快充2.慢充3.超速',
  `PILE_MODEL` int(11) DEFAULT NULL,
  `INTF_TYPE` smallint(1) DEFAULT NULL COMMENT '1.国标',
  `CHA_WAY` smallint(1) DEFAULT NULL COMMENT '1.交流2.直流',
  `COM_TYPE` smallint(1) DEFAULT NULL COMMENT '1.国网2....',
  `COM_ADDR` varchar(32) DEFAULT NULL,
  `COM_SUB_ADDR` varchar(32) DEFAULT NULL COMMENT '针对1桩多枪时有效',
  `IS_APP` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_TIME_CHA` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_RATION_CHA` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_MONEY_CHA` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_CONTROL` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_CHA_LOAD` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `BUILD_TIME` datetime DEFAULT NULL,
  `DOC` varchar(100) DEFAULT NULL,
  `IS_FEE` smallint(1) DEFAULT '1' COMMENT '1.是2.否',
  `FEE_RULE` int(11) DEFAULT NULL COMMENT '0.单一电费 其他见时段定义表',
  `CHARGE_FEE` decimal(10,3) DEFAULT NULL COMMENT '元/度 当电费规则为0时有效',
  `PARK_FEE` decimal(10,3) DEFAULT NULL COMMENT '元/小时',
  `SERVICE_FEE` decimal(10,3) DEFAULT NULL COMMENT '元/度',
  `PAY_WAY` char(8) NOT NULL COMMENT '00000111从右往左依次1.app2.充电卡3.人工',
  `ADDRESS` varchar(250) DEFAULT NULL,
  `IS_LOCK` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `LOCK_CODE` varchar(100) DEFAULT NULL,
  `SOFT_VERSION` varchar(50) DEFAULT NULL,
  `HARD_VERSION` varchar(50) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `pileCodeIndex` (`PILE_CODE`)
) ENGINE=InnoDB AUTO_INCREMENT=126 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pob_charging_station
-- ----------------------------
DROP TABLE IF EXISTS `pob_charging_station`;
CREATE TABLE `pob_charging_station` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `STATION_NAME` varchar(50) NOT NULL,
  `PROVINCE` int(9) DEFAULT NULL,
  `CITY` int(9) DEFAULT NULL,
  `ADDRESS` varchar(200) NOT NULL,
  `LNG` varchar(20) DEFAULT NULL,
  `LAT` varchar(20) DEFAULT NULL,
  `BUS_MEC` int(11) DEFAULT NULL COMMENT '运营类型为个人是ID对应用户ID，为企业时对应企业信息ID',
  `BUS_TYPE` smallint(1) DEFAULT NULL COMMENT '2.企业3.个人',
  `OPEN_DAY` smallint(1) DEFAULT NULL COMMENT '1.每天2.工作日3.非工作日',
  `OPEN_TIME` smallint(1) DEFAULT NULL COMMENT '平台自定义时间段',
  `PARK_TYPE` smallint(1) DEFAULT NULL COMMENT '1.露天2.室内',
  `IS_PARK_FEE` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_SHOW` smallint(1) DEFAULT '1' COMMENT '1.是2.否',
  `REMARK` varchar(250) DEFAULT NULL,
  `IMG` varchar(250) DEFAULT NULL,
  `SCORE` smallint(1) DEFAULT NULL,
  `IS_VALIDATE` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `OPERAT_TIME` datetime DEFAULT NULL,
  `LINK_MAN` varchar(50) DEFAULT NULL,
  `LINK_PHONE` varchar(50) DEFAULT NULL,
  `FAST_NUM` int(11) DEFAULT '0',
  `SLOW_NUM` int(11) DEFAULT '0',
  `DATA_SOURCE` varchar(20) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `busmec_bustype_index` (`BUS_MEC`,`BUS_TYPE`) USING BTREE,
  KEY `stationName_index` (`STATION_NAME`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pob_charging_temp_pile
-- ----------------------------
DROP TABLE IF EXISTS `pob_charging_temp_pile`;
CREATE TABLE `pob_charging_temp_pile` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PILE_CODE` varchar(100) NOT NULL,
  `PILE_NAME` varchar(100) NOT NULL,
  `TEMP_STATION_ID` int(11) DEFAULT NULL,
  `REAL_STATION_ID` int(11) DEFAULT NULL,
  `REAL_PILE_ID` int(11) DEFAULT NULL,
  `BUS_MEC` int(11) DEFAULT NULL COMMENT '运营类型为个人是ID对应用户ID，为企业时对应企业信息ID',
  `BUS_TYPE` smallint(1) DEFAULT NULL COMMENT '2.企业3.个人',
  `STATUS` smallint(1) DEFAULT '1' COMMENT '1.正常2.故障3.维护',
  `PILE_TYPE` smallint(1) DEFAULT NULL COMMENT '1.快充2.慢充3.超速',
  `PILE_MODEL` int(11) DEFAULT NULL,
  `INTF_TYPE` smallint(1) DEFAULT NULL COMMENT '1.国标',
  `CHA_WAY` smallint(1) DEFAULT NULL COMMENT '1.交流2.直流',
  `COM_TYPE` smallint(1) DEFAULT NULL COMMENT '1.国网2....',
  `COM_ADDR` varchar(32) DEFAULT NULL,
  `COM_SUB_ADDR` varchar(32) DEFAULT NULL COMMENT '针对1桩多枪时有效',
  `IS_APP` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_TIME_CHA` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_RATION_CHA` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_MONEY_CHA` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_CONTROL` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_CHA_LOAD` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `BUILD_TIME` datetime DEFAULT NULL,
  `DOC` varchar(100) DEFAULT NULL,
  `IS_FEE` smallint(1) DEFAULT '1' COMMENT '1.是2.否',
  `FEE_RULE` int(11) DEFAULT NULL COMMENT '1.单一电费 其他见时段定义表',
  `ACTIVE_TIME` datetime DEFAULT NULL,
  `CHARGE_FEE` decimal(10,3) DEFAULT NULL COMMENT '元/度 当电费规则为0时有效',
  `PARK_FEE` decimal(10,3) DEFAULT NULL COMMENT '元/小时',
  `SERVICE_FEE` decimal(10,3) DEFAULT NULL COMMENT '元/度',
  `PAY_WAY` char(8) DEFAULT NULL COMMENT '00000111从右往左依次1.app2.充电卡3.人工',
  `ADDRESS` varchar(250) DEFAULT NULL,
  `IS_LOCK` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `LOCK_CODE` varchar(100) DEFAULT NULL,
  `SOFT_VERSION` varchar(50) DEFAULT NULL,
  `HARD_VERSION` varchar(50) DEFAULT NULL,
  `REQUEST_TYPE` smallint(1) DEFAULT NULL COMMENT '1.点+桩新增2.单个桩新增3.修改单个点（已审核通过的点）4.桩修改（已经审核通过的桩修改）',
  `VALID_STATUS` smallint(1) DEFAULT '1' COMMENT '1.待审核2.审核中3.审核成功4.审核失败',
  `VALID_REMARK` varchar(250) DEFAULT NULL COMMENT '审核备注',
  `UPDATE_TIME` datetime DEFAULT NULL,
  `VALID_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `busmec_bustype_index` (`BUS_MEC`,`BUS_TYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pob_charging_temp_station
-- ----------------------------
DROP TABLE IF EXISTS `pob_charging_temp_station`;
CREATE TABLE `pob_charging_temp_station` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `REAL_STATION_ID` int(11) DEFAULT NULL,
  `STATION_NAME` varchar(50) NOT NULL,
  `PROVINCE` int(9) DEFAULT NULL,
  `CITY` int(9) DEFAULT NULL,
  `ADDRESS` varchar(200) NOT NULL,
  `LNG` varchar(20) DEFAULT NULL,
  `LAT` varchar(20) DEFAULT NULL,
  `BUS_MEC` int(11) DEFAULT NULL COMMENT '运营类型为个人是ID对应用户ID，为企业时对应企业信息ID',
  `BUS_TYPE` smallint(1) DEFAULT NULL COMMENT '2.企业3.个人',
  `OPEN_DAY` smallint(1) DEFAULT NULL COMMENT '1.每天2.工作日3.非工作日',
  `OPEN_TIME` smallint(1) DEFAULT NULL COMMENT '平台自定义时间段',
  `PARK_TYPE` smallint(1) DEFAULT NULL COMMENT '1.露天2.室内',
  `IS_PARK_FEE` smallint(1) DEFAULT NULL COMMENT '1.是2.否',
  `IS_SHOW` smallint(1) DEFAULT '1' COMMENT '1.是2.否',
  `REMARK` varchar(250) DEFAULT NULL,
  `REQUEST_TYPE` smallint(1) DEFAULT NULL COMMENT '1.点+桩新增2.单个桩新增3.修改单个点（已审核通过的点）4.桩修改（已经审核通过的桩修改）',
  `VALID_REMARK` varchar(250) DEFAULT NULL COMMENT '审核备注',
  `IMG` varchar(250) DEFAULT NULL,
  `SCORE` smallint(1) DEFAULT NULL,
  `VALID_STATUS` smallint(1) DEFAULT '1' COMMENT '1.待审核2.审核中3.审核成功4.审核失败',
  `OPERAT_TIME` datetime DEFAULT NULL,
  `LINK_MAN` varchar(50) DEFAULT NULL,
  `LINK_PHONE` varchar(50) DEFAULT NULL,
  `FAST_NUM` int(11) DEFAULT NULL,
  `SLOW_NUM` int(11) DEFAULT NULL,
  `DATA_SOURCE` varchar(20) DEFAULT NULL,
  `UPDATE_TIME` datetime DEFAULT NULL,
  `VALID_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `busmec_bustype_index` (`BUS_MEC`,`BUS_TYPE`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pob_news
-- ----------------------------
DROP TABLE IF EXISTS `pob_news`;
CREATE TABLE `pob_news` (
  `ID` int(11) NOT NULL,
  `TYPE` smallint(1) NOT NULL COMMENT '1.充电动态;2:新闻资讯',
  `TITLE` varchar(255) NOT NULL,
  `SUMMARY` varchar(255) NOT NULL,
  `PICTURE` varchar(255) DEFAULT NULL,
  `CONTENT` text,
  `PUBLISHER` varchar(50) DEFAULT NULL,
  `PUBLISHDATE` date DEFAULT NULL,
  `STATUS` smallint(1) DEFAULT NULL,
  `ORDERBY` smallint(5) DEFAULT NULL,
  `VISITCOUNT` int(11) DEFAULT NULL,
  `ISTOP` smallint(1) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_accountrole
-- ----------------------------
DROP TABLE IF EXISTS `sys_accountrole`;
CREATE TABLE `sys_accountrole` (
  `USERID` int(11) NOT NULL,
  `ROLEID` int(11) NOT NULL,
  PRIMARY KEY (`USERID`,`ROLEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_buttondef
-- ----------------------------
DROP TABLE IF EXISTS `sys_buttondef`;
CREATE TABLE `sys_buttondef` (
  `BUTTONID` int(11) NOT NULL AUTO_INCREMENT,
  `BUTTONNAME` varchar(20) NOT NULL,
  `MODULEID` varchar(16) NOT NULL,
  `URL` varchar(100) DEFAULT NULL,
  `ICON` varchar(100) DEFAULT NULL,
  `NOTE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`BUTTONID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_car_brand
-- ----------------------------
DROP TABLE IF EXISTS `sys_car_brand`;
CREATE TABLE `sys_car_brand` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  `PID` int(11) DEFAULT '0',
  `SORT` smallint(5) DEFAULT '0',
  PRIMARY KEY (`ID`),
  KEY `pid_index` (`PID`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=241 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_def_area
-- ----------------------------
DROP TABLE IF EXISTS `sys_def_area`;
CREATE TABLE `sys_def_area` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(50) NOT NULL,
  `NID` varchar(50) DEFAULT NULL COMMENT '区域简拼',
  `PID` int(11) NOT NULL DEFAULT '0' COMMENT '0,顶层',
  `SORT` smallint(5) DEFAULT '0',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3577 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_link
-- ----------------------------
DROP TABLE IF EXISTS `sys_link`;
CREATE TABLE `sys_link` (
  `ID` varchar(32) NOT NULL,
  `TYPE_ID` varchar(32) NOT NULL,
  `NAME` varchar(100) NOT NULL,
  `VALUE` varchar(100) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_link_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_link_type`;
CREATE TABLE `sys_link_type` (
  `ID` varchar(32) NOT NULL,
  `NAME` varchar(50) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_moduledef
-- ----------------------------
DROP TABLE IF EXISTS `sys_moduledef`;
CREATE TABLE `sys_moduledef` (
  `MODULEID` varchar(16) NOT NULL,
  `MODULENAME` varchar(32) NOT NULL,
  `SYSTEMID` smallint(1) NOT NULL DEFAULT '1' COMMENT '1：后台系统；2：运营商系统；3：个人系统',
  `URL` varchar(100) DEFAULT NULL,
  `PARENTMODULEID` varchar(16) NOT NULL,
  `SORTNO` int(8) DEFAULT '0',
  `ICON` varchar(100) DEFAULT NULL,
  `ICON32` varchar(100) DEFAULT NULL,
  `NOTE` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`MODULEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE` smallint(1) NOT NULL COMMENT '1.站内信2.邮件3.短信',
  `SENT_USER` int(11) NOT NULL COMMENT '-1.系统发送',
  `RECIVE_USER` int(11) NOT NULL COMMENT '-1.群发',
  `MESSAGEID` int(11) NOT NULL,
  `STATUS` smallint(1) DEFAULT NULL COMMENT '1.未发送2.成功3.失败',
  `IS_READ` smallint(1) DEFAULT '2' COMMENT '1.是2.否',
  `SEND_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `SEND_TIME` (`SEND_TIME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_notice_message
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_message`;
CREATE TABLE `sys_notice_message` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TITLE` varchar(100) DEFAULT NULL,
  `CONTENT` varchar(1024) NOT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_notice_template
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice_template`;
CREATE TABLE `sys_notice_template` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MSG_TYPE` smallint(1) NOT NULL COMMENT '1.站内信2.邮件3.短信',
  `NAME` varchar(100) NOT NULL,
  `TEMPLATE` varchar(1024) NOT NULL,
  `STATUS` smallint(1) NOT NULL DEFAULT '1' COMMENT '1.启用2.停用',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_operation_log`;
CREATE TABLE `sys_operation_log` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_ID` int(11) NOT NULL,
  `TYPE` smallint(1) NOT NULL COMMENT '1权限日志,2系统访问,3档案维护,4资源访问 5拒绝访问 6 运行错误',
  `DESCRIBED` varchar(1024) DEFAULT NULL,
  `IP` varchar(50) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=1196 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLENAME` varchar(40) NOT NULL,
  `TYPE` smallint(1) NOT NULL COMMENT '1.系统平台2.企业运营3.个人运营',
  `STATUS` smallint(1) NOT NULL DEFAULT '1' COMMENT '1.激活2.禁用',
  `REMARK` varchar(256) DEFAULT NULL,
  `CREATOR` int(11) NOT NULL COMMENT '对应的userid',
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_rolemodule
-- ----------------------------
DROP TABLE IF EXISTS `sys_rolemodule`;
CREATE TABLE `sys_rolemodule` (
  `ROLEID` int(11) NOT NULL,
  `MODULEID` varchar(16) NOT NULL,
  PRIMARY KEY (`ROLEID`,`MODULEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_rule
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule`;
CREATE TABLE `sys_rule` (
  `ID` varchar(30) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `STATUS` smallint(1) NOT NULL DEFAULT '1' COMMENT '1.启用2.停用',
  `RULE_CHECK` varchar(1024) NOT NULL,
  `REMARK` varchar(250) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_rule_copy
-- ----------------------------
DROP TABLE IF EXISTS `sys_rule_copy`;
CREATE TABLE `sys_rule_copy` (
  `ID` varchar(30) NOT NULL,
  `NAME` varchar(50) NOT NULL,
  `STATUS` smallint(1) NOT NULL DEFAULT '1' COMMENT '1.启用2.停用',
  `RULE_CHECK` varchar(1024) NOT NULL,
  `REMARK` varchar(250) DEFAULT NULL,
  `ADD_TIME` datetime DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for sys_secret_key
-- ----------------------------
DROP TABLE IF EXISTS `sys_secret_key`;
CREATE TABLE `sys_secret_key` (
  `operatorID` varchar(9) NOT NULL COMMENT '运营商标识固定9位',
  `operatorSecret` varchar(500) NOT NULL COMMENT '运营商秘钥',
  `dataSecret` varchar(500) NOT NULL COMMENT '消息密钥',
  `dataSecretIV` varchar(16) NOT NULL COMMENT '消息密钥初始化向量，固定16位',
  `sigSecret` varchar(500) NOT NULL COMMENT '签名密钥',
  `time` datetime NOT NULL,
  PRIMARY KEY (`operatorID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Function structure for sayhello
-- ----------------------------
DROP FUNCTION IF EXISTS `sayhello`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` FUNCTION `sayhello`() RETURNS varchar(20) CHARSET utf8
begin
DECLARE temp VARCHAR(100);
set temp='1,222,3333';
select GROUP_CONCAT(ID) into temp from bus_user_info where FIND_IN_SET(ID,temp);
return temp;
end
;;
DELIMITER ;
