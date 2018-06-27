package com.taotao.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.taotao.common.pojo.EasyUITreeNode;
import com.taotao.common.pojo.TaotaoResult;
import com.taotao.content.service.ContentCategoryService;
import com.taotao.mapper.TbContentCategoryMapper;
import com.taotao.pojo.TbContentCategory;
import com.taotao.pojo.TbContentCategoryExample;
import com.taotao.pojo.TbContentCategoryExample.Criteria;

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;

	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {

		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);

		List<EasyUITreeNode> resultList = new ArrayList<>();

		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent() ? "closed" : "open");

			resultList.add(node);
		}

		return resultList;
	}

	@Override
	public TaotaoResult addContentCategory(Long parentId, String name) {
		TbContentCategory contentCategory = new TbContentCategory();
		contentCategory.setParentId(parentId);
		contentCategory.setName(name);
		// 状态,可选值:1(正常),2(删除)
		contentCategory.setStatus(1);
		contentCategory.setSortOrder(1);
		contentCategory.setIsParent(false);
		contentCategory.setCreated(new Date());
		contentCategory.setUpdated(new Date());
		contentCategoryMapper.insert(contentCategory);
		// 判断父节点的状态
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(parentId);
		if (!parent.getIsParent()) {
			// 如果父节点为叶子节点应该改为父节点
			parent.setIsParent(true);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}

		return TaotaoResult.ok(contentCategory);
	}

	@Override
	public TaotaoResult updateContentCategory(Long id, String name) {
		TbContentCategory category = new TbContentCategory();
		category.setId(id);
		category.setName(name);
		category.setUpdated(new Date());
		contentCategoryMapper.updateByPrimaryKeySelective(category);
		return TaotaoResult.ok();
	}

	@Override
	public TaotaoResult deleteContentCategory(Long id) {
		TbContentCategory contentCategory=contentCategoryMapper.selectByPrimaryKey(id);
		contentCategoryMapper.deleteByPrimaryKey(id);
		if(contentCategory.getIsParent()){
			deleteContentCategorySon(contentCategory.getId());
		}else{
			TbContentCategoryExample example=new TbContentCategoryExample();
			
			Criteria criteria=example.createCriteria();
			criteria.andParentIdEqualTo(contentCategory.getParentId());
			List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
			if(list.size()==0){
				TbContentCategory contentCategory2=contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
				contentCategory2.setIsParent(false);
				contentCategoryMapper.updateByPrimaryKey(contentCategory2);
			}
			
		}
		
		return TaotaoResult.ok();
	}
	
	
	/**
	 * 使用递归删除父节点下的所有子节点
	 * @param parentId
	 */
	public void deleteContentCategorySon(Long parentId){
		
		TbContentCategoryExample example=new TbContentCategoryExample();
		
		Criteria criteria=example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> contentList = contentCategoryMapper.selectByExample(example);
		
		
		for(TbContentCategory category:contentList){
			contentCategoryMapper.deleteByPrimaryKey(category.getId());
			deleteContentCategorySon(category.getId());
		}
		
		
		
	}

}
