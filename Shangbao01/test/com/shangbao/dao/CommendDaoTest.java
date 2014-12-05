package com.shangbao.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.New;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.SingleCommend;

public class CommendDaoTest {

	@Test
	public void test() {
		//setCommend()
		addCommend();
		//updateCommend();
		//deleteCommend();
	}
	
	public void setCommend(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		CommendDao dao = (CommendDao)applicationContext.getBean("commendDaoImp");
		Commend comm1 = new NewsCommend();
		Commend comm2 = new NewsCommend();
		comm1.setArticleId(23);
		comm1.setArticleTitle("NewsCommend");
		comm2.setArticleId(44);
		comm2.setArticleTitle("test");
		
		SingleCommend sgCom = new SingleCommend();
		sgCom.setFrom("sina");
		sgCom.setLevel("level1");
		SingleCommend sgCom2 = new SingleCommend();
		sgCom2.setFrom("baidu");
		sgCom2.setLevel("level2");
		List<SingleCommend> list = new ArrayList<SingleCommend>();
		list.add(sgCom);
		list.add(sgCom2);
		comm1.setCommendList(list);
		comm2.setCommendList(list);
		dao.insert(comm1);
		dao.insert(comm2);
	}
	
	public void addCommend(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		CommendDao dao = (CommendDao)applicationContext.getBean("commendDaoImp");
		Update update = new Update();
		Query query = new Query();
		SingleCommend singleCommend = new SingleCommend();
		singleCommend.setContent("newAdded");
		singleCommend.setFrom("shangbao");
		singleCommend.setLevel("level3");
		query.addCriteria(new Criteria().where("articleTitle").is("test"));
		update.push("commendList", singleCommend);
		//System.out.println(update.getUpdateObject());
		dao.update(query, update, new NewsCommend());
	}
	
	public void updateCommend(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		CommendDao dao = (CommendDao)applicationContext.getBean("commendDaoImp");
		
		Update update = new Update();
		Query query = new Query();
		//update.set("articleTitle", "change");
		update.set("commendList.$.level", "change");
//		DBObject updateObject = new BasicDBObject();
//		updateObject.put("$set", new BasicDBObject().put("commendList.$.level", "changed"));
//		update.fromDBObject(updateObject);
		//query.addCriteria(new Criteria().where("articleTitle").is("News2"));
		query.addCriteria(new Criteria().where("articleTitle").is("test"));
		query.addCriteria(new Criteria().where("commendList.from").is("baidu"));
		System.out.println(query.getQueryObject());
		System.out.println(update.getUpdateObject());
		dao.update(query, update, new NewsCommend());
	}
	
	public void deleteCommend(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		CommendDao dao = (CommendDao)applicationContext.getBean("commendDaoImp");
		
		Update update = new Update();
		Query query = new Query();
		
		query.addCriteria(new Criteria().where("articleTitle").is("test"));
		//query.addCriteria(new Criteria().where("commendList.from").is("baidu"));
		
		//update.set("commendList.$", null);
		DBObject object = new BasicDBObject();
		object.put("from", "baidu");
		update.pull("commendList", object);
		
		System.out.println(query.getQueryObject());
		System.out.println(update.getUpdateObject());
		dao.update(query, update, new NewsCommend());
	}
}
