package com.holley.charging.model.def;

import java.math.BigDecimal;

import com.holley.charging.model.bus.BusPayment;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.util.StringUtil;

public class ChargeModel extends BusPayment {

    private String     pileName;          // 充电点名称
    private String     username;          // 用户名
    private String     realName;          // 真实姓名
    private Integer    pileType;          // 充电桩类型
    private String     phone;             // 手机号码

    private String     stationName;       // 充电点名称
    private String     address;           // 充电点地址

    private BigDecimal totalChaFeeOut;    // 总充电费用
    private BigDecimal totalServiceFeeOut; // 总服务费用
    private BigDecimal totalParkFeeOut;   // 总停车费用

    private String     badRecordId;       // 灰色记录号
    private String     freeze;            // 冻结金额
    private String     payNo;             // 序号
    private String     plateNum;          // 车牌号

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

    public Integer getPileType() {
        return pileType;
    }

    public void setPileType(Integer pileType) {
        this.pileType = pileType;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public BigDecimal getTotalChaFeeOut() {
        return totalChaFeeOut;
    }

    public void setTotalChaFeeOut(BigDecimal totalChaFeeOut) {
        this.totalChaFeeOut = totalChaFeeOut;
    }

    public BigDecimal getTotalServiceFeeOut() {
        return totalServiceFeeOut;
    }

    public void setTotalServiceFeeOut(BigDecimal totalServiceFeeOut) {
        this.totalServiceFeeOut = totalServiceFeeOut;
    }

    public BigDecimal getTotalParkFeeOut() {
        return totalParkFeeOut;
    }

    public void setTotalParkFeeOut(BigDecimal totalParkFeeOut) {
        this.totalParkFeeOut = totalParkFeeOut;
    }

    public String getBadRecordId() {
        return badRecordId;
    }

    public void setBadRecordId(String badRecordId) {
        this.badRecordId = badRecordId;
    }

    public String getFreeze() {
        return freeze;
    }

    public void setFreeze(String freeze) {
        this.freeze = freeze;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
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

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

}
