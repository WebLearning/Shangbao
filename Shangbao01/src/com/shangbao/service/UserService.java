package com.shangbao.service;

import java.util.List;

import com.shangbao.model.persistence.User;

public interface UserService {
	/**
	 * 添加一个用户
	 */
	public void addUser(User user);
	
	/**
	 * 列出所有用户
	 * @return
	 */
	public List<User> listUsers();
	
	/**
	 * 删除用户
	 * @param user
	 */
	public void deleteOne(User user);
	
	/**
	 * 查找用户
	 * @param user
	 * @return
	 */
	public User findOne(User user);
	
}
