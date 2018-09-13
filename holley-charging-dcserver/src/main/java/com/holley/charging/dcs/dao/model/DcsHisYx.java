package com.holley.charging.dcs.dao.model;

import java.util.Date;

public class DcsHisYx {
    private Integer id;

    private Integer chargeId;

    private Integer dataType;

    private Date dataTime;
    
    private Integer value;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getChargeid() {
		return chargeId;
	}

	public void setChargeid(Integer chargeid) {
		this.chargeId = chargeid;
	}

	public Integer getDataType() {
		return dataType;
	}

	public void setDataType(Integer dataType) {
		this.dataType = dataType;
	}

	public Date getDataTime() {
		return dataTime;
	}

	public void setDataTime(Date dataTime) {
		this.dataTime = dataTime;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}

   
}