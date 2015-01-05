package com.shangbao.web.control;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.service.ArticleService;
import com.shangbao.service.CommendService;

@Controller
@RequestMapping("/crawler")
public class CrawlerGetController {
	@Resource
	private ArticleService articleServiceImp;
	@Resource
	private CommendService commendServiceImp;
	
	@RequestMapping("/uploadArticle")
	@ResponseBody
	public Long uploadCrawlerArticle(@RequestBody Article article){
		if(article != null){
			article.setState(ArticleState.Crawler);
			return articleServiceImp.addGetId(article);
		}
		return null;
	}
	
	@RequestMapping("/uploadComment/{articleId:[\\d]+}")
	public void uploadCrawlerComment(@PathVariable("articleId") Long articleId, @RequestBody NewsCommend commend){
		commend.setArticleId(articleId);
		commendServiceImp.add(commend);
	}
	
	public ArticleService getArticleServiceImp() {
		return articleServiceImp;
	}
	public void setArticleServiceImp(ArticleService articleServiceImp) {
		this.articleServiceImp = articleServiceImp;
	}
	public CommendService getCommendServiceImp() {
		return commendServiceImp;
	}
	public void setCommendServiceImp(CommendService commendServiceImp) {
		this.commendServiceImp = commendServiceImp;
	}
}
