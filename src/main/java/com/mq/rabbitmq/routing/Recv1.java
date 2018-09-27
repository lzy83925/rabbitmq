package com.mq.rabbitmq.routing;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv1 {
    public static final String QUEUE_NAME="queue_routing_direct";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= ConnectionUtils.getConn();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.basicQos(1);
        channel.queueBind(QUEUE_NAME,Send.EXCHANGE_NAME,Send.ROUTING_KEY_ERROR);
        Consumer consumer = new DefaultConsumer(channel) {
            /**
             * 消息到达，触发本方法
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("[1] Recv1 msg:" + msg);
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
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);

    }
}
