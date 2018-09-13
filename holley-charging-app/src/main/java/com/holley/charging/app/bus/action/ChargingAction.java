package com.holley.charging.app.bus.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

import com.alibaba.rocketmq.client.producer.SendResult;
import com.holley.charging.action.BaseAction;
import com.holley.charging.app.util.AppTool;
import com.holley.charging.common.util.CacheChargeHolder;
import com.holley.charging.model.app.AppointmentInfo;
import com.holley.charging.model.app.AppointmentUserInfo;
import com.holley.charging.model.app.ChargeFeeInfo;
import com.holley.charging.model.app.ChargePayInfo;
import com.holley.charging.model.app.UserRealAndAccount;
import com.holley.charging.model.bus.BusAccount;
import com.holley.charging.model.bus.BusAppointment;
import com.holley.charging.model.bus.BusAppointmentExample;
import com.holley.charging.model.bus.BusPayment;
import com.holley.charging.model.bus.BusPaymentExample;
import com.holley.charging.model.bus.BusPileModel;
import com.holley.charging.model.bus.BusUser;
import com.holley.charging.model.def.ChargeRuleModel;
import com.holley.charging.model.pob.PobChargingPile;
import com.holley.charging.model.pob.PobChargingStation;
import com.holley.charging.service.bussiness.AccountService;
import com.holley.charging.service.bussiness.AppointmentService;
import com.holley.charging.service.bussiness.ChargingService;
import com.holley.charging.service.bussiness.DeviceService;
import com.holley.charging.service.website.UserService;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.Globals;
import com.holley.common.constants.ResultBean;
import com.holley.common.constants.app.ErrorCodeConstants;
import com.holley.common.constants.charge.AppointmentPayStatusEnum;
import com.holley.common.constants.charge.AppointmentStatusEnum;
import com.holley.common.constants.charge.CertificateStatusEnum;
import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.constants.charge.ChargeModeEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.ChargePowerTypeEnum;
import com.holley.common.constants.charge.LinkTypeEnum;
import com.holley.common.constants.charge.PileAppStatusEnum;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.constants.charge.WhetherEnum;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.rocketmq.MQMsgProduct;
import com.holley.common.rocketmq.charging.MsgChargeStart;
import com.holley.common.rocketmq.charging.MsgChargeStop;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.RoleUtil;

/**
 * 充电管理
 * 
 * @author lenovo
 */
