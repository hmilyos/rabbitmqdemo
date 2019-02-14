package com.hmily.rabbitmqspringcloudstreamconsumerdemo.stream;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@EnableBinding(Barista.class)
@Service
@Slf4j
public class RabbitmqReceiver {

    @StreamListener(Barista.INPUT_CHANNEL)
    public void receiver(Message message) throws Exception {
        Channel channel = (com.rabbitmq.client.Channel) message.getHeaders().get(AmqpHeaders.CHANNEL);
        Long deliveryTag = (Long) message.getHeaders().get(AmqpHeaders.DELIVERY_TAG);
        log.info("Input Stream 1 接受数据：" + message);
        log.info("消费完毕------------");
        channel.basicAck(deliveryTag, false);   //是否批量签收
    }
}
