package com.shangbao.service.imp;

import java.util.List;

import com.shangbao.model.persistence.Article;
import com.shangbao.service.ImageService;

public class ImageServiceImp implements ImageService{

	@Override
	public boolean deleteImage(String url) {
		
		return false;
	}

	@Override
	public boolean deleteImage(Article article) {
		List<String> urls = article.getPicturesUrl();
		return false;
	}

}
