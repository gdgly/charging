package com.holley.charging.serviceimpl.bussiness;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.common.util.AccountLogUtil;
import com.holley.charging.dao.bus.BusAccountMapper;
import com.holley.charging.dao.bus.BusAppointmentMapper;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusAppointmentExample;
import com.holley.charging.model.def.AppointmentModel;
import com.holley.charging.model.def.StationAppointmentModel;
import com.holley.charging.service.bussiness.AppointmentService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.AccountLogTypeEnum;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.AppointmentStatusEnum;

public class AppointmentServiceImpl implements AppointmentService {

    private final static Logger  logger = Logger.getLogger(AppointmentServiceImpl.class);
    private BusAppointmentMapper busAppointmentMapper;
    private BusAccountMapper     busAccountMapper;

    public List<BusAppointment> selectAppointmentByExample(BusAppointmentExample emp) {
        return busAppointmentMapper.selectByExample(emp);
    }

    public List<BusAppointment> selectAppointmentByExampleByPage(BusAppointmentExample emp) {
        return busAppointmentMapper.selectAppointmentByExampleByPage(emp);
    }

    public List<StationAppointmentModel> selectStationAppByPage(Map<String, Object> param) {
        return busAppointmentMapper.selectStationAppByPage(param);
    }

    public List<AppointmentModel> selectAppByPage(Map<String, Object> param) {
        return busAppointmentMapper.selectAppByPage(param);
    }

    public BusAppointment selectAppointmentByPK(Integer id) {
        return busAppointmentMapper.selectByPrimaryKey(id);
    }

    public int updateAppointmentByPKSelective(BusAppointment record) {
        return busAppointmentMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updatePaystatusAndAccount(String tradeNo, BigDecimal operateMoney, String accountInfo, AppointmentPayStatusEnum payStatus) {
        // 更新预约记录的支付状态
        int result = 0;
        BusAppointmentExample emp = new BusAppointmentExample();
        BusAppointmentExample.Criteria cr = emp.createCriteria();
        cr.andAppNoEqualTo(tradeNo);
        List<BusAppointment> list = busAppointmentMapper.selectByExample(emp);
        if (list == null || list.size() == 0) {
            logger.info("tradeNo:" + tradeNo + ",预约记录不存在!不处理...");
            return 0;
        }
        BusAppointment record = list.get(0);
        if (AppointmentPayStatusEnum.SUCCESS.getShortValue().equals(record.getPayStatus())) {
            logger.info("tradeNo:" + tradeNo + ",预约记录的支付状态已经为成功!不处理...");
            return 0;
        }

        if (!payStatus.getShortValue().equals(record.getPayStatus())) {
            BusAppointment item = new BusAppointment();
            item.setId(record.getId());
            item.setPayStatus(payStatus.getShortValue());
            item.setAccountInfo(accountInfo);
            item.setAddTime(new Date());
            result += busAppointmentMapper.updateByPrimaryKeySelective(item);
        }
        if (AppointmentPayStatusEnum.SUCCESS.equals(payStatus)) {
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
                AccountLogUtil.insertAccountLog(userAccount, platformAccount, record.getId(), AccountLogTypeEnum.UNACC_APPOINTMENT, operateMoney, null, null);
            }
        }
        return result;
    }

    public int insertAppointmentSelective(BusAppointment apprecord) {
        return busAppointmentMapper.insertSelective(apprecord);
    }

    @Override
    public int insertRenewAppointment(BusAppointment newrecord, Integer oldrecordid) {
        int result = 0;
        result += busAppointmentMapper.insertSelective(newrecord);
        BusAppointment record = new BusAppointment();
        record.setId(oldrecordid);
        record.setAppStatus(AppointmentStatusEnum.RENEW.getShortValue());
        result += busAppointmentMapper.updateByPrimaryKeySelective(record);
        return result;
    }

    @Override
    public List<StationAppointmentModel> exportStationApp(Map<String, Object> param) {
        return busAppointmentMapper.exportStationApp(param);
    }

    @Override
    public List<AppointmentModel> exportAppointment(Map<String, Object> param) {
        return busAppointmentMapper.exportAppointment(param);
    }

    @Override
    public List<BusAppointment> selectUserAppByPage(Map<String, Object> param) {
        return busAppointmentMapper.selectUserAppByPage(param);
    }

    @Override
    public List<AppointmentModel> selectAppointmentByPage(Map<String, Object> params) {
        return busAppointmentMapper.selectAppointmentByPage(params);
    }

    @Override
    public AppointmentModel selectAppointmentDetail(Integer id) {
        return busAppointmentMapper.selectAppointmentDetail(id);
    }

    // SET
    public void setBusAppointmentMapper(BusAppointmentMapper busAppointmentMapper) {
        this.busAppointmentMapper = busAppointmentMapper;
    }

    public void setBusAccountMapper(BusAccountMapper busAccountMapper) {
        this.busAccountMapper = busAccountMapper;
    }

}
