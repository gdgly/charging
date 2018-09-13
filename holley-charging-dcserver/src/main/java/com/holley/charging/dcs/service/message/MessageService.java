package com.holley.charging.dcs.service.message;

import java.util.List;

import com.holley.charging.dcs.service.BaseService;
import com.holley.charging.dcs.service.PileManagerService;
import com.holley.common.rocketmq.MQListener;
import com.holley.common.rocketmq.MsgBase;

public class MessageService extends BaseService implements MQListener {

	private PileManagerService pileManagerService;
	
	@Override
	public int consumeMessage(List<MsgBase> msgs) {
		if(msgs==null||msgs.size()==0){
			return 0;
		}
		pileManagerService.dealMessage(msgs);
		return 0;
	}

	public void setPileManagerService(PileManagerService pileManagerService) {
		this.pileManagerService = pileManagerService;
	}

	
}
