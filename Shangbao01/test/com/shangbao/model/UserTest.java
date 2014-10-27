package com.shangbao.model;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.dao.UserDao;
import com.shangbao.dao.Imp.UserDaoImp;

public class UserTest {

	@Test
	public void test() {
		ConfigurableApplicationContext context = null;  
        context = new ClassPathXmlApplicationContext("applicationContext.xml");  
  
        UserDao userDao = (UserDao)context.getBean("dao");  
        User user = new User();
        user.setName("tom4");
        user.setPasswd("tom21010");
        
        //User u = userDao.findById(1);
        userDao.insert(user);
	}

}
