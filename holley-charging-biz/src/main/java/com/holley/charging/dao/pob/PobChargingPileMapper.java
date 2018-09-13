package com.holley.charging.dao.pob;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.def.PileTypeNumModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingPileExample;

public interface PobChargingPileMapper {

    int countByExample(PobChargingPileExample example);

    int deleteByExample(PobChargingPileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PobChargingPile record);

    int insertSelective(PobChargingPile record);

    List<PobChargingPile> selectByExample(PobChargingPileExample example);

    PobChargingPile selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PobChargingPile record, @Param("example") PobChargingPileExample example);

    int updateByExample(@Param("record") PobChargingPile record, @Param("example") PobChargingPileExample example);

    int updateByPrimaryKeySelective(PobChargingPile record);

    int updateByPrimaryKey(PobChargingPile record);

    /**
     * 分页查询桩列表信息
     * 
     * @param param
     * @return
     */
    List<PobChargingPile> selectPileByPage(Map<String, Object> param);

    /**
     * 查询个人充电桩，传入infoid
     * 
     * @param param
     * @return
     */
    List<PobChargingPile> selectByParam(Map<String, Object> param);

    /**
     * 按条件查询单一充电桩
     * 
     * @param param
     * @return
     */
    PobChargingPile selectPileByMap(Map<String, Object> param);

    /**
     * 统计充电点下的快慢充数量
     * 
     * @param stationid
     * @return
     */
    List<PileTypeNumModel> selectPileTypeNumByStationId(Integer stationid);

    /**
     * 查询充电桩及其费用信息
     * 
     * @param params
     * @return
     */
    List<PobChargingPile> selectPileAndFeeByParam(Map<String, Object> params);

    /**
     * 导出充电桩Excel
     * 
     * @param param
     * @return
     */
    List<PobChargingPile> exportPile(Map<String, Object> param);

    List<PobChargingPile> selectChargePileByPage(Map<String, Object> params);
}
