package com.holley.charging.model.def;

import java.math.BigDecimal;

import com.holley.common.util.NumberUtil;

public class StationAppointmentModel {

    private Integer    stationId;  // 充电点ID
    private String     stationName; // 充电点名称
    private String     address;    // 充电点地址
    private Integer    pileNum;    // 充电桩数量
    private Integer    appNum;     // 充电点预约数量
    private BigDecimal appFee;     // 充电点预约总费用
    private Short      score;      // 评分

    public Integer getStationId() {
        return stationId;
    }

    public void setStationId(Integer stationId) {
        this.stationId = stationId;
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

    public Integer getPileNum() {
        return pileNum;
    }

    public void setPileNum(Integer pileNum) {
        this.pileNum = pileNum;
    }

    public Integer getAppNum() {
        return appNum;
    }

    public void setAppNum(Integer appNum) {
        this.appNum = appNum;
    }

    public BigDecimal getAppFee() {
        return appFee;
    }

    public void setAppFee(BigDecimal appFee) {
        this.appFee = appFee;
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }

    public String getAppFeeDesc() {
        String result = "0";
        if (appFee != null) {
            result = appFee.setScale(NumberUtil.DEF_SCALE, NumberUtil.PERCISION).toString();
        }
        return result;
    }
}
