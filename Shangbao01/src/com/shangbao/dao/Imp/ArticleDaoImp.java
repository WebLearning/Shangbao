package com.shangbao.dao.Imp;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.persistence.Id;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.mongodb.WriteResult;
import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.SequenceDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;

@Repository
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
		Long idLong = sequenceDaoImp.getNextSequenceId(ARTICLE_SEQ_KEY);
		article.setId(idLong);
		//查找当前文章所属于分类
		List<String> channels = article.getChannel();
		if(channels != null && !channels.isEmpty()){
			for(String channelName : channels){
				//找出该channel所属文章的最大的index
				Query channelQuery = new Query();
				Criteria channelCriteria = new Criteria().where("state").is(ArticleState.Published.toString());
				channelQuery.addCriteria(channelCriteria);
				channelQuery.with(new Sort(Direction.DESC, "channelIndex." + channelName));
				channelQuery.limit(1);
				List<Article> articleList = mongoTemplate.find(channelQuery, Article.class);
				if(articleList != null && !articleList.isEmpty() && articleList.get(0).getChannelIndex().get(channelName) != null){
					article.getChannelIndex().put(channelName, articleList.get(0).getChannelIndex().get(channelName) + 1);
				}else{
					article.getChannelIndex().put(channelName, 1);
				}
			}
		}
		mongoTemplate.insert(article);
	}
	
	@Override
	public Long insertAndGetId(Article article){
		Long idLong = sequenceDaoImp.getNextSequenceId(ARTICLE_SEQ_KEY);
		article.setId(idLong);
		mongoTemplate.insert(article);
		return idLong;
	}

	@Override
	public void insertAll(List<Article> articles) {
		if(!articles.isEmpty()){
			List<Article> tempArticles = new ArrayList<>();
			for(Article article : articles){
				article.setId(sequenceDaoImp.getNextSequenceId(ARTICLE_SEQ_KEY));
				tempArticles.add(article);
			}
			mongoTemplate.insertAll(tempArticles);
		}
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
	public void update(Article article){
		mongoTemplate.save(article);
	}
	
	@Override
	public boolean update(Article criteriaArticle, Article article) {
		return false;
	}
	
	@Override
	public void update(Article criteriaArticle, Update update){
		Query query = getQuery(criteriaArticle);
		WriteResult result = mongoTemplate.updateFirst(query, update, Article.class);
	}

	@Override
	public List<Article> find(Article article) {
		return mongoTemplate.find(getQuery(article), Article.class);
	}
	
	@Override
	public Article findById(long id) {
		return mongoTemplate.findById(id, Article.class);
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
	public List<Article> find(Article criteriaArticle, Direction direction, String property){
		Query query = getQuery(criteriaArticle);
		Sort sort = new Sort(direction, property);
		query.with(sort);
		//System.out.println(query.getQueryObject());
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
        if (criteriaArticle.getChannel() != null && !criteriaArticle.getChannel().isEmpty()) {
        	Criteria criteria = Criteria.where("channel").in(criteriaArticle.getChannel());
        	query.addCriteria(criteria);
        }
        if (criteriaArticle.getActivity() != null && !criteriaArticle.getActivity().isEmpty()){
        	Criteria criteria = Criteria.where("activity").is(criteriaArticle.getActivity());
        	query.addCriteria(criteria);
        }
        if(criteriaArticle.getState() != null){
        	Criteria criteria = Criteria.where("state").is(criteriaArticle.getState().toString());
        	query.addCriteria(criteria);
        }
        if (criteriaArticle.isTag()){
        	Criteria criteria = Criteria.where("tag").is(true);
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
		//System.out.println(query.getQueryObject());
		//System.out.println(query.getSortObject());
		List<Article> datas = mongoTemplate.find(query, Article.class);
		page.setDatas(datas);
		return page;
	}

	@Override
	public void setState(ArticleState state, Article criteriaArticle) {
		Query query = getQuery(criteriaArticle);
		Update update = new Update();
		update.set("state", state.toString());
		if(state.equals(ArticleState.Published)){
			//如果是发表，需要设置文章在每个channel里的顺序
			Article article = findById(criteriaArticle.getId());
			List<String> channels = article.getChannel();
			if(channels != null && !channels.isEmpty()){
				for(String channel : channels){
					//针对每一个channel设置该文章的顺序
					Query channelQuery = new Query();
					Criteria channelCriteria = new Criteria().where("state").is(ArticleState.Published.toString());
					//找出该channel所属文章的最大的index
					channelQuery.addCriteria(channelCriteria);
					channelQuery.with(new Sort(Direction.DESC, "channelIndex." + channel));
					channelQuery.limit(1);
					List<Article> articleList = mongoTemplate.find(channelQuery, Article.class);
					if(articleList == null || articleList.isEmpty()){
						//这篇文章是该栏目第一个文章
						update.set("channelIndex." + channel, 1);
					}else{
						//该栏目已经有文章
						int index = 1;
						if(articleList.get(0).getChannelIndex().get(channel) != null){
							index = articleList.get(0).getChannelIndex().get(channel) + 1;
						}
						update.set("channelIndex." + channel, index);
					}
				}
			}
		}
//		System.out.println(query.getQueryObject());
//		System.out.println(update.getUpdateObject());
		WriteResult result = mongoTemplate.updateFirst(query, update, Article.class);
	}

	@Override
	public void setTopArticle(String channelName, Long articleId) {
		Article article = mongoTemplate.findById(articleId, Article.class);
		if(article == null){
			return;
		}
		if (article.getChannel().contains(channelName)){
			Query channelQuery = new Query();
			Criteria channelCriteria = new Criteria().where("state").is(ArticleState.Published.toString());
			//找出该channel所属文章的最大的index
			channelQuery.addCriteria(channelCriteria);
			channelQuery.with(new Sort(Direction.DESC, "channelIndex." + channelName));
			channelQuery.limit(1);
			List<Article> articleList = mongoTemplate.find(channelQuery, Article.class);
			if(articleList != null && !articleList.isEmpty()){
				if(articleList.get(0).getId() == articleId)
					return;
				int index = articleList.get(0).getChannelIndex().get(channelName);
				index ++;
				//将id为articleId的文章置顶
				Update update = new Update();
				Query query = new Query();
				query.addCriteria(new Criteria().where("id").is(articleId));
				update.set("channelIndex." + channelName, index);
				WriteResult result = mongoTemplate.updateFirst(query, update, Article.class);
			}
		}
	}

	@Override
	public void swapArticle(String channelName, Long articleAId, Long articleBId) {
		Article articleA = mongoTemplate.findById(articleAId, Article.class);
		Article articleB = mongoTemplate.findById(articleBId, Article.class);
		if(articleA == null || articleB == null || !articleA.getChannel().contains(channelName) || !articleB.getChannel().contains(channelName)){
			return;
		}
		int indexA = articleB.getChannelIndex().get(channelName);
		int indexB = articleA.getChannelIndex().get(channelName);
		if(indexA > 0 && indexB > 0){
			Update updateA = new Update();
			Query queryA = new Query();
			queryA.addCriteria(new Criteria().where("id").is(articleAId));
			updateA.set("channelIndex." + channelName, indexA);
			WriteResult resultA = mongoTemplate.updateFirst(queryA, updateA, Article.class);
			Update updateB = new Update();
			Query queryB = new Query();
			queryB.addCriteria(new Criteria().where("id").is(articleBId));
			updateB.set("channelIndex." + channelName, indexB);
			WriteResult resultB = mongoTemplate.updateFirst(queryB, updateB, Article.class);
		}
	}
}
