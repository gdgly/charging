package com.holley.charging.model.def;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.util.DateUtil;
import com.holley.platform.util.CacheSysHolder;

public class PileRunStatusModel {

    private Integer    pileid;
    private String     pilename;
    private String     pilecode;
    private Short      comtype;
    private Short      piletype;       // 快慢充
    private Short      chaway;         // 交直流
    private String     comaddr;        // 通讯地址
    private String     address;        // 桩地址
    private String     pilemodel;      // 桩型号
    private Date       updatetime;     // 状态更新时间
    private String     payway;
    private String     paywaydesc;
    private String     currenttypedesc;
    private String     powertypedesc;

    private Integer    userid;
    private String     username;
    private String     phone;

    private Short      status;         // 桩状态
    private String     statusdisc;     // 桩状态描述

    private Integer    stationid;
    private String     stationname;

    private Integer    chalen;         // 当前充电时长
    private Double     chapower;       // 当前充电量
    private BigDecimal money;          // 当前充电金额
    private String     outv;           // 输出电压
    private String     outi;           // 输出电流
    private Integer    paymentid;      // 充电缴费记录ID
    private String     tradeno;        // 交易号

    private Date       appendtime;     // 预约截止时间
    private Integer    apprecordid;    // 预约记录ID

    private String     userkey;        // 对userid进行加密

    private String     plateNum;       // 车牌号
    private Date       starttime;      // 充电开始时间
    private Double     soc;            // 电池剩余电量
    private Short      pileToType;     // 1.汽车2.自行车

    public Integer getPileid() {
        return pileid;
    }

    public void setPileid(Integer pileid) {
        this.pileid = pileid;
    }

    public String getPilename() {
        return pilename;
    }

    public void setPilename(String pilename) {
        this.pilename = pilename;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getStatusdisc() {
        return statusdisc;
    }

    public void setStatusdisc(String statusdisc) {
        this.statusdisc = statusdisc;
    }

    public String getPayway() {
        return payway;
    }

    public void setPayway(String payway) {
        this.payway = payway;
    }

    public String getPaywaydesc() {
        return paywaydesc;
    }

    public void setPaywaydesc(String paywaydesc) {
        this.paywaydesc = paywaydesc;
    }

    public String getCurrenttypedesc() {
        return currenttypedesc;
    }

    public void setCurrenttypedesc(String currenttypedesc) {
        this.currenttypedesc = currenttypedesc;
    }

    public String getPowertypedesc() {
        return powertypedesc;
    }

    public void setPowertypedesc(String powertypedesc) {
        this.powertypedesc = powertypedesc;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStationid() {
        return stationid;
    }

    public void setStationid(Integer stationid) {
        this.stationid = stationid;
    }

    public String getStationname() {
        return stationname;
    }

    public void setStationname(String stationname) {
        this.stationname = stationname;
    }

    public Integer getChalen() {
        return chalen;
    }

    public void setChalen(Integer chalen) {
        this.chalen = chalen;
    }

    public Double getChapower() {
        return chapower;
    }

    public void setChapower(Double chapower) {
        this.chapower = chapower;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public String getOutv() {
        return outv;
    }

    public void setOutv(String outv) {
        this.outv = outv;
    }

    public String getOuti() {
        return outi;
    }

    public void setOuti(String outi) {
        this.outi = outi;
    }

    public Integer getPaymentid() {
        return paymentid;
    }

    public void setPaymentid(Integer paymentid) {
        this.paymentid = paymentid;
    }

    public Date getAppendtime() {
        return appendtime;
    }

    public void setAppendtime(Date appendtime) {
        this.appendtime = appendtime;
    }

    public Integer getApprecordid() {
        return apprecordid;
    }

    public void setApprecordid(Integer apprecordid) {
        this.apprecordid = apprecordid;
    }

    public String getPilecode() {
        return pilecode;
    }

    public void setPilecode(String pilecode) {
        this.pilecode = pilecode;
    }

    public Short getComtype() {
        return comtype;
    }

    public void setComtype(Short comtype) {
        this.comtype = comtype;
    }

    public Short getPiletype() {
        return piletype;
    }

    public void setPiletype(Short piletype) {
        this.piletype = piletype;
    }

    public Short getChaway() {
        return chaway;
    }

    public void setChaway(Short chaway) {
        this.chaway = chaway;
    }

    public String getComaddr() {
        return comaddr;
    }

    public void setComaddr(String comaddr) {
        this.comaddr = comaddr;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPilemodel() {
        return pilemodel;
    }

    public void setPilemodel(String pilemodel) {
        this.pilemodel = pilemodel;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
    }

    // add
    public String getAppEndTimeDesc() {
        String result = "";
        if (appendtime != null) {
            result = DateUtil.DateToNosecStr(appendtime);
        }
        return result;
    }

    public String getComtypeDesc() {
        String result = "";
        if (comtype != null) {
            result = CacheSysHolder.getSysLinkName(LinkTypeEnum.COM_TYPE.getValue(), String.valueOf(comtype));
        }
        return result;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Double getSoc() {
        return soc;
    }

    public void setSoc(Double soc) {
        this.soc = soc;
    }

    public Short getPileToType() {
        return pileToType;
    }

    public void setPileToType(Short pileToType) {
        this.pileToType = pileToType;
    }

    public String getStarttimeStr() {
        return starttime == null ? "" : DateUtil.DateToLongStr(starttime);
    }
}
