package com.shangbao.web.control;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.TitleList;
import com.shangbao.service.PictureService;

@Controller
@RequestMapping("/picture")
public class PictureController {
	@Resource
	private PictureService pictureServiceImp;
	
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
	@RequestMapping(value="/newPicture", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void addPicturePending(@RequestBody Article article){
		article.setState(ArticleState.Pending);//状态设置为待审
		article.setTag(true);//设置为图片新闻
		this.pictureServiceImp.add(article);
	}
	
	/**
	 * 获得图片标题列表
	 * @param articleState
	 * @param pageId
	 * @return
	 */
	@RequestMapping(value="/{articleState}/{pageId}", method=RequestMethod.GET)
	public TitleList getTitleList(@PathVariable ArticleState articleState,
			@PathVariable int pageId){
		
		return null;
	}
	
	/**
	 * 获得排序后的标题列表
	 * @param articleState
	 * @param pageNo
	 * @param order
	 * @return
	 */
	@RequestMapping(value="/{articleState}/{pageId}/{order:[a-z,A-Z]+}", method=RequestMethod.GET)
	public TitleList getOrderedTitleList(@PathVariable ArticleState articleState,
			@PathVariable int pageNo, @PathVariable String order){
		return null;
	}
	
	/**
	 * 状态转移：转移到上一个状态
	 * @return
	 */
	@RequestMapping(value="/{articleState}/{pageId}/{}")
	public TitleList stateChangeUp(){
		
		return null;
	}
	
	/**
	 * 状态转移：转移到下一个状态
	 * @return
	 */
	@RequestMapping(value="/{articleState}/{pageId}/{}")
	public TitleList stateChangeDown(){
		
		return null;
	}
	
	/**
	 * 显示活动
	 */
	@RequestMapping(value="/activity", method=RequestMethod.GET)
	public void showActivity(){
		
	}
	
	/**
	 * 新建活动
	 */
	public void addActivity(){
		
	}
	
	/**
	 * 删除活动
	 */
	public void deleteActivity(){
		
	}
}
