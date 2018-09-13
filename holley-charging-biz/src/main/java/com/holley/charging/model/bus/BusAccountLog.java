package com.holley.charging.model.bus;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.constants.charge.AccountLogTypeEnum;
import com.holley.common.constants.charge.FundDirectionEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;

public class BusAccountLog {

    private Integer    id;

    private Integer    userId;

    private Integer    recordId;

    private String     accountInfo;

    private Short      type;

    private Short      direction;

    private BigDecimal totalMoney;

    private BigDecimal operateMoney;

    private BigDecimal usableMoney;

    private BigDecimal freezeMoney;

    private String     remark;

    private Date       addTime;

    private String     addIp;
    // ADD
    private String     username;
    private Short      usertype;
    private String     phone;
    private String     realName;
    private String     stationName = "";

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

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo == null ? null : accountInfo.trim();
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Short getDirection() {
        return direction;
    }

    public void setDirection(Short direction) {
        this.direction = direction;
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public BigDecimal getOperateMoney() {
        return operateMoney;
    }

    public void setOperateMoney(BigDecimal operateMoney) {
        this.operateMoney = operateMoney;
    }

    public BigDecimal getUsableMoney() {
        return usableMoney;
    }

    public void setUsableMoney(BigDecimal usableMoney) {
        this.usableMoney = usableMoney;
    }

    public BigDecimal getFreezeMoney() {
        return freezeMoney;
    }

    public void setFreezeMoney(BigDecimal freezeMoney) {
        this.freezeMoney = freezeMoney;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public String getAddIp() {
        return addIp;
    }

    public void setAddIp(String addIp) {
        this.addIp = addIp == null ? null : addIp.trim();
    }

    // ADD
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Short getUsertype() {
        return usertype;
    }

    public void setUsertype(Short usertype) {
        this.usertype = usertype;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTypeDesc() {
        String result = "";
        if (type != null) {
            result = AccountLogTypeEnum.getText(type.intValue());
        }
        return result == null ? "" : result;
    }

    public String getDirectionDesc() {
        String result = "";
        if (direction != null) {
            result = FundDirectionEnum.getText(direction.intValue());
        }
        return result == null ? "" : result;
    }

    public String getAddTimeStr() {
        if (addTime == null) return "";
        return DateUtil.DateToLongStr(addTime);
    }

    public String getUserTypeDesc() {
        String result = "";
        if (usertype != null) {
            result = UserTypeEnum.getText(usertype.intValue());
        }
        return result == null ? "" : result;
    }

    public String getOperateMoneyDesc() {
        if (operateMoney == null) return "";
        return NumberUtil.formateScale2Str(operateMoney);
    }

    public String getTotalMoneyDesc() {
        if (totalMoney == null) return "";
        return NumberUtil.formateScale2Str(totalMoney);
    }

    public String getUsableMoneyDesc() {
        if (usableMoney == null) return "";
        return NumberUtil.formateScale2Str(usableMoney);
    }

    public String getFreezeMoneyDesc() {
        if (freezeMoney == null) return "";
        return NumberUtil.formateScale2Str(freezeMoney);
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

}
