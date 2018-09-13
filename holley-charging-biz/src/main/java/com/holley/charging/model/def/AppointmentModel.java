package com.holley.charging.model.def;

import com.holley.charging.model.bus.BusAppointment;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.util.StringUtil;

public class AppointmentModel extends BusAppointment {

    private String username;   // 用户名
    private String phone;      // 用户手机号码
    private String pileName;   // 桩名称
    private Short  pileType;   // 桩类型
    private String stationName;
    private String address;

    public Short getPileType() {
        return pileType;
    }

    public void setPileType(Short pileType) {
        this.pileType = pileType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPileName() {
        return pileName;
    }

    public void setPileName(String pileName) {
        this.pileName = pileName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // DESC
    public String getPileTypeDesc() {
        if (pileType == null) return "未知";
        String desc = ChargePowerTypeEnum.getText(pileType.intValue());
        return desc == null ? "未知" : desc;
    }

    public String getStationPileName() {
        if (StringUtil.isNotEmpty(stationName) && StringUtil.isNotEmpty(pileName)) {
            return stationName + " " + pileName;
        }
        return "";
    }

}
