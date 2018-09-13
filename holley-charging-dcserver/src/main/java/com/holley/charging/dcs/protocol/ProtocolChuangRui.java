package com.holley.charging.dcs.protocol;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.alibaba.druid.util.StringUtils;
import com.holley.charging.dcs.dao.model.Account;
import com.holley.charging.dcs.dao.model.AppointmentRec;
import com.holley.charging.dcs.dao.model.ChargeRecord;
import com.holley.charging.dcs.dao.model.DcsHisYc;
import com.holley.charging.dcs.dao.model.PayMentRec;
import com.holley.charging.dcs.dao.model.PileFeeModel;
import com.holley.charging.dcs.dao.model.User;
import com.holley.charging.dcs.service.channel.ChannelService;
import com.holley.charging.dcs.service.device.PileDev;
import com.holley.charging.dcs.service.device.PileService;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.app.ErrorCodeConstants;
import com.holley.common.constants.charge.AppointmentStatusEnum;
import com.holley.common.constants.charge.ChargeDealStatusEnum;
import com.holley.common.constants.charge.ChargePayStatusEnum;
import com.holley.common.constants.charge.PileAppStatusEnum;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.rocketmq.charging.MsgAppointmentCancle;
import com.holley.common.rocketmq.charging.MsgAppointmentReq;
import com.holley.common.rocketmq.charging.MsgChargeStart;
import com.holley.common.rocketmq.charging.MsgChargeStop;
import com.holley.common.util.ProtocolUtils;

public class ProtocolChuangRui extends BaseProtocol implements IProtocol {

    static Log                      logger            = LogFactory.getLog(ProtocolChuangRui.class.getName());

    public static ProtocolChuangRui protocolChuangRui = new ProtocolChuangRui();

    private final byte              TYPE_M_SP_NA_1    = 1;
    private final byte              TYPE_M_ME_NB_1    = 11;
    private final byte              TYPE_M_IT_NA_1    = 15;
    private final byte              TYPE_M_RE_NA_1    = (byte) 130;
    private final byte              TYPE_M_MD_NA_1    = (byte) 132;
    private final byte              TYPE_M_JC_NA_1    = (byte) 134;
    private final byte              TYPE_C_IC_NA_1    = 100;
    private final byte              TYPE_C_CI_NA_1    = 101;
    private final byte              TYPE_C_CS_NA_1    = 103;
    private final byte              TYPE_C_SD_NA_1    = (byte) 133;

    private final byte              U_T_STARTDT       = 0x07;
    private final byte              U_T_TEST          = 0x43;

    private final int               T0_104            = 20;                                                  // t0：连接建立的超时时间，默认
                                                                                                              // 20 秒；
    private final int               T1_104            = 15;                                                  // t1：发送或测试
                                                                                                              // APDU
                                                                                                              // 的超时时间，默认
                                                                                                              // 15 秒；
    private final int               T2_104            = 10;                                                  // t2：无数据报文确认的超时时间，默认
                                                                                                              // 10 秒；
    private final int               T3_104            = 20;                                                  // t3：长期空闲状态下发送测试帧的超时时间，默认
                                                                                                              // 20 秒
    private final int               T4_heart          = 180;                                                 // 心跳间隔，超过此间隔没收到心跳，重新连接，这里的冗余量比较大
    private final int               K_104_MAX         = 9;                                                   // K:发送方未被确认的
                                                                                                              // I
                                                                                                              // 格式帧的最大数目
    private int                     K_104             = 0;                                                   // 当前发送方未被确认的
                                                                                                              // I
                                                                                                              // 格式帧的数目
    private final int               W_104_MAX         = 6;                                                   // W:接收方最多收到未给确认的
                                                                                                              // I
                                                                                                              // 格式帧的最大数目
    private final int               FAIL_NUM          = 8;
    private int                     W_104             = 0;                                                   // 当前接收方收到未给确认的
                                                                                                              // I
                                                                                                              // 格式帧的数目

    private Date                    lastRecvTime      = Calendar.getInstance().getTime();
    private Date                    lastSendTime      = Calendar.getInstance().getTime();

    private Date                    lastSaveTime      = Calendar.getInstance().getTime();

    private boolean                 needConfirm       = false;
    // 发送接收序列号
    private short                   N_S               = 0;
    private short                   N_R               = 0;

    private boolean                 needClock         = true;
    private Calendar                chargeCmdTime     = Calendar.getInstance();
    private short                   pubAddr;
    private int                     resnum            = 0;

    public PileService registerProtocol(byte[] buffer, ChannelService channel) {
        if (buffer == null || buffer.length < 15) {
            return null;
        }
        int index = 0;
        for (index = 0; index <= buffer.length - 15; index++) {
            /*
             * 起始标识 1Byte BIN码 固定68H 长度 2Byte BIN码 固定为”0x0C 0x00” 启动帧标识 1Byte BIN码 固定为 FF 协议版本 1Byte 压缩BCD码 保留为02 or 03
             * 设备编号 8Byte 压缩BCD码 充电设备编号,16 位编码,如果集中器(此处填全 0) 站地址 2Byte 压缩BCD码 站地址
             */
            if (buffer[index] == 0x68 && buffer[index + 1] == 0x0C && buffer[index + 2] == 0x0 && ProtocolUtils.Unsignd(buffer[index + 3]) == 0xFF
                && (buffer[index + 4] == 0x02 || buffer[index + 4] == 0x03)) { // 注册帧
                // 设备编号
                String devaddr = ProtocolUtils.getByteToHexStringDesc(buffer, index + 5, 8, "");
                PileService pileService = channel.linkPile(devaddr);
                if (pileService == null) {
                    // channel.closeChannel(); //非法桩连入
                    continue;
                }

                byte[] reply = new byte[15];
                System.arraycopy(buffer, index, reply, 0, 15);
                channel.setReadDealPtr(channel.getReadDealPtr() + index + 15);
                pileService.notifyLogin(reply);
                return pileService;
            }
        }
        channel.setReadDealPtr(channel.getReadDealPtr() + index);
        return null;
    }

    @Override
    public int onLogin(byte[] data, PileService pileService, ChannelService channel) {

        // 注册后初始化
        N_S = 0;
        N_R = 0;

        lastRecvTime = Calendar.getInstance().getTime();
        lastSendTime = Calendar.getInstance().getTime();

        needConfirm = false;

        if (data != null && data.length >= 15) {
            // 站地址
            pubAddr = ProtocolUtils.byteToShort(data[14], data[13]);
        }
        // 注册成功,发送U帧
        return writeData_I(channel, makeFrame_U(U_T_STARTDT));
    }

