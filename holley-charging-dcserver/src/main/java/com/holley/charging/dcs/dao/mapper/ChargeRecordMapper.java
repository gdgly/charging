package com.holley.charging.dcs.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.dcs.dao.model.ChargeRecord;
import com.holley.charging.dcs.dao.model.ChargeRecordExample;

public interface ChargeRecordMapper {

    int countByExample(ChargeRecordExample example);

    int deleteByExample(ChargeRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(ChargeRecord record);

    int insertSelective(ChargeRecord record);

    List<ChargeRecord> selectByExample(ChargeRecordExample example);

    ChargeRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record")
    ChargeRecord record, @Param("example")
    ChargeRecordExample example);

    int updateByExample(@Param("record")
    ChargeRecord record, @Param("example")
    ChargeRecordExample example);

    int updateByPrimaryKeySelective(ChargeRecord record);

    int updateByPrimaryKey(ChargeRecord record);

    int insertChargeRecBatch(List<ChargeRecord> list);

    int insertOrUpdateByPrimaryKeySelective(ChargeRecord record);
}
