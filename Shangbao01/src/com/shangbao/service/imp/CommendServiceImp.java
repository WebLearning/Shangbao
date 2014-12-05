package com.shangbao.service.imp;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.shangbao.dao.CommendDao;
import com.shangbao.model.persistence.Commend;
import com.shangbao.model.show.SingleCommend;
import com.shangbao.service.CommendService;

@Service
public class CommendServiceImp implements CommendService{
	@Resource
	private CommendDao commendDaoImp;
	
	@Override
	public Commend get(long articleId){
		Commend criteriaElement = new Commend();
		criteriaElement.setArticleId(articleId);
		List<Commend> commends = commendDaoImp.find(criteriaElement);
		if(!commends.isEmpty())
			return commends.get(0);
		return null;
	}
	
	@Override
	public void add(long articleId, SingleCommend singleCommend) {
		Query query = new Query();
		Update update = new Update();
		
		
	}

	@Override
	public void update(SingleCommend singleCommend) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(List<SingleCommend> singeCommends) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reply(SingleCommend singleCommend, String reply) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void publish(List<SingleCommend> singleCommends) {
		// TODO Auto-generated method stub
		
	}

}
