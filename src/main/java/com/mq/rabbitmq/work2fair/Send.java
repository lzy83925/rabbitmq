package com.mq.rabbitmq.work2fair;

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
    public static final String QUEUE_NAME = "test_work_fair_queue";
    public static final int PRE_FETCH_COUNT=1;
    public static final boolean DURABLE=true;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConn();
        Channel channel = connection.createChannel();

        /**
         * 此参数定义消息是否持久化
         * 改动此参数后，需要将队列名称改掉，或者删除原来队列
         */
        channel.queueDeclare(QUEUE_NAME, DURABLE, false, false, null);


        /**
         * 每个消费者发送确认消息之前，消费队列不发送下一个消息到消费者，一次只处理一个消息
         *
         * 限制发送给同一个消费者不超过一条消息
         */
        channel.basicQos(PRE_FETCH_COUNT);



        for (int i = 0; i < 50; i++) {
            String msg = "I send " + i + " work2fair queue msg";
            System.out.println("Worker Queue Send"+i);
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());
            Thread.sleep(i * 20);

        }

        channel.close();
        connection.close();
    }
}
