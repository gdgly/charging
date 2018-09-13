package com.holley.charging.user.action;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.app.BillInfo;
import com.holley.charging.model.app.StationIntro;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusFavorites;
import com.holley.charging.model.bus.BusGroupInfo;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.model.bus.BusUserReal;
import com.holley.charging.model.bus.BusUserRealExample;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.RedisUtil;
import com.holley.common.cache.charging.BussinessCacheUtil;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.EmailSubjectEnum;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.LogTypeEnum;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.CertificateStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.FundOperateTypeEnum;
import com.holley.common.constants.charge.ImgTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.NoticeTypeEnum;
import com.holley.common.constants.charge.RealVerifyStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.RandomUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.EmailModel;
import com.holley.platform.model.sys.SysCarBrand;
import com.holley.platform.model.sys.SysDefArea;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.model.sys.SysModuledef;
import com.holley.platform.model.sys.SysNotice;
import com.holley.platform.model.sys.SysNoticeMessage;
import com.holley.platform.service.MessageService;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.CachedModuledefUtil;
import com.holley.platform.util.LogUtil;
import com.holley.platform.util.MsgUtil;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.ImageUtil;
import com.holley.web.common.util.Validator;

/**
 * 个人用户
 * 
 * @author sc
 */
public class UserAction extends BaseAction {

    private final static Logger logger       = Logger.getLogger(UserAction.class);
    private UserService         userService;
    private AccountService      accountService;
    private DeviceService       deviceService;
    private List<SysCarBrand>   modelList;
    private File                pic;
    private Page                page;
    private Map<String, Object> result;
    private File                headImg;                                          // 头像
    private String              headImgUrl;                                       // 头像路径
    private static String       changePwd    = "changePwd";
    private static String       changePayPwd = "changePayPwd";
    private static String       changeEmail  = "changeEmail";
    private static String       changePhone  = "changePhone";
    private MessageService      messageService;
    private String              username;

    /**
     * 初始化
     * 
     * @return
     */
    public String init() {
        String tradeStatus = getParameter("tradeStatus");
        if (!StringUtil.isEmpty(tradeStatus)) {
            getRequest().setAttribute("tradeStatus", tradeStatus);
        }
        WebUser webUser = this.getWebuser();
        List<SysModuledef> modules = RoleUtil.selectModuledefByUserid(webUser.getUserId(), webUser.getRoleId());
        List<SysModuledef> topMenu = CachedModuledefUtil.getTopMenu(modules);
        List<SysModuledef> subMenu = CachedModuledefUtil.getSubMenu(modules);
        getRequest().setAttribute("topMenu", topMenu);
        getRequest().setAttribute("subMenu", subMenu);
        return SUCCESS;
    }

