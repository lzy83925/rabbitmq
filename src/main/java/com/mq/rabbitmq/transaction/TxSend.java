package com.mq.rabbitmq.transaction;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 在rabbitmq中，通过数据持久化解决rabbitmq异常导致数据丢失的问题
 * 生产者将消息发出去以后，到底有没有到达rabbitmq服务器默认的情况下是不知道的
 * 解决方法：
 * 1.AMQP实现了事物机制
 * 2.confirm模式
 * <p>
 * 事物机制
 * txSelect:
 */
public class TxSend {
    public static final String QUEUE_NAME = "test_queue_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConn();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String msg = "tx msg send";
        try {
            channel.txSelect();
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());


            //故意抛出一个异常
            int i = 1 / 0;
            //故意抛出一个异常


            channel.txCommit();
        } catch (Exception e) {
            channel.txRollback();
            System.out.println("tx rollback");
        } finally {
            channel.close();
            connection.close();
        }


    }
}
