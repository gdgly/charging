package com.holley.charging.serviceimpl.bussiness;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.holley.charging.common.util.AccountLogUtil;
import com.holley.charging.dao.bus.BusAccountLogMapper;
import com.holley.charging.dao.bus.BusAccountMapper;
import com.holley.charging.dao.bus.BusAppointmentMapper;
import com.holley.charging.dao.bus.BusBillsDetailMapper;
import com.holley.charging.dao.bus.BusBillsMapper;
import com.holley.charging.dao.bus.BusBussinessInfoMapper;
import com.holley.charging.dao.bus.BusBussinessRealMapper;
import com.holley.charging.dao.bus.BusCashMapper;
import com.holley.charging.dao.bus.BusFavoritesMapper;
import com.holley.charging.dao.bus.BusPaymentMapper;
import com.holley.charging.dao.bus.BusRechargeMapper;
import com.holley.charging.dao.bus.BusRepairPointMapper;
import com.holley.charging.dao.bus.BusUserInfoMapper;
import com.holley.charging.dao.bus.BusUserMapper;
import com.holley.charging.dao.bus.BusUserReceiptMapper;
import com.holley.charging.dao.pob.PobChargingStationMapper;
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
import com.holley.charging.service.bussiness.AccountService;
import com.holley.common.constants.BillCycleTypeEnum;
import com.holley.common.constants.BillMarkTypeEnum;
import com.holley.common.constants.Globals;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.AccountLogTypeEnum;
import com.holley.common.constants.charge.AccountStatusEnum;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.BillStatusEnum;
import com.holley.common.constants.charge.CashStatusEnum;
import com.holley.common.constants.charge.CashVerifyStatusEnum;
import com.holley.common.constants.charge.CertificateStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.FundOperateTypeEnum;
import com.holley.common.constants.charge.PayWayEnum;
import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.MsgUtil;
import com.holley.platform.util.RoleUtil;

public class AccountServiceImpl implements AccountService {

    private final static Logger      logger = Logger.getLogger(AccountServiceImpl.class);
    private BusAccountMapper         busAccountMapper;
    private BusCashMapper            busCashMapper;
    private BusAccountLogMapper      busAccountLogMapper;
    private PobChargingStationMapper pobChargingStationMapper;
    private BusFavoritesMapper       busFavoritesMapper;
    private BusRepairPointMapper     busRepairPointMapper;
    private BusUserInfoMapper        busUserInfoMapper;
    private BusBussinessInfoMapper   busBussinessInfoMapper;
    private BusBussinessRealMapper   busBussinessRealMapper;
    private BusAppointmentMapper     busAppointmentMapper;
    private BusPaymentMapper         busPaymentMapper;
    private BusUserMapper            busUserMapper;
    private BusRechargeMapper        busRechargeMapper;
    private BusBillsDetailMapper     busBillsDetailMapper;
    private BusBillsMapper           busBillsMapper;
    private BusUserReceiptMapper     busUserReceiptMapper;

    @Override
    public BusAccount selectAccoutByPrimaryKey(Integer userId) {
        return busAccountMapper.selectByPrimaryKey(userId);
    }

    @Override
    public List<UserAccount> selectAccountByPage(Map<String, Object> params) {
        return busAccountMapper.selectAccountByPage(params);
    }

    @Override
    public List<UserAccount> selectBjAccountByPage(Map<String, Object> params) {
        return busAccountMapper.selectBjAccountByPage(params);
    }

    public List<BusAccount> selectAccountByExample(BusAccountExample emp) {
        return busAccountMapper.selectByExample(emp);
    }

    @Override
    public void updateAccountForAccountBj(BusPayment record) {
        BusAccount userAccount = busAccountMapper.selectByPrimaryKey(record.getUserId());// 资金账户
        if (userAccount != null) {
            BusAccount account = new BusAccount();
            account.setTotalMoney(NumberUtil.sub(userAccount.getTotalMoney(), record.getShouldMoney()));
            account.setUsableMoney(NumberUtil.sub(userAccount.getUsableMoney(), record.getShouldMoney()));
            account.setFreezeMoney(userAccount.getFreezeMoney());
            account.setUserId(record.getUserId());
            account.setUpdateTime(new Date());

            BusPayment payment = new BusPayment();
            payment.setId(record.getId());
            payment.setPayStatus(ChargePayStatusEnum.SUCCESS.getShortValue());
            payment.setUpdateTime(new Date());
            busAccountMapper.updateByPrimaryKeySelective(account);
            busPaymentMapper.updateByPrimaryKeySelective(payment);
            // 日志
            AccountLogUtil.insertAccountLog(account, null, record.getId(), AccountLogTypeEnum.ACC_CHARGING, record.getShouldMoney(), null, null);
        }

    }

