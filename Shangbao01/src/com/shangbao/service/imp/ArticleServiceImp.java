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
	public TitleList fuzzyFindOrder(String words, ArticleState state, int pageNo, int pageSize, String order, String direction){
		Page<Article> page = null;
		if(direction.equals("asc")){
			page = articleDaoImp.fuzzyFind(words, state, false, pageNo, pageSize, order, Direction.ASC);
		}else{
			page = articleDaoImp.fuzzyFind(words, state, false, pageNo, pageSize, order, Direction.DESC);
		}
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
		if(articleState.equals(ArticleState.Crawler)){
			query.with(new Sort(Direction.DESC, "time"));
		}
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
	public void setPutState(ArticleState articleState, List<Long> idList, String message) {
		ArticleState targetState = articleState;
		switch (articleState) {
		case Temp:
			if(pendTagServiceImp.isTag("article")){
				targetState = ArticleState.Pending;
				if(message != null){
					message += " 提交审核";
				}
			}else{
				targetState = ArticleState.Published;
				if(message != null){
					message += " 直接发布";
				}
			}
			break;
		case Pending:
			targetState = ArticleState.Published;
			if(message != null){
				message += " 发布";
			}
			break;
		case Revocation:
			targetState = ArticleState.Temp;
			if(message != null){
				message += " 转暂存";
			}
			break;
		case Crawler:
			if(pendTagServiceImp.isTag("article")){
				targetState = ArticleState.Temp;
				if(message != null){
					message += " 转草稿";
				}
			}else{
				targetState = ArticleState.Published;
				if(message != null){
					message += " 直接发布";
				}
			}
			break;
		default:
			break;
		}
		for(Long id : idList){
			Article criteriaArticle = new Article();
			criteriaArticle.setId(id);
			synchronized (this) {
				articleDaoImp.setState(targetState, criteriaArticle);
				if(message != null){
					articleDaoImp.addMessage(message, criteriaArticle);
				}
			}
		}
	}
	
	@Override
	public void setDeleteState(ArticleState articleState, List<Long> idList, String message){
		ArticleState targetState = articleState;
		switch (articleState) {
		case Crawler:
			targetState = ArticleState.Deleted;
			if(message != null){
				message += " 删除";
			}
			break;
		case Temp:
			targetState = ArticleState.Revocation;
			if(message != null){
				message += " 撤销";
			}
			break;
		case Pending:
			targetState = ArticleState.Revocation;
			if(message != null){
				message += " 撤销";
			}
			break;
		case Published:
			targetState = ArticleState.Revocation;
			if(message != null){
				message += " 撤销";
			}
			break;
		case Revocation:
			targetState = ArticleState.Deleted;
			if(message != null){
				message += " 删除";
			}
			break;

		default:
			break;
		}
		for(Long id : idList){
			Article criteriaArticle = new Article();
			criteriaArticle.setId(id);
			synchronized (this) {
				articleDaoImp.setState(targetState, criteriaArticle);
				if(message != null){
					articleDaoImp.addMessage(message, criteriaArticle);
				}
			}
		}
	}
}
