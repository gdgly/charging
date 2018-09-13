package com.holley.charging.bms.sys.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.model.sys.SysLinkExample;
import com.holley.platform.model.sys.SysLinkType;
import com.holley.platform.model.sys.SysLinkTypeExample;
import com.holley.platform.service.DictionaryService;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.LogUtil;

import net.sf.json.JSONObject;

public class DictionaryAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private DictionaryService dictionaryService;
    private Page              page;

    /**
     * 数据字典列表初始化
     * 
     * @return
     */
    public String init() {
        initParam(true);
        return SUCCESS;
    }

    /**
     * 新增数据值初始化
     * 
     * @return
     */
    public String addLinkInit() {
        initParam(false);
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_ADD);
        return SUCCESS;
    }

    /**
     * 修改数据值初始化
     * 
     * @return
     */
    public String editLinkInit() {
        String id = this.getParameter("id");
        if (StringUtil.isEmpty(id)) {
            return "参数非法";
        }
        initParam(true);
        SysLink record = dictionaryService.selectLinkByPK(id);
        this.getRequest().setAttribute("sysLink", record);
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_EDIT);
        return SUCCESS;
    }

    /**
     * 根据数据类型查询数据列表
     * 
     * @return
     * @throws Exception
     */
    public String queryLinkList() throws Exception {
        String linktype = this.getParameter("linktype");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isEmpty(linktype)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(linktype) && !"0".equals(linktype)) {
            params.put("typeid", linktype);
        }
        if (isExportExcel()) {
            List<SysLink> linkList = dictionaryService.selectLinkByPage(params);
            String[] headsName = { "ID", "名称", "值", "类型" };
            String[] properiesName = { "id", "name", "value", "typeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(linkList, properiesName, headsName, SysLink.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法.";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<SysLink> linkList = dictionaryService.selectLinkByPage(params);
            page.setRoot(linkList);
            this.page = page;
            return SUCCESS;
        }
    }

    /**
     * 新增link
     * 
     * @return
     */
    public String addLink() {
        String jsonObj = this.getParameter("sysLink");
        if (StringUtil.isEmpty(jsonObj)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        Map<String, Object> validMap = validLinkParams(jsonObj);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            SysLink sysLink = (SysLink) validMap.get("sysLink");
            String validmsg = validLinkDB(sysLink, Globals.REQUEST_TYPE_ADD);
            if (!Globals.DEFAULT_MESSAGE.equals(validmsg)) {
                this.success = false;
                this.message = validmsg;
                return SUCCESS;
            }
            if (dictionaryService.insertLink(sysLink) > 0) {
                recordLog(sysLink, Globals.REQUEST_TYPE_ADD);
                // 更新本地缓存
                CacheSysHolder.reloadLink();
                // 供APP和WEB更新本地缓存
                ChargingCacheUtil.setLinkUpdate();
            }
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 修改link
     * 
     * @return
     */
    public String editLink() {
        String jsonObj = this.getParameter("sysLink");
        if (StringUtil.isNull(jsonObj)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        Map<String, Object> validMap = validLinkParams(jsonObj);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            SysLink sysLink = (SysLink) validMap.get("sysLink");
            String validmsg = validLinkDB(sysLink, Globals.REQUEST_TYPE_EDIT);
            if (!Globals.DEFAULT_MESSAGE.equals(validmsg)) {
                this.success = false;
                this.message = validmsg;
                return SUCCESS;
            }
            if (dictionaryService.updateLinkByPKSelective(sysLink) > 0) {
                recordLog(sysLink, Globals.REQUEST_TYPE_EDIT);
            }
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    private void initParam(boolean isall) {
        SysLinkTypeExample emp = new SysLinkTypeExample();
        if (!isall) {
            SysLinkTypeExample.Criteria cr = emp.createCriteria();
            List<String> list = new ArrayList<String>();
            list.add(LinkTypeEnum.ANDROID_VERSION.getValue());
            list.add(LinkTypeEnum.IOS_VERSION.getValue());
            list.add(LinkTypeEnum.BANK_NAME.getValue());
            list.add(LinkTypeEnum.BUS_DOMAIN.getValue());
            list.add(LinkTypeEnum.COM_TYPE.getValue());
            list.add(LinkTypeEnum.OPEN_TIME.getValue());
            cr.andIdIn(list);
        }
        emp.setOrderByClause("ADD_TIME DESC");
        List<SysLinkType> linkTypeList = dictionaryService.selectLinkTypeByExample(emp);
        this.getRequest().setAttribute("linkTypeList", linkTypeList);
    }

    /**
     * 校验sysLink信息
     * 
     * @param jsonObj
     * @return
     */
    private Map<String, Object> validLinkParams(String jsonObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = Globals.DEFAULT_MESSAGE;
        SysLink sysLink = this.JsonToBean(jsonObj, SysLink.class);
        if (StringUtil.isEmpty(sysLink.getId())) {
            msg = "id不能为空.";
        } else if (StringUtil.isEmpty(sysLink.getName())) {
            msg = "名称不能为空.";
        } else if (StringUtil.isEmpty(sysLink.getTypeId())) {
            msg = "数据类型不能为空.";
        } else if (StringUtil.isEmpty(sysLink.getValue())) {
            msg = "值不能为空.";
        }
        map.put("sysLink", sysLink);
        map.put("msg", msg);
        return map;
    }

    /**
     * 校验数据库sysLink信息
     */
    private String validLinkDB(SysLink sysLink, int requestType) {
        String msg = Globals.DEFAULT_MESSAGE;
        SysLinkExample emp = new SysLinkExample();
        SysLinkExample.Criteria cr = emp.createCriteria();
        cr.andTypeIdEqualTo(sysLink.getTypeId());
        if (Globals.REQUEST_TYPE_EDIT == requestType && sysLink.getId() != null) {
            cr.andIdNotEqualTo(sysLink.getId());
        }
        List<SysLink> list = dictionaryService.selectLinkByExample(emp);
        if (list == null || list.size() == 0) {
            return msg;
        }
        for (SysLink record : list) {
            if (sysLink.getId().equals(record.getId())) {
                return "id重复";
            } else if (sysLink.getName().equals(record.getName())) {
                return "名称重复";
            } else if (sysLink.getValue().equals(record.getValue())) {
                return "数据值重复";
            }
        }
        return msg;
    }

    /**
     * 记录操作日志
     */
    private void recordLog(SysLink record, int requestType) {
        String webuserid = getSessionBmsUserId();
        Integer userid = NumberUtils.isNumber(webuserid) ? Integer.valueOf(webuserid) : 0;
        String content = JSONObject.fromObject(record, JsonUtil.returnJosnConfig(false, Date.class)).toString();
        String desc = LinkTypeEnum.getText(record.getTypeId());
        if (Globals.REQUEST_TYPE_ADD == requestType) {
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.ADD, getRemoteIP(), "新增数据字典(" + desc + ")", content, true);
        } else {
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.MODIFY, getRemoteIP(), "修改数据字典(" + desc + ")", content, true);
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
