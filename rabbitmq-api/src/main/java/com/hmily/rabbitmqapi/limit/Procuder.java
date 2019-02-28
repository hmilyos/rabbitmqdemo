package com.hmily.rabbitmqapi.limit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Procuder {

	private static final Logger log = LoggerFactory.getLogger(Procuder.class);

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
		connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
		connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);

		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();

		String msg = "Hello RabbitMQ limit Message";
        for(int i = 0; i < 5; i ++){
            log.info("生产端发送：{}", msg + i);
            channel.basicPublish(Consumer.EXCHANGE_NAME, Consumer.ROUTING_KEY, true, null, (msg + i).getBytes());
        }
		channel.close();
		connection.close();
	}
}
