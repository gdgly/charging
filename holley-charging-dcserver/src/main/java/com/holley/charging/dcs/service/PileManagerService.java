package com.holley.charging.dcs.service;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import com.holley.charging.dcs.dao.model.PileFeeModel;
import com.holley.charging.dcs.dao.model.PobPile;
import com.holley.charging.dcs.protocol.BizCmdTypeEnum;
import com.holley.charging.dcs.service.device.PileDev;
import com.holley.charging.dcs.service.device.PileService;
import com.holley.charging.dcs.thread.MainThread;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.charge.PileAppStatusEnum;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.rocketmq.MsgBase;
import com.holley.common.rocketmq.charging.MsgAppointmentCancle;
import com.holley.common.rocketmq.charging.MsgAppointmentReq;
import com.holley.common.rocketmq.charging.MsgChargeStart;
import com.holley.common.rocketmq.charging.MsgChargeStop;

public class PileManagerService extends BaseService {

    private Hashtable<String, PileDev>      pobPileMap;
    private Hashtable<String, PileService>  pileServiceMap;
    private Hashtable<Integer, PileService> pileServiceMapID;

    private MainThread                      mainThread;
    static Log                              logger = LogFactory.getLog(PileManagerService.class.getName());

    public PileManagerService() {
        pobPileMap = new Hashtable<String, PileDev>();
        pileServiceMap = new Hashtable<String, PileService>();
        pileServiceMapID = new Hashtable<Integer, PileService>();
    }

    public void setMainThread(MainThread mainThread) {
        this.mainThread = mainThread;
    }

    public synchronized void onLoadPile(List<PobPile> list) {

        if (list == null || list.size() == 0) {
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            PileDev pile = new PileDev(list.get(i));
            if (pile.getZHComAddr() != null) {
                String key = pile.getZHComAddr().toUpperCase();
                boolean bNewStatus = true;
                if (pobPileMap.containsKey(key)) { // 参数修改
                    logger.info("充电桩参数更新! PileID=" + pile.getId());
                    if (pileServiceMap.containsKey(key)) { // 修改已经在线的桩的参数
                        PileService ps = pileServiceMap.get(key);
                        if (ps.hasPileDev(pile.getId())) { // 桩的设备地址没有变
                            bNewStatus = false;
                        }
                    }
                } else {
                    logger.info("新增充电桩参数! PileID=" + pile.getId());
                }
                if (bNewStatus) {
                    logger.info("新增或修改充电桩参数更新!---充电桩不在线  PileID=" + pile.getId());
                    pobPileMap.put(key, pile);
                    // 内存库创建记录
                    PileStatusBean bean = new PileStatusBean();
                    bean.setId(pile.getId());
                    bean.setStationid(pile.getStationId());
                    bean.setStatus(PileStatusEnum.OFFLINE);
                    bean.setAppstatus(PileAppStatusEnum.UNORDERABLE);
                    freshMemPileStatus(bean);
                } else {
                    logger.info("修改充电桩参数更新!---充电桩在线，断开连接  PileID=" + pile.getId());
                    PileDev oldPile = pobPileMap.get(key);
                    PileService ps = pileServiceMap.get(key);
                    if (pile.getComType() != oldPile.getComType()) { // 通讯协议变化,断开连接
                        ps.disconnect();
                    }
                    BeanUtils.copyProperties(pile, oldPile);
                    BeanUtils.copyProperties(pile, ps.getPile(Integer.parseInt(pile.getComSubAddr())));
                }
            }
        }
    }

    public int getPileNum() {
        return pobPileMap.size();
    }

    public PileService getPileServiceByCommAddr(String commAddr) {
        String key = commAddr.toUpperCase();
        PileService ps = null;
        for (int i = 0; i < 11; i++) {
            String ZHAddr = commAddr + String.format("%02d", i);
            PobPile pile = pobPileMap.get(ZHAddr);
            if (pile == null) {
                continue;
            }
            // 上线，更新内存库状态
            PileStatusBean bean = new PileStatusBean();
            bean.setId(pile.getId());
            bean.setStationid(pile.getStationId());
            bean.setIsonline((short) 1);
            bean.setStatus(PileStatusEnum.IDLE);
            if (pile.getIsApp() != null && pile.getIsApp() == 1) {
                bean.setAppstatus(PileAppStatusEnum.UNORDERED);
            } else {
                bean.setAppstatus(PileAppStatusEnum.UNORDERABLE);
            }
            freshMemPileStatus(bean);

            if (pileServiceMap.containsKey(ZHAddr)) {
                ps = pileServiceMap.get(ZHAddr);
            }
            if (ps == null) {
                ps = new PileService(pile);

                ps.setProtocol(mainThread.getProtocolService().createProtocol(pile.getComType()));
                ps.setDataBaseService(mainThread.getDataBaseService());
                ps.setManager(this);
                ps.init();
            } else {
                ps.addPile(pile);
            }
            pileServiceMap.put(ZHAddr, ps);
            pileServiceMapID.put(pile.getId(), ps);
        }
        return ps;
    }

