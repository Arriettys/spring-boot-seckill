package top.arrietty.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import top.arrietty.domain.MiaoshaGoods;
import top.arrietty.vo.GoodsVo;

@Mapper
public interface GoodsDao
{
	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id")
	public List<GoodsVo> listGoodsVo();

	@Select("select g.*,mg.stock_count, mg.start_date, mg.end_date,mg.miaosha_price from miaosha_goods mg left join goods g on mg.goods_id = g.id where g.id = #{goodsId}")
	public GoodsVo getGoodsVoByGoodsId(@Param("goodsId")long goodsId);
	
	@Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count>0")//防止库存为负,并且在表中设置unique约束user_id和goods_id唯一性,防止用户多次请求秒杀多个
	public int reduceStock(MiaoshaGoods g);
}
