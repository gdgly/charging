package com.holley.charging.service.bussiness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.holley.charging.model.bus.BusCardRecharge;
import com.holley.charging.model.bus.BusRecharge;
import com.holley.charging.model.def.RechargeModel;
import com.holley.common.constants.charge.RechargeStatusEnum;

/**
 * 充值业务
 */
public interface RechargeService {

    BusRecharge selectRechargeByPK(Integer id);

    int insertRecharge(BusRecharge record);

    int insertRechargeSelective(BusRecharge record);

    int insertRechargeAndUpdateAccount(Object record);

    int updatePaystatusAndAccount(String tradeNo, BigDecimal operateMoney, String accountInfo, RechargeStatusEnum rechargeStatus);

    /**
     * 查询充值记录bms
     * 
     * @param params
     * @return
     */
    List<RechargeModel> selectRechargeByPage(Map<String, Object> params);

    RechargeModel selectRechargeDetail(Integer id);

    List<BusRecharge> selectUserRechargeByPage(Map<String, Object> params);

    BigDecimal selectUserRechargeTotalFee(Map<String, Object> params);

    BusCardRecharge selecCardRechargeDetail(Integer id);

}
