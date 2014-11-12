package com.shangbao.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection="article")
public class Article {
	@Id
	private long id;
	private String author;
	private String inroduction;
	private String content;
	private String title;
	private Date time;
	private String[] picturesUrl;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
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
	public String getInroduction() {
		return inroduction;
	}
	public void setInroduction(String inroduction) {
		this.inroduction = inroduction;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String[] getPicturesUrl() {
		return picturesUrl;
	}
	public void setPicturesUrl(String[] picturesUrl) {
		this.picturesUrl = picturesUrl;
	}
}
