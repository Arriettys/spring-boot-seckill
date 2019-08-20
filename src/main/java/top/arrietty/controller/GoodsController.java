package top.arrietty.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import top.arrietty.domain.MiaoShaUser;
import top.arrietty.redis.GoodsKey;
import top.arrietty.result.Result;
import top.arrietty.service.GoodsService;
import top.arrietty.service.MiaoshaUserService;
import top.arrietty.service.RedisService;
import top.arrietty.vo.GoodsDetailVo;
import top.arrietty.vo.GoodsVo;

@Controller
@RequestMapping("/goods")
public class GoodsController
{
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	@Autowired
	GoodsService goodsService;
	
	@Autowired
	ThymeleafViewResolver thymeleafViewResolver;
	
	
    @RequestMapping(value="/to_list", produces="text/html")
    @ResponseBody
    public String list(HttpServletRequest request, HttpServletResponse response, Model model, MiaoShaUser user) 
    {
    	model.addAttribute("user", user);
        //return "goods_list";
    	
    	//ҳ�滺��
    	//ȡ����
    	String html = redisService.get(GoodsKey.getGoodsList, "", String.class);
    	if (!StringUtils.isEmpty(html))
    		return html;
    	//�ֶ���Ⱦ
    	/*
    	 * spring4�е�SpringWebContext��Spring5��ȡ����
    	 * thymeleaf.spring5��API�аѴ󲿷ֹ����Ƶ���IWebContext,�޳��˶�ApplicationContext���������
    	 */
    	
    	//��ѯ��Ʒ�б�
    	List<GoodsVo> goodsList = goodsService.listGoodsVo();
    	model.addAttribute("goodsList", goodsList);
    	WebContext wc = new WebContext(request, response,
    			request.getServletContext(), request.getLocale(), model.asMap());
    	html = thymeleafViewResolver.getTemplateEngine().process("goods_list", wc);
    	//��Ϊ�վͱ��浽redis������
    	if (!StringUtils.isEmpty(html))
    	{
    		redisService.set(GoodsKey.getGoodsList, "", html);
    	}
    	return html;
    }
    /*
     * @PathVariable("xxx")
     * ͨ�� @PathVariable ���Խ�URL��ռλ������{xxx}�󶨵���������ķ����β���@PathVariable(��xxx��)
     * ���磬@RequestMapping(value=��user/{id}/{name}��)
     * 
     */
    @RequestMapping(value="/to_detail2/{goodsId}", produces="text/html")
    @ResponseBody
    public String detail2(HttpServletRequest request, HttpServletResponse response, 
    		Model model, MiaoShaUser user, @PathVariable("goodsId")long goodsId) 
    {
    	model.addAttribute("user", user);
    	
    	//ȡ����
    	String html = redisService.get(GoodsKey.getGoodsList, ""+goodsId, String.class);
    	if (!StringUtils.isEmpty(html))
    		return html;
    	//�ֶ���Ⱦ  	
    	//��ѯ��Ʒ�б�
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);
    	model.addAttribute("goods", goods);
    	
    	long startAt = goods.getStartDate().getTime();
    	long endAt = goods.getEndDate().getTime();
    	long now = System.currentTimeMillis();
    	int miaoshaStatus = 0;
    	int remainSeconds = 0;
    	if (now<startAt)
    	{
    		//��ɱδ��ʼ������ʱ
    		miaoshaStatus = 0;
    		remainSeconds = (int)((startAt - now)/1000);
    	} else if(now>endAt)
    	{
    		//��ɱ������
    		miaoshaStatus = 2;
    		remainSeconds = -1;
    	} else
    	{
    		//��ɱ������
    		miaoshaStatus = 1;
    		remainSeconds = 0;
    	}
    	model.addAttribute("miaoshaStatus", miaoshaStatus);
    	model.addAttribute("remainSeconds", remainSeconds);
    	
    	WebContext wc = new WebContext(request, response,
    			request.getServletContext(), request.getLocale(), model.asMap());
    	html = thymeleafViewResolver.getTemplateEngine().process("goods_detail", wc);
    	if (!StringUtils.isEmpty(html))
    	{
    		redisService.set(GoodsKey.getGoodsDetail, ""+goodsId, html);
    	}
    	return html;
        //return "goods_detail";  	
    }
    
    @RequestMapping(value="/to_detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> detail(HttpServletRequest request, HttpServletResponse response, 
    		Model model, MiaoShaUser user, @PathVariable("goodsId")long goodsId) 
    {
    	GoodsVo goods = goodsService.getGoodsVoByGoodsId(goodsId);	
    	long startAt = goods.getStartDate().getTime();
    	long endAt = goods.getEndDate().getTime();
    	long now = System.currentTimeMillis();
    	int miaoshaStatus = 0;
    	int remainSeconds = 0;
    	if (now<startAt)
    	{
    		//��ɱδ��ʼ������ʱ
    		miaoshaStatus = 0;
    		remainSeconds = (int)((startAt - now)/1000);
    	} else if(now>endAt)
    	{
    		//��ɱ������
    		miaoshaStatus = 2;
    		remainSeconds = -1;
    	} else
    	{
    		//��ɱ������
    		miaoshaStatus = 1;
    		remainSeconds = 0;
    	}
    	GoodsDetailVo vo = new GoodsDetailVo();
    	vo.setGoods(goods);
    	vo.setUser(user);
    	vo.setRemainSeconds(remainSeconds);
    	vo.setMiaoshaStatus(miaoshaStatus);
        return Result.success(vo);
    }
}
