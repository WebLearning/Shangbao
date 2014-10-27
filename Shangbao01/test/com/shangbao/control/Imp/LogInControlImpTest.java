package com.shangbao.control.Imp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.control.LogInControl;

public class LogInControlImpTest {

	@Test
	public void test() {
		ConfigurableApplicationContext context = null;  
        context = new ClassPathXmlApplicationContext("applicationContext.xml");
        
        LogInControl logIn = (LogInControl)context.getBean("logInControl");
        System.out.println(logIn.logIn("yanyan", "012345"));
	}

}
