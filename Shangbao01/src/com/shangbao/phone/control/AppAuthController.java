package com.shangbao.phone.control;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.shangbao.app.service.UserIdentifyService;
import com.shangbao.model.persistence.User;

@RequestMapping("/auth")
@Controller
public class AppAuthController {

	@Resource
	private UserIdentifyService userIdentifyService;
	
	@RequestMapping(value="/login/{username}/{passwd}", method=RequestMethod.GET)
	@ResponseBody
	public UserDetails test(@PathVariable("username") String userName, @PathVariable("passwd") String passwd){
		byte bb[];
        try {
			bb = userName.getBytes("ISO-8859-1");
			userName= new String(bb, "UTF-8"); 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //以"ISO-8859-1"方式解析name字符串
   
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		UserDetails userDetails = userIdentifyService.identifyUser(userName, passwd, 2, request);
		return userDetails;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(@RequestBody User user){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		if(user.getName() != null && user.getPasswd() != null){
			UserDetails userDetails = userIdentifyService.identifyUser(user.getName(), user.getPasswd(), 2, request);
		}else if(user.getPhone() != 0 && user.getPasswd() != null){
			UserDetails userDetails = userIdentifyService.identifyUser(user.getPhone() + "", user.getPasswd(), 1, request);
		}else if(user.getEmail() != null && user.getPasswd() != null){
			UserDetails userDetails = userIdentifyService.identifyUser(user.getEmail(), user.getPasswd(), 3, request);
		}
		return "Login Failed";
	}
}
