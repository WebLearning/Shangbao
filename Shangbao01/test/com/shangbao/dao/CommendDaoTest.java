package com.shangbao.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;

public class CommendDaoTest {

	@Test
	public void test() {
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		CommendDao dao = (CommendDao)applicationContext.getBean("commendDaoImp");
		Commend comm1 = new NewsCommend();
		Commend comm2 = new CrawlerCommend();
		comm1.setArticleId(23);
		comm1.setArticleTitle("NewsCommend");
		comm2.setArticleId(134);
		comm2.setArticleTitle("CrawlerCommend");
//		List<Commend> list = new ArrayList<Commend>();
//		list.add(comm1);
//		list.add(comm2);
//		dao.insertAll(list);
		dao.insert(comm1);
		dao.insert(comm2);
	}

}
