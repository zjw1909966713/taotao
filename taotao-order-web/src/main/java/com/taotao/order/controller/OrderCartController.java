package com.taotao.order.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.order.pojo.OrderInfo;
import com.taotao.order.service.OrderService;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbUser;

/**
 *订单确认页面Controller
 * @author Administrator
 *
 */
@Controller
public class OrderCartController {
	
	@Value("${TT_CART}")
	private String TT_CART;
	
	
	
	
	@Autowired
	private OrderService orderService;
	
	/**
	 * 展示订单确认页面
	 * @param request
	 * @return
	 */
	@RequestMapping("/order/order-cart")
	public String showOrderCart(HttpServletRequest request){
		//用户必须是登录状态
		//取用户id
		TbUser user = (TbUser) request.getAttribute("user");
		System.out.println(user.getUsername());
		
		//根据用户信息取收货地址列表
		//从cookie中取购物车商品列表展示到页面
		List<TbItem> cartList = this.getCartItemList(request);
		request.setAttribute("cartList", cartList);
		//返回逻辑视图
		return "order-cart";
	}
	
	private List<TbItem> getCartItemList(HttpServletRequest request) {
		// 从cookie中获取购物车商品列表
		String json = CookieUtils.getCookieValue(request, TT_CART, true);
		if (StringUtils.isBlank(json)) {
			return new ArrayList<>();
		}

		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);

		return list;
	}
	
	
	
	/**
	 * 生成订单
	 */
	@RequestMapping(value="/order/create",method=RequestMethod.POST)
	public String createOrder(OrderInfo orderInfo,Model model,HttpServletRequest request,HttpServletResponse response){
		//生成订单
		TaotaoResult result = orderService.createOrder(orderInfo);
		
		//删除购物车
		CookieUtils.deleteCookie(request, response, TT_CART);
		
		//返回逻辑视图
		model.addAttribute("orderId", result.getData().toString());
		model.addAttribute("payment",orderInfo.getPayment());
		
		DateTime dateTime=new DateTime();
		DateTime dateTime2 = dateTime.plusDays(3);
		model.addAttribute("date", dateTime2.toString("yyyy-MM-dd")); 
		return "success";
	}
	
}
