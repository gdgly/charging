package com.holley.charging.bms.run.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.common.util.CacheChargeHolder.PileRunStatusEnum;
import com.holley.charging.model.bms.StationRealData;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.def.DcsAlarmEventsModel;
import com.holley.charging.model.def.PileRunStatusModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.charging.service.website.PobObjectService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;

/**
 * 实时监控相关ACTION
 * 
 * @author zdd
 */
public class MonitoringAction extends BaseAction {

    private final static Logger      logger           = Logger.getLogger(MonitoringAction.class);
    private static final long        serialVersionUID = 1L;
    private PobObjectService         pobObjectService;
    private UserService              userService;
    private List<StationRealData>    stationRealList;
    private List<PileRunStatusModel> pileStatusList;
    private PileRunStatusModel       pileStatus;
    private Page                     page;
    private Map<String, Object>      result;
    private DeviceService            deviceService;

    /**
     * 设备告警
     * 
     * @return
     */
    public String deviceAlarmInit() {
        return SUCCESS;

    }

    public String queryDeviceAlarm() throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        int pageindex = getParamInt("pageindex");// 当前页
        int isExport = getParamInt("isExport");// 是否导出excel
        String startTime = getParameter("startTime");
        String endTime = getParameter("endTime");
        String keyword = getParameter("keyword");
        if (!StringUtil.isEmpty(startTime)) {
            Date startTimeDate = DateUtil.StrToDate(startTime, DateUtil.TIME_SHORT);
            param.put("startTime", startTime);
        }
        if (!StringUtil.isEmpty(endTime)) {
            Date endTimeDate = DateUtil.StrToDate(endTime, DateUtil.TIME_SHORT);
            param.put("endTime", endTime);
        }
        if (!StringUtil.isEmpty(keyword)) {
            param.put("keyword", keyword);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(pageindex, limit);
            param.put(Globals.PAGE, page);
            List<DcsAlarmEventsModel> list = deviceService.selectDcsAlarmEventsModelByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            param.put("maxLimit", MAX_EXPORT);
            List<DcsAlarmEventsModel> exportList = deviceService.selectDcsAlarmEventsModelByPage(param);
            String[] headsName = { "充电点名称", "桩名称", "地址", "事件", "事件等级", "事件发生时间" };
            String[] properiesName = { "stationName", "pileName", "address", "described", "levelDesc", "eventTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, DcsAlarmEventsModel.class);
            return null;
        }
        return SUCCESS;
    }

    /**
     * 设备监控地图初始
     * 
     * @return
     */
    public String deviceMapInit() {
        WebUser webUser = getBmsWebuser();
        int stationId = this.getParamInt("stationId");
        if (stationId > 0) {
            PobChargingStation pobChargingStation = CacheChargeHolder.getChargeStationById(stationId);
            // List<PobChargingPile> pileList =
            // CacheChargeHolder.getOnlineChargePileListByInfoIdAndStationId(webUser.getInfoId(), stationId);
            List<PileRunStatusModel> modelList = CacheChargeHolder.getPileStatusListByStationid(stationId);
            this.getRequest().setAttribute("jsonPobChargingStation", JsonUtil.bean2json(pobChargingStation));
            this.getRequest().setAttribute("jsonPileList", JsonUtil.list2json(modelList));
        } else {
            this.getRequest().setAttribute("jsonPobChargingStation", "{}");
            this.getRequest().setAttribute("jsonPileList", "{}");
        }
        int freePile = 0;
        int chaPile = 0;
        int appPile = 0;
        int offPile = 0;
        int unPile = 0;
        Short status = 0;

        List<PobChargingStation> stationList = CacheChargeHolder.getChargeStationListByMecAndType(webUser.getInfoId(), webUser.getUsertype().getShortValue());
        List<PileRunStatusModel> pileList = CacheChargeHolder.getPileStatusListByStationList(stationList);
        // List<PileRunStatusModel> pileList = CacheChargeHolder.getPileStatusListByInfoId(1,
        // UserTypeEnum.PLATFORM.getShortValue());
        for (PileRunStatusModel pile : pileList) {
            status = pile.getStatus();
            if (status == PileRunStatusEnum.IDLE.getShortValue()) {
                freePile++;
            } else if (status == PileRunStatusEnum.CHARGING.getShortValue()) {
                chaPile++;
            } else if (status == PileRunStatusEnum.APPOINTMENT.getShortValue()) {
                appPile++;
            } else if (status == PileRunStatusEnum.OFFLINE.getShortValue()) {
                offPile++;
            } else {
                unPile++;
            }
        }

        getRequest().setAttribute("pileList", pileList);
        getRequest().setAttribute("freePile", freePile);
        getRequest().setAttribute("chaPile", chaPile);
        getRequest().setAttribute("appPile", appPile);
        getRequest().setAttribute("offPile", offPile);
        getRequest().setAttribute("stationList", stationList);
        String jsonList = JsonUtil.list2json(stationList);
        getRequest().setAttribute("jsonList", jsonList);
        return SUCCESS;
    }

