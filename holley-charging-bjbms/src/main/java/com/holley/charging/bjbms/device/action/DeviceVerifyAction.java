package com.holley.charging.bjbms.device.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusChargeRule;
import com.holley.charging.model.bus.BusPileChargerule;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingTempPile;
import com.holley.charging.model.pob.PobChargingTempStation;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.DeviceVerifyStatusEnum;
import com.holley.common.constants.charge.NoticeTypeEnum;
import com.holley.common.constants.charge.RequestTypeEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.MsgUtil;

/**
 * 充电点、充电桩审核
 * 
 * @author zdd
 */
public class DeviceVerifyAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(DeviceVerifyAction.class);
    private static final long   serialVersionUID = 1L;
    private DeviceService       deviceService;
    private AccountService      accountService;
    private UserService         userService;
    private Page                page;

    public String init() {
        List<DeviceVerifyStatusEnum> verifyStatusList = DeviceVerifyStatusEnum.getVerifyStatusList();
        this.getRequest().setAttribute("verifyStatusList", verifyStatusList);
        this.getRequest().setAttribute("stationVerifyStatus", 0);
        this.getRequest().setAttribute("pileVerifyStatus", 0);
        return SUCCESS;
    }

    public String stationVerifyListInit() {

        List<DeviceVerifyStatusEnum> verifyStatusList = DeviceVerifyStatusEnum.getVerifyStatusList();
        this.getRequest().setAttribute("verifyStatusList", verifyStatusList);
        this.getRequest().setAttribute("stationVerifyStatus", 0);
        return SUCCESS;
    }

    public String pileVerifyListInit() {
        List<DeviceVerifyStatusEnum> verifyStatusList = DeviceVerifyStatusEnum.getVerifyStatusList();
        this.getRequest().setAttribute("verifyStatusList", verifyStatusList);
        this.getRequest().setAttribute("pileVerifyStatus", 0);
        return SUCCESS;
    }

    /**
     * 充电点审核初始化
     * 
     * @return
     */
    public String stationVerifyInit() {
        String id = this.getParameter("id");
        PobChargingTempStation tempStation = deviceService.selectTempStationByPrimaryKey(Integer.valueOf(id));
        if (tempStation == null) return SUCCESS;
        // 更新审核状态为审核中
        if (tempStation.getValidStatus().intValue() == DeviceVerifyStatusEnum.UNVERIFIED.getValue()) {
            PobChargingTempStation record = new PobChargingTempStation();
            record.setId(Integer.valueOf(id));
            record.setValidStatus(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
            deviceService.updatePobChargingTempStationByPrimaryKeySelective(record);
            tempStation.setValidStatus(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
        }
        setTempStationFieldName(tempStation);
        this.getRequest().setAttribute("tempStation", tempStation);
        this.getRequest().setAttribute("verifyStatusList", DeviceVerifyStatusEnum.getVerifyResultList());
        this.getRequest().setAttribute("noticeTypeList", NoticeTypeEnum.getMessageAndSms());
        return SUCCESS;
    }

    /**
     * 充电点审核
     * 
     * @return
     */
    public String stationVerify() {
        String id = this.getParameter("id");
        String validRemark = this.getParameter("validremark");
        String validStatus = this.getParameter("validstatus");
        String noticetypes = this.getParameter("noticetypes");
        if (StringUtil.isEmpty(id) || StringUtil.isEmpty(validStatus)) {
            this.success = false;
            this.message = "id和审核状态不能为空";
            return SUCCESS;
        }
        PobChargingTempStation tempStation = deviceService.selectTempStationByPrimaryKey(Integer.valueOf(id));
        if (tempStation == null) {
            this.success = false;
            this.message = "充点电不存在";
            return SUCCESS;
        }

        if (tempStation.getValidStatus() != null) {
            DeviceVerifyStatusEnum oldStatus = DeviceVerifyStatusEnum.getEnmuByValue(tempStation.getValidStatus().intValue());
            if (DeviceVerifyStatusEnum.UNVERIFIED != oldStatus && DeviceVerifyStatusEnum.VERIFYING != oldStatus) {
                this.success = false;
                this.message = "充电点已审核，不能重复审核";
                return SUCCESS;
            }
        }

        DeviceVerifyStatusEnum statusEnum = DeviceVerifyStatusEnum.getEnmuByValue(Integer.valueOf(validStatus));
        if (statusEnum == null) {
            this.success = false;
            this.message = "审核状态不符合";
            return SUCCESS;
        }
        if (DeviceVerifyStatusEnum.FAILED == statusEnum) {
            if (StringUtil.isNotEmpty(validRemark)) {
                tempStation.setValidRemark(validRemark);
            } else {
                this.success = false;
                this.message = "请填写审核失败原因！";
                return SUCCESS;
            }
        }

        PobChargingStation station = getFromTempStation(tempStation);
        String msg = "";
        try {
            msg = deviceService.insertOrUpdateChargingStation(tempStation, station, statusEnum, getDataPath());
        } catch (Exception e) {
            msg = "必须上传图片类型文件!";
            e.printStackTrace();
        }
        if ("success".equals(msg)) {
            this.success = true;
            // 站内信通知审核结果
            sendStationVerifyResult(tempStation, statusEnum, validRemark, noticetypes);
        } else {
            this.success = false;
            this.message = msg;
        }
        return SUCCESS;
    }

    /**
     * 审核充电点详细初始化
     * 
     * @return
     */
    public String stationVerifyDetailInit() {
        String id = this.getParameter("id");
        PobChargingTempStation tempStation = deviceService.selectTempStationByPrimaryKey(Integer.valueOf(id));
        if (tempStation == null) return SUCCESS;
        setTempStationFieldName(tempStation);
        this.getRequest().setAttribute("tempStation", tempStation);
        return SUCCESS;
    }

    /**
     * 查询充电点审核记录
     * 
     * @return
     */
    public String queryStationList() {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String verifystatus = this.getParameter("verifystatus");
        String stationname = this.getParameter("stationname");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        Map<String, Object> params = new HashMap<String, Object>();

        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startDate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("endDate", DateUtil.ShortStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(verifystatus) && !verifystatus.equals("0")) {
            params.put("validStatus", Short.valueOf(verifystatus));
        }
        if (StringUtil.isNotEmpty(stationname)) {
            params.put("stationName", stationname);
        }

        int pageIndex = 1;
        int pageLimit = Globals.PAGE_LIMIT;
        if (StringUtil.isNotEmpty(pageindex)) {
            pageIndex = Integer.valueOf(pageindex);
        }
        if (StringUtil.isNotEmpty(pagelimit)) {
            pageLimit = Integer.valueOf(pagelimit);
        }
        Page page = this.returnPage(pageIndex, pageLimit);
        params.put(Globals.PAGE, page);

        List<PobChargingTempStation> stationList = deviceService.selectTempStationVerifyByPage(params);
        page.setRoot(stationList);
        this.page = page;
        return SUCCESS;
    }

    /**
     * 充电桩审核初始化
     * 
     * @return
     */
    public String pileVerifyInit() {
        String id = this.getParameter("id");
        PobChargingTempPile tempPile = deviceService.selectTempPileByPrimaryKey(Integer.valueOf(id));
        if (tempPile == null) return SUCCESS;
        // 更新审核状态为审核中
        if (tempPile.getValidStatus().intValue() == DeviceVerifyStatusEnum.UNVERIFIED.getValue()) {
            PobChargingTempPile record = new PobChargingTempPile();
            record.setId(Integer.valueOf(id));
            record.setValidStatus(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
            deviceService.updateChargingTempPileByPKSelective(record);
            tempPile.setValidStatus(DeviceVerifyStatusEnum.VERIFYING.getShortValue());
        }
        // 修改充电桩信息，显示充电桩的正在使用的费用规则和待激活的费用规则
        if (RequestTypeEnum.EDITPILE.getShortValue().equals(tempPile.getRequestType())) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("pileid", tempPile.getRealPileId());
            params.put("status", WhetherEnum.YES.getShortValue());// 有效
            params.put("isactive", WhetherEnum.YES.getShortValue());// 已激活
            BusPileChargerule activeRule = deviceService.selectPileChargeRuleByParams(params);
            this.getRequest().setAttribute("activeRule", activeRule);

            params.put("isactive", WhetherEnum.NO.getShortValue());// 未激活
            BusPileChargerule unactiveRule = deviceService.selectPileChargeRuleByParams(params);
            this.getRequest().setAttribute("unactiveRule", unactiveRule);

        }
        setTempPileFieldName(tempPile);
        this.getRequest().setAttribute("tempPile", tempPile);
        this.getRequest().setAttribute("verifyStatusList", DeviceVerifyStatusEnum.getVerifyResultList());
        this.getRequest().setAttribute("noticeTypeList", NoticeTypeEnum.getMessageAndSms());
        return SUCCESS;
    }

    /**
     * 充电桩审核
     * 
     * @return
     */
    public String pileVerify() {
        String id = this.getParameter("id");
        String validremark = this.getParameter("validremark");
        String validstatus = this.getParameter("validstatus");
        String noticetypes = this.getParameter("noticetypes");
        if (StringUtil.isEmpty(id) || StringUtil.isEmpty(validstatus)) {
            this.success = false;
            this.message = "id和审核状态不能为空";
            return SUCCESS;
        }
        PobChargingTempPile tempPile = deviceService.selectTempPileByPrimaryKey(Integer.valueOf(id));
        if (tempPile == null) {
            this.success = false;
            this.message = "充点桩不存在";
            return SUCCESS;
        }

        if (tempPile.getValidStatus() != null) {
            DeviceVerifyStatusEnum oldStatus = DeviceVerifyStatusEnum.getEnmuByValue(tempPile.getValidStatus().intValue());
            if (DeviceVerifyStatusEnum.UNVERIFIED != oldStatus && DeviceVerifyStatusEnum.VERIFYING != oldStatus) {
                this.success = false;
                this.message = "充电桩已审核，不能重复审核";
                return SUCCESS;
            }
        }

        DeviceVerifyStatusEnum statusEnum = DeviceVerifyStatusEnum.getEnmuByValue(Integer.valueOf(validstatus));
        if (statusEnum == null) {
            this.success = false;
            this.message = "审核状态不符合";
            return SUCCESS;
        }
        if (tempPile.getRequestType() == null || RequestTypeEnum.getEnmuByValue(tempPile.getRequestType().intValue()) == null) {
            this.success = false;
            this.message = "用户操作类型不符合";
            return SUCCESS;
        }
        // 更新表pob_charging_temp_pile的审核信息
        PobChargingTempPile record = new PobChargingTempPile();
        record.setId(tempPile.getId());
        record.setValidStatus(statusEnum.getShortValue());
        record.setValidTime(new Date());
        record.setRequestType(tempPile.getRequestType());

        if (DeviceVerifyStatusEnum.FAILED == statusEnum) {
            if (StringUtil.isNotEmpty(validremark)) {
                record.setValidRemark(validremark);
            } else {
                this.success = false;
                this.message = "请填写审核失败原因！";
                return SUCCESS;
            }
        }
        PobChargingPile pile = getFromTempPile(tempPile);

        BusPileChargerule rule = null;
        if (tempPile.getFeeRule() == null || tempPile.getFeeRule() <= 0) {
            if (RequestTypeEnum.EDITPILE.getShortValue().equals(tempPile.getRequestType())) {
                rule = null;
            } else {
                this.success = false;
                this.message = "费用规则为空";
                return SUCCESS;
            }
        } else {
            rule = getRuleFromTempPile(tempPile);
        }
        boolean result = false;
        try {
            result = deviceService.insertOrUpdateChargingPile(record, pile, rule, statusEnum);
        } catch (Exception e) {
            this.success = false;
            this.message = "审核失败.";
            e.printStackTrace();
        }
        if (result) {
            record.setRealStationId(tempPile.getRealStationId());
            record.setPileName(tempPile.getPileName());
            sendPileVerifyResult(record, noticetypes);
        }
        return SUCCESS;
    }

    /**
     * 审核充电桩详细初始化
     * 
     * @return
     */
    public String pileVerifyDetailInit() {
        String id = this.getParameter("id");
        PobChargingTempPile tempPile = deviceService.selectTempPileByPrimaryKey(Integer.valueOf(id));
        if (tempPile == null) return SUCCESS;
        // 修改充电桩信息，显示充电桩的正在使用的费用规则和待激活的费用规则
        if (RequestTypeEnum.EDITPILE.getShortValue().equals(tempPile.getRequestType())) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("pileid", tempPile.getRealPileId());
            params.put("status", WhetherEnum.YES.getShortValue());// 有效
            params.put("isactive", WhetherEnum.YES.getShortValue());// 已激活
            BusPileChargerule activeRule = deviceService.selectPileChargeRuleByParams(params);
            this.getRequest().setAttribute("activeRule", activeRule);

            params.put("isactive", WhetherEnum.NO.getShortValue());// 未激活
            BusPileChargerule unactiveRule = deviceService.selectPileChargeRuleByParams(params);
            this.getRequest().setAttribute("unactiveRule", unactiveRule);
        }
        setTempPileFieldName(tempPile);
        this.getRequest().setAttribute("tempPile", tempPile);
        return SUCCESS;
    }

    /**
     * 查询充电桩审核记录
     * 
     * @return
     */
    public String queryPileList() {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String verifystatus = this.getParameter("verifystatus");
        String keyword = this.getParameter("keyword");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        Map<String, Object> params = new HashMap<String, Object>();

        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startDate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("endDate", DateUtil.ShortStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(verifystatus) && !verifystatus.equals("0")) {
            params.put("validStatus", Short.valueOf(verifystatus));
        }
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", keyword);
        }

        int pageIndex = 1;
        int pageLimit = Globals.PAGE_LIMIT;
        if (StringUtil.isNotEmpty(pageindex)) {
            pageIndex = Integer.valueOf(pageindex);
        }
        if (StringUtil.isNotEmpty(pagelimit)) {
            pageLimit = Integer.valueOf(pagelimit);
        }
        Page page = this.returnPage(pageIndex, pageLimit);
        params.put(Globals.PAGE, page);
        List<PobChargingTempPile> pileList = deviceService.selectTempPileVerifyByPage(params);
        page.setRoot(pileList);
        this.page = page;
        return SUCCESS;
    }

    private PobChargingStation getFromTempStation(PobChargingTempStation tempStation) {
        PobChargingStation station = new PobChargingStation();
        if (tempStation.getRequestType().intValue() == RequestTypeEnum.EDITSTATION.getValue() && tempStation.getRealStationId() != null) {
            station.setId(tempStation.getRealStationId());
        }
        station.setAddress(tempStation.getAddress());
        station.setBusMec(tempStation.getBusMec());
        station.setBusType(tempStation.getBusType());
        station.setCity(tempStation.getCity());
        station.setDataSource(tempStation.getDataSource());
        // station.setFastNum(tempStation.getFastNum());
        // station.setImg(tempStation.getImg());
        station.setIsParkFee(tempStation.getIsParkFee());
        station.setIsShow(tempStation.getIsShow());
        station.setIsValidate(WhetherEnum.YES.getShortValue());
        station.setLat(tempStation.getLat());
        station.setLinkMan(tempStation.getLinkMan());
        station.setLinkPhone(tempStation.getLinkPhone());
        station.setLng(tempStation.getLng());
        station.setOpenDay(tempStation.getOpenDay());
        station.setOpenTime(tempStation.getOpenTime());
        station.setOperatTime(tempStation.getOperatTime());
        station.setParkType(tempStation.getParkType());
        station.setProvince(tempStation.getProvince());
        station.setRemark(tempStation.getRemark());
        // station.setScore(tempStation.getScore());
        // station.setSlowNum(tempStation.getSlowNum());
        station.setStationName(tempStation.getStationName());
        station.setUpdateTime(new Date());
        return station;
    }

    private PobChargingPile getFromTempPile(PobChargingTempPile tempPile) {
        PobChargingPile pile = new PobChargingPile();
        if (RequestTypeEnum.EDITPILE.getShortValue().equals(tempPile.getRequestType()) && tempPile.getRealPileId() != null) {
            pile.setId(tempPile.getRealPileId());
        }
        pile.setAddress(tempPile.getAddress());
        pile.setBuildTime(tempPile.getBuildTime());
        pile.setChargeFee(tempPile.getChargeFee());
        pile.setChaWay(tempPile.getChaWay());
        pile.setComAddr(tempPile.getComAddr());
        pile.setComSubAddr(tempPile.getComSubAddr());
        pile.setComType(tempPile.getComType());
        pile.setFeeRule(tempPile.getFeeRule());
        pile.setHardVersion(tempPile.getHardVersion());
        pile.setIntfType(tempPile.getIntfType());
        pile.setIsApp(tempPile.getIsApp());
        pile.setIsChaLoad(tempPile.getIsChaLoad());
        pile.setIsControl(tempPile.getIsControl());
        pile.setIsFee(tempPile.getIsFee());
        pile.setIsLock(tempPile.getIsLock());
        pile.setIsMoneyCha(tempPile.getIsMoneyCha());
        pile.setIsRationCha(tempPile.getIsRationCha());
        pile.setIsTimeCha(tempPile.getIsTimeCha());
        pile.setLockCode(tempPile.getLockCode());
        pile.setParkFee(tempPile.getParkFee());
        pile.setPayWay(tempPile.getPayWay());
        pile.setPileCode(tempPile.getPileCode());
        pile.setPileModel(tempPile.getPileModel());
        pile.setPileName(tempPile.getPileName());
        pile.setPileType(tempPile.getPileType());
        pile.setServiceFee(tempPile.getServiceFee());
        pile.setSoftVersion(tempPile.getSoftVersion());
        pile.setStationId(tempPile.getRealStationId());
        pile.setStatus(tempPile.getStatus());
        pile.setUpdateTime(new Date());
        return pile;
    }

    private BusPileChargerule getRuleFromTempPile(PobChargingTempPile tempPile) {
        BusPileChargerule rule = new BusPileChargerule();
        if (tempPile.getRequestType().intValue() == RequestTypeEnum.EDITPILE.getValue() && tempPile.getRealPileId() != null) {
            rule.setPileId(tempPile.getRealPileId());
        }
        rule.setActiveTime(tempPile.getActiveTime());
        rule.setAddTime(new Date());
        rule.setChargeFee(tempPile.getChargeFee());
        rule.setChargeruleId(tempPile.getFeeRule());
        rule.setParkFee(tempPile.getParkFee());
        rule.setServiceFee(tempPile.getServiceFee());
        rule.setStatus(WhetherEnum.YES.getShortValue());// 生效
        return rule;
    }

    /**
     * 设置tempStation描述信息
     * 
     * @param tempStation
     */
    private void setTempStationFieldName(PobChargingTempStation tempStation) {
        if (tempStation == null) return;

        if (tempStation.getBusType() != null && tempStation.getBusMec() != null) {
            if (UserTypeEnum.ENTERPRISE.getValue() == tempStation.getBusType().intValue()) {
                BusBussinessInfo busInfo = accountService.selectBusBussinessInfoByPrimaryKey(tempStation.getBusMec());
                tempStation.setBusMecName(busInfo == null ? "" : busInfo.getBusName());
            } else if (UserTypeEnum.PERSON.getValue() == tempStation.getBusType().intValue()) {
                BusUserInfo userInfo = accountService.selectUserInfoByPrimaryKey(tempStation.getBusMec());
                tempStation.setBusMecName(userInfo == null ? "" : userInfo.getRealName());
            }
        }
    }

    /**
     * 设置tempStation描述信息
     * 
     * @param tempStation
     */
    private void setTempPileFieldName(PobChargingTempPile tempPile) {
        // 所属充电站
        if (tempPile.getRealStationId() != null) {
            PobChargingStation station = CacheChargeHolder.getChargeStationById(tempPile.getRealStationId());
            tempPile.setRealStationName(station == null ? "" : station.getStationName());
        }

        // 计费规则
        if (tempPile.getFeeRule() != null) {
            BusChargeRule rule = CacheChargeHolder.getChargeRuleById(tempPile.getFeeRule());
            if (rule != null) {
                tempPile.setFeeRuleDesc(rule.getName());
                String ruleDetail = "";
                if (rule.getJianFee() != null) {
                    ruleDetail += "尖: " + rule.getJianFee() + " 元/度; ";
                }
                if (rule.getFengFee() != null) {
                    ruleDetail += "峰: " + rule.getFengFee() + " 元/度; ";
                }
                if (rule.getPingFee() != null) {
                    ruleDetail += "平: " + rule.getPingFee() + " 元/度; ";
                }
                if (rule.getGuFee() != null) {
                    ruleDetail += "谷: " + rule.getGuFee() + " 元/度;";
                }
                tempPile.setFeeRuleDetail(ruleDetail);
            }
        }
    }

    private void sendStationVerifyResult(PobChargingTempStation tempStation, DeviceVerifyStatusEnum statusEnum, String validRemark, String noticetypes) {
        if (StringUtil.isEmpty(noticetypes)) {
            return;
        }
        String[] noticetypeArray = noticetypes.split(",");
        NoticeTypeEnum typeenum;
        String result = "";

        if (DeviceVerifyStatusEnum.FAILED == statusEnum) {
            if (StringUtil.isNotEmpty(validRemark)) {
                result = "不通过（原因：" + validRemark + "）";
            } else {
                result = "不通过";
            }
        } else {
            result = "通过";
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("usertype", tempStation.getBusType());
        params.put("infoid", tempStation.getBusMec());
        BusUser busUser = userService.selectUserByInfo(params);
        if (busUser == null) {
            return;
        }
        for (String type : noticetypeArray) {
            typeenum = NoticeTypeEnum.getEnmuByValue(Integer.parseInt(type));
            if (NoticeTypeEnum.MESSAGE == typeenum) {// 站内信发送
                String content = "尊敬的用户：您好！充电点（" + tempStation.getStationName() + "）审核" + result + "，请注意查看。";
                MsgUtil.sendMessage(busUser.getId(), content, "充电点审核");
            } else if (NoticeTypeEnum.SMS == typeenum) {// 短信发送
                MsgUtil.sendVerifySMS(busUser.getPhone(), "充电点（" + tempStation.getStationName() + "）", result);
                logger.info("-----【充电点（" + tempStation.getStationName() + "）审核，短信通知】-----phone=" + busUser.getPhone() + ",content=" + result);
            }
        }
        // if (busUser.getEmail() != null) {
        // MsgUtil.sendEmail(EmailSubjectEnum.NOTICE, content, busUser.getEmail());
        // logger.info("-----【充电点审核，邮箱通知】-----email=" + busUser.getEmail() + ",content=充电点" + result);
        // }
    }

    private void sendPileVerifyResult(PobChargingTempPile record, String noticetypes) {
        if (StringUtil.isEmpty(noticetypes)) {
            return;
        }
        String[] noticetypeArray = noticetypes.split(",");
        NoticeTypeEnum typeenum;
        String result = "";
        if (DeviceVerifyStatusEnum.FAILED.getShortValue().equals(record.getValidStatus())) {
            if (StringUtil.isNotEmpty(record.getValidRemark())) {
                result = "不通过（原因：" + record.getValidRemark() + "）";
            } else {
                result = "不通过";
            }
        } else {
            result = "通过";
        }
        PobChargingStation station = deviceService.selectChargeStationByPrimaryKey(record.getRealStationId());
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("usertype", station.getBusType());
        params.put("infoid", station.getBusMec());
        BusUser busUser = userService.selectUserByInfo(params);
        if (busUser == null) {
            return;
        }
        for (String type : noticetypeArray) {
            typeenum = NoticeTypeEnum.getEnmuByValue(Integer.parseInt(type));
            if (NoticeTypeEnum.MESSAGE == typeenum) {// 站内信发送
                String content = "尊敬的用户：您好！充电桩（" + station.getStationName() + record.getPileName() + "）审核" + result + "，请注意查看。";
                MsgUtil.sendMessage(busUser.getId(), content, "充电桩审核");
            } else if (NoticeTypeEnum.SMS == typeenum) {// 短信发送
                MsgUtil.sendVerifySMS(busUser.getPhone(), "充电桩（" + station.getStationName() + record.getPileName() + "）", result);
                logger.info("-----【充电桩（" + station.getStationName() + record.getPileName() + "）审核，短信通知】-----phone=" + busUser.getPhone() + ",content=" + result);
            }
        }

    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
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

}
