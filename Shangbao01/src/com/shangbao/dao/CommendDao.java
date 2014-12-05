package com.shangbao.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.shangbao.model.persistence.Commend;

public interface CommendDao extends MongoDao<Commend> {
	Query getQuery(Commend commend);
	List find(Query query, Commend commend);
	void update(Query query, Update update, Commend commend);
}
