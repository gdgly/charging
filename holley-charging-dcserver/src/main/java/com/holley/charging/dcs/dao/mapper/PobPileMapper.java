package com.holley.charging.dcs.dao.mapper;

import com.holley.charging.dcs.dao.model.PobPile;
import com.holley.charging.dcs.dao.model.PobPileExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PobPileMapper {
    int countByExample(PobPileExample example);

    int deleteByExample(PobPileExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PobPile record);

    int insertSelective(PobPile record);

    List<PobPile> selectByExample(PobPileExample example);

    PobPile selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PobPile record, @Param("example") PobPileExample example);

    int updateByExample(@Param("record") PobPile record, @Param("example") PobPileExample example);

    int updateByPrimaryKeySelective(PobPile record);

    int updateByPrimaryKey(PobPile record);
}