package com.hmily.rabbitmqapi.exchange.topic;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ProducerTopicExchange {

	private final static Logger log = LoggerFactory.getLogger(ProducerTopicExchange.class);

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
		connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
		connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);

		// 2 创建Connection
		Connection connection = connectionFactory.newConnection();
		// 3 创建Channel
		Channel channel = connection.createChannel();

		// 4 声明
		String routingKey1 = "user.save";
		String routingKey2 = "user.update";
		String routingKey3 = "user.delete.abc";

		String msg1 = "Hello World RabbitMQ  Topic Exchange Message ..." + routingKey1;
		String msg2 = "Hello World RabbitMQ  Topic Exchange Message ..." + routingKey2;
		String msg3 = "Hello World RabbitMQ  Topic Exchange Message ..." + routingKey3;
		log.info("生产端， {} ：{}", routingKey1, msg1);
		channel.basicPublish(ConsumerTopicExchange.EXCHANGE_NAME, routingKey1, null, msg1.getBytes());
		log.info("生产端， {} ：{}", routingKey2, msg2);
		channel.basicPublish(ConsumerTopicExchange.EXCHANGE_NAME, routingKey2, null, msg2.getBytes());
		log.info("生产端， {} ：{}", routingKey3, msg3);
		channel.basicPublish(ConsumerTopicExchange.EXCHANGE_NAME, routingKey3, null, msg3.getBytes());
		
		channel.close();
		connection.close();
	}
}
