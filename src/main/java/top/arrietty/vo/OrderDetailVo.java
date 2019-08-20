package top.arrietty.vo;

import top.arrietty.domain.OrderInfo;

public class OrderDetailVo
{
	private GoodsVo goodsVo;
	private OrderInfo order;
	public GoodsVo getGoods()
	{
		return goodsVo;
	}
	public void setGoods(GoodsVo goodsVo)
	{
		this.goodsVo = goodsVo;
	}
	public OrderInfo getOrder()
	{
		return order;
	}
	public void setOrder(OrderInfo order)
	{
		this.order = order;
	}
	
}
