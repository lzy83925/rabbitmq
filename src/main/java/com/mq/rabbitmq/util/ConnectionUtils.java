package com.mq.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils {
    public static Connection getConn() throws IOException, TimeoutException {
        ConnectionFactory factory= new ConnectionFactory();
        factory.setHost("10.10.10.85");
        factory.setVirtualHost("/");
        factory.setPort(32410);
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory.newConnection();
    }
}
