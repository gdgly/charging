package com.holley.charging.bms.device.action;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusPileChargerule;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingPileExample;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.charging.service.website.PobObjectService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.ShowStausEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysDefArea;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.util.CacheSysHolder;
import com.holley.web.common.util.ImageUtil;

/**
 * 充电点
 * 
 * @author lenovo
 */
public class StationAction extends BaseAction {

    private final static Logger   logger           = Logger.getLogger(StationAction.class);
    private static final long     serialVersionUID = 1L;
    private DeviceService         deviceService;
    private PobObjectService      pobObjectService;
    private AccountService        accountService;
    private Page                  page;
    private List<SysDefArea>      citys;
    private List<PobChargingPile> pileList;
    private Map<String, Object>   dataMap;
    private static String         initStationParam = "initStationParam";                   // 初始化充电点参数
    private static String         initPileParam    = "initPileParam";                      // 初始化充电桩参数
    private Map<String, Object>   map;
    private File                  img;

    public String addStationInit() {
        initParam(initStationParam);
        // 省级
        List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
        this.getRequest().setAttribute("provinceList", provinceList);
        return SUCCESS;
    }

    /**
     * 异步添加充电点
     * 
     * @return
     */
    public String addStationByAjax() {
        String pobTempStation = this.getParameter("pobTempStation");
        Map<String, Object> map = checkAddStation(pobTempStation, img);
        Object msg = map.get("msg");
        Object tempStation = map.get("tempStation");
        if (msg != null && "success".equals((String) msg) && tempStation != null && tempStation instanceof PobChargingStation) {
            PobChargingStation pobTempStationObj = (PobChargingStation) tempStation;
            pobTempStationObj.setBusType(UserTypeEnum.PLATFORM.getShortValue());
            pobTempStationObj.setBusMec(0);// 后台添加设备账户信息默认为0
            pobTempStationObj.setUpdateTime(new Date());
            try {
                map = this.deviceService.insertAndUpdateStation(pobTempStationObj, img, getDataPath());
            } catch (Exception e) {
                map.put("msg", "添加设备失败！！");
                e.printStackTrace();
            }
        }
        this.map = map;
        return SUCCESS;
    }

    /**
     * 修改已审核通过的点信息
     * 
     * @return
     */
    public String editStationInit() {
        int stationId = this.getParamInt("stationId");// 已经审核通过的点ID
        if (stationId > 0) {
            PobChargingStationExample emp = new PobChargingStationExample();
            PobChargingStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(stationId);
            cr.andBusTypeEqualTo(UserTypeEnum.PLATFORM.getShortValue());
            PobChargingStation pobChargingStation = deviceService.selectPobChargingStationByExample(emp);
            if (pobChargingStation != null) {
                this.initParam(initStationParam);
                // 是否类型
                List<SysDefArea> provinceList = CacheSysHolder.getProvinceList();
                List<SysDefArea> cityList = CacheSysHolder.getCityListByPid(pobChargingStation.getProvince());
                this.getRequest().setAttribute("provinceList", provinceList);
                this.getRequest().setAttribute("cityList", cityList);
                this.getRequest().setAttribute("stationId", stationId);
                this.getRequest().setAttribute("pobChargingStation", pobChargingStation);
                return SUCCESS;
            } else {
                return MEMBER;
            }
        }
        return MEMBER;

    }

    /**
     * 修改充电点
     * 
     * @return
     */
    public String editStationByAjax() {
        int stationId = this.getParamInt("stationId");// 未审核通过的点ID
        String pobStation = this.getParameter("pobStation");
        Map<String, Object> mapp = new HashMap<String, Object>();
        mapp = checkEditStation(pobStation, img);
        Object msg = mapp.get("msg");
        Object pobChargingStationObj = mapp.get("pobChargingStationObj");
        if (msg != null && "success".equals((String) msg) && pobChargingStationObj != null && pobChargingStationObj instanceof PobChargingStation) {
            PobChargingStationExample emp = new PobChargingStationExample();
            PobChargingStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(stationId);
            cr.andBusTypeEqualTo(UserTypeEnum.PLATFORM.getShortValue());
            PobChargingStation pobChargingStation = deviceService.selectPobChargingStationByExample(emp);
            if (pobChargingStation != null) {
                PobChargingStation chargingStation = (PobChargingStation) pobChargingStationObj;
                PobChargingStationExample pobStationEmp = new PobChargingStationExample();
                PobChargingStationExample.Criteria pobStationCr = pobStationEmp.createCriteria();
                pobStationCr.andStationNameEqualTo(chargingStation.getStationName());
                pobStationCr.andIdNotEqualTo(stationId);
                int count1 = deviceService.countPobChargingStationByExample(pobStationEmp);// 对比正式充电点
                if ((count1) > 0) {
                    mapp.put("msg", "充电点名称与其他充电点重复！！");
                    map = mapp;
                    return SUCCESS;
                }

                if (img == null) {
                    chargingStation.setImg(pobChargingStation.getImg());
                }
                if ("success".equals(mapp.get("msg"))) {
                    chargingStation.setId(pobChargingStation.getId());
                    chargingStation.setUpdateTime(new Date());
                    // update
                    try {
                        mapp = deviceService.updatePobStation(chargingStation, img, getDataPath());
                    } catch (Exception e1) {
                        mapp.put("msg", "修改失败！！");
                        e1.printStackTrace();
                    }

                }
            } else {
                mapp.put("msg", ERROR);
            }
        }
        map = mapp;
        return SUCCESS;
    }

