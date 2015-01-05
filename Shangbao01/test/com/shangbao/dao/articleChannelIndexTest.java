package com.shangbao.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;

public class articleChannelIndexTest {

	@Test
	public void test() {
		addTest();
	}
	
	public void addTest(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		ArticleDao dao = (ArticleDao)applicationContext.getBean("articleDaoImp");
		Article criteriaArticle = new Article();
		criteriaArticle.setId(5);
		dao.setState(ArticleState.Published, criteriaArticle);
	}

}
