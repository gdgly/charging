package com.holley.charging.dcs.dao.mapper;

import com.holley.charging.dcs.dao.model.FeeModel;
import com.holley.charging.dcs.dao.model.FeeModelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface FeeModelMapper {
    int countByExample(FeeModelExample example);

    int deleteByExample(FeeModelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(FeeModel record);

    int insertSelective(FeeModel record);

    List<FeeModel> selectByExample(FeeModelExample example);

    FeeModel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") FeeModel record, @Param("example") FeeModelExample example);

    int updateByExample(@Param("record") FeeModel record, @Param("example") FeeModelExample example);

    int updateByPrimaryKeySelective(FeeModel record);

    int updateByPrimaryKey(FeeModel record);
}