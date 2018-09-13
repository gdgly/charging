package com.holley.charging.dcs.service.channel;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.holley.charging.dcs.constant.Global;
import com.holley.charging.dcs.media.Media;
import com.holley.charging.dcs.media.common.IMediaListener;
import com.holley.charging.dcs.media.common.ReceiveEventArgs;
import com.holley.charging.dcs.protocol.IProtocol;
import com.holley.charging.dcs.service.ChannelManagerService;
import com.holley.charging.dcs.service.device.PileService;
import com.holley.common.constants.Globals;
import com.holley.common.util.ProtocolUtils;

public class ChannelService implements IMediaListener {

    static Log                    logger       = LogFactory.getLog(ChannelService.class.getName());

    private ChannelRunStatus      runStatus;
    private Media                 media        = null;
    private PileService           pileService  = null;
    private ChannelManagerService manager      = null;
    private String                channelID;

    private int                   readPtr      = 0;
    private int                   oldReadPtr   = 0;
    private int                   readDealPtr  = 0;

    private int                   sendPtr      = 0;
    private int                   sendDealPtr  = 0;

    private byte                  sendBuff[]   = new byte[Globals.MAX_LEN_RECV];
    private byte                  recvBuff[]   = new byte[Globals.MAX_LEN_RECV];

    volatile long                 sendCounts   = 0;
    volatile long                 recvCounts   = 0;

    private Date                  lastDataTime = Calendar.getInstance().getTime();
    private IProtocol             protocol     = null;

    public ChannelService() {
        runStatus = new ChannelRunStatus();
        runStatus.setCmdMaxWaitTime(Global.MAX_CHANCMD_WAITTIME);
    }

    public final ChannelRunStatus getRunStatus() {
        return runStatus;
    }

    public final void setRunStatus(ChannelRunStatus runStatus) {
        this.runStatus = runStatus;
    }

    public IProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(IProtocol protocol) {
        this.protocol = protocol;
    }

    public void timeProc() {
        if (runStatus.getLinkWaitTimeout() != 0) {
            runStatus.setLinkWaitTimeout(runStatus.getLinkWaitTimeout() - 1);
        }

        if (isOpen()) {
            readDevice();
            writeData();
        }
    }

    private void readDevice() {

    }

