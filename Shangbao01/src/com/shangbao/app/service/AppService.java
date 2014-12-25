package com.shangbao.app.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.app.model.ActiveModel;
import com.shangbao.app.model.AppChannelModel;
import com.shangbao.app.model.AppModel;
import com.shangbao.app.model.AppPictureModel;
import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.app.model.FrontPageModel;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.SingleCommend;

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
	 * 获取含有子分栏的顶级目录的页面(如“最新资讯”，“本地报告”，“快拍成都”)
	 * @param ChannelName 顶级分类名
	 * @param titleSize 每个子分类显示的文章标题数目
	 * @return
	 */
	public AppChannelModel getChannelModel(String channelName, int titleSize){
		AppChannelModel appChannelModel = new AppChannelModel();
		List<Channel> topChannels = appModel.getTopChannels();
		if(topChannels == null || topChannels.isEmpty())
			return appChannelModel;
		for(Channel channel : topChannels){//找父分类
			if(channel.getChannelName().equals(channelName)){
				appChannelModel.setChannelName(channelName);
				List<Channel> sonChannels = appModel.getSonChannels(channel);//找子分类
				if(sonChannels == null || sonChannels.isEmpty()){
					return appChannelModel;
				}
				for(Channel sonChannel : sonChannels){
					List<Article> articles = appModel.getAppMap().get(sonChannel.getChannelName());
					if(articles != null){
						appChannelModel.addColumn(sonChannel.getChannelName(), articles.subList(0, titleSize));
					}
				}
			}
		}
		return appChannelModel;
	}
	
	/**
	 * 获取一个分类下的文章标题列表
	 * @param ChannelName
	 * @param pageNo 页码
	 * @param pageSize 页面大小
	 * @return
	 */
	public ColumnPageModel getArticlesFromChannel(String ChannelName, int pageNo, int pageSize){
		ColumnPageModel columnPageModel = new ColumnPageModel();
		List<Article> articles = appModel.getAppMap().get(ChannelName);
		Page<Article> page = new Page<Article>(pageNo, pageSize, articles.size());
		if(articles != null){
			int toIndex = page.getLastResult();
			int fromIndex = page.getFirstResult();
			List<Article> pageArticles = articles.subList(fromIndex-1, toIndex-1);
			columnPageModel.setCurrentNo(page.getPageNo());
			columnPageModel.setPageCount(page.getTotalPage());
			for(Article article : pageArticles){
				columnPageModel.addNewsTitle(article);
			}
		}
		return columnPageModel;
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
	 * 根据文章id返回
	 * @param articleId
	 * @return
	 */
	public List<SingleCommend> getCommentByArticleId(Long articleId){
		return appModel.findComments(articleId);
	}
	
	/**
	 * 获取所有活动信息
	 * @return
	 */
	public ActiveModel getActives(){
		ActiveModel actives = new ActiveModel();
		List<Channel> channels = appModel.getActivities();
		if(channels != null && !channels.isEmpty()){
			for(Channel activity : channels){
				actives.addActive(activity.getChannelName(), activity.getSummary());
			}
		}
		return actives;
	}
	
	/**
	 * 返回图片的详细页面
	 * @param channelName 快拍成都下级分类
	 * @param articleIndex 
	 * @return
	 */
	public AppPictureModel getPictureDetails(String channelName, int articleIndex){
		AppPictureModel pictureModel = new AppPictureModel();
		List<Article> articles;
		if((articles = appModel.getAppMap().get(channelName)) != null){
			if(articleIndex > 0 && articleIndex <= articles.size() + 1){
				pictureModel = new AppPictureModel(articles.get(articleIndex - 1));
			}
		}
		return pictureModel;
	}
	
	
	public AppModel getAppModel() {
		return appModel;
	}

	public void setAppModel(AppModel appModel) {
		this.appModel = appModel;
	}
}
