package com.holley.charging.dcs.media.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.util.List;

import com.holley.charging.dcs.media.Media;
import com.holley.charging.dcs.media.TcpServer;
import com.holley.charging.dcs.media.common.IMediaListener;
import com.holley.charging.dcs.media.common.ReceiveEventArgs;

public class MessageHandle extends ChannelInboundHandlerAdapter {

    private Media media       = null;
    // 用于服务端时记录客户端连接
    private Media clientMedia = null;

    /**
     * Creates a client-side handler.
     */
    public MessageHandle() {
        super();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (media != null && media instanceof TcpServer) {
            ((TcpServer) media).onActiveClient(ctx, true);
        }
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        /*
         * if(clientMedia!=null){ List<IMediaListener> list = clientMedia.getDeviceListeners(); if(list!=null){ for(int
         * i=0;i<list.size();i++){ ByteBuf buffer = ((ByteBuf)msg).copy(); byte[] buf = new byte[buffer.capacity()];
         * buffer.getBytes(0, buf); list.get(i).onReceived(null,new ReceiveEventArgs(buf,null)); } } }
         */
        try {
            if (clientMedia != null) {
                List<IMediaListener> list = clientMedia.getDeviceListeners();
                if (list != null) {
                    ByteBuf buffer = null;
                    try {
                        buffer = ((ByteBuf) msg).copy();
                        byte[] buf = new byte[buffer.capacity()];
                        buffer.getBytes(0, buf);

                        for (int i = 0; i < list.size(); i++) {
                            list.get(i).onReceived(ctx, new ReceiveEventArgs(buf, null));
                        }
                    } finally {
                        ReferenceCountUtil.release(buffer);
                    }
                }
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
        // ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (media != null && media instanceof TcpServer) {
            ((TcpServer) media).onActiveClient(ctx, false);
        }
        super.channelInactive(ctx);
    }

    public void setMedia(Media media) {
        this.media = media;
    }

    public final void setClientMedia(Media clientMedia) {
        this.clientMedia = clientMedia;
    }

}
