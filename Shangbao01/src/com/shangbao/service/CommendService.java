package com.shangbao.service;

import java.util.List;

import com.shangbao.model.persistence.Commend;
import com.shangbao.model.show.SingleCommend;

public interface CommendService {
	/**
	 * 查找一篇文章的所有评论
	 * @param articleId
	 */
	Commend get(long articleId);
	/**
	 * 添加一个评论
	 * @param singleCommend
	 */
	void add(long articleId, SingleCommend singleCommend);
	
	/**
	 * 更新一个评论
	 * @param singleCommend
	 */
	void update(SingleCommend singleCommend);
	
	/**
	 * 更新多个评论
	 * @param singleCommends
	 */
	void update(List<SingleCommend> singeCommends);
	
	/**
	 * 添加一条回复
	 * @param singleCommend
	 * @param reply
	 */
	void reply(SingleCommend singleCommend, String reply);
	
	/**
	 * 发布评论
	 * @param singleCommends
	 */
	void publish(List<SingleCommend> singleCommends);
}
