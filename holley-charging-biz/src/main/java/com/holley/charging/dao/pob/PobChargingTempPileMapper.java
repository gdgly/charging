package com.holley.charging.dao.pob;

import com.holley.charging.model.pob.PobChargingTempPile;
import com.holley.charging.model.pob.PobChargingTempPileExample;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface PobChargingTempPileMapper {
    int countByExample(PobChargingTempPileExample example);

    int deleteByExample(PobChargingTempPileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PobChargingTempPile record);

    int insertSelective(PobChargingTempPile record);

    List<PobChargingTempPile> selectByExample(PobChargingTempPileExample example);

    PobChargingTempPile selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PobChargingTempPile record, @Param("example") PobChargingTempPileExample example);

    int updateByExample(@Param("record") PobChargingTempPile record, @Param("example") PobChargingTempPileExample example);

    int updateByPrimaryKeySelective(PobChargingTempPile record);

    int updateByPrimaryKey(PobChargingTempPile record);
    /**
     * 分页查询未审核充电桩列表
     * 
     * @param param
     * @return
     */
    public List<PobChargingTempPile> selectTempPileByPage(Map<String, Object> param);

    public List<PobChargingTempPile> selectTempPileVerifyByPage(Map<String, Object> param);
}