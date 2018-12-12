package com.hmily.rabbitmqapi.confirm;

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
 * confirm机制消费端
 */
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger(Consumer.class);

    public static final String EXCHANGE_NAME = "test_confirm_exchange";
    public static final String EXCHANGE_TYPE = "topic";
    public static final String ROUTING_KEY_REG = "confirm.#";
    public static final String ROUTING_KEY = "confirm.abc";
    public static final String QUEUE_NAME = "test_confirm_queue";

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
        //4 声明交换机和队列 然后进行绑定设置, 最后制定路由Key
        channel.exchangeDeclare(EXCHANGE_NAME, EXCHANGE_TYPE, true);   //true表示持久化
                //是否持久化，独占模式，自动删除
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY_REG);

        //5 创建消费者
        QueueingConsumer queueingConsumer = new QueueingConsumer(channel);
//        是否自动签收 autoAck
        channel.basicConsume(QUEUE_NAME, true, queueingConsumer);
        log.info("消费端已启动");
        while(true){
            QueueingConsumer.Delivery delivery = queueingConsumer.nextDelivery();
            String msg = new String(delivery.getBody());
            log.info("消费端: {}", msg);
        }
    }
}
