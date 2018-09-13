package com.holley.charging.job;

import java.util.List;

import org.apache.log4j.Logger;

import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusPaymentExample;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.common.constants.charge.ChargePayStatusEnum;

/**
 * 北京账户资金结算任务
 * 
 * @author sc
 */
public class CheckBillForBjJob {

    private final static Logger logger = Logger.getLogger(CheckBillForBjJob.class);
    private ChargingService     chargingService;
    private AccountService      accountService;

    public void execute() throws InterruptedException {
        BusPaymentExample emp = new BusPaymentExample();
        BusPaymentExample.Criteria cr = emp.createCriteria();
        cr.andPayStatusEqualTo(ChargePayStatusEnum.UNPAID.getShortValue());
        cr.andPayWayEqualTo((short) 6);
        cr.andUserIdGreaterThan(0);
        List<BusPayment> list = chargingService.selectChargePaymentByExample(emp);
        if (list != null) {
            logger.info("账户资金结算Start...");
            logger.info("账户资金结算任务数量：" + list.size());
            for (BusPayment pay : list) {
                try {
                    accountService.updateAccountForAccountBj(pay);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            logger.info("账户资金结算end...");
        }
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

}
