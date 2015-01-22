package com.shangbao.phone.control;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.User;
import com.shangbao.service.ArticleService;
import com.shangbao.service.UserService;

@Controller
@RequestMapping("/appuser")
public class AppUserController {

	@Resource
	private UserService userServiceImp;
	@Resource
	private ArticleService articleServiceImp;
	
	
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
	
	
}