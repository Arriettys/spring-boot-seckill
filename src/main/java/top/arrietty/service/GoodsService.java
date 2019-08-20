package top.arrietty.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.arrietty.dao.GoodsDao;
import top.arrietty.domain.Goods;
import top.arrietty.domain.MiaoshaGoods;
import top.arrietty.vo.GoodsVo;

@Service
public class GoodsService
{
	@Autowired
	GoodsDao goodsDao;
	
	public List<GoodsVo> listGoodsVo()
	{
		return goodsDao.listGoodsVo();
	}

	public GoodsVo getGoodsVoByGoodsId(long goodsId)
	{
		return goodsDao.getGoodsVoByGoodsId(goodsId);
	}

	public boolean reduceStock(GoodsVo goodsVo)
	{
		MiaoshaGoods g = new MiaoshaGoods();
		g.setGoodsId(goodsVo.getId());
		int ret = goodsDao.reduceStock(g);
		return ret>0;
	}
}
