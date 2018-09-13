package com.holley.charging.bjbms.sys.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bms.ModuleBtnResult;
import com.holley.charging.model.bms.PermissinTreeNode;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.RoleStatusEnum;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysAccountroleExample;
import com.holley.platform.model.sys.SysAccountroleKey;
import com.holley.platform.model.sys.SysModuledef;
import com.holley.platform.model.sys.SysRole;
import com.holley.platform.model.sys.SysRolemoduleExample;
import com.holley.platform.model.sys.SysRolemoduleKey;
import com.holley.platform.service.RoleService;
import com.holley.platform.util.CachedModuledefUtil;

/**
 * 角色管理
 * 
 * @author zdd
 */
public class RoleAction extends BaseAction {

    private static final long       serialVersionUID = 1L;
    private RoleService             roleService;
    private List<SysRole>           roleList;
    private List<PermissinTreeNode> treeNodeList;

    /**
     * 角色列表初始化
     * 
     * @return
     */
    public String init() {
        RoleTypeEnum[] roleTypeList = { RoleTypeEnum.PLATFORM, RoleTypeEnum.COMPANY };
        this.getRequest().setAttribute("roleTypeList", roleTypeList);
        return SUCCESS;
    }

    /**
     * 角色新增初始化
     * 
     * @return
     */
    public String addRoleInit() {
        RoleTypeEnum[] roleTypeList = { RoleTypeEnum.PLATFORM, RoleTypeEnum.COMPANY };
        RoleStatusEnum[] roleStatusList = RoleStatusEnum.values();
        this.getRequest().setAttribute("roleTypeList", roleTypeList);
        this.getRequest().setAttribute("roleStatusList", roleStatusList);
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_ADD);
        return SUCCESS;
    }

    /**
     * 角色修改初始化
     * 
     * @return
     */
    public String editRoleInit() {
        String roleid = this.getParameter("roleid");
        if (StringUtil.isEmpty(roleid)) {
            this.success = false;
            this.message = "角色Id不能为空.";
            return SUCCESS;
        }
        SysRole sysRole = roleService.selectRoleByPK(Integer.valueOf(roleid));

        RoleTypeEnum[] roleTypeList = { RoleTypeEnum.PLATFORM, RoleTypeEnum.COMPANY };
        RoleStatusEnum[] roleStatusList = RoleStatusEnum.values();
        this.getRequest().setAttribute("roleTypeList", roleTypeList);
        this.getRequest().setAttribute("roleStatusList", roleStatusList);
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_EDIT);
        this.getRequest().setAttribute("sysRole", sysRole);
        return SUCCESS;
    }

    /**
     * 权限分配初始化
     * 
     * @return
     */
    public String permissionInit() {
        String roleid = this.getParameter("roleid");
        if (StringUtil.isEmpty(roleid)) {
            this.success = false;
            this.message = "角色Id不能为空.";
            return SUCCESS;
        }
        this.getRequest().setAttribute("roleid", roleid);
        return SUCCESS;
    }

    /**
     * 查询角色
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        String rolename = this.getParameter("rolename");
        // String roletype = this.getParameter("roletype");
        int roletype = getParamInt("roletype");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(rolename)) {
            params.put("rolename", rolename);
        }
        if (roletype > 0) {
            params.put("type", roletype);
        }
        List<SysRole> list = roleService.selectRoleByParam(params);
        if (isExportExcel()) {
            String[] headsName = { "角色编码", "角色名称", "描述", "角色类型", "状态", "创建人", "创建时间" };
            String[] properiesName = { "id", "rolename", "remark", "typedesc", "statusdesc", "creatorname", "addTimeStr" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, SysRole.class);
            return null;
        } else {
            this.roleList = list;
            return SUCCESS;
        }
    }

    /**
     * 新增角色
     * 
     * @return
     */
    public String addRole() {
        String roleJson = this.getParameter("sysRole");
        Map<String, Object> validMap = validRoleInfo(roleJson);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            String userid = getSessionBmsUserId();
            SysRole sysRole = (SysRole) validMap.get("sysRole");
            sysRole.setCreator(Integer.valueOf(userid));
            sysRole.setAddTime(new Date());
            sysRole.setStatus((short) 1);
            roleService.insertRole(sysRole);
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 修改角色
     * 
     * @return
     */
    public String editRole() {
        String roleJson = this.getParameter("sysRole");
        String roleid = this.getParameter("roleid");
        if (StringUtil.isEmpty(roleJson) || StringUtil.isEmpty(roleid)) {
            this.success = false;
            this.message = "提交的数据为空.";
            return SUCCESS;
        }
        Map<String, Object> validMap = validRoleInfo(roleJson);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            SysRole sysRole = (SysRole) validMap.get("sysRole");
            sysRole.setId(Integer.valueOf(roleid));
            roleService.updateRoleByPKSelective(sysRole);
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 删除角色
     * 
     * @return
     */
    public String delRole() {
        String roleid = this.getParameter("roleid");
        if (StringUtil.isEmpty(roleid)) {
            this.success = false;
            this.message = "角色编码不能为空.";
            return SUCCESS;
        }
        Integer roleId = Integer.valueOf(roleid);
        if (Globals.ADMIN_ROLE_ID == roleId.intValue()) {
            this.success = false;
            this.message = "系统自定义的最高管理员角色不能被删除.";
            return SUCCESS;
        }
        SysAccountroleExample aremp = new SysAccountroleExample();
        SysAccountroleExample.Criteria arcr = aremp.createCriteria();
        arcr.andRoleidEqualTo(roleId);
        List<SysAccountroleKey> accountRoleList = roleService.selectAccountroleByExample(aremp);
        if (accountRoleList != null && accountRoleList.size() > 0) {
            this.success = false;
            this.message = "该角色已被分配给用户,不能删除.";
            return SUCCESS;
        }
        roleService.deleteRoleByPK(roleId);
        return SUCCESS;
    }

    /**
     * 校验角色信息
     * 
     * @param jsonObj
     * @return
     */
    private Map<String, Object> validRoleInfo(String jsonObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = Globals.DEFAULT_MESSAGE;
        SysRole sysRole = this.JsonToBean(jsonObj, SysRole.class);
        if (StringUtil.isEmpty(sysRole.getRolename())) {
            msg = "角色名称不能为空.";
        } else if (sysRole.getType() == null) {
            msg = "角色类型不能为空.";
        }
        /*
         * else if (sysRole.getStatus() == null) { msg = "角色状态不能为空."; }
         */
        map.put("sysRole", sysRole);
        map.put("msg", msg);
        return map;
    }

    /**
     * 角色分配初始化
     * 
     * @return
     */
    public String loadPermission() {
        String roleid = this.getParameter("roleid");
        if (StringUtil.isEmpty(roleid)) {
            this.success = false;
            this.message = "角色id不能为空.";
            return SUCCESS;
        }

        SysRole sysRole = roleService.selectRoleByPK(Integer.valueOf(roleid));
        if (sysRole == null) {
            this.success = false;
            this.message = "角色不存在.";
            return SUCCESS;
        }
        RoleTypeEnum roleType = RoleTypeEnum.getEnmuByValue(sysRole.getType().intValue());
        Short systemid;
        if (RoleTypeEnum.PLATFORM == roleType || RoleTypeEnum.COMPANY == roleType) {
            systemid = Globals.PLATFORM_SYSTEM_ID;
        } else if (RoleTypeEnum.ENTERPRISE == roleType || RoleTypeEnum.ENTERPRISE_SUB == roleType) {
            systemid = Globals.ENTERPRISE_SYSTEM_ID;
        } else if (RoleTypeEnum.PERSON == roleType) {
            systemid = Globals.PERSON_SYSTEM_ID;
        } else {
            return SUCCESS;
        }

        // 查找当前用户所属角色的模块权限
        String userid = getSessionBmsUserId();
        WebUser webUser = ChargingCacheUtil.getSession(userid, KeySessionTypeEnum.BMS, null);
        Integer currentUserRoleid = webUser.getRoleId();
        List<SysModuledef> moduleList = new ArrayList<SysModuledef>();
        if (RoleTypeEnum.PLATFORM == roleType || RoleTypeEnum.COMPANY == roleType) {
            if (Globals.ADMIN_ROLE_ID == currentUserRoleid.intValue()) {
                // SysModuledefExample memp = new SysModuledefExample();
                // SysModuledefExample.Criteria mcr = memp.createCriteria();
                // mcr.andSystemidEqualTo((short) Globals.PLATFORM_SYSTEM_ID);
                // moduleList = roleService.selectModuledefByExample(memp);
                moduleList = CachedModuledefUtil.getModuleBySystemid((short) Globals.PLATFORM_SYSTEM_ID);
            } else {
                SysRolemoduleExample rmemp = new SysRolemoduleExample();
                SysRolemoduleExample.Criteria rmcr = rmemp.createCriteria();
                rmcr.andRoleidEqualTo(currentUserRoleid);
                List<SysRolemoduleKey> roleModuleList = roleService.selectRolemoduleByExample(rmemp);
                Map<String, SysModuledef> moduleMap = new HashMap<String, SysModuledef>();
                for (SysRolemoduleKey roleModule : roleModuleList) {
                    SysModuledef sysModuledef = CachedModuledefUtil.getModuledefByPrimaryKey(roleModule.getModuleid());
                    if (sysModuledef != null) {
                        moduleMap.put(sysModuledef.getModuleid(), sysModuledef);
                        moduleMap.putAll(CachedModuledefUtil.getParentModule(sysModuledef.getParentmoduleid()));
                    }
                }
                if (moduleMap != null && moduleMap.size() > 0) {
                    moduleList.addAll(moduleMap.values());
                    // 排序
                    CachedModuledefUtil.sortModuleList(moduleList);
                }
            }
        } else if (RoleTypeEnum.ENTERPRISE == roleType || RoleTypeEnum.ENTERPRISE_SUB == roleType) {
            moduleList = CachedModuledefUtil.getModuleBySystemid((short) Globals.ENTERPRISE_SYSTEM_ID);
        } else if (RoleTypeEnum.PERSON == roleType) {
            moduleList = CachedModuledefUtil.getModuleBySystemid((short) Globals.PERSON_SYSTEM_ID);
        }

        // 查找所选角色的模块权限
        SysRolemoduleExample rmemp = new SysRolemoduleExample();
        rmemp.createCriteria().andRoleidEqualTo(Integer.valueOf(roleid));
        List<SysRolemoduleKey> roleModuleList = roleService.selectRolemoduleByExample(rmemp);
        Map<String, String> roleModuleMap = new HashMap<String, String>();
        for (SysRolemoduleKey key : roleModuleList) {
            roleModuleMap.put(key.getModuleid(), key.getModuleid());
        }

        // 处理模块对象
        Map<String, ModuleBtnResult> map = new HashMap<String, ModuleBtnResult>();
        ModuleBtnResult moduleBtn = null;
        for (SysModuledef record : moduleList) {
            String moduleid = record.getModuleid();
            moduleBtn = new ModuleBtnResult();
            moduleBtn.setModuleid(moduleid);
            moduleBtn.setModuledisc(record.getModulename());
            if (roleModuleMap.get(moduleid) != null) moduleBtn.setChecked(true);
            map.put(moduleid, moduleBtn);
        }

        List<PermissinTreeNode> treeNodeList = new LinkedList<PermissinTreeNode>();
        treeNodeList.addAll(getRoleModuleWithPrivilege(null, systemid, map));
        this.treeNodeList = treeNodeList;
        return SUCCESS;
    }

    private List<PermissinTreeNode> getRoleModuleWithPrivilege(String parentmoduleid, Short systemId, Map<String, ModuleBtnResult> moduleBtnMap) {
        List<PermissinTreeNode> treeNodeList = new LinkedList<PermissinTreeNode>();
        List<SysModuledef> moduledefList = null;
        if (StringUtil.isEmpty(parentmoduleid)) {
            moduledefList = CachedModuledefUtil.getTopModuledefList();
        } else {
            moduledefList = CachedModuledefUtil.getChildModuleList(parentmoduleid);
        }
        for (SysModuledef record : moduledefList) {
            if (!systemId.equals(record.getSystemid())) continue;
            if (moduleBtnMap != null && moduleBtnMap.containsKey(record.getModuleid())) {
                ModuleBtnResult moduleBtn = moduleBtnMap.get(record.getModuleid());
                PermissinTreeNode node = new PermissinTreeNode();
                node.setId(record.getModuleid());
                node.setText(record.getModulename());
                node.setChecked(moduleBtn.getChecked());
                List<PermissinTreeNode> children = getRoleModuleWithPrivilege(record.getModuleid(), systemId, moduleBtnMap);
                if (children == null || children.size() == 0) {
                    node.setChildren(new LinkedList<PermissinTreeNode>());
                    node.leaf = true;
                } else {
                    node.setChildren(children);
                }
                treeNodeList.add(node);
            }
        }
        return treeNodeList;
    }

    public String savePermission() {
        String roleid = this.getParameter("roleid");
        String modules = this.getParameter("modules");
        if (!NumberUtils.isNumber(roleid)) {
            this.success = false;
            this.message = "角色id不能为空.";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(modules)) {
            this.success = false;
            this.message = "功能id不能为空.";
            return SUCCESS;
        }
        String[] moduleList = modules.split(",");
        if (roleService.saveRole(Integer.valueOf(roleid), moduleList) > 0) {
            this.success = true;
            this.message = "分配权限成功.";
        } else {
            this.success = true;
            this.message = "分配权限失败.";
        }
        return SUCCESS;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public List<SysRole> getRoleList() {
        return roleList;
    }

    public List<PermissinTreeNode> getTreeNodeList() {
        return treeNodeList;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

}
