package com.shangbao.web.control;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shangbao.dao.UserDao;
import com.shangbao.model.User;

@Controller
@RequestMapping("/user")
public class UserController {
	@Resource
	private UserDao userDao;
	
	public UserDao getUserDaoImp() {
		return userDao;
	}

	public void setUserDaoImp(UserDao userDao) {
		this.userDao = userDao;
	}
	
	@RequestMapping(value="/users", method=RequestMethod.GET)
	public String list(Model model){
		List<User> userList = userDao.findAll();
		model.addAttribute("users", userList);
		return "user/list";
	}
	
	
	public String show(){
		
		return null;
	}

}
