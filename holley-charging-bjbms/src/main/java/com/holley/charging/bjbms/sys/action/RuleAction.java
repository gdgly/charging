package com.holley.charging.bjbms.sys.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.RuleStatusEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysRule;
import com.holley.platform.model.sys.SysRuleExample;
import com.holley.platform.service.DictionaryService;
import com.holley.platform.util.LogUtil;
import com.holley.platform.util.RoleUtil;

import net.sf.json.JSONObject;

public class RuleAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private DictionaryService dictionaryService;
    private Page              page;

    /**
     * 系统规则列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("statusList", RuleStatusEnum.values());
        return SUCCESS;
    }

    /**
     * 新增规则初始化
     * 
     * @return
     */
    public String addRuleInit() {
        this.getRequest().setAttribute("statusList", RuleStatusEnum.values());
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_ADD);
        return SUCCESS;
    }

    /**
     * 修改规则初始化
     * 
     * @return
     */
    public String editRuleInit() {
        String id = this.getParameter("id");
        if (StringUtil.isEmpty(id)) {
            return "规则id为空";
        }
        SysRule sysRule = dictionaryService.selectRuleByPK(id);
        this.getRequest().setAttribute("sysRule", sysRule);
        this.getRequest().setAttribute("statusList", RuleStatusEnum.values());
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_EDIT);
        return SUCCESS;
    }

    /**
     * 分页查询系统规则列表
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        String keyword = getParameter("keyword");
        String status = getParameter("status");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(status)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (NumberUtils.isNumber(status) && Integer.parseInt(status) > 0) {
            params.put("status", Short.valueOf(status));
        }
        if (isExportExcel()) {
            List<SysRule> list = dictionaryService.selectRuleByPage(params);
            String[] headsName = { "规则编码", "规则名称", "规则状态", "规则值", "备注", "添加时间" };
            String[] properiesName = { "id", "name", "statusDesc", "ruleCheck", "remark", "addTimeStr" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, SysRule.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法.";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<SysRule> list = dictionaryService.selectRuleByPage(params);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        }
    }

    /**
     * 新增规则
     * 
     * @return
     */
    public String addRule() {
        String jsonObj = this.getParameter("sysRule");
        Map<String, Object> validMap = validRuleParams(jsonObj);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            SysRule sysRule = (SysRule) validMap.get("sysRule");
            String validDBMsg = validRuleDB(sysRule, Globals.REQUEST_TYPE_ADD);
            if (!Globals.DEFAULT_MESSAGE.equals(validDBMsg)) {
                this.success = false;
                this.message = validDBMsg;
                return SUCCESS;
            }
            sysRule.setAddTime(new Date());
            if (dictionaryService.insertRuleSelective(sysRule) > 0) {
                recordLog(sysRule, Globals.REQUEST_TYPE_ADD);
            } else {
                this.success = false;
                this.message = "新增规则失败";
            }

        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 修改规则
     * 
     * @return
     */
    public String editRule() {
        String jsonObj = this.getParameter("sysRule");
        if (StringUtil.isNull(jsonObj)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        Map<String, Object> validMap = validRuleParams(jsonObj);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            SysRule sysRule = (SysRule) validMap.get("sysRule");
            String validmsg = validRuleDB(sysRule, Globals.REQUEST_TYPE_EDIT);
            if (!Globals.DEFAULT_MESSAGE.equals(validmsg)) {
                this.success = false;
                this.message = validmsg;
                return SUCCESS;
            }
            sysRule.setAddTime(new Date());
            if (dictionaryService.updateRuleByPKSelective(sysRule) > 0) {
                RoleUtil.removeRule(sysRule.getId());
                recordLog(sysRule, Globals.REQUEST_TYPE_EDIT);
            } else {
                this.success = false;
                this.message = "修改规则失败";
            }
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 校验用户信息
     * 
     * @param jsonObj
     * @return
     */
    private Map<String, Object> validRuleParams(String jsonObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = Globals.DEFAULT_MESSAGE;
        SysRule sysRule = this.JsonToBean(jsonObj, SysRule.class);
        if (StringUtil.isEmpty(sysRule.getId())) {
            msg = "规则id不能为空.";
        } else if (StringUtil.isEmpty(sysRule.getName())) {
            msg = "规则名称不能为空.";
        } else if (sysRule.getStatus() == null) {
            msg = "规则状态不能为空.";
        } else if (StringUtil.isEmpty(sysRule.getRuleCheck())) {
            msg = "规则值不能为空.";
        }
        map.put("sysRule", sysRule);
        map.put("msg", msg);
        return map;
    }

    /**
     * 校验数据库用户信息
     */
    private String validRuleDB(SysRule sysRule, int requestType) {
        String msg = Globals.DEFAULT_MESSAGE;
        SysRuleExample emp = new SysRuleExample();
        SysRuleExample.Criteria cr = emp.createCriteria();
        if (Globals.REQUEST_TYPE_EDIT == requestType && sysRule.getId() != null) {
            cr.andIdNotEqualTo(sysRule.getId());
        }
        List<SysRule> list = dictionaryService.selectRuleByExample(emp);
        if (list == null || list.size() == 0) {
            return msg;
        }
        for (SysRule record : list) {
            if (sysRule.getId().equals(record.getId())) {
                return "规则id重复";
            } else if (sysRule.getName().equals(record.getName())) {
                return "规则名称重复";
            }
        }
        return msg;
    }

    /**
     * 记录操作日志
     */
    private void recordLog(SysRule record, int requestType) {
        String webuserid = getSessionBmsUserId();
        Integer userid = NumberUtils.isNumber(webuserid) ? Integer.valueOf(webuserid) : 0;
        String content = JSONObject.fromObject(record, JsonUtil.returnJosnConfig(false, Date.class)).toString();
        if (Globals.REQUEST_TYPE_ADD == requestType) {
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.ADD, getRemoteIP(), "新增规则配置", content, true);
        } else {
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.MODIFY, getRemoteIP(), "修改规则配置", content, true);
        }
    }

    public void setDictionaryService(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Page getPage() {
        return page;
    }
}
