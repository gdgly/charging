package com.holley.charging.dcs.dao.mapper;

import com.holley.charging.dcs.dao.model.DcsHisYa;
import com.holley.charging.dcs.dao.model.DcsHisYc;

public interface DcsHisYaMapper {
	
    int insert(DcsHisYa record);

    int insertSelective(DcsHisYa record);

    DcsHisYc selectByPrimaryKey(DcsHisYa id);

    int updateByPrimaryKeySelective(DcsHisYa record);

    int updateByPrimaryKey(DcsHisYa record);
}