package com.holley.charging.bussiness.action;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.def.DcsAlarmEventsModel;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.common.constants.Globals;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;

/**
 * 告警事件相关ACTION
 * 
 * @author shencheng
 */
public class AlarmEventAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private Page              page;
    private DeviceService     deviceService;

    /**
     * 查询预约列表信息
     * 
     * @return
     */
    public String searchEvents() {

        return SUCCESS;
    }

    /**
     * 异步分页查询告警事件
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String searchEventsByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 每页显示条数
        int isExport = this.getParamInt("isExport");// 是否导出excel
        param.put("busMec", webUser.getInfoId());
        param.put("busType", webUser.getUsertype().getValue());
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<DcsAlarmEventsModel> list = deviceService.selectDcsAlarmEventsModelByPage(param);
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
            // List<DcsAlarmEventsModel> exportList = deviceService.exportAlarmEvents(param);
            List<DcsAlarmEventsModel> exportList = deviceService.selectDcsAlarmEventsModelByPage(param);
            String[] headsName = { "充电点名称", "桩名称", "地址", "事件", "事件等级", "事件发生时间" };
            String[] properiesName = { "stationName", "pileName", "address", "described", "levelDesc", "eventTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, DcsAlarmEventsModel.class);
            return null;
        }
        return SUCCESS;
    }

    // SET
    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public Page getPage() {
        return page;
    }

}
