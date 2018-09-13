package com.holley.charging.dao.bus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.bms.BillsInfo;
import com.holley.charging.model.bus.BusBills;
import com.holley.charging.model.bus.BusBillsExample;

public interface BusBillsMapper {

    int countByExample(BusBillsExample example);

    int deleteByExample(BusBillsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusBills record);

    int insertSelective(BusBills record);

    List<BusBills> selectByExample(BusBillsExample example);

    BusBills selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusBills record, @Param("example") BusBillsExample example);

    int updateByExample(@Param("record") BusBills record, @Param("example") BusBillsExample example);

    int updateByPrimaryKeySelective(BusBills record);

    int updateByPrimaryKey(BusBills record);

    // add
    List<BusBills> selectBusBillsByMap(Map<String, Object> param);

    BigDecimal selectUserBillTotalIn(Map<String, Object> params);

    /**
     * 分页查询账单
     * 
     * @param params
     * @return
     */
    List<BusBills> selectBusBillsByPage(Map<String, Object> param);

    List<BusBills> selectGroupBillsByPage(Map<String, Object> param);

    List<BillsInfo> selectBillsByPage(Map<String, Object> params);

}
