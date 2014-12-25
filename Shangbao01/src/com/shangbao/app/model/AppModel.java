package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.ChannelDao;
import com.shangbao.dao.CommendDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.show.SingleCommend;

@Component
@Scope("singleton")
public class AppModel {
	private List<String> startPictures = new ArrayList<String>();//启动显示图片
	private Map<String, List<Article>> appMap = new HashMap<String, List<Article>>();//每个channel包含的文章
	private List<ChannelModel> channelModels = new ArrayList<ChannelModel>();//所有的channel
	private Map<Long, List<SingleCommend>> commends = new HashMap<Long, List<SingleCommend>>();//每篇文章的评论
	private List<Channel> activities = new ArrayList<Channel>();

	
	@Autowired
	public AppModel(@Qualifier("articleDaoImp") ArticleDao articleDaoImp, 
			@Qualifier("channelDaoImp") ChannelDao channelDaoImp,
			@Qualifier("commendDaoImp") CommendDao commendDaoImp){
		//初始化appMap
		System.out.println("Inite!");
		Article criteriaArticle = new Article();
		criteriaArticle.setState(ArticleState.Published);
		List<Article> articles = articleDaoImp.find(criteriaArticle, Direction.DESC, "time");
		for(Article article : articles){
			List<String> articleChannels = article.getChannel();
			if(articleChannels != null && !articleChannels.isEmpty()){
				for(String channelName : articleChannels){
					addArticle(channelName, article);
				}
				//初始化commends
				Commend criteriaCommend = new Commend();
				List<SingleCommend> singleCommends = new ArrayList<SingleCommend>();
				criteriaCommend.setArticleId(article.getId());
				List<Commend> commends = commendDaoImp.find(criteriaCommend);
				if(commends != null && !commends.isEmpty()){
					for(Commend commend : commends){
						if(commend.getCommendList() != null){
							singleCommends.addAll(commend.getCommendList());
						}
					}
				}
				this.commends.put(article.getId(), singleCommends);
			}
		}
				
		//初始化channelModels
		Channel criteriaChannel = new Channel();
		criteriaChannel.setState(ChannelState.Father);
		List<Channel> fatherChannels = channelDaoImp.find(criteriaChannel);
		if(fatherChannels != null && !fatherChannels.isEmpty()){
			for(Channel fatherChannel : fatherChannels){
				Channel sonCriteriaChannel = new Channel();
				sonCriteriaChannel.setState(ChannelState.Son);
				sonCriteriaChannel.setRelated(fatherChannel.getChannelName());
				List<Channel> sonChannels = channelDaoImp.find(sonCriteriaChannel);
				addTopChannel(fatherChannel, sonChannels);
			}
		}
		
		
		//初始化activities
		Channel criteriaActivity = new Channel();
		criteriaActivity.setState(ChannelState.Activity);
		this.activities = channelDaoImp.find(criteriaActivity);
	}
	
	
	public List<String> getStartPictures() {
		return startPictures;
	}

	public void setStartPictures(List<String> startPictures) {
		this.startPictures = startPictures;
	}
	
	public Map<String, List<Article>> getAppMap() {
		return appMap;
	}

	public void setAppMap(Map<String, List<Article>> appMap) {
		this.appMap = appMap;
	}

	public List<ChannelModel> getChannelModels() {
		return channelModels;
	}

	public void setChannelModels(List<ChannelModel> channelModels) {
		this.channelModels = channelModels;
	}

	public Map<Long, List<SingleCommend>> getCommends() {
		return commends;
	}

	public void setCommends(Map<Long, List<SingleCommend>> commends) {
		this.commends = commends;
	}

	public List<Channel> getActivities() {
		return activities;
	}

	public void setActivities(List<Channel> activities) {
		this.activities = activities;
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
	
	/**
	 * 
	 * @param channelName
	 */
	public void addChannel(String channelName) {
		if (!appMap.containsKey(channelName)) {
			List<Article> articles = new LinkedList<Article>();
			appMap.put(channelName, articles);
		}
	}

	public void addArticle(String channelName, Article article) {
		if (appMap.containsKey(channelName)) {
			if (appMap.get(channelName) != null) {
				appMap.get(channelName).add(article);
			}else{
				List<Article> articles = new ArrayList<Article>();
				articles.add(article);
				appMap.put(channelName, articles);
			}
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
		if(this.commends.containsKey(articleId)){
			this.commends.get(articleId).add(singleCommend);
		}else{
			List<SingleCommend> singleCommends = new ArrayList<SingleCommend>();
			singleCommends.add(singleCommend);
			this.commends.put(articleId, singleCommends);
		}
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
