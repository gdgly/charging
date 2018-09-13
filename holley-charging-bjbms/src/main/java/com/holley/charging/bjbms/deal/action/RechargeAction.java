package com.holley.charging.bjbms.deal.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusCardRecharge;
import com.holley.charging.model.def.RechargeModel;
import com.holley.charging.service.bussiness.RechargeService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.PayWayEnum;
import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;

/**
 * 充值记录相关查询ACTION
 * 
 * @author zdd
 */
public class RechargeAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(RechargeAction.class);
    private static final long   serialVersionUID = 1L;
    private RechargeService     rechargeService;
    private Page                page;

    /**
     * 充值列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("statusList", RechargeStatusEnum.values());
        this.getRequest().setAttribute("payWayList", PayWayEnum.values());
        return SUCCESS;
    }

    /**
     * 充电卡充值记录详细
     * 
     * @return
     */
    public String cardDetailInit() {
        String id = this.getParameter("id");
        if (!NumberUtils.isNumber(id)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        BusCardRecharge record = rechargeService.selecCardRechargeDetail(Integer.valueOf(id));
        if (record != null && record.getMoney() != null) {
            record.setMoney(NumberUtil.formateScale2(record.getMoney()));
        }
        this.getRequest().setAttribute("recharge", record);
        return SUCCESS;
    }

    /**
     * 充值记录详细初始化
     * 
     * @return
     */
    public String detailInit() {
        String id = this.getParameter("rechargeid");
        if (!NumberUtils.isNumber(id)) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        RechargeModel record = rechargeService.selectRechargeDetail(Integer.valueOf(id));
        if (record != null && record.getMoney() != null) {
            record.setMoney(NumberUtil.formateScale2(record.getMoney()));
        }
        this.getRequest().setAttribute("recharge", record);
        return SUCCESS;
    }

    /**
     * 查询充值列表
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        WebUser user = getBmsWebuser();
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String searchuser = this.getParameter("searchuser");
        String searchtradeno = this.getParameter("searchtradeno");
        String status = this.getParameter("status");
        String payway = this.getParameter("payway");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");

        if (StringUtil.isNotNumber(status, payway)) {
            this.success = false;
            this.message = "参数非法!";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (UserTypeEnum.COMPANY == user.getUsertype()) {
            params.put("groupId", user.getUserId());
        }
        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startdate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("enddate", DateUtil.ShortStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(searchuser)) {
            params.put("searchuser", searchuser);
        }
        if (StringUtil.isNotEmpty(searchtradeno)) {
            params.put("searchtradeno", searchtradeno);
        }
        if (StringUtil.isNotEmpty(status) && !"0".equals(status)) {
            params.put("status", status);
        }
        if (StringUtil.isNotEmpty(payway) && !"0".equals(payway)) {
            params.put("payway", payway);
        }

        if (isExportExcel()) {
            List<RechargeModel> list = rechargeService.selectRechargeByPage(params);
            String[] headsName = { "充值编码", "交易号", "用户昵称", "手机号码", "订单状态", "充值金额", "支付方式", "支付信息", "手续费", "更新时间" };
            String[] properiesName = { "id", "tradeNo", "username", "phone", "statusDesc", "moneyDesc", "payWayDesc", "accountInfo", "fee", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, RechargeModel.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法!";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);
            List<RechargeModel> list = rechargeService.selectRechargeByPage(params);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        }

    }

    public String getMessage() {
        return message;
    }

    public boolean isSuccess() {
        return success;
    }

    public Page getPage() {
        return page;
    }

    public void setRechargeService(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

}
