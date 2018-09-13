package com.holley.charging.bms.deal.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;

public class ChargeAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private ChargingService   chargingService;
    private Page              page;

    /**
     * 充电记录列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("dealStatusList", ChargeDealStatusEnum.values());
        this.getRequest().setAttribute("payStatusList", ChargePayStatusEnum.values());
        this.getRequest().setAttribute("billStatusList", WhetherEnum.values());
        return SUCCESS;
    }

    /**
     * 充电记录详细初始化
     * 
     * @return
     */
    public String detailInit() {
        String id = this.getParameter("paymentid");
        if (!NumberUtils.isNumber(id)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        ChargeModel record = chargingService.selectPaymentDetail(Integer.valueOf(id));
        this.getRequest().setAttribute("payment", record);
        return SUCCESS;
    }

    /**
     * 分页查询预约记录
     * 
     * @return
     */
    public String queryList() throws Exception {
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String searchuser = this.getParameter("searchuser");
        String searchstation = this.getParameter("searchstation");
        String searchtradeno = this.getParameter("searchtradeno");
        String dealstatus = this.getParameter("dealstatus");
        String paystatus = this.getParameter("paystatus");
        String billstatus = this.getParameter("billstatus");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(dealstatus, paystatus, billstatus)) {
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
        if (StringUtil.isNotEmpty(searchtradeno)) {
            params.put("searchtradeno", searchtradeno);
        }
        if (StringUtil.isNotEmpty(paystatus) && !"0".equals(paystatus)) {
            params.put("paystatus", paystatus);
        }
        if (StringUtil.isNotEmpty(dealstatus) && !"0".equals(dealstatus)) {
            params.put("dealstatus", dealstatus);
        }
        if (StringUtil.isNotEmpty(billstatus) && !"0".equals(billstatus)) {
            params.put("isbill", billstatus);
        }

        if (isExportExcel()) {
            List<ChargeModel> list = chargingService.selectPaymentByPage(params);
            String[] headsName = { "交易编码", "交易号", "用户昵称", "手机号码", "电桩信息", "充电开始时间", "充电结束时间", "充电时长(分钟)", "充电电量(度)", "充电费用(元)", "停车费用(元)", "服务费用(元)", "应缴费用(元)", "实缴费用(元)", "交易状态",
                                   "支付状态", "支付方式", "支付信息", "结算状态", "更新时间" };
            String[] properiesName = { "id", "tradeNo", "username", "phone", "stationPileName", "startTimeDesc", "endTimeDesc", "chaLen", "chaPower", "chaFee", "parkFee",
                                       "serviceFee", "shouldMoney", "actualMoney", "dealStatusDesc", "payStatusDesc", "payWayDesc", "accountInfo", "isBillDesc", "updateTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, BusPileModel.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法!";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);
            List<ChargeModel> list = chargingService.selectPaymentByPage(params);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        }

    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public Page getPage() {
        return page;
    }
}
