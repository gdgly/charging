package com.holley.charging.app.bus.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;

import com.holley.charging.action.BaseAction;
import com.holley.charging.app.util.AlipayConfig;
import com.holley.charging.app.util.AlipayNotify;
import com.holley.charging.app.util.AlipayParams;
import com.holley.charging.app.util.AppTool;
import com.holley.charging.app.util.HttpXmlUtils;
import com.holley.charging.app.util.JdomParseXmlUtils;
import com.holley.charging.app.util.WechatConfig;
import com.holley.charging.app.util.WechatParams;
import com.holley.charging.app.util.WechatPayResult;
import com.holley.charging.app.util.WechatSignUtils;
import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusRecharge;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.AppointmentService;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.charging.service.bussiness.RechargeService;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.ResultBean;
import com.holley.common.constants.app.ErrorCodeConstants;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.FundOperateTypeEnum;
import com.holley.common.constants.charge.PayWayEnum;
import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.platform.util.RoleUtil;

/**
 * 支付管理
 * 
 * @author lenovo
 */
public class PaymentAction extends BaseAction {

    private static final Logger logger                        = Logger.getLogger(PaymentAction.class);
    private static final long   serialVersionUID              = 1L;
    private ResultBean          resultBean                    = new ResultBean();
    private String              userkey;
    private ChargingService     chargingService;
    private AppointmentService  appointmentService;
    private RechargeService     rechargeService;
    private AccountService      accountService;
    private AlipayParams        alipayParams;
    private UserService         userService;
    // 支付宝支付异步调用地址
    private static String       CHARGE_ALIPAY_NOTICE_URL      = "/pay/alipayCallbackForCharge.htm";     // 充电支付
    private static String       APPOINTMENT_ALIPAY_NOTICE_URL = "/pay/alipayCallbackForAppointment.htm"; // 预约支付
    private static String       RECHARGE_ALIPAY_NOTICE_URL    = "/pay/alipayCallbackForRecharge.htm";   // 充值支付

    // 微信支付异步调用地址
    private static String       CHARGE_WECHAT_NOTICE_URL      = "/pay/wechatCallbackForCharge.htm";     // 充电支付
    private static String       APPOINTMENT_WECHAT_NOTICE_URL = "/pay/wechatCallbackForAppointment.htm"; // 预约支付
    private static String       RECHARGE_WECHAT_NOTICE_URL    = "/pay/wechatCallbackForRecharge.htm";   // 充值支付

