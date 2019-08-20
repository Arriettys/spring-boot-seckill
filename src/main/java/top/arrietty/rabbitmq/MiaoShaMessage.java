package top.arrietty.rabbitmq;

import top.arrietty.domain.MiaoShaUser;

public class MiaoShaMessage
{
	private MiaoShaUser user;
	private long goodsId;
	public MiaoShaUser getUser()
	{
		return user;
	}
	public void setUser(MiaoShaUser user)
	{
		this.user = user;
	}
	public long getGoodsId()
	{
		return goodsId;
	}
	public void setGoodsId(long goodsId)
	{
		this.goodsId = goodsId;
	}
	
}