    public boolean isOpen() {
        return media == null ? false : media.isOpen();
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public void closeChannel() {
        this.recvBuff = null;
        this.sendBuff = null;
        if (this.media == null) {
            return;
        }
        try {
            this.media.closeDev();
        } catch (Exception e) {
            logger.info("关闭连接异常！");
        }
        manager.onCloseChannel(pileService, this);
        if (pileService != null) {
            pileService.onCloseChannel();
            pileService = null;
        }
        manager = null;
        protocol = null;

    }

    private void writeData() {
        if (this.media == null || media.isOpen() == false) {
            return;
        }
        if (sendDealPtr == sendPtr) {
            return;
        }
        byte data[] = new byte[ProtocolUtils.cyclenth(sendPtr, sendDealPtr)];
        int pos = 0, dealPtr = sendDealPtr;
        while (dealPtr != sendPtr) {
            data[pos] = sendBuff[dealPtr];
            dealPtr = ProtocolUtils.cycpos(dealPtr, 1);
            pos++;
            if (pos >= data.length) {
                break;
            }
        }
        int sendNum = writeData(data);
        runStatus.setSendNum(runStatus.getSendNum() + sendNum);
        sendDealPtr = ProtocolUtils.cycpos(sendDealPtr, sendNum);
        runStatus.setLastSendTime(Calendar.getInstance().getTime());

    }

    public int writeData(byte[] data) {
        if (data == null || this.media == null || media.isOpen() == false) {
            return 0;
        }
        logger.debug("Channel_Send:" + ProtocolUtils.printMsg(0, data.length, data));
        return media.writeDev(data, pileService == null ? "" : pileService.getCommAddr());
    }

    public void onReceived(Object sender, ReceiveEventArgs e) {
        if (e == null || e.getData() == null) {
            return;
        }
        byte[] data = (byte[]) e.getData();
        int recvNum = data.length, i = 0;

        if (recvNum == 0) {
            return;
        }
        for (i = 0; i < recvNum; i++) {
            recvBuff[readPtr] = data[i];
            readPtr = ProtocolUtils.cycpos(readPtr, 1);
            if (readPtr == readDealPtr) {
                @SuppressWarnings("unused")
                int error = 1;
                error++;
            }
        }
        runStatus.setRecvNum(runStatus.getRecvNum() + recvNum);
        runStatus.setLastRecvTime(Calendar.getInstance().getTime());

        logger.debug("Channel_Recv:" + "(" + readPtr + ")" + ProtocolUtils.printMsg(0, recvNum, data));
        if (pileService == null) {
            if (protocol != null) {
                int len = ProtocolUtils.cyclenth(readPtr, readDealPtr);
                byte[] buffer = getAbsoluteRecvData(len);
                pileService = protocol.registerProtocol(buffer, this);
            } else {
                handleNBLogin();
            }
        } else {
            int len = ProtocolUtils.cyclenth(readPtr, readDealPtr);
            byte[] msg = getAbsoluteRecvData(len);
            int dealLen = pileService.onReceive(msg);
            setReadDealPtr(getReadDealPtr() + dealLen);
        }

    }

    private boolean handleNBLogin() {

        int len = ProtocolUtils.cyclenth(readPtr, readDealPtr);

        if (len < 17) {
            return false;
        }
        byte[] data = getAbsoluteRecvData(len);
        if (pileService == null) {
            int index = 0;
            for (index = 0; index <= data.length - 17; index++) {
                /*
                 * 起始标识 1Byte BIN码 固定68H 长度 2Byte BIN码 固定为 0EH 启动帧标识 1Byte BIN码 固定为 FF 协议版本 1Byte 压缩BCD码 保留为02 设备编号
                 * 8Byte 压缩BCD码 充电设备编号 充电接口数量 1Byte 压缩BCD码 充电模式 1Byte BIN码 站地址 2Byte 压缩BCD码 站地址
                 */
                if (data[index] == 0x68 && data[index + 1] == 0x0E && data[index + 2] == 0x0 && ProtocolUtils.Unsignd(data[index + 3]) == 0xFF && data[index + 4] == 0x02) { // 注册帧
                    // 设备编号
                    String devaddr = ProtocolUtils.getByteToHexStringDesc(data, index + 5, 8, "");
                    pileService = manager.linkPile(devaddr, this);
                    if (pileService == null) {
                        continue;
                    }

                    byte[] reply = new byte[17];
                    System.arraycopy(data, index, reply, 0, 17);
                    writeData(reply);
                    setReadDealPtr(getReadDealPtr() + index + 17);
                    pileService.notifyLogin(data);
                    return true;
                }
            }
            return false;
        }

        return false;
    }

    public final int getReadPtr() {
        return readPtr;
    }

    public final void setReadPtr(int readPtr) {
        this.readPtr = readPtr;
    }

    public final int getOldReadPtr() {
        return oldReadPtr;
    }

    public final void setOldReadPtr(int oldReadPtr) {
        this.oldReadPtr = oldReadPtr;
    }

    public final int getReadDealPtr() {
        return readDealPtr;
    }

    public final void setReadDealPtr(int readDealPtr) {
        this.readDealPtr = readDealPtr % Globals.MAX_LEN_RECV;
    }

    public final int getSendPtr() {
        return sendPtr;
    }

    public final void setSendPtr(int sendPtr) {
        this.sendPtr = sendPtr;
    }

    public final int getSendDealPtr() {
        return sendDealPtr;
    }

    public final void setSendDealPtr(int sendDealPtr) {
        this.sendDealPtr = sendDealPtr;
    }

    public long getCmdMaxWaitTime() {
        return runStatus.getCmdMaxWaitTime();
    }

    public byte[] getSendData() {
        return sendBuff;
    }

    public void sendData(byte[] data, int len) {
        for (int i = 0; i < len; i++) {
            sendBuff[sendPtr] = data[i];
            sendPtr = ProtocolUtils.cycpos(sendPtr, 1);
        }
    }

    public byte[] getRecvData() {
        return recvBuff;
    }

    public byte[] getAbsoluteRecvData(int len) {
        int curreadPtr = readPtr;
        int recvLen = ProtocolUtils.cyclenth(curreadPtr, readDealPtr);
        byte[] buf = new byte[Math.min(recvLen, len)];
        for (int i = 0; i < Math.min(recvLen, len); i++) {
            buf[i] = recvBuff[ProtocolUtils.cycpos(readDealPtr, i)];
        }
        return buf;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        ChannelService io = new ChannelService();
        io.setMedia(this.media);
        io.setRunStatus(this.getRunStatus());
        return io;
    }

    public final Media getMedia() {
        return media;
    }

    public final Date getLastDataTime() {
        return runStatus.lastRecvTime == null ? lastDataTime : runStatus.lastRecvTime;
    }

    public final void setLastDataTime(Date lastDataTime) {
        this.lastDataTime = lastDataTime;
    }

    public final long getSendCounts() {
        return sendCounts;
    }

    public final long getRecvCounts() {
        return recvCounts;
    }

    public void addRecvCounts(int num) {
        recvCounts += num;
        if (recvCounts < 0) {
            recvCounts = num;
        }
    }

    public void addSendCounts(int num) {
        sendCounts += num;
        if (sendCounts < 0) {
            sendCounts = num;
        }
    }

    public void setManager(ChannelManagerService channelService) {
        manager = channelService;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    @Override
    public void onActive(Object media, boolean active) {
        // TODO Auto-generated method stub

    }

    public PileService linkPile(String devaddr) {
        return manager.linkPile(devaddr, this);
    }

    public PileService getPileService() {
        return pileService;
    }

}
