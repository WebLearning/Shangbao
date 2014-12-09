package com.shangbao.dao;

import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.model.persistence.User;
import com.shangbao.model.show.Page;

public interface UserDao extends MongoDao<User> {
	Page<User> getPage(int pageNo, int pageSize, Query query);
}
