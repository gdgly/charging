package com.holley.charging.dcs.media.netty;



import com.holley.charging.dcs.media.Media;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

public class MediaChannelInitializer extends ChannelInitializer<SocketChannel> {

	private Media media = null;
	public MediaChannelInitializer(Media media){
		super();
		this.media=media;
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline p = ch.pipeline();
		MessageHandle handle = new MessageHandle();
		handle.setMedia(this.media);
        p.addLast(handle);
	}

}
