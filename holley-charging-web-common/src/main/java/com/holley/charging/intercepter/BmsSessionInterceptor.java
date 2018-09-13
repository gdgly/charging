package com.holley.charging.intercepter;

import org.apache.log4j.Logger;

import com.holley.common.cache.RedisUtil;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.WebUser;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;

public class BmsSessionInterceptor extends BaseInterceptor {

    private static final long   serialVersionUID = 1008901298342362080L;
    private static final Logger logger           = Logger.getLogger(BmsSessionInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        WebUser webUser = getSessionUser(KeySessionTypeEnum.BMS);
        logger.info("拦截器：webUser=" + webUser);

        if (webUser == null) {
            return Action.LOGIN;
        } else {
            String webUserKey = CacheKeyProvide.getKey(CacheKeyProvide.KEY_BMS_SESSION, getWebUserId(KeySessionTypeEnum.BMS));
            long outTime = RedisUtil.ttl(webUserKey);
            logger.info("webUserKey:" + webUserKey);
            logger.info("webUser过期时间:" + outTime);
            // 重置webuser过期时间
            RedisUtil.expire(webUserKey, RedisUtil.EXRP_30M);
            getRequest().setAttribute(Globals.WEB_USER, webUser);
            return invocation.invoke();
        }

    }
}
