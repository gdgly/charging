package com.holley.charging.model.def;

/**
 * 子账户MODEL
 * 
 * @author shencheng
 */
public class SubAccountModel {

    private Integer userId;  // 用户id
    private String  userName; // 用户名
    private String  phone;   // 手机号码
    private String  roleName; // 角色名称
    private String  addTime; // 添加时间
    private Short   isLock;  // 是否锁定
    private Short   status;  // 状态
    private Short   roleType; // 角色类型

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public Short getRoleType() {
        return roleType;
    }

    public void setRoleType(Short roleType) {
        this.roleType = roleType;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Short getIsLock() {
        return isLock;
    }

    public void setIsLock(Short isLock) {
        this.isLock = isLock;
    }

}
