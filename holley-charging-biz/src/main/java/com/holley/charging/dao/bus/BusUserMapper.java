package com.holley.charging.dao.bus;

import com.holley.charging.model.app.UserRealAndAccount;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.def.SubAccountModel;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

public interface BusUserMapper {
    int countByExample(BusUserExample example);

    int deleteByExample(BusUserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(BusUser record);

    int insertSelective(BusUser record);

    List<BusUser> selectByExample(BusUserExample example);

    BusUser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") BusUser record, @Param("example") BusUserExample example);

    int updateByExample(@Param("record") BusUser record, @Param("example") BusUserExample example);

    int updateByPrimaryKeySelective(BusUser record);

    int updateByPrimaryKey(BusUser record);

    BusUser selectSimpleByPrimaryKey(Integer userid);

    /**
     * 根据条件查询子账户列表
     * 
     * @param
     * @return
     */
    List<SubAccountModel> selectSubAccounts(Map<String, Object> param);

    UserRealAndAccount selectUserRealAndAccount(Integer id);

    /**
     * 根据条件查询用户
     * 
     * @param
     * @return
     */
    List<BusUser> selectBusUserByMap(Map<String, Object> param);

    List<BusUser> selectUserByPage(Map<String, Object> params);

    BusUser selectUserByInfo(Map<String, Object> params);

    /**
     * 查询运营商的帐号(包括企业子账号)
     * 
     * @param infoid
     * @return
     */
    List<BusUser> selectBusinessUserByInfoid(Integer infoid);

}