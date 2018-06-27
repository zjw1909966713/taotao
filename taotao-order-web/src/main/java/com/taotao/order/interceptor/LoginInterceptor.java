package com.taotao.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbUser;
import com.taotao.sso.service.UserService;

/**
 * 登录拦截器
 * @author Administrator
 *
 */
public class LoginInterceptor implements HandlerInterceptor {

	

	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	

	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Autowired
	private UserService userService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//执行handler之前先执行此方法
		//返回true:放行 ,返回false:拦截
		//1.从cookie中取token信息
		String token=CookieUtils.getCookieValue(request, TOKEN_KEY);
		
		//2.取不到token,调到sso
		if(StringUtils.isBlank(token)){
			//去当前的url
			String requestUrl=request.getRequestURL().toString();
			
			//跳转到登录页面
			response.sendRedirect(SSO_URL+"/page/login?url="+requestUrl);
			//拦截
			return false;
		}
		//3.取到token ,调用sso判断是否登录
		TaotaoResult taotaoResult = userService.getUserByToken(token);
		if(taotaoResult.getStatus()!=200){
			//去当前的url
			String requestUrl=request.getRequestURL().toString();
			
			//跳转到登录页面
			response.sendRedirect(SSO_URL+"/page/login?url="+requestUrl);
			//拦截
			return false;
		}
		
		
		//5.
		//String json = taotaoResult.getData().toString();
		
		//把用户信息放到request中
		request.setAttribute("user",taotaoResult.getData());
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// handler执行之后,modelAndView返回之前

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// 在modelandview返回之后,异常处理

	}

}
