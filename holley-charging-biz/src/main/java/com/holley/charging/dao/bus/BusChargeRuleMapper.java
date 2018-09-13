package com.holley.charging.dao.bus;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.bus.BusChargeRule;
import com.holley.charging.model.bus.BusChargeRuleExample;
import com.holley.charging.model.def.ChargeRuleModel;

public interface BusChargeRuleMapper {

    int countByExample(BusChargeRuleExample example);

    int deleteByExample(BusChargeRuleExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusChargeRule record);

    int insertSelective(BusChargeRule record);

    List<BusChargeRule> selectByExample(BusChargeRuleExample example);

    BusChargeRule selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusChargeRule record, @Param("example") BusChargeRuleExample example);

    int updateByExample(@Param("record") BusChargeRule record, @Param("example") BusChargeRuleExample example);

    int updateByPrimaryKeySelective(BusChargeRule record);

    int updateByPrimaryKey(BusChargeRule record);

    /**
     * 根据条件查询收费规则
     * 
     * @param param
     * @return
     */
    ChargeRuleModel selectChargeRuleModelByMap(Map<String, Object> param);

    List<BusChargeRule> selectChargeRuleByPage(Map<String, Object> param);
}
