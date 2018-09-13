package com.holley.charging.intercepter;

import org.apache.log4j.Logger;

import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.constants.charge.RealVerifyStatusEnum;
import com.holley.common.dataobject.WebUser;
import com.opensymphony.xwork2.ActionInvocation;

public class UserCheckRuleInterceptor extends BaseInterceptor {

    private static final long   serialVersionUID = 1008901298342362080L;
    private static final Logger logger           = Logger.getLogger(UserCheckRuleInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        WebUser webUser = getSessionUser(KeySessionTypeEnum.WEB);
        logger.info("拦截器：checkRule" + webUser.getRealStatus());
        if (webUser.getRealStatus() == RealVerifyStatusEnum.PASSED.getShortValue()) {
            return invocation.invoke();
        } else {
            getRequest().setAttribute("msg", "请先实名认证！！");
            getRequest().setAttribute("backUrl2", "userAdmin/initUserInfo.action");
            getRequest().setAttribute("retrunDec", "去实名认证>>");
            return "userMsg";
        }

    }
}
