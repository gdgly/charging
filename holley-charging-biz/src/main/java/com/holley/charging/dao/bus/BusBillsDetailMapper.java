package com.holley.charging.dao.bus;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.app.TradeRecord;
import com.holley.charging.model.bms.BillsDetail;
import com.holley.charging.model.bms.BillsDetailIn;
import com.holley.charging.model.bus.BusBillsDetail;
import com.holley.charging.model.bus.BusBillsDetailExample;
import com.holley.charging.model.def.BillsDetailModel;

public interface BusBillsDetailMapper {

    int countByExample(BusBillsDetailExample example);

    int deleteByExample(BusBillsDetailExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusBillsDetail record);

    int insertSelective(BusBillsDetail record);

    List<BusBillsDetail> selectByExample(BusBillsDetailExample example);

    BusBillsDetail selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusBillsDetail record, @Param("example") BusBillsDetailExample example);

    int updateByExample(@Param("record") BusBillsDetail record, @Param("example") BusBillsDetailExample example);

    int updateByPrimaryKeySelective(BusBillsDetail record);

    int updateByPrimaryKey(BusBillsDetail record);

    /**
     * 批量插入资金结算细表
     * 
     * @param billsDetailList
     * @return
     */
    int insertBillsDetailBatch(List<BusBillsDetail> billsDetailList);

    /**
     * 分页查询详细月账单
     * 
     * @param param
     * @return
     */
    List<BillsDetailModel> selectBillsDetailByPage(Map<String, Object> param);

    /**
     * 导出月账单
     * 
     * @param param
     * @return
     */
    List<BillsDetailModel> exportBillsDetail(Map<String, Object> param);

    /**
     * 我的设备交易明细（充电、预约）
     */
    List<TradeRecord> selectBillDetailByParamByPage(Map<String, Object> params);

    /**
     * 分页查询收入明细
     * 
     * @param params
     * @return
     */
    List<BillsDetailIn> selectBillsDetailInByPage(Map<String, Object> params);

    BusBillsDetail selectBillsDetailInTotal(Map<String, Object> params);

    List<BillsDetail> selectBillsDetailOutByPage(Map<String, Object> params);

    BillsDetail selectBillsDetailOutTotal(Map<String, Object> params);
}
