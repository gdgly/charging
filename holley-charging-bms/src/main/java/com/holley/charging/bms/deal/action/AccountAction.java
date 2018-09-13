package com.holley.charging.bms.deal.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bms.UserAccount;
import com.holley.charging.model.bms.UserRealIntro;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusCash;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.bus.BusRecharge;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.RechargeService;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.AccountStatusEnum;
import com.holley.common.constants.charge.CashStatusEnum;
import com.holley.common.constants.charge.CashVerifyStatusEnum;
import com.holley.common.constants.charge.PayWayEnum;
import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.MsgUtil;
import com.holley.web.common.util.Validator;

/**
 * 账户（钱包）查询相关ACTION
 * 
 * @author zdd
 */
public class AccountAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(AccountAction.class);
    private static final long   serialVersionUID = 1L;
    private AccountService      accountService;
    private RechargeService     rechargeService;
    private UserService         userService;
    private Page                page;
    private String              freezetotal;
    private UserRealIntro       userReal;

    /**
     * 账户列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("userTypeList", UserTypeEnum.values());
        this.getRequest().setAttribute("statusList", AccountStatusEnum.values());
        return SUCCESS;
    }

    /**
     * 人工充值初始化
     * 
     * @return
     */
    public String manualRechargeInit() {
        String userid = this.getParameter("userid");
        if (StringUtil.isNotNumber(userid)) {
            this.getRequest().setAttribute("msg", "参数非法");
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", Integer.valueOf(userid));
        UserRealIntro userReal = userService.selectUserRealIntro(params);
        this.getRequest().setAttribute("userInfo", userReal);
        this.getRequest().setAttribute("userTypeList", UserTypeEnum.values());
        this.getRequest().setAttribute("payWayList", PayWayEnum.values());
        this.getRequest().setAttribute("userType", UserTypeEnum.PERSON.getValue());
        this.getRequest().setAttribute("payWay", PayWayEnum.CACH.getValue());
        return SUCCESS;
    }

    /**
     * 查询账户列表
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        String keyword = this.getParameter("keyword");
        String usertype = this.getParameter("usertype");
        String accountstatus = this.getParameter("accountstatus");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(usertype, accountstatus)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isNotEmpty(usertype) && !"0".equals(usertype)) {
            params.put("usertype", Short.valueOf(usertype));
        }
        if (StringUtil.isNotEmpty(accountstatus) && !"0".equals(accountstatus)) {
            params.put("status", Short.valueOf(accountstatus));
        }
        if (isExportExcel()) {
            List<UserAccount> list = accountService.selectAccountByPage(params);
            String[] headsName = { "用户编码", "用户昵称", "手机号码", "用户类型", "总金额", "可用金额", "冻结金额", "状态", "更新时间" };
            String[] properiesName = { "userId", "username", "phone", "userTypeDesc", "totalMoneyDesc", "usableMoneyDesc", "freezeMoneyDesc", "statusDesc", "updateTimeStr" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, BusPileModel.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数格式不正确.";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<UserAccount> list = accountService.selectAccountByPage(params);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        }
    }

    /**
     * 获取充值用户信息
     * 
     * @return
     */
    public String queryRechargeUser() {
        String userid = this.getParameter("userid");
        if (StringUtil.isEmpty(userid)) {
            this.success = false;
            this.message = "用户编码不能为空!";
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", Integer.valueOf(userid));
        UserRealIntro userReal = userService.selectUserRealIntro(params);
        if (userReal == null) {
            this.success = false;
            this.message = "用户不存在!";
            return SUCCESS;
        }
        this.userReal = userReal;
        return SUCCESS;
    }

    /**
     * 人工充值保存
     * 
     * @return
     */
    public String manualRecharge() {
        String rechargeJson = this.getParameter("recharge");
        Map<String, Object> validMap = validParams(rechargeJson);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            BusRecharge record = (BusRecharge) validMap.get("busRecharge");
            if (!validUserDB(record.getUserId())) {
                return SUCCESS;
            }
            String accountInfo = "[平台操作员编码：" + getSessionBmsUserId() + "]";
            record.setTradeNo(ChargingCacheUtil.getRechargeNo());
            record.setAccountInfo(accountInfo);
            record.setAddIp(getRemoteIP());
            record.setAddTime(new Date());
            record.setStatus(RechargeStatusEnum.SUCCESS.getShortValue());
            if (rechargeService.insertRechargeAndUpdateAccount(record) <= 0) {
                this.success = false;
                this.message = "人工充值失败";
                return SUCCESS;
            } else {// 短信通知人工充值
                try {
                    sendRechargeResult(record);
                } catch (Exception e) {
                    this.success = true;
                    this.message = "已充值成功，但短信通知发送失败！";
                }
            }
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 查询冻结资金明细
     * 
     * @return
     */
    public String queryFreezeList() {
        String userid = this.getParameter("userid");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isNotNumber(userid, pageindex, pagelimit)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
        params.put("userid", userid);
        params.put(Globals.PAGE, page);
        List<BusCash> list = accountService.selectCashFreezeMoneyByPage(params);
        if (list == null || list.size() == 0) {
            this.page = page;
            return SUCCESS;
        }
        for (BusCash record : list) {
            if (record.getValidStatus() != null) {
                if (CashVerifyStatusEnum.PASSED.getShortValue().equals(record.getValidStatus()) && CashStatusEnum.WITHDRAWING.getShortValue().equals(record.getCashStatus())) {
                    record.setValidStatusDesc(CashStatusEnum.WITHDRAWING.getText());// 提现中
                } else {
                    record.setValidStatusDesc(CashVerifyStatusEnum.getText(record.getValidStatus().intValue()));
                }
            }
        }
        page.setRoot(list);
        this.page = page;
        BigDecimal totalmoney = accountService.selectCashFreezeMoneyTotal(params);
        this.freezetotal = NumberUtil.formateScale2Str(totalmoney);
        return SUCCESS;
    }

    /**
     * 校验用户信息
     * 
     * @param jsonObj
     * @return
     */
    private Map<String, Object> validParams(String jsonObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = Globals.DEFAULT_MESSAGE;
        BusRecharge busRecharge = this.JsonToBean(jsonObj, BusRecharge.class);
        BigDecimal money = busRecharge.getMoney();
        if (busRecharge.getUserId() == null) {
            msg = "充值用户不存在.";
        } else if (money == null || !Validator.isMoney(money.toString())) {
            msg = "金额格式不正确.";
        }
        map.put("busRecharge", busRecharge);
        map.put("msg", msg);
        return map;
    }

    /**
     * 校验数据库用户信息
     */
    private boolean validUserDB(Integer userid) {
        // 检验用户昵称是否重复
        BusUser user = userService.selectBusUserByPrimaryKey(userid);
        if (user == null) {
            this.success = false;
            this.message = "充值用户不存在.";
            return false;
        }
        return true;
    }

    private void sendRechargeResult(BusRecharge record) {
        if (record == null || record.getUserId() == null || record.getMoney() == null) return;
        BusUser user = userService.selectBusUserByPrimaryKey(record.getUserId());
        if (user == null) return;
        BusAccount account = accountService.selectAccoutByPrimaryKey(record.getUserId());
        if (account == null || account.getUsableMoney() == null) return;
        String addtime = DateUtil.DateToStr(record.getAddTime(), DateUtil.TIME_LONG_CN);
        String content = addtime + "由后台人工充值" + NumberUtil.formateScale2Str(record.getMoney()) + "元";
        MsgUtil.sendAccountSMS(user.getPhone(), content, NumberUtil.formateScale2Str(account.getUsableMoney()) + "元");
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setRechargeService(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Page getPage() {
        return page;
    }

    public UserRealIntro getUserReal() {
        return userReal;
    }

    public String getFreezetotal() {
        return freezetotal;
    }

}
