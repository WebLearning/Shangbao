package com.shangbao.web.control;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shangbao.model.ArticleState;
import com.shangbao.model.ChannelState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.show.ChannelList;
import com.shangbao.model.show.TitleList;
import com.shangbao.service.DownLoadPicService;
import com.shangbao.service.PictureService;

@Controller
@RequestMapping("/picture")
public class PictureController {
	@Resource
	private PictureService pictureServiceImp;
	@Resource
	private DownLoadPicService downLoadPicServiceImp;
	
	/**
	 * 新建图片
	 * 保存新建的图片
	 * @param article
	 */
	@RequestMapping(value="/newPicture", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void addPicture(@RequestBody Article article){
		article.setState(ArticleState.Temp);//状态设置为暂存
		article.setTag(true);//设置为图片新闻
		this.pictureServiceImp.add(article);
	}
	
	/**
	 * 新建图片
	 * 提交审核图片
	 * @param article
	 */
//	@RequestMapping(value="/newPicture", method=RequestMethod.PUT)
//	@ResponseStatus(HttpStatus.OK)
//	public void addPicturePending(@RequestBody Article article){
//		article.setState(ArticleState.Pending);//状态设置为待审
//		article.setTag(true);//设置为图片新闻
//		this.pictureServiceImp.add(article);
//	}
	
	/**
	 * 获得图片标题列表
	 * @param articleState
	 * @param pageId
	 * @return
	 */
	@RequestMapping(value="/{articleState}/{pageNo}", method=RequestMethod.GET)
	@ResponseBody
	public TitleList getTitleList(@PathVariable ArticleState articleState,
			@PathVariable int pageNo){
		return this.pictureServiceImp.getTiltList(articleState, pageNo);
	}
	
	/**
	 * 获得排序后的标题列表
	 * @param articleState
	 * @param pageNo
	 * @param order
	 * @return
	 */
	@RequestMapping(value="/{articleState}/{pageId}/{order:[a-z,A-Z]+}/{direction:asc|desc}", method=RequestMethod.GET)
	@ResponseBody
	public TitleList getOrderedTitleList(@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageId") int pageNo, @PathVariable("order") String order, @PathVariable("direction") String direction){
		return this.pictureServiceImp.getOrderedList(articleState, pageNo, order, direction);
	}
	
	/**
	 * 获取一篇图片文章
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public Article findOne(@PathVariable("id") Long id) {
		Article article = pictureServiceImp.findOne(id);
		return article;
	}

	
	/**
	 * 修改一篇图片文章
	 * 
	 * @param state 只有暂存，已发布，撤销的文章能够修改
	 * @param id
	 * @param article
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void modifyOne(@PathVariable("articleState") ArticleState state,
			@PathVariable("id") Long id, @RequestBody Article article) {
		if (state.equals(ArticleState.Crawler)
				|| state.equals(ArticleState.Revocation)
				|| state.equals(ArticleState.Temp)) {
			article.setId(id);
			pictureServiceImp.update(article);
		}
	}
	
	/**
	 * 状态转换
	 * 
	 * @param articleState
	 * @param pageNo
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/statechange/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslatePut(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo, @PathVariable("ids") String id) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		pictureServiceImp.setPutState(articleState, idList);
		return pictureServiceImp.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslatePut(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, 
			@PathVariable("ids") String id,
			@PathVariable("direction") String direction) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		pictureServiceImp.setPutState(articleState, idList);
		return pictureServiceImp.getOrderedList(articleState, pageNo, order, direction);
	}
	
	/**
	 * 定时发布
	 * @param ids
	 * @param time
	 */
	@RequestMapping(value="/{articleState}/{pageNo}/timingpublish/{ids:[\\d]+(?:_[\\d]+)*}/{time:[\\d]+}")
	@ResponseStatus(HttpStatus.OK)
	public void timingPublish(@PathVariable("ids") String ids, @PathVariable("time") Long time){
		String[] idStrings = ids.split("_");
		List<Long> idList = new ArrayList<>();
		for(String id : idStrings){
			idList.add(Long.parseLong(id));
		}
		publishTask(idList, time);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/statechange/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslateDelete(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo, @PathVariable("ids") String id) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		pictureServiceImp.setDeleteState(articleState, idList);
		return pictureServiceImp.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslateDelete(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, 
			@PathVariable("ids") String id,
			@PathVariable("direction") String direction) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		pictureServiceImp.setDeleteState(articleState, idList);
		return pictureServiceImp.getOrderedList(articleState, pageNo, order, direction);
	}
	
	private void publishTask(final List<Long> idList, final Long date){
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				pictureServiceImp.setPutState(ArticleState.Pending, idList);
			}
		}, date);
	}
	
	/**
	 * 显示活动
	 */
	@RequestMapping(value="/activity/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public ChannelList showActivity(@PathVariable("pageNo") int pageNo){
		return this.pictureServiceImp.getActivity(pageNo, 20);
	}
	
	@RequestMapping(value="/activity/{pageNo:[\\d]+}/{order:[a-z,A-Z]+}", method=RequestMethod.GET)
	@ResponseBody
	public ChannelList showOderedActivity(@PathVariable("pageNo") int pageNo, @PathVariable("order") String order){
		return this.pictureServiceImp.getActivity(pageNo, 20);
	}
	
	/**
	 * 新建活动
	 */
	@RequestMapping(value="/activity", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void addActivity(@RequestBody Channel activity){
		activity.setState(ChannelState.Activity);
		pictureServiceImp.add(activity);
	}
	
	/**
	 * 删除活动
	 */
	@RequestMapping(value="/activity", method=RequestMethod.DELETE)
	public void deleteActivity(@RequestBody List<Channel> channels){
		pictureServiceImp.delete(channels);
	}

	@RequestMapping(value="/download", method=RequestMethod.GET)
	public void downloadPic(){
		downLoadPicServiceImp.saveAsArticle();
	}
	
	public PictureService getPictureServiceImp() {
		return pictureServiceImp;
	}

	public void setPictureServiceImp(PictureService pictureServiceImp) {
		this.pictureServiceImp = pictureServiceImp;
	}

	public DownLoadPicService getDownLoadPicServiceImp() {
		return downLoadPicServiceImp;
	}

	public void setDownLoadPicServiceImp(DownLoadPicService downLoadPicServiceImp) {
		this.downLoadPicServiceImp = downLoadPicServiceImp;
	}
}
