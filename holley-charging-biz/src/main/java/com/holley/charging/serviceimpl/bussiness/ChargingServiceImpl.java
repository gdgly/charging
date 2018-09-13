package com.holley.charging.serviceimpl.bussiness;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.common.util.AccountLogUtil;
import com.holley.charging.dao.bus.BusAccountMapper;
import com.holley.charging.dao.bus.BusPaymentMapper;
import com.holley.charging.dao.dcs.DcsChargerecordMapper;
import com.holley.charging.model.app.ChargeRecordIntro;
import com.holley.charging.model.app.TradeRecord;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusPaymentExample;
import com.holley.charging.model.dcs.DcsChargerecord;
import com.holley.charging.model.dcs.DcsChargerecordExample;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.model.def.StationChargeModel;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.AccountLogTypeEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;

public class ChargingServiceImpl implements ChargingService {

    private final static Logger   logger = Logger.getLogger(ChargingServiceImpl.class);
    private BusPaymentMapper      busPaymentMapper;
    private BusAccountMapper      busAccountMapper;
    private DcsChargerecordMapper dcsChargerecordMapper;

    public List<StationChargeModel> selectStationChaByPage(Map<String, Object> param) {
        return busPaymentMapper.selectStationChaByPage(param);
    }

    public List<ChargeModel> selectChaByPage(Map<String, Object> param) {
        return busPaymentMapper.selectChaByPage(param);
    }

    public List<BusPayment> selectChargePaymentByExample(BusPaymentExample bpe) {
        return busPaymentMapper.selectByExample(bpe);
    }

    public BusPayment selectSimpleChargePaymentByExample(BusPaymentExample bpexample) {
        return busPaymentMapper.selectOneRecordByExample(bpexample);
    }