    /**
     * 集团管理员查看用户列表页
     * 
     * @return
     */
    public String initGroupUserManagement() {
        String maxGroupUserStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.MAX_GROUP_SUBUSER);
        int maxGroupUser = maxGroupUserStr == null ? Globals.DEFAULT_MAX_GROUP_SUBUSER : Integer.valueOf(maxGroupUserStr);
        String defaultPwd = RoleUtil.selectRuleByPrimaryKey(RoleUtil.DEFAULT_PWD);
        defaultPwd = defaultPwd == null ? Globals.DEFAULT_PASSWORD : defaultPwd;
        getRequest().setAttribute("maxGroupUser", maxGroupUser);
        getRequest().setAttribute("defaultPwd", defaultPwd);
        return SUCCESS;
    }

    /**
     * 集团管理员查看用户列表 ajax
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String groupUser() throws WriteException, IOException {
        WebUser webUser = getWebuser();
        Map<String, Object> params = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        String userName = this.getParameter("userName");
        int isExport = this.getParamInt("isExport");// 是否导出excel
        params.put("groupId", webUser.getUserId());

        if (IS_EXPORT != isExport) {
            params.put("keyword", userName);
            Page page = this.returnPage(currentPage, limit);
            params.put(Globals.PAGE, page);
            List<BusUser> list = userService.selectUserByPage(params);
            page.setRoot(list);
            this.page = page;
        } else {
            params.put("maxLimit", MAX_EXPORT);
            List<BusUser> exportList = userService.selectUserByPage(params);
            String[] headsName = { "用户名", "手机号码", "状态", "添加时间" };
            String[] properiesName = { "username", "phone", "isLockDesc", "registTimeStr" };

            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, BusUser.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 集团管理员添加子账户 ajax
     * 
     * @return
     */
    public String addGroupUser() {
        WebUser webUser = getWebuser();
        String groupUserPhone = getParameter("groupUserPhone");
        String groupUserName = getParameter("groupUserName");
        if (StringUtil.isEmpty(groupUserPhone) || !Validator.isMobile(groupUserPhone)) {
            this.message = "请正确填写11位手机号！！";
            return SUCCESS;
        } else if (StringUtil.isEmpty(groupUserName)) {
            this.message = "请填用户名！！";
            return SUCCESS;
        }
        // 注册验证
        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andPhoneEqualTo(groupUserPhone);
        cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        int userPhoneCount = userService.countBusUserByExample(emp);
        if (userPhoneCount > 0) {
            this.message = "手机号码已被注册！！";
            return SUCCESS;
        }
        BusUserExample emp2 = new BusUserExample();
        BusUserExample.Criteria cr2 = emp2.createCriteria();
        cr2.andUsernameEqualTo(groupUserName);
        int userUserNameCount = userService.countBusUserByExample(emp2);
        if (userUserNameCount > 0) {
            this.message = "用户名已被注册！！";
            return SUCCESS;
        }
        BusUserExample emp3 = new BusUserExample();
        BusUserExample.Criteria cr3 = emp3.createCriteria();
        cr3.andGroupIdEqualTo(webUser.getUserId());
        int groupUserCount = userService.countBusUserByExample(emp3);
        String maxGroupUserStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.MAX_GROUP_SUBUSER);
        int maxGroupUser = maxGroupUserStr == null ? Globals.DEFAULT_MAX_GROUP_SUBUSER : Integer.valueOf(maxGroupUserStr);
        if (groupUserCount >= maxGroupUser) {
            this.message = "添加的子账户不能超过" + maxGroupUser + "个！！";
            return SUCCESS;
        }
        // 用户
        String defaultPwd = RoleUtil.selectRuleByPrimaryKey(RoleUtil.DEFAULT_PWD);
        defaultPwd = defaultPwd == null ? Globals.DEFAULT_PASSWORD : defaultPwd;
        String pwd = SecurityUtil.passwordEncrypt(defaultPwd);
        BusUser user = new BusUser();
        user.setUsername(groupUserName);
        user.setPassword(pwd);
        user.setPayPassword(pwd);
        user.setPhone(groupUserPhone);
        user.setPhoneStatus(CertificateStatusEnum.PASSED.getShortValue());
        user.setEmailStatus(CertificateStatusEnum.FAILED.getShortValue());
        user.setUserType(UserTypeEnum.PERSON.getShortValue());
        user.setRegistTime(new Date());
        user.setRegistIp(getRemoteIP());
        user.setHeadImg(Globals.DEFAULT_HEAD_URL);
        user.setRealStatus(CertificateStatusEnum.PASSED.getShortValue());
        user.setGroupId(webUser.getUserId());
        boolean res = userService.insertUser(user, UserTypeEnum.PERSON, RoleTypeEnum.PERSON, null, null);
        if (res) {
            // 插入添加子账户日志
            LogUtil.recordOperationLog(webUser.getUserId(), LogTypeEnum.RUN_RECORD, LogOperatorEnum.ADD, getRemoteIP(), null, "添加集团子账户");
            message = "success";
        } else {
            message = "添加失败！！";
        }
        return SUCCESS;
    }

    /**
     * 集团管理员修改子账户 ajax
     * 
     * @return
     */
    public String editGroupUser() {
        WebUser webUser = getWebuser();
        String groupUserPhone = getParameter("groupUserPhone");
        int groupUserId = getParamInt("groupUserId");
        if (groupUserId <= 0) {
            message = "非法操作！！";
        } else if (StringUtil.isEmpty(groupUserPhone)) {
            message = "请输入手机号码！！";
        } else if (!Validator.isMobile(groupUserPhone)) {
            message = "请输入正确的手机号码！！";
        } else {
            BusUserExample selectEmp = new BusUserExample();
            BusUserExample.Criteria selectCr = selectEmp.createCriteria();
            selectCr.andPhoneEqualTo(groupUserPhone);
            selectCr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
            selectCr.andIdNotEqualTo(groupUserId);

            List<BusUser> list = userService.selectUserByExample(selectEmp);
            if (list != null && list.size() > 0) {
                message = "该手机号码已经被其他用户注册请重新填写！！";
                return SUCCESS;
            }
            BusUser user = new BusUser();
            user.setPhone(groupUserPhone);
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(groupUserId);
            cr.andGroupIdEqualTo(webUser.getUserId());
            int status = userService.updateBusUserByExampleSelective(user, emp);
            if (status > 0) {
                // 插入修改子账户日志
                LogUtil.recordOperationLog(webUser.getUserId(), LogTypeEnum.RUN_RECORD, LogOperatorEnum.MODIFY, getRemoteIP(), null, "集团子账户：" + groupUserId);
                message = "success";
            } else {
                message = "修改失败！！";
            }
        }

        return SUCCESS;
    }

    /**
     * 修改昵称
     */
    public String editUsername() {
        WebUser webUser = getWebuser();
        String username = StringUtil.defaultIfNull(getParameter("username"), "").trim();
        message = "success";
        if (StringUtil.isEmpty(username)) {
            message = "请输入昵称！！";
        } else if (!webUser.getUserName().equals(username.trim())) {
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andIdNotEqualTo(webUser.getUserId());
            cr.andUsernameEqualTo(username);
            List<BusUser> list = userService.selectUserByExample(emp);
            if (list != null && list.size() > 0) {
                message = "当前昵称已经被别的用户注册！！";
            } else {
                BusUser user = new BusUser();
                user.setId(webUser.getUserId());
                user.setUsername(username);
                int status = userService.updateUserByPKSelective(user);
                if (status > 0) {
                    webUser.setUserName(username);
                    String userKey = getSessionUserId();
                    ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.WEB, userKey);// 同步WEB
                    ChargingCacheUtil.removeUser(webUser.getUserId().toString());// 同步APP
                    LogUtil.recordDocumentlog(user.getId(), LogOperatorEnum.MODIFY, null, "修改昵称", "", false);
                    this.username = username;
                } else {
                    message = "修改失败！！";
                }
            }
        }
        return SUCCESS;
    }

    /**
     * 消费明细
     */
    public String userConsume() {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        String dateTime = getParameter("dateTime");
        // Date startDate = DateUtil.getFirstDayOfMonth(new Date());
        // Date endDate = DateUtil.getLastDayOfMonth(new Date());
        // param.put("startDate", startDate);
        // param.put("endDate", endDate);
        if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            List<Integer> userIdlist = new ArrayList<Integer>();
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andGroupIdEqualTo(webUser.getUserId());
            List<BusUser> listUser = userService.selectUserByExample(emp);
            if (listUser != null && listUser.size() > 0) {
                for (BusUser user : listUser) {
                    userIdlist.add(user.getId());
                }
            } else {
                userIdlist.add(0);
            }
            param.put("isGroup", 1);
            param.put("list", userIdlist);
        } else {
            param.put("userId", webUser.getUserId());
        }

        Page page = this.returnPage(currentPage, limit);
        param.put(Globals.PAGE, page);
        List<BillInfo> list = accountService.selectOutAccountBillByPage(param);
        int operatetype;
        for (BillInfo record : list) {
            operatetype = record.getType().intValue();
            record.setTypedesc(FundOperateTypeEnum.getText(operatetype));
            if (FundOperateTypeEnum.CHARGING.getValue() == operatetype || FundOperateTypeEnum.APPOINTMENT.getValue() == operatetype) {// 充电
                record.setName(CacheChargeHolder.getStationPileNameById(record.getId()));
            } else if (FundOperateTypeEnum.RECHARGE.getValue() == operatetype) {// 充值
                record.setName("钱包充值");
            }
            record.setId(null);// 里面有userid，所以要清除，以免暴露
            record.setFee(NumberUtil.formateScale2(record.getFee()));
        }
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 查看首页
     * 
     * @return
     */
    public String initUserHome() {
        WebUser webUser = getWebuser();
        BusAccount userAccount = null;
        if (RoleTypeEnum.GROUP_SUB.getShortValue() == webUser.getRoleType()) {
            BusUser user = userService.selectBusUserByPrimaryKey(webUser.getUserId());
            userAccount = accountService.selectAccoutByPrimaryKey(user.getGroupId());
        } else {
            userAccount = accountService.selectAccoutByPrimaryKey(webUser.getUserId());
        }
        getRequest().setAttribute("usableMoney", userAccount.getUsableMoney());
        Map<String, Object> param = new HashMap<String, Object>();
        if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            List<Integer> userIdlist = new ArrayList<Integer>();
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andGroupIdEqualTo(webUser.getUserId());
            List<BusUser> listUser = userService.selectUserByExample(emp);
            if (listUser != null && listUser.size() > 0) {
                for (BusUser user : listUser) {
                    userIdlist.add(user.getId());
                }
            } else {
                userIdlist.add(0);
            }
            param.put("isGroup", 1);
            param.put("list", userIdlist);
        } else {
            param.put("userId", webUser.getUserId());
        }
        BigDecimal appOut = BigDecimal.ZERO;
        BigDecimal chaOut = BigDecimal.ZERO;
        BigDecimal serviceOut = BigDecimal.ZERO;
        BigDecimal parkOut = BigDecimal.ZERO;
        BigDecimal totalOut = BigDecimal.ZERO;
        BigDecimal totalChaOut = BigDecimal.ZERO;

        BigDecimal appOutForMonth = BigDecimal.ZERO;
        BigDecimal chaOutForMonth = BigDecimal.ZERO;
        BigDecimal serviceOutForMonth = BigDecimal.ZERO;
        BigDecimal parkOutForMonth = BigDecimal.ZERO;
        BigDecimal totalOutForMonth = BigDecimal.ZERO;
        BigDecimal totalChaOutForMonth = BigDecimal.ZERO;

        param.put("payStatus", AppointmentPayStatusEnum.SUCCESS.getValue());
        BigDecimal appTotal = accountService.getTotalAppFeeByMap(param);
        param.put("payStatus", ChargePayStatusEnum.SUCCESS.getValue());
        List<ChargeModel> list = accountService.getTotalChaFeeByMap(param);
        Date now = new Date();
        int month = DateUtil.getMonthValue(now);
        int year = DateUtil.getYearValue(now);
        param.put("month", month);
        param.put("year", year);
        param.put("payStatus", AppointmentPayStatusEnum.SUCCESS.getValue());
        BigDecimal appTotalForMonth = accountService.getTotalAppFeeByMap(param);
        if (appTotalForMonth != null) {
            appOutForMonth = appTotalForMonth;
        }
        param.put("payStatus", ChargePayStatusEnum.SUCCESS.getValue());
        List<ChargeModel> listForMonth = accountService.getTotalChaFeeByMap(param);
        if (listForMonth != null && listForMonth.size() > 0) {
            ChargeModel model = listForMonth.get(0);
            if (model != null) {
                chaOutForMonth = model.getTotalChaFeeOut() == null ? chaOutForMonth : model.getTotalChaFeeOut();
                serviceOutForMonth = model.getTotalServiceFeeOut() == null ? serviceOutForMonth : model.getTotalServiceFeeOut();
                parkOutForMonth = model.getTotalParkFeeOut() == null ? parkOutForMonth : model.getTotalParkFeeOut();
            }
        }
        if (appTotal != null) {
            appOut = appTotal;
        }
        if (list != null && list.size() > 0) {
            ChargeModel model = list.get(0);
            if (model != null) {
                chaOut = model.getTotalChaFeeOut() == null ? chaOut : model.getTotalChaFeeOut();
                serviceOut = model.getTotalServiceFeeOut() == null ? serviceOut : model.getTotalServiceFeeOut();
                parkOut = model.getTotalParkFeeOut() == null ? parkOut : model.getTotalParkFeeOut();
            }
        }
        totalChaOut = NumberUtil.add(chaOut, serviceOut);
        totalChaOut = NumberUtil.add(totalChaOut, parkOut);
        totalOut = NumberUtil.add(totalChaOut, appOut);

        totalChaOutForMonth = NumberUtil.add(chaOutForMonth, serviceOutForMonth);
        totalChaOutForMonth = NumberUtil.add(totalChaOutForMonth, parkOutForMonth);
        totalOutForMonth = NumberUtil.add(totalChaOutForMonth, appOutForMonth);
        getRequest().setAttribute("totalOutForMonth", totalOutForMonth);// 当月总支出
        getRequest().setAttribute("appOutForMonth", NumberUtil.formateScale2(appOutForMonth));// 当月预约支出
        getRequest().setAttribute("totalChaOutForMonth", totalChaOutForMonth);// 当月总充电支出

        getRequest().setAttribute("totalOut", totalOut);// 总支出
        getRequest().setAttribute("totalChaOut", totalChaOut);// 总充电支出
        getRequest().setAttribute("totalAppOut", appOut);// 总预约支出

        if (RoleTypeEnum.PERSON.getShortValue() == webUser.getRoleType()) {
            // ////收入/////
            BigDecimal totalAppInForMonth = BigDecimal.ZERO;// 当前月预约收入
            BigDecimal totalChaInForMonth = BigDecimal.ZERO;// 当前月充电收入
            Map<String, Object> param2 = new HashMap<String, Object>();
            param2.put("userid", webUser.getUserId());
            BigDecimal totalIn = accountService.selectUserBillTotalIn(param2);// 历史总收入
            getRequest().setAttribute("totalIn", NumberUtil.formateScale2(totalIn));// 历史总收入
            param2.put("busMec", webUser.getInfoId());
            param2.put("busType", webUser.getUsertype().getValue());
            param2.put("payStatus", AppointmentPayStatusEnum.SUCCESS.getValue());
            param2.put("year", year);
            param2.put("month", month);
            List<BusAppointment> appList = accountService.selectAppointmentByMap(param2);
            param2.put("payStatus", ChargePayStatusEnum.SUCCESS.getValue());
            List<BusPayment> chaList = accountService.selectPaymentByMap(param2);
            if (appList != null && appList.size() > 0) {
                for (BusAppointment appointment : appList) {
                    totalAppInForMonth = NumberUtil.add(totalAppInForMonth, NumberUtil.getNotNUll(appointment.getAppFee()));
                }
            }
            if (chaList != null && chaList.size() > 0) {
                for (BusPayment payment : chaList) {
                    totalChaInForMonth = NumberUtil.add(totalChaInForMonth, NumberUtil.getNotNUll(payment.getShouldMoney()));
                }
            }
            BigDecimal totalInForMonth = NumberUtil.add(totalAppInForMonth, totalChaInForMonth);
            getRequest().setAttribute("totalAppInForMonth", NumberUtil.formateScale2(totalAppInForMonth));
            getRequest().setAttribute("totalChaInForMonth", NumberUtil.formateScale2(totalChaInForMonth));
            getRequest().setAttribute("totalInForMonth", totalInForMonth);
            PobChargingStationExample emp = new PobChargingStationExample();
            PobChargingStationExample.Criteria cr = emp.createCriteria();
            cr.andBusMecEqualTo(webUser.getInfoId());
            cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
            int deviceCount = deviceService.countPobChargingStationByExample(emp);
            getRequest().setAttribute("deviceCount", deviceCount);
        }

        return SUCCESS;
    }

    /**
     * 查看个人信息
     * 
     * @return
     */
    public String initUserInfo() {
        WebUser webUser = getWebuser();
        List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
        getRequest().setAttribute("provinceList", provinceList);
        BusUser busUser = userService.selectUserByPrimaryKey(webUser.getUserId());
        if (UserTypeEnum.PERSON == webUser.getUsertype()) {
            // 如果是集团子账户查询集团信息
            if (RoleTypeEnum.GROUP_SUB.getShortValue() == webUser.getRoleType()) {
                BusUser goupUser = userService.selectUserByPrimaryKey(busUser.getGroupId());
                BusGroupInfo groupInfo = userService.selectBusGroupInfoByPrimaryKey(goupUser.getInfoId());
                getRequest().setAttribute("groupName", groupInfo.getGroupName());
            }
            BusUserInfo busUserInfo = userService.selectUserInfoByPrimaryKey(webUser.getInfoId());
            getRequest().setAttribute("busUserInfo", busUserInfo);
            if (busUserInfo.getCity() != null) {
                List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(busUserInfo.getProvince());
                getRequest().setAttribute("cityList", cityList);
            }
            if (!(busUser.getRealStatus() == RealVerifyStatusEnum.PASSED.getShortValue())) {
                // BusUserInfo userInfo = userService.selectUserInfoByPrimaryKey(busUser.getInfoId());
                // getRequest().setAttribute("userInfo", userInfo);
                BusUserRealExample emp = new BusUserRealExample();
                BusUserRealExample.Criteria cr = emp.createCriteria();
                cr.andUserIdEqualTo(webUser.getUserId());
                cr.andStatusEqualTo(RealVerifyStatusEnum.VERIFYING.getShortValue());
                List<BusUserReal> userRealList = userService.selectUserRealByExample(emp);
                if (userRealList != null && userRealList.size() > 0) {
                    getRequest().setAttribute("userRealInfo", userRealList.get(0));
                }
            }
        } else if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            BusGroupInfo groupInfo = userService.selectBusGroupInfoByPrimaryKey(busUser.getInfoId());
            List<SysLink> scaleTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.SCALE_TYPE.getValue());
            getRequest().setAttribute("groupInfo", groupInfo);
            getRequest().setAttribute("scaleTypeList", scaleTypeList);
            if ((groupInfo.getCity() != null && groupInfo.getCity() > 0) && (groupInfo.getProvince() != null) && groupInfo.getProvince() > 0) {
                List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(groupInfo.getProvince());
                getRequest().setAttribute("cityList", cityList);
            }
        }
        this.getRequest().setAttribute(Globals.CURRENTUSER, busUser);
        this.getRequest().setAttribute("safeLevel", safeLevel(busUser));
        return SUCCESS;
    }

    private int safeLevel(BusUser busUser) {
        int level = 0;
        if (busUser.getPassword() != null) {
            level++;
        }
        if (busUser.getPhoneStatus() == CertificateStatusEnum.PASSED.getShortValue()) {
            level++;
        }
        if (busUser.getEmailStatus() == CertificateStatusEnum.PASSED.getShortValue()) {
            level++;
        }
        if (busUser.getRealStatus() == CertificateStatusEnum.PASSED.getShortValue()) {
            level++;
        }
        return level;
    }

    /**
     * 修改个人信息
     * 
     * @return
     */
    public String editUserInfo() {
        WebUser webUser = getWebuser();
        String userInfoStr = getParameter("userInfo");
        message = checkUserInfo(userInfoStr);
        BusUserInfo userInfoObj = null;
        if ("success".equals(message)) {
            JSONObject obj = JSONObject.fromObject(userInfoStr);
            Object birthdayObj = obj.get("birthday");
            if (birthdayObj != null && !StringUtil.isEmpty(birthdayObj.toString())) {
                userInfoObj = JsonToBean(userInfoStr, BusUserInfo.class);
            } else {
                userInfoObj = new BusUserInfo();
                Object o = obj.get("sex");
                if (o == null) {
                    userInfoObj.setSex(new Short("1"));
                } else {
                    userInfoObj.setSex(Short.valueOf(o.toString()));
                }
                userInfoObj.setSign(obj.get("sign").toString());
                userInfoObj.setQq(obj.get("qq").toString());
                userInfoObj.setProvince(Integer.valueOf(obj.get("province").toString()));
                userInfoObj.setCity(Integer.valueOf(obj.get("city").toString()));
            }
            userInfoObj.setId(webUser.getInfoId());
            int status = userService.updateUserInfoByPrimaryKeySelective(userInfoObj);
            LogUtil.recordDocumentlog(webUser.getUserId(), LogOperatorEnum.MODIFY, null, "修改个人信息", "", false);
        }

        return SUCCESS;
    }

    /**
     * 修改头像
     * 
     * @return
     */
    public String editUserHeadImg() {
        WebUser webUser = getWebuser();
        String id = webUser.getUserId() + "_" + System.currentTimeMillis();
        try {
            Map<String, Object> map = ImageUtil.uploadImg(headImg, id, ImgTypeEnum.HEAD_IMG, getDataPath());
            message = map.get("msg").toString();
            if ("success".equals(message)) {
                String headImgPath = map.get("url").toString();
                headImgUrl = headImgPath;
                BusUser user = new BusUser();
                user.setId(webUser.getUserId());
                user.setHeadImg(headImgPath);
                if (userService.updateUserByPKSelective(user) > 0) {
                    webUser.setHeadImg(map.get("url").toString());
                    String userKey = getSessionUserId();
                    ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.WEB, userKey);
                    ChargingCacheUtil.removeUser(webUser.getUserId().toString());// 同步APP
                    LogUtil.recordDocumentlog(user.getId(), LogOperatorEnum.MODIFY, null, "修改头像", "", true);
                }

            }
        } catch (Exception e) {
            message = "修改头像失败！！";
            e.printStackTrace();
        }
        return SUCCESS;
    }

    /**
     * 我的爱车
     * 
     * @return
     */
    public String initUserCar() {
        WebUser webUser = getWebuser();
        BusUserInfo busUserInfo = userService.selectUserInfoByPrimaryKey(webUser.getInfoId());
        List<SysCarBrand> carBrandList = CacheSysHolder.getCarBrandList();
        getRequest().setAttribute("busUserInfo", busUserInfo);
        getRequest().setAttribute("carBrandList", carBrandList);
        if (busUserInfo.getBrand() != null) {
            List<SysCarBrand> modelList = CacheSysHolder.getCarModelListByPid(busUserInfo.getBrand());
            getRequest().setAttribute("modelList", modelList);
        }
        return SUCCESS;

    }

    /**
     * 修改我的爱车信息
     * 
     * @return
     */
    public String editUserCar() {
        WebUser webUser = getWebuser();
        String userInfoStr = getParameter("userInfo");
        message = checkUserInfo(userInfoStr);
        if ("success".equals(message)) {
            BusUserInfo userInfoObj = JsonToBean(userInfoStr, BusUserInfo.class);
            userInfoObj.setId(webUser.getInfoId());
            if (pic != null) {
                try {
                    Map<String, Object> result = ImageUtil.uploadImg(pic, userInfoObj.getId(), ImgTypeEnum.USER_DRIVING_LICENSE, getDataPath());
                    message = result.get("msg").toString();
                    userInfoObj.setPic(result.get("url").toString());
                } catch (Exception e) {
                    message = "上传图片失败！！";
                    e.printStackTrace();
                }
            }
            if ("success".equals(message)) {
                userService.updateUserInfoByPrimaryKeySelective(userInfoObj);
            }

        }
        return SUCCESS;
    }

    /**
     * 请求车型号
     * 
     * @return
     */
    public String requestCarModel() {
        int brand = this.getParamInt("brand");
        if (brand > 0) {
            modelList = CacheSysHolder.getCarModelListByPid(brand);
        }
        return SUCCESS;
    }

    /**
     * 实名认证
     * 
     * @return
     */
    public String initUserReal() {
        WebUser webUser = getWebuser();
        if (RealVerifyStatusEnum.PASSED.getShortValue() == webUser.getRealStatus()) {
            BusUserInfo busUserInfo = userService.selectUserInfoByPrimaryKey(webUser.getInfoId());
            getRequest().setAttribute("busUserInfo", busUserInfo);
        } else {
            BusUserRealExample emp = new BusUserRealExample();
            BusUserRealExample.Criteria cr = emp.createCriteria();
            cr.andUserIdEqualTo(webUser.getUserId());
            cr.andStatusEqualTo(RealVerifyStatusEnum.VERIFYING.getShortValue());
            List<BusUserReal> userRealList = userService.selectUserRealByExample(emp);
            if (userRealList != null && userRealList.size() > 0) {
                getRequest().setAttribute("userRealInfo", userRealList.get(0));
            }
        }
        return SUCCESS;
    }

    /**
     * 提交实名认证
     * 
     * @return
     */
    public String doUserReal() {
        WebUser webUser = getWebuser();
        String userRealInfo = getParameter("userRealInfo");
        BusUserRealExample emp = new BusUserRealExample();
        BusUserRealExample.Criteria cr = emp.createCriteria();
        cr.andUserIdEqualTo(webUser.getUserId());
        cr.andStatusBetween(RealVerifyStatusEnum.VERIFYING.getShortValue(), RealVerifyStatusEnum.PASSED.getShortValue());
        List<BusUserReal> list = userService.selectUserRealByExample(emp);
        Map<String, Object> result = null;
        if (list != null && list.size() > 0) {
            message = "您已经提交实名申请或已经实名！！";
        } else {
            message = checkUserRealInfo(userRealInfo);
            if (pic == null) {
                message = "请上传身份证照片！！";
            } else {
                try {
                    result = ImageUtil.uploadImg(pic, webUser.getUserId(), ImgTypeEnum.USER_CARD_FRONT_TEMP, getDataPath());
                    message = result.get("msg").toString();
                } catch (Exception e) {
                    message = "上传照片失败！！";
                    e.printStackTrace();
                }
            }
            if ("success".equals(message)) {
                BusUserReal userRealInfoObj = JsonToBean(userRealInfo, BusUserReal.class);
                userRealInfoObj.setUserId(webUser.getUserId());
                userRealInfoObj.setAddTime(new Date());
                userRealInfoObj.setFront(result.get("url").toString());
                userRealInfoObj.setStatus(RealVerifyStatusEnum.VERIFYING.getShortValue());
                userService.insertUserReal(userRealInfoObj);
            }
        }

        return SUCCESS;
    }

    /**
     * 信息
     * 
     * @return
     */
    public String initUserMsg() {
        return SUCCESS;
    }

    /**
     * 信息
     * 
     * @return
     */
    public String userMsg() {
        WebUser webUser = getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int isRead = this.getParamInt("isRead");// 1是2否
        Page page = this.returnPage(currentPage, limit);
        param.put(Globals.PAGE, page);
        if (isRead > 0) {
            param.put("isRead", isRead);
        }
        param.put("msgType", NoticeTypeEnum.MESSAGE.getShortValue());
        param.put("reciveUserId", webUser.getUserId());
        List<SysNoticeMessage> list = this.userService.selectSysNoticeByPage(param);
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 显示一条msg
     * 
     * @return
     */
    public String userDetailMsg() {
        WebUser webUser = this.getWebuser();
        int noticeId = this.getParamInt("noticeId");
        int isRead = this.getParamInt("isRead");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        if (noticeId > 0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("reciveUserId", webUser.getUserId());
            param.put("noticId", noticeId);
            SysNoticeMessage sysNoticeMessage = userService.selectSysNoticeMessage(param);
            if (sysNoticeMessage != null) {
                SysNotice sysNotice = userService.selectSysNoticeByPrimaryKey(noticeId);
                map.put("noticeMessage", sysNoticeMessage);
                if (sysNotice.getIsRead() != WhetherEnum.YES.getShortValue()) {
                    SysNotice newSysNotice = new SysNotice();
                    newSysNotice.setId(noticeId);
                    newSysNotice.setIsRead(WhetherEnum.YES.getShortValue());
                    userService.updateSysNoticeByPrimaryKeySelective(newSysNotice);
                }
            } else {
                map.put("msg", "查看信息不存在！！");
            }
        } else {
            map.put("msg", "查看信息失败！！");
        }
        this.result = map;
        return SUCCESS;
    }

    /**
     * 我的收藏
     * 
     * @return
     */
    public String initUserFavorite() {
        return SUCCESS;
    }

    /**
     * 我的收藏
     * 
     * @return
     */
    public String userFavorite() {
        WebUser webUser = getWebuser();
        Map<String, Object> params = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        Page page = this.returnPage(currentPage, limit);
        params.put(Globals.PAGE, page);
        params.put("userid", webUser.getUserId());
        List<BusFavorites> favoritelist = accountService.selectUserFavoritesByPage(params);
        // 设置充电点的空闲桩数，交直流桩数
        Map<String, Integer> numMap;
        List<StationIntro> stationList = new ArrayList<StationIntro>();
        StationIntro station;
        PobChargingStation record;
        for (BusFavorites fa : favoritelist) {
            record = CacheChargeHolder.getChargeStationById(fa.getStationId());
            if (record == null) {
                continue;
            }
            station = new StationIntro();
            station.setStationid(record.getId());
            station.setStationname(record.getStationName());
            station.setLng(record.getLng());
            station.setLat(record.getLat());
            station.setAddress(record.getAddress());
            station.setImg(record.getImg());
            station.setLinkphone(record.getLinkPhone());
            station.setFastnum(record.getFastNum() == null ? 0 : record.getFastNum());
            station.setSlownum(record.getSlowNum() == null ? 0 : record.getSlowNum());
            if (record.getOpenDay() != null) {
                station.setOpendaydesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_DAY.getValue(), record.getOpenDay().toString()));
            }
            if (record.getOpenTime() != null) {
                station.setOpentimedesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.OPEN_TIME.getValue(), record.getOpenTime().toString()));
            }
            station.setScore(record.getScore());
            numMap = CacheChargeHolder.getPileNumByStationid(record.getId());
            station.setAcnum(numMap.get("acnum"));
            station.setDcnum(numMap.get("dcnum"));
            station.setIdlenum(numMap.get("idlenum"));
            station.setFavoriteid(fa.getId());
            stationList.add(station);
        }
        page.setRoot(stationList);
        this.page = page;
        // getRequest().setAttribute("favoriteList", favoriteList);
        return SUCCESS;
    }

    /**
     * 我的收藏删除
     *
     * @return
     */
    public String favoritedel() {
        int favoriteId = this.getParamInt("favoriteId");
        if (favoriteId > 0) {
            if (accountService.deleteFavoritesByPK(favoriteId) > 0) {
                message = "success";
            } else {
                message = "取消收藏失败！！";
            }
        } else {
            message = "请选择收藏点！！";
        }
        return SUCCESS;
    }

    /**
     * 验证账户信息页：修改交易密码
     * 
     * @return
     */
    public String validChangePayPwd() throws Exception {
        WebUser webUser = this.getWebuser();
        String userId = webUser.getUserId().toString();
        BussinessCacheUtil.removieEamil(userId);
        BussinessCacheUtil.removieEamilCode(userId);
        BussinessCacheUtil.removieChangePwd(userId);
        BussinessCacheUtil.removieChangeEmail(userId);
        BussinessCacheUtil.removieChangePhone(userId);
        BussinessCacheUtil.removieChangePhoneCode(userId);
        BussinessCacheUtil.removieChangePhoneNum(userId);
        if (!changePayPwd.equals(BussinessCacheUtil.getChangePayPwd(userId))) {
            BussinessCacheUtil.setChangePayPwd(userId, changePwd);
            BussinessCacheUtil.removiePhoneCode(userId);
        }

        this.getRequest().setAttribute("actionType", changePayPwd);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
    }

    /**
     * 验证邮箱信息页：修改邮箱
     * 
     * @return
     */
    public String validChangePhone() throws Exception {
        WebUser webUser = this.getWebuser();
        String userId = webUser.getUserId().toString();
        BussinessCacheUtil.removieEamil(userId);
        BussinessCacheUtil.removieEamilCode(userId);
        BussinessCacheUtil.removieChangePhoneNum(userId);
        BussinessCacheUtil.removieChangePhoneCode(userId);
        BussinessCacheUtil.removieChangePwd(userId);
        BussinessCacheUtil.removieChangePayPwd(userId);
        BussinessCacheUtil.removieChangeEmail(userId);
        if (!changePhone.equals(BussinessCacheUtil.getChangePhone(userId))) {
            BussinessCacheUtil.setChangePhone(userId, changePhone);
            BussinessCacheUtil.removiePhoneCode(userId);
        }

        this.getRequest().setAttribute("actionType", changePhone);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
    }

    /**
     * 验证邮箱信息页：修改邮箱
     * 
     * @return
     */
    public String validChangeEmail() throws Exception {
        WebUser webUser = this.getWebuser();
        String userId = webUser.getUserId().toString();
        BussinessCacheUtil.removieEamil(userId);
        BussinessCacheUtil.removieEamilCode(userId);
        BussinessCacheUtil.removieChangePhone(userId);
        BussinessCacheUtil.removieChangePhoneCode(userId);
        BussinessCacheUtil.removieChangePhoneNum(userId);
        BussinessCacheUtil.removieChangePwd(userId);
        BussinessCacheUtil.removieChangePayPwd(webUser.getUserId().toString());
        if (!changeEmail.equals(BussinessCacheUtil.getChangeEmail(userId))) {
            BussinessCacheUtil.setChangeEmail(userId, changeEmail);
            BussinessCacheUtil.removiePhoneCode(userId);
        }

        this.getRequest().setAttribute("actionType", changeEmail);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
    }

    /**
     * 验证账户信息页：修改登录密码
     * 
     * @return
     */
    public String validChangePwd() throws Exception {

        WebUser webUser = this.getWebuser();
        String userId = webUser.getUserId().toString();
        BussinessCacheUtil.removieEamil(userId);
        BussinessCacheUtil.removieEamilCode(userId);
        BussinessCacheUtil.removieChangePhone(userId);
        BussinessCacheUtil.removieChangePhoneCode(userId);
        BussinessCacheUtil.removieChangePhoneNum(userId);
        BussinessCacheUtil.removieChangePayPwd(userId);
        BussinessCacheUtil.removieChangeEmail(userId);
        if (!changePwd.equals(BussinessCacheUtil.getChangePwd(userId))) {
            BussinessCacheUtil.setChangePwd(userId, changePwd);
            BussinessCacheUtil.removiePhoneCode(userId);
        }
        this.getRequest().setAttribute("actionType", changePwd);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
    }

    /**
     * 验证账户信息 ajax
     * 
     * @return
     */
    public String doValidUserInfo() throws Exception {
        String actionType = this.getParameter("actionType");
        String phoneCode = this.getParameter("phoneCode");
        WebUser webUser = this.getWebuser();
        if (!changePwd.equals(actionType) && !changePayPwd.equals(actionType) && !changeEmail.equals(actionType) && !changePhone.equals(actionType)) {
            message = ERROR;
            return SUCCESS;
        }
        // BusUser webUser = (BusUser) this.getRedisCache(Globals.CURRENTUSER);
        message = checkPhoneCode(phoneCode, webUser);
        if ("success".equals(message)) {
            BussinessCacheUtil.removiePhoneCode(webUser.getUserId().toString());
        }
        /*
         * if (changePwd.equals(actionType)) { BussinessCacheUtil.setChangePwd(webUser.getUserId().toString(),
         * changePwd); } else if (changePayPwd.equals(actionType)) {
         * BussinessCacheUtil.setChangePayPwd(webUser.getUserId().toString(), changePayPwd); } else if
         * (changeEmail.equals(actionType)) { BussinessCacheUtil.setChangeEmail(webUser.getUserId().toString(),
         * changeEmail); }
         */

        return SUCCESS;
    }

    /**
     * 验证手机验证码信息
     * 
     * @param phoneCode
     * @param webUser
     * @param seconds
     * @return
     */
    private String checkPhoneCode(String phoneCode, WebUser webUser) {
        String msg = "success";

        String realPhoneCode = BussinessCacheUtil.getPhoneCode(webUser.getUserId().toString());
        if (StringUtil.isEmpty(phoneCode)) {
            // this.removeRedisCache(webUser.getPhone());
            msg = "验证码不能为空！！";
        } else if (realPhoneCode == null) {
            msg = "请获取验证码！！";
        } else if (!phoneCode.equals(realPhoneCode)) {
            msg = "验证码不正确,请重新填写！！";
        }

        return msg;
    }

    /**
     * 修改账户密码或交易密码页
     * 
     * @return
     */
    public String changePwd() throws Exception {
        WebUser webUser = this.getWebuser();
        String userId = webUser.getUserId().toString();
        String changePwdType = BussinessCacheUtil.getChangePwd(userId);
        String changePayPwdType = BussinessCacheUtil.getChangePayPwd(userId);
        String msg = "success";

        if (!changePwd.equals(changePwdType) && !changePayPwd.equals(changePayPwdType)) {
            msg = "非法操作！！！";
            return MEMBER;
        }
        if (changePwd.equals(changePwdType)) {
            this.getRequest().setAttribute("actionType", changePwd);
        } else if (changePayPwd.equals(changePayPwdType)) {
            this.getRequest().setAttribute("actionType", changePayPwd);
        }

        return SUCCESS;
    }

    /**
     * 修改手机
     * 
     * @return
     */
    public String changePhone() throws Exception {
        WebUser webUser = this.getWebuser();
        String actionType = BussinessCacheUtil.getChangePhone(webUser.getUserId().toString());
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        if (!changePhone.equals(actionType)) {
            return MEMBER;
        }
        this.getRequest().setAttribute("actionType", changePhone);
        return SUCCESS;
    }

    /**
     * 修改邮箱页
     * 
     * @return
     */
    public String changeEmail() throws Exception {
        WebUser webUser = this.getWebuser();
        String actionType = BussinessCacheUtil.getChangeEmail(webUser.getUserId().toString());
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        if (!changeEmail.equals(actionType)) {
            return MEMBER;
        }
        this.getRequest().setAttribute("actionType", changeEmail);
        return SUCCESS;
    }

    /**
     * 修改手机ajax
     * 
     * @return
     * @throws Exception
     */
    public String doChangePhone() throws Exception {
        String phoneCode = this.getParameter("phoneCode");// 手机验证码
        String phone = this.getParameter("phone");// 新手机号码
        WebUser webUser = this.getWebuser();
        String actionType = BussinessCacheUtil.getChangePhone(webUser.getUserId().toString());
        String sessionPhone = BussinessCacheUtil.getChangePhoneNum(webUser.getUserId().toString());
        if (!changePhone.equals(actionType)) {
            message = ERROR;
        } else if (StringUtil.isEmpty(phoneCode)) {
            message = "验证码不能为空！！";
        } else if (StringUtil.isEmpty(phone)) {
            message = "手机号码不能为空！！";
        } else if (!Validator.isMobile(phone)) {
            message = "手机格式不正确！！";
        } else if (!phone.equals(sessionPhone)) {
            message = "认证手机号不同，请重新请求认证！！";
        } else {
            message = checkChangePhoneCode(phoneCode, webUser.getUserId().toString());

            if ("success".equals(message)) {
                BusUser busUser = new BusUser();
                busUser.setId(webUser.getUserId());
                busUser.setPhone(phone);
                /*
                 * if (webUser.getEamilStatus() == CertificateStatusEnum.FAILED.getShortValue()) {
                 * busUser.setEmailStatus(CertificateStatusEnum.PASSED.getShortValue());
                 * webUser.setEamilStatus(CertificateStatusEnum.PASSED.getShortValue()); }
                 */
                int status = this.userService.updateUserByPKSelective(busUser);
                // 修改成功
                if (status == 1) {
                    logger.info("手机修改成功！！");
                    // 添加日志
                    LogUtil.recordDocumentlog(webUser.getUserId(), LogOperatorEnum.MODIFY, this.getRemoteIP(), "用户 '" + webUser.getUserName() + "' 修改手机",
                                              "原手机：" + webUser.getPhone() + "新手机：" + phone, false);
                    webUser.setPhone(phone);
                    String userKey = getSessionUserId();
                    if (StringUtil.isEmpty(userKey)) {
                        message = "修改失败！！";
                    } else {
                        ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.WEB, userKey);
                        ChargingCacheUtil.removeUser(webUser.getUserId().toString());// 同步APP
                    }

                }
                BussinessCacheUtil.removieChangePhoneCode(webUser.getUserId().toString());
                BussinessCacheUtil.removieChangePhoneNum(webUser.getUserId().toString());

                /*
                 * this.removeRedisCache("phone_code_time"); this.removeRedisCache(actionType);
                 */
            }
        }
        return SUCCESS;
    }

    /**
     * 修改账户密码
     * 
     * @return
     */
    public String doChangePwd() throws Exception {
        WebUser user = this.getWebuser();
        String userId = user.getUserId().toString();
        BusUser webUser = userService.selectUserByPrimaryKey(Integer.valueOf(userId));
        String oldPwd = this.getParameter("oldPwd");
        String newPwd = this.getParameter("newPwd");
        String checkNewPwd = this.getParameter("checkNewPwd");
        String changePwdType = BussinessCacheUtil.getChangePwd(userId);
        String changePayPwdType = BussinessCacheUtil.getChangePayPwd(userId);

        message = checkChangePwd(webUser, oldPwd, newPwd, checkNewPwd, changePwdType, changePayPwdType);
        if (!"success".equals(message)) {
            return SUCCESS;
        }
        newPwd = SecurityUtil.passwordEncrypt(newPwd);
        BusUser busUser = new BusUser();
        busUser.setId(webUser.getId());
        if (changePwd.equals(changePwdType)) {
            busUser.setPassword(newPwd);
            logger.info("修改登录密码");
        } else if (changePayPwd.equals(changePayPwdType)) {
            busUser.setPayPassword(newPwd);
            logger.info("修改交易密码");
        }
        int status = userService.updateUserByPKSelective(busUser);
        if (status != 1) {
            message = "fail";
        } else if (changePwd.equals(changePwdType)) {
            // 添加日志
            LogUtil.recordDocumentlog(webUser.getId(), LogOperatorEnum.MODIFY, this.getRemoteIP(), "用户 '" + webUser.getUsername() + "' 修改登录密码", "", false);
            // 修改密码成功重新登录
            ChargingCacheUtil.removieSession(null, KeySessionTypeEnum.WEB, getSessionUserId());
        }
        // 清除session中手机验证码时间
        /*
         * this.removeRedisCache("phone_code_time"); this.removeRedisCache(actionType);
         */
        return SUCCESS;
    }

    /**
     * 修改或认证邮箱 ajax
     * 
     * @return
     * @throws Exception
     */
    public String doChangeEamil() throws Exception {
        String emailCode = this.getParameter("emailCode");// 邮箱验证码
        String email = this.getParameter("email");// 邮箱
        WebUser webUser = this.getWebuser();
        String actionType = BussinessCacheUtil.getChangeEmail(webUser.getUserId().toString());
        String sessionEmail = BussinessCacheUtil.getEamil(webUser.getUserId().toString());
        if (!changeEmail.equals(actionType)) {
            message = ERROR;

        } else if (StringUtil.isEmpty(emailCode)) {
            message = "验证码不能为空！！";
        } else if (StringUtil.isEmpty(email)) {
            message = "邮箱不能为空！！";
        } else if (!Validator.isEmail(email)) {
            message = "邮箱格式不正确！！";
        } else if (!email.equals(sessionEmail)) {
            message = "认证邮箱不同，请重新请求认证！！";
        } else {
            message = checkEmailCode(emailCode, webUser.getUserId().toString());
            if ("success".equals(message)) {
                BusUser busUser = new BusUser();
                busUser.setId(webUser.getUserId());
                busUser.setEmail(email);
                if (webUser.getEamilStatus() == CertificateStatusEnum.FAILED.getShortValue()) {
                    busUser.setEmailStatus(CertificateStatusEnum.PASSED.getShortValue());
                    webUser.setEamilStatus(CertificateStatusEnum.PASSED.getShortValue());
                }
                int status = this.userService.updateUserByPKSelective(busUser);
                // 修改成功
                if (status == 1) {
                    // 添加日志
                    LogUtil.recordDocumentlog(webUser.getUserId(), LogOperatorEnum.MODIFY, this.getRemoteIP(), "用户 '" + webUser.getUserName() + "' 修改邮箱",
                                              "原邮箱：" + webUser.getEmail() + "新邮箱：" + email, false);
                    logger.info("邮箱修改成功！！");
                    webUser.setEmail(email);
                    String userKey = getSessionUserId();
                    if (StringUtil.isEmpty(userKey)) {
                        message = "修改失败！！";
                    } else {
                        ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.WEB, userKey);
                        ChargingCacheUtil.removeUser(webUser.getUserId().toString());// 同步APP
                    }
                }
                BussinessCacheUtil.removieEamilCode(webUser.getUserId().toString());
                BussinessCacheUtil.removieEamil(webUser.getUserId().toString());

                /*
                 * this.removeRedisCache("phone_code_time"); this.removeRedisCache(actionType);
                 */
            }
        }
        return SUCCESS;
    }

    private String checkChangePhoneCode(String changePhoneCode, String userId) {
        String msg = "success";
        String realChangePhoneCode = BussinessCacheUtil.getChangePhoneCode(userId);
        if (!changePhoneCode.equals(realChangePhoneCode)) {
            msg = "验证码不正确，请重新填写！！";
        } else {
            logger.info("清除session中changePhoneCode");
            BussinessCacheUtil.removieChangePhoneCode(userId);
            BussinessCacheUtil.removieChangePhoneNum(userId);
        }

        return msg;
    }

    private String checkEmailCode(String emailCode, String userId) {
        String msg = "success";
        String realEmailCode = BussinessCacheUtil.getEamilCode(userId);
        if (emailCode.equals(realEmailCode)) {
            logger.info("清除session中eamilCode");
            BussinessCacheUtil.removieEamilCode(userId);
            BussinessCacheUtil.removieEamil(userId);
        } else {
            msg = "验证码不正确，请重新填写！！";
        }

        return msg;
    }

    /**
     * 验证修改密码信息
     * 
     * @param webUser
     * @param oldPwd
     * @param newPwd
     * @param checkNewPwd
     * @param timeLimit
     * @param actionType
     * @return
     */
    private String checkChangePwd(BusUser webUser, String oldPwd, String newPwd, String checkNewPwd, String changePwdType, String changePayPwdType) {
        String msg = "success";
        if (!changePwd.equals(changePwdType) && !changePayPwd.equals(changePayPwdType)) {
            msg = "fail";
        } else if (changePwd.equals(changePwdType) && !webUser.getPassword().equals(SecurityUtil.passwordEncrypt(oldPwd))) {
            msg = "登录原密码不正确！！";

        } else if (changePayPwd.equals(changePayPwdType) && !webUser.getPayPassword().equals(SecurityUtil.passwordEncrypt(oldPwd))) {
            msg = "交易原密码不正确！！";

        } else if (!newPwd.equals(checkNewPwd)) {
            msg = "密码不一致！！";
        } else if (!Validator.isPassword(newPwd)) {
            msg = " 密码为6-16位数字字母组合!";
        }
        return msg;
    }

    /**
     * 获取手机验证码 ajax
     * 
     * @return
     * @throws Exception
     */
    public String phoneCode() throws Exception {
        WebUser webUser = this.getWebuser();
        message = "success";
        String userId = getWebuser().getUserId().toString();
        String cachePhoneCode = BussinessCacheUtil.getPhoneCode(userId);
        if (cachePhoneCode != null) {
            long outTime = RedisUtil.ttl(CacheKeyProvide.getKey(CacheKeyProvide.KEY_PHONE_CODE, userId));
            long temp = RedisUtil.EXRP_3M - outTime;
            if (temp <= 60) {
                temp = 60 - temp;
                message = "操作繁忙请在" + temp + "秒后重试！！";
            }

        }

        if ("success".equals(message)) {
            String phoneCode = RandomUtil.getRandomNumber(4);
            logger.info("生成的手机动态验证码为：" + phoneCode);

            if (MsgUtil.sendSMS(webUser.getPhone(), phoneCode)) {
                BussinessCacheUtil.setPhoneCode(userId, phoneCode);
                BussinessCacheUtil.setPhoneCode(userId, phoneCode);
            } else {
                message = "短信发送失败！！";
            }

        }
        return SUCCESS;
    }

    /**
     * 更改手机号码发送验证码
     * 
     * @return
     * @throws Exception
     */
    public String phoneCodeForChangePhone() throws Exception {
        WebUser webUser = getWebuser();
        String msg = "success";
        String userId = getWebuser().getUserId().toString();
        String cachePhoneCode = BussinessCacheUtil.getChangePhoneCode(userId);
        String phone = this.getParameter("phone");
        if (!StringUtil.isEmpty(phone)) {
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andPhoneEqualTo(phone.trim());
            cr.andUserTypeEqualTo(webUser.getUsertype().getShortValue());
            List<BusUser> list = this.userService.selectUserByExample(emp);
            if (list != null && list.size() > 0) {
                msg = "手机号码：" + phone + "已经被注册，请更换手机号码！！";
            }
            if ("success".equals(msg)) {
                if (cachePhoneCode != null) {
                    long outTime = RedisUtil.ttl(CacheKeyProvide.getKey(CacheKeyProvide.KEY_CHANGE_PHONE_CODE, userId));
                    long temp = RedisUtil.EXRP_3M - outTime;
                    if (temp <= 60) {
                        temp = 60 - temp;
                        message = "操作繁忙请在" + temp + "秒后重试！！";
                    }
                }
                if (Validator.isMobile(phone) && "success".equals(msg)) {
                    String phoneCode = RandomUtil.getRandomNumber(4);
                    logger.info("修改手机号码生成的手机动态验证码为：" + phoneCode);
                    if (MsgUtil.sendSMS(phone, phoneCode)) {
                        BussinessCacheUtil.setChangePhoneCode(userId, phoneCode);
                        BussinessCacheUtil.setChangePhoneNum(userId, phone);

                    } else {
                        msg = "短信发送失败！！";
                    }
                } else {
                    msg = "手机号码不正确！！";
                }
            }
        } else {
            msg = "手机号码不能为空！！";
        }
        message = msg;
        return SUCCESS;
    }

    /**
     * 获取邮箱验证码 ajax
     * 
     * @return
     * @throws Exception
     */
    public String emailCode() throws Exception {
        WebUser webUser = getWebuser();
        String userId = webUser.getUserId().toString();
        String email = this.getParameter("email");
        String start = "<p>华立科技验证码：";
        String end = "</p>";
        String msg = "success";
        boolean senResult = true;
        if (!StringUtil.isEmpty(email)) {
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andEmailEqualTo(email.trim());
            cr.andUserTypeEqualTo(webUser.getUsertype().getShortValue());
            List<BusUser> list = this.userService.selectUserByExample(emp);
            if (list != null && list.size() > 0) {
                msg = "邮箱地址：" + email + "已经被注册，请更换其他邮箱地址！！";
            }
            if ("success".equals(msg)) {
                if (Validator.isEmail(email)) {
                    String emailCode = RandomUtil.getRandomNumber(4);
                    logger.info("邮箱TO：" + email);
                    logger.info("生成的邮箱动态验证码为：" + emailCode);
                    String content = start + emailCode + end;

                    // 插入信息内容表
                    SysNoticeMessage noticeMessage = new SysNoticeMessage();
                    noticeMessage.setContent(content);
                    noticeMessage.setAddTime(new Date());
                    noticeMessage.setTitle("邮箱认证");
                    messageService.insertNoticeMessage(noticeMessage);

                    SysNotice notice = new SysNotice();
                    notice.setMessageid(noticeMessage.getId());
                    notice.setReciveUser(Integer.valueOf(userId));

                    EmailModel emailModel = new EmailModel();
                    emailModel.setSubject("邮箱认证");
                    emailModel.setContent(content);
                    emailModel.setEmail(email);
                    emailModel.setNotice(notice);
                    senResult = MsgUtil.sendEmail(EmailSubjectEnum.CERTIFICATION, content, email);
                    if (senResult) {
                        BussinessCacheUtil.setEamilCode(userId, emailCode);
                        BussinessCacheUtil.setEamil(userId, email);
                    } else {
                        msg = "邮箱发送失败，请重新发送！！";
                    }
                    // MailUtil.getInstance().sendMail(emailModel.getSubject(), emailModel.getContent(),
                    // emailModel.getEmail());
                    // MsgProduct.sendMsg(emailModel);
                    // MsgUtil.sendEmail(emailModel);
                    /*
                     * Boolean emailSendResult = (Boolean) Globals.EMAIL_RESULT_MAP.get("result"); if (emailSendResult
                     * != null && !emailSendResult) { msg = "邮箱发送失败！！"; }
                     */
                } else {
                    msg = "邮箱地址不正确！！";
                }
            }

        } else {
            msg = "邮箱地址不能为空！！";
        }
        message = msg;
        return SUCCESS;
    }

    // public String userChangePwd() {
    // WebUser webUser = getWebuser();
    // String oldPwd = getParameter("oldPwd");
    // String newPwd = getParameter("newPwd");
    // String checkNewPwd = getParameter("checkNewPwd");
    // message = checkChangePwd(oldPwd, newPwd, checkNewPwd, webUser);
    // if ("success".equals(message)) {
    // BusUser busUser = new BusUser();
    // busUser.setId(webUser.getUserId());
    // busUser.setPassword(SecurityUtil.passwordEncrypt(newPwd));
    // userService.updateUserByPKSelective(busUser);
    // }
    // return SUCCESS;
    // }

    // private String checkChangePwd(String oldPwd, String newPwd, String checkNewPwd, WebUser webUser) {
    // if (StringUtil.isEmpty(oldPwd)) {
    // return "请输入原密码！！";
    // } else if (StringUtil.isEmpty(newPwd)) {
    // return "请输入新密码！！";
    // } else if (StringUtil.isEmpty(checkNewPwd)) {
    // return "请再次输入新密码！！";
    // }
    // BusUser busUser = userService.selectBusUserByPrimaryKey(webUser.getUserId());
    // String enoldPwd = SecurityUtil.passwordEncrypt(oldPwd);
    // if (!enoldPwd.equals(busUser.getPassword())) {
    // return "原密码不正确！！";
    // } else if (!newPwd.endsWith(checkNewPwd)) {
    // return "2次输入的新密码不正确！！";
    // } else if (!Validator.isPassword(newPwd)) {
    // return "请输入6-16位数字或字母密码！！";
    // }
    // return "success";
    // }

    private String checkUserRealInfo(String userRealInfo) {
        String msg = "success";
        try {
            BusUserReal userRealInfoObj = JsonToBean(userRealInfo, BusUserReal.class);

        } catch (Exception e) {
            e.printStackTrace();
            msg = "修改失败！！";
        }
        return msg;
    }

    private String checkUserInfo(String userInfo) {
        String msg = "success";
        try {
            JSONObject obj = JSONObject.fromObject(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "修改失败！！";
        }
        return msg;
    }

    // get set

    public String getMessage() {
        return this.message;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public List<SysCarBrand> getModelList() {
        return modelList;
    }

    public void setPic(File pic) {
        this.pic = pic;
    }

    public Page getPage() {
        return page;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public void setHeadImg(File headImg) {
        this.headImg = headImg;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public String getUsername() {
        return username;
    }

}
