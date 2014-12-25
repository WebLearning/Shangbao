package com.shangbao.dao.Imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.shangbao.dao.CommendDao;
import com.shangbao.model.persistence.Article;
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
		List<Commend> commendList = new ArrayList<Commend>();
		if(criteriaElement instanceof NewsCommend){
			List<NewsCommend> newsList = mongoTemplate.find(getQuery(criteriaElement), NewsCommend.class);
			for(NewsCommend commend : newsList){
				commendList.add(commend);
			}
		}
		else if(criteriaElement instanceof CrawlerCommend){
			for(Commend commend : mongoTemplate.find(getQuery(criteriaElement), CrawlerCommend.class)){
				commendList.add(commend);
			}
		}
		else{
			List<NewsCommend> newsList = mongoTemplate.find(getQuery(criteriaElement), NewsCommend.class);
			for(Commend commend : newsList){
				commendList.add(commend);
			}
			for(Commend commend : mongoTemplate.find(getQuery(criteriaElement), CrawlerCommend.class)){
				commendList.add(commend);
			}
		}
		return commendList;
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
		return null;
	}
	
	@Override
	public Commend findById(long id) {
		
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
	public Page<Article> getPage(int pageNo, int pageSize, Query query) {
		long totalCount = mongoTemplate.count(query, Article.class);
		Page<Article> page = new Page<Article>(pageNo, pageSize, totalCount);
		query.skip(page.getFirstResult());// skip相当于从那条记录开始
		query.limit(pageSize);
		List<Article> datas = mongoTemplate.find(query, Article.class);
		page.setDatas(datas);
		return page;
	}

	@Override
	public Query getQuery(Commend commend) {
		Query query = new Query();
		if(commend.getArticleId() > 0){
			query.addCriteria(new Criteria().where("articleId").is(commend.getArticleId()));
		}
		if(commend.getArticleTitle() != null){
			query.addCriteria(new Criteria().where("articleTitle").is(commend.getArticleTitle()));
		}
		if(commend.getState() != null){
			query.addCriteria(new Criteria().where("state").is(commend.getState()));
		}
		return query;
	}

	@Override
	public void update(Commend commend, Update update) {
		Query query = getQuery(commend);
		if(commend instanceof NewsCommend){
			mongoTemplate.updateFirst(query, update, NewsCommend.class);
		}
		else if(commend instanceof CrawlerCommend){
			mongoTemplate.updateFirst(query, update, CrawlerCommend.class);
		}
	}

	@Override
	public void update(Commend commend, Query query, Update update) {
		query.addCriteria(new Criteria().where("articleId").is(commend.getArticleId()));
		if(commend instanceof NewsCommend){
			System.out.println(query.getQueryObject());
			System.out.println(update.getUpdateObject());
			mongoTemplate.updateFirst(query, update, NewsCommend.class);
		}
		else if(commend instanceof CrawlerCommend){
			mongoTemplate.updateFirst(query, update, CrawlerCommend.class);
		}
	}

}
