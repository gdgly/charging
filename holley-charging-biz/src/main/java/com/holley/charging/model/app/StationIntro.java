package com.holley.charging.model.app;

/**
 * 充电点简介
 * 
 * @author lenovo
 */
public class StationIntro {

    private Integer stationid;
    private String  stationname;
    private String  lng;
    private String  lat;
    private String  opendaydesc;
    private String  opentimedesc;
    private String  address;
    private String  img;
    private String  linkphone;
    private int     fastnum;     // 快充桩数量
    private int     slownum;     // 慢充桩数量
    private int     idlenum;     // 空闲桩数量
    private int     acnum;       // 交流桩数量
    private int     dcnum;       // 直流桩数量
    private Short   score;       // 评分
    private double  distance;    // 距离
    private double  fee;         // 服务费+停车费

    private Integer favoriteid;  // 收藏id

    public Integer getStationid() {
        return stationid;
    }

    public void setStationid(Integer stationid) {
        this.stationid = stationid;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
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

    public String getOpendaydesc() {
        return opendaydesc;
    }

    public void setOpendaydesc(String opendaydesc) {
        this.opendaydesc = opendaydesc;
    }

    public String getOpentimedesc() {
        return opentimedesc;
    }

    public void setOpentimedesc(String opentimedesc) {
        this.opentimedesc = opentimedesc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getLinkphone() {
        return linkphone;
    }

    public void setLinkphone(String linkphone) {
        this.linkphone = linkphone;
    }

    public int getFastnum() {
        return fastnum;
    }

    public void setFastnum(int fastnum) {
        this.fastnum = fastnum;
    }

    public int getSlownum() {
        return slownum;
    }

    public void setSlownum(int slownum) {
        this.slownum = slownum;
    }

    public int getIdlenum() {
        return idlenum;
    }

    public void setIdlenum(int idlenum) {
        this.idlenum = idlenum;
    }

    public int getAcnum() {
        return acnum;
    }

    public void setAcnum(int acnum) {
        this.acnum = acnum;
    }

    public int getDcnum() {
        return dcnum;
    }

    public void setDcnum(int dcnum) {
        this.dcnum = dcnum;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public Integer getFavoriteid() {
        return favoriteid;
    }

    public void setFavoriteid(Integer favoriteid) {
        this.favoriteid = favoriteid;
    }

}
