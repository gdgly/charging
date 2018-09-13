package com.holley.charging.bms.device.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.bus.BusPileModelExample;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.charge.ChargeCurrentTypeEnum;
import com.holley.common.constants.charge.ChargeInterfaceTypeEnum;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.PileModelStatusEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.LogUtil;

import net.sf.json.JSONObject;

public class PileModelAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(PileModelAction.class);
    private static final long   serialVersionUID = 1L;

    private DeviceService       deviceService;
    private Page                page;

    /**
     * 充电桩型号列表初始化
     * 
     * @return
     */
    public String init() {
        initParams();
        return SUCCESS;
    }

    /**
     * 新增型号初始化
     * 
     * @return
     */
    public String addPileModelInit() {
        initParams();
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_ADD);
        return SUCCESS;
    }

    /**
     * 修改型号初始化
     * 
     * @return
     */
    public String editPileModelInit() {
        String modelid = this.getParameter("modelid");
        if (!NumberUtils.isNumber(modelid)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        BusPileModel record = deviceService.selectPileModelByPK(Integer.valueOf(modelid));
        initParams();
        this.getRequest().setAttribute("requestType", Globals.REQUEST_TYPE_EDIT);
        this.getRequest().setAttribute("pileModel", record);
        return SUCCESS;
    }

    /**
     * 充电桩型号详细初始化
     * 
     * @return
     */
    public String pileModelDetailInit() {
        String modelid = this.getParameter("modelid");
        if (!NumberUtils.isNumber(modelid)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        BusPileModel record = deviceService.selectPileModelByPK(Integer.valueOf(modelid));
        this.getRequest().setAttribute("pileModel", record);
        return SUCCESS;
    }

    /**
     * 充电桩型号列表
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        String keyword = this.getParameter("keyword");
        String chaway = this.getParameter("chaway");
        String chatype = this.getParameter("chatype");
        String intftype = this.getParameter("intftype");
        String modelstatus = this.getParameter("modelstatus");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(chaway, chatype, intftype, modelstatus)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isNotEmpty(chaway) && !"0".equals(chaway)) {
            params.put("chaway", Short.valueOf(chaway));
        }
        if (StringUtil.isNotEmpty(chatype) && !"0".equals(chatype)) {
            params.put("chatype", Short.valueOf(chatype));
        }
        if (StringUtil.isNotEmpty(intftype) && !"0".equals(intftype)) {
            params.put("standard", Short.valueOf(intftype));
        }
        if (StringUtil.isNotEmpty(modelstatus) && !"0".equals(modelstatus)) {
            params.put("status", Short.valueOf(modelstatus));
        }
        if (isExportExcel()) {
            List<BusPileModel> modelList = deviceService.selectPileModelByPage(params);
            String[] headsName = { "电桩型号编码", "品牌名称", "产品编号", "充电方式", "充电类型", "是否智能", "标准", "输入电压", "输出电压", "最大输出功率", "额定功率", "外壳材质", "尺寸", "防护等级", "缆线长度", "频率", "计量精度", "重量",
                                   "用户界面", "工作温度", "相对湿度", "海拔", "状态", "安装方式", "工作标准", "认证", "更新时间" };
            String[] properiesName = { "id", "brand", "num", "chaWayDesc", "chaTypeDesc", "isIntelligentDesc", "standardDesc", "inV", "outV", "maxP", "ratP", "hull", "size",
                                       "proLevel", "lineLen", "rate", "meaAcc", "weight", "window", "workTem", "relaHum", "altitude", "statusDesc", "insMethod", "workSta",
                                       "identify", "updateTimeStr" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(modelList, properiesName, headsName, BusPileModel.class);
            return null;
        } else {
            if (StringUtil.isNotDigits(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数格式不正确.";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<BusPileModel> modelList = deviceService.selectPileModelByPage(params);
            page.setRoot(modelList);
            this.page = page;
            return SUCCESS;
        }

    }

    /**
     * 新增型号
     * 
     * @return
     */
    public String addPileModel() {
        String modelJson = this.getParameter("pileModel");
        Map<String, Object> validMap = validModelParams(modelJson);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            BusPileModel pileModel = (BusPileModel) validMap.get("pileModel");
            pileModel.setUpdateTime(new Date());
            if (!validModelDB(pileModel)) {
                return SUCCESS;
            }
            if (deviceService.insertPileModelSelective(pileModel) > 0) {
                ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILEMODEL_UPDATETIME, pileModel.getUpdateTime());
                recordLog(pileModel, Globals.REQUEST_TYPE_ADD);
            } else {
                this.success = false;
                this.message = "型号新增失败!";
            }
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 修改型号
     * 
     * @return
     */
    public String editPileModel() {
        String modelJson = this.getParameter("pileModel");
        String modelid = this.getParameter("modelid");
        if (StringUtil.isEmpty(modelJson) || !NumberUtils.isNumber(modelid)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        Map<String, Object> validMap = validModelParams(modelJson);
        String msg = (String) validMap.get("msg");
        if (Globals.DEFAULT_MESSAGE.equals(msg)) {
            BusPileModel pileModel = (BusPileModel) validMap.get("pileModel");
            pileModel.setId(Integer.valueOf(modelid));
            pileModel.setUpdateTime(new Date());
            if (!validModelDB(pileModel)) {
                return SUCCESS;
            }
            if (deviceService.updatePileModelByPKSelective(pileModel) > 0) {
                ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILEMODEL_UPDATETIME, pileModel.getUpdateTime());
                recordLog(pileModel, Globals.REQUEST_TYPE_EDIT);
            }
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    private void initParams() {
        this.getRequest().setAttribute("currentTypeList", ChargeCurrentTypeEnum.values());
        this.getRequest().setAttribute("powerTypeList", ChargePowerTypeEnum.values());
        this.getRequest().setAttribute("intfTypeList", ChargeInterfaceTypeEnum.values());
        this.getRequest().setAttribute("statusList", PileModelStatusEnum.values());
        this.getRequest().setAttribute("whetherList", WhetherEnum.values());
    }

    /**
     * 校验用户信息
     * 
     * @param jsonObj
     * @return
     */
    private Map<String, Object> validModelParams(String jsonObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = Globals.DEFAULT_MESSAGE;
        BusPileModel pileModel = this.JsonToBean(jsonObj, BusPileModel.class);
        if (StringUtil.isEmpty(pileModel.getBrand())) {
            msg = "请输入品牌!";
        } else if (StringUtil.isEmpty(pileModel.getOutV())) {
            msg = "请输入输出电压!";
        } else if (StringUtil.isEmpty(pileModel.getRatP())) {
            msg = "请输入额定功率!";
        } else if (pileModel.getChaWay() == null || ChargeCurrentTypeEnum.getEnmuByValue(pileModel.getChaWay().intValue()) == null) {
            msg = "请选择充电方式!";
        } else if (pileModel.getChaType() == null || ChargePowerTypeEnum.getEnmuByValue(pileModel.getChaType().intValue()) == null) {
            msg = "请选择充电类型!";
        } else if (pileModel.getStandard() == null || ChargeInterfaceTypeEnum.getEnmuByValue(pileModel.getStandard().intValue()) == null) {
            msg = "请选择标准!";
        }
        map.put("pileModel", pileModel);
        map.put("msg", msg);
        return map;
    }

    /**
     * 校验数据库用户信息
     */
    private boolean validModelDB(BusPileModel record) {
        // 检验品牌是否重复
        BusPileModelExample emp = new BusPileModelExample();
        BusPileModelExample.Criteria cr = emp.createCriteria();
        cr.andBrandEqualTo(record.getBrand());
        if (record.getId() != null) {
            cr.andIdNotEqualTo(record.getId());
        }
        List<BusPileModel> modelList = deviceService.selectPileModelByExample(emp);
        if (modelList != null && modelList.size() > 0) {
            this.success = false;
            this.message = "品牌名称已经存在.";
            return false;
        }
        return true;
    }

    /**
     * 记录操作日志
     */
    private void recordLog(BusPileModel record, int requestType) {
        String webuserid = getSessionBmsUserId();
        Integer userid = NumberUtils.isNumber(webuserid) ? Integer.valueOf(webuserid) : 0;
        String content = JSONObject.fromObject(record, JsonUtil.returnJosnConfig(true, Date.class)).toString();
        if (Globals.REQUEST_TYPE_ADD == requestType) {
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.ADD, getRemoteIP(), "新增充电桩型号", content, true);
        } else {
            LogUtil.recordDocumentlog(userid, LogOperatorEnum.MODIFY, getRemoteIP(), "修改充电桩型号", content, true);
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Page getPage() {
        return page;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

}
