package com.taotao.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.taotao.common.pojo.SearchItem;
import com.taotao.common.pojo.SearchResult;

/**
 * 查询索引库商品dao
 * @author Administrator
 *
 */
@Repository
public class SearchDao {

	@Autowired
	private SolrServer solrServer;
	
	
	public SearchResult search(SolrQuery query) throws Exception{
		QueryResponse response = solrServer.query(query);
		
		SolrDocumentList solrDocumentList = response.getResults();
		
		//去查询结果总记录数
		long numFound=solrDocumentList.getNumFound();
		
		SearchResult result=new SearchResult();
		result.setRecordCount(numFound);
		
		List<SearchItem> itemList=new ArrayList<>(); 
		
		
		for (SolrDocument solrDocument : solrDocumentList) {
			SearchItem item=new SearchItem();
			item.setCategory_name(solrDocument.get("item_category_name").toString());
			item.setId(solrDocument.get("id").toString());
			item.setImage(solrDocument.get("item_image").toString());
			item.setPrice((long) solrDocument.get("item_price"));
			item.setSell_point(solrDocument.get("item_sell_point").toString());
			//取高亮显示
			Map<String, Map<String, List<String>>> highlighting = response.getHighlighting();
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title="";
			if (list!=null&&list.size()>0) {
				title=list.get(0);
				
			}else{
				title=solrDocument.get("item_title").toString();
			}
			item.setTitle(title);
			itemList.add(item);
		}
		
		result.setItemList(itemList);
		
		return result;
	}
}
