package com.hmily.rabbitmqapi.returnlistener;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 *  Return返回消息 消费端
 */
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    public static final String EXCHANGE_NAME = "test_return_exchange";
    public static final String EXCHANGE_TYPE = "topic";
    public static final String ROUTING_KEY_REG = "return.#";
    public static final String ROUTING_KEY = "return.save";
    public static final String ROUTINGKEY_ERROR = "abc.true";
    public static final  String ROUTINGKEY_ERROR2 = "abc.false";
    public static final String QUEUE_NAME = "test_return_queue";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
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
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_REG);

        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);

        channel.basicConsume(QUEUE_NAME, true, queueingConsumer);
        log.info("消费端启动成功");
        while(true){
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            log.info("消费者: {}", msg);
        }
    }
}
