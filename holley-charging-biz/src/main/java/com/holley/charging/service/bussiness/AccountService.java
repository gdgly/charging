package com.holley.charging.service.bussiness;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.holley.charging.model.app.BillInfo;
import com.holley.charging.model.app.TradeRecord;
import com.holley.charging.model.bms.BillsDetail;
import com.holley.charging.model.bms.BillsDetailIn;
import com.holley.charging.model.bms.BillsInfo;
import com.holley.charging.model.bms.UserAccount;
import com.holley.charging.model.bms.UserReceipt;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusAccountExample;
import com.holley.charging.model.bus.BusAccountLog;
import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusBills;
import com.holley.charging.model.bus.BusBillsDetail;
import com.holley.charging.model.bus.BusBillsExample;
import com.holley.charging.model.bus.BusBussinessInfo;
import com.holley.charging.model.bus.BusBussinessReal;
import com.holley.charging.model.bus.BusBussinessRealKey;
import com.holley.charging.model.bus.BusCash;
import com.holley.charging.model.bus.BusFavorites;
import com.holley.charging.model.bus.BusFavoritesExample;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusRecharge;
import com.holley.charging.model.bus.BusRechargeExample;
import com.holley.charging.model.bus.BusRepairPoint;
import com.holley.charging.model.bus.BusRepairPointExample;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserInfo;
import com.holley.charging.model.bus.BusUserReceipt;
import com.holley.charging.model.bus.BusUserReceiptExample;
import com.holley.charging.model.def.BillsDetailModel;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.model.def.CountPileModel;
import com.holley.common.constants.BillCycleTypeEnum;
import com.holley.common.constants.RoleTypeEnum;

public interface AccountService {

    /**
     * 更新用户账户和平台账户的余额
     * 
     * @param record
     * @param operateMoney
     * @return
     */
    String updateAccountAndPaystatus(Integer userid, int paytype, Integer payid, BusUser groupUser);

    /**
     * 北京资金账户充电结算激费
     */
    void updateAccountForAccountBj(BusPayment record);

    int updateAccountByPKSelective(BusAccount record);

    BusAccount selectAccoutByPrimaryKey(Integer userId);

    List<UserAccount> selectAccountByPage(Map<String, Object> params);

    List<UserAccount> selectBjAccountByPage(Map<String, Object> params);

    /**
     * 获取账户信息
     * 
     * @param emp
     * @return
     */
    List<BusAccount> selectAccountByExample(BusAccountExample emp);

    /**
     * 获取快慢充电桩总数量
     * 
     * @param param
     * @return
     */
    CountPileModel countFastSlow(Map param);

    /**
     * 提现申请
     * 
     * @param newBusCash新生成的提现记录
     * @param busAccount账户信息
     */
    void insertBusCashAndUpdateBusAccount(BusCash newBusCash, BusAccount busAccount);

    /**
     * 分页查询提现记录
     * 
     * @param param
     * @return
     */
    List<BusCash> selectBusCashByPage(Map<String, Object> param);

    /**
     * 分页查询提现审核记录
     * 
     * @param param
     * @return
     */
    List<BusCash> selectCashVerifyByPage(Map<String, Object> param);

    /**
     * 根据提现记录id和用户类型查询提现信息
     * 
     * @param id
     * @param usertype
     * @return
     */
    BusCash selectCashInfoById(Integer id, Short usertype);

    BusCash selectCashByPK(Integer id);

    int updateCashByPKSelective(BusCash record);

    // 更新提现审核状态和账户资金信息
    String updateCashValidAndAccount(BusCash record);

    // 更新提现状态和账户资金信息
    String updateCashStatusAndAccount(BusCash record);

    List<BusCash> selectCashFreezeMoneyByPage(Map<String, Object> params);

    BigDecimal selectCashFreezeMoneyTotal(Map<String, Object> params);

    List<BusCash> selectUserCashByPage(Map<String, Object> params);

    BigDecimal selectUserCashTotalMoney(Map<String, Object> params);

    /**
     * 查询维护点
     * 
     * @param example
     * @return
     */
    List<BusRepairPoint> selectRepairPointByExample(BusRepairPointExample example);

    /**
     * 钱包账单记录分页查询
     * 
     * @param example
     * @return
     */

    List<BillInfo> selectAccountBillByPage(Map<String, Object> params);

    int insertAccountLog(BusAccountLog record);

    int insertAccountLogSelective(BusAccountLog record);

    /**
     * 查询个人收藏记录
     * 
     * @param example
     * @return
     */
    List<BusFavorites> selectFavoritesByExample(BusFavoritesExample example);

    List<BusFavorites> selectUserFavoritesByPage(Map<String, Object> params);

    int insertFavorites(BusFavorites record);

    int deleteFavoritesByPK(Integer id);

    int deleteFavoritesByExample(BusFavoritesExample example);

