package com.holley.charging.dcs.media;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

import com.holley.charging.dcs.media.common.IMediaListener;
import com.holley.charging.dcs.media.common.ReceiveParameters;
import com.holley.charging.dcs.media.netty.MessageHandle;

public class TcpClient extends Media {

    EventLoopGroup group   = null;
    Channel        channel = null;

    @Override
    public synchronized boolean openDev() {
        if (isOpen()) {
            return true;
        }
        if (group == null) {
            group = new NioEventLoopGroup();
        }
        Bootstrap b = new Bootstrap();
        final MessageHandle handle = new MessageHandle();
        handle.setMedia(this);
        b.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline p = ch.pipeline();
                p.addLast(handle);
            }
        });
        // Start the client.
        try {
            channel = b.connect(mediaPara.getIp(), mediaPara.getPort()).sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
        return channel.isActive();
    }

    @Override
    public synchronized boolean closeDev() {
        if (!isOpen()) {
            return true;
        }
        try {
            channel.close().sync();
            if (group != null) {
                group.shutdownGracefully();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public <T> boolean readDev(ReceiveParameters<T> args) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isOpen() {
        return channel == null ? false : channel.isActive();
    }

    @Override
    public int writeDev(Object data, String receiver) {
        ByteBuf message = null;
        int len = 0;
        if (data instanceof byte[]) {
            len = ((byte[]) data).length;
            message = Unpooled.buffer(len);
            for (int i = 0; i < message.capacity(); i++) {
                message.writeByte(((byte[]) data)[i]);
            }
        }
        try {
            if (message != null) {
                channel.writeAndFlush(message).sync();
            }
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return len;
    }

    public final void setChannel(Channel chan) {
        this.channel = chan;
    }

    public synchronized void setListener(List<IMediaListener> list) {
        deviceListeners.clear();
        ;
        deviceListeners.addAll(list);
    }

    public static void main(String[] args) {
        TcpClient client = new TcpClient();
        MediaPara para = new MediaPara();
        para.setIp("183.129.224.70");
        para.setPort(8080);
        client.setMediaPara(para);
        // test.testCalendar();183.129.224.70:4880:8080http://116.62.57.33:2408/
        // test.testEnum();
        // test.testDate();
        // test.testPtr();
        client.openDev();
    }
}
