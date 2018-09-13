package com.holley.common.rocketmq;

public class Consumer {

    public static void main(String[] args) {
        MQMsgConsumer mq = new MQMsgConsumer();
        mq.init();

        // DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(MQConstants.CONSUMER_GROUP);
        // consumer.setNamesrvAddr("172.16.20.46:9876"); // 10.175.200.141:9876
        // consumer.setMessageModel(MessageModel.BROADCASTING);

        // try {
        // // 订阅PushTopic下Tag为push的消息
        // consumer.subscribe(MQConstants.TOPIC_DCSERVER, "*");
        // // 程序第一次启动从消息队列头取数据
        // consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        // consumer.registerMessageListener(new MessageListenerConcurrently() {
        //
        // public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext Context) {
        //
        // MessageExt msg = list.get(0);
        // System.out.println("topic=" + msg.getTopic() + ", tag=" + msg.getTags() + ", msgId=" + msg.getMsgId() + " " +
        // DateUtil.DateToLongStr(new Date()));
        // return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        // }
        // });
        // consumer.start();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
    }

}
