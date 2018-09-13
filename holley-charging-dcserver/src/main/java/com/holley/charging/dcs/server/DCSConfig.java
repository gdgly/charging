package com.holley.charging.dcs.server;

import java.util.ArrayList;
import java.util.List;

import com.holley.charging.dcs.constant.Global;
import com.holley.charging.dcs.constant.MediaTypeEnum;
import com.holley.charging.dcs.media.MediaPara;
import com.holley.common.util.StringUtil;

public class DCSConfig {

	public static DCSConfig sysConfig=null;
	
	public String locale="us_EN";
	
	private int devmanthreadnum=1;
	private int commthreadnum=1;
	private String portList;
	private List<MediaPara> serverMediaList=new ArrayList<MediaPara>();
		
	public String getPortList() {
		return portList;
	}
	public void setPortList(String portList) {
		this.portList = portList;
		if(portList!=null&&portList.length()>0){
			portList = StringUtil.replaceBlank(portList);
			String media[] = portList.split(",");
			if(media!=null){
				for(int i=0;i<media.length;i++){
					String ids[] = media[i].split(":");
					if(ids==null||ids.length<2){
						continue;
					}
					MediaPara para= new MediaPara();
					para.setPort(Integer.parseInt(ids[0]));
					if(Integer.parseInt(ids[1])==MediaTypeEnum.UDPSERVER.getValue()){
						para.setType(MediaTypeEnum.UDPSERVER);
					}else{
						para.setType(MediaTypeEnum.TCPSERVER);
					}
					if(ids.length>2){
						para.setProtocolID(Integer.parseInt(ids[2]));
					}
					serverMediaList.add(para);
				}
			}
		}
	}

	public List<MediaPara> getServerMediaList() {
		if(serverMediaList.size()==0){
			MediaPara para = new MediaPara();
			para.setPort(2407);
			para.setType(MediaTypeEnum.TCPSERVER);
			serverMediaList.add(para);
		}
		return serverMediaList;
	}

	public  String getLocale() {
		return locale;
	}

	public  void setLocale(String locale) {
		this.locale = locale;
	}

	public int getDevmanthreadnum() {
		return devmanthreadnum==0?Global.MAX_NUM_DEV_THREAD:devmanthreadnum;
	}

	public void setDevmanthreadnum(int devmanthreadnum) {
		this.devmanthreadnum = devmanthreadnum;
	}

	public int getCommthreadnum() {
		return commthreadnum==0?Global.MAX_NUM_CHAN_THREAD:commthreadnum;
	}

	public void setCommthreadnum(int commthreadnum) {
		this.commthreadnum = commthreadnum;
	}
	
	
}
