package com.shangbao.phone.control;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.User;
import com.shangbao.model.show.Page;
import com.shangbao.service.ArticleService;
import com.shangbao.service.UserService;

@Controller
@RequestMapping("/appuser")
public class AppUserController {

	@Resource
	private UserService userServiceImp;
	@Resource
	private ArticleService articleServiceImp;
	@Resource
	private PasswordEncoder passwordEncoder;
	
	
	/**
	 * 获取当前用户信息
	 * @return
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value="/userinfo", method=RequestMethod.GET)
	@ResponseBody
	public User getUserInfo(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SecurityContext context = (SecurityContext)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		User user = (User)context.getAuthentication().getPrincipal();
		user.setPasswd("");
		return user;
	}

	/**
	 * 获得当前用户的文章
	 * @return
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value="/articles/{pageNo}", method=RequestMethod.GET)
	@ResponseBody
	public ColumnPageModel getUserArticle(@PathVariable("pageNo") int pageNo){
		ColumnPageModel columnPageModel = new ColumnPageModel();
		int pageSize = 5;
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SecurityContext context = (SecurityContext)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		if(context != null){
			User user = (User)context.getAuthentication().getPrincipal();
			Article article = new Article();
			article.setUid(user.getUid());
			List<Article> userArticles = articleServiceImp.find(article, Direction.DESC, "time");
			if(userArticles != null && !userArticles.isEmpty()){
				int pageCount = userArticles.size() / pageSize + 1;
				if(pageNo <= pageCount){
					columnPageModel.setCurrentNo(pageNo);
					columnPageModel.setPageCount(pageCount);
					int fromIndex = (pageNo - 1) * pageSize;
					int toIndex = pageNo * pageSize > userArticles.size() ?  userArticles.size() : pageNo * pageSize + 1;
					int index = 1;
					for(Article userArticle : userArticles.subList(fromIndex, toIndex)){
						columnPageModel.addNewsTitle(userArticle, index);
						index ++;
					}
				}
			}
		}
		return columnPageModel;
	}

	/**
	 * 用户收藏一篇文章
	 * @param articleId
	 */
	@RequestMapping(value="/collection/articles/{articleId}", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void collectArticle(@PathVariable("articleId") Long articleId){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user.getName() != null && user.getId() > 0){
			userServiceImp.collectArticle(user, articleId);
		}
	}
	
	/**
	 * 删除用户的一篇收藏文章
	 * @param articleId
	 */
	@RequestMapping(value="/collection/articles/{articleId}", method=RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	public void deleteArticles(@PathVariable("articleId") Long articleId){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user.getName() != null && user.getId() > 0){
			userServiceImp.deleteCollectionArticle(user, articleId);
		}
	}
	
	/**
	 * 获取用户收藏的文章
	 * @param pageNo
	 * @return
	 */
	@RequestMapping(value="/collection/articles/{pageNo}", method=RequestMethod.GET)
	@ResponseBody
	public ColumnPageModel getCollectionArticle(@PathVariable("pageNo") int pageNo){
		ColumnPageModel columnPageModel = new ColumnPageModel();
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user.getName() != null && user.getId() > 0){
			List<Article> articles = userServiceImp.findCollectArticle(user);
			Collections.reverse(articles);
			Page<Article> page = new Page<>(pageNo, 10, articles.size());
			columnPageModel.setCurrentNo(pageNo);
			columnPageModel.setPageCount(page.getTotalPage());
			int index = 1;
			for(Article article : articles.subList(page.getFirstResult(), page.getLastResult())){
				columnPageModel.addNewsTitle(article, index);
				index ++;
			}
		}
		return columnPageModel;
	}
	
	@RequestMapping(value="/update/passwd", method=RequestMethod.POST)
	@ResponseBody
	public boolean updatePasswd(@RequestBody Update update){
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(user != null && update.newPasswd != null && update.oldPasswd != null){
			String oldPasswd = passwordEncoder.encodePassword(update.oldPasswd, null);
			String newPasswd = passwordEncoder.encodePassword(update.newPasswd, null);
			return userServiceImp.updatePasswd(user, oldPasswd, newPasswd);
		}
		return false;
	}
	
	public UserService getUserServiceImp() {
		return userServiceImp;
	}


	public void setUserServiceImp(UserService userServiceImp) {
		this.userServiceImp = userServiceImp;
	}


	public ArticleService getArticleServiceImp() {
		return articleServiceImp;
	}


	public void setArticleServiceImp(ArticleService articleServiceImp) {
		this.articleServiceImp = articleServiceImp;
	}
	
	public class Update{
		public String oldPasswd;
		public String newPasswd;
	}
}
