package com.shangbao.app.service;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserIdentifyService {
//	@Resource
//	private RestTemplate restTemplate;
//	private final String remoteUrl;
//	
//	public UserIdentifyService(){
//		Properties props = new Properties();
//		try {
//			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		if(!props.getProperty("remoteUrl").isEmpty()){
//			remoteUrl = props.getProperty("remoteUrl");
//		}else{
//			remoteUrl = "http://user.itanzi.com/wap/api/v1/";
//		}
//	}
//	
//	public boolean userIsExist(){
//		//restTemplate.get
//		return false;
//	}
//	
//	public void addUser(RemoteUser user){
//		RemoteUser responseUser = restTemplate.postForObject(remoteUrl + "addUser", user, RemoteUser.class);
//		System.out.println(responseUser);
//	}
	
	public class RemoteUser{
		private int telNum;
		private String head;
		private String email;
		private int sex;
		private long birthdaty;
		private int qq;
		private String nickname;
		private String psw;
		public int getTelNum() {
			return telNum;
		}
		public void setTelNum(int telNum) {
			this.telNum = telNum;
		}
		public String getHead() {
			return head;
		}
		public void setHead(String head) {
			this.head = head;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public int getSex() {
			return sex;
		}
		public void setSex(int sex) {
			this.sex = sex;
		}
		public long getBirthdaty() {
			return birthdaty;
		}
		public void setBirthdaty(long birthdaty) {
			this.birthdaty = birthdaty;
		}
		public int getQq() {
			return qq;
		}
		public void setQq(int qq) {
			this.qq = qq;
		}
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
		public String getPsw() {
			return psw;
		}
		public void setPsw(String psw) {
			this.psw = psw;
		}
	}
	
}
