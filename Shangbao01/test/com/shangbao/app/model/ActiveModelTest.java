package com.shangbao.app.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.shangbao.app.model.AppModel.ChannelModel;
import com.shangbao.model.persistence.Channel;

public class ActiveModelTest {

	@Test
	public void test() {
		appModeTest();
	}
	
	public void appModeTest(){
		AppModel model = new AppModel();
		Channel fatherChannel1 = new Channel();
		fatherChannel1.setChannelName("商报原创");
		fatherChannel1.setEnglishName("original");
		Channel faherChannel2 = new Channel();
		faherChannel2.setChannelName("快拍成都");
		faherChannel2.setEnglishName("kuaipai");
		List<Channel> sonChannels = new ArrayList<Channel>();
		sonChannels.add(new Channel());
		model.addTopChannel(fatherChannel1, sonChannels);
		model.addTopChannel(faherChannel2, sonChannels);
		for(ChannelModel testChannelModel : model.getChannelModels()){
			System.out.println(testChannelModel.fatherChannel.getChannelName());
		}
		Channel testChannel = new Channel();
		testChannel.setChannelName("商报原创");
		testChannel.setEnglishName("original");
		model.deleteTopChannel(testChannel);
		for(ChannelModel testChannelModel : model.getChannelModels()){
			System.out.println(testChannelModel.fatherChannel.getChannelName());
		}
	}

}
