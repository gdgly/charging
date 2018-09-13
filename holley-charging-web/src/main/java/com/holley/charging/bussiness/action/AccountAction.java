package com.holley.charging.bussiness.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.write.WriteException;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusBills;
import com.holley.charging.model.bus.BusBillsExample;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusCash;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.def.AppointmentModel;
import com.holley.charging.model.def.BillsDetailModel;
import com.holley.charging.model.def.CountPileModel;
import com.holley.charging.model.def.StationChargeModel;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.website.UserService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;

/**
 * 账户资金相关ACTION
 * 
 * @author shencheng
 */
public class AccountAction extends BaseAction {

    private AccountService      accountService;
    private UserService         userService;
    private static final long   serialVersionUID = 1L;
    private Page                page;
    private Map<String, Object> map;

    /**
     * 显示账单详情
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String showBillsDetailByAjax() throws Exception {

        Map<String, Object> param = new HashMap<String, Object>();
        int billsId = this.getParamInt("billsId");
        int userId = this.getParamInt("userId");
        String checkCycle = this.getParameter("checkCycle");
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        int isExport = this.getParamInt("isExport");// 是否导出excel
        if (billsId > 0 && userId > 0 && !StringUtil.isEmpty(checkCycle)) {
            param.put("userId", userId);
            param.put("checkCycle", checkCycle);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<BillsDetailModel> list = accountService.selectBillsDetailByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            List<BillsDetailModel> exportList = accountService.exportBillsDetail(param);
            String[] headsName = { "充电点名称", "桩名称", "收入金额", "预约金额", "服务费", "充电费", "停车费", "入账方式", "生成时间" };
            String[] properiesName = { "stationName", "pileName", "totalFeeDesc", "appFee", "serviceFee", "chaFee", "parkFee", "checkMarkDesc", "createTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, AppointmentModel.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 显示账单
     * 
     * @return
     */
    public String showBills() {
        WebUser webUser = this.getWebuser();
        if (webUser.getRoleType() == UserTypeEnum.ENTERPRISE.getShortValue()) {
            List<BusBills> billsList = createBills();
            this.getRequest().setAttribute("billsList", billsList);
            this.getRequest().setAttribute("josnBillsList", JsonUtil.list2json(billsList));
        } else {
            this.getRequest().setAttribute("msg", "非管理员用户无法查看账单信息！！");
            return MSG;
        }
        return SUCCESS;
    }

