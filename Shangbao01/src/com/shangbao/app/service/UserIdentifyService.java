package com.shangbao.app.service;

import java.io.IOException;
import java.util.Date;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonAutoDetect.Visibility;
import org.codehaus.jackson.annotate.JsonMethod;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.codehaus.jackson.map.DeserializationConfig; 

import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.User;
import com.shangbao.remotemodel.ResponseModel;
import com.shangbao.remotemodel.UserInfo;
import com.shangbao.service.UserService;
import com.shangbao.web.service.MyUserDetailService;

@Service
public class UserIdentifyService {
	@Resource
	private RestTemplate restTemplate;
	@Resource
	private MyUserDetailService myUserDetailsService;
	@Resource
	private PasswordEncoder passwordEncoder;
	@Resource
	private RememberMeServices rememberMeServices;
	@Resource
	private UserService userServiceImp;
	
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
	
	/**
	 * 
	 * @param userName 
	 * @param password
	 * @param type 1 表示电话号码  2 表示userName  3 表示email
	 * @param request
	 * @return
	 */
	public UserDetails identifyUser(String userName, String password, int type, HttpServletRequest request){
		UserDetails userDetails;
		if(1 == type){
			userDetails = myUserDetailsService.loadUserByPhone(userName);
		}else if(3 == type){
			userDetails = myUserDetailsService.loadUserByEmail(userName);
		}else {
			userDetails = myUserDetailsService.loadUserByUsername(userName);
		}
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
			if(userExist(userName, type)){
				//表示在商报数据库中有该用户
				ResponseModel model = identifyRemoteUser(userName, password, type);
				if(model != null){
					//将用户添加到本地的数据库中
					UserInfo userInfo = model.getData();
					User user  = new User();
					user.setUid(Long.parseLong(userInfo.getUid()));
					user.setAvatar(userInfo.getAvatar());
					user.setBirthday(new Date(Long.parseLong(userInfo.getBirthday()) * 1000));
					user.setEmail(userInfo.getEmail());
					user.setPasswd(passwordEncoder.encodePassword(password, null));
					user.setName(userInfo.getNickname());
					user.setPhone(Integer.parseInt(userInfo.getPhone()));
					user.setQq(Integer.parseInt(userInfo.getQq()));
					user.setSex(Integer.parseInt(userInfo.getSex()));
					user.setRole("ROLE_USER");
					userServiceImp.addUser(user);
					//进行授权
					UsernamePasswordAuthenticationToken authentication =
							new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
					//设置authentication中的details
					authentication.setDetails(new WebAuthenticationDetails(request));
					SecurityContextHolder.getContext().setAuthentication(authentication);
					HttpSession session = request.getSession(true);
					session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());
					return user;
				}
			}
		}
		return null;
	}
	
	public void addUser(User user){
		MultiValueMap<String, Object> userMap = new LinkedMultiValueMap<>();
		if(user.getName() == null || user.getPasswd() == null){
			return;
		}
		userMap.add("nickname", user.getName());
		userMap.add("psw", user.getPasswd());
		if(user.getAvatar() != null)
			userMap.add("avatar", user.getAvatar());
		if(user.getPhone() != 0)
			userMap.add("phone", user.getPhone() + "");
		if(user.getEmail() != null)
			userMap.add("email", user.getEmail());
		if(user.getSex() == 0 || user.getSex() == 1)
			userMap.add("sex", user.getSex() + "");
		if(user.getBirthday() != null)
			userMap.add("birthday", user.getBirthday().getTime()/1000 + "");
		if(user.getQq() != 0)
			userMap.add("qq", user.getQq() + "");
		String responseUser = restTemplate.postForObject(remoteUrl + "addUser", userMap, String.class);
		System.out.println(responseUser);
		ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.enableDefaultTyping();
		try {
			String model = mapper.readValue(responseUser, String.class);
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
	}
	
	public boolean userExist(String account, int accountType){
		String result = restTemplate.getForObject(remoteUrl + "isExists/" + account + "/" + accountType, String.class);
		System.out.println(result);
		ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
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
	
	public ResponseModel identifyRemoteUser(String account, String passwd, int accountType){
		String result = restTemplate.getForObject(remoteUrl + "userMatch/" + account + "/" + passwd + "/" + accountType, String.class);
		System.out.println(result);
		ObjectMapper mapper = new ObjectMapper().setVisibility(JsonMethod.FIELD, Visibility.ANY);
		mapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		try {
			mapper.enableDefaultTyping();
			ResponseModel model = mapper.readValue(result, ResponseModel.class);
			return model;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
