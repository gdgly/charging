package com.holley.charging.action;

import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;
import net.sf.json.util.PropertyFilter;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.util.TokenHelper;

import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.ResultBean;
import com.holley.common.constants.app.AppTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.PileAppStatusEnum;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.JsonDateValueProcessor;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.security.SecurityUtil;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.RoleUtil;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.interceptor.annotations.Before;
import com.opensymphony.xwork2.interceptor.annotations.BeforeResult;

/**
 * 基础strtus的ACTION基类
 * 
 * @author zhouli
 */
public abstract class BaseAction extends ActionSupport implements SessionAware {

    private final static Logger   logger           = Logger.getLogger(BaseAction.class);
    private static final long     serialVersionUID = 1L;

    protected String              MSG              = "msg";                             // add by sc提示信息
    protected String              MEMBER           = "member";                          // add by sc返回主页
    // 返回的错误信息,从国际化资源文件取
    protected boolean             success          = true;
    protected String              message;                                              // json返回页面的
    protected String              encode           = "UTF-8";
    protected static int          limit            = 10;                                // 分页数据大小
    protected static String       ERROR            = "error";
    protected static int          IS_EXPORT        = 1;                                 // 导出Excel标志
    protected static int          MAX_EXPORT       = 3000;                              // 最大导出条数
    protected String              appsecretkey;                                         // app版本号对应的密钥
    protected Map<String, Object> session;
    protected static final int    BY_YEAR          = 1;                                 // 按年
    protected static final int    BY_MONTH         = 2;                                 // 按月
    protected static final int    BY_QUARTER       = 3;                                 // 按季度
    protected static final int    COUNT_SIZE       = 5;                                 // 排行数

    @Before
    public void testBefore() {
        String data = ServletActionContext.getRequest().getParameter("data");
        String version = ServletActionContext.getRequest().getParameter("version");
        String type = ServletActionContext.getRequest().getHeader("platform");// 1:android,2:ios
        logger.info("解密前：" + data);
        if (StringUtils.isEmpty(version) || !NumberUtils.isNumber(type)) {
            return;
        }

        AppTypeEnum apptype = AppTypeEnum.getEnmuByValue(Integer.parseInt(type));
        if (apptype == null) {
            return;
        }

        if (AppTypeEnum.ANDROID == apptype) {
            appsecretkey = CacheSysHolder.getSysLinkName(LinkTypeEnum.ANDROID_VERSION.getValue(), version);
        } else if (AppTypeEnum.IOS == apptype) {
            appsecretkey = CacheSysHolder.getSysLinkName(LinkTypeEnum.IOS_VERSION.getValue(), version);
        }

        if (!StringUtils.isEmpty(appsecretkey) && !StringUtils.isEmpty(data)) {
            if (data.contains("\n")) {
                data = data.replaceAll("\n", "");
            }
            if (data.contains(" ")) {
                data = data.replaceAll(" ", "+");
            }

            try {
                String jsonstr = SecurityUtil.aesDecode(data, appsecretkey);
                JSONObject jsonobj = JSONObject.fromObject(jsonstr);
                ServletActionContext.getRequest().setAttribute("appReturnBean", jsonobj);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        System.out.println("test--------------------------------------before");
    }

    @BeforeResult
    public void testAfter() throws Exception {
        ResultBean resultBean = (ResultBean) ServletActionContext.getRequest().getAttribute("resultBean");
        if (resultBean == null) {
            return;
        }
        Object data = resultBean.getData();
        String jsonstr = "";
        if (data == null) {
            return;
        }

        if (data instanceof List) {
            jsonstr = JSONArray.fromObject(resultBean.getData(), returnJosnConfig(true, Date.class, PileStatusEnum.class, PileAppStatusEnum.class, BigDecimal.class, Double.class)).toString();
        } else {
            jsonstr = JSONObject.fromObject(resultBean.getData(), returnJosnConfig(true, Date.class, PileStatusEnum.class, PileAppStatusEnum.class, BigDecimal.class, Double.class)).toString();
        }
        System.out.println(jsonstr);
        if (!StringUtils.isEmpty(appsecretkey) && !StringUtils.isEmpty(jsonstr)) {
            String jsonstrkey = SecurityUtil.aesEncode(jsonstr, appsecretkey);
            resultBean.setData(jsonstrkey);
        }
        logger.info(resultBean.getData());
        System.out.println("test--------------------------------------after");
    }

    /**
     * 数据加密
     * 
     * @param key
     * @return
     */
    public String encrypt(String key) {
        String securityKey = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RULE_SYS_SECURIRY_DOWN_KEY);
        return SecurityUtil.encrypt(key, securityKey);
    }

    /**
     * 数据解密
     * 
     * @param key
     * @return
     */
    public String decrypt(String key) {
        String securityKey = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RULE_SYS_SECURIRY_DOWN_KEY);
        return SecurityUtil.decrypt(key, securityKey);
    }

    public HttpServletRequest getRequest() {
        return ServletActionContext.getRequest();
    }