    private List<BusBills> createBills() {
        List<BusBills> newBillsList = new ArrayList<BusBills>();
        BusBills newBills = null;
        WebUser webUser = this.getWebuser();
        BusBillsExample billsEmp1 = new BusBillsExample();
        BusBillsExample.Criteria billsCr1 = billsEmp1.createCriteria();
        billsCr1.andUserIdEqualTo(webUser.getUserId());
        BusBillsExample billsEmp2 = new BusBillsExample();
        BusBillsExample.Criteria billsCr2 = billsEmp2.createCriteria();
        billsCr2.andUserIdEqualTo(webUser.getUserId());
        BusBillsExample billsEmp3 = new BusBillsExample();
        BusBillsExample.Criteria billsCr3 = billsEmp3.createCriteria();
        billsCr3.andUserIdEqualTo(webUser.getUserId());
        BusBillsExample billsEmp4 = new BusBillsExample();
        BusBillsExample.Criteria billsCr4 = billsEmp4.createCriteria();
        BusBillsExample billsEmp5 = new BusBillsExample();
        billsCr4.andUserIdEqualTo(webUser.getUserId());
        BusBillsExample.Criteria billsCr5 = billsEmp5.createCriteria();
        BusBillsExample billsEmp6 = new BusBillsExample();
        billsCr5.andUserIdEqualTo(webUser.getUserId());
        BusBillsExample.Criteria billsCr6 = billsEmp6.createCriteria();
        billsCr6.andUserIdEqualTo(webUser.getUserId());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上一个月日期
        String cycleDesc1 = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        billsCr1.andCheckCycleEqualTo(cycleDesc1);
        List<BusBills> bills1 = accountService.selectBusBillsByExample(billsEmp1);
        if (bills1 != null && bills1.size() > 0) {
            newBillsList.add(bills1.get(0));
        } else {
            newBills = new BusBills();
            newBills.setCheckCycle(cycleDesc1);
            newBillsList.add(newBills);
        }
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上二个月日期
        String cycleDesc2 = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        billsCr2.andCheckCycleEqualTo(cycleDesc2);
        List<BusBills> bills2 = accountService.selectBusBillsByExample(billsEmp2);
        if (bills2 != null && bills2.size() > 0) {
            newBillsList.add(bills2.get(0));
        } else {
            newBills = new BusBills();
            newBills.setCheckCycle(cycleDesc2);
            newBillsList.add(newBills);
        }
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上三个月日期
        String cycleDesc3 = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        billsCr3.andCheckCycleEqualTo(cycleDesc3);
        List<BusBills> bills3 = accountService.selectBusBillsByExample(billsEmp3);
        if (bills3 != null && bills3.size() > 0) {
            newBillsList.add(bills3.get(0));
        } else {
            newBills = new BusBills();
            newBills.setCheckCycle(cycleDesc3);
            newBillsList.add(newBills);
        }
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上四个月日期
        String cycleDesc4 = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        billsCr4.andCheckCycleEqualTo(cycleDesc4);
        List<BusBills> bills4 = accountService.selectBusBillsByExample(billsEmp4);
        if (bills4 != null && bills4.size() > 0) {
            newBillsList.add(bills4.get(0));
        } else {
            newBills = new BusBills();
            newBills.setCheckCycle(cycleDesc4);
            newBillsList.add(newBills);
        }
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上五个月日期
        String cycleDesc5 = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        billsCr5.andCheckCycleEqualTo(cycleDesc5);
        List<BusBills> bills5 = accountService.selectBusBillsByExample(billsEmp5);
        if (bills5 != null && bills5.size() > 0) {
            newBillsList.add(bills5.get(0));
        } else {
            newBills = new BusBills();
            newBills.setCheckCycle(cycleDesc5);
            newBillsList.add(newBills);
        }
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上六个月日期
        String cycleDesc6 = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        billsCr6.andCheckCycleEqualTo(cycleDesc6);
        List<BusBills> bills6 = accountService.selectBusBillsByExample(billsEmp6);
        if (bills6 != null && bills6.size() > 0) {
            newBillsList.add(bills6.get(0));
        } else {
            newBills = new BusBills();
            newBills.setCheckCycle(cycleDesc6);
            newBillsList.add(newBills);
        }
        return newBillsList;
    }

    /**
     * 显示账单
     * 
     * @return
     */
    public String showBillsByAjax() {
        WebUser webUser = this.getWebuser();
        int billsId = getParamInt("billsId");
        Map<String, Object> map = new HashMap<String, Object>();
        BusBillsExample billsEmp = new BusBillsExample();
        BusBillsExample.Criteria billsCr = billsEmp.createCriteria();
        billsCr.andIdEqualTo(billsId);
        List<BusBills> list = this.accountService.selectBusBillsByExample(billsEmp);
        if (list != null && list.size() > 0) {
            map.put("busBills", list.get(0));
        }
        this.map = map;
        return SUCCESS;
    }

    public String accountInfo() {
        Map<String, Object> param = new HashMap<String, Object>();
        WebUser webUser = this.getWebuser();
        BusAccount busAccount = null;
        // 查询管理员账户
        if (RoleTypeEnum.ENTERPRISE.getShortValue() == webUser.getRoleType()) {
            busAccount = this.accountService.selectAccoutByPrimaryKey(webUser.getUserId());
        } else {
            param.put("roleType", RoleTypeEnum.ENTERPRISE.getShortValue());
            param.put("infoId", webUser.getInfoId());
            List<BusUser> list = this.userService.selectBusUserByMap(param);
            BusUser bussinessUser = list.get(0);
            busAccount = this.accountService.selectAccoutByPrimaryKey(bussinessUser.getId());
        }
        param.put("busType", webUser.getUsertype().getShortValue());
        param.put("busMec", webUser.getInfoId());
        CountPileModel countPileModel = accountService.countFastSlow(param);
        this.getRequest().setAttribute("busAccount", busAccount);
        this.getRequest().setAttribute("countPileModel", countPileModel);
        return SUCCESS;
    }

