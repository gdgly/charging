package com.holley.charging.model.def;

import java.math.BigDecimal;

public class ProfitModel {

    private String     pileName;    // 桩名称
    private String     dateName;    // 名称：年，季度，月
    private BigDecimal appMoney;    // 统计预约金额
    private BigDecimal serviceMoney; // 统计服务费金额
    private BigDecimal parkMoney;   // 统计停车费金额
    private BigDecimal chaMoney;    // 统计充电费金额
    private BigDecimal totalMoney;  // 统计总金额

    public String getPileName() {
        return pileName;
    }

    public void setPileName(String pileName) {
        this.pileName = pileName;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getAppMoney() {
        return appMoney;
    }

    public void setAppMoney(BigDecimal appMoney) {
        this.appMoney = appMoney;
    }

    public BigDecimal getServiceMoney() {
        return serviceMoney;
    }

    public void setServiceMoney(BigDecimal serviceMoney) {
        this.serviceMoney = serviceMoney;
    }

    public BigDecimal getParkMoney() {
        return parkMoney;
    }

    public void setParkMoney(BigDecimal parkMoney) {
        this.parkMoney = parkMoney;
    }

    public BigDecimal getChaMoney() {
        return chaMoney;
    }

    public void setChaMoney(BigDecimal chaMoney) {
        this.chaMoney = chaMoney;
    }

    public String getDateName() {
        return dateName;
    }

    public void setDateName(String dateName) {
        this.dateName = dateName;
    }

}
