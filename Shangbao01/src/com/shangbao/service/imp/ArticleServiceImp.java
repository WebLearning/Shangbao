package com.shangbao.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.shangbao.dao.ArticleDao;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.TitleList;
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

	@Override
	public void update(Article article) {
		
	}

	@Override
	public Page<Article> getPage(int pageNo) {
		Page<Article> page = articleDao.getPage(pageNo, 20, new Query());
		return page;
	}

	@Override
	public TitleList getTiltList(int pageNo) {
		TitleList titleList = new TitleList();
		Page<Article> page = articleDao.getPage(pageNo, 20, new Query());
		titleList.setCurrentNo(pageNo);
		titleList.setPageCount(page.getTotalPage());
		for(Article article : page.getDatas()){
			titleList.addTitle(article);
		}
		return titleList;
	}
}
