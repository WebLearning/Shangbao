package com.shangbao.dao;

import java.util.List;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;

public interface ArticleDao extends MongoDao<Article> {
	void update(Article article);
	void setState(ArticleState state, Article criteriaArticle);
	Page<Article> getPage(int pageNo, int pageSize, Query query);
	List<Article> find(Article criteriaArticle, Direction direction, String property);
	List<Article> fuzzyFind(String words, ArticleState state, boolean tag);
	Page<Article> fuzzyFind(String words, ArticleState state, boolean tag, int pageNo, int pageSize);
	void setTopArticle(String channelName, Long articleId);
	void swapArticle(String channelName, Long articleAId, Long articleBId);
	void update(Article criteriaArticle, Update update);
	Long insertAndGetId(Article article);
}
