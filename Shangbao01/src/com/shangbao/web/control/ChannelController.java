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
import com.shangbao.service.StartPicturesService;

@Controller
@RequestMapping("/channel")
public class ChannelController {
	@Resource
	private ChannelService channelServiceImp;
	
	@Resource
	private StartPicturesService startPicturesServiceImp;
	
	public ChannelService getChannelServiceImp() {
		return channelServiceImp;
	}

	public void setChannelServiceImp(ChannelService channelServiceImp) {
		this.channelServiceImp = channelServiceImp;
	}

	public StartPicturesService getStartPicturesServiceImp() {
		return startPicturesServiceImp;
	}

	public void setStartPicturesServiceImp(
			StartPicturesService startPicturesServiceImp) {
		this.startPicturesServiceImp = startPicturesServiceImp;
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
	
	@RequestMapping(value="/activities")
	@ResponseBody
	public List<Channel> getActivities(){
		return channelServiceImp.findAllActivities();
	}
	
	/**
	 * 添加一个分类
	 */
	@RequestMapping(value="/{channelState:father|son|activity}", method=RequestMethod.POST)
	@ResponseBody
	public String addChannel(@RequestBody Channel channel, @PathVariable("channelState") ChannelState state){
		channel.setState(state);
		return this.channelServiceImp.addChannel(channel);
	}
	
	
	
	/**
	 * 删除一个分类
	 */
	@RequestMapping(value="/{channelState:father|son|activity}", method=RequestMethod.DELETE)
	@ResponseBody
	public String deleteActivity(@RequestBody Channel channel, @PathVariable("channelState") ChannelState state){
		channel.setState(state);
		return this.channelServiceImp.deleteChannel(channel);
	}
	
	@RequestMapping(value="/{father}/{channelState:father|son|activity}/swap/{indexA}/{indexB}")
	public String swapChannels(@PathVariable("father") String fatherName,
								@PathVariable("state") ChannelState state,
								@PathVariable("indexA") int indexA,
								@PathVariable("indexB") int indexB){
		Channel channelA = new Channel();
		Channel channelB = new Channel();
		if(fatherName.equals("top")){
			channelA.setState(ChannelState.Father);
			channelB.setState(ChannelState.Father);
			channelA.setChannelIndex(indexA);
			channelB.setChannelIndex(indexB);
			channelServiceImp.swapChannel(channelA, channelB);
			return "done";
		}else{
			Channel fatherChannel = channelServiceImp.findByEnName(fatherName, ChannelState.Father);
			if(fatherChannel != null){
				channelA.setState(state);
				channelB.setState(state);
				channelA.setRelated(fatherChannel.getChannelName());
				channelB.setRelated(fatherChannel.getChannelName());
				channelA.setChannelIndex(indexA);
				channelB.setChannelIndex(indexB);
				channelServiceImp.swapChannel(channelA, channelB);
				return "done";
			}
		}
		return "failed";
	}
}
