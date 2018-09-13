package com.holley.charging.dcs.service.task;

import java.util.Date;

public class TaskStatus {

	volatile byte		callTimes;			//已重试次数	
	volatile TaskResultType       exeResult = TaskResultType.NOTDEAL;			//采集任务执行结果
	volatile long       exeChanID;			//采集任务执行通道
	volatile long		sendTimes;			//发送次数
	
	Date	nextTime;					//下一帧开始时间，用于断点续传 
	long		nextPoint;					//下一帧开始表号，用于断点续传.（计量点号/设备号/信息体号/帧序号）
	Date		generalTime;				//采集任务产生的时间
	public void addCallTimes(){
		callTimes++;
	}
	public final byte getCallTimes() {
		return callTimes;
	}
	public final void setCallTimes(byte callTimes) {
		this.callTimes = callTimes;
	}
	
	public final TaskResultType getExeResult() {
		return exeResult;
	}
	public final void setExeResult(TaskResultType exeResult) {
		this.exeResult = exeResult;
	}
	public final long getExeChanID() {
		return exeChanID;
	}
	public final void setExeChanID(long exeChanID) {
		this.exeChanID = exeChanID;
	}
	public final long getSendTimes() {
		return sendTimes;
	}
	public final void setSendTimes(long sendTimes) {
		this.sendTimes = sendTimes;
	}
	public final Date getNextTime() {
		return nextTime;
	}
	public final void setNextTime(Date nextTime) {
		this.nextTime = nextTime;
	}
	public final long getNextPoint() {
		return nextPoint;
	}
	public final void setNextPoint(long nextPoint) {
		this.nextPoint = nextPoint;
	}
	public final Date getGeneralTime() {
		return generalTime;
	}
	public final void setGeneralTime(Date generalTime) {
		this.generalTime = generalTime;
	}
	
	
	
}