    /**
     * 异步查询所在充电点桩实时列表
     * 
     * @return
     */
    public String searchPileListByStationId() {
        Map<String, Object> map = new HashMap<String, Object>();
        int stationId = this.getParamInt("stationId");
        List<PileRunStatusModel> modelList = CacheChargeHolder.getPileStatusListByStationid(stationId);
        map.put("pileList", modelList);
        this.result = map;
        return SUCCESS;
    }

    /**
     * 实时数据初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("pileStatusList", PileRunStatusEnum.values());
        this.getRequest().setAttribute("provinceList", CacheSysHolder.getProvinceList());
        return SUCCESS;
    }

    /**
     * 分页查询充电点实时状态
     * 
     * @return
     * @throws Exception
     */
    public String queryStationList() throws Exception {
        String keyword = this.getParameter("keyword");
        String province = this.getParameter("province");
        String city = this.getParameter("city");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotDigits(province, city)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }

        List<StationRealData> realDataList = new ArrayList<StationRealData>();
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtils.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isDigits(province) && Integer.parseInt(province) > 0) {
            params.put("province", Integer.valueOf(province));
        }

        if (StringUtil.isDigits(city) && Integer.parseInt(city) > 0) {
            params.put("city", Integer.valueOf(city));
        }
        Page page = null;
        if (!isExportExcel()) {
            if (StringUtil.isNotDigits(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数格式不正确.";
                return SUCCESS;
            }
            page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);
        }
        List<PobChargingStation> list = pobObjectService.selectHasPileStationByPage(params);
        Map<String, Integer> pilenumMap;
        StationRealData realdata;
        for (PobChargingStation record : list) {
            pilenumMap = getPileStatusNumByStationid(record.getId());
            realdata = new StationRealData();
            realdata.setStationid(record.getId());
            realdata.setStationname(record.getStationName());
            realdata.setAddress(record.getAddress());
            realdata.setBusmecname(record.getBusMecName());
            realdata.setBustypedesc(record.getBusTypeDesc());
            realdata.setCitydesc(record.getProvinceDesc() + (StringUtil.isEmpty(record.getCityDesc()) ? "" : "，" + record.getCityDesc()));
            realdata.setPilenum(pilenumMap.get("pilenum"));
            realdata.setUnknownum(pilenumMap.get("status_" + PileRunStatusEnum.UNKNOW.getValue()));
            realdata.setIdlenum(pilenumMap.get("status_" + PileRunStatusEnum.IDLE.getValue()));
            realdata.setChargenum(pilenumMap.get("status_" + PileRunStatusEnum.CHARGING.getValue()));
            realdata.setAppnum(pilenumMap.get("status_" + PileRunStatusEnum.APPOINTMENT.getValue()));
            realdata.setOfflinenum(pilenumMap.get("status_" + PileRunStatusEnum.OFFLINE.getValue()));
            realdata.setFaultnum(pilenumMap.get("status_" + PileRunStatusEnum.FAULT.getValue()));
            realDataList.add(realdata);
        }
        if (isExportExcel()) {
            String[] headsName = { "充电点编码", "充电点名称", "地区", "运营机构", "运营类型", "电桩个数", "空闲数", "充电数", "预约数", "离线数", "故障数", "忙碌数" };
            String[] properiesName = { "stationid", "stationname", "citydesc", "busmecname", "bustypedesc", "pilenum", "idlenum", "chargenum", "appnum", "offlinenum", "faultnum",
                    "unknownum" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(realDataList, properiesName, headsName, StationRealData.class);
            return null;
        } else {
            page.setRoot(realDataList);
            this.page = page;
            return SUCCESS;
        }
    }

    /**
     * 查询某个充电点下的充电桩的状态
     * 
     * @return
     * @throws Exception
     */
    public String queryPileList() throws Exception {
        String stationid = this.getParameter("stationid");
        String pilestatus = this.getParameter("pilestatus");
        if (StringUtil.isNotNumber(stationid, pilestatus)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }
        List<PileRunStatusModel> list = CacheChargeHolder.getPileStatusListByStationid(Integer.valueOf(stationid));
        List<PileRunStatusModel> resultList = new ArrayList<PileRunStatusModel>();
        if (StringUtil.isNotEmpty(pilestatus) && !"-1".equals(pilestatus)) {
            PileRunStatusEnum statusEnum = PileRunStatusEnum.getEnmuByValue(Integer.parseInt(pilestatus));
            if (statusEnum == null) {
                this.success = false;
                this.message = "参数格式不正确.";
                return SUCCESS;
            }
            for (PileRunStatusModel record : list) {
                if (statusEnum.getShortValue().equals(record.getStatus())) {
                    resultList.add(record);
                }
            }
        } else {
            resultList = list;
        }
        if (isExportExcel()) {
            String[] headsName = { "充电桩编码", "充电桩名称", "所属充电点", "桩编号", "通信协议", "通信地址", "电桩类型", "充电方式", "支付方式", "电压(V)", "电流(A)", "状态" };
            String[] properiesName = { "pileid", "pilename", "stationname", "pilecode", "comtypeDesc", "comaddr", "powertypedesc", "currenttypedesc", "paywaydesc", "outv", "outi",
                    "statusdisc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(resultList, properiesName, headsName, PileRunStatusModel.class);
            return null;
        } else {
            this.pileStatusList = resultList;
            return SUCCESS;
        }
    }

    public String queryPileStatus() {
        String pileid = this.getParameter("pileid");
        if (StringUtil.isNotNumber(pileid)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }
        Integer pileId = Integer.valueOf(pileid);
        PileRunStatusModel record = CacheChargeHolder.getPileStatusById(pileId);
        if (record.getUserid() != null && record.getUserid() != 0) {
            BusUser user = userService.selectBusUserByPrimaryKey(record.getUserid());
            if (user != null) {
                record.setUsername(user.getUsername());
                record.setPhone(user.getPhone());
            }
        }
        this.pileStatus = record;
        return SUCCESS;
    }

    /**
     * 统计充电点下充电桩各状态数量
     * 
     * @param stationid
     * @return
     */
    public static Map<String, Integer> getPileStatusNumByStationid(Integer stationid) {
        List<PobChargingPile> pileList = CacheChargeHolder.getChargePileListByStationid(stationid);
        PileRunStatusModel runstatus;
        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        String key;
        int num = 0;
        if (pileList != null && pileList.size() > 0) {
            for (PobChargingPile record : pileList) {
                runstatus = CacheChargeHolder.getPileStatusById(record.getId());
                // logger.info("充电桩状态------------pileid=" + record.getId() + ",status=" + runstatus.getStatusdisc());
                if (runstatus == null || runstatus.getStatus() == null) continue;
                PileRunStatusEnum status = PileRunStatusEnum.getEnmuByValue(runstatus.getStatus().intValue());
                if (status == null) continue;

                key = "status_" + status.getValue();
                if (resultMap.containsKey(key)) {
                    num = resultMap.get(key);
                    num++;
                } else {
                    num = 1;
                }
                resultMap.put(key, num);
            }
        }
        resultMap.put("pilenum", pileList == null ? null : pileList.size());
        return resultMap;
    }

    public void setPobObjectService(PobObjectService pobObjectService) {
        this.pobObjectService = pobObjectService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public List<StationRealData> getStationRealList() {
        return stationRealList;
    }

    public List<PileRunStatusModel> getPileStatusList() {
        return pileStatusList;
    }

    public PileRunStatusModel getPileStatus() {
        return pileStatus;
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

    public Map<String, Object> getResult() {
        return result;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

}
