package com.holley.charging.server.job;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Resource;

import com.holley.charging.dao.bus.BusAccountLogMapper;
import com.holley.charging.dao.bus.BusAccountMapper;
import com.holley.charging.dao.bus.BusPaymentMapper;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusAccountLog;
import com.holley.charging.model.bus.BusAccountLogExample;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusRebate;
import com.holley.common.constants.charge.AccountLogTypeEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.FundDirectionEnum;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;

public class CalcJobServiceImpl implements CalcJobService {

    @Resource
    private BusAccountMapper    busAccountMapper;
    @Resource
    private BusPaymentMapper    busPaymentMapper;
    @Resource
    private BusAccountLogMapper busAccountLogMapper;

    @Override
    public void updateAccountForAccountBj(BusPayment pay, BusRebate rebate) {
        synchronized (pay.getId()) {
            BusAccountLogExample alogEmp = new BusAccountLogExample();
            BusAccountLogExample.Criteria alogCr = alogEmp.createCriteria();
            alogCr.andTypeEqualTo(AccountLogTypeEnum.ACC_CHARGING.getShortValue());
            alogCr.andRecordIdEqualTo(pay.getId());
            int c = busAccountLogMapper.countByExample(alogEmp);
            if (c > 0) {
                return;
            }
            BusAccount userAccount = busAccountMapper.selectByPrimaryKey(pay.getUserId());// 资金账户
            if (userAccount != null) {
                BigDecimal shouldMoney = pay.getShouldMoney();
                String content = "";
                if (rebate != null) {
                    shouldMoney = returnRebateValue(shouldMoney, rebate.getRebate());
                    content = "优惠方案>>" + rebate.getRebateDesc() + ";优惠方案ID>>" + rebate.getId() + ";优惠前应付金额>>" + pay.getShouldMoney() + ";优惠后应付金额>>" + shouldMoney + ";优惠折扣>>"
                              + rebate.getRebate() + "折";
                }
                BusAccount account = new BusAccount();
                account.setTotalMoney(NumberUtil.sub(userAccount.getTotalMoney(), shouldMoney));
                account.setUsableMoney(NumberUtil.sub(userAccount.getUsableMoney(), shouldMoney));
                account.setFreezeMoney(userAccount.getFreezeMoney());
                account.setUserId(pay.getUserId());
                account.setUpdateTime(new Date());

                BusPayment payment = new BusPayment();
                payment.setId(pay.getId());
                payment.setPayStatus(ChargePayStatusEnum.SUCCESS.getShortValue());
                payment.setUpdateTime(new Date());
                busAccountMapper.updateByPrimaryKeySelective(account);
                busPaymentMapper.updateByPrimaryKeySelective(payment);
                busAccountLogMapper.insertSelective(createLogBean(account, pay.getId(), shouldMoney, null, AccountLogTypeEnum.ACC_CHARGING, FundDirectionEnum.OUT, content));
            }
        }
    }

    private BusAccountLog createLogBean(BusAccount account, Integer recordId, BigDecimal operateMoney, String ip, AccountLogTypeEnum logType, FundDirectionEnum direct,
                                        String content) {
        BusAccountLog logBean = new BusAccountLog();
        logBean.setOperateMoney(operateMoney);
        logBean.setTotalMoney(account.getTotalMoney());
        logBean.setUsableMoney(account.getUsableMoney());
        logBean.setFreezeMoney(account.getFreezeMoney());
        logBean.setUserId(account.getUserId());
        logBean.setRecordId(recordId);
        logBean.setType(logType.getShortValue());
        logBean.setRemark(logType.getText());
        if (StringUtil.isNotEmpty(content)) {
            logBean.setRemark(logBean.getRemark() + "：" + content);
        }
        logBean.setDirection(direct.getShortValue());
        logBean.setAddIp(ip);
        logBean.setAddTime(new Date());
        return logBean;
    }

    private BigDecimal returnRebateValue(BigDecimal money, BigDecimal rebate) {
        return NumberUtil.mul(money, NumberUtil.div(rebate, BigDecimal.TEN));
    }

}
