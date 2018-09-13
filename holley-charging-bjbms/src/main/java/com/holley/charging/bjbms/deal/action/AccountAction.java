package com.holley.charging.bjbms.deal.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.common.enums.RechargeTypeEnum;
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
import com.holley.common.constants.charge.CashWayEnum;
import com.holley.common.constants.charge.PayWayEnum;
import com.holley.common.constants.charge.PersonUserTypeEnum;
import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
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
    private BusAccount          account;

    /**
     * 账户列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("userTypeList", UserTypeEnum.values());
        this.getRequest().setAttribute("statusList", AccountStatusEnum.values());
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userType", UserTypeEnum.COMPANY.getValue());
        param.put("isLock", WhetherEnum.NO.getValue());
        List<BusUser> companyList = userService.selectBusUserByMap(param);
        this.getRequest().setAttribute("companyList", companyList);// 子公司
        this.getRequest().setAttribute("personUserTypeList", PersonUserTypeEnum.values());// 个人用户类型
        return SUCCESS;
    }

    /**
     * 人工充值初始化
     * 
     * @return
     */
    public String manualRechargeInit() {
        String userid = this.getParameter("userid");
        int rechargeType = getParamInt("rechargeType");
        if (StringUtil.isNotNumber(userid)) {
            this.getRequest().setAttribute("msg", "参数非法");
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", Integer.valueOf(userid));
        UserRealIntro userReal = userService.selectUserRealIntro(params);
        BusAccount account = accountService.selectAccoutByPrimaryKey(Integer.valueOf(userid));
        this.getRequest().setAttribute("userInfo", userReal);
        this.getRequest().setAttribute("userTypeList", UserTypeEnum.values());
        this.getRequest().setAttribute("payWayList", PayWayEnum.values());
        this.getRequest().setAttribute("userType", UserTypeEnum.PERSON.getValue());
        this.getRequest().setAttribute("payWay", PayWayEnum.SYSTEM.getValue());
        this.getRequest().setAttribute("account", account);
        this.getRequest().setAttribute("rechargeType", rechargeType);
        this.getRequest().setAttribute("cashWayList", CashWayEnum.values());
        return SUCCESS;
    }

    /**
     * 查询账户列表
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        WebUser user = getBmsWebuser();

        String keyword = this.getParameter("keyword");
        // String usertype = this.getParameter("usertype");
        String accountstatus = this.getParameter("accountstatus");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        int groupId = getParamInt("groupId");
        int personUserType = getParamInt("personUserType");
        if (StringUtil.isNotNumber(accountstatus)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("personUserType", personUserType);
        if (UserTypeEnum.COMPANY == user.getUsertype()) {
            params.put("companyId", user.getUserId());
        }
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (groupId > 0) {
            params.put("groupId", groupId);
        }
        /*
         * if (StringUtil.isNotEmpty(usertype) && !"0".equals(usertype)) { params.put("usertype",
         * Short.valueOf(usertype)); }
         */
        if (StringUtil.isNotEmpty(accountstatus) && !"0".equals(accountstatus)) {
            params.put("status", Short.valueOf(accountstatus));
        }
        params.put("usertype", UserTypeEnum.PERSON.getValue());
        if (isExportExcel()) {
            List<UserAccount> list = accountService.selectBjAccountByPage(params);
            String[] headsName = { "用户编码", "用户姓名", "所属子公司", "手机号码", "用户类型", "总金额", "可用金额", "冻结金额", "状态", "更新时间" };
            String[] properiesName = { "userId", "realName", "company", "phone", "userTypeDesc", "totalMoneyDesc", "usableMoneyDesc", "freezeMoneyDesc", "statusDesc",
                    "updateTimeStr" };
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
            List<UserAccount> list = accountService.selectBjAccountByPage(params);
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
     * 查询个人账户信息
     * 
     * @return
     */
    public String queryUserAccount() {
        int userId = this.getParamInt("userId");
        if (userId > 0) {
            this.message = SUCCESS;
            BusAccount account = accountService.selectAccoutByPrimaryKey(userId);
            this.account = account;
        } else {
            this.message = "用户不存在！！";
        }

        return SUCCESS;
    }

    /**
     * 人工充值BJ
     * 
     * @return
     */
    public String doRechargeBj() {
        int userId = getParamInt("userId");
        double money = NumberUtils.toDouble(getParameter("money"));
        message = SUCCESS;
        if (userId <= 0) {
            message = "用户不存在！！";
            return SUCCESS;
        }
        if (money <= 0) {
            message = "充值金额不对！！";
            return SUCCESS;
        }
        BusRecharge record = new BusRecharge();
        String accountInfo = "[平台操作员编码：" + getSessionBmsUserId() + "]";
        record.setTradeNo(ChargingCacheUtil.getRechargeNo());
        record.setAccountInfo(accountInfo);
        record.setAddIp(getRemoteIP());
        record.setAddTime(new Date());
        record.setStatus(RechargeStatusEnum.SUCCESS.getShortValue());
        record.setUserId(userId);
        record.setMoney(new BigDecimal(money));
        record.setPayWay(PayWayEnum.SYSTEM.getShortValue());
        if (rechargeService.insertRechargeAndUpdateAccount(record) <= 0) {
            this.message = "人工充值失败，请重试！！";
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 人工充值保存
     * 
     * @return
     */
    public String manualRecharge() {
        WebUser webUser = getBmsWebuser();
        if (UserTypeEnum.PLATFORM != webUser.getUsertype()) {
            this.success = false;
            this.message = "非法操作！！";
            return SUCCESS;
        }
        String rechargeJson = this.getParameter("recharge");
        RechargeTypeEnum rechargeTypeEnum = RechargeTypeEnum.getEnmuByValue(getParamInt("rechargeType"));
        Map<String, Object> validMap = validParams(rechargeJson, rechargeTypeEnum);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            // 充值
            if (RechargeTypeEnum.RECHARGE == rechargeTypeEnum) {
                BusRecharge record = (BusRecharge) validMap.get("busRecharge");
                String accountInfo = "[平台操作员编码：" + getSessionBmsUserId() + "]";
                record.setTradeNo(ChargingCacheUtil.getRechargeNo());
                record.setAccountInfo(accountInfo);
                record.setAddIp(getRemoteIP());
                record.setAddTime(new Date());
                record.setStatus(RechargeStatusEnum.SUCCESS.getShortValue());
                record.setPayWay(PayWayEnum.SYSTEM.getShortValue());
                if (rechargeService.insertRechargeAndUpdateAccount(record) <= 0) {
                    this.success = false;
                    this.message = "人工充值失败";
                    return SUCCESS;
                } else {// 短信通知人工充值
                    this.success = true;
                    this.message = "充值成功！";
                }
            } else if (RechargeTypeEnum.CASH == rechargeTypeEnum) {
                BusCash busCash = (BusCash) validMap.get("busCash");
                busCash.setAddTime(new Date());
                busCash.setValidTime(busCash.getAddTime());
                busCash.setValidStatus(CashVerifyStatusEnum.PASSED.getShortValue());
                busCash.setCashStatus(CashStatusEnum.SUCCESS.getShortValue());
                if (rechargeService.insertRechargeAndUpdateAccount(busCash) <= 0) {
                    this.success = false;
                    this.message = "人工提现失败";
                    return SUCCESS;
                } else {// 短信通知人工充值
                    this.success = true;
                    this.message = "提现成功！";
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
    private Map<String, Object> validParams(String jsonObj, RechargeTypeEnum rechargeTypeEnum) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = Globals.DEFAULT_MESSAGE;
        if (RechargeTypeEnum.RECHARGE == rechargeTypeEnum) {
            BusRecharge busRecharge = this.JsonToBean(jsonObj, BusRecharge.class);
            BigDecimal money = busRecharge.getMoney();
            if (busRecharge.getUserId() == null || !validUserDB(busRecharge.getUserId())) {
                msg = "用户不存在.";
            } else if (money == null || !Validator.isMoney(money.toString())) {
                msg = "金额格式不正确.";
            }
            map.put("busRecharge", busRecharge);
        } else if (RechargeTypeEnum.CASH == rechargeTypeEnum) {
            BusCash busCash = this.JsonToBean(jsonObj, BusCash.class);
            BigDecimal money = busCash.getMoney();
            if (busCash.getUserId() == null || !validUserDB(busCash.getUserId())) {
                msg = "用户不存在.";
            } else if (money == null || !Validator.isMoney(money.toString())) {
                msg = "金额格式不正确.";
            }
            BusAccount account = accountService.selectAccoutByPrimaryKey(busCash.getUserId());
            if (money.compareTo(account.getUsableMoney()) > 0) {
                msg = "账户余额不足.";
            }
            map.put("busCash", busCash);
        } else {
            msg = "非法操作.";
        }

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

    public BusAccount getAccount() {
        return account;
    }

}
