package com.holley.charging.dao.bus;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusAppointmentExample;
import com.holley.charging.model.def.AppointmentModel;
import com.holley.charging.model.def.ProfitModel;
import com.holley.charging.model.def.StationAppointmentModel;

public interface BusAppointmentMapper {

    int countByExample(BusAppointmentExample example);

    int deleteByExample(BusAppointmentExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusAppointment record);

    int insertSelective(BusAppointment record);

    List<BusAppointment> selectByExample(BusAppointmentExample example);

    BusAppointment selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusAppointment record, @Param("example") BusAppointmentExample example);

    int updateByExample(@Param("record") BusAppointment record, @Param("example") BusAppointmentExample example);

    int updateByPrimaryKeySelective(BusAppointment record);

    int updateByPrimaryKey(BusAppointment record);

    // 分页查询所有充电点预约记录

    List<StationAppointmentModel> selectStationAppByPage(Map<String, Object> param);

    // 分页查询某个充电点预约记录

    List<AppointmentModel> selectAppByPage(Map<String, Object> param);

    List<ProfitModel> createAppProfit(Map<String, Object> param);

    List<BusAppointment> selectAppointmentByExampleByPage(BusAppointmentExample emp);

    /**
     * 查询符合条件的预约记录 用于资金结算服务
     * 
     * @param emp
     * @return
     */

    List<BusAppointment> selectAppointmentByMap(Map<String, Object> param);

    /**
     * 根据条件计算所有预约支出
     * 
     * @param param
     * @return
     */
    BigDecimal getTotalAppFeeByMap(Map<String, Object> param);

    /**
     * 批量更新预约记录
     * 
     * @param billsDetailList
     * @return
     */
    int updateAppointmentBatch(List<BusAppointment> busAppointmentList);

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
     * 分页查询个人预约记录
     * 
     * @param param
     * @return
     */
    List<BusAppointment> selectUserAppByPage(Map<String, Object> param);

    /**
     * 分页查询预约记录bms
     * 
     * @param params
     * @return
     */
    List<AppointmentModel> selectAppointmentByPage(Map<String, Object> params);

    AppointmentModel selectAppointmentDetail(Integer id);
}
