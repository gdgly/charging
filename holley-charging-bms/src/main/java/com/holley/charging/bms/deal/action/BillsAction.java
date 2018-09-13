package com.holley.charging.bms.deal.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.bms.BillsDetail;
import com.holley.charging.model.bms.BillsDetailIn;
import com.holley.charging.model.bms.BillsInfo;
import com.holley.charging.model.bus.BusBillsDetail;
import com.holley.charging.model.bus.BusCash;
import com.holley.charging.model.bus.BusRecharge;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.RechargeService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.BillMarkTypeEnum;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;

/**
 * 结算账单相关ACTION
 * 
 * @author zdd
 */
public class BillsAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private AccountService    accountService;
    private UserService       userService;
    private RechargeService   rechargeService;
    private Page              page;

    /**
     * 账单列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("userTypeList", UserTypeEnum.values());
        this.getRequest().setAttribute("whetherList", WhetherEnum.values());// 是否开票
        this.getRequest().setAttribute("biiiMarkList", BillMarkTypeEnum.values());
        return SUCCESS;
    }

    public String detailInit() {
        String userid = getParameter("userid");
        String datatime = getParameter("datatime");
        String detailtype = getParameter("detailtype");
        this.getRequest().setAttribute("userid", userid);
        this.getRequest().setAttribute("datatime", datatime);
        this.getRequest().setAttribute("detailtype", detailtype);
        this.getRequest().setAttribute("biiiMarkList", BillMarkTypeEnum.values());
        return SUCCESS;
    }

    /**
     * 分页查询账单列表
     * 
     * @return
     * @throws Exception
     */
    public String queryBillsList() throws Exception {
        String startmonth = this.getParameter("startmonth");
        String endmonth = this.getParameter("endmonth");
        String keyword = this.getParameter("keyword");
        String usertype = this.getParameter("usertype");
        String isreceipt = this.getParameter("isreceipt");
        String datetime = this.getParameter("datetime");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isNotNumber(usertype, isreceipt)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("checkcycle", datetime);

        if (StringUtil.isNotEmpty(startmonth)) {
            params.put("startmonth", StringUtil.trim(startmonth));
        }
        if (StringUtil.isNotEmpty(endmonth)) {
            params.put("endmonth", StringUtil.trim(endmonth));
        }
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isNotEmpty(usertype) && !"0".equals(usertype)) {
            params.put("usertype", Short.valueOf(usertype));
        }
        if (StringUtil.isNotEmpty(isreceipt) && !"0".equals(isreceipt)) {
            params.put("isreceipt", Short.valueOf(isreceipt));
        }

        if (isExportExcel()) {
            List<BillsInfo> list = accountService.selectBillsByPage(params);
            String[] headsName = { "资金统计编码", "用户编码", "用户昵称", "手机号码", "月份", "预约(+)", "充电(+)", "服务(+)", "停车(+)", "总收入", "充值", "提现", "预约(-)", "充电(-)", "服务(-)", "停车(-)", "总支出", "开票编码" };
            String[] properiesName = { "id", "userId", "username", "phone", "checkCycle", "appFeeInDesc", "chaFeeInDesc", "serviceFeeInDesc", "parkFeeInDesc", "totalFeeInDesc",
                    "rechargeFeeDesc", "cashFeeDesc", "appFeeOutDesc", "chaFeeOutDesc", "serviceFeeOutDesc", "parkFeeOutDesc", "totalFeeOutDesc", "receiptId" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, BillsInfo.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<BillsInfo> list = accountService.selectBillsByPage(params);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        }

    }

    /**
     * 分页查询收入明细
     * 
     * @return
     * @throws Exception
     * @throws WriteException
     */
    public String queryInDetail() throws Exception {
        String userid = this.getParameter("userid");
        String checkcycle = this.getParameter("checkcycle");
        String checkmark = this.getParameter("checkmark");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isNotNumber(userid, checkmark)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(checkcycle)) {
            this.success = false;
            this.message = "请选择月份";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", Integer.valueOf(userid));
        params.put("checkcycle", checkcycle);
        if (StringUtil.isNotEmpty(checkmark) && !"0".equals(checkmark)) {
            params.put("checkmark", checkmark);
        }
        if (isExportExcel()) {
            // 查询收入明细
            List<BillsDetailIn> indetailList = accountService.selectBillsDetailInByPage(params);
            if (indetailList != null && indetailList.size() > 1) {
                String stationpilename = "";
                for (BillsDetailIn record : indetailList) {
                    stationpilename = CacheChargeHolder.getStationPileNameById(record.getPileId());
                    record.setStationPileName(stationpilename);
                }
                // 查询收入总额
                BusBillsDetail intotal = accountService.selectBillsDetailInTotal(params);
                BillsDetailIn record = new BillsDetailIn();
                record.setStationPileName("合计");
                record.setAppFeeIn(intotal.getAppFeeIn());
                record.setChaFeeIn(intotal.getChaFeeIn());
                record.setServiceFeeIn(intotal.getServiceFeeIn());
                record.setParkFeeIn(intotal.getParkFeeIn());
                record.setTotalFee(intotal.getTotalFee());
                indetailList.add(record);
            }

            String[] headsName = { "数据时间", "电桩", "业务类型", "预约费", "充电费", "服务费", "停车费", "结算后合计", "结算前合计", "业务记录编码" };
            String[] properiesName = { "addTimeDesc", "stationPileName", "checkMarkDesc", "appFeeInDesc", "chaFeeInDesc", "serviceFeeInDesc", "parkFeeInDesc", "totalFeeInDesc",
                    "totalFeeDesc", "recordId" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(indetailList, properiesName, headsName, BillsDetailIn.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            // 查询收入明细
            List<BillsDetailIn> indetailList = accountService.selectBillsDetailInByPage(params);
            if (indetailList != null && indetailList.size() > 0) {
                String stationpilename = "";
                for (BillsDetailIn record : indetailList) {
                    stationpilename = CacheChargeHolder.getStationPileNameById(record.getPileId());
                    record.setStationPileName(stationpilename);
                }
            }
            page.setRoot(indetailList);
            // 查询收入总额
            BusBillsDetail intotal = accountService.selectBillsDetailInTotal(params);
            page.setObj(intotal);
            this.page = page;
            return SUCCESS;
        }

    }

    /**
     * 分页查询支出明细
     * 
     * @return
     * @throws Exception
     */
    public String queryOutDetail() throws Exception {
        String userid = this.getParameter("userid");
        String checkcycle = this.getParameter("checkcycle");
        String checkmark = this.getParameter("checkmark");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isNotNumber(userid, checkmark)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(checkcycle)) {
            this.success = false;
            this.message = "请选择月份";
            return SUCCESS;
        }
        BusUser user = userService.selectBusUserByPrimaryKey(Integer.valueOf(userid));
        if (user == null) {
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", user.getId());
        Date startdate = DateUtil.ShortStrToDate(checkcycle + "-01");
        Date enddate = DateUtil.addMonths(startdate, 1);
        params.put("startdate", startdate);
        params.put("enddate", enddate);
        if (StringUtil.isNotEmpty(checkmark) && !"0".equals(checkmark)) {
            params.put("checkmark", checkmark);
        }
        if (isExportExcel()) {
            List<BillsDetail> list = accountService.selectBillsDetailOutByPage(params);
            if (list != null && list.size() > 1) {
                // 查询收入总额
                BillsDetail intotal = accountService.selectBillsDetailOutTotal(params);
                intotal.setStationPileName("合计");
                list.add(intotal);
            }
            String[] headsName = { "数据时间", "电桩", "业务类型", "预约费", "充电费", "服务费", "停车费", "合计", "业务记录编码" };
            String[] properiesName = { "addTimeDesc", "stationPileName", "checkMarkDesc", "appFeeDesc", "chaFeeDesc", "serviceFeeDesc", "parkFeeDesc", "totalFeeDesc", "recordId" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, BillsDetail.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<BillsDetail> list = accountService.selectBillsDetailOutByPage(params);
            page.setRoot(list);
            // 查询收入总额
            BillsDetail intotal = accountService.selectBillsDetailOutTotal(params);
            page.setObj(intotal);
            this.page = page;
            return SUCCESS;
        }
    }

    /**
     * 分页查询充值明细
     * 
     * @return
     * @throws Exception
     * @throws WriteException
     */
    public String queryRechargeDetail() throws Exception {
        String userid = this.getParameter("userid");
        String checkcycle = this.getParameter("checkcycle");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isNotNumber(userid)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(checkcycle)) {
            this.success = false;
            this.message = "请选择月份";
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", Integer.valueOf(userid));
        Date startdate = DateUtil.ShortStrToDate(checkcycle + "-01");
        Date enddate = DateUtil.addMonths(startdate, 1);
        params.put("startdate", startdate);
        params.put("enddate", enddate);
        if (isExportExcel()) {
            List<BusRecharge> list = rechargeService.selectUserRechargeByPage(params);
            if (list != null && list.size() > 1) {
                BigDecimal totalfee = rechargeService.selectUserRechargeTotalFee(params);

                BusRecharge record = new BusRecharge();
                record.setTradeNo("合计");
                record.setMoney(totalfee);
                list.add(record);
            }
            String[] headsName = { "充值编码", "交易号", "用户编码", "订单状态", "充值金额", "支付方式", "支付信息", "手续费", "更新时间" };
            String[] properiesName = { "id", "tradeNo", "userId", "statusDesc", "moneyDesc", "payWayDesc", "accountInfo", "fee", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, BusRecharge.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<BusRecharge> list = rechargeService.selectUserRechargeByPage(params);
            page.setRoot(list);
            BigDecimal totalfee = rechargeService.selectUserRechargeTotalFee(params);
            page.setObj(NumberUtil.formateScale2Str(totalfee));
            this.page = page;
            return SUCCESS;
        }
    }

    /**
     * 分页查询提现明细
     * 
     * @return
     * @throws Exception
     */
    public String queryCashDetail() throws Exception {
        String userid = this.getParameter("userid");
        String checkcycle = this.getParameter("checkcycle");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isNotNumber(userid)) {
            this.success = false;
            this.message = "参数非法";
            return SUCCESS;
        }
        if (StringUtil.isEmpty(checkcycle)) {
            this.success = false;
            this.message = "请选择月份";
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userid", Integer.valueOf(userid));
        Date startdate = DateUtil.ShortStrToDate(checkcycle + "-01");
        Date enddate = DateUtil.addMonths(startdate, 1);
        params.put("startdate", startdate);
        params.put("enddate", enddate);
        if (isExportExcel()) {
            List<BusCash> list = accountService.selectUserCashByPage(params);
            if (list != null && list.size() > 1) {
                BigDecimal totalmoney = accountService.selectUserCashTotalMoney(params);
                BusCash record = new BusCash();
                record.setMoney(totalmoney);
                record.setUserName("合计");
                list.add(record);
            }
            String[] headsName = { "提现编码", "用户编码", "用户类型", "用户昵称", "手机号码", "申请时间", "提现金额", "提现方式", "提现状态" };
            String[] properiesName = { "id", "userId", "userName", "userTypeDesc", "phone", "addTimeDesc", "moneyDesc", "cashWayDesc", "cashStatusDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, BusCash.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<BusCash> list = accountService.selectUserCashByPage(params);
            page.setRoot(list);

            BigDecimal totalmoney = accountService.selectUserCashTotalMoney(params);
            page.setObj(NumberUtil.formateScale2Str(totalmoney));
            this.page = page;
            return SUCCESS;
        }

    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setRechargeService(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
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
