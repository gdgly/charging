package com.holley.charging.model.app;

/**
 * 地图充电点对象
 * 
 * @author lenovo
 */
public class StationMarker {

    private Integer stationid;
    private String  lng;
    private String  lat;
    private int     fastnum;  // 快充桩数量
    private int     slownum;  // 慢充桩数量
    private int     idlenum;  // 空闲桩数量

    public Integer getStationid() {
        return stationid;
    }

    public void setStationid(Integer stationid) {
        this.stationid = stationid;
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

}
