package com.holley.charging.dcs.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.dcs.dao.model.PayMentRec;
import com.holley.charging.dcs.dao.model.PayMentRecExample;

public interface PayMentRecMapper {

    int countByExample(PayMentRecExample example);

    int deleteByExample(PayMentRecExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PayMentRec record);

    int insertSelective(PayMentRec record);

    List<PayMentRec> selectByExample(PayMentRecExample example);

    PayMentRec selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record")
    PayMentRec record, @Param("example")
    PayMentRecExample example);

    int updateByExample(@Param("record")
    PayMentRec record, @Param("example")
    PayMentRecExample example);

    int updateByPrimaryKeySelective(PayMentRec record);

    int updateByPrimaryKey(PayMentRec record);

    int insertOrUpdateByPrimaryKeySelective(PayMentRec record);
}
