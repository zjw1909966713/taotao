package com.taotao.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {

	
	@Test
	public void testAddDocument() throws  Exception{
		//创建一个solrServer对象,创建一个HttpSolrServer对象
		//需要指定solr服务的url
		SolrServer solrServer=new HttpSolrServer("http://172.16.15.123:8080/solr/collection1");
		//创建一个文档对象solrInputDocument
		SolrInputDocument document=new SolrInputDocument();
		
		//向文档添加域,必须有id域,域的名称必须在schema.xml中定义
		document.addField("id", "12233");
		document.addField("item_title", "999");
		document.addField("item_price", 123000);
		//把文档对象写入索引库
		
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	
	@Test
	public void deleteDocumentById() throws Exception{
		SolrServer solrServer=new HttpSolrServer("http://172.16.15.123:8080/solr/collection1");
		//solrServer.deleteById("12233");
		solrServer.commit();
	}
	
	@Test
	public void deleteDocumentByQuery() throws Exception{
		SolrServer solrServer=new HttpSolrServer("http://172.16.15.123:8080/solr/collection1");
		//solrServer.deleteByQuery("item_title:测试商品122");
		solrServer.commit();
	}
	
	
	@Test
	public void searchDocument() throws Exception{
		//创建一个solrServer对象
		SolrServer solrServer=new HttpSolrServer("http://172.16.15.123:8080/solr/collection1");
		//创建一个SolrQuery对象
		
		SolrQuery query=new SolrQuery();
		
		//设置查询条件,过滤条件,分页条件,排序条件,高亮
		
		//query.set("q","");
		query.setQuery("手机");
		query.setStart(30);
		query.setRows(20);
		
		//设置默认搜索域
		query.set("df", "item_keywords");
		
		//设置高亮显示
		query.setHighlight(true);
		query.addHighlightField("item_title");
		
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		
		
		
		//执行查询 得到一个Response
		
		QueryResponse response = solrServer.query(query);
		
		
		
		
		SolrDocumentList solrDocumentList = response.getResults();
		
		//去查询结果总记录数
		System.out.println("查询结果总记录数:"+solrDocumentList.getNumFound());
		
		Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
		
		
		for (SolrDocument solrDocument : solrDocumentList) {
			System.out.println(solrDocument.get("id"));
			//取高亮显示
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String itemTitle="";
			if(list.size()>0){
				itemTitle=list.get(0);
			}else{
				itemTitle=solrDocument.get("item_title").toString();
			}
			
			
			
			
			System.out.println(itemTitle);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
			System.out.println(solrDocument.get("item_desc"));
			System.out.println("====================================");
		}
	}
	
	
}
