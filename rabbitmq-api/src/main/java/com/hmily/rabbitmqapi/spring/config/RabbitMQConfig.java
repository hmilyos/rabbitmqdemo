package com.hmily.rabbitmqapi.spring.config;


import com.hmily.rabbitmqapi.spring.adapter.MessageDelegate;
import com.hmily.rabbitmqapi.spring.convert.ImageMessageConverter;
import com.hmily.rabbitmqapi.spring.convert.PDFMessageConverter;
import com.hmily.rabbitmqapi.spring.convert.TextMessageConverter;
import com.rabbitmq.client.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.ConsumerTagStrategy;
import org.springframework.amqp.support.converter.ContentTypeDelegatingMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.hmily.rabbitmqapi.common.RabbitMQCommon;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Configuration
@ComponentScan({"com.hmily.rabbitmqapi.spring.*"})
public class RabbitMQConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);
    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(RabbitMQCommon.RABBITMQ_HOST + ":" + RabbitMQCommon.RABBITMQ_PORT);
        connectionFactory.setUsername(RabbitMQCommon.RABBITMQ_USERNAME);
        connectionFactory.setPassword(RabbitMQCommon.RABBITMQ_PASSWORD);
        connectionFactory.setVirtualHost(RabbitMQCommon.RABBITMQ_DEFAULT_VIRTUAL_HOST);
        return connectionFactory;
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory){
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return  rabbitAdmin;
    }

    /**
     *  针对消费者配置
     * 1. 设置交换机类型
     * 2. 将队列绑定到交换机
     * FanoutExchange: 将消息分发到所有的绑定队列，无routingkey的概念
     * HeadersExchange ：通过添加属性key-value匹配
     * DirectExchange:按照routingkey分发到指定队列
     * TopicExchange:多关键字匹配
     */

    /**
     *  声明 TopicExchange 类型的交换机 topic001
     * @return
     */
    @Bean
    public TopicExchange exchange001() {
//    	是否持久化，是否自动删除
		return new TopicExchange("topic001", true, false);
	}
    
//    声明 queue001 队列
    @Bean
    public Queue queue001() {
    	return new Queue("queue001", true); //队列持久
    }
    
//    将上面的交换机和队列绑定
    @Bean
    public Binding binding001() {
    	return BindingBuilder.bind(queue001()).to(exchange001()).with("spring.*");
    }
    
    @Bean
    public TopicExchange exchange002() {
    	return new TopicExchange("topic002", true, false);
    }
    
    @Bean
    public Queue queue002() {
    	return new Queue("queue002", true);
    }
    
    @Bean
    public Binding binding002() {
    	return BindingBuilder.bind(queue002()).to(exchange002()).with("rabbit.*");
    }
    
    @Bean
    public Queue queue003() {
        return new Queue("queue003", true); //队列持久
    }

    @Bean
    public Binding binding003() {
        return BindingBuilder.bind(queue003()).to(exchange001()).with("mq.*");
    }

    @Bean //connectionFactory 也是要和最上面方法名保持一致
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

    @Bean   //connectionFactory 也是要和最上面方法名保持一致
    public SimpleMessageListenerContainer messageContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueues(queue001(), queue002(), queue003());    //监听的队列
        container.setConcurrentConsumers(1);    //当前的消费者数量
        container.setMaxConcurrentConsumers(5); //  最大的消费者数量
        container.setDefaultRequeueRejected(false); //是否重回队列
        container.setAcknowledgeMode(AcknowledgeMode.AUTO); //签收模式
        container.setExposeListenerChannel(true);
        container.setConsumerTagStrategy(new ConsumerTagStrategy() {    //消费端的标签策略
            @Override
            public String createConsumerTag(String queue) {
                return queue + "_" + UUID.randomUUID().toString();
            }
        });


//        container.setMessageListener(new ChannelAwareMessageListener() {
//            @Override
//            public void onMessage(Message message, Channel channel) throws Exception {
//                String msg = new String(message.getBody());
//                log.info("----------消费者: " + msg);
//            }
//        });
//        return container;

//        1.1 适配器方式. 默认是有自己的方法名字的：handleMessage
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//    	container.setMessageListener(adapter);
//        return container;

        //1.2 适配器方式. 可以自己指定一个方法的名字: consumeMessage
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//    	container.setMessageListener(adapter);
//        return container;

        //1.3 适配器方式.也可以添加一个转换器: 从字节数组转换为String
//    	MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//    	adapter.setDefaultListenerMethod("consumeMessage");
//    	adapter.setMessageConverter(new TextMessageConverter());
//    	container.setMessageListener(adapter);
//        return container;

        //        2 适配器方式: 我们的队列名称 和 方法名称 也可以进行一一的匹配
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//    	adapter.setMessageConverter(new TextMessageConverter());
//    	Map<String, String> queueOrTagToMethodName = new HashMap<>();
//    	queueOrTagToMethodName.put("queue001", "method1");
//    	queueOrTagToMethodName.put("queue002", "method2");
//    	adapter.setQueueOrTagToMethodName(queueOrTagToMethodName);
//    	container.setMessageListener(adapter);
//        return container;

        //3  支持json格式的转换器
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);
//        return container;

        // 4  DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象转换
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);
//        return container;

        // 5 DefaultJackson2JavaTypeMapper & Jackson2JsonMessageConverter 支持java对象多映射转换
//        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
//        adapter.setDefaultListenerMethod("consumeMessage");
//        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
//        DefaultJackson2JavaTypeMapper javaTypeMapper = new DefaultJackson2JavaTypeMapper();
//
//        Map<String, Class<?>> idClassMapping = new HashMap<String, Class<?>>();
//        idClassMapping.put("order", com.hmily.rabbitmqapi.spring.domain.Order.class);
//        idClassMapping.put("packaged", com.hmily.rabbitmqapi.spring.domain.Packaged.class);
//
//        javaTypeMapper.setIdClassMapping(idClassMapping);
//
//        jackson2JsonMessageConverter.setJavaTypeMapper(javaTypeMapper);
//        adapter.setMessageConverter(jackson2JsonMessageConverter);
//        container.setMessageListener(adapter);
//        return container;

        // 6 ext convert
        MessageListenerAdapter adapter = new MessageListenerAdapter(new MessageDelegate());
        adapter.setDefaultListenerMethod("consumeMessage");

        //全局的转换器:
        ContentTypeDelegatingMessageConverter convert = new ContentTypeDelegatingMessageConverter();

        TextMessageConverter textConvert = new TextMessageConverter();
        convert.addDelegate("text", textConvert);
        convert.addDelegate("html/text", textConvert);
        convert.addDelegate("xml/text", textConvert);
        convert.addDelegate("text/plain", textConvert);

        Jackson2JsonMessageConverter jsonConvert = new Jackson2JsonMessageConverter();
        convert.addDelegate("json", jsonConvert);
        convert.addDelegate("application/json", jsonConvert);

        ImageMessageConverter imageConverter = new ImageMessageConverter();
        convert.addDelegate("image/png", imageConverter);
        convert.addDelegate("image", imageConverter);

        PDFMessageConverter pdfConverter = new PDFMessageConverter();
        convert.addDelegate("application/pdf", pdfConverter);


        adapter.setMessageConverter(convert);
        container.setMessageListener(adapter);

        return container;
    }
}
