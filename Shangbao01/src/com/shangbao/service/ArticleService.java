package com.shangbao.service;

import java.util.List;
import java.util.Map;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.TitleList;

public interface ArticleService {
	/**
	 * 添加一篇文章
	 * @param article
	 */
	public void add(Article article);
	
	/**
	 * 列出文章标题
	 * @return
	 */
	public Map<Long, String> showTitles();
	
	/**
	 * 查找一篇文章
	 * @param id
	 */
	public Article findOne(Long id);
	
	/**
	 * 更新一篇文章
	 * @param article
	 */
	public void update(Article article);
	
	/**
	 * 删除一篇文章
	 * @param id
	 */
	public void deleteOne(Article article);
	
	/**
	 * 获得标题列表
	 * @return
	 */
	public TitleList getTiltList(ArticleState articleState, int pageNo);
	
	/**
	 * 获得排序后的标题列表
	 * @param articleState
	 * @param pageNo
	 * @param order
	 * @return
	 */
	public TitleList getOrderedList(ArticleState articleState, int pageNo, String order);
	
	/**
	 * 设置文章的状态
	 * @param articleState
	 * @param id
	 */
	public void setPutState(ArticleState articleState, List<Long> idList);
	
	public void setDeleteState(ArticleState articleState, List<Long> idList);
}
