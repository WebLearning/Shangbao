package com.shangbao.app.service;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.shangbao.app.model.ActiveModel;
import com.shangbao.app.model.AppChannelModel;
import com.shangbao.app.model.AppModel;
import com.shangbao.app.model.AppPictureModel;
import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.app.model.FrontPageModel;
import com.shangbao.dao.ArticleDao;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.ArticleService;

@Service
public class AppService {
	@Resource
	private AppModel appModel;
	@Resource
	private ArticleService articleServiceImp;
	@Resource
	private ArticleDao articleDaoImp;
	
	private final String appUrlPrefix = "/{phoneType}/";
	
	
	/**
	 * 获取app顶级栏目信息
	 * @return
	 */
	public FrontPageModel getChannels(String id){
		FrontPageModel frontPageModel = new FrontPageModel();
		List<String> pictures = appModel.getStartPictures(id);
		if(pictures != null){
			for(String picUrl : pictures){
				frontPageModel.addPictureUrl(picUrl);
			}
		}
		List<Channel> channels = appModel.getTopChannels();
		if(channels.isEmpty() || channels == null){
			return frontPageModel;
		}
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
	public AppChannelModel getChannelModel(String channelEnName, int titleSize){
		Map<String, String> channelEn_Cn = appModel.getChannelEn_Cn();
		String channelName = channelEn_Cn.get(channelEnName);
		if(channelName == null){
			return null;
		}
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
					if(channelEnName.equals("kuaipai") && !sonChannel.getChannelName().startsWith("#")){
						continue;
					}
					List<Article> articles = appModel.getAppMap().get(sonChannel.getChannelName());
					if(articles != null){
						if(titleSize >= articles.size()){
							appChannelModel.addColumn(sonChannel.getChannelName(), sonChannel.getEnglishName(), articles.subList(0, articles.size()));
						}else{
							appChannelModel.addColumn(sonChannel.getChannelName(), sonChannel.getEnglishName(), articles.subList(0, titleSize));
						}
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
	public ColumnPageModel getArticlesFromChannel(String channelName, int pageNo, int pageSize){
		Map<String, String> channelEn_Cn = appModel.getChannelEn_Cn();
		channelName = channelEn_Cn.get(channelName);
		//channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		ColumnPageModel columnPageModel = new ColumnPageModel();
		List<Article> articles = appModel.getAppMap().get(channelName);
		if(articles == null || articles.isEmpty()){
			return columnPageModel;
		}
		Page<Article> page = new Page<Article>(pageNo, pageSize, articles.size());
		if(articles != null){
			int toIndex = page.getLastResult();
			int fromIndex = page.getFirstResult();
			if(0 <= fromIndex && fromIndex <= toIndex){
				List<Article> pageArticles = articles.subList(fromIndex, toIndex);
				columnPageModel.setCurrentNo(page.getPageNo());
				columnPageModel.setPageCount(page.getTotalPage());
				int index = fromIndex + 1;
				for(Article article : pageArticles){
					columnPageModel.addNewsTitle(article, index ++);
				}
			}
		}
		return columnPageModel;
	}
	
	/**
	 * 获取一个分类下的子分类
	 * @return
	 */
	public List<Channel> getSonChannels(String channelName){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		for(Channel fatherChannel : appModel.getTopChannels()){
			if(channelName.equals(fatherChannel.getChannelName())){
				return appModel.getSonChannels(fatherChannel);
			}
		}
		return null;
	}
	
	/**
	 * 获取一篇文章的HTML
	 * @return
	 */
	public AppHtml getNewsHtml(Long articleId){
		AppHtml appHtml = new AppHtml();
		if(!appModel.getArticleMap().isEmpty()){
			if(appModel.getArticleMap().containsKey(articleId)){
				appHtml.html = articleToHtml(appModel.getArticleMap().get(articleId));
				appModel.addClick(articleId);
				appHtml.articleId = articleId;
			}else{
				Article articleInMongo = articleServiceImp.findOne(articleId);
				if(articleInMongo != null){
					appModel.getArticleMap().put(articleId, articleInMongo);
					//appHtml.html = articleInMongo.getContent();
					appHtml.html = articleToHtml(articleInMongo);
//					appHtml.articleId = articleId;
//					Update update = new Update();
//					update.inc("clicks", 1);
//					articleDaoImp.update(articleInMongo, update);
					appModel.addClick(articleId);
					return appHtml;
				}
			}
		}
		return appHtml;
	}
	
	public int addJsClick(Long articleId){
		return appModel.addJsClick(articleId);
	}

	
	public int getJsClick(Long articleId){
		return appModel.getJsClick(articleId);
	}
	
	public int addLike(Long articleId){
		return appModel.addLike(articleId);
	}
	
	public int getLike(Long articleId){
		return appModel.getLike(articleId);
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
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		AppPictureModel pictureModel = new AppPictureModel();
		List<Article> articles;
		if((articles = appModel.getAppMap().get(channelName)) != null){
			if(articleIndex > 0 && articleIndex <= articles.size() + 1){
				pictureModel = new AppPictureModel(articles.get(articleIndex - 1));
			}
		}
		return pictureModel;
	}
	
	/**
	 * 添加用户评论
	 * @return
	 */
	public void addComment(Long articleId, SingleCommend singleCommend){
		appModel.addComment(articleId, singleCommend);
	}
	
	/**
	 * 添加用户点赞
	 */
	public void sendLike(Long articleId){
		appModel.addLike(articleId);
	}
	
	/**
	 * 用户上传图片
	 */
	public void postPictures(Article pictureArticle){
		appModel.postPictures(pictureArticle);
	}
	
	/*
	 * 下面方法为后台Controller调用
	 */
	
	/**
	 * 返回当前所有Channel以及文章列表
	 * @return
	 */
	public List<BackChannelModel> getAllChannels(){
		List<BackChannelModel> channels = new ArrayList<BackChannelModel>();
		List<Channel> orderedChannels = appModel.getChannelOrdered();
		if(!this.appModel.getAppMap().isEmpty() && !orderedChannels.isEmpty()){
			for(Channel tempChannel : orderedChannels){
				String channelEnName = tempChannel.getEnglishName();
				String channelChName = tempChannel.getChannelName();
				BackChannelModel backChannelModel = new BackChannelModel();
				if(appModel.getAppMap().get(channelChName) != null && !appModel.getAppMap().get(channelChName).isEmpty()){
					backChannelModel.ChannelEnglishName = channelEnName;
					backChannelModel.ChannelName = channelChName;
					List<Article> articles = appModel.getAppMap().get(channelChName);
					int i = 1;
					for(Article article : articles){
						String articleTitle = article.getTitle();
						backChannelModel.addTitle(articleTitle, article.getId(), i, article.getChannelIndex().get(channelChName) > (Integer.MAX_VALUE/2));
						i ++;
						if(i >= 30)
							break;
					}
					channels.add(backChannelModel);
				}
			}
			
//			for(Map.Entry<String, List<Article>> entry : this.appModel.getAppMap().entrySet()){
//				String channelName = entry.getKey();
//				List<Article> articles = entry.getValue();
//				BackChannelModel backChannelModel = new BackChannelModel();
//				backChannelModel.ChannelName = channelName;
//				
//				if(articles != null && !articles.isEmpty()){
//					int i = 1;
//					for(Article article : articles){
//						String articleTitle = article.getTitle();
//						backChannelModel.addTitle(articleTitle, i);
//						i ++;
//					}
//				}
//				channels.add(backChannelModel);
//			}
		}
		return channels;
	}
	
	public BackChannelModel getChannelByName(String channelName){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		BackChannelModel backChannelModel = new BackChannelModel();
		if(appModel.getAppMap().containsKey(channelName)){
			backChannelModel.ChannelName = channelName;
			List<Article> articles = appModel.getAppMap().get(channelName);
			if(articles != null && !articles.isEmpty()){
				int i = 1;
				for(Article article : articles){
					backChannelModel.addTitle(article.getTitle(), article.getId(), i, article.getChannelIndex().get(channelName) > (Integer.MAX_VALUE/2));
					i ++;
				}
			}
		}
		return backChannelModel;
	}
	
	/**
	 * 设置文章位置
	 * @param channelName 分类名字
	 * @param index 文章在appModel中的位置
	 * @param tag  true为上移一位，false为下移一位
	 */
	public synchronized BackChannelModel setArticleLocation(String channelName, int index, boolean tag){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return null;
		}
		index --;
		if(this.appModel.getAppMap().containsKey(channelName)){
			if(index >= 0 && index < appModel.getAppMap().get(channelName).size()){
				if(tag){//上移一位
					if(index > 0){
						Article articleA = appModel.getAppMap().get(channelName).get(index - 1);
						Article articleB = appModel.getAppMap().get(channelName).get(index);
						appModel.swapArticle(channelName, articleA.getId(), articleB.getId());
					}
				}else{//下移一位
					if(index < appModel.getAppMap().get(channelName).size() - 1){
						Article articleA = appModel.getAppMap().get(channelName).get(index + 1);
						Article articleB = appModel.getAppMap().get(channelName).get(index);
						appModel.swapArticle(channelName, articleA.getId(), articleB.getId());
						appModel.redeployChannelArticles(channelName);
					}
				}
			}
		}
		return getChannelByName(channelName);
	}
	
	/**
	 * 将文章置顶
	 * @param channelName 分类名字
	 * @param index 文章在appModel中的位置
	 */
	public synchronized void setArticleTop(String channelName, int index){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return;
		}
		index --;
		if(this.appModel.getAppMap().containsKey(channelName)){
			if(index > 0 && index < appModel.getAppMap().get(channelName).size()){
				//Article article = appModel.getAppMap().get(channelName).get(index);
				appModel.setTopArticle(channelName, index);
				//appModel.redeployChannelArticles(channelName);
			}
		}
	}
	
	public synchronized void unSetArticleTop(String channelName, int index){
		channelName = appModel.getChannelEn_Cn().get(channelName);
		if(channelName == null){
			return;
		}
		index --;
		if(this.appModel.getAppMap().containsKey(channelName)){
			appModel.unSetTopArticle(channelName, index);
		}
	}
	
	public void refresh(){
		appModel.redeployAll();
	}
	
	public AppModel getAppModel() {
		return appModel;
	}

	public void setAppModel(AppModel appModel) {
		this.appModel = appModel;
	}
	
	
	public ArticleService getArticleServiceImp() {
		return articleServiceImp;
	}

	public void setArticleServiceImp(ArticleService articleServiceImp) {
		this.articleServiceImp = articleServiceImp;
	}


	public ArticleDao getArticleDaoImp() {
		return articleDaoImp;
	}

	public void setArticleDaoImp(ArticleDao articleDaoImp) {
		this.articleDaoImp = articleDaoImp;
	}

	
	private String articleToHtml(Article article){
		if(article.getOutSideUrl() == null || article.getOutSideUrl().equals("")){
			//不是外链文章
			String localhostString = "";
			try {
				Properties properties = PropertiesLoaderUtils.loadAllProperties("config.properties");
				localhostString = properties.getProperty("localhost");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String css = "app.css";
			if(article.isTag()){
				css = "kuaipai.css";
			}
			SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd");
			String duxq = "<div ng-app=\"readAndZan\" ng-controller=\"readAndZanCtrl\"><div data-ng-init=\"load()\"></div><div class=\"single-post-meta-top\">阅读{{clickNum}} &nbsp;&nbsp;&nbsp;&nbsp;<a ng-click=\"zanAdd(zanNum,pictureUrl)\"><img alt=\"\" src={{pictureUrl}}>{{zanNum}}</a></div></div>";
			String html = "";
			html += "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\"><html xmlns=\"http://www.w3.org/1999/xhtml\" lang=\"zh-CN\"><head profile=\"http://gmpg.org/xfn/11\"> <meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /> <title>";
			html += article.getTitle() +"  | 成都商报新闻客户端</title>" + "<link rel=\"stylesheet\" href=\"" + localhostString + "/WEB-SRC/" + css + "\" type=\"text/css\" /> <script src=\"" + localhostString + "/WEB-SRC/src/js/angular.min.js\"></script> <script src=\"" + localhostString + "/WEB-SRC/click.js\"></script>";
			html += "</head><body class=\"classic-wptouch-bg\"> " +  " <input type=\"hidden\" name=\"id\" value=" + article.getId() + "/> <div class=\"content single\"> <div class=\"post\"> <a class=\"sh2\">";
			html += article.getTitle() + "</a><div style=\"font-size:15px; padding: 5px 0;\"></div><div class=\"single-post-meta-top\">";
			html += (article.getAuthor() == null ? "" : article.getAuthor()) + "&nbsp&nbsp" + (article.getTime() == null ? "" : format.format(article.getTime()));
			html += "</div><div style=\"margin-top:10px; border-top:1px solid #d8d8d8; height:1px; background-color:#fff;\"></div> <div id=\"singlentry\" class=\"left-justified\">";
			html += article.getContent();
			html += "<p>&nbsp;</p> " + duxq + "</div></div></div> <div id=\"footer\"><p>成都商报</p></div></body></html>";
//			html = "<html><head><title>MyHtml.html</title><meta charset=\"utf-8\"><script src=\"" + "http://202.115.17.218:8080/Shangbao01" + "/WEB-SRC/src/js/angular.min.js\"></script><script src=\"" + "http://202.115.17.218:8080/Shangbao01" + "/WEB-SRC/click.js\"></script></head><body><div ng-app=\"readAndZan\" ng-controller=\"readAndZanCtrl\"><div data-ng-init=\"load()\"></div><div class=\"single-post-meta-top\">阅读{{clickNum}} &nbsp;&nbsp;&nbsp;&nbsp;<a ng-click=\"zanAdd(zanNum,pictureUrl)\"><img alt=\"\" src={{pictureUrl}}>{{zanNum}}</a></div></div></body></html>";
			return html;
		}else{
			//是外联文章
			String html = "";
			html += "<html><head><title></title></head><body>";
			html += "<script language=\"javascript\">document.location = \"";
			html +=	(article.getOutSideUrl().startsWith("http://") || article.getOutSideUrl().startsWith("https://")) ? article.getOutSideUrl() : ("http://" + article.getOutSideUrl());
			html += "\"</script></body></html>";
			System.out.println(html);
			return html;
		}
	}

	/**
	 * 后台在一览众显示分类和文章的模板
	 */
	public class BackChannelModel{
		public String ChannelName;
		public String ChannelEnglishName;
		public List<Title> content = new ArrayList<Title>();
		class Title{
			public String title;
			public long articleId;
			public int index;
			public boolean top;
		}
		public void addTitle(String articleTitle, long articleId, int index, boolean top){
			Title title = new Title();
			title.title = articleTitle;
			title.articleId = articleId;
			title.index = index;
			title.top = top;
			this.content.add(title);
		}
		
	}
	
	public class AppHtml{
		public String html;
		public Long articleId;
	}
}
