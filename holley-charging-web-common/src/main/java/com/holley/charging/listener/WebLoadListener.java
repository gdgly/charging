package com.holley.charging.listener;

import java.math.BigDecimal;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.holley.common.cache.RedisUtil;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.security.Base64;
import com.holley.common.util.StringUtil;
import com.holley.platform.service.MessageService;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.AlidayuSendPhone;
import com.holley.web.common.util.MailUtil;

public class WebLoadListener implements ServletContextListener {

    private final static Logger logger = Logger.getLogger(WebLoadListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {

    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ServletContext context = event.getServletContext();
        context.setAttribute("webroot", context.getContextPath());
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        RoleUtil.removeRule(RoleUtil.MAX_CHARGECARD_LIMIT);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        context.setAttribute("rate", rate);
        /*
         * ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
         * DictionaryService dictionaryService = (DictionaryService) ctx.getBean("dictionaryService"); SysDefAreaExample
         * emp = new SysDefAreaExample(); SysDefAreaExample.Criteria cr = emp.createCriteria(); cr.andPidEqualTo(0);
         * List<SysDefArea> provinceList = dictionaryService.selectByAreaExample(emp); context.setAttribute("provinces",
         * provinceList);
         */
        String key = CacheKeyProvide.getKey(CacheKeyProvide.KEY_RULE, RoleUtil.IMG_URL);
        ChargingCacheUtil.delKey(key);
        String key2 = CacheKeyProvide.getKey(CacheKeyProvide.KEY_RULE, RoleUtil.DATA_PATH);
        ChargingCacheUtil.delKey(key2);
        String imgUrl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL);
        context.setAttribute(Globals.IMG_URL, imgUrl);

        // 初始化短信接口参数start
        String appKey = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_APP_KEY);
        String appSecret = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_APP_SECRET);
        String sendUrl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_SEND_URL);
        String signTitle = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_SIGN_TITLE);
        String template = RoleUtil.selectRuleByPrimaryKey(RoleUtil.SMS_TEMPLATE);
        AlidayuSendPhone.init(appKey, appSecret, sendUrl, template, signTitle);
        // 初始化短信接口参数end
        ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);
        MessageService messageService = (MessageService) ctx.getBean("messageService");
        String ekey = CacheKeyProvide.getKey(CacheKeyProvide.KEY_RULE, RoleUtil.EMAIL_INFO);
        RedisUtil.delKey(ekey);
        String emailInfo = RoleUtil.selectRuleByPrimaryKey(RoleUtil.EMAIL_INFO);
        if (StringUtil.isNotEmpty(emailInfo)) {
            String[] infoArray = emailInfo.split(Globals.COLUMNSPLIT);
            if (infoArray != null && infoArray.length >= 4) {
                MailUtil.init(infoArray[0], infoArray[1], new String(Base64.decode(infoArray[2])), infoArray[3]);
            }
        }
        RedisUtil.delKey(CacheKeyProvide.getKey(CacheKeyProvide.KEY_RULE, RoleUtil.WEB_URL));
        String sys = System.getProperties().getProperty("os.name");
        logger.info("当前操作系统：========" + sys);
        if (sys != null && !sys.toLowerCase().contains("windows")) {
            Globals.TEMP_EXCEL_PATH = "/root/crxny/temp";
            // Globals.TEMP_EXCEL_PATH = "/home/emcp/tomcat/temp";
        }
        logger.info("Excel临时路径：========" + Globals.TEMP_EXCEL_PATH);
    }
}