public class ChargingAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(ChargingAction.class);
    private static final long   serialVersionUID = 1L;
    private ResultBean          resultBean       = new ResultBean();
    private String              userkey;

    private ChargingService     chargingService;
    private AppointmentService  appointmentService;
    private UserService         userService;
    private DeviceService       deviceService;
    private AccountService      accountService;
    private MQMsgProduct        mqMsgProduct;

    /**
     * 扫描二维码申请充电
     *
     * @return
     */
    public String scanpile() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pilecode = this.getAttribute("pilecode");

        if (AppTool.isNull(pilecode) || !NumberUtils.isNumber(userkey)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        PobChargingPile pile = CacheChargeHolder.getChargePileByCode(pilecode);
        if (pile == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002：无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }

        // 请求缓存桩状态
        PileStatusBean pileStatusBean = ChargingCacheUtil.getPileStatusBean(pile.getId());
        // 检验桩状态
        if (pileStatusBean == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002：无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }

        // 判断是否被其他用户扫码，如果被其他用户扫码还在有效期内不能充电,扫码成功5分钟有效
        if (pileStatusBean.getScanuserid() != null && pileStatusBean.getScantime() != null) {
            if (!Integer.valueOf(userkey).equals(pileStatusBean.getScanuserid()) && !AppTool.isTimeOut(pileStatusBean.getScantime().getTime(), Globals.TIMEOUT_MIN_5 * 60)) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_SCAN_OTHERSCAN);// 20010: 其他用户已经扫码待充电
                resultBean.setMessage("其他用户已经扫码待充电!");
                return SUCCESS;
            }
        }
        // 判断充电桩是否可充电
        if (isMakeCharge(pileStatusBean, true)) {
            Map<String, Object> data = new HashMap<String, Object>();
            // 设置缓存里桩的扫描状态
            pileStatusBean.setScanuserid(Integer.valueOf(userkey));
            pileStatusBean.setScantime(new Date());
            pileStatusBean.setErrorcode(0);
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);

            // 查询充电桩的相关充电费用信息和可支持的充电模式
            ChargeFeeInfo chargefeeinfo = queryChargeFeeInfo(pileStatusBean.getId(), pileStatusBean.getStationid());
            data.put("chargefeeinfo", chargefeeinfo);
            resultBean.setData(data);
        }
        return SUCCESS;
    }

    /**
     * 申请成功后显示充电选项，启动充电
     *
     * @return
     */
    public String startcharge() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pileid = this.getAttribute("pileid");
        String chargemode = this.getAttribute("chargemode");
        String chargevalue = this.getAttribute("chargevalue");

        if (AppTool.isNotNumber(userkey, pileid) || AppTool.isNull(chargemode, chargevalue)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        // 请求缓存桩状态
        PileStatusBean pileStatusBean = ChargingCacheUtil.getPileStatusBean(Integer.valueOf(pileid));
        // 检验桩状态
        if (pileStatusBean == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002：无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }

        Integer userid = Integer.valueOf(userkey);
        Integer scanuserid = pileStatusBean.getScanuserid();
        Date scantime = pileStatusBean.getScantime();

        if (scanuserid == null || scantime == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_STCHA_TIMEOUT);// 4: 规定时间未操作，扫码失效，请重新扫码
            resultBean.setMessage("请先扫码!");
            return SUCCESS;
        }

        if (userid.equals(scanuserid)) {// 自己扫码，判断扫码是否超时
            if (AppTool.isTimeOut(scantime.getTime(), Globals.TIMEOUT_MIN_5 * 60)) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_STCHA_TIMEOUT);// 4：扫码后5分钟内未操作，扫码失效，请重新扫码
                resultBean.setMessage("扫码后5分钟内未操作,扫码失效,请重新扫码!");
                return SUCCESS;
            }
        } else {// 判断是否被其他用户扫码，如果被其他用户扫码还在有效期内不能充电,扫码成功5分钟有效
            if (!AppTool.isTimeOut(scantime.getTime(), Globals.TIMEOUT_MIN_5 * 60)) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_SCAN_OTHERSCAN);// 20010: 其他用户已经扫码待充电
                resultBean.setMessage("其他用户已经扫码待充电!");
                return SUCCESS;
            }
        }

        // 判断充电桩是否可充电
        if (!isMakeCharge(pileStatusBean, false)) {
            return SUCCESS;
        }

        // 查看实名状态及钱包余额信息
        UserRealAndAccount real = userService.selectUserRealAndAccount(userid);
        if (CertificateStatusEnum.FAILED.getShortValue().equals(real.getRealstatus()) && real.getUsablemoney().doubleValue() < Globals.CHARGE_USABLEMONEY_LIMIT) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_STCHA_NOREAL);// 5：未实名且钱包余额少于100元
            resultBean.setMessage("账户余额少于" + NumberUtil.formateScale2Str(new BigDecimal(Globals.CHARGE_USABLEMONEY_LIMIT)) + "元，无法充电，请往账户充值或实名验证!");
            return SUCCESS;
        }

        if (ChargeModeEnum.BY_AUTO.getShortValue().equals(Short.valueOf(chargemode))) {
            chargevalue = real.getUsablemoney().toString();
        }

        BusUser busUser = userService.selectUserByCache(Integer.valueOf(userkey));
        if (busUser == null || AppTool.isNull(busUser.getPhone())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        // 产生交易流水号：年（两位）+月（两位）+日（两位）+时（两位）+分（两位)）+序号（6位)
        PobChargingPile pile = CacheChargeHolder.getChargePileById(pileStatusBean.getStationid(), pileStatusBean.getId());
        if (pile == null || StringUtil.isEmpty(pile.getComAddr())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }
        Date now = new Date();
        String comaddr = StringUtil.AddjustLength(pile.getComAddr(), 16, "0");
        int num = ChargingCacheUtil.getChargeNo(pileid);
        String tradeNo = comaddr + DateUtil.DateToNosec10Str(now) + StringUtil.zeroPadString(num + "", 6);

        MsgChargeStart msg = new MsgChargeStart(Integer.valueOf(pileid), userid, busUser.getPhone(), Integer.valueOf(chargemode).byteValue(), Double.valueOf(chargevalue), tradeNo);
        SendResult result = mqMsgProduct.sendMQMsg(msg);
        if (result == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        logger.info("发送开始充电命令------tradeNo=" + tradeNo + ",pileid=" + pileid + ",userid=" + userid + ",chargemode=" + chargemode + ",chargevalue=" + chargevalue + ",sendstatus="
                    + result.getSendStatus() + ",msgid=" + result.getMsgId());
        // 开始等待前置状态
        long startTime = System.currentTimeMillis();
        boolean queryResult = false;
        try {
            while (true) {
                // 在超时范围内等待返回命令
                if (AppTool.isTimeOut(startTime, Globals.TIMEOUT_30)) {
                    break;
                }

                // 请求缓存桩状态
                pileStatusBean = ChargingCacheUtil.getPileStatusBean(Integer.valueOf(pileid));
                if (pileStatusBean == null) {
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002:无此充电桩
                    resultBean.setMessage("无此充电桩!");
                    return SUCCESS;
                }

                PileStatusEnum status = pileStatusBean.getStatus();
                if (PileStatusEnum.CHARGING == status) { // 充电中
                    queryResult = true;
                    break;
                } else if (PileStatusEnum.FAULT == status || PileStatusEnum.OFFLINE == status) { // 故障或离线
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PILEOFFLINE);// 20000： 充电桩离线/故障
                    resultBean.setMessage("充电桩离线/故障!");
                    return SUCCESS;
                } else if (PileStatusEnum.BUSYING == status) {// 忙碌中或充电完成
                                                              // ||PileStatusEnum.FINISH.equals(pileStatus)
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_BUSYING);// 20003：充电桩忙碌中(桩屏幕正在被操作等原因)
                    resultBean.setMessage("充电桩忙碌中!");
                    return SUCCESS;
                } else if (pileStatusBean.getErrorcode() > 0) {// 充电桩返回的错误码
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(pileStatusBean.getErrorcode());
                    // 根据充电桩返回的错误码显示信息
                    if (ErrorCodeConstants.ERR_STCHA_NOGUN == pileStatusBean.getErrorcode()) {
                        resultBean.setMessage("未插充电枪!");
                    }
                    pileStatusBean.setErrorcode(0);
                    ChargingCacheUtil.setPileStatusBean(pileStatusBean);
                    return SUCCESS;
                }
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);// 10003: 系统调用异常，请稍后重试
            resultBean.setMessage("系统调用异常，请稍后重试!");
            return SUCCESS;
        }

        if (queryResult) {// 启动充电成功
            // 设置充电用户
            pileStatusBean.setUserid(Integer.valueOf(userkey));
            // 清除缓存扫码人信息
            pileStatusBean.setScantime(null);
            pileStatusBean.setScanuserid(null);
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);
        } else { // 超时
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_STCHA_PILEERR);// 6:充电失败
            resultBean.setMessage("充电失败!");
            logger.info("--------[userid:" + userkey + ",pileid:" + pileid + "]--------超时，充电失败---------");
        }
        return SUCCESS;
    }

    /**
     * 充电完成，自动停止充电
     * 
     * @return
     */
    public String autoendcharge() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pileid = this.getAttribute("pileid");

        if (AppTool.isNull(pileid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004： 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        PileStatusBean pileStatusBean = ChargingCacheUtil.getPileStatusBean(Integer.valueOf(pileid));
        if (pileStatusBean == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002：无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }

        Map<String, Object> data = new HashMap<String, Object>();
        // 查询充电缴费记录
        if (pileStatusBean.getTradeno() != null) {
            BusPaymentExample emp = new BusPaymentExample();
            BusPaymentExample.Criteria cr = emp.createCriteria();
            cr.andTradeNoEqualTo(pileStatusBean.getTradeno());
            List<BusPayment> list = chargingService.selectChargePaymentByExample(emp);
            if (list != null && list.size() > 0) {
                BusPayment record = list.get(0);
                if (ChargeDealStatusEnum.SUCCESS.getShortValue().equals(record.getDealStatus())) {
                    ChargePayInfo info = new ChargePayInfo();
                    info.setChafee(NumberUtil.formateScale2(record.getChaFee()));
                    info.setChalen(record.getChaLen());
                    info.setChapower(record.getChaPower());
                    info.setChargemodedesc(ChargeModeEnum.getDefaultText(record.getChaMode()));
                    info.setId(record.getId());
                    info.setParkfee(NumberUtil.formateScale2(record.getParkFee()));
                    info.setPileid(record.getPileId());
                    info.setServicefee(NumberUtil.formateScale2(record.getServiceFee()));
                    info.setShouldmoney(NumberUtil.formateScale2(record.getShouldMoney()));
                    info.setStarttime(record.getStartTime());
                    data.put("payment", info);

                    // 查询账户余额
                    BusAccount account = accountService.selectAccoutByPrimaryKey(Integer.valueOf(userkey));
                    if (account != null) {
                        account.setUserId(null);
                        data.put("account", account);
                    }
                    resultBean.setData(data);
                } else {
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDCHA_GETFEEORDER);// 2:获取缴费订单失败
                    resultBean.setMessage("停止充电成功，订单正在生成中，请稍候在我的充电记录中查看并缴费。");
                    logger.info("--------[userid:" + userkey + ",pileid:" + pileid + "]--------超时，停止充电成功，未成功获取充电缴费记录---------");
                }
            }
        }

        if (PileStatusEnum.FINISH == pileStatusBean.getStatus()) {
            pileStatusBean.setStatus(PileStatusEnum.IDLE);
            pileStatusBean.setTradeno(null);
            pileStatusBean.setUserid(null);
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);
        }
        return SUCCESS;
    }

    /**
     * 手动停止充电
     *
     * @return
     */
    public String endcharge() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pileid = this.getAttribute("pileid");

        if (AppTool.isNotNumber(userkey, pileid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004： 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        PileStatusBean pileStatusBean = ChargingCacheUtil.getPileStatusBean(Integer.valueOf(pileid));
        if (pileStatusBean == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002：无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }

        if (PileStatusEnum.IDLE == pileStatusBean.getStatus()) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDCHA_NOTSTARTPILE);// 1: 充电桩未启动
            resultBean.setMessage("充电桩未启动!");
            return SUCCESS;
        } else if (PileStatusEnum.BUSYING == pileStatusBean.getStatus()) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_BUSYING);// 20003: 充电桩忙碌中(桩屏幕正在被操作等原因)
            resultBean.setMessage("充电桩忙碌中!");
            return SUCCESS;
        } else if (PileStatusEnum.FAULT == pileStatusBean.getStatus() || PileStatusEnum.OFFLINE == pileStatusBean.getStatus()) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PILEOFFLINE);// 20000: 充电桩离线/故障
            resultBean.setMessage("充电桩离线/故障!");
            return SUCCESS;
        }

        if (!Integer.valueOf(userkey).equals(pileStatusBean.getUserid())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_OTHERUSING);// 20001: 其他用户正在充电
            resultBean.setMessage("其他用户正在充电!");
            return SUCCESS;
        }

        if (PileStatusEnum.FINISH == pileStatusBean.getStatus()) {// 充电完成，可能由电桩应答超时，第一次没有检测到完成状态，再一次请求的时候检测到
            if (finishedrec(pileStatusBean.getTradeno())) {
                endchargesuccess(pileStatusBean);
            } else {
                pileStatusBean.setStatus(PileStatusEnum.IDLE);
                pileStatusBean.setTradeno(null);
                pileStatusBean.setUserid(null);
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDCHA_GETFEEORDER);// 2:获取缴费订单失败
                resultBean.setMessage("停止充电成功，订单正在生成中，请稍候在我的充电记录中查看并缴费。");
                logger.info("--------[userid:" + userkey + ",pileid:" + pileid + "]--------超时，停止充电成功，未成功获取充电缴费记录---------");
            }
            return SUCCESS;
        }

        // 查看用户信息
        BusUser busUser = userService.selectUserByCache(Integer.valueOf(userkey));
        if (busUser == null || AppTool.isNull(busUser.getPhone())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        // 向充电桩发送停止充电请求
        MsgChargeStop msg = new MsgChargeStop(Integer.valueOf(pileid), Integer.valueOf(userkey), busUser.getPhone(), pileStatusBean.getTradeno());
        SendResult result = mqMsgProduct.sendMQMsg(msg);
        if (result == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        logger.info("发送结束充电命令------tradeNo=" + pileStatusBean.getTradeno() + ",pileid=" + pileid + ",userid=" + userkey + ",sendstatus=" + result.getSendStatus() + ",msgid="
                    + result.getMsgId());
        // 开始等待前置状态
        long startTime = System.currentTimeMillis();
        boolean queryResult = false;
        try {
            while (true) {
                // 在超时范围内等待返回命令
                if (AppTool.isTimeOut(startTime, Globals.TIMEOUT_30)) {
                    break;
                }

                // 请求缓存桩状态
                pileStatusBean = ChargingCacheUtil.getPileStatusBean(Integer.valueOf(pileid));
                if (pileStatusBean == null) {
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002:无此充电桩
                    resultBean.setMessage("无此充电桩!");
                    return SUCCESS;
                }

                PileStatusEnum status = pileStatusBean.getStatus();
                if (PileStatusEnum.FINISH == status && finishedrec(pileStatusBean.getTradeno())) { // 充电完成,并已更新充电缴费记录，表示停止充电成功
                    queryResult = true;
                    break;
                } else if (PileStatusEnum.FAULT == status || PileStatusEnum.OFFLINE == status) { // 故障或离线
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PILEOFFLINE);// 20000： 充电桩离线/故障
                    resultBean.setMessage("充电桩离线/故障!");
                    return SUCCESS;
                } else if (PileStatusEnum.BUSYING == status) {// 忙碌中
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_BUSYING);// 20003：充电桩忙碌中(桩屏幕正在被操作等原因)
                    resultBean.setMessage("充电桩忙碌中!");
                    return SUCCESS;
                } else if (pileStatusBean.getErrorcode() > 0) {// 充电桩返回的错误码
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(pileStatusBean.getErrorcode());
                    // 根据充电桩返回的错误码显示提示信息
                    resultBean.setMessage("充电桩未知错误!");

                    pileStatusBean.setErrorcode(0);
                    ChargingCacheUtil.setPileStatusBean(pileStatusBean);
                    return SUCCESS;
                }
                if (PileStatusEnum.FINISH == status) {
                    Thread.sleep(2000);// 充电停止成功，但前置未更新充电缴费记录，则间隔时间改为2秒一次
                } else {
                    Thread.sleep(500);// 半秒
                }
            }

        } catch (InterruptedException e) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);// 10003: 系统调用异常，请稍后重试
            resultBean.setMessage("系统调用异常，请稍后重试!");
            return SUCCESS;
        }

        if (queryResult) {// 停止充电成功
            endchargesuccess(pileStatusBean);
        } else { // 超时
            resultBean.setSuccess(false);
            if (PileStatusEnum.FINISH == pileStatusBean.getStatus()) {
                pileStatusBean.setStatus(PileStatusEnum.IDLE);
                pileStatusBean.setTradeno(null);
                pileStatusBean.setUserid(null);
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDCHA_GETFEEORDER);// 2:获取缴费订单失败
                resultBean.setMessage("停止充电成功，订单正在生成中，请稍候在我的充电记录中查看并缴费。");
                logger.info("--------[userid:" + userkey + ",pileid:" + pileid + "]--------超时，停止充电成功，未成功获取充电缴费记录---------");
            } else {
                resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDCHA_PILEERR);// 3:停止充电失败
                resultBean.setMessage("停止充电失败!");
                logger.info("--------[userid:" + userkey + ",pileid:" + pileid + "]--------超时，停止充电失败---------");
            }
        }
        return SUCCESS;
    }

    /**
     * 判断用户是否已预约其他桩
     *
     * @param userkey
     * @param pileid
     * @return
     */
    private boolean queryOrderedOtherPile(String userkey, Integer pileid) {
        BusAppointmentExample emp = new BusAppointmentExample();
        BusAppointmentExample.Criteria cr = emp.createCriteria();
        cr.andUserIdEqualTo(Integer.valueOf(userkey));
        cr.andAppStatusEqualTo(AppointmentStatusEnum.ORDERING.getShortValue());
        cr.andEndTimeGreaterThan(new Date());
        cr.andPileIdNotEqualTo(pileid);
        List<BusAppointment> orderedlist = appointmentService.selectAppointmentByExample(emp);
        if (orderedlist != null && orderedlist.size() > 0) {
            return false;
        }
        return true;
    }

    /**
     * 用户是否存在未缴费记录
     *
     * @param userid
     * @return
     */
    private boolean queryUnpaidFee(String userid) {
        // 查看是否有未缴费的预约记录
        BusAppointmentExample aemp = new BusAppointmentExample();
        BusAppointmentExample.Criteria acr = aemp.createCriteria();
        acr.andUserIdEqualTo(Integer.valueOf(userid));
        acr.andPayStatusNotEqualTo(AppointmentPayStatusEnum.SUCCESS.getShortValue());
        acr.andAppStatusNotEqualTo(AppointmentStatusEnum.DEL.getShortValue());
        List<BusAppointment> appList = appointmentService.selectAppointmentByExample(aemp);
        Map<String, Object> data = new HashMap<String, Object>();
        if (appList != null && appList.size() > 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_UNPAIDFEE_APPO);// 20004: 用户有未缴清的预约费用
            resultBean.setMessage("用户有未缴清的预约费用!");
            BusAppointment record = appList.get(0);

            AppointmentInfo info = new AppointmentInfo();
            info.setAddtime(record.getAddTime());
            info.setAppfee(NumberUtil.formateScale2(record.getAppFee()));
            info.setApplen(record.getAppLen());
            // info.setAppstatus(record.getAppStatus());
            info.setEndtime(record.getEndTime());
            info.setId(record.getId());
            // info.setPaystatus(record.getPayStatus());
            info.setPileid(record.getPileId());
            info.setStarttime(record.getStartTime());

            PobChargingPile pile = CacheChargeHolder.getChargePileById(record.getPileId());
            if (pile != null) {
                info.setPilename(pile.getPileName());
                if (pile.getPileType() != null) {
                    info.setPiletypedesc(ChargePowerTypeEnum.getText(pile.getPileType().intValue()));
                }

                PobChargingStation station = CacheChargeHolder.getChargeStationById(pile.getStationId());
                if (station != null) {
                    info.setStationname(station.getStationName());
                }

                BusPileModel model = CacheChargeHolder.selectPileModelById(pile.getPileModel());
                if (model != null) {
                    info.setOutv(model.getOutV() == null ? "" : model.getOutV());
                }
            }
            data.put("appointment", info);
            resultBean.setData(data);
            return false;
        }
        // 查看是否有未缴费的充电记录
        BusPaymentExample pemp = new BusPaymentExample();
        BusPaymentExample.Criteria pcr = pemp.createCriteria();
        pcr.andUserIdEqualTo(Integer.valueOf(userid));
        pcr.andDealStatusEqualTo(ChargeDealStatusEnum.SUCCESS.getShortValue());
        pcr.andPayStatusNotEqualTo(ChargePayStatusEnum.SUCCESS.getShortValue());
        // pcr.andDealStatusNotEqualTo(ChargeDealStatusEnum.DEL.getShortValue());
        List<BusPayment> chargeList = chargingService.selectChargePaymentByExample(pemp);
        if (chargeList != null && chargeList.size() > 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_UNPAIDFEE_CHARGE);// 20005: 用户有未缴清的充电费用
            resultBean.setMessage("用户有未缴清的充电费用!");

            BusPayment record = chargeList.get(0);
            ChargePayInfo info = new ChargePayInfo();
            info.setChafee(NumberUtil.formateScale2(record.getChaFee()));
            info.setChalen(record.getChaLen());
            info.setChapower(record.getChaPower());
            info.setChargemodedesc(ChargeModeEnum.getDefaultText(record.getChaMode()));
            info.setId(record.getId());
            info.setParkfee(NumberUtil.formateScale2(record.getParkFee()));
            info.setPileid(record.getPileId());
            info.setServicefee(NumberUtil.formateScale2(record.getServiceFee()));
            info.setShouldmoney(NumberUtil.formateScale2(record.getShouldMoney()));
            info.setStarttime(record.getStartTime());
            // info.setUpdatetime(record.getUpdateTime());

            data.put("payment", info);
            resultBean.setData(data);
            return false;
        }
        return true;
    }

    /**
     * 查询充电桩的相关充电费用信息和可支持的充电模式
     * 
     * @param pileid
     * @param stationid
     * @return
     */
    private ChargeFeeInfo queryChargeFeeInfo(Integer pileid, Integer stationid) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pileId", pileid);
        params.put("status", WhetherEnum.YES.getShortValue());// 生效
        params.put("active", WhetherEnum.YES.getShortValue());// 已使用
        ChargeRuleModel ruleModel = deviceService.selectChargeRuleModelByMap(params);
        if (ruleModel == null) return null;
        PobChargingPile pile = CacheChargeHolder.getChargePileById(stationid, Integer.valueOf(pileid));

        ChargeFeeInfo feeInfo = new ChargeFeeInfo();
        feeInfo.setPileid(Integer.valueOf(pileid));
        feeInfo.setPilename(pile.getPileName());
        feeInfo.setChawaydesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.CHA_WAY.getValue(), pile.getChaWay().toString()));
        feeInfo.setPiletypedesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.PILE_TYPE.getValue(), pile.getChaWay().toString()));
        if (Globals.CHARGE_RULE_SINGLE == ruleModel.getId().intValue()) {
            feeInfo.setChargeFee(NumberUtil.formateScale2Str(ruleModel.getChargeFee()));
        } else {
            String ruleDetail = "";
            if (ruleModel.getJianFee() != null) {
                ruleDetail += "尖:" + NumberUtil.formateScale2Str(ruleModel.getJianFee()) + ";";
            }
            if (ruleModel.getFengFee() != null) {
                ruleDetail += "峰:" + NumberUtil.formateScale2Str(ruleModel.getFengFee()) + ";";
            }
            if (ruleModel.getPingFee() != null) {
                ruleDetail += "平:" + NumberUtil.formateScale2Str(ruleModel.getPingFee()) + ";";
            }
            if (ruleModel.getGuFee() != null) {
                ruleDetail += "谷:" + NumberUtil.formateScale2Str(ruleModel.getGuFee()) + ";";
            }
            feeInfo.setChargeFee(ruleDetail);
        }
        feeInfo.setParkFee(ruleModel.getParkFee() == null ? "" : NumberUtil.formateScale2Str(ruleModel.getParkFee()));
        feeInfo.setServiceFee(ruleModel.getServiceFee() == null ? "" : NumberUtil.formateScale2Str(ruleModel.getServiceFee()));
        String chargemode = ChargeModeEnum.BY_AUTO.getValue() + "";// 自动
        if (pile.getIsRationCha() != null && pile.getIsRationCha().intValue() == WhetherEnum.YES.getValue()) {
            chargemode += "," + ChargeModeEnum.BY_EQ.getValue();// 按电量
        }
        if (pile.getIsTimeCha() != null && pile.getIsTimeCha().intValue() == WhetherEnum.YES.getValue()) {
            chargemode += "," + ChargeModeEnum.BY_TIME.getValue();// 按时间
        }
        if (pile.getIsMoneyCha() != null && pile.getIsMoneyCha().intValue() == WhetherEnum.YES.getValue()) {
            chargemode += "," + ChargeModeEnum.BY_MONEY.getValue();// 按金额
        }
        feeInfo.setChargemode(chargemode);

        // 查询桩型号信息
        BusPileModel pileModel = CacheChargeHolder.selectPileModelById(pile.getPileModel());
        if (pileModel != null) {
            feeInfo.setRatp(pileModel.getRatP() == null ? "" : pileModel.getRatP());
        }
        return feeInfo;
    }

    /**
     * 手动停止充电成功后业务处理
     * 
     * @param pileStatusBean
     */
    private void endchargesuccess(PileStatusBean pileStatusBean) {
        // 查询充电缴费记录
        Map<String, Object> data = new HashMap<String, Object>();
        if (pileStatusBean.getTradeno() != null) {
            BusPaymentExample emp = new BusPaymentExample();
            BusPaymentExample.Criteria cr = emp.createCriteria();
            cr.andTradeNoEqualTo(pileStatusBean.getTradeno());
            List<BusPayment> list = chargingService.selectChargePaymentByExample(emp);
            if (list != null && list.size() > 0) {
                BusPayment record = list.get(0);
                ChargePayInfo info = new ChargePayInfo();
                info.setChafee(NumberUtil.formateScale2(record.getChaFee()));
                info.setChalen(record.getChaLen());
                info.setChapower(record.getChaPower());
                info.setChargemodedesc(ChargeModeEnum.getDefaultText(record.getChaMode()));
                info.setId(record.getId());
                info.setParkfee(NumberUtil.formateScale2(record.getParkFee()));
                info.setPileid(record.getPileId());
                info.setServicefee(NumberUtil.formateScale2(record.getServiceFee()));
                info.setShouldmoney(NumberUtil.formateScale2(record.getShouldMoney()));
                info.setStarttime(record.getStartTime());
                data.put("payment", info);
            }
        }
        // 查询账户余额
        BusAccount account = accountService.selectAccoutByPrimaryKey(Integer.valueOf(userkey));
        if (account != null) {
            account.setUserId(null);
            data.put("account", account);
        }
        resultBean.setData(data);
        if (PileStatusEnum.FINISH == pileStatusBean.getStatus()) {
            pileStatusBean.setStatus(PileStatusEnum.IDLE);
            pileStatusBean.setTradeno(null);
            pileStatusBean.setUserid(null);
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);
        }
    }

    private boolean finishedrec(String tradeNo) {
        logger.info("--------[tradeNo:" + tradeNo + "]--------已停止充电，等待前置更新充电缴费记录---------");
        if (StringUtil.isEmpty(tradeNo)) {
            return false;
        }
        BusPaymentExample emp = new BusPaymentExample();
        BusPaymentExample.Criteria cr = emp.createCriteria();
        cr.andTradeNoEqualTo(tradeNo);
        List<BusPayment> list = chargingService.selectChargePaymentByExample(emp);
        if (list != null && list.size() > 0) {
            BusPayment record = list.get(0);
            if (ChargeDealStatusEnum.SUCCESS.getShortValue().equals(record.getDealStatus())) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    private boolean isMakeCharge(PileStatusBean pileStatusBean, boolean isscanpile) {
        PileStatusEnum pileStatus = pileStatusBean.getStatus();
        PileAppStatusEnum appStatus = pileStatusBean.getAppstatus();
        Map<String, Object> data = new HashMap<String, Object>();
        if (PileStatusEnum.OFFLINE == pileStatus || PileStatusEnum.FAULT == pileStatus) {// 故障或离线
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PILEOFFLINE);// 20000： 充电桩离线/故障
            resultBean.setMessage("充电桩离线/故障!");
            return false;
        } else if (PileStatusEnum.CHARGING == pileStatus) {// 充电中
            if (Integer.valueOf(userkey).equals(pileStatusBean.getUserid())) {
                pileStatusBean.setUserid(null);
                data.put("pilestatus", pileStatusBean);
                resultBean.setData(data);

                resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_USING);// 20007: 自己正在充电
                resultBean.setMessage("您正在充电!");
                resultBean.setSuccess(true);
            } else {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_OTHERUSING);// 20001：其他用户正在充电
                resultBean.setMessage("其他用户正在充电!");
            }
            return false;
        } else if (PileStatusEnum.BUSYING == pileStatus) {// 忙碌中
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_BUSYING);// 20003：充电桩忙碌中(桩屏幕正在被操作等原因)
            resultBean.setMessage("充电桩忙碌中!");
            return false;
        } else if (PileStatusEnum.IDLE == pileStatus || PileStatusEnum.FINISH == pileStatus) {// 空闲中
            // 查看该桩是否被预约
            if (PileAppStatusEnum.ORDER_SUCCESS == appStatus && !Integer.valueOf(userkey).equals(pileStatusBean.getUserid()) && pileStatusBean.getAppendtime() != null
                && new Date().compareTo(pileStatusBean.getAppendtime()) < 0) {
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_OTHERUSERAPPO);// 20008: 已经被其他用户预约
                resultBean.setMessage("已经被其他用户预约!");
                if (isscanpile) {
                    resultBean.setSuccess(true);
                    // 查询预约人信息
                    BusUser busUser = userService.selectUserByCache(pileStatusBean.getUserid());
                    if (busUser != null) {
                        String imgurl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL);
                        AppointmentUserInfo record = new AppointmentUserInfo();
                        record.setUserkey(encrypt(busUser.getId().toString()));
                        record.setUsername(busUser.getUsername());
                        if (busUser.getHeadImg() != null) {
                            record.setHeadimg(imgurl + busUser.getHeadImg());
                        }
                        if (pileStatusBean.getAppendtime() != null) {
                            record.setAppendtime(pileStatusBean.getAppendtime());
                            long times = pileStatusBean.getAppendtime().getTime() - System.currentTimeMillis();
                            record.setTimestart(times);
                        }

                        // 如果预约人不是自己，则返回自己的信息，供融云聊天使用
                        if (!Integer.valueOf(userkey).equals(busUser.getId())) {
                            BusUser meuser = userService.selectUserByCache(Integer.valueOf(userkey));
                            if (meuser != null) {
                                record.setMeuserkey(encrypt(userkey));
                                record.setMeusername(meuser.getUsername());
                                record.setMeheadimg(imgurl + meuser.getHeadImg());
                            }
                        }

                        data.put("otheruser", record);
                        // 查询充电桩的相关充电费用信息和可支持的充电模式
                        ChargeFeeInfo chargefeeinfo = queryChargeFeeInfo(pileStatusBean.getId(), pileStatusBean.getStationid());
                        data.put("chargefeeinfo", chargefeeinfo);

                        resultBean.setData(data);
                    }
                }
                return false;
            }
            // 查看用户是否预约了其他桩
            if (!queryOrderedOtherPile(userkey, pileStatusBean.getId())) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_USER_HAVEAPPO_OTHERPILE);// 20009: 已经预约其他充电桩，需先取消
                resultBean.setMessage("你已经预约其他充电桩，需先取消!");
                return false;
            }
            // 查看用户是否有欠费(预约费用和充电费用)
            if (!queryUnpaidFee(userkey)) {
                return false;
            }
        }
        return true;
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

    public void setUserkey(String userkey) {
        this.userkey = userkey;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setMqMsgProduct(MQMsgProduct mqMsgProduct) {
        this.mqMsgProduct = mqMsgProduct;
    }

}
