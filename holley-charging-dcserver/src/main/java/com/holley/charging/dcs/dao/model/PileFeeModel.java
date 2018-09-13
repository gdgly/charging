package com.holley.charging.dcs.dao.model;

import java.math.BigDecimal;
import java.util.Date;

public class PileFeeModel {

    private FeeModel   feeModel;

    private Integer    id;

    private Integer    pileId;

    private Integer    chargeruleId;

    private BigDecimal chargeFee;

    private BigDecimal parkFee;

    private BigDecimal serviceFee;

    private Date       activeTime;

    private Short      status;

    private Date       addTime;

    public FeeModel getFeeModel() {
        return feeModel;
    }

    public void setFeeModel(FeeModel feeModel) {
        this.feeModel = feeModel;
    }

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
}
