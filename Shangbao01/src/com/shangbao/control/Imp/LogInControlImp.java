package com.shangbao.control.Imp;

import java.util.List;

import com.shangbao.control.LogInControl;
import com.shangbao.dao.UserDao;
import com.shangbao.model.User;

public class LogInControlImp implements LogInControl{
	
	private UserDao userDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public Boolean logIn(String name, String passwd) {
		User user = new User();
		List<User> ulist = null;
		user.setName(name);
		user.setPasswd(passwd);
		if((ulist = userDao.find(user)).size() != 0){
			return true;
		}else{
			return false;
		}
	}

}
