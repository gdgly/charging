package com.holley.charging.dao.dcs;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.model.dcs.DcsAlarmEvents;
import com.holley.charging.model.dcs.DcsAlarmEventsExample;
import com.holley.charging.model.def.DcsAlarmEventsModel;

public interface DcsAlarmEventsMapper {

    int countByExample(DcsAlarmEventsExample example);

    int deleteByExample(DcsAlarmEventsExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(DcsAlarmEvents record);

    int insertSelective(DcsAlarmEvents record);

    List<DcsAlarmEvents> selectByExample(DcsAlarmEventsExample example);

    DcsAlarmEvents selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") DcsAlarmEvents record, @Param("example") DcsAlarmEventsExample example);

    int updateByExample(@Param("record") DcsAlarmEvents record, @Param("example") DcsAlarmEventsExample example);

    int updateByPrimaryKeySelective(DcsAlarmEvents record);

    int updateByPrimaryKey(DcsAlarmEvents record);

    /**
     * 分页查询告警事件列表
     * 
     * @param param
     * @return
     */
    List<DcsAlarmEventsModel> selectDcsAlarmEventsModelByPage(Map<String, Object> param);

    /**
     * 导出事件Excel
     * 
     * @param param
     * @return
     */
    List<DcsAlarmEventsModel> exportAlarmEvents(Map<String, Object> param);

}
