package com.shangbao.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.model.ArticleState;
import com.shangbao.model.ChannelState;
import com.shangbao.model.CommendState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.ChannelService;

public class ChannelDaoTest {

	@Test
	public void test() {
		//addChannel();
		appChannelTest();
//		addAppArticle();
		//addAppComment();
		addAppActivity();
	}
	
	public void addChannel(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		ChannelDao dao = (ChannelDao)applicationContext .getBean("channelDaoImp");
		Channel channel = new Channel();
		channel.setChannelName("FatherChannel");
		channel.setState(ChannelState.Father);
		channel.setSummary("Channel Summary");
		dao.insert(channel);
		Channel sonChannel = new Channel();
		sonChannel.setChannelName("SonChannel");
		sonChannel.setRelated("FatherChannel");
		sonChannel.setState(ChannelState.Son);
		dao.insert(sonChannel);
		Channel activity = new Channel();
		activity.setChannelName("活动一");
		activity.setState(ChannelState.Activity);
		activity.setRelated("SonChannel");
		dao.insert(activity);
	}
	
	public void appChannelTest(){
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		//ChannelDao dao = (ChannelDao) factory.getBean("channelDaoImp");
		ChannelService service = (ChannelService) factory.getBean("channelServiceImp");
		Channel topChannel1 = new Channel();
		topChannel1.setChannelName("商报原创");
		topChannel1.setEnglishName("original");
		topChannel1.setState(ChannelState.Father);
		topChannel1.setSummary("商报原创新闻");
		//dao.insert(topChannel1);
		service.addChannel(topChannel1);
		Channel topChannel2 = new Channel();
		topChannel2.setChannelName("最新资讯");
		topChannel2.setEnglishName("newest");
		topChannel2.setState(ChannelState.Father);
		topChannel2.setSummary("最新的新闻快报");
//		dao.insert(topChannel2);
		service.addChannel(topChannel2);
		Channel topChannel3 = new Channel();
		topChannel3.setChannelName("本地报告");
		topChannel3.setEnglishName("local");
		topChannel3.setState(ChannelState.Father);
		topChannel3.setSummary("成都本地新闻");
//		dao.insert(topChannel3);
		service.addChannel(topChannel3);
		Channel topChannel4 = new Channel();
		topChannel4.setChannelName("快拍成都");
		topChannel4.setEnglishName("kuaipai");
		topChannel4.setState(ChannelState.Father);
		topChannel4.setSummary("图片新闻");
//		dao.insert(topChannel4);
		service.addChannel(topChannel4);
		
		Channel secChannel1 = new Channel();
		secChannel1.setChannelName("国内");
		secChannel1.setEnglishName("domestic");
		secChannel1.setState(ChannelState.Son);
		secChannel1.setRelated("最新资讯");
//		dao.insert(secChannel1);
		service.addChannel(secChannel1);
		Channel secChannel2 = new Channel();
		secChannel2.setChannelName("国际");
		secChannel2.setEnglishName("internal");
		secChannel2.setState(ChannelState.Son);
		secChannel2.setRelated("最新资讯");
//		dao.insert(secChannel2);
		service.addChannel(secChannel2);
		Channel secChannel3 = new Channel();
		secChannel3.setChannelName("最热图片");
		secChannel3.setEnglishName("tophot");
		secChannel3.setState(ChannelState.Son);
		secChannel3.setRelated("快拍成都");
//		dao.insert(secChannel3);
		service.addChannel(secChannel3);
		
		Channel secChannel4 = new Channel();
		secChannel4.setChannelName("成华区");
		secChannel4.setEnglishName("chenghua");
		secChannel4.setState(ChannelState.Son);
		secChannel4.setRelated("本地报告");
//		dao.insert(secChannel4);
		service.addChannel(secChannel4);
		Channel secChannel5 = new Channel();
		secChannel5.setChannelName("金牛区");
		secChannel5.setEnglishName("jinniu");
		secChannel5.setState(ChannelState.Son);
		secChannel5.setRelated("本地报告");
//		dao.insert(secChannel5);
		service.addChannel(secChannel5);
		
		Channel secChannel6 = new Channel();
		secChannel6.setChannelName("活动");
		secChannel6.setEnglishName("activities");
		secChannel6.setState(ChannelState.Son);
		secChannel6.setRelated("快拍成都");
//		dao.insert(secChannel6);
		service.addChannel(secChannel6);
	}
	