    public void freshMemPileStatus(PileStatusBean bean) {
        freshMemPileStatus(bean, 3);
    }

    /**
     * @param bean
     * @param operator 1:insert,2:delete,other:update
     */
    public void freshMemPileStatus(PileStatusBean bean, int operator) {
        if (bean == null) {
            return;
        }
        if (operator == 2) { // delete
            return;
        }
        PileStatusBean oriBean = ChargingCacheUtil.getPileStatusBean(bean.getId());
        if (oriBean == null) {
            ChargingCacheUtil.setPileStatusBean(bean);
        } else if (operator != 1) {
            if (bean.getUpdatetime().before(oriBean.getUpdatetime())) {
                return;
            }
            if (bean.getAppstatus() != null) {
                oriBean.setAppstatus(bean.getAppstatus());
            }
            if (bean.getIsonline() != null) {
                oriBean.setIsonline(bean.getIsonline());
            }
            if (bean.getMoney() != null) {
                oriBean.setMoney(bean.getMoney());
            }
            if (bean.getOuti() != null) {
                oriBean.setOuti(bean.getOuti());
            }
            if (bean.getOutv() != null) {
                oriBean.setOutv(bean.getOutv());
            }
            if (bean.getPilecode() != null) {
                oriBean.setPilecode(bean.getPilecode());
            }
            if (bean.getStationid() != null) {
                oriBean.setStationid(bean.getStationid());
            }
            if (bean.getStatus() != null) {
                oriBean.setStatus(bean.getStatus());
            }
            if (bean.getChalen() != null) {
                oriBean.setChalen(bean.getChalen());
            }
            if (bean.getChapower() != null) {
                oriBean.setChapower(bean.getChapower());
            }
            if (bean.getSoc() != null) {
                oriBean.setSoc(bean.getSoc());
            }
            oriBean.setUpdatetime(bean.getUpdatetime());
            ChargingCacheUtil.setPileStatusBean(oriBean);
        }

    }

    public void dealMessage(List<MsgBase> msgs) {
        if (msgs == null || msgs.size() == 0) {
            return;
        }
        for (int i = 0; i < msgs.size(); i++) {
            MsgBase msg = msgs.get(i);
            if (msg == null) {
                continue;
            }
            if (msg instanceof MsgChargeStart) {
                MsgChargeStart msgChargeStart = (MsgChargeStart) msg;
                PileService ps = pileServiceMapID.get(msgChargeStart.getPileID());
                if (ps == null) {
                    continue;
                }
                ps.setRemoteChargeControl(msg, BizCmdTypeEnum.STARTCHARGE, msgChargeStart.getPileID());
            } else if (msg instanceof MsgChargeStop) {
                MsgChargeStop msgChargeStop = (MsgChargeStop) msg;
                PileService ps = pileServiceMapID.get(msgChargeStop.getPileID());
                if (ps == null) {
                    continue;
                }
                ps.setRemoteChargeControl(msg, BizCmdTypeEnum.STOPCHARGE, msgChargeStop.getPileID());
            } else if (msg instanceof MsgAppointmentReq) {
                MsgAppointmentReq msgAppointmentReq = (MsgAppointmentReq) msg;
                PileService ps = pileServiceMapID.get(msgAppointmentReq.getPileID());
                if (ps == null) {
                    continue;
                }
                ps.setRemoteChargeControl(msg, BizCmdTypeEnum.STARTAPPOINTMENT, msgAppointmentReq.getPileID());
            } else if (msg instanceof MsgAppointmentCancle) {
                MsgAppointmentCancle msgAppointmentCancle = (MsgAppointmentCancle) msg;
                PileService ps = pileServiceMapID.get(msgAppointmentCancle.getPileID());
                if (ps == null) {
                    continue;
                }
                ps.setRemoteChargeControl(msg, BizCmdTypeEnum.STOPAPPOINTMENT, msgAppointmentCancle.getPileID());
            }
        }

    }

    public void onOffLine(PileService pileService) {

        if (pileServiceMap != null) {
            pileServiceMap.remove(pileService.getCommAddr());
        }
    }

    public void onTimer() {
        if (pileServiceMap != null && pileServiceMap.size() > 0) {
            Enumeration<PileService> psEnum = pileServiceMap.elements();
            while (psEnum.hasMoreElements()) {
                PileService ps = psEnum.nextElement();
                ps.onTimer();
            }
        }
    }

    public void onLoadPileFeeModel(List<PileFeeModel> pileFeeModelList) {
        if (pileFeeModelList == null || pileFeeModelList.size() == 0) {
            return;
        }
        for (int i = 0; i < pileFeeModelList.size(); i++) {
            PileFeeModel pileFeeModel = pileFeeModelList.get(i);
            if (pileServiceMapID.containsKey(pileFeeModel.getPileId())) {
                PileService ps = pileServiceMapID.get(pileFeeModel.getPileId());
                if (ps != null) {
                    ps.updateFeeModel(pileFeeModel);
                }
            }
        }
    }

}
