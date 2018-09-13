package com.holley.charging.dcs.service.device;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.holley.charging.dcs.dao.model.PileFeeModel;
import com.holley.charging.dcs.dao.model.PobPile;
import com.holley.charging.dcs.dao.model.User;
import com.holley.charging.dcs.database.IDataBaseService;
import com.holley.charging.dcs.protocol.BizCmdTypeEnum;
import com.holley.charging.dcs.protocol.IProtocol;
import com.holley.charging.dcs.service.BaseService;
import com.holley.charging.dcs.service.PileManagerService;
import com.holley.charging.dcs.service.channel.ChannelService;
import com.holley.common.cache.charging.CacheKeyProvide.KeySessionTypeEnum;
import com.holley.common.cache.charging.ChargingCacheUtil;
import com.holley.common.constants.charge.PileStatusEnum;
import com.holley.common.dataobject.PileStatusBean;
import com.holley.common.dataobject.WebUser;

public class PileService extends BaseService {

    IProtocol                           protocol          = null;
    private Hashtable<Integer, PileDev> pileMap           = new Hashtable<Integer, PileDev>();

    ChannelService                      channel           = null;
    IDataBaseService                    dataBaseService   = null;
    PileManagerService                  manager;

    String                              commAddr;
    boolean                             bNeedDownFeeModel = false;

    public boolean isbNeedDownFeeModel() {
        return bNeedDownFeeModel;
    }

    public void setbNeedDownFeeModel(boolean bNeedDownFeeModel) {
        this.bNeedDownFeeModel = bNeedDownFeeModel;
    }

    public PileService() {

    }

    public PileService(PobPile pobPile) {
        this();
        PileDev pile = new PileDev();
        BeanUtils.copyProperties(pobPile, pile);
        pileMap.put(pile.getId(), pile);
        this.commAddr = pile.getComAddr();
    }

    public PileService(PileDev pile) {
        this();
        pileMap.put(pile.getId(), pile);
        this.commAddr = pile.getComAddr();
    }

    public void init() {
        if (dataBaseService != null) {
            Enumeration<PileDev> en2 = pileMap.elements();
            while (en2.hasMoreElements()) {
                PileDev pile = en2.nextElement();
                List<PileFeeModel> list = dataBaseService.loadPileFeeModel(pile.getId());
                if (list != null) {
                    pile.getPfmList().clear();
                    pile.getPfmList().addAll(list);
                }
            }
        }
    }

    public PileFeeModel getUpdatePFM(Date time, int subAddr) {
        PileFeeModel pfm = null;
        Enumeration<PileDev> en2 = pileMap.elements();
        while (en2.hasMoreElements()) {
            PileDev pile = en2.nextElement();
            Calendar now = Calendar.getInstance();
            if (time == null) {
                time = new Date(0);
            }
            for (int i = 0; i < pile.getPfmList().size(); i++) {
                if (pile.getPfmList().get(i).getStatus() != 1) {
                    continue;
                }
                if (pile.getPfmList().get(i).getAddTime().before(time)) {
                    continue;
                }
                if (pfm == null || pfm.getActiveTime().after(pile.getPfmList().get(i).getActiveTime())) {
                    pfm = pile.getPfmList().get(i);
                    break;
                }
            }
            int temp = Integer.parseInt(pile.getComSubAddr());
            if (temp == subAddr) {
                return pfm;
            }
        }

        return pfm;
    }

    public PileDev getPile(int chargeInterface) {
        Enumeration<PileDev> en2 = pileMap.elements();
        PileDev pile = null;
        while (en2.hasMoreElements()) {
            pile = en2.nextElement();
            int temp = Integer.parseInt(pile.getComSubAddr());
            if (temp == chargeInterface) {
                return pile;
            }
        }
        return pile;
    }

    /*
     * public void setPile(PileDev pile) { this.pile = pile; }
     */
    public void notifyLogin(byte[] data) {
        if (channel == null || protocol == null) {
            return;
        }
        protocol.onLogin(data, this, channel);
    }

    public IProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(IProtocol protocol) {
        this.protocol = protocol;
    }

    public void setChannel(ChannelService channel) {
        this.channel = channel;
    }

    public IDataBaseService getDataBaseService() {
        return dataBaseService;
    }

    public void setDataBaseService(IDataBaseService dataBaseService) {
        this.dataBaseService = dataBaseService;
    }

    public void setManager(PileManagerService pileManagerService) {
        this.manager = pileManagerService;
    }

    public int onReceive(byte[] msg) {
        if (protocol == null) {
            return 0;
        } else {
            return protocol.onReceive(msg, this, channel);
        }
    }

    public void onCloseChannel() {
        // 刷新内存状态
        Enumeration<PileDev> en2 = pileMap.elements();
        while (en2.hasMoreElements()) {
            PileDev pile = en2.nextElement();
            PileStatusBean bean = ChargingCacheUtil.getPileStatusBean(pile.getId());
            if (bean != null) {
                bean.setStatus(PileStatusEnum.OFFLINE);
                bean.setIsonline((short) 0);
                bean.setUpdatetime(Calendar.getInstance().getTime());
                freshMemPileStatus(bean);
            }
        }
        this.channel = null;
        manager.onOffLine(this);
    }

    public int setRemoteChargeControl(Object data, BizCmdTypeEnum cmdType, int pileID) {
        if (protocol == null || pileMap.get(pileID) == null) {
            return 0;
        } else {
            return protocol.sendBizData(cmdType, data, pileMap.get(pileID), channel);
        }
    }

    public void freshMemPileStatus(PileStatusBean bean) {
        manager.freshMemPileStatus(bean);
    }

    public void onTimer() {
        if (protocol != null) {
            protocol.onTimer(this, channel);
        }
    }

    public WebUser getUserInfo(Integer userID) {
        return ChargingCacheUtil.getSession(userID.toString(), KeySessionTypeEnum.APP, null);
    }

    public User getUserbyPhone(String phoneNum) {
        return dataBaseService.getUserByPhone(phoneNum);
    }

    public void disconnect() {
        if (channel != null && channel.isOpen()) {
            channel.closeChannel();
        }
    }

    public String getCommAddr() {
        return commAddr;
    }

    public void setCommAddr(String commAddr) {
        this.commAddr = commAddr;
    }

    public boolean hasPileDev(Integer id) {
        Enumeration<PileDev> en2 = pileMap.elements();
        while (en2.hasMoreElements()) {
            PileDev pile = en2.nextElement();
            if (pile.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public void addPile(PobPile pile) {
        PileDev pileDev = new PileDev();
        BeanUtils.copyProperties(pile, pileDev);
        pileMap.put(pile.getId(), pileDev);
    }

    public void updateFeeModel(PileFeeModel pileFeeModel) {
        Enumeration<PileDev> en2 = pileMap.elements();
        while (en2.hasMoreElements()) {
            PileDev pile = en2.nextElement();
            if (pile.getId() != pileFeeModel.getPileId()) {
                continue;
            }
            pile.getPfmList().clear();
            pile.getPfmList().add(pileFeeModel);
            bNeedDownFeeModel = true;
        }

    }
}
