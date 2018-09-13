package com.holley.charging.dao.bus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.bus.BusCash;
import com.holley.charging.model.bus.BusCashExample;

public interface BusCashMapper {

    int countByExample(BusCashExample example);

    int deleteByExample(BusCashExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusCash record);

    int insertSelective(BusCash record);

    List<BusCash> selectByExample(BusCashExample example);

    BusCash selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusCash record, @Param("example") BusCashExample example);

    int updateByExample(@Param("record") BusCash record, @Param("example") BusCashExample example);

    int updateByPrimaryKeySelective(BusCash record);

    int updateByPrimaryKey(BusCash record);

    // 分页查询所有提现记录
    List<BusCash> selectBusCashByPage(Map<String, Object> param);

    // 分页查询所有提现审核记录
    List<BusCash> selectCashVerifyByPage(Map<String, Object> param);

    // 根据id查询运营商提现记录
    BusCash selectEnterpriseCashById(Integer id);

    // 根据id查询个人提现记录
    BusCash selectPersionCashById(Integer id);

    /**
     * 根据条件计算月总提现金额
     * 
     * @param param
     * @return
     */
    BigDecimal getTotalCashByMap(Map<String, Object> param);

    /**
     * 导出提现记录Excel
     * 
     * @param param
     * @return
     */
    List<BusCash> exportCashs(Map<String, Object> param);

    List<BusCash> selectCashFreezeMoneyByPage(Map<String, Object> params);

    BigDecimal selectCashFreezeMoneyTotal(Map<String, Object> params);

    List<BusCash> selectUserCashByPage(Map<String, Object> params);

    BigDecimal selectUserCashTotalMoney(Map<String, Object> params);
}
