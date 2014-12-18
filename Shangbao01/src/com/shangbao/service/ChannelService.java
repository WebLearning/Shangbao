package com.shangbao.service;

import java.util.List;

import com.shangbao.model.persistence.Channel;

public interface ChannelService {
	
	List<Channel> findAllFatherChannels();
	List<Channel> findAllSonChannels(String fatherChannelName);
	String addChannel(Channel channel);
	String deleteChannel(Channel channel);
}
