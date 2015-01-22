package com.shangbao.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.shangbao.model.persistence.User;

public class UserDaoImpTest {

	@Test
	public void test() {
		addUser();
	}
	
	public void addUser(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserDao dao = (UserDao) applicationContext.getBean("userDaoImp");
		PasswordEncoder encoder = (PasswordEncoder) applicationContext.getBean("passwordEncoder");
		User user = new User();
		user.setName("test");
		user.setPasswd(encoder.encodePassword("123", null));
		//user.setPasswd("123");
		user.setRole("ROLE_USER");
		dao.insert(user);
	}

}