    public int onReceive(byte[] msg, PileService pileService, ChannelService channel) {
        int eopLen = 7;
        if (msg == null || msg.length < eopLen) {
            return 0;
        }
        int dealLen = 0;
        int recvLen = msg.length;
        while (recvLen - dealLen > 0) {
            if (msg[dealLen] != 0x68) {
                dealLen++;
                continue;
            }
            if (recvLen - dealLen < eopLen) {
                return dealLen;
            }
            APCI apci = new APCI(msg, dealLen);
            if (resnum >= FAIL_NUM) {// 解析8次解析不了丢弃
                resnum = 0;
                logger.warn(pileService.getCommAddr() + "--解析不正确的数据超8次--" + ProtocolUtils.printMsg(0, msg.length, msg));
                return dealLen + 1;
            } else {
                resnum++;
            }
            if (apci.length + 3 > recvLen - dealLen) { // 长度不够
                if (apci.length > 2047) { // 非法长度
                    dealLen++;
                    continue;
                }
                resnum++;
                return dealLen;
            }
            dealLen += 3;
            lastRecvTime = Calendar.getInstance().getTime();
            needConfirm = false;
            if (apci.length == 4) { // 无ASDU数据，U帧、S帧

                if (msg[dealLen] == 0x0B && msg[dealLen + 1] == 0x0 && msg[dealLen + 2] == 0x0 && msg[dealLen + 3] == 0x0) { // STARTDT_CONT
                    // 发起总召
                    writeData_I(channel, makeIFrame(TYPE_C_IC_NA_1, 0, null));

                } else if (msg[dealLen] == 0x43 && msg[dealLen + 1] == 0x0 && msg[dealLen + 2] == 0x0 && msg[dealLen + 3] == 0x0) { // TEST
                    // 确认测试
                    byte[] U_TEST = { 0x68, 0x04, 0x00, (byte) 0x83, 0x00, 0x00, 0x00 };
                    channel.writeData(U_TEST);
                }
            } else if (msg[dealLen - 3] == 0x68 && msg[dealLen - 2] == 0x0C && msg[dealLen - 1] == 0x0 && ProtocolUtils.Unsignd(msg[dealLen]) == 0xFF
                       && (msg[dealLen + 1] == 0x02 || msg[dealLen + 1] == 0x03)) { // 重复注册帧
                writeData_I(channel, makeFrame_U(U_T_STARTDT));
            } else {

                ASDU asdu = new ASDU(msg, dealLen, apci.length);
                if (N_R != apci.ns) { // 出现漏帧或重复帧，关闭现在的 TCP 连接
                    // closeConnect(pileService, channel);
                }
                N_R++;
                if (asdu.infoList != null && asdu.infoList.size() > 0) {
                    W_104++;

                    switch (asdu.type) {
                        case TYPE_M_RE_NA_1:
                            TYPE130 type_130 = (TYPE130) asdu.infoList.get(0).data;
                            switch (type_130.type) {
                                case BizTypeUp.ReqFeeModel:
                                    BizUpReqFeeModel req = (BizUpReqFeeModel) type_130.value;
                                    if (req != null) {
                                        PileFeeModel pfm = pileService.getUpdatePFM(req.lastTime.getTime(), 1);
                                        if (pfm != null) {
                                            BizDownFeeModel bdfm = new BizDownFeeModel(pileService.getPile(0), pfm);
                                            writeData_I(channel, makeIFrame(TYPE_C_SD_NA_1, 0, bdfm));
                                            pileService.setbNeedDownFeeModel(false);
                                            break;
                                        }
                                    }
                                    writeData_S(channel, makeFrame_S());
                                    break;
                                case BizTypeUp.AppointmentLock: {
                                    BizUpAppointmentLock al = (BizUpAppointmentLock) type_130.value;
                                    PileStatusBean bean = ChargingCacheUtil.getPileStatusBean(pileService.getPile(al.chargeInterface).getId());
                                    bean.setUpdatetime(Calendar.getInstance().getTime());
                                    if (al.result == 1) {
                                        bean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                                    } else {
                                        bean.setAppstatus(PileAppStatusEnum.ORDER_FAILED);
                                        bean.setErrorcode(ErrorCodeConstants.ERR_MAKEAPPO_PILEERR);
                                    }

                                    ChargingCacheUtil.setPileStatusBean(bean);
                                    pileService.freshMemPileStatus(bean);
                                    writeData_S(channel, makeFrame_S());
                                }
                                    break;
                                case BizTypeUp.AppointmentCancel: {
                                    BizUpAppointmentCancel ac = (BizUpAppointmentCancel) type_130.value;
                                    PileStatusBean bean = ChargingCacheUtil.getPileStatusBean(pileService.getPile(ac.chargeInterface).getId());
                                    bean.setUpdatetime(Calendar.getInstance().getTime());
                                    if (ac.result == 1) {
                                        bean.setAppstatus(PileAppStatusEnum.UNORDERED);
                                    } else {
                                        bean.setAppstatus(PileAppStatusEnum.ORDER_FAILED);
                                        bean.setErrorcode(ErrorCodeConstants.ERR_ENDAPPO_PILEERR);
                                    }
                                    ChargingCacheUtil.setPileStatusBean(bean);
                                    pileService.freshMemPileStatus(bean);
                                    writeData_S(channel, makeFrame_S());
                                }
                                    break;
                                case BizTypeUp.RemoteStartResult: {
                                    BizUpRemoteStartResult rsr = (BizUpRemoteStartResult) type_130.value;
                                    PileStatusBean bean = ChargingCacheUtil.getPileStatusBean(pileService.getPile(rsr.chargeInterface).getId());
                                    bean.setUpdatetime(Calendar.getInstance().getTime());
                                    if (rsr.isSuccess != 1 || rsr.errorInfo != 0) {
                                        chargeCmdTime = Calendar.getInstance();
                                        bean.setErrorcode(ErrorCodeConstants.ERR_STCHA_NOGUN);
                                        ChargingCacheUtil.setPileStatusBean(bean);
                                        pileService.freshMemPileStatus(bean);
                                    }
                                    writeData_S(channel, makeFrame_S());
                                }
                                    break;
                                case BizTypeUp.ChargeEventStart: {
                                    BizUpChargeEventStart ces = (BizUpChargeEventStart) type_130.value;
                                    if (ces.isSuccess == 1) {
                                        // 保存充电记录，以备充电过程中离线
                                        PileStatusBean bean = ChargingCacheUtil.getPileStatusBean(pileService.getPile(ces.chargeInterface).getId());
                                        bean.setUpdatetime(Calendar.getInstance().getTime());
                                        saveChargeRec(pileService, pileService.getPile(ces.chargeInterface), ces, bean);
                                        bean.setStatus(PileStatusEnum.CHARGING);
                                        bean.setAppstatus(PileAppStatusEnum.UNORDERED);
                                        bean.setTradeno(ces.tradeNo);
                                        bean.setPlateNum(ces.plateNum);
                                        bean.setStarttime(ces.timeBegin.getTime());
                                        ChargingCacheUtil.setPileStatusBean(bean);
                                        pileService.freshMemPileStatus(bean);
                                    }
                                    writeData_S(channel, makeFrame_S());
                                }
                                    break;
                                case BizTypeUp.ChargeEventEnd: {
                                    BizUpChargeEventEnd cee = (BizUpChargeEventEnd) type_130.value;
                                    if (cee.isSuccess == 1) {
                                        PileStatusBean bean = ChargingCacheUtil.getPileStatusBean(pileService.getPile(cee.chargeInterface).getId());
                                        bean.setUpdatetime(Calendar.getInstance().getTime());
                                        bean.setStatus(PileStatusEnum.FINISH);
                                        bean.setTradeno(cee.tradeNo);
                                        ChargingCacheUtil.setPileStatusBean(bean);
                                        pileService.freshMemPileStatus(bean);
                                    }
                                    writeData_S(channel, makeFrame_S());
                                }
                                    break;
                                case BizTypeUp.ChargeRec:
                                    BizUpChargeRec rec = (BizUpChargeRec) type_130.value;
                                    saveChargeRec(pileService, pileService.getPile(rec.chargeInterface), rec);
                                    BizDownChargeConfirm cc = new BizDownChargeConfirm(pileService.getPile(rec.chargeInterface), rec.tradeNo);
                                    writeData_I(channel, makeIFrame(TYPE_C_SD_NA_1, 0, cc));
                                    break;
                                case BizTypeUp.ChargeStartByCard:
                                    BizUpChargeStartByCard csc = (BizUpChargeStartByCard) type_130.value;
                                    BizDownChargeStartByCardConfirm cscc = new BizDownChargeStartByCardConfirm(pileService, csc.chargeInterface, csc.cardID);
                                    writeData_I(channel, makeIFrame(TYPE_C_SD_NA_1, 0, cscc));
                                    break;
                                case BizTypeUp.ResultFeeModel:
                                case BizTypeUp.RemoteStopResult:
                                default:
                                    writeData_S(channel, makeFrame_S());
                                    break;
                            }
                            break;
                        case TYPE_M_MD_NA_1:
                            /*
                             * DcsHisYa hisYa = new DcsHisYa(); TYPE132 type_132 = (TYPE132) asdu.infoList.get(0).data;
                             * hisYa.setChargeid(pileService.getPile().getId()); if
                             * (pileService.getPile().getChaWay().equals(ChargeCurrentTypeEnum.DC.getShortValue())) {
                             * hisYa.setDataType(INFO_ADDR_DC.getYXCode(asdu.infoList.get(0).addr)); } else {
                             * hisYa.setDataType(INFO_ADDR_AC.getYXCode(asdu.infoList.get(0).addr)); }
                             * hisYa.setDataTime(Calendar.getInstance().getTime()); int value =
                             * ProtocolUtils.byteToInteger(type_132.value, 0); hisYa.setValue(value);
                             * pileService.getDataBaseService().saveData(hisYa); bean.setChapower(Double.valueOf(value +
                             * "")); pileService.freshMemPileStatus(bean);
                             */
                            break;
                        case TYPE_M_JC_NA_1: {
                            TYPE134 type_134 = (TYPE134) asdu.infoList.get(0).data;
                            if (type_134 != null) {
                                PileDev pile = pileService.getPile(type_134.value.chargeInterface);
                                if (NumberUtils.toInt(pile.getComSubAddr()) != type_134.value.chargeInterface) {
                                    break;
                                }
                                PileStatusBean bean = ChargingCacheUtil.getPileStatusBean(pile.getId());
                                bean.setUpdatetime(Calendar.getInstance().getTime());

                                if (pile.getChaWay().intValue() != type_134.type) {
                                    logger.error("Pile parameters error!!(PileID=" + pile.getId() + ")	Cha_way=" + pile.getChaWay() + ",but type=" + type_134.type);
                                }
                                if (bean.getStatus().equals(PileStatusEnum.FINISH)) {
                                    if (bean.getUpdatetime() != null) {
                                        if ((Calendar.getInstance().getTime().getTime() - bean.getUpdatetime().getTime()) < 300000) {
                                            break;
                                        }
                                    }
                                }

                                if (type_134.type == 1) {
                                    BizACPileMonitor m = (BizACPileMonitor) type_134.value;
                                    bean.setOuti(String.valueOf(m.acOutI));
                                    bean.setOutv(String.valueOf(m.acOutV));
                                    bean.setChalen(m.totalTime);
                                    bean.setChapower(m.chargedEnergy.doubleValue());
                                    bean.setMoney(m.chargedMoney.add(m.serviceMoney));
                                    bean.setChaMoney(m.chargedMoney);
                                    bean.setServiceMoney(m.serviceMoney);
                                    bean.setIsonline((short) 1);
                                    if (m.workstatus == WorkStatusEnum.IDLE.getValue()) {
                                        if (m.connectCar) {
                                            bean.setStatus(PileStatusEnum.BUSYING);
                                        } else {
                                            bean.setStatus(PileStatusEnum.IDLE);
                                        }
                                    } else if (m.workstatus == WorkStatusEnum.WORK.getValue()) {
                                        bean.setStatus(PileStatusEnum.CHARGING);
                                        // 充电过程中才保存
                                        // saveYC_AC(pileService, m);
                                    } else if (m.workstatus == WorkStatusEnum.APPOINTMENT.getValue()) {
                                        bean.setStatus(PileStatusEnum.IDLE);
                                        bean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                                    } else if (m.workstatus == WorkStatusEnum.ERROR.getValue() || m.workstatus == WorkStatusEnum.ERROR_LOWV.getValue()
                                               || m.workstatus == WorkStatusEnum.ERROR_OVERV.getValue() || m.workstatus == WorkStatusEnum.ERROR_OVERI.getValue()) {
                                        bean.setStatus(PileStatusEnum.FAULT);
                                    } else {
                                        if ((Calendar.getInstance().getTimeInMillis() - chargeCmdTime.getTimeInMillis()) > 300000) {
                                            bean.setStatus(PileStatusEnum.BUSYING);
                                        }
                                    }
                                    // saveYC_AC(pileService, m);
                                } else if (type_134.type == 2) {
                                    BizDCPileMonitor m = (BizDCPileMonitor) type_134.value;
                                    bean.setOuti(String.valueOf(m.dcOutI));
                                    bean.setOutv(String.valueOf(m.dcOutV));
                                    bean.setChalen(m.totalTime);
                                    bean.setChapower((double) m.chargedEnergy);
                                    bean.setMoney(m.chargedMoney.add(m.serviceMoney));
                                    bean.setChaMoney(m.chargedMoney);
                                    bean.setServiceMoney((m.serviceMoney));
                                    bean.setIsonline((short) 1);
                                    // bean.setSoc(bean.getSoc());
                                    bean.setSoc(m.SOC + 0.0);
                                    if (m.workstatus == WorkStatusEnum.IDLE.getValue()) {
                                        if (m.connectCar) {
                                            bean.setStatus(PileStatusEnum.BUSYING);
                                            bean.setAppstatus(PileAppStatusEnum.UNORDERED);
                                        } else {
                                            bean.setStatus(PileStatusEnum.IDLE);
                                            bean.setAppstatus(PileAppStatusEnum.UNORDERED);
                                        }
                                    } else if (m.workstatus == WorkStatusEnum.WORK.getValue()) {
                                        bean.setStatus(PileStatusEnum.CHARGING);
                                        // bean.setAppstatus(PileAppStatusEnum.UNORDERED);
                                        // 充电过程中才保存
                                        // saveYC_DC(pileService, m);
                                    } else if (m.workstatus == WorkStatusEnum.APPOINTMENT.getValue()) {
                                        bean.setStatus(PileStatusEnum.IDLE);
                                        bean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                                    } else if (m.workstatus == WorkStatusEnum.ERROR.getValue() || m.workstatus == WorkStatusEnum.ERROR_LOWV.getValue()
                                               || m.workstatus == WorkStatusEnum.ERROR_OVERV.getValue() || m.workstatus == WorkStatusEnum.ERROR_OVERI.getValue()) {
                                        bean.setStatus(PileStatusEnum.FAULT);
                                    } else {
                                        if ((Calendar.getInstance().getTimeInMillis() - chargeCmdTime.getTimeInMillis()) > 600000) {
                                            bean.setStatus(PileStatusEnum.BUSYING);
                                        }
                                    }
                                    // saveYC_DC(pileService, m);
                                } else if (type_134.type == 3) {
                                    EleBicyclePileMonitor m = (EleBicyclePileMonitor) type_134.value;

                                    // 上面的未设置类型
                                    bean.setOuti(String.valueOf(m.chargeOutI));
                                    bean.setOutv(String.valueOf(m.chargeOutV));
                                    bean.setChalen(m.totalTime);
                                    bean.setIsonline((short) 1);

                                    if (m.workStatus == WorkStatusEnum.IDLE.getValue()) {
                                        bean.setStatus(PileStatusEnum.IDLE);
                                        bean.setAppstatus(PileAppStatusEnum.UNORDERED);
                                    } else if (m.workStatus == WorkStatusEnum.WORK.getValue()) {
                                        bean.setStatus(PileStatusEnum.CHARGING);
                                        // bean.setAppstatus(PileAppStatusEnum.UNORDERED);
                                        // 充电过程中才保存
                                        // saveYC_BY(pileService, m);
                                    } else if (m.workStatus == WorkStatusEnum.APPOINTMENT.getValue()) {
                                        bean.setStatus(PileStatusEnum.IDLE);
                                        bean.setAppstatus(PileAppStatusEnum.ORDER_SUCCESS);
                                    } else if (m.workStatus == WorkStatusEnum.ERROR.getValue() || m.workStatus == WorkStatusEnum.ERROR_LOWV.getValue()
                                               || m.workStatus == WorkStatusEnum.ERROR_OVERV.getValue() || m.workStatus == WorkStatusEnum.ERROR_OVERI.getValue()) {
                                        bean.setStatus(PileStatusEnum.FAULT);
                                    } else {
                                        if ((Calendar.getInstance().getTimeInMillis() - chargeCmdTime.getTimeInMillis()) > 600000) {
                                            bean.setStatus(PileStatusEnum.BUSYING);
                                        }
                                    }
                                    // saveYC_BY(pileService, m);
                                }
                                pileService.freshMemPileStatus(bean);
                            }
                            writeData_S(channel, makeFrame_S());
                        }
                            break;
                        default:
                            break;
                    }
                    if (W_104 >= W_104_MAX) {
                        writeData_S(channel, makeFrame_S());
                    }
                }
            }
            dealLen += apci.length;
            resnum = 0;
        }
        return dealLen;
    }

