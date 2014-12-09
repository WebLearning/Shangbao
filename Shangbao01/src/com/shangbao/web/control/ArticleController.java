package com.shangbao.web.control;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.show.TitleList;
import com.shangbao.service.ArticleService;

@Controller
@RequestMapping("/article")
public class ArticleController {
	@Resource
	private ArticleService articleService;

	/**
	 * 新建文章
	 * 保存文章
	 * @param article
	 */
	@RequestMapping(value = "/newArticle", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void add(@RequestBody Article article) {
		article.setState(ArticleState.Temp);
		articleService.add(article);
		System.out.println("newArticle");
		System.out.println(article.getTitle());
	}

	/**
	 * 新建文章
	 * 提交审核
	 * @param article
	 */
	@RequestMapping(value = "/newArticle", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void addPending(@RequestBody Article article){
		article.setState(ArticleState.Pending);
		articleService.add(article);
	}
	
	/**
	 * 标题列表分页
	 * 
	 * @param articleState
	 * @param pageId
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageId}", method = RequestMethod.GET)
	@ResponseBody
	public TitleList pageTest(@PathVariable ArticleState articleState,
			@PathVariable int pageId) {
		TitleList titleList = articleService.getTiltList(articleState, pageId);
		return titleList;
	}

	/**
	 * 按照Order排序
	 * 
	 * @param articleState
	 * @param pageId
	 * @param order
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}", method = RequestMethod.GET)
	@ResponseBody
	public TitleList order(@PathVariable ArticleState articleState,
			@PathVariable int pageNo, @PathVariable String order) {
		TitleList titleList = articleService.getOrderedList(articleState,
				pageNo, order);
		return titleList;
	}

	/**
	 * 获取一篇文章
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+}", method = RequestMethod.GET)
	@ResponseBody
	public Article findOne(@PathVariable("id") Long id) {
		Article article = articleService.findOne(id);
		return article;
	}

	/**
	 * 修改一篇文章
	 * 
	 * @param state
	 *            只有暂存，已发布，撤销的文章能够修改
	 * @param id
	 * @param article
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+}", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void modifyOne(@PathVariable("articleState") ArticleState state,
			@PathVariable("id") Long id, @RequestBody Article article) {
		if (state.equals(ArticleState.Published)
				|| state.equals(ArticleState.Revocation)
				|| state.equals(ArticleState.Pending)) {
			articleService.update(article);
		}
	}

	/**
	 * 状态转换
	 * 
	 * @param articleState
	 * @param pageNo
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslatePut(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo, @PathVariable("ids") String id) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		articleService.setPutState(articleState, idList);
		return articleService.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslatePut(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, @PathVariable("ids") String id) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		articleService.setPutState(articleState, idList);
		return articleService.getOrderedList(articleState, pageNo, order);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslateDelete(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo, @PathVariable("ids") String id) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		articleService.setDeleteState(articleState, idList);
		return articleService.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslateDelete(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, @PathVariable("ids") String id) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		articleService.setDeleteState(articleState, idList);
		return articleService.getOrderedList(articleState, pageNo, order);
	}

	/**
	 * 上传文件
	 * @param file
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadPicture(@RequestParam("file") MultipartFile file) {
		if (!file.isEmpty()) {
			byte[] bytes;
			String fileName = new Date().toString() + file.getSize();
			try {
				bytes = file.getBytes();
				FileOutputStream fos = new FileOutputStream("/WEB-SRC/IMG/"
						+ fileName); // 上传到写死的上传路径
				fos.write(bytes); // 写入文件
				fos.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "/WEB-SRC/IMG/" + fileName;
		}
		return null;
	}
}
