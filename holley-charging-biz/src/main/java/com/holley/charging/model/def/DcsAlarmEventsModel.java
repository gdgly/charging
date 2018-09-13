package com.holley.charging.model.def;

import com.holley.charging.model.dcs.DcsAlarmEvents;
import com.holley.common.util.StringUtil;

public class DcsAlarmEventsModel extends DcsAlarmEvents {

    private String stationName;
    private String pileName;
    private String address;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPileName() {
        return pileName;
    }

    public void setPileName(String pileName) {
        this.pileName = pileName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getShortStationAndPileNameDesc() {
        if (!StringUtil.isEmpty(stationName) && !StringUtil.isEmpty(pileName)) {
            String temp = stationName + " " + pileName;
            if (temp.length() > 13) {
                temp = temp.substring(0, 10) + "...";
            }
            return temp;
        } else {
            return "未知";
        }
    }

    public String getAllStationAndPileNameDesc() {
        if (!StringUtil.isEmpty(stationName) && !StringUtil.isEmpty(pileName)) {
            String temp = stationName + " " + pileName;
            return temp;
        } else {
            return "未知";
        }
    }
}