    private void saveYC_BY(PileService pileService, EleBicyclePileMonitor m) {
        List<DcsHisYc> list = new ArrayList<DcsHisYc>();
        DcsHisYc hisYc = new DcsHisYc();
        hisYc.setChargeid(pileService.getPile(m.chargeInterface).getId());
        hisYc.setDataTime(Calendar.getInstance().getTime());
        hisYc.setDataType(DataTypeEnum.YC_AC_OUT_V.getValue());
        hisYc.setValue(m.chargeOutV);
        list.add(hisYc);

        DcsHisYc hisYc1 = new DcsHisYc();
        hisYc1.setChargeid(pileService.getPile(m.chargeInterface).getId());
        hisYc1.setDataTime(Calendar.getInstance().getTime());
        hisYc1.setDataType(DataTypeEnum.YC_AC_OUT_I.getValue());
        hisYc1.setValue(m.chargeOutI);
        list.add(hisYc1);

        pileService.getDataBaseService().saveData(list.toArray());
    }

    private void saveYC_DC(PileService pileService, BizDCPileMonitor m) {
        List<DcsHisYc> list = new ArrayList<DcsHisYc>();
        DcsHisYc hisYc = new DcsHisYc();
        hisYc.setChargeid(pileService.getPile(m.chargeInterface).getId());
        hisYc.setDataTime(Calendar.getInstance().getTime());
        hisYc.setDataType(DataTypeEnum.YC_OUT_V.getValue());
        hisYc.setValue(m.dcOutV);
        list.add(hisYc);

        DcsHisYc hisYc1 = new DcsHisYc();
        hisYc1.setChargeid(pileService.getPile(m.chargeInterface).getId());
        hisYc1.setDataTime(Calendar.getInstance().getTime());
        hisYc1.setDataType(DataTypeEnum.YC_OUT_I.getValue());
        hisYc1.setValue(m.dcOutI);
        list.add(hisYc1);

        pileService.getDataBaseService().saveData(list.toArray());
    }

    private void saveYC_AC(PileService pileService, BizACPileMonitor m) {
        List<DcsHisYc> list = new ArrayList<DcsHisYc>();
        DcsHisYc hisYc = new DcsHisYc();
        hisYc.setChargeid(pileService.getPile(m.chargeInterface).getId());
        hisYc.setDataTime(Calendar.getInstance().getTime());
        hisYc.setDataType(DataTypeEnum.YC_AC_OUT_V.getValue());
        hisYc.setValue(m.acOutV.doubleValue());
        list.add(hisYc);

        DcsHisYc hisYc1 = new DcsHisYc();
        hisYc1.setChargeid(pileService.getPile(m.chargeInterface).getId());
        hisYc1.setDataTime(Calendar.getInstance().getTime());
        hisYc1.setDataType(DataTypeEnum.YC_AC_OUT_I.getValue());
        hisYc1.setValue(m.acOutI.doubleValue());
        list.add(hisYc1);

        pileService.getDataBaseService().saveData(list.toArray());
    }

    @Override
    public int sendBizData(BizCmdTypeEnum cmdType, Object data, PileDev pileDev, ChannelService channel) {
        BizDownBase bdb = null;
        if (cmdType.equals(BizCmdTypeEnum.STARTCHARGE)) {
            MsgChargeStart msgChargeStart = (MsgChargeStart) data;
            bdb = new BizDownRemoteStart(pileDev, msgChargeStart);
        } else if (cmdType.equals(BizCmdTypeEnum.STOPCHARGE)) {
            MsgChargeStop msgChargeStop = (MsgChargeStop) data;
            bdb = new BizDownRemoteStop(pileDev, msgChargeStop);
        } else if (cmdType.equals(BizCmdTypeEnum.STARTAPPOINTMENT)) {
            MsgAppointmentReq msgAppointmentReq = (MsgAppointmentReq) data;
            bdb = new BizDownAppointmentLock(pileDev, msgAppointmentReq);
        } else if (cmdType.equals(BizCmdTypeEnum.STOPAPPOINTMENT)) {
            MsgAppointmentCancle msgAppointmentReq = (MsgAppointmentCancle) data;
            bdb = new BizDownAppointmentCancel(pileDev, msgAppointmentReq);
        } else {
            logger.warn("收到非法业务下发请求!  cmdType=" + cmdType.getValue());
            return 0;
        }

        if (bdb != null) {
            writeData_I(channel, makeIFrame(TYPE_C_SD_NA_1, 0, bdb));
        }
        return 0;
    }

    @Override
    public void onTimer(PileService pileService, ChannelService channel) {
        if (channel == null) {
            return;
        }
        Calendar cnow = Calendar.getInstance();
        Date now = cnow.getTime();

        // 连续收到上报I帧，超时确认
        if (W_104 > 0 && (now.getTime() - lastRecvTime.getTime() > T2_104 * 1000)) {
            writeData_S(channel, makeFrame_S());
        }

        // 未收到确认帧断开连接
        if (now.getTime() - lastSendTime.getTime() > T1_104 * 1000) {
            if (needConfirm) {
                // closeConnect(pileService, channel);
            }
        }

        // 超过心跳间隔
        if ((now.getTime() - lastRecvTime.getTime()) > T4_heart * 1000) {
            closeConnect(pileService, channel);
        }
        if (cnow.get(Calendar.HOUR_OF_DAY) == 2) {
            if (needClock) {
                needClock = false;
                writeData_I(channel, makeIFrame(TYPE_C_CS_NA_1, 0, null));
            }
        } else {
            needClock = true;
        }
        if (pileService.isbNeedDownFeeModel()) {
            PileFeeModel pfm = pileService.getUpdatePFM(null, 1);
            if (pfm != null) {
                BizDownFeeModel bdfm = new BizDownFeeModel(pileService.getPile(0), pfm);
                writeData_I(channel, makeIFrame(TYPE_C_SD_NA_1, 0, bdfm));
                pileService.setbNeedDownFeeModel(false);
            }
        }
    }

    @Override
    public int sendHeart(PileService pileService, ChannelService channel) {
        return writeData_I(channel, makeFrame_U(U_T_TEST));
    }

    private byte[] makeFrame_U(byte type) {
        byte[] U_STARTDT = { 0x68, 0x04, 0x00, type, 0x00, 0x00, 0x00 };
        return U_STARTDT;
    }

    private byte[] makeFrame_S() {
        byte[] S = { 0x68, 0x04, 0x00, 0x01, 0x00, (byte) ((N_R << 1) & 0xFF), (byte) ((N_R >> 7) & 0xFF) };
        return S;
    }

    private byte[] makeIFrame(byte type, int infoaddr, BizDownBase biz) {
        APDU callAll = new APDU();
        N_S++;
        if (type == TYPE_C_IC_NA_1) {
            callAll.apci.ns = N_S;
            callAll.apci.nr = 0;
            callAll.asdu.sq = 0;
            callAll.asdu.num = 1;
            callAll.asdu.cos = 6;
        } else {
            callAll.apci.ns = N_S;
            callAll.apci.nr = N_R;
            callAll.asdu.sq = 1;
            callAll.asdu.num = 1;
            callAll.asdu.cos = 7;
        }

        callAll.asdu.type = type;

        // 激活
        callAll.asdu.pubaddr = this.pubAddr;
        ASDU_Info ai = new ASDU_Info(infoaddr);
        if (type == TYPE_C_CS_NA_1) {
            ai.data = Calendar.getInstance();
        }
        callAll.asdu.infoList.add(ai);
        return callAll.toByte(biz);
    }

    private class APCI {

        byte  start_c = 0x68;
        short length  = 0;
        short ns      = 0;
        short nr      = 0;

        APCI() {

        }

        APCI(byte[] buf, int from) {
            length = ProtocolUtils.byteToShort(buf[from + 2], buf[from + 1]);
            ns = ProtocolUtils.byteToShort(buf[from + 4], buf[from + 3]);
            nr = ProtocolUtils.byteToShort(buf[from + 6], buf[from + 5]);
        }

        byte[] toByte() {
            byte[] APCI = { start_c, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00 };
            APCI[1] = (byte) (length & 0xFF);
            APCI[2] = (byte) ((length >> 8) & 0x07);

            APCI[4] = (byte) ((ns << 1) & 0xFF);
            APCI[3] = (byte) ((ns >> 7) & 0xFF);

            APCI[5] = (byte) (nr & 0xFF);
            APCI[6] = (byte) ((nr >> 8) & 0xFF);
            return APCI;
        }
    }

    private class ASDU {

        byte            type;
        byte            sq;
        byte            num      = 1;
        short           cos;
        short           pubaddr;
        List<ASDU_Info> infoList = new ArrayList<ASDU_Info>();
        byte[]          appdata;

        ASDU() {
        }

        ASDU(byte[] data, int from, int len) {
            int index = from + 4;
            type = data[index++];
            num = (byte) (data[index] & 0x7F);
            if (num == 0) {
                num = 1;
            }
            sq = (byte) ((data[index] & 0x80) == 0 ? 0 : 1);
            index++;
            cos = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            pubaddr = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;

            if (sq == 0) {
                for (int i = 0; i < num; i++) {
                    int addr_info = ProtocolUtils.Unsignd(data[index]) + ProtocolUtils.Unsignd(data[index + 1]) * 0x100 + ProtocolUtils.Unsignd(data[index + 2]) * 0x10000;
                    index += 3;
                    ASDU_Info info = new ASDU_Info(addr_info);
                    int infodatalen = parseASDUInfo(type, data, index, info);
                    index += infodatalen;
                    infoList.add(info);
                    if ((index + infodatalen) > len) {
                        break;
                    }
                }
            } else {
                int addr_info = ProtocolUtils.Unsignd(data[index]) + ProtocolUtils.Unsignd(data[index + 1]) * 0x100 + ProtocolUtils.Unsignd(data[index + 2]) * 0x10000;
                index += 3;
                for (int i = 0; i < num; i++) {
                    ASDU_Info info = new ASDU_Info(addr_info);
                    int infodatalen = parseASDUInfo(type, data, index, info);
                    index += infodatalen;
                    infoList.add(info);
                    if ((index + infodatalen) > len) {
                        break;
                    }
                }
            }

        }

