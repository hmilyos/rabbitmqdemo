package com.hmily.rabbitmqspringcloudstreamproducterdemo.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import java.util.Map;

@EnableBinding(Barista.class)
@Service
@Slf4j
public class RabbitmqSender {

    @Autowired
    private Barista barista;

    // 发送消息
    public String sendMessage(Object message, Map<String, Object> properties) throws Exception {
        try{
            MessageHeaders mhs = new MessageHeaders(properties);
            Message msg = MessageBuilder.createMessage(message, mhs);
            boolean sendStatus = barista.logoutput().send(msg);
            log.info("--------------sending -------------------");
            log.info("发送数据：" + message + ",sendStatus: " + sendStatus);
        }catch (Exception e){
            log.error("sendMessage Exception: {}", e);
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
        return null;
    }
}
