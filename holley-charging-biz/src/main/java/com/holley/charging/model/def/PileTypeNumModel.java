package com.holley.charging.model.def;

public class PileTypeNumModel {

    private Integer stationid;
    private Short   piletype;
    private Integer pilenum;

    public Integer getStationid() {
        return stationid;
    }

    public void setStationid(Integer stationid) {
        this.stationid = stationid;
    }

    public Short getPiletype() {
        return piletype;
    }

    public void setPiletype(Short piletype) {
        this.piletype = piletype;
    }

    public Integer getPilenum() {
        return pilenum;
    }

    public void setPilenum(Integer pilenum) {
        this.pilenum = pilenum;
    }

}
