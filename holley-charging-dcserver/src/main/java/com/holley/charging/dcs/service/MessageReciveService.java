package com.holley.charging.dcs.service;

import java.util.ArrayList;
import java.util.List;

import com.holley.common.jms.MessageDeal;
import com.holley.common.rocketmq.MsgBase;

/**
 * 消息中间件接受的消息命令
 * 
 * @author zhoulide
 */
public class MessageReciveService implements MessageDeal {

    private PileManagerService pileManagerService;

    @Override
    public boolean deal(Object msg) {
        if (msg instanceof MsgBase) {
            MsgBase msgDeal = (MsgBase) msg;
            List<MsgBase> list = new ArrayList<MsgBase>();
            list.add(msgDeal);
            pileManagerService.dealMessage(list);

        }
        return false;
    }

    public void setPileManagerService(PileManagerService pileManagerService) {
        this.pileManagerService = pileManagerService;
    }

}
