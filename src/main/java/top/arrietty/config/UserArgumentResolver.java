package top.arrietty.config;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import top.arrietty.domain.MiaoShaUser;
import top.arrietty.service.MiaoshaUserService;
/*
 * 会对Controller层方法的参数执行 HandlerMethodArgumentResolver(对参数的解析器)中的方法
 */
@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver
{
	@Autowired
	MiaoshaUserService miaoshaUserService;
	
	/*
	 * 解析器是否支持参数
	 * 判断Controller层中的参数，是否满足条件，满足条件则执行resolveArgument方法，不满足则跳过
	 */
	public boolean supportsParameter(MethodParameter parameter)
	{
		Class<?> clazz = parameter.getParameterType();
		return  clazz==MiaoShaUser.class;
	}
	/*
	 * 解析参数,填充到参数值
	 */
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception
	{
		HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
		HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
		String paramToken = request.getParameter(MiaoshaUserService.COOKI_NAME_TOKEN);
		String cookieToken = getCookieValue(request, MiaoshaUserService.COOKI_NAME_TOKEN);
		if(StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) 
		{
			return null;
		}
		String token = StringUtils.isEmpty(paramToken)?cookieToken:paramToken;
		return miaoshaUserService.getByToken(response, token);
	}

	private String getCookieValue(HttpServletRequest request, String cookiName)
	{
		Cookie[] cookies = request.getCookies();
		if (cookies==null || cookies.length<=0)
			return null;
		for (Cookie cookie : cookies)
		{
			if (cookie.getName().equals(cookiName));
			return cookie.getValue();
		}
		return null;
	}

}
