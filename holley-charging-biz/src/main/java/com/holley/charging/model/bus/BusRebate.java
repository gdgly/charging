package com.holley.charging.model.bus;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.util.DateUtil;

public class BusRebate {

    private Integer    id;

    private String     rebateDesc;

    private BigDecimal rebate;

    private Date       startTime;

    private Date       endTime;

    private Date       addTime;

    private Date       updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRebateDesc() {
        return rebateDesc;
    }

    public void setRebateDesc(String rebateDesc) {
        this.rebateDesc = rebateDesc == null ? null : rebateDesc.trim();
    }

    public BigDecimal getRebate() {
        return rebate;
    }

    public void setRebate(BigDecimal rebate) {
        this.rebate = rebate;
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

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    // add
    public String getAddTimeDesc() {
        return DateUtil.DateToLongStr(addTime);
    }

    public String getStartTimeDesc() {
        return DateUtil.DateToLongStr(startTime);
    }

    public String getEndTimeDesc() {
        return DateUtil.DateToLongStr(endTime);
    }

    public boolean getIsActive() {
        return System.currentTimeMillis() >= startTime.getTime();
    }

    public String getIsActiveDesc() {
        long current = System.currentTimeMillis();
        if (current >= startTime.getTime() && current <= endTime.getTime()) {
            return "激活";
        } else if (current > endTime.getTime()) {
            return "已过期";
        }
        return "未激活";
    }
}
