package com.holley.charging.user.action;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.common.util.CacheChargeHolder.PileRunStatusEnum;
import com.holley.charging.model.bus.BusChargeRule;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.def.ChargeRuleModel;
import com.holley.charging.model.def.DcsAlarmEventsModel;
import com.holley.charging.model.def.PileRunStatusModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingPileExample;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.model.pob.PobChargingTempPile;
import com.holley.charging.model.pob.PobChargingTempPileExample;
import com.holley.charging.model.pob.PobChargingTempStation;
import com.holley.charging.model.pob.PobChargingTempStationExample;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.RequestTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysDefArea;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.util.CacheSysHolder;

/**
 * 个人用户设备
 * 
 * @author sc
 */
public class UserDeviceAction extends BaseAction {

    private final static Logger logger               = Logger.getLogger(UserDeviceAction.class);
    private Page                page;
    private DeviceService       deviceService;
    private Map<String, Object> result;
    private static String       addNewStationPile    = "addNewStationPile";                     // 添加新充电桩操作
    private static String       addValidStationPile  = "addValidStationPile";                   // 在已审核充电点添加桩操作
    private static String       editNewStationPile   = "editNewStationPile";                    // 修改未审核充电桩操作
    private static String       editValidStationPile = "editValidStationPile";                  // 修改已审核充电桩操作
    private static String       editNewStation       = "editNewStation";                        // 修改未审核新充电点操作
    private static String       editValidStation     = "editValidStation";                      // 修改已审核充电点操作
    private static String       initStationParam     = "initStationParam";                      // 初始化充电点参数
    private static String       initPileParam        = "initPileParam";                         // 初始化充电桩参数
    private static String       detailStation        = "detailStation";
    private static String       detailPile           = "detailPile";

    /**
     * 添加充电点设备
     * 
     * @return
     */
    public String addUserStation() {
        WebUser webUser = this.getWebuser();
        // 运营商信息
        // BusBussinessInfo busBussinessInfo = accountService.selectBusBussinessInfoByPrimaryKey(webUser.getInfoId());
        // initParam(initStationParam);
        // 省级
        List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
        // this.getRequest().setAttribute("busBussinessInfo", busBussinessInfo);
        this.getRequest().setAttribute("provinceList", provinceList);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
    }

    /**
     * 我的设备
     * 
     * @return
     */
    public String initUserDevice() {
        int gobackStationId = getParamInt("gobackStationId");
        getRequest().setAttribute("gobackStationId", gobackStationId);
        // 运营商信息
        initParam(initStationParam);
        // 省级
        List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
        this.getRequest().setAttribute("provinceList", provinceList);
        return SUCCESS;
    }

    /**
     * 我的待审设备
     * 
     * @return
     */
    public String initUserUnDevice() {
        // int gobackStationId = getParamInt("gobackStationId");
        // getRequest().setAttribute("gobackStationId", gobackStationId);
        // // 运营商信息
        // initParam(initStationParam);
        // // 省级
        // List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
        // this.getRequest().setAttribute("provinceList", provinceList);
        return SUCCESS;
    }