    @Override
    public String updateAccountAndPaystatus(Integer userid, int paytype, Integer payid, BusUser groupUser) {
        String msg = Globals.DEFAULT_MESSAGE;
        BigDecimal operateMoney = BigDecimal.ZERO;
        AccountLogTypeEnum accountLogType = null;
        if (FundOperateTypeEnum.CHARGING.getValue() == paytype) {// 充电
            BusPayment record = busPaymentMapper.selectByPrimaryKey(payid);
            if (ChargePayStatusEnum.SUCCESS.getShortValue().equals(record.getPayStatus())) {
                return "充电费用已支付过!";
            }
            operateMoney = record.getShouldMoney();
            accountLogType = AccountLogTypeEnum.ACC_CHARGING;
        } else if (FundOperateTypeEnum.APPOINTMENT.getValue() == paytype) {// 预约
            BusAppointment record = busAppointmentMapper.selectByPrimaryKey(payid);
            if (AppointmentPayStatusEnum.SUCCESS.getShortValue().equals(record.getPayStatus())) {
                return "预约费用已支付过!";
            }
            operateMoney = record.getAppFee();
            accountLogType = AccountLogTypeEnum.ACC_APPOINTMENT;
        }

        if (operateMoney == null || operateMoney.doubleValue() <= 0) {
            return "缴费金额不大于0.00元,不需要缴费!";// 缴费金额不大于0.00元,不需要缴费
        }

        // 更新用户账户和平台账户
        BusAccount userAccount = busAccountMapper.selectByPrimaryKey(userid);
        BigDecimal totalMoney;
        BigDecimal usableMoney;
        int row = 0;
        BusAccount newuAccount = null;
        BusAccount newpAccount = null;
        if (userAccount != null) {
            if (AccountStatusEnum.BLOCKED.getShortValue().equals(userAccount.getStatus())) {
                return "钱包已被冻结!";
            }
            totalMoney = userAccount.getTotalMoney();
            usableMoney = userAccount.getUsableMoney();
            if (usableMoney.doubleValue() < operateMoney.doubleValue()) {
                return "账户可用金额不足!";// 账户可用金额不足
            }
            totalMoney = totalMoney.subtract(operateMoney);
            usableMoney = usableMoney.subtract(operateMoney);

            newuAccount = new BusAccount();
            newuAccount.setUserId(userAccount.getUserId());
            newuAccount.setTotalMoney(totalMoney);
            newuAccount.setUsableMoney(usableMoney);
            newuAccount.setUpdateTime(new Date());
            row += busAccountMapper.updateByPrimaryKeySelective(newuAccount);
            newuAccount.setFreezeMoney(userAccount.getFreezeMoney());
        }
        // 更新平台账户
        BusAccount platformAccount = busAccountMapper.selectByPrimaryKey(Globals.ADMIN_USER_ID);
        if (platformAccount != null) {
            totalMoney = platformAccount.getTotalMoney().add(operateMoney);
            usableMoney = platformAccount.getUsableMoney().add(operateMoney);

            newpAccount = new BusAccount();
            newpAccount.setUserId(platformAccount.getUserId());
            newpAccount.setTotalMoney(totalMoney);
            newpAccount.setUsableMoney(usableMoney);
            newpAccount.setUpdateTime(new Date());
            row += busAccountMapper.updateByPrimaryKeySelective(newpAccount);
            newpAccount.setFreezeMoney(platformAccount.getFreezeMoney());
        }

        String updatetime = "";
        // 更新记录的支付信息
        if (FundOperateTypeEnum.CHARGING.getValue() == paytype) {// 充电
            BusPayment record = new BusPayment();
            record.setId(payid);
            record.setPayWay(PayWayEnum.ACCOUNT.getShortValue());
            record.setAccountInfo("[钱包账户:" + userid.toString() + "]");
            record.setPayStatus(ChargePayStatusEnum.SUCCESS.getShortValue());
            record.setUpdateTime(new Date());
            busPaymentMapper.updateByPrimaryKeySelective(record);
            updatetime = DateUtil.DateToStr(record.getUpdateTime(), DateUtil.TIME_LONG_CN);
        } else if (FundOperateTypeEnum.APPOINTMENT.getValue() == paytype) {// 预约
            BusAppointment record = new BusAppointment();
            record.setId(payid);
            record.setPayWay(PayWayEnum.ACCOUNT.getShortValue());
            record.setAccountInfo("[钱包账户:" + userid.toString() + "]");
            record.setPayStatus(AppointmentPayStatusEnum.SUCCESS.getShortValue());
            record.setAddTime(new Date());
            busAppointmentMapper.updateByPrimaryKeySelective(record);
            updatetime = DateUtil.DateToStr(record.getAddTime(), DateUtil.TIME_LONG_CN);
        }

        // 插入资金日志
        if (accountLogType != null && row > 0) {
            BusUser user = busUserMapper.selectByPrimaryKey(userid);
            if (UserTypeEnum.GROUP.getShortValue() == user.getUserType()) {
                newuAccount.setUserId(groupUser.getId());
            }
            AccountLogUtil.insertAccountLog(newuAccount, newpAccount, payid, accountLogType, operateMoney, null, null);

            // 发送账户消费短信
            if (user != null) {
                String paytypedesc = FundOperateTypeEnum.getText(paytype);
                String content = updatetime + "由" + paytypedesc + "支出" + NumberUtil.formateScale2Str(operateMoney) + "元";
                if (UserTypeEnum.GROUP.getShortValue() == user.getUserType()) {
                    // content = "您的子账户" + "[" + groupUser.getUsername() + "]于" + content;
                    content = "于" + content + ",消费用户" + "[" + groupUser.getUsername() + "]";
                }
                MsgUtil.sendAccountSMS(user.getPhone(), content, NumberUtil.formateScale2Str(newuAccount.getUsableMoney()) + "元");
            }
        }
        return msg;
    }

    @Override
    public int updateAccountByPKSelective(BusAccount record) {
        return busAccountMapper.updateByPrimaryKeySelective(record);
    }

    public CountPileModel countFastSlow(Map param) {
        return pobChargingStationMapper.countFastSlow(param);
    }

