package com.holley.charging.bjbms.run.action;

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
import com.holley.charging.model.dcs.DcsHisyc;
import com.holley.charging.model.dcs.DcsHisycExample;
import com.holley.charging.model.def.DcsAlarmEventsModel;
import com.holley.charging.model.def.PileRunStatusModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.charging.service.website.PobObjectService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
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
    // private static int OUT_V = 33; // 充电机单相输出电压
    // private static int OUT_A = 34; // 充电机单相输出电流
    private static int               OUT_V            = 31;                                      // 充电机单相输出电压
    private static int               OUT_A            = 32;                                      // 充电机单相输出电流
    private static int               OUT_V1           = 25;                                      // 充电机三相输出电压
    private static int               OUT_V2           = 26;                                      // 充电机三相输出电压
    private static int               OUT_V3           = 27;                                      // 充电机三相输出电压
    private static int               OUT_A1           = 28;                                      // 充电机三相输出电流
    private static int               OUT_A2           = 29;                                      // 充电机三相输出电流
    private static int               OUT_A3           = 30;                                      // 充电机三相输出电流
    private static int               SINGLE_PHASE     = 1;                                       // 单相
    private static int               THREE_PHASE      = 2;                                       // 三相
    private String                   stationName;
    private int                      idleCount        = 0;                                       // 空闲桩数量
    private int                      chargingCount    = 0;                                       // 充电中桩数量
    private int                      unLineCount      = 0;                                       // 离线桩数量
    private int                      faultCount       = 0;                                       // 故障桩数量
    private int                      busyCount        = 0;                                       // 忙碌中桩数量
    private int                      pileCount        = 0;                                       // 桩总数量
    private int                      fastCount        = 0;                                       // 快充桩总数量
    private int                      slowCount        = 0;                                       // 慢充桩总数量
    private int                      selectStationId;

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
        isSubCompany(param);
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(pageindex, limit);
            param.put(Globals.PAGE, page);
            List<DcsAlarmEventsModel> list = deviceService.selectDcsAlarmEventsModelByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            param.put("maxLimit", MAX_EXPORT);
            List<DcsAlarmEventsModel> exportList = deviceService.selectDcsAlarmEventsModelByPage(param);
            String[] headsName = { "充电站名称", "桩名称", "地址", "事件", "事件等级", "事件发生时间" };
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
        int badPile = 0;
        Short status = 0;

        List<PobChargingStation> stationList = CacheChargeHolder.getChargeStationListByMecAndType(webUser.getUserId(), webUser.getUsertype().getShortValue());
        List<PileRunStatusModel> pileList = CacheChargeHolder.getPileStatusListByStationList(stationList);
        // List<PileRunStatusModel> pileList = CacheChargeHolder.getPileStatusListByInfoId(1,
        // UserTypeEnum.PLATFORM.getShortValue());
        if (pileList != null && pileList.size() > 0) {
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
                } else if (status == PileRunStatusEnum.FAULT.getShortValue()) {
                    badPile++;
                } else {
                    unPile++;
                }
            }
        }

        getRequest().setAttribute("pileList", pileList);
        getRequest().setAttribute("freePile", freePile);// 空闲桩
        getRequest().setAttribute("chaPile", chaPile);// 充电桩
        getRequest().setAttribute("appPile", appPile);// 预约桩
        getRequest().setAttribute("offPile", offPile);// 离线桩
        getRequest().setAttribute("badPile", badPile);// 故障桩
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
            String[] headsName = { "充电站编码", "充电点名称", "地区", "运营机构", "运营类型", "电桩个数", "空闲数", "充电数", "预约数", "离线数", "故障数", "忙碌数" };
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
            String[] headsName = { "充电桩编码", "充电桩名称", "所属充电站", "桩编号", "通信协议", "通信地址", "电桩类型", "充电方式", "支付方式", "电压(V)", "电流(A)", "状态" };
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

    /**
     * 单站监控
     * 
     * @return
     */
    public String singleStaMonitor() {
        List<PileRunStatusEnum> list = PileRunStatusEnum.getPileRunStatusList();
        getRequest().setAttribute("list", list);
        return SUCCESS;
    }

    /**
     * 异步加载单站桩信息
     * 
     * @return
     */
    public String querySingleStaPile() {
        WebUser user = getBmsWebuser();
        int idleCount = 0;// 空闲桩数量
        int chargingCount = 0;// 充电中桩数量
        int unLineCount = 0;// 离线桩数量
        int faultCount = 0;// 故障桩数量
        int busyCount = 0;// 忙碌中桩数量
        int fastCount = 0;// 快充桩数量
        int slowCount = 0;// 慢充桩数量
        int stationId = getParamInt("stationId");
        String sta = getParameter("pileSta");// 终端设备
        Short pileSta = Short.valueOf(sta);
        String pileCode = getParameter("pileCode");
        PobChargingStation station = null;
        if (stationId > 0) {
            PobChargingStationExample emp = new PobChargingStationExample();
            PobChargingStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(stationId);
            station = deviceService.selectPobChargingStationByExample(emp);

        } else {
            if (isSubCompany(null)) {
                PobChargingStationExample emp = new PobChargingStationExample();
                PobChargingStationExample.Criteria cr = emp.createCriteria();
                cr.andBusMecEqualTo(user.getUserId());
                station = deviceService.selectPobChargingStationByExample(emp);
            } else {
                station = deviceService.selectPobChargingStationByExample(null);
            }

        }
        this.stationName = station.getStationName();
        this.selectStationId = station.getId();
        List<PileRunStatusModel> list = CacheChargeHolder.getPileStatusListByStationid(station.getId());

        List<PileRunStatusModel> listResult = new ArrayList<PileRunStatusModel>();
        List<PileRunStatusModel> tempListResult = new ArrayList<PileRunStatusModel>();
        if (pileSta >= 0 || !StringUtil.isEmpty(pileCode)) {
            if (pileSta >= 0) {
                if (list != null && list.size() > 0) {
                    for (PileRunStatusModel m : list) {
                        if (pileSta.equals(m.getStatus())) {
                            listResult.add(m);
                        }
                    }
                }
            }

            if (!StringUtil.isEmpty(pileCode)) {
                if (pileSta >= 0) {
                    for (PileRunStatusModel m : listResult) {
                        if (m.getPilecode().contains(pileCode)) {
                            tempListResult.add(m);
                        }
                    }
                    listResult = tempListResult;
                } else {
                    for (PileRunStatusModel m : list) {
                        if (m.getPilecode().contains(pileCode)) {
                            listResult.add(m);
                        }
                    }
                }
            }
        } else {
            listResult = list;
        }
        if (listResult != null && listResult.size() > 0) {
            this.pileStatusList = listResult;
            this.pileCount = listResult.size();
            for (PileRunStatusModel model : listResult) {
                if (ChargePowerTypeEnum.FAST.getShortValue().equals(model.getPiletype())) {// 快充桩
                    fastCount++;
                } else if (ChargePowerTypeEnum.SLOW.getShortValue().equals(model.getPiletype())) {// 慢充桩
                    slowCount++;
                }
                if (PileRunStatusEnum.IDLE.getShortValue().equals(model.getStatus())) {// 空闲
                    idleCount++;
                } else if (PileRunStatusEnum.CHARGING.getShortValue().equals(model.getStatus())) {// 充电中
                    chargingCount++;
                } else if (PileRunStatusEnum.FAULT.getShortValue().equals(model.getStatus())) {// 故障
                    faultCount++;
                } else if (PileRunStatusEnum.OFFLINE.getShortValue().equals(model.getStatus())) {// 离线
                    unLineCount++;
                } else if (PileRunStatusEnum.UNKNOW.getShortValue().equals(model.getStatus())) {// 忙碌中
                    busyCount++;
                }
            }
            this.fastCount = fastCount;
            this.slowCount = slowCount;
            this.idleCount = idleCount;
            this.chargingCount = chargingCount;
            this.faultCount = faultCount;
            this.unLineCount = unLineCount;
            this.busyCount = busyCount;
        }

        return SUCCESS;
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
     * 查看充电桩电压电流曲线
     * 
     * @return
     */
    public String queryPileChart() {
        int pileId = getParamInt("pileId");
        int pileType = getParamInt("pileType");
        int addDay = getParamInt("addDay");
        // pileId = 28;
        Map<String, Object> map = new HashMap<String, Object>();
        List<Integer> dataTypeList = new ArrayList<Integer>();
        String msg = "success";
        if (pileId > 0 && pileType > 0) {
            Date now = new Date();
            if (SINGLE_PHASE == pileType) {
                dataTypeList.add(OUT_V);
                dataTypeList.add(OUT_A);
            } else if (THREE_PHASE == pileType) {
                dataTypeList.add(OUT_V1);
                dataTypeList.add(OUT_V2);
                dataTypeList.add(OUT_V3);
                dataTypeList.add(OUT_A1);
                dataTypeList.add(OUT_A2);
                dataTypeList.add(OUT_A3);
            }
            DcsHisycExample emp = new DcsHisycExample();
            DcsHisycExample.Criteria cr = emp.createCriteria();
            cr.andChargeidEqualTo(pileId);
            cr.andDatatypeIn(dataTypeList);
            Date last = DateUtil.getLastHour(now);
            now = DateUtil.addDays(now, addDay);
            Date first = DateUtil.getFirstHour(now);
            cr.andDatatimeGreaterThanOrEqualTo(first);
            cr.andDatatimeLessThanOrEqualTo(last);
            List<DcsHisyc> list = deviceService.selectDcsHisycByExample(emp);
            if (list != null && list.size() > 0) {
                crateData(pileType, list, map);
            } else {
                msg = "暂无监控数据！！";
            }
        }
        map.put("msg", msg);
        this.result = map;
        return SUCCESS;
    }

    private void crateData(int pileType, List<DcsHisyc> list, Map<String, Object> map) {
        if (SINGLE_PHASE == pileType) {// 单相数据
            List<String> dataTimeV = new ArrayList<String>();
            List<Double> dataV = new ArrayList<Double>();
            List<String> dataTimeA = new ArrayList<String>();
            List<Double> dataA = new ArrayList<Double>();
            DcsHisyc yc = null;
            // DcsHisyc yc2 = null;
            // int countV = 0;
            // int countA = 0;
            for (int i = 0; i < list.size(); i++) {
                yc = list.get(i);
                /*
                 * if (i > 0) { yc2 = list.get(i - 1); }
                 */
                if (OUT_V == yc.getDatatype()) {// 输出电压
                    dataTimeV.add(DateUtil.DateToLongStr(yc.getDatatime()));

                    /*
                     * if (countV == 0) { dataTimeV.add(DateUtil.DateToLongStr(yc.getDatatime()) + " F"); countV++; }
                     * else { if
                     * (!DateUtil.DateToShortStr(yc.getDatatime()).equals(DateUtil.DateToShortStr(yc2.getDatatime()))) {
                     * dataTimeV.add(DateUtil.DateToLongStr(yc.getDatatime()) + " F"); } else {
                     * dataTimeV.add(DateUtil.DateToLongStr(yc.getDatatime())); } }
                     */
                    dataV.add(yc.getValue());
                } else if (OUT_A == yc.getDatatype()) {
                    dataTimeA.add(DateUtil.DateToLongStr(yc.getDatatime()));
                    /*
                     * if (countA == 0) { dataTimeA.add(DateUtil.DateToLongStr(yc.getDatatime()) + " F"); countA++; }
                     * else { if
                     * (!DateUtil.DateToShortStr(yc.getDatatime()).equals(DateUtil.DateToShortStr(yc2.getDatatime()))) {
                     * dataTimeA.add(DateUtil.DateToLongStr(yc.getDatatime()) + " F"); } else {
                     * dataTimeA.add(DateUtil.DateToLongStr(yc.getDatatime())); } }
                     */

                    dataA.add(yc.getValue());
                }
            }
            map.put("dataTimeV", dataTimeV);
            map.put("dataV", dataV);
            map.put("dataTimeA", dataTimeA);
            map.put("dataA", dataA);
        } else if (THREE_PHASE == pileType) {// 三相数据
            List<String> dataTimeV = new ArrayList<String>();
            List<Double> dataV1 = new ArrayList<Double>();
            List<Double> dataV2 = new ArrayList<Double>();
            List<Double> dataV3 = new ArrayList<Double>();
            List<String> dataTimeA = new ArrayList<String>();
            List<Double> dataA1 = new ArrayList<Double>();
            List<Double> dataA2 = new ArrayList<Double>();
            List<Double> dataA3 = new ArrayList<Double>();
            for (DcsHisyc yc : list) {
                if (OUT_V1 == yc.getDatatype()) {// 输出电压
                    dataV1.add(yc.getValue());
                } else if (OUT_V2 == yc.getDatatype()) {
                    dataV2.add(yc.getValue());
                } else if (OUT_V3 == yc.getDatatype()) {
                    dataV3.add(yc.getValue());
                } else if (OUT_A1 == yc.getDatatype()) {
                    dataA1.add(yc.getValue());
                } else if (OUT_A2 == yc.getDatatype()) {
                    dataA2.add(yc.getValue());
                } else if (OUT_A3 == yc.getDatatype()) {
                    dataA3.add(yc.getValue());
                }
            }
            if (dataV1.size() > 0) {
                for (DcsHisyc yc : list) {
                    if (OUT_V1 == yc.getDatatype()) {
                        dataTimeV.add(DateUtil.DateToLongStr(yc.getDatatime()));
                    }
                }
            } else if (dataV2.size() > 0) {
                for (DcsHisyc yc : list) {
                    if (OUT_V2 == yc.getDatatype()) {
                        dataTimeV.add(DateUtil.DateToLongStr(yc.getDatatime()));
                    }
                }
            } else if (dataV3.size() > 0) {
                for (DcsHisyc yc : list) {
                    if (OUT_V3 == yc.getDatatype()) {
                        dataTimeV.add(DateUtil.DateToLongStr(yc.getDatatime()));
                    }
                }
            }
            if (dataA1.size() > 0) {
                for (DcsHisyc yc : list) {
                    if (OUT_A1 == yc.getDatatype()) {
                        dataTimeA.add(DateUtil.DateToLongStr(yc.getDatatime()));
                    }
                }
            } else if (dataA2.size() > 0) {
                for (DcsHisyc yc : list) {
                    if (OUT_A2 == yc.getDatatype()) {
                        dataTimeA.add(DateUtil.DateToLongStr(yc.getDatatime()));
                    }
                }
            } else if (dataA3.size() > 0) {
                for (DcsHisyc yc : list) {
                    if (OUT_A3 == yc.getDatatype()) {
                        dataTimeA.add(DateUtil.DateToLongStr(yc.getDatatime()));
                    }
                }
            }
            map.put("dataTimeV", dataTimeV);
            map.put("dataTimeA", dataTimeA);
            map.put("dataV1", dataV1);
            map.put("dataV2", dataV2);
            map.put("dataV3", dataV3);
            map.put("dataA1", dataA1);
            map.put("dataA2", dataA2);
            map.put("dataA3", dataA3);
        }
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

    public String getStationName() {
        return stationName;
    }

    public int getIdleCount() {
        return idleCount;
    }

    public int getUnLineCount() {
        return unLineCount;
    }

    public int getFaultCount() {
        return faultCount;
    }

    public int getBusyCount() {
        return busyCount;
    }

    public int getPileCount() {
        return pileCount;
    }

    public int getFastCount() {
        return fastCount;
    }

    public int getSlowCount() {
        return slowCount;
    }

    public int getSelectStationId() {
        return selectStationId;
    }

    public int getChargingCount() {
        return chargingCount;
    }
}
