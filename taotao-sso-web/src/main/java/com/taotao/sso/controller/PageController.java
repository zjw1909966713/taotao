package com.taotao.sso.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 页面展示
 * @author Administrator
 *
 */
@Controller
public class PageController {
	
	
	@RequestMapping("/page/register")
	public String showRegister(){
		return "register";
	}
	
	@RequestMapping("/page/login")
	public String showLogin(String url,Model model,HttpServletRequest request){
	
		System.out.println("登录成功后跳转的"+url);
		model.addAttribute("redirectUrl", url);
		request.setAttribute("redirectUrl", url);
		return "login";
	}

}
