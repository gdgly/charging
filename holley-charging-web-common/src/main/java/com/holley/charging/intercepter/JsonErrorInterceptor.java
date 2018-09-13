package com.holley.charging.intercepter;

import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.constants.ResultBean;
import com.holley.common.constants.app.ErrorCodeConstants;
import com.holley.platform.util.CacheSysHolder;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import com.opensymphony.xwork2.util.ValueStack;

/**
 * JSON调用异常拦截
 * 
 * @author zhouli
 */
public class JsonErrorInterceptor extends AbstractInterceptor {

    private static final long serialVersionUID = 1L;

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        String result = null;
        try {
            CacheSysHolder.reloadLink(KeySessionTypeEnum.APP);
            result = invocation.invoke();
            return result;
        } catch (Exception e) {
            // actionContext.put("result", result);
            ValueStack st = invocation.getStack();
            ResultBean bean = (ResultBean) st.findValue("resultBean");
            if (bean != null) {
                bean.setSuccess(false);
                bean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);
                bean.setMessage("系统调用异常，请稍后重试!");
                // bean.setMessage(e.getMessage());
            }
            e.printStackTrace();
            result = "errorjson";
            return result;
        }
    }

}
