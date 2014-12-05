package com.shangbao.dao;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;

public interface ArticleDao extends MongoDao<Article> {
	void update(Article article);
	void setState(ArticleState state, Article criteriaArticle);
}
