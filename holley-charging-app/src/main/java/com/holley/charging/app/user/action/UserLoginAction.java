package com.holley.charging.app.user.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.app.util.AppTool;
import com.holley.charging.app.util.rong.ApiHttpClient;
import com.holley.charging.app.util.rong.FormatType;
import com.holley.charging.app.util.rong.RongCloudConfig;
import com.holley.charging.app.util.rong.SdkHttpResult;
import com.holley.charging.common.ValidateUtil;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.bus.BusUserToken;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.CacheKeyProvide.KeyMsgCodeTypeEnum;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.ResultBean;
import com.holley.common.constants.app.ErrorCodeConstants;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.LoginCountBean;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.LogUtil;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.Validator;

import net.sf.json.JSONObject;

public class UserLoginAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(UserLoginAction.class);
    private static final long   serialVersionUID = 1L;
    private UserService         userService;

    private String              userkey;
    private ResultBean          resultBean       = new ResultBean();

    public String init() {
        return SUCCESS;
    }

    public String login() throws Exception {
        String loginuser = this.getAttribute("loginuser");// 登录帐号为手机号
        String password = this.getAttribute("password");
        String verifycode = this.getAttribute("validatecode");

        String imagecode = ChargingCacheUtil.getImgValidateCode(getSessionID());

        if (!validateParams(loginuser, password, verifycode)) {
            return SUCCESS;
        }

        if (!verifycode.equalsIgnoreCase(imagecode)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_LOG_VALIDATEWORING);
            resultBean.setMessage("验证码不正确!");
            return SUCCESS;
        }

        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andPhoneEqualTo(loginuser);
        cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        emp.or().andEmailEqualTo(loginuser).andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        List<BusUser> userList = userService.selectUserByExample(emp);

        String errmsg = "";
        if (userList == null || userList.size() == 0) {
            errmsg = "用户未注册";
            return loginError(errmsg);
        }

        BusUser busUser = userList.get(0);
        if (WhetherEnum.YES.getShortValue().equals(busUser.getIsLock())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_USRLOCK);
            resultBean.setMessage("账户已被锁定，不能正常登录!");
            return SUCCESS;
        }

        LoginCountBean lcbean = ValidateUtil.initLogin(loginuser, KeySessionTypeEnum.APP);
        if (lcbean.isRefuseLogin()) {
            return loginError(lcbean.getLoginFailMsg());
        }

        if (!SecurityUtil.passwordEncrypt(password).equals(busUser.getPassword())) {
            if (lcbean.getReTryCount() > 0) {
                errmsg = "密码错误,还剩" + lcbean.getReTryCount() + "次正确输入机会";
            } else {
                errmsg = "用户登录失败已" + Globals.LOGIN_FAIL_TOTAL + "次，请" + Globals.LOGIN_INTERVAL + "分钟后重试";
            }
            LogUtil.recordRefuselog(busUser.getId(), null, "登录密码错误", true);
            return loginError(errmsg);
        }

        // 清除登录失败次数
        ValidateUtil.clearLoginBean(loginuser, KeySessionTypeEnum.APP);

        // 返回加密的userkey
        String securityKey = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RULE_SYS_SECURIRY_DOWN_KEY);
        this.userkey = SecurityUtil.encrypt(busUser.getId().toString(), securityKey);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("userkey", this.userkey);

        // 返回用户融云token
        BusUserToken record = userService.selectUserTokenByPK(busUser.getId());
        String token = null;
        if (record == null) {
            token = rongusertoken(busUser.getId());
        } else {
            token = record.getToken();
        }
        data.put("rongtoken", token);
        resultBean.setData(data);

        // 记录登录日志
        LogUtil.recordLoginlog(busUser.getId(), null, "成功登录APP", true);
        return SUCCESS;
    }

    /**
     * 通过手机号发送验证码来修改密码，用户填写验证码和修改后的密码后提交后台修改。
     * 
     * @return
     */
    public String modifypassword() {
        String mobile = this.getAttribute("mobile");
        String password = this.getAttribute("password");
        String password2 = this.getAttribute("password2");
        String validatecode = this.getAttribute("validatecode");

        // 验证用户信息
        if (!validateParams(mobile, password, password2, validatecode)) {
            return SUCCESS;
        }

        String realpcode = ChargingCacheUtil.getMessageValidateCode(getSessionID(), KeyMsgCodeTypeEnum.RESETPWD);
        if (realpcode == null || !realpcode.equals(validatecode)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_FINDPW_VALIDATEWORING);
            resultBean.setMessage("短信验证码不正确!");
            return SUCCESS;
        }

        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andPhoneEqualTo(mobile);
        cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        List<BusUser> userList = userService.selectUserByExample(emp);
        if (userList == null || userList.size() == 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_NOUSR);
            resultBean.setMessage("用户不存在!");
            return SUCCESS;
        }

        // -----修改密码
        String pwd = SecurityUtil.passwordEncrypt(password);
        BusUser user = userList.get(0);
        user.setPassword(pwd);
        // user.setPayPassword(pwd);
        if (userService.updateUserByPKSelective(user) > 0) {
            // 清除登录失败次数
            ValidateUtil.clearLoginBean(mobile, KeySessionTypeEnum.APP);
            LogUtil.recordDocumentlog(user.getId(), LogOperatorEnum.MODIFY, null, "修改登录密码", "", true);
        }
        return SUCCESS;
    }

    private boolean validateParams(String mobile, String password, String password2, String validatecode) {
        if (StringUtil.isEmpty(mobile) || !Validator.isMobile(mobile)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("请正确填写11位手机号!");
            return false;
        }

        if (password == null || password == "" || !Validator.isPassword(password)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("密码为6-16位数字字母组合!");
            return false;
        }

        if (!password2.equals(password)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("请保持两次密码输入一致!");
            return false;
        }

        if (StringUtil.isEmpty(validatecode)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("请输入短信验证码!");
            return false;
        }

        return true;
    }

    private boolean validateParams(String loginuser, String password, String verifycode) {
        if (StringUtil.isEmpty(loginuser) || !Validator.isMobile(loginuser)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("请正确填写11位手机号!");
            return false;
        }

        if (password == null || password.equals("") || !Validator.isPassword(password)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("密码为6-16位数字字母组合!");
            return false;
        }

        if (StringUtil.isEmpty(verifycode)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("请输入验证码!");
            return false;
        }

        return true;
    }

    private String loginError(String errmsg) {
        resultBean.setSuccess(false);
        resultBean.setErrorCode(ErrorCodeConstants.ERR_LOG_USERPWWORING);
        resultBean.setMessage(errmsg);
        return SUCCESS;
    }

    /**
     * 获取用户融云token
     * 
     * @param userid
     * @return
     */
    private String rongusertoken(Integer userid) {
        BusUser user = userService.selectUserByCache(userid);
        String memberid = encrypt(user.getId().toString());
        String username = user.getUsername();
        String headimg = user.getHeadImg();
        try {
            logger.info("-----rongcloud-----获取token开始-------");
            SdkHttpResult result = ApiHttpClient.getToken(RongCloudConfig.appKey, RongCloudConfig.appSecret, memberid, username, headimg, FormatType.json);
            JSONObject jsonobj = JSONObject.fromObject(result.getResult());
            String code = jsonobj.getString("code");
            String userId = jsonobj.getString("userId");
            String token = jsonobj.getString("token");
            logger.info("-----rongcloud-----获取token返回结果--code=" + code + ",userId=" + userId + ",token=" + token + "-----");
            if ("200".equals(code)) {// 正常
                if (AppTool.isNull(userId, token)) {
                    return null;
                }
                userId = decrypt(userId);
                if (NumberUtils.isNumber(userId)) {
                    BusUserToken record = new BusUserToken();
                    record.setUserid(Integer.valueOf(userId));
                    record.setToken(token);

                    BusUserToken userToken = userService.selectUserTokenByPK(Integer.valueOf(userId));
                    if (userToken == null) {
                        if (userService.insertUserToken(record) > 0) {
                            LogUtil.recordDocumentlog(Integer.valueOf(userId), LogOperatorEnum.ADD, null, "获取融云token", "{user:" + userId + ",token:" + token + "}", true);
                        }
                    } else {
                        if (userService.updateUserTokenByPK(record) > 0) {
                            LogUtil.recordDocumentlog(Integer.valueOf(userId), LogOperatorEnum.MODIFY, null, "获取融云token", "{user:" + userId + ",token:" + token + "}", true);
                        }
                    }
                    return token;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

}
