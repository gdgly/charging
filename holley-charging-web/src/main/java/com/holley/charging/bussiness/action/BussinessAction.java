package com.holley.charging.bussiness.action;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusBussinessReal;
import com.holley.charging.model.bus.BusBussinessRealExample;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.def.SubAccountModel;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.RedisUtil;
import com.holley.common.cache.charging.BussinessCacheUtil;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.EmailSubjectEnum;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.LogTypeEnum;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.BusRealVerifyStatusEnum;
import com.holley.common.constants.charge.CertificateStatusEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.NoticeTypeEnum;
import com.holley.common.constants.charge.RealVerifyStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.ValidMoneyCountBean;
import com.holley.common.dataobject.WebUser;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.RandomUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.EmailModel;
import com.holley.platform.model.sys.SysDefArea;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.model.sys.SysNotice;
import com.holley.platform.model.sys.SysNoticeMessage;
import com.holley.platform.service.MessageService;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.LogUtil;
import com.holley.platform.util.MsgUtil;
import com.holley.web.common.util.Validator;

/**
 * 账户相关ACTION
 * 
 * @author shencheng profit
 */
public class BussinessAction extends BaseAction {

    private final static Logger logger            = Logger.getLogger(BussinessAction.class);
    private UserService         userService;
    private MessageService      messageService;
    private File                headImg;
    private List<SysDefArea>    citys;
    private static String       changePwd         = "changePwd";
    private static String       changePayPwd      = "changePayPwd";
    private static String       changeEmail       = "changeEmail";
    private static String       changePhone       = "changePhone";
    private static String       enableSubAccount  = "enableSubAccount";                     // 激活子账户
    private static String       disableSubAccount = "disableSubAccount";                    // 禁用子账户
    private static String       editSubAccount    = "editSubAccount";                       // 修改子账户
    private static String       addSubAccount     = "addSubAccount";                        // 添加子账户
    private Page                page;
    private File                licenceImg;                                                 // 运营证件照
    private File                corporateImg;                                               // 法人身份证正面
    private File                transatorImg;                                               // 操作人身份证正面
    private Map<String, Object> result;

    /**
     * 查看消息页
     * 
     * @return
     */
    public String searchMsg() {
        return SUCCESS;
    }

    /**
     * 异步获取msg列表
     * 
     * @return
     */
    public String searchMsgByAjax() {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        int isRead = this.getParamInt("isRead");// 1是2否
        Page page = this.returnPage(currentPage, limit);
        param.put(Globals.PAGE, page);
        if (isRead > 0) {
            param.put("isRead", isRead);
        }
        param.put("msgType", NoticeTypeEnum.MESSAGE.getShortValue());
        param.put("reciveUserId", webUser.getUserId());
        List<SysNoticeMessage> list = this.userService.selectSysNoticeByPage(param);
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 显示一条详细信息
     * 
     * @return
     */
    public String detailMsgByAjax() {
        WebUser webUser = this.getWebuser();
        int noticeId = this.getParamInt("noticeId");
        int isRead = this.getParamInt("isRead");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "success");
        if (noticeId > 0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("reciveUserId", webUser.getUserId());
            param.put("noticId", noticeId);
            SysNoticeMessage sysNoticeMessage = userService.selectSysNoticeMessage(param);
            if (sysNoticeMessage != null) {
                SysNotice sysNotice = userService.selectSysNoticeByPrimaryKey(noticeId);
                map.put("noticeMessage", sysNoticeMessage);
                if (sysNotice.getIsRead() != WhetherEnum.YES.getShortValue()) {
                    SysNotice newSysNotice = new SysNotice();
                    newSysNotice.setId(noticeId);
                    newSysNotice.setIsRead(WhetherEnum.YES.getShortValue());
                    userService.updateSysNoticeByPrimaryKeySelective(newSysNotice);
                }
            } else {
                map.put("msg", "查看信息不存在！！");
            }
        } else {
            map.put("msg", "查看信息失败！！");
        }
        this.result = map;
        return SUCCESS;
    }

