package com.hmily.rabbitmqapi.spring.adapter;

import com.hmily.rabbitmqapi.spring.domain.Order;
import com.hmily.rabbitmqapi.spring.domain.Packaged;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Map;

public class MessageDelegate {

    private static final Logger log = LoggerFactory.getLogger(MessageDelegate.class);

    //这个handleMessage方法名要根据org.springframework.amqp.rabbit.listener.adapter包下的
    //      MessageListenerAdapter.ORIGINAL_DEFAULT_LISTENER_METHOD的默认值来确定
    public void handleMessage(byte[] messageBody) {
        log.info("默认方法, 消息内容:" + new String(messageBody));
    }

    public void consumeMessage(byte[] messageBody) {
        log.info("字节数组方法, 消息内容:" + new String(messageBody));
    }

    //1.3 适配器方式.也可以添加一个转换器: 从字节数组转换为String
    public void consumeMessage(String messageBody) {
        log.info("字符串方法, 消息内容:" + messageBody);
    }

    //        2 适配器方式: 我们的队列名称 和 方法名称 也可以进行一一的匹配
    public void method1(String messageBody) {
        log.info("method1 收到消息内容:" + new String(messageBody));
    }
    public void method2(String messageBody) {
        log.info("method2 收到消息内容:" + new String(messageBody));
    }

    public void consumeMessage(Map messageBody) {
        log.info("map方法, 消息内容:" + messageBody);
    }

    public void consumeMessage(Order order) {
        log.info("order对象, 消息内容, id: " + order.getId() +
                ", name: " + order.getName() +
                ", content: "+ order.getContent());
    }

    public void consumeMessage(Packaged pack) {
        log.info("package对象, 消息内容, id: " + pack.getId() +
                ", name: " + pack.getName() +
                ", content: "+ pack.getDescription());
    }


    public void consumeMessage(File file) {
        log.info("文件对象 方法, 消息内容:" + file.getName());
    }
}
