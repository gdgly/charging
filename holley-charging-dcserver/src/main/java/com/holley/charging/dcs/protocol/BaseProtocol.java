package com.holley.charging.dcs.protocol;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.holley.charging.dcs.dao.model.PobPile;
import com.holley.charging.dcs.service.channel.ChannelService;
import com.holley.charging.dcs.service.task.TaskItem;

public class BaseProtocol {

    static short                                            trade_no    = 0;

    static Log                                              logger      = LogFactory.getLog(BaseProtocol.class.getName());

    private short                                           protocolid;
    private Hashtable<IProtocolCallBack, IProtocolCallBack> callBackMap = null;

    public final void addCallBack(IProtocolCallBack callBack) {
        if (callBackMap == null) {
            callBackMap = new Hashtable<IProtocolCallBack, IProtocolCallBack>();
        }
        if (callBackMap.contains(callBack)) {
            return;
        }
        callBackMap.put(callBack, callBack);
    }

    public void setProtocolid(short protocolid) {
        this.protocolid = protocolid;
    }

    public Short getProtocolID() {
        return protocolid;
    }

    public void onExplained(PobPile rtu, ChannelService chan, TaskItem task, byte[] data, int len, Object handle, boolean error) {

    }

}
