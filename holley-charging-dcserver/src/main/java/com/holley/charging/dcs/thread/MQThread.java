package com.holley.charging.dcs.thread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.holley.charging.dcs.constant.Global;
import com.holley.charging.dcs.service.message.MessageService;
import com.holley.common.rocketmq.MQMsgConsumer;

public class MQThread extends BaseThread {

    static Log         logger = LogFactory.getLog(ServerMonitorThread.class.getName());
    private MainThread mainThread;

    public MQThread(MainThread mainThread) {
        this.mainThread = mainThread;
    }

    @Override
    public void run() {
        logger.info("MQThread start! (" + Thread.currentThread().getName() + ")");
        MQMsgConsumer mq = new MQMsgConsumer();
        MessageService ms = new MessageService();
        ms.setPileManagerService(mainThread.getPileManagerService());
        mq.setMqListener(ms);
        mq.init();

        while (!bStopThread) {
            try {
                Thread.sleep(Global.INTERVAL_CHAN_THREAD);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // 消息处理服务
            if (mq == null) {
                mq = new MQMsgConsumer();
            }
            if (mq.alive() == false) {
                mq.setMqListener(ms);
                mq.init();
            }
        }

        super.run();
    }
}
