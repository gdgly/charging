package com.holley.charging.dcs.service.task;

import java.util.Calendar;
import java.util.Date;

public class TaskItem {
	private int id=0;
	private TaskAttribute taskAttribute;
	private TaskStatus taskStatus;
	
	// 透明转发任务
	private boolean isTransparent=false;
	private byte[] msg;
	
	public TaskItem(TaskAttribute attr,int id) {
		this.id=id;
		taskStatus = new TaskStatus(); 
		taskAttribute = new TaskAttribute();
		
		taskAttribute.copy(attr);
		if(taskAttribute.getMaxAssignTimes()==0){
			taskAttribute.setMaxAssignTimes((byte)1);
		}
		if(taskAttribute.getMaxSendTimes()==0){
			taskAttribute.setMaxSendTimes((byte)1);
		}
		if(taskAttribute.getMaxTimeout()==0){
			taskAttribute.setMaxAssignTimes((byte)1);
		}
		this.taskStatus.setGeneralTime(Calendar.getInstance().getTime());
		this.taskStatus.setNextTime((Date)taskAttribute.getBeginTime().clone());
		this.taskStatus.setNextPoint(taskAttribute.getBeginPoint());
	}
	
	
	public final boolean isTransparent() {
		return isTransparent;
	}


	public final void setTransparent(boolean isTransparent) {
		this.isTransparent = isTransparent;
	}


	public final byte[] getMsg() {
		return msg;
	}

	public final void setMsg(byte[] msg,int len) {
		this.msg= new byte[len];
		int j=len>msg.length?msg.length:len;
		for(int i=0;i<j;i++){
			this.msg[i]=msg[i];
		}
	}


	public final TaskAttribute getTaskAttribute() {
		return taskAttribute;
	}
	public final void setTaskAttribute(TaskAttribute taskAttribute) {
		this.taskAttribute = taskAttribute;
	}
	public final TaskStatus getTaskStatus() {
		return taskStatus;
	}
	public final void setTaskStatus(TaskStatus taskStatus) {
		this.taskStatus = taskStatus;
	}
	public boolean isTimeOut(Date curTM) {
		if(curTM==null){
			curTM = Calendar.getInstance().getTime();
		}
		return (curTM.getTime()-this.getTaskStatus().getGeneralTime().getTime())>this.getTaskAttribute().getMaxTimeout();
	}


	public final int getId() {
		return id;
	}	
}
