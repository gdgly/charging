package com.holley.charging.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.AlidayuSendPhone;

public class AppLoadListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // 初始化短信接口参数start
        String appKey = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_APP_KEY);
        String appSecret = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_APP_SECRET);
        String sendUrl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_SEND_URL);
        String signTitle = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_SIGN_TITLE);
        String template = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_TEMPLATE);
        AlidayuSendPhone.init(appKey, appSecret, sendUrl, template, signTitle);
        // 初始化短信接口参数end
    }
}
