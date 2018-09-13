package com.holley.charging.user.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import jxl.write.WriteException;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.holley.charging.action.BaseAction;
import com.holley.charging.common.JxlXlsUtil;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.app.BillInfo;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusBills;
import com.holley.charging.model.bus.BusBillsExample;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusRecharge;
import com.holley.charging.model.bus.BusRechargeExample;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.bus.BusUserExample;
import com.holley.charging.model.bus.BusUserReceipt;
import com.holley.charging.model.def.ChargeModel;
import com.holley.charging.model.def.UserBillChartsModel;
import com.holley.charging.pay.util.AlipayConfig;
import com.holley.charging.pay.util.AlipayNotify;
import com.holley.charging.pay.util.AlipaySubmit;
import com.holley.charging.pay.util.HttpXmlUtils;
import com.holley.charging.pay.util.JdomParseXmlUtils;
import com.holley.charging.pay.util.MatrixToImageWriter;
import com.holley.charging.pay.util.Unifiedorder;
import com.holley.charging.pay.util.UnifiedorderResult;
import com.holley.charging.pay.util.WechatConfig;
import com.holley.charging.pay.util.WechatPayResult;
import com.holley.charging.pay.util.WechatSignUtils;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.AppointmentService;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.charging.service.bussiness.RechargeService;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.RoleTypeEnum;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.AppointmentStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.FundOperateTypeEnum;
import com.holley.common.constants.charge.PayWayEnum;
import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.common.constants.charge.UserTypeEnum;
import com.holley.common.dataobject.Page;
import com.holley.common.dataobject.WebUser;
import com.holley.common.util.DateUtil;
import com.holley.common.util.JsonUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.RoleUtil;
import com.holley.web.common.util.Validator;

/**
 * 个人用户账户相关
 * 
 * @author sc
 */
