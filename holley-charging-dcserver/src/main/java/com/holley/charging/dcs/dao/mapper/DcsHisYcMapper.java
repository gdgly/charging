package com.holley.charging.dcs.dao.mapper;

import com.holley.charging.dcs.dao.model.DcsHisYc;

public interface DcsHisYcMapper {
	
    int insert(DcsHisYc record);

    int insertSelective(DcsHisYc record);

    DcsHisYc selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(DcsHisYc record);

    int updateByPrimaryKey(DcsHisYc record);
}