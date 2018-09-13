package com.holley.charging.model.app;

import java.math.BigDecimal;
import java.util.Date;

public class TradeRecord {

    private Integer    pileid;
    private String     name;
    private Short      type;    // 交易类型，2：充电，3：预约
    private String     typedesc;// 交易类型描述
    private Date       datatime;
    private BigDecimal fee;
    private Short      status;  // 支付类型，0:已结算,1：未支付，2：支付中，3：支付成功，4：支付失败

    public Integer getPileid() {
        return pileid;
    }

    public void setPileid(Integer pileid) {
        this.pileid = pileid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public Date getDatatime() {
        return datatime;
    }

    public void setDatatime(Date datatime) {
        this.datatime = datatime;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public Short getStatus() {
        return status;
    }

    public void setStatus(Short status) {
        this.status = status;
    }

    public String getTypedesc() {
        return typedesc;
    }

    public void setTypedesc(String typedesc) {
        this.typedesc = typedesc;
    }

}
