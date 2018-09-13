package com.holley.charging.pay.util;

import java.util.HashMap;
import java.util.Map;

import com.holley.common.cache.charging.ChargingCacheUtil;

/* *
 * 类名：AlipayConfig功能：基础配置类详细：设置帐户有关信息及返回路径版本：3.4修改日期：2016-03-08说明：
 * 以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

public class AlipayConfig {

    // ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 合作身份者ID，签约账号，以2088开头由16位纯数字组成的字符串，查看地址：https://b.alipay.com/order/pidAndKey.htm
    public static String partner           = "2088221931544322";

    // 收款支付宝账号，以2088开头由16位纯数字组成的字符串，一般情况下收款账号就是签约账号
    public static String seller_id         = partner;

    // 商户的私钥,需要PKCS8格式，RSA公私钥生成：https://doc.open.alipay.com/doc2/detail.htm?spm=a219a.7629140.0.0.nBDxfy&treeId=58&articleId=103242&docType=1
    public static String private_key       = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAOdoP82Ne2Zbas1iQrCGFmxZynwkOjqlOpozNPLmljtLq+F7FiRb91H5sXe6l1z/4/o6wSnexfvcfJBSBkSAECfelT/UDMXhc7P4rFWJulwhpXt9G/Awc9FHtwZW8n7JDW6BkI9u2+lR9vwLQqAhJDwDXS5qYiUUUcdOxBRnrFIhAgMBAAECgYAjsNSGY5fwxZDHdKxMhaHXvFGarAu/F9TeC5/TBvWM17L+5/kWSojuv9A7YzANS/phkcZRKubmXa9oov4MeqQC9w6XhkA2y032eN6iJPpWwaOy0Yr+Y6b4VWW0cmKmPMV2dhTywRieQeGQPaRALqT38zLTAp+DPpp6u6X/jLan8QJBAP5R9xSeKFJ+woeAkKT3mgIw5aZKdN0h6xp9/EwNRxFZVdxmDt0AZFJGm7ytr2gDSlPXNVluY8v7wLqew2OW9jcCQQDo74pFd2cRa20QmTRXBYaOOM2gkuBcmwrXD6jjTsM7AOfTyBzvLRZPN/iXAMjwG9AmLv8boG8r006nk/Z7Kc5nAkEAwrj5aLTsBLX/tgZPObBHIDRWMavCrOnifCdIfOaRqovvPNB7wuUg6wobBi0qJ4aVa3pU48Os76sz1u/mhKM6owJAfAIasMTSgDmaxL1rWfC9I7Yl8ph/DY4VBU128hyaXy0qPltI3CQ6vk3j8DAgyYNlGw7mDYUp6kECnMBu9j7ZfQJATQmJ0lNfBwN/D/4J9ErMhvptJGqtw035whWy/IDSJROWlX5Sr96e5BVcbRBvT5CKwbTn4XcBA7iH6AdIfhXweA==";
    // public static String private_key =
    // "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    // 支付宝的公钥,查看地址：https://b.alipay.com/order/pidAndKey.htm
    // public static String alipay_public_key =
    // "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    public static String alipay_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    // 服务器异步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url        = "/userAccount/notifyAliPay.action";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url        = "/userAccount/returnAliPay.action";

    // 签名方式
    public static String sign_type         = "RSA";

    // 调试用，创建TXT日志文件夹路径，见AlipayCore.java类中的logResult(String sWord)打印方法。
    public static String log_path          = "C:\\";

    // 字符编码格式 目前支持 gbk 或 utf-8
    public static String input_charset     = "utf-8";

    // 支付类型 ，无需修改
    public static String payment_type      = "1";

    // 调用的接口名，无需修改
    public static String service           = "create_direct_pay_by_user";
    // 商品名称
    public static String subject           = "网页充值";
    // 商品描述
    public static String body              = "线上交易产品";
    public static String show_url          = "/userAdmin/home.action";

    // ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    // ↓↓↓↓↓↓↓↓↓↓ 请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 防钓鱼时间戳 若要使用请调用类文件submit中的query_timestamp函数
    public static String anti_phishing_key = "";

    // 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
    public static String exter_invoke_ip   = "";

    // ↑↑↑↑↑↑↑↑↑↑请在这里配置防钓鱼信息，如果没开通防钓鱼功能，为空即可 ↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
    public static Map<String, String> returnParam(String total_fee, String noticeUrl) {
        Map<String, String> sParaTemp = new HashMap<String, String>();
        sParaTemp.put("service", AlipayConfig.service);
        sParaTemp.put("partner", AlipayConfig.partner);
        sParaTemp.put("seller_id", AlipayConfig.seller_id);
        sParaTemp.put("_input_charset", AlipayConfig.input_charset);
        sParaTemp.put("payment_type", AlipayConfig.payment_type);
        sParaTemp.put("notify_url", noticeUrl + AlipayConfig.notify_url);
        sParaTemp.put("return_url", noticeUrl + AlipayConfig.return_url);
        // sParaTemp.put("anti_phishing_key", AlipayConfig.anti_phishing_key);// 防钓鱼时间戳
        // 若要使用请调用类文件submit中的query_timestamp函数
        // sParaTemp.put("exter_invoke_ip", AlipayConfig.exter_invoke_ip); // 客户端的IP地址 非局域网的外网IP地址，如：221.0.0.1
        sParaTemp.put("out_trade_no", ChargingCacheUtil.getRechargeNo());// 商户订单号
        sParaTemp.put("subject", subject);// 商品名称
        sParaTemp.put("total_fee", total_fee);// 付款金额
        sParaTemp.put("body", body);// 商品描述
        sParaTemp.put("show_url", noticeUrl + show_url + "?tradeStatus=" + "return");// 连接
        // sParaTemp.put("qr_pay_mode", "1");// 支付显示方式
        return sParaTemp;
    }

    /**
     * 生成订单号
     * 
     * @return
     */
    private static String createTradeNo() {
        return String.valueOf(System.currentTimeMillis());
    }
}