    public void insertBusCashAndUpdateBusAccount(BusCash newBusCash, BusAccount busAccount) {
        busCashMapper.insertSelective(newBusCash);
        int recordId = newBusCash.getId();
        if (recordId > 0) {
            BusAccount newBusAccount = new BusAccount();
            newBusAccount.setUserId(busAccount.getUserId());
            newBusAccount.setUpdateTime(new Date());
            newBusAccount.setUsableMoney(NumberUtil.sub(busAccount.getUsableMoney(), newBusCash.getMoney()));
            newBusAccount.setFreezeMoney(NumberUtil.add(busAccount.getFreezeMoney(), newBusCash.getMoney()));
            this.busAccountMapper.updateByPrimaryKeySelective(newBusAccount);
            newBusAccount.setTotalMoney(busAccount.getTotalMoney());
            // 插入资金日志
            AccountLogUtil.insertAccountLog(newBusAccount, null, recordId, AccountLogTypeEnum.APPLY_CASH, newBusCash.getMoney(), null, null);

            // 发送账户消费短信
            BusUser user = busUserMapper.selectByPrimaryKey(busAccount.getUserId());
            if (user != null) {
                String updatetime = DateUtil.DateToStr(newBusAccount.getUpdateTime(), DateUtil.TIME_LONG_CN);
                String content = updatetime + "由申请提现冻结" + NumberUtil.formateScale2Str(newBusCash.getMoney()) + "元";
                MsgUtil.sendAccountSMS(user.getPhone(), content, NumberUtil.formateScale2Str(newBusAccount.getUsableMoney()) + "元");
            }
        }
    }

    public List<BusCash> selectBusCashByPage(Map<String, Object> param) {
        return busCashMapper.selectBusCashByPage(param);
    }

    @Override
    public List<BusCash> selectCashVerifyByPage(Map<String, Object> param) {
        return busCashMapper.selectCashVerifyByPage(param);
    }

    @Override
    public BusCash selectCashInfoById(Integer id, Short type) {
        BusCash busCash = null;
        if (type.intValue() == UserTypeEnum.ENTERPRISE.getValue()) {
            busCash = busCashMapper.selectEnterpriseCashById(id);
        } else if (type.intValue() == UserTypeEnum.PERSON.getValue()) {
            busCash = busCashMapper.selectPersionCashById(id);
        } else {
            return null;
        }
        return busCash;
    }

