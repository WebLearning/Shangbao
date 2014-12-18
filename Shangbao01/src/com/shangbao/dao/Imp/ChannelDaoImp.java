package com.shangbao.dao.Imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.shangbao.dao.ChannelDao;
import com.shangbao.model.persistence.Channel;

public class ChannelDaoImp implements ChannelDao{

	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(Channel element) {
		this.mongoTemplate.insert(element);
	}

	@Override
	public void insertAll(List<Channel> elements) {
		this.mongoTemplate.insertAll(elements);
	}

	@Override
	public void delete(Channel criteriaElement) {
		this.mongoTemplate.remove(getQuery(criteriaElement), Channel.class);
	}

	@Override
	public void deleteAll() {
		this.mongoTemplate.remove(new Query(), Channel.class);
	}

	@Override
	public void update(Channel criteriaElement, Channel updateElement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Channel> find(Channel criteriaElement) {
		return this.mongoTemplate.find(getQuery(criteriaElement), Channel.class);
	}

	@Override
	public Channel findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Channel> findAll() {
		return this.mongoTemplate.findAll(Channel.class);
	}

	@Override
	public List<Channel> find(Channel criteriaElement, int skip, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Channel findAndModify(Channel criteriaElement, Channel updateElement) {
		Update update = new Update();
		update.set("channelName", updateElement.getChannelName());
		update.set("summary", updateElement.getSummary());
		update.set("state", updateElement.getState());
		update.set("related", updateElement.getRelated());
		return this.mongoTemplate.findAndModify(getQuery(updateElement), update, Channel.class);
	}

	@Override
	public Channel findAndRemove(Channel criteriaElement) {
		return this.mongoTemplate.findAndRemove(getQuery(criteriaElement), Channel.class);
	}

	@Override
	public long count(Channel criteriaElement) {
		return this.mongoTemplate.count(getQuery(criteriaElement), Channel.class);
	}

	private Query getQuery(Channel criteriaChannel){
		Query query = new Query();
		if(!criteriaChannel.getChannelName().isEmpty()){
			query.addCriteria(new Criteria().where("channelName").is(criteriaChannel.getChannelName()));
		}
		if(!criteriaChannel.getSummary().isEmpty()){
			query.addCriteria(new Criteria().where("summary").is(criteriaChannel.getSummary()));
		}
		if(!criteriaChannel.getRelated().isEmpty()){
			query.addCriteria(new Criteria().where("related").is(criteriaChannel.getRelated()));
		}
		if(criteriaChannel.getState() != null){
			query.addCriteria(new Criteria().where("state").is(criteriaChannel.getState()));
		}
		return query;
	}
}
