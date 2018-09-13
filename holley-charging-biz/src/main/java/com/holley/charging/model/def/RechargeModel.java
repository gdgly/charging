package com.holley.charging.model.def;

import com.holley.charging.model.bus.BusRecharge;

public class RechargeModel extends BusRecharge {

    private String username; // 用户名
    private String phone;    // 用户手机号码

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
