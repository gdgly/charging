package com.holley.charging.service.website;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.holley.charging.model.app.UserRealAndAccount;
import com.holley.charging.model.bms.UserRealIntro;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusBussinessReal;
import com.holley.charging.model.bus.BusBussinessRealExample;
import com.holley.charging.model.bus.BusCardRecharge;
import com.holley.charging.model.bus.BusChargeCard;
import com.holley.charging.model.bus.BusChargeCardExample;
import com.holley.charging.model.bus.BusGroupInfo;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusRebate;
import com.holley.charging.model.bus.BusRebateExample;
import com.holley.charging.model.bus.BusSuggestion;
import com.holley.charging.model.bus.BusSuggestionKey;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.model.bus.BusUserReal;
import com.holley.charging.model.bus.BusUserRealExample;
import com.holley.charging.model.bus.BusUserRealKey;
import com.holley.charging.model.bus.BusUserRebate;
import com.holley.charging.model.bus.BusUserRebateExample;
import com.holley.charging.model.bus.BusUserToken;
import com.holley.charging.model.dcs.DcsChargerecord;
import com.holley.charging.model.def.SubAccountModel;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.RealVerifyStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.WebUser;
import com.holley.platform.model.sys.SysNotice;
import com.holley.platform.model.sys.SysNoticeMessage;

public interface UserService {

    // ----------------------user

    /**
     * 用户注册（个人，及供应商）
     * 
     * @param user
     * @return
     */
    boolean insertUser(BusUser user, UserTypeEnum type, RoleTypeEnum roleType, Integer roleid, BusGroupInfo groupInfo);

    List<BusUser> selectUserByExample(BusUserExample emp);

    BusUser selectUserByPrimaryKey(Integer userid);

    BusUser selectUserByCache(Integer userid);

    List<BusUser> selectBusUserByMap(Map<String, Object> param);

    int updateUserByPKSelective(BusUser busUser);

    int updateUserAndRole(BusUser busUser, int rebateId);

    List<BusUser> selectUserByPage(Map<String, Object> params);

    BusUser selectUserByInfo(Map<String, Object> params);

    List<BusUser> selectBusinessUserByInfoid(Integer infoid);

    // --------------------bus_user_real--------------------
    /**
     * 查询用户的实名状态及账户金额
     */
    UserRealAndAccount selectUserRealAndAccount(Integer id);

    int insertUserReal(BusUserReal record);

    List<BusUserReal> selectUserRealByPage(Map<String, Object> params);

    BusUserReal selectUserRealByPK(BusUserRealKey key);

    String updateUserRealAndInfo(BusUserReal record, RealVerifyStatusEnum verifystatus) throws Exception;

    List<BusUserReal> selectUserRealByExample(BusUserRealExample example);

    BusUserReal selectLatestUserRealByUserid(Integer userid);

    // ----------------------bus_business

    int updateBussinessByPrimaryKeySelective(BusBussinessInfo info);

    BusBussinessInfo selectBussinessByPrimaryKey(Integer infoId);

    List<BusBussinessInfo> selectBusinessInfoByPage(Map<String, Object> params);

    // ----------------------userinfo
    int insertUserInfo(BusUserInfo info);

    BusUserInfo selectUserInfoByPrimaryKey(Integer id);

    List<BusUserInfo> selectUserInfoByPage(Map<String, Object> params);

    BusUserInfo selectUserInfoDetail(Integer id);

    UserRealIntro selectUserRealIntro(Map<String, Object> params);

    int updateUserInfoByPrimaryKeySelective(BusUserInfo record);

    BusUser selectSimpleUserInfoByPrimaryKey(Integer userid);

    BusBussinessReal selectBusBussinessRealByExample(BusBussinessRealExample example);

    int insertBusBussinessRealSelective(BusBussinessReal BusBussinessReal);

    int updateBusBussinessRealByPrimaryKeySelective(BusBussinessReal busBussinessReal);

    /**
     * 验证实名认证
     * 
     * @param busUser
     * @param busBussinessInfo
     * @param busBussinessReal
     * @param dataPath
     * @throws Exception
     */

    void updateBusBussinessRealAndInfoAndUser(BusUser busUser, BusBussinessInfo busBussinessInfo, BusBussinessReal busBussinessReal, String dataPath) throws Exception;

    /**
     * 根据条件查询子账户列表
     * 
     * @param infoId
     * @return
     */
    List<SubAccountModel> selectSubAccounts(Map<String, Object> param);

    /**
     * 修改
     */
    Map<String, Object> updateHeadImgAndUserName(WebUser webUser, String username, File headImg, String dataPath) throws Exception;

    /**
     * 分页查询消息列表
     * 
     * @param param
     * @return
     */
    List<SysNoticeMessage> selectSysNoticeByPage(Map<String, Object> param);

