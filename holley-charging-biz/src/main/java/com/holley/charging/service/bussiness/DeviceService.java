package com.holley.charging.service.bussiness;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.holley.charging.model.bus.BusChargeRule;
import com.holley.charging.model.bus.BusChargeRuleExample;
import com.holley.charging.model.bus.BusPileApply;
import com.holley.charging.model.bus.BusPileChargerule;
import com.holley.charging.model.bus.BusPileChargeruleExample;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.bus.BusPileModelExample;
import com.holley.charging.model.dcs.DcsHisyc;
import com.holley.charging.model.dcs.DcsHisycExample;
import com.holley.charging.model.def.ChargeRuleModel;
import com.holley.charging.model.def.DcsAlarmEventsModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingPileExample;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;
import com.holley.charging.model.pob.PobChargingTempPile;
import com.holley.charging.model.pob.PobChargingTempPileExample;
import com.holley.charging.model.pob.PobChargingTempStation;
import com.holley.charging.model.pob.PobChargingTempStationExample;
import com.holley.common.constants.charge.DeviceVerifyStatusEnum;

public interface DeviceService {

    int deletePileByPrimaryKey(Integer id);

    // --------------------------PobChargingTempStation-----------------------------
    int insertPobChargingTempStationSelective(PobChargingTempStation pobChargingTempStation);

    String insertOrUpdateChargingStation(PobChargingTempStation tempStation, PobChargingStation station, DeviceVerifyStatusEnum statusEnum, String servletRealPath)
                                                                                                                                                                   throws Exception;

    // 新增充电点
    Map<String, Object> insertAndUpdateTempStation(PobChargingTempStation tempStation, File img, String servletRealPath) throws Exception;

    // 新增正式充电点无需审核过程
    Map<String, Object> insertAndUpdateStation(PobChargingStation tempStation, File img, String servletRealPath) throws Exception;

    // 修改增充电点
    Map<String, Object> insertAndUpdateTempStationForEdit(PobChargingTempStation oldEditTempStation, PobChargingTempStation tempStation, File img, String servletRealPath)
                                                                                                                                                                          throws Exception;

    int updatePobChargingTempStationByPrimaryKeySelective(PobChargingTempStation record);

    int updatePobChargingTempPileByPrimaryKeySelective(PobChargingTempPile record, PobChargingTempPileExample example);

    PobChargingTempStation selectTempStationByPrimaryKey(Integer id);

    /**
     * 分页查询未审核充电点的列表信息
     * 
     * @param param
     * @return
     */
    List<PobChargingTempStation> selectTempStationByPage(Map<String, Object> param);

    /**
     * 根据条件查询临时表单个电点
     * 
     * @param example
     * @return
     */
    List<PobChargingTempStation> selectPobChargingTempStationByExample(PobChargingTempStationExample example);

    /**
     * 分页查询充电点审核记录
     * 
     * @param param
     * @return
     */
    List<PobChargingTempStation> selectTempStationVerifyByPage(Map<String, Object> params);

    /**
     * 取临时表充电点数量
     * 
     * @param example
     * @return
     */
    int countPobChargingTempStationByExample(PobChargingTempStationExample example);

    // -----------------------------------PobChargingTempPile---------------------------------
    int insertPobChargingTempPileSelective(PobChargingTempPile pobChargingTempPile);

    boolean insertOrUpdateChargingPile(PobChargingTempPile tempPile, PobChargingPile pile, BusPileChargerule rule, DeviceVerifyStatusEnum statusEnum) throws Exception;

    // 新增充电桩
    Map<String, Object> insertAndUpdateTempPile(PobChargingTempPile tempPile, File doc, String docType, String servletRealPath) throws Exception;

    // 修改增充电桩
    Map<String, Object> insertAndUpdateTempPileForEdit(PobChargingTempPile oldEditTempPile, PobChargingTempPile tempPile, File doc, String docType, String servletRealPath)
                                                                                                                                                                           throws Exception;

    int updateChargingTempPileByPKSelective(PobChargingTempPile record);

    int updateTempPileByExampleSelective(PobChargingTempPile record, PobChargingTempPileExample example);

    PobChargingTempPile selectTempPileByPrimaryKey(Integer id);

    /**
     * 根据条件查询临时表单个充电桩
     * 
     * @param example
     * @return
     */
    List<PobChargingTempPile> selectPobChargingTempPileByExample(PobChargingTempPileExample example);

    /**
     * 分页查询未审核桩列表信息
     * 
     * @param param
     * @return
     */
    List<PobChargingTempPile> selectTempPileByPage(Map<String, Object> param);

    /**
     * 分页查询充电桩审核记录
     * 
     * @param param
     * @return
     */
    List<PobChargingTempPile> selectTempPileVerifyByPage(Map<String, Object> params);

    // ----------------------------------PobChargingStation-----------------------------
    int insertChargingStation(PobChargingStation record);

    int updateChargingStationByPK(PobChargingStation record);

    int updateChargingStationByPKSelective(PobChargingStation record);

    PobChargingStation selectChargeStationByPrimaryKey(Integer id);

