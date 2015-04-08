package com.shangbao.model;

import java.util.ArrayList;
import java.util.List;


public class OldNews {
	public int ResultCode;
	public String ResultMsg;
	public OldNewsTitles data = new OldNewsTitles();
	
	public void add(){
		OldNewsTitle title = new OldNewsTitle();
		title.CommendsUrl = "sdfsdf";
		title.ContentUrlForiPad = "sdfsdf";
		data.Hot.add(title);
		data.Normal.add(title);
	}
}

class OldNewsTitles {
	public List<OldNewsTitle> Hot = new ArrayList<>();
	public List<OldNewsTitle> Normal = new ArrayList<>();
}

class OldNewsTitle{
	public String NewsID;
	public String NewsCategory = "50";
	public String NewsType = "30";
	public String Keywords;
	public String SubHead;
	public String ContentUrlForiPhone;
	public String ContentUrlForiPad;
	public String ImageUrl;
	public String NewsDate;
	public int SortTime;
	public String NewsContent;
	public String CommentsCount;
	public String CommendsUrl = "";
	public String CommentsID = "";
}
