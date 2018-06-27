package com.taotao.jedis;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class TestJedis {

	@Test
	public void testJedis() throws Exception {
		Jedis jedis = new Jedis("172.16.9.214", 6379);

		jedis.set("jedis-key", "123456");

		String string = jedis.get("jedis-key");
		System.out.println(string);
		jedis.close();
	}

	
	@Test
	public void testJedisPool() throws Exception {

		// 创建一个数据库连接池对象,需要制定服务的ip和端口号
		//从连接池中获得连接
		//使用Jedis 操作数据库
		//一定要关闭jedis连接
		//系统关闭前关闭连接池
		
		JedisPool jedisPool=new JedisPool("172.16.9.214", 6379);
		Jedis jedis=jedisPool.getResource();
		String result=jedis.get("jedis-key");
		System.out.println(result);
		
		jedis.close();
		jedisPool.close();
	}
	
	
	@Test
	public void testJedisCluster() throws Exception{
		
		//创建一个JedisCluster对象,构造参数set类型,集合中每个元素是HostAndPort类型
		
		Set<HostAndPort> nodes=new HashSet<>();
		nodes.add(new HostAndPort("172.16.9.214", 7001));
		nodes.add(new HostAndPort("172.16.9.214", 7002));
		nodes.add(new HostAndPort("172.16.9.214", 7003));
		nodes.add(new HostAndPort("172.16.9.214", 7004));
		nodes.add(new HostAndPort("172.16.9.214", 7005));
		nodes.add(new HostAndPort("172.16.9.214", 7006));
		
		JedisCluster jedisCluster=new JedisCluster(nodes);
		//直接使用JedisCluster操作redis,自带连接池,jedisCluster对象可以是单例的
		jedisCluster.set("cluster-test", "hello jedis cluster");
		
		String string = jedisCluster.get("cluster-test");
		
		System.out.println(string);
		
		jedisCluster.close();
		
	}

}
