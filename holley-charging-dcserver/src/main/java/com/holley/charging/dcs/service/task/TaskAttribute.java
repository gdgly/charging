package com.holley.charging.dcs.service.task;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TaskAttribute {
	long		rtuID;				//终端编码	
	TaskType		taskType;			//任务类型，定义见CTaskItem::TASKTYPE
	int 	manuTaskID = -1;	//手动任务时，对应数据库内的任务序号
	long		dataClass;			//数据类型
	long		dataPara1;			//数据参数1，具体数据具体含义
	long		dataPara2;			//数据参数2，具体数据具体含义
	String 	dataCode;		//数据类型对应的规约内的数据标示
	int profilePeriod=0;	//数据类型是曲线类的，曲线冻结时间间隔,单位分钟

	Date		beginTime;			//数据招读开始时间	
	Date		endTime;			//数据招读结束时间
	Date		lastDataTime;	//最后数据时间
	long		beginPoint;			//数据开始的点号
	byte		maxSendTimes;		//任务中单个命令最大重发次数
	byte		maxAssignTimes;		//整个任务连续重试几次
	long		maxTimeout;			//任务自产生后,若通道无法连通，任务持续有效性，秒

	ClientTask	fromClient;			//客户端标示信息
	byte[]		taskBuf;			//客户端报文,必须用new BYTE[]申请
	long		bfLen;
	public final long getRtuID() {
		return rtuID;
	}
	public final void setRtuID(long rtuID) {
		this.rtuID = rtuID;
	}

	public final TaskType getTaskType() {
		return taskType;
	}
	public final void setTaskType(TaskType taskType) {
		this.taskType = taskType;
	}
	public final long getDataClass() {
		return dataClass;
	}
	public final void setDataClass(long dataClass) {
		this.dataClass = dataClass;
	}
	public final long getDataPara1() {
		return dataPara1;
	}
	public final void setDataPara1(long dataPara1) {
		this.dataPara1 = dataPara1;
	}
	public final long getDataPara2() {
		return dataPara2;
	}
	public final void setDataPara2(long dataPara2) {
		this.dataPara2 = dataPara2;
	}
	public final Date getBeginTime() {
		return beginTime;
	}
	public final void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}
	public final Date getEndTime() {
		return endTime;
	}
	public final void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	public final long getBeginPoint() {
		return beginPoint;
	}
	public final void setBeginPoint(long beginPoint) {
		this.beginPoint = beginPoint;
	}
	public final byte getMaxSendTimes() {
		return maxSendTimes;
	}
	public final void setMaxSendTimes(byte maxSendTimes) {
		this.maxSendTimes = maxSendTimes;
	}
	public final byte getMaxAssignTimes() {
		return maxAssignTimes;
	}
	public final void setMaxAssignTimes(byte maxAssignTimes) {
		this.maxAssignTimes = maxAssignTimes;
	}
	public final long getMaxTimeout() {
		return maxTimeout;
	}
	public final void setMaxTimeout(long maxTimeout) {
		this.maxTimeout = maxTimeout;
	}
	public final ClientTask getFromClient() {
		return fromClient;
	}
	public final void setFromClient(ClientTask fromClient) {
		this.fromClient = fromClient;
	}
	public final byte[] getTaskBuf() {
		return taskBuf;
	}
	public final void setTaskBuf(byte[] taskBuf) {
		this.taskBuf = taskBuf;
	}
	public final long getBfLen() {
		return bfLen;
	}
	public final void setBfLen(long bfLen) {
		this.bfLen = bfLen;
	}
	
	public final String getDataCode() {
		return dataCode;
	}
	public final void setDataCode(String dataCode) {
		this.dataCode = dataCode;
	}
	
	public final Date getLastDataTime() {
		return lastDataTime;
	}
	public final void setLastDataTime(Date lastDataTime) {
		this.lastDataTime = lastDataTime;
	}
	
	public final int getProfilePeriod() {
		return profilePeriod;
	}
	public final void setProfilePeriod(int profilePeriod) {
		this.profilePeriod = profilePeriod;
	}
	
	public final int getManuTaskID() {
		return manuTaskID;
	}
	public final void setManuTaskID(int manuTaskID) {
		this.manuTaskID = manuTaskID;
	}
	public void copy(TaskAttribute attr) {
		if(attr==null){
			return;
		}
		this.beginPoint = attr.getBeginPoint();
		this.beginTime = attr.getBeginTime();
		this.bfLen = attr.getBfLen();
		this.dataClass = attr.getDataClass();
		this.dataPara1 = attr.getDataPara1();
		this.dataPara2 = attr.getDataPara2();
		this.endTime = attr.getEndTime();
		this.lastDataTime = attr.getLastDataTime();
		this.fromClient = attr.getFromClient();
		this.maxAssignTimes = attr.getMaxAssignTimes();
		this.maxSendTimes = attr.getMaxSendTimes();
		this.maxTimeout = attr.getMaxTimeout();
		this.rtuID = attr.getRtuID();
		this.dataCode = attr.getDataCode();
		if(attr.getTaskBuf()!=null&&attr.getTaskBuf().length>0){
			 this.taskBuf =Arrays.copyOf(attr.getTaskBuf(), attr.getTaskBuf().length);
		}else{
			this.taskBuf = null;
		}
		this.taskType = attr.getTaskType();
	}
}
