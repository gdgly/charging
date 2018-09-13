package com.holley.charging.service.bussiness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.holley.charging.model.app.ChargeRecordIntro;
import com.holley.charging.model.app.TradeRecord;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusPaymentExample;
import com.holley.charging.model.dcs.DcsChargerecord;
import com.holley.charging.model.dcs.DcsChargerecordExample;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.model.def.StationChargeModel;
import com.holley.common.constants.charge.ChargePayStatusEnum;

/**
 * 充电业务
 * 
 * @author shencheng
 */
public interface ChargingService {

    /**
     * 分页查询所有充电点充电记录
     * 
     * @param param
     * @return
     */

    List<StationChargeModel> selectStationChaByPage(Map<String, Object> param);

    /**
     * 分页查询某个充电点充电记录
     * 
     * @param param
     * @return
     */

    List<ChargeModel> selectChaByPage(Map<String, Object> param);

    /**
     * 分页查询个人充电记录
     * 
     * @param param
     * @return
     */
    List<BusPayment> selectUserChaByPage(Map<String, Object> param);

    /**
     * 分页取交易完成并已支付的充电记录
     * 
     * @param example
     * @return
     */
    List<ChargeRecordIntro> selectFinishedPaymentByPage(Map<String, Object> params);

    /**
     * 分页取交易未完成或者未支付的充电记录
     * 
     * @param example
     * @return
     */
    List<ChargeRecordIntro> selectUnfinishedPaymentByPage(Map<String, Object> params);

    List<BusPayment> selectChargePaymentByExample(BusPaymentExample bpe);

    BusPayment selectSimpleChargePaymentByExample(BusPaymentExample bpexample);

    BusPayment selectPaymentByPK(Integer id);

    int updatePaymentByPKSelective(BusPayment record);

    int updatePaystatusAndAccount(String tradeNo, BigDecimal operateMoney, String accountInfo, ChargePayStatusEnum payStatus);

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

    /**
     * 分页查询充电记录bms
     * 
     * @param params
     * @return
     */
    List<ChargeModel> selectPaymentByPage(Map<String, Object> params);

    List<ChargeModel> selectPaymentNoUserByPage(Map<String, Object> params);

    List<ChargeModel> selectBikePaymentByPage(Map<String, Object> params);

    /**
     * 统计系统用户数据
     * 
     * @param params
     * @return
     */
    BusPayment selectPaymentAllData(Map<String, Object> params);

    BusPayment selectBikePaymentAllData(Map<String, Object> params);

    /**
     * 统计非系统用户数据
     * 
     * @param params
     * @return
     */
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

    /**
     * 查询充电记录DCS
     * 
     * @param example
     * @return
     */
    List<DcsChargerecord> selectDcsChargerecordByExample(DcsChargerecordExample example);

    int updateDcsChargerecordByPrimaryKeySelective(DcsChargerecord record);

    List<ChargeModel> selectBusPaymentList(Map<String, Object> param);

    ChargeModel selectBikePaymentDetail(Integer id);

}
