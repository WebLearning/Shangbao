package com.shangbao.web.control;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.model.Article;
import com.shangbao.model.Page;
import com.shangbao.model.Title;
import com.shangbao.model.TitleList;
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
	
	@RequestMapping(value="/{pageID}/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Article findOne(@PathVariable Long id){
		Article article = articleService.findOne(id);
		return article;
	}
	
	@RequestMapping(method=RequestMethod.POST)
	public void saveOne(@RequestBody Article article){
		articleService.add(article);
	}
	
	@RequestMapping(value="/test", method=RequestMethod.GET)
	@ResponseBody
	public TitleList pageTest(){
		TitleList titleList = articleService.getTiltList(1);
//		for(Title title : titleList.getTileList()){
//			System.out.println(title.getIntroduction());
//		}
		return titleList;
	}
	
	@RequestMapping(value="/test1", method=RequestMethod.GET)
	@ResponseBody
	public Page<Article> test(){
		return articleService.getPage(1);
	}
	
}
