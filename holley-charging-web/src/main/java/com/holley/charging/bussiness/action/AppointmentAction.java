package com.holley.charging.bussiness.action;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.def.AppointmentModel;
import com.holley.charging.model.def.StationAppointmentModel;
import com.holley.charging.service.bussiness.AppointmentService;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.RoleUtil;

/**
 * 预约相关ACTION
 * 
 * @author shencheng
 */
public class AppointmentAction extends BaseAction {

    private Page               page;

    private AppointmentService appointmentService;

    private static final long  serialVersionUID = 1L;

    /**
     * 查询预约列表信息
     * 
     * @return
     * @throws Exception
     */
    public String searchAppointments() throws Exception {
        return SUCCESS;
    }

    /**
     * ajax分页查询充电点预约记录信息
     * 
     * @return
     * @throws Exception
     */
    public String searchStationAppointmentsByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        String searchKeyName = this.getParameter("searchKeyName");
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        param.put("busType", webUser.getUsertype().getShortValue());
        param.put("busMec", webUser.getInfoId());
        param.put("rate", rate);
        if (!StringUtil.isEmpty(searchKeyName)) {
            param.put("searchStationName", searchKeyName);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<StationAppointmentModel> list = appointmentService.selectStationAppByPage(param);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        } else {
            // List<StationAppointmentModel> exportList = appointmentService.exportStationApp(param);
            List<StationAppointmentModel> exportList = appointmentService.selectStationAppByPage(param);
            String[] headsName = { "充电点名称", "地址", "桩数量", "预约次数", "预约金额" };
            String[] properiesName = { "stationName", "address", "pileNum", "appNum", "appFee" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, StationAppointmentModel.class);
            return null;
        }

    }

    /**
     * 分页查询具体充电点分页信息
     * 
     * @return
     * @throws Exception
     */
    public String searchAppointmentsByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        String searchKeyName = this.getParameter("searchKeyName");
        String searchTime = this.getParameter("datetimeInput");
        int stationId = this.getParamInt("stationId");
        param.put("busType", webUser.getUsertype().getShortValue());
        param.put("busMec", webUser.getInfoId());
        if (stationId > 0) {
            param.put("stationId", stationId);
        }
        if (!StringUtil.isEmpty(searchKeyName)) {
            param.put("searchPileName", searchKeyName);
        }
        if (!StringUtil.isEmpty(searchTime)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM");
            Calendar cr = Calendar.getInstance();
            cr.setTime(format.parse(searchTime));
            int year = cr.get(Calendar.YEAR);
            int month = cr.get(Calendar.MONTH) + 1;
            param.put("year", year);
            param.put("month", month);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<AppointmentModel> list = appointmentService.selectAppByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            param.put("maxLimit", MAX_EXPORT);
            // Date now = new Date();
            // Calendar calendar = Calendar.getInstance();
            // calendar.setTime(now);
            // calendar.add(Calendar.MONTH, DateUtil.PRE_THREE_MONTH);// 获取上三个月日期
            // param.put("startTime", calendar.getTime());
            // param.put("endTime", now);
            // List<AppointmentModel> exportList = appointmentService.exportAppointment(param);
            List<AppointmentModel> exportList = appointmentService.selectAppByPage(param);
            String[] headsName = { "桩名称", "桩类型", "用户名", "手机号码", "预约状态", "预约时长", "预约费用", "入账状态", "预约时间" };
            String[] properiesName = { "pileName", "pileTypeDesc", "username", "phone", "appStatusDesc", "appLen", "appFeeDesc", "isBillDesc", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, AppointmentModel.class);
            return null;
        }
        return SUCCESS;
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public Page getPage() {
        return page;
    }

}