    /**
     * 修改头像用户名
     * 
     * @return
     */
    public String editHeadImgByAjax() {
        String username = this.getParameter("username");
        WebUser webUser = this.getWebuser();
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            map = this.userService.updateHeadImgAndUserName(webUser, username, headImg, getDataPath());
        } catch (Exception e) {
            map.put("msg", "修改信息失败！！");
        }
        message = (String) map.get("msg");
        if ("success".equals(message)) {
            Object imgUrl = map.get("url");
            Object name = map.get("username");
            if (imgUrl != null || name != null) {
                if (imgUrl != null) {
                    webUser.setHeadImg(imgUrl.toString());
                }
                if (name != null) {
                    webUser.setUserName(name.toString());
                }
                String userKey = getSessionUserId();
                if (StringUtil.isEmpty(userKey)) {
                    message = "修改失败！！";
                } else {
                    ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.WEB, userKey);
                }

            }
        }

        return SUCCESS;
    }

    /**
     * 实名认证页
     * 
     * @return
     * @throws Exception
     */
    public String realName() throws Exception {
        WebUser webUser = this.getWebuser();
        BusBussinessRealExample emp = new BusBussinessRealExample();
        BusBussinessRealExample.Criteria cr = emp.createCriteria();
        cr.andBusInfoIdEqualTo(webUser.getInfoId());
        cr.andValidStatusLessThanOrEqualTo(BusRealVerifyStatusEnum.CHECK_PASSED.getShortValue());
        BusBussinessReal busBussinessRealbean = userService.selectBusBussinessRealByExample(emp);
        if (busBussinessRealbean != null || webUser.getRoleType() != UserTypeEnum.ENTERPRISE.getShortValue()) {
            return MEMBER;
        }
        return SUCCESS;
    }

    public String realNameInfo() throws Exception {
        WebUser webUser = this.getWebuser();
        BusBussinessRealExample emp = new BusBussinessRealExample();
        BusBussinessRealExample.Criteria cr = emp.createCriteria();
        cr.andBusInfoIdEqualTo(webUser.getInfoId());
        cr.andValidStatusLessThanOrEqualTo(BusRealVerifyStatusEnum.CHECK_PASSED.getShortValue());
        BusBussinessReal busBussinessRealbean = userService.selectBusBussinessRealByExample(emp);
        if (busBussinessRealbean != null || webUser.getRoleType() != UserTypeEnum.ENTERPRISE.getShortValue()) {
            return MEMBER;
        }
        this.getRequest().setAttribute("provinceList", CacheSysHolder.getProvinceList());
        this.getRequest().setAttribute("bankNameList", CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.BANK_NAME.getValue()));
        return SUCCESS;
    }

    public String saveRealNameInfoByAjax() {
        WebUser webUser = this.getWebuser();
        BusBussinessRealExample emp = new BusBussinessRealExample();
        BusBussinessRealExample.Criteria cr = emp.createCriteria();
        cr.andBusInfoIdEqualTo(webUser.getInfoId());
        cr.andValidStatusLessThanOrEqualTo(BusRealVerifyStatusEnum.CHECK_PASSED.getShortValue());
        BusBussinessReal busBussinessRealbean = userService.selectBusBussinessRealByExample(emp);
        String realNameInfoJson = this.getParameter("realNameInfoJson");
        Map<String, Object> map = cheackrealNameInfo(webUser, realNameInfoJson, busBussinessRealbean);
        message = (String) map.get("msg");
        if ("success".equals(message)) {
            // insert info
            BusBussinessReal busBussinessReal = (BusBussinessReal) map.get("busBussinessReal");
            busBussinessReal.setAddTime(new Date());
            busBussinessReal.setUserId(webUser.getUserId());
            busBussinessReal.setBusInfoId(webUser.getInfoId());
            try {
                message = userService.insertAndUpdateBusBussinessReal(busBussinessReal, licenceImg, corporateImg, transatorImg, getDataPath());
                // 添加日志
                LogUtil.recordOperationLog(webUser.getUserId(), LogTypeEnum.RUN_LOGIN, LogOperatorEnum.APPLY, getRemoteIP(), "用户 '" + webUser.getUserName() + " 提交实名审核申请",
                                           realNameInfoJson);
            } catch (Exception e) {
                message = "提交申请失败！！";
                e.printStackTrace();
            }
            // this.userService.insertBusBussinessRealSelective(busBussinessReal);

        }
        return SUCCESS;
    }

    private Map<String, Object> cheackrealNameInfo(WebUser webUser, String busBussinessRealJson, BusBussinessReal busBussinessRealbean) {
        String msg = "success";
        Map<String, Object> map = new HashMap<String, Object>();
        if (busBussinessRealbean != null || webUser.getRoleType() != UserTypeEnum.ENTERPRISE.getShortValue()) {
            msg = ERROR;
        } else {
            try {
                BusBussinessReal busBussinessReal = this.JsonToBean(busBussinessRealJson, BusBussinessReal.class);
                map.put("busBussinessReal", busBussinessReal);
                if (StringUtil.isEmpty(busBussinessReal.getBusName())) {
                    msg = "请填写公司名称！！";
                } else if (StringUtil.isEmpty(busBussinessReal.getAccRealName())) {
                    msg = "请填写企业银行开户名！！";
                } else if (StringUtil.isEmpty(busBussinessReal.getBankName())) {
                    msg = "请填写对公账号开户行！！";
                } else if (StringUtil.isEmpty(busBussinessReal.getBankAccount())) {
                    msg = "请填写公司对公账号！！";
                } else if (licenceImg == null) {
                    msg = "请上传运营证件图片！！";
                } else if (corporateImg == null) {
                    msg = "请上传法人身份证正面图片！！";
                } else if (transatorImg == null) {
                    msg = "请上传操作人身份证正面图片！！";
                }

            } catch (Exception e) {
                msg = "申请信息有误！！";
                e.printStackTrace();
            }
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * 验证打款信息页
     * 
     * @return
     */
    public String validRealNameInfo() {
        WebUser webUser = getWebuser();
        BusBussinessRealExample emp = new BusBussinessRealExample();
        BusBussinessRealExample.Criteria cr = emp.createCriteria();
        cr.andBusInfoIdEqualTo(webUser.getInfoId());
        cr.andValidStatusEqualTo(BusRealVerifyStatusEnum.UNCHECKED.getShortValue());
        BusBussinessReal busBussinessReal = userService.selectBusBussinessRealByExample(emp);
        if (busBussinessReal == null || webUser.getUsertype() != UserTypeEnum.ENTERPRISE) {
            return MEMBER;
        }
        busBussinessReal.setBankAccount(StringUtil.getSubString(busBussinessReal.getBankAccount()));
        this.getRequest().setAttribute("bankNameList", CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.BANK_NAME.getValue()));
        this.getRequest().setAttribute("busBussinessReal", busBussinessReal);
        return SUCCESS;
    }

    /**
     * 异步验证打款信息
     * 
     * @return
     */
    public String validRealNameInfoByAjax() {
        String msg = "success";
        WebUser webUser = getWebuser();
        String validCode = getParameter("validCode");
        ValidMoneyCountBean validMoneyCountBean = null;
        BusBussinessRealExample emp = new BusBussinessRealExample();
        BusBussinessRealExample.Criteria cr = emp.createCriteria();
        cr.andBusInfoIdEqualTo(webUser.getInfoId());
        cr.andValidStatusEqualTo(BusRealVerifyStatusEnum.UNCHECKED.getShortValue());
        BusBussinessReal busBussinessReal = userService.selectBusBussinessRealByExample(emp);
        if (busBussinessReal == null || busBussinessReal.getValidCode() == null) {
            msg = "无验证信息！！";
        } else if (webUser.getRoleType() != UserTypeEnum.ENTERPRISE.getShortValue()) {
            msg = "必须管理员才能进行实名认证操作！！";
        } else {
            validMoneyCountBean = initVlidMoney(webUser.getInfoId());
            if (validMoneyCountBean.isRefuseValid()) {
                msg = validMoneyCountBean.getValidFailMsg();
                busBussinessReal.setValidStatus(BusRealVerifyStatusEnum.CHECK_FAILED.getShortValue());
                userService.updateBusBussinessRealByPrimaryKeySelective(busBussinessReal);
                // 验证次数超过5次更新记录表状态
            } else if (busBussinessReal.getValidCode().equals(validCode)) {
                // 验证成功
                BusUser busUser = new BusUser();
                busUser.setRealStatus(RealVerifyStatusEnum.PASSED.getShortValue());
                busUser.setInfoId(webUser.getInfoId());
                BusBussinessInfo busBussinessInfo = new BusBussinessInfo();
                busBussinessInfo.setId(webUser.getInfoId());
                busBussinessInfo.setBusName(busBussinessReal.getBusName());
                busBussinessInfo.setAccRealName(busBussinessReal.getAccRealName());
                busBussinessInfo.setBankAccount(busBussinessReal.getBankAccount());
                busBussinessInfo.setBankName(busBussinessReal.getBankName());
                /*
                 * busBussinessInfo.setLicenceImg(busBussinessReal.getLicenceImg());
                 * busBussinessInfo.setCorporateImg(busBussinessReal.getCorporateImg());
                 * busBussinessInfo.setTransatorImg(busBussinessReal.getTransatorImg());
                 */
                BusBussinessReal newBusBussinessReal = new BusBussinessReal();
                newBusBussinessReal.setBusInfoId(busBussinessReal.getBusInfoId());
                newBusBussinessReal.setAddTime(busBussinessReal.getAddTime());
                busBussinessReal.setValidStatus(BusRealVerifyStatusEnum.CHECK_PASSED.getShortValue());
                try {
                    userService.updateBusBussinessRealAndInfoAndUser(busUser, busBussinessInfo, busBussinessReal, getDataPath());
                    // 添加日志
                    // LogUtil.recordOperationLog(webUser.getUserId(), LogTypeEnum.RUN_LOGIN, LogOperatorEnum.VALID,
                    // getRemoteIP(), "用户 '" + webUser.getUserName() + " 验证实名审认证信息",
                    // null);
                    webUser.setRealStatus(RealVerifyStatusEnum.PASSED.getShortValue());
                    String userKey = getSessionUserId();
                    if (StringUtil.isEmpty(userKey)) {
                        msg = "修改失败！！";
                    } else {
                        ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.WEB, userKey);
                        Globals.VALID_COUNT_MAP.remove(webUser.getInfoId());
                    }

                } catch (Exception e) {
                    msg = ERROR;
                    e.printStackTrace();
                }

            } else {
                msg = validMoneyCountBean.getValidFailMsg();
            }
        }
        message = msg;
        return SUCCESS;
    }

    /**
     * 基本信息设置页
     * 
     * @return
     */
    public String baseInfo() throws Exception {
        WebUser webUser = this.getWebuser();
        BusBussinessInfo busBussinessInfo = userService.selectBussinessByPrimaryKey(webUser.getInfoId());
        if (busBussinessInfo.getProvince() != null) {
            List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(busBussinessInfo.getProvince());
            this.getRequest().setAttribute("cityList", cityList);
        }
        // 运营类型
        List<SysLink> busDomainList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.BUS_DOMAIN.getValue());
        this.getRequest().setAttribute("busDomainList", busDomainList);
        this.getRequest().setAttribute("busBussinessInfo", busBussinessInfo);
        this.getRequest().setAttribute("provinceList", CacheSysHolder.getProvinceList());
        return SUCCESS;
    }

    /**
     * 安全信息设置页
     * 
     * @return
     */
    public String safeInfo() throws Exception {
        WebUser webUser = this.getWebuser();
        BusUser busUser = userService.selectUserByPrimaryKey(webUser.getUserId());
        if (busUser.getRealStatus() == RealVerifyStatusEnum.PASSED.getShortValue()) {
            BusBussinessInfo busBussinessInfo = userService.selectBussinessByPrimaryKey(webUser.getInfoId());
            this.getRequest().setAttribute("busBussinessInfo", busBussinessInfo);
        } else {
            BusBussinessRealExample emp = new BusBussinessRealExample();
            BusBussinessRealExample.Criteria cr = emp.createCriteria();
            cr.andBusInfoIdEqualTo(webUser.getInfoId());
            cr.andValidStatusLessThanOrEqualTo(BusRealVerifyStatusEnum.CHECK_PASSED.getShortValue());
            BusBussinessReal busBussinessRealbean = userService.selectBusBussinessRealByExample(emp);
            this.getRequest().setAttribute("busBussinessReal", busBussinessRealbean);
        }
        // BusBussinessRealExample emp = new BusBussinessRealExample();
        // BusBussinessRealExample.Criteria cr = emp.createCriteria();
        // cr.andBusInfoIdEqualTo(webUser.getInfoId());
        // cr.andValidStatusEqualTo(RealVerifyStatusEnum.VERIFYING.getShortValue());
        // userService.selectBusBussinessRealByExample(emp);
        this.getRequest().setAttribute(Globals.CURRENTUSER, busUser);
        this.getRequest().setAttribute("safeLevel", safeLevel(busUser));

        return SUCCESS;
    }

    /**
     * 验证账户信息页：修改登录密码
     * 
     * @return
     */
    public String validChangePwd() throws Exception {

        WebUser webUser = this.getWebuser();
        String userId = webUser.getUserId().toString();
        BussinessCacheUtil.removieEamil(userId);
        BussinessCacheUtil.removieEamilCode(userId);
        BussinessCacheUtil.removieChangePhone(userId);
        BussinessCacheUtil.removieChangePhoneCode(userId);
        BussinessCacheUtil.removieChangePhoneNum(userId);
        BussinessCacheUtil.removieChangePayPwd(webUser.getUserId().toString());
        BussinessCacheUtil.removieChangeEmail(webUser.getUserId().toString());
        if (!changePwd.equals(BussinessCacheUtil.getChangePwd(userId))) {
            BussinessCacheUtil.setChangePwd(userId, changePwd);
            BussinessCacheUtil.removiePhoneCode(userId);
        }
        this.getRequest().setAttribute("actionType", changePwd);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
    }

    /**
     * 验证账户信息页：修改交易密码
     * 
     * @return
     */
    public String validChangePayPwd() throws Exception {
        WebUser webUser = this.getWebuser();
        String userId = webUser.getUserId().toString();
        BussinessCacheUtil.removieEamil(userId);
        BussinessCacheUtil.removieEamilCode(userId);
        BussinessCacheUtil.removieChangePwd(userId);
        BussinessCacheUtil.removieChangeEmail(userId);
        BussinessCacheUtil.removieChangePhone(userId);
        BussinessCacheUtil.removieChangePhoneCode(userId);
        BussinessCacheUtil.removieChangePhoneNum(userId);
        if (!changePayPwd.equals(BussinessCacheUtil.getChangePayPwd(userId))) {
            BussinessCacheUtil.setChangePayPwd(userId, changePwd);
            BussinessCacheUtil.removiePhoneCode(userId);
        }

        this.getRequest().setAttribute("actionType", changePayPwd);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
    }

    /**
     * 验证邮箱信息页：修改邮箱
     * 
     * @return
     */
    public String validChangeEmail() throws Exception {
        WebUser webUser = this.getWebuser();
        String userId = webUser.getUserId().toString();
        BussinessCacheUtil.removieEamil(userId);
        BussinessCacheUtil.removieEamilCode(userId);
        BussinessCacheUtil.removieChangePhone(userId);
        BussinessCacheUtil.removieChangePhoneCode(userId);
        BussinessCacheUtil.removieChangePhoneNum(userId);
        BussinessCacheUtil.removieChangePwd(userId);
        BussinessCacheUtil.removieChangePayPwd(webUser.getUserId().toString());
        if (!changeEmail.equals(BussinessCacheUtil.getChangeEmail(userId))) {
            BussinessCacheUtil.setChangeEmail(userId, changeEmail);
            BussinessCacheUtil.removiePhoneCode(userId);
        }

        this.getRequest().setAttribute("actionType", changeEmail);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
    }

    /**
     * 验证邮箱信息页：修改邮箱
     * 
     * @return
     */
    public String validChangePhone() throws Exception {
        WebUser webUser = this.getWebuser();
        String userId = webUser.getUserId().toString();
        BussinessCacheUtil.removieEamil(userId);
        BussinessCacheUtil.removieEamilCode(userId);
        BussinessCacheUtil.removieChangePhoneNum(userId);
        BussinessCacheUtil.removieChangePhoneCode(userId);
        BussinessCacheUtil.removieChangePwd(userId);
        BussinessCacheUtil.removieChangePayPwd(userId);
        BussinessCacheUtil.removieChangeEmail(userId);
        if (!changePhone.equals(BussinessCacheUtil.getChangePhone(userId))) {
            BussinessCacheUtil.setChangePhone(userId, changePhone);
            BussinessCacheUtil.removiePhoneCode(userId);
        }

        this.getRequest().setAttribute("actionType", changePhone);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
    }

    /**
     * 验证账户信息 ajax
     * 
     * @return
     */
    public String doValidUserInfo() throws Exception {
        String actionType = this.getParameter("actionType");
        String phoneCode = this.getParameter("phoneCode");
        WebUser webUser = this.getWebuser();
        if (!changePwd.equals(actionType) && !changePayPwd.equals(actionType) && !changeEmail.equals(actionType) && !changePhone.equals(actionType)) {
            message = ERROR;
            return SUCCESS;
        }
        // BusUser webUser = (BusUser) this.getRedisCache(Globals.CURRENTUSER);
        message = checkPhoneCode(phoneCode, webUser);
        if ("success".equals(message)) {
            BussinessCacheUtil.removiePhoneCode(webUser.getUserId().toString());
        }
        /*
         * if (changePwd.equals(actionType)) { BussinessCacheUtil.setChangePwd(webUser.getUserId().toString(),
         * changePwd); } else if (changePayPwd.equals(actionType)) {
         * BussinessCacheUtil.setChangePayPwd(webUser.getUserId().toString(), changePayPwd); } else if
         * (changeEmail.equals(actionType)) { BussinessCacheUtil.setChangeEmail(webUser.getUserId().toString(),
         * changeEmail); }
         */

        return SUCCESS;
    }

    /**
     * 修改账户密码或交易密码页
     * 
     * @return
     */
    public String changePwd() throws Exception {
        String userId = SecurityUtil.decrypt(getCookieByName(Globals.COOKIE_USERID), Globals.COOKIE_DESKEY);
        String changePwdType = BussinessCacheUtil.getChangePwd(userId);
        String changePayPwdType = BussinessCacheUtil.getChangePayPwd(userId);
        String msg = "success";

        if (!changePwd.equals(changePwdType) && !changePayPwd.equals(changePayPwdType)) {
            msg = "非法操作！！！";
            return MEMBER;
        }
        if (changePwd.equals(changePwdType)) {
            this.getRequest().setAttribute("actionType", changePwd);
        } else if (changePayPwd.equals(changePayPwdType)) {
            this.getRequest().setAttribute("actionType", changePayPwd);
        }

        return SUCCESS;
    }

    /**
     * 修改邮箱页
     * 
     * @return
     */
    public String changeEmail() throws Exception {
        WebUser webUser = this.getWebuser();
        String actionType = BussinessCacheUtil.getChangeEmail(webUser.getUserId().toString());
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        if (!changeEmail.equals(actionType)) {
            return MEMBER;
        }
        this.getRequest().setAttribute("actionType", changeEmail);
        return SUCCESS;
    }

    /**
     * 修改手机
     * 
     * @return
     */
    public String changePhone() throws Exception {
        WebUser webUser = this.getWebuser();
        String actionType = BussinessCacheUtil.getChangePhone(webUser.getUserId().toString());
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        if (!changePhone.equals(actionType)) {
            return MEMBER;
        }
        this.getRequest().setAttribute("actionType", changePhone);
        return SUCCESS;
    }

    /**
     * 修改账户密码
     * 
     * @return
     */
    public String doChangePwd() throws Exception {
        String userId = SecurityUtil.decrypt(getCookieByName(Globals.COOKIE_USERID), Globals.COOKIE_DESKEY);
        BusUser webUser = userService.selectUserByPrimaryKey(Integer.valueOf(userId));
        String oldPwd = this.getParameter("oldPwd");
        String newPwd = this.getParameter("newPwd");
        String checkNewPwd = this.getParameter("checkNewPwd");
        String changePwdType = BussinessCacheUtil.getChangePwd(userId);
        String changePayPwdType = BussinessCacheUtil.getChangePayPwd(userId);

        message = checkChangePwd(webUser, oldPwd, newPwd, checkNewPwd, changePwdType, changePayPwdType);
        if (!"success".equals(message)) {
            return SUCCESS;
        }
        newPwd = SecurityUtil.passwordEncrypt(newPwd);
        BusUser busUser = new BusUser();
        busUser.setId(webUser.getId());
        if (changePwd.equals(changePwdType)) {
            busUser.setPassword(newPwd);
            logger.info("修改登录密码");
        } else if (changePayPwd.equals(changePayPwdType)) {
            busUser.setPayPassword(newPwd);
            logger.info("修改交易密码");
        }
        int status = userService.updateUserByPKSelective(busUser);
        if (status != 1) {
            message = "fail";
        } else if (changePwd.equals(changePwdType)) {
            // 添加日志
            LogUtil.recordDocumentlog(webUser.getId(), LogOperatorEnum.MODIFY, this.getRemoteIP(), "用户 '" + webUser.getUsername() + "' 修改登录密码", "", false);
            // 修改密码成功重新登录
            ChargingCacheUtil.removieSession(null, KeySessionTypeEnum.WEB, getSessionUserId());
        }
        // 清除session中手机验证码时间
        /*
         * this.removeRedisCache("phone_code_time"); this.removeRedisCache(actionType);
         */
        return SUCCESS;
    }

    /**
     * 修改手机ajax
     * 
     * @return
     * @throws Exception
     */
    public String doChangePhone() throws Exception {
        String phoneCode = this.getParameter("phoneCode");// 手机验证码
        String phone = this.getParameter("phone");// 新手机号码
        WebUser webUser = this.getWebuser();
        String actionType = BussinessCacheUtil.getChangePhone(webUser.getUserId().toString());
        String sessionPhone = BussinessCacheUtil.getChangePhoneNum(webUser.getUserId().toString());
        if (!changePhone.equals(actionType)) {
            message = ERROR;
        } else if (StringUtil.isEmpty(phoneCode)) {
            message = "验证码不能为空！！";
        } else if (StringUtil.isEmpty(phone)) {
            message = "手机号码不能为空！！";
        } else if (!Validator.isMobile(phone)) {
            message = "手机格式不正确！！";
        } else if (!phone.equals(sessionPhone)) {
            message = "认证手机号不同，请重新请求认证！！";
        } else {
            message = checkChangePhoneCode(phoneCode, webUser.getUserId().toString());

            if ("success".equals(message)) {
                BusUser busUser = new BusUser();
                busUser.setId(webUser.getUserId());
                busUser.setPhone(phone);
                /*
                 * if (webUser.getEamilStatus() == CertificateStatusEnum.FAILED.getShortValue()) {
                 * busUser.setEmailStatus(CertificateStatusEnum.PASSED.getShortValue());
                 * webUser.setEamilStatus(CertificateStatusEnum.PASSED.getShortValue()); }
                 */
                int status = this.userService.updateUserByPKSelective(busUser);
                // 修改成功
                if (status == 1) {
                    logger.info("手机修改成功！！");
                    // 添加日志
                    LogUtil.recordDocumentlog(webUser.getUserId(), LogOperatorEnum.MODIFY, this.getRemoteIP(), "用户 '" + webUser.getUserName() + "' 修改手机",
                                              "原手机：" + webUser.getPhone() + "新手机：" + phone, false);
                    webUser.setPhone(phone);
                    String userKey = getSessionUserId();
                    if (StringUtil.isEmpty(userKey)) {
                        message = "修改失败！！";
                    } else {
                        ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.WEB, userKey);
                    }

                }
                BussinessCacheUtil.removieChangePhoneCode(webUser.getUserId().toString());
                BussinessCacheUtil.removieChangePhoneNum(webUser.getUserId().toString());

                /*
                 * this.removeRedisCache("phone_code_time"); this.removeRedisCache(actionType);
                 */
            }
        }
        return SUCCESS;
    }

    /**
     * 修改或认证邮箱 ajax
     * 
     * @return
     * @throws Exception
     */
    public String doChangeEamil() throws Exception {
        String emailCode = this.getParameter("emailCode");// 邮箱验证码
        String email = this.getParameter("email");// 邮箱
        WebUser webUser = this.getWebuser();
        String actionType = BussinessCacheUtil.getChangeEmail(webUser.getUserId().toString());
        String sessionEmail = BussinessCacheUtil.getEamil(webUser.getUserId().toString());
        if (!changeEmail.equals(actionType)) {
            message = ERROR;

        } else if (StringUtil.isEmpty(emailCode)) {
            message = "验证码不能为空！！";
        } else if (StringUtil.isEmpty(email)) {
            message = "邮箱不能为空！！";
        } else if (!Validator.isEmail(email)) {
            message = "邮箱格式不正确！！";
        } else if (!email.equals(sessionEmail)) {
            message = "认证邮箱不同，请重新请求认证！！";
        } else {
            message = checkEmailCode(emailCode, webUser.getUserId().toString());
            if ("success".equals(message)) {
                BusUser busUser = new BusUser();
                busUser.setId(webUser.getUserId());
                busUser.setEmail(email);
                if (webUser.getEamilStatus() == CertificateStatusEnum.FAILED.getShortValue()) {
                    busUser.setEmailStatus(CertificateStatusEnum.PASSED.getShortValue());
                    webUser.setEamilStatus(CertificateStatusEnum.PASSED.getShortValue());
                }
                int status = this.userService.updateUserByPKSelective(busUser);
                // 修改成功
                if (status == 1) {
                    // 添加日志
                    LogUtil.recordDocumentlog(webUser.getUserId(), LogOperatorEnum.MODIFY, this.getRemoteIP(), "用户 '" + webUser.getUserName() + "' 修改邮箱",
                                              "原邮箱：" + webUser.getEmail() + "新邮箱：" + email, false);
                    logger.info("邮箱修改成功！！");
                    webUser.setEmail(email);
                    String userKey = getSessionUserId();
                    if (StringUtil.isEmpty(userKey)) {
                        message = "修改失败！！";
                    } else {
                        ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.WEB, userKey);
                    }
                }
                BussinessCacheUtil.removieEamilCode(webUser.getUserId().toString());
                BussinessCacheUtil.removieEamil(webUser.getUserId().toString());

                /*
                 * this.removeRedisCache("phone_code_time"); this.removeRedisCache(actionType);
                 */
            }
        }
        return SUCCESS;
    }

    /**
     * 获取手机验证码 ajax
     * 
     * @return
     * @throws Exception
     */
    public String phoneCode() throws Exception {
        WebUser webUser = this.getWebuser();
        message = "success";
        String userId = getWebuser().getUserId().toString();
        String cachePhoneCode = BussinessCacheUtil.getPhoneCode(userId);
        if (cachePhoneCode != null) {
            long outTime = RedisUtil.ttl(CacheKeyProvide.getKey(CacheKeyProvide.KEY_PHONE_CODE, userId));
            long temp = RedisUtil.EXRP_3M - outTime;
            if (temp <= 60) {
                temp = 60 - temp;
                message = "操作繁忙请在" + temp + "秒后重试！！";
            }

        }

        if ("success".equals(message)) {
            String phoneCode = RandomUtil.getRandomNumber(4);
            logger.info("生成的手机动态验证码为：" + phoneCode);
            if (MsgUtil.sendSMS(webUser.getPhone(), phoneCode)) {
                BussinessCacheUtil.setPhoneCode(userId, phoneCode);
            } else {
                message = "短信发送失败！！";
            }

        }
        return SUCCESS;
    }

    /**
     * 更改手机号码发送验证码
     * 
     * @return
     * @throws Exception
     */
    public String phoneCodeForChangePhone() throws Exception {
        WebUser webUser = getWebuser();
        String msg = "success";
        String userId = getWebuser().getUserId().toString();
        String cachePhoneCode = BussinessCacheUtil.getChangePhoneCode(userId);
        String phone = this.getParameter("phone");
        if (!StringUtil.isEmpty(phone)) {
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andPhoneEqualTo(phone.trim());
            cr.andUserTypeEqualTo(webUser.getUsertype().getShortValue());
            List<BusUser> list = this.userService.selectUserByExample(emp);
            if (list != null && list.size() > 0) {
                msg = "手机号码：" + phone + "已经被注册，请更换手机号码！！";
            }
            if ("success".equals(msg)) {
                if (cachePhoneCode != null) {
                    long outTime = RedisUtil.ttl(CacheKeyProvide.getKey(CacheKeyProvide.KEY_CHANGE_PHONE_CODE, userId));
                    long temp = RedisUtil.EXRP_3M - outTime;
                    if (temp <= 60) {
                        temp = 60 - temp;
                        message = "操作繁忙请在" + temp + "秒后重试！！";
                    }
                }
                if (Validator.isMobile(phone) && "success".equals(msg)) {
                    String phoneCode = RandomUtil.getRandomNumber(4);
                    logger.info("修改手机号码生成的手机动态验证码为：" + phoneCode);
                    if (MsgUtil.sendSMS(phone, phoneCode)) {
                        BussinessCacheUtil.setChangePhoneCode(userId, phoneCode);
                        BussinessCacheUtil.setChangePhoneNum(userId, phone);

                    } else {
                        msg = "短信发送失败！！";
                    }
                } else {
                    msg = "手机号码不正确！！";
                }
            }
        } else {
            msg = "手机号码不能为空！！";
        }
        message = msg;
        return SUCCESS;
    }

    /**
     * 获取邮箱验证码 ajax
     * 
     * @return
     * @throws Exception
     */
    public String emailCode() throws Exception {
        WebUser webUser = getWebuser();
        String userId = SecurityUtil.decrypt(getCookieByName(Globals.COOKIE_USERID), Globals.COOKIE_DESKEY);
        String email = this.getParameter("email");
        String start = "<p>华立科技验证码：";
        String end = "</p>";
        String msg = "success";
        boolean senResult = true;
        if (!StringUtil.isEmpty(email)) {
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andEmailEqualTo(email.trim());
            cr.andUserTypeEqualTo(webUser.getUsertype().getShortValue());
            List<BusUser> list = this.userService.selectUserByExample(emp);
            if (list != null && list.size() > 0) {
                msg = "邮箱地址：" + email + "已经被注册，请更换其他邮箱地址！！";
            }
            if ("success".equals(msg)) {
                if (Validator.isEmail(email)) {
                    String emailCode = RandomUtil.getRandomNumber(4);
                    logger.info("邮箱TO：" + email);
                    logger.info("生成的邮箱动态验证码为：" + emailCode);
                    String content = start + emailCode + end;

                    // 插入信息内容表
                    SysNoticeMessage noticeMessage = new SysNoticeMessage();
                    noticeMessage.setContent(content);
                    noticeMessage.setAddTime(new Date());
                    noticeMessage.setTitle("邮箱认证");
                    messageService.insertNoticeMessage(noticeMessage);

                    SysNotice notice = new SysNotice();
                    notice.setMessageid(noticeMessage.getId());
                    notice.setReciveUser(Integer.valueOf(userId));

                    EmailModel emailModel = new EmailModel();
                    emailModel.setSubject("邮箱认证");
                    emailModel.setContent(content);
                    emailModel.setEmail(email);
                    emailModel.setNotice(notice);
                    senResult = MsgUtil.sendEmail(EmailSubjectEnum.CERTIFICATION, content, email);
                    if (senResult) {
                        BussinessCacheUtil.setEamilCode(userId, emailCode);
                        BussinessCacheUtil.setEamil(userId, email);
                    } else {
                        msg = "邮箱发送失败，请重新发送！！";
                    }
                    // MailUtil.getInstance().sendMail(emailModel.getSubject(), emailModel.getContent(),
                    // emailModel.getEmail());
                    // MsgProduct.sendMsg(emailModel);
                    // MsgUtil.sendEmail(emailModel);
                    /*
                     * Boolean emailSendResult = (Boolean) Globals.EMAIL_RESULT_MAP.get("result"); if (emailSendResult
                     * != null && !emailSendResult) { msg = "邮箱发送失败！！"; }
                     */
                } else {
                    msg = "邮箱地址不正确！！";
                }
            }

        } else {
            msg = "邮箱地址不能为空！！";
        }
        message = msg;
        return SUCCESS;
    }

    /**
     * 根据省级ID获取市级 ajax
     * 
     * @return
     */
    public String requestCitys() {
        int provinceId = this.getParamInt("province");
        logger.info("provinceId:" + provinceId);
        if (provinceId > 0) {
            citys = CacheSysHolder.getCityListByPid(provinceId);
        }
        return SUCCESS;
    }

    public String saveBaseInfo() {
        WebUser webUser = this.getWebuser();
        Map<String, Object> result = checkBaseInfo(webUser);
        message = (String) result.get("msg");
        /* BusBusinessInfo busBusinessInfo = (BusBusinessInfo)result.get(key); */
        Object obj = result.get("busBussinessInfo");
        if (obj != null && obj instanceof BusBussinessInfo) {
            int rs = this.userService.updateBussinessByPrimaryKeySelective((BusBussinessInfo) obj);
            if (rs != 1) {
                message = "修改失败！！";
            } else {
                LogUtil.recordDocumentlog(webUser.getUserId(), LogOperatorEnum.MODIFY, getRemoteIP(), "修改运营商基本资料",
                                          JSONObject.fromObject(obj, returnJosnConfig(true, Date.class)).toString(), false);// 插入修改日志
            }
        }
        return SUCCESS;
    }

    /**
     * 子账户列表
     * 
     * @return
     */
    public String searchSubAccount() {
        WebUser webUser = getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("infoId", webUser.getInfoId());
        List<SubAccountModel> subAccountList = this.userService.selectSubAccounts(param);
        this.getRequest().setAttribute("subAccountList", subAccountList);
        return SUCCESS;
    }

    /**
     * 异步添加子账户
     * 
     * @return
     */
    public String addSubAccount() {
        WebUser webUser = getWebuser();
        String userName = getParameter("userName");
        String phone = getParameter("phone");
        String msg = checkSubAccountInfo(null, userName, phone, addSubAccount, webUser);
        boolean isOk = false;
        if ("success".equals(msg)) {
            String pwd = SecurityUtil.passwordEncrypt(Globals.DEFAULT_PASSWORD);
            BusUser user = new BusUser();
            user.setUsername(userName);
            user.setPassword(pwd);
            user.setPayPassword(pwd);
            user.setPhone(phone);
            user.setPhoneStatus(CertificateStatusEnum.PASSED.getShortValue());
            user.setUserType(webUser.getUsertype().getShortValue());
            user.setRegistTime(new Date());
            user.setRegistIp(getRemoteIP());
            user.setHeadImg(Globals.DEFAULT_HEAD_URL);
            user.setInfoId(webUser.getInfoId());
            user.setRealStatus(webUser.getRealStatus());
            isOk = userService.insertUser(user, webUser.getUsertype(), RoleTypeEnum.ENTERPRISE_SUB, null, null);
            if (!isOk) {
                msg = ERROR;
            }
        }
        message = msg;
        return SUCCESS;
    }

    /**
     * 异步修改子账户
     * 
     * @return
     */
    public String editSubAccount() {
        WebUser webUser = getWebuser();
        String requestType = this.getParameter("requestType");
        int userId = this.getParamInt("userId");
        String userName = this.getParameter("userName");
        String phone = this.getParameter("phone");
        Map<String, Object> param;
        String msg = "success";
        BusUser newBusUser;
        if (!StringUtil.isEmpty(requestType) && userId > 0) {
            param = new HashMap<String, Object>();
            if (enableSubAccount.equals(requestType) || disableSubAccount.equals(requestType)) {
                param.put("InfoId", webUser.getInfoId());
                param.put("userId", userId);
                param.put("roleType", RoleTypeEnum.ENTERPRISE_SUB.getShortValue());// 子账户
                List<SubAccountModel> subAccountList = this.userService.selectSubAccounts(param);
                if (subAccountList != null && subAccountList.size() == 1) {
                    SubAccountModel subAccountModel = subAccountList.get(0);
                    newBusUser = new BusUser();
                    if (enableSubAccount.equals(requestType)) {
                        if (subAccountModel.getIsLock() != WhetherEnum.NO.getShortValue()) {
                            newBusUser.setIsLock(WhetherEnum.NO.getShortValue());
                            newBusUser.setId(userId);
                            this.userService.updateUserByPKSelective(newBusUser);
                        } else {
                            msg = "该子账户已被激活！！";
                        }
                    } else if (disableSubAccount.equals(requestType)) {
                        if (subAccountModel.getIsLock() != WhetherEnum.YES.getShortValue()) {
                            newBusUser.setIsLock(WhetherEnum.YES.getShortValue());
                            newBusUser.setId(userId);
                            this.userService.updateUserByPKSelective(newBusUser);
                        } else {
                            msg = "该子账户已被禁用！！";
                        }
                    }
                } else {
                    msg = "子账户不存在！！";
                }
            } else if (editSubAccount.equals(requestType)) {

                msg = checkSubAccountInfo(userId, userName, phone, editSubAccount, null);
                if ("success".equals(msg)) {
                    newBusUser = new BusUser();
                    newBusUser.setId(userId);
                    newBusUser.setUsername(userName);
                    newBusUser.setPhone(phone);
                    this.userService.updateUserByPKSelective(newBusUser);
                }
            }
        } else {
            msg = ERROR;
        }
        message = msg;
        return SUCCESS;
    }

    /**
     * 检查提交的子账户信息
     * 
     * @return
     */
    private String checkSubAccountInfo(Integer userId, String userName, String phone, String requestType, WebUser webUser) {
        String msg = "success";
        if (webUser != null && addSubAccount.equals(requestType)) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("infoId", webUser.getInfoId());
            param.put("roleType", RoleTypeEnum.ENTERPRISE_SUB.getShortValue());
            List<SubAccountModel> subAccountList = this.userService.selectSubAccounts(param);
            if (subAccountList != null && subAccountList.size() >= 5) {
                msg = "最多添加5个子账户！！";
            }
        }
        if ("success".equals(msg)) {
            if (StringUtil.isEmpty(userName)) {
                msg = "添加的用户名不能为空！！";
            } else if (StringUtil.isEmpty(phone)) {
                msg = "添加的手机号码不能为空！！";
            } else if (!Validator.isMobile(phone)) {
                msg = "请正确填写11位手机号！！";
            }
            if ("success".equals(msg) && addSubAccount.equals(requestType)) {
                BusUserExample emp = new BusUserExample();
                BusUserExample.Criteria cr = emp.createCriteria();
                cr.andPhoneEqualTo(phone);
                cr.andUserTypeEqualTo(UserTypeEnum.ENTERPRISE.getShortValue());
                List<BusUser> userPhoneList = userService.selectUserByExample(emp);
                if (userPhoneList != null && userPhoneList.size() > 0) {
                    msg = "手机号码'" + phone + "'已被注册！！";
                }
                if ("success".equals(msg)) {
                    BusUserExample example = new BusUserExample();
                    BusUserExample.Criteria crit = example.createCriteria();
                    crit.andUsernameEqualTo(userName);
                    crit.andUserTypeEqualTo(UserTypeEnum.ENTERPRISE.getShortValue());
                    List<BusUser> userNameList = userService.selectUserByExample(example);
                    if (userNameList != null && userNameList.size() > 0) {
                        msg = "用户名'" + userName + "'已被注册！！";
                    }
                }
            }

            else if ("success".equals(msg) && editSubAccount.equals(requestType)) {
                BusUserExample emp = new BusUserExample();
                BusUserExample.Criteria cr = emp.createCriteria();
                cr.andPhoneEqualTo(phone);
                cr.andUserTypeEqualTo(UserTypeEnum.ENTERPRISE.getShortValue());
                cr.andIdNotEqualTo(userId);
                List<BusUser> userPhoneList = userService.selectUserByExample(emp);
                if (userPhoneList != null && userPhoneList.size() > 0) {
                    msg = "手机号码'" + phone + "'已被注册！！";
                }
                if ("success".equals(msg)) {
                    BusUserExample example = new BusUserExample();
                    BusUserExample.Criteria crit = example.createCriteria();
                    crit.andUsernameEqualTo(userName);
                    crit.andUserTypeEqualTo(UserTypeEnum.ENTERPRISE.getShortValue());
                    crit.andIdNotEqualTo(userId);
                    List<BusUser> userNameList = userService.selectUserByExample(example);
                    if (userNameList != null && userNameList.size() > 0) {
                        msg = "用户名'" + userName + "'已被注册！！";
                    }
                }
            }

        }
        return msg;
    }

    private Map<String, Object> checkBaseInfo(WebUser webUser) {
        Map<String, Object> result = new HashMap<String, Object>();
        String msg = "success";
        // String busName = this.getParameter("busName");// 单位名称*
        // String limitYear = this.getParameter("limitYear");// 营业年限*
        // String scale = this.getParameter("scale");// 企业规模*
        // 业务信息
        String busDomain = this.getParameter("busDomain");// 主营业务应用*
        String domain = this.getParameter("domain");// 主营业务描述
        // 地域信息
        // String country = this.getParameter("country");// 国家地区
        int province = this.getParamInt("province");// 省份*
        int city = this.getParamInt("city");// 市区*
        String address = this.getParameter("address");// 街道地址
        String tel = this.getParameter("tel");// 联系电话*
        if (StringUtil.isEmpty(busDomain) || "0".equals(busDomain)) {
            msg = "请选择主营业务应用！！";
        } else if (province == 0) {
            msg = "请选择省份！！";
        } else if (city == 0) {
            msg = "请选择市区！！";
        } else if (StringUtil.isEmpty(tel)) {
            msg = "请填写联系电话！！";
        } else {
            BusBussinessInfo busBussinessInfo = new BusBussinessInfo();
            busBussinessInfo.setId(webUser.getInfoId());
            busBussinessInfo.setAddress(address);
            busBussinessInfo.setAddTime(new Date());
            busBussinessInfo.setBusDomain(busDomain);
            busBussinessInfo.setDomain(domain);
            busBussinessInfo.setProvince(province);
            busBussinessInfo.setCity(city);
            busBussinessInfo.setTel(tel);
            result.put("busBussinessInfo", busBussinessInfo);
        }
        result.put("msg", msg);
        return result;
    }

    /**
     * 验证手机验证码信息
     * 
     * @param phoneCode
     * @param webUser
     * @param seconds
     * @return
     */
    private String checkPhoneCode(String phoneCode, WebUser webUser) {
        String msg = "success";

        String realPhoneCode = BussinessCacheUtil.getPhoneCode(webUser.getUserId().toString());
        if (StringUtil.isEmpty(phoneCode)) {
            // this.removeRedisCache(webUser.getPhone());
            msg = "验证码不能为空！！";
        } else if (realPhoneCode == null) {
            msg = "请获取验证码！！";
        } else if (!phoneCode.equals(realPhoneCode)) {
            msg = "验证码不正确,请重新填写！！";
        }

        return msg;
    }

    /**
     * 验证修改密码信息
     * 
     * @param webUser
     * @param oldPwd
     * @param newPwd
     * @param checkNewPwd
     * @param timeLimit
     * @param actionType
     * @return
     */
    private String checkChangePwd(BusUser webUser, String oldPwd, String newPwd, String checkNewPwd, String changePwdType, String changePayPwdType) {
        String msg = "success";
        if (!changePwd.equals(changePwdType) && !changePayPwd.equals(changePayPwdType)) {
            msg = "fail";
        } else if (changePwd.equals(changePwdType) && !webUser.getPassword().equals(SecurityUtil.passwordEncrypt(oldPwd))) {
            msg = "登录原密码不正确！！";

        } else if (changePayPwd.equals(changePayPwdType) && !webUser.getPayPassword().equals(SecurityUtil.passwordEncrypt(oldPwd))) {
            msg = "交易原密码不正确！！";

        } else if (!newPwd.equals(checkNewPwd)) {
            msg = "密码不一致！！";
        } else if (!Validator.isPassword(newPwd)) {
            msg = " 密码为6-16位数字字母组合!";
        }
        return msg;
    }

    private String checkEmailCode(String emailCode, String userId) {
        String msg = "success";
        String realEmailCode = BussinessCacheUtil.getEamilCode(userId);
        if (emailCode.equals(realEmailCode)) {
            logger.info("清除session中eamilCode");
            BussinessCacheUtil.removieEamilCode(userId);
            BussinessCacheUtil.removieEamil(userId);
        } else {
            msg = "验证码不正确，请重新填写！！";
        }

        return msg;
    }

    private String checkChangePhoneCode(String changePhoneCode, String userId) {
        String msg = "success";
        String realChangePhoneCode = BussinessCacheUtil.getChangePhoneCode(userId);
        if (!changePhoneCode.equals(realChangePhoneCode)) {
            msg = "验证码不正确，请重新填写！！";
        } else {
            logger.info("清除session中changePhoneCode");
            BussinessCacheUtil.removieChangePhoneCode(userId);
            BussinessCacheUtil.removieChangePhoneNum(userId);
        }

        return msg;
    }

    private ValidMoneyCountBean initVlidMoney(int infoId) {
        Calendar now = Calendar.getInstance();
        int totalFailCount = Globals.VALID_FAIL_TOTAL; // 总共登录失败次数
        Object obj = Globals.VALID_COUNT_MAP.get(infoId);
        ValidMoneyCountBean validMoneyCountBean = null;
        boolean isRefuseValid = false;
        int failCount = 1;
        String errmsg = null;
        int ReTryCount;
        if (obj != null && obj instanceof ValidMoneyCountBean) {
            validMoneyCountBean = (ValidMoneyCountBean) obj;
            failCount = validMoneyCountBean.getFailCount() + 1;
            if (failCount >= totalFailCount) { // 若超过5次，必须延时等待
                isRefuseValid = true;
                errmsg = "outMaxCount";
                // errmsg = "验证失败超过" + totalFailCount + "次，请重新提交认证！！";
            } else {
                ReTryCount = totalFailCount - failCount;
                errmsg = "验证失败您还有" + ReTryCount + "次验证机会！！";
            }

        } else {
            validMoneyCountBean = new ValidMoneyCountBean();
            validMoneyCountBean.setInfoId(infoId);
            ReTryCount = totalFailCount - failCount;
            errmsg = "验证失败您还有" + ReTryCount + "次验证机会！！";
        }

        validMoneyCountBean.setFailCount(failCount);
        validMoneyCountBean.setReTryCount(totalFailCount - failCount);
        validMoneyCountBean.setValidTime(now.getTime());
        validMoneyCountBean.setValidFailMsg(errmsg);
        validMoneyCountBean.setRefuseValid(isRefuseValid);
        Globals.VALID_COUNT_MAP.put(infoId, validMoneyCountBean);
        return validMoneyCountBean;
    }

    private int safeLevel(BusUser busUser) {
        int level = 0;
        if (busUser.getPassword() != null) {
            level++;
        }
        if (busUser.getPhoneStatus() == CertificateStatusEnum.PASSED.getShortValue()) {
            level++;
        }
        if (busUser.getEmailStatus() == CertificateStatusEnum.PASSED.getShortValue()) {
            level++;
        }
        if (busUser.getRealStatus() == CertificateStatusEnum.PASSED.getShortValue()) {
            level++;
        }
        return level;
    }

    // set get

    public void setHeadImg(File headImg) {
        this.headImg = headImg;
    }

    public void setLicenceImg(File licenceImg) {
        this.licenceImg = licenceImg;
    }

    public void setCorporateImg(File corporateImg) {
        this.corporateImg = corporateImg;
    }

    public void setTransatorImg(File transatorImg) {
        this.transatorImg = transatorImg;
    }

    public List<SysDefArea> getCitys() {
        return citys;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public String getMessage() {
        return message;
    }

    public Page getPage() {
        return page;
    }

    public Map<String, Object> getResult() {
        return result;
    }
}
