package com.taotao.portal.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.taotao.common.utils.JsonUtils;
import com.taotao.content.service.ContentService;
import com.taotao.pojo.TbContent;
import com.taotao.portal.pojo.AD1Node;

/**
 * 首页
 * @author Administrator
 *
 */
@Controller
public class IndexController {
	
	@Autowired
	private ContentService contentService;
	
	
	@Value("${AD1_CATEGORY_ID}")
	private Long AD1_CATEGORY_ID;
	
	@Value("${AD1_WIDTH}")
	private Integer AD1_WIDTH;
	
	@Value("${AD1_WIDTH_B}")
	private Integer AD1_WIDTH_B;
	
	@Value("${AD1_HEIGHT}")
	private Integer AD1_HEIGHT;
	
	@Value("${AD1_HEIGHT_B}")
	private Integer AD1_HEIGHT_B;
	
	@RequestMapping("/index")
	public String showIndex(Model model){
		//根据cid查询轮播图
		List<TbContent> contentList = contentService.getContentByCid(AD1_CATEGORY_ID);
		
		System.out.println(JsonUtils.objectToJson(contentList));
		
		List<AD1Node> ad1Nodes=new ArrayList<>();
		for (TbContent tbContent:contentList) {
			AD1Node node=new AD1Node();
			node.setAlt(tbContent.getTitle());
			node.setHeight(AD1_HEIGHT);
			node.setHeightB(AD1_HEIGHT_B);
			node.setWidth(AD1_WIDTH);
			node.setWidthB(AD1_WIDTH_B);
			node.setSrc(tbContent.getPic());
			node.setSrcB(tbContent.getPic2());
			node.setHref(tbContent.getUrl());
			
			ad1Nodes.add(node);
			
		}
		
		//转化成JSON
		String ad1Json=JsonUtils.objectToJson(ad1Nodes);
		
		
		model.addAttribute("ad1", ad1Json);
		return "index";
	}

}
