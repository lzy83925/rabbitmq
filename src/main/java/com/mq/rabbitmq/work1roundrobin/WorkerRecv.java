package com.mq.rabbitmq.work1roundrobin;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 自动发送回执
 * 一旦生产者将消息发出来，将会在内存中删除
 * 这种情况，如果消费者挂掉，将会导致消息丢失
 */
public class WorkerRecv {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConn();
        Channel channel = connection.createChannel();
        channel.queueDeclare(Send.QUEUE_NAME, false, false, false, null);
        Consumer consumer = new DefaultConsumer(channel) {
            /**
             * 消息到达，触发本方法
             */
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String msg = new String(body, "UTF-8");
                System.out.println("[1] Recv msg:" + msg);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("[1] Done");
                }
            }
        };
        //这个属性为自动应答，在本处可设置成自动，但是在后面的场景必须设置为手动
        boolean autoAck=true;
        channel.basicConsume(Send.QUEUE_NAME,autoAck,consumer);


    }
}
