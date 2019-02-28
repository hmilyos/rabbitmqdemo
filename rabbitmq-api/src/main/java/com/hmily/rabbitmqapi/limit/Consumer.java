package com.hmily.rabbitmqapi.limit;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 使用自定义消费者
 */
public class Consumer {

	private static final Logger log = LoggerFactory.getLogger(Consumer.class);
	
	public static final String EXCHANGE_NAME = "test_qos_exchange";
	public static final String EXCHANGE_TYPE = "topic";
	public static final String ROUTING_KEY_TYPE = "qos.#";
	public static final String ROUTING_KEY = "qos.save";
	public static final String QUEUE_NAME = "test_qos_queue";
	
	public static void main(String[] args) throws IOException, TimeoutException {
		//1 创建ConnectionFactory
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);
        //2 获取C	onnection
        Connection connection = connectionFactory.newConnection();
        //3 通过Connection创建一个新的Channel
        Channel channel = connection.createChannel();
        
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, null);
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_TYPE);
        
        /**
         * prefetchSize：0
         prefetchCount：会告诉RabbitMQ不要同时给一个消费者推送多于N个消息，限速N个
            即一旦有 N 个消息还没有 ack，则该 consumer 将 block 掉，直到有消息 ack 回来，你再发送 N 个过来
         global：true\false 是否将上面设置应用于channel级别，false是consumer级别
         prefetchSize 和global这两项，rabbitmq没有实现，暂且不研究
         */
        channel.basicQos(0, 1, false);

        //使用自定义消费者
        //1 限流方式  第一件事就是 autoAck设置为 false
      //使用自定义消费者
        channel.basicConsume(QUEUE_NAME, false, new MyConsumer(channel));
        log.info("消费端启动成功");
	}
}
