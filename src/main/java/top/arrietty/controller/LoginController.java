package top.arrietty.controller;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import top.arrietty.MD5.ValidatorUtil;
import top.arrietty.result.CodeMsg;
import top.arrietty.result.Result;
import top.arrietty.service.MiaoshaUserService;
import top.arrietty.service.UserService;
import top.arrietty.vo.LoginVo;

@Controller
@RequestMapping("/login")
public class LoginController
{
	private static Logger log = LoggerFactory.getLogger(LoginController.class);
	@Autowired 
	UserService userService;
	@Autowired
	MiaoshaUserService miaoshaUserService;
	@RequestMapping("/to_login")
	public String toLogin()
	{
		return "login";
	}
	
	@RequestMapping("/do_login")
	@ResponseBody
	public Result<String> dologin(HttpServletResponse response, @Valid LoginVo loginVo)
	{
		log.info(loginVo.toString());
		//µÇÂ¼
		String token = miaoshaUserService.login(response, loginVo);
		return Result.success(token);
	}
}
