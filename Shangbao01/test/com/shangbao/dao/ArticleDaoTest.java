package com.shangbao.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.app.service.AppService;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;

public class ArticleDaoTest {

	@Test
	public void test() {
		addArticle();
	}
	
	public void addArticle(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		ArticleDao dao = (ArticleDao)applicationContext.getBean("articleDaoImp");
		List<Article> articles = new ArrayList<Article>();
		for(int i = 1; i < 150; i ++){
			Article article = new Article();
			switch (i % 5) {
			case 0:
				article.setState(ArticleState.Crawler);
				break;
			case 1:
				article.setState(ArticleState.Pending);
				break;
			case 2:
				article.setState(ArticleState.Deleted);
				break;
			case 3:
				article.setState(ArticleState.Revocation);
				break;
			case 4:
				article.setState(ArticleState.Temp);
				break;
			default:
				break;
			}
			article.setAuthor("yangyi");
			article.setClicks(i * 3);
			article.setContent("文章的内容" + i);
			article.setCrawlerCommends(i * 2 + 1);
			article.setCrawlerCommendsPublish(i + 1);
			article.setLevel("level" + (i % 3 + 1));
			article.setWords(i * 5 + 1);
			article.setLikes(i + 10);
			article.setNewsCommends(i + 14);
			article.setNewsCommendsPublish(i + 12);
			article.setSubTitle("副标题" + i);
			article.setTitle("标题" + i);
			articles.add(article);
		}
		if(articles == null || articles.isEmpty())
		{
			System.out.println("empty");
			return;
		}
		dao.insertAll(articles);
	}

}
