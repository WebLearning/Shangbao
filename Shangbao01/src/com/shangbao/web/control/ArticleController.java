package com.shangbao.web.control;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.model.Article;
import com.shangbao.service.ArticleService;

@Controller
@RequestMapping("/article")
public class ArticleController {
	@Resource
	private ArticleService articleService;
	
	@RequestMapping(value="/articles", method=RequestMethod.GET)
	@ResponseBody
	public Map<Long, String> list(){
		return articleService.showTitles();
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Article findOne(@PathVariable Long id){
		Article article = articleService.findOne(id);
		return article;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public void saveOne(){
		
	}
}
