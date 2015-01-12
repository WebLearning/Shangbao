package com.shangbao.web.control;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Channel;
import com.shangbao.service.ChannelService;

@Controller
@RequestMapping("/channel")
public class ChannelController {
	@Resource
	private ChannelService channelServiceImp;
	
	public ChannelService getChannelServiceImp() {
		return channelServiceImp;
	}

	public void setChannelServiceImp(ChannelService channelServiceImp) {
		this.channelServiceImp = channelServiceImp;
	}

	/**
	 * 获取所有的顶级分类
	 * @return
	 */
	@RequestMapping(value="/channels", method=RequestMethod.GET)
	@ResponseBody
	public List<Channel> getFatherChannel(){
		return channelServiceImp.findAllFatherChannels();
	}
	
	/**
	 * 获取一个顶级分类下的所有子分类
	 * @param fatherChannelName
	 * @return
	 */
	@RequestMapping(value="/{fatherChannelName}/channels", method=RequestMethod.GET)
	@ResponseBody
	public List<Channel> getSonChannel(@PathVariable("fatherChannelName") String fatherChannelName){
		System.out.println(fatherChannelName);
		return channelServiceImp.findAllSonChannels(fatherChannelName);
	}
	
	/**
	 * 添加一个分类
	 */
	@RequestMapping(value="/{channelState:father|son|activity}", method=RequestMethod.POST)
	public String addChannel(@RequestBody Channel channel, @PathVariable("channelState") ChannelState state){
		channel.setState(state);
		return this.channelServiceImp.addChannel(channel);
	}
	
	
	
	/**
	 * 删除一个分类
	 */
	@RequestMapping(value="/{channelState:father|son|activity}", method=RequestMethod.DELETE)
	public String deleteActivity(@RequestBody Channel channel, @PathVariable("channelState") ChannelState state){
		channel.setState(state);
		return this.channelServiceImp.deleteChannel(channel);
	}
	
	public String swapChannels(){
		
		return "false";
	}
}
