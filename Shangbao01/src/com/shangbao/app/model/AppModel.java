package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.ChannelDao;
import com.shangbao.dao.CommendDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.ChannelState;
import com.shangbao.model.CommendState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.show.SingleCommend;

@Component
@Scope("singleton")
public class AppModel {
	
	private ArticleDao articleDaoImp;
	private ChannelDao channelDaoImp;
	private CommendDao commendDaoImp;
	
	private final List<String> startPictures = new CopyOnWriteArrayList<String>();//启动显示图片
	private final Map<String, List<Article>> appMap = new ConcurrentHashMap<String, List<Article>>();//每个channel包含的文章
	private final List<ChannelModel> channelModels = new CopyOnWriteArrayList<ChannelModel>();//所有的channel
	private final Map<Long, List<SingleCommend>> commends = new ConcurrentHashMap<Long, List<SingleCommend>>();//每篇文章的评论
	private final List<Channel> activities = new CopyOnWriteArrayList<Channel>();
	private final Map<String, String> channelEn_Cn = new ConcurrentHashMap<String, String>();//key:英文名； value:中文名
	private final Map<Long, Article> articleMap = new ConcurrentHashMap<>();//key是文章的id，value对应一篇文章
	
	
	@Autowired
	public AppModel(@Qualifier("articleDaoImp") ArticleDao articleDaoImp, 
			@Qualifier("channelDaoImp") ChannelDao channelDaoImp,
			@Qualifier("commendDaoImp") CommendDao commendDaoImp){
		this.articleDaoImp = articleDaoImp;
		this.channelDaoImp = channelDaoImp;
		this.commendDaoImp = commendDaoImp;
		
		System.out.println("Init!");
		//初始化appMap，articleMap
		List<Channel> channels = channelDaoImp.find(new Channel());
		for(Channel channel : channels){
			redeployChannelArticles(channel.getChannelName());
		}
		//打印
		for(String key : appMap.keySet()){
			System.out.println("key: " + key + "  value: ");
			for(Article article : appMap.get(key)){
				System.out.println("   " + article.getId() + "  " + article.getChannelIndex());
			}
		}
		
		//初始化commends
		Article criteriaArticle = new Article();
		criteriaArticle.setState(ArticleState.Published);
		List<Article> articles = articleDaoImp.find(criteriaArticle);
		for(Article article : articles){
			redeployComment(article.getId());
		}
		//打印
//		for(Long id : commends.keySet()){
//			System.out.println("ID: " + id + "Comments: ");
//			for(SingleCommend singleCommend : commends.get(id)){
//				System.out.println("    " + singleCommend.getContent());
//			}
//		}
				
		//初始化channelModels channelEn_Cn activities
		redeployChannels();
		//打印
		for(String key : channelEn_Cn.keySet()){
			System.out.println("key: " + key + "  value: " + channelEn_Cn.get(key));
		}
	}
	
	/**
	 * 更新appModel
	 * @param channelName
	 */
	public void redeployChannelArticles(String channelName){
		 Article criteriaArticle = new Article();
		 criteriaArticle.addChannel(channelName);
		 criteriaArticle.setState(ArticleState.Published);
		 List<Article> articles = articleDaoImp.find(criteriaArticle, Direction.DESC, "channelIndex." + channelName);
		 if(articles != null && !articles.isEmpty()){
			 appMap.put(channelName, articles);
			 for(Article article : articles){
				 articleMap.put(article.getId(), article);
			 }
		 }
	}
	
	/**
	 * 更新评论
	 * @param articleId
	 */
	public void redeployComment(Long articleId){
		Commend criteriaCommend = new Commend();
		List<SingleCommend> singleCommends = new ArrayList<SingleCommend>();
		criteriaCommend.setArticleId(articleId);
		List<Commend> commends = commendDaoImp.find(criteriaCommend);
		if(commends != null && !commends.isEmpty()){
			for(Commend commend : commends){
				if(commend.getCommendList() != null && !commend.getCommendList().isEmpty()){
					for(SingleCommend singleCommend : commend.getCommendList()){
						if(singleCommend.getState() != null && singleCommend.getState().equals(CommendState.published)){
							singleCommends.add(singleCommend);
						}
					}
				}
			}
		}
		this.commends.put(articleId, singleCommends);
	}
	
	/**
	 * 更新channels
	 */
	public void redeployChannels(){
		Channel criteriaChannel = new Channel();
		criteriaChannel.setState(ChannelState.Father);
		List<Channel> fatherChannels = channelDaoImp.find(criteriaChannel);
		if(fatherChannels != null && !fatherChannels.isEmpty()){
			for(Channel fatherChannel : fatherChannels){
				channelEn_Cn.put(fatherChannel.getEnglishName(), fatherChannel.getChannelName());
				Channel sonCriteriaChannel = new Channel();
				sonCriteriaChannel.setState(ChannelState.Son);
				sonCriteriaChannel.setRelated(fatherChannel.getChannelName());
				List<Channel> sonChannels = channelDaoImp.find(sonCriteriaChannel);
				addTopChannel(fatherChannel, sonChannels);
				for(Channel sonChannel : sonChannels){
					channelEn_Cn.put(sonChannel.getEnglishName(), sonChannel.getChannelName());
				}
			}
		}
		Channel criteriaActivity = new Channel();
		criteriaActivity.setState(ChannelState.Activity);
		this.activities.addAll(channelDaoImp.find(criteriaActivity));
	}
	
