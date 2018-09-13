package com.holley.charging.bjbms.deal.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusCash;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.CashStatusEnum;
import com.holley.common.constants.charge.CashVerifyStatusEnum;
import com.holley.common.constants.charge.NoticeTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.MsgUtil;

/**
 * 提现审核相关ACTION
 * 
 * @author zdd
 */
public class CashTransferAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(CashTransferAction.class);
    private static final long   serialVersionUID = 1L;
    private AccountService      accountService;
    private UserService         userService;
    private Page                page;

    /**
     * 提现审核记录初始化
     * 
     * @return
     */
    public String init() {
        CashStatusEnum[] cashStatusList = CashStatusEnum.values();
        this.getRequest().setAttribute("cashStatusList", cashStatusList);
        this.getRequest().setAttribute("cashStatus", 0);
        this.getRequest().setAttribute("cashVerifyStatus", 0);
        return SUCCESS;
    }

    /**
     * 根据过滤条件查询符合的提现审核记录
     * 
     * @return
     * @throws Exception
     */
    public String queryCashList() throws Exception {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String cashstatus = this.getParameter("cashstatus");
        String userinfo = this.getParameter("userinfo");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(cashstatus)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("validStatus", CashVerifyStatusEnum.PASSED.getShortValue());// 审核通过的才允许转账
        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startDate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("endDate", DateUtil.ShortStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(cashstatus) && !cashstatus.equals("0")) {
            params.put("cashStatus", Short.valueOf(cashstatus));
        }
        if (StringUtil.isNotEmpty(userinfo)) {
            params.put("userInfo", userinfo);
        }
        if (isExportExcel()) {
            List<BusCash> cashList = accountService.selectCashVerifyByPage(params);
            String[] headsName = { "提现编码", "真实姓名", "用户昵称", "用户类型", "手机号码", "提现金额", "提现方式", "提现状态", "提现账户", "备注", "申请时间", "审核状态", "审核时间", "审核备注" };
            String[] properiesName = { "id", "realName", "userName", "userTypeDesc", "phone", "moneyDesc", "cashWayDesc", "cashStatusDesc", "accountInfo", "remark", "addTimeDesc",
                                       "validStatusDesc", "validTimeDesc", "validRemark" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(cashList, properiesName, headsName, BusCash.class);
            return null;
        } else {
            Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);
            List<BusCash> cashList = accountService.selectCashVerifyByPage(params);
            page.setRoot(cashList);
            this.page = page;
            return SUCCESS;
        }
    }

    /**
     * 转账初始化
     * 
     * @return
     */
    public String cashTransferInit() {
        String id = this.getParameter("id");
        String usertype = this.getParameter("usertype");
        if (StringUtil.isEmpty(id)) {
            this.success = false;
            this.message = "提现记录id不能为空.";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(usertype)) {
            this.success = false;
            this.message = "用户类型不能为空.";
            return SUCCESS;
        }

        BusCash busCash = accountService.selectCashInfoById(Integer.valueOf(id), Short.valueOf(usertype));
        if (busCash == null) return SUCCESS;
        setAccountInfo(busCash);
        this.getRequest().setAttribute("busCash", busCash);
        this.getRequest().setAttribute("cashStatusList", CashStatusEnum.getCashResultList());
        this.getRequest().setAttribute("noticeTypeList", NoticeTypeEnum.getMessageAndSms());
        return SUCCESS;
    }

    /**
     * 提现转账
     * 
     * @return
     */
    public String cashTransfer() {
        String id = this.getParameter("id");
        String validremark = this.getParameter("validremark");
        String cashstatus = this.getParameter("cashstatus");
        String noticetypes = this.getParameter("noticetypes");
        if (StringUtil.isEmpty(id) || StringUtil.isEmpty(cashstatus)) {
            this.success = false;
            this.message = "提现记录id和转账状态不能为空.";
            return SUCCESS;
        }
        BusCash busCash = accountService.selectCashByPK(Integer.valueOf(id));
        if (busCash == null) {
            this.success = false;
            this.message = "提现记录不存在.";
            return SUCCESS;
        }

        if (!CashStatusEnum.WITHDRAWING.getShortValue().equals(busCash.getCashStatus())) {
            this.message = "提现已转账，不能重复转账.";
            this.success = false;
            return SUCCESS;
        }

        CashStatusEnum statusEnum = CashStatusEnum.getEnmuByValue(Integer.valueOf(cashstatus));
        if (statusEnum == null) {
            this.success = false;
            this.message = "提现状态不符合.";
            return SUCCESS;
        }

        busCash.setCashStatus(statusEnum.getShortValue());

        if (CashStatusEnum.FAILURE == statusEnum) {
            if (StringUtil.isNotEmpty(validremark)) {
                busCash.setValidRemark(validremark);
            } else {
                this.success = false;
                this.message = "请填写审核失败原因！";
                return SUCCESS;
            }
        }

        String msg = accountService.updateCashStatusAndAccount(busCash);
        if (!Globals.DEFAULT_MESSAGE.equals(msg)) {
            this.success = false;
            this.message = msg;
            return SUCCESS;
        }
        // 提现成功，则通知用户提现结果
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {// && CashStatusEnum.SUCCESS == statusEnum
            sendTransferResult(busCash, noticetypes);
        }
        this.success = true;
        return SUCCESS;
    }

    /**
     * 审核提现详细初始化
     * 
     * @return
     */
    public String cashTransferDetailInit() {
        String id = this.getParameter("id");
        String usertype = this.getParameter("usertype");
        if (StringUtil.isEmpty(id)) {
            this.success = false;
            this.message = "提现记录id不能为空.";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(usertype)) {
            this.success = false;
            this.message = "用户类型不能为空.";
            return SUCCESS;
        }
        BusCash busCash = accountService.selectCashInfoById(Integer.valueOf(id), Short.valueOf(usertype));
        if (busCash == null) return SUCCESS;
        setAccountInfo(busCash);
        this.getRequest().setAttribute("busCash", busCash);
        return SUCCESS;
    }

    /**
     * 解析提现账户信息
     * 
     * @param record
     */
    private void setAccountInfo(BusCash record) {
        String info = record.getAccountInfo();
        if (StringUtil.isNotEmpty(info)) {
            String[] infoArray = info.split(",");
            if (infoArray.length == 3) {
                record.setAccRealName(infoArray[0]);
                record.setBankName(infoArray[1]);
                record.setAccount(infoArray[2]);
            } else if (infoArray.length == 1) {
                record.setAccount(infoArray[0]);
            }
        }
    }

    /**
     * 发送审核结果(短信和站内信)
     */
    private void sendTransferResult(BusCash record, String noticetypes) {
        // if (!CashStatusEnum.SUCCESS.getShortValue().equals(record.getCashStatus())) {
        // return;
        // }
        if (StringUtil.isEmpty(noticetypes)) {
            return;
        }
        String[] noticetypeArray = noticetypes.split(",");
        NoticeTypeEnum typeenum;
        String result = "";
        if (CashStatusEnum.SUCCESS.getShortValue().equals(record.getCashStatus())) {
            result = "通过，账户冻结资金支出" + NumberUtil.formateScale2Str(record.getMoney()) + "元";
        } else {
            if (StringUtil.isNotEmpty(record.getValidRemark())) {
                result = "不通过（原因：" + record.getValidRemark() + "）";
            } else {
                result = "不通过";
            }
            result += "，账户冻结资金解冻" + NumberUtil.formateScale2Str(record.getMoney()) + "元";
        }

        for (String type : noticetypeArray) {
            typeenum = NoticeTypeEnum.getEnmuByValue(Integer.parseInt(type));
            if (NoticeTypeEnum.MESSAGE == typeenum) {// 站内信发送
                String content = "尊敬的用户：您好！提现审核" + result + "，请注意查看。";
                MsgUtil.sendMessage(record.getUserId(), content, "提现审核");
            } else if (NoticeTypeEnum.SMS == typeenum) {// 短信发送
                BusUser user = userService.selectUserByPrimaryKey(record.getUserId());
                if (user == null) continue;
                MsgUtil.sendVerifySMS(user.getPhone(), "提现", result);
                logger.info("-----【提现审核，短信通知】-----phone=" + user.getPhone() + ",content=" + result);
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

    public Page getPage() {
        return page;
    }

}
