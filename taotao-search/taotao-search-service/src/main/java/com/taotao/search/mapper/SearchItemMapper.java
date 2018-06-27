package com.taotao.search.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.taotao.common.pojo.SearchItem;

public interface SearchItemMapper {

	
	List<SearchItem>  getItemList();
	
	SearchItem getItemById(@Param("id")long id);
	
}
