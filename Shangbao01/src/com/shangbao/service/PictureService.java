package com.shangbao.service;

import java.util.List;

import com.shangbao.model.ArticleState;
import com.shangbao.model.persistence.Article;
import com.shangbao.model.persistence.Channel;
import com.shangbao.model.show.ChannelList;
import com.shangbao.model.show.TitleList;

public interface PictureService {
	void add(Article article);
	void add(Channel activity);
	void delete(List<Channel> activity);
	TitleList getTiltList(ArticleState articleState, int pageNo);
	TitleList getOrderedList(ArticleState articleState, int pageNo,
			String order ,String direction);
	TitleList fuzzyFind(String words, ArticleState state, int pageNo, int pageSize);
	void setPutState(ArticleState articleState, List<Long> idList);
	void setDeleteState(ArticleState articleState, List<Long> idList);
	Article findOne(Long id);
	void update(Article article);
	ChannelList getActivity(int pageNo, int pageSize);
	ChannelList getOrderActivity(int pageNo, int pageSize, String order);
}
