package com.mq.rabbitmq.topic;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 将路由和某个主题模式匹配
 * 需要通配符来匹配
 * # 匹配一个或者多个
 * * 匹配一个
 */
public class Send {
    public static final String EXCHANGE_NAME="TOPIC EXCHANGE ";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= ConnectionUtils.getConn();
        Channel channel=connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        String msg="商品 。 。 。 ";
        channel.basicPublish(EXCHANGE_NAME,"goods.update",null,msg.getBytes());
        System.out.println("-----topic send succ-----");
        channel.close();
        connection.close();
    }
}
