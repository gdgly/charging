package com.holley.charging.dcs.media.netty;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		ByteBuf in = (ByteBuf) msg;
		
		try {
	        /*while (in.isReadable()) { // (1)
	            System.out.println((char) in.readByte());
	            System.out.flush();
	        }*/
			System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII));
	        ctx.writeAndFlush(msg);
	    } finally {
	       // ReferenceCountUtil.release(msg); // (2)
	    }
		
		super.channelRead(ctx, msg);
	}

	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		cause.printStackTrace();
        ctx.close();
	}
	
	
	

	
}
