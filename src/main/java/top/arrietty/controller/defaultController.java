package top.arrietty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.arrietty.domain.User;
import top.arrietty.rabbitmq.MQSender;
import top.arrietty.redis.UserKey;
import top.arrietty.result.Result;
import top.arrietty.service.RedisService;
import top.arrietty.service.UserService;

@Controller
@RequestMapping("/default")
public class defaultController
{
	@Autowired
	UserService userService;
	@Autowired
	RedisService redisService;
	@Autowired
	MQSender mqSender;

	public String shymeleaf(ModelMap map)
	{
		map.addAttribute("name", "joe");
		return "hello";
	}
	
	@RequestMapping("/db/get")
	@ResponseBody
	public String dbGet()
	{
		User user = userService.getById(1);
		return user.getName();
	}
	
	@RequestMapping("/db/insert")
	@ResponseBody
	public Boolean dbInsert()
	{
		return userService.tx();
	}
	
	@RequestMapping("/redis/get")
    @ResponseBody
    public Result<User> redisGet() 
	{
    	User  user  = redisService.get(UserKey.getById, ""+1, User.class);
        return Result.success(user);
    }
    
    @RequestMapping("/redis/set")
    @ResponseBody
    public Result<Boolean> redisSet() 
    {
    	User user  = new User();
    	user.setId(1);
    	user.setName("1111");
    	redisService.set(UserKey.getById, ""+1, user);//UserKey:id1
        return Result.success(true);
    }
    @RequestMapping("/mq")
    @ResponseBody
    public Result<String> mq()
    {
    	mqSender.send("Hello World");
    	return Result.success("Hello World");
    }
    @RequestMapping("/mq/topic")
    @ResponseBody
    public Result<String> topic()
    {
    	mqSender.sendTopic("Hello World");
    	return Result.success("Hello World");
    }
    @RequestMapping("/mq/fanout")
    @ResponseBody
    public Result<String> fanout()
    {
    	mqSender.sendFanout("Hello World");
    	return Result.success("Hello World");
    }
    @RequestMapping("/mq/headers")
    @ResponseBody
    public Result<String> headers()
    {
    	mqSender.sendHeaders("Hello World");
    	return Result.success("Hello World");
    }
}