    /**
     * 查询已审核充电点设备
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String userValidStation() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        int isExport = this.getParamInt("isExport");// 是否导出excel
        String searchName = this.getParameter("searchName");
        param.put("busMec", webUser.getInfoId());
        if (!StringUtil.isEmpty(searchName)) {
            param.put("searchStationName", searchName);
        }
        param.put("busType", webUser.getUsertype().getValue());
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<PobChargingStation> list = deviceService.selectStationByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            // PobChargingStationExample stationEmp = new PobChargingStationExample();
            // PobChargingStationExample.Criteria stationCr = stationEmp.createCriteria();
            // stationCr.andBusMecEqualTo(webUser.getInfoId());
            // List<PobChargingStation> exportList = deviceService.selectStationByExample(stationEmp);
            List<PobChargingStation> exportList = deviceService.selectStationByPage(param);
            String[] headsName = { "充电点名称", "地址", "开放日", "开放时间", "快充桩数量", "慢充桩数量", "评分", "经度", "纬度", "省份", "市区", "停车场类型", "联系电话" };
            String[] properiesName = { "stationName", "address", "openDayDesc", "openTimeDesc", "fastNum", "slowNum", "scoreDesc", "lng", "lat", "provinceDesc", "cityDesc",
                    "parkTypeDesc", "linkPhone" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, PobChargingStation.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 查询已审核充电桩设备
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String userValidPile() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        String searchName = this.getParameter("searchName");
        int stationId = this.getParamInt("stationId");
        int isExport = this.getParamInt("isExport");// 是否导出excel
        param.put("busType", webUser.getUsertype().getValue());// 查找企业桩
        if (stationId <= 0) {
            message = "请选择充电点！！";
            return SUCCESS;
        }
        param.put("stationId", stationId);
        param.put("busMec", webUser.getInfoId());
        if (!StringUtil.isEmpty(searchName)) {
            param.put("pileName", searchName);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<PobChargingPile> list = deviceService.selectPileByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            // List<PobChargingPile> exportList = deviceService.exportPile(param);
            List<PobChargingPile> exportList = deviceService.selectPileByPage(param);
            String[] headsName = { "桩名称", "桩编号", "桩状态", "充电类型", "充电方式", "通讯协议", "通讯地址", "支持预约", "支付方式", "充电模式" };
            String[] properiesName = { "pileName", "pileCode", "statusDesc", "pileTypeDesc", "chaWayDesc", "comTypeDesc", "comAddr", "isAppDesc", "payWayDesc", "chaModelDesc", };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, PobChargingPile.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 添加桩设备
     * 
     * @return
     */
    public String addUserNewPile() {
        WebUser webUser = this.getWebuser();
        int newStationId = this.getParamInt("newStationId");
        if (newStationId > 0) {
            PobChargingTempStationExample emp = new PobChargingTempStationExample();
            PobChargingTempStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(newStationId);
            cr.andBusMecEqualTo(webUser.getInfoId());
            cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
            PobChargingTempStation pobChargingTempStation = null;
            List<PobChargingTempStation> pobChargingStationList = deviceService.selectPobChargingTempStationByExample(emp);
            if (pobChargingStationList != null && pobChargingStationList.size() > 0) {
                pobChargingTempStation = pobChargingStationList.get(0);
            }
            if (pobChargingTempStation != null) {
                this.initParam(initPileParam);
                List<BusChargeRule> chargeRuleList = CacheChargeHolder.getChargeRuleList();
                this.getRequest().setAttribute("chargeRuleList", chargeRuleList);
                this.getRequest().setAttribute("chargeRuleListJson", JsonUtil.list2json(chargeRuleList));
                this.getRequest().setAttribute("stationId", newStationId);
                this.getRequest().setAttribute("actionType", addNewStationPile);
                Calendar a = Calendar.getInstance();
                a.set(Calendar.HOUR, 0);
                a.set(Calendar.MINUTE, 0);
                a.set(Calendar.SECOND, 0);
                a.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy/MM/dd");
                this.getRequest().setAttribute("currentTime", formater.format(a.getTime()));
                return SUCCESS;
            } else {
                return MEMBER;
            }

        }
        return MEMBER;
    }

    /**
     * 添加桩设备
     * 
     * @return
     */
    public String addUserValidPile() {
        int validStationId = this.getParamInt("validStationId");
        int isStation = this.getParamInt("isStation");
        WebUser webUser = this.getWebuser();
        if (validStationId > 0) {
            PobChargingStationExample emp = new PobChargingStationExample();
            PobChargingStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(validStationId);
            cr.andBusMecEqualTo(webUser.getInfoId());
            cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
            PobChargingStation pobChargingStation = this.deviceService.selectPobChargingStationByExample(emp);
            if (pobChargingStation != null) {
                this.initParam(initPileParam);
                List<BusChargeRule> chargeRuleList = CacheChargeHolder.getChargeRuleList();
                this.getRequest().setAttribute("chargeRuleListJson", JsonUtil.list2json(chargeRuleList));
                this.getRequest().setAttribute("chargeRuleList", chargeRuleList);
                if (isStation != 1) {
                    this.getRequest().setAttribute("backStationId", validStationId);
                }
                this.getRequest().setAttribute("stationId", validStationId);
                this.getRequest().setAttribute("actionType", addValidStationPile);
                Calendar a = Calendar.getInstance();
                a.set(Calendar.HOUR, 0);
                a.set(Calendar.MINUTE, 0);
                a.set(Calendar.SECOND, 0);
                a.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
                this.getRequest().setAttribute("currentTime", formater.format(a.getTime()));
                return SUCCESS;
            } else {
                return MEMBER;
            }
        } else {
            return MEMBER;
        }
    }

