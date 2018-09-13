package com.holley.charging.dao.bus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.bus.BusRecharge;
import com.holley.charging.model.bus.BusRechargeExample;
import com.holley.charging.model.def.RechargeModel;

public interface BusRechargeMapper {

    int countByExample(BusRechargeExample example);

    int deleteByExample(BusRechargeExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusRecharge record);

    int insertSelective(BusRecharge record);

    List<BusRecharge> selectByExample(BusRechargeExample example);

    BusRecharge selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusRecharge record, @Param("example") BusRechargeExample example);

    int updateByExample(@Param("record") BusRecharge record, @Param("example") BusRechargeExample example);

    int updateByPrimaryKeySelective(BusRecharge record);

    int updateByPrimaryKey(BusRecharge record);

    /**
     * 根据条件计算月总充值金额
     * 
     * @param param
     * @return
     */
    BigDecimal getTotalRechargeFeeByMap(Map<String, Object> param);

    List<BusRecharge> selectByPage(Map<String, Object> params);

    List<RechargeModel> selectRechargeByPage(Map<String, Object> params);

    RechargeModel selectRechargeDetail(Integer id);

    List<BusRecharge> selectUserRechargeByPage(Map<String, Object> params);

    BigDecimal selectUserRechargeTotalFee(Map<String, Object> params);
}
