package com.holley.charging.bjbms.device.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;

import com.alibaba.fastjson.JSON;
import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.common.util.CacheChargeHolder.PileRunStatusEnum;
import com.holley.charging.model.bus.BusChargeRule;
import com.holley.charging.model.bus.BusPileChargerule;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.def.ChargeRuleModel;
import com.holley.charging.model.def.PileRunStatusModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingPileExample;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.model.pob.PobChargingTempPile;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.LogTypeEnum;
import com.holley.common.constants.charge.ChargeCurrentTypeEnum;
import com.holley.common.constants.charge.ChargeInterfaceTypeEnum;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.ChargeWayEnum;
import com.holley.common.constants.charge.FeeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.PileInfoStatusEnum;
import com.holley.common.constants.charge.UseToTypeEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.LogUtil;

public class PileAction extends BaseAction {

    private static final long   serialVersionUID = 1L;
    private DeviceService       deviceService;
    private Page                page;
    private static String       initStationParam = "initStationParam"; // 初始化充电点参数
    private static String       initPileParam    = "initPileParam";   // 初始化充电桩参数
    private Map<String, Object> map;
    private static String       addPile          = "addPile";         // 添加新充电桩操作
    private static String       editPile         = "editPile";        // 在已审核充电点添加桩操作

