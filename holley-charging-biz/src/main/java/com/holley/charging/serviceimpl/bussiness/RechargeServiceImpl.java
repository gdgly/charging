package com.holley.charging.serviceimpl.bussiness;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.common.util.AccountLogUtil;
import com.holley.charging.dao.bus.BusAccountMapper;
import com.holley.charging.dao.bus.BusCardRechargeMapper;
import com.holley.charging.dao.bus.BusCashMapper;
import com.holley.charging.dao.bus.BusRechargeMapper;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusCardRecharge;
import com.holley.charging.model.bus.BusCash;
import com.holley.charging.model.bus.BusRecharge;
import com.holley.charging.model.bus.BusRechargeExample;
import com.holley.charging.model.def.RechargeModel;
import com.holley.charging.service.bussiness.RechargeService;
import com.holley.common.constants.charge.AccountLogTypeEnum;
import com.holley.common.constants.charge.RechargeStatusEnum;

public class RechargeServiceImpl implements RechargeService {

    private final static Logger   logger = Logger.getLogger(RechargeServiceImpl.class);
    private BusRechargeMapper     busRechargeMapper;
    private BusAccountMapper      busAccountMapper;
    private BusCardRechargeMapper busCardRechargeMapper;
    private BusCashMapper         busCashMapper;

    @Override
    public BusRecharge selectRechargeByPK(Integer id) {
        return busRechargeMapper.selectByPrimaryKey(id);
    }

    @Override
    public int insertRecharge(BusRecharge record) {
        return busRechargeMapper.insert(record);
    }

    @Override
    public int insertRechargeSelective(BusRecharge record) {
        return busRechargeMapper.insertSelective(record);
    }

    @Override
    public int insertRechargeAndUpdateAccount(Object record) {
        int result = 0;
        if (record instanceof BusRecharge) {
            BusRecharge temp = (BusRecharge) record;
            // 插入充值记录
            result += busRechargeMapper.insertSelective(temp);
            // 更新用户钱包金额
            BusAccount userAccount = busAccountMapper.selectByPrimaryKey(temp.getUserId());
            BigDecimal totalMoney = userAccount.getTotalMoney() == null ? BigDecimal.ZERO : userAccount.getTotalMoney();
            BigDecimal usableMoney = userAccount.getUsableMoney() == null ? BigDecimal.ZERO : userAccount.getUsableMoney();
            totalMoney = totalMoney.add(temp.getMoney());
            usableMoney = usableMoney.add(temp.getMoney());
            userAccount.setTotalMoney(totalMoney);
            userAccount.setUsableMoney(usableMoney);
            userAccount.setUpdateTime(new Date());
            result += busAccountMapper.updateByPrimaryKeySelective(userAccount);
            // 插入资金日志
            AccountLogUtil.insertAccountLog(userAccount, null, temp.getId(), AccountLogTypeEnum.RECHARGE, temp.getMoney(), null, null);
        } else if (record instanceof BusCash) {
            BusCash temp = (BusCash) record;
            result += busCashMapper.insertSelective(temp);
            BusAccount userAccount = busAccountMapper.selectByPrimaryKey(temp.getUserId());
            BigDecimal totalMoney = userAccount.getTotalMoney() == null ? BigDecimal.ZERO : userAccount.getTotalMoney();
            BigDecimal usableMoney = userAccount.getUsableMoney() == null ? BigDecimal.ZERO : userAccount.getUsableMoney();
            totalMoney = totalMoney.subtract(temp.getMoney());
            usableMoney = usableMoney.subtract(temp.getMoney());
            userAccount.setTotalMoney(totalMoney);
            userAccount.setUsableMoney(usableMoney);
            userAccount.setUpdateTime(new Date());
            result += busAccountMapper.updateByPrimaryKeySelective(userAccount);
            // 插入资金日志
            AccountLogUtil.insertAccountLog(userAccount, null, temp.getId(), AccountLogTypeEnum.VALID_CASH, temp.getMoney(), null, temp.getCashStatus());
        }

        return result;
    }

