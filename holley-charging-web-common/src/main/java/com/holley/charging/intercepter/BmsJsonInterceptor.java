package com.holley.charging.intercepter;

import org.apache.log4j.Logger;

import com.holley.common.cache.RedisUtil;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.dataobject.WebUser;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * JSON调用异常拦截
 * 
 * @author zhouli
 */
public class BmsJsonInterceptor extends BaseInterceptor {

    private static final long   serialVersionUID = 1L;
    private static final Logger logger           = Logger.getLogger(BmsJsonInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        WebUser webUser = getSessionUser(KeySessionTypeEnum.BMS);
        logger.info("拦截器：webUser=" + webUser);
        ValueStack st = invocation.getStack();
        String result = null;
        if (webUser == null) {
            st.set("message", "relogin");
            return "errorjson";
        } else {
            // 重置webuser过期时间
            String webUserKey = CacheKeyProvide.getKey(CacheKeyProvide.KEY_BMS_SESSION, getWebUserId(KeySessionTypeEnum.BMS));
            long outTime = RedisUtil.ttl(webUserKey);
            logger.info("webUserKey:" + webUserKey);
            logger.info("webUser过期时间:" + outTime);
            RedisUtil.expire(webUserKey, RedisUtil.EXRP_30M);
            try {
                result = invocation.invoke();
            } catch (Exception e) {
                st.set("errormsg", getExceptionAllinformation(e));
                e.printStackTrace();
                result = "errorjson";
            }
            return result;
        }
    }

    private String getExceptionAllinformation(Exception e) {
        String sOut = "System Error:\n";
        sOut += e.toString() + "\n";
        StackTraceElement[] trace = e.getStackTrace();
        for (StackTraceElement s : trace) {
            sOut += s + "\n";
        }
        return sOut;
    }

}
