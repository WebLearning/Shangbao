import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.codehaus.jackson.map.*;

import com.shangbao.model.persistence.Article;
import com.shangbao.remotemodel.ResponseModel;


public class RestTemplateTest {

	@Test
	public void test() {
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		RestTemplate restTemplate = (RestTemplate) factory.getBean(RestTemplate.class);
		
//		String identifyUrl = "http://user.itanzi.com/index.php/wap/api/v1/userMatch/一梦醉千年/330810852/2";
//		String model = restTemplate.getForObject(identifyUrl, String.class);
		
		String picUrl = "http://img.eva.chengdu.cn/upload/thumper/diary/259481/54b0d6118b3d5.jpg";
		String localPicDir = "";
		try {
			Properties props=PropertiesLoaderUtils.loadAllProperties("config.properties");
			if(!props.getProperty("pictureDir").isEmpty()){
				localPicDir = props.getProperty("pictureDir") + "picWeb/";
			}
			byte[] bytes;
			Path path = Paths.get(localPicDir);
			if(Files.notExists(path)){
				Path filPath = Files.createDirectories(path);
			}
			bytes = restTemplate.getForObject(picUrl, byte[].class);
			FileOutputStream fos = new FileOutputStream(localPicDir + "\\" + picUrl.substring(picUrl.lastIndexOf("/")));
			fos.write(bytes); 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
