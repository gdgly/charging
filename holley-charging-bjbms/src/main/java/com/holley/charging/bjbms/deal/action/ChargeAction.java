package com.holley.charging.bjbms.deal.action;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.common.constants.Globals;
import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.UseToTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;

public class ChargeAction extends BaseAction {

    private static final long serialVersionUID = 1L;
    private ChargingService   chargingService;
    private Page              page;
    private BusPayment        payMent;
    private DeviceService     deviceService;

    /**
     * 充电记录列表初始化
     * 
     * @return
     */
    public String init() {
        this.getRequest().setAttribute("dealStatusList", ChargeDealStatusEnum.values());
        this.getRequest().setAttribute("payStatusList", ChargePayStatusEnum.values());
        this.getRequest().setAttribute("billStatusList", WhetherEnum.values());
        this.getRequest().setAttribute("stationToTypeList", UseToTypeEnum.values());
        return SUCCESS;
    }

    /**
     * 充电记录详细初始化
     * 
     * @return
     */
    public String detailInit() {
        int paymentid = this.getParamInt("paymentid");
        ChargeModel record = null;
        int isUser = getParamInt("isUser");
        BusPayment payment = this.chargingService.selectPaymentByPK(paymentid);
        if (payment == null) {
            this.getRequest().setAttribute("msg", "参数非法.");
            return "msg";
        }
        PobChargingPile pile = deviceService.selectPileByPrimaryKey(payment.getPileId());

        if (isUser == 1) {
            // 汽车站
            if (UseToTypeEnum.CAR.getShortValue().equals(pile.getPileToType())) {
                record = chargingService.selectPaymentDetail(paymentid);
            } else if (UseToTypeEnum.BIKE.getShortValue().equals(pile.getPileToType())) {
                record = chargingService.selectBikePaymentDetail(paymentid);
            }

        } else {
            record = chargingService.selectNoUserPaymentDetail(paymentid);
        }
        this.getRequest().setAttribute("payment", record);
        return SUCCESS;
    }

