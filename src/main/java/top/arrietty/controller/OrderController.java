package top.arrietty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.druid.stat.TableStat.Mode;

import top.arrietty.domain.MiaoShaUser;
import top.arrietty.domain.OrderInfo;
import top.arrietty.result.CodeMsg;
import top.arrietty.result.Result;
import top.arrietty.service.GoodsService;
import top.arrietty.service.OrderService;
import top.arrietty.vo.GoodsVo;
import top.arrietty.vo.OrderDetailVo;

@Controller
@RequestMapping("/order")
public class OrderController
{
	@Autowired
	OrderService orderService;
	@Autowired
	GoodsService goodsService;
	public Result<OrderDetailVo> info(Mode model, MiaoShaUser user, @RequestParam("orderId")long orderId)
	{
		if (user==null)
			return Result.error(CodeMsg.SESSION_ERROR);
		OrderInfo order = orderService.getOrderById(orderId);
		if (order==null)
			return Result.error(CodeMsg.ORDER_NOT_EXIST);
		long goodsId = order.getGoodsId();
		GoodsVo goodsVo = goodsService.getGoodsVoByGoodsId(goodsId);
		OrderDetailVo vo = new OrderDetailVo();
		vo.setGoods(goodsVo);
		vo.setOrder(order);
		return Result.success(vo);
	}
}
