package com.hmily.rabbitmqapi.exchange.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;


public class ProducerDirectExchange {

	private final static Logger log = LoggerFactory.getLogger(ProducerDirectExchange.class);

	public static void main(String[] args) throws IOException, TimeoutException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);
		connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
		connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);

		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		
		String msg = "Hello World RabbitMQ  Direct Exchange test.direct Message  ... ";
		log.info("生产端发送了：{}", msg);
		channel.basicPublish(ConsumerDirectExchange.EXCHANGE_NAME, ConsumerDirectExchange.ROUTING_KEY, null, msg.getBytes());

		//		channel.basicPublish(ConsumerDirectExchange.EXCHANGE_NAME, ConsumerDirectExchange.ROUTING_KEY_ERROR, null, msg.getBytes());
		channel.close();
		connection.close();
	}
	
}
