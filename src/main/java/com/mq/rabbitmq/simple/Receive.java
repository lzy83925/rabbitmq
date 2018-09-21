package com.mq.rabbitmq.simple;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产者生产消费毫不费力
 * 消费者一般和业务相关，可能会比较耗时
 */
public class Receive {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection=ConnectionUtils.getConn();
        Channel channel=connection.createChannel();
        channel.queueDeclare(Send.QUEUE_NAME,false,false,false,null);
        DefaultConsumer consumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                super.handleDelivery(consumerTag, envelope, properties, body);
                String rec_msg=new String(body,"UTF-8");
                System.out.println(rec_msg);
            }
        };
        channel.basicConsume(Send.QUEUE_NAME,true,consumer);

    }
}
