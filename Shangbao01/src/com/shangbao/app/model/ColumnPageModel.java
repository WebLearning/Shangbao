package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.shangbao.model.persistence.Article;

public class ColumnPageModel {
	private int PageCount;//页数
	private int currentNo;//当前页码
	private List<NewsTitle> content = new ArrayList<NewsTitle>();
	
	public int getPageCount() {
		return PageCount;
	}

	public void setPageCount(int pageCount) {
		PageCount = pageCount;
	}

	public int getCurrentNo() {
		return currentNo;
	}

	public void setCurrentNo(int currentNo) {
		this.currentNo = currentNo;
	}

	public List<NewsTitle> getContent() {
		return content;
	}

	public void setContent(List<NewsTitle> content) {
		this.content = content;
	}
	
	public void addNewsTitle(Article article, Integer index){
		NewsTitle newsTitle = new NewsTitle(article, index, article.getId());
		this.content.add(newsTitle);
	}

	class NewsTitle{
		public String title;
		public List<String> picUrl = new ArrayList<String>();
		public String summary;
		public Date time;
		public int clicks;
		public Integer indexId;
		public Long newsId;
		
		public NewsTitle(){
			
		}
		
		public NewsTitle(Article article, Integer indexId, Long newsId){
			this.title = article.getTitle();
			this.picUrl = article.getPicturesUrl();
			this.summary = article.getSummary();
			this.time = article.getTime();
			this.clicks = article.getClicks();
			this.indexId = indexId;
			this.newsId = newsId;
		}
	}
}
