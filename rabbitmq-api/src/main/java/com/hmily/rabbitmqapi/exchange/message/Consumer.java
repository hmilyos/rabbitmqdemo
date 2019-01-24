package com.hmily.rabbitmqapi.exchange.message;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    public static void main(String[] args) throws IOException, TimeoutException,
            ShutdownSignalException, ConsumerCancelledException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);
        //2 通过连接工厂创建连接
        Connection connection = connectionFactory.newConnection();
        //3 通过connection创建一个Channel
        Channel channel = connection.createChannel();
        //4 声明（创建）一个队列
        String queueName = "test001";
        channel.queueDeclare(queueName, true, false, false, null);
        //5 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
        //6 设置Channel
        channel.basicConsume(queueName, true, queueingConsumer);
        while (true) {
            //7 获取消息
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            log.info("消费端: " + msg);
            Map<String, Object> headers = delivery.getProperties().getHeaders();
            log.info("headers get myHeaders1 value: " + headers.get("myHeaders1"));
            log.info("headers get myHeaders2value: " + headers.get("myHeaders2"));
            //Envelope envelope = delivery.getEnvelope();
        }
    }

}
