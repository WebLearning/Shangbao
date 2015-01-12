package com.shangbao.app.service;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.shangbao.model.persistence.User;

public class UserIdentifyServiceTest {

	@Test
	public void test() {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		UserIdentifyService identiy = (UserIdentifyService) factory.getBean("userIdentifyService");
//		com.shangbao.model.RemoteUser user = new com.shangbao.model.RemoteUser();
//		user.setBirthday(System.currentTimeMillis()/1000);
//		user.setEmail("yangyistd@163.com");
//		user.setNickname("tiankongyy");
//		user.setPsw("330810852");
//		user.setQq(123123123);
//		user.setSex(1);
//		user.setPhone(123123);
//		user.setAvatar("http://java.dzone.com/users/johnathansmith1969");
//		System.out.println(user.getBirthday());
//		MultiValueMap<String, Object> user = new LinkedMultiValueMap<>();
//		user.add("phone", "123123");
//		user.add("email", "sdfsdf@123.com");
//		user.add("qq", "123123");
//		user.add("nickname", "测试昵称");
//		user.add("psw", "12344321");
//		identiy.addUser(user);
//		identiy.addUser(user);
		//System.out.println(identiy.userExist("测试昵称", 2));
	}

}
