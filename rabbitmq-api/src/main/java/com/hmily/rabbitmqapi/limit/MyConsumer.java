package com.hmily.rabbitmqapi.limit;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class MyConsumer extends DefaultConsumer {

	private static final Logger log = LoggerFactory.getLogger(MyConsumer.class);
	
	 private Channel channel;
	 
	public MyConsumer(Channel channel) {
		super(channel);
		this.channel = channel;
	}

	@Override
    public void handleDelivery(String consumerTag,  //消费者标签
                               Envelope envelope,
                               AMQP.BasicProperties properties,
                               byte[] body) throws IOException {
        
        log.info("------limit-----consume message----------");
        log.info("consumerTag: " + consumerTag);
        log.info("envelope: " + envelope);
        log.info("properties: " + properties);
        log.info("body: " + new String(body));
        //一定要手动ACK回去
        channel.basicAck(envelope.getDeliveryTag(), false);
    }
}
