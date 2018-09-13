package com.holley.charging.user.action;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import jxl.write.WriteException;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusBills;
import com.holley.charging.model.bus.BusBillsExample;
import com.holley.charging.model.bus.BusCash;
import com.holley.charging.model.def.AppointmentModel;
import com.holley.charging.model.def.BillsDetailModel;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.model.def.ProfitModel;
import com.holley.charging.model.def.StationAppointmentModel;
import com.holley.charging.model.def.StationChargeModel;
import com.holley.charging.model.def.UserBillChartsModel;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.AppointmentService;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.charging.service.bussiness.ProfitService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.CashWayEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.CreateEchart;
import com.holley.web.common.util.CreateEchartBar1;
import com.holley.web.common.util.CreateEchartLine1;
import com.holley.web.common.util.EchartData;

/**
 * 个人用户交易中心
 * 
 * @author sc
 */
public class UserTransactionAction extends BaseAction {

    private final static Logger logger = Logger.getLogger(UserTransactionAction.class);
    private ChargingService     chargingService;
    private AppointmentService  appointmentService;
    private AccountService      accountService;
    private Page                page;
    private ProfitService       profitService;
    private Map<String, Object> map;

    /**
     * 查询充电点充电记录列表信息
     * 
     * @return
     */
    public String initUserDeviceCharge() {
        return SUCCESS;
    }

    /**
     * ajax分页查询充所有电点充电记录信息
     * 
     * @return
     * @throws Exception
     */
    public String userDeviceStationCharge() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int limit = 5;// 显示行数
        String searchName = this.getParameter("searchName");
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        param.put("busType", webUser.getUsertype().getValue());
        param.put("busMec", webUser.getInfoId());
        param.put("rate", rate);
        if (!StringUtil.isEmpty(searchName)) {
            param.put("searchStationName", searchName);
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

    public String userDevicePileCharge() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int currentPage = this.getParamInt("currentPage");// 当前页
        int stationId = this.getParamInt("stationId");
        int limit = this.getParamInt("limit");// 显示行数
        String searchKeyName = this.getParameter("searchName");
        String searchTime = this.getParameter("searchTime");
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
            jxl.exportXLS(exportList, properiesName, headsName, ChargeModel.class);
            return null;
        }

