package com.holley.charging.service.bussiness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusAppointmentExample;
import com.holley.charging.model.def.AppointmentModel;
import com.holley.charging.model.def.StationAppointmentModel;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;

/**
 * 预约业务
 * 
 * @author shencheng
 */
public interface AppointmentService {

    List<BusAppointment> selectAppointmentByExample(BusAppointmentExample emp);

    /**
     * 分页example查询
     * 
     * @param emp
     * @return
     */
    List<BusAppointment> selectAppointmentByExampleByPage(BusAppointmentExample emp);

    /**
     * 分页查询所有充电点预约记录
     * 
     * @param param
     * @return
     */

    List<StationAppointmentModel> selectStationAppByPage(Map<String, Object> param);

    /**
     * 导出充点电预约统计Excel
     * 
     * @param param
     * @return
     */
    List<StationAppointmentModel> exportStationApp(Map<String, Object> param);

    /**
     * 导出单一充电点预约记录Excel
     * 
     * @param param
     * @return
     */
    List<AppointmentModel> exportAppointment(Map<String, Object> param);

    /**
     * 分页查询某个充电点预约记录
     * 
     * @param param
     * @return
     */

    List<AppointmentModel> selectAppByPage(Map<String, Object> param);

    BusAppointment selectAppointmentByPK(Integer id);

    int updateAppointmentByPKSelective(BusAppointment record);

    int insertAppointmentSelective(BusAppointment apprecord);

    int updatePaystatusAndAccount(String tradeNo, BigDecimal operateMoney, String accountInfo, AppointmentPayStatusEnum payStatus);

    /**
     * 插入续约记录
     * 
     * @param newrecord新的续约记录
     * @param oldrecordid原来的预约记录id
     * @return
     */
    int insertRenewAppointment(BusAppointment newrecord, Integer oldrecordid);

    /**
     * 分页查询个人预约记录
     * 
     * @param param
     * @return
     */
    List<BusAppointment> selectUserAppByPage(Map<String, Object> param);

    /**
     * 预约记录查询(BMS)
     */
    List<AppointmentModel> selectAppointmentByPage(Map<String, Object> params);

    AppointmentModel selectAppointmentDetail(Integer id);
}
