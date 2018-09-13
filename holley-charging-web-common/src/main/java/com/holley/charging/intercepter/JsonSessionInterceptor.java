package com.holley.charging.intercepter;

import org.apache.log4j.Logger;

import com.holley.common.cache.RedisUtil;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.StringUtil;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

public class JsonSessionInterceptor extends BaseInterceptor {

    private static final long   serialVersionUID = 1008901298342362080L;
    private static final Logger logger           = Logger.getLogger(JsonSessionInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String webUserKey = getWebUserKey();
        String userId = getWebUserId(KeySessionTypeEnum.WEB);
        WebUser webUser = getSessionUser(KeySessionTypeEnum.WEB);
        logger.info("拦截器：webUser" + webUser);
        if (webUser == null) {
            ValueStack st = invocation.getStack();
            st.set("userLoginStatus", 2);
            Object sessionMsg = Globals.SESSION_USERID_MAP.get(webUserKey);
            if (!StringUtil.isEmpty(userId) && sessionMsg != null) {
                Globals.SESSION_USERID_MAP.remove(webUserKey);
                st.set("sessionMsg", sessionMsg);
            }
            return "userLoginStatus";
        } else {
            long outTime = RedisUtil.ttl(webUserKey);
            logger.info("webUserKey:" + webUserKey);
            logger.info("User过期时间:" + outTime);
            // 重置webuser过期时间
            RedisUtil.expire(webUserKey, RedisUtil.EXRP_30M);
            return invocation.invoke();
        }

    }
}