	/**
	 * 全部更新
	 */
	public void redeployAll(){
		appMap.clear();
		channelModels.clear();
		commends.clear();
		activities.clear();
		channelEn_Cn.clear();
		List<Channel> channels = channelDaoImp.find(new Channel());
		for(Channel channel : channels){
			redeployChannelArticles(channel.getChannelName());
		}
		
		Article criteriaArticle = new Article();
		criteriaArticle.setState(ArticleState.Published);
		List<Article> articles = articleDaoImp.find(criteriaArticle);
		for(Article article : articles){
			redeployComment(article.getId());
		}
		
		redeployChannels();
	}
	
	public List<String> getStartPictures() {
		return startPictures;
	}

//	public void setStartPictures(List<String> startPictures) {
//		this.startPictures = startPictures;
//	}
	
	public Map<String, List<Article>> getAppMap() {
		return appMap;
	}

//	public void setAppMap(Map<String, List<Article>> appMap) {
//		this.appMap = appMap;
//	}

	public List<ChannelModel> getChannelModels() {
		return channelModels;
	}

//	public void setChannelModels(List<ChannelModel> channelModels) {
//		this.channelModels = channelModels;
//	}

	public Map<Long, List<SingleCommend>> getCommends() {
		return commends;
	}

//	public void setCommends(Map<Long, List<SingleCommend>> commends) {
//		this.commends = commends;
//	}

	public List<Channel> getActivities() {
		return activities;
	}

//	public void setActivities(List<Channel> activities) {
//		this.activities = activities;
//	}

	public Map<String, String> getChannelEn_Cn() {
		return channelEn_Cn;
	}


//	public void setChannelEn_Cn(Map<String, String> channelEn_Cn) {
//		this.channelEn_Cn = channelEn_Cn;
//	}


	public Map<Long, Article> getArticleMap() {
		return articleMap;
	}

	/**
	 * 添加开始图片
	 * @param picUrl
	 */
	public void addStartPicture(String picUrl){
		if(!this.startPictures.contains(picUrl)){
			this.startPictures.add(picUrl);
		}
	}
	
	/**
	 * 添加开始图片
	 * @param picUrl
	 * @param picIndex
	 */
	public void addStartPicture(String picUrl, int picIndex){
		if(picIndex > 0){
			this.startPictures.add(picIndex, picUrl);
		}
	}
	
	/**
	 * 删除开始图片
	 * @param picUrl
	 */
	public void deleteStartPicture(String picUrl){
		if(this.startPictures.contains(picUrl)){
			this.startPictures.remove(picUrl);
		}
	}
	
	/**
	 * 按位置删除开始图片
	 * @param picIndex
	 */
	public void deleteStartPicture(int picIndex){
		if(picIndex > 0 && picIndex <= this.startPictures.size() + 1){
			this.startPictures.remove(picIndex - 1);
		}
	}

	public void addTopChannel(Channel fatherChannel, List<Channel> sonChannels) {
		if (!this.channelModels.contains(fatherChannel)) {
			ChannelModel channelModel = new ChannelModel();
			channelModel.fatherChannel = fatherChannel;
			if (sonChannels == null || sonChannels.isEmpty()) {
				channelModel.sonChannels = null;
			} else {
				channelModel.sonChannels = sonChannels;
			}
			this.channelModels.add(channelModel);
		}
	}

	public void deleteTopChannel(Channel fatherChannel) {
		ChannelModel deleteModel = new ChannelModel();
		deleteModel.fatherChannel = fatherChannel;
		if (this.channelModels.contains(deleteModel)) {
			this.channelModels.remove(deleteModel);
		}
	}

	
	public void addActivity(Channel activity){
		if(activity.getState().equals(ChannelState.Activity)){
			if(!this.activities.isEmpty()){
				for(Channel channel : this.activities){
					if(channel.getChannelName().equals(activity.getChannelName()))
						return;
				}
			}
			this.activities.add(activity);
		}
	}
	
	/**
	 * 获取所有一级分类
	 * @return
	 */
	public List<Channel> getTopChannels(){
		List<Channel> topChannels = new ArrayList<Channel>();
		if(!this.channelModels.isEmpty()){
			for(ChannelModel channelModel : this.channelModels){
				topChannels.add(channelModel.fatherChannel);
			}
			return topChannels;
		}
		return null;
	}
	
