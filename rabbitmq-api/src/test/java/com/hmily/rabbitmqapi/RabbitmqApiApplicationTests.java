package com.hmily.rabbitmqapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApiApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private RabbitAdmin rabbitAdmin;

	@Test
	public void testAdmin() throws Exception {
		rabbitAdmin.declareExchange(new DirectExchange("test.direct", false, false));
		rabbitAdmin.declareExchange(new TopicExchange("test.topic", false, false));
		rabbitAdmin.declareExchange(new FanoutExchange("test.fanout", false, false));
		rabbitAdmin.declareQueue(new Queue("test.direct.queue", false));
		rabbitAdmin.declareQueue(new Queue("test.topic.queue", false));
		rabbitAdmin.declareQueue(new Queue("test.fanout.queue", false));
	}
}
