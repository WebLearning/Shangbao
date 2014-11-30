package com.shangbao.dao.Imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.shangbao.dao.CommendDao;
import com.shangbao.model.persistence.Commend;
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
		// TODO Auto-generated method stub

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

}
