package com.holley.charging.job;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.holley.charging.service.bussiness.AccountService;
import com.holley.common.constants.BillCycleTypeEnum;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.util.DateUtil;

/**
 * 定时生成账单
 * 
 * @author sc
 */
public class CheckBillJob {

    private final static Logger logger = Logger.getLogger(CheckBillJob.class);
    private AccountService      accountService;

    public void crateBussinessBill() {
        logger.info("进入运营商结算任务。。。。。。");
        Calendar calendar = Calendar.getInstance();
        // 每月的1号执行结算
        boolean isFirstDayOfMonth = DateUtil.isFirstDayOfMonth(calendar.getTime());
        if (isFirstDayOfMonth) {
            accountService.insertAndUpdateBussinessOrPersonBill(BillCycleTypeEnum.BYMONTH, RoleTypeEnum.ENTERPRISE);
        }
    }

    public void cratePersonBill() {
        logger.info("进入个人/运营商结算任务。。。。。。");
        Calendar calendar = Calendar.getInstance();
        // 每月的1号执行结算
        boolean isFirstDayOfMonth = DateUtil.isFirstDayOfMonth(calendar.getTime());
        if (isFirstDayOfMonth) {
            accountService.insertAndUpdateBussinessOrPersonBill(BillCycleTypeEnum.BYMONTH, RoleTypeEnum.PERSON);
        }
    }

    // SET

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

}
