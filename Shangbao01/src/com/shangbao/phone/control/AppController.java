package com.shangbao.phone.control;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.shangbao.app.model.ActiveModel;
import com.shangbao.app.model.AppChannelModel;
import com.shangbao.app.model.AppPictureModel;
import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.app.model.CommentModel;
import com.shangbao.app.model.CommentPageModel;
import com.shangbao.app.model.FrontPageModel;
import com.shangbao.app.model.OriginalPageModel;
import com.shangbao.model.persistence.Article;

@Controller
@RequestMapping("/app")
public class AppController {
	
	@Resource
	private Producer captchaProducer;

	/**
	 * 获取宣传图片，首页信息
	 */
	@RequestMapping(value="/{phoneType}/start", method=RequestMethod.GET)
	@ResponseBody
	public FrontPageModel getStartPage(){
		System.out.println("Start");
		FrontPageModel frontPageModel = new FrontPageModel();
		frontPageModel.addChannel("商报原创", "/{phoneType}/original/1");
		frontPageModel.addChannel("最新资讯", "/{phoneType}/newest");
		frontPageModel.addChannel("本地报告", "/{phoneType}/local");
		frontPageModel.addChannel("快拍成都", "/{phoneType}/kuaipai");
		frontPageModel.addPictureUrl("/WEB-SRC/picture/1.jpg");
		return frontPageModel;
	}
	
	/**
	 * 获取商报原创
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/original/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public OriginalPageModel getOriginal(@PathVariable("pageNo") int pageNo){
		System.out.println("Original");
		OriginalPageModel origPageModel = new OriginalPageModel();
		origPageModel.setCurrentNo(1);
		origPageModel.setPageCount(2);
		for(int i = 0; i < 5; i ++){
			origPageModel.addTitle("商报原创新闻标题(测试)", "/WEB-SRC/picture/1.jpg", new Long(i));
		}
		return origPageModel;
	}
	
	/**
	 * 获取最新资讯，本地报告，快拍成都等
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{channelName:[a-z,A-Z]+}", method=RequestMethod.GET)
	@ResponseBody
	public AppChannelModel getChannelContent(@PathVariable("channelName") String channelName){
		System.out.println(channelName);
		AppChannelModel appChannelModel = new AppChannelModel();
		List<Article> articles = new ArrayList<Article>();
		List<String> picUrls = new ArrayList<String>();
		picUrls.add("/WEB-SRC/picture/1.jpg");
		for(int i = 0; i < 3; i ++){
			Article article = new Article();
			article.setId(i);
			article.setTitle("测试Title" + i);
			article.setSummary("测试Summary" + i);
			article.setPicturesUrl(picUrls);
			articles.add(article);
		}
		appChannelModel.addColumn("分栏一", articles);
		appChannelModel.addColumn("分栏二", articles);
		return appChannelModel;
	}
	
	/**
	 * 获取新闻详细页面
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}")
	@ResponseBody
	public String getNews(@PathVariable("newsId") Long newsId){
		System.out.println("news");
		return  "<html><head><title>Test Html</title></head><body><p>Test HTML</p><img src=\"../../WEB-SRC/picture/1.jpg\"></body></html>";
	}
	
	/**
	 * 获取新闻评论
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}/comment/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public CommentPageModel getComment(@PathVariable("newsId") Long newsId, @PathVariable("pageNo") int pageNo){
		CommentPageModel commentPageModel = new CommentPageModel();
		commentPageModel.setCurrentNo(pageNo);
		commentPageModel.setPageCount(3);
		for(int i = 0; i < 5; i ++){
			commentPageModel.addComment(new Long(i), "TestUser", new Long(11), new Date(), "TestLevel", "TestFrom", "TestContent", "TestReply");
		}
		return commentPageModel;
	}
	
	/**
	 * 发表评论
	 */
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}/comment", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void sendComment(@RequestBody CommentModel comment){
		
	}
	
	/**
	 * 点赞
	 */
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}/like", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void sendLike(){
		
	}
	
	/**
	 * 分栏详细页面
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{channelName}/{columnName}/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public ColumnPageModel getColumnDetail(@PathVariable("channelName") String channelName, @PathVariable("pageNo") int pageNo){
		ColumnPageModel columnPageModel = new ColumnPageModel();
		columnPageModel.setCurrentNo(pageNo);
		columnPageModel.setPageCount(3);
		List<String> urls = new ArrayList<String>();
		urls.add("/WEB-SRC/picture/1.jpg");
		urls.add("/WEB-SRC/picture/2.jpg");
		for(int i = 0; i < 5; i ++){
			columnPageModel.addNewsTitle("TestTitle" + i, urls, "TestSummary", new Date(), i, new Long(i));
		}
		return columnPageModel;
	}
	
	/**
	 * 获取活动信息
	 * @return
	 */
	@RequestMapping(value="{phoneType}/kuaipai/activity", method=RequestMethod.GET)
	@ResponseBody
	public ActiveModel getActives(){
		System.out.println("activity");
		ActiveModel activeModel = new ActiveModel();
		for(int i = 0; i < 3; i ++){
			activeModel.addActive("测试活动" + i, "测试活动描述" + i);
		}
		return activeModel;
	}
	
	/**
	 * 图片详细页面
	 */
	@RequestMapping(value="/{phoneType}/kuaipai/{newsId:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public AppPictureModel getPictureDetial(){
		System.out.println("pictureDetail");
		AppPictureModel appPictureModel = new AppPictureModel();
		List<String> urls = new ArrayList<String>();
		urls.add("/WEB-SRC/picture/1.jpg");
		urls.add("/WEB-SRC/picture/2.jpg");
		appPictureModel.setTitleString("图片测试");
		appPictureModel.setTime(new Date());
		appPictureModel.setPictureUrl(urls);
		appPictureModel.setActiveName("测试活动");
		appPictureModel.setAuthor("测试作者");
		appPictureModel.setContent("测试内容");
		return appPictureModel;
	}
	
	/**
	 * 上传用户图片
	 */
	@RequestMapping(value="/{phoneType}/kuaipai/", method=RequestMethod.POST)
	public void sendUserPicture(){
		
	}
	
	@RequestMapping(value = "/{phoneType}/kuaipai", method = RequestMethod.GET)
	public String initCaptcha() {
		return "kaptcha";
	}

	@RequestMapping(value = "/{phoneType}/kuaipai", method = RequestMethod.POST)
	@ResponseBody
	public String test(String verifyCode, HttpServletRequest request) {
		String code = (String) request.getSession().getAttribute(
				Constants.KAPTCHA_SESSION_KEY); // 获取生成的验证码
		System.out.println(verifyCode + "," + code);
		if (verifyCode.equals(code)) {
			System.out.println("验证通过 ");
		}
		return "sdfsdf";
	}

	/**
	 * 生成验证码的图片
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value="/Kaptcha")
	public void getPic(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		response.setHeader("Pragma", "no-cache");
		response.setContentType("image/jpeg");
		String capText = captchaProducer.createText();
		request.getSession().setAttribute(Constants.KAPTCHA_SESSION_KEY,
				capText);
		BufferedImage bi = captchaProducer.createImage(capText);

		ServletOutputStream out = response.getOutputStream();
		ImageIO.write(bi, "jpg", out);

		try { 
			out.flush();
		} finally {
			out.close();
		}
		//return (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
	}
}
