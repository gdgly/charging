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
import com.holley.charging.model.app.AppointmentFeeInfo;
import com.holley.charging.model.app.AppointmentInfo;
import com.holley.charging.model.app.AppointmentUserInfo;
import com.holley.charging.model.app.ChargePayInfo;
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
import com.holley.common.rocketmq.charging.MsgAppointmentCancle;
import com.holley.common.rocketmq.charging.MsgAppointmentReq;
import com.holley.common.util.DateUtil;
import com.holley.common.util.NumberUtil;
import com.holley.common.util.StringUtil;
import com.holley.platform.util.CacheSysHolder;
import com.holley.platform.util.RoleUtil;

/**
 * 预约管理
 * 
 * @author zhouli
 */
public class AppointmentAction extends BaseAction {

    private final static Logger logger           = Logger.getLogger(AppointmentAction.class);
    private static final long   serialVersionUID = 1L;
    private ResultBean          resultBean       = new ResultBean();
    private AppointmentService  appointmentService;
    private ChargingService     chargingService;
    private DeviceService       deviceService;
    private UserService         userService;
    private AccountService      accountService;
    private String              userkey;
    private MQMsgProduct        mqMsgProduct;

    /**
     * 请求预约,点击预约按钮
     * 
     * @return
     */
    public String reqappointment() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pileid = this.getAttribute("pileid");

