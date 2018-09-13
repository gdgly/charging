package com.holley.charging.dcs.media;

import com.holley.charging.dcs.constant.MediaTypeEnum;

/**
 * @author armording
 *
 */
public class MediaPara {
	
	String ID;
	
	//网络专用
    String ip;
    Integer port;
    MediaTypeEnum type;
    Integer protocolID;
    
    
	public String getID() {
		return ip+":"+port;
	}
	
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getPort() {
		return port;
	}
	public void setPort(Integer port) {
		this.port = port;
	}
	public String getAddress() {
		if(this.ip==null||this.port==null){
			return null;
		}else{
			return ip+":"+port;
		}
	}
	public MediaTypeEnum getType() {
		return type;
	}
	public void setType(MediaTypeEnum type) {
		this.type = type;
	}

	public Integer getProtocolID() {
		return protocolID;
	}

	public void setProtocolID(Integer protocolID) {
		this.protocolID = protocolID;
	}   
    
}
