package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;

@Component
@Scope("singleton")
public class AppModel {
	private List<String> startPictures = new ArrayList<String>();
	private Map<String, List<Article>> appMap = new HashMap<String, List<Article>>();
	private Map<String, Article> topMap = new HashMap<String, Article>();// 置顶文章
	private List<ChannelModel> channelModels = new ArrayList<ChannelModel>();
	
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

	public Map<String, Article> getTopMap() {
		return topMap;
	}

	public void setTopMap(Map<String, Article> topMap) {
		this.topMap = topMap;
	}

	public List<ChannelModel> getChannelModels() {
		return channelModels;
	}

	public void setChannelModels(List<ChannelModel> channelModels) {
		this.channelModels = channelModels;
	}

	public void addStartPicture(String picUrl){
		if(!this.startPictures.contains(picUrl)){
			this.startPictures.add(picUrl);
		}
	}
	
	public void addStartPicture(String picUrl, int picIndex){
		if(picIndex > 0){
			this.startPictures.add(picIndex, picUrl);
		}
	}
	
	public void deleteStartPicture(String picUrl){
		if(this.startPictures.contains(picUrl)){
			this.startPictures.remove(picUrl);
		}
	}
	
	public void deleteStartPicture(int picIndex){
		if(picIndex > 0 && picIndex <= this.startPictures.size() + 1){
			this.startPictures.remove(picIndex - 1);
		}
	}
	
	public void addChannel(String channelName) {
		if (!appMap.containsKey(channelName)) {
			List<Article> articles = new LinkedList<Article>();
			appMap.put(channelName, articles);
		}
		if (topMap.containsKey(channelName)) {
			topMap.remove(channelName);
		}
	}

	public void addArticle(String channelName, Article article) {
		if (appMap.containsKey(channelName)) {
			List<Article> articles = appMap.get(channelName);
			if (articles != null) {
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
	
	public List<Channel> getSonChannels(Channel fatherChannel){
		ChannelModel channelModel = new ChannelModel();
		channelModel.fatherChannel = fatherChannel;
		if(this.channelModels.contains(channelModel)){
			return this.channelModels.get(this.channelModels.indexOf(channelModel)).sonChannels;
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
