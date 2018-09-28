package com.mq.rabbitmq.transaction;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class TxRecv {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConn();
        Channel channel = connection.createChannel();
        channel.queueDeclare(TxSend.QUEUE_NAME, false, false, false, null);


        channel.basicConsume(TxSend.QUEUE_NAME,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Recv[tx] msg:"+new String(body,"UTF-8"));
            }
        });
    }
}
