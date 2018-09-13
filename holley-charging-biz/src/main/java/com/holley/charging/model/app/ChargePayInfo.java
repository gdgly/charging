package com.holley.charging.model.app;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充电支付页面
 * 
 * @author lenovo
 */
public class ChargePayInfo {

    private Integer    id;
    private Integer    pileid;
    private String     chargemodedesc;
    private Integer    chalen;
    private Double     chapower;
    private Date       starttime;
    private BigDecimal parkfee;
    private BigDecimal chafee;
    private BigDecimal servicefee;
    private BigDecimal shouldmoney;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPileid() {
        return pileid;
    }

    public void setPileid(Integer pileid) {
        this.pileid = pileid;
    }

    public String getChargemodedesc() {
        return chargemodedesc;
    }

    public void setChargemodedesc(String chargemodedesc) {
        this.chargemodedesc = chargemodedesc;
    }

    public Integer getChalen() {
        return chalen;
    }

    public void setChalen(Integer chalen) {
        this.chalen = chalen;
    }

    public Double getChapower() {
        return chapower;
    }

    public void setChapower(Double chapower) {
        this.chapower = chapower;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public BigDecimal getParkfee() {
        return parkfee;
    }

    public void setParkfee(BigDecimal parkfee) {
        this.parkfee = parkfee;
    }

    public BigDecimal getChafee() {
        return chafee;
    }

    public void setChafee(BigDecimal chafee) {
        this.chafee = chafee;
    }

    public BigDecimal getServicefee() {
        return servicefee;
    }

    public void setServicefee(BigDecimal servicefee) {
        this.servicefee = servicefee;
    }

    public BigDecimal getShouldmoney() {
        return shouldmoney;
    }

    public void setShouldmoney(BigDecimal shouldmoney) {
        this.shouldmoney = shouldmoney;
    }

}
