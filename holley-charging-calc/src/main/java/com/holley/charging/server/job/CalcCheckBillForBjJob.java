package com.holley.charging.server.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import com.holley.charging.dao.bus.BusPaymentMapper;
import com.holley.charging.dao.bus.BusRebateMapper;
import com.holley.charging.dao.bus.BusUserRebateMapper;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusPaymentExample;
import com.holley.charging.model.bus.BusRebate;
import com.holley.charging.model.bus.BusRebateExample;
import com.holley.charging.model.bus.BusUserRebate;
import com.holley.charging.model.bus.BusUserRebateExample;
import com.holley.common.constants.IsActiveEnum;
import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;

/**
 * 北京账户资金结算任务
 * 
 * @author sc
 */
public class CalcCheckBillForBjJob {

    private final static Logger logger = Logger.getLogger(CalcCheckBillForBjJob.class);
    @Resource
    private BusPaymentMapper    busPaymentMapper;
    @Resource
    private BusRebateMapper     busRebateMapper;
    @Resource
    private BusUserRebateMapper busUserRebateMapper;
    @Resource
    private CalcJobService      calcJobService;

    public void execute() throws InterruptedException {
        BusPaymentExample emp = new BusPaymentExample();
        BusPaymentExample.Criteria cr = emp.createCriteria();
        cr.andPayStatusEqualTo(ChargePayStatusEnum.UNPAID.getShortValue());
        cr.andPayWayEqualTo((short) 6);
        cr.andUserIdGreaterThan(0);
        cr.andDealStatusEqualTo(ChargeDealStatusEnum.SUCCESS.getShortValue());
        List<BusPayment> list = busPaymentMapper.selectByExample(emp);
        if (list != null && !list.isEmpty()) {
            logger.info("账户资金结算Start...");
            logger.info("账户资金结算任务数量：" + list.size());
            Map<Integer, BusRebate> map = returnRebateMap(list);
            for (BusPayment pay : list) {
                try {
                    calcJobService.updateAccountForAccountBj(pay, map.get(pay.getUserId()));
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }
            logger.info("账户资金结算end...");
        }
    }

    private Set<Integer> returnUserIdList(List<BusPayment> list) {
        Set<Integer> userList = new HashSet<Integer>();
        for (BusPayment payment : list) {
            userList.add(payment.getUserId());
        }
        return userList;
    }

    private Map<Integer, BusRebate> returnRebateMap(List<BusPayment> list) {
        Set<Integer> userIdList = returnUserIdList(list);
        Set<Integer> rebateIdList = new HashSet<Integer>();
        Date now = new Date();
        Map<Integer, BusRebate> map = new HashMap<Integer, BusRebate>();
        BusUserRebateExample userRebateEmp = new BusUserRebateExample();
        BusUserRebateExample.Criteria userRebateCr = userRebateEmp.createCriteria();
        userRebateCr.andUserIdIn(new ArrayList<Integer>(userIdList));
        userRebateCr.andStatusEqualTo(IsActiveEnum.ACTIVE.getShortValue());
        List<BusUserRebate> userRebateList = busUserRebateMapper.selectByExample(userRebateEmp);

        for (BusUserRebate userRebate : userRebateList) {
            rebateIdList.add(userRebate.getRebateId());
        }
        if (!rebateIdList.isEmpty()) {
            BusRebateExample rebateEmp = new BusRebateExample();
            BusRebateExample.Criteria rebateCr = rebateEmp.createCriteria();
            rebateCr.andIdIn(new ArrayList<Integer>(rebateIdList));
            rebateCr.andStartTimeLessThanOrEqualTo(now);
            rebateCr.andEndTimeGreaterThanOrEqualTo(now);

            List<BusRebate> rebateList = busRebateMapper.selectByExample(rebateEmp);
            for (BusUserRebate userRebate : userRebateList) {
                for (BusRebate rebate : rebateList) {
                    if (userRebate.getRebateId().equals(rebate.getId())) {
                        map.put(userRebate.getUserId(), rebate);
                        break;
                    }
                }
            }
        }
        return map;
    }
}
