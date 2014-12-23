package com.shangbao.dao;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Channel;

public class ChannelDaoTest {

	@Test
	public void test() {
		addChannel();
	}
	
	public void addChannel(){
		BeanFactory applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		ChannelDao dao = (ChannelDao)applicationContext .getBean("channelDaoImp");
		Channel channel = new Channel();
		channel.setChannelName("FatherChannel");
		channel.setState(ChannelState.Father);
		channel.setSummary("Channel Summary");
		dao.insert(channel);
		Channel sonChannel = new Channel();
		sonChannel.setChannelName("SonChannel");
		sonChannel.setRelated("FatherChannel");
		sonChannel.setState(ChannelState.Son);
		dao.insert(sonChannel);
		Channel activity = new Channel();
		activity.setChannelName("活动一");
		activity.setState(ChannelState.Activity);
		activity.setRelated("SonChannel");
		dao.insert(activity);
	}

}
