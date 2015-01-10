package com.shangbao.app.service;

import java.io.IOException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
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
	
	public void addUser(MultiValueMap user){
		String responseUser = restTemplate.postForObject(remoteUrl + "addUser", user, String.class);
		System.out.println(responseUser);
	}
	
	public boolean userExist(String account, String accountType){
		String result = restTemplate.getForObject(remoteUrl + "isExist/" + account + "/" + accountType, String.class);
		System.out.println(result);
		ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.enableDefaultTyping();
			ResponseModel model = mapper.readValue(result, ResponseModel.class);
			System.out.println(model.getData().getAvatar());
			if(model.getResultMsg().equals("userExist fail")){
				return false;
			}else{
				return true;
			}
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
		return false;
	}
	
}
