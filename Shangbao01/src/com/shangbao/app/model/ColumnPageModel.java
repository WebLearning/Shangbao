package com.shangbao.app.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
	
	public void addNewsTitle(String title, List<String> pictureUrl, String summary, Date time, int clicks, Long newsId){
		NewsTitle newsTitle = new NewsTitle(title, pictureUrl, summary, time, clicks, newsId);
		this.content.add(newsTitle);
	}

	class NewsTitle{
		public String title;
		public List<String> pictureUrl = new ArrayList<String>();
		public String summary;
		public Date time;
		public int clicks;
		public Long newsId;
		
		public NewsTitle(){
			
		}
		public NewsTitle(String title, List<String> pictureUrl, String summary, Date time, int clicks, Long newsId){
			this.title = title;
			this.pictureUrl = pictureUrl;
			this.summary = summary;
			this.time = time;
			this.clicks = clicks;
			this.newsId = newsId;
		}
	}
}