public class UserAccountAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(UserAccountAction.class);
    private UserService         userService;
    private AccountService      accountService;
    private AppointmentService  appointmentService;
    private ChargingService     chargingService;
    private Page                page;
    private RechargeService     rechargeService;
    private final static double maxRechargeMoney = 50000.0;
    private String              tradeStatus;

    public String wechatPay() throws Exception {
        String money = getParameter("rechargeMoney");
        String rechargeWay = getParameter("rechargeWay");
        WebUser webUser = getWebuser();
        String msg = checkRecharge(rechargeWay, money);
        if ("success".equals(msg)) {
            String out_trade_no = ChargingCacheUtil.getRechargeNo();
            getRequest().setAttribute("rechargeMoney", NumberUtil.formateScale2Str(new BigDecimal(money)));
            getRequest().setAttribute("rechargeWay", rechargeWay);
            getRequest().setAttribute("out_trade_no", out_trade_no);
            return SUCCESS;

        } else {
            getRequest().setAttribute("msg", msg);
            return "userMsg";
        }

    }

    public String createQRCode() throws Exception {
        WebUser webUser = getWebuser();
        String money = getParameter("rechargeMoney");
        String rechargeWay = getParameter("rechargeWay");
        String out_trade_no = getParameter("out_trade_no");
        String msg = checkRecharge(rechargeWay, money);
        if ("success".equals(msg) && !StringUtil.isEmpty(out_trade_no)) {
            BusRecharge record = new BusRecharge();
            record.setAccountInfo(null);
            record.setAddTime(new Date());
            record.setFee(null);
            record.setMoney(new BigDecimal(money));
            record.setPayWay(Short.valueOf(rechargeWay));
            record.setStatus(RechargeStatusEnum.RECHARGING.getShortValue());
            record.setTradeNo(out_trade_no);
            record.setUserId(webUser.getUserId());
            if (rechargeService.insertRechargeSelective(record) > 0) {
                BigDecimal tempmoney = new BigDecimal(money).multiply(new BigDecimal("100")).setScale(0);
                SortedMap<String, Object> params = new TreeMap<String, Object>();
                params.put("appid", WechatConfig.appid);
                params.put("mch_id", WechatConfig.mch_id);
                params.put("nonce_str", StringUtil.randomString(16));
                params.put("time_start", DateUtil.DateToLong14Str(new Date()));
                params.put("trade_type", "NATIVE");
                params.put("spbill_create_ip", getRemoteIP());
                params.put("body", "充值支付");
                params.put("detail", "充值支付");
                params.put("out_trade_no", out_trade_no);
                params.put("total_fee", tempmoney);
                String noticeUrl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.WEB_URL) + getRequest().getContextPath();
                params.put("notify_url", noticeUrl + WechatConfig.notify_url);

                String sign = WechatSignUtils.createSign("UTF-8", params);
                Iterator<String> it = params.keySet().iterator();
                String key;
                Object value;
                Unifiedorder unifiedorder = new Unifiedorder();
                while (it.hasNext()) {
                    key = it.next();
                    value = params.get(key);
                    try {
                        BeanUtils.setProperty(unifiedorder, key, value);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
                unifiedorder.setSign(sign);
                String weixinPos = HttpXmlUtils.httpsRequest(WechatConfig.unifiedorder_url, "POST", HttpXmlUtils.xmlInfo(unifiedorder)).toString();
                logger.info("xml============================" + weixinPos);
                UnifiedorderResult result = JdomParseXmlUtils.getUnifiedorderResult(weixinPos);
                MatrixToImageWriter.createCodeStream(result.getCode_url(), getResponse());
                return null;
            } else {
                getRequest().setAttribute("msg", "充值失败请重新操作！！");
                return "userMsg";
            }

        } else {
            getRequest().setAttribute("msg", msg);
            return "userMsg";
        }

    }

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
                        SortedMap<String, Object> parameters = WechatConfig.objectToTreeMap(wxPayResult);
                        // 反校验签名
                        String sign = WechatSignUtils.createSign("UTF-8", parameters);
                        if (WechatConfig.isNull(wxPayResult.getOut_trade_no(), wxPayResult.getTotal_fee(), sign)) {
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

    public String checkWechatPayStatus() {
        String out_trade_no = getParameter("out_trade_no");
        if (!StringUtil.isEmpty(out_trade_no)) {
            BusRechargeExample emp = new BusRechargeExample();
            BusRechargeExample.Criteria cr = emp.createCriteria();
            cr.andTradeNoEqualTo(out_trade_no);
            List<BusRecharge> list = accountService.selectBusRechargeByExample(emp);
            if (list != null && list.size() > 0) {
                BusRecharge recharge = list.get(0);
                if (RechargeStatusEnum.SUCCESS.getShortValue() == recharge.getStatus() || RechargeStatusEnum.FAILURE.getShortValue() == recharge.getStatus()) {
                    message = "success";
                }
            } else {
                message = ERROR;
            }
        } else {
            message = ERROR;
        }

        return SUCCESS;
    }

    /**
     * 取消当前交易
     * 
     * @return
     */
    public String cancelWechatPay() {
        String out_trade_no = getParameter("out_trade_no");
        if (!StringUtil.isEmpty(out_trade_no)) {
            BusRechargeExample emp = new BusRechargeExample();
            BusRechargeExample.Criteria cr = emp.createCriteria();
            cr.andTradeNoEqualTo(out_trade_no);
            List<BusRecharge> list = accountService.selectBusRechargeByExample(emp);
            if (list != null && list.size() > 0) {
                BusRecharge recharge = list.get(0);
            }
        }
        return SUCCESS;
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

    public String aliPay() {
        String money = getParameter("rechargeMoney");
        String rechargeWay = getParameter("rechargeWay");
        WebUser webUser = getWebuser();
        String msg = checkRecharge(rechargeWay, money);
        if ("success".equals(msg)) {
            BigDecimal rechargeMoney = NumberUtil.formateScale2(new BigDecimal(money));
            String noticeUrl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.WEB_URL) + getRequest().getContextPath();
            Map<String, String> param = AlipaySubmit.buildRequestPara(AlipayConfig.returnParam(rechargeMoney.toString(), noticeUrl));
            getRequest().setAttribute("sign", param.get("sign"));// 签名
            getRequest().setAttribute("body", param.get("body"));
            getRequest().setAttribute("_input_charset", param.get("_input_charset"));
            getRequest().setAttribute("subject", param.get("subject"));// 商品名称
            getRequest().setAttribute("total_fee", param.get("total_fee"));
            getRequest().setAttribute("sign_type", param.get("sign_type"));// 标签加密方式RSA/MD5
            getRequest().setAttribute("service", param.get("service"));
            getRequest().setAttribute("notify_url", param.get("notify_url"));// 异步返回地址
            getRequest().setAttribute("partner", param.get("partner"));// 商户号
            getRequest().setAttribute("seller_id", param.get("seller_id"));
            getRequest().setAttribute("out_trade_no", param.get("out_trade_no"));// 商户订单号
            getRequest().setAttribute("payment_type", param.get("payment_type"));
            getRequest().setAttribute("return_url", param.get("return_url"));// 同步返回地址
            getRequest().setAttribute("show_url", param.get("show_url"));// 同步返回地址
            BusRecharge record = new BusRecharge();
            record.setAccountInfo(null);
            record.setAddTime(new Date());
            record.setFee(null);
            record.setMoney(rechargeMoney);
            record.setPayWay(Short.valueOf(rechargeWay));
            record.setStatus(RechargeStatusEnum.RECHARGING.getShortValue());
            record.setTradeNo(param.get("out_trade_no"));
            record.setUserId(webUser.getUserId());
            if (rechargeService.insertRechargeSelective(record) <= 0) {
                getRequest().setAttribute("msg", "系统调用异常，请稍后重试!");
                return "userMsg";
            }

        } else {
            getRequest().setAttribute("msg", msg);
            return "userMsg";
        }

        return SUCCESS;
    }

    private String checkRecharge(String rechargeWay, String money) {
        String msg = "success";
        PayWayEnum payWayEnum = PayWayEnum.getEnmuByValue(Integer.parseInt(rechargeWay));
        if (payWayEnum == null || (PayWayEnum.ALIPAY != payWayEnum && PayWayEnum.WECHAT != payWayEnum)) {
            msg = "不支持该支付方式！！";
        } else if (StringUtil.isEmpty(money)) {
            msg = "请输入充值金额！！";
        }
        if ("success".equals(msg)) {
            double douMoney = NumberUtil.formateScale2(new BigDecimal(money)).doubleValue();
            if (douMoney <= 0) {
                msg = "输入的充值金额太小！！";
            } else if (douMoney > maxRechargeMoney) {
                msg = "充值金额不能大于" + maxRechargeMoney + "元！！";
            }
        }

        return msg;
    }

    /**
     * 异步回调
     * 
     * @return
     * @throws Exception
     */
    public void notifyAliPay() throws Exception {
        logger.info("----[支付宝异步回调：充值支付]---start---" + DateUtil.DateToLongStr(new Date()));
        // 获取支付宝POST过来反馈信息
        Map requestParams = getRequest().getParameterMap();
        Map<String, String> params = getRequestParams(requestParams);
        PrintWriter writer = getResponse().getWriter();
        try {
            String tradeStatus = params.get("trade_status");// 交易状态
            String outTradeNo = params.get("out_trade_no");// 商户订单号
            String buyerAccount = params.get("buyer_email");// 买家支付宝账号
            String totalFee = params.get("total_fee");// 交易金额
            String tradeNo = params.get("trade_no");// 支付宝交易号

            if (StringUtil.isNull(tradeStatus, outTradeNo, totalFee)) {
                writer.println("fail");
                return;
            }
            logger.info("---[支付宝回调：充值支付]---outTradeNo:" + outTradeNo);
            if (AlipayNotify.verify(params)) {// 验证成功
                BigDecimal operateMoney = new BigDecimal(totalFee);
                String accountInfo = getAlipayAccountInfo(tradeNo, buyerAccount);
                RechargeStatusEnum rechargeStatus;
                if (tradeStatus.equals("WAIT_BUYER_PAY")) {// 表示等待付款
                    logger.info("---trade_status:WAIT_BUYER_PAY,不处理---" + DateUtil.DateToLongStr(new Date()));
                    writer.println("success");
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

    /**
     * 同步回调
     * 
     * @return
     * @throws Exception
     */
    public String returnAliPay() throws Exception {
        logger.info("----[支付宝同步回调：充值支付回调]---start---" + DateUtil.DateToLongStr(new Date()));
        // 获取支付宝POST过来反馈信息
        Map requestParams = getRequest().getParameterMap();
        Map<String, String> params = getRequestParams(requestParams);
        PrintWriter writer = getResponse().getWriter();
        HttpServletRequest request = getRequest();
        String tradeStatus = params.get("trade_status");// 交易状态
        String outTradeNo = params.get("out_trade_no");// 商户订单号
        String buyerAccount = params.get("buyer_email");// 买家支付宝账号
        String totalFee = params.get("total_fee");// 交易金额
        String tradeNo = params.get("trade_no");// 支付宝交易号
        logger.info("tradeStatus====" + tradeStatus);
        logger.info("outTradeNo====" + outTradeNo);
        logger.info("buyerAccount====" + buyerAccount);
        logger.info("buyerAccount====" + buyerAccount);
        logger.info("totalFee====" + totalFee);
        logger.info("tradeNo====" + tradeNo);
        System.out.println("tradeStatus====" + tradeStatus);
        System.out.println("outTradeNo====" + outTradeNo);
        System.out.println("buyerAccount====" + buyerAccount);
        System.out.println("totalFee====" + totalFee);
        System.out.println("tradeNo====" + tradeNo);
        setTradeStatus(tradeStatus);
        return SUCCESS;
    }

    public static Map<String, String> getRequestParams(Map<String, String[]> requestParams) {
        Map<String, String> params = new HashMap<String, String>();
        Iterator<String> it = requestParams.keySet().iterator();
        while (it.hasNext()) {
            String name = (String) it.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr += (i == values.length - 1) ? values[i] : (values[i] + ",");
            }
            // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
            params.put(name, valueStr);
        }
        return params;
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
     * 我的充值
     * 
     * @return
     */
    public String initUserRecharge() {
        WebUser webUser = getWebuser();
        BusAccount userAccount = accountService.selectAccoutByPrimaryKey(webUser.getUserId());
        getRequest().setAttribute("usableMoney", userAccount.getUsableMoney());
        return SUCCESS;
    }

    /**
     * 我的充值
     * 
     * @return
     * @throws IOException
     * @throws WriteException
     */
    public String userRecharge() throws Exception {
        WebUser webUser = getWebuser();
        Map<String, Object> params = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        String searchDate = this.getParameter("searchDate");
        int isExport = this.getParamInt("isExport");// 是否导出excel
        params.put("userId", webUser.getUserId());
        if (!StringUtil.isEmpty(searchDate)) {
            Date startTime = DateUtil.getFirstDayOfMonth(DateUtil.StrToDate(searchDate, "yyyy/MM"));
            Date endTime = DateUtil.getLastDayOfMonth(DateUtil.StrToDate(searchDate, "yyyy/MM"));
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            params.put(Globals.PAGE, page);
            List<BusRecharge> list = accountService.selectBusRechargeByPage(params);
            page.setRoot(list);
            this.page = page;
        } else {
            params.put("maxLimit", MAX_EXPORT);
            List<BusRecharge> exportList = accountService.selectBusRechargeByPage(params);
            String[] headsName = { "充值金额(元)", "充值方式", "充值状态", "交易号", "充值时间" };
            String[] properiesName = { "moneyDesc", "payWayDesc", "statusDesc", "tradeNo", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, BusAppointment.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 我的预约
     * 
     * @return
     */
    public String initUserAppointment() {
        return SUCCESS;
    }

    public String userAppointment() throws Exception {
        WebUser webUser = getWebuser();
        int currentPage = this.getParamInt("currentPage");// 当前页
        String searchDate = this.getParameter("searchDate");
        String userName = this.getParameter("userName");
        int isExport = this.getParamInt("isExport");// 是否导出excel
        Map<String, Object> params = new HashMap<String, Object>();
        if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            List<Integer> userIdlist = new ArrayList<Integer>();
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andGroupIdEqualTo(webUser.getUserId());
            List<BusUser> listUser = userService.selectUserByExample(emp);
            if (listUser != null && listUser.size() > 0) {
                for (BusUser user : listUser) {
                    userIdlist.add(user.getId());
                }
            } else {
                userIdlist.add(0);
            }
            if (!StringUtil.isEmpty(userName)) {
                params.put("userName", userName);
            }
            params.put("isGroup", 1);
            params.put("list", userIdlist);
        } else {
            params.put("userId", webUser.getUserId());
        }

        params.put("notAppStatus", AppointmentStatusEnum.DEL.getValue());
        if (!StringUtil.isEmpty(searchDate)) {
            Date startTime = DateUtil.getFirstDayOfMonth(DateUtil.StrToDate(searchDate, "yyyy/MM"));
            Date endTime = DateUtil.getLastDayOfMonth(DateUtil.StrToDate(searchDate, "yyyy/MM"));
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            params.put(Globals.PAGE, page);
            List<BusAppointment> list = appointmentService.selectUserAppByPage(params);
            page.setRoot(list);
            this.page = page;
        } else {
            params.put("maxLimit", MAX_EXPORT);
            List<BusAppointment> exportList = appointmentService.selectUserAppByPage(params);
            String[] headsName = { "预约金额", "预约时长(分)", "支付状态", "预约状态", "交易号", "预约时间" };
            String[] properiesName = { "appFeeDesc", "appLen", "payStatusDesc", "appStatusDesc", "appNo", "addTimeDesc" };
            if (UserTypeEnum.GROUP == webUser.getUsertype()) {
                headsName = new String[] { "用户名", "预约金额", "预约时长(分)", "支付状态", "预约状态", "交易号", "预约时间" };
                properiesName = new String[] { "userName", "appFeeDesc", "appLen", "payStatusDesc", "appStatusDesc", "appNo", "addTimeDesc" };
            }

            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, BusAppointment.class);
            return null;
        }
        return SUCCESS;
    }

    /**
     * 我的充电
     * 
     * @return
     */
    public String initUserCharge() {
        return SUCCESS;
    }

    public String userCharge() throws Exception {
        WebUser webUser = getWebuser();
        Map<String, Object> params = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        String searchDate = this.getParameter("searchDate");
        String userName = this.getParameter("userName");
        int isExport = this.getParamInt("isExport");// 是否导出excel

        if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            List<Integer> userIdlist = new ArrayList<Integer>();
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andGroupIdEqualTo(webUser.getUserId());
            List<BusUser> listUser = userService.selectUserByExample(emp);
            if (listUser != null && listUser.size() > 0) {
                for (BusUser user : listUser) {
                    userIdlist.add(user.getId());
                }
            } else {
                userIdlist.add(0);
            }
            if (!StringUtil.isEmpty(userName)) {
                params.put("userName", userName);
            }
            params.put("isGroup", 1);
            params.put("list", userIdlist);
        } else {
            params.put("userId", webUser.getUserId());
        }

        if (!StringUtil.isEmpty(searchDate)) {
            Date startTime = DateUtil.getFirstDayOfMonth(DateUtil.StrToDate(searchDate, "yyyy/MM"));
            Date endTime = DateUtil.getLastDayOfMonth(DateUtil.StrToDate(searchDate, "yyyy/MM"));
            params.put("startTime", startTime);
            params.put("endTime", endTime);
        }
        if (IS_EXPORT != isExport) {
            Page page = this.returnPage(currentPage, limit);
            params.put(Globals.PAGE, page);
            List<BusPayment> list = chargingService.selectUserChaByPage(params);
            page.setRoot(list);
            this.page = page;
        } else {
            params.put("maxLimit", MAX_EXPORT);
            // Date now = new Date();
            // Calendar calendar = Calendar.getInstance();
            // calendar.setTime(now);
            // calendar.add(Calendar.MONTH, DateUtil.PRE_THREE_MONTH);// 获取上三个月日期
            // BusPaymentExample emp = new BusPaymentExample();
            // BusPaymentExample.Criteria cr = emp.createCriteria();
            // cr.andUserIdEqualTo(webUser.getUserId());
            // cr.andUpdateTimeGreaterThanOrEqualTo(calendar.getTime());
            // cr.andUpdateTimeLessThanOrEqualTo(now);
            // List<BusPayment> exportList = chargingService.selectChargePaymentByExample(emp);
            List<BusPayment> exportList = chargingService.selectUserChaByPage(params);
            String[] headsName = { "充电金额(元)", "停车金额(元)", "服务金额(元)", "充电时长(分)", "支付状态", "充电状态", "交易号", "充电时间" };
            String[] properiesName = { "chaFeeDesc", "parkFeeDesc", "serviceFeeDesc", "chaLen", "payStatusDesc", "dealStatusDesc", "tradeNo", "updateTimeDesc" };
            if (UserTypeEnum.GROUP == webUser.getUsertype()) {
                headsName = new String[] { "用户名", "充电金额(元)", "停车金额(元)", "服务金额(元)", "充电时长(分)", "支付状态", "充电状态", "交易号", "充电时间" };
                properiesName = new String[] { "userName", "chaFeeDesc", "parkFeeDesc", "serviceFeeDesc", "chaLen", "payStatusDesc", "dealStatusDesc", "tradeNo", "updateTimeDesc" };
            }
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, BusPayment.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 我的账单
     * 
     * @return
     */
    public String initUserBill() {
        List<UserBillChartsModel> list = createUserBillCharts();
        getRequest().setAttribute("userBillCharts", JsonUtil.list2json(list));
        return SUCCESS;
    }

    private List<UserBillChartsModel> createUserBillCharts() {
        List<UserBillChartsModel> list = new ArrayList<UserBillChartsModel>();
        WebUser webUser = getWebuser();
        Map<String, Object> param = new HashMap<String, Object>();
        // charts app start
        Calendar calendar = Calendar.getInstance();
        int year1 = calendar.get(Calendar.YEAR);
        int month1 = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上一个月日期
        int year2 = calendar.get(Calendar.YEAR);
        int month2 = calendar.get(Calendar.MONTH) + 1;
        calendar.add(Calendar.MONTH, DateUtil.PRE_ONE_MONTH);// 获取上一个月日期
        int year3 = calendar.get(Calendar.YEAR);
        int month3 = calendar.get(Calendar.MONTH) + 1;

        if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            List<Integer> userIdlist = new ArrayList<Integer>();
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andGroupIdEqualTo(webUser.getUserId());
            List<BusUser> listUser = userService.selectUserByExample(emp);
            if (listUser != null && listUser.size() > 0) {
                for (BusUser user : listUser) {
                    userIdlist.add(user.getId());
                }
            } else {
                userIdlist.add(0);
            }
            param.put("isGroup", 1);
            param.put("list", userIdlist);
        } else {
            param.put("userId", webUser.getUserId());
        }
        // param.put("userId", webUser.getUserId());

        param.put("payStatus", AppointmentPayStatusEnum.SUCCESS.getValue());
        param.put("year", year1);
        param.put("month", month1);
        BigDecimal appTotal1 = accountService.getTotalAppFeeByMap(param);
        appTotal1 = appTotal1 == null ? BigDecimal.ZERO : appTotal1;
        param.put("year", year2);
        param.put("month", month2);
        BigDecimal appTotal2 = accountService.getTotalAppFeeByMap(param);
        appTotal2 = appTotal2 == null ? BigDecimal.ZERO : appTotal2;
        param.put("year", year3);
        param.put("month", month3);
        BigDecimal appTotal3 = accountService.getTotalAppFeeByMap(param);
        appTotal3 = appTotal3 == null ? BigDecimal.ZERO : appTotal3;
        // app end
        // cha start

        BigDecimal chaOut = BigDecimal.ZERO;
        BigDecimal serviceOut = BigDecimal.ZERO;
        BigDecimal parkOut = BigDecimal.ZERO;
        BigDecimal totalChaOut = BigDecimal.ZERO;
        BigDecimal chaOut2 = BigDecimal.ZERO;
        BigDecimal serviceOut2 = BigDecimal.ZERO;
        BigDecimal parkOut2 = BigDecimal.ZERO;
        BigDecimal totalChaOut2 = BigDecimal.ZERO;
        BigDecimal chaOut3 = BigDecimal.ZERO;
        BigDecimal serviceOut3 = BigDecimal.ZERO;
        BigDecimal parkOut3 = BigDecimal.ZERO;
        BigDecimal totalChaOut3 = BigDecimal.ZERO;
        param.put("payStatus", ChargePayStatusEnum.SUCCESS.getValue());
        param.put("year", year1);
        param.put("month", month1);
        List<ChargeModel> chargeList = accountService.getTotalChaFeeByMap(param);
        if (chargeList != null && chargeList.size() > 0) {
            ChargeModel model = chargeList.get(0);
            if (model != null) {
                chaOut = model.getTotalChaFeeOut() == null ? chaOut : model.getTotalChaFeeOut();
                serviceOut = model.getTotalServiceFeeOut() == null ? serviceOut : model.getTotalServiceFeeOut();
                parkOut = model.getTotalParkFeeOut() == null ? parkOut : model.getTotalParkFeeOut();
            }
        }

        param.put("year", year2);
        param.put("month", month2);
        chargeList = accountService.getTotalChaFeeByMap(param);
        if (chargeList != null && chargeList.size() > 0) {
            ChargeModel model = chargeList.get(0);
            if (model != null) {
                chaOut2 = model.getTotalChaFeeOut() == null ? chaOut2 : model.getTotalChaFeeOut();
                serviceOut2 = model.getTotalServiceFeeOut() == null ? serviceOut2 : model.getTotalServiceFeeOut();
                parkOut2 = model.getTotalParkFeeOut() == null ? parkOut2 : model.getTotalParkFeeOut();
            }
        }

        param.put("year", year3);
        param.put("month", month3);
        chargeList = accountService.getTotalChaFeeByMap(param);
        if (chargeList != null && chargeList.size() > 0) {
            ChargeModel model = chargeList.get(0);
            if (model != null) {
                chaOut3 = model.getTotalChaFeeOut() == null ? chaOut3 : model.getTotalChaFeeOut();
                serviceOut3 = model.getTotalServiceFeeOut() == null ? serviceOut3 : model.getTotalServiceFeeOut();
                parkOut3 = model.getTotalParkFeeOut() == null ? parkOut3 : model.getTotalParkFeeOut();
            }
        }
        totalChaOut = NumberUtil.add(chaOut, serviceOut);
        totalChaOut = NumberUtil.add(totalChaOut, parkOut);

        totalChaOut2 = NumberUtil.add(chaOut2, serviceOut2);
        totalChaOut2 = NumberUtil.add(totalChaOut2, parkOut2);

        totalChaOut3 = NumberUtil.add(chaOut3, serviceOut3);
        totalChaOut3 = NumberUtil.add(totalChaOut3, parkOut3);

        UserBillChartsModel model1 = new UserBillChartsModel();
        Calendar calendar2 = Calendar.getInstance();
        calendar2.set(Calendar.YEAR, year1);
        calendar2.set(Calendar.MONTH, month1 - 1);
        String dateStr = DateUtil.DateToYYYYMMStr(calendar2.getTime());
        model1.setName(dateStr);
        model1.setAppFee(appTotal1.toString());
        model1.setChaFee(totalChaOut.toString());
        calendar2.set(Calendar.YEAR, year2);
        calendar2.set(Calendar.MONTH, month2 - 1);
        dateStr = DateUtil.DateToYYYYMMStr(calendar2.getTime());
        UserBillChartsModel model2 = new UserBillChartsModel();
        model2.setName(dateStr);
        model2.setAppFee(appTotal2.toString());
        model2.setChaFee(totalChaOut2.toString());
        calendar2.set(Calendar.YEAR, year3);
        calendar2.set(Calendar.MONTH, month3 - 1);
        dateStr = DateUtil.DateToYYYYMMStr(calendar2.getTime());
        UserBillChartsModel model3 = new UserBillChartsModel();
        model3.setName(dateStr);
        model3.setAppFee(appTotal3.toString());
        model3.setChaFee(totalChaOut3.toString());
        // cha end
        list.add(model1);
        list.add(model2);
        list.add(model3);

        return list;

    }

    public String userBill() throws Exception {
        WebUser webUser = getWebuser();
        Map<String, Object> params = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        String searchDate = this.getParameter("searchDate");
        int isBill = this.getParamInt("isBill");
        int isExport = this.getParamInt("isExport");// 是否导出excel
        if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            List<Integer> userIdlist = new ArrayList<Integer>();
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andGroupIdEqualTo(webUser.getUserId());
            List<BusUser> listUser = userService.selectUserByExample(emp);
            if (listUser != null && listUser.size() > 0) {
                for (BusUser user : listUser) {
                    userIdlist.add(user.getId());
                }
            } else {
                userIdlist.add(0);
            }
            params.put("list", userIdlist);
        } else {
            params.put("userId", webUser.getUserId());
        }

        params.put("isBill", isBill);
        if (!StringUtil.isEmpty(searchDate)) {
            Date cycleTime = DateUtil.StrToDate(searchDate, "yyyy/MM");
            String cycleDesc = DateUtil.DateToYYYYMMStr(cycleTime);// 结账周期描述
            params.put("cycleDesc", cycleDesc);
        }
        if (IS_EXPORT != isExport) {
            List<BusBills> list = null;
            Page page = this.returnPage(currentPage, limit);
            params.put(Globals.PAGE, page);
            if (UserTypeEnum.GROUP == webUser.getUsertype()) {
                list = accountService.selectGroupBillsByPage(params);
            } else {
                list = accountService.selectBusBillsByPage(params);
            }

            page.setRoot(list);
            this.page = page;
        } else {
            // BusBillsExample emp = new BusBillsExample();
            // BusBillsExample.Criteria cr = emp.createCriteria();
            // cr.andUserIdEqualTo(webUser.getUserId());
            // List<BusBills> exportList = accountService.selectBusBillsByExample(emp);
            List<BusBills> exportList = null;
            if (UserTypeEnum.GROUP == webUser.getUsertype()) {
                exportList = accountService.selectGroupBillsByPage(params);
            } else {
                exportList = accountService.selectBusBillsByPage(params);
            }
            String[] headsName = { "预约金额(元)", "充电金额(元)", "停车金额(元)", "服务金额(元)", "消费总金额(元)", "结账周期", "开票状态" };
            String[] properiesName = { "appFeeOutDesc", "chaFeeOutDesc", "parkFeeOutDesc", "serviceFeeOutDesc", "totalFeeOutDesc", "checkCycle", "receiptIdDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, BusBills.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 开票
     * 
     * @return
     */
    public String doReceipt() {
        WebUser webUser = getWebuser();
        String jsonObj = getParameter("jsonObj");
        String billsIds = getParameter("billsId");

        List<BusBills> billsList = null;
        if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            List<Integer> userIdlist = new ArrayList<Integer>();
            List<String> cyclelist = new ArrayList<String>();
            JSONObject jsonObjBean = JSONObject.fromObject(jsonObj);
            String checkCycle = jsonObjBean.getString("time");
            if (StringUtil.isEmpty(checkCycle)) {
                message = "提交的开票信息有误！！";
                return SUCCESS;
            }
            String[] checkCycles = checkCycle.split(",");
            for (String cycle : checkCycles) {
                cyclelist.add(cycle);
            }

            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andGroupIdEqualTo(webUser.getUserId());
            List<BusUser> listUser = userService.selectUserByExample(emp);
            if (listUser != null && listUser.size() > 0) {
                for (BusUser user : listUser) {
                    userIdlist.add(user.getId());
                }
            }
            if (userIdlist.size() > 0 && cyclelist.size() > 0) {
                BusBillsExample billsEmp = new BusBillsExample();
                BusBillsExample.Criteria billsCr = billsEmp.createCriteria();
                billsCr.andUserIdIn(userIdlist);
                billsCr.andCheckCycleIn(cyclelist);
                billsList = accountService.selectBusBillsByExample(billsEmp);

            } else {
                message = "提交的开票信息有误！！";
                return SUCCESS;
            }

        } else {
            if (StringUtil.isEmpty(billsIds)) {
                message = "提交的开票信息有误！！";
                return SUCCESS;
            }
            List<Integer> billIdList = new ArrayList<Integer>();
            String[] bId = billsIds.split(",");
            for (String id : bId) {
                billIdList.add(Integer.valueOf(id));
            }
            if (billIdList.size() > 0) {
                BusBillsExample emp = new BusBillsExample();
                BusBillsExample.Criteria cr = emp.createCriteria();
                cr.andIdIn(billIdList);
                cr.andUserIdEqualTo(webUser.getUserId());
                billsList = accountService.selectBusBillsByExample(emp);

            } else {
                message = "提交的开票信息有误！！";
                return SUCCESS;
            }
        }

        if (billsList == null || billsList.size() <= 0) {
            message = "账单不存在！！";
            return SUCCESS;
        }
        for (BusBills b : billsList) {
            if (b.getReceiptId() != null && b.getReceiptId() > 0) {
                message = b.getCheckCycle() + "账单已经开过票了！！";
                return SUCCESS;
            }
        }
        message = checkReceipt(jsonObj);
        if ("success".equals(message)) {
            BusUserReceipt userReceipt = JsonToBean(jsonObj, BusUserReceipt.class);
            userReceipt.setUserId(webUser.getUserId());
            userReceipt.setAddTime(new Date());
            int count = accountService.insertAndUpdateUserReceipt(userReceipt, billsList);
            if (count <= 0) {
                message = "提交失败！！";
            }
        }

        return SUCCESS;
    }

    private String checkReceipt(String jsonObj) {
        String msg = "success";
        if (StringUtil.isEmpty(jsonObj)) {
            msg = "提交失败！！";
        } else {
            try {
                BusUserReceipt userReceipt = JsonToBean(jsonObj, BusUserReceipt.class);
                if (StringUtil.isEmpty(userReceipt.getTime())) {
                    msg = "开票周期不存在！！";
                } else if (userReceipt.getMoney() == null || userReceipt.getMoney().doubleValue() <= 0) {
                    msg = "开票金额必须大于0元！！";
                } else if (StringUtil.isEmpty(userReceipt.getBillHead())) {
                    msg = "请填写发票抬头！！";
                } else if (StringUtil.isEmpty(userReceipt.getRecipient())) {
                    msg = "请填写收件人姓名！！";
                } else if (StringUtil.isEmpty(userReceipt.getPhone()) || !Validator.isMobile(userReceipt.getPhone())) {
                    msg = "请正确填写11位手机号码！！";
                } else if (StringUtil.isEmpty(userReceipt.getAddress())) {
                    msg = "请填写收件地址！！";
                }
            } catch (Exception e) {
                msg = "提交失败！！";
                e.printStackTrace();
            }
        }
        return msg;
    }

    /**
     * 我的发票
     * 
     * @return
     */
    public String initUserInvoice() {
        return SUCCESS;
    }

    public String userInvoice() throws Exception {
        WebUser webUser = getWebuser();
        Map<String, Object> params = new HashMap<String, Object>();
        int currentPage = this.getParamInt("currentPage");// 当前页
        String searchDate = getParameter("searchDate");
        int isExport = this.getParamInt("isExport");// 是否导出excel
        params.put("userId", webUser.getUserId());
        if (!StringUtil.isEmpty(searchDate)) {
            Date cycleTime = DateUtil.StrToDate(searchDate, "yyyy/MM");
            String cycleDesc = DateUtil.DateToYYYYMMStr(cycleTime);// 结账周期描述
            params.put("searchDate", cycleDesc);
        }
        if (IS_EXPORT != isExport) {
            Page page = returnPage(currentPage, limit);
            params.put(Globals.PAGE, page);
            List<BusUserReceipt> list = accountService.selectBusUserReceiptByPage(params);
            page.setRoot(list);
            this.page = page;
        } else {
            // Date now = new Date();
            // Calendar calendar = Calendar.getInstance();
            // calendar.setTime(now);
            // calendar.add(Calendar.MONTH, DateUtil.PRE_THREE_MONTH);// 获取上三个月日期
            // BusUserReceiptExample emp = new BusUserReceiptExample();
            // BusUserReceiptExample.Criteria cr = emp.createCriteria();
            // cr.andUserIdEqualTo(webUser.getUserId());
            // cr.andAddTimeGreaterThanOrEqualTo(calendar.getTime());
            // cr.andAddTimeLessThanOrEqualTo(now);
            // List<BusUserReceipt> exportList = accountService.selectBusUserReceiptByExample(emp);
            List<BusUserReceipt> exportList = accountService.selectBusUserReceiptByPage(params);
            String[] headsName = { "发票类型", "开票月份", "开票金额(元)", "开票抬头", "收件人", "联系电话", "收件地址", "快递单号", "开票状态", "审核备注", "开票时间" };
            String[] properiesName = { "billTypeDesc", "time", "moneyDesc", "billHead", "recipient", "phone", "address", "expressNum", "statusDesc", "validRemark", "addTimeDesc" };
            JxlXlsUtil jxl = new JxlXlsUtil(this.getRequest(), this.getResponse());
            jxl.exportXLS(exportList, properiesName, headsName, BusUserReceipt.class);
            return null;
        }

        return SUCCESS;
    }

    /**
     * 我的钱包
     * 
     * @return
     */
    public String initUserAccount() {
        WebUser webUser = getWebuser();
        BusAccount userAccount = accountService.selectAccoutByPrimaryKey(webUser.getUserId());
        getRequest().setAttribute("usableMoney", userAccount.getUsableMoney());
        Map<String, Object> param = new HashMap<String, Object>();
        BigDecimal appOut = BigDecimal.ZERO;
        BigDecimal chaOut = BigDecimal.ZERO;
        BigDecimal serviceOut = BigDecimal.ZERO;
        BigDecimal parkOut = BigDecimal.ZERO;
        BigDecimal totalOut = BigDecimal.ZERO;
        BigDecimal totalChaOut = BigDecimal.ZERO;
        if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            List<Integer> userIdlist = new ArrayList<Integer>();
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andGroupIdEqualTo(webUser.getUserId());
            List<BusUser> listUser = userService.selectUserByExample(emp);
            if (listUser != null && listUser.size() > 0) {
                for (BusUser user : listUser) {
                    userIdlist.add(user.getId());
                }
            } else {
                userIdlist.add(0);
            }
            param.put("isGroup", 1);
            param.put("list", userIdlist);
        } else {
            param.put("userId", webUser.getUserId());
        }
        // param.put("userId", webUser.getUserId());

        param.put("payStatus", AppointmentPayStatusEnum.SUCCESS.getValue());
        BigDecimal appTotal = accountService.getTotalAppFeeByMap(param);
        param.put("payStatus", ChargePayStatusEnum.SUCCESS.getValue());
        List<ChargeModel> list = accountService.getTotalChaFeeByMap(param);
        if (appTotal != null) {
            appOut = appTotal;
        }
        if (list != null && list.size() > 0) {
            ChargeModel model = list.get(0);
            if (model != null) {
                chaOut = model.getTotalChaFeeOut() == null ? chaOut : model.getTotalChaFeeOut();
                serviceOut = model.getTotalServiceFeeOut() == null ? serviceOut : model.getTotalServiceFeeOut();
                parkOut = model.getTotalParkFeeOut() == null ? parkOut : model.getTotalParkFeeOut();
            }
        }
        totalChaOut = NumberUtil.add(chaOut, serviceOut);
        totalChaOut = NumberUtil.add(totalChaOut, parkOut);
        totalOut = NumberUtil.add(totalChaOut, appOut);

        getRequest().setAttribute("totalOut", totalOut);// 总支出
        getRequest().setAttribute("totalChaOut", totalChaOut);// 总充电支出
        getRequest().setAttribute("totalAppOut", appOut);// 总预约支出
        if (RoleTypeEnum.PERSON.getShortValue() == webUser.getRoleType()) {
            Map<String, Object> param2 = new HashMap<String, Object>();
            param2.put("userid", webUser.getUserId());
            BigDecimal totalIn = accountService.selectUserBillTotalIn(param2);// 历史总收入
            getRequest().setAttribute("totalIn", NumberUtil.formateScale2(totalIn));// 历史总收入
        }

        return SUCCESS;
    }

    public String userDeal() throws Exception {
        WebUser webUser = getWebuser();
        int currentPage = getParamInt("currentPage");
        int type = getParamInt("dealType");
        String searchDate = getParameter("searchDate");
        Map<String, Object> params = new HashMap<String, Object>();
        if (UserTypeEnum.GROUP == webUser.getUsertype()) {
            List<Integer> userIdlist = new ArrayList<Integer>();
            BusUserExample emp = new BusUserExample();
            BusUserExample.Criteria cr = emp.createCriteria();
            cr.andGroupIdEqualTo(webUser.getUserId());
            List<BusUser> listUser = userService.selectUserByExample(emp);
            if (listUser != null && listUser.size() > 0) {
                for (BusUser user : listUser) {
                    userIdlist.add(user.getId());
                }
            }
            userIdlist.add(webUser.getUserId());
            params.put("isGroup", 1);
            params.put("list", userIdlist);
        } else {
            params.put("userid", webUser.getUserId());
        }

        if (!StringUtil.isEmpty(searchDate)) {
            SimpleDateFormat formater = new java.text.SimpleDateFormat("yyyy/MM");
            Date date = formater.parse(searchDate);
            Date startdate = DateUtil.getFirstDayOfMonth(date);
            Date enddate = DateUtil.getLastDayOfMonth(date);
            params.put("startdate", startdate);
            params.put("enddate", enddate);
        }

        Page page = this.returnPage(currentPage, limit);
        params.put(Globals.PAGE, page);
        if (type > 0) {
            params.put("type", type);
        }
        params.put("page", page);
        List<BillInfo> billlist = accountService.selectAccountBillByPage(params);
        int operatetype;
        for (BillInfo record : billlist) {
            operatetype = record.getType().intValue();
            record.setTypedesc(FundOperateTypeEnum.getText(operatetype));
            if (FundOperateTypeEnum.RECHARGE.getValue() == operatetype && webUser.getUsertype() == UserTypeEnum.PERSON) {// 充值
                record.setName("钱包充值");
            } else if (FundOperateTypeEnum.RECHARGE.getValue() == operatetype && webUser.getUsertype() == UserTypeEnum.GROUP) {
                record.setName("账户充值");
            } else if (FundOperateTypeEnum.CHARGING.getValue() == operatetype || FundOperateTypeEnum.APPOINTMENT.getValue() == operatetype) {// 充电
                record.setName(CacheChargeHolder.getStationPileNameById(record.getId()));
            } else if (FundOperateTypeEnum.CASH.getValue() == operatetype && webUser.getUsertype() == UserTypeEnum.PERSON) {// 提现
                record.setName("钱包提现");
            } else if (FundOperateTypeEnum.CASH.getValue() == operatetype && webUser.getUsertype() == UserTypeEnum.GROUP) {// 提现
                record.setName("账户提现");
            }
            record.setId(null);// 里面有userid，所以要清除，以免暴露
            record.setFee(NumberUtil.formateScale2(record.getFee()));
        }
        page.setRoot(billlist);
        this.page = page;
        return SUCCESS;
    }

    // get set
    public String getMessage() {
        return this.message;
    }

    public void setRechargeService(RechargeService rechargeService) {
        this.rechargeService = rechargeService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public Page getPage() {
        return page;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

}
