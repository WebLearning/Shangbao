package com.shangbao.phone.control;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.shangbao.app.service.UserIdentifyService;

@RequestMapping("/auth")
@Controller
public class AppAuthController {

	@Resource
	private UserIdentifyService userIdentifyService;
	
	@RequestMapping(value="/login/{username}/{passwd}", method=RequestMethod.GET)
	@ResponseBody
	public UserDetails test(@PathVariable("username") String userName, @PathVariable("passwd") String passwd){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		UserDetails userDetails = userIdentifyService.identifyUser(userName, passwd, request);
		return userDetails;
	}
}
