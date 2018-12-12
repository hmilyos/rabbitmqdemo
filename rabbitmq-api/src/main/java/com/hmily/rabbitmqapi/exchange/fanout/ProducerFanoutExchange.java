package com.hmily.rabbitmqapi.exchange.fanout;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ProducerFanoutExchange {

    private static final Logger log = LoggerFactory.getLogger(ProducerFanoutExchange.class);

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);
        // 2 创建Connection
        Connection connection = connectionFactory.newConnection();
        // 3 创建Channel
        Channel channel = connection.createChannel();
        // 5 发送
        for (int i = 0; i < 10; i++) {
            String msg = "Hello World RabbitMQ  FANOUT Exchange Message ...";
            log.info("生产端，routingKey{}: {}", i, msg);
            channel.basicPublish(ConsumerFanoutExchange.EXCHANGE_NAME, "" + i, null, (msg + i).getBytes());
        }
        channel.close();
        connection.close();
    }
}
