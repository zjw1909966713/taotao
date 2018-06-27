package co.taotao.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class TestFreemarker {


	@Test
	public void testFreemarker() throws Exception{
		//1.创建一个模板文件
		//2.创建一个configuration对象
		Configuration configuration=new Configuration(Configuration.getVersion());
		//3.设置模板所在路劲
		configuration.setDirectoryForTemplateLoading(new File("E:/workspace/eclipsemars/taotao-item-web/src/main/webapp/WEB-INF/ftl/"));
		//4.设置模板的字符集,一般utf-8
		configuration.setDefaultEncoding("utf-8");
		//5.使用Configuration对象加载一个模板文件,需要指定文件名
		Template template = configuration.getTemplate("student.ftl");
		//6.创建一个数据集,可以是pojo也可以是map,推荐使用map
		//7.创建一个 writer对象,指定输出文件的路径和文件名
		Map data=new HashMap<>();
		data.put("hello", "hello freemarker");
		
		Student student=new Student(1, "小张", 12, "天津市津南区辛庄镇");
		data.put("student", student);

		Writer out=new FileWriter(new File("E:/www/javaee28/out/student.html"));
		//8.使用模板对象的process方法输出文件
		template.process(data, out);
		//9.关闭流
		out.close();
		
	}
}
