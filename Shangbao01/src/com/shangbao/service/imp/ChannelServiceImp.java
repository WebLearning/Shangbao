package com.shangbao.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.shangbao.dao.ChannelDao;
import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Channel;
import com.shangbao.service.ChannelService;

@Service
public class ChannelServiceImp implements ChannelService{
	@Resource
	private ChannelDao channelDaoImp;

	public ChannelDao getChannelDaoImp() {
		return channelDaoImp;
	}

	public void setChannelDaoImp(ChannelDao channelDaoImp) {
		this.channelDaoImp = channelDaoImp;
	}

	@Override
	public List<Channel> findAllFatherChannels() {
		Channel channel = new Channel();
		channel.setState(ChannelState.Father);
		return channelDaoImp.find(channel);
	}

	@Override
	public List<Channel> findAllSonChannels(String fatherChannelName) {
		Channel channel = new Channel();
		Channel fatherChannel = new Channel();
		fatherChannel.setEnglishName(fatherChannelName);
		List<Channel> fathers = channelDaoImp.find(fatherChannel);
		if(fathers.isEmpty() || fathers == null)
			return null;
		String fatherCnName = fathers.get(0).getChannelName();
		channel.setRelated(fatherCnName);
		channel.setState(ChannelState.Son);
		return channelDaoImp.find(channel);
	}

	@Override
	public String addChannel(Channel channel) {
		if(channel.getState().equals(ChannelState.Son)){
			Channel relateChannel = new Channel();
			relateChannel.setState(ChannelState.Father);
			relateChannel.setChannelName(channel.getRelated());
			if(this.channelDaoImp.find(relateChannel).isEmpty()){//没有父分类
				return "no father channel";
			}else{
				Channel sonChannel = new Channel();
				sonChannel.setChannelName(channel.getChannelName());
				sonChannel.setRelated(channel.getRelated());
				sonChannel.setState(ChannelState.Son);
				if(!this.channelDaoImp.find(sonChannel).isEmpty()){
					return channel.getChannelName() + "is already exist";
				}else{
					this.channelDaoImp.insert(channel);
					return "OK";
				}
			}
		}else if(channel.getState().equals(ChannelState.Father)){
			Channel fatherChannel = new Channel();
			fatherChannel.setChannelName(channel.getChannelName());
			fatherChannel.setState(ChannelState.Father);
			if(this.channelDaoImp.find(fatherChannel).isEmpty()){
				this.channelDaoImp.insert(channel);
				return "OK";
			}
			return channel.getChannelName() + "is already exist";
		}else if(channel.getState().equals(ChannelState.Activity)){
			Channel activityChannel = new Channel();
			activityChannel.setChannelName(channel.getChannelName());
			activityChannel.setState(ChannelState.Activity);
			if(this.channelDaoImp.find(activityChannel).isEmpty()){
				this.channelDaoImp.insert(channel);
				return "OK";
			}
		}
		return "error";
	}

	@Override
	public String deleteChannel(Channel channel) {
		if(channel.getState().equals(ChannelState.Father)){
			Channel fatherChannel = new Channel();
			fatherChannel.setState(ChannelState.Son);
			fatherChannel.setRelated(channel.getChannelName());
			this.channelDaoImp.delete(fatherChannel);
			this.channelDaoImp.delete(channel);
			return "OK";
		}else if(channel.getState().equals(ChannelState.Son)){
			this.channelDaoImp.delete(channel);
			return "OK";
		}else if(channel.getState().equals(ChannelState.Activity)){
			this.channelDaoImp.delete(channel);
			return "OK";
		}
		return "error";
	}
}