    /**
     * 提现
     * 
     * @return
     */
    public String accountCash() {
        // 获取账户信息
        WebUser webUser = this.getWebuser();
        if (webUser.getRoleType() == UserTypeEnum.ENTERPRISE.getShortValue()) {
            BusAccount busAccount = this.accountService.selectAccoutByPrimaryKey(webUser.getUserId());
            this.getRequest().setAttribute("busAccount", busAccount);
        } else {
            this.getRequest().setAttribute("msg", "非管理员用户无法提现！！");
            return MSG;
        }
        return SUCCESS;
    }

    /**
     * 提现操作
     * 
     * @return
     */
    public String doAccountCash() {
        String cashMoney = StringUtil.defaultIfNull(getParameter("cashMoney"), "0");
        String remark = StringUtil.defaultIfNull(getParameter("remark"), "");
        WebUser webUser = this.getWebuser();
        if (webUser.getRoleType() == UserTypeEnum.ENTERPRISE.getShortValue()) {
            BusAccount busAccount = this.accountService.selectAccoutByPrimaryKey(webUser.getUserId());// 账户信息
            message = checkAccountCash(cashMoney, busAccount);
            if ("success".equals(message)) {
                try {
                    BusBussinessInfo busBussinessInfo = accountService.selectBusBussinessInfoByPrimaryKey(webUser.getInfoId());
                    // 插入提示申请记录更新账户资金
                    BigDecimal money = NumberUtil.get4Out5In(cashMoney);// 提现的金额
                    BusCash newBusCash = new BusCash();
                    newBusCash.setAddTime(new Date());
                    newBusCash.setMoney(money);
                    newBusCash.setRemark(remark);
                    newBusCash.setUserId(webUser.getUserId());
                    String bankName = CacheSysHolder.getSysLinkName(LinkTypeEnum.BANK_NAME.getValue(), busBussinessInfo.getBankName());
                    newBusCash.setAccountInfo(busBussinessInfo.getAccRealName() + "," + bankName + "," + busBussinessInfo.getBankAccount());
                    this.accountService.insertBusCashAndUpdateBusAccount(newBusCash, busAccount);
                } catch (Exception e) {
                    e.printStackTrace();
                    message = "提现失败！！";
                }
            }
        } else {
            message = "必须企业管理员才能提现操作！！";
        }
        return SUCCESS;
    }

    private String checkAccountCash(String cashMoney, BusAccount busAccount) {
        String msg = "success";
        // 加入提现规则
        // TODO
        try {
            BigDecimal usableMoney = busAccount.getUsableMoney();// 可用余额
            BigDecimal Money = NumberUtil.get4Out5In(cashMoney);// 提现金额
            if (usableMoney.compareTo(BigDecimal.ZERO) == 0 || usableMoney.compareTo(BigDecimal.ZERO) == -1 || usableMoney.compareTo(Money) == -1) {
                msg = "您的可用余额不足！！";
            }
        } catch (NumberFormatException e) {
            msg = "请填写正确的金额！！";
        }

        return msg;
    }

    /**
     * 查询提现列表信息
     * 
     * @return
     */
    public String searchAccountCashs() {
        return SUCCESS;
    }

    public String searchAccountCashsByAjax() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        String dateTime = this.getParameter("searchDate");
        param.put("infoId", webUser.getInfoId());
        if (!StringUtil.isEmpty(dateTime)) {
            SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy/MM");
            Date date = formater.parse(dateTime);
            Calendar cr = Calendar.getInstance();
            cr.setTime(date);
            param.put("year", cr.get(Calendar.YEAR));
            param.put("month", cr.get(Calendar.MONTH) + 1);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<BusCash> list = this.accountService.selectBusCashByPage(param);
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
            // List<BusCash> exportList = accountService.exportCashs(param);
            List<BusCash> exportList = this.accountService.selectBusCashByPage(param);
            String[] headsName = { "账户信息", "审核备注", "提现金额", "审核结果", "提现申请时间" };
            String[] properiesName = { "accountInfoDesc", "validRemarkDesc", "moneyDesc", "validStatusDesc2", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, StationChargeModel.class);
            return null;
        }
        return SUCCESS;
    }

    // SET
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public String getMessage() {
        return this.message;
    }

    public Page getPage() {
        return page;
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
