package com.shangbao.app.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.map.HashedMap;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AppPushService {
	private RestTemplate restTemplate;
	private String url = "https://api.jpush.cn/v3/push";
	private String appKey;
	private String masterSecret;
	private String encoded;
	
	public AppPushService(){
		Properties props = new Properties();
		try {
			props=PropertiesLoaderUtils.loadAllProperties("config.properties");
			appKey = props.getProperty("push_appKey");
			masterSecret = props.getProperty("push_masterSecret");
			url = props.getProperty("push_URL");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		restTemplate = new RestTemplate();
		endcode();
		addHeader("Authorization", "Basic " + encoded);
	}
	
	public void push(String message, long newsId){
		String result = restTemplate.postForObject(url, getJson(message, newsId), String.class);
		//getJson();
		System.out.println(result);
	}
	
	private String getJson(String alert, long newsId){
		String json = "";
		Map<String, Object> map = new HashedMap();
		map.put("platform", "all");
		map.put("audience", "all");
		
		Map<String, Object> notification = new HashedMap();
		Map<String, Object> ios = new HashedMap();
		Map<String, Object> android = new HashedMap();
		Map<String, Object> extras = new HashedMap();
		Map<String, Object> options = new HashedMap();
		
		ios.put("alert", alert);
		android.put("alert", alert);
		extras.put("newsId", newsId);
		ios.put("extras", extras);
		android.put("extras", extras);
		notification.put("ios", ios);
		notification.put("android", android);
		options.put("apns_production", false);
		
		map.put("notification", notification);
		map.put("options", options);
		ObjectMapper mapper = new ObjectMapper();
		try {
			json = mapper.writeValueAsString(map);
		} catch (JsonGenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(json);
		return json;
	}
	
	private void addHeader(final String headerName, final String headerValue){
		List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
		interceptors.add(new ClientHttpRequestInterceptor() {
			
			@Override
			public ClientHttpResponse intercept(HttpRequest request, byte[] body,
					ClientHttpRequestExecution execution) throws IOException {
				request.getHeaders().add(headerName, headerValue);
				return execution.execute(request, body);
			}
		});
		restTemplate.setInterceptors(interceptors);
	}
	
	private void endcode(){
		if(appKey != null && masterSecret != null){
			encoded = new String(Base64.encodeBase64((appKey + ":" + masterSecret).getBytes()));
		}
	}
	
	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public String getMasterSecret() {
		return masterSecret;
	}

	public void setMasterSecret(String masterSecret) {
		this.masterSecret = masterSecret;
	}

	public String getEncoded() {
		return encoded;
	}

	public void setEncoded(String encoded) {
		this.encoded = encoded;
	}
	
}
