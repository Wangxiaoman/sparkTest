package com.wxm.service.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisLocalPool {
	
	private JedisPool jedisPool;
	
	public RedisLocalPool(String host,int port,int maxTotal,int maxIdle,int maxWaitMillis) {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(maxTotal);
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxWaitMillis(maxWaitMillis);
        jedisPool = new JedisPool(poolConfig, host, port);
	}
	
	public Jedis getJedis(){
		return jedisPool.getResource();
	}
	
	public void release(Jedis jedis){
		jedis.close();
	}
}
