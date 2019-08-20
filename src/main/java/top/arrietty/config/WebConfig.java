package top.arrietty.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter
{
	@Autowired
	UserArgumentResolver userArgumentResolver;
	
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers)
	{
		/* 注册自定义控制器(controller)方法参数类型*/
		argumentResolvers.add(userArgumentResolver);
	}
}