    public String stationListInit() {
        this.getRequest().setAttribute("busTypeList", UserTypeEnum.getMecTypeList());
        this.getRequest().setAttribute("showStatusList", ShowStausEnum.values());
        this.getRequest().setAttribute("isShow", ShowStausEnum.SHOW.getShortValue());
        this.getRequest().setAttribute("provinceList", CacheSysHolder.getProvinceList());
        this.getRequest().setAttribute("pileTypeList", ChargePowerTypeEnum.values());
        return SUCCESS;
    }

    /**
     * 充电点详细内容初始化
     * 
     * @return
     */
    public String stationDetailInit() {
        String id = this.getParameter("id");
        PobChargingStation chargeStation = deviceService.selectChargeStationByPrimaryKey((Integer.valueOf(id)));
        if (chargeStation == null) return SUCCESS;
        setStationFieldName(chargeStation);
        this.getRequest().setAttribute("chargeStation", chargeStation);
        return SUCCESS;
    }

    /**
     * 查询已审核通过的充电点
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String queryStationList() throws Exception {
        String keyword = this.getParameter("keyword");
        String province = this.getParameter("province");
        String city = this.getParameter("city");
        String bustype = this.getParameter("bustype");
        String isshow = this.getParameter("isshow");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotDigits(province, city, bustype, isshow)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", keyword);
        }
        if (StringUtil.isDigits(province) && Integer.parseInt(province) > 0) {
            params.put("province", Integer.valueOf(province));
        }
        if (StringUtil.isDigits(city) && Integer.parseInt(city) > 0) {
            params.put("city", Integer.valueOf(city));
        }
        if (StringUtil.isDigits(bustype) && Integer.parseInt(bustype) > 0) {
            params.put("bustype", Short.valueOf(bustype));
        }
        if (StringUtil.isDigits(isshow) && Integer.parseInt(isshow) > 0) {
            params.put("isshow", Short.valueOf(isshow));
        }

        if (isExportExcel()) {
            List<PobChargingStation> stationList = pobObjectService.selectChargeStationByPage(params);
            String[] headsName = { "充电点编码", "充电点名称", "所属省", "所属市", "详细地址", "经度", "纬度", "运营类型", "运营机构", "开放日", "开放时间", "停车场类型", "联系电话", "状态", "快桩数", "慢桩数", "评分", "更新时间" };
            String[] properiesName = { "id", "stationName", "provinceDesc", "cityDesc", "address", "lng", "lat", "busTypeDesc", "busMecName", "openDayDesc", "openTimeDesc",
                    "parkTypeDesc", "linkPhone", "isShowDesc", "fastNum", "slowNum", "score", "updateTimeStr" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(stationList, properiesName, headsName, PobChargingStation.class);
            return null;
        } else {
            if (StringUtil.isNotDigits(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);

            List<PobChargingStation> stationList = pobObjectService.selectChargeStationByPage(params);
            page.setRoot(stationList);
            this.page = page;
            return SUCCESS;
        }
    }

    public String queryPileList() throws Exception {
        String stationid = this.getParameter("stationid");
        String piletype = this.getParameter("piletype");
        if (StringUtil.isNotDigits(stationid, piletype)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        PobChargingPileExample emp = new PobChargingPileExample();
        PobChargingPileExample.Criteria cr = emp.createCriteria();
        cr.andStationIdEqualTo(Integer.valueOf(stationid));
        if (StringUtil.isDigits(piletype) && Integer.parseInt(piletype) > 0) {
            cr.andPileTypeEqualTo(Short.valueOf(piletype));
        }
        List<PobChargingPile> list = pobObjectService.selectChargingPileByExample(emp);
        if (isExportExcel()) {
            if (list != null && list.size() > 0) {
                PobChargingStation station = CacheChargeHolder.getChargeStationById(Integer.valueOf(stationid));
                if (station != null && station.getStationName() != null) {
                    for (PobChargingPile record : list) {
                        record.setStationName(station.getStationName());
                    }
                }
            }
            String[] headsName = { "电桩编码", "电桩名称", "所属充电点", "电桩类型", "电桩编号", "通讯协议", "通讯地址", "通讯子地址", "电桩型号", "充电方式", "充电模式", "支付方式", "是否支持远程控制", "是否支持负荷调度", "是否支持预约", "状态",
                    "更新时间", "安装时间", "详细地址", };
            String[] properiesName = { "id", "pileName", "stationName", "pileTypeDesc", "pileCode", "comTypeDesc", "comAddr", "comSubAddr", "pileModelDesc", "chaWayDesc",
                    "chaModelDesc", "payWayDesc", "isControlDesc", "isChaLoadDesc", "isAppDesc", "statusDesc", "updateTimeStr", "buildTimeStr", "address" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, PobChargingPile.class);
            return null;
        } else {
            this.pileList = list;
            return SUCCESS;
        }

    }

    /**
     * 充电桩详细内容初始化
     * 
     * @return
     */
    public String queryPileDetail() {
        String id = this.getParameter("pileid");
        if (!StringUtil.isDigits(id)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        PobChargingPile chargePile = deviceService.selectPileByPrimaryKey(Integer.valueOf(id));
        if (chargePile == null) return SUCCESS;
        // 设置充电点名称
        PobChargingStation station = CacheChargeHolder.getChargeStationById(chargePile.getStationId());
        if (station != null) {
            chargePile.setStationName(station.getStationName());
        }

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("chargePile", chargePile);

        // 查询费用信息：正在使用的费用规则和待激活的费用规则
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pileid", chargePile.getId());
        params.put("status", WhetherEnum.YES.getShortValue());// 有效
        params.put("isactive", WhetherEnum.YES.getShortValue());// 已激活
        BusPileChargerule activeRule = deviceService.selectPileChargeRuleByParams(params);
        if (activeRule != null) {
            result.put("activeRule", activeRule);
        }

        params.put("isactive", WhetherEnum.NO.getShortValue());// 未激活
        BusPileChargerule unactiveRule = deviceService.selectPileChargeRuleByParams(params);
        if (unactiveRule != null) {
            result.put("unactiveRule", unactiveRule);
        }
        this.dataMap = result;
        return SUCCESS;
    }

    /**
     * 根据省级ID获取市级 ajax
     * 
     * @return
     */
    public String queryCitys() {
        int provinceId = this.getParamInt("province");
        logger.info("provinceId:" + provinceId);
        if (provinceId > 0) {
            citys = CacheSysHolder.getCityListByPid(provinceId);
        }
        return SUCCESS;
    }

    /**
     * 设置chargStation的描述信息
     * 
     * @param chargeStation
     */
    private void setStationFieldName(PobChargingStation chargeStation) {
        if (chargeStation == null) return;
        if (chargeStation.getBusType() != null) {
            if (chargeStation.getBusMec() != null) {
                if (UserTypeEnum.ENTERPRISE.getShortValue().equals(chargeStation.getBusType())) {
                    BusBussinessInfo busInfo = accountService.selectBusBussinessInfoByPrimaryKey(chargeStation.getBusMec());
                    chargeStation.setBusMecName(busInfo == null ? "" : busInfo.getBusName());
                } else if (UserTypeEnum.PERSON.getShortValue().equals(chargeStation.getBusType())) {
                    BusUserInfo userInfo = accountService.selectUserInfoByPrimaryKey(chargeStation.getBusMec());
                    chargeStation.setBusMecName(userInfo == null ? "" : userInfo.getRealName());
                }
            }
        }
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
            getRequest().setAttribute("chaWayList", chaWayList);
            getRequest().setAttribute("isOrNoList", isOrNoList);
            getRequest().setAttribute("comTypeList", comTypeList);
            getRequest().setAttribute("pileTypeList", pileTypeList);
            getRequest().setAttribute("payWayList", payWayList);
            getRequest().setAttribute("busPileModelList", busPileModelList);
        }

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
                PobChargingStation tempStation = this.JsonToBean(pobTempStation, PobChargingStation.class);
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

                PobChargingStation pobChargingStation = this.JsonToBean(pobStation, PobChargingStation.class);
                if (StringUtil.isEmpty(pobChargingStation.getStationName())) {
                    msg = "请输入充电点名称！！";
                } else if (StringUtil.isEmpty(pobChargingStation.getLng()) || StringUtil.isEmpty(pobChargingStation.getLat())) {
                    msg = "请指定经纬度！！";
                } else if (StringUtil.isEmpty(pobChargingStation.getAddress())) {
                    msg = "请输入地址信息！！";
                } else if (StringUtil.isEmpty(pobChargingStation.getLinkPhone())) {
                    msg = "请输入联系电话！！";
                }
                map.put("pobChargingStationObj", pobChargingStation);

            } catch (Exception e) {
                msg = "修改充电点信息失败！！";
            }
        }
        map.put("msg", msg);
        return map;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void setPobObjectService(PobObjectService pobObjectService) {
        this.pobObjectService = pobObjectService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
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

    public List<SysDefArea> getCitys() {
        return citys;
    }

    public List<PobChargingPile> getPileList() {
        return pileList;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setImg(File img) {
        this.img = img;
    }
}
