package com.shangbao.model;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="article")
public class Article {
	private String author;
	private String context;
	private String title;
	private Date time;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
}
