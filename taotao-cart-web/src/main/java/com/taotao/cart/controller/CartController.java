package com.taotao.cart.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.common.utils.CookieUtils;
import com.taotao.common.utils.JsonUtils;
import com.taotao.pojo.TbItem;
import com.taotao.service.ItemService;

@Controller
public class CartController {

	@Value("${TT_CART}")
	private String TT_CART;

	@Value("${CART_EXPIER}")
	private int CART_EXPIER;

	@Autowired
	private ItemService itemService;

	/**
	 * 加入购物车
	 * 
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addItemCart(@PathVariable Long itemId, @RequestParam(defaultValue = "1") Integer num,
			HttpServletRequest request, HttpServletResponse response) {
		// 去购物车列表
		List<TbItem> cartItemList = this.getCartItemList(request);
		System.out.println(cartItemList.size());
		// 判断是否在购物车中是否存在
		boolean flag = false;
		for (TbItem tbItem : cartItemList) {
			if (tbItem.getId() == itemId.longValue()) {
				flag = true;
				tbItem.setNum(tbItem.getNum() + num);
				break;
			}
		}

		// 如果不存在
		if (!flag) {
			TbItem item = itemService.getItemById(itemId);
			item.setNum(num);
			String image = item.getImage();
			if (StringUtils.isNotBlank(image)) {
				item.setImage(image.split(",")[0]);
			}

			cartItemList.add(item);
		}

		// 把购物车加到cookie中
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);

		return "cartSuccess";
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

	@RequestMapping("/cart/cart")
	public String showCartList(HttpServletRequest request) {
		List<TbItem> cartItemList = this.getCartItemList(request);
		request.setAttribute("cartList", cartItemList);
		return "cart";
	}

	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public TaotaoResult updateItemNum(@PathVariable Long itemId, @PathVariable Integer num, HttpServletRequest request,
			HttpServletResponse response) {
		List<TbItem> cartItemList = this.getCartItemList(request);
		for (TbItem tbItem : cartItemList) {
			if (tbItem.getId() == itemId.longValue()) {
				tbItem.setNum(num);
				break;
			}
		}
		// 把购物车加到cookie中
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);

		return TaotaoResult.ok();
	}

	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request, HttpServletResponse response) {
		List<TbItem> cartItemList = this.getCartItemList(request);
		for (TbItem tbItem : cartItemList) {
			if (tbItem.getId() == itemId.longValue()) {
				cartItemList.remove(tbItem);
				break;
			}
		}
		// 把购物车加到cookie中
		CookieUtils.setCookie(request, response, TT_CART, JsonUtils.objectToJson(cartItemList), CART_EXPIER, true);

		return "redirect:/cart/cart.html";
	}
}
