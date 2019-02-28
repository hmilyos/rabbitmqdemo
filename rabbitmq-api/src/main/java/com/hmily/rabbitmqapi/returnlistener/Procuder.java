package com.hmily.rabbitmqapi.returnlistener;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *  Return返回消息 生产端
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

        String msg = "Hello RabbitMQ Return Message";

        channel.addReturnListener(new ReturnListener() {
            @Override
            public void handleReturn(int replyCode, String replyText, String exchange,
                                     String routingKey, AMQP.BasicProperties properties,
                                     byte[] body) throws IOException {
                log.info("---------handle  return----------");
                log.info("响应码replyCode: {}", replyCode);
                log.info("文本信息replyText: {}", replyText);
                log.info("exchange:  {}", exchange);
                log.info("routingKey:  {}", routingKey);
                log.info("properties:  {}", properties);
                log.info("body:  {}" ,new String(body));
            }
        });

        /**
         * 如果为true，则监听器会接收到路由不可达的消息，然后进行后续处理，
         * 如果为false，则broker端自动删除该消息。
         */
        log.info("生产端{}发送：{}", Consumer.ROUTING_KEY, msg + Consumer.ROUTING_KEY);
        channel.basicPublish(Consumer.EXCHANGE_NAME, Consumer.ROUTING_KEY, true, null, (msg + Consumer.ROUTING_KEY).getBytes());
        log.info("生产端{}发送：{}", Consumer.ROUTINGKEY_ERROR, msg + Consumer.ROUTINGKEY_ERROR);
        channel.basicPublish(Consumer.EXCHANGE_NAME, Consumer.ROUTINGKEY_ERROR, true, null, (msg + Consumer.ROUTINGKEY_ERROR).getBytes());
        log.info("生产端{}发送：{}", Consumer.ROUTINGKEY_ERROR2, msg + Consumer.ROUTINGKEY_ERROR2);
        channel.basicPublish(Consumer.EXCHANGE_NAME, Consumer.ROUTINGKEY_ERROR2, false, null, (msg + Consumer.ROUTINGKEY_ERROR2).getBytes());
        channel.close();
        connection.close();
    }
}
