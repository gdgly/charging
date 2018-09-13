package com.holley.charging.bussiness.action;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusChargeRule;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.def.ChargeRuleModel;
import com.holley.charging.model.def.StationAppointmentModel;
import com.holley.charging.model.def.StationChargeModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingPileExample;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.model.pob.PobChargingTempPile;
import com.holley.charging.model.pob.PobChargingTempPileExample;
import com.holley.charging.model.pob.PobChargingTempStation;
import com.holley.charging.model.pob.PobChargingTempStationExample;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.DeviceVerifyStatusEnum;
import com.holley.common.constants.charge.FeeEnum;
import com.holley.common.constants.charge.ImgTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.RequestTypeEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysDefArea;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.util.CacheSysHolder;
import com.holley.web.common.util.ImageUtil;

/**
 * 设备相关ACTION
 * 
 * @author shencheng
 */
public class DeviceAction extends BaseAction {

    private static final long   serialVersionUID     = 1L;
    private final static Logger logger               = Logger.getLogger(DeviceAction.class);
    private DeviceService       deviceService;
    private Page                page;
    private AccountService      accountService;
    private File                img;
    private File                doc;
    private Map<String, Object> map;
    private static String       addNewStationPile    = "addNewStationPile";                 // 添加新充电桩操作
    private static String       addValidStationPile  = "addValidStationPile";               // 在已审核充电点添加桩操作
    private static String       editNewStationPile   = "editNewStationPile";                // 修改未审核充电桩操作
    private static String       editValidStationPile = "editValidStationPile";              // 修改已审核充电桩操作
    private static String       editNewStation       = "editNewStation";                    // 修改未审核新充电点操作
    private static String       editValidStation     = "editValidStation";                  // 修改已审核充电点操作
    private static String       initStationParam     = "initStationParam";                  // 初始化充电点参数
    private static String       initPileParam        = "initPileParam";                     // 初始化充电桩参数
    private static String       detailStation        = "detailStation";
    private static String       detailPile           = "detailPile";

    /**
     * 显示已经审核设备详情
     * 
     * @return
     */
    public String showDetailPileModel() {
        int pileModelId = this.getParamInt("pileModelId");
        Map<String, Object> result = new HashMap<String, Object>();
        result.put("msg", "success");
        if (pileModelId > 0) {
            BusPileModel busPileModel = CacheChargeHolder.selectPileModelById(pileModelId);
            if (busPileModel != null) {
                result.put("busPileModel", busPileModel);
            } else {
                result.put("msg", "选择的充电桩型号不存在！！");
            }
        } else {
            result.put("msg", "请选择充电桩型号！！");
        }
        this.map = result;
        return SUCCESS;
    }

