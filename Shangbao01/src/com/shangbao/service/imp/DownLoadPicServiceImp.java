package com.shangbao.service.imp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shangbao.dao.ArticleDao;
import com.shangbao.remotemodel.Pic;
import com.shangbao.remotemodel.PicTitle;
import com.shangbao.service.DownLoadPicService;

@Service
public class DownLoadPicServiceImp implements DownLoadPicService {

	@Resource
	private ArticleDao articleDaoImp;
	
	@Resource
	private RestTemplate restTemplate;
	
	private final String remoteUrl;
	
	public DownLoadPicServiceImp(){
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!props.getProperty("remotePicUrl").isEmpty()){
			remoteUrl = props.getProperty("remotePicUrl");
		}else{
			remoteUrl = "http://photo.chengdu.cn/api.php?m=diary";
		}
	}
	
	@Override
	public List<PicTitle> getPictureTitles(){
		String url = remoteUrl + "&a=query";
		String picTitle = restTemplate.getForObject(url, String.class);
		System.out.println(picTitle);
		ObjectMapper mapper = new ObjectMapper();
		JavaType type = mapper.getTypeFactory().constructParametricType(ArrayList.class, PicTitle.class);
		List<PicTitle> picTitles = new ArrayList<>();
		try {
			mapper.enableDefaultTyping();
			picTitles = (List<PicTitle>)mapper.readValue(picTitle, type);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return picTitles;
	}
	
	@Override
	public List<PicTitle> getPictureTitles(Date startDate, Date endDate) {
		String url = remoteUrl;
		List<PicTitle> picTitles = restTemplate.getForObject(url, List.class);
		return picTitles;
	}

	@Override
	public Pic getPictures(String id) {
		String url = remoteUrl + "&a=detail&id=" + id;
		String picString = restTemplate.getForObject(url, String.class);
		System.out.println(picString);
		Pic pic = null;
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.enableDefaultTyping();
			pic = mapper.readValue(picString, Pic.class);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pic;
	}

	@Override
	public void saveAsArticle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void saveAsArticle(Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		
	}
	
}
