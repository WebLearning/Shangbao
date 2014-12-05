package com.shangbao.dao.Imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.shangbao.dao.CommendDao;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.show.Page;

@Repository
public class CommendDaoImp implements CommendDao {

	@Resource
	private MongoTemplate mongoTemplate;
	
	@Override
	public void insert(Commend element) {
		this.mongoTemplate.insert(element);
	}

	@Override
	public void insertAll(List<Commend> elements) {
		this.mongoTemplate.insertAll(elements);
	}

	@Override
	public void delete(Commend criteriaElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteAll() {
		
	}

	@Override
	public void update(Commend criteriaElement, Commend updateElement) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Commend> find(Commend criteriaElement) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * 查找评论
	 * 若commend是NewsCommend则只查找商报评论
	 * 若commend是CrawlerCommend则只查找爬虫评论
	 * 若commend是Commend类型则查找两种评论
	 */
	@Override
	public List find(Query query, Commend commend) {
		if(commend instanceof NewsCommend){
			return mongoTemplate.find(query, NewsCommend.class);
		}
		else if(commend instanceof CrawlerCommend){
			return mongoTemplate.find(query, CrawlerCommend.class);
		}
		else if(commend instanceof Commend){
			List returnList = new ArrayList();
			returnList.addAll(mongoTemplate.find(query, NewsCommend.class));
			returnList.addAll(mongoTemplate.find(query, CrawlerCommend.class));
			return returnList;
		}
		return null;
	}
	
	@Override
	public Commend findById(long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Commend> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Commend> find(Commend criteriaElement, int skip, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Commend findAndModify(Commend criteriaElement, Commend updateElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Commend findAndRemove(Commend criteriaElement) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count(Commend criteriaElement) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Page<Commend> getPage(int pageNo, int pageSize, Query query) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Query getQuery(Commend commend) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update(Query query, Update update, Commend commend) {
		if(commend instanceof NewsCommend){
			mongoTemplate.updateFirst(query, update, NewsCommend.class);
		}
		else if(commend instanceof CrawlerCommend){
			mongoTemplate.updateFirst(query, update, CrawlerCommend.class);
		}
	}

}