    /**
     * 显示已经审核设备详情
     * 
     * @return
     */
    public String showValidDetail() {
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
                    BusBussinessInfo busBussinessInfo = accountService.selectBusBussinessInfoByPrimaryKey(webUser.getInfoId());
                    this.initParam(initStationParam);
                    List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
                    List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(pobChargingStation.getProvince());
                    this.getRequest().setAttribute("provinceList", provinceList);
                    this.getRequest().setAttribute("cityList", cityList);
                    this.getRequest().setAttribute("busBussinessInfo", busBussinessInfo);
                    this.getRequest().setAttribute("pobChargingStation", pobChargingStation);
                    this.getRequest().setAttribute("validOrUnValid", "valid");
                } else {
                    this.getRequest().setAttribute("msg", "请选择充电点！！");
                    result = MSG;
                }
            } else {
                this.getRequest().setAttribute("msg", "请选择充电点！！");
                result = MSG;
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
                    result = MSG;
                }

            } else {
                this.getRequest().setAttribute("msg", "请选择充电桩！！");
                result = MSG;
            }
        } else {
            this.getRequest().setAttribute("msg", "非法操作！！");
            result = MSG;
        }
        return result;
    }

    /**
     * 显示未审核设备详情
     * 
     * @return
     */
    public String showUnValidDetail() {
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
                    BusBussinessInfo busBussinessInfo = accountService.selectBusBussinessInfoByPrimaryKey(webUser.getInfoId());
                    this.initParam(initStationParam);
                    List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
                    List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(pobChargingStation.getProvince());
                    this.getRequest().setAttribute("provinceList", provinceList);
                    this.getRequest().setAttribute("cityList", cityList);
                    this.getRequest().setAttribute("busBussinessInfo", busBussinessInfo);
                    this.getRequest().setAttribute("pobChargingStation", pobChargingStation);
                    this.getRequest().setAttribute("validOrUnValid", "unValid");
                } else {
                    this.getRequest().setAttribute("msg", "请选择充电点！！");
                    result = MSG;
                }
            } else {
                this.getRequest().setAttribute("msg", "请选择充电点！！");
                result = MSG;
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
                    result = MSG;
                }

            } else {
                this.getRequest().setAttribute("msg", "请选择充电桩！！");
                result = MSG;
            }
        } else {
            this.getRequest().setAttribute("msg", "非法操作！！");
            result = MSG;
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
                BusBussinessInfo busBussinessInfo = accountService.selectBusBussinessInfoByPrimaryKey(webUser.getInfoId());
                this.initParam(initStationParam);
                // 是否类型
                List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
                List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(pobChargingStation.getProvince());
                this.getRequest().setAttribute("provinceList", provinceList);
                this.getRequest().setAttribute("cityList", cityList);
                this.getRequest().setAttribute("busBussinessInfo", busBussinessInfo);
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
                BusBussinessInfo busBussinessInfo = accountService.selectBusBussinessInfoByPrimaryKey(pobChargingTempStation.getBusMec());
                this.initParam(initStationParam);
                List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
                List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(pobChargingTempStation.getProvince());
                this.getRequest().setAttribute("provinceList", provinceList);
                this.getRequest().setAttribute("cityList", cityList);
                this.getRequest().setAttribute("busBussinessInfo", busBussinessInfo);
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
     * 添加充电点设备页
     * 
     * @return
     */
    public String addStation() {
        WebUser webUser = this.getWebuser();
        // 运营商信息
        BusBussinessInfo busBussinessInfo = accountService.selectBusBussinessInfoByPrimaryKey(webUser.getInfoId());
        initParam(initStationParam);
        // 省级
        List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
        this.getRequest().setAttribute("busBussinessInfo", busBussinessInfo);
        this.getRequest().setAttribute("provinceList", provinceList);
        this.getRequest().setAttribute(Globals.CURRENTUSER, webUser);
        return SUCCESS;
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
     * 异步修改充电点信息
     * 
     * @return
     */
    public String editStationByAjax() {
        WebUser webUser = this.getWebuser();
        int stationId = this.getParamInt("stationId");// 未审核通过的点ID
        String actionType = this.getParameter("actionType");
        String pobStation = this.getParameter("pobStation");
        Map<String, Object> mapp = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        Object url;
        int count;
        if (stationId > 0 && !StringUtil.isEmpty(actionType)) {
            logger.info("修改充电点信息START");
            mapp = checkEditStation(pobStation, img);
            Object msg = mapp.get("msg");
            Object pobChargingStationObj = mapp.get("pobChargingStationObj");

            // 修改未审核或审核不通过的充电点
            if (editNewStation.equals(actionType) && msg != null && "success".equals((String) msg) && pobChargingStationObj != null
                && pobChargingStationObj instanceof PobChargingTempStation) {
                PobChargingTempStation chargingStation = (PobChargingTempStation) pobChargingStationObj;
                // TODO 修改未审核通过点信息
                PobChargingTempStationExample emp = new PobChargingTempStationExample();
                PobChargingTempStationExample.Criteria cr = emp.createCriteria();
                cr.andIdEqualTo(stationId);
                cr.andBusMecEqualTo(webUser.getInfoId());
                cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
                cr.andRequestTypeEqualTo(RequestTypeEnum.ADDSTATIONPILE.getShortValue());
                PobChargingTempStation pobChargingTempStation = null;
                List<PobChargingTempStation> pobChargingStationList = deviceService.selectPobChargingTempStationByExample(emp);
                if (pobChargingStationList != null && pobChargingStationList.size() > 0) {
                    pobChargingTempStation = pobChargingStationList.get(0);
                }
                if (pobChargingTempStation != null) {
                    chargingStation.setId(pobChargingTempStation.getId());
                    // ////////////////
                    PobChargingStationExample pobStationEmp = new PobChargingStationExample();
                    PobChargingStationExample.Criteria pobStationCr = pobStationEmp.createCriteria();
                    pobStationCr.andStationNameEqualTo(chargingStation.getStationName());
                    int count1 = deviceService.countPobChargingStationByExample(pobStationEmp);// 对比正式充电点

                    List<Short> values = new ArrayList<Short>();
                    values.add(DeviceVerifyStatusEnum.UNVERIFIED.getShortValue());
                    values.add(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
                    PobChargingTempStationExample pobTempStationEmp = new PobChargingTempStationExample();
                    PobChargingTempStationExample.Criteria pobTempStationCr = pobTempStationEmp.createCriteria();
                    pobTempStationCr.andStationNameEqualTo(chargingStation.getStationName());
                    pobTempStationCr.andValidStatusIn(values);
                    pobTempStationCr.andIdNotEqualTo(chargingStation.getId());
                    int count2 = deviceService.countPobChargingTempStationByExample(pobTempStationEmp);// 对比正在审核中或待审的点
                    // //////////////////

                    if ((count1 + count2) > 0) {
                        mapp.put("msg", "充电点名称与其他充电点重复！！");
                        map = mapp;
                        return SUCCESS;
                    } else if (pobChargingTempStation.getValidStatus() == DeviceVerifyStatusEnum.UNVERIFIED.getShortValue()
                               || pobChargingTempStation.getValidStatus() == DeviceVerifyStatusEnum.FAILED.getShortValue()) {
                        chargingStation.setValidStatus(DeviceVerifyStatusEnum.UNVERIFIED.getShortValue());
                        chargingStation.setUpdateTime(new Date());
                        if (this.img != null) {
                            try {
                                param = ImageUtil.uploadImg(img, chargingStation.getId(), ImgTypeEnum.STATION_TEMP_IMG, getDataPath());
                            } catch (Exception e) {
                                param.put("msg", "必须上传图片类型文件！！");
                            }
                            if ("success".equals(param.get("msg"))) {
                                chargingStation.setImg(param.get("url").toString());
                            }
                        } else {
                            chargingStation.setImg(pobChargingTempStation.getImg());
                        }
                        deviceService.updatePobChargingTempStationByPrimaryKeySelective(chargingStation);
                    } else {
                        mapp.put("msg", "该点信息正在审核中或已审核通过！！");
                    }

                } else {
                    mapp.put("msg", ERROR);
                }
            }
            // 修改正式点信息
            else if (editValidStation.equals(actionType) && msg != null && "success".equals((String) msg) && pobChargingStationObj != null
                     && pobChargingStationObj instanceof PobChargingTempStation) {
                PobChargingStationExample emp = new PobChargingStationExample();
                PobChargingStationExample.Criteria cr = emp.createCriteria();
                cr.andIdEqualTo(stationId);
                cr.andBusMecEqualTo(webUser.getInfoId());
                cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
                PobChargingStation pobChargingStation = deviceService.selectPobChargingStationByExample(emp);
                if (pobChargingStation != null) {
                    PobChargingTempStation chargingStation = (PobChargingTempStation) pobChargingStationObj;
                    // ////////////////
                    PobChargingStationExample pobStationEmp = new PobChargingStationExample();
                    PobChargingStationExample.Criteria pobStationCr = pobStationEmp.createCriteria();
                    pobStationCr.andStationNameEqualTo(chargingStation.getStationName());
                    pobStationCr.andIdNotEqualTo(stationId);
                    int count1 = deviceService.countPobChargingStationByExample(pobStationEmp);// 对比正式充电点

                    List<Short> values = new ArrayList<Short>();
                    values.add(DeviceVerifyStatusEnum.UNVERIFIED.getShortValue());
                    values.add(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
                    PobChargingTempStationExample pobTempStationEmp = new PobChargingTempStationExample();
                    PobChargingTempStationExample.Criteria pobTempStationCr = pobTempStationEmp.createCriteria();
                    pobTempStationCr.andStationNameEqualTo(chargingStation.getStationName());
                    pobTempStationCr.andValidStatusIn(values);
                    pobTempStationCr.andRealStationIdNotEqualTo(stationId);
                    pobTempStationCr.andRequestTypeEqualTo(RequestTypeEnum.EDITSTATION.getShortValue());
                    int count2 = deviceService.countPobChargingTempStationByExample(pobTempStationEmp);// 对比正在审核中或待审的点
                    // //////////////////
                    if ((count1 + count2) > 0) {
                        mapp.put("msg", "充电点名称与其他充电点重复！！");
                        map = mapp;
                        return SUCCESS;
                    }
                    PobChargingTempStationExample temEmp = new PobChargingTempStationExample();
                    PobChargingTempStationExample.Criteria temCr1 = temEmp.createCriteria();
                    PobChargingTempStationExample.Criteria temCr2 = temEmp.createCriteria();
                    temCr1.andRealStationIdEqualTo(stationId);
                    temCr1.andRequestTypeEqualTo(RequestTypeEnum.EDITSTATION.getShortValue());
                    temCr1.andValidStatusEqualTo(DeviceVerifyStatusEnum.UNVERIFIED.getShortValue());

                    temCr2.andRealStationIdEqualTo(stationId);
                    temCr2.andRequestTypeEqualTo(RequestTypeEnum.EDITSTATION.getShortValue());
                    temCr2.andValidStatusEqualTo(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
                    temEmp.or(temCr2);
                    PobChargingTempStation pobChargeTempStation = null;
                    List<PobChargingTempStation> pobChargingStationList = deviceService.selectPobChargingTempStationByExample(temEmp);
                    if (pobChargingStationList != null && pobChargingStationList.size() > 0) {
                        pobChargeTempStation = pobChargingStationList.get(0);
                    }

                    // PobChargingTempStation pobChargeTempStation =
                    // deviceService.selectPobChargingTempStationByExample(temEmp);

                    chargingStation.setRealStationId(stationId);
                    chargingStation.setRequestType(RequestTypeEnum.EDITSTATION.getShortValue());
                    chargingStation.setUpdateTime(new Date());
                    chargingStation.setBusMec(webUser.getInfoId());
                    chargingStation.setBusType(webUser.getUsertype().getShortValue());
                    if (img == null) {
                        chargingStation.setImg(pobChargingStation.getImg());
                    }
                    if ("success".equals(mapp.get("msg"))) {
                        if (pobChargeTempStation != null && pobChargeTempStation.getValidStatus() == DeviceVerifyStatusEnum.VERIFYING.getShortValue()) {
                            mapp.put("msg", "该充电点有修改操作正在审核中，请稍后再试！！");
                        } else {
                            PobChargingTempStation newPobChargingTempStation = null;
                            if (pobChargeTempStation != null) {
                                newPobChargingTempStation = new PobChargingTempStation();
                                newPobChargingTempStation.setId(pobChargeTempStation.getId());
                                newPobChargingTempStation.setValidStatus(DeviceVerifyStatusEnum.CANCEL.getShortValue());
                                newPobChargingTempStation.setUpdateTime(new Date());
                            }
                            // update
                            // deviceService.updatePobChargingTempStationByPrimaryKeySelective(newPobChargingTempStation);
                            try {
                                mapp = deviceService.insertAndUpdateTempStationForEdit(newPobChargingTempStation, chargingStation, img, getDataPath());
                            } catch (Exception e1) {
                                mapp.put("msg", "修改失败！！");
                                e1.printStackTrace();
                            }

                        }

                    }

                    // if ("success".equals(mapp.get("msg"))) {

                    // insert
                    /*
                     * deviceService.insertPobChargingTempStationSelective(chargingStation); if (this.img != null &&
                     * chargingStation.getId() > 0) { try { param = ImageUtil.uploadImg(img, chargingStation.getId(),
                     * Globals.STATION_TEMP_IMG, getServletRealPath(Globals.IMG_DATA_FILE)); } catch (Exception e) {
                     * param.put("msg", "必须上传图片类型文件!"); } url = param.get("url"); PobChargingTempStation
                     * newPobChargingTempStation = new PobChargingTempStation();
                     * newPobChargingTempStation.setId(chargingStation.getId()); if (url != null) {
                     * newPobChargingTempStation.setImg(url.toString()); }
                     * deviceService.updatePobChargingTempStationByPrimaryKeySelective(newPobChargingTempStation); }
                     */
                    // }

                    // deviceService.updatePobChargingStationByPrimaryKeySelective(pobChargingStation);

                } else {
                    mapp.put("msg", ERROR);
                }
            }
        } else {
            mapp.put("msg", ERROR);
        }
        logger.info("修改充电点信息END");
        this.map = mapp;
        return SUCCESS;
    }

    private Map<String, Object> checkEditStation(String pobStation, File stationImg) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "success";
        if (stationImg != null) {
            try {
                ImageUtil.fileIsImage(stationImg);
            } catch (Exception e) {
                msg = "必须上传图片类型文件！！";
            }
        }
        if ("success".equals(msg)) {
            try {

                PobChargingTempStation pobChargingTempStation = this.JsonToBean(pobStation, PobChargingTempStation.class);
                if (StringUtil.isEmpty(pobChargingTempStation.getStationName())) {
                    msg = "请输入充电点名称！！";
                } else if (StringUtil.isEmpty(pobChargingTempStation.getLng()) || StringUtil.isEmpty(pobChargingTempStation.getLat())) {
                    msg = "请指定经纬度！！";
                } else if (StringUtil.isEmpty(pobChargingTempStation.getAddress())) {
                    msg = "请输入地址信息！！";
                } else if (StringUtil.isEmpty(pobChargingTempStation.getLinkPhone())) {
                    msg = "请输入联系电话！！";
                }
                map.put("pobChargingStationObj", pobChargingTempStation);

            } catch (Exception e) {
                msg = "修改充电点信息失败！！";
            }
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * 异步添加新充电点桩
     * 
     * @return
     */
    public String addStationPileByAjax() {
        WebUser webUser = this.getWebuser();
        Map<String, Object> mapp = new HashMap<String, Object>();
        int newStationId = this.getParamInt("newStationId");// 未审核通过的点ID
        String actionType = this.getParameter("actionType");
        int validStationId = this.getParamInt("validStationId");// 已经审核通过的点ID
        String pobChargingTempPile = this.getParameter("pobChargingTempPile");
        String docType = ".doc";
        List<SysLink> comSubAddrList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_SUB_ADDR.getValue());
        List<Short> values = new ArrayList<Short>();
        int count;
        if (!StringUtil.isEmpty(actionType)) {
            mapp = checkPile(pobChargingTempPile, actionType, null);
            if ("success".equals(mapp.get("msg"))) {
                PobChargingTempPile pobTempPile = (PobChargingTempPile) mapp.get("tempPile");
                // ////////////////////////////////////////
                values.add(DeviceVerifyStatusEnum.UNVERIFIED.getShortValue());
                values.add(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
                PobChargingPileExample pileEmp = new PobChargingPileExample();
                PobChargingPileExample.Criteria pileCr = pileEmp.createCriteria();
                pileCr.andPileCodeEqualTo(pobTempPile.getPileCode());
                int count1 = deviceService.countPobChargingPileByExample(pileEmp);

                PobChargingTempPileExample tempPileEmp = new PobChargingTempPileExample();
                PobChargingTempPileExample.Criteria tempPileCr = tempPileEmp.createCriteria();
                tempPileCr.andPileCodeEqualTo(pobTempPile.getPileCode());
                tempPileCr.andValidStatusIn(values);
                int count2 = deviceService.countPobChargingTeampPileByExample(tempPileEmp);

                if ((count1 + count2) > 0) {
                    mapp.put("msg", "桩编号重复，请重新填写！！");
                    map = mapp;
                    return SUCCESS;
                }

                // ////////////////////////////////////////////////////////////
                PobChargingPileExample pileEmp2 = new PobChargingPileExample();
                PobChargingPileExample.Criteria pileCr2 = pileEmp2.createCriteria();
                pileCr2.andComAddrEqualTo(pobTempPile.getComAddr());
                int count3 = deviceService.countPobChargingPileByExample(pileEmp2);

                PobChargingTempPileExample tempPileEmp2 = new PobChargingTempPileExample();
                PobChargingTempPileExample.Criteria tempPileCr2 = tempPileEmp2.createCriteria();
                tempPileCr2.andPileCodeEqualTo(pobTempPile.getComAddr());
                tempPileCr2.andValidStatusIn(values);

                if (addNewStationPile.equals(actionType)) {
                    tempPileCr2.andTempStationIdNotEqualTo(newStationId);
                    int count4 = deviceService.countPobChargingTeampPileByExample(tempPileEmp2);
                    if (count3 + count4 > 0) {
                        mapp.put("msg", "桩通讯地址已被占用，请重新填写！！");
                        map = mapp;
                        return SUCCESS;
                    }

                    PobChargingTempPileExample tempPileEmp6 = new PobChargingTempPileExample();
                    PobChargingTempPileExample.Criteria tempPileCr6 = tempPileEmp6.createCriteria();
                    tempPileCr6.andPileNameEqualTo(pobTempPile.getPileName());
                    tempPileCr6.andValidStatusIn(values);
                    tempPileCr6.andTempStationIdEqualTo(newStationId);
                    int count8 = deviceService.countPobChargingTeampPileByExample(tempPileEmp6);
                    if (count8 > 0) {
                        mapp.put("msg", "桩名称重复，请重新填写！！");
                        map = mapp;
                        return SUCCESS;
                    }

                    PobChargingTempPileExample tempPileEmp3 = new PobChargingTempPileExample();
                    PobChargingTempPileExample.Criteria tempPileCr3 = tempPileEmp3.createCriteria();
                    tempPileCr3.andPileCodeEqualTo(pobTempPile.getComAddr());
                    tempPileCr3.andValidStatusIn(values);
                    tempPileCr3.andTempStationIdEqualTo(newStationId);
                    int count5 = deviceService.countPobChargingTeampPileByExample(tempPileEmp3);
                    if (count5 >= comSubAddrList.size()) {
                        mapp.put("msg", "通讯地址[" + pobTempPile.getComAddr() + "]下已经有" + comSubAddrList.size() + "个充电抢不能添加新充电抢！！");
                        map = mapp;
                        return SUCCESS;
                    }
                } else if (addValidStationPile.equals(actionType)) {
                    tempPileCr2.andRealStationIdNotEqualTo(validStationId);
                    int count4 = deviceService.countPobChargingTeampPileByExample(tempPileEmp2);
                    if (count3 + count4 > 0) {
                        mapp.put("msg", "桩通讯地址已被占用，请重新填写！！");
                        map = mapp;
                        return SUCCESS;
                    }
                    // 桩名称检查start
                    PobChargingTempPileExample tempPileEmp7 = new PobChargingTempPileExample();
                    PobChargingTempPileExample.Criteria tempPileCr7 = tempPileEmp7.createCriteria();
                    tempPileCr7.andPileNameEqualTo(pobTempPile.getPileName());
                    tempPileCr7.andValidStatusIn(values);
                    tempPileCr7.andRealStationIdEqualTo(validStationId);
                    int count8 = deviceService.countPobChargingTeampPileByExample(tempPileEmp7);
                    PobChargingPileExample pileEmp3 = new PobChargingPileExample();
                    PobChargingPileExample.Criteria pileCr3 = pileEmp3.createCriteria();
                    pileCr3.andStationIdEqualTo(validStationId);
                    pileCr3.andPileNameEqualTo(pobTempPile.getPileName());
                    int count9 = deviceService.countPobChargingPileByExample(pileEmp3);

                    if (count8 + count9 > 0) {
                        mapp.put("msg", "桩名称重复，请重新填写！！");
                        map = mapp;
                        return SUCCESS;
                    }

                    // 桩名称检查end

                    PobChargingTempPileExample tempPileEmp4 = new PobChargingTempPileExample();
                    PobChargingTempPileExample.Criteria tempPileCr4 = tempPileEmp4.createCriteria();
                    tempPileCr4.andPileCodeEqualTo(pobTempPile.getComAddr());
                    tempPileCr4.andValidStatusIn(values);
                    tempPileCr4.andRealStationIdEqualTo(validStationId);
                    int count6 = deviceService.countPobChargingTeampPileByExample(tempPileEmp4);

                    PobChargingPileExample pileEmp5 = new PobChargingPileExample();
                    PobChargingPileExample.Criteria pileCr5 = pileEmp5.createCriteria();
                    pileCr5.andComAddrEqualTo(pobTempPile.getComAddr());
                    pileCr5.andStationIdEqualTo(validStationId);
                    int count7 = deviceService.countPobChargingPileByExample(pileEmp5);
                    if ((count6 + count7) >= comSubAddrList.size()) {
                        mapp.put("msg", "通讯地址[" + pobTempPile.getComAddr() + "]下已经有" + comSubAddrList.size() + "个充电抢不能添加新充电抢！！");
                        map = mapp;
                        return SUCCESS;
                    }

                }

                // ///////////////////////////////////////
                if (newStationId > 0 && addNewStationPile.equals(actionType)) {
                    pobTempPile.setRequestType(RequestTypeEnum.ADDSTATIONPILE.getShortValue());
                    pobTempPile.setTempStationId(newStationId);
                    pobTempPile.setBusMec(webUser.getInfoId());
                    pobTempPile.setBusType(webUser.getUsertype().getShortValue());
                    pobTempPile.setUpdateTime(new Date());

                    // 插入充电桩设备
                    // deviceService.insertPobChargingTempPileSelective(pobTempPile);
                    try {
                        mapp = deviceService.insertAndUpdateTempPile(pobTempPile, doc, docType, getServletRealPath(Globals.IMG_DATA_FILE));
                    } catch (Exception e) {
                        mapp.put("msg", "添加桩设备失败！！");
                        e.printStackTrace();
                    }
                } else if (validStationId > 0 && addValidStationPile.equals(actionType)) {
                    PobChargingStationExample emp = new PobChargingStationExample();
                    PobChargingStationExample.Criteria cr = emp.createCriteria();
                    cr.andIdEqualTo(validStationId);
                    cr.andBusMecEqualTo(webUser.getInfoId());
                    // cr.andBusTypeEqualTo(webUser.getUsertype().ENTERPRISE.getShortValue());
                    count = deviceService.countPobChargingStationByExample(emp);
                    if (count > 0) {
                        pobTempPile.setRequestType(RequestTypeEnum.ADDPILE.getShortValue());
                        pobTempPile.setRealStationId(validStationId);
                        pobTempPile.setBusMec(webUser.getInfoId());
                        pobTempPile.setBusType(webUser.getUsertype().getShortValue());
                        pobTempPile.setUpdateTime(new Date());
                        // 插入充电桩设备
                        try {
                            mapp = deviceService.insertAndUpdateTempPile(pobTempPile, doc, docType, getServletRealPath(Globals.IMG_DATA_FILE));
                        } catch (Exception e) {
                            mapp.put("msg", "添加桩设备失败！！");
                            e.printStackTrace();
                        }
                        // deviceService.insertPobChargingTempPileSelective(pobTempPile);
                    } else {
                        mapp.put("msg", ERROR);
                    }
                } else {
                    mapp.put("msg", ERROR);
                }
            }
        } else {
            mapp.put("msg", ERROR);
        }
        this.map = mapp;
        return SUCCESS;
    }

    private Map<String, Object> checkPile(String pobChargingTempPile, String actionType, Integer pileId) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "success";
        List<PobChargingPile> pileList = null;
        List<PobChargingTempPile> tempPileList = null;
        try {
            JSONObject o = JSONObject.fromObject(pobChargingTempPile);
            Object activeTime = o.get("activeTime");
            PobChargingTempPile tempPile = null;
            if (activeTime != null && activeTime.toString().length() > 0) {
                tempPile = this.JsonToBean(pobChargingTempPile, PobChargingTempPile.class);
                if (tempPile.getFeeRule() <= 0) {
                    tempPile.setActiveTime(null);
                }
            } else {
                o.remove("activeTime");
                tempPile = (PobChargingTempPile) JSONObject.toBean(o, PobChargingTempPile.class);
            }

            // PobChargingTempPile tempPile = this.JsonToBean(pobChargingTempPile, PobChargingTempPile.class);
            BigDecimal parkFee = tempPile.getParkFee();
            BigDecimal serviceFee = tempPile.getServiceFee();
            BigDecimal chargeFee = tempPile.getChargeFee();
            int feeRule = tempPile.getFeeRule();
            if (StringUtil.isEmpty(tempPile.getPileName())) {
                msg = "请输入充电桩名称！！";
            } else if (StringUtil.isEmpty(tempPile.getPileCode())) {
                msg = "请输入桩编号！！";
            } else if (tempPile.getPileModel() <= 0) {
                msg = "请选择电桩型号！！";
            } else if (StringUtil.isEmpty(tempPile.getPayWay())) {
                msg = "请勾选支付方式！！";
            } else if (StringUtil.isEmpty(tempPile.getAddress())) {
                msg = "请输入详细地址！！";
            } else if (StringUtil.isEmpty(tempPile.getComAddr())) {
                msg = "请选填写通讯地址！！";
            }

            if (addNewStationPile.equals(actionType) || addValidStationPile.equals(actionType) || editNewStationPile.equals(actionType)) {
                if (feeRule <= 0) {
                    msg = "请选择收费规则！！";
                } else if (parkFee == null) {
                    msg = "请输入停车费金额！！";
                } else if (serviceFee == null) {
                    msg = "请输入停车费金额！！";
                } else if (tempPile.getActiveTime() == null) {
                    msg = "请设置费用规则启用时间！！";
                }
            } else if (editValidStationPile.equals(actionType)) {
                if (feeRule > 0) {
                    if (parkFee == null) {
                        msg = "请输入停车费金额！！";
                    } else if (serviceFee == null) {
                        msg = "请输入停车费金额！！";
                    } else if (tempPile.getActiveTime() == null) {
                        msg = "请设置费用规则启用时间！！";
                    }
                }
            }
            if ("success".equals(msg) && tempPile.getActiveTime() != null) {
                SimpleDateFormat formater = new SimpleDateFormat("yyyy/MM/dd");
                String aStr = formater.format(tempPile.getActiveTime());
                String nStr = formater.format(new Date());
                Date aTime = formater.parse(aStr);
                Date nTime = formater.parse(nStr);
                if (nTime.getTime() > aTime.getTime()) {
                    msg = "费用规则启用时间不能小于当前日！！";
                }
            }
            if (tempPile.getIsChaLoad() == null) {
                tempPile.setIsChaLoad(Short.valueOf("2"));
            }
            if (tempPile.getIsRationCha() == null) {
                tempPile.setIsRationCha(Short.valueOf("2"));
            }
            if (tempPile.getIsMoneyCha() == null) {
                tempPile.setIsMoneyCha(Short.valueOf("2"));
            }

            if ("success".equals(msg)) {
                if (feeRule == FeeEnum.ONE.getValue()) {
                    if (chargeFee == null || (chargeFee.compareTo(BigDecimal.ZERO) < 0)) {
                        msg = "请输入单一电费金额！！";
                    } else if (false) {
                        // TODO 金额范围规则判断
                        // msg = "您输入的单一电费金额必须大于等于X小于等于X！！";
                    }
                }
                /* map.put("payWay", NumberUtil.getBinaryStr(payWay)); */
            }
            map.put("tempPile", tempPile);
        } catch (Exception e) {
            msg = "添加桩设备失败！！";
            e.printStackTrace();
        }
        map.put("msg", msg);
        return map;
    }

    /**
     * 在审核通过充电点添加桩页
     * 
     * @return
     */
    public String addValidStationPile() {
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
     * 异步添加充电点
     * 
     * @return
     */
    public String addStationByAjax() {
        WebUser webUser = this.getWebuser();
        String pobTempStation = this.getParameter("pobTempStation");
        Map<String, Object> map = checkAddStation(pobTempStation, img);
        Object msg = map.get("msg");
        Object tempStation = map.get("tempStation");
        if (msg != null && "success".equals((String) msg) && tempStation != null && tempStation instanceof PobChargingTempStation) {
            PobChargingTempStation pobTempStationObj = (PobChargingTempStation) tempStation;
            pobTempStationObj.setBusMec(webUser.getInfoId());
            pobTempStationObj.setBusType(webUser.getUsertype().getShortValue());
            pobTempStationObj.setRequestType(RequestTypeEnum.ADDSTATIONPILE.getShortValue());
            pobTempStationObj.setUpdateTime(new Date());
            try {
                map = this.deviceService.insertAndUpdateTempStation(pobTempStationObj, img, getDataPath());
            } catch (Exception e) {
                map.put("msg", "添加设备失败！！");
                e.printStackTrace();
            }

        }
        this.map = map;
        return SUCCESS;
    }

    private Map<String, Object> checkAddStation(String pobTempStation, File stationImg) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "success";
        if (stationImg == null) {
            msg = "上传图片为空！！";
        } else {
            try {
                ImageUtil.fileIsImage(stationImg);
            } catch (Exception e) {
                msg = "必须上传图片类型文件！！";
            }
        }
        if ("success".equals(msg)) {
            try {
                PobChargingTempStation tempStation = this.JsonToBean(pobTempStation, PobChargingTempStation.class);
                if (StringUtil.isEmpty(tempStation.getStationName())) {
                    msg = "请输入充电点名称！！";
                } else if (tempStation.getProvince() <= 0 || tempStation.getCity() <= 0) {
                    msg = "请选择省市！！";
                } else if (StringUtil.isEmpty(tempStation.getLng()) || StringUtil.isEmpty(tempStation.getLat())) {
                    msg = "请指定经纬度！！";
                } else if (StringUtil.isEmpty(tempStation.getLinkPhone())) {
                    msg = "请填写联系电话！！";
                } else if (tempStation.getOpenDay() <= 0) {
                    msg = "请选择开放日！！";
                } else if (tempStation.getOpenTime() <= 0) {
                    msg = "请选择开放时间！！";
                } else if (StringUtil.isEmpty(tempStation.getAddress())) {
                    msg = "请输入地址信息！！";
                }
                map.put("tempStation", tempStation);
            } catch (Exception e) {
                msg = "上传图片失败！！";
                e.printStackTrace();
            }
        }
        map.put("msg", msg);
        return map;
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
     * 修改充电桩信息
     * 
     * @return
     */
    public String editPileByAjax() {
        Map<String, Object> mapp = new HashMap<String, Object>();
        Map<String, Object> param = new HashMap<String, Object>();
        WebUser webUser = this.getWebuser();
        String actionType = this.getParameter("actionType");
        String pobChargingTempPileStr = this.getParameter("pobChargingTempPile");
        String msg = "success";
        String docType = ".doc";
        List<Short> values = new ArrayList<Short>();
        values.add(DeviceVerifyStatusEnum.UNVERIFIED.getShortValue());
        values.add(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
        int status;
        int pileId = this.getParamInt("pileId");
        if (pileId > 0 && !StringUtil.isEmpty(actionType)) {
            mapp = checkPile(pobChargingTempPileStr, actionType, pileId);
            if ("success".equals(mapp.get("msg"))) {
                PobChargingTempPile pobChargingTempPileObj = (PobChargingTempPile) mapp.get("tempPile");
                if (editNewStationPile.equals(actionType)) {
                    PobChargingTempPileExample emp = new PobChargingTempPileExample();
                    PobChargingTempPileExample.Criteria cr = emp.createCriteria();
                    cr.andIdEqualTo(pileId);
                    cr.andBusMecEqualTo(webUser.getInfoId());
                    cr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
                    List<PobChargingTempPile> pobChargingPileList = deviceService.selectPobChargingTempPileByExample(emp);
                    PobChargingTempPile pobChargingTempPile = null;
                    if (pobChargingPileList != null && pobChargingPileList.size() > 0) {
                        pobChargingTempPile = pobChargingPileList.get(0);
                    }
                    if (pobChargingTempPile != null) {
                        // //////////////////////////////////
                        PobChargingPileExample pileEmp = new PobChargingPileExample();
                        PobChargingPileExample.Criteria pileCr = pileEmp.createCriteria();
                        pileCr.andPileCodeEqualTo(pobChargingTempPileObj.getPileCode());
                        int count1 = deviceService.countPobChargingPileByExample(pileEmp);

                        PobChargingTempPileExample tempPileEmp = new PobChargingTempPileExample();
                        PobChargingTempPileExample.Criteria tempPileCr = tempPileEmp.createCriteria();
                        tempPileCr.andPileCodeEqualTo(pobChargingTempPileObj.getPileCode());
                        tempPileCr.andIdNotEqualTo(pileId);
                        tempPileCr.andValidStatusIn(values);
                        int count2 = deviceService.countPobChargingTeampPileByExample(tempPileEmp);
                        if ((count1 + count2) > 0) {
                            mapp.put("msg", "桩编号重复，请重新填写！！");
                            map = mapp;
                            return SUCCESS;
                        }
                        // ////////////////////////////////////////////////////////////////////////
                        int count3 = 0;
                        if (pobChargingTempPileObj.getRealPileId() != null && pobChargingTempPileObj.getRealPileId() > 0) {
                            PobChargingPileExample pileEmp2 = new PobChargingPileExample();
                            PobChargingPileExample.Criteria pileCr2 = pileEmp2.createCriteria();
                            pileCr2.andComAddrEqualTo(pobChargingTempPileObj.getComAddr());
                            pileCr2.andComSubAddrEqualTo(pobChargingTempPileObj.getComSubAddr());
                            pileCr2.andIdNotEqualTo(pobChargingTempPileObj.getRealPileId());
                            count3 = deviceService.countPobChargingPileByExample(pileEmp2);

                        } else {
                            PobChargingPileExample pileEmp2 = new PobChargingPileExample();
                            PobChargingPileExample.Criteria pileCr2 = pileEmp2.createCriteria();
                            pileCr2.andComAddrEqualTo(pobChargingTempPileObj.getComAddr());
                            // pileCr2.andComSubAddrEqualTo(pobChargingTempPileObj.getComSubAddr());
                            count3 = deviceService.countPobChargingPileByExample(pileEmp2);
                        }

                        PobChargingTempPileExample tempPileEmp2 = new PobChargingTempPileExample();
                        PobChargingTempPileExample.Criteria tempPileCr2 = tempPileEmp2.createCriteria();
                        tempPileCr2.andPileCodeEqualTo(pobChargingTempPileObj.getComAddr());
                        tempPileCr2.andComSubAddrEqualTo(pobChargingTempPileObj.getComSubAddr());
                        tempPileCr2.andIdNotEqualTo(pileId);
                        tempPileCr2.andValidStatusIn(values);
                        int count4 = deviceService.countPobChargingTeampPileByExample(tempPileEmp2);

                        if ((count3 + count4) > 0) {
                            mapp.put("msg", "桩通讯地址重复，请重新填写！！");
                            map = mapp;
                            return SUCCESS;
                        }
                        // ///////////////////////////////////
                        if (pobChargingTempPile.getValidStatus() == DeviceVerifyStatusEnum.UNVERIFIED.getShortValue()
                            || pobChargingTempPile.getValidStatus() == DeviceVerifyStatusEnum.FAILED.getShortValue()) {

                            pobChargingTempPileObj.setValidStatus(DeviceVerifyStatusEnum.UNVERIFIED.getShortValue());
                            pobChargingTempPileObj.setId(pobChargingTempPile.getId());
                            pobChargingTempPileObj.setUpdateTime(new Date());
                            /*
                             * PobChargingTempPileExample example = new PobChargingTempPileExample();
                             * PobChargingTempPileExample.Criteria crr = example.createCriteria();
                             * crr.andIdEqualTo(pileId); crr.andBusMecEqualTo(webUser.getInfoId());
                             * crr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
                             */
                            if (this.doc != null) {
                                try {
                                    param = ImageUtil.uploadDoc(doc, pobChargingTempPileObj.getId(), docType, getDataPath());
                                } catch (Exception e) {
                                    param.put("msg", "资料上传失败!");
                                }
                                if ("success".equals(param.get("msg"))) {
                                    pobChargingTempPileObj.setDoc(param.get("url").toString());
                                }
                            } else {
                                pobChargingTempPileObj.setDoc(pobChargingTempPile.getDoc());
                            }

                            deviceService.updateChargingTempPileByPKSelective(pobChargingTempPileObj);

                        } else {
                            msg = "设备正在审核中或已审核通过！！";
                            mapp.put("msg", msg);
                        }
                    } else {
                        msg = ERROR;
                        mapp.put("msg", msg);
                    }
                    // 修改未审核桩信息end

                } else if (editValidStationPile.equals(actionType)) {
                    // 修改已审核桩信息start
                    param.put("pileId", pileId);
                    param.put("busMec", webUser.getInfoId());
                    param.put("busType", webUser.getUsertype().getValue());
                    PobChargingPile pobChargingPile = deviceService.selectPileByMap(param);
                    if (pobChargingPile != null) {
                        // //////////////////////////////////
                        PobChargingPileExample pileEmp = new PobChargingPileExample();
                        PobChargingPileExample.Criteria pileCr = pileEmp.createCriteria();
                        pileCr.andPileCodeEqualTo(pobChargingTempPileObj.getPileCode());
                        pileCr.andIdNotEqualTo(pileId);
                        int count1 = deviceService.countPobChargingPileByExample(pileEmp);

                        PobChargingTempPileExample tempPileEmp = new PobChargingTempPileExample();
                        PobChargingTempPileExample.Criteria tempPileCr = tempPileEmp.createCriteria();
                        tempPileCr.andPileCodeEqualTo(pobChargingTempPileObj.getPileCode());
                        tempPileCr.andValidStatusIn(values);
                        tempPileCr.andRealPileIdNotEqualTo(pileId);
                        tempPileEmp.or().andPileCodeEqualTo(pobChargingTempPileObj.getPileCode()).andValidStatusIn(values).andRealPileIdIsNull();
                        // tempPileCr.andRequestTypeEqualTo(RequestTypeEnum.EDITPILE.getShortValue());
                        int count2 = deviceService.countPobChargingTeampPileByExample(tempPileEmp);

                        // ////////////////////////////////////////////////////////////////////////
                        PobChargingPileExample pileEmp2 = new PobChargingPileExample();
                        PobChargingPileExample.Criteria pileCr2 = pileEmp2.createCriteria();
                        pileCr2.andComAddrEqualTo(pobChargingTempPileObj.getComAddr());
                        pileCr2.andIdNotEqualTo(pileId);
                        pileCr2.andComSubAddrEqualTo(pobChargingTempPileObj.getComSubAddr());
                        int count3 = deviceService.countPobChargingPileByExample(pileEmp2);

                        PobChargingTempPileExample tempPileEmp2 = new PobChargingTempPileExample();
                        PobChargingTempPileExample.Criteria tempPileCr2 = tempPileEmp2.createCriteria();
                        tempPileCr2.andComAddrEqualTo(pobChargingTempPileObj.getComAddr());
                        tempPileCr2.andValidStatusIn(values);
                        tempPileCr2.andRealPileIdNotEqualTo(pileId);
                        tempPileCr2.andComSubAddrEqualTo(pobChargingTempPileObj.getComSubAddr());
                        tempPileEmp2.or().andComAddrEqualTo(pobChargingTempPileObj.getComAddr()).andValidStatusIn(values).andComSubAddrEqualTo(pobChargingTempPileObj.getComSubAddr()).andRealPileIdIsNull();
                        // tempPileCr2.andRequestTypeEqualTo(RequestTypeEnum.EDITPILE.getShortValue());

                        int count4 = deviceService.countPobChargingTeampPileByExample(tempPileEmp2);

                        if ((count1 + count2) > 0) {
                            mapp.put("msg", "桩编号重复，请重新填写！！");
                            map = mapp;
                            return SUCCESS;
                        } else if ((count3 + count4) > 0) {
                            mapp.put("msg", "桩通讯地址重复，请重新填写！！");
                            map = mapp;
                            return SUCCESS;
                        }

                        pobChargingTempPileObj.setRealPileId(pileId);
                        pobChargingTempPileObj.setRequestType(RequestTypeEnum.EDITPILE.getShortValue());
                        pobChargingTempPileObj.setBusMec(webUser.getInfoId());
                        pobChargingTempPileObj.setBusType(webUser.getUsertype().getShortValue());
                        pobChargingTempPileObj.setUpdateTime(new Date());
                        pobChargingTempPileObj.setRealStationId(pobChargingPile.getStationId());
                        if (doc == null) {
                            pobChargingTempPileObj.setDoc(pobChargingPile.getDoc());
                        }
                        PobChargingTempPileExample emp = new PobChargingTempPileExample();
                        PobChargingTempPileExample.Criteria cr1 = emp.createCriteria();
                        PobChargingTempPileExample.Criteria cr2 = emp.createCriteria();
                        cr1.andRealPileIdEqualTo(pileId);
                        cr1.andRequestTypeEqualTo(RequestTypeEnum.EDITPILE.getShortValue());
                        cr1.andBusMecEqualTo(webUser.getInfoId());
                        cr1.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
                        cr1.andValidStatusEqualTo(DeviceVerifyStatusEnum.UNVERIFIED.getShortValue());
                        cr2.andRealPileIdEqualTo(pileId);
                        cr2.andRequestTypeEqualTo(RequestTypeEnum.EDITPILE.getShortValue());
                        cr2.andBusMecEqualTo(webUser.getInfoId());
                        cr2.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
                        cr2.andValidStatusEqualTo(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
                        emp.or(cr2);
                        List<PobChargingTempPile> pobChargingPileList = deviceService.selectPobChargingTempPileByExample(emp);
                        PobChargingTempPile pobChargingTempPileOld = null;
                        if (pobChargingPileList != null && pobChargingPileList.size() > 0) {
                            pobChargingTempPileOld = pobChargingPileList.get(0);
                        }
                        if (pobChargingTempPileOld != null) {
                            if (pobChargingTempPileOld.getValidStatus() == DeviceVerifyStatusEnum.VERIFYING.getShortValue()) {
                                msg = "该充电点有修改信息正在审核中，请稍后再试！！";
                                mapp.put("msg", msg);
                            }

                        }
                        if ("success".equals(msg)) {
                            PobChargingTempPile newPobChargingTempPile = null;
                            if (pobChargingTempPileOld != null) {
                                newPobChargingTempPile = new PobChargingTempPile();
                                newPobChargingTempPile.setValidStatus(DeviceVerifyStatusEnum.CANCEL.getShortValue());
                                newPobChargingTempPile.setUpdateTime(new Date());
                                newPobChargingTempPile.setId(pobChargingTempPileOld.getId());
                            }
                            try {
                                this.deviceService.insertAndUpdateTempPileForEdit(newPobChargingTempPile, pobChargingTempPileObj, doc, docType, getDataPath());
                            } catch (Exception e) {
                                msg = "修改桩设备失败！！";
                                mapp.put("msg", msg);
                                e.printStackTrace();
                            }
                            /*
                             * status =
                             * deviceService.updatePobChargingTempPileByPrimaryKeySelective(newPobChargingTempPile,
                             * example); // insert deviceService.insertPobChargingTempPileSelective(tempPile);
                             */
                        }

                    } else {
                        mapp.put("msg", ERROR);
                    }
                }
            }
        } else {
            mapp.put("msg", ERROR);
        }
        this.map = mapp;
        return SUCCESS;
    }

    /**
     * 查询未审核充电点设备列表信息
     * 
     * @return
     */
    public String searchUnValidDevice() {

        return SUCCESS;
    }

    /**
     * 异步查询未审核充电点设备列表信息
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String searchUnValidStationsByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int limit = this.getParamInt("limit");// 显示行数
        String searchStationName = this.getParameter("searchStationName");
        int validStatus = this.getParamInt("validStatus");
        param.put("busType", webUser.getUsertype().getValue());
        param.put("busMec", webUser.getInfoId());
        if (!StringUtil.isEmpty(searchStationName)) {
            param.put("searchStationName", searchStationName);
        }
        if (validStatus > 0) {
            param.put("validStatus", validStatus);
        } else {
            List notValidStatus = new ArrayList();
            notValidStatus.add(DeviceVerifyStatusEnum.PASSED.getShortValue());
            notValidStatus.add(DeviceVerifyStatusEnum.CANCEL.getValue());
            param.put("notValidStatus", notValidStatus);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            // 查询待审核，审核未通过，审核中
            List<PobChargingTempStation> list = deviceService.selectTempStationByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            PobChargingTempStationExample tempStationEmp = new PobChargingTempStationExample();
            PobChargingTempStationExample.Criteria tempStationCr = tempStationEmp.createCriteria();
            tempStationCr.andBusMecEqualTo(webUser.getInfoId());
            tempStationCr.andBusTypeEqualTo(webUser.getUsertype().getShortValue()); // 查找企业桩
            tempStationCr.andValidStatusNotEqualTo(DeviceVerifyStatusEnum.CANCEL.getShortValue());
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.MONTH, DateUtil.PRE_THREE_MONTH);// 获取上三个月日期
            tempStationCr.andUpdateTimeGreaterThanOrEqualTo(calendar.getTime());
            tempStationCr.andUpdateTimeLessThanOrEqualTo(now);
            List<PobChargingTempStation> exportList = deviceService.selectPobChargingTempStationByExample(tempStationEmp);
            // List<PobChargingTempStation> exportList = deviceService.selectTempStationByPage(param);
            String[] headsName = { "充电点名称", "地址", "开放日", "开放时间", "快充桩数量", "慢充桩数量", "评分", "经度", "纬度", "省份", "市区", "停车场类型", "联系电话", "申请时间", "申请状态", "申请类型" };
            String[] properiesName = { "stationName", "address", "openDayDesc2", "openTimeDesc2", "fastNum", "slowNum", "scoreDesc2", "lng", "lat", "provinceDesc2", "cityDesc2",
                    "parkTypeDesc2", "linkPhone", "updateTimeDesc2", "validStatusDesc2", "requestTypeDesc2" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, StationAppointmentModel.class);
            return null;
        }
        return SUCCESS;
    }

    /**
     * 异步查询未审核充电桩信息
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String searchUnValidPilesByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int limit = this.getParamInt("limit");// 显示行数
        int validStatus = this.getParamInt("validStatus");
        param.put("busMec", webUser.getInfoId());
        param.put("busType", webUser.getUsertype().getValue());
        if (validStatus > 0) {
            param.put("validStatus", validStatus);
        } else {
            List notValidStatus = new ArrayList();
            notValidStatus.add(DeviceVerifyStatusEnum.PASSED.getShortValue());
            notValidStatus.add(DeviceVerifyStatusEnum.CANCEL.getValue());
            param.put("notValidStatus", notValidStatus);
        }
        String searchPileName = this.getParameter("searchPileName");
        if (!StringUtil.isEmpty(searchPileName)) {
            param.put("searchPileName", searchPileName);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<PobChargingTempPile> list = deviceService.selectTempPileByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            PobChargingTempPileExample tempPileEmp = new PobChargingTempPileExample();
            PobChargingTempPileExample.Criteria tempPileCr = tempPileEmp.createCriteria();
            tempPileCr.andBusMecEqualTo(webUser.getInfoId());
            tempPileCr.andBusTypeEqualTo(webUser.getUsertype().getShortValue());
            tempPileCr.andValidStatusNotEqualTo(DeviceVerifyStatusEnum.CANCEL.getShortValue());
            Date now = new Date();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(now);
            calendar.add(Calendar.MONTH, DateUtil.PRE_THREE_MONTH);// 获取上三个月日期
            tempPileCr.andUpdateTimeGreaterThanOrEqualTo(calendar.getTime());
            tempPileCr.andUpdateTimeLessThanOrEqualTo(now);
            List<PobChargingTempPile> exportList = deviceService.selectPobChargingTempPileByExample(tempPileEmp);
            String[] headsName = { "桩名称", "桩编号", "充电类型", "充电方式", "通讯协议", "通讯地址", "支持预约", "支付方式", "充电模式", "申请时间", "申请状态", "申请类型" };
            String[] properiesName = { "pileName", "pileCode", "pileTypeDesc2", "chaWayDesc2", "comTypeDesc2", "comAddr", "isAppDesc2", "payWayDesc2", "chaModelDesc2",
                    "updateTimeDesc2", "validStatusDesc2", "requestTypeDesc2" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, StationAppointmentModel.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 查询充正式充电点设备列表信息
     * 
     * @return
     */
    public String searchValidDevice() {
        int stationId = this.getParamInt("stationId");
        getRequest().setAttribute("stationId", stationId);
        return SUCCESS;
    }

    public String readDocForHtml() {
        /*
         * String fileName = "data/stationTempImg/xxx.doc"; fileName = fileName.substring(fileName.lastIndexOf("/") + 1,
         * fileName.length()); fileName = fileName.substring(0, fileName.indexOf(".")); String path =
         * ServletActionContext.getServletContext().getRealPath(Globals.IMG_DATA_FILE) + File.separator +
         * ImgTypeEnum.STATION_TEMP_IMG.getFilename() + File.separator; String realPath = path + fileName + ".html"; try
         * { String content = TestUtil.convert2Html(path + "xxx.doc", path); HttpServletResponse response =
         * ServletActionContext.getResponse(); response.setContentType("text/html;charset=UTF-8"); PrintWriter out =
         * response.getWriter(); out.print(content); out.flush(); out.close(); } catch (Exception e) {
         * logger.info("发送HTML，失败！！"); e.printStackTrace(); }
         */
        return null;
    }

    /**
     * ajax分页查询正式充电点列表
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String searchStationsByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = 8;// 显示行数
        int isExport = this.getParamInt("isExport");// 是否导出excel
        String searchKeyName = this.getParameter("searchKeyName");
        param.put("busMec", webUser.getInfoId());
        param.put("busType", webUser.getUsertype().getValue());// 查找企业桩
        if (!StringUtil.isEmpty(searchKeyName)) {
            param.put("searchStationName", searchKeyName);
        }
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
            jxl.exportXLS(exportList, properiesName, headsName, StationChargeModel.class);
            return null;
        }

        // logger.info("充电点数量：" + list.size());
        return SUCCESS;
    }

    /**
     * 分页查询正式充电点桩列表信息
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String searchPilesByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        String searchKeyName = this.getParameter("searchKeyName");
        int stationId = this.getParamInt("stationId");
        int isExport = this.getParamInt("isExport");// 是否导出excel
        if (UserTypeEnum.ENTERPRISE == webUser.getUsertype()) {
            param.put("busType", UserTypeEnum.ENTERPRISE.getValue());// 查找企业桩
        } else if (UserTypeEnum.PERSON == webUser.getUsertype()) {
            param.put("busType", UserTypeEnum.PERSON.getValue());// 查找个人桩
        }
        if (stationId <= 0) {
            message = "请选择充电点！！";
            return SUCCESS;
        }
        param.put("stationId", stationId);
        param.put("busMec", webUser.getInfoId());
        if (!StringUtil.isEmpty(searchKeyName)) {
            param.put("pileName", searchKeyName);
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
            jxl.exportXLS(exportList, properiesName, headsName, StationChargeModel.class);
            return null;
        }

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
            // 通讯协议：南网，创锐
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

    // SET GET
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public Page getPage() {
        return page;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setImg(File img) {
        this.img = img;
    }

    public void setDoc(File doc) {
        this.doc = doc;
    }

    public String getMessage() {
        return message;
    }
}
