package com.shangbao.dao;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
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
//		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
//		ArticleDao dao = (ArticleDao)applicationContext.getBean("articleDaoImp");
//		Article article = new Article();
//		Map channelMap = new HashMap<String, Integer>();
//		channelMap.put("channelA", 21);
//		channelMap.put("channel122", 12);
//		article.setAuthor("yangyi");
//		article.setContent("MapTest");
//		article.setTitle("Test title");
//		//article.setChannelMap(channelMap);
//		article.setFrom("Sina");
//		article.setTime(new Date());
//		article.setState(ArticleState.Crawler);
//		dao.insert(article);
//		Criteria criteria = new Criteria().where("Title").is("Test title");
//		Query query = new Query().addCriteria(criteria);
//		Page<Article> page = dao.getPage(1, 5, query);
//		System.out.println(page.getPageSize());
//		for(Article article : page.getDatas()){
//			article.setContent("I have a drean");
//			dao.update(article, article);
//		}
		addArticle();
	}
	
	public void addArticle(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		ArticleDao dao = (ArticleDao)applicationContext.getBean("articleDaoImp");
		for(int i = 1; i < 10; i ++){
			Article article = new Article();
			article.setTitle("Title" + i);
			article.setAuthor("yy");
			article.setState(ArticleState.Crawler);
			article.setFrom("sina");
			article.setContent("This is content" + i);
			article.setSummary("This is summary" + i);
			article.setSubTitle("SubTitle" + i);
			dao.insert(article);
		}
	}

}
