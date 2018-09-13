package com.holley.charging.bjbms.person.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bms.UserReceipt;
import com.holley.charging.model.bus.BusBills;
import com.holley.charging.model.bus.BusBillsExample;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserReceipt;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.charge.BillStatusEnum;
import com.holley.common.constants.charge.NoticeTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.LogUtil;
import com.holley.platform.util.MsgUtil;

import net.sf.json.JSONObject;

public class UserReceiptAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(UserReceiptAction.class);
    private static final long   serialVersionUID = 1L;
    private AccountService      accountService;
    private UserService         userService;
    private Page                page;

    /**
     * 开票列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("statusList", BillStatusEnum.values());
        return SUCCESS;
    }

    /**
     * 开票审核初始化
     * 
     * @return
     */
    public String verifyInit() {
        String id = getParameter("id");
        String requestype = getParameter("requestype");
        if (!NumberUtils.isNumber(id) && StringUtil.isEmpty(requestype)) {
            this.getRequest().setAttribute("msg", "参数非法");
            return "msg";
        }
        UserReceipt record = accountService.selectUserReceiptById(Integer.valueOf(id));
        List<BusBills> billsList = null;
        BigDecimal total = BigDecimal.ZERO;
        if (StringUtil.isNotEmpty(record.getTime())) {
            String[] timeArray = record.getTime().split(",");
            List<String> timeList = new ArrayList<String>();
            for (String time : timeArray) {
                timeList.add(time);
            }
            BusBillsExample emp = new BusBillsExample();
            BusBillsExample.Criteria cr = emp.createCriteria();
            cr.andUserIdEqualTo(record.getUserId());
            cr.andCheckCycleIn(timeList);
            cr.andReceiptIdEqualTo(record.getId());
            billsList = accountService.selectBusBillsByExample(emp);
            if (billsList != null && billsList.size() > 1) {
                BigDecimal tempvalue;
                for (BusBills item : billsList) {
                    tempvalue = new BigDecimal(item.getTotalFeeOutDesc());
                    total = total.add(tempvalue);
                }
            }
        }
        this.getRequest().setAttribute("userReceipt", record);
        this.getRequest().setAttribute("billsList", billsList);
        this.getRequest().setAttribute("totalFee", NumberUtil.formateScale2Str(total));
        this.getRequest().setAttribute("statusList", BillStatusEnum.getVerifyResultList());
        this.getRequest().setAttribute("noticeTypeList", NoticeTypeEnum.getMessageAndSms());
        this.getRequest().setAttribute("requestType", requestype);
        return SUCCESS;
    }

    /**
     * 分页查询用户开票记录
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String status = this.getParameter("status");
        String keyword = this.getParameter("keyword");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(status)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startdate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("enddate", DateUtil.ShortStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(status) && !status.equals("0")) {
            params.put("status", Short.valueOf(status));
        }
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", keyword);
        }

        if (isExportExcel()) {
            List<UserReceipt> list = accountService.selectUserReceiptByPage(params);
            String[] headsName = { "开票编码", "开票状态", "开票月份", "开票金额", "发票类型", "发票抬头", "收件人", "收件人联系方式", "收件地址", "申请人编码", "申请人昵称", "申请人手机号码", "审核备注", "快递公司", "快递单号" };
            String[] properiesName = { "id", "statusDesc", "time", "moneyDesc", "billTypeDesc", "billHead", "recipient", "phone", "address", "userId", "username", "userphone",
                                       "validRemark", "expressCompany", "expressNum" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, UserReceipt.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);
            List<UserReceipt> list = accountService.selectUserReceiptByPage(params);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        }

    }

    public String receiptVerify() {
        String id = this.getParameter("id");
        String validstatus = this.getParameter("validstatus");
        String expresscompany = this.getParameter("expresscompany");
        String expressnum = this.getParameter("expressnum");
        String validremark = this.getParameter("validremark");
        String noticetypes = this.getParameter("noticetypes");
        if (StringUtil.isNotNumber(id, validstatus)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        BusUserReceipt record = accountService.selectUserReceiptByPK(Integer.valueOf(id));
        if (record == null) {
            this.success = false;
            this.message = "开票申请不存在！";
            return SUCCESS;
        }
        if (!BillStatusEnum.VERIFYING.getShortValue().equals(record.getStatus())) {
            this.success = false;
            this.message = "开票申请已审核,不能重复审核！";
            return SUCCESS;
        }
        BillStatusEnum statusEnum = BillStatusEnum.getEnmuByValue(Integer.parseInt(validstatus));
        if (BillStatusEnum.SUCCESS != statusEnum && BillStatusEnum.FAILURE != statusEnum) {
            this.success = false;
            this.message = "开票申请的审核状态不不符合！";
            return SUCCESS;
        }
        record.setStatus(statusEnum.getShortValue());
        if (BillStatusEnum.SUCCESS == statusEnum) {
            if (StringUtil.isNotEmpty(expresscompany) && StringUtil.isNotEmpty(expressnum)) {
                record.setExpressCompany(StringUtil.trim(expresscompany));
                record.setExpressNum(StringUtil.trim(expressnum));
            } else {
                this.success = false;
                this.message = "请填写寄出的快递公司和单号！";
                return SUCCESS;
            }
        } else {
            if (StringUtil.isNotEmpty(validremark)) {
                record.setValidRemark(validremark);
            } else {
                this.success = false;
                this.message = "请填写审核失败原因！";
                return SUCCESS;
            }
        }
        if (accountService.updateUserReceiptStatus(record) > 0) {
            // 插入操作日志
            String webuserid = getSessionBmsUserId();
            Integer userid = NumberUtils.isNumber(webuserid) ? Integer.valueOf(webuserid) : 0;
            String content = JSONObject.fromObject(record, JsonUtil.returnJosnConfig(true, Date.class)).toString();
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.MODIFY, getRemoteIP(), "个人开票审核", content, true);

            // 发送审核结果通知
            sendValidResult(record, statusEnum, noticetypes);

        }
        return SUCCESS;
    }

    /**
     * 发送审核结果通知
     * 
     * @param record
     * @param statusenum
     * @param noticetypes<br>
     * 短信模板内容:尊敬的用户：您好！${content}审核${result}，请注意查看。
     */
    private void sendValidResult(BusUserReceipt record, BillStatusEnum statusenum, String noticetypes) {
        if (StringUtil.isEmpty(noticetypes)) {
            return;
        }
        String[] noticetypeArray = noticetypes.split(",");
        NoticeTypeEnum typeenum;
        String result = "";
        if (BillStatusEnum.SUCCESS == statusenum) {
            result = "通过(" + record.getExpressCompany() + "单号：" + record.getExpressNum() + ")";
        } else {
            String validRemark = record.getValidRemark();
            result = "不通过" + (StringUtil.isEmpty(validRemark) ? "" : "(原因：" + validRemark + ")");
        }
        for (String type : noticetypeArray) {
            typeenum = NoticeTypeEnum.getEnmuByValue(Integer.parseInt(type));
            if (NoticeTypeEnum.MESSAGE == typeenum) {// 站内信发送
                String content = "尊敬的用户：您好！开票申请审核" + result + "，请注意查看。";
                MsgUtil.sendMessage(record.getUserId(), content, "开票审核");
            } else if (NoticeTypeEnum.SMS == typeenum) {// 短信发送
                BusUser busUser = userService.selectBusUserByPrimaryKey(record.getUserId());
                MsgUtil.sendVerifySMS(busUser.getPhone(), "开票申请", result);
                logger.info("-----【个人开票审核，短信通知】-----phone=" + busUser.getPhone() + ",content=" + result);
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
