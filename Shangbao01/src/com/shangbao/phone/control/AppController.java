package com.shangbao.phone.control;

import java.awt.image.BufferedImage;
import java.io.File;
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
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.shangbao.app.model.ActiveModel;
import com.shangbao.app.model.AppChannelModel;
import com.shangbao.app.model.AppModel;
import com.shangbao.app.model.AppPictureModel;
import com.shangbao.app.model.ColumnPageModel;
import com.shangbao.app.model.CommentModel;
import com.shangbao.app.model.CommentPageModel;
import com.shangbao.app.model.FrontPageModel;
import com.shangbao.app.model.OriginalPageModel;
import com.shangbao.app.service.AppService;
import com.shangbao.app.service.AppService.AppHtml;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.User;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.utils.CompressPicUtils;

/**
 * 手机app获取数据的Controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/app")
public class AppController {
	
	@Resource
	private Producer captchaProducer;//用于生产验证码
	@Resource
	private AppService appService;
	@Resource
	private CompressPicUtils compressPicUtils;
	
	@RequestMapping(value="/addUser", method=RequestMethod.POST)
	@ResponseBody
	public String testPost(@RequestParam("phone") String phone, @RequestParam("psw") String psw){
		System.out.println(phone);
		return "get it";
	}
	
	/**
	 * 获取宣传图片，首页信息
	 */
	@RequestMapping(value="/{phoneType}/start", method=RequestMethod.GET)
	@ResponseBody
	public FrontPageModel getStartPage(@PathVariable("phoneType") String phoneType){
		
		return appService.getChannels(phoneType);
	}
	
	/**
	 * 获取商报原创
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{channelname}/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public ColumnPageModel getOriginal(@PathVariable("pageNo") int pageNo, @PathVariable("channelname") String channelname){
		return appService.getArticlesFromChannel(channelname, pageNo, 10);
	}
	
	/**
	 * 获取最新资讯，本地报告，快拍成都等
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{channelName:[a-z,A-Z]+}", method=RequestMethod.GET)
	@ResponseBody
	public AppChannelModel getChannelContent(@PathVariable("channelName") String channelName){
		if(channelName.equals("kuaipai")){
			return appService.getChannelModel("kuaipai", 1);
		}
		return appService.getChannelModel(channelName, 10);
	}
	
	/**
	 * 分栏详细页面
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{channelName:[a-z,A-Z]+}/{columnName:[a-z,A-Z]+}/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public ColumnPageModel getColumnDetail(@PathVariable("columnName") String channelName, @PathVariable("pageNo") int pageNo){
		return appService.getArticlesFromChannel(channelName, pageNo, 10);
	}
	
	@RequestMapping(value="/{phoneType}/query")
	public ColumnPageModel getFuzzyFind(@RequestBody Article article){
		
		return null;
	}
	
	/**
	 * 获取新闻详细页面
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/articledetail/{articleId:[\\d]+}", method=RequestMethod.GET, produces={"text/html;charset=UTF-8"})
	@ResponseBody
	public String getNews(@PathVariable("articleId") long articleId){
		return appService.getNewsHtml(articleId).html;
	}
	
	@RequestMapping(value="/js/addclick/{articleId}", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public int addClicks(@PathVariable("articleId") long articleId){
		return appService.addJsClick(articleId);
	}
	
	/**
	 * 获取新闻评论
	 * @return
	 */
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}/comment/{pageNo:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public CommentPageModel getComment(@PathVariable("newsId") Long newsId, @PathVariable("pageNo") int pageNo){
		CommentPageModel commentPageModel = new CommentPageModel();
		List<SingleCommend> singleCommends = appService.getCommentByArticleId(newsId);
		if(singleCommends == null || singleCommends.isEmpty()){
			return commentPageModel;
		}
		int pageCount = singleCommends.size() / 10 + 1;
		commentPageModel.setPageCount(pageCount);
		if(pageNo > pageCount)
			return null;
		commentPageModel.setCurrentNo(pageNo);
		int fromIndex = (pageNo - 1) * 10;
		int toIndex = pageNo * 10 > singleCommends.size() ? singleCommends.size() : pageNo * 10;
		commentPageModel.addComment(singleCommends.subList(fromIndex, toIndex));
		return commentPageModel;
	}
	
	/**
	 * 发表评论
	 */
	//@Secured("ROLE_USER")
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}/comment", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.CREATED)
	public void sendComment(@PathVariable("newsId") Long articleId, @RequestBody SingleCommend comment){
		if(comment.getContent() != null){
			User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(user != null){
				comment.setUserId(user.getId());
				comment.setUserName(user.getName());
			}
			appService.addComment(articleId, comment);
		}
	}
	
	/**
	 * 点赞
	 */
	@RequestMapping(value="/{phoneType}/{newsId:[\\d]+}/like", method=RequestMethod.PUT)
	@ResponseStatus(HttpStatus.OK)
	public void sendLike(@PathVariable("newsId") Long articleId){
		appService.sendLike(articleId);
	}
	
	/**
	 * 获取活动信息
	 * @return
	 */
	@RequestMapping(value="{phoneType}/kuaipai/activity", method=RequestMethod.GET)
	@ResponseBody
	public ActiveModel getActives(){
		return appService.getActives();
	}
	
	/**
	 * 图片详细页面
	 */
	@RequestMapping(value="/{phoneType}/kuaipai/{channelName:[a-z,A-Z]+}/{pageNo:[\\d]+}/{articleIndex:[\\d]+}", method=RequestMethod.GET)
	@ResponseBody
	public AppPictureModel getPictureDetial(@PathVariable("channelName") String channelName,
											@PathVariable("articleIndex") int articleIndex){
		return appService.getPictureDetails(channelName, articleIndex);
	}
	
	/**
	 * 上传用户图片
	 */
	@Secured("ROLE_USER")
	@RequestMapping(value="/sendarticle", method=RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String sendUserPicture(@RequestBody Article article){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		SecurityContext context = (SecurityContext)request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
		if(context != null){
			User user = (User)context.getAuthentication().getPrincipal();
			article.setUid(user.getUid());
			article.setAuthor(user.getName());
			List<String> logs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
			logs.add(sdf.format(new Date()) + "" + user.getUid() + " " + user.getName() + " 创建");
			article.setLogs(logs);
		}
		article.setTime(new Date());
		appService.postPictures(article);
		return "done";
	}
	
	@Secured("ROLE_USER")
	@RequestMapping(value = "/{userId:[\\d]+}/uploadpic", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public String uploadPicture(@RequestParam(value = "file", required = true) MultipartFile file, @PathVariable("userId") long userId) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmm");
		String returnPath = "";
		System.out.println("upload done!");
		if (!file.isEmpty()) {
			byte[] bytes;
			String fileName = sdf.format(new Date()) + file.getSize();
			String localhostString = "";
			try {
				bytes = file.getBytes();
				Properties props = new Properties();
				props=PropertiesLoaderUtils.loadAllProperties("config.properties");
				
				String fileURL = props.getProperty("pictureDir") + File.separator +  "userPic" + File.separator
						+ userId;
				localhostString = props.getProperty("localhost");
				String fileNameString = fileName + file.getOriginalFilename();
				String fileUrlSim = fileURL + File.separator + "sim";
				Path path = Paths.get(fileURL);
				if(Files.notExists(path)){
					Path filePath = Files.createDirectories(path);
				}
				Path pathSim = Paths.get(fileUrlSim);
				if(Files.notExists(pathSim)){
					Files.createDirectories(pathSim);
				}
				FileOutputStream fos = new FileOutputStream(fileURL + File.separator + fileNameString);
				fos.write(bytes); // 写入文件
				fos.close();
				compressPicUtils.compressByThumbnailator(new File(fileURL + File.separator + fileNameString), new File(fileUrlSim + File.separator + fileNameString), 800, 0, 0.5, true);
				//returnPath = path.toString().split("Shangbao01")[1] + File.separator + fileNameString;
				returnPath = path.toString().split("Shangbao01")[1] + File.separator + "sim" + File.separator + fileNameString;
				System.out.println(returnPath);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return localhostString + returnPath.replaceAll("\\\\", "/");
		}
		return null;
	}
	
	@RequestMapping(value = "/{phoneType}/KaptchaTest", method = RequestMethod.GET)
	public String initCaptcha() {
		return "kaptcha";
	}

//	@RequestMapping(value = "/{phoneType}/kuaipai", method = RequestMethod.POST)
//	@ResponseBody
//	public String test(String verifyCode, HttpServletRequest request) {
//		String code = (String) request.getSession().getAttribute(
//				Constants.KAPTCHA_SESSION_KEY); // 获取生成的验证码
//		System.out.println(verifyCode + "," + code);
//		if (verifyCode.equals(code)) {
//			System.out.println("验证通过 ");
//		}
//		return "sdfsdf";
//	}

	
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
