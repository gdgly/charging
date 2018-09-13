package com.holley.charging.bjbms.pay.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.annotation.Resource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeWapPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.holley.charging.action.BaseAction;
import com.holley.charging.bjbms.pay.util.AlipayConfig;
import com.holley.charging.bjbms.pay.util.HttpXmlUtils;
import com.holley.charging.bjbms.pay.util.JdomParseXmlUtils;
import com.holley.charging.bjbms.pay.util.Unifiedorder;
import com.holley.charging.bjbms.pay.util.UnifiedorderResult;
import com.holley.charging.bjbms.pay.util.WechatConfig;
import com.holley.charging.bjbms.pay.util.WechatPayResult;
import com.holley.charging.bjbms.pay.util.WechatSignUtils;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusPaymentExample;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.common.constants.charge.RechargeStatusEnum;
import com.holley.common.util.DateUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.RoleUtil;

public class PayAction extends BaseAction {

    private final static Logger logger = Logger.getLogger(PayAction.class);
    String                      wechatPayUrl;
    @Resource
    private ChargingService     chargingService;

    /**
     * 支付宝异步返回
     * 
     * @return
     */
    public String alipayNotify() {
        PrintWriter writer = null;
        // 支付宝交易号
        // String trade_no = getRequest().getParameter("trade_no");
        // 商户订单号
        String out_trade_no = getParameter("out_trade_no");
        // 交易状态
        String trade_status = getParameter("trade_status");
        BusPayment payment = returnPayment(out_trade_no);
        // 计算得出通知验证结果
        try {
            writer = getResponse().getWriter();
            if (payment != null) {
                if (alipayResult()) {// 验证成功
                    System.out.println("alipayNotify:" + "验证成功");
                    // 请在这里加上商户的业务逻辑程序代码
                    // ——请根据您的业务逻辑来编写程序（以下代码仅作参考）——
                    if (trade_status.equals("TRADE_FINISHED")) {
                        // 判断该笔订单是否在商户网站中已经做过处理
                        // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        // 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        // 如果有做过处理，不执行商户的业务程序

                        // 注意：
                        // 如果签约的是可退款协议，退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                        // 如果没有签约可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                    } else if (trade_status.equals("TRADE_SUCCESS")) {
                        // 判断该笔订单是否在商户网站中已经做过处理
                        // 如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        // 请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        // 如果有做过处理，不执行商户的业务程序

                        // 注意：
                        // 如果签约的是可退款协议，那么付款完成后，支付宝系统发送该交易状态通知。
                    }

                    // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                    writer.println("success"); // 请不要修改或删除
                    // ////////////////////////////////////////////////////////////////////////////////////////
                } else {// 验证失败
                    System.out.println("alipayNotify:" + "验证失败");
                    writer.println("fail");
                }
            } else {
                logger.info("未知订单：" + out_trade_no);
                // writer.println("success");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                writer.flush();
                writer.close();
            }
        }

        return SUCCESS;
    }

