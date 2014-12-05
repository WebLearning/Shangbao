package com.shangbao.web.control;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.model.persistence.Commend;
import com.shangbao.model.show.CommendPage;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.CommendService;

@Controller
@RequestMapping("/commend")
public class CommendController {
	@Resource
	private CommendService commendServiceImp;
	
	@RequestMapping(value="/{pageId}", method=RequestMethod.GET)
	@ResponseBody
	public CommendPage getPage(@PathVariable("pageId") long pageId){
		
		return null;
	}
	
	@RequestMapping(value="/{pageId}/{articleId}", method=RequestMethod.GET)
	@ResponseBody
	public Commend getCommends(@PathVariable("articleId") long articleId){
		return commendServiceImp.get(articleId);
	}
	
	@RequestMapping(value="/{pageId}/{articleId}", method=RequestMethod.POST)
	public void addCommend(@PathVariable("articleId") long articleId,
							@RequestBody SingleCommend singleCommend){
		commendServiceImp.add(articleId, singleCommend);
	}
	
	@RequestMapping(value="/{pageId}/{articleId}/", method=RequestMethod.PUT)
	public void reply(){
		
	}
	
	@RequestMapping(value="", method=RequestMethod.PUT)
	public void publish(){
		
	}
	
}
