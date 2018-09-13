package com.holley.charging.user.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.def.AppointmentModel;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;

/**
 * 个人用户预约
 * 
 * @author sc
 */
public class UserAppointmentAction extends BaseAction {

    private final static Logger logger = Logger.getLogger(UserAppointmentAction.class);
    private Page                page;

    public String searchAppointments() throws Exception {
        return SUCCESS;
    }

    public String searchAppointmentsByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        String searchTime = this.getParameter("searchTime");
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        param.put("userId", webUser.getUserId());
        if (IS_EXPORT != isExport) {

        } else {
            List exportList = null;
            String[] headsName = { "桩名称", "桩类型", "用户名", "手机号码", "预约状态", "预约时长", "预约费用", "入账状态", "预约时间" };
            String[] properiesName = { "pileName", "pileTypeDesc", "username", "phone", "appStatusDesc", "appLen", "appFeeDesc", "isBillDesc", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, AppointmentModel.class);
            return null;
        }
        return SUCCESS;
    }

    // get
    public String getMessage() {
        return this.message;
    }

    public Page getPage() {
        return page;
    }
}
