package com.wxm.service.redis;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import redis.clients.jedis.Jedis;

public class RedisDemo {

	public static void randomWriteAndRead(RedisLocalPool redisPool) {
		for (int i = 0; i < 10; i++) {
			new Thread(new randomWriteAndReadThread(redisPool)).start();
		}
	}

	public static void main(String[] args) {
//		RedisLocalPool redisPool = new RedisLocalPool("tencent-recom-tair01", 6379, 100, 20, 20000);
		RedisLocalPool redisPool = new RedisLocalPool("localhost", 6379, 100, 20, 2000);
		randomWriteAndRead(redisPool);
	}
}

class randomWriteAndReadThread implements Runnable{
	private RedisLocalPool redisPool;
	public randomWriteAndReadThread(RedisLocalPool redisPool){
		this.redisPool = redisPool;
	}
	
	@Override
	public void run() {
		Jedis jedis = redisPool.getJedis();
		while (true) {
			String kv = UUID.randomUUID().toString();
			jedis.set(kv, kv);
			
			try {
				TimeUnit.MICROSECONDS.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}		
	}
	
}
