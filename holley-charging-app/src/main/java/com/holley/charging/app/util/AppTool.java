package com.holley.charging.app.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;

import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;

public class AppTool {

    private static final double EARTH_RADIUS = 6371; // 地球平均半径，单位为千米；

    public enum StationSortTypeEnum {
        BY_MULTIPLE(1, "综合"), BY_SCORE(2, "评价"), BY_FEE(3, "价格"), BY_DISTANCE(4, "距离");

        private final int    value;
        private final String text;

        StationSortTypeEnum(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

        public static StationSortTypeEnum getEnmuByValue(int value) {
            for (StationSortTypeEnum record : StationSortTypeEnum.values()) {
                if (value == record.getValue()) {
                    return record;
                }
            }
            return null;
        }
    }

    /**
     * 判断是否超时
     * 
     * @param starttime
     * @param timeout 超时时间秒
     * @return
     */
    public static boolean isTimeOut(long starttime, int timeout) {
        long timeoutlong = timeout * 1000;
        long curr = System.currentTimeMillis();
        return curr - starttime > timeoutlong;
    }

    /**
     * 不定参数校验空
     * 
     * @param args
     * @return
     */
    public static boolean isNull(String... args) {
        for (String arg : args) {
            if (StringUtils.isEmpty(arg)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 不定参数校验数字
     * 
     * @param args
     * @return
     */
    public static boolean isNotNumber(String... args) {
        for (String arg : args) {
            if (!NumberUtils.isNumber(arg)) {
                return true;
            }
        }
        return false;
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

    private static double rad(double d) {
        return d * Math.PI / 180.0;
    }

    /**
     * 计算两个GPS坐标点之间的距离, 单位：千米
     * 
     * @param lat1
     * @param lng1
     * @param lat2
     * @param lng2
     * @return
     */
    public static double getPointDistance(double lng1, double lat1, double lng2, double lat2) {
        double radLng1 = rad(lng1);
        double radLng2 = rad(lng2);

        double radLat1 = rad(lat1);
        double radLat2 = rad(lat2);

        double d = Math.acos(Math.sin(radLat1) * Math.sin(radLat2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.cos(radLng2 - radLng1)) * EARTH_RADIUS;
        d = Math.round(d * 10) / 10.0;
        return d;
    }

    public static SortedMap<String, Object> objectToTreeMap(Object obj) throws Exception {
        if (obj == null) {
            return null;
        }
        SortedMap<String, Object> map = new TreeMap<String, Object>();

        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(obj));
        }
        return map;
    }

    public static Object mapToObject(Map<String, Object> map, Class<?> beanClass) throws Exception {
        if (map == null) return null;

        Object obj = beanClass.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }
            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }
        return obj;
    }

    /**
     * 充电桩缓存数值格式化
     * 
     * @param runstatus
     */
    public static void pileStatusValueFormat(PileStatusBean runstatus) {
        if (runstatus == null) return;
        if (runstatus.getUserid() != null) {
            runstatus.setUserid(null);
        }
        if (runstatus.getChapower() == null) {
            runstatus.setChapower(Double.valueOf(0.00));
        }
        if (runstatus.getMoney() == null) {
            runstatus.setMoney(new BigDecimal("0.00"));
        } else {
            runstatus.setMoney(NumberUtil.formateScale2(runstatus.getMoney()));
        }
        if (runstatus.getChalen() == null) {
            runstatus.setChalen(0);
        }
    }

    public static void main(String[] args) {
        // System.out.println(getPointDistance(120.010778, 30.191695, 120.312608, 30.368342));
        //
        SortedMap<String, Object> params = new TreeMap<String, Object>();

        params.put("appid", WechatConfig.appid);
        params.put("mch_id", WechatConfig.mch_id);
        params.put("nonce_str", StringUtil.randomString(16));
        params.put("time_start", DateUtil.DateToLong14Str(new Date()));
        params.put("trade_type", "NATIVE");
        params.put("spbill_create_ip", "172.16.20.111");
        params.put("body", "充值支付");
        params.put("detail", "充值支付");
        params.put("out_trade_no", ChargingCacheUtil.getRechargeNo());
        params.put("total_fee", new BigDecimal("100"));
        params.put("notify_url", "http://172.16.20.101/app/pay/wechatCallbackForRecharge.htm");

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
        System.out.println("xml============================" + weixinPos);
        UnifiedorderResult result = JdomParseXmlUtils.getUnifiedorderResult(weixinPos);
        System.out.println("url===================" + result.getCode_url());
    }

}
