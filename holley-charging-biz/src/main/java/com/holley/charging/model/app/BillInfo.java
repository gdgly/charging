package com.holley.charging.model.app;

import java.math.BigDecimal;
import java.util.Date;

import com.holley.common.util.DateUtil;

public class BillInfo {

    private String     name;
    private Integer    id;
    private Date       datatime;
    private Short      type;     // 业务类型：1:充值,2:充电,3:预约,4, 提现
    private String     typedesc; // 业务类型描述
    private Short      direction; // 1:收入，2：支出
    private BigDecimal fee;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getDatatime() {
        return datatime;
    }

    public void setDatatime(Date datatime) {
        this.datatime = datatime;
    }

    public Short getType() {
        return type;
    }

    public void setType(Short type) {
        this.type = type;
    }

    public String getTypedesc() {
        return typedesc;
    }

    public void setTypedesc(String typedesc) {
        this.typedesc = typedesc;
    }

    public Short getDirection() {
        return direction;
    }

    public void setDirection(Short direction) {
        this.direction = direction;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public String getDatatimeDesc() {
        String result = "";
        if (datatime != null) {
            result = DateUtil.DateToLongStr(datatime);
        }
        return result;
    }

}
