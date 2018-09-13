package com.holley.charging.model.bus;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.constants.BillMarkTypeEnum;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;

public class BusBillsDetail {

    private Integer    id;

    private Integer    recordId;

    private Integer    userId;

    private String     checkCycle;

    private BigDecimal totalFee;

    private BigDecimal appFeeIn;

    private BigDecimal chaFeeIn;

    private BigDecimal serviceFeeIn;

    private BigDecimal parkFeeIn;

    private Short      checkMark;

    private Date       addTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCheckCycle() {
        return checkCycle;
    }

    public void setCheckCycle(String checkCycle) {
        this.checkCycle = checkCycle == null ? null : checkCycle.trim();
    }

    public BigDecimal getTotalFee() {
        return totalFee;
    }

    public void setTotalFee(BigDecimal totalFee) {
        this.totalFee = totalFee;
    }

    public BigDecimal getAppFeeIn() {
        return appFeeIn;
    }

    public void setAppFeeIn(BigDecimal appFeeIn) {
        this.appFeeIn = appFeeIn;
    }

    public BigDecimal getChaFeeIn() {
        return chaFeeIn;
    }

    public void setChaFeeIn(BigDecimal chaFeeIn) {
        this.chaFeeIn = chaFeeIn;
    }

    public BigDecimal getServiceFeeIn() {
        return serviceFeeIn;
    }

    public void setServiceFeeIn(BigDecimal serviceFeeIn) {
        this.serviceFeeIn = serviceFeeIn;
    }

    public BigDecimal getParkFeeIn() {
        return parkFeeIn;
    }

    public void setParkFeeIn(BigDecimal parkFeeIn) {
        this.parkFeeIn = parkFeeIn;
    }

    public Short getCheckMark() {
        return checkMark;
    }

    public void setCheckMark(Short checkMark) {
        this.checkMark = checkMark;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getCheckMarkDesc() {
        if (checkMark == null) return "";
        String desc = BillMarkTypeEnum.getText(checkMark.shortValue());
        return desc == null ? "" : desc;
    }

    public String getAddTimeDesc() {
        if (addTime == null) return "";
        return DateUtil.DateToLongStr(addTime);
    }

    public String getTotalFeeDesc() {
        if (totalFee == null) return "";
        return NumberUtil.formateScale2Str(totalFee);
    }

    public String getAppFeeInDesc() {
        if (appFeeIn == null) return "";
        return NumberUtil.formateScale2Str(appFeeIn);
    }

    public String getChaFeeInDesc() {
        if (chaFeeIn == null) return "";
        return NumberUtil.formateScale2Str(chaFeeIn);
    }

    public String getServiceFeeInDesc() {
        if (serviceFeeIn == null) return "";
        return NumberUtil.formateScale2Str(serviceFeeIn);
    }

    public String getParkFeeInDesc() {
        if (parkFeeIn == null) return "";
        return NumberUtil.formateScale2Str(parkFeeIn);
    }

    public String getTotalFeeInDesc() {
        BigDecimal total = BigDecimal.ZERO;
        if (appFeeIn != null) {
            total = total.add(appFeeIn);
        }
        if (chaFeeIn != null) {
            total = total.add(chaFeeIn);
        }
        if (serviceFeeIn != null) {
            total = total.add(serviceFeeIn);
        }
        if (parkFeeIn != null) {
            total = total.add(parkFeeIn);
        }
        return NumberUtil.formateScale2Str(total);
    }
}
