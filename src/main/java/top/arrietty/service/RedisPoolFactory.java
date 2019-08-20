package top.arrietty.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import top.arrietty.redis.RedisConfig;

@Service
public class RedisPoolFactory
{
	@Autowired
	RedisConfig redisConfig;
	@Bean
	public JedisPool JedisPoolFactory()
	{
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
		poolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
		poolConfig.setMaxWaitMillis(redisConfig.getPoolMaxwait()*1000);
		JedisPool jp = new JedisPool(poolConfig, redisConfig.getHost(), redisConfig.getPort(), redisConfig.getTimeout()*1000, redisConfig.getPassword(), 0);
		return jp;
	}
}
