package top.arrietty.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.arrietty.domain.MiaoShaUser;
import top.arrietty.result.Result;
import top.arrietty.service.MiaoshaUserService;
import top.arrietty.service.RedisService;

@Controller
@RequestMapping("/user")
public class UserController
{
	@Autowired
	MiaoshaUserService userService;
	
	@Autowired
	RedisService redisService;
	
	
    @RequestMapping("/info")
    @ResponseBody
    public Result<MiaoShaUser> info(Model model, MiaoShaUser user) 
    { 	
    	model.addAttribute("user", user);
        return Result.success(user);
    }
    
}
