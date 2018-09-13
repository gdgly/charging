package com.holley.charging.dcs.media;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

import com.holley.charging.dcs.media.common.ReceiveParameters;
import com.holley.charging.dcs.media.netty.MediaChannelInitializer;
import com.holley.charging.dcs.media.netty.MessageHandle;

public class TcpServer extends Media {

    ChannelFuture         server      = null;
    static EventLoopGroup bossGroup   = new NioEventLoopGroup(); // (1)
    static EventLoopGroup workerGroup = new NioEventLoopGroup(8);

    public static void main(String[] args) {
        TcpServer server = new TcpServer();
        MediaPara para = new MediaPara();
        para.setIp("192.168.1.103");
        para.setPort(8866);
        server.setMediaPara(para);
        // test.testCalendar();
        // test.testEnum();
        // test.testDate();
        // test.testPtr();

        System.out.println(server.openDev());
    }

    @Override
    public synchronized boolean openDev() {
        if (isOpen()) {
            return true;
        }
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class) // (3)
        .option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO)).childHandler(new MediaChannelInitializer(this));
        try {
            server = b.bind(mediaPara.getPort()).sync();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return server == null ? false : server.channel().isActive();

    }

    @Override
    public boolean closeDev() {
        if (server != null && server.channel().isActive()) {
            try {
                server.channel().closeFuture().sync();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                bossGroup.shutdownGracefully();
                workerGroup.shutdownGracefully();
            }
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
        return server == null ? false : server.channel().isActive();
    }

    @Override
    public int writeDev(Object data, String receiver) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void onActiveClient(ChannelHandlerContext ctx, boolean active) {

        MessageHandle handle = null;
        if (ctx.handler() instanceof MessageHandle) {
            handle = (MessageHandle) ctx.handler();
        } else {
            return;
        }
        // 客户端地址
        InetSocketAddress sa = (InetSocketAddress) ctx.channel().remoteAddress();
        TcpClient client = new TcpClient();
        MediaPara mp = new MediaPara();
        mp.setIp(sa.getAddress().toString().replace("/", ""));
        mp.setPort(sa.getPort());
        client.setMediaPara(mp);
        handle.setClientMedia(client);
        client.setChannel(ctx.channel());

        if (deviceListeners != null) {
            for (int i = 0; i < deviceListeners.size(); i++) {
                deviceListeners.get(i).onActive(client, active);
            }
        }
    }
}
