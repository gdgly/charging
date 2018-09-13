package com.holley.charging.dcs.dao.mapper;

import com.holley.charging.dcs.dao.model.AppointmentRec;
import com.holley.charging.dcs.dao.model.AppointmentRecExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AppointmentRecMapper {
    int countByExample(AppointmentRecExample example);

    int deleteByExample(AppointmentRecExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AppointmentRec record);

    int insertSelective(AppointmentRec record);

    List<AppointmentRec> selectByExample(AppointmentRecExample example);

    AppointmentRec selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AppointmentRec record, @Param("example") AppointmentRecExample example);

    int updateByExample(@Param("record") AppointmentRec record, @Param("example") AppointmentRecExample example);

    int updateByPrimaryKeySelective(AppointmentRec record);

    int updateByPrimaryKey(AppointmentRec record);
}