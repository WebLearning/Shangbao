package com.shangbao.service.imp;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.dao.ArticleDao;
import com.shangbao.model.persistence.Article;
import com.shangbao.service.PictureService;

@Service
public class PictureServiceImp implements PictureService{
	@Resource
	private ArticleDao articleDaoImp;

	public ArticleDao getArticleDaoImp() {
		return articleDaoImp;
	}

	public void setArticleDaoImp(ArticleDao articleDaoImp) {
		this.articleDaoImp = articleDaoImp;
	}

	@Override
	public void add(Article article) {
		this.articleDaoImp.insert(article);
	}
	
	
}
