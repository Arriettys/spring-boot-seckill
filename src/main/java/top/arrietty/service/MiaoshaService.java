package top.arrietty.service;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.arrietty.dao.GoodsDao;
import top.arrietty.domain.Goods;
import top.arrietty.domain.MiaoShaUser;
import top.arrietty.domain.MiaoshaOrder;
import top.arrietty.domain.OrderInfo;
import top.arrietty.redis.GoodsKey;
import top.arrietty.redis.MiaoshaKey;
import top.arrietty.vo.GoodsVo;

@Service
public class MiaoshaService
{
	@Autowired
	GoodsService goodsService;
	@Autowired
	OrderService orderService;
	@Autowired
	RedisService redisService;

	@Transactional
	public OrderInfo miaosha(MiaoShaUser user, GoodsVo goodsVo)
	{
		//减库存
		boolean success = goodsService.reduceStock(goodsVo);
		if (success)
		{
			//生成订单
			return orderService.createOrder(user, goodsVo);
		} else
		{
			setGoodsOver(goodsVo.getId());
			return null;
		}		
	}	
	
	public long getMiaoShaResult(Long userId, long goodsId)
	{
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
		if (order!=null)//秒杀成功
			return order.getOrderId();
		else
		{
			boolean isOver = getGoodsOver(goodsId);
			if (isOver)
				return -1;
			else
				return 0;
		}
	}
	
	private void setGoodsOver(Long goodsId) 
	{
		redisService.set(MiaoshaKey.isGoodsOver, ""+goodsId, true);
	}
	
	private boolean getGoodsOver(long goodsId) 
	{
		return redisService.exists(MiaoshaKey.isGoodsOver, ""+goodsId); 
	}
	
}
