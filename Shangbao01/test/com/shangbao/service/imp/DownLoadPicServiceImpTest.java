package com.shangbao.service.imp;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.remotemodel.Pic;
import com.shangbao.remotemodel.PicTitle;
import com.shangbao.remotemodel.PicUrl;
import com.shangbao.service.DownLoadPicService;


public class DownLoadPicServiceImpTest {

	@Test
	public void test() {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		DownLoadPicService service = (DownLoadPicService) factory.getBean("downLoadPicServiceImp");
		List<PicTitle> titles = service.getPictureTitles();
		for(PicTitle title : titles){
			System.out.println(title.id + "  " + title.nickname + " " + title.picurl + " " + title.title + " "
					+ title.uid);
		}
//		Pic pic = service.getPictures("1609499");
//		System.out.println(pic.nickname + " " + pic.title + " ");
//		for(PicUrl eString : pic.list){
//			System.out.println("    " + eString.picurl);
//		}
	}

}
