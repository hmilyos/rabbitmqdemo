package com.hmily.rabbitmqapi.exchange.fanout;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerFanoutExchange {
    private static final Logger log = LoggerFactory.getLogger(ConsumerFanoutExchange.class);

    public static final String EXCHANGE_NAME = "test_fanout_exchange";
    public static final String EXCHANGE_TYPE = "fanout";
    public static final String QUEUE_NAME = "test_fanout_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(RabbitMQCommon.RABBITMQ_HOST);
        connectionFactory.setPort(RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);

        connectionFactory.setAutomaticRecoveryEnabled(true);
        connectionFactory.setNetworkRecoveryInterval(3000);

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true, false, false, null);
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//        不设置路由键
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "");

        QueueingConsumer consumer = new QueueingConsumer(channel);
        //参数：队列名称、是否自动ACK、Consumer
        channel.basicConsume(QUEUE_NAME, true, consumer);
        log.info("消费端启动。。。");
        //循环获取消息
        while (true) {
            //获取消息，如果没有消息，这一步将会一直阻塞
            QueueingConsumer.Delivery delivery = consumer.nextDelivery();
            String msg = new String(delivery.getBody());
            log.info("消费端收到消息：{}", msg);
        }
    }
}
