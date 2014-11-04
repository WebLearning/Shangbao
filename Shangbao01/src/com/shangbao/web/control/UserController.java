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
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(){
		return "user/login";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(String username, String password, Model model){
		User user = new User();
		System.out.println(username + "  " + password);
		user.setName(username);
		user.setPasswd(password);
		List<User> ulist = userDao.find(user);
		if(ulist.isEmpty()){
			model.addAttribute("login", false);
		}else{
			for(User u : ulist){
				System.out.println(u);
			}
			model.addAttribute("login", true);
		}
		return "user/login";
	}

}
