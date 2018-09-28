package com.mq.rabbitmq.confirm;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * confirm模式为异步
 * 编程模式 ：
 * 1,发一条  waitForConfirms()
 * 2,发一批  waitForConfirms()
 * 3,异步confirm模式，提供一个回调方法
 */
public class ConfirmSend {
    public static final String QUEUE_NAME = "queue_confirm_1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConn();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        /**
         * 将模式设置为confirm模式
         * 生产者调用，将channel改为confirm模式
         * 无法与事物模式txSelect共存
         */
        channel.confirmSelect();


        for (int i = 0; i < 10; i++) {
            String msg = "confirm msg send:" + i;
            channel.basicPublish("", QUEUE_NAME, null, msg.getBytes());

        }

        if (!channel.waitForConfirms()) {
            System.out.println("confirm msg send failed");
        } else {
            System.out.println("confirm msg send succ");
        }

        channel.close();
        connection.close();


    }
}
