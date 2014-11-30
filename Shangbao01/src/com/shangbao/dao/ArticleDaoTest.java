package com.shangbao.dao;

import java.util.Date;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.model.persistence.Article;

public class ArticleDaoTest {

	public static void main(String[] args) {
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		ArticleDao dao = (ArticleDao)applicationContext.getBean("articleDaoImp");
		Article article = new Article();
		article.setTitle("TestTile7ohfjdhjdfhjopmfjdgoidfjpo");
		article.setAuthor("small4kmgjbfidgjh09dfj0h-t0h-rhkidfgjihjdihji");
		article.setTime(new Date());
		article.setSummary("this is a introduction4");
		article.setContent("This is a test context22224");
		dao.insert(article);
//		Article article = dao.findById(2);
//		System.out.println(article.getTitle());
	}

}
