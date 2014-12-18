package com.shangbao.model.persistence;

import org.springframework.data.mongodb.core.mapping.Document;

import com.shangbao.model.ChannelState;

@Document(collection="channel")
public class Channel {
	private String channelName;//栏目名称
	private String summary;//栏目介绍
	private ChannelState state;//栏目状态
	private String related;//相关栏目(父栏目，子栏目)
	
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public ChannelState getState() {
		return state;
	}
	public void setState(ChannelState state) {
		this.state = state;
	}
	public String getRelated() {
		return related;
	}
	public void setRelated(String related) {
		this.related = related;
	}
	
}
