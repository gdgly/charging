package com.holley.charging.common;

import java.util.Calendar;

import com.holley.common.cache.RedisUtil;
import com.holley.common.cache.charging.CacheKeyProvide;
import com.holley.common.cache.charging.CacheKeyProvide.KeyMsgCodeTypeEnum;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.ResultBean;
import com.holley.common.dataobject.LoginCountBean;
import com.holley.common.util.DateUtil;

public class ValidateUtil {

    /**
     * 检测短信验证码的发送间隔
     * 
     * @param jsessionId
     * @return
     */
    public static ResultBean checkCanGetMobileCode(String jsessionId, KeyMsgCodeTypeEnum type) {
        ResultBean bean = new ResultBean();
        String code = ChargingCacheUtil.getMessageValidateCode(jsessionId, type);

        if (code != null) {// 申请过一次 查询过期时间
            String key = null;
            if (type == KeyMsgCodeTypeEnum.REGISTER) {
                key = CacheKeyProvide.getKey(CacheKeyProvide.KEY_MESSAGE_REGISTER_VALIDATE, jsessionId);
            } else if (type == KeyMsgCodeTypeEnum.RESETPWD) {
                key = CacheKeyProvide.getKey(CacheKeyProvide.KEY_MESSAGE_RESETPWD_VALIDATE, jsessionId);
            } else if (type == KeyMsgCodeTypeEnum.CASH) {
                key = CacheKeyProvide.getKey(CacheKeyProvide.KEY_MESSAGE_CASH_VALIDATE, jsessionId);
            }
            Long time = ChargingCacheUtil.ttl(key);
            long temp = RedisUtil.EXRP_3M - time;
            if (temp <= 60) {
                temp = 60 - temp;
                bean.setSuccess(false);
                bean.setMessage("请您 " + temp + " 秒后，重新获取验证码");
            }
        }

        return bean;
    }

    /**
     * 登录失败处理，记录短时间的次数和登录时间，若超过5次，必须延时等待
     * 
     * @param account
     * @return
     */
    public static LoginCountBean initLogin(String account, KeySessionTypeEnum type) {
        Calendar now = Calendar.getInstance();

        int failCount = 1;
        String errmsg = null;
        boolean refuseLogin = false;

        int loginInterval = Globals.LOGIN_INTERVAL; // 短时登录失败超多次后再次允许登录的时间间隔
        int totalFailCount = Globals.LOGIN_FAIL_TOTAL; // 总共登录失败次数
        // Object obj = Globals.LOGIN_COUNT_MAP.get(account);
        LoginCountBean lcbean = ChargingCacheUtil.getLoginCount(account, type);
        if (lcbean != null) {
            long mins = DateUtil.calcMinuteBetween2Dates(lcbean.getLogintime(), now.getTime());
            if (Math.abs(mins) > loginInterval) {// 记录三十分钟内频繁登录失败的次数
                failCount = 1;// 等待时间超过30分钟，允许重新登录，重新计数
                refuseLogin = false;
            } else {
                failCount = lcbean.getCount() + 1;
                if (failCount > totalFailCount) { // 若超过5次，必须延时等待
                    long remain = loginInterval - mins;
                    errmsg = "该用户登录失败超过" + totalFailCount + "次，请";
                    if (remain > 0) {
                        errmsg += remain + "分钟后";
                    } else {
                        long start = lcbean.getLogintime().getTime();
                        long end = Calendar.getInstance().getTime().getTime();
                        remain = 60 - ((end - start) % (1000 * 60) / 1000);
                        errmsg += remain + "秒后";
                    }
                    errmsg += "重试!";
                    refuseLogin = true;
                }
            }
        } else {
            lcbean = new LoginCountBean();
            lcbean.setAccount(account);
        }
        lcbean.setCount(failCount);
        lcbean.setReTryCount(totalFailCount - failCount);
        lcbean.setLoginFailMsg(errmsg);
        lcbean.setRefuseLogin(refuseLogin);
        if (failCount <= totalFailCount) {
            lcbean.setLogintime(now.getTime());
        }
        ChargingCacheUtil.setLoginCount(account, type, lcbean);
        return lcbean;
    }

    /**
     * 登录成功后清除登录异常验证bean
     * 
     * @param account
     */
    public static void clearLoginBean(String account, KeySessionTypeEnum type) {
        ChargingCacheUtil.removeLoginCount(account, type);
    }

}
