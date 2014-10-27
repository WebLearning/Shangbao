package com.shangbao.control.Imp;

import java.util.List;

import com.shangbao.control.RejisterControl;
import com.shangbao.dao.UserDao;
import com.shangbao.model.User;

public class RejisterControlImp implements RejisterControl{

	private UserDao userDao;
	
	public UserDao getUserDao() {
		return userDao;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	@Override
	public Boolean rejister(String name, String passwd) {
		User user = new User();
		user.setName(name);
		List<User> uList = userDao.find(user);
		if(uList.size() > 0){
			return false;
		}else{
			user.setPasswd(passwd);
			userDao.insert(user);
			return true;
		}
	}

}
