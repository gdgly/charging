package com.holley.charging.intercepter;

import org.apache.log4j.Logger;

import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;
import com.opensymphony.xwork2.ActionInvocation;

public class ActionInterceptor extends BaseInterceptor {

    private static final long   serialVersionUID = 1008901298342362080L;
    private static final Logger log              = Logger.getLogger(ActionInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        CacheSysHolder.reloadLink(KeySessionTypeEnum.WEB);// 更新link表
        // ////////////////////////////////////////////////
        String actionName = invocation.getInvocationContext().getName();
        String className = invocation.getAction().getClass().getName();
        String servletPath = getServletPath();
        WebUser webUser = getSessionUser(KeySessionTypeEnum.WEB);
        /*
         * String path = request.getContextPath(); String basePath = request.getScheme() + "://" +
         * request.getServerName() + ":" + request.getServerPort() + path + "/"; log.info("path:" + path);
         * log.info("basePath:" + basePath); log.info("servletPath:" + servletPath);
         */
        getRequest().setAttribute(Globals.WEB_USER, webUser);
        log.info("WebInfo>>>>>>>>>>");
        log.info("className:" + className);
        log.info("actionName:" + actionName);
        log.info("servletPath:" + servletPath);
        if (!StringUtil.isEmpty(servletPath)) {
            getRequest().setAttribute("actionName", servletPath.substring(1, servletPath.length()));
            getRequest().setAttribute("action", actionName);
        }

        try {
            String result = invocation.invoke();
            return result;
        } catch (Exception e) {
            log.error(e);
            log.error("Exception className :" + className);
            log.error("Exception methodName:" + actionName);
            log.error("Exception message   :", e);
            getRequest().setAttribute("msg", "系统异常！！");
            return "msg";
        }
    }
}
