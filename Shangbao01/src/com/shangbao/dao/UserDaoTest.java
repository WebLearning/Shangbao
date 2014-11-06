package com.shangbao.dao;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.model.User;

public class UserDaoTest {

	public static void main(String[] args) {
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserDao dao = (UserDao)applicationContext.getBean("userDaoImp");
		User u = new User();
		u.setName("t1");
		u.setPasswd("123");
		u.setRole("ROLE_USER");
		dao.insert(u);
	}

}
