package com.taotao.sso.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.JsonUtils;
import com.taotao.jedis.JedisClient;
import com.taotao.mapper.TbUserMapper;
import com.taotao.pojo.TbUser;
import com.taotao.pojo.TbUserExample;
import com.taotao.pojo.TbUserExample.Criteria;
import com.taotao.sso.service.UserService;


@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private TbUserMapper userMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${USER_SESSION}")
	private String USER_SESSION;
	
	@Value("${SESSION_EXPIRE}")
	private Integer SESSION_EXPIRE;

	@Override
	public TaotaoResult checkUserData(String data, int type) {
		TbUserExample example=new TbUserExample();
		Criteria criteria=example.createCriteria();
		//设置查询条件
		
		//1.判断用户可用
		if(type==1){
			criteria.andUsernameEqualTo(data);
			
		}else if(type==2){//判断手机号是否可用
			criteria.andPhoneEqualTo(data);
		}else if(type==3){//判断邮箱是否可用
			criteria.andEmailEqualTo(data);
		}else{
			return TaotaoResult.build(400, "请求数据非法");
		}
		
		
		List<TbUser> list = userMapper.selectByExample(example);
		if(list.size()>0){
			return TaotaoResult.ok(false);
		}
		return TaotaoResult.ok(true);
	}

	@Override
	public TaotaoResult register(TbUser tbUser) {
		//检查数据的有效性
		if(StringUtils.isBlank(tbUser.getUsername())){
			return TaotaoResult.build(400, "用户名不能为空");
		}
		
		//判断用户名是否重复
		TaotaoResult taotaoResult = checkUserData(tbUser.getUsername(), 1);
		
		if(!(boolean) taotaoResult.getData()){
			return TaotaoResult.build(400, "用户名重复");
		}
		
		if(StringUtils.isBlank(tbUser.getPassword())){
			return TaotaoResult.build(400, "密码不能为空");
		}
		
		if(StringUtils.isNotBlank(tbUser.getPhone())){
			//重复校验
			taotaoResult=checkUserData(tbUser.getPhone(), 2);
			if(!(boolean) taotaoResult.getData()){
				return TaotaoResult.build(400, "手机号重复");
			}
			
		}
		
		if(StringUtils.isNotBlank(tbUser.getEmail())){
			//重复校验
			taotaoResult=checkUserData(tbUser.getEmail(), 3);
			if(!(boolean) taotaoResult.getData()){
				return TaotaoResult.build(400, "邮箱重复");
			}
			
		}
		
		
		tbUser.setCreated(new Date());
		tbUser.setUpdated(new Date());
		String md5DigestAsHex = DigestUtils.md5DigestAsHex(tbUser.getPassword().getBytes());
		tbUser.setPassword(md5DigestAsHex);
		
		//插入数据
		userMapper.insert(tbUser);
		
		
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult login(String username, String password) {
		//判断是否登录
		TbUserExample example=new TbUserExample();
		Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);
		List<TbUser> list = userMapper.selectByExample(example);
		if(list.size()==0){
			return TaotaoResult.build(400, "用户名和密码不匹配");
		}
		
		//密码md5之后校验
		TbUser user=list.get(0);
		System.out.println(DigestUtils.md5DigestAsHex(password.getBytes()));
		if(!DigestUtils.md5DigestAsHex(password.getBytes()).equals(user.getPassword())){
			return TaotaoResult.build(400, "用户名和密码不匹配");
		}
		String token=UUID.randomUUID().toString();
		user.setPassword(null);//清除密码
		//把用户信息保存到redis中,key是token,value是用户信息
		jedisClient.set("USER_SESSION:"+token,JsonUtils.objectToJson(user));
		//设置key的过期时间
		jedisClient.expire("USER_SESSION:"+token, SESSION_EXPIRE);
		
		return TaotaoResult.ok(token);
	}

	@Override
	public TaotaoResult getUserByToken(String token) {
		String json = jedisClient.get("USER_SESSION:"+token);
		if(StringUtils.isBlank(json)){
			return TaotaoResult.build(400, "用户没有登录");
		}
		
		jedisClient.expire("USER_SESSION:"+token, SESSION_EXPIRE);
		TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
		
		
		return TaotaoResult.ok(user);
	}

	@Override
	public TaotaoResult logout(String token) {
		jedisClient.del("USER_SESSION:"+token);
		return TaotaoResult.ok();
	}

}