	public void addAppArticle(){
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		ArticleDao dao = (ArticleDao) factory.getBean("articleDaoImp");
//		for(int i = 0; i < 15; i ++){
//			Article article = new Article();
//			article.setState(ArticleState.Published);
//			article.setTitle("原创新闻" + i);
//			article.setAuthor("yy");
//			article.setContent("新闻内容" + i);
//			article.setSummary("新闻概要" + i);
//			List<String> channels = new ArrayList<String>();
//			channels.add("商报原创");
//			article.setChannel(channels);
//			dao.insert(article);
//		}
//		for(int i = 0; i < 15; i ++){
//			Article article = new Article();
//			article.setState(ArticleState.Published);
//			article.setTitle("国内新闻" + i);
//			article.setAuthor("yy");
//			article.setContent("新闻内容" + i);
//			article.setSummary("新闻概要" + i);
//			List<String> channels = new ArrayList<String>();
//			channels.add("国内");
//			article.setChannel(channels);
//			dao.insert(article);
//		}
//		for(int i = 0; i < 15; i ++){
//			Article article = new Article();
//			article.setState(ArticleState.Published);
//			article.setTitle("国际新闻" + i);
//			article.setAuthor("yy");
//			article.setContent("新闻内容" + i);
//			article.setSummary("新闻概要" + i);
//			List<String> channels = new ArrayList<String>();
//			channels.add("国际");
//			article.setChannel(channels);
//			dao.insert(article);
//		}
		
		for(int i = 0; i < 15; i ++){
			Article article = new Article();
			article.setState(ArticleState.Published);
			article.setTitle("成华新闻" + i);
			article.setAuthor("yy");
			article.setContent("新闻内容" + i);
			article.setSummary("新闻概要" + i);
			List<String> channels = new ArrayList<String>();
			channels.add("成华区");
			article.setChannel(channels);
			dao.insert(article);
		}
		for(int i = 0; i < 15; i ++){
			Article article = new Article();
			article.setState(ArticleState.Published);
			article.setTitle("金牛新闻" + i);
			article.setAuthor("yy");
			article.setContent("新闻内容" + i);
			article.setSummary("新闻概要" + i);
			List<String> channels = new ArrayList<String>();
			channels.add("金牛区");
			article.setChannel(channels);
			dao.insert(article);
		}
		
//		for(int i = 0; i < 15; i ++){
//			Article article = new Article();
//			article.setTag(true);
//			article.setState(ArticleState.Published);
//			article.setTitle("图片新闻" + i);
//			article.setAuthor("yy");
//			article.addPicture("/WEB-SRC/picture/1.jpg");
//			article.addPicture("/WEB-SRC/picture/1.jpg");
//			article.addPicture("/WEB-SRC/picture/1.jpg");
//			article.setTitlePicUrl("/WEB-SRC/picture/1.jpg");
//			article.addChannel("最热图片");
//			article.addChannel("活动");
//			article.setActivity("活动" + i % 7 + 1);
//			dao.insert(article);
//		}
	}
	
	public void addAppComment(){
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		ArticleDao articleDao = (ArticleDao) factory.getBean("articleDaoImp");
		CommendDao commendDao = (CommendDao) factory.getBean("commendDaoImp");
		List<Article> articles = articleDao.findAll();
		for(Article article : articles){
			NewsCommend newsCommend = new NewsCommend();
			CrawlerCommend crawlerCommend = new CrawlerCommend();
			newsCommend.setArticleId(article.getId());
			crawlerCommend.setArticleId(article.getId());
			newsCommend.setArticleTitle(article.getTitle());
			crawlerCommend.setArticleTitle(article.getTitle());
			newsCommend.setState(ArticleState.Published);
			crawlerCommend.setState(ArticleState.Published);
			List<SingleCommend> comments = new ArrayList<SingleCommend>();
			for(int i = 0; i < 3; i ++){
				SingleCommend singleCommend = new SingleCommend();
				singleCommend.setCommendId(new Date().toString());
				singleCommend.setContent("测试评论" + i);
				singleCommend.setState(CommendState.published);
				comments.add(singleCommend);
			}
			newsCommend.setCommendList(comments);
			crawlerCommend.setCommendList(comments);
			commendDao.insert(newsCommend);
			commendDao.insert(crawlerCommend);
		}
	}
	
	public void addAppActivity(){
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
//		ChannelDao activityDao = (ChannelDao) factory.getBean("channelDaoImp");
		ChannelService service = (ChannelService) factory.getBean("channelServiceImp");
		List<Channel> activities = new ArrayList<Channel>();
		for(int i = 1; i < 8; i ++){
			Channel activity = new Channel();
			activity.setChannelName("活动" + i);
			activity.setEnglishName("activity" + i);
			activity.setState(ChannelState.Activity);
			activity.setSummary("这是一个快拍成都的活动");
			activities.add(activity);
			//activityDao.insert(activity);
			String resultString = service.addChannel(activity);
		}
//		activityDao.insertAll(activities);
	}

}
