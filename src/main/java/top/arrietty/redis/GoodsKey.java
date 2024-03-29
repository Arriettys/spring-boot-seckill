package top.arrietty.redis;

public class GoodsKey extends BasePrefix
{

	public GoodsKey(int expireSeconds, String prefix)
	{
		super(expireSeconds, prefix);
	}
	public static GoodsKey getGoodsList = new GoodsKey(60, "gl");
	public static GoodsKey getGoodsDetail = new GoodsKey(60, "gt");
	public static GoodsKey getMiaoShaGoodsStock = new GoodsKey(0, "gs");
}