    /**
     * 插入运营商实名审核信息
     * 
     * @param busBussinessReal
     * @param licenceImg 运营证件图片
     * @param corporateImg 法人身份证正面图片
     * @param transatorImg 操作人身份证正面图片
     * @return
     */
    String insertAndUpdateBusBussinessReal(BusBussinessReal busBussinessReal, File licenceImg, File corporateImg, File transatorImg, String dataPath) throws Exception;

    /**
     * 更新sysNotice
     * 
     * @param sysNotice
     * @return
     */
    int updateSysNoticeByPrimaryKeySelective(SysNotice sysNotice);

    /**
     * 根据条件查询单一信息
     * 
     * @param param
     * @return
     */
    SysNoticeMessage selectSysNoticeMessage(Map<String, Object> param);

    /**
     * 查询信息中间表
     * 
     * @param id
     * @return
     */
    SysNotice selectSysNoticeByPrimaryKey(Integer id);

    // ------------------------------bus_suggestion---------------------------
    int insertSuggestion(BusSuggestion record);

    int updateSuggestionByPKSelective(BusSuggestion record);

    BusSuggestion selectSuggestionByPK(BusSuggestionKey key);

    List<BusSuggestion> selectSuggestionByPage(Map<String, Object> params);

    // -----------------bus_user_token-----------------------------------------
    int insertUserToken(BusUserToken record);

    int updateUserTokenByPK(BusUserToken record);

    BusUserToken selectUserTokenByPK(Integer id);

    BusUser selectBusUserByPrimaryKey(Integer id);

    int updateBusUserByExampleSelective(BusUser record, BusUserExample example);

    int countBusUserByExample(BusUserExample example);

    BusGroupInfo selectBusGroupInfoByPrimaryKey(Integer id);

    /**
     * 充电卡管理查询用户信息
     * 
     * @param params
     * @return
     */
    List<BusUserInfo> selectUserInfoForCardByPage(Map<String, Object> params);

    /**
     * 获取用户充电卡列表
     * 
     * @param example
     * @return
     */
    List<BusChargeCard> selectChargeCardByExample(BusChargeCardExample example);

    /**
     * 开户
     * 
     * @param user
     * @param userInfo
     * @return
     */
    int insertCardUser(BusUser user, BusUserInfo userInfo);

    /**
     * 开卡
     * 
     * @param record
     * @return
     */
    int insertChargeCardSelective(BusChargeCard record);

    /**
     * 充电卡数量
     */
    int countChargeCardByExample(BusChargeCardExample example);

    /**
     * 插入充值记录
     * 
     * @param record
     * @return
     */
    int insertCardRechargeSelective(BusCardRecharge record);

    /**
     * 充电卡充值
     * 
     * @param cardRecharge
     * @param chargeCard
     */
    void insertAndUpdateCardRecharge(BusCardRecharge cardRecharge, BusChargeCard chargeCard);

    /**
     * 更新卡信息
     * 
     * @param record
     * @param example
     * @return
     */
    int updateChargeCardByExampleSelective(BusChargeCard record, BusChargeCardExample example);

    /**
     * 查询充电卡充值记录
     * 
     * @param params
     * @return
     */
    List<BusCardRecharge> selectCardRechargeByPage(Map<String, Object> params);

    /**
     * 清灰色记录
     * 
     * @param dcsChargerecordList
     * @return
     */
    int insertAndUpdateBadCardCharge(List<String> chargerecordList);

    /**
     * 清灰色记录
     * 
     * @param newRecord
     * @param newPay
     */
    void updateCleanBadRecord(DcsChargerecord newRecord, BusPayment newPay, BusChargeCard newChargeCard, BigDecimal operateMoney);

    /**
     * 添加优惠方案
     * 
     * @param record
     * @return
     */
    int insertRebate(BusRebate record);

    /**
     * 更新优惠方案
     * 
     * @param record
     * @return
     */
    int updateRebateByPrimaryKeySelective(BusRebate record);

    /**
     * 分页查询优惠方案
     * 
     * @param params
     * @return
     */
    List<BusRebate> selectRebateByPage(Map<String, Object> params);

    /**
     * 查询优惠方案
     * 
     * @param id
     * @return
     */
    BusRebate selectRebateByPrimaryKey(Integer id);

    /**
     * 查询优惠方案记录数
     * 
     * @param example
     * @return
     */
    int countUserRebateByExample(BusUserRebateExample example);

    /**
     * 删除优惠方案
     * 
     * @param example
     * @return
     */
    int deleteRebateByPrimaryKey(Integer id);

    /**
     * 查询优惠方案
     * 
     * @param example
     * @return
     */
    List<BusRebate> selectRebateByExample(BusRebateExample example);

    List<BusUserRebate> selectUserRebateByExample(BusUserRebateExample example);
}
