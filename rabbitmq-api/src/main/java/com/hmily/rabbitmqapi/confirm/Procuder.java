package com.hmily.rabbitmqapi.confirm;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * confirm机制生产端
 */
public class Procuder {

    private static final Logger log = LoggerFactory.getLogger(Procuder.class);

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

        //4 指定我们的消息投递模式: 消息的确认模式
        channel.confirmSelect();

        //5 发送一条消息
        String msg = "Hello RabbitMQ Send confirm message!";
        log.info("生产已启动，并发送了：{}", msg);
        channel.basicPublish(Consumer.EXCHANGE_NAME, Consumer.ROUTING_KEY, null, msg.getBytes());

        //6 添加一个确认监听
        channel.addConfirmListener(new ConfirmListener() {
            @Override  //deliveryTag表示消息的唯一标签，
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                log.info("-------ack!-----------");
                log.info("deliveryTag: {}, multiple: {}", deliveryTag, multiple);
            }
            @Override  //失败时进入这里
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                log.info("-------no ack!-----------");
                log.info("deliveryTag: {}, multiple: {}", deliveryTag, multiple);
            }
        });
        channel.close();
        connection.close();
    }
}
