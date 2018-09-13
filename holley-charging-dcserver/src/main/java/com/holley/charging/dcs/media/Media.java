package com.holley.charging.dcs.media;

import java.util.ArrayList;
import java.util.List;

import com.holley.charging.dcs.media.common.IMediaListener;
import com.holley.charging.dcs.media.common.ReceiveParameters;

/**
 * 通讯设备的基类 实现对物理层设备的打开、关闭、读、写的访问
 * 
 * @author armording
 */
public abstract class Media {

    protected boolean              isopen;

    protected MediaPara            mediaPara;

    protected List<IMediaListener> deviceListeners = new ArrayList<IMediaListener>();

    public abstract boolean openDev();

    public abstract boolean closeDev();

    public MediaPara getMediaPara() {
        return mediaPara;
    }

    public void setMediaPara(MediaPara mediaPara) {
        this.mediaPara = mediaPara;
    }

    public abstract int writeDev(final Object data, String receiver);

    /*
     * 同步读取
     */
    public abstract <T> boolean readDev(ReceiveParameters<T> args);

    public boolean isOpen() {
        return isopen;
    }

    public void addListener(IMediaListener listener) {
        deviceListeners.add(listener);
    }

    public final List<IMediaListener> getDeviceListeners() {
        return deviceListeners;
    }

}
