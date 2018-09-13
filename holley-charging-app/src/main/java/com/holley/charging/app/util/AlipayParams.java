package com.holley.charging.app.util;

public class AlipayParams {

    // 基本参数
    // private String service; // 必填,接口名称
    private String partner;      // 必填,合作者身份ID,String(16)
    // private String input_charset;// 必填,参数编码字符集
    // private String sign_type; // 必填,签名方式
    // private String sign; // 必填,签名
    private String notify_url;   // 必填,服务器异步通知页面路径,String(200)
    // private String app_id; // 客户端号
    // private String appenv; // 客户端来源

    // 业务参数
    private String out_trade_no; // 必填,商户网站唯一订单号,String(64)
    private String subject;      // 必填,商品名称,String(128)
    // private String payment_type; // 必填,支付类型,String(4)
    private String seller_id;    // 必填,卖家支付宝账号,String(16)
    private Number total_fee;    // 必填,总金额,单位为RMB-Yuan。取值范围为[0.01，100000000.00]，精确到小数点后两位。
    private String body;         // 必填,商品详情,String(512)
    // private String it_b_pay; // 未付款交易的超时时间,取值范围：1m～15d，或者使用绝对时间（示例格式：2014-06-13
    // // 16:00:00）。m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
    // private String extern_token; // 授权令牌,String(32)

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public Number getTotal_fee() {
        return total_fee;
    }

    public void setTotal_fee(Number total_fee) {
        this.total_fee = total_fee;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
