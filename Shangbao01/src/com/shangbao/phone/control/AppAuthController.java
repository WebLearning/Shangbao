package com.shangbao.phone.control;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.shangbao.app.model.AppResponseModel;
import com.shangbao.app.service.UserIdentifyService;
import com.shangbao.model.persistence.User;
import com.shangbao.remotemodel.ResponseModel;

@RequestMapping("/auth")
@Controller
public class AppAuthController {

	@Resource
	private UserIdentifyService userIdentifyService;
	
	@RequestMapping(value="/login/{username}/{passwd}", method=RequestMethod.GET)
	@ResponseBody
	public UserDetails test(@PathVariable("username") String userName, @PathVariable("passwd") String passwd, HttpServletResponse response){
		byte bb[];
        try {
			bb = userName.getBytes("ISO-8859-1");
			userName= new String(bb, "UTF-8"); 
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} //以"ISO-8859-1"方式解析name字符串
   
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		UserDetails userDetails = userIdentifyService.identifyUser(userName, passwd, 2, request, response);
		return userDetails;
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public AppResponseModel login(@RequestBody User user, HttpServletResponse response){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		UserDetails userDetails = null;
		AppResponseModel appResponseModel = new AppResponseModel();
		if(user.getName() != null && user.getPasswd() != null){
			userDetails = userIdentifyService.identifyUser(user.getName(), user.getPasswd(), 2, request, response);
		}else if(user.getPhone() != 0 && user.getPasswd() != null){
			userDetails = userIdentifyService.identifyUser(user.getPhone() + "", user.getPasswd(), 1, request, response);
		}else if(user.getEmail() != null && user.getPasswd() != null){
			userDetails = userIdentifyService.identifyUser(user.getEmail(), user.getPasswd(), 3, request, response);
		}
		if(userDetails != null){
			appResponseModel.setResultCode(1);
			appResponseModel.setResultMsg("Login Success");
		}else{
			appResponseModel.setResultCode(0);
			appResponseModel.setResultMsg("Login Failed");
		}
		return appResponseModel;
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public AppResponseModel register(@RequestBody User user){
		AppResponseModel appResponseModel = new AppResponseModel();
		if(userIdentifyService.addUser(user)){
			appResponseModel.setResultCode(1);
			appResponseModel.setResultMsg("Register Success");
		}else{
			appResponseModel.setResultCode(0);
			appResponseModel.setResultMsg("Register Failed");
		}
		return appResponseModel;
	}
	
	@RequestMapping(value="/phoneidentify/{phone:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public AppResponseModel AppPhoneNumIdentify(@PathVariable("phone") String phoneNum){
		AppResponseModel appResponseModel = new AppResponseModel();
		String code = userIdentifyService.identifyUserPhone(phoneNum);
		if(code != null){
			appResponseModel.setResultCode(1);
			appResponseModel.setResultMsg(code);
		}
		return appResponseModel;
	}
	
}