    public String editPileInit() {
        if (isSubCompany(null)) {
            return MEMBER;
        }
        int pileId = this.getParamInt("pileId");
        Map<String, Object> param;
        if (pileId > 0) {
            /*
             * param = new HashMap<String, Object>(); param.put("pileId", validPileId); param.put("busType",
             * webUser.getUsertype().getValue()); param.put("busMec", webUser.getInfoId()); PobChargingPile
             * pobChargingPile = deviceService.selectPileByMap(param);
             */
            param = new HashMap<String, Object>();
            param.put("pileId", pileId);
            param.put("active", WhetherEnum.YES.getShortValue());// 时间已激活
            param.put("status", WhetherEnum.YES.getShortValue());// 已生效
            ChargeRuleModel activeRule = deviceService.selectChargeRuleModelByMap(param);
            param.put("active", WhetherEnum.NO.getShortValue());// 时间未激活
            ChargeRuleModel unActiveRule = deviceService.selectChargeRuleModelByMap(param);
            PobChargingPileExample emp = new PobChargingPileExample();
            PobChargingPileExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(pileId);
            List<PobChargingPile> pobChargingPileList = deviceService.selectPobChargingPileByExample(emp);
            PobChargingPile pobChargingPile = pobChargingPileList.get(0);
            if (pobChargingPile != null) {
                PobChargingStationExample stationEmp = new PobChargingStationExample();
                PobChargingStationExample.Criteria stationCr = stationEmp.createCriteria();
                stationCr.andIdEqualTo(pobChargingPile.getStationId());
                PobChargingStation station = deviceService.selectPobChargingStationByExample(stationEmp);

                if (!(UserTypeEnum.PLATFORM.getShortValue().shortValue() == station.getBusType().shortValue())) {
                    return MEMBER;
                }
                this.initParam(initPileParam);
                List<BusChargeRule> chargeRuleList = CacheChargeHolder.getChargeRuleList();
                this.getRequest().setAttribute("chargeRuleList", chargeRuleList);
                this.getRequest().setAttribute("chargeRuleListJson", JsonUtil.list2json(chargeRuleList));
                this.getRequest().setAttribute("pobChargingPile", pobChargingPile);
                // this.getRequest().setAttribute("pileId", pileId);
                this.getRequest().setAttribute("activeRule", activeRule);
                this.getRequest().setAttribute("unActiveRule", unActiveRule);
                this.getRequest().setAttribute("stationToType", station.getStationToType());
                Calendar a = Calendar.getInstance();
                a.add(Calendar.DATE, 1);
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

    public String editPileByAjax() throws Exception {
        Map<String, Object> mapp = new HashMap<String, Object>();
        if (isSubCompany(null)) {
            mapp.put("msg", "非法操作！！");
            this.map = mapp;
            return SUCCESS;
        }

        String pobChargingTempPileStr = this.getParameter("pobChargingTempPile");
        int pileId = this.getParamInt("pileId");
        if (pileId > 0) {
            mapp = checkPile(pobChargingTempPileStr, editPile);
            if ("success".equals(mapp.get("msg"))) {
                PobChargingTempPile pobChargingTempPileObj = (PobChargingTempPile) mapp.get("tempPile");
                PobChargingPileExample emp = new PobChargingPileExample();
                PobChargingPileExample.Criteria cr = emp.createCriteria();
                cr.andIdEqualTo(pileId);
                List<PobChargingPile> list = deviceService.selectPobChargingPileByExample(emp);
                PobChargingPile pile = list.get(0);

                PobChargingPileExample pileEmp4 = new PobChargingPileExample();
                PobChargingPileExample.Criteria pileCr4 = pileEmp4.createCriteria();
                pileCr4.andComAddrEqualTo(pobChargingTempPileObj.getComAddr());
                pileCr4.andIdNotEqualTo(pileId);
                int count4 = deviceService.countPobChargingPileByExample(pileEmp4);// 查询同一通讯地址对应抢个数
                List<SysLink> comSubAddrList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_SUB_ADDR.getValue());

                if (count4 >= comSubAddrList.size()) {
                    mapp.put("msg", "通讯地址[" + pobChargingTempPileObj.getComAddr() + "]下已经有" + comSubAddrList.size() + "个充电抢不能添加新充电抢！！");
                    map = mapp;
                    return SUCCESS;
                }

                PobChargingPileExample pileEmp = new PobChargingPileExample();
                PobChargingPileExample.Criteria pileCr = pileEmp.createCriteria();
                pileCr.andPileCodeEqualTo(pobChargingTempPileObj.getPileCode());
                pileCr.andIdNotEqualTo(pileId);
                int count1 = deviceService.countPobChargingPileByExample(pileEmp);// 查询桩编号重复情况
                if (count1 > 0) {
                    mapp.put("msg", "桩编号重复，请重新填写！！");
                    map = mapp;
                    return SUCCESS;
                }

                PobChargingPileExample pileEmp2 = new PobChargingPileExample();
                PobChargingPileExample.Criteria pileCr2 = pileEmp2.createCriteria();
                pileCr2.andComAddrEqualTo(pobChargingTempPileObj.getComAddr());
                pileCr2.andStationIdNotEqualTo(pile.getStationId());
                int count2 = deviceService.countPobChargingPileByExample(pileEmp2);// 查询桩通讯地址重复情况
                if (count2 > 0) {
                    mapp.put("msg", "桩通讯地址重复，请重新填写！！");
                    map = mapp;
                    return SUCCESS;
                }

                PobChargingPileExample pileEmp3 = new PobChargingPileExample();
                PobChargingPileExample.Criteria pileCr3 = pileEmp3.createCriteria();
                pileCr3.andStationIdEqualTo(pile.getStationId());
                pileCr3.andPileNameEqualTo(pobChargingTempPileObj.getPileName());
                pileCr3.andIdNotEqualTo(pileId);
                int count3 = deviceService.countPobChargingPileByExample(pileEmp3);// 查询桩名称重复情况
                if (count3 > 0) {
                    mapp.put("msg", "桩名称重复，请重新填写！！");
                    map = mapp;
                    return SUCCESS;
                }

                /*
                 * PobChargingTempPile pobTempPile = (PobChargingTempPile) mapp.get("tempPile"); PobChargingPileExample
                 * pileEmp = new PobChargingPileExample(); PobChargingPileExample.Criteria pileCr =
                 * pileEmp.createCriteria(); pileCr.andPileCodeEqualTo(pobTempPile.getPileCode());
                 * pileCr.andIdNotEqualTo(pileId); int count1 = deviceService.countPobChargingPileByExample(pileEmp);//
                 * 查询桩编号重复情况 if ((count1) > 0) { mapp.put("msg", "桩编号重复，请重新填写！！"); map = mapp; return SUCCESS; }
                 * PobChargingPileExample pileEmp2 = new PobChargingPileExample(); PobChargingPileExample.Criteria
                 * pileCr2 = pileEmp2.createCriteria(); pileCr2.andComAddrEqualTo(pobTempPile.getComAddr());
                 * pileCr2.andIdNotEqualTo(pileId); int count3 =
                 * deviceService.countPobChargingPileByExample(pileEmp2);// 查询桩通讯地址重复情况 if ((count3) > 0) {
                 * mapp.put("msg", "桩通讯地址重复，请重新填写！！"); map = mapp; return SUCCESS; }
                 */

                if (pile != null) {
                    pobChargingTempPileObj.setUpdateTime(new Date());
                    pobChargingTempPileObj.setRealStationId(pile.getStationId());
                    pobChargingTempPileObj.setRealPileId(pile.getId());
                    mapp = deviceService.updatePile(pobChargingTempPileObj);
                    // 设置缓存里充电桩的更新时间
                    // ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_PILE_UPDATETIME,
                    // pobChargingTempPileObj.getUpdateTime());
                    // ChargingCacheUtil.setUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME,
                    // pobChargingTempPileObj.getUpdateTime());
                    // 修改桩信息操作日志
                    LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.MODIFY, getRemoteIP(), "修改充电桩信息",
                                               JSON.toJSONString(pobChargingTempPileObj));
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

    public String addPileInit() {
        if (isSubCompany(null)) {
            return MEMBER;
        }
        int stationId = this.getParamInt("stationId");
        // int isStation = this.getParamInt("isStation");
        if (stationId > 0) {
            PobChargingStationExample emp = new PobChargingStationExample();
            PobChargingStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(stationId);
            cr.andBusTypeEqualTo(UserTypeEnum.PLATFORM.getShortValue());
            PobChargingStation pobChargingStation = this.deviceService.selectPobChargingStationByExample(emp);
            if (pobChargingStation != null) {
                this.initParam(initPileParam);
                List<BusChargeRule> chargeRuleList = CacheChargeHolder.getChargeRuleList();
                this.getRequest().setAttribute("chargeRuleListJson", JsonUtil.list2json(chargeRuleList));
                this.getRequest().setAttribute("chargeRuleList", chargeRuleList);
                // if (isStation != 1) {
                // this.getRequest().setAttribute("backStationId", validStationId);
                // }
                this.getRequest().setAttribute("stationId", stationId);
                this.getRequest().setAttribute("stationToType", pobChargingStation.getStationToType());
                Calendar a = Calendar.getInstance();
                a.add(Calendar.DATE, 1);
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

    public String addPileByAjax() throws Exception {

        int stationId = this.getParamInt("stationId");
        Map<String, Object> mapp = new HashMap<String, Object>();
        if (isSubCompany(null)) {
            mapp.put("msg", "非法操作！！");
            this.map = mapp;
            return SUCCESS;
        }

        String pobChargingTempPile = this.getParameter("pobChargingTempPile");
        mapp = checkPile(pobChargingTempPile, addPile);
        if ("success".equals(mapp.get("msg"))) {
            PobChargingTempPile pobTempPile = (PobChargingTempPile) mapp.get("tempPile");

            PobChargingPileExample pileEmp4 = new PobChargingPileExample();
            PobChargingPileExample.Criteria pileCr4 = pileEmp4.createCriteria();
            pileCr4.andComAddrEqualTo(pobTempPile.getComAddr());
            int count4 = deviceService.countPobChargingPileByExample(pileEmp4);// 查询同一通讯地址对应抢个数
            List<SysLink> comSubAddrList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_SUB_ADDR.getValue());

            if (count4 >= comSubAddrList.size()) {
                mapp.put("msg", "通讯地址[" + pobTempPile.getComAddr() + "]下已经有" + comSubAddrList.size() + "个充电抢不能添加新充电抢！！");
                map = mapp;
                return SUCCESS;
            }

            PobChargingPileExample pileEmp = new PobChargingPileExample();
            PobChargingPileExample.Criteria pileCr = pileEmp.createCriteria();
            pileCr.andPileCodeEqualTo(pobTempPile.getPileCode());
            int count1 = deviceService.countPobChargingPileByExample(pileEmp);// 查询桩编号重复情况
            if (count1 > 0) {
                mapp.put("msg", "桩编号重复，请重新填写！！");
                map = mapp;
                return SUCCESS;
            }

            PobChargingPileExample pileEmp2 = new PobChargingPileExample();
            PobChargingPileExample.Criteria pileCr2 = pileEmp2.createCriteria();
            pileCr2.andComAddrEqualTo(pobTempPile.getComAddr());
            pileCr2.andStationIdNotEqualTo(stationId);
            int count2 = deviceService.countPobChargingPileByExample(pileEmp2);// 查询桩通讯地址重复情况
            if (count2 > 0) {
                mapp.put("msg", "桩通讯地址重复，请重新填写！！");
                map = mapp;
                return SUCCESS;
            }

            PobChargingPileExample pileEmp3 = new PobChargingPileExample();
            PobChargingPileExample.Criteria pileCr3 = pileEmp3.createCriteria();
            pileCr3.andStationIdEqualTo(stationId);
            pileCr3.andPileNameEqualTo(pobTempPile.getPileName());
            int count3 = deviceService.countPobChargingPileByExample(pileEmp3);// 查询桩名称重复情况
            if (count3 > 0) {
                mapp.put("msg", "桩名称重复，请重新填写！！");
                map = mapp;
                return SUCCESS;
            }

            PobChargingStationExample emp = new PobChargingStationExample();
            PobChargingStationExample.Criteria cr = emp.createCriteria();
            cr.andIdEqualTo(stationId);
            cr.andBusTypeEqualTo(UserTypeEnum.PLATFORM.getShortValue());
            // cr.andBusTypeEqualTo(webUser.getUsertype().ENTERPRISE.getShortValue());
            PobChargingStation pobChargingStation = deviceService.selectPobChargingStationByExample(emp);
            if (pobChargingStation != null) {
                pobTempPile.setRealStationId(stationId);
                pobTempPile.setUpdateTime(new Date());
                pobTempPile.setPileToType(pobChargingStation.getStationToType());
                try {
                    deviceService.insertAndUpdatePile(pobTempPile);
                    // 添加桩信息操作日志
                    LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.ADD, getRemoteIP(), "添加充电桩信息",
                                               JSON.toJSONString(pobTempPile));
                } catch (Exception e) {
                    mapp.put("msg", "添加桩设备失败！！");
                    e.printStackTrace();
                }
                // deviceService.insertPobChargingTempPileSelective(pobTempPile);
            } else {
                mapp.put("msg", ERROR);
            }
        }
        this.map = mapp;
        return SUCCESS;
    }

    public String pileListInit() {
        this.getRequest().setAttribute("pileTypeList", ChargePowerTypeEnum.values());
        this.getRequest().setAttribute("chaWayList", ChargeCurrentTypeEnum.values());
        this.getRequest().setAttribute("intfTypeList", ChargeInterfaceTypeEnum.values());
        this.getRequest().setAttribute("payWayList", ChargeWayEnum.values());
        this.getRequest().setAttribute("statusList", PileInfoStatusEnum.values());
        this.getRequest().setAttribute("comTypeList", CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_TYPE.getValue()));
        this.getRequest().setAttribute("pileToTypeList", UseToTypeEnum.values());

        return SUCCESS;
    }

    /**
     * 查询已审核过的充电桩
     * 
     * @throws Exception
     */
    public String queryPileList() throws Exception {
        String keyword = this.getParameter("keyword");
        String piletype = this.getParameter("piletype");
        String chaway = this.getParameter("chaway");
        String intftype = this.getParameter("intftype");
        String payway = this.getParameter("payway");
        String comtype = this.getParameter("comtype");
        String status = this.getParameter("pilestatus");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        int stationIdForSelectModal = getParamInt("stationIdForSelectModal");
        int pileToType = getParamInt("pileToType");
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotDigits(piletype, chaway, intftype, comtype, status) || StringUtil.isNotNumber(payway)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        isSubCompany(params);
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", keyword);
        }
        if (StringUtil.isDigits(piletype) && Integer.parseInt(piletype) > 0) {
            params.put("piletype", piletype);
        }
        if (StringUtil.isDigits(chaway) && Integer.parseInt(chaway) > 0) {
            params.put("chaway", chaway);
        }
        if (StringUtil.isDigits(intftype) && Integer.parseInt(intftype) > 0) {
            params.put("intftype", intftype);
        }
        if (StringUtil.isDigits(payway) && Integer.parseInt(payway) > -1) {
            params.put("payway", payway);
        }
        if (StringUtil.isDigits(comtype) && Integer.parseInt(comtype) > 0) {
            params.put("comtype", comtype);
        }
        if (StringUtil.isDigits(status) && Integer.parseInt(status) > 0) {
            params.put("status", status);
        }
        if (stationIdForSelectModal > 0) {
            params.put("stationId", stationIdForSelectModal);
        }
        if (pileToType > 0) {
            params.put("pileToType", pileToType);
        }
        if (isExportExcel()) {
            List<PobChargingPile> chargePile = deviceService.selectChargePileByPage(params);
            String[] headsName = { "电桩编码", "电桩名称", "所属充电站", "桩类型", "电桩类型", "电桩编号", "通讯协议", "通讯地址", "通讯子地址", "电桩型号", "充电方式", "充电模式", "支付方式", "是否支持远程控制", "是否支持负荷调度", "是否支持预约", "状态",
                    "更新时间", "安装时间", "详细地址", };
            String[] properiesName = { "id", "pileName", "stationName", "pileToTypeDesc", "pileTypeDesc", "pileCode", "comTypeDesc", "comAddr", "comSubAddr", "pileModelDesc",
                    "chaWayDesc", "chaModelDesc", "payWayDesc", "isControlDesc", "isChaLoadDesc", "isAppDesc", "statusDesc", "updateTimeStr", "buildTimeStr", "address" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(chargePile, properiesName, headsName, PobChargingPile.class);
            return null;
        } else {
            if (StringUtil.isNotDigits(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);

            List<PobChargingPile> chargePile = deviceService.selectChargePileByPage(params);
            page.setRoot(chargePile);
            this.page = page;
            return SUCCESS;
        }

    }

    /**
     * 充电桩详细内容初始化
     * 
     * @return
     */
    public String pileDetailInit() {
        String id = this.getParameter("id");
        PobChargingPile chargePile = deviceService.selectPileByPrimaryKey(Integer.valueOf(id));
        if (chargePile == null) return SUCCESS;
        PobChargingStationExample emp = new PobChargingStationExample();
        PobChargingStationExample.Criteria cr = emp.createCriteria();
        cr.andIdEqualTo(chargePile.getStationId());
        cr.andBusTypeEqualTo(UserTypeEnum.PLATFORM.getShortValue());
        PobChargingStation pobChargingStation = this.deviceService.selectPobChargingStationByExample(emp);
        // 查询费用信息：正在使用的费用规则和待激活的费用规则
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pileid", chargePile.getId());
        params.put("status", WhetherEnum.YES.getShortValue());// 有效
        params.put("isactive", WhetherEnum.YES.getShortValue());// 已激活
        BusPileChargerule activeRule = deviceService.selectPileChargeRuleByParams(params);
        this.getRequest().setAttribute("activeRule", activeRule);

        params.put("isactive", WhetherEnum.NO.getShortValue());// 未激活
        BusPileChargerule unactiveRule = deviceService.selectPileChargeRuleByParams(params);
        this.getRequest().setAttribute("unactiveRule", unactiveRule);
        this.getRequest().setAttribute("stationToType", pobChargingStation.getStationToType());
        setPileFieldName(chargePile);
        this.getRequest().setAttribute("chargePile", chargePile);
        return SUCCESS;
    }

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
     * 删除终端设备
     * 
     * @return
     */
    public String delPile() {
        int pileId = getParamInt("pileId");
        this.message = SUCCESS;
        if (pileId > 0) {
            PileRunStatusModel runPile = CacheChargeHolder.getPileStatusById(pileId);
            if (PileRunStatusEnum.OFFLINE.getShortValue().equals(runPile.getStatus())) {
                // int result = deviceService.deletePileByPrimaryKey(pileId);
                int result = deviceService.deleteAndUpdatePile(pileId);
                if (result > 0) {
                    // 插入日志
                    LogUtil.recordOperationLog(NumberUtils.toInt(getSessionBmsUserId()), LogTypeEnum.RUN_RECORD, LogOperatorEnum.DELETE, getRemoteIP(), "删除充电桩",
                                               JSON.toJSONString(runPile));
                } else {
                    this.message = "删除设备失败！";
                }
            } else {
                this.message = "未离线设备不可删除！";
            }

        } else {
            this.message = "请选择终端设备！";
        }
        return SUCCESS;
    }

    /**
     * 设置chargePile的描述信息
     * 
     * @param chargePile
     * @return
     */
    private void setPileFieldName(PobChargingPile chargePile) {
        if (chargePile == null) return;
        // 充电点名称
        PobChargingStation station = CacheChargeHolder.getChargeStationById(chargePile.getStationId());
        if (station != null) {
            chargePile.setStationName(station.getStationName());
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
            getRequest().setAttribute("stationToTypeList", UseToTypeEnum.values());
        }
        // pile
        else if (initPileParam.equals(initParamType)) {
            // 充电桩类型：交流，直流
            List<SysLink> chaWayList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.CHA_WAY.getValue());
            // 是否类型：是，否
            List<SysLink> isOrNoList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.IS_NO.getValue());
            // 通讯协议：南网，创锐
            List<SysLink> comTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_TYPE.getValue());
            // 充电功率类型：快，慢
            List<SysLink> pileTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.PILE_TYPE.getValue());
            // 支付方式：充电卡，人工
            List<SysLink> payWayList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.PAY_WAY.getValue());
            // 子通讯地址
            List<SysLink> comSubAddrList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_SUB_ADDR.getValue());
            Collections.sort(comSubAddrList, new Comparator<SysLink>() {

                @Override
                public int compare(SysLink o1, SysLink o2) {
                    return CompareToBuilder.reflectionCompare(o1.getValue(), o2.getValue());
                }

            });
            // 电桩型号
            List<BusPileModel> busPileModelList = CacheChargeHolder.getPileModelList();
            getRequest().setAttribute("chaWayList", chaWayList);
            getRequest().setAttribute("isOrNoList", isOrNoList);
            getRequest().setAttribute("comTypeList", comTypeList);
            getRequest().setAttribute("pileTypeList", pileTypeList);
            getRequest().setAttribute("payWayList", payWayList);
            getRequest().setAttribute("busPileModelList", busPileModelList);
            getRequest().setAttribute("comSubAddrList", comSubAddrList);
            getRequest().setAttribute("pileToTypeList", UseToTypeEnum.values());
        }

    }

    private Map<String, Object> checkPile(String pobChargingTempPile, String actionType) {
        Map<String, Object> map = new HashMap<String, Object>();
        String msg = "success";
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

            if (addPile.equals(actionType)) {
                if (feeRule <= 0) {
                    msg = "请选择收费规则！！";
                } else if (parkFee == null) {
                    msg = "请输入停车费金额！！";
                } else if (serviceFee == null) {
                    msg = "请输入停车费金额！！";
                } else if (tempPile.getActiveTime() == null) {
                    msg = "请设置费用规则启用时间！！";
                }
            } else if (editPile.equals(actionType)) {
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

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
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

    public Map<String, Object> getMap() {
        return map;
    }

}
