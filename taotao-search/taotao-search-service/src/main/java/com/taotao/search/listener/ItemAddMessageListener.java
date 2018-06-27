package com.taotao.search.listener;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrInputDocument;
import org.aspectj.weaver.patterns.ConcreteCflowPointcut.Slot;
import org.springframework.beans.factory.annotation.Autowired;

import com.taotao.common.pojo.SearchItem;
import com.taotao.search.mapper.SearchItemMapper;

/**
 * 监听商品添加事件,同步索引库
 * @author Administrator
 *
 */
public class ItemAddMessageListener implements MessageListener{

	@Autowired
	private SearchItemMapper searchItemMapper;
	
	@Autowired
	private SolrServer solrServer;
	
	
	@Override
	public void onMessage(Message message) {
		System.out.println("====================消费者取信息================");
		try {
			//从消息中取商品id
			TextMessage textMessage=(TextMessage) message;
			String text=textMessage.getText();
			long itemId = Long.parseLong(text);
			//根据商品id查询数据,取商品信息
			Thread.sleep(1000);
			
			SearchItem searchItem = searchItemMapper.getItemById(itemId);
			
			//同步索引库
			SolrInputDocument document=new SolrInputDocument();
	
			document.addField("id", searchItem.getId());
			document.addField("item_title", searchItem.getTitle());
			document.addField("item_sell_point", searchItem.getSell_point());
			document.addField("item_price", searchItem.getPrice());
			document.addField("item_image", searchItem.getImage());
			document.addField("item_category_name", searchItem.getCategory_name());
			document.addField("item_desc", searchItem.getItem_desc());
			//把文档写入索引库
			solrServer.add(document);
			
			//提交
			solrServer.commit();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	
		
	}

}
