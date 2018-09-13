package com.holley.charging.dcs.dao.model;


public class DcsBaseDataType {

	private Integer dataType;

	private Integer typeClass;
	
	private String name;

    private String unit;
    
    private Character code1;

    private Character code2;

	public Integer getDataType() {
		return dataType;
	}

	public Integer getTypeClass() {
		return typeClass;
	}

	public String getName() {
		return name;
	}

	public String getUnit() {
		return unit;
	}

	public Character getCode1() {
		return code1;
	}

	public Character getCode2() {
		return code2;
	}

    
   
}