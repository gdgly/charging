package com.holley.charging.bms.device.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusRepairPoint;
import com.holley.charging.service.website.PobObjectService;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.charge.ShowStausEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.LogUtil;
import com.holley.web.common.util.Validator;

import net.sf.json.JSONObject;

public class RepairPointAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private PobObjectService  pobObjectService;
    private Page              page;

    /**
     * 服务点列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("showStatusList", ShowStausEnum.values());
        this.getRequest().setAttribute("isShow", ShowStausEnum.SHOW.getShortValue());
        return SUCCESS;
    }

    /**
     * 新增或删除服务点初始化
     * 
     * @return
     */
    public String editRepairPointInit() {
        String requesttype = getParameter("requesttype");
        String id = getParameter("id");
        if (!StringUtil.isDigits(requesttype)) {
            this.getRequest().setAttribute("msg", "参数非法");
            return "msg";
        }
        if (Globals.REQUEST_TYPE_EDIT == Integer.parseInt(requesttype)) {
            if (!StringUtil.isDigits(id)) {
                this.getRequest().setAttribute("msg", "参数非法");
                return "msg";
            }
            BusRepairPoint record = pobObjectService.selectRepairPointByPK(Integer.valueOf(id));
            if (record == null) {
                this.getRequest().setAttribute("msg", "服务点不存在");
                return "msg";
            }
            this.getRequest().setAttribute("repairPoint", record);
        }
        this.getRequest().setAttribute("showStatusList", ShowStausEnum.values());
        this.getRequest().setAttribute("requestType", requesttype);
        return SUCCESS;
    }

    /**
     * 分页查询服务点
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        String keyword = getParameter("keyword");
        String isshow = getParameter("isshow");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isNotDigits(isshow)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isDigits(isshow) && !"0".equals(isshow)) {
            params.put("isshow", Short.valueOf(isshow));
        }
        if (isExportExcel()) {
            List<BusRepairPoint> list = pobObjectService.selectRepairPointByPage(params);
            String[] headsName = { "服务点编码", "名称", "地址", "状态", "经度", "纬度", "座机号码", "手机号码", "营业时间", "更新时间" };
            String[] properiesName = { "id", "name", "address", "isShowDesc", "lng", "lat", "tel", "phone", "workTime", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, BusRepairPoint.class);
            return null;
        } else {
            if (StringUtil.isNotDigits(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);
            List<BusRepairPoint> list = pobObjectService.selectRepairPointByPage(params);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;

        }

    }

    /**
     * 新增服务点
     * 
     * @return
     */
    public String addRepairPoint() {
        String objJson = this.getParameter("repairPoint");
        Map<String, Object> validMap = validObjParams(objJson);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            BusRepairPoint record = (BusRepairPoint) validMap.get("repairPoint");
            record.setAddTime(new Date());
            if (pobObjectService.insertRepairPointSelective(record) > 0) {
                // 更新redis缓存服务点更新时间
                ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_REPAIRPOINT_UPDATETIME, record.getAddTime());
                // 记录操作日志
                recordLog(record, Globals.REQUEST_TYPE_ADD);
            }
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 修改服务点
     * 
     * @return
     */
    public String editRepairPoint() {
        String objJson = this.getParameter("repairPoint");
        if (StringUtil.isEmpty(objJson)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        Map<String, Object> validMap = validObjParams(objJson);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            BusRepairPoint record = (BusRepairPoint) validMap.get("repairPoint");
            if (record.getId() == null) {
                this.success = false;
                this.message = "服务点编码为空";
                return SUCCESS;
            }
            record.setAddTime(new Date());
            if (pobObjectService.updateRepairPointByPKSelective(record) > 0) {
                ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_REPAIRPOINT_UPDATETIME, record.getAddTime());
                recordLog(record, Globals.REQUEST_TYPE_EDIT);
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
    private Map<String, Object> validObjParams(String jsonObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = Globals.DEFAULT_MESSAGE;
        BusRepairPoint record = this.JsonToBean(jsonObj, BusRepairPoint.class);
        if (StringUtil.isEmpty(record.getName())) {
            msg = "维修点名称不能为空.";
        } else if (StringUtil.isEmpty(record.getAddress())) {
            msg = "维修点地址不能为空.";
        } else if (record.getIsShow() == null) {
            msg = "维修点显示状态不能为空.";
        } else if (StringUtil.isNotNumber(record.getLat(), record.getLng())) {
            msg = "维修点经纬度不能为空.";
        } else if (!(Validator.isTelephone(record.getTel()) || Validator.isMobile(record.getTel()))) {
            msg = "座机号码为空或格式不正确";
        } else if (StringUtil.isNotEmpty(record.getPhone()) && !Validator.isMobile(record.getPhone())) {
            msg = "手机号码格式不正确";
        }
        map.put("repairPoint", record);
        map.put("msg", msg);
        return map;
    }

    /**
     * 记录操作日志
     */
    private void recordLog(BusRepairPoint record, int requestType) {
        String webuserid = getSessionBmsUserId();
        Integer userid = NumberUtils.isNumber(webuserid) ? Integer.valueOf(webuserid) : 0;
        String content = JSONObject.fromObject(record, JsonUtil.returnJosnConfig(true, Date.class)).toString();
        if (Globals.REQUEST_TYPE_ADD == requestType) {
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.ADD, getRemoteIP(), "新增服务点", content, true);
        } else {
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.MODIFY, getRemoteIP(), "修改服务点", content, true);
        }
    }

    public void setPobObjectService(PobObjectService pobObjectService) {
        this.pobObjectService = pobObjectService;
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
