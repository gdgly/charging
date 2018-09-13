package com.holley.charging.model.bus;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.constants.charge.CashStatusEnum;
import com.holley.common.constants.charge.CashVerifyStatusEnum;
import com.holley.common.constants.charge.CashWayEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;

public class BusCash {

    private Integer    id;

    private Integer    userId;

    private BigDecimal money;

    private String     remark;

    private Date       addTime;

    private Date       validTime;

    private String     validRemark;

    private Short      validStatus;

    private Short      cashWay;

    private String     accountInfo;

    private Short      cashStatus;

    private String     userName;
    private Short      userType;
    private String     phone;
    private String     realName;
    private String     validStatusDesc;

    private String     accRealName;    // 开户姓名
    private String     bankName;       // 开户银行
    private String     account;        // 支付宝账户/银行账户/微信账户

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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
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

    public Date getValidTime() {
        return validTime;
    }

    public void setValidTime(Date validTime) {
        this.validTime = validTime;
    }

    public String getValidRemark() {
        return validRemark;
    }

    public void setValidRemark(String validRemark) {
        this.validRemark = validRemark == null ? null : validRemark.trim();
    }

    public Short getValidStatus() {
        return validStatus;
    }

    public void setValidStatus(Short validStatus) {
        this.validStatus = validStatus;
    }

    public Short getCashWay() {
        return cashWay;
    }

    public void setCashWay(Short cashWay) {
        this.cashWay = cashWay;
    }

    public String getAccountInfo() {
        return accountInfo;
    }

    public void setAccountInfo(String accountInfo) {
        this.accountInfo = accountInfo == null ? null : accountInfo.trim();
    }

    public Short getCashStatus() {
        return cashStatus;
    }

    public void setCashStatus(Short cashStatus) {
        this.cashStatus = cashStatus;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Short getUserType() {
        return userType;
    }

    public void setUserType(Short userType) {
        this.userType = userType;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccRealName() {
        return accRealName;
    }

    public void setAccRealName(String accRealName) {
        this.accRealName = accRealName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getValidStatusDesc2() {
        String result = "";
        if (validStatus != null) {
            switch (validStatus) {
                case 1:
                    result = CashVerifyStatusEnum.VERIFYING.getText();
                    break;
                case 2:
                    result = CashVerifyStatusEnum.PASSED.getText();
                    break;
                case 3:
                    result = CashVerifyStatusEnum.FAILED.getText();
                    break;
                default:
                    result = "未知";
            }
        } else {
            result = "未知";
        }
        return result;
    }

    public String getValidRemarkDesc() {
        String result = "";
        if (!StringUtil.isEmpty(validRemark)) {
            result = validRemark;
        } else {
            result = "无";
        }
        return result;
    }

    public String getMoneyDesc() {
        if (money == null) return "";
        return NumberUtil.formateScale2Str(money);
    }

    public String getAddTimeDesc() {
        String result = "";
        if (addTime != null) {
            result = DateUtil.DateToLongStr(addTime);
        } else {
            result = "";
        }
        return result;
    }

    public String getAccountInfoDesc() {
        String result = "";
        if (!StringUtil.isEmpty(accountInfo)) {
            result = accountInfo;
        } else {
            result = "无";
        }
        return result;
    }

    public String getCashWayDesc2() {
        String result = "";
        if (cashWay != null) {
            result = CashWayEnum.getText(cashWay);
        } else {
            result = "无";
        }
        return result;
    }

    public String getValidTimeDesc() {
        if (validTime == null) return "";
        return DateUtil.DateToLongStr(validTime);
    }

    public String getUserTypeDesc() {
        if (userType == null) return "";
        String desc = UserTypeEnum.getText(userType.intValue());
        return desc == null ? "" : desc;
    }

    public String getValidStatusDesc() {
        if (validStatus == null) return "";
        String desc = CashVerifyStatusEnum.getText(validStatus.intValue());
        return desc == null ? "" : desc;
    }

    public void setValidStatusDesc(String validStatusDesc) {
        this.validStatusDesc = validStatusDesc;
    }

    public String getCashWayDesc() {
        if (cashWay == null) return "";
        String desc = CashWayEnum.getText(cashWay.intValue());
        return desc == null ? "" : desc;
    }

    public String getCashStatusDesc() {
        if (cashStatus == null) return "";
        String desc = CashStatusEnum.getText(cashStatus.intValue());
        return desc == null ? "" : desc;
    }
}
