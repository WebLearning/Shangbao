package com.shangbao.web.control;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.core.io.support.PropertiesLoaderUtils;
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
	private ArticleService articleServiceImp;

	/**
	 * 新建文章
	 * 保存文章
	 * @param article
	 */
	@RequestMapping(value = "/newArticle", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void add(@RequestBody Article article) {
		article.setState(ArticleState.Temp);
		articleServiceImp.add(article);
		System.out.println("newArticle");
		System.out.println(article.getTitle());
	}

//	/**
//	 * 新建文章
//	 * 提交审核
//	 * @param article
//	 */
//	@RequestMapping(value = "/newArticle", method = RequestMethod.PUT)
//	@ResponseStatus(HttpStatus.OK)
//	public void addPending(@RequestBody Article article){
//		article.setState(ArticleState.Pending);
//		articleServiceImp.add(article);
//	}
	
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
		TitleList titleList = articleServiceImp.getTiltList(articleState, pageId);
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
	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}", method = RequestMethod.GET)
	@ResponseBody
	public TitleList order(@PathVariable ArticleState articleState,
			@PathVariable int pageNo, @PathVariable String order, @PathVariable String direction) {
		TitleList titleList = articleServiceImp.getOrderedList(articleState,
				pageNo, order, direction);
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
		Article article = articleServiceImp.findOne(id);
		return article;
	}
	
	/**
	 * 修改一篇文章
	 * 
	 * @param state
	 *            只有暂存，爬虫，撤销的文章能够修改
	 * @param id
	 * @param article
	 */
	@RequestMapping(value = "/{articleState}/{pageNo}/{id:[\\d]+}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void modifyOne(@PathVariable("articleState") ArticleState state,
			@PathVariable("id") Long id, @RequestBody Article article) {
		if (state.equals(ArticleState.Crawler)
				|| state.equals(ArticleState.Revocation)
				|| state.equals(ArticleState.Temp)) {
			article.setId(id);
			articleServiceImp.update(article);
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
	@RequestMapping(value = "/{articleState}/{pageNo}/statechange/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
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
		articleServiceImp.setPutState(articleState, idList);
		return articleServiceImp.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslatePut(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, @PathVariable("ids") String id,
			@PathVariable("direction") String direction) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		articleServiceImp.setPutState(articleState, idList);
		return articleServiceImp.getOrderedList(articleState, pageNo, order, direction);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/statechange/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
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
		articleServiceImp.setDeleteState(articleState, idList);
		return articleServiceImp.getTiltList(articleState, pageNo);
	}

	@RequestMapping(value = "/{articleState}/{pageNo}/{order:[a-z,A-Z]+}/{direction:asc|desc}/{ids:[\\d]+(?:_[\\d]+)*}", method = RequestMethod.DELETE)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public TitleList stateTranslateDelete(
			@PathVariable("articleState") ArticleState articleState,
			@PathVariable("pageNo") int pageNo,
			@PathVariable("order") String order, @PathVariable("ids") String id,
			@PathVariable("direction") String direction) {
		String[] idsString = id.split("_");
		List<Long> idList = new ArrayList<Long>();
		for (String idString : idsString) {
			idList.add(Long.parseLong(idString));
		}
		articleServiceImp.setDeleteState(articleState, idList);
		return articleServiceImp.getOrderedList(articleState, pageNo, order, direction);
	}

	/**
	 * 上传文件
	 * @param file
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadPicture(@RequestParam(value = "file", required = true) MultipartFile file) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String returnString = "";
		String fileName = sdf.format(new Date()) + file.getSize() + file.getOriginalFilename();//保存到本地的文件名
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
			String filePath = props.getProperty("pictureDir") + "\\articlePic";//目录的路径
			Path path = Paths.get(filePath);
			if(Files.notExists(path)){
				Path filPath = Files.createDirectories(path);
			}
			if(!file.isEmpty()){
				byte[] bytes;
				bytes = file.getBytes();
				FileOutputStream fos = new FileOutputStream(filePath + "\\" + fileName);
				fos.write(bytes); // 写入文件
				fos.close();
				returnString = path.toString().split("Shangbao01")[1] + "\\" + fileName;
				System.out.println(returnString);
				return returnString.replaceAll("\\\\", "/");
			}
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("upload done!");
		return null;
	}
	
	@RequestMapping(value ="/upload", method = RequestMethod.GET)
	public String uploadPage(){
		return "upload";
	}
}
