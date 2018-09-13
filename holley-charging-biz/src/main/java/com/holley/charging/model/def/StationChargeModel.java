package com.holley.charging.model.def;

import java.math.BigDecimal;

import com.holley.common.util.NumberUtil;

public class StationChargeModel {

    private Integer    stationId;  // 充电点ID
    private String     stationName; // 充电点名称
    private String     address;    // 充电点地址
    private Integer    pileNum;    // 充电桩数量
    private Integer    chaNum;     // 充电点充电数量
    private BigDecimal serviceFee; // 充电点服务费总费用
    private BigDecimal parkFee;    // 充电点停车费总费用
    private BigDecimal chaFee;     // 充电点充电费总费用
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

    public Integer getChaNum() {
        return chaNum;
    }

    public void setChaNum(Integer chaNum) {
        this.chaNum = chaNum;
    }

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public BigDecimal getParkFee() {
        return parkFee;
    }

    public void setParkFee(BigDecimal parkFee) {
        this.parkFee = parkFee;
    }

    public BigDecimal getChaFee() {
        return chaFee;
    }

    public void setChaFee(BigDecimal chaFee) {
        this.chaFee = chaFee;
    }

    public Short getScore() {
        return score;
    }

    public void setScore(Short score) {
        this.score = score;
    }

    public String getTotalFeeDesc() {
        BigDecimal temp = BigDecimal.ZERO;
        BigDecimal serviceMoney = NumberUtil.getNotNUll(serviceFee);
        BigDecimal parkMoney = NumberUtil.getNotNUll(parkFee);
        BigDecimal chaMoney = NumberUtil.getNotNUll(chaFee);
        temp = NumberUtil.add(serviceMoney, parkMoney);
        temp = NumberUtil.add(temp, chaMoney);
        return temp.toString();
    }
}
