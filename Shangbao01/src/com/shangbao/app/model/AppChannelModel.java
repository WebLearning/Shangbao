package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.List;

import com.shangbao.model.persistence.Article;

/**
 * 点击大类后获得的页面(不包括商报原创)
 * @author Administrator
 *
 */
public class AppChannelModel {
	private String channelName;
	private List<Column> contentColumns = new ArrayList<Column>();
	
	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public List<Column> getContentColumns() {
		return contentColumns;
	}

	public void setContentColumns(List<Column> contentColumns) {
		this.contentColumns = contentColumns;
	}
	
	public void addColumn(String columnName, List<Article> articles){
		Column column = new Column();
		column.columnName = columnName;
		if(!articles.isEmpty()){
			for(Article article : articles){
				AppTitle appTitle = new AppTitle(article);
				column.content.add(appTitle);
			}
		}
		this.contentColumns.add(column);
	}

	/*
	 * 分栏
	 */
	class Column{
		public String columnName;
		public List<AppTitle> content = new ArrayList<AppTitle>();
	}
	
	/*
	 * 首页新闻显示标题
	 */
	class AppTitle{
		public String title;
		public List<String> picUrl;
		public String summary;
		public Long newsId;
		public AppTitle(){	
		}
		public AppTitle(Article article){
			this.title = article.getTitle();
			this.picUrl = article.getPicturesUrl();
			this.summary = article.getSummary();
			this.newsId = article.getId();
		}
	}
}
