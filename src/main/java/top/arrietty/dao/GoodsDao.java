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
	
	@Update("update miaosha_goods set stock_count = stock_count - 1 where goods_id = #{goodsId} and stock_count>0")//��ֹ���Ϊ��,�����ڱ�������uniqueԼ��user_id��goods_idΨһ��,��ֹ�û����������ɱ���
	public int reduceStock(MiaoshaGoods g);
}
