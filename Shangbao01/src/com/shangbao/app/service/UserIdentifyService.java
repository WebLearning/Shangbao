package com.shangbao.app.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.shangbao.model.RemoteUser;

@Service
public class UserIdentifyService {
	@Resource
	private RestTemplate restTemplate;
	private final String remoteUrl;
	
	public UserIdentifyService(){
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(!props.getProperty("remoteUrl").isEmpty()){
			remoteUrl = props.getProperty("remoteUrl");
		}else{
			remoteUrl = "http://user.itanzi.com/wap/api/v1/";
		}
	}
	
	public boolean userIsExist(){
		//restTemplate.get
		return false;
	}
	
	public void addUser(RemoteUser user){
		String responseUser = restTemplate.postForObject(remoteUrl + "addUser", user, String.class);
		System.out.println(responseUser);
	}
	
	public void addUser(MultiValueMap user){
		String responseUser = restTemplate.postForObject(remoteUrl + "addUser", user, String.class);
		System.out.println(responseUser);
	}
	
	public void addUser(String user){
		System.out.println(user);
		String responseUser = restTemplate.postForObject(remoteUrl + "addUser", user, String.class);
		System.out.println(responseUser);
	}
	
}
