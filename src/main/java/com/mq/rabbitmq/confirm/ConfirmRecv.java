package com.mq.rabbitmq.confirm;

import com.mq.rabbitmq.transaction.TxSend;
import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConfirmRecv {
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConn();
        Channel channel = connection.createChannel();
        channel.queueDeclare(ConfirmSendAsync.QUEUE_NAME, false, false, false, null);


        channel.basicConsume(ConfirmSendAsync.QUEUE_NAME,true,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Recv[confirm] msg:"+new String(body,"UTF-8"));
            }
        });
    }
}
