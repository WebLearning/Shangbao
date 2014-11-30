package com.shangbao.web.control;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.Page;
import com.shangbao.model.show.Title;
import com.shangbao.model.show.TitleList;
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
	
	@RequestMapping(value="/{pageId}/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Article findOne(@PathVariable("id") Long id){
		Article article = articleService.findOne(id);
		return article;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public void saveOne(@RequestBody Article article){
		articleService.add(article);
	}
	
	@RequestMapping(value="/{pageId}", method=RequestMethod.GET)
	@ResponseBody
	public TitleList pageTest(@PathVariable int pageId){
		TitleList titleList = articleService.getTiltList(pageId);
		return titleList;
	}
	
	@RequestMapping(value="/newArticle", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void add(@RequestBody Article article){
		System.out.println("test");
		System.out.println(article.getTitle());
	}
	
//	@RequestMapping(value="/newArticle", method=RequestMethod.POST)
//	@ResponseStatus(HttpStatus.OK)
//	public void add(HttpServletRequest req){
//		System.out.println("test");
//		System.out.println(req.getParameter("article"));
//		
//	}
	
	@RequestMapping(value="/newArticle", method=RequestMethod.GET)
	public String test(){
		System.out.println("Get method");
		return "postTest";
	}
	
}
