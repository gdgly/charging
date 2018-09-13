package com.holley.charging.dao.bus;

import com.holley.charging.model.bus.BusUserRebate;
import com.holley.charging.model.bus.BusUserRebateExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface BusUserRebateMapper {
    int countByExample(BusUserRebateExample example);

    int deleteByExample(BusUserRebateExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusUserRebate record);

    int insertSelective(BusUserRebate record);

    List<BusUserRebate> selectByExample(BusUserRebateExample example);

    BusUserRebate selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusUserRebate record, @Param("example") BusUserRebateExample example);

    int updateByExample(@Param("record") BusUserRebate record, @Param("example") BusUserRebateExample example);

    int updateByPrimaryKeySelective(BusUserRebate record);

    int updateByPrimaryKey(BusUserRebate record);
}