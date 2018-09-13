package com.holley.charging.model.app;

import java.math.BigDecimal;

public class UserRealAndAccount {

    private Integer    userid;
    private Short      realstatus;
    private BigDecimal totalmoney;
    private BigDecimal usablemoney;
    private BigDecimal freezemoney;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Short getRealstatus() {
        return realstatus;
    }

    public void setRealstatus(Short realstatus) {
        this.realstatus = realstatus;
    }

    public BigDecimal getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(BigDecimal totalmoney) {
        this.totalmoney = totalmoney;
    }

    public BigDecimal getUsablemoney() {
        return usablemoney;
    }

    public void setUsablemoney(BigDecimal usablemoney) {
        this.usablemoney = usablemoney;
    }

    public BigDecimal getFreezemoney() {
        return freezemoney;
    }

    public void setFreezemoney(BigDecimal freezemoney) {
        this.freezemoney = freezemoney;
    }

}