        byte[] toByte(BizDownBase biz) {
            int index = 0; // APCI的长度
            byte[] data = new byte[2096];

            data[index++] = type;
            data[index++] = sq == 0 ? num : (byte) (num | 0x80);
            data[index++] = (byte) (cos & 0xFF);
            data[index++] = (byte) ((cos >> 8) & 0xFF);
            data[index++] = (byte) (pubaddr & 0xFF);
            data[index++] = (byte) ((pubaddr >> 8) & 0xFF);
            for (int i = 0; i < infoList.size(); i++) {
                data[index++] = (byte) (infoList.get(i).addr & 0xFF);
                data[index++] = (byte) ((infoList.get(i).addr >> 8) & 0xFF);
                data[index++] = 0;
                switch (type) {
                    case TYPE_C_IC_NA_1:
                        data[index++] = 0x14; // 站召唤
                        break;
                    case TYPE_C_CI_NA_1:
                        data[index++] = 0x05; // 站召唤
                        break;
                    case TYPE_C_CS_NA_1:
                        if (infoList.get(i).data instanceof Calendar) {
                            Calendar date = (Calendar) infoList.get(i).data;
                            ProtocolUtils.copyBytes(cp56time2a.toByte(date), 0, data, index, 7);
                            index += 7;
                        }
                        break;
                    case TYPE_C_SD_NA_1:
                        data[index++] = (byte) biz.type;
                        byte[] temp = biz.toByte();
                        System.arraycopy(temp, 0, data, index, temp.length);
                        index += temp.length;
                        break;
                    default:
                        break;
                }
            }
            byte[] result = new byte[index];
            ProtocolUtils.copyBytes(data, result, index);
            return result;
        }

        int parseASDUInfo(byte type, byte[] data, int from, ASDU_Info info) {
            int len = 0;
            switch (type) {
                case TYPE_M_SP_NA_1:
                    info.data = new TYPE1(data[from], info.addr);
                    len = 1;
                    break;
                case TYPE_M_ME_NB_1:
                    len = 3;
                    info.data = new TYPE11(data, from, info.addr);
                    break;
                case TYPE_M_IT_NA_1:
                    len = 5;
                    info.data = new TYPE15(data, from, info.addr);
                    break;
                case TYPE_M_RE_NA_1:
                    info.data = new TYPE130(data, from, info.addr);
                    len = ((TYPE130) info.data).len + 2;
                    break;
                case TYPE_M_MD_NA_1:
                    info.data = new TYPE132(data, from, info.addr);
                    len = ((TYPE132) info.data).len + 2;
                    break;
                case TYPE_M_JC_NA_1:
                    info.data = new TYPE134(data, from, info.addr);

                    len = ((TYPE134) info.data).len + 2;
                    break;
                default:
                    break;
            }
            return len;
        }
    }

    private class ASDU_Info {

        public ASDU_Info(int infoaddr) {
            addr = infoaddr;
        }

        int    addr;
        Object data;
    }

    private class APDU {

        APCI apci = new APCI();
        ASDU asdu;

        public APDU() {
            asdu = new ASDU();
        }

        byte[] toByte(BizDownBase biz) {
            byte[] buf_asdu = asdu.toByte(biz);
            byte[] data = new byte[buf_asdu.length + 7];
            apci.length = (short) (buf_asdu.length + 4);
            ProtocolUtils.copyBytes(apci.toByte(), data, 7);
            ProtocolUtils.copyBytes(buf_asdu, 0, data, 7, buf_asdu.length);
            return data;
        }
    }

    private static class cp56time2a {

        static byte[] toByte(Calendar date) {
            short msec = (short) (date.get(Calendar.SECOND) * 1000 + date.get(Calendar.MILLISECOND));
            byte min = (byte) date.get(Calendar.MINUTE);
            byte hour = (byte) date.get(Calendar.HOUR_OF_DAY);
            byte day_m = (byte) date.get(Calendar.DAY_OF_MONTH);
            byte day_w = (byte) (date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ? 7 : date.get(Calendar.DAY_OF_WEEK) - 1);
            byte month = (byte) (date.get(Calendar.MONTH) + 1);
            byte year = (byte) (date.get(Calendar.YEAR) - 2000);
            byte[] time = new byte[7];
            time[0] = (byte) (msec & 0xFF);
            time[1] = (byte) ((msec >> 8) & 0xFF);
            time[2] = (byte) (min & 0x3F);
            time[3] = (byte) (hour & 0x1F);
            time[4] = (byte) ((day_m & 0x1F) | (day_w << 5));
            time[5] = month;
            time[6] = year;
            return time;
        }

        static Calendar toDate(byte[] buf) {
            if (buf == null || buf.length < 7) {
                return null;
            }
            Calendar date = Calendar.getInstance();
            date.set(ProtocolUtils.Unsignd(buf[6]) + 2000, ProtocolUtils.Unsignd(buf[5]) - 1, ProtocolUtils.Unsignd((byte) (buf[4] & 0x1F)),
                     ProtocolUtils.Unsignd((byte) (buf[3] & 0x1F)), ProtocolUtils.Unsignd((byte) (buf[2] & 0x3F)), ProtocolUtils.byteToShort(buf[1], buf[1]) / 1000);
            return date;
        }
    }

    private class TYPE1 {

        byte spi = 0;

        TYPE1(byte data, int infoaddr) {
            spi = (byte) (data & 0x01);

        }
    }

    private class TYPE11 {

        byte qds = 0;
        int  value;

        TYPE11(byte[] data, int from, int addr) {
            value = ProtocolUtils.byteToShort(data[from + 1], data[from]);
            qds = (byte) data[from + 2];
        }
    }

    private class TYPE15 {

        boolean bInValid = false;
        int     value;

        TYPE15(byte[] data, int from, int addr) {
            value = ProtocolUtils.byteToShort(data[from + 1], data[from]) + ProtocolUtils.byteToShort(data[from + 3], data[from + 2]) * 0x10000;
            bInValid = (data[from + 4] & 0x80) == 0 ? false : true;
        }
    }

    private class TYPE132 {

        byte   qds = 0;
        byte[] value;
        int    len = 0;

        TYPE132(byte[] data, int from, int addr) {
            len = ProtocolUtils.Unsignd(data[from]);
            value = new byte[len];
            System.arraycopy(data, from + 1, value, 0, len);
            qds = (byte) data[from + 1 + len];
        }
    }

    private class TYPE134 {

        int       len = 0;
        int       type;
        BizUpBase value;

        TYPE134(byte[] data, int from, int addr) {
            type = ProtocolUtils.Unsignd(data[from]);
            switch (type) {
                case 1:
                    value = new BizACPileMonitor(data, from + 1);
                    break;
                case 2:
                    value = new BizDCPileMonitor(data, from + 1);
                    break;
                case 3:
                    value = new EleBicyclePileMonitor(data, from + 1);
                    break;
            }
            if (value != null) {
                len += value.len;
            }
        }
    }

    private class TYPE130 {

        int       len = 0;
        int       type;
        BizUpBase value;

        TYPE130(byte[] data, int from, int addr) {
            type = ProtocolUtils.Unsignd(data[from]);
            switch (type) {
                case BizTypeUp.ReqFeeModel:
                    value = new BizUpReqFeeModel(data, from + 1);
                    break;
                case BizTypeUp.ResultFeeModel:
                    value = new BizUpResultFeeModel(data, from + 1);
                    break;
                case BizTypeUp.AppointmentLock:
                    value = new BizUpAppointmentLock(data, from + 1);
                    break;
                case BizTypeUp.AppointmentCancel:
                    value = new BizUpAppointmentCancel(data, from + 1);
                    break;
                case BizTypeUp.RemoteStartResult:
                    value = new BizUpRemoteStartResult(data, from + 1);
                    break;
                case BizTypeUp.ChargeEventStart:
                    value = new BizUpChargeEventStart(data, from + 1);
                    break;
                case BizTypeUp.RemoteStopResult:
                    value = new BizUpRemoteStopResult(data, from + 1);
                    break;
                case BizTypeUp.ChargeEventEnd:
                    value = new BizUpChargeEventEnd(data, from + 1);
                    break;
                case BizTypeUp.ChargeRec:
                    value = new BizUpChargeRec(data, from + 1);
                    break;
                case BizTypeUp.ChargeStartByCard:
                    value = new BizUpChargeStartByCard(data, from + 1);
                    break;
            }
            if (value != null) {
                len += value.len;
            }
        }
    }

    private abstract class BizDownBase {

        int    type;
        String devcode;
        int    chargeInterface = 0;

        public BizDownBase(PileDev pile, int type) {
            devcode = pile.getComAddr();
            this.type = type;
            if (!StringUtils.isEmpty(pile.getComSubAddr())) {
                chargeInterface = Integer.parseInt(pile.getComSubAddr());
            }
        }

        public abstract byte[] toByte();
    }

    private class BizDownFeeModel extends BizDownBase {

        int      feeModelId;
        Calendar startTime;
        Calendar endTime;
        int      feeType     = 1;
        int      singlePrice = 0;
        int      jianPrice   = 0; // 尖单价
        int      peakPrice   = 0;
        int      flatPrice   = 0;
        int      valleyPrice = 0;
        int      chargePrice = 0; // 充电服务费单价

        public BizDownFeeModel(PileDev pile, PileFeeModel pfm) {
            super(pile, BizTypeDown.FeeModel);
            if (pfm != null) {
                feeModelId = pfm.getChargeruleId();
                startTime = Calendar.getInstance();
                startTime.setTime(pfm.getActiveTime());
                endTime = Calendar.getInstance();
                endTime.set(Calendar.YEAR, 2050);
                feeType = pfm.getChargeruleId() == 1 ? 0 : 1;
                if (feeType == 0) {
                    singlePrice = pfm.getChargeFee().multiply(new BigDecimal(10000)).intValue();
                } else {
                    jianPrice = (int) (pfm.getFeeModel().getJianFee().doubleValue() * 10000);
                    peakPrice = (int) (pfm.getFeeModel().getFengFee().doubleValue() * 10000);
                    flatPrice = (int) (pfm.getFeeModel().getPingFee().doubleValue() * 10000);
                    valleyPrice = (int) (pfm.getFeeModel().getGuFee().doubleValue() * 10000);
                }
                chargePrice = (int) (pfm.getServiceFee().doubleValue() * 10000);
            }
        }

        @Override
        public byte[] toByte() {
            byte[] data = new byte[512];
            int index = 0;
            System.arraycopy(ProtocolUtils.StringToHex(devcode, 8), 0, data, 0, 8);
            index += 8;
            data[index++] = (byte) (chargeInterface & 0xFF);

            data[index++] = (byte) (feeModelId & 0xFF);
            data[index++] = (byte) ((feeModelId >> 8) & 0xFF);
            data[index++] = (byte) ((feeModelId >> 16) & 0xFF);
            data[index++] = (byte) ((feeModelId >> 24) & 0xFF);
            data[index++] = 0;
            data[index++] = 0;
            data[index++] = 0;
            data[index++] = 0;

            System.arraycopy(cp56time2a.toByte(startTime), 0, data, index, 7);
            index += 7;
            System.arraycopy(cp56time2a.toByte(endTime), 0, data, index, 7);
            index += 7;

            data[index++] = (byte) (feeType & 0xFF);

            data[index++] = (byte) (singlePrice & 0xFF);
            data[index++] = (byte) ((singlePrice >> 8) & 0xFF);
            data[index++] = (byte) ((singlePrice >> 16) & 0xFF);
            data[index++] = (byte) ((singlePrice >> 24) & 0xFF);

            data[index++] = (byte) (jianPrice & 0xFF);
            data[index++] = (byte) ((jianPrice >> 8) & 0xFF);
            data[index++] = (byte) ((jianPrice >> 16) & 0xFF);
            data[index++] = (byte) ((jianPrice >> 24) & 0xFF);

            data[index++] = (byte) (peakPrice & 0xFF);
            data[index++] = (byte) ((peakPrice >> 8) & 0xFF);
            data[index++] = (byte) ((peakPrice >> 16) & 0xFF);
            data[index++] = (byte) ((peakPrice >> 24) & 0xFF);

            data[index++] = (byte) (flatPrice & 0xFF);
            data[index++] = (byte) ((flatPrice >> 8) & 0xFF);
            data[index++] = (byte) ((flatPrice >> 16) & 0xFF);
            data[index++] = (byte) ((flatPrice >> 24) & 0xFF);

            data[index++] = (byte) (valleyPrice & 0xFF);
            data[index++] = (byte) ((valleyPrice >> 8) & 0xFF);
            data[index++] = (byte) ((valleyPrice >> 16) & 0xFF);
            data[index++] = (byte) ((valleyPrice >> 24) & 0xFF);

            data[index++] = (byte) (chargePrice & 0xFF);
            data[index++] = (byte) ((chargePrice >> 8) & 0xFF);
            data[index++] = (byte) ((chargePrice >> 16) & 0xFF);
            data[index++] = (byte) ((chargePrice >> 24) & 0xFF);

            byte[] res = new byte[index];
            System.arraycopy(data, 0, res, 0, index);
            logger.debug("Pile " + devcode + " update feemodel!(modeid=" + feeModelId);
            return res;
        }
    }

