package com.holley.charging.intercepter;

import java.util.List;

import org.apache.log4j.Logger;

import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysButtondef;
import com.holley.platform.model.sys.SysModuledef;
import com.holley.platform.util.CachedModuledefUtil;
import com.holley.platform.util.RoleUtil;
import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionInvocation;

import net.sf.json.JSONArray;

public class PermissionInterceptor extends BaseInterceptor {

    private static final long   serialVersionUID = 1008901298342362080L;
    private static final Logger logger           = Logger.getLogger(PermissionInterceptor.class);

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
            if (getModuledefByUrl(servletPath, modules) || getButtondefByUrl(servletPath, buttons)) {
                // 功能权限
                if (modules != null) {
                    List<SysModuledef> topMenu = CachedModuledefUtil.getTopMenu(modules);
                    List<SysModuledef> subMenu = CachedModuledefUtil.getSubMenu(modules);
                    getRequest().setAttribute("jsonTopMenu", JSONArray.fromObject(topMenu).toString());
                    getRequest().setAttribute(Globals.TOP_MENU, topMenu);
                    getRequest().setAttribute("jsonSubMenu", JSONArray.fromObject(subMenu).toString());
                    getRequest().setAttribute(Globals.SUB_MENU, subMenu);
                }
                // 按钮权限
                if (buttons != null) {
                    getRequest().setAttribute("jsonUserModuleButton", JSONArray.fromObject(buttons).toString());
                    getRequest().setAttribute(Globals.USER_MODULE_BUTTON, buttons);
                }
            } else {
                getRequest().setAttribute("msg", "无访问权限！！");
                return "msg";
            }

            return invocation.invoke();
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
