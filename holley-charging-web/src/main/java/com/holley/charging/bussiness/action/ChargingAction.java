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
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.model.def.StationChargeModel;
import com.holley.charging.service.bussiness.ChargingService;
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
public class ChargingAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private ChargingService   chargingService;
    private Page              page;

    /**
     * 查询充电点充电记录列表信息
     * 
     * @return
     */
    public String searchCharges() {
        return SUCCESS;
    }

    /**
     * ajax分页查询充所有电点充电记录信息
     * 
     * @return
     * @throws Exception
     */
    public String searchStationChargesByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int limit = this.getParamInt("limit");// 显示行数
        String searchKeyName = this.getParameter("searchKeyName");
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        param.put("busType", webUser.getUsertype().getValue());
        param.put("busMec", webUser.getInfoId());
        param.put("rate", rate);
        if (!StringUtil.isEmpty(searchKeyName)) {
            param.put("searchStationName", searchKeyName);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<StationChargeModel> list = chargingService.selectStationChaByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            // List<StationChargeModel> exportList = chargingService.exportStationCha(param);
            List<StationChargeModel> exportList = chargingService.selectStationChaByPage(param);
            String[] headsName = { "充电点名称", "地址", "桩数量", "充电次数", "充电金额" };
            String[] properiesName = { "stationName", "address", "pileNum", "chaNum", "totalFeeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, StationChargeModel.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 分页查询具体充电点分页信息
     * 
     * @return
     * @throws Exception
     */
    public String searchChargesByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int currentPage = this.getParamInt("currentPage");// 当前页
        int stationId = this.getParamInt("stationId");
        int limit = this.getParamInt("limit");// 显示行数
        String searchKeyName = this.getParameter("searchKeyName");
        String searchTime = this.getParameter("datetimeInput");
        if (stationId == 0) {
            param.put("busType", webUser.getUsertype().getValue());
            param.put("busMec", webUser.getInfoId());
        } else {
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
            List<ChargeModel> list = chargingService.selectChaByPage(param);
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
            // List<ChargeModel> exportList = chargingService.exportCharge(param);
            List<ChargeModel> exportList = chargingService.selectChaByPage(param);
            String[] headsName = { "桩名称", "桩类型", "用户名", "手机号码", "激费状态", "充电费用", "服务费用", "停车费用", "入账状态", "充电时间" };
            String[] properiesName = { "pileName", "pileTypeDesc", "username", "phone", "payStatusDesc", "chaFeeDesc", "serviceFeeDesc", "parkFeeDesc", "isBillDesc",
                    "updateTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, AppointmentModel.class);
            return null;
        }

        return SUCCESS;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public Page getPage() {
        return page;
    }
}