    public Map<String, Object> getApplication() {
        return ActionContext.getContext().getApplication();
    }

    protected String getParameter(String para) {
        String paraValue = this.getRequest().getParameter(para);

        /*
         * if (StringUtils.isEmpty(paraValue)) { Object obj = this.getSession().getAttribute(para); if (obj != null) {
         * return String.valueOf(obj); } else { return null; } }
         */
        return paraValue;
    }

    protected String getAttribute(String para) {
        Object obj = this.getRequest().getAttribute("appReturnBean");
        String result = null;
        if (obj == null) {
            return null;
        } else {
            JSONObject jsonObj = (JSONObject) obj;
            try {
                result = jsonObj.getString(para);
            } catch (Exception e) {
                LOG.info(para + "参数不存在...");
            }
            return result;
        }
    }

    protected String getCookieByName(String cookieName) {
        Cookie allCookie[] = getRequest().getCookies();
        if (allCookie != null && allCookie.length != 0) {
            for (int i = 0; i < allCookie.length; i++) {
                String keyname = allCookie[i].getName();
                String value = allCookie[i].getValue();
                if (StringUtils.equals(cookieName, keyname)) {
                    return value;
                }
            }
        }

        return null;
    }

    protected Cookie saveToCookie30Min(String cookieName, String cookieValue) throws Exception {
        return saveToCookie(cookieName, cookieValue, 60 * 30);// cookie保存30分钟
    }

    protected Cookie saveToCookie(String cookieName, String cookieValue) throws Exception {
        return saveToCookie(cookieName, cookieValue, -1);// cookie保存当前会话
    }

