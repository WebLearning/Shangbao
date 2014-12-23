package com.shangbao.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
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
import com.shangbao.service.PictureService;

@Service
public class PictureServiceImp implements PictureService{
	@Resource
	private ArticleDao articleDaoImp;
	@Resource
	private ChannelDao channelDaoImp;

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
		query.addCriteria(new Criteria().where("state").is(articleState));
		query.addCriteria(new Criteria().where("tag").is(true));
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
			String order) {
		TitleList titleList = new TitleList();
		Query query = new Query();
		query.addCriteria(new Criteria().where("state").is(articleState));
		query.addCriteria(new Criteria().where("tag").is(true));
		query.with(new Sort(order));
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
			targetState = ArticleState.Pending;
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
		activeQuery.addCriteria(new Criteria().where("state").is(ChannelState.Activity));
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
		activeQuery.addCriteria(new Criteria().where("state").is(ChannelState.Activity));
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