    /**
     * 根据条件查询正式表单个电点
     * 
     * @param example
     * @return
     */
    PobChargingStation selectPobChargingStationByExample(PobChargingStationExample example);

    /**
     * 分页查询充电点的列表信息
     * 
     * @param param
     * @return
     */

    List<PobChargingStation> selectStationByPage(Map<String, Object> param);

    List<PobChargingStation> selectStationByParamByPage(Map<String, Object> param);

    List<PobChargingStation> selectStationByExample(PobChargingStationExample example);

    /**
     * 取正式表充电点数量
     * 
     * @param example
     * @return
     */
    int countPobChargingStationByExample(PobChargingStationExample example);

    // ------------------------------PobChargingPile----------------------------
    int insertChargingPile(PobChargingPile record);

    int updateChargingPileByPK(PobChargingPile record);

    int updateChargingPileByPKSelective(PobChargingPile record);

    /**
     * 根据条件查询正式表单个充电桩
     * 
     * @param example
     * @return
     */
    List<PobChargingPile> selectPobChargingPileByExample(PobChargingPileExample example);

    /**
     * 分页查询充电点的桩列表信息
     * 
     * @param param
     * @return
     */
    List<PobChargingPile> selectPileByPage(Map<String, Object> param);

    /**
     * 导出充电桩Excel
     * 
     * @param param
     * @return
     */
    List<PobChargingPile> exportPile(Map<String, Object> param);

    /**
     * 按条件查询单一充电桩
     * 
     * @param param
     * @return
     */
    PobChargingPile selectPileByMap(Map<String, Object> param);

    PobChargingPile selectPileByPrimaryKey(Integer id);

    List<PobChargingPile> selectChargePileByPage(Map<String, Object> params);

    // ------------------------------BusChargeRule-----------------------
    List<BusChargeRule> selectBusChargeRuleByExample(BusChargeRuleExample busChargeRuleExample);

    // ------------------------------BusPileChargeRule-----------------------
    List<BusPileChargerule> selectBusPileChargeruleByExample(BusPileChargeruleExample emp);

    BusPileChargerule selectPileChargeRuleByParams(Map<String, Object> params);

    /**
     * 根据条件查询收费规则
     * 
     * @param param
     * @return
     */
    ChargeRuleModel selectChargeRuleModelByMap(Map<String, Object> param);

    // --------------------------DcsAlarmEventsModel---------------------
    /**
     * 分页查询告警事件列表
     * 
     * @param param
     * @return
     */
    List<DcsAlarmEventsModel> selectDcsAlarmEventsModelByPage(Map<String, Object> param);

    // ------------------------BusPileModel---------------------------------
    List<BusPileModel> selectPileModelByExample(BusPileModelExample example);

    List<BusPileModel> selectPileModelByPage(Map<String, Object> params);

    BusPileModel selectPileModelByPK(Integer id);

    int updatePileModelByPKSelective(BusPileModel record);

    int insertPileModelSelective(BusPileModel record);

    // ------------------------BusPileApply---------------------------------
    int insertPileApply(BusPileApply record);

    int insertPileApplySelective(BusPileApply record);

    int updatePileApplyByPKSelective(BusPileApply record);

    BusPileApply selectPileApplyByPK(Integer id);

    List<BusPileApply> selectPileApplyByParams(Map<String, Object> params);

    /**
     * 导出事件Excel
     * 
     * @param param
     * @return
     */
    List<DcsAlarmEventsModel> exportAlarmEvents(Map<String, Object> param);

    int countPobChargingPileByExample(PobChargingPileExample example);

    int countPobChargingTeampPileByExample(PobChargingTempPileExample example);

    // 更新充电点
    Map<String, Object> updatePobStation(PobChargingStation tempStation, PobChargingStation oldStation, File img, String servletRealPath) throws Exception;

    /**
     * 后台添加充电桩
     * 
     * @param tempPile
     * @return
     */
    Map<String, Object> insertAndUpdatePile(PobChargingTempPile tempPile) throws Exception;

    /**
     * 后台修改充电桩
     * 
     * @param tempPile
     * @return
     * @throws Exception
     */
    Map<String, Object> updatePile(PobChargingTempPile tempPile) throws Exception;

    List<DcsHisyc> selectDcsHisycByExample(DcsHisycExample example);

    int deleteAndUpdatePile(int pileId);

    List<BusChargeRule> selectChargeRuleByPage(Map<String, Object> param);

    int insertChargeRuleSelective(BusChargeRule record);

    int updateChargeRuleByPrimaryKeySelective(BusChargeRule record);

    int deleteChargeRuleByPrimaryKey(Integer id);

    BusChargeRule selectChargeRuleByPrimaryKey(Integer id);

    int updatePileChargeruleByExampleSelective(BusPileChargerule record, BusPileChargeruleExample example);

    int insertPileChargeRuleBatch(List<BusPileChargerule> list);

    void insertAndUpdatePileChargeRuleBatch(List<Integer> stationIds, BusPileChargerule pileChargerule) throws Exception;
}
