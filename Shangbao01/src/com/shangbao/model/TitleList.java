package com.shangbao.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TitleList {	
	private int pageCount;
	private int currentNo; 
	private List<Title> titleList = new ArrayList<Title>();
	
	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getCurrentNo() {
		return currentNo;
	}

	public void setCurrentNo(int currentNo) {
		this.currentNo = currentNo;
	}

	public List<Title> getTileList() {
		return titleList;
	}

	public void setTileList(List<Title> tileList) {
		this.titleList = tileList;
	} 
	
	public void addTitle(String title,
						 String author,
						 Date time,
						 String introduction,
						 String pictureUrl,
						 long articleId){
		Title title2 = new Title();
		title2.setTitle(title);
		title2.setAuthor(author);
		title2.setTime(time);
		title2.setIntroduction(introduction);
		title2.setPictureUrl(pictureUrl);
		title2.setArticleId(articleId);
		this.titleList.add(title2);
	}
}
