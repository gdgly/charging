package com.holley.charging.dcs.dao.mapper;

import com.holley.charging.dcs.dao.model.PileFeeModel;
import com.holley.charging.dcs.dao.model.PileFeeModelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PileFeeModelMapper {
    int countByExample(PileFeeModelExample example);

    int deleteByExample(PileFeeModelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(PileFeeModel record);

    int insertSelective(PileFeeModel record);

    List<PileFeeModel> selectByExample(PileFeeModelExample example);

    PileFeeModel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") PileFeeModel record, @Param("example") PileFeeModelExample example);

    int updateByExample(@Param("record") PileFeeModel record, @Param("example") PileFeeModelExample example);

    int updateByPrimaryKeySelective(PileFeeModel record);

    int updateByPrimaryKey(PileFeeModel record);
}