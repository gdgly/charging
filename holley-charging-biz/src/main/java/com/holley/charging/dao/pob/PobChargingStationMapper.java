package com.holley.charging.dao.pob;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.def.CountPileModel;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.model.pob.PobChargingStationExample;

public interface PobChargingStationMapper {

    int countByExample(PobChargingStationExample example);

    int deleteByExample(PobChargingStationExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PobChargingStation record);

    int insertSelective(PobChargingStation record);

    List<PobChargingStation> selectByExample(PobChargingStationExample example);

    PobChargingStation selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PobChargingStation record, @Param("example") PobChargingStationExample example);

    int updateByExample(@Param("record") PobChargingStation record, @Param("example") PobChargingStationExample example);

    int updateByPrimaryKeySelective(PobChargingStation record);

    int updateByPrimaryKey(PobChargingStation record);

    int updateChargingStationScore(Map<String, Object> params);

    /**
     * 统计快慢充数量
     * 
     * @param param
     * @return
     */
    CountPileModel countFastSlow(Map<String, Object> param);

    /**
     * 分页查找所有充电点
     * 
     * @param String
     * @return
     */
    List<PobChargingStation> selectStationByPage(Map<String, Object> param);

    List<PobChargingStation> selectStationByParamByPage(Map<String, Object> param);

    /**
     * 最近充电记录的充电点
     * 
     * @param userid
     * @return
     */
    List<Integer> selectRecentChargeStation(Integer userid);

    /**
     * 分页查询有桩的充电点(bms)
     * 
     * @param param
     * @return
     */
    List<PobChargingStation> selectHasPileStationByPage(Map<String, Object> params);

    /**
     * 分页查询充电桩(bms)
     * 
     * @param params
     * @return
     */
    List<PobChargingStation> selectChargeStationByPage(Map<String, Object> params);

}
