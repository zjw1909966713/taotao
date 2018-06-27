package com.taotao.content.service;


import java.util.List;

import com.taotao.common.pojo.EasyUIDataGridResult;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbContent;

public interface ContentService {
	
	TaotaoResult addContent(TbContent content);

	EasyUIDataGridResult getContentList(Long categoryId, Integer page, Integer rows);

	
	
	List<TbContent> getContentByCid(Long cid);
}
