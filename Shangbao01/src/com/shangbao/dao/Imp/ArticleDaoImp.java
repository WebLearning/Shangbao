package com.shangbao.dao.Imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.SequenceDao;
import com.shangbao.model.Article;
import com.shangbao.model.Page;

@Component
public class ArticleDaoImp implements ArticleDao {

	@Resource
	private MongoTemplate mongoTemplate;
	
	@Resource
	private SequenceDao sequenceDaoImp;
	
	private static final String ARTICLE_SEQ_KEY = "article";
	
	public MongoTemplate getMongoTemplate() {
		return mongoTemplate;
	}

	public void setMongoTemplate(MongoTemplate mongoTemplate) {
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public void insert(Article article) {
		article.setId(sequenceDaoImp.getNextSequenceId(ARTICLE_SEQ_KEY));
		mongoTemplate.insert(article);
	}

	@Override
	public void insertAll(List<Article> articles) {
		if(!articles.isEmpty()){
			List<Article> tempArticles = null;
			for(Article article : articles){
				article.setId(sequenceDaoImp.getNextSequenceId(ARTICLE_SEQ_KEY));
				tempArticles.add(article);
			}
			mongoTemplate.insertAll(tempArticles);
		}
	}

	@Override
	public void deleteById(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(Article criteriaArticle) {
		mongoTemplate.remove(getQuery(criteriaArticle), Article.class);
	}

	@Override
	public void deleteAll() {
		mongoTemplate.dropCollection(Article.class);
	}

	@Override
	public void updateById(long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(Article criteriaArticle, Article article) {
		
	}

	@Override
	public List<Article> find(Article article) {
		return mongoTemplate.find(getQuery(article), Article.class);
	}

	@Override
	public Article findById(long id) {
		Criteria criteria = Criteria.where("id").is(id);
		return mongoTemplate.findOne(new Query().addCriteria(criteria), Article.class);
	}

	@Override
	public List<Article> findAll() {
		return mongoTemplate.findAll(Article.class);
	}

	@Override
	public List<Article> find(Article criteriaArticle, int skip, int limit) {
		Query query = getQuery(criteriaArticle);  
        query.skip(skip);  
        query.limit(limit);  
        return mongoTemplate.find(query, Article.class);
	}

	@Override
	public Article findAndModify(Article criteriaArticle, Article updateArticle) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Article findAndRemove(Article criteriaArticle) {
		Query query = getQuery(criteriaArticle);
		return mongoTemplate.findAndRemove(query, Article.class);
	}

	@Override
	public long count(Article criteriaArticle) {
		return mongoTemplate.count(getQuery(criteriaArticle), Article.class);
	}
	
	private Query getQuery(Article criteriaArticle) {  
        if (criteriaArticle == null) {  
        	criteriaArticle = new Article();  
        }  
        Query query = new Query();  
        if(criteriaArticle.getId() > 0){
        	Criteria criteria = Criteria.where("id").is(criteriaArticle.getId());
        	query.addCriteria(criteria);
        }
        if (criteriaArticle.getTitle() != null) {  
            Criteria criteria = Criteria.where("title").is(criteriaArticle.getTitle());  
            query.addCriteria(criteria);  
        }  
        if (criteriaArticle.getAuthor() != null) {  
            Criteria criteria = Criteria.where("author").is(criteriaArticle.getAuthor());  
            query.addCriteria(criteria);  
        }  
        if (criteriaArticle.getTime() != null) {  
            Criteria criteria = Criteria.where("time").is(criteriaArticle.getTime());  
            query.addCriteria(criteria);  
        }  
        return query;  
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

}
