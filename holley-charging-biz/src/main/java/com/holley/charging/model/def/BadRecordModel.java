package com.holley.charging.model.def;

public class BadRecordModel {

    private String badRecordId; // 灰色记录号
    private String freeze;     // 冻结金额
    private String payNo;      // 序号

    public String getBadRecordId() {
        return badRecordId;
    }

    public void setBadRecordId(String badRecordId) {
        this.badRecordId = badRecordId;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

}
