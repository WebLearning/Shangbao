package com.shangbao.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.dao.UserDao;
import com.shangbao.model.User;
import com.shangbao.service.UserService;

@Service
public class UserServiceImp implements UserService {

	@Resource
	private UserDao userDao;
	
	@Override
	public void addUser(User user) {
		userDao.insert(user);
	}

	@Override
	public List<User> listUsers() {
		List<User> userList = userDao.findAll();
		return userList;
	}

	@Override
	public void deleteOne(User user) {
		userDao.delete(user);
	}

	@Override
	public User findOne(User user) {
		return userDao.find(user).get(0);
	}

}
