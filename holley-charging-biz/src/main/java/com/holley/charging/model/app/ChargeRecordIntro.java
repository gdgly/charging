package com.holley.charging.model.app;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充电记录概要信息
 * 
 * @author lenovo
 */
public class ChargeRecordIntro {

    private Integer    id;       // 充电记录id
    private Integer    pileid;
    private String     pilename;
    private Short      piletype; // 充电桩类型

    private Short      paystatus;
    private Date       starttime;
    private BigDecimal fee;

    private Integer    commentid;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public Short getPiletype() {
        return piletype;
    }

    public void setPiletype(Short piletype) {
        this.piletype = piletype;
    }

    public Short getPaystatus() {
        return paystatus;
    }

    public void setPaystatus(Short paystatus) {
        this.paystatus = paystatus;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Integer getCommentid() {
        return commentid;
    }

    public void setCommentid(Integer commentid) {
        this.commentid = commentid;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

}
