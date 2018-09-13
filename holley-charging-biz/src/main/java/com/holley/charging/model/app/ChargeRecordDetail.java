package com.holley.charging.model.app;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充电记录详情
 * 
 * @author lenovo
 */
public class ChargeRecordDetail {

    private Integer    id;
    private Integer    pileid;
    private BigDecimal parkfee;
    private BigDecimal chafee;
    private BigDecimal servicefee;
    private Date       starttime;
    private Date       endtime;
    private Integer    chalen;
    private Double     chapower;
    private BigDecimal shouldmoney;
    private Short      paystatus;

    private String     pilename;
    private Short      piletype;

    private String     stationname;
    private String     openday;
    private String     opentime;
    private String     address;

    private String     chargemodedesc;
    private String     tradeno;

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

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
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

    public BigDecimal getShouldmoney() {
        return shouldmoney;
    }

    public void setShouldmoney(BigDecimal shouldmoney) {
        this.shouldmoney = shouldmoney;
    }

    public Short getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(Short paystatus) {
        this.paystatus = paystatus;
    }

    public Short getPiletype() {
        return piletype;
    }

    public void setPiletype(Short piletype) {
        this.piletype = piletype;
    }

    public String getOpenday() {
        return openday;
    }

    public void setOpenday(String openday) {
        this.openday = openday;
    }

    public String getOpentime() {
        return opentime;
    }

    public void setOpentime(String opentime) {
        this.opentime = opentime;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getChargemodedesc() {
        return chargemodedesc;
    }

    public void setChargemodedesc(String chargemodedesc) {
        this.chargemodedesc = chargemodedesc;
    }

    public String getPilename() {
        return pilename;
    }

    public void setPilename(String pilename) {
        this.pilename = pilename;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
    }

}
