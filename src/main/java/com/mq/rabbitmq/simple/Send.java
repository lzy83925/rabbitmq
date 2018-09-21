package com.mq.rabbitmq.simple;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 简单队列
 * 耦合性较高，一个生产者对应一个消费者
 * 队列名变更，发送和消费方对队列名的同时更新
 *
 */
public class Send {
    public static final String QUEUE_NAME="test_simple_queue";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= ConnectionUtils.getConn();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String msg="I send a simple queue msg";
        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        channel.close();
        connection.close();
    }
}
