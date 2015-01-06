package com.shangbao.service.imp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.CommendDao;
import com.shangbao.model.CommendState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.show.CommendForArticle;
import com.shangbao.model.show.CommendList;
import com.shangbao.model.show.CommendPage;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.CommendService;

@Service
public class CommendServiceImp implements CommendService {

	@Resource
	private CommendDao commendDaoImp;
	
	@Resource
	private ArticleDao articleDaoImp;

	@Override
	public void add(Commend commend){
		commendDaoImp.insert(commend);
	}
	
	@Override
	public CommendList get(Commend criteriaElement, int pageId) {
		CommendList commendList = new CommendList();
		List<Commend> commends = commendDaoImp.find(criteriaElement);
		if (commends.size() == 1
				&& commends.get(0).getCommendList().size() > 10 * (pageId - 1)) {
			List<SingleCommend> singleCommends = commends.get(0)
					.getCommendList();
			int pageCount = singleCommends.size() / 10 + 1;
			commendList.setPageCount(pageCount);
			commendList.setCurrentNo(pageId);
			commendList.setCommendList(singleCommends.subList(
					(pageId - 1) * 10,
					pageId * 10 - 1 > singleCommends.size() ? singleCommends.size() : pageId * 10 - 1));
			return commendList;
		}
		return null;
	}

	@Override
	public CommendList get(Commend criteriaElement, int pageId, String order) {
		CommendList commendList = new CommendList();
		List<Commend> commends = commendDaoImp.find(criteriaElement);
		if (commends.size() == 1
				&& commends.get(0).getCommendList().size() > 10 * (pageId - 1)) {
			List<SingleCommend> singleCommends = reSort(commends.get(0)
					.getCommendList(), order);
			if (singleCommends.size() > 0) {
				int pageCount = singleCommends.size() / 10;
				commendList.setCurrentNo(pageCount);
				commendList.setCurrentNo(pageId);
				commendList.setCommendList(singleCommends.subList(
						(pageId - 1) * 10,
						pageId * 10 - 1 > singleCommends.size() ? singleCommends.size() : pageId * 10 - 1));
				return commendList;
			}
		}
		return null;
	}

	@Override
	public void add(Commend commend, SingleCommend singleCommend) {
		List<Commend> commends = commendDaoImp.find(commend);
		if(commends == null || commends.isEmpty()){
			Article article = new Article();
			article.setId(commend.getArticleId());
			List<Article> articles = articleDaoImp.find(article);
			if(articles == null || articles.isEmpty()){
				return;
			}
			commend.setArticleTitle(articles.get(0).getTitle());
			commend.setState(articles.get(0).getState());
			commend.getCommendList().add(singleCommend);
			commendDaoImp.insert(commend);
		}else{
			Update updateElement = new Update();
			singleCommend.setCommendId(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()));
			updateElement.push("commendList", singleCommend);
			commendDaoImp.update(commend, updateElement);
		}
		//将文章评论数加一
		Article article = new Article();
		article.setId(commend.getArticleId());
		Update update = new Update();
		if(commend instanceof CrawlerCommend){
			update.inc("newsCommends", 1);
			articleDaoImp.update(article, update);
		}else{
			update.inc("crawlerCommends", 1);
			articleDaoImp.update(article, update);
		}
	}

	@Override
	public void update(Commend commend, SingleCommend singleCommend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Commend commend, List<SingleCommend> singeCommends) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reply(Commend commend, String commendId, String reply) {
		Update updateElement = new Update();
		Query query = new Query();
		query.addCriteria(new Criteria().where("commendList.commendId").is(commendId));
		updateElement.set("commendList.$.reply", reply);
		commendDaoImp.update(commend, query, updateElement);
	}

	@Override
	public void publish(Commend commend, List<String> singleCommendIds) {
		Update updateElement = new Update();
		updateElement.set("commendList.$.state", CommendState.published.toString());
		for(String commendId : singleCommendIds){
			Query query = new Query();
			query.addCriteria(new Criteria().where("commendList.commendId").is(commend));
			commendDaoImp.update(commend, query, updateElement);
		}
		Article article = new Article();
		article.setId(commend.getArticleId());
		List<Article> articles = articleDaoImp.find(article);
		if(articles != null && !articles.isEmpty()){
			Update update = new Update();
			if(commend instanceof CrawlerCommend){
				update.inc("newsCommendsPublish", 1);
			}else{
				update.inc("crawlerCommendsPublish", 1);
			}
			articleDaoImp.update(article, update);
		}
	}

	@Override
	public void delete(Commend commend, List<String> singleCommendIds) {
		for(String commendId : singleCommendIds){
			Update updateElement = new Update();
			Query query = new Query();
			DBObject object = new BasicDBObject();
			object.put("commendId", commendId);
			updateElement.pull("commendList", object);
			query.addCriteria(new Criteria().where("commendList.commendId").is(commend));
			commendDaoImp.update(commend, query, updateElement);
		}
	}

	@Override
	public CommendPage getCommendPage(int pageNo) {
		CommendPage commendPage = new CommendPage();
		List<CommendForArticle> commendList = new ArrayList<CommendForArticle>();
		Page<Article> page = commendDaoImp.getPage(pageNo, 20, new Query());
		commendPage.setCurrentNo(pageNo);
		commendPage.setPageCount(page.getTotalPage());
		for(Article article : page.getDatas()){
			CommendForArticle commendForArticle = new CommendForArticle(article);
			commendList.add(commendForArticle);
		}
		commendPage.setCommendList(commendList);
		return commendPage;
	}

	@Override
	public CommendPage getCommendPage(int pageNo, String order) {
		CommendPage commendPage = new CommendPage();
		List<CommendForArticle> commendList = new ArrayList<CommendForArticle>();
		Query query = new Query();
		query.with(new Sort(order));
		Page<Article> page = commendDaoImp.getPage(pageNo, 20, query);
		commendPage.setCurrentNo(pageNo);
		commendPage.setPageCount(page.getTotalPage());
		for(Article article : page.getDatas()){
			CommendForArticle commendForArticle = new CommendForArticle(article);
			commendList.add(commendForArticle);
		}
		commendPage.setCommendList(commendList);
		return commendPage;
	}

	private List<SingleCommend> reSort(List<SingleCommend> singleCommends,
			String order) {
		Comparator comparator = new SingleCommendCompara(order);
		Collections.sort(singleCommends, comparator);
		return singleCommends;
	}
	
	class SingleCommendCompara implements Comparator<SingleCommend>{
		private String order;
		public SingleCommendCompara(String order){
			this.order = order;
		}
		@Override
		public int compare(SingleCommend o1, SingleCommend o2) {
			if(order.equals("time")){
				return o1.getTimeDate().after(o2.getTimeDate()) ? 1 : 0;
			}else if(order.equals("level")){
				return o1.getLevel().compareTo(o2.getLevel());
			}else if(order.equals("state")){
				return o1.getState().toString().compareTo(o2.getState().toString());
			}else if(order.equals("from")){
				return o1.getFrom().compareTo(o2.getFrom());
			}
			return 0;
		}
	}
}
