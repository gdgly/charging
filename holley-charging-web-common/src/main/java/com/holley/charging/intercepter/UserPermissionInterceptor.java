package com.holley.charging.intercepter;

import java.util.List;

import org.apache.log4j.Logger;

import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysButtondef;
import com.holley.platform.model.sys.SysModuledef;
import com.holley.platform.util.RoleUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;

public class UserPermissionInterceptor extends BaseInterceptor {

    private static final long   serialVersionUID = 1008901298342362080L;
    private static final Logger logger           = Logger.getLogger(UserPermissionInterceptor.class);

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        WebUser webUser = getSessionUser(KeySessionTypeEnum.WEB);
        if (webUser == null) {
            return Action.LOGIN;
        } else {
            // 重置webuser过期时间
            String servletPath = getServletPath();
            servletPath = servletPath.replaceFirst("/", "");
            List<SysModuledef> modules = RoleUtil.selectModuledefByUserid(webUser.getUserId(), webUser.getRoleId());
            List<SysButtondef> buttons = RoleUtil.selectButtondefByUserid(webUser.getUserId(), webUser.getRoleId());
            if ("userAdmin/home.action".endsWith(servletPath) || "userAdmin/initUserHome.action".equals(servletPath)) {
                return invocation.invoke();
            } else if (getModuledefByUrl(servletPath, modules) || getButtondefByUrl(servletPath, buttons)) {
                return invocation.invoke();
            } else {
                getRequest().setAttribute("msg", "无访问权限！！");
                return "userMsg";
            }
        }
        /*
         * HttpServletRequest request = ServletActionContext.getRequest(); String path = request.getServletPath();
         * String passPath[] = {"/invest/detail.html","/invest/detailTenderForJson.html"}; if( (passPath[0].equals(path)
         * || passPath[1].equals(path))&&"1".equals(Global.getValue("is_open_borrow_detail"))){//系统配置是否登陆可查看标详情 return
         * invocation.invoke(); }
         */
        /*
         * BusUser busUser = (BusUser) session.get(Globals.CURRENTUSER); if (busUser == null) { return Action.LOGIN; }
         * else { return invocation.invoke(); }
         */

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
