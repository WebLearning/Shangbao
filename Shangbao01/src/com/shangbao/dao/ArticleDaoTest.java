package com.shangbao.dao;

import java.util.Date;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.model.Article;

public class ArticleDaoTest {

	public static void main(String[] args) {
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		ArticleDao dao = (ArticleDao)applicationContext.getBean("articleDaoImp");
		Article article = new Article();
		article.setTitle("TestTile2");
		article.setAuthor("yy");
		article.setTime(new Date());
		article.setContent("This is a test context2222");
		dao.insert(article);
	}

}