    /**
     * 充电、预约缴费
     *
     * @return
     */
    public String payment() {
        userkey = decrypt(this.getAttribute("userkey"));
        String payid = this.getAttribute("payid");
        String payway = this.getAttribute("payway");// 1:钱包,2:支付宝,3：微信
        String paytype = this.getAttribute("paytype");// 2:充电,3:预约
        if (AppTool.isNotNumber(userkey, payid, payway, paytype) || FundOperateTypeEnum.getEnmuByValue(Integer.parseInt(paytype)) == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004： 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        Integer userId = Integer.valueOf(userkey);
        Integer payId = Integer.valueOf(payid);
        Integer payType = Integer.valueOf(paytype);
        // TODO 如果是集团子账户传递集团账户ID start
        BusUser busUser = userService.selectUserByCache(userId);// 增加用户类型判断
        Integer groupUserId = busUser.getGroupId();
        boolean isGroupUser = false;// 是否集团子账户
        if (groupUserId != null && groupUserId > 0) {
            userId = groupUserId;
            isGroupUser = true;
        }
        // 如果是集团子账户传递集团账户ID end
        if (PayWayEnum.ACCOUNT.getValue() == Integer.valueOf(payway) || isGroupUser) {// 钱包 如果为集团子账户直接集团账户余额支付
            // 更新用户账户和平台账户
            String msg = accountService.updateAccountAndPaystatus(userId, payType, payId, busUser);
            if ("充电费用已支付过!".equals(msg) || "预约费用已支付过!".equals(msg)) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAY_PAID);// 3:费用已支付过
                resultBean.setMessage(msg);
                return SUCCESS;
            } else if ("钱包已被冻结!".equals(msg)) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAY_BLOCKED);// 4:钱包已被冻结
                resultBean.setMessage(msg);
                return SUCCESS;
            } else if ("账户可用金额不足!".equals(msg)) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAY_NOMONEY);// 1:账户可用金额不足
                resultBean.setMessage(msg);
                return SUCCESS;
            } else if ("缴费金额不大于0.00元,不需要缴费!".equals(msg)) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAY_FEENULL);// 2:缴费金额为空或不大于0
                resultBean.setMessage(msg);
                return SUCCESS;
            }
        } else if (PayWayEnum.ALIPAY.getValue() == Integer.valueOf(payway)) {// 支付宝
            dealAlipay(userId, payType, payId);
        } else if (PayWayEnum.WECHAT.getValue() == Integer.valueOf(payway)) {// 微信
            dealWechatpay(userId, payType, payId);
        } else {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_NOWAY);
            resultBean.setMessage("不支持该支付方式!");
        }
        return SUCCESS;
    }

    /**
     * 处理支付宝支付
     * 
     * @return
     */
    private boolean dealAlipay(Integer userid, int paytype, Integer payid) {
        AlipayParams params = new AlipayParams();
        String noticeUrl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.APP_URL) + getRequest().getContextPath();
        if (FundOperateTypeEnum.CHARGING.getValue() == paytype) {// 充电
            BusPayment record = chargingService.selectPaymentByPK(payid);
            if (record == null) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_NORECORD);// 3:缴费记录不存在
                resultBean.setMessage("缴费记录不存在!");
                return false;
            }
            if (ChargePayStatusEnum.SUCCESS.getShortValue().equals(record.getPayStatus())) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_PAID);// 5:记录已支付过
                resultBean.setMessage("充电费用已经支付过!");
                return false;
            }
            if (record.getShouldMoney() == null || record.getShouldMoney().doubleValue() <= 0) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_FEENULL);// 4： 缴费金额为空或不大于0
                resultBean.setMessage("缴费金额不大于0.00元,不需要缴费!");
                return false;
            }
            params.setSubject("充电支付");
            params.setBody("充电支付");
            params.setTotal_fee(NumberUtil.formateScale2(record.getShouldMoney()));
            params.setOut_trade_no(record.getTradeNo());
            params.setNotify_url(noticeUrl + CHARGE_ALIPAY_NOTICE_URL);

            // 更新充电缴费记录的支付方式和缴费状态
            if (!PayWayEnum.ALIPAY.getShortValue().equals(record.getPayWay()) || !ChargePayStatusEnum.PAYING.getShortValue().equals(record.getPayStatus())) {
                BusPayment item = new BusPayment();
                item.setId(record.getId());
                item.setPayWay(PayWayEnum.ALIPAY.getShortValue());
                item.setPayStatus(ChargePayStatusEnum.PAYING.getShortValue());
                item.setUpdateTime(new Date());
                chargingService.updatePaymentByPKSelective(item);
            }
        } else if (FundOperateTypeEnum.APPOINTMENT.getValue() == paytype) {// 预约
            BusAppointment record = appointmentService.selectAppointmentByPK(payid);
            if (record == null) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_NORECORD);// 3:缴费记录不存在
                resultBean.setMessage("缴费记录不存在!");
                return false;
            }
            if (AppointmentPayStatusEnum.SUCCESS.getShortValue().equals(record.getPayStatus())) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_PAID);// 5:记录已支付过
                resultBean.setMessage("预约费用已经支付过!");
                return false;
            }
            if (record.getAppFee() == null || record.getAppFee().doubleValue() <= 0) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_FEENULL);// 4： 缴费金额为空或不大于0
                resultBean.setMessage("缴费金额不大于0.00元,不需要缴费!");
                return false;
            }
            params.setSubject("预约支付");
            params.setBody("预约支付");
            params.setTotal_fee(NumberUtil.formateScale2(record.getAppFee()));
            params.setOut_trade_no(record.getAppNo());
            params.setNotify_url(noticeUrl + APPOINTMENT_ALIPAY_NOTICE_URL);

            // 更新预约记录的支付方式和支付状态
            if (!PayWayEnum.ALIPAY.getShortValue().equals(record.getPayWay()) || !AppointmentPayStatusEnum.PAYING.getShortValue().equals(record.getPayStatus())) {
                BusAppointment item = new BusAppointment();
                item.setId(record.getId());
                item.setPayWay(PayWayEnum.ALIPAY.getShortValue());
                item.setPayStatus(AppointmentPayStatusEnum.PAYING.getShortValue());
                item.setAddTime(new Date());
                appointmentService.updateAppointmentByPKSelective(item);
            }
        }
        params.setPartner(AlipayConfig.partner);
        params.setSeller_id(AlipayConfig.seller_id);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("alipayparams", params);
        resultBean.setData(data);
        return true;
    }

    /**
     * 处理微信支付
     * 
     * @return
     */
    private boolean dealWechatpay(Integer userid, int paytype, Integer payid) {
        String noticeUrl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.APP_URL) + getRequest().getContextPath();
        WechatParams params = new WechatParams();
        if (FundOperateTypeEnum.CHARGING.getValue() == paytype) {// 充电
            BusPayment record = chargingService.selectPaymentByPK(payid);
            if (record == null) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_NORECORD);// 3:缴费记录不存在
                resultBean.setMessage("缴费记录不存在!");
                return false;
            }
            if (ChargePayStatusEnum.SUCCESS.getShortValue().equals(record.getPayStatus())) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_PAID);// 5:记录已支付过
                resultBean.setMessage("充电费用已经支付过!");
                return false;
            }
            if (record.getShouldMoney() == null) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_NORECORD);// 3:缴费记录不存在
                resultBean.setMessage("金额为空!");
                return false;
            }
            params.setBody("充电支付");
            params.setDetail("充电支付");
            params.setNotify_url(noticeUrl + CHARGE_WECHAT_NOTICE_URL);
            params.setOut_trade_no(record.getTradeNo());
            params.setTotal_fee(NumberUtil.formateScale2(record.getShouldMoney()));

            // 更新充电缴费记录的支付方式和缴费状态
            if (!PayWayEnum.WECHAT.getShortValue().equals(record.getPayWay()) || !ChargePayStatusEnum.PAYING.getShortValue().equals(record.getPayStatus())) {
                BusPayment item = new BusPayment();
                item.setId(record.getId());
                item.setPayWay(PayWayEnum.WECHAT.getShortValue());
                item.setPayStatus(ChargePayStatusEnum.PAYING.getShortValue());
                item.setUpdateTime(new Date());
                chargingService.updatePaymentByPKSelective(item);
            }
        } else if (FundOperateTypeEnum.APPOINTMENT.getValue() == paytype) {// 预约
            BusAppointment record = appointmentService.selectAppointmentByPK(payid);
            if (record == null) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_NORECORD);// 3:缴费记录不存在
                resultBean.setMessage("缴费记录不存在!");
                return false;
            }
            if (AppointmentPayStatusEnum.SUCCESS.getShortValue().equals(record.getPayStatus())) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_PAID);// 5:记录已支付过
                resultBean.setMessage("预约费用已经支付过!");
                return false;
            }
            if (record.getAppFee() == null) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_NORECORD);// 3:缴费记录不存在
                resultBean.setMessage("金额为空!");
                return false;
            }
            params.setBody("预约支付");
            params.setDetail("预约支付");
            params.setNotify_url(noticeUrl + APPOINTMENT_WECHAT_NOTICE_URL);
            params.setOut_trade_no(record.getAppNo());
            params.setTotal_fee(NumberUtil.formateScale2(record.getAppFee()));

            // 更新预约记录的支付方式和支付状态
            if (!PayWayEnum.WECHAT.getShortValue().equals(record.getPayWay()) || !AppointmentPayStatusEnum.PAYING.getShortValue().equals(record.getPayStatus())) {
                BusAppointment item = new BusAppointment();
                item.setId(record.getId());
                item.setPayWay(PayWayEnum.WECHAT.getShortValue());
                item.setPayStatus(AppointmentPayStatusEnum.PAYING.getShortValue());
                item.setAddTime(new Date());
                appointmentService.updateAppointmentByPKSelective(item);
            }
        }
        params.setAppid(WechatConfig.appid);
        params.setMch_id(WechatConfig.mch_id);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("wechatparams", params);
        resultBean.setData(data);
        return true;
    }

    /***
     * 钱包充值
     * 
     * @return
     */
    public String paymentforrecharge() {
        userkey = decrypt(this.getAttribute("userkey"));
        String money = this.getAttribute("money");
        String payway = this.getAttribute("payway");// 2:支付宝,3：微信
        if (AppTool.isNotNumber(userkey, money, payway)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004： 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        BusUser busUser = userService.selectUserByCache(Integer.valueOf(userkey));// 增加用户类型判断
        if (busUser.getGroupId() != null && busUser.getGroupId() > 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004： 用户输入参数非法
            resultBean.setMessage("用户非法操作!");
            return SUCCESS;
        }
        PayWayEnum payWayEnum = PayWayEnum.getEnmuByValue(Integer.parseInt(payway));
        if (payWayEnum == null || (PayWayEnum.ALIPAY != payWayEnum && PayWayEnum.WECHAT != payWayEnum)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PAYRESU_NOWAY);// 2： 不支持该支付方式
            resultBean.setMessage("不支持该支付方式!");
            return SUCCESS;
        }
        BigDecimal fee = NumberUtil.formateScale2(new BigDecimal(money));
        if (fee.doubleValue() > Globals.RECHARGE_UPPERLIMIT) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_MONEY_UPPERLIMIT_EXCESS);// 超出充值金额上限
            resultBean.setMessage("充值金额最多为" + NumberUtil.formateScale2Str(new BigDecimal(Globals.RECHARGE_UPPERLIMIT)) + "元");
            return SUCCESS;
        }

        // 生成充值记录
        String rechargeNo = ChargingCacheUtil.getRechargeNo();
        BusRecharge record = new BusRecharge();
        record.setAccountInfo(null);
        record.setAddTime(new Date());
        record.setFee(null);
        record.setMoney(fee);
        record.setPayWay(Short.valueOf(payway));
        record.setStatus(RechargeStatusEnum.RECHARGING.getShortValue());
        record.setTradeNo(rechargeNo);
        record.setUserId(Integer.valueOf(userkey));
        if (rechargeService.insertRechargeSelective(record) <= 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);// 10003: 系统调用异常，请稍后重试
            resultBean.setMessage("系统调用异常，请稍后重试!");
            return SUCCESS;
        }

        Map<String, Object> data = new HashMap<String, Object>();
        String noticeUrl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.APP_URL) + getRequest().getContextPath();
        if (PayWayEnum.ALIPAY.getValue() == Integer.parseInt(payway)) {// 支付宝
            AlipayParams params = new AlipayParams();
            params.setSubject("充值支付");
            params.setBody("充值支付");
            params.setTotal_fee(fee);
            params.setOut_trade_no(rechargeNo);
            params.setNotify_url(noticeUrl + RECHARGE_ALIPAY_NOTICE_URL);
            params.setPartner(AlipayConfig.partner);
            params.setSeller_id(AlipayConfig.seller_id);
            data.put("alipayparams", params);
            resultBean.setData(data);
        } else if (PayWayEnum.WECHAT.getValue() == Integer.parseInt(payway)) {// 微信
            WechatParams params = new WechatParams();
            params.setAppid(WechatConfig.appid);
            params.setBody("充值支付");
            params.setDetail("充值支付");
            params.setTotal_fee(fee);
            params.setOut_trade_no(rechargeNo);
            params.setMch_id(WechatConfig.mch_id);
            params.setNotify_url(noticeUrl + RECHARGE_WECHAT_NOTICE_URL);
            data.put("wechatparams", params);
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    // -----------------------支付宝服务器异步通知处理------------------------------
    /**
     * 获取支付宝POST过来反馈信息,处理充电缴费记录的支付状态
     * 
     * @return
     */
    public void alipayCallbackForCharge() {
        logger.info("---[支付宝回调：充电支付]---start---" + DateUtil.DateToLongStr(new Date()));
        Map<String, String> params = AppTool.getRequestParams(getRequest().getParameterMap());
        try {
            String tradeStatus = params.get("trade_status");// 交易状态
            String outTradeNo = params.get("out_trade_no");// 商户订单号
            String buyerAccount = params.get("buyer_email");// 买家支付宝账号
            String totalFee = params.get("total_fee");// 交易金额
            String tradeNo = params.get("trade_no");// 支付宝交易号

            PrintWriter writer = getResponse().getWriter();
            if (AppTool.isNull(tradeStatus, outTradeNo, totalFee)) {
                writer.println(("fail"));
                return;
            }

            logger.info("---[支付宝回调：充电支付]---outTradeNo:" + outTradeNo);
            if (AlipayNotify.verify(params)) {// 验证成功
                ChargePayStatusEnum payStatus;
                if (tradeStatus.equals("WAIT_BUYER_PAY")) {// 表示等待付款
                    logger.info("---trade_status:WAIT_BUYER_PAY,不处理---" + DateUtil.DateToLongStr(new Date()));
                    writer.println(("success"));
                    if (writer != null) {
                        writer.flush();
                        writer.close();
                    }
                    return;
                } else if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                    payStatus = ChargePayStatusEnum.SUCCESS;
                } else {// TRADE_CLOSED:在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。
                    payStatus = ChargePayStatusEnum.FAILURE;
                }
                BigDecimal operateMoney = new BigDecimal(totalFee);
                String accountInfo = getAlipayAccountInfo(tradeNo, buyerAccount);
                chargingService.updatePaystatusAndAccount(outTradeNo, operateMoney, accountInfo, payStatus);
                logger.info("success");
                writer.println(("success"));
            } else {// 验证失败
                logger.info("fail");
                writer.println(("fail"));
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取支付宝POST过来反馈信息,处理预约记录的支付状态
     * 
     * @return
     */
    public void alipayCallbackForAppointment() {
        logger.info("----[支付宝回调：预约支付]---start---" + DateUtil.DateToLongStr(new Date()));
        Map<String, String> params = AppTool.getRequestParams(getRequest().getParameterMap());
        try {
            String tradeStatus = params.get("trade_status");// 交易状态
            String outTradeNo = params.get("out_trade_no");// 商户订单号
            String buyerAccount = params.get("buyer_email");// 买家支付宝账号
            String totalFee = params.get("total_fee");// 交易金额
            String tradeNo = params.get("trade_no");// 支付宝交易号

            PrintWriter writer = getResponse().getWriter();
            if (AppTool.isNull(tradeStatus, outTradeNo, totalFee)) {
                writer.println(("fail"));
                return;
            }

            logger.info("---[支付宝回调：预约支付]---outTradeNo:" + outTradeNo);
            if (AlipayNotify.verify(params)) {// 验证成功
                AppointmentPayStatusEnum payStatus;
                if (tradeStatus.equals("WAIT_BUYER_PAY")) {// 表示等待付款
                    logger.info("---trade_status:WAIT_BUYER_PAY,不处理---" + DateUtil.DateToLongStr(new Date()));
                    writer.println(("success"));
                    if (writer != null) {
                        writer.flush();
                        writer.close();
                    }
                    return;
                } else if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                    payStatus = AppointmentPayStatusEnum.SUCCESS;
                } else {// TRADE_CLOSED:在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。
                    payStatus = AppointmentPayStatusEnum.FAILURE;
                }
                BigDecimal operateMoney = new BigDecimal(totalFee);
                String accountInfo = getAlipayAccountInfo(tradeNo, buyerAccount);
                appointmentService.updatePaystatusAndAccount(outTradeNo, operateMoney, accountInfo, payStatus);
                logger.info("success");
                writer.println("success");
            } else {// 验证失败
                logger.info("fail");
                writer.println("fail");
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取支付宝POST过来反馈信息,处理充值记录的支付状态
     * 
     * @return
     */
    public void alipayCallbackForRecharge() {
        logger.info("----[支付宝回调：充值支付]---start---" + DateUtil.DateToLongStr(new Date()));
        Map<String, String> params = AppTool.getRequestParams(getRequest().getParameterMap());
        try {
            String tradeStatus = params.get("trade_status");// 交易状态
            String outTradeNo = params.get("out_trade_no");// 商户订单号
            String buyerAccount = params.get("buyer_email");// 买家支付宝账号
            String totalFee = params.get("total_fee");// 交易金额
            String tradeNo = params.get("trade_no");// 支付宝交易号

            PrintWriter writer = getResponse().getWriter();
            if (AppTool.isNull(tradeStatus, outTradeNo, totalFee)) {
                writer.println(("fail"));
                return;
            }
            logger.info("---[支付宝回调：充值支付]---outTradeNo:" + outTradeNo);
            if (AlipayNotify.verify(params)) {// 验证成功
                BigDecimal operateMoney = new BigDecimal(totalFee);
                String accountInfo = getAlipayAccountInfo(tradeNo, buyerAccount);
                RechargeStatusEnum rechargeStatus;
                if (tradeStatus.equals("WAIT_BUYER_PAY")) {// 表示等待付款
                    logger.info("---trade_status:WAIT_BUYER_PAY,不处理---" + DateUtil.DateToLongStr(new Date()));
                    writer.println(("success"));
                    if (writer != null) {
                        writer.flush();
                        writer.close();
                    }
                    return;
                } else if (tradeStatus.equals("TRADE_FINISHED") || tradeStatus.equals("TRADE_SUCCESS")) {
                    rechargeStatus = RechargeStatusEnum.SUCCESS;
                } else {// TRADE_CLOSED:在指定时间段内未支付时关闭的交易；在交易完成全额退款成功时关闭的交易。
                    rechargeStatus = RechargeStatusEnum.FAILURE;
                }
                rechargeService.updatePaystatusAndAccount(outTradeNo, operateMoney, accountInfo, rechargeStatus);
                logger.info("success");
                writer.println("success");
            } else {// 验证失败
                logger.info("fail");
                writer.println("fail");
            }
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -----------------------微信服务器异步通知处理------------------------------

    /**
     * 微信支付回调,处理充电缴费记录的支付状态
     * 
     * @return
     */
    @RequestMapping(value = "/notifyUrlWeixin")
    public void wechatCallbackForCharge() {
        logger.info("---[微信回调：充电支付]---start---" + DateUtil.DateToLongStr(new Date()));
        try {
            BufferedReader reader = getRequest().getReader();
            String line = "";
            StringBuffer inputString = new StringBuffer();
            try {
                PrintWriter writer = getResponse().getWriter();
                while ((line = reader.readLine()) != null) {
                    inputString.append(line);
                }
                if (reader != null) {
                    reader.close();
                }
                logger.info("----[微信回调：充电支付]接收到的报文---" + inputString.toString());
                if (!StringUtils.isEmpty(inputString.toString())) {
                    WechatPayResult wxPayResult = JdomParseXmlUtils.getWXPayResult(inputString.toString());
                    if ("SUCCESS".equalsIgnoreCase(wxPayResult.getReturn_code())) {
                        SortedMap<String, Object> parameters = AppTool.objectToTreeMap(wxPayResult);
                        // 反校验签名
                        String sign = WechatSignUtils.createSign("UTF-8", parameters);
                        if (AppTool.isNull(wxPayResult.getOut_trade_no(), wxPayResult.getTotal_fee(), sign)) {
                            logger.info("[FAIL]商户订单号为空或者总金额为空或者签名为空!");
                            writer.println(HttpXmlUtils.backWeixin("FAIL", "商户订单号为空或者总金额为空或者签名为空"));
                            writer.close();
                            return;
                        }
                        if (sign.equals(wxPayResult.getSign())) {
                            // 修改订单的状态
                            ChargePayStatusEnum payStatus;
                            if ("SUCCESS".equals(wxPayResult.getResult_code())) {// 表示交易成功
                                payStatus = ChargePayStatusEnum.SUCCESS;
                            } else {// 交易失败
                                payStatus = ChargePayStatusEnum.FAILURE;
                            }
                            BigDecimal operateMoney = new BigDecimal(wxPayResult.getTotal_fee());
                            operateMoney = operateMoney.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            String accountInfo = getWechatAccountInfo(wxPayResult.getTransaction_id());
                            chargingService.updatePaystatusAndAccount(wxPayResult.getOut_trade_no(), operateMoney, accountInfo, payStatus);
                            writer.println(HttpXmlUtils.backWeixin("SUCCESS", "OK"));
                            logger.info("[SUCCESS]OK!");
                        } else {
                            writer.println(HttpXmlUtils.backWeixin("FAIL", "签名失败"));
                            logger.info("[FAIL]签名失败!sign1=" + sign + ",sign2=" + wxPayResult.getSign());
                        }
                    } else {
                        writer.println(HttpXmlUtils.backWeixin("FAIL", wxPayResult.getReturn_msg()));
                        logger.info("---------微信支付返回Fail----------" + wxPayResult.getReturn_msg());
                    }
                } else {
                    logger.info("[FAIL]未获取到微信返回的结果!");
                    writer.println(HttpXmlUtils.backWeixin("FAIL", "未获取到微信返回的结果"));
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 微信支付回调,处理预约记录的支付状态
     * 
     * @return
     */
    @RequestMapping(value = "/notifyUrlWeixin")
    public void wechatCallbackForAppointment() {
        logger.info("---[微信回调：预约支付]---start---" + DateUtil.DateToLongStr(new Date()));
        try {
            BufferedReader reader = getRequest().getReader();
            String line = "";
            StringBuffer inputString = new StringBuffer();
            try {
                PrintWriter writer = getResponse().getWriter();
                while ((line = reader.readLine()) != null) {
                    inputString.append(line);
                }
                if (reader != null) {
                    reader.close();
                }
                logger.info("----[微信回调：预约支付]接收到的报文---" + inputString.toString());
                if (!StringUtils.isEmpty(inputString.toString())) {
                    WechatPayResult wxPayResult = JdomParseXmlUtils.getWXPayResult(inputString.toString());
                    if ("SUCCESS".equalsIgnoreCase(wxPayResult.getReturn_code())) {
                        SortedMap<String, Object> parameters = AppTool.objectToTreeMap(wxPayResult);
                        // 反校验签名
                        String sign = WechatSignUtils.createSign("UTF-8", parameters);
                        if (AppTool.isNull(wxPayResult.getOut_trade_no(), wxPayResult.getTotal_fee(), sign)) {
                            logger.info("[FAIL]商户订单号为空或者总金额为空或者签名为空!");
                            writer.println(HttpXmlUtils.backWeixin("FAIL", "商户订单号为空或者总金额为空或者签名为空"));
                            writer.close();
                            return;
                        }
                        if (sign.equals(wxPayResult.getSign())) {
                            // 修改订单的状态
                            AppointmentPayStatusEnum payStatus;
                            if ("SUCCESS".equals(wxPayResult.getResult_code())) {// 表示交易成功
                                payStatus = AppointmentPayStatusEnum.SUCCESS;
                            } else {// 交易失败
                                payStatus = AppointmentPayStatusEnum.FAILURE;
                            }
                            BigDecimal operateMoney = new BigDecimal(wxPayResult.getTotal_fee());
                            operateMoney = operateMoney.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            String accountInfo = getWechatAccountInfo(wxPayResult.getTransaction_id());
                            appointmentService.updatePaystatusAndAccount(wxPayResult.getOut_trade_no(), operateMoney, accountInfo, payStatus);
                            writer.println(HttpXmlUtils.backWeixin("SUCCESS", "OK"));
                            logger.info("[SUCCESS]OK!");
                        } else {
                            writer.println(HttpXmlUtils.backWeixin("FAIL", "签名失败"));
                            logger.info("[FAIL]签名失败!sign1=" + sign + ",sign2=" + wxPayResult.getSign());
                        }
                    } else {
                        writer.println(HttpXmlUtils.backWeixin("FAIL", wxPayResult.getReturn_msg()));
                        logger.info("---------微信支付返回Fail----------" + wxPayResult.getReturn_msg());
                    }
                } else {
                    writer.println(HttpXmlUtils.backWeixin("FAIL", "未获取到微信返回的结果"));
                    logger.info("[FAIL]未获取到微信返回的结果!");
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 微信支付回调,处理充值记录的支付状态
     * 
     * @return
     */
    @RequestMapping(value = "/notifyUrlWeixin")
    public void wechatCallbackForRecharge() {
        logger.info("---[微信回调：充值支付]---start---" + DateUtil.DateToLongStr(new Date()));
        try {
            BufferedReader reader = getRequest().getReader();
            String line = "";
            StringBuffer inputString = new StringBuffer();
            try {
                PrintWriter writer = getResponse().getWriter();
                while ((line = reader.readLine()) != null) {
                    inputString.append(line);
                }
                if (reader != null) {
                    reader.close();
                }
                logger.info("----[微信回调：充值支付]接收到的报文---" + inputString.toString());
                if (!StringUtils.isEmpty(inputString.toString())) {
                    WechatPayResult wxPayResult = JdomParseXmlUtils.getWXPayResult(inputString.toString());
                    if ("SUCCESS".equalsIgnoreCase(wxPayResult.getReturn_code())) {
                        SortedMap<String, Object> parameters = AppTool.objectToTreeMap(wxPayResult);
                        // 反校验签名
                        String sign = WechatSignUtils.createSign("UTF-8", parameters);
                        if (AppTool.isNull(wxPayResult.getOut_trade_no(), wxPayResult.getTotal_fee(), sign)) {
                            logger.info("[FAIL]商户订单号为空或者总金额为空或者签名为空!");
                            writer.println(HttpXmlUtils.backWeixin("FAIL", "商户订单号为空或者总金额为空或者签名为空"));
                            writer.close();
                            return;
                        }
                        if (sign.equals(wxPayResult.getSign())) {
                            // 修改订单的状态
                            RechargeStatusEnum payStatus;
                            if ("SUCCESS".equals(wxPayResult.getResult_code())) {// 表示交易成功
                                payStatus = RechargeStatusEnum.SUCCESS;
                            } else {// 交易失败
                                payStatus = RechargeStatusEnum.FAILURE;
                            }
                            BigDecimal operateMoney = new BigDecimal(wxPayResult.getTotal_fee());
                            operateMoney = operateMoney.divide(new BigDecimal(100), 2, RoundingMode.HALF_UP);
                            String accountInfo = getWechatAccountInfo(wxPayResult.getTransaction_id());
                            rechargeService.updatePaystatusAndAccount(wxPayResult.getOut_trade_no(), operateMoney, accountInfo, payStatus);
                            writer.println(HttpXmlUtils.backWeixin("SUCCESS", "OK"));
                            logger.info("[SUCCESS]OK!");
                        } else {
                            writer.println(HttpXmlUtils.backWeixin("FAIL", "签名失败"));
                            logger.info("[FAIL]签名失败!sign1=" + sign + ",sign2=" + wxPayResult.getSign());
                        }
                    } else {
                        writer.println(HttpXmlUtils.backWeixin("FAIL", wxPayResult.getReturn_msg()));
                        logger.info("---------微信支付返回Fail----------" + wxPayResult.getReturn_msg());
                    }
                } else {
                    writer.println(HttpXmlUtils.backWeixin("FAIL", "未获取到微信返回的结果"));
                    logger.info("[FAIL]未获取到微信返回的结果!");
                }
                if (writer != null) {
                    writer.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * 处理支付宝支付的账户信息
     * 
     * @param tradeNo
     * @param buyerAccount
     * @return
     */
    private String getAlipayAccountInfo(String tradeNo, String buyerAccount) {
        String accountInfo = "";
        if (!StringUtils.isEmpty(tradeNo)) {
            accountInfo += "[支付宝交易号:" + tradeNo + "]";
        }
        if (!StringUtils.isEmpty(buyerAccount)) {
            accountInfo += StringUtils.isEmpty(accountInfo) ? "" : "," + "[买家支付宝账号:" + buyerAccount + "]";
        }
        return accountInfo;
    }

    /**
     * 处理微信支付的账号信息
     * 
     * @param transactionid
     * @return
     */
    private String getWechatAccountInfo(String transactionid) {
        String accountInfo = "";
        if (!StringUtils.isEmpty(transactionid)) {
            accountInfo += "[微信支付订单号:" + transactionid + "]";
        }
        return accountInfo;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

    public AlipayParams getAlipayParams() {
        return alipayParams;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void setRechargeService(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
