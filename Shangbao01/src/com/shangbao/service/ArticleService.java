package com.shangbao.service;

import java.util.Map;

import com.shangbao.model.Article;

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
	 * 删除一篇文章
	 * @param id
	 */
	public void deleteOne(Article article);
}
