package com.holley.charging.dcs.protocol;


import java.util.List;

import com.holley.charging.dcs.dao.model.PobPile;
import com.holley.charging.dcs.service.channel.ChannelService;
import com.holley.charging.dcs.service.task.TaskItem;


public interface IProtocolCallBack {
	public void onExplained(PobPile pile, ChannelService chan, TaskItem task, byte[] data, int len,Object userdata,boolean error);
}
