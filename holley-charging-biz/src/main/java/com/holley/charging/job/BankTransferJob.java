package com.holley.charging.job;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Date;

import com.holley.charging.model.bus.BusBussinessReal;
import com.holley.charging.model.bus.BusBussinessRealKey;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.common.constants.charge.BusRealVerifyStatusEnum;
import com.holley.common.util.DateUtil;

/**
 * 银行转账任务
 * 
 * @author zdd
 */
public class BankTransferJob implements Runnable {

    private AccountService accountService;
    private String         jobName = "bankTransferJob";
    private Integer        busInfoId;
    private Date           addTime;
    private String         accRealName;
    private String         bankName;
    private String         bankAccount;

    public BankTransferJob(Integer busInfoId, Date addTime, String accRealName, String bankName, String bankAccount) {
        super();
        this.busInfoId = busInfoId;
        this.addTime = addTime;
        this.accRealName = accRealName;
        this.bankName = bankName;
        this.bankAccount = bankAccount;
    }

    @Override
    public void run() {
        System.out.println(jobName);
        System.out.println("企业信息ID:" + busInfoId);
        System.out.println("提交时间:" + DateUtil.DateToLongStr(addTime));
        System.out.println("开户姓名:" + accRealName);
        System.out.println("开户银行:" + bankName);
        System.out.println("开户账号:" + bankAccount);
        String result = "转账成功";
        // 待完善，调转账接口
        // 调转账接口，根据转账结果更新记录
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        String money = n.format(Math.random());
        System.out.println(money);
        BusBussinessRealKey busRealKey = new BusBussinessRealKey();
        busRealKey.setBusInfoId(busInfoId);
        busRealKey.setAddTime(addTime);
        BusBussinessReal busReal = accountService.selectBusinessRealByPrimaryKey(busRealKey);
        if (busReal == null) return;
        // 转账成功：审核状态改为待校验，记录转账金额，等待用户金额校验
        if (result.equals("转账成功")) {
            busReal.setValidStatus(BusRealVerifyStatusEnum.UNCHECKED.getShortValue());
            busReal.setValidMoney(new BigDecimal(money));
            busReal.setRemark(result);
        } else if (result.equals("银行账户不存在")) {
            busReal.setValidStatus(BusRealVerifyStatusEnum.VERIFY_FAILED.getShortValue());
            busReal.setRemark(result);
        } else {

        }
        accountService.updateBusinessRealByPrimaryKeySelective(busReal);
        // 调用短信接口，通知用户打款金额，尽快校验
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

}