    @Override
    public BusCash selectCashByPK(Integer id) {
        return busCashMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateCashByPKSelective(BusCash record) {
        return busCashMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public String updateCashValidAndAccount(BusCash record) {
        String msg = Globals.DEFAULT_MESSAGE;
        BusCash tempCash = new BusCash();
        tempCash.setId(record.getId());
        tempCash.setValidStatus(record.getValidStatus());
        tempCash.setValidTime(new Date());
        if (StringUtil.isNotEmpty(record.getValidRemark())) {
            tempCash.setValidRemark(record.getValidRemark());
        }
        if (CashVerifyStatusEnum.PASSED.getValue() == tempCash.getValidStatus().intValue()) {
            // 审核通过，提现状态改为提现中
            tempCash.setCashStatus(CashStatusEnum.WITHDRAWING.getShortValue());
        } else {
            // 审核不通过，操作金额从冻结变为解冻
            BusAccount busAccount = selectAccoutByPrimaryKey(record.getUserId());
            BigDecimal money = record.getMoney() == null ? BigDecimal.ZERO : record.getMoney();
            BigDecimal freezeMoney = busAccount.getFreezeMoney() == null ? BigDecimal.ZERO : busAccount.getFreezeMoney();
            BigDecimal usableMoney = busAccount.getUsableMoney() == null ? BigDecimal.ZERO : busAccount.getUsableMoney();

            BusAccount tempAccount = new BusAccount();
            tempAccount.setUserId(busAccount.getUserId());
            tempAccount.setUsableMoney(NumberUtil.add(usableMoney, money));
            tempAccount.setFreezeMoney(NumberUtil.sub(freezeMoney, money));
            tempAccount.setUpdateTime(new Date());
            if (busAccountMapper.updateByPrimaryKeySelective(tempAccount) > 0) {
                tempAccount.setTotalMoney(busAccount.getTotalMoney());
                AccountLogUtil.insertAccountLog(tempAccount, null, record.getId(), AccountLogTypeEnum.VALID_CASH, money, null, CashStatusEnum.FAILURE.getShortValue());
            }
        }
        busCashMapper.updateByPrimaryKeySelective(tempCash);
        return msg;
    }

    @Override
    public String updateCashStatusAndAccount(BusCash record) {
        String msg = Globals.DEFAULT_MESSAGE;
        BusCash tempCash = new BusCash();
        tempCash.setId(record.getId());
        tempCash.setCashStatus(record.getCashStatus());
        tempCash.setValidTime(new Date());
        if (StringUtil.isNotEmpty(record.getValidRemark())) {
            tempCash.setValidRemark(record.getValidRemark());
        }
        BusAccount busAccount = selectAccoutByPrimaryKey(record.getUserId());
        if (busAccount == null) {
            msg = "用户资金账户不存在.";
            return msg;
        }
        BigDecimal money = record.getMoney() == null ? BigDecimal.ZERO : record.getMoney();
        BigDecimal totalMoney = busAccount.getTotalMoney() == null ? BigDecimal.ZERO : busAccount.getTotalMoney();
        BigDecimal freezeMoney = busAccount.getFreezeMoney() == null ? BigDecimal.ZERO : busAccount.getFreezeMoney();
        BigDecimal usableMoney = busAccount.getUsableMoney() == null ? BigDecimal.ZERO : busAccount.getUsableMoney();

        BusAccount tempAccount = new BusAccount();
        tempAccount.setUserId(busAccount.getUserId());
        tempAccount.setUpdateTime(new Date());
        if (CashStatusEnum.SUCCESS.getValue() == tempCash.getCashStatus().intValue()) {
            // 提现成功，账户资金中，冻结资金扣除操作金额，同时更新总金额
            freezeMoney = freezeMoney.subtract(money);
            totalMoney = totalMoney.subtract(money);

            tempAccount.setFreezeMoney(freezeMoney);
            tempAccount.setTotalMoney(totalMoney);
        } else {
            // 提现失败，操作金额从冻结变为解冻
            freezeMoney = freezeMoney.subtract(money);
            usableMoney = usableMoney.add(money);

            tempAccount.setFreezeMoney(freezeMoney);
            tempAccount.setUsableMoney(usableMoney);
        }
        if (busAccountMapper.updateByPrimaryKeySelective(tempAccount) > 0) {
            if (tempAccount.getTotalMoney() == null) tempAccount.setTotalMoney(totalMoney);
            if (tempAccount.getUsableMoney() == null) tempAccount.setUsableMoney(usableMoney);
            AccountLogUtil.insertAccountLog(tempAccount, null, record.getId(), AccountLogTypeEnum.VALID_CASH, money, null, tempCash.getCashStatus());
        }
        busCashMapper.updateByPrimaryKeySelective(tempCash);
        return msg;
    }

    @Override
    public List<BusCash> selectCashFreezeMoneyByPage(Map<String, Object> params) {
        return busCashMapper.selectCashFreezeMoneyByPage(params);
    }

    @Override
    public BigDecimal selectCashFreezeMoneyTotal(Map<String, Object> params) {
        return busCashMapper.selectCashFreezeMoneyTotal(params);
    }

    @Override
    public List<BusCash> selectUserCashByPage(Map<String, Object> params) {
        return busCashMapper.selectUserCashByPage(params);
    }

    @Override
    public BigDecimal selectUserCashTotalMoney(Map<String, Object> params) {
        return busCashMapper.selectUserCashTotalMoney(params);
    }

    @Override
    public List<BusRepairPoint> selectRepairPointByExample(BusRepairPointExample example) {
        return busRepairPointMapper.selectByExample(example);
    }

    @Override
    public List<BillInfo> selectAccountBillByPage(Map<String, Object> params) {
        return busAccountLogMapper.selectAccountBillByPage(params);
    }

    @Override
    public int insertAccountLog(BusAccountLog record) {
        return busAccountLogMapper.insert(record);
    }

    @Override
    public int insertAccountLogSelective(BusAccountLog record) {
        return busAccountLogMapper.insertSelective(record);
    }

    @Override
    public List<BusFavorites> selectFavoritesByExample(BusFavoritesExample example) {
        return busFavoritesMapper.selectByExample(example);
    }

    @Override
    public List<BusFavorites> selectUserFavoritesByPage(Map<String, Object> params) {
        return busFavoritesMapper.selectUserFavoritesByPage(params);
    }

    @Override
    public int insertFavorites(BusFavorites record) {
        return busFavoritesMapper.insert(record);
    }

    @Override
    public int deleteFavoritesByPK(Integer id) {
        return busFavoritesMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int deleteFavoritesByExample(BusFavoritesExample example) {
        return busFavoritesMapper.deleteByExample(example);
    }

    @Override
    public BusUserInfo selectUserInfoByPrimaryKey(Integer id) {
        return busUserInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateBusUserInfo(BusUserInfo record) {
        return busUserInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public int updateUserInfoByPKSelective(BusUserInfo record) {
        return busUserInfoMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public Integer insertBusBussinessInfo(BusBussinessInfo record) {
        return busBussinessInfoMapper.insert(record);
    }

    @Override
    public BusBussinessInfo selectBusBussinessInfoByPrimaryKey(Integer id) {
        return busBussinessInfoMapper.selectByPrimaryKey(id);
    }

    @Override
    public Integer updateBusUserInfoByPrimaryKey(BusBussinessInfo record) {
        return busBussinessInfoMapper.updateByPrimaryKey(record);
    }

    @Override
    public List<BusBussinessReal> selectBusinessRealByPage(Map<String, Object> params) {
        return busBussinessRealMapper.selectBusinessRealByPage(params);
    }

    @Override
    public BusBussinessReal selectBusinessRealByPrimaryKey(BusBussinessRealKey key) {
        return busBussinessRealMapper.selectByPrimaryKey(key);
    }

    @Override
    public int updateBusinessRealByPrimaryKeySelective(BusBussinessReal record) {
        return busBussinessRealMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public void insertAndUpdateBussinessOrPersonBill(BillCycleTypeEnum billCycleType, RoleTypeEnum roleType) {
        logger.info("资金周期结算start。。。");
        Calendar calendar = Calendar.getInstance();
        calendar.roll(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上一个月日期
        String cycleDesc = DateUtil.DateToYYYYMMStr(calendar.getTime());// 结账周期描述
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        BigDecimal rechargeFee = null;// 充值总额
        BigDecimal cashFee = null;// 提现总额
        BigDecimal totalAppFeeOut = null;// 预约费支出总额
        BigDecimal totalChaFeeOut = null;// 充电费支出总额
        BigDecimal totalServiceFeeOut = null;// 服务费支出总额
        BigDecimal totalParkFeeOut = null;// 停车费支出总额
        BigDecimal totalFee = null;// 总费用（预约费，服务费，充电费，停车费）结算前
        BigDecimal checkTotalFee = null;// 总费用（预约费，服务费，充电费，停车费）结算后
        BigDecimal shouldMoney = null;// 每笔电费应付金额
        BigDecimal appFee = null;// 每笔未结算预约费
        BigDecimal checkAppFee = null;// 每笔结算预约费
        BigDecimal totalCheckAppFee = null;// 总结算预约费

        BigDecimal chaFee = null;// 充电费
        BigDecimal checkChaFee = null;// 每笔结算充电费
        BigDecimal totalCheckChaFee = null;// 总结算充电费

        BigDecimal serviceFee = null;// 服务费
        BigDecimal checkServiceFee = null;// 每笔结算服务费
        BigDecimal totalCheckServiceFee = null;// 总结算服务费

        BigDecimal parkFee = null;// 停车费
        BigDecimal checkParkFee = null;// 每笔结算停车费
        BigDecimal totalCheckParkFee = null;// 总结算停车费
        String rateStr = RoleUtil.selectRuleByPrimaryKey(RoleUtil.RATE);
        BigDecimal rate = new BigDecimal(rateStr);// 费率
        rate = BigDecimal.ONE.subtract(rate);
        BusBillsDetail busBillsDetail = null;
        BusBills busBills = null;
        List<BusBillsDetail> busBillsDetailListForApp = null;
        List<BusBillsDetail> busBillsDetailListForCha = null;
        List<BusUser> userList = null;
        Integer recordId;
        // 加载结算用户Start
        Map<String, Object> pam = new HashMap<String, Object>();

        if (roleType == RoleTypeEnum.ENTERPRISE) {
            pam.put("realStatus", CertificateStatusEnum.PASSED.getShortValue());
            pam.put("userType", UserTypeEnum.ENTERPRISE.getShortValue());
            pam.put("isLock", WhetherEnum.NO.getShortValue());
            pam.put("roleType", RoleTypeEnum.ENTERPRISE.getShortValue());
            userList = busUserMapper.selectBusUserByMap(pam);
        } else if (roleType == RoleTypeEnum.PERSON) {
            // pam.put("realStatus", CertificateStatusEnum.PASSED.getShortValue());
            pam.put("userType", UserTypeEnum.PERSON.getShortValue());
            pam.put("isLock", WhetherEnum.NO.getShortValue());
            userList = busUserMapper.selectBusUserByMap(pam);
        }
        // 加载结算用户End
        if (userList != null && userList.size() > 0) {
            Map<String, Object> param = new HashMap<String, Object>();
            param.put("payStatus", AppointmentPayStatusEnum.SUCCESS.getShortValue());
            param.put("rechargeStatus", RechargeStatusEnum.SUCCESS.getShortValue());
            param.put("cashStatus", CashStatusEnum.SUCCESS.getShortValue());
            param.put("isBill", WhetherEnum.NO.getShortValue());
            param.put("year", year);
            param.put("month", month);
            for (BusUser busUser : userList) {
                BusBillsExample billsEmp = new BusBillsExample();
                BusBillsExample.Criteria billsCr = billsEmp.createCriteria();
                billsCr.andUserIdEqualTo(busUser.getId());
                billsCr.andCheckCycleEqualTo(cycleDesc);
                List<BusBills> billsList = busBillsMapper.selectByExample(billsEmp);
                if (billsList != null && billsList.size() > 0) {
                    continue;
                }
                totalFee = new BigDecimal(0);
                checkTotalFee = new BigDecimal(0);
                totalCheckAppFee = new BigDecimal(0);
                totalCheckChaFee = new BigDecimal(0);
                totalCheckServiceFee = new BigDecimal(0);
                totalCheckParkFee = new BigDecimal(0);
                totalAppFeeOut = new BigDecimal(0);
                totalServiceFeeOut = new BigDecimal(0);
                totalChaFeeOut = new BigDecimal(0);
                totalParkFeeOut = new BigDecimal(0);
                busBills = new BusBills();
                rechargeFee = new BigDecimal(0);
                cashFee = new BigDecimal(0);
                List<BusPayment> chaList = null;
                List<BusAppointment> appList = null;
                param.put("busMec", busUser.getInfoId());
                param.put("busType", busUser.getUserType());
                param.put("userId", busUser.getId());
                // 预约记录
                appList = busAppointmentMapper.selectAppointmentByMap(param);
                // 充电记录
                chaList = busPaymentMapper.selectPaymentByMap(param);
                if (RoleTypeEnum.PERSON == roleType) {
                    // 预约费支出总额
                    totalAppFeeOut = NumberUtil.getNotNUll(busAppointmentMapper.getTotalAppFeeByMap(param));

                    List<ChargeModel> chargeModelList = this.busPaymentMapper.getTotalChaFeeByMap(param);
                    ChargeModel chargeModel = chargeModelList.get(0);
                    chargeModel = chargeModel != null ? chargeModel : new ChargeModel();
                    // 充电费支出总额
                    totalChaFeeOut = NumberUtil.getNotNUll(chargeModel.getTotalChaFeeOut());
                    // 服务费支总额
                    totalServiceFeeOut = NumberUtil.getNotNUll(chargeModel.getTotalServiceFeeOut());
                    // 停车费支出总额
                    totalParkFeeOut = NumberUtil.getNotNUll(chargeModel.getTotalParkFeeOut());
                    // 充值总额
                    rechargeFee = NumberUtil.getNotNUll(this.busRechargeMapper.getTotalRechargeFeeByMap(param));

                }
                // 提现总额
                cashFee = NumberUtil.getNotNUll(busCashMapper.getTotalCashByMap(param));
                if (appList != null && appList.size() > 0) {
                    busBillsDetailListForApp = new ArrayList<BusBillsDetail>();
                    List<BusAppointment> newAppointmentList = new ArrayList<BusAppointment>();
                    for (BusAppointment app : appList) {
                        app.setIsBill(WhetherEnum.YES.getShortValue());
                        appFee = NumberUtil.getNotNUll(app.getAppFee());// 预约费（结算前）
                        checkAppFee = NumberUtil.mul(appFee, rate);// 预约费（结算后）
                        totalCheckAppFee = NumberUtil.add(totalCheckAppFee, checkAppFee);
                        totalFee = NumberUtil.add(totalFee, appFee);

                        busBillsDetail = new BusBillsDetail();
                        busBillsDetail.setTotalFee(appFee);
                        busBillsDetail.setAppFeeIn(checkAppFee);
                        busBillsDetail.setCheckCycle(cycleDesc);
                        busBillsDetail.setCheckMark(BillMarkTypeEnum.APPOINTMENT.getShortValue());
                        busBillsDetail.setRecordId(app.getId());
                        busBillsDetail.setUserId(busUser.getId());
                        busBillsDetail.setAddTime(app.getAddTime());

                        busBillsDetailListForApp.add(busBillsDetail);
                        newAppointmentList.add(app);
                    }
                    if (busBillsDetailListForApp != null && busBillsDetailListForApp.size() > 0) {
                        // 批量插入资金周期细分表(预约) 修改预约记录结算状态为已结算
                        busAppointmentMapper.updateAppointmentBatch(newAppointmentList);
                        busBillsDetailMapper.insertBillsDetailBatch(busBillsDetailListForApp);
                    }
                }
                if (chaList != null && chaList.size() > 0) {
                    busBillsDetailListForCha = new ArrayList<BusBillsDetail>();
                    List<BusPayment> newChaList = new ArrayList<BusPayment>();
                    for (BusPayment busPayment : chaList) {
                        busPayment.setIsBill(WhetherEnum.YES.getShortValue());
                        shouldMoney = NumberUtil.getNotNUll(busPayment.getShouldMoney());// 应付充电总金额（结算前）
                        chaFee = NumberUtil.getNotNUll(busPayment.getChaFee());// 充电费（结算前）
                        serviceFee = NumberUtil.getNotNUll(busPayment.getServiceFee());// 服务费（结算前）
                        parkFee = NumberUtil.getNotNUll(busPayment.getParkFee());// 停车费（结算前）

                        checkChaFee = NumberUtil.mul(chaFee, rate);// 充电费（结算后）
                        totalCheckChaFee = NumberUtil.add(totalCheckChaFee, checkChaFee);

                        checkServiceFee = NumberUtil.mul(serviceFee, rate);// 服务费（结算后）
                        totalCheckServiceFee = NumberUtil.add(totalCheckServiceFee, checkServiceFee);

                        checkParkFee = NumberUtil.mul(parkFee, rate);// 停车费（结算后）
                        totalCheckParkFee = NumberUtil.add(totalCheckParkFee, checkParkFee);

                        totalFee = NumberUtil.add(totalFee, shouldMoney);// 总金额包含（预约，充电）结算前

                        busBillsDetail = new BusBillsDetail();
                        busBillsDetail.setTotalFee(shouldMoney);
                        busBillsDetail.setChaFeeIn(checkChaFee);
                        busBillsDetail.setServiceFeeIn(checkServiceFee);
                        busBillsDetail.setParkFeeIn(checkParkFee);
                        busBillsDetail.setCheckCycle(cycleDesc);
                        busBillsDetail.setCheckMark(BillMarkTypeEnum.CHARGE.getShortValue());
                        busBillsDetail.setRecordId(busPayment.getId());
                        busBillsDetail.setUserId(busUser.getId());
                        busBillsDetail.setAddTime(busPayment.getUpdateTime());

                        newChaList.add(busPayment);
                        busBillsDetailListForCha.add(busBillsDetail);
                    }
                    if (busBillsDetailListForCha != null && busBillsDetailListForCha.size() > 0) {
                        // 批量插入资金周期细分表(充电) 修改充电记录结算状态为已结算
                        busPaymentMapper.updatePaymentBatch(newChaList);
                        busBillsDetailMapper.insertBillsDetailBatch(busBillsDetailListForCha);
                    }
                }
                busBills.setTotalFee(totalFee);// 设置总金额(未结算)
                busBills.setAppFeeIn(totalCheckAppFee);// 预约费（已结算）
                busBills.setChaFeeIn(totalCheckChaFee);// 充电费（已结算）
                busBills.setServiceFeeIn(totalCheckServiceFee);// 总服务费（已结算）
                busBills.setParkFeeIn(totalCheckParkFee);// 总停车费（已结算）
                busBills.setAppFeeOut(totalAppFeeOut);// 预约费总支出
                busBills.setChaFeeOut(totalChaFeeOut);// 充电费总支出
                busBills.setServiceFeeOut(totalServiceFeeOut);// 服务费总支出
                busBills.setParkFeeOut(totalParkFeeOut);// 停车费总支出
                busBills.setCashFee(cashFee);// 总提现金额
                busBills.setRechargeFee(rechargeFee);// 充值总额
                busBills.setUserId(busUser.getId());
                busBills.setCheckType(BillCycleTypeEnum.BYMONTH.getShortValue());// 结账周期类型
                busBills.setCheckCycle(cycleDesc);// 结算周期描述
                busBills.setAddTime(new Date());// 添加时间
                // 插入资金周期总表 insert busBills
                busBillsMapper.insertSelective(busBills);
                recordId = busBills.getId();
                checkTotalFee = NumberUtil.add(checkTotalFee, totalCheckAppFee);
                checkTotalFee = NumberUtil.add(checkTotalFee, totalCheckChaFee);
                checkTotalFee = NumberUtil.add(checkTotalFee, totalCheckServiceFee);
                checkTotalFee = NumberUtil.add(checkTotalFee, totalCheckParkFee);
                if (checkTotalFee.compareTo(BigDecimal.ZERO) == 1) {
                    // 运营商账户
                    BusAccount bussinessAccount = busAccountMapper.selectByPrimaryKey(busUser.getId());
                    BusAccount newBusAccount = new BusAccount();
                    newBusAccount.setUserId(bussinessAccount.getUserId());
                    newBusAccount.setTotalMoney(NumberUtil.add(bussinessAccount.getTotalMoney(), checkTotalFee));
                    newBusAccount.setUsableMoney(NumberUtil.add(bussinessAccount.getUsableMoney(), checkTotalFee));
                    newBusAccount.setUpdateTime(new Date());
                    // 更新运营商账户资金
                    busAccountMapper.updateByPrimaryKeySelective(newBusAccount);
                    newBusAccount.setFreezeMoney(bussinessAccount.getFreezeMoney());
                    // 平台账户
                    BusAccount adminAccount = busAccountMapper.selectByPrimaryKey(Globals.ADMIN_USER_ID);
                    BusAccount newAdminAccount = new BusAccount();
                    newAdminAccount.setUserId(adminAccount.getUserId());
                    newAdminAccount.setTotalMoney(NumberUtil.sub(adminAccount.getTotalMoney(), checkTotalFee));
                    newAdminAccount.setUsableMoney(NumberUtil.sub(adminAccount.getUsableMoney(), checkTotalFee));
                    newAdminAccount.setUpdateTime(new Date());
                    // 更新平台账户资金
                    busAccountMapper.updateByPrimaryKeySelective(newAdminAccount);
                    newAdminAccount.setFreezeMoney(adminAccount.getFreezeMoney());
                    // 插入资金日志 accountLog
                    AccountLogUtil.insertAccountLog(newAdminAccount, newBusAccount, recordId, AccountLogTypeEnum.BILL, checkTotalFee, null, null);
                }
            }

        }

        logger.info("资金周期结算end。。。");
    }

    @Override
    public List<BusBills> selectBusBillsByMap(Map<String, Object> param) {
        return busBillsMapper.selectBusBillsByMap(param);
    }

    @Override
    public List<BillsInfo> selectBillsByPage(Map<String, Object> params) {
        return busBillsMapper.selectBillsByPage(params);
    }

    @Override
    public List<BusBills> selectBusBillsByExample(BusBillsExample example) {
        return busBillsMapper.selectByExample(example);
    }

    @Override
    public BusBills selectBillsByPK(Integer id) {
        return busBillsMapper.selectByPrimaryKey(id);
    }

    @Override
    public BigDecimal selectUserBillTotalIn(Map<String, Object> params) {
        return busBillsMapper.selectUserBillTotalIn(params);
    }

    @Override
    public List<BillsDetailModel> selectBillsDetailByPage(Map<String, Object> param) {
        return busBillsDetailMapper.selectBillsDetailByPage(param);
    }

    @Override
    public List<TradeRecord> selectBillDetailByParamByPage(Map<String, Object> params) {
        return busBillsDetailMapper.selectBillDetailByParamByPage(params);
    }

    @Override
    public List<BillsDetailIn> selectBillsDetailInByPage(Map<String, Object> params) {
        return busBillsDetailMapper.selectBillsDetailInByPage(params);
    }

    @Override
    public BusBillsDetail selectBillsDetailInTotal(Map<String, Object> params) {
        return busBillsDetailMapper.selectBillsDetailInTotal(params);
    }

    @Override
    public List<BillsDetail> selectBillsDetailOutByPage(Map<String, Object> params) {
        return busBillsDetailMapper.selectBillsDetailOutByPage(params);
    }

    @Override
    public BillsDetail selectBillsDetailOutTotal(Map<String, Object> params) {
        return busBillsDetailMapper.selectBillsDetailOutTotal(params);
    }

    @Override
    public List<BusCash> exportCashs(Map<String, Object> param) {
        return busCashMapper.exportCashs(param);
    }

    @Override
    public List<BillsDetailModel> exportBillsDetail(Map<String, Object> param) {
        return busBillsDetailMapper.exportBillsDetail(param);
    }

    @Override
    public List<BusRecharge> selectBusRechargeByPage(Map<String, Object> params) {
        return busRechargeMapper.selectByPage(params);
    }

    @Override
    public List<BusBills> selectBusBillsByPage(Map<String, Object> param) {
        return busBillsMapper.selectBusBillsByPage(param);
    }

    @Override
    public List<BusUserReceipt> selectBusUserReceiptByPage(Map<String, Object> params) {
        return busUserReceiptMapper.selectBusUserReceiptByPage(params);
    }

    @Override
    public List<UserReceipt> selectUserReceiptByPage(Map<String, Object> params) {
        return busUserReceiptMapper.selectUserReceiptByPage(params);
    }

    @Override
    public UserReceipt selectUserReceiptById(Integer id) {
        return busUserReceiptMapper.selectUserReceiptById(id);
    }

    @Override
    public BusUserReceipt selectUserReceiptByPK(Integer id) {
        return busUserReceiptMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateUserReceiptStatus(BusUserReceipt record) {
        int result = 0;
        // 更新BusUserReceipt的状态
        BusUserReceipt userReceipt = new BusUserReceipt();
        userReceipt.setId(record.getId());
        userReceipt.setStatus(record.getStatus());
        userReceipt.setExpressCompany(record.getExpressCompany());
        userReceipt.setExpressNum(record.getExpressNum());
        userReceipt.setValidRemark(record.getValidRemark());
        result += busUserReceiptMapper.updateByPrimaryKeySelective(userReceipt);

        // 如果审核失败，则更新BusBill表的RECEIPT_ID字段为空
        if (BillStatusEnum.FAILURE.getShortValue().equals(userReceipt.getStatus())) {
            BusBillsExample emp = new BusBillsExample();
            BusBillsExample.Criteria cr = emp.createCriteria();
            cr.andUserIdEqualTo(record.getUserId());
            cr.andReceiptIdEqualTo(record.getId());
            if (record.getTime().indexOf(",") == -1) {
                cr.andCheckCycleEqualTo(record.getTime());
            } else {
                String[] times = record.getTime().split(",");
                List<String> checkCycleList = new ArrayList<String>(Arrays.asList(times));
                cr.andCheckCycleIn(checkCycleList);
            }
            BusBills bills = new BusBills();
            bills.setReceiptId(-1);
            result += busBillsMapper.updateByExampleSelective(bills, emp);
        }
        return result;
    }

    @Override
    public int insertAndUpdateUserReceipt(BusUserReceipt busUserReceipt, List<BusBills> busBillsList) {
        int count = 0;
        BusBills newBills = null;
        busUserReceiptMapper.insertSelective(busUserReceipt);// 插入开票记录
        Integer id = busUserReceipt.getId();
        if (id != null && id > 0) {
            for (BusBills b : busBillsList) {
                newBills = new BusBills();
                newBills.setId(b.getId());
                newBills.setReceiptId(id);
                count = count + busBillsMapper.updateByPrimaryKeySelective(newBills);// 更新结算记录
            }
        }
        return count;
    }

    @Override
    public List<BusAccountLog> selectAccountLogByPage(Map<String, Object> params) {
        return busAccountLogMapper.selectAccountLogByPage(params);
    }

    @Override
    public List<BillInfo> selectOutAccountBillByPage(Map<String, Object> params) {
        return busAccountLogMapper.selectOutAccountBillByPage(params);
    }

    @Override
    public BigDecimal getTotalAppFeeByMap(Map<String, Object> param) {
        return busAppointmentMapper.getTotalAppFeeByMap(param);
    }

    @Override
    public List<ChargeModel> getTotalChaFeeByMap(Map<String, Object> param) {
        return busPaymentMapper.getTotalChaFeeByMap(param);
    }

    @Override
    public List<BusAppointment> selectAppointmentByMap(Map<String, Object> param) {
        return busAppointmentMapper.selectAppointmentByMap(param);
    }

    @Override
    public List<BusPayment> selectPaymentByMap(Map<String, Object> param) {
        return busPaymentMapper.selectPaymentByMap(param);
    }

    @Override
    public List<BusUserReceipt> selectBusUserReceiptByExample(BusUserReceiptExample example) {
        return busUserReceiptMapper.selectByExample(example);
    }

    @Override
    public List<BusRecharge> selectBusRechargeByExample(BusRechargeExample example) {
        return busRechargeMapper.selectByExample(example);
    }

    @Override
    public List<BusBills> selectGroupBillsByPage(Map<String, Object> param) {
        return busBillsMapper.selectGroupBillsByPage(param);
    }

    // ----------------get and set
    public void setBusAccountMapper(BusAccountMapper busAccountMapper) {
        this.busAccountMapper = busAccountMapper;
    }

    public void setBusCashMapper(BusCashMapper busCashMapper) {
        this.busCashMapper = busCashMapper;
    }

    public void setBusAccountLogMapper(BusAccountLogMapper busAccountLogMapper) {
        this.busAccountLogMapper = busAccountLogMapper;
    }

    public void setPobChargingStationMapper(PobChargingStationMapper pobChargingStationMapper) {
        this.pobChargingStationMapper = pobChargingStationMapper;
    }

    public void setBusFavoritesMapper(BusFavoritesMapper busFavoritesMapper) {
        this.busFavoritesMapper = busFavoritesMapper;
    }

    public void setBusRepairPointMapper(BusRepairPointMapper busRepairPointMapper) {
        this.busRepairPointMapper = busRepairPointMapper;
    }

    public void setBusUserInfoMapper(BusUserInfoMapper busUserInfoMapper) {
        this.busUserInfoMapper = busUserInfoMapper;
    }

    public void setBusBussinessInfoMapper(BusBussinessInfoMapper busBussinessInfoMapper) {
        this.busBussinessInfoMapper = busBussinessInfoMapper;
    }

    public void setBusBussinessRealMapper(BusBussinessRealMapper busBussinessRealMapper) {
        this.busBussinessRealMapper = busBussinessRealMapper;
    }

    public void setBusAppointmentMapper(BusAppointmentMapper busAppointmentMapper) {
        this.busAppointmentMapper = busAppointmentMapper;
    }

    public void setBusPaymentMapper(BusPaymentMapper busPaymentMapper) {
        this.busPaymentMapper = busPaymentMapper;
    }

    public void setBusUserMapper(BusUserMapper busUserMapper) {
        this.busUserMapper = busUserMapper;
    }

    public void setBusRechargeMapper(BusRechargeMapper busRechargeMapper) {
        this.busRechargeMapper = busRechargeMapper;
    }

    public void setBusBillsDetailMapper(BusBillsDetailMapper busBillsDetailMapper) {
        this.busBillsDetailMapper = busBillsDetailMapper;
    }

    public void setBusBillsMapper(BusBillsMapper busBillsMapper) {
        this.busBillsMapper = busBillsMapper;
    }

    public void setBusUserReceiptMapper(BusUserReceiptMapper busUserReceiptMapper) {
        this.busUserReceiptMapper = busUserReceiptMapper;
    }

}
