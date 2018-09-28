package com.mq.rabbitmq.confirm;

import com.mq.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class ConfirmSendAsync {
    public static final String QUEUE_NAME = "queue_confirm_async_1";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection = ConnectionUtils.getConn();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.confirmSelect();

        /**
         * 存放未确认的消息
         */
        SortedSet<Long> confirmSet= Collections.synchronizedSortedSet(new TreeSet<Long>());

        /**
         * 通道添加监听
         */
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 没有问题的
             * @param deliverytag
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleAck(long deliverytag, boolean multiple) throws IOException {
                if(multiple){
                    System.out.println("handle ack multiple");
                    confirmSet.headSet(deliverytag+1).clear();
                }else{
                    System.out.println("handle ack multiple false");
                    confirmSet.remove(deliverytag);
                }
            }

            /**
             * 有问题的会进入这个方法
             * @param deliverytag
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleNack(long deliverytag, boolean multiple) throws IOException {
                if(multiple){
                    System.out.println("handle nack multiple");
                    confirmSet.headSet(deliverytag+1).clear();
                }else{
                    System.out.println("handle nack multiple false");
                    confirmSet.remove(deliverytag);
                }
            }
        });

        String msg="sssss";
        while (true){
            long seqNo=channel.getNextPublishSeqNo();
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            confirmSet.add(seqNo);
        }


    }
}