        if (AppTool.isNotNumber(userkey, pileid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        Map<String, Object> data = new HashMap<String, Object>();
        // 返回费用信息
        AppointmentFeeInfo appfeeinfo = queryAppointmentFeeInfo(Integer.valueOf(pileid));
        data.put("appfeeinfo", appfeeinfo);

        // 请求缓存桩状态
        PileStatusBean pilestatus = ChargingCacheUtil.getPileStatusBean(Integer.valueOf(pileid));
        if (pilestatus == null) {
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
            resultBean.setSuccess(true);
            resultBean.setMessage("充电桩未上线!");
            resultBean.setData(data);
            return SUCCESS;
        }

        // 查看该桩是否被预约,如果被预约，则返回预约人信息
        // if (PileStatusEnum.IDLE == pilestatus.getStatus()) {
        if (PileAppStatusEnum.UNORDERED != pilestatus.getAppstatus() && PileAppStatusEnum.UNORDERABLE != pilestatus.getAppstatus()) {
            if (pilestatus.getUserid() != null && pilestatus.getUserid().intValue() > 0 && pilestatus.getAppendtime() != null
                && new Date().compareTo(pilestatus.getAppendtime()) < 0) {
                BusUser busUser = userService.selectUserByCache(pilestatus.getUserid());
                if (busUser != null) {
                    String imgurl = RoleUtil.selectRuleByPrimaryKey(RoleUtil.IMG_URL);
                    AppointmentUserInfo record = new AppointmentUserInfo();
                    record.setUserkey(encrypt(busUser.getId().toString()));
                    record.setUsername(busUser.getUsername());
                    if (busUser.getHeadImg() != null) {
                        record.setHeadimg(imgurl + busUser.getHeadImg());
                    }
                    if (pilestatus.getAppendtime() != null) {
                        record.setAppendtime(pilestatus.getAppendtime());
                        long times = pilestatus.getAppendtime().getTime() - System.currentTimeMillis();
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
                    data.put("appuserinfo", record);
                }
            }
        }
        // }
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 申请预约点，在预约时长界面选择时长，点击确定，调用此接口
     * 
     * @return
     */
    public String startappointment() {
        userkey = decrypt(this.getAttribute("userkey"));
        String pileid = this.getAttribute("pileid");
        String applen = this.getAttribute("applen");
        String appfee = this.getAttribute("appfee");

        if (AppTool.isNotNumber(userkey, pileid, applen, appfee)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
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

        // 请求缓存桩状态
        PileStatusBean pileStatusBean = ChargingCacheUtil.getPileStatusBean(Integer.valueOf(pileid));
        PobChargingPile pile = CacheChargeHolder.getChargePileById(pileStatusBean.getStationid(), pileStatusBean.getId());
        if (pile == null || StringUtil.isEmpty(pile.getComAddr())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }

        Date starttime;
        Date endtime;
        synchronized (pile) {
            pileStatusBean = ChargingCacheUtil.getPileStatusBean(Integer.valueOf(pileid));
            // 判断桩是否可以预约
            if (!isMakeAppointment(pileStatusBean, false)) {
                return SUCCESS;
            }

            starttime = new Date();
            endtime = DateUtil.addMinutes(starttime, Integer.valueOf(applen));

            // 修改充电桩的预约状态为：预约中
            pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_REQUEST);
            pileStatusBean.setUserid(Integer.valueOf(userkey));
            pileStatusBean.setAppendtime(endtime);
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);
        }

        // 产生预约号：年（两位）+月（两位）+日（两位）+时（两位）+分（两位)）+序号（2位)
        Date now = new Date();
        String comaddr = StringUtil.AddjustLength(pile.getComAddr(), 16, "0");
        int num = ChargingCacheUtil.getChargeNo(pileid);
        String appNo = comaddr + DateUtil.DateToNosec10Str(now) + StringUtil.zeroPadString(num + "", 6);

        // 发送前置消息
        System.out.println("sendtime" + DateUtil.DateToLongStr(new Date()));
        try {
            MsgAppointmentReq msg = new MsgAppointmentReq(Integer.valueOf(pileid), Integer.valueOf(userkey), busUser.getPhone(), endtime, appNo);
            SendResult result = mqMsgProduct.sendMQMsg(msg);
            logger.info("开始预约--------userkey=" + userkey + ",phone=" + busUser.getPhone() + ",appNo=" + appNo);
            logger.info("缓存用户--------userid=" + pileStatusBean.getUserid() + "");
            if (result == null) {
                pileStatusBean.setAppstatus(PileAppStatusEnum.UNORDERED);
                pileStatusBean.setUserid(0);
                pileStatusBean.setApprecordid(null);
                pileStatusBean.setAppendtime(null);
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
                resultBean.setMessage("用户输入参数非法!");
                return SUCCESS;
            }
            logger.info("发送预约命令---------appNo=" + appNo + ",pileid=" + pileid + ",userid=" + userkey + ",endtime=" + DateUtil.DateToLongStr(endtime) + ",sendstatus="
                        + result.getSendStatus() + ",msgid=" + result.getMsgId());
        } catch (Exception e) {
            pileStatusBean.setAppstatus(PileAppStatusEnum.UNORDERED);
            pileStatusBean.setUserid(0);
            pileStatusBean.setApprecordid(null);
            pileStatusBean.setAppendtime(null);
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);

            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);// 10003: 系统调用异常，请稍后重试
            resultBean.setMessage("系统调用异常，请稍后重试!");
            return SUCCESS;
        }

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
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
                    resultBean.setMessage("无此充电桩!");
                    return SUCCESS;
                }

