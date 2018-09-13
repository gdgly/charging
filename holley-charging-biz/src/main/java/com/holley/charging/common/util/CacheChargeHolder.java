package com.holley.charging.common.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.holley.charging.model.bus.BusChargeRule;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.bus.BusPileModelExample;
import com.holley.charging.model.bus.BusRepairPoint;
import com.holley.charging.model.bus.BusRepairPointExample;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.def.PileRunStatusModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingPileExample;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.charging.service.website.PobObjectService;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.BussinessCacheUtil;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.charge.ChargeCurrentTypeEnum;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.PileAppStatusEnum;
import com.holley.common.constants.charge.PileModelStatusEnum;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.util.ObjectUtil;
import com.holley.common.util.SerializeCoderUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;

/**
 * 缓存服务
 * 
 * @author zdd
 */
public class CacheChargeHolder {

    private final static Logger                        logger           = Logger.getLogger(CacheChargeHolder.class);
    private static PobObjectService                    pobObjectService;
    private static DeviceService                       deviceService;
    private static UserService                         userService;

    private static Map<Integer, PobChargingStation>    chargeStationMap = new HashMap<Integer, PobChargingStation>();
    private static Date                                stationUpdateTime;
    private static Map<Integer, List<PobChargingPile>> chargePileMap    = new HashMap<Integer, List<PobChargingPile>>();
    private static Date                                pileUpdateTime;
    private static List<BusChargeRule>                 chargeRuleList;
    private static List<BusPileModel>                  pileModelList    = new ArrayList<BusPileModel>();
    private static Date                                pileModelUpdateTime;

    private static Map<Integer, BusRepairPoint>        repairPointMap   = new HashMap<Integer, BusRepairPoint>();
    private static Date                                repairPointUpdateTime;

    public enum PileRunStatusEnum {
        UNKNOW(0, "忙碌中"), IDLE(1, "空闲"), CHARGING(2, "充电中"), APPOINTMENT(3, "预约中"), OFFLINE(4, "离线"), FAULT(5, "故障");

        private final int    value;
        private final String text;