    /**
     * 查询用户信息 1.我的爱车
     * 
     * @param integer
     * @return
     */
    BusUserInfo selectUserInfoByPrimaryKey(Integer integer);

    int updateBusUserInfo(BusUserInfo record);

    int updateUserInfoByPKSelective(BusUserInfo record);

    Integer insertBusBussinessInfo(BusBussinessInfo record);

    BusBussinessInfo selectBusBussinessInfoByPrimaryKey(Integer integer);

    Integer updateBusUserInfoByPrimaryKey(BusBussinessInfo record);

    /**
     * 查询运营商实名认证信息
     * 
     * @param params
     * @return
     */
    List<BusBussinessReal> selectBusinessRealByPage(Map<String, Object> params);

    BusBussinessReal selectBusinessRealByPrimaryKey(BusBussinessRealKey key);

    int updateBusinessRealByPrimaryKeySelective(BusBussinessReal record);

    // -------------busBills-------------------------

    /**
     * 生成账单
     */
    void insertAndUpdateBussinessOrPersonBill(BillCycleTypeEnum billCycleType, RoleTypeEnum roleType);

    List<BusBills> selectBusBillsByMap(Map<String, Object> param);

    List<BusBills> selectBusBillsByExample(BusBillsExample example);

    BusBills selectBillsByPK(Integer id);

    /**
     * 分页查询账单
     * 
     * @param params
     * @return
     */
    List<BusBills> selectBusBillsByPage(Map<String, Object> param);

    List<BusBills> selectGroupBillsByPage(Map<String, Object> param);

    /**
     * 分页查询账单BMS
     * 
     * @param params
     * @return
     */
    List<BillsInfo> selectBillsByPage(Map<String, Object> params);

    /**
     * 查询用户已结算的总收入
     */
    BigDecimal selectUserBillTotalIn(Map<String, Object> params);

    // -------------busBillsDetail-------------------------
    /**
     * 分页查询详细月账单
     * 
     * @param param
     * @return
     */
    List<BillsDetailModel> selectBillsDetailByPage(Map<String, Object> param);

    /**
     * app,分页查询我的设备已结算的交易明细（充电、预约）
     */
    List<TradeRecord> selectBillDetailByParamByPage(Map<String, Object> params);

    /**
     * bms,分页查询月收入明细
     * 
     * @param params
     * @return
     */
    List<BillsDetailIn> selectBillsDetailInByPage(Map<String, Object> params);

    BusBillsDetail selectBillsDetailInTotal(Map<String, Object> params);

    /**
     * bms,分页查询月支出明细
     * 
     * @param params
     * @return
     */
    List<BillsDetail> selectBillsDetailOutByPage(Map<String, Object> params);

    BillsDetail selectBillsDetailOutTotal(Map<String, Object> params);

    /**
     * 导出提现记录Excel
     * 
     * @param param
     * @return
     */
    List<BusCash> exportCashs(Map<String, Object> param);

    /**
     * 导出月账单
     * 
     * @param param
     * @return
     */
    List<BillsDetailModel> exportBillsDetail(Map<String, Object> param);

    /**
     * 查询充值记录
     * 
     * @param example
     * @return
     */
    List<BusRecharge> selectBusRechargeByPage(Map<String, Object> params);

    List<BusRecharge> selectBusRechargeByExample(BusRechargeExample example);

    // -------------busUserReceipt-------------------------

    /**
     * 分页查询发票
     * 
     * @param params
     * @return
     */
    List<BusUserReceipt> selectBusUserReceiptByPage(Map<String, Object> params);

    List<BusUserReceipt> selectBusUserReceiptByExample(BusUserReceiptExample example);

    /**
     * 分页查询用户开票申请BMS
     * 
     * @param params
     * @return
     */
    List<UserReceipt> selectUserReceiptByPage(Map<String, Object> params);

    UserReceipt selectUserReceiptById(Integer id);

    BusUserReceipt selectUserReceiptByPK(Integer id);

    int updateUserReceiptStatus(BusUserReceipt record);

    /**
     * 生成开票记录
     * 
     * @param record
     * @return
     */
    int insertAndUpdateUserReceipt(BusUserReceipt busUserReceipt, List<BusBills> busBillsList);

    /**
     * 分页查询资金日志
     */
    List<BusAccountLog> selectAccountLogByPage(Map<String, Object> params);

    /**
     * 分页查询消费明细
     * 
     * @param params
     * @return
     */
    List<BillInfo> selectOutAccountBillByPage(Map<String, Object> params);

    /**
     * 根据条件计算所有预约支出
     * 
     * @param param
     * @return
     */
    BigDecimal getTotalAppFeeByMap(Map<String, Object> param);

    /**
     * 计算总充电费用
     * 
     * @param param
     * @return
     */
    List<ChargeModel> getTotalChaFeeByMap(Map<String, Object> param);

    List<BusAppointment> selectAppointmentByMap(Map<String, Object> param);

    List<BusPayment> selectPaymentByMap(Map<String, Object> param);
}
