package com.mq.rabbitmq.publishsubscribe;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class PublishSend {
    public static final String EXCHANGE_NAME="exchange_queue_fanout";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= ConnectionUtils.getConn();
        Channel channel=connection.createChannel();
        /**
         * 如果没有接收者绑定交换机，消息发送出去后，消息丢失
         * 交换机是没有存储能力的，只有队列有
         */
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        String msg="publish subscribe mode";
        channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
        System.out.println("TxSend:"+msg);
        channel.close();
        connection.close();


    }
}
