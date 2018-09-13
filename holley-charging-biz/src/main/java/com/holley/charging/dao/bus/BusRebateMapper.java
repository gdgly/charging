package com.holley.charging.dao.bus;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.bus.BusRebate;
import com.holley.charging.model.bus.BusRebateExample;

public interface BusRebateMapper {

    int countByExample(BusRebateExample example);

    int deleteByExample(BusRebateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusRebate record);

    int insertSelective(BusRebate record);

    List<BusRebate> selectByExample(BusRebateExample example);

    BusRebate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusRebate record, @Param("example") BusRebateExample example);

    int updateByExample(@Param("record") BusRebate record, @Param("example") BusRebateExample example);

    int updateByPrimaryKeySelective(BusRebate record);

    int updateByPrimaryKey(BusRebate record);

    // ADD
    List<BusRebate> selectRebateByPage(Map<String, Object> params);
}
