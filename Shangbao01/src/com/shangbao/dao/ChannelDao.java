package com.shangbao.dao;

import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.model.persistence.Channel;
import com.shangbao.model.show.Page;

public interface ChannelDao extends MongoDao<Channel>{
	Page<Channel> getPage(int pageNo, int pageSize, Query query);
}
