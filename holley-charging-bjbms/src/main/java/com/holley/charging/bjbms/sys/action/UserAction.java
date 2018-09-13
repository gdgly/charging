package com.holley.charging.bjbms.sys.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;

import com.alibaba.fastjson.JSON;
import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusGroupInfo;
import com.holley.charging.model.bus.BusRebate;
import com.holley.charging.model.bus.BusRebateExample;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.model.bus.BusUserRebate;
import com.holley.charging.model.bus.BusUserRebateExample;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.IsActiveEnum;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.LogTypeEnum;
import com.holley.common.constants.RoleEnum;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.CertificateStatusEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysAccountroleKey;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.model.sys.SysRole;
import com.holley.platform.model.sys.SysRoleExample;
import com.holley.platform.service.RoleService;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.LogUtil;
import com.holley.web.common.util.Validator;

/**
 * 用户管理
 * 
 * @author zdd
 */
public class UserAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private UserService       userService;
    private RoleService       roleService;
    private AccountService    accountService;
    private Page              page;
    private BusRebate         rebateBean;

    /**
     * 帐号列表初始化
     * 
     * @return
     */
    public String init() {
        // CertificateStatusEnum[] realStatusList = CertificateStatusEnum.values();
        UserTypeEnum[] userTypeList = UserTypeEnum.values();
        // WhetherEnum[] lockStatusList = WhetherEnum.values();
        // this.getRequest().setAttribute("realStatusList", realStatusList);
        this.getRequest().setAttribute("userTypeList", userTypeList);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userType", UserTypeEnum.COMPANY.getValue());
        param.put("isLock", WhetherEnum.NO.getValue());
        List<BusUser> companyList = userService.selectBusUserByMap(param);
        this.getRequest().setAttribute("companyList", companyList);// 子公司
        // this.getRequest().setAttribute("lockStatusList", lockStatusList);
        // this.getRequest().setAttribute("lockStatus", WhetherEnum.NO.getShortValue());
        // this.getRequest().setAttribute("userType", UserTypeEnum.PLATFORM.getValue());

        return SUCCESS;
    }

    /**
     * 用户新增初始化
     * 
     * @return
     */
    public String addUserInit() {
        String webuserid = getSessionBmsUserId();
        CertificateStatusEnum[] realStatusList = CertificateStatusEnum.values();
        UserTypeEnum[] userTypeList = UserTypeEnum.values();

        SysRoleExample emp = new SysRoleExample();
        SysRoleExample.Criteria cr = emp.createCriteria();
        List<Short> list = new ArrayList<Short>();
        list.add(RoleTypeEnum.PLATFORM.getShortValue());
        list.add(RoleTypeEnum.COMPANY.getShortValue());
        cr.andTypeIn(list);
        // cr.andTypeEqualTo(RoleTypeEnum.PLATFORM.getShortValue());
        /*
         * if (Globals.ADMIN_USER_ID != Integer.parseInt(webuserid)) { cr.andCreatorEqualTo(Integer.valueOf(webuserid));
         * }
         */
        List<SysLink> scaleTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.SCALE_TYPE.getValue());
        List<SysRole> roleList = roleService.selectRoleByExample(emp);
        this.getRequest().setAttribute("provinceList", CacheSysHolder.getProvinceList());
        this.getRequest().setAttribute("scaleTypeList", scaleTypeList);
        this.getRequest().setAttribute("realStatusList", realStatusList);
        this.getRequest().setAttribute("userTypeList", userTypeList);
        this.getRequest().setAttribute("roleList", roleList);
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_ADD);

        return SUCCESS;
    }

    /**
     * 用户修改初始化
     * 
     * @return
     */
    public String editUserInit() {
        String userid = this.getParameter("userid");
        if (!NumberUtils.isNumber(userid)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }

        String webuserid = getSessionBmsUserId();
        CertificateStatusEnum[] realStatusList = CertificateStatusEnum.values();
        UserTypeEnum[] userTypeList = UserTypeEnum.values();

        Integer userId = Integer.valueOf(userid);
        BusUser busUser = userService.selectUserByPrimaryKey(userId);
        if (UserTypeEnum.PERSON.getShortValue().equals(busUser.getUserType())) {// 个人
            BusUserInfo userInfo = userService.selectUserInfoByPrimaryKey(busUser.getInfoId());
            getRequest().setAttribute("userInfo", userInfo);
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("userType", UserTypeEnum.COMPANY.getValue());
            param.put("isLock", WhetherEnum.NO.getValue());
            List<BusUser> companyList = userService.selectBusUserByMap(param);
            this.getRequest().setAttribute("companyList", companyList);// 子公司
        }
        /*
         * if (Globals.ADMIN_USER_ID != Integer.parseInt(webuserid)) { cr.andCreatorEqualTo(Integer.valueOf(webuserid));
         * }
         */
        List<SysRole> roleList = null;
        if (busUser != null) {
            SysAccountroleKey accountrole = roleService.selectAccountRoleByUserid(userId);
            if (accountrole != null) {
                busUser.setRoleid(accountrole.getRoleid());
            }
            if (UserTypeEnum.PLATFORM.getShortValue().equals(busUser.getUserType())) {
                SysRoleExample emp = new SysRoleExample();
                SysRoleExample.Criteria cr = emp.createCriteria();
                cr.andTypeEqualTo(RoleTypeEnum.PLATFORM.getShortValue());
                roleList = roleService.selectRoleByExample(emp);
            } else if (UserTypeEnum.COMPANY.getShortValue().equals(busUser.getUserType())) {
                SysRoleExample emp = new SysRoleExample();
                SysRoleExample.Criteria cr = emp.createCriteria();
                cr.andTypeEqualTo(RoleTypeEnum.COMPANY.getShortValue());
                roleList = roleService.selectRoleByExample(emp);
            }
        }
        this.getRequest().setAttribute("realStatusList", realStatusList);
        this.getRequest().setAttribute("userTypeList", userTypeList);
        this.getRequest().setAttribute("roleList", roleList);
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_EDIT);
        this.getRequest().setAttribute("busUser", busUser);
        if (UserTypeEnum.PERSON.getShortValue().equals(busUser.getUserType())) {
            BusUserRebateExample userRebateEmp = new BusUserRebateExample();
            BusUserRebateExample.Criteria userRebateCr = userRebateEmp.createCriteria();
            userRebateCr.andUserIdEqualTo(userId);
            userRebateCr.andStatusEqualTo(IsActiveEnum.ACTIVE.getShortValue());
            List<BusUserRebate> userRebateList = userService.selectUserRebateByExample(userRebateEmp);
            if (!userRebateList.isEmpty()) {
                this.getRequest().setAttribute("rebateBean", userRebateList.get(0));
            }
            BusRebateExample rebateEmp = new BusRebateExample();
            BusRebateExample.Criteria rebateCr = rebateEmp.createCriteria();
            rebateCr.andEndTimeGreaterThan(DateUtil.StrToDate(DateUtil.DateToShortStr(new Date()), DateUtil.TIME_SHORT));
            List<BusRebate> rebateList = userService.selectRebateByExample(rebateEmp);
            this.getRequest().setAttribute("rebateList", rebateList);
        }

        return SUCCESS;
    }

    public String modifyPassword() {
        int userId = getParamInt("userId");
        String password = getParameter("password");
        String repeatpwd = getParameter("repeatpwd");
        String msg = SUCCESS;
        if (userId == 0) {
            msg = "请选择用户！";
        } else if (StringUtil.isEmpty(password) || !Validator.isPassword(password)) {
            msg = "密码为6-16位数字字母组合!";
        } else if (!password.equals(repeatpwd)) {
            msg = "2次输入的密码不一致!";
        }
        if (msg.equals(SUCCESS)) {
            BusUser user = new BusUser();
            user.setId(userId);
            user.setPassword(SecurityUtil.passwordEncrypt(password));
            int row = userService.updateUserByPKSelective(user);
        }
        this.message = msg;
        return SUCCESS;
    }

    public String userDetailInit() {
        String userid = getParameter("userid");
        String usertype = getParameter("usertype");
        if (StringUtil.isNotNumber(userid, usertype)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        BusUser busUser = userService.selectUserByPrimaryKey(Integer.valueOf(userid));
        // 用户角色信息
        if (busUser != null) {
            SysRole role = roleService.selectRoleByUserid(busUser.getId());
            if (role != null) {
                busUser.setRoleid(role.getId());
                busUser.setRolename(role.getRolename());
            }
        }
        // 查询运营商信息或个人信息
        if (busUser != null) {
            UserTypeEnum userTypeEnum = UserTypeEnum.getEnmuByValue(Integer.parseInt(usertype));
            if (UserTypeEnum.PERSON == userTypeEnum) {
                BusUserInfo userinfo = userService.selectUserInfoByPrimaryKey(busUser.getInfoId());
                this.getRequest().setAttribute("userInfo", userinfo);
            } else if (UserTypeEnum.ENTERPRISE == userTypeEnum) {
                BusBussinessInfo businfo = userService.selectBussinessByPrimaryKey(busUser.getInfoId());
                this.getRequest().setAttribute("busInfo", businfo);
            }

        }
        this.getRequest().setAttribute("busUser", busUser);
        return SUCCESS;
    }

    /**
     * 查询帐号列表
     * 
     * @return
     * @throws Exception
     * @throws WriteException
     */
    public String queryList() throws Exception {
        WebUser user = getBmsWebuser();
        String keyword = this.getParameter("keyword");
        int usertype = this.getParamInt("usertype");
        String realstatus = this.getParameter("realstatus");
        String lockstatus = this.getParameter("lockstatus");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        int groupId = getParamInt("groupId");
        List<BusUser> userList = null;
        if (StringUtil.isNotNumber(realstatus, lockstatus)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (UserTypeEnum.COMPANY == user.getUsertype()) {
            groupId = user.getUserId();
            usertype = UserTypeEnum.PERSON.getValue();
        }
        if (usertype > 0) {
            params.put("usertype", usertype);
            if (usertype == UserTypeEnum.PERSON.getValue() && groupId > 0) {
                params.put("groupId", groupId);
            }
        }
        if (StringUtil.isNotEmpty(realstatus) && !"0".equals(realstatus)) {
            params.put("realstatus", Short.valueOf(realstatus));
        }
        if (StringUtil.isNotEmpty(lockstatus) && !"0".equals(lockstatus)) {
            params.put("islock", Short.valueOf(lockstatus));
        }
        if (isExportExcel()) {
            userList = userService.selectUserByPage(params);

            String[] headsName = { "用户编码", "用户昵称", "用户角色", "用户类型", "注册时间", "手机号码", "手机认证", "电子邮箱", "邮箱认证", "实名认证", "用户头像" };
            String[] properiesName = { "id", "username", "rolename", "userTypeDesc", "registTimeStr", "phone", "phoneStatusDesc", "email", "emailStatusDesc", "realStatusDesc",
                    "headImg" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(userList, properiesName, headsName, BusUser.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数格式不正确.";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            userList = userService.selectUserByPage(params);
            page.setRoot(userList);
            this.page = page;
            return SUCCESS;
        }
    }

    /**
     * 新增用户
     * 
     * @return
     */
    public String addUser() {
        String userJson = this.getParameter("busUser");
        Map<String, Object> validMap = validUserParams(userJson, true);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            BusUser busUser = (BusUser) validMap.get("busUser");
            BusGroupInfo busGroupInfo = null;
            if (!validUserDB(busUser)) {
                return SUCCESS;
            }
            if (!StringUtil.isEmpty(busUser.getPassword())) {
                busUser.setPassword(SecurityUtil.passwordEncrypt(busUser.getPassword()));
                busUser.setPayPassword(SecurityUtil.passwordEncrypt(busUser.getPassword()));
            } else {
                busUser.setPassword(SecurityUtil.passwordEncrypt(Globals.DEFAULT_PASSWORD));
                busUser.setPayPassword(SecurityUtil.passwordEncrypt(Globals.DEFAULT_PASSWORD));
            }

            SysRole sysRole = roleService.selectRoleByPK(busUser.getRoleid());

            if (RoleEnum.COMPANY.getValue() == sysRole.getType().intValue()) {
                busUser.setUserType(UserTypeEnum.COMPANY.getShortValue());
            } else {
                busUser.setUserType(UserTypeEnum.PLATFORM.getShortValue());
            }
            busUser.setHeadImg(Globals.DEFAULT_HEAD_URL);
            busUser.setRegistTime(new Date());
            busUser.setRegistIp(getRemoteIP());
            if (RoleEnum.GROUP.getValue() == sysRole.getType().intValue()) {// 集团管理员
                busGroupInfo = (BusGroupInfo) validMap.get("busGroupInfo");
                busUser.setUserType(UserTypeEnum.GROUP.getShortValue());
                busUser.setPhoneStatus(CertificateStatusEnum.PASSED.getShortValue());
                busUser.setRealStatus(CertificateStatusEnum.PASSED.getShortValue());
                userService.insertUser(busUser, UserTypeEnum.GROUP, RoleTypeEnum.GROUP, null, busGroupInfo);
            } else if (RoleEnum.COMPANY.getValue() == sysRole.getType().intValue()) {// 子公司
                userService.insertUser(busUser, UserTypeEnum.COMPANY, RoleTypeEnum.COMPANY, busUser.getRoleid(), busGroupInfo);
            } else {
                userService.insertUser(busUser, UserTypeEnum.PLATFORM, RoleTypeEnum.PLATFORM, busUser.getRoleid(), busGroupInfo);
            }

            // 添加充电卡用户操作日志
            LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.ADD, getRemoteIP(), "平台用户", "[" + busUser.getUsername()
                                                                                                                                                     + "]");
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 修改用户
     * 
     * @return
     */
    public String editUser() {
        String userJson = this.getParameter("busUser");
        String userid = this.getParameter("userid");
        int rebateId = getParamInt("rebateId");
        if (StringUtil.isEmpty(userJson) || !NumberUtils.isNumber(userid)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        Map<String, Object> validMap = validUserParams(userJson, false);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            BusUser busUser = (BusUser) validMap.get("busUser");
            busUser.setId(Integer.valueOf(userid));
            if (!validUserDB(busUser)) {
                return SUCCESS;
            }
            if (userService.updateUserAndRole(busUser, rebateId) > 0) {
                // 更新缓存里用户相关信息及权限
                ChargingCacheUtil.removeUser(userid);
                ChargingCacheUtil.removeUserModule(userid);
                ChargingCacheUtil.removeUserModuleBtn(userid);
                // 更新缓存里登录账号的信息
                WebUser webUser = ChargingCacheUtil.getSession(userid, KeySessionTypeEnum.BMS, null);
                if (webUser != null) {
                    webUser.setUserName(busUser.getUsername());
                    webUser.setPhone(busUser.getPhone());
                    webUser.setRoleId(busUser.getRoleid());
                    ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.BMS, null);
                }
                // 添加充电卡用户操作日志
                LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.MODIFY, getRemoteIP(), "平台用户",
                                           "[" + busUser.getUsername() + "]");
            }
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 禁用/激活用户
     * 
     * @return
     */
    public String delUser() {
        String userid = getParameter("userid");
        String isLock = getParameter("isLock");
        if (StringUtil.isNotNumber(userid)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        } else if (StringUtil.isNotNumber(isLock)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        BusUser record = new BusUser();
        record.setId(Integer.valueOf(userid));
        if (WhetherEnum.YES.getValue() == NumberUtils.toInt(isLock)) {
            record.setIsLock(WhetherEnum.NO.getShortValue());
        } else if (WhetherEnum.NO.getValue() == NumberUtils.toInt(isLock)) {
            record.setIsLock(WhetherEnum.YES.getShortValue());
        }

        if (userService.updateUserByPKSelective(record) > 0) {
            String moduleDesc = "禁止用户";
            ChargingCacheUtil.removieSession(userid, KeySessionTypeEnum.BMS, null);
            // 启用/禁用操作员操作日志
            if (WhetherEnum.YES.getValue() == NumberUtils.toInt(isLock)) {
                moduleDesc = "激活用户";
            }
            LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.MODIFY, getRemoteIP(), moduleDesc, "[userId:" + userid
                                                                                                                                                            + "]");
        } else {
            this.success = false;
            this.message = "删除用户失败.";
        }
        return SUCCESS;
    }

    /**
     * 初始化密码
     * 
     * @return
     */
    public String initPassword() {
        String userid = getParameter("userid");
        if (!NumberUtils.isNumber(userid)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        BusUser record = new BusUser();
        record.setId(Integer.valueOf(userid));
        record.setPassword(SecurityUtil.passwordEncrypt(Globals.DEFAULT_PASSWORD));
        if (userService.updateUserByPKSelective(record) > 0) {
            // 删除缓存，用户需重新登录
            ChargingCacheUtil.removieSession(userid, KeySessionTypeEnum.BMS, null);
        }
        return SUCCESS;
    }

    public String initRebateList() {
        return SUCCESS;
    }

    public String queryRebateList() throws Exception {
        String keyword = this.getParameter("keyword");
        int pageindex = getParamInt("pageindex");
        int pagelimit = getParamInt("pagelimit");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        List<BusRebate> rebateList = null;
        if (isExportExcel()) {
            rebateList = userService.selectRebateByPage(params);
            String[] headsName = { "优惠方案名称", "折扣", "优惠开始时间", "优惠结束时间", "添加时间", "状态" };
            String[] properiesName = { "rebateDesc", "rebate", "startTimeDesc", "endTimeDesc", "addTimeDesc", "isActiveDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(rebateList, properiesName, headsName, BusRebate.class);
            return null;
        } else {
            Page page = this.returnPage(pageindex, pagelimit);
            params.put(Globals.PAGE, page);
            rebateList = userService.selectRebateByPage(params);
            page.setRoot(rebateList);
            this.page = page;
            return SUCCESS;
        }

    }

    public String editRebateInit() {
        int rebateId = getParamInt("rebateId");
        BusRebate rebate = userService.selectRebateByPrimaryKey(rebateId);
        if (rebate == null) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        this.rebateBean = rebate;
        return SUCCESS;
    }

    /**
     * 删除优惠方案
     * 
     * @return
     */
    public String deleteRebate() {
        int rebateId = getParamInt("rebateId");
        BusRebate rebate = userService.selectRebateByPrimaryKey(rebateId);
        if (rebate == null) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        BusUserRebateExample emp = new BusUserRebateExample();
        BusUserRebateExample.Criteria cr = emp.createCriteria();
        cr.andRebateIdEqualTo(rebateId);
        int count = userService.countUserRebateByExample(emp);
        if (count > 0) {
            this.success = false;
            this.message = "优惠方案【" + rebate.getRebateDesc() + "】已产生绑定记录不能删除";
            return SUCCESS;
        }

        int count2 = userService.deleteRebateByPrimaryKey(rebateId);
        if (count2 > 0) {
            // 添加操作日志
            LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.DELETE, getRemoteIP(), LogOperatorEnum.DELETE.getTitle()
                                                                                                                                                + "优惠方案", JSON.toJSONString(rebate));
        } else {
            this.success = false;
            this.message = "删除失败";
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 添加修改优惠方案
     * 
     * @return
     */
    public String addOrEditRebate() {
        String rebateJson = getParameter("rebateJson");
        int opertype = getParamInt("opertype");
        BusRebate rebate = JsonToBean(rebateJson, BusRebate.class);
        checkRebateData(rebate, opertype);
        LogOperatorEnum logType = null;
        if (!success) {
            return SUCCESS;
        }
        int count = 0;
        if (Globals.REQUEST_TYPE_ADD == opertype) {// 添加
            logType = LogOperatorEnum.ADD;
            rebate.setAddTime(new Date());
            rebate.setUpdateTime(rebate.getAddTime());
            count = userService.insertRebate(rebate);
        } else if (Globals.REQUEST_TYPE_EDIT == opertype) {// 修改
            logType = LogOperatorEnum.MODIFY;
            rebate.setUpdateTime(new Date());
            count = userService.updateRebateByPrimaryKeySelective(rebate);
        }
        if (count > 0) {
            // 添加操作日志
            LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, logType, getRemoteIP(), logType.getTitle() + "优惠方案",
                                       JSON.toJSONString(rebate));
        }
        return SUCCESS;
    }

    private void checkRebateData(BusRebate rebate, int opertype) {
        if (Globals.REQUEST_TYPE_EDIT != opertype && Globals.REQUEST_TYPE_ADD != opertype) {
            success = false;
            message = "参数非法";
        } else if (rebate == null) {
            success = false;
            message = "参数非法";
        } else if (StringUtil.isEmpty(rebate.getRebateDesc())) {
            success = false;
            message = "优惠方案名称不能为空";
        } else if (rebate.getStartTime() == null) {
            success = false;
            message = "优惠方案开始时间不能为空";
        } else if (rebate.getEndTime() == null) {
            success = false;
            message = "优惠方案结束时间不能为空";
        } else if (rebate.getStartTime().getTime() >= rebate.getEndTime().getTime()) {
            success = false;
            message = "优惠结束时间必须大于优惠开始时间";
        }
        // 只能修改未激活的优惠方案
        if (Globals.REQUEST_TYPE_EDIT == opertype) {
            if (rebate.getId() == null || rebate.getId() <= 0) {
                success = false;
                message = "参数非法";
            }
            BusRebate oldRebate = userService.selectRebateByPrimaryKey(rebate.getId());
            if (oldRebate == null) {
                success = false;
                message = "参数非法";
            } else if (System.currentTimeMillis() >= oldRebate.getStartTime().getTime()) {
                success = false;
                message = "该方案已经激活不能修改";
            }
        }
    }

    /**
     * 校验用户信息
     * 
     * @param jsonObj
     * @return
     */
    private Map<String, Object> validUserParams(String jsonObj, boolean cheakPassword) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = Globals.DEFAULT_MESSAGE;
        BusUser busUser = null;
        BusGroupInfo busGroupInfo = null;
        JSONObject jsonBean = JSONObject.fromObject(jsonObj);
        int roleid = jsonBean.getInt("roleid");
        if (roleid == 5) {
            // user信息
            String userName = jsonBean.getString("username");
            String phone = jsonBean.getString("phone");

            // 集团信息
            String groupName = jsonBean.getString("groupName");
            int scale = jsonBean.getInt("scale");
            int province = jsonBean.getInt("province");
            int city = jsonBean.getInt("city");
            String tel = jsonBean.getString("tel");
            String domain = jsonBean.getString("domain");
            String address = jsonBean.getString("address");
            if (StringUtil.isEmpty(userName)) {
                msg = "用户昵称不能为空.";
            } else if (userName.length() < 3) {
                msg = "用户昵称长度不小于3个字符.";
            } else if (StringUtil.isNotEmpty(phone) && !Validator.isMobile(phone)) {
                msg = "用户手机号码格式不正确.";
            } else if (StringUtil.isEmpty(groupName)) {
                msg = "集团名称不能为空.";
            } else if (scale <= 0) {
                msg = "请选择集团规模.";
            } else if (province <= 0) {
                msg = "请选择省份.";
            } else if (city <= 0) {
                msg = "请选择市区.";
            } else if (StringUtil.isEmpty(tel) || !Validator.isMobile(tel)) {
                msg = "联系电话填写不正确.";
            } else if (StringUtil.isEmpty(domain)) {
                msg = "集团主营业务不能为空.";
            } else if (StringUtil.isEmpty(address)) {
                msg = "集团详细地址不能为空.";
            } else {
                busUser = new BusUser();
                busUser.setRoleid(roleid);
                busUser.setUsername(userName);
                busUser.setPhone(phone);

                busGroupInfo = new BusGroupInfo();
                busGroupInfo.setGroupName(groupName);
                busGroupInfo.setScale(String.valueOf(scale));
                busGroupInfo.setProvince(province);
                busGroupInfo.setCity(city);
                busGroupInfo.setDomain(domain);
                busGroupInfo.setPhone(tel);
                busGroupInfo.setAddress(address);
                busGroupInfo.setAddTime(new Date());
            }

        } else {
            busUser = this.JsonToBean(jsonObj, BusUser.class);

            if (StringUtil.isEmpty(busUser.getUsername())) {
                msg = "用户昵称不能为空.";
            } else if (busUser.getUsername().length() < 3) {
                msg = "用户昵称长度不小于3个字符.";
            } else if (busUser.getRoleid() == null) {
                msg = "角色不能为空.";
            } else if (StringUtil.isNotEmpty(busUser.getPhone()) && !Validator.isMobile(busUser.getPhone())) {
                msg = "手机号码格式不正确.";
            }
            if (cheakPassword) {
                if (StringUtil.isEmpty(busUser.getPassword())) {
                    msg = "密码不能为空.";
                } else if (busUser.getPassword().length() < 6) {
                    msg = "密码不能小于6个字符.";
                }
            }

        }

        map.put("busUser", busUser);
        map.put("busGroupInfo", busGroupInfo);
        map.put("msg", msg);
        return map;
    }

    /**
     * 校验数据库用户信息
     */
    private boolean validUserDB(BusUser busUser) {
        // 检验用户昵称是否重复
        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andUsernameEqualTo(busUser.getUsername());
        if (busUser.getId() != null) {
            cr.andIdNotEqualTo(busUser.getId());
        }
        List<BusUser> userList = userService.selectUserByExample(emp);
        if (userList != null && userList.size() > 0) {
            this.success = false;
            this.message = "用户昵称已经存在.";
            return false;
        }

        // 校验用户手机是否重复
        if (StringUtil.isNotEmpty(busUser.getPhone())) {
            emp.clear();
            cr = emp.createCriteria();
            cr.andUserTypeEqualTo(UserTypeEnum.PLATFORM.getShortValue());
            cr.andPhoneEqualTo(busUser.getPhone());
            if (busUser.getId() != null) {
                cr.andIdNotEqualTo(busUser.getId());
            }
            userList = userService.selectUserByExample(emp);
            if (userList != null && userList.size() > 0) {
                this.success = false;
                this.message = "手机号码已经存在.";
                return false;
            }
        }
        return true;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Page getPage() {
        return page;
    }

    public BusRebate getRebateBean() {
        return rebateBean;
    }

}
