package com.holley.charging.dcs.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.holley.charging.dcs.constant.Global;
import com.holley.charging.dcs.media.MediaPara;
import com.holley.charging.dcs.media.TcpServer;
import com.holley.charging.dcs.protocol.IProtocol;

public class ServerMonitorThread extends BaseThread {

	static Log logger = LogFactory.getLog(ServerMonitorThread.class.getName());
	private MainThread mainThread;
	private MediaPara para=null;
	public ServerMonitorThread(MainThread mainThread) {
		this.mainThread = mainThread;
	}

	public void setPara(MediaPara para) {
		this.para = para;
	}

	public MediaPara getPara() {
		return para;
	}

	@Override
	public void run() {
		if(para==null||mainThread==null){
			logger.error("system error!");
			super.run();
			return;
		}
		logger.info("ServerManThread start! ("+Thread.currentThread().getName()+")");
		TcpServer server = new TcpServer();
		server.setMediaPara(para);
		IProtocol protocol = mainThread.getProtocolService().getProtocol(para.getProtocolID().shortValue());
		server.addListener(mainThread.getChannelService().createMonitor(protocol));
		while(!bStopThread){
				
			if(server.isOpen()==false){
				server.openDev();		
			}	
			try {
				Thread.sleep(Global.INTERVAL_CHAN_THREAD);
			} catch (InterruptedException e) {
				continue;
			}
		}
		logger.info("ServerManThread exit! ("+Thread.currentThread().getName()+")");
		super.run();
	}

}
