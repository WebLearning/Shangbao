package com.shangbao.web.control;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
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
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.persistence.CrawlerCommend;
import com.shangbao.model.persistence.NewsCommend;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.ArticleService;
import com.shangbao.service.CommendService;
import com.shangbao.utils.CompressPicUtils;

@Controller
@RequestMapping("/crawler")
public class CrawlerGetController {
	@Resource
	private ArticleService articleServiceImp;
	@Resource
	private CommendService commendServiceImp;
	@Resource
	private CompressPicUtils compressPicUtils;
	
	/**
	 * 接收上传的一篇爬虫新闻 返回新闻的id
	 * @param article
	 * @return
	 */
	@RequestMapping(value="/uploadArticle", method=RequestMethod.POST)
	@ResponseBody
	public Long uploadCrawlerArticle(@RequestBody Article article){
		if(article != null){
			article.setState(ArticleState.Crawler);
			//article.setContent(articleToHtml(article));
			article.setContent(stringToBody(article));
			return articleServiceImp.addGetId(article);
		}
		return null;
	}
	
	/**
	 * 接收爬虫新闻的评论
	 * @param articleId
	 * @param commend
	 * @return
	 */
	@RequestMapping(value="/uploadComment/{articleId:[\\d]+}", method=RequestMethod.POST)
	@ResponseBody
	public CrawlerCommend uploadCrawlerComment(@PathVariable("articleId") Long articleId, @RequestBody CrawlerCommend commend){
		commend.setArticleId(articleId);
		if(!commend.getCommendList().isEmpty() && commend.getCommendList().get(1).getCommendId() == null){
			int count = commend.getCommendList().size();
			for(int i = 0; i < count; i ++){
				commend.getCommendList().get(i).setCommendId(new SimpleDateFormat("yyyyMMddHHmm").format(new Date()) + "" + (int)(Math.random()*100) + "" + (int)(Math.random()*100));
			}
		}
		commendServiceImp.add(commend);
		return commend;
	}
	
	/**
	 * 接收爬虫新闻的图片
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/uploadpic", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadPicture(@RequestParam(value = "file", required = true) MultipartFile file) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String returnPath = "";
		System.out.println("upload done!");
		if (!file.isEmpty()) {
			byte[] bytes;
			String fileName = sdf.format(new Date()) + file.getSize();
			try {
				bytes = file.getBytes();
				Properties props = new Properties();
				props=PropertiesLoaderUtils.loadAllProperties("config.properties");
				returnPath = props.getProperty("localhost");
				String fileURL = props.getProperty("pictureDir") + File.separator + "crawlerPic";
				String fileURLSim = fileURL + File.separator + "sim";
				String fileNameString = fileName + file.getOriginalFilename();
				Path path = Paths.get(fileURL);
				if(Files.notExists(path)){
					Path filePath = Files.createDirectories(path);
				}
				Path pathSim = Paths.get(fileURLSim);
				if(Files.notExists(pathSim)){
					Files.createDirectories(pathSim);
				}
				FileOutputStream fos = new FileOutputStream(fileURL + File.separator + fileNameString);
				fos.write(bytes); // 写入文件
				fos.close();
				compressPicUtils.compressPic(new File(fileURL + File.separator + fileNameString), new File(fileURLSim + File.separator + fileNameString), 180, 120, true);
				returnPath = returnPath + path.toString().split("Shangbao01")[1] + File.separator + fileNameString;
				System.out.println(returnPath.replaceAll("\\\\", "/"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return returnPath.replaceAll("\\\\", "/");
		}
		return null;
	}
	
	/**
	 * 跟新一篇爬虫文章
	 * @param article
	 * @param articleId
	 * @return
	 */
	@RequestMapping(value = "/update/{articleId}", method=RequestMethod.POST)
	@ResponseBody
	public Long updateArticle(@RequestBody Article article, @PathVariable("articleId") Long articleId){
		article.setId(articleId);
		articleServiceImp.updateCrawler(article);
		return articleId;
	}
	
	/**
	 * 添加爬虫文章评论
	 * @param articleId
	 * @param singleCommends
	 * @return
	 */
	@RequestMapping(value = "/update/comment/{articleId}")
	public Long updateComment(@PathVariable("articleId") Long articleId, @RequestBody List<SingleCommend> singleCommends){
		Commend commend = new CrawlerCommend();
		commend.setArticleId(articleId);
		commendServiceImp.update(commend, singleCommends);
		return null;
	}
	
	private String stringToBody(Article article){
		String body = "";
		if(article.getSummary() != null && !article.getSummary().isEmpty()){
			body += "<p style=\"text-indent: 2em;\"><em>" + article.getSummary() + "</em></p>"; 
		}
		if(article.getContent() != null && !article.getContent().isEmpty()){
			List<String> pictureUrls = article.getPicturesUrl();
			if(pictureUrls != null && !pictureUrls.isEmpty()){
				body += "<p style=\"text-align: center;\"><img src=" + pictureUrls.get(0) + " width=\"330\" height=\"220\"></p>";
			}
			String [] parms = article.getContent().split("\n");
			for(String parm : parms){
				body += "<p style=\"text-indent: 2em;\">" + parm + "</p>";
			}
		}
		return body;
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
