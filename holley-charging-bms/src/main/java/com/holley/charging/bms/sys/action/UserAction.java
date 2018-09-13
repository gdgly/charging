package com.holley.charging.bms.sys.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;
import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusGroupInfo;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.RoleEnum;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.CertificateStatusEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.security.SecurityUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysAccountroleKey;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.model.sys.SysRole;
import com.holley.platform.model.sys.SysRoleExample;
import com.holley.platform.service.RoleService;
import com.holley.platform.util.CacheSysHolder;
import com.holley.web.common.util.Validator;

/**
 * 用户管理
 * 
 * @author zdd
 */
public class UserAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private UserService       userService;
    private RoleService       roleService;
    private Page              page;

    /**
     * 帐号列表初始化
     * 
     * @return
     */
    public String init() {
        CertificateStatusEnum[] realStatusList = CertificateStatusEnum.values();
        UserTypeEnum[] userTypeList = UserTypeEnum.values();
        WhetherEnum[] lockStatusList = WhetherEnum.values();
        this.getRequest().setAttribute("realStatusList", realStatusList);
        this.getRequest().setAttribute("userTypeList", userTypeList);
        this.getRequest().setAttribute("lockStatusList", lockStatusList);
        this.getRequest().setAttribute("lockStatus", WhetherEnum.NO.getShortValue());
        return SUCCESS;
    }

    /**
     * 用户新增初始化
     * 
     * @return
     */
    public String addUserInit() {
        String webuserid = getSessionBmsUserId();
        CertificateStatusEnum[] realStatusList = CertificateStatusEnum.values();
        UserTypeEnum[] userTypeList = UserTypeEnum.values();

        SysRoleExample emp = new SysRoleExample();
        SysRoleExample.Criteria cr = emp.createCriteria();
        List<Short> list = new ArrayList<Short>();
        list.add(RoleTypeEnum.PLATFORM.getShortValue());
        list.add(RoleTypeEnum.GROUP.getShortValue());
        cr.andTypeIn(list);
        // cr.andTypeEqualTo(RoleTypeEnum.PLATFORM.getShortValue());
        if (Globals.ADMIN_USER_ID != Integer.parseInt(webuserid)) {
            cr.andCreatorEqualTo(Integer.valueOf(webuserid));
        }
        List<SysLink> scaleTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.SCALE_TYPE.getValue());
        List<SysRole> roleList = roleService.selectRoleByExample(emp);
        this.getRequest().setAttribute("provinceList", CacheSysHolder.getProvinceList());
        this.getRequest().setAttribute("scaleTypeList", scaleTypeList);
        this.getRequest().setAttribute("realStatusList", realStatusList);
        this.getRequest().setAttribute("userTypeList", userTypeList);
        this.getRequest().setAttribute("roleList", roleList);
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_ADD);

        return SUCCESS;
    }

    /**
     * 用户修改初始化
     * 
     * @return
     */
    public String editUserInit() {
        String userid = this.getParameter("userid");
        if (!NumberUtils.isNumber(userid)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }

        String webuserid = getSessionBmsUserId();
        CertificateStatusEnum[] realStatusList = CertificateStatusEnum.values();
        UserTypeEnum[] userTypeList = UserTypeEnum.values();

        SysRoleExample emp = new SysRoleExample();
        SysRoleExample.Criteria cr = emp.createCriteria();
        cr.andTypeEqualTo(RoleTypeEnum.PLATFORM.getShortValue());
        if (Globals.ADMIN_USER_ID != Integer.parseInt(webuserid)) {
            cr.andCreatorEqualTo(Integer.valueOf(webuserid));
        }
        List<SysRole> roleList = roleService.selectRoleByExample(emp);

        Integer userId = Integer.valueOf(userid);
        BusUser busUser = userService.selectUserByPrimaryKey(userId);
        if (busUser != null) {
            SysAccountroleKey accountrole = roleService.selectAccountRoleByUserid(userId);
            if (accountrole != null) {
                busUser.setRoleid(accountrole.getRoleid());
            }
        }
        this.getRequest().setAttribute("realStatusList", realStatusList);
        this.getRequest().setAttribute("userTypeList", userTypeList);
        this.getRequest().setAttribute("roleList", roleList);
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_EDIT);
        this.getRequest().setAttribute("busUser", busUser);
        return SUCCESS;
    }

    public String userDetailInit() {
        String userid = getParameter("userid");
        String usertype = getParameter("usertype");
        if (StringUtil.isNotNumber(userid, usertype)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        BusUser busUser = userService.selectUserByPrimaryKey(Integer.valueOf(userid));
        // 用户角色信息
        if (busUser != null) {
            SysRole role = roleService.selectRoleByUserid(busUser.getId());
            if (role != null) {
                busUser.setRoleid(role.getId());
                busUser.setRolename(role.getRolename());
            }
        }
        // 查询运营商信息或个人信息
        if (busUser != null) {
            UserTypeEnum userTypeEnum = UserTypeEnum.getEnmuByValue(Integer.parseInt(usertype));
            if (UserTypeEnum.PERSON == userTypeEnum) {
                BusUserInfo userinfo = userService.selectUserInfoByPrimaryKey(busUser.getInfoId());
                this.getRequest().setAttribute("userInfo", userinfo);
            } else if (UserTypeEnum.ENTERPRISE == userTypeEnum) {
                BusBussinessInfo businfo = userService.selectBussinessByPrimaryKey(busUser.getInfoId());
                this.getRequest().setAttribute("busInfo", businfo);
            }

        }
        this.getRequest().setAttribute("busUser", busUser);
        return SUCCESS;
    }

    /**
     * 查询帐号列表
     * 
     * @return
     * @throws Exception
     * @throws WriteException
     */
    public String queryList() throws Exception {
        String keyword = this.getParameter("keyword");
        String usertype = this.getParameter("usertype");
        String realstatus = this.getParameter("realstatus");
        String lockstatus = this.getParameter("lockstatus");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(usertype, realstatus, lockstatus)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isNotEmpty(usertype) && !"0".equals(usertype)) {
            params.put("usertype", Short.valueOf(usertype));
        }
        if (StringUtil.isNotEmpty(realstatus) && !"0".equals(realstatus)) {
            params.put("realstatus", Short.valueOf(realstatus));
        }
        if (StringUtil.isNotEmpty(lockstatus) && !"0".equals(lockstatus)) {
            params.put("islock", Short.valueOf(lockstatus));
        }
        if (isExportExcel()) {
            List<BusUser> userList = userService.selectUserByPage(params);
            String[] headsName = { "用户编码", "用户昵称", "用户角色", "用户类型", "注册时间", "手机号码", "手机认证", "电子邮箱", "邮箱认证", "实名认证", "用户头像" };
            String[] properiesName = { "id", "username", "rolename", "userTypeDesc", "registTimeStr", "phone", "phoneStatusDesc", "email", "emailStatusDesc", "realStatusDesc",
                    "headImg" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(userList, properiesName, headsName, BusUser.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数格式不正确.";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<BusUser> userList = userService.selectUserByPage(params);
            page.setRoot(userList);
            this.page = page;
            return SUCCESS;
        }
    }

    /**
     * 新增用户
     * 
     * @return
     */
    public String addUser() {
        String userJson = this.getParameter("busUser");
        Map<String, Object> validMap = validUserParams(userJson);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            BusUser busUser = (BusUser) validMap.get("busUser");
            BusGroupInfo busGroupInfo = null;
            if (!validUserDB(busUser)) {
                return SUCCESS;
            }

            busUser.setPassword(SecurityUtil.passwordEncrypt(Globals.DEFAULT_PASSWORD));
            busUser.setPayPassword(SecurityUtil.passwordEncrypt(Globals.DEFAULT_PASSWORD));
            busUser.setUserType(UserTypeEnum.PLATFORM.getShortValue());
            busUser.setHeadImg(Globals.DEFAULT_HEAD_URL);
            busUser.setRegistTime(new Date());
            busUser.setRegistIp(getRemoteIP());
            if (RoleEnum.GROUP.getValue() == busUser.getRoleid()) {// 集团管理员
                busGroupInfo = (BusGroupInfo) validMap.get("busGroupInfo");
                busUser.setUserType(UserTypeEnum.GROUP.getShortValue());
                busUser.setPhoneStatus(CertificateStatusEnum.PASSED.getShortValue());
                busUser.setRealStatus(CertificateStatusEnum.PASSED.getShortValue());
                userService.insertUser(busUser, UserTypeEnum.GROUP, RoleTypeEnum.GROUP, null, busGroupInfo);
            } else {
                userService.insertUser(busUser, UserTypeEnum.PLATFORM, RoleTypeEnum.PLATFORM, busUser.getRoleid(), busGroupInfo);
            }

        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 修改用户
     * 
     * @return
     */
    public String editUser() {
        String userJson = this.getParameter("busUser");
        String userid = this.getParameter("userid");
        if (StringUtil.isEmpty(userJson) || !NumberUtils.isNumber(userid)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        Map<String, Object> validMap = validUserParams(userJson);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            BusUser busUser = (BusUser) validMap.get("busUser");
            busUser.setId(Integer.valueOf(userid));
            if (!validUserDB(busUser)) {
                return SUCCESS;
            }
            if (userService.updateUserAndRole(busUser) > 0) {
                // 更新缓存里用户相关信息及权限
                ChargingCacheUtil.removeUser(userid);
                ChargingCacheUtil.removeUserModule(userid);
                ChargingCacheUtil.removeUserModuleBtn(userid);
                // 更新缓存里登录账号的信息
                WebUser webUser = ChargingCacheUtil.getSession(userid, KeySessionTypeEnum.BMS, null);
                if (webUser != null) {
                    webUser.setUserName(busUser.getUsername());
                    webUser.setPhone(busUser.getPhone());
                    webUser.setRoleId(busUser.getRoleid());
                    ChargingCacheUtil.setSession(webUser, KeySessionTypeEnum.BMS, null);
                }
            }
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 删除用户（锁定用户，不真正删除）
     * 
     * @return
     */
    public String delUser() {
        String userid = getParameter("userid");
        if (StringUtil.isNotNumber(userid)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        BusUser record = new BusUser();
        record.setId(Integer.valueOf(userid));
        record.setIsLock(WhetherEnum.YES.getShortValue());
        if (userService.updateUserByPKSelective(record) > 0) {
            ChargingCacheUtil.removieSession(userid, KeySessionTypeEnum.BMS, null);
        } else {
            this.success = false;
            this.message = "删除用户失败.";
        }
        return SUCCESS;
    }

    /**
     * 初始化密码
     * 
     * @return
     */
    public String initPassword() {
        String userid = getParameter("userid");
        if (!NumberUtils.isNumber(userid)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        BusUser record = new BusUser();
        record.setId(Integer.valueOf(userid));
        record.setPassword(SecurityUtil.passwordEncrypt(Globals.DEFAULT_PASSWORD));
        if (userService.updateUserByPKSelective(record) > 0) {
            // 删除缓存，用户需重新登录
            ChargingCacheUtil.removieSession(userid, KeySessionTypeEnum.BMS, null);
        }
        return SUCCESS;
    }

    /**
     * 校验用户信息
     * 
     * @param jsonObj
     * @return
     */
    private Map<String, Object> validUserParams(String jsonObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = Globals.DEFAULT_MESSAGE;
        BusUser busUser = null;
        BusGroupInfo busGroupInfo = null;
        JSONObject jsonBean = JSONObject.fromObject(jsonObj);
        int roleid = jsonBean.getInt("roleid");
        if (roleid == 5) {
            // user信息
            String userName = jsonBean.getString("username");
            String phone = jsonBean.getString("phone");

            // 集团信息
            String groupName = jsonBean.getString("groupName");
            int scale = jsonBean.getInt("scale");
            int province = jsonBean.getInt("province");
            int city = jsonBean.getInt("city");
            String tel = jsonBean.getString("tel");
            String domain = jsonBean.getString("domain");
            String address = jsonBean.getString("address");
            if (StringUtil.isEmpty(userName)) {
                msg = "用户昵称不能为空.";
            } else if (userName.length() < 3) {
                msg = "用户昵称长度不小于3个字符.";
            } else if (StringUtil.isNotEmpty(phone) && !Validator.isMobile(phone)) {
                msg = "用户手机号码格式不正确.";
            } else if (StringUtil.isEmpty(groupName)) {
                msg = "集团名称不能为空.";
            } else if (scale <= 0) {
                msg = "请选择集团规模.";
            } else if (province <= 0) {
                msg = "请选择省份.";
            } else if (city <= 0) {
                msg = "请选择市区.";
            } else if (StringUtil.isEmpty(tel) || !Validator.isMobile(tel)) {
                msg = "联系电话填写不正确.";
            } else if (StringUtil.isEmpty(domain)) {
                msg = "集团主营业务不能为空.";
            } else if (StringUtil.isEmpty(address)) {
                msg = "集团详细地址不能为空.";
            } else {
                busUser = new BusUser();
                busUser.setRoleid(roleid);
                busUser.setUsername(userName);
                busUser.setPhone(phone);

                busGroupInfo = new BusGroupInfo();
                busGroupInfo.setGroupName(groupName);
                busGroupInfo.setScale(String.valueOf(scale));
                busGroupInfo.setProvince(province);
                busGroupInfo.setCity(city);
                busGroupInfo.setDomain(domain);
                busGroupInfo.setPhone(tel);
                busGroupInfo.setAddress(address);
                busGroupInfo.setAddTime(new Date());
            }

        } else {
            busUser = this.JsonToBean(jsonObj, BusUser.class);
            if (StringUtil.isEmpty(busUser.getUsername())) {
                msg = "用户昵称不能为空.";
            } else if (busUser.getUsername().length() < 3) {
                msg = "用户昵称长度不小于3个字符.";
            } else if (busUser.getRoleid() == null) {
                msg = "角色不能为空.";
            } else if (StringUtil.isNotEmpty(busUser.getPhone()) && !Validator.isMobile(busUser.getPhone())) {
                msg = "手机号码格式不正确.";
            }
        }

        map.put("busUser", busUser);
        map.put("busGroupInfo", busGroupInfo);
        map.put("msg", msg);
        return map;
    }

    /**
     * 校验数据库用户信息
     */
    private boolean validUserDB(BusUser busUser) {
        // 检验用户昵称是否重复
        BusUserExample emp = new BusUserExample();
        BusUserExample.Criteria cr = emp.createCriteria();
        cr.andUsernameEqualTo(busUser.getUsername());
        if (busUser.getId() != null) {
            cr.andIdNotEqualTo(busUser.getId());
        }
        List<BusUser> userList = userService.selectUserByExample(emp);
        if (userList != null && userList.size() > 0) {
            this.success = false;
            this.message = "用户昵称已经存在.";
            return false;
        }

        // 校验用户手机是否重复
        if (StringUtil.isNotEmpty(busUser.getPhone())) {
            emp.clear();
            cr = emp.createCriteria();
            cr.andUserTypeEqualTo(UserTypeEnum.PLATFORM.getShortValue());
            cr.andPhoneEqualTo(busUser.getPhone());
            if (busUser.getId() != null) {
                cr.andIdNotEqualTo(busUser.getId());
            }
            userList = userService.selectUserByExample(emp);
            if (userList != null && userList.size() > 0) {
                this.success = false;
                this.message = "手机号码已经存在.";
                return false;
            }
        }
        return true;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public Page getPage() {
        return page;
    }
}
