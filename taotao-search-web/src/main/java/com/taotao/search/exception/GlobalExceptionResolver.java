package com.taotao.search.exception;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

public class GlobalExceptionResolver implements HandlerExceptionResolver {

	private static final Logger logger=LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	
	@Override
	public ModelAndView resolveException(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2,
			Exception arg3) {
		logger.info("进入全局异常处理器......");
		logger.debug("测试handler的类型:"+arg2.getClass());
		//控制台打印异常
		arg3.printStackTrace();
		//向日志文件中写入异常
		logger.error("系统发送异常", arg3);
		
		//发邮件
		
		//jmail
		
		//发短信
		//展示错误页面
		
		
		ModelAndView modelAndView=new ModelAndView();
		modelAndView.addObject("message", "系统发送异常,请您稍后重试");
		modelAndView.setViewName("error/exception");
		return modelAndView;
	}

}
