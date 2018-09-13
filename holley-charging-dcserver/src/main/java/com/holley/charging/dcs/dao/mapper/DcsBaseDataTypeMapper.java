package com.holley.charging.dcs.dao.mapper;

import java.util.List;

import com.holley.charging.dcs.dao.model.DcsBaseDataType;



public interface DcsBaseDataTypeMapper {

    //List<DcsBaseDataType> selectByExample(AlarmEventsExample example);

	List<Integer> selectByTypeClass(Integer typeClass);

}