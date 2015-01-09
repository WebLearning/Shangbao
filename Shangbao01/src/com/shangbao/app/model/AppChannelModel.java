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
	
	public void addColumn(String columnName, String columnEnName, List<Article> articles){
		Column column = new Column();
		column.columnName = columnName;
		column.columnEnglishName = columnEnName;
		if(!articles.isEmpty()){
			int index = 1;
			for(Article article : articles){
				AppTitle appTitle = new AppTitle(article, index);
				column.content.add(appTitle);
				index ++;
			}
		}
		this.contentColumns.add(column);
	}

	/*
	 * 分栏
	 */
	class Column{
		public String columnName;
		public String columnEnglishName;
		public List<AppTitle> content = new ArrayList<AppTitle>();
	}
	
	/*
	 * 首页新闻显示标题
	 */
	class AppTitle{
		public String title;
		public String picUrl;
		public String summary;
		public Integer indexId;
		public Long newsId;
		public AppTitle(){	
		}
		public AppTitle(Article article, Integer id){
			this.title = article.getTitle();
			this.picUrl = article.getTitlePicUrl();
			this.summary = article.getSummary();
			indexId = id;
			this.newsId = article.getId();
		}
	}
}