	/**
	 * 获取某个一级分类下的子分类
	 * @param fatherChannel 一级分类名
	 * @return
	 */
	public List<Channel> getSonChannels(Channel fatherChannel){
		ChannelModel channelModel = new ChannelModel();
		channelModel.fatherChannel = fatherChannel;
		if(this.channelModels.contains(channelModel)){
			return this.channelModels.get(this.channelModels.indexOf(channelModel)).sonChannels;
		}
		return null;
	}
	
	/**
	 * 添加一个评论
	 * @param articleId 评论文章的id
	 * @param singleCommend 添加的评论
	 */
	public void addComment(Long articleId, SingleCommend singleCommend){
		Update update = new Update();
		singleCommend.setState(CommendState.unpublished);
		singleCommend.setTimeDate(new Date());
		singleCommend.setCommendId("" + new Date().getTime() + singleCommend.getUserId());
		NewsCommend newsCommend = new NewsCommend();
		newsCommend.setArticleId(articleId);
		List<Commend> commends = commendDaoImp.find(newsCommend);
		if(commends != null && !commends.isEmpty()){
			update.push("commendList", singleCommend);
			commendDaoImp.update(newsCommend, update);
		}else{
			//如果当前没有该文章的评论
			Article article = new Article();
			article.setId(articleId);
			List<Article> articles = articleDaoImp.find(article);
			if(articles != null && !articles.isEmpty()){
				newsCommend.setArticleTitle(articles.get(0).getTitle());
				newsCommend.getCommendList().add(singleCommend);
				commendDaoImp.insert(newsCommend);
			}
		}
		Article criteriaArticle = new Article();
		criteriaArticle.setId(articleId);
		Update articleUpdate = new Update();
		update.inc("newsCommends", 1);
		articleDaoImp.update(criteriaArticle, articleUpdate);
	}
	
	/**
	 * 点赞
	 * @param articleId
	 */
	public void addLike(Long articleId){
		Article criteriaArticle = new Article();
		criteriaArticle.setId(articleId);
		Update update = new Update();
		update.inc("likes", 1);
		articleDaoImp.update(criteriaArticle, update);
	}
	
	/**
	 * 点击
	 * @param articleId
	 */
	public void addClick(Long articleId){
		Article criteriaArticle = new Article();
		criteriaArticle.setId(articleId);
		Update update = new Update();
		update.inc("clicks", 1);
		articleDaoImp.update(criteriaArticle, update);
	}
	
	public void postPictures(Article pictureArticle){
		pictureArticle.setTag(true);
		pictureArticle.setState(ArticleState.Temp);
		articleDaoImp.insert(pictureArticle);
	}
	
	/**
	 * 根据文章id返回文章评论列表
	 * @param articleId
	 * @return
	 */
	public List<SingleCommend> findComments(Long articleId){
		if(this.commends.containsKey(articleId)){
			return this.commends.get(articleId);
		}
		return null;
	}
	
	/**
	 * 将一个文章置顶
	 * @param channelName 栏目名称
	 * @param articleId 文章Id
	 */
	public void setTopArticle(String channelName, Long articleId){
		if(!appMap.containsKey(channelName)){
			return;
		}
		articleDaoImp.setTopArticle(channelName, articleId);
		redeployChannelArticles(channelName);
	}
	
	/**
	 * 交换两个文章的位置
	 * @param channelName 栏目名称
	 * @param articleAId 文章A的Id
	 * @param articleBId 文章B的Id
	 */
	public void swapArticle(String channelName, Long articleAId, Long articleBId){
		if(!appMap.containsKey(channelName)){
			return;
		}
		articleDaoImp.swapArticle(channelName, articleAId, articleBId);
		redeployChannelArticles(channelName);
	}
	
	class ChannelModel {
		public Channel fatherChannel;
		public List<Channel> sonChannels = new LinkedList<Channel>();

		@Override
		public boolean equals(Object object) {
			ChannelModel channelModel = (ChannelModel) object;
			if (fatherChannel != null && channelModel.fatherChannel != null) {
				if (fatherChannel.getChannelName() != null
						&& channelModel.fatherChannel.getChannelName() != null) {
					if (fatherChannel.getEnglishName() != null
							&& channelModel.fatherChannel.getEnglishName() != null) {
						return fatherChannel.getChannelName().equals(
								channelModel.fatherChannel.getChannelName())
								&& fatherChannel.getEnglishName().equals(
										channelModel.fatherChannel
												.getEnglishName());
					} else if (fatherChannel.getEnglishName() == null
							&& channelModel.fatherChannel.getEnglishName() == null) {
						return fatherChannel.getChannelName().equals(
								channelModel.fatherChannel.getChannelName());
					}
				}
			}
			return false;
		}

		@Override
		public int hashCode() {
			if (fatherChannel != null) {
				return fatherChannel.getChannelName().hashCode();
			} else {
				return 0;
			}
		}
	}
}
