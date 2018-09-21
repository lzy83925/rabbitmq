package com.mq.rabbitmq.work1roundrobin;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 工作队列
 * 一个生产者对应多个消费者
 * 基于round-robin轮询分发
 */
public class Send {
    public static final String QUEUE_NAME = "test_work_round_robin_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConn();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        for (int i = 0; i < 50; i++) {
            String msg = "I send " + i + " work1roundrobin queue msg";
            System.out.println("Worker Queue Send"+i);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            Thread.sleep(i * 20);

        }

        channel.close();
        connection.close();
    }
}