    @Override
    public int updatePaystatusAndAccount(String tradeNo, BigDecimal operateMoney, String accountInfo, RechargeStatusEnum rechargeStatus) {
        int result = 0;
        // 更新充值记录的支付状态
        BusRechargeExample emp = new BusRechargeExample();
        BusRechargeExample.Criteria cr = emp.createCriteria();
        cr.andTradeNoEqualTo(tradeNo);
        List<BusRecharge> list = busRechargeMapper.selectByExample(emp);
        if (list == null || list.size() == 0) {
            logger.info("tradeNo:" + tradeNo + ",充值记录不存在!不处理...");
            return 0;
        }
        BusRecharge record = list.get(0);
        if (RechargeStatusEnum.SUCCESS.getShortValue().equals(record.getStatus())) {
            logger.info("tradeNo:" + tradeNo + ",充值记录的交易状态已经为成功!不处理...");
            return 0;
        }

        if (!rechargeStatus.getShortValue().equals(record.getStatus())) {
            BusRecharge item = new BusRecharge();
            item.setId(record.getId());
            item.setStatus(rechargeStatus.getShortValue());
            item.setAccountInfo(accountInfo);
            item.setAddTime(new Date());
            result += busRechargeMapper.updateByPrimaryKeySelective(item);
        }

        if (RechargeStatusEnum.SUCCESS == rechargeStatus) {
            // 更新用户账户余额
            BusAccount userAccount = busAccountMapper.selectByPrimaryKey(record.getUserId());
            BigDecimal totalMoney = userAccount.getTotalMoney() == null ? BigDecimal.ZERO : userAccount.getTotalMoney();
            BigDecimal usableMoney = userAccount.getUsableMoney() == null ? BigDecimal.ZERO : userAccount.getUsableMoney();
            totalMoney = totalMoney.add(operateMoney);
            usableMoney = usableMoney.add(operateMoney);
            userAccount.setTotalMoney(totalMoney);
            userAccount.setUsableMoney(usableMoney);
            userAccount.setUpdateTime(new Date());
            result += busAccountMapper.updateByPrimaryKeySelective(userAccount);
            // 插入资金日志
            AccountLogUtil.insertAccountLog(userAccount, null, record.getId(), AccountLogTypeEnum.RECHARGE, operateMoney, null, null);
        }
        return result;
    }

    @Override
    public List<RechargeModel> selectRechargeByPage(Map<String, Object> params) {
        return busRechargeMapper.selectRechargeByPage(params);
    }

    @Override
    public RechargeModel selectRechargeDetail(Integer id) {
        return busRechargeMapper.selectRechargeDetail(id);
    }

    @Override
    public List<BusRecharge> selectUserRechargeByPage(Map<String, Object> params) {
        return busRechargeMapper.selectUserRechargeByPage(params);
    }

    @Override
    public BigDecimal selectUserRechargeTotalFee(Map<String, Object> params) {
        return busRechargeMapper.selectUserRechargeTotalFee(params);
    }

    @Override
    public BusCardRecharge selecCardRechargeDetail(Integer id) {
        return busCardRechargeMapper.selectCardRechargeDetail(id);
    }

    public void setBusRechargeMapper(BusRechargeMapper busRechargeMapper) {
        this.busRechargeMapper = busRechargeMapper;
    }

    public void setBusAccountMapper(BusAccountMapper busAccountMapper) {
        this.busAccountMapper = busAccountMapper;
    }

    public void setBusCardRechargeMapper(BusCardRechargeMapper busCardRechargeMapper) {
        this.busCardRechargeMapper = busCardRechargeMapper;
    }

    public void setBusCashMapper(BusCashMapper busCashMapper) {
        this.busCashMapper = busCashMapper;
    }

}
