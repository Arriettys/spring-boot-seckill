package top.arrietty.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import top.arrietty.dao.GoodsDao;
import top.arrietty.dao.OrderDao;
import top.arrietty.domain.MiaoShaUser;
import top.arrietty.domain.MiaoshaOrder;
import top.arrietty.domain.OrderInfo;
import top.arrietty.redis.OrderKey;
import top.arrietty.vo.GoodsVo;

@Service
public class OrderService
{
	@Autowired
	OrderDao orderDao;
	
	@Autowired
	RedisService redisService;

	public MiaoshaOrder getMiaoshaOrderByUserIdGoodsId(long userId, long goodsId)
	{		
		//return orderDao.getMiaoshaOrderByUserIdGoodsId(userId, goodsId);
		return redisService.get(OrderKey.getMiaoshaOrderByUidGid, userId+"_"+goodsId, MiaoshaOrder.class);
	}
	
	public OrderInfo getOrderById(long orderId) 
	{
		return orderDao.getOrderById(orderId);
	}
	
	@Transactional
	public OrderInfo createOrder(MiaoShaUser user, GoodsVo goodsVo)
	{
		OrderInfo orderInfo = new OrderInfo();
		orderInfo.setCreateDate(new Date());
		orderInfo.setDeliveryAddrId(0L);
		orderInfo.setGoodsCount(1);
		orderInfo.setGoodsId(goodsVo.getId());
		orderInfo.setGoodsName(goodsVo.getGoodsName());
		orderInfo.setGoodsPrice(goodsVo.getMiaoshaPrice());
		orderInfo.setOrderChannel(1);
		orderInfo.setStatus(0);
		orderInfo.setUserId(user.getId());
		orderDao.insert(orderInfo);
		MiaoshaOrder miaoshaOrder = new MiaoshaOrder();
		miaoshaOrder.setGoodsId(goodsVo.getId());
		miaoshaOrder.setOrderId(orderInfo.getId());
		miaoshaOrder.setUserId(user.getId());		
		orderDao.insertMiaoshaOrder(miaoshaOrder);
		redisService.set(OrderKey.getMiaoshaOrderByUidGid, user.getId()+"_"+goodsVo.getId(), miaoshaOrder);
		return orderInfo;
	}
}
