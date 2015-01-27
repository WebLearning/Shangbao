package com.shangbao.service.imp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.shangbao.dao.ArticleDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.TitleList;
import com.shangbao.service.ArticleService;
import com.shangbao.service.PendTagService;

@Service
public class ArticleServiceImp implements ArticleService {

	@Resource
	private ArticleDao articleDaoImp;
	@Resource
	private PendTagService pendTagServiceImp;
	
	public ArticleDao getArticleDaoImp() {
		return articleDaoImp;
	}

	public void setArticleDao(ArticleDao articleDaoImp) {
		this.articleDaoImp = articleDaoImp;
	}

	@Override
	public void add(Article article) {
		articleDaoImp.insert(article);
	}
	
	@Override
	public Long addGetId(Article article){
		return articleDaoImp.insertAndGetId(article);
	}

	@Override
	public Article findOne(Long id) {
		Article article = articleDaoImp.findById(id);
		return article;
	}
	
	@Override
	public List<Article> find(Article criteriaArticle){
		return articleDaoImp.find(criteriaArticle);
	}

	@Override
	public TitleList fuzzyFind(String words, ArticleState state, int pageNo, int pageSize){
		Page<Article> page = articleDaoImp.fuzzyFind(words, state, false, pageNo, pageSize);
		TitleList titleList = new TitleList();
		titleList.setCurrentNo(pageNo);
		titleList.setPageCount(page.getTotalPage());
		for(Article article : page.getDatas()){
			titleList.addTitle(article);
		}
		return titleList;
	}
	
	@Override
	public void deleteOne(Article article) {
		articleDaoImp.delete(article);
	}
	
	@Override
	public List<Article> find(Article criteriaArticle, Direction direction, String property){
		return articleDaoImp.find(criteriaArticle, direction, property);
	}

	@Override
	public Map<Long, String> showTitles() {
		Map<Long, String> titleMap = new HashMap<Long, String>();
		List<Article> articles = articleDaoImp.findAll();
		if(!articles.isEmpty()){
			for(Article article : articles){
				titleMap.put(article.getId(), article.getTitle());
			}
		}
		return titleMap;
	}

	@Override
	public void update(Article article) {
		articleDaoImp.update(article);
	}

	@Override
	public void updateCrawler(Article article){
		Article criteriaArticle = new Article();
		criteriaArticle.setId(article.getId());
		Update update = new Update();
		if(article.getCrawlerCommends() != 0){
			update.set("crawlerCommends", article.getCrawlerCommends());
		}
		if(article.getLevel() != null){
			update.set("level", article.getLevel());
		}
		articleDaoImp.update(criteriaArticle, update);
	}
	
	@Override
	public TitleList getTiltList(ArticleState articleState, int pageNo) {
		TitleList titleList = new TitleList();
		Query query = new Query();
		query.addCriteria(Criteria.where("state").is(articleState.toString()));
		query.addCriteria(Criteria.where("tag").is(false));
		Page<Article> page = articleDaoImp.getPage(pageNo, 20, query);
		titleList.setCurrentNo(pageNo);
		titleList.setPageCount(page.getTotalPage());
		for(Article article : page.getDatas()){
			titleList.addTitle(article);
		}
		return titleList;
	}

	@Override
	public TitleList getOrderedList(ArticleState articleState, int pageNo,
			String order, String direction) {
		TitleList titleList = new TitleList();
		Query query = new Query();
		query.addCriteria(Criteria.where("state").is(articleState.toString()));
		query.addCriteria(Criteria.where("tag").is(false));
		if(direction.equals("asc")){
			query.with(new Sort(Direction.ASC, order));
		}else{
			query.with(new Sort(Direction.DESC, order));
		}
		Page<Article> page = articleDaoImp.getPage(pageNo, 20, query);
		//System.out.println(query.getSortObject());
		titleList.setCurrentNo(pageNo);
		titleList.setPageCount(page.getTotalPage());
		for(Article article : page.getDatas()){
			titleList.addTitle(article);
		}
		return titleList;
	}

	@Override
	public void setPutState(ArticleState articleState, List<Long> idList) {
		ArticleState targetState = articleState;
		switch (articleState) {
		case Temp:
			if(pendTagServiceImp.isTag("article")){
				targetState = ArticleState.Pending;
			}else{
				targetState = ArticleState.Published;
			}
			break;
		case Pending:
			targetState = ArticleState.Published;
			break;
		case Revocation:
			targetState = ArticleState.Temp;
			break;
		case Crawler:
			targetState = ArticleState.Temp;
			break;
		default:
			break;
		}
		for(Long id : idList){
			Article criteriaArticle = new Article();
			criteriaArticle.setId(id);
			articleDaoImp.setState(targetState, criteriaArticle);
		}
	}
	
	@Override
	public void setDeleteState(ArticleState articleState, List<Long> idList){
		ArticleState targetState = articleState;
		switch (articleState) {
		case Crawler:
			targetState = ArticleState.Deleted;
			break;
		case Temp:
			targetState = ArticleState.Revocation;
			break;
		case Pending:
			targetState = ArticleState.Revocation;
			break;
		case Published:
			targetState = ArticleState.Revocation;
			break;
		case Revocation:
			targetState = ArticleState.Deleted;	
			break;

		default:
			break;
		}
		for(Long id : idList){
			Article criteriaArticle = new Article();
			criteriaArticle.setId(id);
			articleDaoImp.setState(targetState, criteriaArticle);
		}
	}
	
	private void setArticleIndex(){
		
	}
}
