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

import com.shangbao.dao.ArticleDao;
import com.shangbao.model.Article;

@Controller
@RequestMapping("/article")
public class ArticleController {
	@Resource
	private ArticleDao articleDao;

	public ArticleDao getArticleDao() {
		return articleDao;
	}

	public void setArticleDao(ArticleDao articleDao) {
		this.articleDao = articleDao;
	}
	
	@RequestMapping(value="/articles", method=RequestMethod.GET)
	@ResponseBody
	public Map<Long, String> list(){
		Map<Long, String> titleMap = new HashMap<Long, String>();
		List<Article> articles = articleDao.findAll();
		if(!articles.isEmpty()){
			for(Article article : articles){
				titleMap.put(article.getId(), article.getTitle());
			}
		}
		return titleMap;
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	@ResponseBody
	public Article findOne(@PathVariable Long id){
		Article article = articleDao.findById(id);
		return article;
	}
}
