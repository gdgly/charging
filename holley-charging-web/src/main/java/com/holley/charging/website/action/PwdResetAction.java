package com.holley.charging.website.action;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.ValidateUtil;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.CacheKeyProvide.KeyMsgCodeTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.ResultBean;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.RandomUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.service.MessageService;
import com.holley.platform.util.MsgUtil;
import com.holley.web.common.util.Validator;

public class PwdResetAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private UserService       userService;
    private MessageService    messageService;
    private static final Log  logger           = LogFactory.getLog(PwdResetAction.class);

    public String init() {
        String usertype = this.getParameter("usertype");
        this.getRequest().setAttribute("usertype", usertype);
        return SUCCESS;
    }

    /**
     * 发送短信验证码
     * 
     * @return
     */
    public String sendPhoneCode() {
        String phone = this.getParameter("mobile");
        String usertype = this.getParameter("usertype");
        if (StringUtil.isEmpty(phone)) {
            this.success = false;
            this.message = "手机号码为空!";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(usertype)) {
            this.success = false;
            this.message = "用户类型为空!";
            return SUCCESS;
        }

        // 注册验证
        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andPhoneEqualTo(phone);
        cr.andUserTypeEqualTo(Short.valueOf(usertype));
        List<BusUser> userList = userService.selectUserByExample(emp);
        if (userList == null || userList.size() == 0) {
            this.success = false;
            this.message = "不存在该用户!";
            return SUCCESS;
        }
        ResultBean bean = ValidateUtil.checkCanGetMobileCode(this.getSessionID(), KeyMsgCodeTypeEnum.RESETPWD);
        if (bean.isSuccess()) {
            String randomCode = RandomUtil.getRandomNumber(4);
            boolean result = MsgUtil.sendSMS(phone, randomCode);
            if (result) {
                // 存储验证码
                ChargingCacheUtil.setMessageValidateCode(this.getSessionID(), randomCode, KeyMsgCodeTypeEnum.RESETPWD);
                this.success = true;
                this.message = "短信发送成功!";
            } else {
                this.success = false;
                this.message = "短信发送失败!";
            }
        } else {
            this.success = false;
            this.message = bean.getMessage();
        }
        return SUCCESS;
    }

    /**
     * 重置密码
     * 
     * @return
     * @throws Exception
     */
    public String resetPwd() throws Exception {
        String phone = this.getParameter("mobile");
        String password = this.getParameter("password");
        String phonecode = this.getParameter("phonecode");
        String usertype = this.getParameter("usertype");

        // 验证注册信息
        if (!validateParams(phone, password, phonecode, usertype)) {
            return SUCCESS;
        }
        String realpcode = ChargingCacheUtil.getMessageValidateCode(getSessionID(), KeyMsgCodeTypeEnum.RESETPWD);

        if (realpcode == null || !realpcode.equals(phonecode)) {
            this.success = false;
            this.message = "短信验证码不正确!";
            return SUCCESS;
        }

        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andPhoneEqualTo(phone);
        cr.andUserTypeEqualTo(Short.valueOf(usertype));
        List<BusUser> userList = userService.selectUserByExample(emp);
        if (userList == null || userList.size() == 0) {
            this.success = false;
            this.message = "不存在该用户!";
            return SUCCESS;
        }

        // 用户
        String pwd = SecurityUtil.passwordEncrypt(password);
        BusUser user = userList.get(0);
        user.setPassword(pwd);

        // 插入用户角色
        int row = userService.updateUserByPKSelective(user);
        if (row > 0) {
            this.success = true;
            this.message = "重置密码成功!";
        } else {
            this.success = false;
            this.message = "充值密码失败!";
        }

        return SUCCESS;
    }

    private boolean validateParams(String phone, String password, String phonecode, String usertype) {
        if (StringUtil.isEmpty(phone) || !Validator.isMobile(phone)) {
            this.success = false;
            this.message = "请正确填写11位手机号!";
            return false;
        }

        if (password == null || password == "" || !Validator.isPassword(password)) {
            this.success = false;
            this.message = "密码为6-16位数字字母组合!";
            return false;
        }

        if (StringUtil.isEmpty(phonecode)) {
            this.success = false;
            this.message = "请输入短信验证码!";
            return false;
        }

        if (StringUtil.isEmpty(usertype)) {
            this.success = false;
            this.message = "请选择用户类型!";
            return false;
        }
        return true;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public static void main(String[] args) {
        System.out.println(SecurityUtil.passwordEncrypt("123456"));
    }
}
