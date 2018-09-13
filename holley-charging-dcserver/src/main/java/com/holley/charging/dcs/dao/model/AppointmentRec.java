package com.holley.charging.dcs.dao.model;

import java.math.BigDecimal;
import java.util.Date;

public class AppointmentRec {
    private Integer id;

    private Integer userId;

    private Integer pileId;

    private String appNo;

    private Date startTime;

    private Date endTime;

    private Integer appLen;

    private BigDecimal appFee;

    private Short appStatus;

    private Short payWay;

    private String accountInfo;

    private Short payStatus;

    private Short isBill;

    private Date addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPileId() {
        return pileId;
    }

    public void setPileId(Integer pileId) {
        this.pileId = pileId;
    }

    public String getAppNo() {
        return appNo;
    }

    public void setAppNo(String appNo) {
        this.appNo = appNo == null ? null : appNo.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getAppLen() {
        return appLen;
    }

    public void setAppLen(Integer appLen) {
        this.appLen = appLen;
    }

    public BigDecimal getAppFee() {
        return appFee;
    }

    public void setAppFee(BigDecimal appFee) {
        this.appFee = appFee;
    }

    public Short getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(Short appStatus) {
        this.appStatus = appStatus;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo == null ? null : accountInfo.trim();
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public Short getIsBill() {
        return isBill;
    }

    public void setIsBill(Short isBill) {
        this.isBill = isBill;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
}