package com.holley.charging.dcs.service.channel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.holley.charging.dcs.media.TcpClient;
import com.holley.charging.dcs.media.common.IMediaListener;
import com.holley.charging.dcs.media.common.ReceiveEventArgs;
import com.holley.charging.dcs.protocol.IProtocol;
import com.holley.charging.dcs.service.ChannelManagerService;

public class ChannelMonitor implements IMediaListener {

    static Log                    logger   = LogFactory.getLog(ChannelService.class.getName());

    private ChannelManagerService manager  = null;
    private IProtocol             protocol = null;

    public IProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(IProtocol protocol) {
        this.protocol = protocol;
    }

    @Override
    public void onReceived(Object sender, ReceiveEventArgs e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onActive(Object media, boolean active) {
        if (media instanceof TcpClient) {
            TcpClient client = (TcpClient) media;
            String key = client.getMediaPara().getID();
            if (active == true) {
                ChannelService io = new ChannelService();
                io.setManager(manager);
                io.setChannelID(key);
                io.setMedia(client);
                io.setProtocol(protocol);
                client.addListener(io);
                manager.addUNRegisterClient(io);

                logger.info(key + ": connected!");
            } else {
                manager.removeUNRegisterClient(key);
                logger.info(key + ": disconnected!");
            }

        }
    }

    public ChannelManagerService getManager() {
        return manager;
    }

    public void setManager(ChannelManagerService manager) {
        this.manager = manager;
    }

}