    /**
     * 在未审核通过充电点上添加桩页
     * 
     * @return
     */
    public String addNewStationPile() {
        WebUser webUser = this.getWebuser();
        int newStationId = this.getParamInt("newStationId");
        if (newStationId > 0) {
            PobChargingTempStationExample emp = new PobChargingTempStationExample();
            PobChargingTempStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(newStationId);
            cr.andBusMecEqualTo(webUser.getInfoId());
            cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
            PobChargingTempStation pobChargingTempStation = null;
            List<PobChargingTempStation> pobChargingStationList = deviceService.selectPobChargingTempStationByExample(emp);
            if (pobChargingStationList != null && pobChargingStationList.size() > 0) {
                pobChargingTempStation = pobChargingStationList.get(0);
            }
            if (pobChargingTempStation != null) {
                this.initParam(initPileParam);
                List<BusChargeRule> chargeRuleList = CacheChargeHolder.getChargeRuleList();
                this.getRequest().setAttribute("chargeRuleList", chargeRuleList);
                this.getRequest().setAttribute("chargeRuleListJson", JsonUtil.list2json(chargeRuleList));
                this.getRequest().setAttribute("stationId", newStationId);
                this.getRequest().setAttribute("actionType", addNewStationPile);
                Calendar a = Calendar.getInstance();
                a.set(Calendar.HOUR, 0);
                a.set(Calendar.MINUTE, 0);
                a.set(Calendar.SECOND, 0);
                a.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy/MM/dd");
                this.getRequest().setAttribute("currentTime", formater.format(a.getTime()));
                return SUCCESS;
            } else {
                return MEMBER;
            }

        }
        return MEMBER;
    }

    /**
     * 显示未审核设备详情
     * 
     * @return
     */
    public String showUserUnVlaidDeviceDetail() {
        WebUser webUser = this.getWebuser();
        int stationId = this.getParamInt("stationId");
        int pileId = this.getParamInt("pileId");
        String detailType = this.getParameter("detailType");
        String result = "";
        if (detailStation.equals(detailType)) {
            result = "station";
            if (stationId > 0) {
                PobChargingTempStationExample emp = new PobChargingTempStationExample();
                PobChargingTempStationExample.Criteria cr = emp.createCriteria();
                cr.andIdEqualTo(stationId);
                cr.andBusMecEqualTo(webUser.getInfoId());
                cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
                PobChargingTempStation pobChargingStation = null;
                List<PobChargingTempStation> pobChargingStationList = deviceService.selectPobChargingTempStationByExample(emp);
                if (pobChargingStationList != null && pobChargingStationList.size() > 0) {
                    pobChargingStation = pobChargingStationList.get(0);
                }
                if (pobChargingStation != null) {
                    this.initParam(initStationParam);
                    List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
                    List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(pobChargingStation.getProvince());
                    this.getRequest().setAttribute("provinceList", provinceList);
                    this.getRequest().setAttribute("cityList", cityList);
                    this.getRequest().setAttribute("pobChargingStation", pobChargingStation);
                    this.getRequest().setAttribute("validOrUnValid", "unValid");
                } else {
                    this.getRequest().setAttribute("msg", "请选择充电点！！");
                    result = "userMsg";
                }
            } else {
                this.getRequest().setAttribute("msg", "请选择充电点！！");
                result = "userMsg";
            }
        } else if (detailPile.equals(detailType)) {
            result = "pile";
            if (pileId > 0) {
                PobChargingTempPileExample emp = new PobChargingTempPileExample();
                PobChargingTempPileExample.Criteria cr = emp.createCriteria();
                cr.andIdEqualTo(pileId);
                cr.andBusMecEqualTo(webUser.getInfoId());
                cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
                List<PobChargingTempPile> pobChargingPileList = deviceService.selectPobChargingTempPileByExample(emp);
                PobChargingTempPile pobChargingPile = null;
                if (pobChargingPileList != null && pobChargingPileList.size() > 0) {
                    pobChargingPile = pobChargingPileList.get(0);
                }
                if (pobChargingPile != null) {
                    this.initParam(initPileParam);
                    List<BusChargeRule> chargeRuleList = CacheChargeHolder.getChargeRuleList();
                    this.getRequest().setAttribute("chargeRuleList", chargeRuleList);
                    this.getRequest().setAttribute("chargeRuleListJson", JsonUtil.list2json(chargeRuleList));
                    this.getRequest().setAttribute("pobChargingPile", pobChargingPile);
                    this.getRequest().setAttribute("showPileType", "unValidPile");
                    this.getRequest().setAttribute("validOrUnValid", "unValid");
                } else {
                    this.getRequest().setAttribute("msg", "请选择充电桩！！");
                    result = "userMsg";
                }

            } else {
                this.getRequest().setAttribute("msg", "请选择充电桩！！");
                result = "userMsg";
            }
        } else {
            this.getRequest().setAttribute("msg", "非法操作！！");
            result = "userMsg";
        }
        return result;
    }

