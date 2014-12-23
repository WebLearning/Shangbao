package com.shangbao.app.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.app.model.AppModel;
import com.shangbao.app.model.FrontPageModel;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;

@Service
public class AppService {
	@Resource
	private AppModel appModel;
	private final String appUrlPrefix = "/{phoneType}/";
	
	/**
	 * 获取app顶级栏目信息
	 * @return
	 */
	public FrontPageModel getChannels(){
		FrontPageModel frontPageModel = new FrontPageModel();
		List<Channel> channels = appModel.getTopChannels();
		for(Channel channel : channels){
			frontPageModel.addChannel(channel.getChannelName(), appUrlPrefix + channel.getEnglishName());
		}
		return frontPageModel;
	}
	
	/**
	 * 获取一个分类下的文章
	 * @param ChannelName
	 * @param fromIndex
	 * @param toIndex
	 * @return
	 */
	public List<Article> getArticlesFromChannel(String ChannelName, int fromIndex, int toIndex){
		List<Article> articles = appModel.getAppMap().get(ChannelName);
		if(articles != null){
			if(toIndex <= articles.size() + 1 && fromIndex > 0 && toIndex > fromIndex){
				return articles.subList(fromIndex-1, toIndex-1);
			}
		}
		return null;
	}
	
	/**
	 * 获取一个分类下的子分类
	 * @return
	 */
	public List<Channel> getSonChannels(String ChannelName){
		for(Channel fatherChannel : appModel.getTopChannels()){
			if(ChannelName.equals(fatherChannel.getChannelName())){
				return appModel.getSonChannels(fatherChannel);
			}
		}
		return null;
	}
	
	/**
	 * 获取一篇文章的HTML
	 * @return
	 */
	public String getNewsHtml(String channelName, int articleIndex){
		List<Article> articles;
		if((articles = appModel.getAppMap().get(channelName)) != null){
			if(articleIndex > 0 && articleIndex <= articles.size() + 1){
				return articles.get(articleIndex -1).getContent();
			}
		}
		return null;
	}
	
	/**
	 * 获取一个栏目的置顶文章
	 * @param channelName
	 * @return
	 */
	public String getTopHtml(String channelName){
		Article article;
		if((article = appModel.getTopMap().get(channelName)) != null){
			return article.getContent();
		}
		return null;
	}

	
	
	public AppModel getAppModel() {
		return appModel;
	}

	public void setAppModel(AppModel appModel) {
		this.appModel = appModel;
	}
}
