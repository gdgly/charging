package com.holley.charging.bjbms.business.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
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
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.LogUtil;
import com.holley.platform.util.MsgUtil;

import net.sf.json.JSONObject;

/**
 * 运营商实名转账审核
 * 
 * @author zdd
 */
public class BusRealTransferAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(BusRealTransferAction.class);
    private static final long   serialVersionUID = 1L;
    private AccountService      accountService;
    private UserService         userService;

    private Page                page;

    /**
     * 实名认证记录列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("statusList", getStatusList());
        this.getRequest().setAttribute("verifyStatus", 0);
        return SUCCESS;
    }

    /**
     * 实名转账初始化
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
        this.getRequest().setAttribute("businessReal", busReal);
        this.getRequest().setAttribute("verifyStatusList", BusRealVerifyStatusEnum.getRealTransferResultList());
        this.getRequest().setAttribute("noticeTypeList", NoticeTypeEnum.getMessageAndSms());
        return SUCCESS;
    }

    /**
     * 实名转账详细信息
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

        Map<String, Object> params = new HashMap<String, Object>();

        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startDate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("endDate", DateUtil.ShortStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(verifystatus) && !verifystatus.equals("0")) {
            params.put("validStatus", Short.valueOf(verifystatus));
        } else {
            params.put("validStatusList", getStatusList());
        }
        if (StringUtil.isNotEmpty(busname)) {
            params.put("busName", busname);
        }

        int pageIndex = 1;
        int pageLimit = Globals.PAGE_LIMIT;
        if (StringUtil.isNotEmpty(pageindex)) {
            pageIndex = Integer.valueOf(pageindex);
        }
        if (StringUtil.isNotEmpty(pagelimit)) {
            pageLimit = Integer.valueOf(pagelimit);
        }
        Page page = this.returnPage(pageIndex, pageLimit);
        params.put(Globals.PAGE, page);

        List<BusBussinessReal> list = accountService.selectBusinessRealByPage(params);
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 转账审核
     * 
     * @return
     */
    public String realVerify() {
        String businfoid = this.getParameter("businfoid");
        String addtimestr = this.getParameter("addtimestr");
        String validstatus = this.getParameter("validstatus");
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

        if (busReal.getValidStatus() != null && busReal.getValidStatus().intValue() != BusRealVerifyStatusEnum.TRANSFERING.getValue()) {
            this.message = "企业实名信息已审核，不能重复审核.";
            this.success = false;
            return SUCCESS;
        }

        BusBussinessReal record = new BusBussinessReal();
        record.setBusInfoId(busInfoId);
        record.setAddTime(addTime);
        record.setValidStatus(statusEnum.getShortValue());
        if (StringUtil.isNotEmpty(remark)) {
            record.setRemark(remark);
        }
        if (accountService.updateBusinessRealByPrimaryKeySelective(record) > 0) {
            // 记录操作日志
            String userId = getSessionBmsUserId();
            String content = JSONObject.fromObject(record, JsonUtil.returnJosnConfig(true, Date.class)).toString();
            LogUtil.recordDocumentlog(Integer.valueOf(userId), LogOperatorEnum.MODIFY, getRemoteIP(), "运营商实名转账", content, false);
            // 通知用户审核结果
            sendVerifyResult(record, noticetypes);
        }

        return SUCCESS;
    }

    /**
     * 发送审核结果(短信和站内信)
     */
    private void sendVerifyResult(BusBussinessReal record, String noticetypes) {
        if (StringUtil.isEmpty(noticetypes)) {
            return;
        }

        String[] noticetypeArray = noticetypes.split(",");
        NoticeTypeEnum typeenum;
        String result = "";

        if (BusRealVerifyStatusEnum.VERIFY_FAILED.getShortValue().equals(record.getValidStatus())) {
            if (StringUtil.isNotEmpty(record.getRemark())) {
                result = "不通过（原因：" + record.getRemark() + "）";
            } else {
                result = "不通过";
            }
        } else if (BusRealVerifyStatusEnum.UNCHECKED.getShortValue().equals(record.getValidStatus())) {
            result = "等待您验证转账校验码，请尽快到51充电运营商管理系统进行处理";
        } else {
            return;
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
                logger.info("-----【运营商实名转账，短信通知】-----phone=" + busUser.getPhone() + ",content=" + result);
            }

        }
    }

    private List<BusRealVerifyStatusEnum> getStatusList() {
        List<BusRealVerifyStatusEnum> statusList = new ArrayList<BusRealVerifyStatusEnum>();
        statusList.add(BusRealVerifyStatusEnum.TRANSFERING);
        statusList.add(BusRealVerifyStatusEnum.UNCHECKED);
        statusList.add(BusRealVerifyStatusEnum.VERIFY_FAILED);
        return statusList;
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
