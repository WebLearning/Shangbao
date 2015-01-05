package com.shangbao.app.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.app.service.UserIdentifyService.RemoteUser;
import com.shangbao.model.persistence.User;

public class UserIdentifyServiceTest {

	@Test
	public void test() {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserIdentifyService identiy = (UserIdentifyService) factory.getBean("userIdentifyService");
//		RemoteUser user = identiy.new RemoteUser();
//		user.setBirthdaty(System.currentTimeMillis()/1000);
//		user.setEmail("yangyistd@163.com");
//		user.setNickname("一梦醉千年");
//		user.setPsw("330810852");
//		user.setSex(1);
		Map user = new HashMap<String, String>();
		user.put("phone", "123123");
		user.put("email", "sdfsdf@123.com");
		user.put("qq", "123123");
		identiy.addUser(user);
	}

}
