package com.shangbao.service;

import java.util.List;

import com.shangbao.model.persistence.StartPictures;

public interface StartPicturesService {
	
	List<StartPictures> getAll();
	
	StartPictures findStartPictures(StartPictures startPictures);
	
	void addPicture(StartPictures startPictures, String pictureUrl);
	
	void deleteAll(StartPictures startPictures);
	
	void delete(StartPictures startPictures, int index);
	
	
}