        PileRunStatusEnum(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public static List<PileRunStatusEnum> getPileRunStatusList() {
            List<PileRunStatusEnum> list = new ArrayList<PileRunStatusEnum>();
            list.add(PileRunStatusEnum.UNKNOW);
            list.add(PileRunStatusEnum.IDLE);
            list.add(PileRunStatusEnum.CHARGING);
            list.add(PileRunStatusEnum.OFFLINE);
            list.add(PileRunStatusEnum.FAULT);
            return list;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

        public static String getText(int value) {
            PileRunStatusEnum task = getEnmuByValue(value);
            return task == null ? null : task.getText();
        }

        public Short getShortValue() {
            Integer obj = value;
            return obj.shortValue();
        }

        /**
         * 通过传入的值匹配枚举
         * 
         * @param value
         * @return
         */
        public static PileRunStatusEnum getEnmuByValue(int value) {
            for (PileRunStatusEnum record : PileRunStatusEnum.values()) {
                if (value == record.getValue()) {
                    return record;
                }
            }
            return null;
        }
    }

    public static void init() {
        initChargeStation();
        initChargePile();
        initChargeRule();
        initPileModel();
        initRepairPoint();
    }

    // --------------------------------------------充电点-----------------------------------------
    /**
     * 初始化充电点缓存数据
     */
    public static void initOnlineDevice(int infoId) {
        byte[] value = BussinessCacheUtil.getOlineDeviceList(String.valueOf(infoId));
        if (value == null) {
            List<PobChargingPile> tempList = new ArrayList<PobChargingPile>();
            List<PobChargingPile> tempList2;
            List<PobChargingStation> stationList = getChargeStationListByMecAndTypeData(infoId, UserTypeEnum.ENTERPRISE.getShortValue());
            for (PobChargingStation station : stationList) {
                tempList2 = getChargePileListByStationid(station.getId());
                if (tempList2 != null && tempList2.size() > 0) {
                    tempList.addAll(tempList2);
                }
            }

            BussinessCacheUtil.setOlineDeviceList(String.valueOf(infoId), SerializeCoderUtil.serializeList(tempList));

        }
    }

    /**
     * 初始化充电点缓存数据
     */
    public static void initChargeStation() {
        List<PobChargingStation> list = pobObjectService.selectChargingStationByExample(null);
        Date tempDate = null;
        for (PobChargingStation record : list) {
            chargeStationMap.put(record.getId(), record);
            if (record.getUpdateTime() == null) continue;
            if (tempDate == null) {
                tempDate = record.getUpdateTime();
            } else if (tempDate.compareTo(record.getUpdateTime()) < 0) {
                tempDate = record.getUpdateTime();
            }
        }
        if (tempDate == null) {
            stationUpdateTime = new Date();
        } else {
            stationUpdateTime = tempDate;
        }
    }

    /**
     * 增量更新充电点缓存数据
     */
    public static synchronized void reloadChargeStation() {
        Date updateTime = ChargingCacheUtil.getUpdateTime(CacheKeyProvide.KEY_STATION_UPDATETIME);
        if (stationUpdateTime == null || updateTime == null) return;
        if (stationUpdateTime.getTime() == updateTime.getTime()) {
            return;
        }
        PobChargingStationExample emp = new PobChargingStationExample();
        PobChargingStationExample.Criteria cr = emp.createCriteria();
        cr.andUpdateTimeGreaterThan(stationUpdateTime);
        List<PobChargingStation> list = pobObjectService.selectChargingStationByExample(emp);
        // Date tempDate = stationUpdateTime;
        for (PobChargingStation record : list) {
            chargeStationMap.put(record.getId(), record);
            /*
             * if (record.getUpdateTime() == null) continue; if (tempDate == null) { tempDate = record.getUpdateTime();
             * } else if (tempDate.compareTo(record.getUpdateTime()) < 0) { tempDate = record.getUpdateTime(); }
             */
        }
        stationUpdateTime = updateTime;
    }

    public static void deleteChargeStationById(Integer stationid) {
        if (chargeStationMap.containsKey(stationid)) {
            chargeStationMap.remove(stationid);
        }
    }

    /**
     * 更新单个充电点缓存信息，主要是给充电桩审核通过以后，更新缓存里的快充桩、慢充桩数量信息用的
     * 
     * @param stationid
     */
    public static void reloadChargeStationById(Integer stationid) {
        if (stationid == null) return;
        PobChargingStation record = pobObjectService.selectChargingStationByPK(stationid);
        if (record == null) return;
        chargeStationMap.put(record.getId(), record);
    }

    /**
     * 清空充电点缓存数据
     */
    public static void clearChargeStation() {
        if (chargeStationMap != null) {
            chargeStationMap.clear();
        }
    }

    /**
     * 获取可显示充电点List
     */
    public static List<PobChargingStation> getChargeStationList() {
        reloadChargeStation();
        if (chargeStationMap == null || chargeStationMap.size() == 0) return null;
        Collection<PobChargingStation> csCollection = chargeStationMap.values();
        List<PobChargingStation> list = new ArrayList<PobChargingStation>();
        int pileNum = 0;
        for (PobChargingStation record : csCollection) {
            pileNum = (record.getFastNum() == null ? 0 : record.getFastNum()) + (record.getSlowNum() == null ? 0 : record.getSlowNum());
            if (record.getIsShow().intValue() == WhetherEnum.YES.getValue() && pileNum > 0) {
                list.add(record);
            }
        }
        return list;
    }

    public static PobChargingStation getChargeStationById(Integer id) {
        if (id == null) return null;
        reloadChargeStation();
        return chargeStationMap.get(id);
    }

    /**
     * 根据运营类型和运营信息ID获取充电点信息
     * 
     * @param busMec
     * @param busType
     * @return
     */
    private static List<PobChargingStation> getChargeStationListByMecAndTypeData(Integer busMec, Short busType) {
        reloadChargeStation();
        if (chargeStationMap == null || chargeStationMap.size() == 0) return null;
        Collection<PobChargingStation> csCollection = chargeStationMap.values();
        List<PobChargingStation> list = new ArrayList<PobChargingStation>();
        for (PobChargingStation record : csCollection) {
            if (record.getIsShow().intValue() == WhetherEnum.YES.getValue()) {
                if (UserTypeEnum.PLATFORM.getShortValue().equals(busType)) {
                    list.add(record);
                } else if (busMec != null && busMec > 0 && UserTypeEnum.COMPANY.getShortValue().equals(busType)) {
                    if (record.getBusMec().equals(busMec)) {
                        list.add(record);
                    }
                } else if (busMec != null && busMec > 0 && busType != null && busType > 0) {
                    if (record.getBusMec().equals(busMec) && record.getBusType().equals(busType)) {
                        list.add(record);
                    }
                }
            }

        }
        return list;
    }

    public static List<PobChargingStation> getChargeStationListByMecAndType(Integer busMec, Short busType) {

        return getChargeStationListByMecAndTypeData(busMec, busType);

    }

    /**
     * 根据地图边界范围获取充电点
     * 
     * @param swlng
     * @param swlat
     * @param nelng
     * @param nelat
     * @return
     */
    public static List<PobChargingStation> getStationByRange(double swlng, double swlat, double nelng, double nelat) {
        List<PobChargingStation> list = getChargeStationList();
        if (list == null || list.size() == 0) return null;
        List<PobChargingStation> resultList = new ArrayList<PobChargingStation>();
        double lng;
        double lat;
        for (PobChargingStation record : list) {
            lng = StringUtil.parseDouble(record.getLng());
            lat = StringUtil.parseDouble(record.getLat());
            if (lng >= swlng && lng <= nelng && lat >= swlat && lat <= nelat) {
                resultList.add(record);
            }
        }
        return resultList;
    }

    /**
     * 统计充电点下空闲桩数量
     * 
     * @param stationid
     * @return
     */
    public static int getPileIdleNumByStationid(Integer stationid) {
        List<PobChargingPile> pileList = getChargePileListByStationid(stationid);
        if (pileList == null || pileList.size() == 0) return 0;
        PileStatusBean statusbean;
        int idlenum = 0;
        for (PobChargingPile record : pileList) {
            statusbean = ChargingCacheUtil.getPileStatusBean(record.getId());
            if (statusbean == null || statusbean.getStatus() == null) continue;
            if (PileStatusEnum.IDLE.equals(statusbean.getStatus()) || PileStatusEnum.FINISH.equals(statusbean.getStatus())) {// 空闲
                if (PileAppStatusEnum.UNORDERED.equals(statusbean.getAppstatus()) || PileAppStatusEnum.UNORDERABLE.equals(statusbean.getAppstatus())) {// 未预约
                    idlenum++;
                }
            }
        }
        return idlenum;
    }

    /**
     * 统计充电点下空闲桩数量、交直流桩数量
     * 
     * @param stationid
     * @return
     */
    public static Map<String, Integer> getPileNumByStationid(Integer stationid) {
        List<PobChargingPile> pileList = getChargePileListByStationid(stationid);
        int acnum = 0;
        int dcnum = 0;
        int idlenum = 0;
        PileStatusBean statusbean;
        if (pileList != null && pileList.size() > 0) {
            for (PobChargingPile record : pileList) {
                if (ChargeCurrentTypeEnum.AC.getShortValue().equals(record.getChaWay())) {// 交流
                    acnum++;
                } else if (ChargeCurrentTypeEnum.DC.getShortValue().equals(record.getChaWay())) {// 直流
                    dcnum++;
                }
                statusbean = ChargingCacheUtil.getPileStatusBean(record.getId());
                if (statusbean == null || statusbean.getStatus() == null) continue;
                if (PileStatusEnum.IDLE.equals(statusbean.getStatus()) || PileStatusEnum.FINISH.equals(statusbean.getStatus())) {// 空闲
                    if (PileAppStatusEnum.UNORDERED.equals(statusbean.getAppstatus()) || PileAppStatusEnum.UNORDERABLE.equals(statusbean.getAppstatus())) {// 未预约
                        idlenum++;
                    }
                }
            }
        }

        Map<String, Integer> resultMap = new HashMap<String, Integer>();
        resultMap.put("acnum", acnum);
        resultMap.put("dcnum", dcnum);
        resultMap.put("idlenum", idlenum);
        return resultMap;
    }

    /**
     * 获取某充电点下充电桩List
     */
    public static List<PobChargingPile> getChargePileListByStationid(Integer stationid) {
        reloadChargePile();
        if (stationid == null || chargePileMap == null || chargePileMap.size() == 0) return null;
        return chargePileMap.get(stationid);
    }

    /**
     * 获取某充电点下充电桩
     */
    public static PobChargingPile getChargePileById(Integer stationid, Integer pileid) {
        reloadChargePile();
        if (stationid == null || pileid == null || chargePileMap == null || chargePileMap.size() == 0) return null;
        List<PobChargingPile> pileList = chargePileMap.get(stationid);
        if (pileList == null || pileList.size() == 0) return null;
        for (PobChargingPile record : pileList) {
            if (record.getId().equals(pileid)) return record;
        }
        return null;
    }

    // --------------------------------------------充电桩-----------------------------------------

    /**
     * 初始化充电桩缓存数据
     */
    public static void initChargePile() {
        List<PobChargingPile> list = pobObjectService.selectChargingPileByExample(null);
        Integer stationid;
        List<PobChargingPile> tempList;
        Date tempDate = null;
        for (PobChargingPile record : list) {
            stationid = record.getStationId();
            if (chargePileMap.containsKey(stationid)) {
                tempList = chargePileMap.get(stationid);
                tempList.add(record);
            } else {
                tempList = new ArrayList<PobChargingPile>();
                tempList.add(record);
                chargePileMap.put(stationid, tempList);
            }
            if (record.getUpdateTime() == null) continue;
            if (tempDate == null) {
                tempDate = record.getUpdateTime();
            } else if (tempDate.compareTo(record.getUpdateTime()) < 0) {
                tempDate = record.getUpdateTime();
            }
        }
        if (tempDate == null) {
            pileUpdateTime = new Date();
        } else {
            pileUpdateTime = tempDate;
        }
    }

    /**
     * 增量更新充电桩缓存数据
     */
    public static synchronized void reloadChargePile() {
        List<String> stations = ChargingCacheUtil.getDelPile();// 有没删除桩
        if (stations != null && stations.size() > 0) {
            for (String sid : stations) {
                Integer stationId = NumberUtils.toInt(sid);
                if (chargePileMap.containsKey(stationId)) {
                    PobChargingPileExample emp = new PobChargingPileExample();
                    PobChargingPileExample.Criteria cr = emp.createCriteria();
                    cr.andStationIdEqualTo(stationId);
                    List<PobChargingPile> list = pobObjectService.selectChargingPileByExample(emp);
                    chargePileMap.put(stationId, list);
                }
            }
            ChargingCacheUtil.removeDelPile();
        }

        Date updateTime = ChargingCacheUtil.getUpdateTime(CacheKeyProvide.KEY_PILE_UPDATETIME);
        if (pileUpdateTime == null || updateTime == null) return;
        if (pileUpdateTime.getTime() == updateTime.getTime()) {
            return;
        }
        PobChargingPileExample emp = new PobChargingPileExample();
        PobChargingPileExample.Criteria cr = emp.createCriteria();
        cr.andUpdateTimeGreaterThan(pileUpdateTime);
        List<PobChargingPile> list = pobObjectService.selectChargingPileByExample(emp);
        List<PobChargingPile> tempList;
        // Date tempDate = pileUpdateTime;
        boolean isExist = false;
        for (PobChargingPile record : list) {
            isExist = false;
            if (chargePileMap.containsKey(record.getStationId())) {
                tempList = chargePileMap.get(record.getStationId());
                for (PobChargingPile pile : tempList) {
                    if (pile.getId().intValue() == record.getId().intValue()) {
                        ObjectUtil.copyProperty(record, pile);
                        isExist = true;
                        break;
                    }
                }
                if (!isExist) tempList.add(record);
            } else {
                tempList = new ArrayList<PobChargingPile>();
                tempList.add(record);
                chargePileMap.put(record.getStationId(), tempList);
            }
            /*
             * if (record.getUpdateTime() == null) continue; if (tempDate == null) { tempDate = record.getUpdateTime();
             * } else if (tempDate.compareTo(record.getUpdateTime()) < 0) { tempDate = record.getUpdateTime(); }
             */
        }
        pileUpdateTime = updateTime;
    }

    /**
     * 清空充电桩缓存数据
     */
    public static void clearChargePile() {
        if (chargePileMap != null) {
            chargePileMap.clear();
        }
    }

    /**
     * 根据ID获取充电桩
     */
    public static PobChargingPile getChargePileById(Integer pileid) {
        reloadChargePile();
        if (chargePileMap == null || chargePileMap.size() == 0 || pileid == null) return null;
        Iterator<Integer> it = chargePileMap.keySet().iterator();
        Integer stationid;
        List<PobChargingPile> pileList;
        while (it.hasNext()) {
            stationid = (Integer) it.next();
            pileList = chargePileMap.get(stationid);
            if (pileList == null || pileList.size() == 0) continue;
            for (PobChargingPile record : pileList) {
                if (record.getId().equals(pileid)) return record;
            }
        }
        return null;
    }

    /**
     * 根据code获取充电桩
     */
    public static PobChargingPile getChargePileByCode(String code) {
        reloadChargePile();
        if (chargePileMap == null || chargePileMap.size() == 0 || StringUtils.isEmpty(code)) return null;
        Iterator<Integer> it = chargePileMap.keySet().iterator();
        Integer stationid;
        List<PobChargingPile> pileList;
        while (it.hasNext()) {
            stationid = (Integer) it.next();
            pileList = chargePileMap.get(stationid);
            if (pileList == null || pileList.size() == 0) continue;
            for (PobChargingPile record : pileList) {
                if (record.getPileCode().equals(code)) return record;
            }
        }
        return null;
    }

    /**
     * 获取充电点+充电桩名称
     */
    public static String getStationPileNameById(Integer pileid) {
        if (pileid == null) return "未知";
        reloadChargePile();
        if (chargePileMap == null || chargePileMap.size() == 0) return "未知";
        Iterator<Integer> it = chargePileMap.keySet().iterator();
        Integer stationid;
        List<PobChargingPile> pileList;
        PobChargingStation station;
        String name = "";
        while (it.hasNext()) {
            stationid = (Integer) it.next();
            station = getChargeStationById(stationid);
            if (station == null) continue;
            pileList = chargePileMap.get(stationid);
            if (pileList == null || pileList.size() == 0) continue;
            for (PobChargingPile record : pileList) {
                if (record.getId().equals(pileid)) {
                    name = station.getStationName() + " " + record.getPileName();
                    return name;
                }
            }
        }
        return "未知";
    }

    /**
     * 获取实时充电桩List
     */
    public static void setOnlineChargePileListByInfoIdAndStationId(int infoId) {
        initOnlineDevice(infoId);
        byte[] value = BussinessCacheUtil.getOlineDeviceList(String.valueOf(infoId));
        if (value != null) {
            List<PobChargingPile> tempPileList = new ArrayList<PobChargingPile>();
            List<PobChargingPile> pileList = SerializeCoderUtil.deserializeList(value);
            for (PobChargingPile pile : pileList) {
                // TODO 实时更新缓存中数据
                Integer ran = new Random().nextInt(100) % 4 + 1;
                pile.setStatus(Short.valueOf(ran.toString()));
                pile.setUpdateTime(new Date());
                tempPileList.add(pile);
            }
            BussinessCacheUtil.setOlineDeviceList(String.valueOf(infoId), SerializeCoderUtil.serializeList(tempPileList));
        }
    }

    /**
     * 根据充电点列表桩的实时状态
     * 
     * @param busMec
     * @param busType
     * @return
     */
    public static List<PileRunStatusModel> getPileStatusListByStationList(List<PobChargingStation> stationList) {
        if (stationList == null || stationList.size() <= 0) return null;
        List<PobChargingPile> pileList;
        List<PileRunStatusModel> statusList = new ArrayList<PileRunStatusModel>();
        PileRunStatusModel runstatus;
        PileRunStatusModel tempstatus;
        for (PobChargingStation station : stationList) {
            pileList = getChargePileListByStationid(station.getId());
            if (pileList != null && pileList.size() > 0) {
                for (PobChargingPile pile : pileList) {
                    runstatus = new PileRunStatusModel();
                    runstatus.setPileid(pile.getId());
                    runstatus.setPilename(pile.getPileName());

                    runstatus.setPilecode(pile.getPileCode());
                    runstatus.setComtype(pile.getComType());
                    runstatus.setPiletype(pile.getPileType());
                    runstatus.setChaway(pile.getChaWay());
                    runstatus.setComaddr(pile.getComAddr());
                    runstatus.setAddress(pile.getAddress());
                    runstatus.setPilemodel(CacheChargeHolder.selectPileModelById(pile.getPileModel() == null ? 0 : pile.getPileModel()).getBrand());

                    runstatus.setCurrenttypedesc(ChargeCurrentTypeEnum.getText(pile.getChaWay()));
                    runstatus.setPayway(pile.getPayWay());
                    runstatus.setPaywaydesc(getPayWaydisc(pile.getPayWay()));
                    runstatus.setPowertypedesc(ChargePowerTypeEnum.getText(pile.getPileType()));
                    runstatus.setStationid(station.getId());
                    runstatus.setStationname(station.getStationName());
                    runstatus.setPhone(null);
                    runstatus.setUsername(null);

                    tempstatus = getPileStatusById(pile.getId());
                    if (tempstatus != null) {
                        runstatus.setUpdatetime(tempstatus.getUpdatetime());
                        runstatus.setOutv(tempstatus.getOutv());
                        runstatus.setOuti(tempstatus.getOuti());
                        runstatus.setStatus(tempstatus.getStatus());
                        runstatus.setStatusdisc(tempstatus.getStatusdisc());
                        runstatus.setAppendtime(tempstatus.getAppendtime());
                        runstatus.setApprecordid(tempstatus.getApprecordid());
                        runstatus.setUserid(tempstatus.getUserid());
                        runstatus.setUserid(tempstatus.getUserid());
                        runstatus.setUsername(tempstatus.getUsername());
                        runstatus.setChalen(tempstatus.getChalen());
                        runstatus.setChapower(tempstatus.getChapower());
                        runstatus.setMoney(tempstatus.getMoney());
                        runstatus.setPhone(tempstatus.getPhone());
                    }
                    statusList.add(runstatus);
                }
            }
        }
        return statusList;
    }

    /**
     * 根据运营商类型和运营商id获取桩的实时状态
     * 
     * @param busMec
     * @param busType
     * @return
     */
    public static List<PileRunStatusModel> getPileStatusListByInfoId(Integer busMec, Short busType) {
        if (busMec == null || busType == null) return null;
        List<PobChargingStation> stationList = getChargeStationListByMecAndTypeData(busMec, busType);
        List<PobChargingPile> pileList;
        List<PileRunStatusModel> statusList = new ArrayList<PileRunStatusModel>();
        PileRunStatusModel runstatus;
        PileRunStatusModel tempstatus;
        for (PobChargingStation station : stationList) {
            pileList = getChargePileListByStationid(station.getId());
            if (pileList != null && pileList.size() > 0) {
                for (PobChargingPile pile : pileList) {
                    runstatus = new PileRunStatusModel();
                    runstatus.setPileid(pile.getId());
                    runstatus.setPilename(pile.getPileName());

                    runstatus.setPilecode(pile.getPileCode());
                    runstatus.setComtype(pile.getComType());
                    runstatus.setPiletype(pile.getPileType());
                    runstatus.setChaway(pile.getChaWay());
                    runstatus.setComaddr(pile.getComAddr());
                    runstatus.setAddress(pile.getAddress());
                    runstatus.setPilemodel(CacheChargeHolder.selectPileModelById(pile.getPileModel() == null ? 0 : pile.getPileModel()).getBrand());

                    runstatus.setCurrenttypedesc(ChargeCurrentTypeEnum.getText(pile.getChaWay()));
                    runstatus.setPayway(pile.getPayWay());
                    runstatus.setPaywaydesc(getPayWaydisc(pile.getPayWay()));
                    runstatus.setPowertypedesc(ChargePowerTypeEnum.getText(pile.getPileType()));
                    runstatus.setStationid(station.getId());
                    runstatus.setStationname(station.getStationName());
                    runstatus.setPhone(null);
                    runstatus.setUsername(null);

                    tempstatus = getPileStatusById(pile.getId());
                    if (tempstatus != null) {
                        runstatus.setUpdatetime(tempstatus.getUpdatetime());
                        runstatus.setOutv(tempstatus.getOutv());
                        runstatus.setOuti(tempstatus.getOuti());
                        runstatus.setStatus(tempstatus.getStatus());
                        runstatus.setStatusdisc(tempstatus.getStatusdisc());
                        runstatus.setAppendtime(tempstatus.getAppendtime());
                        runstatus.setApprecordid(tempstatus.getApprecordid());
                        runstatus.setUserid(tempstatus.getUserid());
                        runstatus.setUserid(tempstatus.getUserid());
                        runstatus.setUsername(tempstatus.getUsername());
                        runstatus.setChalen(tempstatus.getChalen());
                        runstatus.setChapower(tempstatus.getChapower());
                        runstatus.setMoney(tempstatus.getMoney());
                    }
                    statusList.add(runstatus);
                }
            }
        }
        return statusList;
    }

    public static List<PileRunStatusModel> getPileStatusListByStationid(Integer stationid) {
        if (stationid == null) return null;
        PobChargingStation station = getChargeStationById(stationid);
        if (station == null) return null;
        List<PobChargingPile> pileList;
        List<PileRunStatusModel> statusList = new ArrayList<PileRunStatusModel>();
        PileRunStatusModel runstatus;
        PileRunStatusModel tempstatus;
        pileList = getChargePileListByStationid(station.getId());
        if (pileList != null && pileList.size() > 0) {
            for (PobChargingPile pile : pileList) {
                runstatus = new PileRunStatusModel();
                runstatus.setPileToType(pile.getPileToType());
                runstatus.setPileid(pile.getId());
                runstatus.setPilename(pile.getPileName());
                runstatus.setPilecode(pile.getPileCode());
                runstatus.setComtype(pile.getComType());
                runstatus.setPiletype(pile.getPileType());
                runstatus.setChaway(pile.getChaWay());
                runstatus.setComaddr(pile.getComAddr());
                runstatus.setAddress(pile.getAddress());
                runstatus.setPilemodel(CacheChargeHolder.selectPileModelById(pile.getPileModel() == null ? 0 : pile.getPileModel()).getBrand());
                runstatus.setCurrenttypedesc(ChargeCurrentTypeEnum.getText(pile.getChaWay() == null ? 0 : pile.getChaWay()));
                runstatus.setPayway(pile.getPayWay());
                runstatus.setPaywaydesc(getPayWaydisc(pile.getPayWay()));
                runstatus.setPowertypedesc(ChargePowerTypeEnum.getText(pile.getPileType() == null ? 0 : pile.getPileType()));
                runstatus.setStationid(pile.getStationId());
                runstatus.setStationname(station.getStationName());
                runstatus.setPhone(null);
                runstatus.setUsername(null);

                tempstatus = getPileStatusById(pile.getId());
                if (tempstatus != null) {
                    runstatus.setUpdatetime(tempstatus.getUpdatetime());
                    runstatus.setOutv(tempstatus.getOutv());
                    runstatus.setOuti(tempstatus.getOuti());
                    runstatus.setStatus(tempstatus.getStatus());
                    runstatus.setStatusdisc(tempstatus.getStatusdisc());
                    runstatus.setAppendtime(tempstatus.getAppendtime());
                    runstatus.setApprecordid(tempstatus.getApprecordid());
                    runstatus.setUserid(tempstatus.getUserid());
                    runstatus.setUsername(tempstatus.getUsername());
                    runstatus.setChalen(tempstatus.getChalen());
                    runstatus.setChapower(tempstatus.getChapower());
                    runstatus.setMoney(tempstatus.getMoney());
                    runstatus.setPaywaydesc(tempstatus.getPhone());
                    runstatus.setPhone(tempstatus.getPhone());
                    runstatus.setPlateNum(tempstatus.getPlateNum());
                    runstatus.setSoc(tempstatus.getSoc());
                    runstatus.setStarttime(tempstatus.getStarttime());
                }
                statusList.add(runstatus);
            }
        }
        return statusList;
    }

    public static PileRunStatusModel getPileStatusById(Integer id) {
        PileStatusBean statusbean = ChargingCacheUtil.getPileStatusBean(id);
        // statusbean.setStarttime(new Date());
        // statusbean.setOuti("1.21");
        // statusbean.setOutv("3.6");
        // statusbean.setSoc(0.36);
        // statusbean.setPlateNum("浙A11111");
        // statusbean.setChalen(55);
        // statusbean.setStatus(PileStatusEnum.CHARGING);
        // statusbean.setChapower(2.6);
        // TEST

        /*
         * statusbean.setStatus(PileStatusEnum.CHARGING); statusbean.setOutv("500"); statusbean.setOuti("20");
         * statusbean.setUserid(168);
         */
        // TEST
        BusUser user;
        PileRunStatusModel runstatus = new PileRunStatusModel();
        if (statusbean == null) {
            runstatus.setPileid(id);
            runstatus.setStatus(PileRunStatusEnum.UNKNOW.getShortValue());
            runstatus.setStatusdisc(PileRunStatusEnum.UNKNOW.getText());
            PobChargingPile pile = getChargePileById(id);
            if (pile != null) {
                runstatus.setStationid(pile.getStationId());
            }
        } else {
            runstatus.setPileid(statusbean.getId());
            runstatus.setStationid(statusbean.getStationid());
            runstatus.setUpdatetime(statusbean.getUpdatetime());
            if (PileStatusEnum.OFFLINE == statusbean.getStatus()) {
                runstatus.setStatus(PileRunStatusEnum.OFFLINE.getShortValue());
                runstatus.setStatusdisc(PileRunStatusEnum.OFFLINE.getText());
            } else if (PileStatusEnum.FAULT == statusbean.getStatus()) {
                runstatus.setStatus(PileRunStatusEnum.FAULT.getShortValue());
                runstatus.setStatusdisc(PileRunStatusEnum.FAULT.getText());
            } else if (PileStatusEnum.IDLE == statusbean.getStatus() || PileStatusEnum.FINISH == statusbean.getStatus()) {// 空闲
                if (PileAppStatusEnum.UNORDERED == statusbean.getAppstatus() || PileAppStatusEnum.UNORDERABLE == statusbean.getAppstatus()) {// 空闲
                    runstatus.setStatus(PileRunStatusEnum.IDLE.getShortValue());
                    runstatus.setStatusdisc(PileRunStatusEnum.IDLE.getText());
                } else {// 缓存中预约状态为与请求预约/预约中/预约失败时，则判断预约时间是否过期
                    if (statusbean.getUserid() != null && statusbean.getUserid().intValue() > 0 && statusbean.getAppendtime() != null
                        && new Date().compareTo(statusbean.getAppendtime()) < 0) {// 预约中
                        runstatus.setStatus(PileRunStatusEnum.APPOINTMENT.getShortValue());
                        runstatus.setStatusdisc(PileRunStatusEnum.APPOINTMENT.getText());
                        runstatus.setAppendtime(statusbean.getAppendtime());
                        runstatus.setApprecordid(statusbean.getApprecordid());
                        runstatus.setUserid(statusbean.getUserid());
                        user = userService.selectUserByCache(statusbean.getUserid());
                        if (user != null) {
                            runstatus.setUsername(user.getUsername());
                        }
                    } else {// 空闲
                        runstatus.setStatus(PileRunStatusEnum.IDLE.getShortValue());
                        runstatus.setStatusdisc(PileRunStatusEnum.IDLE.getText());
                    }
                }
            } else if (PileStatusEnum.CHARGING == statusbean.getStatus()) {// 充电中
                runstatus.setStatus(PileRunStatusEnum.CHARGING.getShortValue());
                runstatus.setStatusdisc(PileRunStatusEnum.CHARGING.getText());
                runstatus.setTradeno(statusbean.getTradeno());
                runstatus.setChalen(statusbean.getChalen());
                runstatus.setChapower(statusbean.getChapower());
                runstatus.setMoney(statusbean.getMoney());
                runstatus.setUserid(statusbean.getUserid());
                runstatus.setPlateNum(statusbean.getPlateNum());
                runstatus.setSoc(statusbean.getSoc());
                runstatus.setStarttime(statusbean.getStarttime());
                runstatus.setOutv(statusbean.getOutv());
                runstatus.setOuti(statusbean.getOuti());
                user = userService.selectUserByCache(statusbean.getUserid());
                if (user != null) {
                    runstatus.setUsername(user.getUsername());
                    runstatus.setPhone(user.getPhone());
                }
            } else {
                runstatus.setStatus(PileRunStatusEnum.UNKNOW.getShortValue());
                runstatus.setStatusdisc(PileRunStatusEnum.UNKNOW.getText());
            }
        }
        return runstatus;
    }

    /**
     * 获取实时充电桩List
     */
    public static List<PobChargingPile> getOnlineChargePileListByInfoIdAndStationId(int infoId, int stationId) {
        initOnlineDevice(infoId);
        byte[] value = BussinessCacheUtil.getOlineDeviceList(String.valueOf(infoId));
        List<PobChargingPile> plist = new ArrayList<PobChargingPile>();
        if (value != null) {
            List<PobChargingPile> pileList = SerializeCoderUtil.deserializeList(value);
            for (PobChargingPile pile : pileList) {
                if (stationId > 0) {
                    if (pile.getStationId() == stationId) {
                        plist.add(pile);
                    }
                } else {
                    plist.add(pile);
                }
            }
        }
        return plist;
    }

    // --------------------------------------------充电桩型号-----------------------------------------

    /**
     * 初始化充电桩型号
     */
    public static void initPileModel() {
        List<BusPileModel> list = deviceService.selectPileModelByExample(null);
        Date tempDate = null;
        for (BusPileModel record : list) {
            if (record.getUpdateTime() == null) continue;
            if (tempDate == null) {
                tempDate = record.getUpdateTime();
            } else if (tempDate.compareTo(record.getUpdateTime()) < 0) {
                tempDate = record.getUpdateTime();
            }
        }
        if (list != null && list.size() > 0) {
            pileModelList.addAll(list);
        }
        if (tempDate == null) {
            pileModelUpdateTime = new Date();
        } else {
            pileModelUpdateTime = tempDate;
        }
    }

    /**
     * 增量更新电桩型号缓存数据
     */
    public static void reloadPileModel() {
        Date updateTime = ChargingCacheUtil.getUpdateTime(CacheKeyProvide.KEY_PILEMODEL_UPDATETIME);
        if (pileModelUpdateTime == null || updateTime == null) return;
        if (pileModelUpdateTime.getTime() == updateTime.getTime()) {
            return;
        }
        BusPileModelExample emp = new BusPileModelExample();
        BusPileModelExample.Criteria cr = emp.createCriteria();
        cr.andUpdateTimeGreaterThan(pileModelUpdateTime);
        List<BusPileModel> list = deviceService.selectPileModelByExample(emp);
        boolean isExist = false;
        List<BusPileModel> tempList = new ArrayList<BusPileModel>();
        for (BusPileModel record : list) {
            isExist = false;
            for (BusPileModel pileModel : pileModelList) {
                if (pileModel.getId().intValue() == record.getId().intValue()) {
                    ObjectUtil.copyProperty(record, pileModel);
                    isExist = true;
                    break;
                }
            }
            if (!isExist) tempList.add(record);
        }
        if (tempList.size() > 0) {
            pileModelList.addAll(tempList);
        }
        pileModelUpdateTime = updateTime;
    }

    public static BusPileModel selectPileModelById(Integer id) {
        if (pileModelList == null || id == null) return null;
        reloadPileModel();
        for (BusPileModel record : pileModelList) {
            if (record.getId().equals(id)) {
                return record;
            }
        }
        return null;
    }

    public static List<BusPileModel> getPileModelList() {
        reloadPileModel();
        List<BusPileModel> list = new ArrayList<BusPileModel>();
        for (BusPileModel record : pileModelList) {
            if (PileModelStatusEnum.VALID.getShortValue().equals(record.getStatus())) {
                list.add(record);
            }
        }
        return list;
    }

    // --------------------------------------------充电桩计费规则-----------------------------------------

    /**
     * 初始化计费规则数据
     */
    public static void initChargeRule() {
        chargeRuleList = deviceService.selectBusChargeRuleByExample(null);
    }

    public static BusChargeRule getChargeRuleById(Integer id) {
        if (chargeRuleList == null || chargeRuleList.size() == 0) return null;
        for (BusChargeRule record : chargeRuleList) {
            if (record.getId().equals(id)) return record;
        }
        return null;
    }

    public static List<BusChargeRule> getChargeRuleList() {
        return chargeRuleList;
    }

    // ------------------------------------服务点----------------------------------------------
    /**
     * 初始化服务点缓存数据
     */
    public static void initRepairPoint() {
        List<BusRepairPoint> list = pobObjectService.selectRepairPointByExample(null);
        Date tempDate = null;
        for (BusRepairPoint record : list) {
            repairPointMap.put(record.getId(), record);
            if (record.getAddTime() == null) continue;
            if (tempDate == null) {
                tempDate = record.getAddTime();
            } else if (tempDate.compareTo(record.getAddTime()) < 0) {
                tempDate = record.getAddTime();
            }
        }
        if (tempDate == null) {
            repairPointUpdateTime = new Date();
        } else {
            repairPointUpdateTime = tempDate;
        }
    }

    /**
     * 增量更新服务点缓存数据
     */
    public static void reloadRepairPoint() {
        Date updateTime = ChargingCacheUtil.getUpdateTime(CacheKeyProvide.KEY_REPAIRPOINT_UPDATETIME);
        if (repairPointUpdateTime == null || updateTime == null) return;
        if (repairPointUpdateTime.getTime() == updateTime.getTime()) {
            return;
        }

        BusRepairPointExample emp = new BusRepairPointExample();
        BusRepairPointExample.Criteria cr = emp.createCriteria();
        cr.andAddTimeGreaterThan(repairPointUpdateTime);
        List<BusRepairPoint> list = pobObjectService.selectRepairPointByExample(emp);
        for (BusRepairPoint record : list) {
            repairPointMap.put(record.getId(), record);
        }
        repairPointUpdateTime = updateTime;
    }

    /**
     * 更新单个服务点缓存信息
     * 
     * @param id
     */
    public static void reloadRepairPointById(Integer id) {
        if (id == null) return;
        reloadRepairPoint();
        BusRepairPoint record = pobObjectService.selectRepairPointByPK(id);
        if (record == null) return;
        repairPointMap.put(record.getId(), record);
    }

    /**
     * 清空服务点缓存数据
     */
    public static void clearRepairPoint() {
        if (repairPointMap != null) {
            repairPointMap.clear();
        }
    }

    /**
     * 获取可显示服务点List
     */
    public static List<BusRepairPoint> getRepairPointList() {
        reloadRepairPoint();
        if (repairPointMap == null || repairPointMap.size() == 0) return null;
        Collection<BusRepairPoint> csCollection = repairPointMap.values();
        List<BusRepairPoint> list = new ArrayList<BusRepairPoint>();
        for (BusRepairPoint record : csCollection) {
            if (WhetherEnum.YES.getShortValue().equals(record.getIsShow())) {
                list.add(record);
            }
        }
        return list;
    }

    // --------------------------------------------其他----------------------------------------

    private static String getPayWaydisc(String payWay) {
        String desc = "";
        if (payWay != null) {
            String[] ways = payWay.split(",");
            String temp = "";
            for (String way : ways) {
                temp = CacheSysHolder.getSysLinkName(LinkTypeEnum.PAY_WAY.getValue(), way);
                if (StringUtil.isNotEmpty(temp)) {
                    desc += " " + temp + " ,";
                }
            }
            if (desc.endsWith(",")) {
                desc = desc.substring(0, desc.length() - 1);
            }
        }
        return desc;
    }

    // SET
    public void setPobObjectService(PobObjectService pobObjectService) {
        CacheChargeHolder.pobObjectService = pobObjectService;
    }

    public void setDeviceService(DeviceService deviceService) {
        CacheChargeHolder.deviceService = deviceService;
    }

    public static void setUserService(UserService userService) {
        CacheChargeHolder.userService = userService;
    }

}
