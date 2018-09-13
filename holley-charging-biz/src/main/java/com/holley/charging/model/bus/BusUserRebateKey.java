package com.holley.charging.model.bus;

import java.util.Date;

public class BusUserRebateKey {
    private Date addTime;

    private Integer rebateId;

    private Integer userId;

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getRebateId() {
        return rebateId;
    }

    public void setRebateId(Integer rebateId) {
        this.rebateId = rebateId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}