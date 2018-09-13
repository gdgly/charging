package com.holley.charging.dcs.protocol;

import com.holley.charging.dcs.service.channel.ChannelService;
import com.holley.charging.dcs.service.device.PileDev;
import com.holley.charging.dcs.service.device.PileService;

public interface IProtocol {

    public Short getProtocolID();

    public void addCallBack(IProtocolCallBack callBack);

    public PileService registerProtocol(byte[] buffer, ChannelService channel);

    public int onReceive(byte[] msg, PileService pileService, ChannelService channel);

    public int onLogin(byte[] data, PileService pileService, ChannelService channel);

    public int sendHeart(PileService pileService, ChannelService channel);

    public int sendBizData(BizCmdTypeEnum cmdType, Object data, PileDev pileService, ChannelService channel);

    public void onTimer(PileService pileService, ChannelService channel);

}