    @Override
    public BusPayment selectPaymentByPK(Integer id) {
        return busPaymentMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<ChargeRecordIntro> selectFinishedPaymentByPage(Map<String, Object> params) {
        return busPaymentMapper.selectFinishedPaymentByPage(params);
    }

    @Override
    public List<ChargeRecordIntro> selectUnfinishedPaymentByPage(Map<String, Object> params) {
        return busPaymentMapper.selectUnfinishedPaymentByPage(params);
    }

    @Override
    public int updatePaymentByPKSelective(BusPayment record) {
        return busPaymentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updatePaystatusAndAccount(String tradeNo, BigDecimal operateMoney, String accountInfo, ChargePayStatusEnum payStatus) {
        // 更新充电缴费记录的支付状态
        int result = 0;
        BusPaymentExample emp = new BusPaymentExample();
        BusPaymentExample.Criteria cr = emp.createCriteria();
        cr.andTradeNoEqualTo(tradeNo);
        List<BusPayment> list = selectChargePaymentByExample(emp);
        if (list == null || list.size() == 0) {
            logger.info("tradeNo:" + tradeNo + ",充电记录不存在!不处理...");
            return 0;
        }
        BusPayment record = list.get(0);
        if (ChargePayStatusEnum.SUCCESS.getShortValue().equals(record.getPayStatus())) {
            logger.info("tradeNo:" + tradeNo + ",充电记录的支付状态已经为成功!不处理...");
            return 0;
        }

        if (!payStatus.getShortValue().equals(record.getPayStatus())) {
            BusPayment item = new BusPayment();
            item.setId(record.getId());
            item.setPayStatus(payStatus.getShortValue());
            if (operateMoney != null) {
                item.setActualMoney(operateMoney);
            }
            item.setAccountInfo(accountInfo);
            item.setUpdateTime(new Date());
            result += updatePaymentByPKSelective(item);
        }

        if (ChargePayStatusEnum.SUCCESS == payStatus) {
            // 更新平台账户
            BusAccount platformAccount = busAccountMapper.selectByPrimaryKey(Globals.ADMIN_USER_ID);
            BigDecimal totalMoney = platformAccount.getTotalMoney();
            BigDecimal usableMoney = platformAccount.getUsableMoney();
            totalMoney = totalMoney.add(operateMoney);
            usableMoney = usableMoney.add(operateMoney);

            platformAccount.setTotalMoney(totalMoney);
            platformAccount.setUsableMoney(usableMoney);
            platformAccount.setUpdateTime(new Date());
            if (busAccountMapper.updateByPrimaryKeySelective(platformAccount) > 0) {
                result += 1;
                // 插入资金日志
                BusAccount userAccount = busAccountMapper.selectByPrimaryKey(record.getUserId());
                AccountLogUtil.insertAccountLog(userAccount, platformAccount, record.getId(), AccountLogTypeEnum.UNACC_CHARGING, operateMoney, null, null);
            }
        }
        return result;
    }

    @Override
    public List<TradeRecord> selectTradeDetailByParamByPage(Map<String, Object> params) {
        return busPaymentMapper.selectTradeDetailByParamByPage(params);
    }

    @Override
    public List<StationChargeModel> exportStationCha(Map<String, Object> param) {
        return busPaymentMapper.exportStationCha(param);
    }

    @Override
    public List<ChargeModel> exportCharge(Map<String, Object> param) {
        return busPaymentMapper.exportCharge(param);
    }

    @Override
    public List<BusPayment> selectUserChaByPage(Map<String, Object> param) {
        return busPaymentMapper.selectUserChaByPage(param);
    }

    @Override
    public List<ChargeModel> selectPaymentByPage(Map<String, Object> params) {
        return busPaymentMapper.selectPaymentByPage(params);
    }

    @Override
    public ChargeModel selectPaymentDetail(Integer id) {
        return busPaymentMapper.selectPaymentDetail(id);
    }

    @Override
    public List<ChargeModel> selectBadCardPaymentByMap(Map<String, Object> params) {
        return busPaymentMapper.selectBadCardPaymentByMap(params);
    }

    @Override
    public List<DcsChargerecord> selectDcsChargerecordByExample(DcsChargerecordExample example) {
        return dcsChargerecordMapper.selectByExample(example);
    }

    @Override
    public int updateDcsChargerecordByPrimaryKeySelective(DcsChargerecord record) {
        return dcsChargerecordMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public List<ChargeModel> selectPaymentNoUserByPage(Map<String, Object> params) {
        return busPaymentMapper.selectPaymentNoUserByPage(params);
    }

    @Override
    public ChargeModel selectNoUserPaymentDetail(Integer id) {
        return busPaymentMapper.selectNoUserPaymentDetail(id);
    }

    @Override
    public BusPayment selectPaymentAllData(Map<String, Object> params) {
        return busPaymentMapper.selectPaymentAllData(params);
    }

    @Override
    public BusPayment selectPaymentNoUserAllData(Map<String, Object> params) {
        return busPaymentMapper.selectPaymentNoUserAllData(params);
    }

    @Override
    public List<ChargeModel> selectBusPaymentList(Map<String, Object> param) {
        return busPaymentMapper.selectBusPaymentList(param);
    }

    // ---get and set
    public void setBusPaymentMapper(BusPaymentMapper busPaymentMapper) {
        this.busPaymentMapper = busPaymentMapper;
    }

    public void setBusAccountMapper(BusAccountMapper busAccountMapper) {
        this.busAccountMapper = busAccountMapper;
    }

    public void setDcsChargerecordMapper(DcsChargerecordMapper dcsChargerecordMapper) {
        this.dcsChargerecordMapper = dcsChargerecordMapper;
    }

    @Override
    public List<ChargeModel> selectBikePaymentByPage(Map<String, Object> params) {
        return busPaymentMapper.selectBikePaymentByPage(params);
    }

    @Override
    public BusPayment selectBikePaymentAllData(Map<String, Object> params) {
        return busPaymentMapper.selectBikePaymentAllData(params);
    }

    @Override
    public ChargeModel selectBikePaymentDetail(Integer id) {
        return busPaymentMapper.selectBikePaymentDetail(id);
    }

}
