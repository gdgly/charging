package com.holley.charging.dcs.service.channel;


import java.util.Date;

public class ChannelRunStatus {
	volatile LinkStatus linkStatus;			//通道连接状态,enum LINKSTATUS中的值
	volatile long linkWaitTimeout;		//通道连接过程中的等待延时
	volatile long linkTimes;			//本通道被多个RTU使用次数，物理连接次数，可用于负载均衡,
	
	boolean use;				//使用过标志
	boolean openErr;				//打开通讯端口错误,0-正常,1-错误
	boolean lastOpenErr;			//上一次打开通讯端口错误,0-正常,1-错误
	long openFailCount;		//连续打开设备错误次数
	
	long linkFailCounts;		//通道物理连接失败次数,用于通道质量
	long recvFailCounts;		//无接收或接收校验错误次数,用于通道质量

	long sendNum;				//发送字节数
	long recvNum;				//接收字节数

	Date	lastOpenTime;		//最后一次打开时间
	Date	lastRecvTime;		//最后一次接收时间
	Date	lastSendTime;		//最后一次发送时间

	long cmdMaxWaitTime;		//命令下发后，缺省等待最大时间	

	public final LinkStatus getLinkStatus() {
		return linkStatus;
	}

	public final void setLinkStatus(LinkStatus linkStatus) {
		this.linkStatus = linkStatus;
	}

	public final long getLinkWaitTimeout() {
		return linkWaitTimeout;
	}

	public final void setLinkWaitTimeout(long linkWaitTimeout) {
		this.linkWaitTimeout = linkWaitTimeout;
	}

	public final long getLinkTimes() {
		return linkTimes;
	}

	public final void setLinkTimes(long linkTimes) {
		this.linkTimes = linkTimes;
	}

	public final boolean isUse() {
		return use;
	}

	public final void setUse(boolean use) {
		this.use = use;
	}

	public final boolean isOpenErr() {
		return openErr;
	}

	public final void setOpenErr(boolean openErr) {
		this.openErr = openErr;
	}

	public final boolean isLastOpenErr() {
		return lastOpenErr;
	}

	public final void setLastOpenErr(boolean lastOpenErr) {
		this.lastOpenErr = lastOpenErr;
	}

	public final long getOpenFailCount() {
		return openFailCount;
	}

	public final void setOpenFailCount(long openFailCount) {
		this.openFailCount = openFailCount;
	}

	public final long getLinkFailCounts() {
		return linkFailCounts;
	}

	public final void setLinkFailCounts(long linkFailCounts) {
		this.linkFailCounts = linkFailCounts;
	}

	public final long getRecvFailCounts() {
		return recvFailCounts;
	}

	public final void setRecvFailCounts(long recvFailCounts) {
		this.recvFailCounts = recvFailCounts;
	}

	public final long getSendNum() {
		return sendNum;
	}

	public final void setSendNum(long sendNum) {
		this.sendNum = sendNum;
	}

	public final long getRecvNum() {
		return recvNum;
	}

	public final void setRecvNum(long recvNum) {
		this.recvNum = recvNum;
	}

	public final Date getLastOpenTime() {
		return lastOpenTime;
	}

	public final void setLastOpenTime(Date lastOpenTime) {
		this.lastOpenTime = lastOpenTime;
	}

	public final Date getLastRecvTime() {
		return lastRecvTime;
	}

	public final void setLastRecvTime(Date lastRecvTime) {
		this.lastRecvTime = lastRecvTime;
	}

	public final Date getLastSendTime() {
		return lastSendTime;
	}

	public final void setLastSendTime(Date lastSendTime) {
		this.lastSendTime = lastSendTime;
	}

	public final long getCmdMaxWaitTime() {
		return cmdMaxWaitTime;
	}

	public final void setCmdMaxWaitTime(long cmdMaxWaitTime) {
		this.cmdMaxWaitTime = cmdMaxWaitTime;
	}


	public void addLinkFailCounts() {
		linkFailCounts++;
	}

	public void addRecvFailCounts() {
		this.recvFailCounts++;
	}
}