                PileAppStatusEnum status = pileStatusBean.getAppstatus();
                if (PileAppStatusEnum.ORDER_SUCCESS == status && Integer.valueOf(userkey).equals(pileStatusBean.getUserid())) { // 预约成功
                    queryResult = true;
                    break;
                } else if (PileAppStatusEnum.ORDER_FAILED == status) { // 失败
                    pileStatusBean.setAppstatus(PileAppStatusEnum.UNORDERED);
                    pileStatusBean.setUserid(0);
                    pileStatusBean.setApprecordid(null);
                    pileStatusBean.setAppendtime(null);
                    ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_PILEERR);// 5: 充电桩预约失败
                    resultBean.setMessage("充电桩预约失败!");
                    return SUCCESS;
                }
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            pileStatusBean.setAppstatus(PileAppStatusEnum.UNORDERED);
            pileStatusBean.setUserid(0);
            pileStatusBean.setApprecordid(null);
            pileStatusBean.setAppendtime(null);
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);

            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);// 10003: 系统调用异常，请稍后重试
            resultBean.setMessage("系统调用异常，请稍后重试!");
            return SUCCESS;
        }

        // 预约成功
        if (queryResult) {
            BusAppointment apprecord = new BusAppointment();
            apprecord.setUserId(Integer.valueOf(userkey));
            apprecord.setPileId(Integer.valueOf(pileid));
            apprecord.setAppNo(appNo);
            apprecord.setStartTime(starttime);
            apprecord.setEndTime(endtime);
            apprecord.setAppLen(Integer.valueOf(applen));
            apprecord.setAppFee(new BigDecimal(appfee));
            apprecord.setAppStatus(AppointmentStatusEnum.ORDERING.getShortValue());
            apprecord.setPayStatus(AppointmentPayStatusEnum.UNPAID.getShortValue());
            apprecord.setAddTime(new Date());
            if (appointmentService.insertAppointmentSelective(apprecord) > 0) {
                // 记录预约记录id
                pileStatusBean.setApprecordid(apprecord.getId());
                pileStatusBean.setAppendtime(endtime);
                pileStatusBean.setUserid(Integer.valueOf(userkey));
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                // 返回预约记录信息和账户余额信息
                appointmentsuccess(apprecord);
            }
        } else { // 超时或失败
            pileStatusBean.setAppstatus(PileAppStatusEnum.UNORDERED);
            pileStatusBean.setUserid(0);
            pileStatusBean.setApprecordid(null);
            pileStatusBean.setAppendtime(null);
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);

            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_PILEERR);// 5: 充电桩预约失败
            resultBean.setMessage("充电桩预约失败!");
            logger.info("--------[userid:" + userkey + ",pileid:" + pileid + ",appNo:" + appNo + "]--------超时，开始预约失败---------");
            return SUCCESS;
        }
        return SUCCESS;
    }

    /**
     * 预约终止
     * 
     * @return
     */
    public String endappointment() {
        userkey = decrypt(this.getAttribute("userkey"));
        String appointmentid = this.getAttribute("appointmentid");

        if (AppTool.isNotNumber(userkey, appointmentid)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        BusUser busUser = userService.selectUserByCache(Integer.valueOf(userkey));
        if (busUser == null || AppTool.isNull(busUser.getPhone())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        BusAppointment record = appointmentService.selectAppointmentByPK(Integer.valueOf(appointmentid));
        if (record == null || !record.getUserId().equals(Integer.valueOf(userkey))) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDAPPO_NOAPPO);// 2: 当前用户未预约
            resultBean.setMessage("当前用户未预约!");
            logger.info("当前用户未预约test1!" + DateUtil.DateToLongStr(new Date()));
            return SUCCESS;
        }

        if (AppointmentStatusEnum.OVERDUE.getShortValue().equals(record.getAppStatus())) {// 过时
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDAPPO_OVERDUE);// 3: 预约已经过期
            resultBean.setMessage("预约已经过期!");
            return SUCCESS;
        } else if (AppointmentStatusEnum.CANCEL.getShortValue().equals(record.getAppStatus()) || AppointmentStatusEnum.EXECUTED.getShortValue().equals(record.getAppStatus())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDAPPO_NOAPPO);// 2: 当前用户未预约
            resultBean.setMessage("当前用户未预约!");
            logger.info("当前用户未预约test2!" + DateUtil.DateToLongStr(new Date()));
            return SUCCESS;
        } else if (AppointmentStatusEnum.ORDERING.getShortValue().equals(record.getAppStatus())) {// 预约中
            if (new Date().compareTo(record.getEndTime()) >= 0) {// 根据截止时间判断是否过期
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDAPPO_OVERDUE);// 3: 预约已经过期
                resultBean.setMessage("预约已经过期!");
                return SUCCESS;
            }

            // 请求缓存桩状态
            PileStatusBean pileStatusBean = ChargingCacheUtil.getPileStatusBean(record.getPileId());
            if (pileStatusBean == null) {
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
                resultBean.setMessage("无此充电桩!");
                return SUCCESS;
            }

            // 如果缓存中预约状态不为预约中，则预约记录的预约状态直接更新为取消
            // if (PileAppStatusEnum.ORDER_SUCCESS != pileStatusBean.getAppstatus()) {
            // logger.info("--------[userid:" + userkey + ",appointmentid:" + appointmentid +
            // "]--------缓存中充电桩状态不为预约中，则不向前置下发取消预约命令，直接更新数据库预约记录的状态并返回取消成功---------");
            // BusAppointment apprecord = new BusAppointment();
            // apprecord.setId(Integer.valueOf(appointmentid));
            // apprecord.setAppStatus(AppointmentStatusEnum.CANCEL.getShortValue());
            // if (appointmentService.updateAppointmentByPKSelective(apprecord) > 0) {
            // pileStatusBean.setAppstatus(PileAppStatusEnum.UNORDERED);
            // pileStatusBean.setUserid(0);
            // pileStatusBean.setApprecordid(null);
            // pileStatusBean.setAppendtime(null);
            // ChargingCacheUtil.setPileStatusBean(pileStatusBean);
            // }
            // return SUCCESS;
            // }

            // 修改状态为请求终止预约
            logger.info("appstatus=" + pileStatusBean.getAppstatus().getValue() + "," + userkey + "=" + pileStatusBean.getUserid());
            if (PileAppStatusEnum.ORDER_SUCCESS == pileStatusBean.getAppstatus() && Integer.valueOf(userkey).equals(pileStatusBean.getUserid())) {
                pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_REQUEST);
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);
            }

            // 发送前置消息
            try {
                MsgAppointmentCancle msg = new MsgAppointmentCancle(record.getPileId(), record.getUserId(), busUser.getPhone(), record.getAppNo());
                SendResult result = mqMsgProduct.sendMQMsg(msg);
                if (result == null) {
                    pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                    ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
                    resultBean.setMessage("用户输入参数非法!");
                    return SUCCESS;
                }
                logger.info("发送取消预约命令---------appNo=" + record.getAppNo() + ",pileid=" + record.getPileId() + ",userid" + record.getUserId() + ",sendstatus="
                            + result.getSendStatus() + ",msgid=" + result.getMsgId());
            } catch (Exception e) {
                pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);// 10003: 系统调用异常，请稍后重试
                resultBean.setMessage("系统调用异常，请稍后重试!");
                return SUCCESS;
            }

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
                    pileStatusBean = ChargingCacheUtil.getPileStatusBean(record.getPileId());
                    if (pileStatusBean == null) {
                        resultBean.setSuccess(false);
                        resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
                        resultBean.setMessage("无此充电桩!");
                        return SUCCESS;
                    }

                    PileAppStatusEnum status = pileStatusBean.getAppstatus();
                    if (PileAppStatusEnum.UNORDERED == status) { // 未预约
                        queryResult = true;
                        break;
                    } else if (PileAppStatusEnum.ORDER_FAILED == status) { // 取消失败，则改为原来的预约中
                        pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                        ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                        resultBean.setSuccess(false);
                        resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDAPPO_PILEERR);// 4: 取消预约失败
                        resultBean.setMessage("取消预约失败!");
                        return SUCCESS;
                    }
                    Thread.sleep(500);
                }

            } catch (InterruptedException e) {
                pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);// 10003: 系统调用异常，请稍后重试
                resultBean.setMessage("系统调用异常，请稍后重试!");
                return SUCCESS;
            }
            // 取消成功缴费
            if (queryResult) {
                // 预约记录的预约状态更新为取消
                BusAppointment apprecord = new BusAppointment();
                apprecord.setId(Integer.valueOf(appointmentid));
                apprecord.setAppStatus(AppointmentStatusEnum.CANCEL.getShortValue());
                if (appointmentService.updateAppointmentByPKSelective(apprecord) > 0) {
                    // pileStatusBean.setAppstatus(PileAppStatusEnum.UNORDERED);
                    pileStatusBean.setUserid(0);
                    pileStatusBean.setApprecordid(null);
                    pileStatusBean.setAppendtime(null);
                    ChargingCacheUtil.setPileStatusBean(pileStatusBean);
                }
            } else { // 超时或失败
                pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDAPPO_PILEERR);// 4: 取消预约失败
                resultBean.setMessage("取消预约失败!");
                logger.info("--------[userid:" + userkey + ",appointmentid:" + appointmentid + "]--------超时，取消预约失败---------");
                return SUCCESS;
            }
        }
        return SUCCESS;
    }

    /**
     * 续约
     * 
     * @return
     */
    public String againappointment() {
        userkey = decrypt(this.getAttribute("userkey"));
        String appointmentid = this.getAttribute("appointmentid");
        String applen = this.getAttribute("applen");
        String appfee = this.getAttribute("appfee");

        if (AppTool.isNotNumber(userkey, appointmentid, applen, appfee)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }

        BusAppointment record = appointmentService.selectAppointmentByPK(Integer.valueOf(appointmentid));
        if (record == null || record.getAppStatus() == null || !record.getAppStatus().equals(AppointmentStatusEnum.ORDERING.getShortValue())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_UNAPP);// 6: 用户未预约
            resultBean.setMessage("您未预约!");
            return SUCCESS;
        }

        if (record.getEndTime() != null && new Date().compareTo(record.getEndTime()) > 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_OVERDUE);// 7:当前预约已经过期
            resultBean.setMessage("当前预约已经过期，请重新预约！");
            return SUCCESS;
        }

        Integer pileid = record.getPileId();
        // 请求缓存桩状态
        PileStatusBean pileStatusBean = ChargingCacheUtil.getPileStatusBean(pileid);
        // 判断桩是否可以预约
        if (!isMakeAppointment(pileStatusBean, true)) {
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

        // 修改充电桩的预约状态为：预约中
        pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_REQUEST);
        pileStatusBean.setUserid(Integer.valueOf(userkey));
        ChargingCacheUtil.setPileStatusBean(pileStatusBean);

        Date starttime = record.getEndTime();
        Date endtime = DateUtil.addMinutes(starttime, Integer.valueOf(applen));

        // 产生预约号：年（两位）+月（两位）+日（两位）+时（两位）+分（两位)）+序号（2位)
        PobChargingPile pile = CacheChargeHolder.getChargePileById(pileStatusBean.getStationid(), pileStatusBean.getId());
        if (pile == null || StringUtil.isEmpty(pile.getComAddr())) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
            resultBean.setMessage("无此充电桩!");
            return SUCCESS;
        }
        Date now = new Date();
        String comaddr = StringUtil.AddjustLength(pile.getComAddr(), 16, "0");
        int num = ChargingCacheUtil.getChargeNo(pileid.toString());
        String appNo = comaddr + DateUtil.DateToNosec10Str(now) + StringUtil.zeroPadString(num + "", 6);

        // 发送前置消息
        System.out.println("sendtime" + DateUtil.DateToLongStr(new Date()));
        try {
            MsgAppointmentReq msg = new MsgAppointmentReq(Integer.valueOf(pileid), Integer.valueOf(userkey), busUser.getPhone(), endtime, appNo);
            SendResult result = mqMsgProduct.sendMQMsg(msg);
            if (result == null) {
                pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                pileStatusBean.setUserid(Integer.valueOf(userkey));
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);// 10004: 用户输入参数非法
                resultBean.setMessage("用户输入参数非法!");
                return SUCCESS;
            }
            logger.info("发送续约命令------appNo=" + appNo + ",pileid=" + pileid + ",userid=" + userkey + ",endtime=" + DateUtil.DateToLongStr(endtime) + ",sendstatus="
                        + result.getSendStatus() + ",msgid=" + result.getMsgId());
        } catch (Exception e) {
            pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
            pileStatusBean.setUserid(Integer.valueOf(userkey));
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);
            return SUCCESS;
        }

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
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
                    resultBean.setMessage("无此充电桩!");
                    return SUCCESS;
                }

                PileAppStatusEnum status = pileStatusBean.getAppstatus();
                if (PileAppStatusEnum.ORDER_SUCCESS == status) { // 预约成功
                    queryResult = true;
                    break;
                } else if (PileAppStatusEnum.ORDER_FAILED == status) { // 失败
                    pileStatusBean.setAppstatus(PileAppStatusEnum.UNORDERED);
                    pileStatusBean.setUserid(0);
                    ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_PILEERR);// 5: 充电桩预约失败
                    resultBean.setMessage("充电桩预约失败!");
                    return SUCCESS;
                }
                Thread.sleep(500);
            }

        } catch (InterruptedException e) {
            pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
            pileStatusBean.setUserid(Integer.valueOf(userkey));
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);

            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_SYSERR);// 10003: 系统调用异常，请稍后重试
            resultBean.setMessage("系统调用异常，请稍后重试!");
            return SUCCESS;
        }

        // 预约成功缴费
        if (queryResult) {
            BusAppointment apprecord = new BusAppointment();
            apprecord.setUserId(Integer.valueOf(userkey));
            apprecord.setPileId(Integer.valueOf(pileid));
            apprecord.setAppNo(appNo);
            apprecord.setStartTime(starttime);
            apprecord.setEndTime(endtime);
            apprecord.setAppLen(Integer.valueOf(applen));
            apprecord.setAppFee(new BigDecimal(appfee));
            apprecord.setAppStatus(AppointmentStatusEnum.ORDERING.getShortValue());
            apprecord.setPayStatus(AppointmentPayStatusEnum.UNPAID.getShortValue());
            apprecord.setAddTime(new Date());
            if (appointmentService.insertRenewAppointment(apprecord, Integer.valueOf(appointmentid)) > 0) {
                // 记录预约记录id
                pileStatusBean.setApprecordid(apprecord.getId());
                pileStatusBean.setAppendtime(endtime);
                pileStatusBean.setUserid(Integer.valueOf(userkey));
                ChargingCacheUtil.setPileStatusBean(pileStatusBean);

                // 返回预约记录信息和账户余额信息
                appointmentsuccess(apprecord);
            }
        } else { // 超时或失败
            pileStatusBean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
            pileStatusBean.setUserid(Integer.valueOf(userkey));
            ChargingCacheUtil.setPileStatusBean(pileStatusBean);

            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_PILEERR);// 5: 充电桩预约失败
            resultBean.setMessage("充电桩预约失败!");
            logger.info("--------[userid:" + userkey + ",pileid:" + pileid + ",appNo:" + appNo + "]--------超时，续约失败---------");
            return SUCCESS;
        }

        return SUCCESS;
    }

    /**
     * @param pileStatusBean
     * @param isagain:是否续约
     * @return
     */
    private boolean isMakeAppointment(PileStatusBean pileStatusBean, boolean isagain) {

        // 请求缓存桩状态
        if (pileStatusBean == null) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_NOPILE);// 20002: 无此充电桩
            resultBean.setMessage("无此充电桩!");
            return false;
        }

        PileStatusEnum pileStatus = pileStatusBean.getStatus();
        PileAppStatusEnum appStatus = pileStatusBean.getAppstatus();
        Integer userid = Integer.valueOf(userkey);
        if (PileStatusEnum.OFFLINE == pileStatus || PileStatusEnum.FAULT == pileStatus) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PILEOFFLINE);// 20000: 充电桩离线/故障
            resultBean.setMessage("充电桩离线/故障!");
            return false;
        } else if (PileStatusEnum.CHARGING == pileStatus) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_USING);// 3: 充电桩正在使用中
            resultBean.setMessage("充电桩正在使用中!");
            return false;
        } else if (PileStatusEnum.BUSYING == pileStatus) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_PILESTATUS_BUSYING);// 20003: 充电桩忙碌中(桩屏幕正在被操作等原因)
            resultBean.setMessage("充电桩忙碌中!");
            return false;
        } else if (PileAppStatusEnum.UNORDERABLE == appStatus) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_UNORDERABLE);// 8: 此桩不可预约
            resultBean.setMessage("此桩不可预约!");
            return false;
        } else if (PileAppStatusEnum.ORDER_REQUEST == appStatus || PileAppStatusEnum.ORDER_SUCCESS == appStatus || PileAppStatusEnum.ORDER_FAILED == appStatus) {
            if (pileStatusBean.getUserid() != null && pileStatusBean.getUserid().intValue() > 0 && pileStatusBean.getAppendtime() != null
                && new Date().compareTo(pileStatusBean.getAppendtime()) < 0) {// 截止时间判断是否过期
                if (userid.equals(pileStatusBean.getUserid())) {
                    if (!isagain) {// 已经预约的用户，只能续约，不能重复预约
                        resultBean.setSuccess(false);
                        resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_HAVEAPPO);// 1: 当前用户已经预约
                        resultBean.setMessage("当前用户已经预约!");
                        logger.info("------------当前用户已经预约-----------");
                        return false;
                    }
                } else {
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_OTHERUSERAPPO);// 2: 已经被其他用户预约
                    resultBean.setMessage("充电桩已经被其他用户预约!");
                    logger.info("缓存中预约状态为" + appStatus.getText() + "------------充电桩已经被其他用户预约-----------");
                    return false;
                }
            }
        } else if (PileStatusEnum.IDLE == pileStatus || PileStatusEnum.FINISH == pileStatus) {
            Integer scanuserid = pileStatusBean.getScanuserid();
            Date scantime = pileStatusBean.getScantime();
            if (scanuserid != null && scantime != null && !userid.equals(scanuserid) && !AppTool.isTimeOut(scantime.getTime(), Globals.TIMEOUT_MIN_5 * 60)) {// 若充电现场已有人扫码待充电
                resultBean.setSuccess(false);
                resultBean.setErrorCode(ErrorCodeConstants.ERR_SCAN_OTHERSCAN);// 20010: 其他用户已经扫码待充电
                resultBean.setMessage("其他用户已经扫码待充电!");
                return false;
            }
        }

        // 查看用户是否已预约其他桩
        if (!queryOtherAppointment(pileStatusBean.getId(), isagain)) {
            return false;
        }
        // 查看费用是否缴清
        if (!queryUnpaidFee(userkey)) {
            return false;
        }

        return true;
    }

    /**
     * 查看用户是否已预约其他桩
     * 
     * @param pileid
     * @return
     */
    private boolean queryOtherAppointment(Integer pileid, boolean isagain) {
        BusAppointmentExample emp = new BusAppointmentExample();
        BusAppointmentExample.Criteria cr = emp.createCriteria();
        cr.andUserIdEqualTo(Integer.valueOf(userkey));
        cr.andAppStatusEqualTo(AppointmentStatusEnum.ORDERING.getShortValue());
        cr.andEndTimeGreaterThanOrEqualTo(new Date());
        List<BusAppointment> orderList = appointmentService.selectAppointmentByExample(emp);
        if (orderList != null && orderList.size() > 0) {
            for (BusAppointment record : orderList) {
                if (pileid.equals(record.getPileId())) {// 当前用户已经预约
                    if (!isagain) {// 若当前用户已经预约，则只能走续约，不能再预约
                        resultBean.setSuccess(false);
                        resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_HAVEAPPO);// 1: 当前用户已经预约
                        resultBean.setMessage("当前用户已经预约!");
                        logger.info("------------当前用户已经预约2-----------");
                        return false;
                    }
                } else {// 当前用户已经预约其他桩
                    resultBean.setSuccess(false);
                    resultBean.setErrorCode(ErrorCodeConstants.ERR_MAKEAPPO_OTHERPILEAPPO);// 4: 用户已经预约其他桩
                    resultBean.setMessage("用户已经预约其他桩!");
                    return false;
                }
            }
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

            data.put("payment", info);
            resultBean.setData(data);
            return false;
        }
        return true;
    }

    // 查询APP开始预约界面费用信息
    private AppointmentFeeInfo queryAppointmentFeeInfo(Integer pileid) {
        AppointmentFeeInfo feeInfo = new AppointmentFeeInfo();
        feeInfo.setPileid(Integer.valueOf(pileid));

        PobChargingPile pile = CacheChargeHolder.getChargePileById(Integer.valueOf(pileid));
        if (pile != null) {
            feeInfo.setPilename(pile.getPileName());
            feeInfo.setChawaydesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.CHA_WAY.getValue(), pile.getChaWay().toString()));
            feeInfo.setPiletypedesc(CacheSysHolder.getSysLinkName(LinkTypeEnum.PILE_TYPE.getValue(), pile.getChaWay().toString()));

            // 查询桩型号信息
            BusPileModel pileModel = CacheChargeHolder.selectPileModelById(pile.getPileModel());
            if (pileModel != null) {
                feeInfo.setRatp(pileModel.getRatP() == null ? "" : pileModel.getRatP());
                feeInfo.setOutv(pileModel.getOutV() == null ? "" : pileModel.getOutV());
            }
        }

        // 预约费
        String orderfee = RoleUtil.selectRuleByPrimaryKey(RoleUtil.APPOINTMENT_FEE);
        if (StringUtil.isNotEmpty(orderfee)) {
            feeInfo.setAppointmentfee(orderfee);
        }

        // 充电费、停车费、服务费
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pileId", pileid);
        params.put("status", WhetherEnum.YES.getShortValue());// 生效
        params.put("active", WhetherEnum.YES.getShortValue());// 已使用
        ChargeRuleModel ruleModel = deviceService.selectChargeRuleModelByMap(params);
        if (ruleModel == null) {
            return feeInfo;
        }
        if (Globals.CHARGE_RULE_SINGLE == ruleModel.getId().intValue()) {
            feeInfo.setChargefee(NumberUtil.formateScale2Str(ruleModel.getChargeFee()));
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
            feeInfo.setChargefee(ruleDetail);
        }
        feeInfo.setParkfee(ruleModel.getParkFee() == null ? "" : NumberUtil.formateScale2Str(ruleModel.getParkFee()));
        feeInfo.setServicefee(ruleModel.getServiceFee() == null ? "" : NumberUtil.formateScale2Str(ruleModel.getServiceFee()));
        return feeInfo;
    }

    /**
     * 查询用户是否有正在预约中的记录
     * 
     * @return
     */
    public String queryAppointment() {
        userkey = decrypt(userkey);
        if (!NumberUtils.isNumber(userkey)) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_G_PARAERR);
            resultBean.setMessage("用户输入参数非法!");
            return SUCCESS;
        }
        BusAppointmentExample emp = new BusAppointmentExample();
        BusAppointmentExample.Criteria cr = emp.createCriteria();
        cr.andUserIdEqualTo(Integer.valueOf(userkey));
        cr.andAppStatusEqualTo(AppointmentStatusEnum.ORDERING.getShortValue());
        cr.andEndTimeLessThan(new Date());
        List<BusAppointment> list = appointmentService.selectAppointmentByExample(emp);
        if (list == null || list.size() == 0) {
            resultBean.setSuccess(false);
            resultBean.setErrorCode(ErrorCodeConstants.ERR_ENDAPPO_NOAPPO);// 当前用户未预约
            resultBean.setMessage("当前用户未预约!");
            return SUCCESS;
        }
        BusAppointment appointment = list.get(0);
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("appointment", appointment);
        resultBean.setData(data);
        return SUCCESS;
    }

    /**
     * 预约成功后返回的数据
     * 
     * @param record
     */
    public void appointmentsuccess(BusAppointment record) {
        AppointmentInfo info = new AppointmentInfo();
        info.setAddtime(record.getAddTime());
        info.setAppfee(NumberUtil.formateScale2(record.getAppFee()));
        info.setApplen(record.getAppLen());
        info.setEndtime(record.getEndTime());
        info.setId(record.getId());
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
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("appointment", info);

        BusAccount account = accountService.selectAccoutByPrimaryKey(Integer.valueOf(userkey));
        if (account != null) {
            account.setUserId(null);
            data.put("account", account);
            resultBean.setData(data);
        }
    }

    public ResultBean getResultBean() {
        return resultBean;
    }

    public void setAppointmentService(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    public void setChargingService(ChargingService chargingService) {
        this.chargingService = chargingService;
    }

    public void setDeviceService(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    public void setMqMsgProduct(MQMsgProduct mqMsgProduct) {
        this.mqMsgProduct = mqMsgProduct;
    }

}
