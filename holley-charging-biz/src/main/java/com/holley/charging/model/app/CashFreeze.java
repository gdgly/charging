package com.holley.charging.model.app;

import java.util.Date;

public class CashFreeze {

    private Integer id;
    private String  money;
    private Date    addTime;
    private String  accountInfo;
    private String  validStatusDesc;
    private String  cashWayDesc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo;
    }

    public String getValidStatusDesc() {
        return validStatusDesc;
    }

    public void setValidStatusDesc(String validStatusDesc) {
        this.validStatusDesc = validStatusDesc;
    }

    public String getCashWayDesc() {
        return cashWayDesc;
    }

    public void setCashWayDesc(String cashWayDesc) {
        this.cashWayDesc = cashWayDesc;
    }

}
