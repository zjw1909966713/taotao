package com.taotao.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.taotao.item.pojo.Item;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.service.ItemService;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class ItemAddMessageListener implements MessageListener{
	
	@Autowired
	private ItemService itemService;
	
	
	@Value("${HTML_OUT_PATH}")
	private String HTML_OUT_PATH;
	
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;

	@Override
	public void onMessage(Message message) {
		try {
			//从消息中取商品id
			TextMessage textMessage=(TextMessage) message;
			String strId=textMessage.getText();
			Long itemId=Long.parseLong(strId);
			//根据商品id查询商品信息及商品描述
			System.out.println("消费者消费"+strId);
			
			Thread.sleep(1000);
			
			
			TbItem tbItem=itemService.getItemById(itemId);
			Item item=new Item(tbItem);
			TbItemDesc itemDesc=itemService.getItemDescById(itemId);
			
			
			//使用freemarker生成静态页面
			Configuration configuration=freeMarkerConfigurer.getConfiguration();
			configuration.setDefaultEncoding("UTF-8");
			//1.创建模板
			//2.加载模板对象
			Template template = configuration.getTemplate("item.ftl");
			
			template.setEncoding("UTF-8");
			//3.准备模板需要的的数据
			Map data=new HashMap<>();
			data.put("item", item);
			data.put("itemDesc", itemDesc);
			
			//4.指定输出的目录及文件名
			Writer out=new FileWriter(new File(HTML_OUT_PATH+strId+".html"));
			//5.生成静态页面
			template.process(data, out);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
