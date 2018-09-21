package com.mq.rabbitmq.work2fair;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 公平分发，能者多劳
 * 生产者会把信息发给多个消费者，如果有一个消费者挂掉，就会交付给其他消费者
 * 消息者会产生应答，告诉rabbitmq生产者这条消息我已经处理完成，你条消息你可以删除掉了acknowledge   ack
 * 目前模式都上存在内存中
 */
public class WorkerRecv1 {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConn();
        Channel channel = connection.createChannel();
        channel.queueDeclare(Send.QUEUE_NAME, Send.DURABLE, false, false, null);
        /**
         * 与生产者对应
         * 保证一次只分发一个
         */
        channel.basicQos(Send.PRE_FETCH_COUNT);

        Consumer consumer = new DefaultConsumer(channel) {
            /**
             * 消息到达，触发本方法
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("[1] Recv msg:" + msg);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("[1] Done");
                    /**
                     * 手动回执消息
                     */
                    channel.basicAck(envelope.getDeliveryTag(),false);
                }
            }
        };
        //此场景必须设置为手动
        boolean autoAck=false;
        channel.basicConsume(Send.QUEUE_NAME,autoAck,consumer);


    }
}
