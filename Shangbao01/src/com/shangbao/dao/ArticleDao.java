package com.shangbao.dao;

import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;

public interface ArticleDao extends MongoDao<Article> {
	void update(Article article);
	void setState(ArticleState state, Article criteriaArticle);
	Page<Article> getPage(int pageNo, int pageSize, Query query);
}
