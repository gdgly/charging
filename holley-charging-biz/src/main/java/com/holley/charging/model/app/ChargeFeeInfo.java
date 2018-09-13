package com.holley.charging.model.app;

import java.util.Date;

public class ChargeFeeInfo {

    private Integer pileid;
    private String  pilename;
    private String  piletypedesc;
    private String  chawaydesc;
    private Integer chargeruleid;
    private Date    activeTime;
    private String  chargeFee;
    private String  parkFee;
    private String  serviceFee;
    private String  chargemode;  // 充电模式：0 自动；1 按电量；2 按时间； 3 按金额；格式：0,1,2,3
    private String  ratp;        // 额定功率

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

    public String getPiletypedesc() {
        return piletypedesc;
    }

    public void setPiletypedesc(String piletypedesc) {
        this.piletypedesc = piletypedesc;
    }

    public String getChawaydesc() {
        return chawaydesc;
    }

    public void setChawaydesc(String chawaydesc) {
        this.chawaydesc = chawaydesc;
    }

    public Integer getChargeruleid() {
        return chargeruleid;
    }

    public void setChargeruleid(Integer chargeruleid) {
        this.chargeruleid = chargeruleid;
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public String getChargeFee() {
        return chargeFee;
    }

    public void setChargeFee(String chargeFee) {
        this.chargeFee = chargeFee;
    }

    public String getParkFee() {
        return parkFee;
    }

    public void setParkFee(String parkFee) {
        this.parkFee = parkFee;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getChargemode() {
        return chargemode;
    }

    public void setChargemode(String chargemode) {
        this.chargemode = chargemode;
    }

    public String getRatp() {
        return ratp;
    }

    public void setRatp(String ratp) {
        this.ratp = ratp;
    }

}
