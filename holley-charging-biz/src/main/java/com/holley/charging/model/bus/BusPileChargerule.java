package com.holley.charging.model.bus;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.common.util.DateUtil;

public class BusPileChargerule {

    private Integer    id;

    private Integer    pileId;

    private Integer    chargeruleId;

    private BigDecimal chargeFee;

    private BigDecimal parkFee;

    private BigDecimal serviceFee;

    private Date       activeTime;

    private Short      status;

    private Date       addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPileId() {
        return pileId;
    }

    public void setPileId(Integer pileId) {
        this.pileId = pileId;
    }

    public Integer getChargeruleId() {
        return chargeruleId;
    }

    public void setChargeruleId(Integer chargeruleId) {
        this.chargeruleId = chargeruleId;
    }

    public BigDecimal getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(BigDecimal chargeFee) {
        this.chargeFee = chargeFee;
    }

    public BigDecimal getParkFee() {
        return parkFee;
    }

    public void setParkFee(BigDecimal parkFee) {
        this.parkFee = parkFee;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getActiveTimeStr() {
        if (activeTime == null) return "";
        return DateUtil.DateToShortStr(activeTime);
    }

    public String getFeeRuleDesc() {
        if (chargeruleId == null) return "";
        BusChargeRule rule = CacheChargeHolder.getChargeRuleById(chargeruleId);
        if (rule == null) return "";
        return rule.getName();

    }

    public String getFeeRuleDetail() {
        if (chargeruleId == null || chargeruleId == 1) return "";
        BusChargeRule rule = CacheChargeHolder.getChargeRuleById(chargeruleId);
        if (rule == null) return "";
        String result = "";
        if (rule.getJianFee() != null) {
            result += "尖: " + rule.getJianFee() + " 元/度; ";
        }
        if (rule.getFengFee() != null) {
            result += "峰: " + rule.getFengFee() + " 元/度; ";
        }
        if (rule.getPingFee() != null) {
            result += "平: " + rule.getPingFee() + " 元/度; ";
        }
        if (rule.getGuFee() != null) {
            result += "谷: " + rule.getGuFee() + " 元/度;";
        }
        return result;
    }
}
