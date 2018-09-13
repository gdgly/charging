package com.holley.charging.website.action;

import java.util.Date;
import java.util.List;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.ValidateUtil;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.LoginCountBean;
import com.holley.common.dataobject.WebUser;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.DateUtil;
import com.holley.common.util.SerializeCoderUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysButtondef;
import com.holley.platform.model.sys.SysModuledef;
import com.holley.platform.model.sys.SysModuledefExample;
import com.holley.platform.model.sys.SysRole;
import com.holley.platform.service.RoleService;
import com.holley.platform.util.LogUtil;
import com.holley.web.common.util.Validator;

public class UserLoginAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private UserService       userService;
    private RoleService       roleService;

    public String init() {
        WebUser WebUser = getWebuser();
        if (WebUser != null && WebUser.getUsertype() == UserTypeEnum.ENTERPRISE) {
            return "bussiness";
        } else if (WebUser != null && WebUser.getUsertype() == UserTypeEnum.PERSON) {
            return "user";
        }
        return SUCCESS;
    }

    public String login() throws Exception {
        WebUser WebUser = getWebuser();
        if (WebUser != null) {
            this.success = false;
            this.message = "isLogin";
            return SUCCESS;
        }

        String usertype = this.getParameter("usertype");
        String loginuser = this.getParameter("loginuser");// m登录帐号可为手机号或者邮箱
        String password = this.getParameter("password");
        String verifycode = this.getParameter("verifycode");

        String imagecode = ChargingCacheUtil.getImgValidateCode(getSessionID());

        if (!validateParams(loginuser, password, verifycode, usertype)) {
            return SUCCESS;
        }

        if (!verifycode.equalsIgnoreCase(imagecode)) {
            this.success = false;
            this.message = "验证码不正确!";
            return SUCCESS;
        }

        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        List<BusUser> userList = null;
        if (Validator.isMobile(loginuser) || Validator.isEmail(loginuser)) {
            cr.andPhoneEqualTo(loginuser);
            cr.andUserTypeEqualTo(Short.valueOf(usertype));
            emp.or().andEmailEqualTo(loginuser).andUserTypeEqualTo(Short.valueOf(usertype));
            userList = userService.selectUserByExample(emp);
        } else if (UserTypeEnum.ENTERPRISE.getShortValue() == Short.valueOf(usertype)) {
            cr.andUsernameEqualTo(loginuser);
            cr.andUserTypeEqualTo(Short.valueOf(usertype));
            userList = userService.selectUserByExample(emp);
        } else {
            cr.andUsernameEqualTo(loginuser);
            userList = userService.selectUserByExample(emp);
        }

        String errmsg = "";
        if (userList == null || userList.size() == 0) {
            errmsg = "用户名不存在";
            return loginError(errmsg);
        }

        BusUser busUser = userList.get(0);
        if (UserTypeEnum.PERSON.getShortValue() == Short.valueOf(usertype)) {
            if ((UserTypeEnum.PERSON.getShortValue() != busUser.getUserType()) && (UserTypeEnum.GROUP.getShortValue() != busUser.getUserType())) {
                errmsg = "用户名不存在";
                return loginError(errmsg);
            }
        }

        if (WhetherEnum.YES.getShortValue().equals(busUser.getIsLock())) {
            errmsg = "账户已被禁用.";
            return loginError(errmsg);
        }

        LoginCountBean lcbean = ValidateUtil.initLogin(loginuser, KeySessionTypeEnum.WEB);
        if (lcbean.isRefuseLogin()) {
            return loginError(lcbean.getLoginFailMsg());
        }

        if (!SecurityUtil.passwordEncrypt(password).equals(busUser.getPassword())) {
            if (lcbean.getReTryCount() > 0) {
                errmsg = "密码错误,还剩" + lcbean.getReTryCount() + "次正确输入机会";
            } else {
                errmsg = "用户登录失败已" + Globals.LOGIN_FAIL_TOTAL + "次，请" + Globals.LOGIN_INTERVAL + "分钟后重试.";
            }
            LogUtil.recordRefuselog(busUser.getId(), getRemoteIP(), "登录密码错误", false);
            return loginError(errmsg);
        }

        // 清除登录失败次数
        ValidateUtil.clearLoginBean(loginuser, KeySessionTypeEnum.WEB);

        WebUser user = new WebUser();
        user.setUserId(busUser.getId());
        user.setUserName(busUser.getUsername());
        user.setUsertype(UserTypeEnum.getEnmuByValue(busUser.getUserType().intValue()));
        user.setIp(getRemoteIP());
        user.setLoginDate(new Date());
        user.setEmail(StringUtil.defaultIfNull(busUser.getEmail(), ""));
        user.setEamilStatus(busUser.getEmailStatus());
        user.setPhone(StringUtil.defaultIfNull(busUser.getPhone(), ""));
        user.setPhoneStatus(busUser.getPhoneStatus());
        user.setInfoId(busUser.getInfoId());
        user.setRealStatus(busUser.getRealStatus());
        user.setRegistTime(busUser.getRegistTime());
        user.setHeadImg(StringUtil.defaultIfNull(busUser.getHeadImg(), Globals.DEFAULT_HEAD_URL));
        SysRole sysRole = roleService.selectRoleByUserid(busUser.getId());
        if (sysRole != null) {
            user.setRoleId(sysRole.getId());
            user.setRoleType(sysRole.getType());
        }
        long sessionTime = System.currentTimeMillis();
        String webUserKey = Globals.COOKIE_USERID + "_" + sessionTime + "_" + busUser.getId();
        ChargingCacheUtil.setSession(user, KeySessionTypeEnum.WEB, webUserKey);
        this.success = true;
        // 修改登录COOKIE
        String sessionUserKey = Globals.SESSION_USERID_MAP.get(Globals.KEY_SESSION_USER + busUser.getId());
        if (sessionUserKey != null) {
            // Globals.SESSION_USERID_MAP.put(Globals.KEY_SESSION_MSG + busUser.getId(), DateUtil.DateToLongStr(new
            // Date()) + " 您的账户在别处登录...");
            Globals.SESSION_USERID_MAP.put(sessionUserKey, DateUtil.DateToLongStr(new Date()) + " 您的账户在别处登录...");
            ChargingCacheUtil.removieSession(null, KeySessionTypeEnum.WEB, sessionUserKey);
        }
        saveToCookie(SecurityUtil.encrypt(Globals.COOKIE_SESSION_USERID + getSessionID(), Globals.COOKIE_DESKEY), SecurityUtil.encrypt(webUserKey, Globals.COOKIE_DESKEY));
        saveToCookie(SecurityUtil.encrypt(Globals.COOKIE_USERID + getSessionID(), Globals.COOKIE_DESKEY), SecurityUtil.encrypt(busUser.getId().toString(), Globals.COOKIE_DESKEY));
        Globals.SESSION_USERID_MAP.put(Globals.KEY_SESSION_USER + busUser.getId(), webUserKey);
        // 加载用户功能和按钮权限
        loadPermission(user.getUserId(), user.getRoleId());

        // 记录登录日志
        LogUtil.recordLoginlog(user.getUserId(), getRemoteIP(), "成功登录系统", false);
        return SUCCESS;
    }

    private boolean validateParams(String loginuser, String password, String verifycode, String usertype) {

        // if (loginuser == null || !((loginuser.indexOf("@") == -1 && Validator.isMobile(loginuser)) ||
        // (loginuser.indexOf("@") != -1 && Validator.isEmail(loginuser)))) {
        // this.success = false;
        // this.message = "请正确填写手机或邮箱帐号或登录名!";
        // return false;
        // }
        if (StringUtil.isEmpty(loginuser)) {
            this.success = false;
            this.message = "请正确填写手机或邮箱帐号或登录名!";
            return false;
        }
        if (password == null || password.equals("") || !Validator.isPassword(password)) {
            this.success = false;
            this.message = "密码为6-16位数字字母组合!";
            return false;
        }

        if (StringUtil.isEmpty(verifycode)) {
            this.success = false;
            this.message = "请输入验证码!";
            return false;
        }

        if (StringUtil.isEmpty(usertype)) {
            this.success = false;
            this.message = "请选择用户类型!";
            return false;
        }
        return true;
    }

    private String loginError(String errmsg) {
        this.success = false;
        this.message = errmsg;
        return SUCCESS;
    }

    public String loginOut() throws Exception {
        // String userid = getCookieByName(Globals.COOKIE_USERID);
        String sessionUserKey = getSessionUserId();
        WebUser webUser = this.getWebuser();
        if (sessionUserKey != null && webUser != null) {
            // userid = SecurityUtil.decrypt(userid, Globals.COOKIE_DESKEY);
            Globals.SESSION_USERID_MAP.remove(Globals.KEY_SESSION_USER + webUser.getUserId());
            LogUtil.recordLoginlog(webUser.getUserId(), getRemoteIP(), "退出系统.", false);
            ChargingCacheUtil.removieSession(null, KeySessionTypeEnum.WEB, sessionUserKey);
            ChargingCacheUtil.removeUserModule(webUser.getUserId().toString());
            ChargingCacheUtil.removeUserModuleBtn(webUser.getUserId().toString());
        }
        return SUCCESS;
    }

    private void loadPermission(Integer userid, Integer roleid) {
        // 设置缓存里的用户功能权限
        List<SysModuledef> moduleList;
        if (roleid == Globals.ADMIN_ROLE_ID) {
            SysModuledefExample memp = new SysModuledefExample();
            SysModuledefExample.Criteria mcr = memp.createCriteria();
            mcr.andSystemidEqualTo((short) Globals.PLATFORM_SYSTEM_ID);
            memp.setOrderByClause("SORTNO");
            moduleList = roleService.selectModuledefByExample(memp);
        } else {
            moduleList = roleService.selectModuledefByUserid(userid);
        }
        ChargingCacheUtil.setUserModule(userid.toString(), SerializeCoderUtil.serializeList(moduleList));

        // 设置缓存里的用户功能按钮权限
        List<SysButtondef> buttonList;
        if (roleid == Globals.ADMIN_ROLE_ID) {
            buttonList = roleService.selectButtondefBySystemid((short) Globals.PLATFORM_SYSTEM_ID);
        } else {
            buttonList = roleService.selectButtondefByUserid(userid);
        }
        ChargingCacheUtil.setUserModuleBtn(userid.toString(), SerializeCoderUtil.serializeList(buttonList));
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
