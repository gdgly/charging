package com.holley.charging.dcs.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.holley.charging.dcs.media.common.IMediaListener;
import com.holley.charging.dcs.protocol.IProtocol;
import com.holley.charging.dcs.service.channel.ChannelMonitor;
import com.holley.charging.dcs.service.channel.ChannelService;
import com.holley.charging.dcs.service.device.PileDev;
import com.holley.charging.dcs.service.device.PileService;
import com.holley.charging.dcs.thread.MainThread;

public class ChannelManagerService extends BaseService {

    static Log                        logger                = LogFactory.getLog(ChannelManagerService.class.getName());

    Hashtable<String, ChannelService> unregisterClientTable = new Hashtable<String, ChannelService>();
    Hashtable<String, ChannelService> registerClientTable   = new Hashtable<String, ChannelService>();
    List<ChannelMonitor>              monitorList           = new ArrayList<ChannelMonitor>();
    private MainThread                mainThread;

    public ChannelManagerService() {

    }

    public void onTimer() {
        Calendar now = Calendar.getInstance();
        now.add(Calendar.HOUR, -2);

        if (unregisterClientTable != null) {
            Iterator<Entry<String, ChannelService>> iter = unregisterClientTable.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, ChannelService> e = iter.next();
                ChannelService cs = e.getValue();
                if (cs != null) {
                    if (cs.getLastDataTime().before(now.getTime())) {
                        iter.remove();
                        cs.closeChannel();
                    }
                }
            }
        }
        if (registerClientTable != null) {
            logger.debug("Finding Zombie channel!  ");
            Iterator<Entry<String, ChannelService>> iter = registerClientTable.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, ChannelService> e = iter.next();
                ChannelService cs = e.getValue();
                if (cs != null) {
                    if (cs.getLastDataTime().before(now.getTime())) {
                        logger.debug("Finded Zombie channel!  ID=" + cs.getChannelID() + "  lastdatatime=" + cs.getLastDataTime());
                        iter.remove();
                        cs.closeChannel();
                    }
                }
            }
        }

    }

    public PileService linkPile(String commAddr, ChannelService channelIO) {
        PileService pileService = mainThread.getPileManagerService().getPileServiceByCommAddr(commAddr);
        if (pileService == null) {
            return null;
        }
        pileService.setChannel(channelIO);
        PileDev pile = pileService.getPile(0);
        registerClientTable.put(channelIO.getChannelID(), channelIO);

        unregisterClientTable.remove(channelIO.getChannelID());

        logger.info("Pile Register,PileID=" + pile.getId() + "	PileCode=" + pile.getPileCode() + "	PileCommAddr=" + pile.getComAddr().toUpperCase() + "	Channel="
                    + channelIO.getChannelID());
        return pileService;
    }

    public void onCloseChannel(PileService pileService, ChannelService channelIO) {
        registerClientTable.remove(channelIO.getChannelID());
    }

    public void setMainThread(MainThread mainThread) {
        this.mainThread = mainThread;
    }

    public IMediaListener createMonitor(IProtocol protocol) {
        ChannelMonitor monitor = new ChannelMonitor();
        monitor.setManager(this);
        if (protocol != null) {
            monitor.setProtocol(protocol);
        }
        monitorList.add(monitor);

        return monitor;
    }

    public void addUNRegisterClient(ChannelService io) {
        unregisterClientTable.put(io.getChannelID(), io);
    }

    public void removeUNRegisterClient(String key) {
        ChannelService cs = registerClientTable.get(key);
        if (cs != null) {
            cs.closeChannel();
        } else {
            unregisterClientTable.remove(key);
        }
    }
}
