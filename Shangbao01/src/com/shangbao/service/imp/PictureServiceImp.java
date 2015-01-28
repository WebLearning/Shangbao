package com.shangbao.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.ChannelDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.show.ChannelList;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.TitleList;
import com.shangbao.service.PendTagService;
import com.shangbao.service.PictureService;

@Service
public class PictureServiceImp implements PictureService{
	@Resource
	private ArticleDao articleDaoImp;
	@Resource
	private ChannelDao channelDaoImp;
	@Resource
	private PendTagService pendTagServiceImp;

	public ArticleDao getArticleDaoImp() {
		return articleDaoImp;
	}

	public void setArticleDaoImp(ArticleDao articleDaoImp) {
		this.articleDaoImp = articleDaoImp;
	}

	@Override
	public void add(Article article) {
		this.articleDaoImp.insert(article);
	}
	
	public Long addGetId(Article article){
		return articleDaoImp.insertAndGetId(article);
	}
	
	@Override
	public void add(Channel activity) {
		this.channelDaoImp.insert(activity);
	}
	
	@Override
	public void delete(List<Channel> channels){
		if(!channels.isEmpty()){
			for(Channel channel : channels){
				channel.setState(ChannelState.Activity);
				this.channelDaoImp.delete(channel);
			}
		}
	}
	
	@Override
	public TitleList getTiltList(ArticleState articleState, int pageNo) {
		TitleList titleList = new TitleList();
		Query query = new Query();
		query.addCriteria(Criteria.where("state").is(articleState.toString()));
		query.addCriteria(Criteria.where("tag").is(true));
		Page<Article> page = articleDaoImp.getPage(pageNo, 20, query);
		titleList.setCurrentNo(pageNo);
		titleList.setPageCount(page.getTotalPage());
		for(Article article : page.getDatas()){
			titleList.addTitle(article);
		}
		return titleList;
	}
	
	@Override
	public TitleList fuzzyFind(String words, ArticleState state, int pageNo, int pageSize){
		Page<Article> page = articleDaoImp.fuzzyFind(words, state, true, pageNo, pageSize);
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
			page = articleDaoImp.fuzzyFind(words, state, true, pageNo, pageSize, order, Direction.ASC);
		}else{
			page = articleDaoImp.fuzzyFind(words, state, true, pageNo, pageSize, order, Direction.DESC);
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
	public TitleList getOrderedList(ArticleState articleState, int pageNo,
			String order, String direction) {
		TitleList titleList = new TitleList();
		Query query = new Query();
		query.addCriteria(Criteria.where("state").is(articleState.toString()));
		query.addCriteria(Criteria.where("tag").is(true));
		if(direction.equals("asc")){
			query.with(new Sort(Direction.ASC, order));
		}else{
			query.with(new Sort(Direction.DESC, order));
		}
		//query.with(new Sort(order));
		Page<Article> page = articleDaoImp.getPage(pageNo, 20, query);
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

	@Override
	public Article findOne(Long id) {
		return articleDaoImp.findById(id);
	}

	@Override
	public void update(Article article) {
		articleDaoImp.update(article);
	}

	@Override
	public ChannelList getActivity(int pageNo, int pageSize) {
		ChannelList channelList = new ChannelList();
		Query activeQuery = new Query();
		activeQuery.addCriteria(Criteria.where("state").is(ChannelState.Activity.toString()));
		List<Channel> actives = this.channelDaoImp.getPage(pageNo, pageSize, activeQuery).getDatas();
		for(Channel channel : actives){
			Long pictureNum = new Long(0);
			Article criteriaArticle = new Article();
			criteriaArticle.setTag(true);
			String activeName = channel.getChannelName();
			criteriaArticle.setActivity(activeName);
			Long articleNum = this.articleDaoImp.count(criteriaArticle);
			if(!this.articleDaoImp.find(criteriaArticle).isEmpty()){
				for(Article article : this.articleDaoImp.find(criteriaArticle)){
					pictureNum += article.getPicturesUrl().size();
				}
			}
			channelList.addChannel(activeName, articleNum, pictureNum);
		}
		return channelList;
	}

	@Override
	public ChannelList getOrderActivity(int pageNo, int pageSize, String order) {
		ChannelList channelList = new ChannelList();
		Query activeQuery = new Query();
		activeQuery.addCriteria(Criteria.where("state").is(ChannelState.Activity.toString()));
		List<Channel> actives = this.channelDaoImp.getPage(pageNo, pageSize, activeQuery).getDatas();
		for(Channel channel : actives){
			Long pictureNum = new Long(0);
			Article criteriaArticle = new Article();
			criteriaArticle.setTag(true);
			String activeName = channel.getChannelName();
			criteriaArticle.setActivity(activeName);
			Long articleNum = this.articleDaoImp.count(criteriaArticle);
			if(!this.articleDaoImp.find(criteriaArticle).isEmpty()){
				for(Article article : this.articleDaoImp.find(criteriaArticle)){
					pictureNum += article.getPicturesUrl().size();
				}
			}
			channelList.addChannel(activeName, articleNum, pictureNum);
		}
		channelList.sorted(order);
		return channelList;
	}
}
