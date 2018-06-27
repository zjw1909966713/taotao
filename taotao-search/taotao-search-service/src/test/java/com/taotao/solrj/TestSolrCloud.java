package com.taotao.solrj;

import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrCloud {
	
	
	@Test
	public void testSolrCloudAddDocument() throws Exception{
		//创建一个CloudSolrServer对象,构造方法制定zookeeper
		CloudSolrServer cloudSolrServer=new CloudSolrServer("172.16.15.123:2181,172.16.15.123:2182,172.16.15.123:2183");
		//需要设置默认的collection
		cloudSolrServer.setDefaultCollection("collection2");
		
		//创建一个文档对象
		
		SolrInputDocument document=new SolrInputDocument();
		//向文档中添加域
		
		document.addField("id", "test001");
		document.addField("item_title", "测试商品名称");
		document.addField("item_price", 500);
		//把文档写入索引库
		cloudSolrServer.add(document);
		//提交
		cloudSolrServer.commit();
		
	}

}
