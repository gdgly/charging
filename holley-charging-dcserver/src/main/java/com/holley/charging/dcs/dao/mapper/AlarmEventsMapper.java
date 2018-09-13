package com.holley.charging.dcs.dao.mapper;

import com.holley.charging.dcs.dao.model.AlarmEvents;
import com.holley.charging.dcs.dao.model.AlarmEventsExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface AlarmEventsMapper {
    int countByExample(AlarmEventsExample example);

    int deleteByExample(AlarmEventsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AlarmEvents record);

    int insertSelective(AlarmEvents record);

    List<AlarmEvents> selectByExample(AlarmEventsExample example);

    AlarmEvents selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AlarmEvents record, @Param("example") AlarmEventsExample example);

    int updateByExample(@Param("record") AlarmEvents record, @Param("example") AlarmEventsExample example);

    int updateByPrimaryKeySelective(AlarmEvents record);

    int updateByPrimaryKey(AlarmEvents record);
}