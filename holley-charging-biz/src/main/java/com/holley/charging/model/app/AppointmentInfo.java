package com.holley.charging.model.app;

import java.math.BigDecimal;
import java.util.Date;

public class AppointmentInfo {

    private Integer    id;           // 预约记录id
    private Integer    pileid;
    private String     pilename;
    private Short      piletype;     // 充电桩类型
    private String     piletypedesc; // 充电桩类型
    private String     outv;         // 输出电压
    private String     ratp;         // 额定功率

    private String     stationname;
    private String     address;
    private String     lng;
    private String     lat;

    private Date       starttime;
    private Date       endtime;
    private Integer    applen;
    private BigDecimal appfee;
    private Short      appstatus;
    private Short      paystatus;
    private String     appno;
    private Date       addtime;

    private String     appstatusdesc;

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

    public String getPilename() {
        return pilename;
    }

    public void setPilename(String pilename) {
        this.pilename = pilename;
    }

    public Short getPiletype() {
        return piletype;
    }

    public void setPiletype(Short piletype) {
        this.piletype = piletype;
    }

    public String getPiletypedesc() {
        return piletypedesc;
    }

    public void setPiletypedesc(String piletypedesc) {
        this.piletypedesc = piletypedesc;
    }

    public String getOutv() {
        return outv;
    }

    public void setOutv(String outv) {
        this.outv = outv;
    }

    public String getRatp() {
        return ratp;
    }

    public void setRatp(String ratp) {
        this.ratp = ratp;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public Integer getApplen() {
        return applen;
    }

    public void setApplen(Integer applen) {
        this.applen = applen;
    }

    public BigDecimal getAppfee() {
        return appfee;
    }

    public void setAppfee(BigDecimal appfee) {
        this.appfee = appfee;
    }

    public Short getAppstatus() {
        return appstatus;
    }

    public void setAppstatus(Short appstatus) {
        this.appstatus = appstatus;
    }

    public Short getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(Short paystatus) {
        this.paystatus = paystatus;
    }

    public String getAppno() {
        return appno;
    }

    public void setAppno(String appno) {
        this.appno = appno;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAppstatusdesc() {
        return appstatusdesc;
    }

    public void setAppstatusdesc(String appstatusdesc) {
        this.appstatusdesc = appstatusdesc;
    }

}
