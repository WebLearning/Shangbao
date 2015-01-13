package com.shangbao.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.model.persistence.User;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.TitleList;

public interface UserDao extends MongoDao<User> {
	Page<User> getPage(int pageNo, int pageSize, Query query);
}