        return SUCCESS;
    }

    public String initUserDeviceAppointment() throws Exception {
        return SUCCESS;
    }

    public String userDeviceStationAppointment() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = 5;// 显示行数
        String searchName = this.getParameter("searchName");
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        param.put("busType", webUser.getUsertype().getShortValue());
        param.put("busMec", webUser.getInfoId());
        param.put("rate", rate);
        if (!StringUtil.isEmpty(searchName)) {
            param.put("searchStationName", searchName);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<StationAppointmentModel> list = appointmentService.selectStationAppByPage(param);
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        } else {
            // List exportList = appointmentService.exportStationApp(param);
            List<StationAppointmentModel> exportList = appointmentService.selectStationAppByPage(param);
            String[] headsName = { "充电点名称", "地址", "桩数量", "预约次数", "预约金额" };
            String[] properiesName = { "stationName", "address", "pileNum", "appNum", "appFee" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, StationAppointmentModel.class);
            return null;
        }
    }

    public String userDevicePileAppointment() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        String searchKeyName = this.getParameter("searchName");
        String searchTime = this.getParameter("searchTime");
        int stationId = this.getParamInt("stationId");
        param.put("busType", webUser.getUsertype().getShortValue());
        param.put("busMec", webUser.getInfoId());
        if (stationId > 0) {
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
            List<AppointmentModel> list = appointmentService.selectAppByPage(param);
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
            // List<AppointmentModel> exportList = appointmentService.exportAppointment(param);
            List<AppointmentModel> exportList = appointmentService.selectAppByPage(param);
            String[] headsName = { "桩名称", "桩类型", "用户名", "手机号码", "预约状态", "预约时长", "预约费用", "入账状态", "预约时间" };
            String[] properiesName = { "pileName", "pileTypeDesc", "username", "phone", "appStatusDesc", "appLen", "appFeeDesc", "isBillDesc", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, AppointmentModel.class);
            return null;
        }
        return SUCCESS;
    }

    /**
     * 提现
     * 
     * @return
     */
    public String initUserCash() {
        // 获取账户信息
        WebUser webUser = this.getWebuser();
        BusAccount busAccount = this.accountService.selectAccoutByPrimaryKey(webUser.getUserId());
        this.getRequest().setAttribute("busAccount", busAccount);
        return SUCCESS;
    }

    /**
     * 提现操作
     * 
     * @return
     */
    public String doUserCash() {
        String cashMoney = StringUtil.defaultIfNull(getParameter("cashMoney"), "0");
        Integer cashWay = getParamInt("cashWay");
        String accountInfo = getParameter("accountInfo");
        WebUser webUser = this.getWebuser();
        BusAccount busAccount = this.accountService.selectAccoutByPrimaryKey(webUser.getUserId());// 账户信息
        message = checkAccountCash(cashMoney, busAccount, cashWay, accountInfo);
        if ("success".equals(message)) {
            try {
                // 插入提示申请记录更新账户资金
                BigDecimal money = NumberUtil.get4Out5In(cashMoney);// 提现的金额
                BusCash newBusCash = new BusCash();
                newBusCash.setAddTime(new Date());
                newBusCash.setMoney(money);
                newBusCash.setUserId(webUser.getUserId());
                newBusCash.setAccountInfo(accountInfo);
                newBusCash.setCashWay(Short.valueOf(cashWay.toString()));
                this.accountService.insertBusCashAndUpdateBusAccount(newBusCash, busAccount);
            } catch (Exception e) {
                e.printStackTrace();
                message = "提现失败！！";
            }
        }
        return SUCCESS;
    }

    private String checkAccountCash(String cashMoney, BusAccount busAccount, Integer cashWay, String accountInfo) {
        String msg = "success";
        // 加入提现规则
        // TODO
        try {
            BigDecimal usableMoney = busAccount.getUsableMoney();// 可用余额
            BigDecimal Money = NumberUtil.get4Out5In(cashMoney);// 提现金额
            if (usableMoney.compareTo(BigDecimal.ZERO) == 0 || usableMoney.compareTo(BigDecimal.ZERO) == -1 || usableMoney.compareTo(Money) == -1) {
                msg = "您的可用余额不足！！";
            } else if (CashWayEnum.getEnmuByValue(cashWay) == null) {
                msg = "暂不支持该提现方式！！";
            } else if (StringUtil.isEmpty(accountInfo)) {
                msg = "提现账号不能为空！！";
            }
        } catch (NumberFormatException e) {
            msg = "请填写正确的金额！！";
        }

        return msg;
    }

    public String userCash() throws Exception {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int isExport = this.getParamInt("isExport");// 是否导出excel
        int currentPage = this.getParamInt("currentPage");// 当前页
        int limit = this.getParamInt("limit");// 显示行数
        String searchDate = this.getParameter("searchDate");
        param.put("infoId", webUser.getInfoId());
        if (!StringUtil.isEmpty(searchDate)) {
            SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy/MM");
            Date date = formater.parse(searchDate);
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
            jxl.exportXLS(exportList, properiesName, headsName, BusCash.class);
            return null;
        }
        return SUCCESS;
    }

    /**
     * 收益分析
     */
    public String initUserProfit() {
        WebUser webUser = getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("busType", webUser.getUsertype().getShortValue());
        param.put("busMec", webUser.getInfoId());
        param.put("chaPayStatus", ChargePayStatusEnum.SUCCESS.getShortValue());
        param.put("profitType", BY_MONTH);// 按月统计排行
        param.put("size", COUNT_SIZE);
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        param.put("rate", rate);
        List<ProfitModel> list = profitService.countProfit(param);
        this.getRequest().setAttribute("countProfits", list);
        Date now = new Date();
        int year = DateUtil.getYearValue(now);
        int month = DateUtil.getMonthValue(now);
        param.put("year", year);
        param.put("month", month);
        int countApp = profitService.countAppiontment(param);
        int countCha = profitService.countCharge(param);
        this.getRequest().setAttribute("countApp", countApp);
        this.getRequest().setAttribute("countCha", countCha);
        this.getRequest().setAttribute("currentMonth", month);
        return SUCCESS;
    }

    public String userProfit() throws Exception {
        int index = this.getParamInt("index");
        int profitType = this.getParamInt("profitType");// 3.按季度2.按月份1.按年份
        int year = this.getParamInt("year");// 3.按季度2.按月份1.按年份
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        param.put("profitType", profitType);
        param.put("busMec", webUser.getInfoId());
        param.put("busType", UserTypeEnum.PERSON.getShortValue());
        param.put("appPayStatus", AppointmentPayStatusEnum.SUCCESS.getShortValue());
        param.put("chaPayStatus", ChargePayStatusEnum.SUCCESS.getShortValue());
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        param.put("rate", rate);
        if (year > 0) {
            SimpleDateFormat f = new SimpleDateFormat("yyyy");
            Calendar c = Calendar.getInstance();
            c.setTime(f.parse(String.valueOf(year)));
            param.put("year", year);
        }

        map = createSqlDataByType(index, profitType, param, newMap);
        return SUCCESS;
    }

    private Map<String, Object> createSqlDataByType(int index, int profitType, Map<String, Object> param, Map<String, Object> newMap) {
        if (index == 0) {
            List<ProfitModel> appList1 = profitService.createAppProfit(param);
            List<ProfitModel> chaList1 = profitService.createYMQProfit(param);
            EchartData main4Data = createMain4Data(appList1, chaList1, profitType);
            newMap.put("main4", main4Data);
            EchartData main1Data = createMain1Data(appList1, chaList1, profitType);
            newMap.put("main1", main1Data);
        }
        // 提取echart1的数据
        else if (index == 1) {
            List<ProfitModel> appList1 = profitService.createAppProfit(param);
            List<ProfitModel> chaList1 = profitService.createYMQProfit(param);
            EchartData echartData1 = createMain4Data(appList1, chaList1, profitType);
            newMap.put("echart1", echartData1);
        }
        // 提取echart2的数据
        else if (index == 2) {

        }
        // 提取echart3的数据
        else if (index == 3) {

        }
        // 提取echart4的数据
        else if (index == 4) {

        }
        return newMap;
    }

    /**
     * 封装main1数据
     * 
     * @param appList
     * @param chaList
     * @param profitType
     * @return
     */
    private EchartData createMain1Data(List<ProfitModel> appList, List<ProfitModel> chaList, int profitType) {
        EchartData echartData = null;
        CreateEchart createEchart = new CreateEchartLine1();
        double[] totalMoney;

        // 1年2月3季度
        if (profitType == BY_QUARTER) {
            totalMoney = new double[4];
            totalMoney = queryMain1Data(appList, chaList, totalMoney, profitType, null);
            echartData = createEchart.createOptingByQuarter(null, totalMoney, null);
        } else if (profitType == BY_MONTH) {
            totalMoney = new double[12];
            totalMoney = queryMain1Data(appList, chaList, totalMoney, profitType, null);
            echartData = createEchart.createOptingByMonth(null, totalMoney, null);
        } else if (profitType == BY_YEAR) {
            Set set = new TreeSet();
            if (appList != null) {
                for (ProfitModel pm : appList) {
                    set.add(pm.getDateName());
                }
            }
            if (chaList != null) {
                for (ProfitModel pm : chaList) {
                    set.add(pm.getDateName());
                }
            }
            totalMoney = new double[set.size()];
            totalMoney = queryMain1Data(appList, chaList, totalMoney, profitType, set);
            echartData = createEchart.createOptingByYear(null, totalMoney, null, set);
        }
        return echartData;
    }

    private double[] queryMain1Data(List<ProfitModel> appList, List<ProfitModel> chaList, double[] totalMoneyData, int profitType, Set set) {
        double totalMoney = 0;
        if (profitType == BY_MONTH || profitType == BY_QUARTER) {
            for (int x = 0; x < totalMoneyData.length; x++) {
                BigDecimal appMoney = BigDecimal.ZERO;
                BigDecimal chaMoney = BigDecimal.ZERO;
                Integer k = x + 1;
                if (chaList != null && chaList.size() > 0) {
                    for (int i = 0; i < chaList.size(); i++) {
                        ProfitModel model = (ProfitModel) chaList.get(i);
                        if (k.toString().equals(model.getDateName())) {
                            BigDecimal bChaMoney = model.getChaMoney();
                            BigDecimal bParkMoney = model.getParkMoney();
                            BigDecimal bServiceMoney = model.getServiceMoney();
                            bChaMoney = bChaMoney == null ? BigDecimal.ZERO : bChaMoney;
                            bParkMoney = bParkMoney == null ? BigDecimal.ZERO : bParkMoney;
                            bServiceMoney = bServiceMoney == null ? BigDecimal.ZERO : bServiceMoney;
                            // chaMoney = bChaMoney.doubleValue() + bParkMoney.doubleValue() +
                            // bServiceMoney.doubleValue();
                            chaMoney = NumberUtil.add(bChaMoney, bParkMoney);
                            chaMoney = NumberUtil.add(chaMoney, bServiceMoney);
                            break;
                        }
                    }
                }
                if (appList != null && appList.size() > 0) {
                    for (int i = 0; i < appList.size(); i++) {
                        ProfitModel model = (ProfitModel) appList.get(i);
                        if (k.toString().equals(model.getDateName())) {
                            BigDecimal bAppMoney = model.getAppMoney();
                            bAppMoney = bAppMoney == null ? BigDecimal.ZERO : bAppMoney;
                            appMoney = bAppMoney;
                            break;
                        }
                    }
                }
                totalMoneyData[x] = NumberUtil.add(appMoney, chaMoney).doubleValue();
            }
        } else if (profitType == this.BY_YEAR) {
            Object[] oa = set.toArray();
            for (int x = 0; x < totalMoneyData.length; x++) {
                double appMoney = 0;
                double chaMoney = 0;
                if (chaList != null && chaList.size() > 0) {
                    for (int i = 0; i < chaList.size(); i++) {
                        ProfitModel model = (ProfitModel) chaList.get(i);
                        if (oa[x].toString().equals(model.getDateName())) {
                            BigDecimal bChaMoney = model.getChaMoney();
                            BigDecimal bParkMoney = model.getParkMoney();
                            BigDecimal bServiceMoney = model.getServiceMoney();
                            bChaMoney = bChaMoney == null ? BigDecimal.ZERO : bChaMoney;
                            bParkMoney = bParkMoney == null ? BigDecimal.ZERO : bParkMoney;
                            bServiceMoney = bServiceMoney == null ? BigDecimal.ZERO : bServiceMoney;
                            chaMoney = bChaMoney.doubleValue() + bParkMoney.doubleValue() + bServiceMoney.doubleValue();
                            break;
                        }
                    }
                }
                if (appList != null && appList.size() > 0) {
                    for (int i = 0; i < appList.size(); i++) {
                        ProfitModel model = (ProfitModel) appList.get(i);
                        if (oa[x].toString().equals(model.getDateName())) {
                            BigDecimal bAppMoney = model.getAppMoney();
                            bAppMoney = bAppMoney == null ? BigDecimal.ZERO : bAppMoney;
                            appMoney = bAppMoney.doubleValue();
                            break;
                        }
                    }
                }
                totalMoneyData[x] = appMoney + chaMoney;
            }
        }
        return totalMoneyData;
    }

    /**
     * 封装main4数据
     * 
     * @param appList
     * @param chaList
     */
    private EchartData createMain4Data(List<ProfitModel> appList, List<ProfitModel> chaList, int profitType) {
        double[][] chargingMoney = null;
        Set set = new TreeSet();
        if (profitType == 3) {
            chargingMoney = new double[4][4];
        } else if (profitType == 2) {
            chargingMoney = new double[4][12];
        } else if (profitType == 1) {
            if (appList != null) {
                for (ProfitModel pm : appList) {
                    set.add(pm.getDateName());
                }
            }
            if (chaList != null) {
                for (ProfitModel pm : chaList) {
                    set.add(pm.getDateName());
                }
            }
            chargingMoney = new double[4][set.size()];
        }

        ProfitModel p = null;
        int num = 0;
        double money = 0;
        if (appList != null && appList.size() > 0) {
            for (int x = 0; x < appList.size(); x++) {
                p = (ProfitModel) appList.get(x);
                num = NumberUtils.toInt(p.getDateName()) - 1;
                BigDecimal appMoney = p.getAppMoney();
                appMoney = appMoney == null ? BigDecimal.ZERO : appMoney;
                money = NumberUtils.toDouble(appMoney.toString());
                if (profitType == 1) {
                    chargingMoney[3][x] = money;

                } else {
                    chargingMoney[3][num] = money;
                }
            }
        }
        if (chaList != null && chaList.size() > 0) {
            for (int x = 0; x < chaList.size(); x++) {
                p = (ProfitModel) chaList.get(x);
                num = NumberUtils.toInt(p.getDateName()) - 1;
                BigDecimal serviceMoney = p.getServiceMoney();
                serviceMoney = serviceMoney == null ? BigDecimal.ZERO : serviceMoney;
                money = NumberUtils.toDouble(serviceMoney.toString());
                if (profitType == 1) {
                    chargingMoney[2][x] = money;

                } else {
                    chargingMoney[2][num] = money;
                }
            }
            for (int x = 0; x < chaList.size(); x++) {
                p = (ProfitModel) chaList.get(x);
                num = NumberUtils.toInt(p.getDateName()) - 1;
                BigDecimal parkMoney = p.getParkMoney();
                parkMoney = parkMoney == null ? BigDecimal.ZERO : parkMoney;
                money = NumberUtils.toDouble(parkMoney.toString());
                if (profitType == 1) {
                    chargingMoney[1][x] = money;

                } else {
                    chargingMoney[1][num] = money;
                }
            }
            for (int x = 0; x < chaList.size(); x++) {
                p = (ProfitModel) chaList.get(x);
                num = NumberUtils.toInt(p.getDateName()) - 1;
                BigDecimal chaMoney = p.getChaMoney();
                chaMoney = chaMoney == null ? BigDecimal.ZERO : chaMoney;
                money = NumberUtils.toDouble(chaMoney.toString());
                if (profitType == 1) {
                    chargingMoney[0][x] = money;

                } else {
                    chargingMoney[0][num] = money;
                }
            }
        }

        CreateEchart createEchart = new CreateEchartBar1();
        EchartData echartData = null;
        if (profitType == 3) {
            echartData = createEchart.createOptingByQuarter(chargingMoney, null, null);
        } else if (profitType == 2) {
            echartData = createEchart.createOptingByMonth(chargingMoney, null, null);
        } else if (profitType == 1) {

            echartData = createEchart.createOptingByYear(chargingMoney, null, null, set);
        }
        return echartData;
    }

    /**
     * 设备收入账单
     * 
     * @return
     */
    public String initUserDeviceBill() {
        List<UserBillChartsModel> list = createUserDeviceBillCharts();
        getRequest().setAttribute("userDeviceBillCharts", JsonUtil.list2json(list));
        return SUCCESS;
    }

    private List<UserBillChartsModel> createUserDeviceBillCharts() {
        WebUser webUser = getWebuser();
        List<UserBillChartsModel> list = new ArrayList<UserBillChartsModel>();
        List<String> cycle = new ArrayList<String>();
        Map<String, Object> param = new HashMap<String, Object>();

        BigDecimal chaIn = BigDecimal.ZERO;
        BigDecimal serviceIn = BigDecimal.ZERO;
        BigDecimal parkIn = BigDecimal.ZERO;
        BigDecimal totalChaIn = BigDecimal.ZERO;
        // BusBills deviceBill = null;
        // charts app start
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上一个月日期
        String cycleDesc1 = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上一个月日期
        String cycleDesc2 = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上一个月日期
        String cycleDesc3 = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        cycle.add(cycleDesc1);
        cycle.add(cycleDesc2);
        cycle.add(cycleDesc3);
        BusBillsExample emp1 = new BusBillsExample();
        BusBillsExample.Criteria cr1 = emp1.createCriteria();
        cr1.andUserIdEqualTo(webUser.getUserId());
        cr1.andCheckCycleIn(cycle);
        emp1.setOrderByClause("CHECK_CYCLE desc");
        List<BusBills> list1 = accountService.selectBusBillsByExample(emp1);
        for (int x = 0; x < cycle.size(); x++) {
            BusBills b = null;
            try {
                b = list1.get(x);
            } catch (Exception e) {
                e.printStackTrace();
            }

            UserBillChartsModel model = new UserBillChartsModel();
            if (b != null) {
                if (cycle.contains(b.getCheckCycle())) {
                    model.setAppFee(b.getAppFeeIn().toString());
                    chaIn = b.getChaFeeIn();
                    serviceIn = b.getServiceFeeIn();
                    parkIn = b.getParkFeeIn();
                    totalChaIn = NumberUtil.add(chaIn, serviceIn);
                    totalChaIn = NumberUtil.add(totalChaIn, parkIn);
                    model.setChaFee(totalChaIn.toString());
                    model.setName(b.getCheckCycle());
                } else {
                    model.setAppFee(BigDecimal.ZERO.toString());
                    model.setChaFee(BigDecimal.ZERO.toString());
                    model.setName(cycle.get(x));
                }

            } else {
                model.setAppFee(BigDecimal.ZERO.toString());
                model.setChaFee(BigDecimal.ZERO.toString());
                model.setName(cycle.get(x));
            }
            list.add(model);
        }
        return list;

    }

    /**
     * 分页展示所有月账单
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String userDeviceBill() throws Exception {
        WebUser webUser = getWebuser();
        int currentPage = this.getParamInt("currentPage");// 当前页
        String searchDate = this.getParameter("searchDate");
        int isExport = this.getParamInt("isExport");// 是否导出excel
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("userId", webUser.getUserId());
        if (IS_EXPORT != isExport) {
            if (!StringUtil.isEmpty(searchDate)) {
                Date cycleTime = DateUtil.StrToDate(searchDate, "yyyy/MM");
                String cycleDesc = DateUtil.DateToYYYYMMStr(cycleTime);// 结账周期描述
                param.put("cycleDesc", cycleDesc);
            }
            Page page = this.returnPage(currentPage, limit);
            param.put(Globals.PAGE, page);
            List<BusBills> list = accountService.selectBusBillsByPage(param);
            page.setRoot(list);
            this.page = page;
        } else {
            // BusBillsExample emp = new BusBillsExample();
            // BusBillsExample.Criteria cr = emp.createCriteria();
            // cr.andUserIdEqualTo(webUser.getUserId());
            // List<BusBills> exportList = accountService.selectBusBillsByExample(emp);
            List<BusBills> exportList = accountService.selectBusBillsByPage(param);
            String[] headsName = { "预约金额(元)", "充电金额(元)", "停车金额(元)", "服务金额(元)", "收入总金额(元)", "结账周期" };
            String[] properiesName = { "appFeeInDesc", "chaFeeInDesc", "parkFeeInDesc", "serviceFeeInDesc", "totalFeeInDesc", "checkCycle" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, BusBills.class);
            return null;
        }

        return SUCCESS;
    }

    public String userIncome() {
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        String dateTime = getParameter("dateTime");
        // Date startDate = DateUtil.getFirstDayOfMonth(new Date());
        // Date endDate = DateUtil.getLastDayOfMonth(new Date());
        // param.put("startDate", startDate);
        // param.put("endDate", endDate);
        param.put("userId", webUser.getUserId());
        Page page = this.returnPage(currentPage, limit);
        param.put(Globals.PAGE, page);
        List<BillsDetailModel> list = accountService.selectBillsDetailByPage(param);
        page.setRoot(list);
        this.page = page;
        return SUCCESS;
    }

    // get set

    public String getMessage() {
        return this.message;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setProfitService(ProfitService profitService) {
        this.profitService = profitService;
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public Page getPage() {
        return page;
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