    /**
     * 分页查询充电记录
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
        int stationToType = getParamInt("stationToType");// 站类型
        String startupdatedate = this.getParameter("startupdatedate");// 更新开始时间
        String endupdatedate = this.getParameter("endupdatedate");// 更新结束时间
        pagelimit = "15";
        // int stationIdForSelectModal = getParamInt("stationIdForSelectModal");
        String stationIdForSelectModals = getParameter("stationIdForSelectModal");
        String header = "";
        int isUser = this.getParamInt("isUser");
        List<ChargeModel> list = null;
        BusPayment payMent = null;
        if (StringUtil.isNotNumber(dealstatus, paystatus, billstatus) || stationToType <= 0) {
            this.success = false;
            this.message = "参数非法!";
            return SUCCESS;
        }
        Map<String, Object> params = new HashMap<String, Object>();
        if (StringUtil.isNotEmpty(startdate)) {
            // params.put("startdate", DateUtil.ShortStrToDate(startdate));
            params.put("startdate", DateUtil.LongStrToDate(startdate));

        }

        if (StringUtil.isNotEmpty(enddate)) {
            // params.put("enddate", DateUtil.ShortStrToDate(enddate));
            params.put("enddate", DateUtil.LongStrToDate(enddate));
        }
        if (StringUtil.isNotEmpty(startupdatedate)) {
            params.put("startupdatedate", DateUtil.LongStrToDate(startupdatedate));
        }
        if (StringUtil.isNotEmpty(endupdatedate)) {
            params.put("endupdatedate", DateUtil.LongStrToDate(endupdatedate));
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
        if (StringUtil.isNotEmpty(stationIdForSelectModals)) {
            String[] ids = stationIdForSelectModals.split(",");
            List<String> stationIds = Arrays.asList(ids);
            params.put("stationIds", stationIds);
        }

        // if (stationIdForSelectModal > 0) {
        // params.put("stationId", stationIdForSelectModal);
        // }
        isSubCompany(params);
        if (isExportExcel()) {
            if (isUser == 1) {
                if (UseToTypeEnum.CAR.getValue() == stationToType) {
                    list = chargingService.selectPaymentByPage(params);
                    payMent = chargingService.selectPaymentAllData(params);
                } else if (UseToTypeEnum.BIKE.getValue() == stationToType) {
                    list = chargingService.selectBikePaymentByPage(params);
                    payMent = chargingService.selectBikePaymentAllData(params);
                }
            } else {
                list = chargingService.selectPaymentNoUserByPage(params);
                payMent = chargingService.selectPaymentNoUserAllData(params);
            }
            if (payMent != null) {
                header = "总计：" + payMent.getId() + " 条 " + "充电电量总计：" + payMent.getChaPower() + " 度 " + "充电电费统计：" + payMent.getChaFee() + " 元 " + "充电服务费统计："
                         + payMent.getServiceFee() + " 元 " + "充电总费统计：" + payMent.getShouldMoney() + " 元 " + "充电时长统计：" + payMent.getChaLen() + " 分钟";
            }
            Map<String, List<ChargeModel>> maps = groupChargeListByStationName(list);
            String[] headsName = { "交易编码", "交易号", "用户姓名", "手机号码", "电桩信息", "充电开始时间", "充电结束时间", "充电时长(分钟)", "充电电量(度)", "充电费用(元)", "停车费用(元)", "服务费用(元)", "应缴费用(元)", "实缴费用(元)", "交易状态",
                    "支付状态", "支付方式", "支付信息", "车牌号", "更新时间" };
            String[] properiesName = { "id", "tradeNo", "realName", "phone", "stationPileName", "startTimeDesc", "endTimeDesc", "chaLen", "chaPower", "chaFee", "parkFee",
                    "serviceFee", "shouldMoney", "actualMoney", "dealStatusDesc", "payStatusDesc", "payWayDesc", "accountInfo", "plateNum", "updateTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            // jxl.exportXLS(list, properiesName, headsName, ChargeModel.class, Alignment.LEFT, header);
            // 导出初始化生成总的导出列表

            jxl.exportMoreXLSInit(null, header, list, properiesName, headsName, ChargeModel.class);
            // 多站点时，分多站点（sheet）生成excel
            if (maps.size() > 1) {
                for (Map.Entry<String, List<ChargeModel>> e : maps.entrySet()) {
                    jxl.exportMoreXLS(e.getKey(), wrapHeaderData(e.getValue()), e.getValue(), properiesName, headsName, ChargeModel.class);
                }
            }
            jxl.closeAndFlush();
            return null;
        } else {
            if (StringUtil.isNotNumber(pageindex, pagelimit)) {
                this.success = false;
                this.message = "参数非法!";
                return SUCCESS;
            }
            Page page = this.returnPage(Integer.parseInt(pageindex), Integer.parseInt(pagelimit));
            params.put(Globals.PAGE, page);
            if (isUser == 1) {
                if (UseToTypeEnum.CAR.getValue() == stationToType) {
                    list = chargingService.selectPaymentByPage(params);
                    payMent = chargingService.selectPaymentAllData(params);
                } else if (UseToTypeEnum.BIKE.getValue() == stationToType) {
                    list = chargingService.selectBikePaymentByPage(params);
                    payMent = chargingService.selectBikePaymentAllData(params);
                }

            } else {
                list = chargingService.selectPaymentNoUserByPage(params);
                payMent = chargingService.selectPaymentNoUserAllData(params);
            }
            this.payMent = payMent;
            page.setRoot(list);
            this.page = page;
            return SUCCESS;
        }

    }

    /**
     * 封装导出excel头部统计数据（多sheet导出）
     * 
     * @param list
     * @return
     */
    private String wrapHeaderData(List<ChargeModel> list) {
        StringBuffer header = new StringBuffer("");
        int totalCount = 0;// 总条数
        double totalChappower = 0;// 充电电量 度
        int totalChaLen = 0;// 充电时长 分钟
        BigDecimal totalChaFee = BigDecimal.ZERO;// 充电总费用 元
        BigDecimal totalServiceFee = BigDecimal.ZERO;// 充电总服务费 元
        BigDecimal totalFee = BigDecimal.ZERO;// 充电总费用 元

        double tempChappower = 0;
        int tempChaLen = 0;
        BigDecimal tempChaFee = BigDecimal.ZERO;
        BigDecimal tempServiceFee = BigDecimal.ZERO;
        if (list != null && list.size() > 0) {
            totalCount = list.size();
            for (ChargeModel model : list) {
                tempChaFee = model.getChaFee() == null ? BigDecimal.ZERO : model.getChaFee();
                tempChaLen = model.getChaLen() == null ? 0 : model.getChaLen();
                tempServiceFee = model.getServiceFee() == null ? BigDecimal.ZERO : model.getServiceFee();
                tempChappower = model.getChaPower() == null ? 0 : model.getChaPower();

                totalChaFee = totalChaFee.add(tempChaFee);
                totalChaLen = totalChaLen + tempChaLen;
                totalServiceFee = totalServiceFee.add(tempServiceFee);
                totalChappower = totalChappower + tempChappower;
            }
            totalFee = totalChaFee.add(totalServiceFee);
            totalFee = NumberUtil.get4Out5In(totalFee.toString());
            totalChaFee = NumberUtil.get4Out5In(totalChaFee.toString());
            totalServiceFee = NumberUtil.get4Out5In(totalServiceFee.toString());
            totalChappower = NumberUtil.get4Out5In(totalChappower + "").doubleValue();
            header.append("总计：").append(totalCount).append(" 条 ").append("充电电量总计：").append(totalChappower).append(" 度 ").append("充电电费统计：").append(totalChaFee).append(" 元 ").append("充电服务费统计：").append(totalServiceFee).append(" 元 ").append("充电总费统计：").append(totalFee).append(" 元 ").append("充电时长统计：").append(totalChaLen).append(" 分钟");
        }
        return header.toString();
    }

    /**
     * 封装多充电站对应充电桩集合
     * 
     * @param list
     * @return
     */
    private Map<String, List<ChargeModel>> groupChargeListByStationName(List<ChargeModel> list) {
        Map<String, List<ChargeModel>> map = new HashMap<String, List<ChargeModel>>();
        if (list != null && list.size() > 0) {
            ChargeModel model = null;
            String key = null;
            List<ChargeModel> cs = null;
            for (int x = 0; x < list.size(); x++) {
                model = list.get(x);
                key = model.getStationName();
                cs = map.get(key);
                if (map.containsKey(key)) {
                    cs.add(model);
                } else {
                    cs = new ArrayList<ChargeModel>();
                    cs.add(model);
                    map.put(key, cs);
                }

            }
        }
        return map;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public Page getPage() {
        return page;
    }

    public BusPayment getPayMent() {
        return payMent;
    }

}
