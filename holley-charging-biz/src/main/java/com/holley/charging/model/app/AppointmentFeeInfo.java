package com.holley.charging.model.app;

/**
 * APP预约时长界面信息
 * 
 * @author lenovo
 */
public class AppointmentFeeInfo {

    private Integer pileid;
    private String  pilename;
    private String  piletypedesc;  // 充电类型
    private String  chawaydesc;    // 充电方式
    private String  chargefee;     // 充电费
    private String  parkfee;       // 停车费
    private String  servicefee;    // 服务费
    private String  appointmentfee;// 预约费
    private String  outv;          // 输出电压
    private String  ratp;          // 额定功率

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

    public String getChargefee() {
        return chargefee;
    }

    public void setChargefee(String chargefee) {
        this.chargefee = chargefee;
    }

    public String getParkfee() {
        return parkfee;
    }

    public void setParkfee(String parkfee) {
        this.parkfee = parkfee;
    }

    public String getServicefee() {
        return servicefee;
    }

    public void setServicefee(String servicefee) {
        this.servicefee = servicefee;
    }

    public String getAppointmentfee() {
        return appointmentfee;
    }

    public void setAppointmentfee(String appointmentfee) {
        this.appointmentfee = appointmentfee;
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

}
