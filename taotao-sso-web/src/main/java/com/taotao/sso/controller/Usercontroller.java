package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

@Controller
public class Usercontroller {
	
	@Autowired
	private UserService userService;
	
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public TaotaoResult checkUserData(@PathVariable String param,@PathVariable Integer type){
		TaotaoResult result = userService.checkUserData(param, type);
		return result;
	}
	
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult register(TbUser user){
		TaotaoResult result = userService.register(user);
		return result;
		
	}
	
	/**
	 * 登录
	 * @param username
	 * @param password
	 * @return
	 */
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public TaotaoResult login(String username,String password,HttpServletResponse response,HttpServletRequest request){
		TaotaoResult result = userService.login(username, password);
		
		
		//把token写入cookie
		if(result.getStatus()==200){
			
			CookieUtils.setCookie(request, response, TOKEN_KEY, result.getData().toString());
		}
		
		return result;
	}
	
	
	
	
	/*@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public String getUserByToken(@PathVariable String token,String callback){
		TaotaoResult result = userService.getUserByToken(token);
		if(StringUtils.isNotBlank(callback)){
			return callback+"("+JsonUtils.objectToJson(result)+");";
		}
		
		return JsonUtils.objectToJson(result);
	}*/
	
	
	
	//jsonp两种方法,spring4.1以上
	
	@RequestMapping(value="/user/token/{token}",method=RequestMethod.GET)
	@ResponseBody
	public Object getUserByToken(@PathVariable String token,String callback){
		TaotaoResult result = userService.getUserByToken(token);
		if(StringUtils.isNotBlank(callback)){
			MappingJacksonValue mappingJacksonValue=new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		
		return result;
	}
	
	
	@RequestMapping(value="/logout/{token}",method=RequestMethod.GET)
	@ResponseBody
	public TaotaoResult logout(@PathVariable String token){
		TaotaoResult result = userService.logout(token);
		
		return result;
	}
	
	
}
