package com.holley.charging.bjbms.frame.action;

import java.util.ArrayList;
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
import com.holley.common.util.SerializeCoderUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysButtondef;
import com.holley.platform.model.sys.SysModuledef;
import com.holley.platform.model.sys.SysModuledefExample;
import com.holley.platform.model.sys.SysRole;
import com.holley.platform.service.RoleService;
import com.holley.platform.util.CachedModuledefUtil;
import com.holley.platform.util.LogUtil;
import com.holley.web.common.util.Validator;

public class LoginAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private UserService       userService;
    private RoleService       roleService;
    private String            homepage;

    public String init() {
        WebUser webUser = getBmsWebuser();
        if (webUser != null) {
            return MEMBER;
        }
        return SUCCESS;
    }

    public String login() throws Exception {
        WebUser webUser = getBmsWebuser();
        if (webUser != null) {
            this.success = false;
            this.message = "isLogin";
            return SUCCESS;
        }
        String loginuser = this.getParameter("loginuser");// m登录帐号可为手机号或者邮箱
        String password = this.getParameter("password");
        String verifycode = this.getParameter("verifycode");
        short usertype = UserTypeEnum.PLATFORM.getShortValue();

        String imagecode = ChargingCacheUtil.getImgValidateCode(getSessionID());

        if (!validateParams(loginuser, password, verifycode)) {
            return SUCCESS;
        }

        if (!verifycode.equalsIgnoreCase(imagecode)) {
            this.success = false;
            this.message = "验证码不正确!";
            return SUCCESS;
        }
        List<Short> listUserType = new ArrayList<Short>();
        listUserType.add(UserTypeEnum.PLATFORM.getShortValue());
        listUserType.add(UserTypeEnum.COMPANY.getShortValue());
        loginuser = StringUtil.trim(loginuser);
        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andPhoneEqualTo(loginuser);
        // cr.andUserTypeEqualTo(usertype);
        cr.andUserTypeIn(listUserType);
        // emp.or().andEmailEqualTo(loginuser).andUserTypeEqualTo(usertype);
        emp.or().andUsernameEqualTo(loginuser).andUserTypeIn(listUserType);
        List<BusUser> userList = userService.selectUserByExample(emp);

        String errmsg = "";
        if (userList == null || userList.size() == 0) {
            errmsg = "用户名不存在!";
            return loginError(errmsg);
        }

        BusUser busUser = userList.get(0);
        if (WhetherEnum.YES.getShortValue().equals(busUser.getIsLock())) {
            errmsg = "账户已被禁用.";
            return loginError(errmsg);
        }

        LoginCountBean lcbean = ValidateUtil.initLogin(loginuser, KeySessionTypeEnum.BMS);
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
        ValidateUtil.clearLoginBean(loginuser, KeySessionTypeEnum.BMS);

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
        // user.setRoleType(RoleTypeEnum.PLATFORM.getShortValue());
        ChargingCacheUtil.setSession(user, KeySessionTypeEnum.BMS, null);
        this.success = true;

        saveToCookie(SecurityUtil.encrypt(Globals.COOKIE_BMS_USERID + getSessionID(), Globals.COOKIE_DESKEY),
                     SecurityUtil.encrypt(busUser.getId().toString(), Globals.COOKIE_DESKEY));

        loadPermission(user.getUserId(), user.getRoleId());
        // 记录登录日志
        LogUtil.recordLoginlog(user.getUserId(), getRemoteIP(), "成功登录后台系统", false);
        return SUCCESS;
    }

    private boolean validateParams(String loginuser, String password, String verifycode) {
        // if (loginuser == null
        // || !((loginuser.indexOf("@") == -1 && (Validator.isMobile(loginuser) || loginuser.length() <= 25)) ||
        // (loginuser.indexOf("@") != -1 && Validator.isEmail(loginuser)))) {
        // this.success = false;
        // this.message = "请正确填写手机或邮箱或昵称!";
        // return false;
        // }

        if (StringUtil.isEmpty(loginuser)) {
            this.success = false;
            this.message = "请正确填写昵称或手机号码!";
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
        return true;
    }

    private String loginError(String errmsg) {
        this.success = false;
        this.message = errmsg;
        return SUCCESS;
    }

    public String loginOut() throws Exception {
        String userid = getCookieByName(SecurityUtil.encrypt(Globals.COOKIE_BMS_USERID + getSessionID(), Globals.COOKIE_DESKEY));
        if (userid != null) {
            userid = SecurityUtil.decrypt(userid, Globals.COOKIE_DESKEY);
            ChargingCacheUtil.removieSession(userid, KeySessionTypeEnum.BMS, null);
            ChargingCacheUtil.removeUserModule(userid);
            ChargingCacheUtil.removeUserModuleBtn(userid);
        }
        return SUCCESS;
    }

    public String changePwd() {
        String oldPwd = this.getParameter("oldpwd");
        String newPwd = this.getParameter("newpwd");
        String repeatPwd = this.getParameter("repeatpwd");

        String userid = getSessionBmsUserId();
        BusUser busUser = userService.selectUserByPrimaryKey(Integer.valueOf(userid));

        message = checkChangePwd(busUser, oldPwd, newPwd, repeatPwd);
        if (!Globals.DEFAULT_MESSAGE.equals(message)) {
            success = false;
            return SUCCESS;
        }
        BusUser user = new BusUser();
        user.setId(busUser.getId());
        user.setPassword(SecurityUtil.passwordEncrypt(newPwd));
        int row = userService.updateUserByPKSelective(user);
        if (row > 0) {
            // 修改密码成功重新登录
            ChargingCacheUtil.removieSession(userid, KeySessionTypeEnum.BMS, null);
        } else {
            success = false;
            message = "密码修改失败.";
        }
        return SUCCESS;
    }

    private String checkChangePwd(BusUser busUser, String oldPwd, String newPwd, String repeatPwd) {
        String msg = Globals.DEFAULT_MESSAGE;
        if (StringUtil.isEmpty(oldPwd)) {
            msg = "旧密码不能为空.";
        } else if (StringUtil.isEmpty(newPwd)) {
            msg = "新密码不能为空.";
        } else if (StringUtil.isEmpty(repeatPwd)) {
            msg = "确认密码不能为空.";
        } else if (!busUser.getPassword().equals(SecurityUtil.passwordEncrypt(oldPwd))) {
            msg = "登录原密码不正确.";
        } else if (!newPwd.equals(repeatPwd)) {
            msg = "确认密码不一致.";
        } else if (!Validator.isPassword(newPwd)) {
            msg = " 密码为6-16位数字字母组合!";
        }
        return msg;
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
        List<SysModuledef> subMenuList = CachedModuledefUtil.getSubMenu(moduleList);
        if (subMenuList != null && subMenuList.size() > 0) {
            this.homepage = subMenuList.get(0).getUrl();
        }

        // 设置缓存里的用户功能按钮权限
        List<SysButtondef> buttonList;
        if (roleid == Globals.ADMIN_ROLE_ID) {
            buttonList = roleService.selectButtondefBySystemid((short) Globals.PLATFORM_SYSTEM_ID);
        } else {
            buttonList = roleService.selectButtondefByUserid(userid);
        }
        ChargingCacheUtil.setUserModuleBtn(userid.toString(), SerializeCoderUtil.serializeList(buttonList));
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

}
