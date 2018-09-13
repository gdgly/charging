package com.holley.charging.model.def;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;

public class BillsDetailModel {

    private String     stationName; // 用户名
    private String     pileName;   // 手机号码
    private BigDecimal appFee;     // 预约费
    private BigDecimal chaFee;     // 充电费
    private BigDecimal serviceFee; // 服务费
    private BigDecimal parkFee;    // 停车费
    private Short      checkMark;  // 入账方式1充电2预约
    private Date       createTime; // 预约或充电记录生成时间

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPileName() {
        return pileName;
    }

    public void setPileName(String pileName) {
        this.pileName = pileName;
    }

    public BigDecimal getAppFee() {
        return appFee;
    }

    public void setAppFee(BigDecimal appFee) {
        this.appFee = appFee;
    }

    public BigDecimal getChaFee() {
        return chaFee;
    }

    public void setChaFee(BigDecimal chaFee) {
        this.chaFee = chaFee;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getParkFee() {
        return parkFee;
    }

    public void setParkFee(BigDecimal parkFee) {
        this.parkFee = parkFee;
    }

    public Short getCheckMark() {
        return checkMark;
    }

    public void setCheckMark(Short checkMark) {
        this.checkMark = checkMark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getCheckMarkDesc() {
        String result = "";
        if (checkMark == 1) {
            result = "充电";
        } else if (checkMark == 2) {
            result = "预约";
        }
        return result;
    }

    public BigDecimal getTotalFeeDesc() {
        BigDecimal result = BigDecimal.ZERO;
        BigDecimal appMoney = NumberUtil.getNotNUll(appFee);
        BigDecimal serviceMoney = NumberUtil.getNotNUll(serviceFee);
        BigDecimal parkMoney = NumberUtil.getNotNUll(parkFee);
        BigDecimal chaMoney = NumberUtil.getNotNUll(chaFee);
        if (checkMark == 1) {// 充电
            result = NumberUtil.add(serviceMoney, parkMoney);
            result = NumberUtil.add(result, chaMoney);
        } else if (checkMark == 2) {// 预约
            result = NumberUtil.add(result, appMoney);
        }
        return result;
    }

    public String getCreateTimeDesc() {
        String result = "";
        if (createTime != null) {
            result = DateUtil.DateToLongStr(createTime);
        }
        return result;
    }
}
