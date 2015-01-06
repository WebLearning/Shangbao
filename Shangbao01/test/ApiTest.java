import org.junit.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.client.RestTemplate;


public class ApiTest {
	
	@Test
	public void test(){
		BeanFactory factory = new ClassPathXmlApplicationContext("applicationContext.xml");
		RestTemplate restTemplate = (RestTemplate)factory.getBean("restTemplate");
		
	}

}
