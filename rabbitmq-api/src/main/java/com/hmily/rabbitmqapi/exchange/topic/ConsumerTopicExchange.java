package com.hmily.rabbitmqapi.exchange.topic;

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

public class ConsumerTopicExchange {

	private final static Logger log = LoggerFactory.getLogger(ConsumerTopicExchange.class);

	// 声明
	public static final String EXCHANGE_NAME = "test_topic_exchange";
	public static final String EXCHANGE_TYPE = "topic";
	public static final String QUEUE_NAME = "test_topic_queue";
	public static final String ROUTING_KEY_one = "user.#";
	public static final String ROUTING_KEY = "user.*";

	public static void main(String[] args) throws IOException, TimeoutException, ShutdownSignalException,
			ConsumerCancelledException, InterruptedException {

		ConnectionFactory connectionFactory = new ConnectionFactory();
		connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
		connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
		connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);

		connectionFactory.setAutomaticRecoveryEnabled(true);
		connectionFactory.setNetworkRecoveryInterval(3000);

		Connection connection = connectionFactory.newConnection();
		Channel channel = connection.createChannel();

		// 1 声明交换机
		channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, false, null);
		// 2 声明队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		// 3 建立交换机和队列的绑定关系:
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

		// durable 是否持久化消息
		QueueingConsumer consumer = new QueueingConsumer(channel);
		// 参数：队列名称、是否自动ACK、Consumer
		channel.basicConsume(QUEUE_NAME, true, consumer);
		// 循环获取消息
		while (true) {
			// 获取消息，如果没有消息，这一步将会一直阻塞
			Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			log.info("消费端收到消息：{}", msg);
		}
	}
}
