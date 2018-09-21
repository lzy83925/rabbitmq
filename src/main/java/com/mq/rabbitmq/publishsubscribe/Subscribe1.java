package com.mq.rabbitmq.publishsubscribe;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.mq.rabbitmq.work2fair.Send;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Subscribe1 {
    public static final String QUEUE_NAME="ps_queue_sms";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= ConnectionUtils.getConn();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME,PublishSend.EXCHANGE_NAME,"");
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
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);


    }
}
