package com.holley.charging.dcs.thread;


public class BaseThread extends Thread {

	protected volatile boolean bStopThread=false;
	
	public void exitThread(){
		bStopThread = true;
		Thread.currentThread().interrupt();
	}
}