    protected Cookie saveToCookie(String cookieName, String cookieValue, int maxAge) throws Exception {
        Cookie cookie = null;
        if (this.getCookieByName(cookieName) == null) {
            cookie = new Cookie(cookieName, cookieValue);
        } else {
            Cookie[] cookies = this.getRequest().getCookies();
            for (int i = 0; i < cookies.length; i++) {
                cookie = cookies[i];
                if (cookie.getName().equals(cookieName)) {
                    cookie.setValue(cookieValue);
                    break;
                }

            }
        }
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);// 秒
        this.getResponse().addCookie(cookie);
        return cookie;
    }

    /**
     * 是否需要导出excel
     * 
     * @return
     */
    public boolean isExportExcel() {
        String isExport = getRequest().getParameter("isExport");
        return StringUtils.equals("true", isExport);
    }

    public HttpServletResponse getResponse() {
        return ServletActionContext.getResponse();
    }

    public ServletContext getServletContext() {
        return ServletActionContext.getServletContext();
    }

    public String getRealyPath(String path) {
        return getServletContext().getRealPath(path);
    }

    public HttpSession getSession() {
        return this.getRequest().getSession();
    }

    /**
     * 取当前登录用户的帐号
     * 
     * @return
     */
    public String getCurrentAccount() {
        WebUser user = getCurrentUser();
        return user == null ? null : user.getLoginUser();
    }

    /**
     * 取当前登录用户信息
     * 
     * @return
     */
    public WebUser getCurrentUser() {
        return (WebUser) getSession().getAttribute(Globals.CURRENTUSER);
    }

    public String getSessionID() {
        String jsessionID = getSession().getId();
        if (jsessionID.length() > 32) {
            jsessionID = jsessionID.substring(0, 32);
        }
        return jsessionID;
    }

    public String getRemoteIP() {
        String ip = this.getRequest().getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = this.getRequest().getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = this.getRequest().getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = this.getRequest().getRemoteAddr();
        }

        return ip;
    }

    /**
     * 发送JSON数据
     * 
     * @author shencheng
     * @param json
     */
    protected void printJson(String json) {
        try {
            HttpServletResponse response = ServletActionContext.getResponse();
            response.setContentType("application/json;charset=UTF-8");
            PrintWriter out = response.getWriter();
            out.print(json);
            out.flush();
            out.close();
        } catch (Exception e) {
            logger.info("发送json数据，失败！！");
            e.printStackTrace();
        }
    }

    /**
     * seconds 设置几分钟后可以 重新获取验证码
     *
     * @param seconds
     */

    protected String checkCanGetMobileCode(long seconds) {
        String msg = "success";
        Object codeInteceptor = getSession().getAttribute("phone_code_time");
        if (codeInteceptor != null) {// 申请过一次 计算时间差
            long currTime = System.currentTimeMillis();
            long remainTime = currTime - Long.parseLong(codeInteceptor.toString());
            long get_time = seconds * 60 * 1000;
            if (remainTime < get_time) {
                msg = "请您" + (get_time - remainTime) / (1000) + " 秒后，重新获取验证码";

            } else {
                getSession().setAttribute("phone_code_time", System.currentTimeMillis());
            }
        } else {
            getSession().setAttribute("phone_code_time", System.currentTimeMillis());
        }
        return msg;
    }

    /**
     * 提示消息
     *
     * @param msg
     */
    protected void message(String msg) {
        getRequest().setAttribute("msg", msg);
        // String urltext = "<a href=" + request.getContextPath() + url + " >" + text + "></a>";
        // request.setAttribute("backurl", urltext);
    }

    protected void msg(String msg) {
        this.success = false;
        this.message = msg;
    }

    /**
     * 获取缓存数据
     * 
     * @param key
     * @return
     */
    protected String getCache(String key) {
        return ChargingCacheUtil.getString(key);
    }

    /**
     * 删除缓存数据
     * 
     * @param key
     */
    protected void removeCache(String key) {
        ChargingCacheUtil.delKey(key);
    }

    protected Page returnPage(int currentPage, int limit) {
        currentPage = currentPage == 0 ? 1 : currentPage;
        limit = limit == 0 ? 8 : limit;
        Page page = new Page((currentPage - 1) * limit, limit);
        return page;
    }

    protected String getServletRealPath(String pathName) {
        return ServletActionContext.getServletContext().getRealPath(pathName);
    }

    protected String getDataPath() {
        return RoleUtil.selectRuleByPrimaryKey(RoleUtil.DATA_PATH);
    }

    protected int getParamInt(String param) {
        return NumberUtils.toInt(getParameter(param));
    }

    protected WebUser getWebuser() {
        String webUserKey = getSessionUserId();
        String userId = SecurityUtil.decrypt(getCookieByName(SecurityUtil.encrypt(Globals.COOKIE_USERID + getSessionID(), Globals.COOKIE_DESKEY)), Globals.COOKIE_DESKEY);

        WebUser webUser = null;
        if (webUserKey != null && userId != null) {
            webUser = ChargingCacheUtil.getSession(userId, KeySessionTypeEnum.WEB, webUserKey);
        }
        return webUser;
    }

    protected WebUser getBmsWebuser() {
        WebUser webUser = null;
        String userId = getSessionBmsUserId();
        if (userId != null) {
            webUser = ChargingCacheUtil.getSession(userId, KeySessionTypeEnum.BMS, null);
        }
        return webUser;
    }

    /**
     * 判断是否子公司账户
     */
    protected boolean isSubCompany(Map<String, Object> params) {
        WebUser user = getBmsWebuser();
        if (user != null && UserTypeEnum.COMPANY == user.getUsertype()) {
            if (params != null) {
                params.put("busMec", user.getUserId());
            }
            // params.put("busType", user.getUsertype().getValue());
            return true;
        }
        return false;
    }

    protected String getSessionUserId() {
        return SecurityUtil.decrypt(getCookieByName(SecurityUtil.encrypt(Globals.COOKIE_SESSION_USERID + getSessionID(), Globals.COOKIE_DESKEY)), Globals.COOKIE_DESKEY);
    }

    protected String getSessionBmsUserId() {
        return SecurityUtil.decrypt(getCookieByName(SecurityUtil.encrypt(Globals.COOKIE_BMS_USERID + getSessionID(), Globals.COOKIE_DESKEY)), Globals.COOKIE_DESKEY);
    }

    protected <T> T JsonToBean(String JsonString, Class<T> clazz) {
        JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher(new String[] { "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd", "yyyy/MM", "yyyy-MM-dd" }));
        JSONObject jsonObject = JSONObject.fromObject(JsonString);
        T entity = (T) JSONObject.toBean(jsonObject, clazz);
        return entity;
    }

    protected JsonConfig returnJosnConfig(boolean isFilterNull, Class... filterClass) {
        JsonConfig jsonConfig = new JsonConfig();
        if (isFilterNull) {
            // 过滤空值
            PropertyFilter filter = new PropertyFilter() {

                public boolean apply(Object object, String fieldName, Object fieldValue) {
                    return null == fieldValue;
                }
            };
            jsonConfig.setJsonPropertyFilter(filter);
        }
        for (Class clazz : filterClass) {
            jsonConfig.registerJsonValueProcessor(clazz, new JsonDateValueProcessor());
        }
        // 日期类型的值转化成yyyy-MM-dd HH:mm:ss

        return jsonConfig;
    }

    protected String saveToken(String name) {
        String token = TokenHelper.setToken(name);
        session.put(name, token);
        return token;
    }

    protected String checkToken(String name) {
        String paramValue = getParameter(name);
        Object token = session.get(name);
        String tokenValue = token == null ? "" : token.toString();
        // 参数、session中都没用token值提示错误
        if (StringUtils.isBlank(tokenValue)) {
            return "会话Token未设定！";
        } else if (StringUtils.isBlank(paramValue)) {
            return "表单Token未设定！";
        } else if (paramValue.equals(tokenValue)) { // session中有token,防止重复提交检查
            session.remove(name);
            return "success";
        } else {
            return "请勿重复提交！";
        }
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getAppsecretkey() {
        return appsecretkey;
    }

    @Override
    public void setSession(Map<String, Object> session) {
        this.session = session;
    }
}
