package com.holley.charging.app.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import java.util.SortedMap;

import org.apache.commons.beanutils.BeanUtils;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

/**
 * 微信解析xml：带有CDATA格式的
 * 
 * @author iYjrg_xiebin
 * @date 2015年11月26日上午10:18:07
 */
public class JdomParseXmlUtils {

    /**
     * 1、统一下单获取微信返回 解析的时候自动去掉CDMA
     * 
     * @param xml
     */
    @SuppressWarnings("unchecked")
    public static UnifiedorderResult getUnifiedorderResult(String xml) {
        UnifiedorderResult unifieorderResult = new UnifiedorderResult();
        try {
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document
            Document doc;
            doc = (Document) sb.build(source);

            Element root = doc.getRootElement();// 指向根节点
            List<Element> list = root.getChildren();

            if (list != null && list.size() > 0) {
                String name = "";
                String value = "";
                for (Element element : list) {
                    /*
                     * <xml> <return_code><![CDATA[SUCCESS]]></return_code> <return_msg><![CDATA[OK]]></return_msg>
                     * <appid><![CDATA[wx2421b1c4370ec43b]]></appid> <mch_id><![CDATA[10000100]]></mch_id>
                     * <nonce_str><![CDATA[IITRi8Iabbblz1Jc]]></nonce_str>
                     * <sign><![CDATA[7921E432F65EB8ED0CE9755F0E86D72F]]></sign>
                     * <result_code><![CDATA[SUCCESS]]></result_code>
                     * <prepay_id><![CDATA[wx201411101639507cbf6ffd8b0779950874]]></prepay_id>
                     * <trade_type><![CDATA[JSAPI]]></trade_type> </xml>
                     */
                    name = element.getName();
                    value = element.getText();
                    System.out.println("key是：" + name + "，值是：" + value);
                    BeanUtils.setProperty(unifieorderResult, name, value);
                }
            }

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return unifieorderResult;
    }

    /**
     * 2、微信回调后参数解析 解析的时候自动去掉CDMA
     * 
     * @param xml
     */
    @SuppressWarnings("unchecked")
    public static WechatPayResult getWXPayResult(String xml) {
        WechatPayResult wXPayResult = new WechatPayResult();
        try {
            StringReader read = new StringReader(xml);
            // 创建新的输入源SAX 解析器将使用 InputSource 对象来确定如何读取 XML 输入
            InputSource source = new InputSource(read);
            // 创建一个新的SAXBuilder
            SAXBuilder sb = new SAXBuilder();
            // 通过输入源构造一个Document
            Document doc;
            doc = (Document) sb.build(source);

            org.jdom.Element root = doc.getRootElement();// 指向根节点
            List<Element> list = root.getChildren();

            if (list != null && list.size() > 0) {
                String name = "";
                String value = "";
                for (Element element : list) {
                    name = element.getName();
                    value = element.getText();
                    System.out.println("key是：" + name + "，值是：" + value);
                    BeanUtils.setProperty(wXPayResult, name, value);
                }
            }

        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return wXPayResult;
    }

    public static void main(String[] args) throws Exception {
        String xml = "<xml><appid><![CDATA[wxf6aafe068889931b]]></appid><bank_type><![CDATA[CFT]]></bank_type><cash_fee><![CDATA[1]]></cash_fee><device_info><![CDATA[APP-001]]></device_info><fee_type><![CDATA[CNY]]></fee_type><is_subscribe><![CDATA[N]]></is_subscribe><mch_id><![CDATA[1357957402]]></mch_id><nonce_str><![CDATA[464719728]]></nonce_str><openid><![CDATA[oevhwwUWJBUdPyqD1Jryzk4wqeuU]]></openid><out_trade_no><![CDATA[20160630083344000057]]></out_trade_no><result_code><![CDATA[SUCCESS]]></result_code><return_code><![CDATA[SUCCESS]]></return_code><sign><![CDATA[98866FC195A710A5D64AD09AC27957B2]]></sign><time_end><![CDATA[20160630083556]]></time_end><total_fee>1</total_fee><trade_type><![CDATA[APP]]></trade_type><transaction_id><![CDATA[4001292001201606308110420876]]></transaction_id></xml>";
        WechatPayResult result = getWXPayResult(xml);
        System.out.println("接收签名：" + result.getSign());
        SortedMap<String, Object> parameters = AppTool.objectToTreeMap(result);
        // 反校验签名
        String sign = WechatSignUtils.createSign("UTF-8", parameters);
        System.out.println("反签验证：" + sign);
        System.out.println(result.getSign().equals(sign));

    }

}
