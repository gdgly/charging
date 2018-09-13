package com.holley.charging.model.bus;

import java.util.Date;

import com.holley.common.constants.charge.SexEnum;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;

public class BusUserInfo {

    private Integer id;

    private String  front;

    private String  con;

    private String  cardNo;

    private String  realName;

    private Date    birthday;

    private Short   sex;

    private String  sign;

    private String  qq;

    private Integer province;

    private Integer city;

    private Integer brand;

    private Integer model;

    private String  vin;          // 车架号

    private String  plateNo;      // 车牌号

    private String  pic;          // 驾驶证

    private String  headImg;
    private String  username;
    private String  phone;
    private String  email;
    // private String provinceDesc; // 省级描述
    // private String cityDesc; // 市级描述
    // private String brandDesc; // 品牌名称
    // private String modelDesc; // 型号名称
    private Short   realStatus;   // 实名状态

    private Short   userType;     // 用户类型，3 普通个人；6 集团子账户

    private String  groupName;
    private Integer chargeCardNum;
    private String  company;
    private Integer rebateId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFront() {
        return front;
    }

    public void setFront(String front) {
        this.front = front == null ? null : front.trim();
    }

    public String getCon() {
        return con;
    }

    public void setCon(String con) {
        this.con = con == null ? null : con.trim();
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo == null ? null : cardNo.trim();
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName == null ? null : realName.trim();
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq == null ? null : qq.trim();
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public Integer getProvince() {
        return province;
    }

    public void setProvince(Integer province) {
        this.province = province;
    }

    public Integer getCity() {
        return city;
    }

    public void setCity(Integer city) {
        this.city = city;
    }

    public Integer getBrand() {
        return brand;
    }

    public void setBrand(Integer brand) {
        this.brand = brand;
    }

    public Integer getModel() {
        return model;
    }

    public void setModel(Integer model) {
        this.model = model;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin == null ? null : vin.trim();
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo == null ? null : plateNo.trim();
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getHeadImg() {
        return headImg;
    }

    public void setHeadImg(String headImg) {
        this.headImg = headImg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProvinceDesc() {
        if (province == null) return "";
        String desc = CacheSysHolder.getProvinceName(province);
        return desc == null ? "" : desc;
    }

    public String getCityDesc() {
        if (province == null || city == null) return "";
        String desc = CacheSysHolder.getCityName(province, city);
        return desc == null ? "" : desc;
    }

    public String getBrandDesc() {
        if (brand == null) return "";
        String desc = CacheSysHolder.getCarBrandName(brand);
        return desc == null ? "" : desc;
    }

    public String getModelDesc() {
        if (brand == null || model == null) return "";
        String desc = CacheSysHolder.getCarSubBrandName(brand, model);
        return desc == null ? "" : desc;
    }

    public Short getRealStatus() {
        return realStatus;
    }

    public void setRealStatus(Short realStatus) {
        this.realStatus = realStatus;
    }

    public Short getUserType() {
        return userType;
    }

    public void setUserType(Short userType) {
        this.userType = userType;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Integer getChargeCardNum() {
        return chargeCardNum;
    }

    public void setChargeCardNum(Integer chargeCardNum) {
        this.chargeCardNum = chargeCardNum;
    }

    public String getBirthdayDesc() {
        String result = "";
        if (birthday != null) {
            result = DateUtil.DateToStr(birthday, "yyyy/MM/dd");
        }
        return result;
    }

    public String getCardNoDesc() {
        String result = "";
        if (cardNo != null) {
            result = StringUtil.showStarCardNo(cardNo);
        }
        return result;
    }

    public String getRealNameDesc() {
        String result = "";
        if (realName != null) {
            result = StringUtil.showStarName(realName);
        }
        return result;
    }

    public String getSexDesc() {
        String result = "";
        if (sex != null) {
            result = SexEnum.getText(sex.intValue());
        }
        return result;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public Integer getRebateId() {
        return rebateId;
    }

    public void setRebateId(Integer rebateId) {
        this.rebateId = rebateId;
    }

}
