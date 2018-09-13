package com.holley.charging.bussiness.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.common.util.CacheChargeHolder.PileRunStatusEnum;
import com.holley.charging.model.def.DcsAlarmEventsModel;
import com.holley.charging.model.def.PileRunStatusModel;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.JsonUtil;
import com.holley.platform.model.sys.SysLink;
import com.holley.platform.util.CacheSysHolder;

/**
 * 监控相关ACTION
 * 
 * @author shencheng
 */
public class MonitorAction extends BaseAction {

    private static final long   serialVersionUID = 1L;
    private Map<String, Object> result;
    private DeviceService       deviceService;

    public String monitorMap() {
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
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("busMec", webUser.getInfoId());
            param.put("busType", webUser.getUsertype().getValue());
            param.put("limit", 10);// 10条告警数量
            this.getRequest().setAttribute("jsonPobChargingStation", "{}");
            this.getRequest().setAttribute("jsonPileList", "{}");
            List<DcsAlarmEventsModel> alarmEventList = this.deviceService.exportAlarmEvents(param);
            getRequest().setAttribute("alarmEventList", alarmEventList);
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
     * 异步查询所在充电点桩实时列表
     * 
     * @return
     */
    public String searchPileListByStationId() {
        WebUser webUser = getWebuser();
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
        // 通讯协议：南网，创锐
        List<SysLink> comTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.COM_TYPE.getValue());
        // 充电功率类型：快，慢，超速
        List<SysLink> pileTypeList = CacheSysHolder.getLinkListBytypeId(LinkTypeEnum.PILE_TYPE.getValue());
        this.getRequest().setAttribute("allOnlinePileList", allOnlinePileList);
        this.getRequest().setAttribute("chaWayList", chaWayList);
        this.getRequest().setAttribute("comTypeList", comTypeList);
        this.getRequest().setAttribute("pileTypeList", pileTypeList);
        return SUCCESS;
    }

    /**
     * 异步查询实时监控信息
     * 
     * @return
     */
    public String searchAllOnlinePileListByAjax() {

        return SUCCESS;
    }

    // GET SET
    public Map<String, Object> getResult() {
        return result;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

}
