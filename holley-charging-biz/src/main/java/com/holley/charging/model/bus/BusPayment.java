package com.holley.charging.model.bus;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.charging.common.enums.BikePayStatusEnum;
import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.PayWayEnum;
import com.holley.common.constants.charge.UseToTypeEnum;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.platform.util.RoleUtil;

public class BusPayment {

    private Integer    id;

    private Integer    userId;

    private Integer    pileId;

    private String     tradeNo;

    private Short      chaMode;

    private Short      dealStatus;

    private BigDecimal parkFee;

    private BigDecimal chaFee;

    private BigDecimal serviceFee;

    private Date       startTime;

    private Date       endTime;

    private Integer    chaLen;

    private Double     chaPower;

    private BigDecimal shouldMoney;

    private BigDecimal actualMoney;

    private Short      payWay;

    private String     accountInfo;

    private Short      payStatus;

    private Short      isBill;

    private Date       updateTime;
    // ADD
    private String     userName;
    private Short      stationToType;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPileId() {
        return pileId;
    }

    public void setPileId(Integer pileId) {
        this.pileId = pileId;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo == null ? null : tradeNo.trim();
    }

    public Short getChaMode() {
        return chaMode;
    }

    public void setChaMode(Short chaMode) {
        this.chaMode = chaMode;
    }

    public Short getDealStatus() {
        return dealStatus;
    }

    public void setDealStatus(Short dealStatus) {
        this.dealStatus = dealStatus;
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

    public BigDecimal getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(BigDecimal serviceFee) {
        this.serviceFee = serviceFee;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getChaLen() {
        return chaLen;
    }

    public void setChaLen(Integer chaLen) {
        this.chaLen = chaLen;
    }

    public Double getChaPower() {
        return chaPower;
    }

    public void setChaPower(Double chaPower) {
        this.chaPower = chaPower;
    }

    public BigDecimal getShouldMoney() {
        return shouldMoney;
    }

    public void setShouldMoney(BigDecimal shouldMoney) {
        this.shouldMoney = shouldMoney;
    }

    public BigDecimal getActualMoney() {
        return actualMoney;
    }

    public void setActualMoney(BigDecimal actualMoney) {
        this.actualMoney = actualMoney;
    }

    public Short getPayWay() {
        return payWay;
    }

    public void setPayWay(Short payWay) {
        this.payWay = payWay;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo == null ? null : accountInfo.trim();
    }

    public Short getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Short payStatus) {
        this.payStatus = payStatus;
    }

    public Short getIsBill() {
        return isBill;
    }

    public void setIsBill(Short isBill) {
        this.isBill = isBill;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Short getStationToType() {
        return stationToType;
    }

    public void setStationToType(Short stationToType) {
        this.stationToType = stationToType;
    }

    // DESC
    public String getDealStatusDesc() {
        String result = "";
        if (dealStatus != null) {
            result = ChargeDealStatusEnum.getText(dealStatus.intValue());
        }
        return result == null ? "未知" : result;
    }

    public String getPayStatusDesc() {
        String result = "";
        if (payStatus != null && stationToType != null) {
            if (UseToTypeEnum.CAR.getShortValue().equals(stationToType)) {
                result = ChargePayStatusEnum.getText(payStatus.intValue());
            } else if (UseToTypeEnum.BIKE.getShortValue().equals(stationToType)) {
                result = BikePayStatusEnum.getText(payStatus.intValue());
            }

        }
        return result == null ? "未知" : result;
    }

    public String getPayWayDesc() {
        if (payWay == null) return "";
        String desc = PayWayEnum.getText(payWay.intValue());
        return desc == null ? "" : desc;
    }

    public String getIsBillDesc() {
        String result = "";
        if (isBill != null) {
            switch (isBill) {
                case 1:
                    result = "已入账";
                    break;
                case 2:
                    result = "未入账";
                    break;
                default:
                    result = "未知";
            }
        } else {
            result = "未知";
        }
        return result;
    }

    public String getStartTimeDesc() {
        String result = "";
        if (startTime != null) {
            result = DateUtil.DateToLongStr(startTime);
        } else {
            result = "";
        }
        return result;
    }

    public String getEndTimeDesc() {
        String result = "";
        if (endTime != null) {
            result = DateUtil.DateToLongStr(endTime);
        } else {
            result = "";
        }
        return result;
    }

    public String getUpdateTimeDesc() {
        String result = "";
        if (updateTime != null) {
            result = DateUtil.DateToLongStr(updateTime);
        } else {
            result = "";
        }
        return result;
    }

    public BigDecimal getChaFeeDesc() {
        BigDecimal result = BigDecimal.ZERO;
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        if (chaFee != null) {
            result = NumberUtil.mul(chaFee, rate);
        }
        return result;
    }

    public BigDecimal getServiceFeeDesc() {
        BigDecimal result = BigDecimal.ZERO;
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        if (serviceFee != null) {
            result = NumberUtil.mul(serviceFee, rate);
        }
        return result;
    }

    public BigDecimal getParkFeeDesc() {
        BigDecimal result = BigDecimal.ZERO;
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        if (parkFee != null) {
            result = NumberUtil.mul(parkFee, rate);
        }
        return result;
    }

    public String getTotalFeeDesc() {
        BigDecimal temp = BigDecimal.ZERO;
        BigDecimal serviceMoney = getServiceFeeDesc();
        BigDecimal parkMoney = getParkFeeDesc();
        BigDecimal chaMoney = getChaFeeDesc();
        temp = NumberUtil.add(serviceMoney, parkMoney);
        temp = NumberUtil.add(temp, chaMoney);
        return temp.toString();
    }

}
