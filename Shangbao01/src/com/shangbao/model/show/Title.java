package com.shangbao.model.show;

import java.util.Date;

import com.shangbao.model.persistence.Article;

public class Title {
	private String title;//标题
	private String author;//作者
	private String level;//等级
	private int words;//字数
	private int commends;//评论数
	private int clicks;//点击数
	private String from;//来源
	private String summary;//摘要
	private Date time;//时间
	private String titleUrl;//标题图片
	private long articleId;//文章ID
	
	public Title(){	
	}
	
	public Title(Article article){
		setTitle(article.getTitle());
		setAuthor(article.getAuthor());
		setLevel(article.getLevel());
		setWords(article.getWords());
		setCommends(article.getNewsCommends() + article.getNewsCommendsPublish()
				+ article.getCrawlerCommends() + article.getCrawlerCommendsPublish());
		setClicks(article.getClicks());
		setFrom(article.getFrom());
		setSummary(article.getSummary());
		setTime(article.getTime());
		setTitleUrl(article.getTitlePicUrl());
		setArticleId(article.getId());
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public int getWords() {
		return words;
	}

	public void setWords(int words) {
		this.words = words;
	}

	public int getCommends() {
		return commends;
	}

	public void setCommends(int commends) {
		this.commends = commends;
	}

	public int getClicks() {
		return clicks;
	}

	public void setClicks(int clicks) {
		this.clicks = clicks;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public String getTitleUrl() {
		return titleUrl;
	}

	public void setTitleUrl(String titleUrl) {
		this.titleUrl = titleUrl;
	}

	public long getArticleId() {
		return articleId;
	}

	public void setArticleId(long articleId) {
		this.articleId = articleId;
	}
	
}