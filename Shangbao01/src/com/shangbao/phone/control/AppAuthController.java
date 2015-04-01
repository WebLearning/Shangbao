package com.shangbao.phone.control;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.shangbao.model.PasswdModel;
import com.shangbao.model.persistence.User;
import com.shangbao.service.UserService;


@RequestMapping("/auth")
@Controller
public class AppAuthController {
	
	@Resource
	private UserIdentifyService userIdentifyService;
	@Resource
	private UserService userServiceImp;
	
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
		}else if(user.getPhone() != null && user.getPasswd() != null){
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
	
	/**
	 * 注册，客户端验证手机验证码
	 * @param user
	 * @return
	 */
	@RequestMapping(value="/register", method=RequestMethod.POST)
	@ResponseBody
	public AppResponseModel register(@RequestBody User user){
		AppResponseModel appResponseModel = new AppResponseModel();
		if(user.getPhone() == null){
			appResponseModel.setResultCode(0);
			appResponseModel.setResultMsg("Null Phone Num");
		}else{
			User criteriaUser = new User();
			criteriaUser.setPhone(user.getPhone());
			if(userServiceImp.findOne(criteriaUser) == null){
				User criteriaUser2 = new User();
				criteriaUser2.setName(user.getName());
				if(userServiceImp.findOne(criteriaUser2) == null){
					if(userIdentifyService.addUser(user)){
						appResponseModel.setResultCode(1);
						appResponseModel.setResultMsg("Register Success");
					}else{
						appResponseModel.setResultCode(0);
						appResponseModel.setResultMsg("Register Failed");
					}
				}else{
					appResponseModel.setResultCode(0);
					appResponseModel.setResultMsg("Name Rejisted");
				}
			}else{
				appResponseModel.setResultCode(0);
				appResponseModel.setResultMsg("Phone Num Rejisted");
			}
		}
		return appResponseModel;
	}
	
	
	/**
	 * 注册，服务器验证手机验证码
	 * @param user
	 * @param request
	 * @param phoneText
	 * @return
	 */
	@RequestMapping(value="/register/{phonetext}", method=RequestMethod.POST)
	@ResponseBody
	public AppResponseModel registerSession(@RequestBody User user, HttpServletRequest request, @PathVariable("phonetext") String phoneText){
		AppResponseModel appResponseModel = new AppResponseModel();
		String text = (String)request.getSession().getAttribute("PHONE_TEXT");
		if(!text.equals(phoneText)){
			appResponseModel.setResultCode(0);
			appResponseModel.setResultMsg("Wrong code");
		}else{
			if(user.getPhone() == null){
				appResponseModel.setResultCode(0);
				appResponseModel.setResultMsg("Null Phone Num");
			}else{
				User criteriaUser = new User();
				criteriaUser.setPhone(user.getPhone());
				if(userServiceImp.findOne(criteriaUser) == null){
					User criteriaUser2 = new User();
					criteriaUser2.setName(user.getName());
					if(userServiceImp.findOne(criteriaUser2) == null){
						if(userIdentifyService.addUser(user)){
							appResponseModel.setResultCode(1);
							appResponseModel.setResultMsg("Register Success");
						}else{
							appResponseModel.setResultCode(0);
							appResponseModel.setResultMsg("Register Failed");
						}
					}else{
						appResponseModel.setResultCode(0);
						appResponseModel.setResultMsg("Name Rejisted");
					}
				}else{
					appResponseModel.setResultCode(0);
					appResponseModel.setResultMsg("Phone Num Rejisted");
				}
			}
		}
		return appResponseModel;
	}
	
	/**
	 * 测试用
	 * @return
	 */
	@RequestMapping(value="/register", method=RequestMethod.GET)
	@ResponseBody
	public AppResponseModel registerGet(){
		User user = new User();
		user.setName("phoneTest");
		user.setPhone("15196612209");
		user.setPasswd("123");
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
	
	
	/**
	 * 注册时发送手机验证码
	 * @param phoneNum
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/phoneidentify/{phone:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public AppResponseModel AppPhoneNumIdentify(@PathVariable("phone") String phoneNum, HttpServletRequest request){
		AppResponseModel appResponseModel = new AppResponseModel();
		//判断该手机是否已经注册
		User criteriaUser = new User();
		criteriaUser.setPhone(phoneNum);
		User user = userServiceImp.findOne(criteriaUser);
		if(user != null){
			appResponseModel.setResultCode(0);
			appResponseModel.setResultMsg("Phone has been rejisted");
			return appResponseModel;
		}
		String code = userIdentifyService.identifyUserPhone(phoneNum);
		if(code != null){
			request.getSession().setAttribute("PHONE_TEXT", code);
			appResponseModel.setResultCode(1);
			appResponseModel.setResultMsg(code);
		}
		return appResponseModel;
	}
	
	/**
	 * 找回密码时发送手机验证码
	 * @param phoneNum
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/resetpasswd/phoneidentify/{phone:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public AppResponseModel resetPhoneIdentify(@PathVariable("phone") String phoneNum, HttpServletRequest request){
		AppResponseModel appResponseModel = new AppResponseModel();
		String code = userIdentifyService.identifyUserPhone(phoneNum);
		if(code != null){
			request.getSession().setAttribute("PHONE_TEXT", code);
			request.getSession().setAttribute("PHONE_NUM", phoneNum);
			appResponseModel.setResultCode(1);
			appResponseModel.setResultMsg("message sended");
		}else{
			appResponseModel.setResultCode(0);
			appResponseModel.setResultMsg("error");
		}
		return appResponseModel;
	}
	
	/**
	 * 重置密码
	 * @param request
	 * @param passwdModel
	 * @return
	 */
	@RequestMapping(value="/resetpasswd", method=RequestMethod.POST)
	@ResponseBody
	public AppResponseModel setNewPasswd(HttpServletRequest request, @RequestBody PasswdModel passwdModel){
		AppResponseModel appResponseModel = new AppResponseModel();
		String phoneText = (String)request.getSession().getAttribute("PHONE_TEXT");
		String phoneNum = (String)request.getSession().getAttribute("PHONE_NUM");
		if(phoneNum != null && passwdModel.getOldPasswd() != null && passwdModel.getNewPasswd() != null){
			if(phoneText.equals(passwdModel.getOldPasswd())){
				//设置新的密码
				User criteriaUser = new User();
				criteriaUser.setPhone(phoneNum);
				User user = userServiceImp.findOne(criteriaUser);
				if(user == null){
					appResponseModel.setResultCode(0);
					appResponseModel.setResultMsg("User Not Found");
					request.getSession().removeAttribute("PHONE_TEXT");
					request.getSession().removeAttribute("PHONE_NUM");
					return appResponseModel;
				}else{
					user.setPasswd(passwdModel.getNewPasswd());
					if(!userIdentifyService.updateUser(user)){
						userServiceImp.updatePasswd(user, user.getPasswd(), passwdModel.getNewPasswd());
						appResponseModel.setResultCode(1);
						appResponseModel.setResultMsg("SUCCESS");
					}else{
						appResponseModel.setResultCode(0);
						appResponseModel.setResultMsg("ERROR");
					}
				}
			}else{
				appResponseModel.setResultCode(0);
				appResponseModel.setResultMsg("wrong identify code");
				request.getSession().removeAttribute("PHONE_TEXT");
				request.getSession().removeAttribute("PHONE_NUM");
			}
		}else{
			appResponseModel.setResultCode(0);
			appResponseModel.setResultMsg("param error");
			request.getSession().removeAttribute("PHONE_TEXT");
			request.getSession().removeAttribute("PHONE_NUM");
		}
		request.getSession().removeAttribute("PHONE_TEXT");
		request.getSession().removeAttribute("PHONE_NUM");
		return appResponseModel;
	}
	
	/**
	 * 测试用
	 * @param request
	 * @param phoneCode
	 * @param newPasswd
	 * @return
	 */
	@RequestMapping(value="/resetpasswd/{phoneText}/{newPasswd}", method=RequestMethod.GET)
	@ResponseBody
	public AppResponseModel setNewPasswdGET(HttpServletRequest request,@PathVariable("phoneText") String phoneCode, @PathVariable("newPasswd") String newPasswd){
		AppResponseModel appResponseModel = new AppResponseModel();
		String phoneText = (String)request.getSession().getAttribute("PHONE_TEXT");
		String phoneNum = (String)request.getSession().getAttribute("PHONE_NUM");
		if(phoneNum != null && phoneCode != null && newPasswd != null){
			if(phoneText.equals(phoneCode)){
				//设置新的密码
				User criteriaUser = new User();
				criteriaUser.setPhone(phoneNum);
				User user = userServiceImp.findOne(criteriaUser);
				if(user == null){
					appResponseModel.setResultCode(0);
					appResponseModel.setResultMsg("User Not Found");
					request.getSession().removeAttribute("PHONE_TEXT");
					request.getSession().removeAttribute("PHONE_NUM");
					return appResponseModel;
				}else{
					user.setPasswd(newPasswd);
					if(userIdentifyService.updateUser(user)){
						userServiceImp.updatePasswd(user, user.getPasswd(), newPasswd);
						appResponseModel.setResultCode(1);
						appResponseModel.setResultMsg("SUCCESS");
					}else{
						appResponseModel.setResultCode(0);
						appResponseModel.setResultMsg("ERROR");
					}
				}
			}else{
				appResponseModel.setResultCode(0);
				appResponseModel.setResultMsg("wrong identify code");
				request.getSession().removeAttribute("PHONE_TEXT");
				request.getSession().removeAttribute("PHONE_NUM");
			}
		}else{
			appResponseModel.setResultCode(0);
			appResponseModel.setResultMsg("param error");
			request.getSession().removeAttribute("PHONE_TEXT");
			request.getSession().removeAttribute("PHONE_NUM");
		}
		request.getSession().removeAttribute("PHONE_TEXT");
		request.getSession().removeAttribute("PHONE_NUM");
		return appResponseModel;
	}
	
}
