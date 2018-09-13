package com.holley.charging.bms.deal.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.def.AppointmentModel;
import com.holley.charging.service.bussiness.AppointmentService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.AppointmentStatusEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;

/**
 * 预约纪录
 * 
 * @author zdd
 */
public class AppointmentAction extends BaseAction {

    private static final long  serialVersionUID = 1L;
    private AppointmentService appointmentService;
    private Page               page;

    /**
     * 预约记录列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("payStatusList", AppointmentPayStatusEnum.values());
        this.getRequest().setAttribute("appStatusList", AppointmentStatusEnum.values());
        this.getRequest().setAttribute("billStatusList", WhetherEnum.values());
        return SUCCESS;
    }

    /**
     * 预约记录详细初始化
     * 
     * @return
     */
    public String detailInit() {
        String id = this.getParameter("appointmentid");
        if (!NumberUtils.isNumber(id)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        AppointmentModel record = appointmentService.selectAppointmentDetail(Integer.valueOf(id));
        dealAppStatus(record);
        this.getRequest().setAttribute("appointment", record);
        return SUCCESS;
    }

    /**
     * 分页查询预约记录
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String searchuser = this.getParameter("searchuser");
        String searchstation = this.getParameter("searchstation");
        String searchappno = this.getParameter("searchappno");
        String appstatus = this.getParameter("appstatus");
        String paystatus = this.getParameter("paystatus");
        String billstatus = this.getParameter("billstatus");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(appstatus, paystatus, billstatus)) {
            this.success = false;
            this.message = "参数非法!";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startdate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("enddate", DateUtil.ShortStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(searchuser)) {
            params.put("searchuser", searchuser);
        }
        if (StringUtil.isNotEmpty(searchstation)) {
            params.put("searchstation", searchstation);
        }
        if (StringUtil.isNotEmpty(searchappno)) {
            params.put("searchappno", searchappno);
        }
        if (StringUtil.isNotEmpty(paystatus) && !"0".equals(paystatus)) {
            params.put("paystatus", paystatus);
        }
        if (StringUtil.isNotEmpty(appstatus) && !"0".equals(appstatus)) {
            params.put("appstatus", appstatus);
        }
        if (StringUtil.isNotEmpty(billstatus) && !"0".equals(billstatus)) {
            params.put("isbill", billstatus);
        }

        if (isExportExcel()) {
            List<AppointmentModel> appointmentList = appointmentService.selectAppointmentByPage(params);
            for (AppointmentModel record : appointmentList) {
                dealAppStatus(record);
            }
            String[] headsName = { "预约编码", "交易号", "用户昵称", "手机号码", "电桩信息", "预约开始时间", "预约结束时间", "预约时长(分钟)", "预约费用(元)", "预约状态", "支付状态", "支付方式", "支付信息", "结算状态", "更新时间" };
            String[] properiesName = { "id", "appNo", "username", "phone", "stationPileName", "startTimeDesc", "endTimeDesc", "appLen", "appFeeDesc", "appStatusDesc",
                                       "payStatusDesc", "payWayDesc", "accountInfo", "isBillDesc", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(appointmentList, properiesName, headsName, AppointmentModel.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法!";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);
            List<AppointmentModel> appointmentList = appointmentService.selectAppointmentByPage(params);
            for (AppointmentModel record : appointmentList) {
                dealAppStatus(record);
            }
            page.setRoot(appointmentList);
            this.page = page;
            return SUCCESS;
        }

    }

    /**
     * 处理履约状态
     * 
     * @param record
     */
    private void dealAppStatus(AppointmentModel record) {
        if (record == null) return;
        if (record.getAppStatus() != null) {
            AppointmentStatusEnum statusenum = AppointmentStatusEnum.getEnmuByValue(record.getAppStatus().intValue());
            if (AppointmentStatusEnum.ORDERING == statusenum && new Date().compareTo(record.getEndTime()) >= 0) {
                record.setAppStatus(AppointmentStatusEnum.OVERDUE.getShortValue());
            }
        }

    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public Page getPage() {
        return page;
    }

}
