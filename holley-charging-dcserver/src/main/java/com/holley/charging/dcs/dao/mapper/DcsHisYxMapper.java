package com.holley.charging.dcs.dao.mapper;

import com.holley.charging.dcs.dao.model.DcsHisYx;

public interface DcsHisYxMapper {
	
    int insert(DcsHisYx record);

    int insertSelective(DcsHisYx record);

    DcsHisYx selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DcsHisYx record);

    int updateByPrimaryKey(DcsHisYx record);
}