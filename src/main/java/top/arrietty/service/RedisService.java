package top.arrietty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import top.arrietty.domain.MiaoShaUser;
import top.arrietty.redis.KeyPrefix;

@Service
public class RedisService
{
	@Autowired
	JedisPool jedisPool;
	//��ȡ����
	public <T> T get(KeyPrefix prefix, String key, Class<T> clazz)
	{
		Jedis jedis = null;
		try
		{
			jedis = jedisPool.getResource();
			//����������key
			 String realKey  = prefix.getPrefix() + key;
			 String  str = jedis.get(realKey);
			 T t =  StringToBean(str, clazz);
			return t;
		} finally
		{
			if (jedis!=null)
				jedis.close();
		}
	}
	//���ö���
	public <T> boolean set(KeyPrefix prefix, String key, T value)
	{
		Jedis jedis = null;
		try
		{ 
			jedis = jedisPool.getResource();
			String str = beanToString(value);
			if(str == null || str.length() <= 0) 
			{
				return false;
			}
			//����������key
			String realKey  = prefix.getPrefix() + key;
			int seconds =  prefix.expireSeconds();
			if(seconds <= 0) 
			{
				 jedis.set(realKey, str);
			 }else 
			 {
				 jedis.setex(realKey, seconds, str);
			 }
			 return true;
		} finally
		{
			if(jedis!=null)
				jedis.close();
		}
	}
	
	public <T> String beanToString(T value)
	{
		if (value==null) return null;
		Class<?> clazz = value.getClass();
		if (clazz==int.class || clazz==Integer.class)
		{ 
			return ""+value;
		} else if (clazz==String.class)
		{
			return (String) value;
		} else if (clazz==long.class || clazz==Long.class)
		{
			return ""+value;
		} else
		{
			return JSON.toJSONString(value);
		}
	}
	
	public <T> T StringToBean(String str, Class<T> clazz)
	{
		if (str==null || str.length()==0 || clazz==null)
			return null;
		if (clazz==int.class || clazz==Integer.class)
		{
			return (T) Integer.valueOf(str);
		} else if (clazz==long.class || clazz==Long.class)
		{
			return (T) Long.valueOf(str);
		} else if (clazz==String.class)
		{
			return (T) str;
		} else
		{
			return JSON.toJavaObject(JSON.parseObject(str), clazz);
		}
	}
	
	public boolean delete(KeyPrefix prefix, String key)
	{
		Jedis jedis = null;
		jedis = jedisPool.getResource();
		String realKey = prefix.getPrefix() + key;
		long res = jedis.del(realKey);
		return res>0;
	}
	
	/*
	 * 增加值
	 */
	public <T> Long incr(KeyPrefix prefix, String key)
	{
		Jedis jedis = null;
		try
		{
			jedis = jedisPool.getResource();
			//生成真正的key
			String realKey = prefix.getPrefix() + key;
			return jedis.incr(realKey);
		} finally
		{
			if (jedis!=null)
				jedis.close();
		}
	}
	
	/*
	 * 减小值
	 */
	public <T> Long decr(KeyPrefix prefix, String key)
	{
		Jedis jedis = null;
		try
		{
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			return jedis.decr(realKey);
		} finally
		{
			if (jedis!=null)
				jedis.close();
		}
	}
	/*
	 * 判断key是否存在
	 */
	public <T> boolean exists(KeyPrefix prefix, String key)
	{
		Jedis jedis = null;
		try
		{
			jedis = jedisPool.getResource();
			String realKey = prefix.getPrefix() + key;
			return jedis.exists(realKey);
		} finally
		{
			if (jedis!=null)
				jedis.close();
		}
	}
}
