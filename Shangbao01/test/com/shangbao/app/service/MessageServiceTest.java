package com.shangbao.app.service;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MessageServiceTest {

	@Test
	public void test() throws Exception {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		MessageService service = (MessageService) factory.getBean("messageService");
//		String result = service.mt("15196612209", new String(("请不要把验证码泄露给其他人，如非本人操作，可不用理会！ 【成都商报】").getBytes(),"gb2312"), "", "", "009");
		String result = service.mt("15196612209", "123321请不要把验证码泄露给其他人，如非本人操作，可不用理会！ 【成都商报】", "", "", "009");
		System.out.println(result);
//		System.out.println(service.mo());
	}
}
