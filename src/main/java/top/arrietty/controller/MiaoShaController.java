package top.arrietty.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import top.arrietty.domain.MiaoShaUser;
import top.arrietty.domain.MiaoshaOrder;
import top.arrietty.domain.OrderInfo;
import top.arrietty.rabbitmq.MQSender;
import top.arrietty.rabbitmq.MiaoShaMessage;
import top.arrietty.redis.GoodsKey;
import top.arrietty.result.CodeMsg;
import top.arrietty.result.Result;
import top.arrietty.service.GoodsService;
import top.arrietty.service.MiaoshaService;
import top.arrietty.service.MiaoshaUserService;
import top.arrietty.service.OrderService;
import top.arrietty.service.RedisService;
import top.arrietty.vo.GoodsVo;

@Controller
@RequestMapping("/miaosha")
public class MiaoShaController implements InitializingBean
{

	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	OrderService orderService;
	
	@Autowired
	MiaoshaService miaoshaService;
	
	@Autowired
	MQSender mqSender;
	
	private Map<Long, Boolean> localOverMap = new HashMap<Long, Boolean>();
	
	@RequestMapping(value="/do_miaosha", method=RequestMethod.POST)
	@ResponseBody
	public Result<Integer> miaosha(Model model, MiaoShaUser user, @RequestParam("goodsId") long goodsId)
	{
		model.addAttribute("user", user);
		if (user==null)
			return Result.error(CodeMsg.SESSION_ERROR);
		//内存标记，减少redis访问
		boolean over = localOverMap.get(goodsId);
		if (over)
			return Result.error(CodeMsg.MIAO_SHA_OVER);
//		//判断库存
//		GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
//		int stock = goodsVo.getGoodsStock();
//		if (stock<=0)
//		{
//			return Result.error(CodeMsg.MIAO_SHA_OVER);
//		}
//		//判断是否秒杀到
//		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
//		if (order!=null)
//		{
//			return Result.error(CodeMsg.REPEATE_MIAOSHA);
//		}
//		//减库存 下订单 写入秒杀订单
//		OrderInfo orderInfo = miaoshaService.miaosha(user, goodsVo);
//		return Result.success(orderInfo);
		//预减库存
		long stock = redisService.decr(GoodsKey.getMiaoShaGoodsStock, ""+goodsId);
		if (stock<0)		
		{
			localOverMap.put(goodsId, true);
			return Result.error(CodeMsg.MIAO_SHA_OVER);
		}
		MiaoshaOrder order = orderService.getMiaoshaOrderByUserIdGoodsId(user.getId(), goodsId);
		if (order!=null)
		{
			return Result.error(CodeMsg.REPEATE_MIAOSHA);
		}
		//入队
		MiaoShaMessage mm = new MiaoShaMessage();
		mm.setUser(user);
		mm.setGoodsId(goodsId);
		mqSender.sendMiaoshaMessage(mm);
		return Result.success(0);//排队中
	}
	
	/*
	 * 系统初始化
	 */
	public void afterPropertiesSet() throws Exception
	{
		List<GoodsVo> goodsList = goodsService.listGoodsVo();
		if (goodsList==null)
			return;
		for (GoodsVo goods : goodsList)
		{
			System.out.println("stock:"+goods.getGoodsStock());
			redisService.set(GoodsKey.getMiaoShaGoodsStock, ""+goods.getId(), goods.getStockCount());
			localOverMap.put(goods.getId(), false);
		}
	}
	
	/*
	 *orderId：成功
	 *-1：秒杀失败
	 *0：排队中
	 */
	@RequestMapping(value="/result", method=RequestMethod.GET)
	@ResponseBody
	public Result<Long> miaoshaResult(Model model, MiaoShaUser user, @RequestParam("goodsId")long goodsId)
	{
		model.addAttribute("user", user);
		if (user==null)
			return Result.error(CodeMsg.SESSION_ERROR);
		long result = miaoshaService.getMiaoShaResult(user.getId(), goodsId);
		return Result.success(result);
	}
	
	public static void main(String[] args)
	{
		
		
	}
}
