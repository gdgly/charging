package com.holley.charging.bms.sys.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.holley.charging.action.BaseAction;
import com.holley.common.constants.Globals;
import com.holley.common.constants.LogOperatorEnum;
import com.holley.common.constants.LogTypeEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.model.sys.SysOperationLog;
import com.holley.platform.service.LogService;

/**
 * 操作日志相关ACTION
 * 
 * @author zdd
 */
public class OperationLogAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private LogService        logService;
    private Page              page;

    /**
     * 操作日志列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("logTypeList", LogTypeEnum.values());
        this.getRequest().setAttribute("operatorTypeList", LogOperatorEnum.values());
        this.getRequest().setAttribute("userTypeList", UserTypeEnum.values());
        return SUCCESS;
    }

    /**
     * 分页查询操作日志
     * 
     * @return
     */
    public String queryList() {
        String keyword = this.getParameter("keyword");
        String usertype = this.getParameter("usertype");
        String logtype = this.getParameter("logtype");
        String operatortype = this.getParameter("operatortype");
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isNotDigits(logtype, usertype, pageindex, pagelimit)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(Globals.PAGE, page);
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isNotEmpty(usertype) && !"0".equals(usertype)) {
            params.put("usertype", Short.valueOf(usertype));
        }
        if (StringUtil.isNotEmpty(logtype) && !"0".equals(logtype)) {
            params.put("logtype", Short.valueOf(logtype));
        }
        if (StringUtil.isNotEmpty(operatortype) && !"0".equals(operatortype)) {
            params.put("operatortype", LogOperatorEnum.getTitle(operatortype));
        }
        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startdate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("enddate", DateUtil.ShortStrToDate(enddate));
        }
        List<SysOperationLog> list = logService.selectOperationLogByPage(params);
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    public void setLogService(LogService logService) {
        this.logService = logService;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Page getPage() {
        return page;
    }

}
