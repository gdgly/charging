package com.holley.charging.intercepter;

import java.util.List;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;

import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysButtondef;
import com.holley.platform.model.sys.SysModuledef;
import com.holley.platform.util.RoleUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;

/**
 * 后台系统权限拦截器
 * 
 * @author zdd
 */
public class BmsPermissionInterceptor extends BaseInterceptor {

    private static final long   serialVersionUID = 1008901298342362080L;
    private static final Logger logger           = Logger.getLogger(BmsPermissionInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {

        WebUser webUser = getSessionUser(KeySessionTypeEnum.BMS);
        if (webUser == null) {
            return Action.LOGIN;
        } else {
            String servletPath = getServletPath();
            servletPath = servletPath.replaceFirst("/", "");

            List<SysModuledef> modules = RoleUtil.selectModuledefByUserid(webUser.getUserId(), webUser.getRoleId());
            List<SysButtondef> buttons = RoleUtil.selectButtondefByUserid(Integer.valueOf(getWebUserId(KeySessionTypeEnum.BMS)), webUser.getRoleId());

            if (getModuledefByUrl(servletPath, modules) || getButtondefByUrl(servletPath, buttons)) {
                // 功能权限
                if (modules != null) {
                    getRequest().setAttribute(Globals.USER_MODULE, JSONArray.fromObject(modules).toString());
                }
                // 按钮权限
                if (buttons != null) {
                    getRequest().setAttribute(Globals.USER_MODULE_BUTTON, JSONArray.fromObject(buttons).toString());
                }
            } else {
                getRequest().setAttribute(Globals.USER_MODULE, "{}");
                getRequest().setAttribute(Globals.USER_MODULE_BUTTON, "{}");
                getRequest().setAttribute("msg", "无访问权限.");
                return "msg";
            }
            return invocation.invoke();
        }
    }

    private boolean getModuledefByUrl(String url, List<SysModuledef> list) {
        if (StringUtil.isEmpty(url) || list == null || list.size() == 0) return false;
        for (SysModuledef record : list) {
            if (url.equals(record.getUrl())) return true;
        }
        return false;
    }

    private boolean getButtondefByUrl(String url, List<SysButtondef> list) {
        if (StringUtil.isEmpty(url) || list == null || list.size() == 0) return false;
        for (SysButtondef record : list) {
            if (url.equals(record.getUrl())) return true;
        }
        return false;
    }
}
