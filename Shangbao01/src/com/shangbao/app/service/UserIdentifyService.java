package com.shangbao.app.service;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.shangbao.model.RemoteUser;
import com.shangbao.remotemodel.ResponseModel;

@Service
public class UserIdentifyService {
	@Resource
	private RestTemplate restTemplate;
	@Resource
	private UserDetailsService myUserDetailsService;
	@Resource
	private PasswordEncoder passwordEncoder;
	@Resource
	private RememberMeServices rememberMeServices;
	
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
			remoteUrl = "http://user.itanzi.com/index.php/wap/api/v1/";
		}
	}
	
	public UserDetails identifyUser(String userName, String password, int type, HttpServletRequest request){
		UserDetails userDetails = myUserDetailsService.loadUserByUsername(userName);
		String password_Encoded = passwordEncoder.encodePassword(password, null);
		if(userDetails != null && password_Encoded.equals(userDetails.getPassword())){
			UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
			//设置authentication中的details
			authentication.setDetails(new WebAuthenticationDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			HttpSession session = request.getSession(true);
			session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
			//request.getCookies().a
			return userDetails;
		}else{
			//从商报的用户数据库中查找用户数据并添加到本地
			//String identifyUrl = remoteUrl + "userMatch/" + userName + "/" + password + "/" + type;
			String identifyUrl = "http://user.itanzi.com/index.php/wap/api/v1/userMatch/一梦醉千年/330810852/2";
			ResponseModel model = restTemplate.getForObject(identifyUrl, ResponseModel.class);
			System.out.println(model.getResultCode());
		}
		return null;
	}
	
	public boolean userIsExist(){
		//restTemplate.get
		return false;
	}
	
	public void addUser(RemoteUser user){
		String responseUser = restTemplate.postForObject("http://192.168.1.119:8080/Shangbao01/app/" + "addUser", user, String.class);
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
	
	public void userExist(){
		String result = restTemplate.getForObject("http://localhost:8080/Shangbao01/backapp/all", String.class);
		System.out.println(result);
	}
	
}
