package com.hmily.rabbitmqapi.exchange.direct;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.QueueingConsumer.Delivery;
import com.rabbitmq.client.ShutdownSignalException;

public class ConsumerDirectExchange {

	private static final Logger log = LoggerFactory.getLogger(ConsumerDirectExchange.class);

	// 声明
	public final static String EXCHANGE_NAME = "test_direct_exchange";
	public final static String EXCHANGE_TYPE = "direct";
	public final static String QUEUE_NAME = "test_direct_queue";
	public final static String ROUTING_KEY = "test.direct";
	public final static String ROUTING_KEY_ERROR = "test.direct.error";
	
	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException,
			ConsumerCancelledException, InterruptedException {
		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);
		connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
		connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);

		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setNetworkRecoveryInterval(3000);

		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();
		// 表示声明了一个交换机
		channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, false, null);
		// 表示声明了一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 建立一个绑定关系:
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

		// durable 是否持久化消息
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// 参数：队列名称、是否自动ACK、Consumer
		channel.basicConsume(QUEUE_NAME, true, consumer);

		while (true) {
			// 获取消息，如果没有消息，这一步将会一直阻塞
			Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			log.info("收到消息：{}", msg);
		}
	}
}
