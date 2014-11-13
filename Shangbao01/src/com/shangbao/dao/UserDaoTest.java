package com.shangbao.dao;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.encoding.PasswordEncoder;

import com.shangbao.model.User;

public class UserDaoTest {

	public static void main(String[] args) {
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserDao dao = (UserDao)applicationContext.getBean("userDaoImp");
		PasswordEncoder encoder = (PasswordEncoder)applicationContext.getBean("passwordEncoder");
		User u = new User();
		u.setName("yy");
		u.setPasswd(encoder.encodePassword("123", null));
		u.setRole("ROLE_USER");
		dao.insert(u);
	}

}
