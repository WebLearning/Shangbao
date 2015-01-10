import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
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
		
		String identifyUrl = "http://user.itanzi.com/index.php/wap/api/v1/userMatch/一梦醉千年/330810852/2";
		String model = restTemplate.getForObject(identifyUrl, String.class);
		
	}

}
