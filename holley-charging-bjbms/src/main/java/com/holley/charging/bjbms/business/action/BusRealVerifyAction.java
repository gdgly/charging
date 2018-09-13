package com.holley.charging.bjbms.business.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.job.BankTransferJob;
import com.holley.charging.model.bus.BusBussinessReal;
import com.holley.charging.model.bus.BusBussinessRealKey;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.charge.BusRealVerifyStatusEnum;
import com.holley.common.constants.charge.NoticeTypeEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.pool.ThreadPoolUtil;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.LogUtil;
import com.holley.platform.util.MsgUtil;

import net.sf.json.JSONObject;

/**
 * 运营商实名审核
 * 
 * @author zdd
 */
public class BusRealVerifyAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(BusRealVerifyAction.class);
    private static final long   serialVersionUID = 1L;
    private AccountService      accountService;
    private UserService         userService;

    private Page                page;

    /**
     * 企业实名认证记录列表初始化
     * 
     * @return
     */
    public String init() {
        BusRealVerifyStatusEnum[] busRealVerifyStatusList = BusRealVerifyStatusEnum.values();

        this.getRequest().setAttribute("busRealVerifyStatusList", busRealVerifyStatusList);
        this.getRequest().setAttribute("verifyStatus", 0);
        return SUCCESS;
    }

    /**
     * 某个企业实名审核初始化
     * 
     * @return
     */
    public String realInfoInit() {
        String busInfoId = this.getParameter("busInfoId");
        String addTimeStr = this.getParameter("addTimeStr");
        if (StringUtil.isEmpty(busInfoId)) {
            this.message = "企业信息Id为空.";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(addTimeStr)) {
            this.message = "提交时间为空.";
            return SUCCESS;
        }
        BusBussinessRealKey key = new BusBussinessRealKey();
        key.setBusInfoId(Integer.valueOf(busInfoId));
        key.setAddTime(DateUtil.LongStrToDate(addTimeStr));
        BusBussinessReal busReal = accountService.selectBusinessRealByPrimaryKey(key);
        // 转账金额0.01元
        busReal.setValidMoney(new BigDecimal("0.01"));
        // 产生校验码
        busReal.setValidCode(StringUtil.randomString(4));
        this.getRequest().setAttribute("businessReal", busReal);
        this.getRequest().setAttribute("verifyStatusList", BusRealVerifyStatusEnum.getRealVerifyResultList());
        this.getRequest().setAttribute("noticeTypeList", NoticeTypeEnum.getMessageAndSms());
        return SUCCESS;
    }

    /**
     * 查看企业实名认证详细信息
     * 
     * @return
     */
    public String detailInit() {
        String busInfoId = this.getParameter("busInfoId");
        String addTimeStr = this.getParameter("addTimeStr");
        if (StringUtil.isEmpty(busInfoId)) {
            this.message = "企业信息Id为空.";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(addTimeStr)) {
            this.message = "提交时间为空.";
            return SUCCESS;
        }
        BusBussinessRealKey key = new BusBussinessRealKey();
        key.setBusInfoId(Integer.valueOf(busInfoId));
        key.setAddTime(DateUtil.LongStrToDate(addTimeStr));
        BusBussinessReal busReal = accountService.selectBusinessRealByPrimaryKey(key);
        this.getRequest().setAttribute("businessReal", busReal);
        return SUCCESS;
    }

    /**
     * 查询企业实名审核记录
     * 
     * @return
     */
    public String queryRealVerifyList() {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String verifystatus = this.getParameter("verifystatus");
        String busname = this.getParameter("busname");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(verifystatus, pageindex, pagelimit)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startDate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("endDate", DateUtil.ShortStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(verifystatus) && !verifystatus.equals("0")) {
            params.put("validStatus", Short.valueOf(verifystatus));
        }
        if (StringUtil.isNotEmpty(busname)) {
            params.put("busName", busname);
        }
        Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
        params.put(Globals.PAGE, page);

        List<BusBussinessReal> list = accountService.selectBusinessRealByPage(params);
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 实名审核
     * 
     * @return
     * @throws Exception
     * @throws NumberFormatException
     */
    public String realVerify() throws NumberFormatException, Exception {
        String businfoid = this.getParameter("businfoid");
        String addtimestr = this.getParameter("addtimestr");
        String validstatus = this.getParameter("validstatus");
        String validmoney = this.getParameter("validmoney");
        String validcode = this.getParameter("validcode");
        String remark = this.getParameter("remark");
        String noticetypes = this.getParameter("noticetypes");

        if (StringUtil.isEmpty(businfoid)) {
            this.message = "企业信息Id不能为空.";
            this.success = false;
            return SUCCESS;
        }

        if (StringUtil.isEmpty(addtimestr)) {
            this.message = "提交时间不能为空.";
            this.success = false;
            return SUCCESS;
        }

        if (StringUtil.isEmpty(validstatus)) {
            this.message = "审核状态不能为空.";
            this.success = false;
            return SUCCESS;
        }

        BusRealVerifyStatusEnum statusEnum = BusRealVerifyStatusEnum.getEnmuByValue(Integer.valueOf(validstatus));
        if (statusEnum == null) {
            this.success = false;
            this.message = "审核状态不符合.";
            return SUCCESS;
        }

        Integer busInfoId = Integer.valueOf(businfoid);
        Date addTime = DateUtil.LongStrToDate(addtimestr);

        BusBussinessRealKey busRealKey = new BusBussinessRealKey();
        busRealKey.setBusInfoId(busInfoId);
        busRealKey.setAddTime(addTime);
        BusBussinessReal busReal = accountService.selectBusinessRealByPrimaryKey(busRealKey);
        if (busReal == null) {
            this.message = "该审核记录不存在.";
            this.success = false;
            return SUCCESS;
        }

        if (busReal.getValidStatus() != null && busReal.getValidStatus().intValue() != BusRealVerifyStatusEnum.UNVERIFIED.getValue()) {
            this.message = "企业实名信息已审核，不能重复审核.";
            this.success = false;
            return SUCCESS;
        }

        BusBussinessReal record = new BusBussinessReal();
        record.setBusInfoId(busInfoId);
        record.setAddTime(addTime);
        record.setValidStatus(statusEnum.getShortValue());
        if (BusRealVerifyStatusEnum.VERIFY_FAILED == statusEnum) {
            if (StringUtil.isNotEmpty(remark)) {
                record.setRemark(remark);
            } else {
                this.success = false;
                this.message = "请填写审核失败原因！";
                return SUCCESS;
            }
        } else if (BusRealVerifyStatusEnum.TRANSFERING == statusEnum) {
            if (StringUtil.isNotEmpty(validmoney)) {
                record.setValidMoney(new BigDecimal(validmoney));
            }
            if (StringUtil.isNotEmpty(validcode)) {
                record.setValidCode(validcode);
            }
        }
        if (accountService.updateBusinessRealByPrimaryKeySelective(record) > 0) {
            // 记录操作日志
            String userId = getSessionBmsUserId();
            String content = JSONObject.fromObject(record, JsonUtil.returnJosnConfig(true, Date.class)).toString();
            LogUtil.recordDocumentlog(Integer.valueOf(userId), LogOperatorEnum.MODIFY, getRemoteIP(), "运营商实名审核", content, false);

            // 审核未通过，则通知用户审核结果
            if (BusRealVerifyStatusEnum.VERIFY_FAILED == statusEnum) {
                sendVerifyResult(record, noticetypes);
            }
        }

        return SUCCESS;
    }

    /**
     * 线上打款审核
     * 
     * @return
     */
    public String verifyByOnlineBankTransfer() {
        String businfoid = this.getParameter("businfoid");
        String addtimestr = this.getParameter("addtimestr");
        String accrealname = this.getParameter("accrealname");
        String bankname = this.getParameter("bankname");
        String bankaccount = this.getParameter("bankaccount");
        String remark = this.getParameter("remark");
        if (StringUtil.isEmpty(businfoid)) {
            this.message = "企业信息Id不能为空.";
            this.success = false;
            return SUCCESS;
        }

        if (StringUtil.isEmpty(addtimestr)) {
            this.message = "提交时间不能为空.";
            this.success = false;
            return SUCCESS;
        }

        if (StringUtil.isEmpty(accrealname)) {
            this.message = "开户姓名不能为空.";
            this.success = false;
            return SUCCESS;
        }

        if (StringUtil.isEmpty(bankname)) {
            this.message = "开户银行不能为空.";
            this.success = false;
            return SUCCESS;
        }

        if (StringUtil.isEmpty(bankaccount)) {
            this.message = "开户账号不能为空.";
            this.success = false;
            return SUCCESS;
        }
        Integer busInfoId = Integer.valueOf(businfoid);
        Date addTime = DateUtil.LongStrToDate(addtimestr);

        BusBussinessRealKey busRealKey = new BusBussinessRealKey();
        busRealKey.setBusInfoId(busInfoId);
        busRealKey.setAddTime(addTime);
        BusBussinessReal busReal = accountService.selectBusinessRealByPrimaryKey(busRealKey);
        if (busReal == null) {
            this.message = "该审核记录不存在.";
            this.success = false;
            return SUCCESS;
        }

        if (busReal.getValidStatus() != null && busReal.getValidStatus().intValue() != BusRealVerifyStatusEnum.UNVERIFIED.getValue()) {
            this.message = "企业实名信息已审核，不能重复审核.";
            this.success = false;
            return SUCCESS;
        }
        busReal.setValidStatus(BusRealVerifyStatusEnum.TRANSFERING.getShortValue());
        if (StringUtil.isNotEmpty(remark)) {
            busReal.setRemark(remark);
        }
        int row = accountService.updateBusinessRealByPrimaryKeySelective(busReal);
        if (row > 0) {
            // 启动银行转账任务
            BankTransferJob btjob = new BankTransferJob(busInfoId, addTime, accrealname, bankname, bankaccount);
            btjob.setAccountService(accountService);
            ThreadPoolUtil.execute(btjob, "bankTransferJob");
        }
        this.success = true;
        return SUCCESS;
    }

    /**
     * 校验转账金额(0.00~0.99元)
     * 
     * @param money
     * @return 校验通过返回true，否则返回false
     */
    private boolean isValidMoney(String money) {
        if (money == null) return false;
        String reg = "^0\\.[1-9]{0,2}$";
        return Pattern.matches(reg, money);
    }

    /**
     * 发送审核结果(短信和站内信)
     */
    private void sendVerifyResult(BusBussinessReal record, String noticetypes) {
        if (!BusRealVerifyStatusEnum.VERIFY_FAILED.getShortValue().equals(record.getValidStatus())) {
            return;
        }
        if (StringUtil.isEmpty(noticetypes)) {
            return;
        }

        String[] noticetypeArray = noticetypes.split(",");
        NoticeTypeEnum typeenum;
        String result = "";
        if (StringUtil.isNotEmpty(record.getRemark())) {
            result = "不通过（原因：" + record.getRemark() + "）";
        } else {
            result = "不通过";
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("usertype", UserTypeEnum.ENTERPRISE.getShortValue());
        params.put("infoid", record.getBusInfoId());
        BusUser busUser = userService.selectUserByInfo(params);
        if (busUser == null) return;
        for (String type : noticetypeArray) {
            typeenum = NoticeTypeEnum.getEnmuByValue(Integer.parseInt(type));
            if (NoticeTypeEnum.MESSAGE == typeenum) {// 站内信发送
                String content = "尊敬的用户：您好！运营商实名审核" + result + "，请注意查看。";
                MsgUtil.sendMessage(busUser.getId(), content, "运营商实名审核");
            } else if (NoticeTypeEnum.SMS == typeenum) {// 短信发送
                MsgUtil.sendVerifySMS(busUser.getPhone(), "运营商实名", result);
                logger.info("-----【运营商实名审核，短信通知】-----phone=" + busUser.getPhone() + ",content=" + result);
            }

        }
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Page getPage() {
        return page;
    }

}
