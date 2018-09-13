package com.holley.charging.dcs.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.holley.charging.dcs.dao.model.Account;
import com.holley.charging.dcs.dao.model.AccountExample;

public interface AccountMapper {

    int countByExample(AccountExample example);

    int deleteByExample(AccountExample example);

    int deleteByPrimaryKey(Integer userId);

    int insert(Account record);

    int insertSelective(Account record);

    List<Account> selectByExample(AccountExample example);

    Account selectByPrimaryKey(Integer userId);

    int updateByExampleSelective(@Param("record")
    Account record, @Param("example")
    AccountExample example);

    int updateByExample(@Param("record")
    Account record, @Param("example")
    AccountExample example);

    int updateByPrimaryKeySelective(Account record);

    int updateByPrimaryKey(Account record);

    Account selectByCardID(String cardId);

}
