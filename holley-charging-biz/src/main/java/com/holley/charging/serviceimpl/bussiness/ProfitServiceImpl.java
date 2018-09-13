package com.holley.charging.serviceimpl.bussiness;

import java.util.List;
import java.util.Map;

import com.holley.charging.dao.bus.BusAppointmentMapper;
import com.holley.charging.dao.bus.BusPaymentMapper;
import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.def.ProfitModel;
import com.holley.charging.service.bussiness.ProfitService;

public class ProfitServiceImpl implements ProfitService {

    private BusAppointmentMapper busAppointmentMapper;
    private BusPaymentMapper     busPaymentMapper;

    public List<ProfitModel> createAppProfit(Map<String, Object> param) {
        return busAppointmentMapper.createAppProfit(param);
    }

    public List<ProfitModel> createYMQProfit(Map<String, Object> param) {
        return busPaymentMapper.createYMQProfit(param);
    }

    @Override
    public List<ProfitModel> countProfit(Map<String, Object> param) {
        return busPaymentMapper.countProfit(param);
    }

    @Override
    public int countAppiontment(Map<String, Object> param) {
        List<BusAppointment> list = busAppointmentMapper.selectAppointmentByMap(param);
        return list == null ? 0 : list.size();
    }

    @Override
    public int countCharge(Map<String, Object> param) {
        List<BusPayment> list = busPaymentMapper.selectPaymentByMap(param);
        return list == null ? 0 : list.size();
    }

    // SET
    public void setBusAppointmentMapper(BusAppointmentMapper busAppointmentMapper) {
        this.busAppointmentMapper = busAppointmentMapper;
    }

    public void setBusPaymentMapper(BusPaymentMapper busPaymentMapper) {
        this.busPaymentMapper = busPaymentMapper;
    }

}
