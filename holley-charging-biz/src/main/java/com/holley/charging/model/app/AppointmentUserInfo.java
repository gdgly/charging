package com.holley.charging.model.app;

import java.util.Date;

/**
 * 预约人信息
 */
public class AppointmentUserInfo {

    private Integer userid;
    private String  userkey;   // 预约人id
    private String  username;  // 预约人昵称
    private String  headimg;   // 预约人头像
    private Date    appendtime;// 预约截止时间
    private long    timestart; // 计时开始时间

    // -------------以下三个字段供融云
    private String  meuserkey; // 自己的id
    private String  meusername;// 自己的昵称
    private String  meheadimg; // 自己的头像

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getUserkey() {
        return userkey;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public Date getAppendtime() {
        return appendtime;
    }

    public void setAppendtime(Date appendtime) {
        this.appendtime = appendtime;
    }

    public long getTimestart() {
        return timestart;
    }

    public void setTimestart(long timestart) {
        this.timestart = timestart;
    }

    public String getMeuserkey() {
        return meuserkey;
    }

    public void setMeuserkey(String meuserkey) {
        this.meuserkey = meuserkey;
    }

    public String getMeusername() {
        return meusername;
    }

    public void setMeusername(String meusername) {
        this.meusername = meusername;
    }

    public String getMeheadimg() {
        return meheadimg;
    }

    public void setMeheadimg(String meheadimg) {
        this.meheadimg = meheadimg;
    }

}
