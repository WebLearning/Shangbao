package com.shangbao.web.control;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.model.show.TitleList;
import com.shangbao.service.ArticleService;

@Controller
@RequestMapping("/crowlerArticle")
public class CrowlerArticleControl {
	@Resource
	private ArticleService articleService;
	
	@RequestMapping(value="/{pageId:[/d]+}", method=RequestMethod.GET)
	@ResponseBody
	public TitleList crowlerArticleList(@PathVariable int pageId){
		return new TitleList();
	}
}
