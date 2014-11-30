package com.shangbao.service;

import java.util.Map;

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
	 * 获得分页
	 * @return
	 */
	public Page<Article> getPage(int pageNo);
	
	/**
	 * 获得标题列表
	 * @return
	 */
	public TitleList getTiltList(int pageNo);
}
