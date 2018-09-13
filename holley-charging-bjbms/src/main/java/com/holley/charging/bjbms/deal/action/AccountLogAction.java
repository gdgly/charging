package com.holley.charging.bjbms.deal.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bms.BillsInfo;
import com.holley.charging.model.bus.BusAccountLog;
import com.holley.charging.model.bus.BusBills;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.AccountLogTypeEnum;
import com.holley.common.constants.charge.FundDirectionEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;

/**
 * 资金日志查询相关ACTION
 * 
 * @author zdd
 */
public class AccountLogAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(AccountLogAction.class);
    private static final long   serialVersionUID = 1L;
    private AccountService      accountService;
    private ChargingService     chargingService;
    private UserService         userService;
    private Page                page;
    private BillsInfo           billsInfo;

    /**
     * 资金日志列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("userTypeList", UserTypeEnum.values());
        this.getRequest().setAttribute("logTypeList", AccountLogTypeEnum.values());
        this.getRequest().setAttribute("directionList", FundDirectionEnum.values());
        return SUCCESS;
    }

    /**
     * 查询资金日志列表
     * 
     * @return
     * @throws Exception
     */
    public String queryList() throws Exception {
        WebUser user = getBmsWebuser();
        String keyword = this.getParameter("keyword");
        String usertype = this.getParameter("usertype");
        String logtype = this.getParameter("logtype");
        String direction = this.getParameter("direction");
        String startdate = this.getParameter("startdate");
        String enddate = this.getParameter("enddate");
        String pageindex = this.getParameter("pageindex");
        String pagelimit = this.getParameter("pagelimit");
        if (StringUtil.isNotNumber(usertype, logtype, direction)) {
            this.success = false;
            this.message = "参数格式不正确.";
            return SUCCESS;
        }

        Map<String, Object> params = new HashMap<String, Object>();
        if (UserTypeEnum.COMPANY == user.getUsertype()) {
            params.put("groupId", user.getUserId());
        }
        if (StringUtil.isNotEmpty(keyword)) {
            params.put("keyword", StringUtil.trim(keyword));
        }
        if (StringUtil.isNotEmpty(usertype) && !"0".equals(usertype)) {
            params.put("usertype", Short.valueOf(usertype));
        }
        if (StringUtil.isNotEmpty(logtype) && !"0".equals(logtype)) {
            params.put("type", Short.valueOf(logtype));
        }
        if (StringUtil.isNotEmpty(direction) && !"0".equals(direction)) {
            params.put("direction", Short.valueOf(direction));
        }
        if (StringUtil.isNotEmpty(startdate)) {
            params.put("startdate", DateUtil.ShortStrToDate(startdate));
        }
        if (StringUtil.isNotEmpty(enddate)) {
            params.put("enddate", DateUtil.ShortStrToDate(enddate));
        }
        if (isExportExcel()) {
            List<BusAccountLog> list = accountService.selectAccountLogByPage(params);
            wrapStationName(list);
            String[] headsName = { "资金日志编码", "充电站信息", "用户编码", "用户昵称", "手机号码", "用户类型", "对应记录编码", "操作类型", "资金流向", "操作金额", "账户总额", "可用金额", "冻结金额", "更新时间" };
            String[] properiesName = { "id", "stationName", "userId", "username", "phone", "userTypeDesc", "recordId", "typeDesc", "directionDesc", "operateMoneyDesc",
                    "totalMoneyDesc", "usableMoneyDesc", "freezeMoneyDesc", "addTimeStr" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(list, properiesName, headsName, BusAccountLog.class);
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数格式不正确.";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.valueOf(pageindex), Integer.valueOf(pagelimit));
            params.put(Globals.PAGE, page);
            List<BusAccountLog> list = accountService.selectAccountLogByPage(params);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        }

    }

    private List<Integer> filterPaymentList(List<BusAccountLog> list) {
        BusAccountLog log = null;
        List<Integer> payIdList = new ArrayList<Integer>();
        for (int i = 0; i < list.size(); i++) {
            log = list.get(i);
            if (AccountLogTypeEnum.ACC_CHARGING.getShortValue().equals(log.getType()) || AccountLogTypeEnum.UNACC_CHARGING.getShortValue().equals(log.getType())
                || AccountLogTypeEnum.CARD_CHARGING.getShortValue().equals(log.getType())) {
                payIdList.add(log.getRecordId());
            }
        }
        return payIdList;
    }

    private void wrapStationName(List<BusAccountLog> list) {
        if (list != null && list.size() > 0) {
            List<Integer> payIdList = filterPaymentList(list);
            if (payIdList != null && payIdList.size() > 0) {
                Map<String, Object> param = new HashMap<String, Object>();
                param.put("list", payIdList);
                List<ChargeModel> ms = this.chargingService.selectBusPaymentList(param);
                if (ms != null && ms.size() > 0) {
                    for (BusAccountLog log : list) {
                        for (ChargeModel model : ms) {
                            if (AccountLogTypeEnum.ACC_CHARGING.getShortValue().equals(log.getType()) || AccountLogTypeEnum.UNACC_CHARGING.getShortValue().equals(log.getType())
                                || AccountLogTypeEnum.CARD_CHARGING.getShortValue().equals(log.getType())) {
                                if (model.getId().equals(log.getRecordId())) {
                                    log.setStationName(model.getStationName() == null ? "" : model.getStationName());
                                }
                            }
                        }
                    }
                }
            }
        }

    }

    public String queryRecordDetail() {
        String type = this.getParameter("type");
        String recordid = this.getParameter("recordid");
        if (StringUtil.isNotDigits(type, recordid)) {
            this.success = false;
            this.message = "参数非法.";
            return SUCCESS;
        }
        if (!AccountLogTypeEnum.BILL.getShortValue().equals(Short.valueOf(type))) {
            return SUCCESS;
        }
        BusBills busBills = accountService.selectBillsByPK(Integer.valueOf(recordid));
        if (busBills == null) {
            this.success = false;
            this.message = "结算账单不存在.";
            return SUCCESS;
        }
        BusUser busUser = userService.selectBusUserByPrimaryKey(busBills.getUserId());
        BillsInfo record = new BillsInfo();
        record.setUserId(busBills.getUserId());
        if (busUser != null) {
            record.setUsertype(busUser.getUserType());
            record.setUsername(busUser.getUsername());
            record.setPhone(busUser.getPhone());
        }
        record.setCheckCycle(busBills.getCheckCycle());
        record.setAppFeeIn(busBills.getAppFeeIn());
        record.setChaFeeIn(busBills.getChaFeeIn());
        record.setServiceFeeIn(busBills.getServiceFeeIn());
        record.setParkFeeIn(busBills.getParkFeeIn());
        this.billsInfo = record;
        return SUCCESS;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
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

    public BillsInfo getBillsInfo() {
        return billsInfo;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

}
