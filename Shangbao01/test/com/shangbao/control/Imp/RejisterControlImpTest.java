package com.shangbao.control.Imp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.control.RejisterControl;
import com.shangbao.dao.UserDao;

public class RejisterControlImpTest {

	@Test
	public void test() {
		ConfigurableApplicationContext context = null;  
        context = new ClassPathXmlApplicationContext("applicationContext.xml");  
  
        RejisterControl rejist = (RejisterControl)context.getBean("rejisterControl");
        
        System.out.println(rejist.rejister("yanyan", "012345"));
	}

}
