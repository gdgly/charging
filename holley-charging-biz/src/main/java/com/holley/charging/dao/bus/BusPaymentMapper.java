package com.holley.charging.dao.bus;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.app.ChargeRecordIntro;
import com.holley.charging.model.app.TradeRecord;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusPaymentExample;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.model.def.ProfitModel;
import com.holley.charging.model.def.StationChargeModel;

public interface BusPaymentMapper {

    int countByExample(BusPaymentExample example);

    int deleteByExample(BusPaymentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusPayment record);

    int insertSelective(BusPayment record);

    List<BusPayment> selectByExample(BusPaymentExample example);

    BusPayment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusPayment record, @Param("example") BusPaymentExample example);

    int updateByExample(@Param("record") BusPayment record, @Param("example") BusPaymentExample example);

    int updateByPrimaryKeySelective(BusPayment record);

    int updateByPrimaryKey(BusPayment record);

    /**
     * 获取年，月，季度的服务费，停车费，充电费统计数据
     * 
     * @param param
     * @return
     */
    List<ProfitModel> createYMQProfit(Map<String, Object> param);

    // 分页查询所有充电点充电记录

    List<StationChargeModel> selectStationChaByPage(Map<String, Object> param);

    // 分页查询某个充电点充电记录

    List<ChargeModel> selectChaByPage(Map<String, Object> param);

    /**
     * 按条件统计充电桩收益排行
     * 
     * @param param
     * @return
     */
    List<ProfitModel> countProfit(Map<String, Object> param);

    BusPayment selectOneRecordByExample(BusPaymentExample bpexample);

    /**
     * 根据条件查询符合条件的充电记录
     * 
     * @param param
     * @return
     */
    List<BusPayment> selectPaymentByMap(Map<String, Object> param);

    /**
     * 计算总充电费用
     * 
     * @param param
     * @return
     */
    List<ChargeModel> getTotalChaFeeByMap(Map<String, Object> param);

    /**
     * 批量更新充电记录
     * 
     * @param busPaymentList
     * @return
     */
    int updatePaymentBatch(List<BusPayment> busPaymentList);

    List<TradeRecord> selectTradeDetailByParamByPage(Map<String, Object> params);

    /**
     * 导出充电点充电统计Excel
     * 
     * @param param
     * @return
     */
    List<StationChargeModel> exportStationCha(Map<String, Object> param);

    /**
     * 导出单一充电点充电记录Excel
     * 
     * @param param
     * @return
     */
    List<ChargeModel> exportCharge(Map<String, Object> param);

    List<ChargeRecordIntro> selectFinishedPaymentByPage(Map<String, Object> params);

    List<ChargeRecordIntro> selectUnfinishedPaymentByPage(Map<String, Object> params);

    /**
     * 分页查询个人充电记录
     * 
     * @param param
     * @return
     */
    List<BusPayment> selectUserChaByPage(Map<String, Object> param);

    /**
     * 分页查询充电记录 bms
     * 
     * @param params
     * @return
     */
    List<ChargeModel> selectPaymentByPage(Map<String, Object> params);

    List<ChargeModel> selectPaymentNoUserByPage(Map<String, Object> params);

    BusPayment selectPaymentAllData(Map<String, Object> params);

    BusPayment selectPaymentNoUserAllData(Map<String, Object> params);

    ChargeModel selectPaymentDetail(Integer id);

    ChargeModel selectNoUserPaymentDetail(Integer id);

    /**
     * 查询灰色记录
     * 
     * @param params
     * @return
     */
    List<ChargeModel> selectBadCardPaymentByMap(Map<String, Object> params);

    List<ChargeModel> selectBusPaymentList(Map<String, Object> param);

    List<ChargeModel> selectBikePaymentByPage(Map<String, Object> params);

    BusPayment selectBikePaymentAllData(Map<String, Object> params);

    ChargeModel selectBikePaymentDetail(Integer id);
}
