package com.taotao.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 *网页静态化
 * @author Administrator
 *
 */
@Controller
public class HtmlGenController {
	
	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@RequestMapping("/genhtml")
	@ResponseBody
	public String  genHtml() throws Exception {
		
		Configuration configuration=freeMarkerConfigurer.getConfiguration();
		
		Template template = configuration.getTemplate("hello.ftl");
		Map data=new HashMap<>();
		
		data.put("hello", "spring freemarker test");
		
		Writer out=new FileWriter(new File("E:/www/javaee28/out/test.html"));
		//8.使用模板对象的process方法输出文件
		template.process(data, out);
		//9.关闭流
		out.close();
		
		return "OK";
	}

}