    private class BizDownAppointmentLock extends BizDownBase {

        int      appointFlag = 0;                     // 0预约，1续约
        Calendar endTime     = Calendar.getInstance();
        String   userID;                              // 预约账号，手机号码
        String   cardID      = "0000000000000000";    // 预约卡号
        String   appointID   = "000000000000";        // 预约号

        public BizDownAppointmentLock(PileDev pile, MsgAppointmentReq msgAppointmentReq) {
            super(pile, BizTypeDown.AppointmentLock);
            appointID = msgAppointmentReq.getAppointmentID();
            userID = msgAppointmentReq.getPhone();

            endTime.setTime(msgAppointmentReq.getEndTime());
        }

        @Override
        public byte[] toByte() {
            byte[] data = new byte[512];
            int index = 0;
            System.arraycopy(ProtocolUtils.StringToHex(devcode, 8), 0, data, 0, 8);
            index += 8;
            data[index++] = (byte) (chargeInterface & 0xFF);
            data[index++] = (byte) (appointFlag & 0xFF);
            System.arraycopy(cp56time2a.toByte(endTime), 0, data, index, 7);
            index += 7;
            System.arraycopy(ProtocolUtils.StringToHex(userID, 6), 0, data, index, 6);
            index += 6;
            System.arraycopy(ProtocolUtils.StringToHex(cardID, 8), 0, data, index, 8);
            index += 8;
            System.arraycopy(ProtocolUtils.StringToHex(appointID, 6), 0, data, index, 6);
            index += 6;
            byte[] res = new byte[index];
            System.arraycopy(data, 0, res, 0, index);
            logger.debug("User(Phone=" + userID + ") appointment request Pile(addr=" + devcode);
            return res;
        }
    }

    private class BizDownAppointmentCancel extends BizDownBase {

        String appointID; // 预约号

        public BizDownAppointmentCancel(PileDev pile, MsgAppointmentCancle msgAppointmentCancle) {
            super(pile, BizTypeDown.AppointmentCancel);
            appointID = msgAppointmentCancle.getAppointmentID();
        }

        @Override
        public byte[] toByte() {
            byte[] data = new byte[512];
            int index = 0;
            System.arraycopy(ProtocolUtils.StringToHex(devcode, 8), 0, data, index, 8);
            index += 8;
            data[index++] = (byte) (chargeInterface & 0xFF);
            System.arraycopy(ProtocolUtils.StringToHex(appointID, 6), 0, data, index, 6);
            index += 6;
            byte[] res = new byte[index];
            System.arraycopy(data, 0, res, 0, index);
            logger.debug("User(Phone=" + appointID + ") appointment Cancle Pile(addr=" + devcode);
            return res;
        }
    }

    private class BizDownRemoteStart extends BizDownBase {

        String tradeNo;       // 充电流水号号
        String userID;        // 预约账号，手机号码
        int    chargeType = 1; // 00 自动；01 按电量；02 按时间； 03 按金额
        int    value      = 0; // 预充金额

        public BizDownRemoteStart(PileDev pile, MsgChargeStart msgChargeStart) {
            super(pile, BizTypeDown.RemoteStart);

            tradeNo = msgChargeStart.getTradeNo();
            userID = msgChargeStart.getPhone();
            chargeType = msgChargeStart.getChargeMode();
            value = msgChargeStart.getValue();
        }

        @Override
        public byte[] toByte() {
            byte[] data = new byte[512];
            int index = 0;
            System.arraycopy(ProtocolUtils.StringToHex(devcode, 8), 0, data, index, 8);
            index += 8;
            data[index++] = (byte) (chargeInterface & 0xFF);

            System.arraycopy(ProtocolUtils.StringToHex(tradeNo, 16), 0, data, index, 16);
            index += 16;
            System.arraycopy(ProtocolUtils.StringToHex(userID, 6), 0, data, index, 6);
            index += 6;

            data[index++] = (byte) (chargeType & 0xFF);

            data[index++] = (byte) (value & 0xFF);
            data[index++] = (byte) ((value >> 8) & 0xFF);
            data[index++] = (byte) ((value >> 16) & 0xFF);
            data[index++] = (byte) ((value >> 24) & 0xFF);

            byte[] res = new byte[index];
            System.arraycopy(data, 0, res, 0, index);
            logger.debug("User(Phone=" + userID + ") start app charge request Pile(addr=" + devcode);
            return res;
        }
    }

    private class BizDownRemoteStop extends BizDownBase {

        String tradeNo; // 充电流水号号

        public BizDownRemoteStop(PileDev pile, MsgChargeStop msgChargeStop) {
            super(pile, BizTypeDown.RemoteStop);
            tradeNo = msgChargeStop.getTradeNo();
        }

        @Override
        public byte[] toByte() {
            byte[] data = new byte[512];
            int index = 0;
            System.arraycopy(ProtocolUtils.StringToHex(devcode, 8), 0, data, index, 8);
            index += 8;
            data[index++] = (byte) (chargeInterface & 0xFF);
            System.arraycopy(ProtocolUtils.StringToHex(tradeNo, 16), 0, data, index, 16);
            index += 16;
            byte[] res = new byte[index];
            System.arraycopy(data, 0, res, 0, index);
            logger.debug("User(TradeNo=" + tradeNo + ") appointment Cancle Pile(addr=" + devcode);
            return res;
        }
    }

    private class BizDownChargeConfirm extends BizDownBase {

        String tradeNo; // 充电流水号号
        int    result; // 3:已经处理 2:数据不存在 1:处理成功

        public BizDownChargeConfirm(PileDev pile, String tradeNo) {
            super(pile, BizTypeDown.ChargeConfirm);
            this.tradeNo = tradeNo;
        }

        @Override
        public byte[] toByte() {
            byte[] data = new byte[512];
            int index = 0;
            System.arraycopy(ProtocolUtils.StringToHex(devcode, 8), 0, data, index, 8);
            index += 8;
            data[index++] = (byte) (chargeInterface & 0xFF);
            System.arraycopy(ProtocolUtils.StringToHex(tradeNo, 16), 0, data, index, 16);
            index += 16;
            data[index++] = 0x01;

            byte[] res = new byte[index];
            System.arraycopy(data, 0, res, 0, index);
            return res;
        }
    }

    // --------------

    private class BizUpBase {

        String devcode;
        int    chargeInterface;
        int    len = 0;

        BizUpBase(byte[] data, int from) {
            devcode = ProtocolUtils.getByteToHexStringDesc(data, from, 8, "");
            chargeInterface = ProtocolUtils.Unsignd(data[from + 8]);
            len = 9;
        }

    }

    private class BizUpReqFeeModel extends BizUpBase {

        Calendar lastTime;

        BizUpReqFeeModel(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            lastTime = cp56time2a.toDate(Arrays.copyOfRange(data, index, index + 7));
            index += 7;
            len = index;
        }

    }

    private class BizUpResultFeeModel extends BizUpBase {

        String feeModelId;
        int    isSuccess;
        int    errorCode;

        BizUpResultFeeModel(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            feeModelId = ProtocolUtils.getByteToHexStringDesc(data, index, 8, "");
            index += 8;
            isSuccess = ProtocolUtils.Unsignd(data[index++]);
            errorCode = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            len = index;
        }

    }

    private class BizUpAppointmentLock extends BizUpBase {

        int    appointFlag = 0; // 0预约，1续约
        int    result;         // 1:表示成功 0：其它表示失败
        int    errorInfo;
        String appointID;      // 预约号

        BizUpAppointmentLock(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            appointFlag = ProtocolUtils.Unsignd(data[index++]);
            appointID = ProtocolUtils.getByteToHexStringDesc(data, index, 6, "");
            index += 6;
            result = ProtocolUtils.Unsignd(data[index++]);
            errorInfo = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            len = index;
        }

    }

    private class BizUpAppointmentCancel extends BizUpBase {

        String userID;   // 预约账号，手机号码,把预约下行锁定的账号上报
        String appointID; // 预约号
        int    result;   // 1:表示取消预约成功
        int    errorInfo;

        BizUpAppointmentCancel(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            userID = ProtocolUtils.getByteToHexStringDesc(data, index, 6, "");
            index += 6;

            appointID = ProtocolUtils.getByteToHexStringDesc(data, index, 6, "");
            index += 6;
            result = ProtocolUtils.Unsignd(data[index++]);
            errorInfo = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            len = index;
        }

    }

    private class BizUpChargeStartByCard extends BizUpBase {

        String cardID; // 8字节ASCII码

        BizUpChargeStartByCard(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            cardID = ProtocolUtils.getByteToHexStringDesc(data, index, 8, "");
            index += 8;
            len = index;
        }

    }

    private class BizDownChargeStartByCardConfirm extends BizDownBase {

        String  cardID;        // 充电卡卡号
        Account account = null;

        public BizDownChargeStartByCardConfirm(PileService pileService, Integer chargeinterface, String cardID) {
            super(pileService.getPile(chargeinterface), BizTypeDown.ChargeStartByCardConfirm);
            this.cardID = cardID;
            account = pileService.getDataBaseService().getAccountByCardID(cardID);
        }

        @Override
        public byte[] toByte() {
            byte[] data = new byte[512];
            int index = 0;
            System.arraycopy(ProtocolUtils.StringToHex(devcode, 8), 0, data, index, 8);
            index += 8;
            data[index++] = (byte) (chargeInterface & 0xFF);
            String tradeNo = this.devcode;
            Calendar now = Calendar.getInstance();
            tradeNo += String.format("%02d%02d%02d%02d%02d%02d1%03d", now.get(Calendar.YEAR) % 100, now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH),
                                     now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), now.get(Calendar.SECOND), BaseProtocol.trade_no++);
            System.arraycopy(ProtocolUtils.StringToHex(tradeNo, 16), 0, data, index, 16);
            index += 16;
            System.arraycopy(ProtocolUtils.StringToHex(cardID, 8), 0, data, index, 8);
            index += 8;
            int value = 0;
            if (account != null) {
                value = account.getUsableMoney().multiply(new BigDecimal(100)).intValue();
                if (value < 0) {
                    value = 0;
                }
                data[index++] = 0x0;
            } else {
                data[index++] = 0x03;
            }

            data[index++] = (byte) (value & 0xFF);
            data[index++] = (byte) ((value >> 8) & 0xFF);
            data[index++] = (byte) ((value >> 16) & 0xFF);
            data[index++] = (byte) ((value >> 24) & 0xFF);

