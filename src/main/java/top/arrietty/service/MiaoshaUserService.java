package top.arrietty.service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import top.arrietty.MD5.MD5Util;
import top.arrietty.MD5.UUIDUtil;
import top.arrietty.dao.MiaoshaUserDao;
import top.arrietty.domain.MiaoShaUser;
import top.arrietty.exception.GlobalException;
import top.arrietty.redis.MiaoshaUserKey;
import top.arrietty.result.CodeMsg;
import top.arrietty.vo.LoginVo;

@Service
public class MiaoshaUserService
{
	public static final String COOKI_NAME_TOKEN = "token";
	@Autowired
	MiaoshaUserDao miaoshaUserDao;
	@Autowired
	RedisService redisService;
	
	public MiaoShaUser getById(long id)
	{
		//ȡ����
		MiaoShaUser user = redisService.get(MiaoshaUserKey.getById, ""+id, MiaoShaUser.class);
		if (user!=null)
			return user;
		//ȡ���ݿ�
		user = miaoshaUserDao.getById(id);
		if (user!=null)
			redisService.set(MiaoshaUserKey.getById, ""+id, user);
		
		return user;
	}
	
	public MiaoShaUser getByToken(HttpServletResponse response, String token)
	{
		if(StringUtils.isEmpty(token)) 
		{
			return null;
		}
		MiaoShaUser user = redisService.get(MiaoshaUserKey.token, token, MiaoShaUser.class);
		//�ӳ���Ч��
		if(user != null) 
		{
			addCookie(response, token, user);
		}
		return user;
	}
	
	public boolean updatePassword(String token, long id, String password)
	{
		//ȥuser
		MiaoShaUser user = getById(id);
		if (user==null)
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		MiaoShaUser toBeUpdate = new MiaoShaUser();
		//�������ݿ�
		toBeUpdate.setPassword(MD5Util.formPassToDBPass(password, user.getSalt()));
		miaoshaUserDao.update(toBeUpdate);
		//������
		redisService.delete(MiaoshaUserKey.getById, ""+id);
		user.setPassword(toBeUpdate.getPassword());
		redisService.set(MiaoshaUserKey.getById, token, user);
		return true;
	}
	
	public String login(HttpServletResponse response, LoginVo loginVo)
	{
		if (loginVo==null)
			throw new GlobalException(CodeMsg.SERVER_ERROR);
		String mobile = loginVo.getMobile();
		String password = loginVo.getPassword();
		//����ֻ����Ƿ����
		MiaoShaUser miaoshaUser = getById(Long.parseLong(mobile));
		if (miaoshaUser==null)
			throw new GlobalException(CodeMsg.MOBILE_NOT_EXIST);
		//��֤����
		String dbPass = miaoshaUser.getPassword();
		String saltDB = miaoshaUser.getSalt();
		String calculatePass = MD5Util.formPassToDBPass(password, saltDB);
		if (!calculatePass.equals(dbPass))
			throw new GlobalException(CodeMsg.PASSWORD_ERROR);
		//����cookie
		String token = UUIDUtil.uuid(); 
		addCookie(response, token, miaoshaUser);
		return token;
	}
	
	private void addCookie(HttpServletResponse response, String token, MiaoShaUser user) 
	{
		redisService.set(MiaoshaUserKey.token, token, user);
		Cookie cookie = new Cookie(COOKI_NAME_TOKEN, token);
		cookie.setMaxAge(MiaoshaUserKey.token.expireSeconds());
		cookie.setPath("/");
		response.addCookie(cookie);
	}
}