    /**
     * 支付宝同步返回
     * 
     * @return
     * @throws AlipayApiException
     */
    public String alipayReturn() {

        // 商户订单号
        String out_trade_no = getParameter("out_trade_no");
        // 支付宝交易号
        // String trade_no = getRequest().getParameter("trade_no");
        BusPayment payment = returnPayment(out_trade_no);
        try {
            if (payment != null) {
                if (alipayResult()) {// 验证成功
                    logger.info("alipayReturn:" + "验证成功");
                    // 请在这里加上商户的业务逻辑程序代码
                    // 该页面可做页面美工编辑
                    // ——请根据您的业务逻辑来编写程序（以上代码仅作参考）——
                    message("支付成功");
                } else {
                    // 该页面可做页面美工编辑
                    logger.info("alipayReturn:" + "验证失败");
                    message("支付失败");
                }
            } else {
                message("订单不存在");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return SUCCESS;
    }

    public String payInit() throws IOException {
        getRequest().setAttribute("total_amount", "0.01");
        getRequest().setAttribute("out_trade_no", System.currentTimeMillis());
        return SUCCESS;
    }

    public void alipay() {
        try {
            // 调用SDK生成表单
            String form = wrapAlipayFormData();
            getResponse().setContentType("text/html;charset=" + AlipayConfig.CHARSET);
            getResponse().getWriter().write(form);// 直接将完整的表单html输出到页面
            getResponse().getWriter().flush();
            getResponse().getWriter().close();
        } catch (Exception e) {
            logger.info("生成表单出错");
            e.printStackTrace();
        }
    }

    public String wechatPay() throws Exception {

        // WebUser webUser = getWebuser();
        // String money = getParameter("rechargeMoney");
        // String rechargeWay = getParameter("rechargeWay");
        // String out_trade_no = getParameter("out_trade_no");
        // String msg = checkRecharge(rechargeWay, money);
        // BusRecharge record = new BusRecharge();
        // record.setAccountInfo(null);
        // record.setAddTime(new Date());
        // record.setFee(null);
        // record.setMoney(new BigDecimal(money));
        // record.setPayWay(Short.valueOf(rechargeWay));
        // record.setStatus(RechargeStatusEnum.RECHARGING.getShortValue());
        // record.setTradeNo(out_trade_no);
        // record.setUserId(webUser.getUserId());
        // if (rechargeService.insertRechargeSelective(record) > 0) {
        BigDecimal tempmoney = new BigDecimal("0.01").multiply(new BigDecimal("100")).setScale(0);
        SortedMap<String, Object> params = new TreeMap<String, Object>();
        params.put("appid", WechatConfig.appid);
        params.put("mch_id", WechatConfig.mch_id);
        params.put("nonce_str", StringUtil.randomString(16));
        params.put("time_start", DateUtil.DateToLong14Str(new Date()));
        params.put("trade_type", "MWEB");// H5支付
        params.put("spbill_create_ip", getRemoteIP());
        params.put("body", "充电支付");
        params.put("detail", "充电支付");
        params.put("out_trade_no", DateUtil.DateToLong14Str(new Date()) + StringUtil.zeroPadString(1 + "", 6));
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
        if ("SUCCESS".equals(result.getReturn_code()) && "SUCCESS".equals(result.getResult_code()) && StringUtil.isNotEmpty(result.getMweb_url())) {
            wechatPayUrl = result.getMweb_url();
        }
        // MatrixToImageWriter.createCodeStream(result.getCode_url(), getResponse());
        return SUCCESS;
        // } else {
        // getRequest().setAttribute("msg", "充值失败请重新操作！！");
        // return "userMsg";
        // }

    }

    public String blank() {
        return SUCCESS;
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
                            // rechargeService.updatePaystatusAndAccount(wxPayResult.getOut_trade_no(), operateMoney,
                            // accountInfo, payStatus);
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

    private String wrapAlipayFormData() throws AlipayApiException {
        String out_trade_no = getParameter("out_trade_no");
        // 订单名称，必填
        String subject = "手机网站支付测试商品";
        // 付款金额，必填
        String total_amount = getParameter("total_amount");
        // 商品描述，可空
        String body = "购买测试商品0.01元";
        // 超时时间 可空
        // String timeout_express = "2m";
        // 销售产品码 必填
        String product_code = "QUICK_WAP_PAY";
        // SDK 公共请求类，包含公共请求参数，以及封装了签名与验签，开发者无需关注签名与验签
        // 调用RSA签名方式
        AlipayClient client = new DefaultAlipayClient(AlipayConfig.URL, AlipayConfig.APPID, AlipayConfig.RSA_PRIVATE_KEY, AlipayConfig.FORMAT, AlipayConfig.CHARSET,
                                                      AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.SIGNTYPE);
        AlipayTradeWapPayRequest alipay_request = new AlipayTradeWapPayRequest();

        // 封装请求支付信息
        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setOutTradeNo(out_trade_no);
        model.setSubject(subject);
        model.setTotalAmount(total_amount);
        model.setBody(body);
        // model.setTimeoutExpress(timeout_express);
        model.setProductCode(product_code);
        alipay_request.setBizModel(model);
        // 设置异步通知地址
        alipay_request.setNotifyUrl(RoleUtil.selectRuleByPrimaryKey(RoleUtil.WEB_URL) + getRequest().getContextPath() + AlipayConfig.notify_url);
        // 设置同步地址
        alipay_request.setReturnUrl(RoleUtil.selectRuleByPrimaryKey(RoleUtil.WEB_URL) + getRequest().getContextPath() + AlipayConfig.return_url);

        // form表单生产
        String form = client.pageExecute(alipay_request).getBody();
        return form;
    }

    private BusPayment returnPayment(String out_trade_no) {
        String tradeNo = out_trade_no != null ? out_trade_no : "";
        BusPaymentExample emp = new BusPaymentExample();
        BusPaymentExample.Criteria cr = emp.createCriteria();
        cr.andTradeNoEqualTo(tradeNo);
        List<BusPayment> list = chargingService.selectChargePaymentByExample(emp);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    /**
     * 解析支付返回结果
     * 
     * @return
     */
    private boolean alipayResult() {
        boolean rs = false;
        try {
            Map<String, String> params = new HashMap<String, String>();
            Map requestParams = getRequest().getParameterMap();
            for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
                String name = (String) iter.next();
                String[] values = (String[]) requestParams.get(name);
                String valueStr = "";
                for (int i = 0; i < values.length; i++) {
                    valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
                }
                // 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
                // valueStr = new String(valueStr.getBytes("ISO-8859-1"), "gbk");
                params.put(name, valueStr);
            }
            if (params.containsKey("sign")) {
                String sign = params.get("sign");
                if (sign.contains(" ")) {
                    sign = sign.replace(" ", "+");
                    params.put("sign", sign);
                }
            }
            rs = AlipaySignature.rsaCheckV1(params, AlipayConfig.ALIPAY_PUBLIC_KEY, AlipayConfig.CHARSET, "RSA2");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rs;
    }

    public String getWechatPayUrl() {
        return wechatPayUrl;
    }

}
