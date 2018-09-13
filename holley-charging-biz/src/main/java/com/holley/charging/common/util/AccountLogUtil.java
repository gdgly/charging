package com.holley.charging.common.util;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusAccountLog;
import com.holley.charging.model.bus.BusChargeCard;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.common.constants.charge.AccountLogTypeEnum;
import com.holley.common.constants.charge.CashStatusEnum;
import com.holley.common.constants.charge.FundDirectionEnum;
import com.holley.common.util.NumberUtil;

/**
 * 资金日志
 * 
 * @author zdd
 */
public class AccountLogUtil {

    private static AccountService accountService;
    private static int            result = 0;

    public static int insertAccountLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, AccountLogTypeEnum logType, BigDecimal operateMoney, String ip, Short status) {
        int result = 0;
        switch (logType.getValue()) {
            case 1:// 充值
                result = rechargeAccountLog(fromAccount, recordId, operateMoney, ip);
                break;
            case 2:// 平台账户充电
                result = AccountChargeLog(fromAccount, toAccount, recordId, operateMoney, ip);
                break;
            case 3:// 非平台账户充电
                result = UnAccountChargeLog(fromAccount, toAccount, recordId, operateMoney, ip);
                break;
            case 4:// 平台账户预约
                result = AccountAppointmentLog(fromAccount, toAccount, recordId, operateMoney, ip);
                break;
            case 5:// 非平台账户预约
                result = UnAccountAppointmentLog(fromAccount, toAccount, recordId, operateMoney, ip);
                break;
            case 6:// 申请提现
                result = applyCashLog(fromAccount, recordId, operateMoney, ip);
                break;
            case 7:// 审核提现
                result = validCashLog(fromAccount, recordId, operateMoney, ip, status);
                break;
            case 8:// 资金结算
                result = accountBillLog(fromAccount, toAccount, recordId, operateMoney, ip);
                break;
            default:
                break;
        }
        return result;
    }

    public static int insertAccountLogForChargeCard(BusChargeCard chargeCard, Integer recordId, AccountLogTypeEnum logType, BigDecimal operateMoney, String ip) {
        int result = 0;
        switch (logType.getValue()) {
            case 9:// 充电卡充值
                result = cardRechargeAccountLog(chargeCard, recordId, operateMoney, ip);
                break;
            case 10:// 充电卡扣款
                result = cardCutMoneyAccountLog(chargeCard, recordId, operateMoney, ip);
                break;
            case 11:// 充电卡充电
                result = cardChargeAccountLog(chargeCard, recordId, operateMoney, ip);
                break;
            default:
                break;
        }
        return result;
    }

    /**
     * 充电卡充值日志
     */
    private static int cardRechargeAccountLog(BusChargeCard chargeCard, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        BusAccountLog fromLog = createLogBean(chargeCard, recordId, operateMoney, ip, AccountLogTypeEnum.CARD_RECHARGE, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(fromLog);
        return result;
    }

    /**
     * 充电卡扣款日志
     */
    private static int cardCutMoneyAccountLog(BusChargeCard chargeCard, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        BusAccountLog fromLog = createLogBean(chargeCard, recordId, operateMoney, ip, AccountLogTypeEnum.CARD_CUT_MONEY, FundDirectionEnum.OUT);
        result += accountService.insertAccountLogSelective(fromLog);
        return result;
    }

    /**
     * 充电卡充电扣款日志CARD_CHARGING
     */
    private static int cardChargeAccountLog(BusChargeCard chargeCard, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        BusAccountLog fromLog = createLogBean(chargeCard, recordId, operateMoney, ip, AccountLogTypeEnum.CARD_CHARGING, FundDirectionEnum.OUT);
        result += accountService.insertAccountLogSelective(fromLog);
        return result;
    }

    /**
     * 充值日志
     */
    private static int rechargeAccountLog(BusAccount fromAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 个人用户
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.RECHARGE, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(fromLog);
        return result;
    }

    /**
     * 平台账户充电日志
     */
    private static int AccountChargeLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 个人用户
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.ACC_CHARGING, FundDirectionEnum.OUT);
        result += accountService.insertAccountLogSelective(fromLog);
        // 平台
        if (toAccount != null) {
            BusAccountLog toLog = createLogBean(toAccount, recordId, operateMoney, ip, AccountLogTypeEnum.ACC_CHARGING, FundDirectionEnum.IN);
            result += accountService.insertAccountLogSelective(toLog);
        }

        return result;
    }

    /**
     * 非平台账户充电日志
     */
    private static int UnAccountChargeLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 个人用户
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.UNACC_CHARGING, FundDirectionEnum.NO);
        result += accountService.insertAccountLogSelective(fromLog);
        // 平台
        BusAccountLog toLog = createLogBean(toAccount, recordId, operateMoney, ip, AccountLogTypeEnum.UNACC_CHARGING, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(toLog);
        return result;
    }

    /**
     * 平台账户预约日志
     */
    private static int AccountAppointmentLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 个人用户
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.ACC_APPOINTMENT, FundDirectionEnum.OUT);
        result += accountService.insertAccountLogSelective(fromLog);
        // 平台
        BusAccountLog toLog = createLogBean(toAccount, recordId, operateMoney, ip, AccountLogTypeEnum.ACC_APPOINTMENT, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(toLog);
        return result;
    }

    /**
     * 非平台账户预约日志
     */
    private static int UnAccountAppointmentLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 个人用户
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.UNACC_APPOINTMENT, FundDirectionEnum.NO);
        result += accountService.insertAccountLogSelective(fromLog);
        // 平台
        BusAccountLog toLog = createLogBean(toAccount, recordId, operateMoney, ip, AccountLogTypeEnum.UNACC_APPOINTMENT, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(toLog);
        return result;
    }

    /**
     * 提现申请日志
     */
    private static int applyCashLog(BusAccount fromAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 运营商
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.APPLY_CASH, FundDirectionEnum.NO);
        result += accountService.insertAccountLogSelective(fromLog);
        return result;
    }

    /**
     * 提现审核日志
     */
    private static int validCashLog(BusAccount fromAccount, Integer recordId, BigDecimal operateMoney, String ip, Short status) {
        result = 0;
        // 运营商
        FundDirectionEnum directionEnum = null;
        if (status != null && CashStatusEnum.SUCCESS.getValue() == status.intValue()) {// 提现成功
            directionEnum = FundDirectionEnum.OUT;
        } else {// 提现失败
            directionEnum = FundDirectionEnum.NO;
        }
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.VALID_CASH, directionEnum);
        result += accountService.insertAccountLogSelective(fromLog);
        return result;
    }

    /**
     * 资金结算日志
     */
    private static int accountBillLog(BusAccount fromAccount, BusAccount toAccount, Integer recordId, BigDecimal operateMoney, String ip) {
        result = 0;
        // 平台
        BusAccountLog fromLog = createLogBean(fromAccount, recordId, operateMoney, ip, AccountLogTypeEnum.BILL, FundDirectionEnum.OUT);
        result += accountService.insertAccountLogSelective(fromLog);
        // 运营商
        BusAccountLog toLog = createLogBean(toAccount, recordId, operateMoney, ip, AccountLogTypeEnum.BILL, FundDirectionEnum.IN);
        result += accountService.insertAccountLogSelective(toLog);
        return result;
    }

    private static BusAccountLog createLogBean(BusAccount account, Integer recordId, BigDecimal operateMoney, String ip, AccountLogTypeEnum logType, FundDirectionEnum direct) {
        BusAccountLog logBean = new BusAccountLog();
        logBean.setOperateMoney(operateMoney);
        logBean.setTotalMoney(account.getTotalMoney());
        logBean.setUsableMoney(account.getUsableMoney());
        logBean.setFreezeMoney(account.getFreezeMoney());
        logBean.setUserId(account.getUserId());
        logBean.setRecordId(recordId);
        logBean.setType(logType.getShortValue());
        logBean.setRemark(logType.getText());
        logBean.setDirection(direct.getShortValue());
        logBean.setAddIp(ip);
        logBean.setAddTime(new Date());
        return logBean;
    }

    private static BusAccountLog createLogBean(BusChargeCard busChargeCard, Integer recordId, BigDecimal operateMoney, String ip, AccountLogTypeEnum logType,
                                               FundDirectionEnum direct) {
        BusAccountLog logBean = new BusAccountLog();
        logBean.setOperateMoney(operateMoney);
        logBean.setTotalMoney(NumberUtil.add(busChargeCard.getUsableMoney(), busChargeCard.getFreezeMoney()));
        logBean.setUsableMoney(busChargeCard.getUsableMoney());
        logBean.setFreezeMoney(busChargeCard.getFreezeMoney());
        logBean.setUserId(busChargeCard.getUserId());
        logBean.setRecordId(recordId);
        logBean.setType(logType.getShortValue());
        logBean.setRemark(logType.getText());
        logBean.setDirection(direct.getShortValue());
        logBean.setAddIp(ip);
        logBean.setAddTime(new Date());
        return logBean;
    }

    public void setAccountService(AccountService accountService) {
        AccountLogUtil.accountService = accountService;
    }

}
