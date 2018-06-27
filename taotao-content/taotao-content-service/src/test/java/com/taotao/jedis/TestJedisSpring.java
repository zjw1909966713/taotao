package com.taotao.jedis;


import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestJedisSpring {
	
	
	
	@Test
	public void testJedisClientPool() throws Exception{
		//初始化spring容器
		ApplicationContext applicationContext=new ClassPathXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		
		JedisClient jedisClient = applicationContext.getBean(JedisClient.class);
		
		//从容器中获得jedisclient
		
		jedisClient.set("aaaffa", "86");
		
		
		String result = jedisClient.get("aaaffa");
		
		
		System.out.println(result);
		//使用jedisclien
	}

}