    /**
     * 显示已审核设备详情
     * 
     * @return
     */
    public String showUserVlaidDeviceDetail() {
        WebUser webUser = this.getWebuser();
        int stationId = this.getParamInt("stationId");
        int backStationId = this.getParamInt("backStationId");
        int pileId = this.getParamInt("pileId");
        String detailType = this.getParameter("detailType");
        String result = "";
        if (detailStation.equals(detailType)) {
            result = "station";
            if (stationId > 0) {
                PobChargingStationExample emp = new PobChargingStationExample();
                PobChargingStationExample.Criteria cr = emp.createCriteria();
                cr.andIdEqualTo(stationId);
                cr.andBusMecEqualTo(webUser.getInfoId());
                cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
                PobChargingStation pobChargingStation = deviceService.selectPobChargingStationByExample(emp);
                if (pobChargingStation != null) {
                    this.initParam(initStationParam);
                    List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
                    List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(pobChargingStation.getProvince());
                    this.getRequest().setAttribute("provinceList", provinceList);
                    this.getRequest().setAttribute("cityList", cityList);
                    this.getRequest().setAttribute("pobChargingStation", pobChargingStation);
                    this.getRequest().setAttribute("validOrUnValid", "valid");
                } else {
                    this.getRequest().setAttribute("msg", "请选择充电点！！");
                    result = "userMsg";
                }
            } else {
                this.getRequest().setAttribute("msg", "请选择充电点！！");
                result = "userMsg";
            }
        } else if (detailPile.equals(detailType)) {
            result = "pile";
            if (pileId > 0) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("busMec", webUser.getInfoId());
                param.put("busType", webUser.getUsertype().getShortValue());
                param.put("pileId", pileId);
                PobChargingPile pobChargingPile = deviceService.selectPileByMap(param);
                if (pobChargingPile != null) {
                    param.put("pileId", pileId);
                    param.put("active", WhetherEnum.YES.getShortValue());// 时间已激活
                    param.put("status", WhetherEnum.YES.getShortValue());// 已生效
                    ChargeRuleModel activeRule = deviceService.selectChargeRuleModelByMap(param);
                    param.put("active", WhetherEnum.NO.getShortValue());// 时间未激活
                    ChargeRuleModel unActiveRule = deviceService.selectChargeRuleModelByMap(param);
                    this.initParam(initPileParam);
                    List<BusChargeRule> chargeRuleList = CacheChargeHolder.getChargeRuleList();
                    this.getRequest().setAttribute("chargeRuleList", chargeRuleList);
                    this.getRequest().setAttribute("chargeRuleListJson", JsonUtil.list2json(chargeRuleList));
                    this.getRequest().setAttribute("activeRule", activeRule);
                    this.getRequest().setAttribute("unActiveRule", unActiveRule);
                    this.getRequest().setAttribute("pobChargingPile", pobChargingPile);
                    this.getRequest().setAttribute("showPileType", "validPile");
                    this.getRequest().setAttribute("validOrUnValid", "valid");
                    this.getRequest().setAttribute("backStationId", backStationId);
                } else {
                    this.getRequest().setAttribute("msg", "请选择充电桩！！");
                    result = "userMsg";
                }

            } else {
                this.getRequest().setAttribute("msg", "请选择充电桩！！");
                result = "userMsg";
            }
        } else {
            this.getRequest().setAttribute("msg", "非法操作！！");
            result = "userMsg";
        }
        return result;
    }

    /**
     * 修改已审核通过的点信息
     * 
     * @return
     */
    public String editValidStation() {
        WebUser webUser = this.getWebuser();
        int validStationId = this.getParamInt("validStationId");// 已经审核通过的点ID
        if (validStationId > 0) {
            PobChargingStationExample emp = new PobChargingStationExample();
            PobChargingStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(validStationId);
            cr.andBusMecEqualTo(webUser.getInfoId());
            cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
            PobChargingStation pobChargingStation = deviceService.selectPobChargingStationByExample(emp);
            if (pobChargingStation != null) {
                // 运营商信息
                this.initParam(initStationParam);
                // 是否类型
                List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
                List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(pobChargingStation.getProvince());
                this.getRequest().setAttribute("provinceList", provinceList);
                this.getRequest().setAttribute("cityList", cityList);
                this.getRequest().setAttribute("stationId", validStationId);
                this.getRequest().setAttribute("actionType", editValidStation);
                this.getRequest().setAttribute("pobChargingStation", pobChargingStation);
                return SUCCESS;
            } else {
                return MEMBER;
            }
        }
        return MEMBER;

    }

    /**
     * 修改未审核通过的点信息
     * 
     * @return
     */
    public String editNewStation() {
        WebUser webUser = this.getWebuser();
        int newStationId = this.getParamInt("newStationId");
        if (newStationId > 0) {
            PobChargingTempStationExample emp = new PobChargingTempStationExample();
            PobChargingTempStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(newStationId);
            cr.andBusMecEqualTo(webUser.getInfoId());
            cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
            cr.andRequestTypeEqualTo(RequestTypeEnum.ADDSTATIONPILE.getShortValue());
            PobChargingTempStation pobChargingTempStation = null;
            List<PobChargingTempStation> pobChargingStationList = deviceService.selectPobChargingTempStationByExample(emp);
            if (pobChargingStationList != null && pobChargingStationList.size() > 0) {
                pobChargingTempStation = pobChargingStationList.get(0);
            }
            if (pobChargingTempStation != null) {
                // 运营商信息
                this.initParam(initStationParam);
                List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
                List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(pobChargingTempStation.getProvince());
                this.getRequest().setAttribute("provinceList", provinceList);
                this.getRequest().setAttribute("cityList", cityList);
                this.getRequest().setAttribute("stationId", newStationId);
                this.getRequest().setAttribute("actionType", editNewStation);
                this.getRequest().setAttribute("pobChargingStation", pobChargingTempStation);
                return SUCCESS;
            } else {
                return MEMBER;
            }

        }
        return MEMBER;
    }

    /**
     * 修改已审核桩信息
     * 
     * @return
     */
    public String editValidPile() {
        WebUser webUser = this.getWebuser();
        int validPileId = this.getParamInt("validPileId");
        int backStationId = this.getParamInt("backStationId");
        Map<String, Object> param;
        if (validPileId > 0) {
            /*
             * param = new HashMap<String, Object>(); param.put("pileId", validPileId); param.put("busType",
             * webUser.getUsertype().getValue()); param.put("busMec", webUser.getInfoId()); PobChargingPile
             * pobChargingPile = deviceService.selectPileByMap(param);
             */
            param = new HashMap<String, Object>();
            param.put("pileId", validPileId);
            param.put("active", WhetherEnum.YES.getShortValue());// 时间已激活
            param.put("status", WhetherEnum.YES.getShortValue());// 已生效
            ChargeRuleModel activeRule = deviceService.selectChargeRuleModelByMap(param);
            param.put("active", WhetherEnum.NO.getShortValue());// 时间未激活
            ChargeRuleModel unActiveRule = deviceService.selectChargeRuleModelByMap(param);
            PobChargingPileExample emp = new PobChargingPileExample();
            PobChargingPileExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(validPileId);
            List<PobChargingPile> pobChargingPileList = deviceService.selectPobChargingPileByExample(emp);
            PobChargingPile pobChargingPile = pobChargingPileList.get(0);
            if (pobChargingPile != null) {
                this.initParam(initPileParam);
                List<BusChargeRule> chargeRuleList = CacheChargeHolder.getChargeRuleList();
                this.getRequest().setAttribute("chargeRuleList", chargeRuleList);
                this.getRequest().setAttribute("chargeRuleListJson", JsonUtil.list2json(chargeRuleList));
                this.getRequest().setAttribute("actionType", editValidStationPile);
                this.getRequest().setAttribute("pobChargingPile", pobChargingPile);
                this.getRequest().setAttribute("pileId", validPileId);
                this.getRequest().setAttribute("activeRule", activeRule);
                this.getRequest().setAttribute("unActiveRule", unActiveRule);
                this.getRequest().setAttribute("backStationId", backStationId);
                Calendar a = Calendar.getInstance();
                a.set(Calendar.HOUR, 0);
                a.set(Calendar.MINUTE, 0);
                a.set(Calendar.SECOND, 0);
                a.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy/MM/dd");
                this.getRequest().setAttribute("currentTime", formater.format(a.getTime()));
                return SUCCESS;
            }
        }
        return MEMBER;
    }

    /**
     * 修改未审核桩信息
     * 
     * @return
     */
    public String editNewPile() {
        WebUser webUser = this.getWebuser();
        int newPileId = this.getParamInt("newPileId");
        int type = this.getParamInt("type");
        if (newPileId > 0 && (type == RequestTypeEnum.ADDSTATIONPILE.getShortValue() || type == RequestTypeEnum.ADDPILE.getShortValue())) {
            PobChargingTempPileExample emp = new PobChargingTempPileExample();
            PobChargingTempPileExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(newPileId);
            cr.andBusMecEqualTo(webUser.getInfoId());
            cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
            if (type == 1) {
                cr.andRequestTypeEqualTo(RequestTypeEnum.ADDSTATIONPILE.getShortValue());
            } else if (type == 2) {
                cr.andRequestTypeEqualTo(RequestTypeEnum.ADDPILE.getShortValue());
            }
            List<PobChargingTempPile> pobChargingPileList = deviceService.selectPobChargingTempPileByExample(emp);
            PobChargingTempPile pobChargingTempPile = null;
            if (pobChargingPileList != null && pobChargingPileList.size() > 0) {
                pobChargingTempPile = pobChargingPileList.get(0);
            }
            if (pobChargingTempPile != null) {
                this.initParam(initPileParam);
                List<BusChargeRule> chargeRuleList = CacheChargeHolder.getChargeRuleList();
                this.getRequest().setAttribute("chargeRuleList", chargeRuleList);
                this.getRequest().setAttribute("chargeRuleListJson", JsonUtil.list2json(chargeRuleList));
                this.getRequest().setAttribute("actionType", editNewStationPile);
                this.getRequest().setAttribute("pobChargingPile", pobChargingTempPile);
                this.getRequest().setAttribute("pileId", newPileId);
                Calendar a = Calendar.getInstance();
                a.set(Calendar.HOUR, 0);
                a.set(Calendar.MINUTE, 0);
                a.set(Calendar.SECOND, 0);
                a.set(Calendar.MILLISECOND, 0);
                SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy/MM/dd");
                this.getRequest().setAttribute("currentTime", formater.format(a.getTime()));
                return SUCCESS;
            }
        }
        return MEMBER;
    }

    /**
     * 查询未审核充电点设备
     * 
     * @return
     */
    public String userUnValidStation() {
        return SUCCESS;
    }

    /**
     * 查询已审核充电桩设备
     * 
     * @return
     */
    public String userUnValidPile() {
        return SUCCESS;
    }

    /**
     * 设备监控地图
     * 
     * @return
     */
    public String initUserDeviceMap() {
        WebUser webUser = this.getWebuser();
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
        List<PobChargingStation> list = CacheChargeHolder.getChargeStationListByMecAndType(webUser.getInfoId(), webUser.getUsertype().getShortValue());
        List<PileRunStatusModel> pileList = CacheChargeHolder.getPileStatusListByInfoId(webUser.getInfoId(), webUser.getUsertype().getShortValue());
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

        this.getRequest().setAttribute("pileList", pileList);
        this.getRequest().setAttribute("freePile", freePile);
        this.getRequest().setAttribute("chaPile", chaPile);
        this.getRequest().setAttribute("appPile", appPile);
        this.getRequest().setAttribute("offPile", offPile);
        this.getRequest().setAttribute("stationList", list);
        String jsonList = JsonUtil.list2json(list);
        this.getRequest().setAttribute("jsonList", jsonList);
        return SUCCESS;

    }

    /**
     * 设备告警
     * 
     * @return
     */
    public String initUserDeviceAlarm() {
        return SUCCESS;

    }

    public String userDeviceAlarm() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 每页显示条数
        int isExport = this.getParamInt("isExport");// 是否导出excel
        String searchDate = getParameter("searchDate");
        param.put("busMec", webUser.getInfoId());
        param.put("busType", webUser.getUsertype().getValue());
        if (!StringUtil.isEmpty(searchDate)) {
            Date startTime = DateUtil.getFirstDayOfMonth(DateUtil.StrToDate(searchDate, "yyyy/MM"));
            Date endTime = DateUtil.getLastDayOfMonth(DateUtil.StrToDate(searchDate, "yyyy/MM"));
            param.put("startTime", startTime);
            param.put("endTime", endTime);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
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
     * 实时监控列表页
     * 
     * @return
     */
    public String searchAllOnlinePileList() {
        WebUser webUser = getWebuser();
        List<PileRunStatusModel> allOnlinePileList = CacheChargeHolder.getPileStatusListByInfoId(webUser.getInfoId(), webUser.getUsertype().getShortValue());
        // 充电桩类型：交流，直流
        List<SysLink> chaWayList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.CHA_WAY.getValue());
        // 通讯协议：51充电
        List<SysLink> comTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_TYPE.getValue());
        // 充电功率类型：快，慢，超速
        List<SysLink> pileTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.PILE_TYPE.getValue());
        this.getRequest().setAttribute("allOnlinePileList", allOnlinePileList);
        this.getRequest().setAttribute("chaWayList", chaWayList);
        this.getRequest().setAttribute("comTypeList", comTypeList);
        this.getRequest().setAttribute("pileTypeList", pileTypeList);
        return SUCCESS;
    }

    private void initParam(String initParamType) {
        // station
        if (initStationParam.equals(initParamType)) {
            // 运营类型：企业，个人
            List<SysLink> busTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.BUS_TYPE.getValue());
            // 停车场类型：露天，室内
            List<SysLink> parkTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.PARK_TYPE.getValue());
            List<SysLink> openDayList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.OPEN_DAY.getValue());
            List<SysLink> openTimeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.OPEN_TIME.getValue());
            getRequest().setAttribute("busTypeList", busTypeList);
            getRequest().setAttribute("parkTypeList", parkTypeList);
            getRequest().setAttribute("openDayList", openDayList);
            getRequest().setAttribute("openTimeList", openTimeList);
        }
        // pile
        else if (initPileParam.equals(initParamType)) {
            // 充电桩类型：交流，直流
            List<SysLink> chaWayList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.CHA_WAY.getValue());
            // 是否类型：是，否
            List<SysLink> isOrNoList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.IS_NO.getValue());
            // 通讯协议：51充电
            List<SysLink> comTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_TYPE.getValue());
            // 充电功率类型：快，慢，超速
            List<SysLink> pileTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.PILE_TYPE.getValue());
            // 支付方式：APP，充电卡，人工
            List<SysLink> payWayList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.PAY_WAY.getValue());
            // 电桩型号
            List<BusPileModel> busPileModelList = CacheChargeHolder.getPileModelList();
            // 子通讯地址
            List<SysLink> comSubAddrList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_SUB_ADDR.getValue());
            getRequest().setAttribute("chaWayList", chaWayList);
            getRequest().setAttribute("isOrNoList", isOrNoList);
            getRequest().setAttribute("comTypeList", comTypeList);
            getRequest().setAttribute("pileTypeList", pileTypeList);
            getRequest().setAttribute("payWayList", payWayList);
            getRequest().setAttribute("busPileModelList", busPileModelList);
            getRequest().setAttribute("comSubAddrList", comSubAddrList);
        }

    }

    // get
    public String getMessage() {
        return this.message;
    }

    public Map<String, Object> getResult() {
        return result;
    }

    public Page getPage() {
        return page;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }
}
