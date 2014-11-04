package com.shangbao.web.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.shangbao.dao.UserDao;
import com.shangbao.model.User;


@Service
public class MyUserDetailService implements UserDetailsService {

	@Resource
	private UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException {
		User user = new User();
		user.setName(userName);
		List<User>ulist = userDao.find(user);
		if(ulist.size() == 1){
			return (UserDetails) (ulist.toArray())[0];
		}
		return null;
	}

}
