package com.hmily.rabbitmqspringcloudstreamproducterdemo;

import com.hmily.rabbitmqspringcloudstreamproducterdemo.stream.RabbitmqSender;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class RabbitmqSpringcloudstreamProducterDemoApplicationTests {

	@Test
	public void contextLoads() {
	}

    @Autowired
    private RabbitmqSender rabbitmqSender;

    @Test
    public void sendMessageTest1() {
            try {
                Map<String, Object> properties = new HashMap<String, Object>();
                properties.put("SERIAL_NUMBER", "12345");
                properties.put("BANK_NUMBER", "abc");
                properties.put("PLAT_SEND_TIME", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
                String msg = "Hello, I am amqp sender num :";
                log.info("生产端发送：{}", msg);
                rabbitmqSender.sendMessage(msg, properties);

            } catch (Exception e) {
                log.error("sendMessageTest1 Exception： {}", e);
            }
        //TimeUnit.SECONDS.sleep(Integer.MAX_VALUE);
    }
}
