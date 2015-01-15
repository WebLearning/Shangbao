package com.shangbao.service.imp;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.model.persistence.StartPictures;
import com.shangbao.service.StartPicturesService;

public class StartPicturesServiceImpTest {

	@Test
	public void test() {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		StartPicturesService service = (StartPicturesServiceImp) factory.getBean("startPicturesServiceImp");
		StartPictures startPictures = new StartPictures();
		startPictures.setId("Test1");
		startPictures.getPictureUrls().add("http://localhost:8080/Shangbao01/testpic");
		service.addStartPictures(startPictures);
	}

}
