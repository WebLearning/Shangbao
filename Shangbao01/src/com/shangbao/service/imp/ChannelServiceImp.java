package com.shangbao.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
		Sort sort = new Sort(Direction.ASC, "channelIndex");
		return channelDaoImp.find(channel, sort);
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
		Sort sort = new Sort(Direction.ASC, "channelIndex");
		return channelDaoImp.find(channel, sort);
	}

	@Override
	public String addChannel(Channel channel) {
		if(channel.getState().equals(ChannelState.Son)){
			Channel relateChannel = new Channel();
			relateChannel.setState(ChannelState.Father);
			relateChannel.setChannelName(channel.getRelated());
			List<Channel> fChannels = this.channelDaoImp.find(relateChannel);
			if(fChannels.isEmpty()){//没有父分类
				return "no father channel";
			}else{
				Channel sonChannel = new Channel();
				sonChannel.setChannelName(channel.getChannelName());
				sonChannel.setRelated(channel.getRelated());
				sonChannel.setState(ChannelState.Son);
				if(!this.channelDaoImp.find(sonChannel).isEmpty()){
					return channel.getChannelName() + "is already exist";
				}else{
					channel.setChannelIndex(findSonCount(channel.getRelated()) + 1);
					this.channelDaoImp.insert(channel);
					return "OK";
				}
			}
		}else if(channel.getState().equals(ChannelState.Father)){
			Channel fatherChannel = new Channel();
			fatherChannel.setChannelName(channel.getChannelName());
			fatherChannel.setState(ChannelState.Father);
			if(this.channelDaoImp.find(fatherChannel).isEmpty()){
				channel.setChannelIndex(findFatherCount() + 1);
				this.channelDaoImp.insert(channel);
				return "OK";
			}
			return channel.getChannelName() + "is already exist";
		}else if(channel.getState().equals(ChannelState.Activity)){
			Channel activityChannel = new Channel();
			activityChannel.setChannelName(channel.getChannelName());
//			activityChannel.setChannelIndex(findActivityCount() + 1);
			activityChannel.setState(ChannelState.Activity);
			if(this.channelDaoImp.find(activityChannel).isEmpty()){
				channel.setChannelIndex(findActivityCount() + 1);
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
	
	private int findFatherCount(){
		int count = 0;
		List<Channel> fathers = findAllFatherChannels();
		if(fathers != null && !fathers.isEmpty())
			return fathers.size();
		return count;
	}
	
	private int findSonCount(String fatherName){
		int count = 0;
		Channel sonChannel = new Channel();
		sonChannel.setRelated(fatherName);
		sonChannel.setState(ChannelState.Son);
		List<Channel> sons = channelDaoImp.find(sonChannel);
		if(sons != null && !sons.isEmpty())
			return sons.size();
		return count;
	}
	
	private int findActivityCount(){
		int count = 0;
		Channel activityChannel = new Channel();
		activityChannel.setState(ChannelState.Activity);
		List<Channel> activities = channelDaoImp.find(activityChannel);
		if(activities != null && !activities.isEmpty())
			return activities.size();
		return count;
	}
}
