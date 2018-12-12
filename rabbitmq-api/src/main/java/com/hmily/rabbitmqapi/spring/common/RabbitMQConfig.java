package com.hmily.rabbitmqapi.spring.common;

import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;
import com.rabbitmq.client.ConnectionFactory;


@Configuration
@ComponentScan({"com.hmily.rabbitmqapi.spring.*"})
public class RabbitMQConfig {

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(RabbitMQCommon.RABBITMQ_HOST + ":" + RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setUsername(RabbitMQCommon.RABBITMQ_USERNAME);
        connectionFactory.setPassword(RabbitMQCommon.RABBITMQ_PASSWORD);
        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);
        return connectionFactory;
    }
}
