package com.shangbao.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.dao.ArticleDao;
import com.shangbao.dao.ChannelDao;
import com.shangbao.dao.ReadLogDao;
import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.persistence.ReadLog;

@Service
public class KuaipaiPKTask {
	@Resource
	private ReadLogDao readLogDaoImp;
	@Resource
	private ChannelDao channelDaoImp;
	@Resource
	private ArticleDao articleDaoImp;
	
	private String pkChannelName = "快拍PK";
	
	public void findTopArticle(){
		Long newTopArticleId = null;
		List<Long> oldTopArticleIds = new ArrayList<>();
		Date date = new Date();
		SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
		String dateString = dft.format(date);
		Channel criteriaChannel = new Channel();
		criteriaChannel.setChannelName(pkChannelName);
		List<Channel> channels = channelDaoImp.find(criteriaChannel);
		if(channels.isEmpty() || channels == null){
			return;
		}
		//获取当前发布的快拍pk的所有文章
		Article criteriaArticle = new Article();
		criteriaArticle.setState(ArticleState.Published);
		criteriaArticle.addChannel(pkChannelName);
		List<Article> pkArticles = articleDaoImp.find(criteriaArticle);
		if(pkArticles.isEmpty() || pkArticles == null){
			return;
		}
		//找到今日点赞最多的文章
		int likes = 0;
		for(Article article : pkArticles){
			if(article.getChannelIndex().get(pkChannelName) > (Integer.MAX_VALUE / 2)){
				oldTopArticleIds.add(article.getId());
			}
			ReadLog criteriaReadLog = new ReadLog();
			criteriaReadLog.setId(article.getId());
			List<ReadLog> readLogs = readLogDaoImp.find(criteriaReadLog);
			if(readLogs.isEmpty() || readLogs == null){
				continue;
			}else{
				if(readLogs.get(0).getDateLike().get(dateString) != null && readLogs.get(0).getDateLike().get(dateString) > likes){
					newTopArticleId = readLogs.get(0).getId();
					likes = readLogs.get(0).getDateLike().get(dateString);
				}
			}
		}
		//取消已经置顶的文章
		if(likes > 0){
			if(!oldTopArticleIds.isEmpty()){
				for(Long oldTopId : oldTopArticleIds){
					articleDaoImp.unSetTopArticle(pkChannelName, oldTopId);
				}
			}
			//置顶赞最多的文章
			if(newTopArticleId != null){
				articleDaoImp.setTopArticle(pkChannelName, newTopArticleId);
			}
		}
	}
}
