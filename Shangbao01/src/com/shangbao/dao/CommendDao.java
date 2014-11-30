package com.shangbao.dao;

import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.model.persistence.Commend;

public interface CommendDao extends MongoDao<Commend> {
	Query getQuery(Commend commend);
}
