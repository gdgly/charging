package com.holley.charging.app.user.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.ValidateUtil;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.CacheKeyProvide.KeyMsgCodeTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.ResultBean;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.app.ErrorCodeConstants;
import com.holley.common.constants.charge.CertificateStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.RandomUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.MsgUtil;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.Validator;

public class UserRegisterAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(UserRegisterAction.class);
    private static final long   serialVersionUID = 1L;
    private UserService         userService;

    private String              userkey;
    private ResultBean          resultBean       = new ResultBean();

    public String init() {
        return SUCCESS;
    }

    /**
     * 发送短信验证码
     * 
     * @return
     */
    public String messagecode() {
        String mobile = this.getAttribute("mobile");
        String type = this.getAttribute("type");
        if (StringUtil.isEmpty(mobile)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_MSGVAL_MOBILEERR);
            resultBean.setMessage("手机号码为空!");
            return SUCCESS;
        }
        if (!NumberUtils.isNumber(type)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法");
            return SUCCESS;
        }

        KeyMsgCodeTypeEnum codetype = KeyMsgCodeTypeEnum.getEnmuByValue(Integer.valueOf(type));
        if (codetype == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法");
            return SUCCESS;
        }

        ResultBean bean = null;
        BusUserExample emp;
        List<BusUser> userList;
        if (KeyMsgCodeTypeEnum.REGISTER == codetype) {// 注册
            // 注册验证
            emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andPhoneEqualTo(mobile);
            cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
            userList = userService.selectUserByExample(emp);
            if (userList != null && userList.size() > 0) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_MSGVAL_HAVEUSER);
                resultBean.setMessage("手机号码已被注册!");
                return SUCCESS;
            }
            bean = ValidateUtil.checkCanGetMobileCode(this.getSessionID(), KeyMsgCodeTypeEnum.REGISTER);
        } else if (KeyMsgCodeTypeEnum.RESETPWD == codetype || KeyMsgCodeTypeEnum.CASH == codetype) {// 找回密码
            emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andPhoneEqualTo(mobile);
            cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
            userList = userService.selectUserByExample(emp);
            if (userList == null || userList.size() == 0) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_MSGVAL_MOBILEERR);
                resultBean.setMessage("手机号码为空或不符合要求!");
                return SUCCESS;
            }
            if (KeyMsgCodeTypeEnum.RESETPWD == codetype) {
                bean = ValidateUtil.checkCanGetMobileCode(this.getSessionID(), KeyMsgCodeTypeEnum.RESETPWD);
            } else {
                bean = ValidateUtil.checkCanGetMobileCode(this.getSessionID(), KeyMsgCodeTypeEnum.CASH);
            }
        }
        if (bean != null && bean.isSuccess()) {
            String randomCode = RandomUtil.getRandomNumber(4);
            boolean result = MsgUtil.sendSMS(mobile, randomCode);
            logger.info("mobile=" + mobile + ",randomCode=" + randomCode);
            if (result) {
                // 存储验证码
                if (KeyMsgCodeTypeEnum.REGISTER == codetype) {
                    ChargingCacheUtil.setMessageValidateCode(this.getSessionID(), randomCode, KeyMsgCodeTypeEnum.REGISTER);
                } else if (KeyMsgCodeTypeEnum.RESETPWD == codetype) {
                    ChargingCacheUtil.setMessageValidateCode(this.getSessionID(), randomCode, KeyMsgCodeTypeEnum.RESETPWD);
                } else if (KeyMsgCodeTypeEnum.CASH == codetype) {
                    ChargingCacheUtil.setMessageValidateCode(this.getSessionID(), randomCode, KeyMsgCodeTypeEnum.CASH);
                }
                resultBean.setSuccess(true);
                resultBean.setMessage("短信发送成功!");
            } else {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
                resultBean.setMessage("短信发送失败!");
            }
        } else {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_MSGVAL_REPEAT);
            resultBean.setMessage(bean.getMessage());
        }
        return SUCCESS;
    }

    /**
     * 注册
     * 
     * @return
     * @throws Exception
     */
    public String register() throws Exception {
        String phone = this.getAttribute("mobile");
        String password = this.getAttribute("password");
        String password2 = this.getAttribute("password2");
        String validatecode = this.getAttribute("validatecode");

        // 验证注册信息
        if (!validateParams(phone, password, password2, validatecode)) {
            return SUCCESS;
        }

        String realpcode = ChargingCacheUtil.getMessageValidateCode(getSessionID(), KeyMsgCodeTypeEnum.REGISTER);

        if (realpcode == null || !realpcode.equals(validatecode)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_REG_VALIDATEWORING);
            resultBean.setMessage("短信验证码不正确!");
            return SUCCESS;
        }

        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andUserTypeEqualTo(UserTypeEnum.PERSON.getShortValue());
        cr.andPhoneEqualTo(phone);
        List<BusUser> userList = userService.selectUserByExample(emp);
        if (userList != null && userList.size() > 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_REG_HAVEUSER);
            resultBean.setMessage("手机号码已被注册!");
            return SUCCESS;
        }

        // 用户
        String pwd = SecurityUtil.passwordEncrypt(password);
        BusUser user = new BusUser();
        user.setUsername(System.currentTimeMillis() + "");
        user.setPassword(pwd);
        user.setPayPassword(pwd);
        user.setPhone(phone);
        user.setPhoneStatus(CertificateStatusEnum.PASSED.getShortValue());
        user.setEmailStatus(CertificateStatusEnum.FAILED.getShortValue());
        user.setUserType(UserTypeEnum.PERSON.getShortValue()); // 默认个人
        user.setRegistTime(new Date());
        user.setRegistIp(getRemoteIP());
        user.setHeadImg(Globals.DEFAULT_HEAD_URL);// 默认头像
        user.setRealStatus(CertificateStatusEnum.FAILED.getShortValue());

        boolean res = userService.insertUser(user, UserTypeEnum.PERSON, RoleTypeEnum.PERSON, null, null);
        if (!res) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
            resultBean.setMessage("注册失败，请稍后重试!");
            return SUCCESS;
        }

        String securityKey = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RULE_SYS_SECURIRY_DOWN_KEY);
        this.userkey = SecurityUtil.encrypt(user.getId().toString(), securityKey);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("userkey", this.userkey);
        resultBean.setData(data);
        return SUCCESS;
    }

    private boolean validateParams(String phone, String password, String password2, String validatecode) {
        if (StringUtil.isEmpty(phone) || !Validator.isMobile(phone)) {
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

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

}
