package com.mq.rabbitmq.routing;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 路由模式最关键的是在对应的routing-key
 */
public class Send {
    public static final String EXCHANGE_NAME="exchange_queue_direct";
    public static final String ROUTING_KEY_ERROR="error";
    public static final String ROUTING_KEY_INFO="info";
    public static final String ROUTING_KEY_WARN="warn";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection=ConnectionUtils.getConn();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        String msg="hello direct ";
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KEY_INFO,null,msg.getBytes());
        channel.close();
        connection.close();
    }
}
