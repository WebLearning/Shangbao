package com.shangbao.model.persistence;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.shangbao.model.ArticleState;

@Document(collection="article")
public class Article {
	@Id
	private long id;
	private String author;//作者
	private String summary;//摘要
	private String content;//内容
	private String title;//标题
	private Date time;//时间
	private String titlePicUrl;//标题图片url
	private List<String> picturesUrl;//图片url
	private ArticleState state;//状态
	private Map<String, Integer> channelMap;//所属栏目
	private String level;//等级
	private int words;//字数
	private int newsCommends;//商报评论数
	private int newsCommendsPublish;//商报评论发表数
	private int crawlerCommends;//爬虫评论数
	private int crawlerCommendsPublish;//爬虫评论发表数
	private int clicks;//点击数
	private String from;//来源
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
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
	public String getTitlePicUrl() {
		return titlePicUrl;
	}
	public void setTitlePicUrl(String titlePicUrl) {
		this.titlePicUrl = titlePicUrl;
	}
	public List<String> getPicturesUrl() {
		return picturesUrl;
	}
	public void setPicturesUrl(List<String> picturesUrl) {
		this.picturesUrl = picturesUrl;
	}
	public ArticleState getState() {
		return state;
	}
	public void setState(ArticleState state) {
		this.state = state;
	}
	public Map<String, Integer> getChannelMap() {
		return channelMap;
	}
	public void setChannelMap(Map<String, Integer> channelMap) {
		this.channelMap = channelMap;
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
	public int getNewsCommends() {
		return newsCommends;
	}
	public void setNewsCommends(int newsCommends) {
		this.newsCommends = newsCommends;
	}
	public int getNewsCommendsPublish() {
		return newsCommendsPublish;
	}
	public void setNewsCommendsPublish(int newsCommendsPublish) {
		this.newsCommendsPublish = newsCommendsPublish;
	}
	public int getCrawlerCommends() {
		return crawlerCommends;
	}
	public void setCrawlerCommends(int crawlerCommends) {
		this.crawlerCommends = crawlerCommends;
	}
	public int getCrawlerCommendsPublish() {
		return crawlerCommendsPublish;
	}
	public void setCrawlerCommendsPublish(int crawlerCommendsPublish) {
		this.crawlerCommendsPublish = crawlerCommendsPublish;
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
	
	
	public void addPicture(String url){
		this.picturesUrl.add(url);
	}
	public void deletePicture(String url){
		if(this.picturesUrl.contains(url)){
			this.picturesUrl.remove(this.picturesUrl.indexOf(url));
		}
	}
	
	public void addChannel(String channelName, int channelId){
		this.channelMap.put(channelName, channelId);
	}
	public void deleteChannel(String channelName){
		if(this.channelMap.containsKey(channelName)){
			this.channelMap.remove(channelName);
		}
	}
}
