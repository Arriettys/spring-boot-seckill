package top.arrietty.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.arrietty.service.RedisService;

@Service
public class MQSender
{
	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
	@Autowired
	AmqpTemplate amqpTemplate;
	
	@Autowired
	RedisService redisService;
	public void send(Object message)
	{
		String msg = redisService.beanToString(message); 
		log.info("send message:" + msg);
		amqpTemplate.convertAndSend(MQConfig.QUEUE,message);
	}
	
	public void sendTopic(Object message)
	{
		String msg = redisService.beanToString(message);
		log.info("send topic message:" + msg);
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key1", msg+"1");
		amqpTemplate.convertAndSend(MQConfig.TOPIC_EXCHANGE, "topic.key2", msg+"2");
	}
	
	public void sendFanout(Object message)
	{
		String msg = redisService.beanToString(message);
		log.info("send fanout message:" + msg);
		amqpTemplate.convertAndSend(MQConfig.FANOUT_EXCHANGE, "", msg+"1");
	}
	
	public void sendHeaders(Object message)
	{
		String msg = redisService.beanToString(message);
		log.info("send fanout message:" + msg);
		MessageProperties properties = new MessageProperties();
		properties.setHeader("header1", "value1");
		properties.setHeader("header2", "value2");
		Message obj = new Message(msg.getBytes(), properties);
		amqpTemplate.convertAndSend(MQConfig.HEADERS_EXCHANGE, "" , obj);
	}

	public void sendMiaoshaMessage(MiaoShaMessage mm)
	{
		String msg = redisService.beanToString(mm);
		log.info("send message:" + msg);
		amqpTemplate.convertAndSend(MQConfig.MIAOSHA_QUEUE, msg);
	}
}
