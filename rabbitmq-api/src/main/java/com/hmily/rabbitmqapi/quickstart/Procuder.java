package com.hmily.rabbitmqapi.quickstart;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 快速开始：生产者
 */
@Slf4j
public class Procuder {

	private static final Logger log = LoggerFactory.getLogger(Procuder.class);
	
    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        for (int i = 0; i < 5; i++) {
            String msg = "hello RabbitMQ + " + i;
            log.info("生产者发送消息：{}", msg);
            channel.basicPublish("", "test1001", null, msg.getBytes());
        }
        log.info("生产者发送消息完毕");
        channel.close();
        connection.close();
    }
}
