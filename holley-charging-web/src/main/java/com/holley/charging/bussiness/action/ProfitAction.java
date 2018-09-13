package com.holley.charging.bussiness.action;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.math.NumberUtils;

import com.holley.charging.action.BaseAction;
import com.holley.charging.model.def.ProfitModel;
import com.holley.charging.service.bussiness.AppointmentService;
import com.holley.charging.service.bussiness.ProfitService;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.CreateEchart;
import com.holley.web.common.util.CreateEchartBar1;
import com.holley.web.common.util.CreateEchartLine1;
import com.holley.web.common.util.EchartData;

/**
 * 收益分析相关ACTION
 * 
 * @author shencheng
 */
public class ProfitAction extends BaseAction {

    private static final long   serialVersionUID = 1L;
    private AppointmentService  appointmentService;
    private Map<String, Object> map;
    private ProfitService       profitService;

    public String profitAnalysis() {
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

    /**
     * 异步获取图表数据
     * 
     * @return
     * @throws ParseException
     */
    public String profitAnalysisByAjax() throws ParseException {
        int index = this.getParamInt("index");
        int profitType = this.getParamInt("profitType");// 3.按季度2.按月份1.按年份
        int year = this.getParamInt("year");// 3.按季度2.按月份1.按年份
        WebUser webUser = this.getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        Map<String, Object> newMap = new HashMap<String, Object>();
        param.put("profitType", profitType);
        param.put("busMec", webUser.getInfoId());
        param.put("busType", UserTypeEnum.ENTERPRISE.getShortValue());
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

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public Map<String, Object> getMap() {
        return map;
    }

    public void setProfitService(ProfitService profitService) {
        this.profitService = profitService;
    }

}
