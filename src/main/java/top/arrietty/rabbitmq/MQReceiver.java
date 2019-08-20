package top.arrietty.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.arrietty.domain.MiaoShaUser;
import top.arrietty.domain.MiaoshaOrder;
import top.arrietty.result.CodeMsg;
import top.arrietty.result.Result;
import top.arrietty.service.GoodsService;
import top.arrietty.service.MiaoshaService;
import top.arrietty.service.OrderService;
import top.arrietty.service.RedisService;
import top.arrietty.vo.GoodsVo;

@Service
public class MQReceiver
{
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoshaService miaoshaService;
	
	private static Logger log = LoggerFactory.getLogger(MQReceiver.class);
	@RabbitListener(queues=MQConfig.QUEUE)
	public void receive(String message)
	{
		log.info("receive message:" + message);
	}
	
	@RabbitListener(queues=MQConfig.TOPIC_QUEUE1)
	public void receiveTopic1(String message)
	{
		log.info("topic queue1 message:" + message);
	}
	
	@RabbitListener(queues=MQConfig.TOPIC_QUEUE2)
	public void receiveTopic2(String message)
	{
		log.info("topic queue2 message:" + message);
	}
	
	@RabbitListener(queues=MQConfig.HEADERS_QUEUE)
	public void receiveHeaders(byte[] message)
	{
		log.info("headers queue1 message:" + (new String(message)));
	}
	
	@RabbitListener(queues=MQConfig.MIAOSHA_QUEUE)
	public void miaoShaReceive(String message)
	{
		log.info("receive message:" + message);
		MiaoShaMessage mm = redisService.StringToBean(message, MiaoShaMessage.class);
		MiaoShaUser user = mm.getUser();
		long goodsId = mm.getGoodsId();
		//判断库存
		GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
		int stock = goodsVo.getGoodsStock();
		if (stock<=0)
		{
			return;
		}
		//判断是否秒杀到
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if (order!=null)
		{
			return;
		}
		//减库存 下订单 写入秒杀订单
		miaoshaService.miaosha(user, goodsVo);
	}
}