            byte[] res = new byte[index];
            System.arraycopy(data, 0, res, 0, index);
            return res;
        }
    }

    private class BizUpRemoteStartResult extends BizUpBase {

        String tradeNo;
        int    isSuccess; // 1成功，其他失败
        int    errorInfo;

        BizUpRemoteStartResult(byte[] data, int from) {
            super(data, from);
            int index = from + len;

            tradeNo = ProtocolUtils.getByteToHexStringDesc(data, index, 16, "");
            index += 16;
            isSuccess = ProtocolUtils.Unsignd(data[index++]);
            errorInfo = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            len = index;
        }

    }

    private class BizUpChargeEventStart extends BizUpBase {

        String   tradeNo;    // 充电流水号号 ：终端机器编码 16+序列号 16
        int      userType;   // 1：帐号 2：普通卡
        String   userID;     // 当userType 等于帐号时，这儿是账户 BCD 码 当 这 儿 是 卡 时，这儿是内卡号的 ASCII 码
        double   energyBegin;
        Calendar timeBegin;
        int      leftTime;   // 直流有效,交流数据为 0
        int      isSuccess;  // 1:成功充电;0:失败 0:放弃（未插枪超时等
        int      errorInfo;
        String   plateNum;   // 充电车牌号

        BizUpChargeEventStart(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            tradeNo = ProtocolUtils.getByteToHexStringDesc(data, index, 16, "");
            index += 16;
            userType = ProtocolUtils.Unsignd(data[index++]);
            plateNum = "京" + ProtocolUtils.asciiToStringDesc(data, index, 6);
            index += 6;
            userID = ProtocolUtils.getByteToHexStringDesc(data, index, 26, "");
            index += 26;
            if (userType == 1) {
                userID = userID.substring(1, 12);
            } else {
                userID = userID.substring(0, 20);
            }
            energyBegin = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            timeBegin = cp56time2a.toDate(Arrays.copyOfRange(data, index, index + 7));
            index += 7;
            leftTime = ProtocolUtils.byteToInteger(data, index);
            index += 4;
            isSuccess = ProtocolUtils.Unsignd(data[index++]);
            errorInfo = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            len = index;
        }

    }

    private class BizUpRemoteStopResult extends BizUpBase {

        int isSuccess; // 0 成功; 其它失败

        BizUpRemoteStopResult(byte[] data, int from) {
            super(data, from);
            int index = from + len;

            isSuccess = ProtocolUtils.Unsignd(data[index++]);
            len = index;
        }

    }

    private class BizUpChargeEventEnd extends BizUpBase {

        String   tradeNo;   // 充电流水号号 ：终端机器编码 16+序列号 16
        double   energyEnd;
        Calendar timeEnd;
        int      endCause;
        /*
         * 1：正常结束 2：用户强制结束 3：急停 4：连接线断掉 5: 电表异常 6：过流停止 7：过压停止 8：防雷器故障 9：接触器故障 10:余额不足 11:漏电保护器 12:自动完成 13:BMS 通信异常故障
         * 14:违规拔枪 15::电桩断电 21.用户 app 上停止充电 22.二维码方式充电桩上结束充电 23.用户名密码用户输入密码结束充电 24.验证码方式 用户输入验证码结束充电
         * 25.充电桩检测汽车充电充满信号结束充电 26.按时间充时间达到设定值 27.按电量充电量达到设定值 28.按金额充金额达到设定值 29.自动充满方式: 30.第 5 点充电桩检测汽车充电充满信号结束充电
         * 31.电流小于 1A 超过规定时间(1 分钟和 10 分钟)结束充电
         */
        int      endType;
        int      onlineFlag; // 1：在线， 0:离线，用于卡停止充电或者本 地鉴权，断网情况下的停止充电
        int      isSuccess;

        BizUpChargeEventEnd(byte[] data, int from) {
            super(data, from);
            int index = from + len;

            tradeNo = ProtocolUtils.getByteToHexStringDesc(data, index, 16, "");
            index += 16;
            energyEnd = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;

            timeEnd = cp56time2a.toDate(Arrays.copyOfRange(data, index, index + 7));
            index += 7;
            endCause = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            isSuccess = ProtocolUtils.Unsignd(data[index++]);
            len = index;
        }

    }

    private class BizUpChargeRec extends BizUpBase {

        String   tradeNo;
        int      userType;     // 1：帐号 2：普通卡
        String   userID;       // 当userType 等于帐号时，这儿是账户 BCD 码 当 这 儿 是 卡 时，这儿是内卡号的 ASCII 码
        int      isOffline;    // 0:离线 1:在线
        int      chargeMode;   // 00 自动；01 按电量；02 按时间； 03 按金额
        Calendar startTime;
        Calendar endTime;
        double   jianElec;     // 尖电量
        double   jianMoney;    // 尖金额
        double   peakElec;
        double   peakMoney;
        double   flatElec;
        double   flatMoney;
        double   valleyElec;
        double   valleyMoney;
        double   totalElec;    // 总电量
        double   energyMoney;  // 总电量金额
        double   serviceMoney; // 服务费金额
        double   totalBegValue; // 总起示值
        double   totalEndValue;
        int      endCause;
        int      payFlag;
        String   plateNum;

        BizUpChargeRec(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            tradeNo = ProtocolUtils.getByteToHexStringDesc(data, index, 16, "");
            index += 16;
            userType = ProtocolUtils.Unsignd(data[index++]);
            plateNum = "京" + ProtocolUtils.asciiToStringDesc(data, index, 6);
            index += 6;
            userID = ProtocolUtils.getByteToHexStringDesc(data, index, 26, "");
            index += 26;
            if (userType == 1) {
                userID = userID.substring(1, 12);
            } else {
                userID = userID.substring(0, 20);
            }
            isOffline = ProtocolUtils.Unsignd(data[index++]);
            chargeMode = ProtocolUtils.Unsignd(data[index++]);
            startTime = cp56time2a.toDate(Arrays.copyOfRange(data, index, index + 7));
            index += 7;
            endTime = cp56time2a.toDate(Arrays.copyOfRange(data, index, index + 7));
            index += 7;
            jianElec = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            jianMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01")).doubleValue();
            index += 4;
            peakElec = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            peakMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01")).doubleValue();
            index += 4;
            flatElec = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            flatMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01")).doubleValue();
            index += 4;
            valleyElec = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            valleyMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01")).doubleValue();
            index += 4;
            totalElec = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            energyMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01")).doubleValue();
            index += 4;
            serviceMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01")).doubleValue();
            index += 4;
            totalBegValue = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            totalEndValue = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            endCause = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            payFlag = ProtocolUtils.Unsignd(data[index++]);
            len = index;
        }
    }

    private class BizDCPileMonitor extends BizUpBase {

        double     dcOutV;
        double     dcOutI;
        int        SOC;
        double     BatTempLow;     // 电池组最低温度
        double     BatTempHigh;
        int        workstatus;
        boolean    BmsError;
        boolean    dcOutOver;      // 直流母线输出过压告警
        boolean    dcOutLoss;
        boolean    BatCurOver;     // 蓄电池充电过流告警
        boolean    BatPntempOver;

        boolean    BatLinked;      // 是否连接电池
        double     BatSvHigh;      // 单体电池最高电压
        double     BatSvLow;       // 单体电池最低电压
        boolean    plugBaseStatus; // 0:未收枪 1:收枪 壁挂式不需要此状态
        boolean    plugCoverStatus; // 0:开;1:关闭 壁挂式不需要此状态
        boolean    connectCar;     // 0:未建立通讯 1:建立通讯 没有填 0
        boolean    parkBusy;       // 1：表示有车： 0：表示没车

        boolean    recordOverflow; // 交 易 记 录 已满告警
        boolean    cardReadError;  // 读 卡 器 通 讯异常
        boolean    meterError;     // 电度表异常
        double     ygZong;         // 有功总电能
        int        totalTime;
        double     chargedEnergy;  // 已充总度数 精确到小数点后两位，倍数 100
        BigDecimal chargedMoney;   // 已充金额 精确到小数点后两位，倍数 100
        BigDecimal serviceMoney;   // 电价 精确到小数点后两位， 倍数 100

        BizDCPileMonitor(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            dcOutV = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.1")).doubleValue();
            index += 2;
            dcOutI = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.01")).doubleValue();
            index += 2;
            SOC = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            BatTempLow = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.1")).doubleValue();
            index += 2;
            BatTempHigh = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.1")).doubleValue();
            index += 2;
            workstatus = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 1;
            BmsError = data[index] == 0 ? false : true;
            index += 1;
            dcOutOver = data[index] == 0 ? false : true;
            index += 1;
            dcOutLoss = data[index] == 0 ? false : true;
            index += 1;
            BatCurOver = data[index] == 0 ? false : true;
            index += 1;
            BatPntempOver = data[index] == 0 ? false : true;
            index += 1;
            BatLinked = data[index] == 0 ? false : true;
            index += 1;
            BatSvHigh = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.1")).doubleValue();
            index += 2;
            BatSvLow = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.1")).doubleValue();
            index += 2;
            plugBaseStatus = data[index++] == 0 ? false : true;
            plugCoverStatus = data[index++] == 0 ? false : true;
            connectCar = data[index++] == 0 ? false : true;
            parkBusy = data[index++] == 0 ? false : true;
            recordOverflow = data[index++] == 0 ? false : true;
            cardReadError = data[index++] == 0 ? false : true;
            meterError = data[index++] == 0 ? false : true;
            ygZong = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            totalTime = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            chargedEnergy = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001")).doubleValue();
            index += 4;
            chargedMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01"));
            index += 4;
            serviceMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01"));
            index += 4;
            len = index - from;
        }
    }

    private class BizACPileMonitor extends BizUpBase {

        boolean    linkcon;
        int        workstatus;
        boolean    plugBaseStatus; // 0:未收枪 1:收枪 壁挂式不需要此状态
        boolean    plugCoverStatus; // 0:开;1:关闭 壁挂式不需要此状态
        boolean    connectCar;     // 0:未建立通讯 1:建立通讯 没有填 0
        boolean    acInOver;
        boolean    acInLoss;
        boolean    iOver;
        BigDecimal acOutV;
        BigDecimal acOutI;
        boolean    relayOut;
        boolean    parkBusy;       // 1：表示有车： 0：表示没车
        BigDecimal ygZong;
        int        totalTime;
        BigDecimal chargedEnergy;  // 已充总度数 精确到小数点后两位，倍数 100
        BigDecimal chargedMoney;   // 已充金额 精确到小数点后两位，倍数 100
        BigDecimal serviceMoney;   // 电价 精确到小数点后两位， 倍数 100

        BizACPileMonitor(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            linkcon = data[index++] == 0 ? false : true;
            workstatus = data[index++];
            plugBaseStatus = data[index++] == 0 ? false : true;
            plugCoverStatus = data[index++] == 0 ? false : true;
            connectCar = data[index++] == 0 ? false : true;

            acInOver = data[index++] == 0 ? false : true;
            acInLoss = data[index++] == 0 ? false : true;
            iOver = data[index++] == 0 ? false : true;
            acOutV = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.1"));
            index += 2;
            acOutI = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.01"));
            index += 2;
            relayOut = data[index++] == 0 ? false : true;
            parkBusy = data[index++] == 0 ? false : true;
            ygZong = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001"));
            index += 4;
            totalTime = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            chargedEnergy = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.001"));
            index += 4;
            chargedMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01"));
            index += 4;
            serviceMoney = new BigDecimal(ProtocolUtils.byteToInteger(data, index)).multiply(new BigDecimal("0.01"));
            index += 4;
            len = index - from;
        }
    }

    private class EleBicyclePileMonitor extends BizUpBase {

        int     workStatus;
        boolean acInOver;
        boolean acInLoss;
        boolean iOver;
        boolean tempOverAlarm; // 过温报警
        boolean smokeAlarm;    // 烟雾报警
        boolean alarm1;        // 报警1预留
        boolean alarm2;        // 报警2预留
        // 第九和十项预留 各占1Byte
        double  chargeOutV;    // 充电输出电压
        double  chargeOutI;    // 充电输出电流
        int     touchPointTemp; // 接触点温度 整数上传 预留
        int     totalTime;

        EleBicyclePileMonitor(byte[] data, int from) {
            super(data, from);
            int index = from + len;
            workStatus = data[index++];
            acInOver = data[index++] == 0 ? false : true;
            acInLoss = data[index++] == 0 ? false : true;
            iOver = data[index++] == 0 ? false : true;
            tempOverAlarm = data[index++] == 0 ? false : true;
            smokeAlarm = data[index++] == 0 ? false : true;
            alarm1 = data[index++] == 0 ? false : true;
            alarm2 = data[index++] == 0 ? false : true;
            // 预留的两位
            chargeOutV = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.1")).doubleValue();
            index += 2;
            chargeOutI = new BigDecimal(ProtocolUtils.byteToShort(data[index + 1], data[index])).multiply(new BigDecimal("0.01")).doubleValue();
            index += 2;
            touchPointTemp = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            totalTime = ProtocolUtils.byteToShort(data[index + 1], data[index]);
            index += 2;
            len = index - from;
        }
    }

    private static class INFO_ADDR_AC {

        static int       ME            = 0;
        static int       SP            = 0;
        static int       MD            = 0;
        /** 信息体定义，请注意顺序不能变动，必须与协议一致 **/
        // static final int DEVCODE=MD++; //充电设备编号
        // static final int CHARGE_INTERFACE=SP++; //充电接口
        static final int LINKCON       = SP++; // 连接确认
        static final int WORKSTATUS    = ME++; // 工作状态
        static final int AC_IN_OVER    = SP++; // 交流输入过压状态
        static final int AC_IN_LOSS    = SP++; // 交流输入欠压状态
        static final int I_OVER        = SP++; // 充电电流过负荷告警
        static final int AC_OUT_V      = ME++; // 充电输出电压
        static final int AC_OUT_I      = ME++; // 充电输出电流
        static final int RELAY_OUT     = SP++; // 输出继电器状态
        static final int YG_ZONG       = MD++; // 有功总电能
        static final int TOTAL_TIME    = ME++; // 累计充电时间
        static final int LEFT_TIME     = ME++; // 预计剩余充电时间
        static final int VA_CUR        = ME++; // 当前A相电压
        static final int VB_CUR        = ME++; // 当前B相电压
        static final int VC_CUR        = ME++; // 当前C相电压
        static final int IA_CUR        = ME++; // 当前A相电流
        static final int IB_CUR        = ME++; // 当前B相电流
        static final int IC_CUR        = ME++; // 当前C相电流
        static final int PROTECT_TEMP  = SP++; // 温度过高保护
        static final int PROTECT_SHORT = SP++; // 短路保护
        static final int PROTECT_LEAK  = SP++; // 漏电保护
        static final int PROTECT_STOP  = SP++; // 急停开关触发

        static Integer getYCCode(int addrInfo) {
            if (addrInfo == WORKSTATUS) {
                return DataTypeEnum.YC_WORKSTATUS.getValue();
            } else if (addrInfo == AC_OUT_V) {
                return DataTypeEnum.YC_AC_OUT_V.getValue();
            } else if (addrInfo == AC_OUT_I) {
                return DataTypeEnum.YC_AC_OUT_I.getValue();
            } else if (addrInfo == TOTAL_TIME) {
                return DataTypeEnum.YC_TOTAL_TIME.getValue();
            } else if (addrInfo == LEFT_TIME) {
                return DataTypeEnum.YC_LEFT_TIME.getValue();
            } else if (addrInfo == VA_CUR) {
                return DataTypeEnum.YC_VA_CUR.getValue();
            } else if (addrInfo == VB_CUR) {
                return DataTypeEnum.YC_VB_CUR.getValue();
            } else if (addrInfo == VC_CUR) {
                return DataTypeEnum.YC_VC_CUR.getValue();
            } else if (addrInfo == IA_CUR) {
                return DataTypeEnum.YC_IA_CUR.getValue();
            } else if (addrInfo == IB_CUR) {
                return DataTypeEnum.YC_IB_CUR.getValue();
            } else if (addrInfo == IC_CUR) {
                return DataTypeEnum.YC_IC_CUR.getValue();
            } else {
                return null;
            }
        }

        static Integer getYXCode(int addrInfo) {
            if (addrInfo == LINKCON) {
                return DataTypeEnum.YX_LINKCON.getValue();
            } else if (addrInfo == AC_IN_OVER) {
                return DataTypeEnum.YX_AC_IN_OVER.getValue();
            } else if (addrInfo == AC_IN_LOSS) {
                return DataTypeEnum.YX_AC_IN_LOSS.getValue();
            } else if (addrInfo == I_OVER) {
                return DataTypeEnum.YX_I_OVER.getValue();
            } else if (addrInfo == RELAY_OUT) {
                return DataTypeEnum.YX_RELAY_OUT.getValue();
            } else if (addrInfo == PROTECT_TEMP) {
                return DataTypeEnum.YX_PROTECT_TEMP.getValue();
            } else if (addrInfo == PROTECT_SHORT) {
                return DataTypeEnum.YX_PROTECT_SHORT.getValue();
            } else if (addrInfo == PROTECT_LEAK) {
                return DataTypeEnum.YX_PROTECT_LEAK.getValue();
            } else if (addrInfo == PROTECT_STOP) {
                return DataTypeEnum.YX_PROTECT_STOP.getValue();
            } else {
                return null;
            }
        }

        static Integer getYACode(int addrInfo) {
            if (addrInfo == YG_ZONG) {
                return DataTypeEnum.YA_YG_ZONG.getValue();
            } else {
                return null;
            }
        }
    }

    private static class INFO_ADDR_DC {

        static int       ME              = 0;
        static int       SP              = 0;
        static int       MD              = 0;
        /** 信息体定义，请注意顺序不能变动，必须与协议一致 **/
        static final int OUT_V           = ME++; // 充电机输出电压
        static final int OUT_I           = ME++; // 充电机输出电流
        static final int IN_V            = ME++; // 充电机输入电压
        static final int SOC             = ME++; // SOC
        static final int BAT_TEMP_LOW    = ME++; // 电池组最低温度
        static final int BAT_TEMP_HIGH   = ME++; // 电池组最高温度
        static final int TOTAL_TIME      = ME++; // 累计充电时间
        static final int WORKSTATUS      = ME++; // 工作状态
        static final int BMS_ERROR       = SP++; // BMS通信异常
        static final int DC_OUT_OVER     = SP++; // 直流母线输出过压告警
        static final int DC_OUT_LOSS     = SP++; // 直流母线输出欠压告警
        static final int IN_OVER         = SP++; // 充电机输入过压告警
        static final int IN_LOSS         = SP++; // 充电机输入欠压告警
        static final int BAT_CUR_OVER    = SP++; // 蓄电池充电过流告警
        static final int BAT_PNTEMP_OVER = SP++; // 蓄电池模块采样点过温告警
        static final int YG_ZONG         = MD++; // 有功总电能
        static final int BAT_LINKED      = SP++; // 是否连接电池
        static final int BAT_SV_HIGH     = ME++; // 单体电池最高电压
        static final int BAT_SV_LOW      = ME++; // 单体电池最低电压
        static final int DEV_LINKED_ERR  = SP++; // 充电机连接器故障
        static final int BMS_LOSS        = SP++; // BMS终止
        static final int DEV_TEMP_ERROR  = SP++; // 充电机内部温度故障
        static final int PROTECT_TEMP    = SP++; // 温度过高保护
        static final int PROTECT_SHORT   = SP++; // 短路保护
        static final int PROTECT_LEAK    = SP++; // 漏电保护
        static final int PROTECT_STOP    = SP++; // 急停开关触发

        /****************************************/
        static Integer getYCCode(int addrInfo) {
            if (addrInfo == OUT_V) {
                return DataTypeEnum.YC_OUT_V.getValue();
            } else if (addrInfo == OUT_I) {
                return DataTypeEnum.YC_OUT_I.getValue();
            } else if (addrInfo == IN_V) {
                return DataTypeEnum.YC_IN_V.getValue();
            } else if (addrInfo == SOC) {
                return DataTypeEnum.YC_SOC.getValue();
            } else if (addrInfo == BAT_TEMP_LOW) {
                return DataTypeEnum.YC_BAT_TEMP_LOW.getValue();
            } else if (addrInfo == BAT_TEMP_HIGH) {
                return DataTypeEnum.YC_BAT_TEMP_HIGH.getValue();
            } else if (addrInfo == TOTAL_TIME) {
                return DataTypeEnum.YC_TOTAL_TIME.getValue();
            } else if (addrInfo == WORKSTATUS) {
                return DataTypeEnum.YC_WORKSTATUS.getValue();
            } else if (addrInfo == BAT_SV_HIGH) {
                return DataTypeEnum.YC_BAT_SV_HIGH.getValue();
            } else if (addrInfo == BAT_SV_LOW) {
                return DataTypeEnum.YC_BAT_SV_LOW.getValue();
            } else {
                return null;
            }
        }

        static Integer getYXCode(int addrInfo) {
            if (addrInfo == BMS_ERROR) {
                return DataTypeEnum.YX_BMS_ERROR.getValue();
            } else if (addrInfo == DC_OUT_OVER) {
                return DataTypeEnum.YX_DC_OUT_OVER.getValue();
            } else if (addrInfo == DC_OUT_LOSS) {
                return DataTypeEnum.YX_DC_OUT_LOSS.getValue();
            } else if (addrInfo == IN_OVER) {
                return DataTypeEnum.YX_AC_IN_OVER.getValue();
            } else if (addrInfo == IN_LOSS) {
                return DataTypeEnum.YX_AC_IN_LOSS.getValue();
            } else if (addrInfo == BAT_CUR_OVER) {
                return DataTypeEnum.YX_BAT_CUR_OVER.getValue();
            } else if (addrInfo == BAT_PNTEMP_OVER) {
                return DataTypeEnum.YX_BAT_PNTEMP_OVER.getValue();
            } else if (addrInfo == BAT_LINKED) {
                return DataTypeEnum.YX_BAT_LINKED.getValue();
            } else if (addrInfo == DEV_LINKED_ERR) {
                return DataTypeEnum.YX_DEV_LINKED_ERR.getValue();
            } else if (addrInfo == BMS_LOSS) {
                return DataTypeEnum.YX_BMS_LOSS.getValue();
            } else if (addrInfo == DEV_TEMP_ERROR) {
                return DataTypeEnum.YX_DEV_TEMP_ERROR.getValue();
            } else if (addrInfo == PROTECT_TEMP) {
                return DataTypeEnum.YX_PROTECT_TEMP.getValue();
            } else if (addrInfo == PROTECT_SHORT) {
                return DataTypeEnum.YX_PROTECT_SHORT.getValue();
            } else if (addrInfo == PROTECT_LEAK) {
                return DataTypeEnum.YX_PROTECT_LEAK.getValue();
            } else if (addrInfo == PROTECT_STOP) {
                return DataTypeEnum.YX_PROTECT_STOP.getValue();
            } else {
                return null;
            }
        }

        static Integer getYACode(int addrInfo) {
            if (addrInfo == YG_ZONG) {
                return DataTypeEnum.YA_YG_ZONG.getValue();
            } else {
                return null;
            }
        }

    }

    private class BizTypeUp {

        static final int ReqFeeModel       = 1; // <1> 请求下发计费模型数据
        static final int ResultFeeModel    = 2; // <5> 下发计费模型结果数据
        static final int AppointmentLock   = 3; // 预约锁定上行应答数据
        static final int AppointmentCancel = 4; // 取消预约上行应答数据
        static final int RemoteStartResult = 5; // 启动充电应答上行据
        static final int ChargeEventStart  = 6; // 充电开始事件上行数据
        static final int RemoteStopResult  = 7; // 用户停止充电应答上行数据
        static final int ChargeEventEnd    = 8; // 充电结束事件上行数据
        static final int ChargeRec         = 9; // 充电消费记录上行数据
        static final int ChargeStartByCard = 10; // 充电卡启动充电请求
    }

    private class BizTypeDown {

        static final int FeeModel                 = 1; // <6> 下发计费模型下行数据
        static final int AppointmentLock          = 3; // 预约锁定下行数据
        static final int AppointmentCancel        = 4; // 取消预约下行数据
        static final int RemoteStart              = 5; // 启动充电下行数据
        static final int RemoteStop               = 7; // 用户停止充电
        static final int ChargeConfirm            = 9; // 消费记录确认下行数据
        static final int ChargeStartByCardConfirm = 10; // 充电卡启动充电请求应答
    }

    private void closeConnect(PileService pileService, ChannelService channel) {
        channel.closeChannel();
    }

    private int writeData_I(ChannelService channel, byte[] data) {
        W_104 = 0;
        needConfirm = true;
        return channel.writeData(data);
    }

    private int writeData_S(ChannelService channel, byte[] data) {
        W_104 = 0;
        return channel.writeData(data);
    }

    private void saveChargeRec(PileService pileService, PileDev pile, BizUpChargeRec rec) {

        // 超大数
        if (rec.energyMoney > 99999 || rec.flatElec > 99999 || rec.flatMoney > 99999 || rec.jianElec > 99999 || rec.jianMoney > 99999 || rec.peakElec > 99999
            || rec.peakMoney > 99999 || rec.serviceMoney > 99999 || rec.totalElec > 999999 || rec.valleyElec > 99999 || rec.valleyMoney > 99999 || rec.totalBegValue > 999999
            || rec.totalEndValue > 999999) {
            return;
        }
        // 记录数据库
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setChargeid(pile.getId());
        chargeRecord.setChargeType((short) (rec.userType == 1 ? 0 : 1));
        chargeRecord.setPileNum((short) (rec.chargeInterface));
        chargeRecord.setTradeNo(rec.tradeNo);
        chargeRecord.setPayNo(rec.userID);
        System.out.println("rec.tradeNo:" + rec.tradeNo);
        if (rec.userType == 1) {
            chargeRecord.setCardNo(rec.userID);
        } else {
            chargeRecord.setCardNo(rec.userID.substring(0, 16));
        }

        chargeRecord.setStartTime((rec.startTime).getTime());
        chargeRecord.setEndTime((rec.endTime).getTime());

        chargeRecord.setDlj(rec.jianElec);
        chargeRecord.setJej(new BigDecimal((rec.jianMoney) + ""));// JEJ 尖金额

        chargeRecord.setDlf(rec.peakElec);
        chargeRecord.setJef(new BigDecimal((rec.peakMoney) + ""));

        chargeRecord.setDlp(rec.flatElec);
        chargeRecord.setJep(new BigDecimal((rec.flatMoney) + ""));

        chargeRecord.setDlg(rec.valleyElec);
        chargeRecord.setJeg(new BigDecimal((rec.valleyMoney) + ""));

        chargeRecord.setDlz(rec.totalElec);
        chargeRecord.setBmz1(rec.totalBegValue);// BMZ1 总起示值
        chargeRecord.setBmz2(rec.totalEndValue);

        chargeRecord.setXfje(new BigDecimal((rec.energyMoney + rec.serviceMoney) + ""));
        chargeRecord.setPayStatus((short) rec.payFlag);
        chargeRecord.setPayMoney(new BigDecimal((rec.energyMoney + rec.serviceMoney) + ""));
        chargeRecord.setChaMoney(new BigDecimal((rec.serviceMoney) + ""));

        pileService.getDataBaseService().saveData(chargeRecord);

        if (rec.userType == 1) { // 系统账户方式
            PayMentRec pmr = new PayMentRec();
            User user = pileService.getUserbyPhone(rec.userID);
            if (user != null) {
                pmr.setUserId(user.getId());
            } else {
                pmr.setUserId(-1);
                logger.warn("Received UnRegister User's charge record!! phonenum=" + rec.userID + ",devCode=" + rec.devcode);
            }
            pmr.setPileId(pile.getId());
            pmr.setTradeNo(rec.tradeNo);
            pmr.setDealStatus(ChargeDealStatusEnum.SUCCESS.getShortValue());
            pmr.setParkFee(BigDecimal.ZERO);
            pmr.setChaFee(new BigDecimal(rec.energyMoney));
            pmr.setServiceFee(new BigDecimal(rec.serviceMoney));
            pmr.setStartTime(rec.startTime.getTime());
            pmr.setEndTime(rec.endTime.getTime());
            pmr.setChaLen((int) (rec.endTime.getTimeInMillis() - rec.startTime.getTimeInMillis()) / 60000);
            pmr.setChaPower(rec.totalElec);
            pmr.setShouldMoney(new BigDecimal(rec.energyMoney + rec.serviceMoney));
            pmr.setActualMoney(BigDecimal.ZERO);
            pmr.setChaMode((short) rec.chargeMode);
            pmr.setAccountInfo(rec.userID);// 2018-03-08 add
            if (pmr.getShouldMoney().equals(BigDecimal.ZERO)) {
                pmr.setPayStatus(ChargePayStatusEnum.SUCCESS.getShortValue());
            } else {
                pmr.setPayStatus(ChargePayStatusEnum.UNPAID.getShortValue());
            }
            pmr.setUpdateTime(Calendar.getInstance().getTime());
            pmr.setPlateNum(rec.plateNum);

            pileService.getDataBaseService().saveData(pmr);
        } else {
            PayMentRec pmr = new PayMentRec();
            Account account = pileService.getDataBaseService().getAccountByCardID(rec.userID.substring(0, 16));
            if (account != null) {
                pmr.setUserId(account.getUserId());
            } else {
                pmr.setUserId(-1);
                logger.warn("Received UnRegister User's charge record!! CPU CardID=" + rec.userID + ",devCode=" + rec.devcode);
            }
            pmr.setPileId(pile.getId());
            pmr.setTradeNo(rec.tradeNo);
            pmr.setDealStatus(ChargeDealStatusEnum.SUCCESS.getShortValue());
            pmr.setParkFee(BigDecimal.ZERO);
            pmr.setChaFee(new BigDecimal(rec.energyMoney));
            pmr.setServiceFee(new BigDecimal(rec.serviceMoney));
            pmr.setStartTime(rec.startTime.getTime());
            pmr.setEndTime(rec.endTime.getTime());
            pmr.setChaLen((int) (rec.endTime.getTimeInMillis() - rec.startTime.getTimeInMillis()) / 60000);
            pmr.setChaPower(rec.totalElec);
            pmr.setShouldMoney(new BigDecimal(rec.energyMoney + rec.serviceMoney));
            pmr.setActualMoney(BigDecimal.ZERO);
            pmr.setChaMode((short) rec.chargeMode);
            pmr.setAccountInfo(rec.userID.substring(0, 16));
            if (pmr.getShouldMoney().equals(BigDecimal.ZERO)) {
                pmr.setPayStatus(ChargePayStatusEnum.SUCCESS.getShortValue());
            } else {
                pmr.setPayStatus((short) rec.payFlag);
            }
            if (rec.userType == 2) {
                pmr.setPayWay((short) 6); // 充电卡
            } else if (rec.userType == 3) {
                pmr.setPayWay((short) 1); // 系统账户
            }
            pmr.setPlateNum(rec.plateNum);
            pmr.setUpdateTime(Calendar.getInstance().getTime());

            pileService.getDataBaseService().saveData(pmr);
        }
    }

    private void saveChargeRec(PileService pileService, PileDev pile, BizUpChargeEventStart ces, PileStatusBean bean) {

        // 记录数据库
        ChargeRecord chargeRecord = new ChargeRecord();
        chargeRecord.setChargeid(pile.getId());
        chargeRecord.setChargeType((short) (ces.userType == 1 ? 0 : 1));
        chargeRecord.setPileNum((short) (ces.chargeInterface));
        chargeRecord.setTradeNo(ces.tradeNo);
        chargeRecord.setPayNo(ces.userID);
        if (ces.userType == 1) {
            chargeRecord.setCardNo(ces.userID);
        } else {
            chargeRecord.setCardNo(ces.userID.substring(0, 16));
        }
        chargeRecord.setStartTime((ces.timeBegin).getTime());

        chargeRecord.setBmz1(ces.energyBegin);// BMZ1 总起示值

        pileService.getDataBaseService().saveData(chargeRecord);

        if (ces.userType == 1) { // 系统账户方式
            PayMentRec pmr = new PayMentRec();
            User user = pileService.getUserbyPhone(ces.userID);
            if (user != null) {
                pmr.setUserId(user.getId());
            } else {
                pmr.setUserId(-1);
                logger.warn("Received UnRegister User's charge record!! phonenum=" + ces.userID + ",devCode=" + ces.devcode);
            }
            pmr.setPileId(pile.getId());
            pmr.setTradeNo(ces.tradeNo);
            pmr.setDealStatus(ChargeDealStatusEnum.DEALING.getShortValue());
            pmr.setStartTime(ces.timeBegin.getTime());
            pmr.setPayStatus(ChargePayStatusEnum.UNPAID.getShortValue());
            pmr.setUpdateTime(Calendar.getInstance().getTime());

            pileService.getDataBaseService().saveData(pmr);

            if (bean != null && user != null) {
                bean.setUserid(user.getId());
                pileService.freshMemPileStatus(bean);
                if (bean.getAppstatus().equals(PileAppStatusEnum.ORDER_SUCCESS)) {
                    AppointmentRec appRec = new AppointmentRec();
                    appRec.setId(bean.getApprecordid());
                    appRec.setUserId(user.getId());
                    appRec.setPileId(pile.getId());
                    appRec.setAppStatus(AppointmentStatusEnum.EXECUTED.getShortValue());
                    appRec.setAddTime(Calendar.getInstance().getTime());
                    pileService.getDataBaseService().saveData(appRec);
                }

            }
        }
    }

    public enum WorkStatusEnum {

        OFFLINE(0, "离线"), ERROR(1, "故障"), IDLE(2, "空闲"), WORK(3, "充电"), ERROR_LOWV(4, "欠压故障 "), ERROR_OVERV(5, "过压故障 "), ERROR_OVERI(6, "过流故障 "), APPOINTMENT(8, "预约"),
        UPDATE(9, "升级中"), OPERATE(10, "操作");

        private final int    value;
        private final String text;

        WorkStatusEnum(int value, String text) {
            this.value = value;
            this.text = text;
        }

        public int getValue() {
            return value;
        }

        public String getText() {
            return text;
        }

        public static String getText(int value) {
            WorkStatusEnum devtype = getEnmuByValue(value);
            return devtype == null ? null : devtype.getText();
        }

        /**
         * 通过传入的字符串匹配枚举，传入值
         * 
         * @param value
         * @return
         */
        public static WorkStatusEnum getEnmuByValue(int value) {
            for (WorkStatusEnum devtype : WorkStatusEnum.values()) {
                if (value == devtype.getValue()) {
                    return devtype;
                }
            }
            return null;
        }
    }
}
