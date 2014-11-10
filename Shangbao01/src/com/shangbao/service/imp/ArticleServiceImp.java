package com.shangbao.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.dao.ArticleDao;
import com.shangbao.model.Article;
import com.shangbao.service.ArticleService;

@Service
public class ArticleServiceImp implements ArticleService {

	@Resource
	private ArticleDao articleDao;
	
	public ArticleDao getArticleDao() {
		return articleDao;
	}

	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}

	@Override
	public void add(Article article) {
		articleDao.insert(article);
	}

	@Override
	public Article findOne(Long id) {
		Article article = articleDao.findById(id);
		return article;
	}

	@Override
	public void deleteOne(Article article) {
		articleDao.delete(article);
	}

	@Override
	public Map<Long, String> showTitles() {
		Map<Long, String> titleMap = new HashMap<Long, String>();
		List<Article> articles = articleDao.findAll();
		if(!articles.isEmpty()){
			for(Article article : articles){
				titleMap.put(article.getId(), article.getTitle());
			}
		}
		return titleMap;
	}

}
