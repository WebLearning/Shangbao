package com.shangbao.dao;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;

import com.shangbao.model.Article;
import com.shangbao.model.Page;

public interface ArticleDao {
	/** 
     * 新增 
     * <br>------------------------------<br> 
     * @param user 
     */  
    void insert(Article article);  
      
    /** 
     * 新增 
     * <br>------------------------------<br> 
     * @param users 
     */  
    void insertAll(List<Article> articles);  
      
    /** 
     * 删除,主键id, 如果主键的值为null,删除会失败 
     * <br>------------------------------<br> 
     * @param id 
     */  
    void deleteById(String id);  
      
    /** 
     * 按条件删除 
     * <br>------------------------------<br> 
     * @param criteriaUser 
     */  
    void delete(Article criteriaArticle);  
      
    /** 
     * 删除全部 
     * <br>------------------------------<br> 
     */  
    void deleteAll();  
      
    /** 
     * 修改 
     * <br>------------------------------<br> 
     * @param user 
     */  
    void updateById(long id);  
      
    /** 
     * 更新多条 
     * <br>------------------------------<br> 
     * @param criteriaUser 
     * @param user 
     */  
    void update(Article criteriaArticle, Article article);  
     
    List<Article> find(Article article);
    
    /** 
     * 根据主键查询 
     * <br>------------------------------<br> 
     * @param id 
     * @return 
     */  
    Article findById(long id);  
      
    /** 
     * 查询全部 
     * <br>------------------------------<br> 
     * @return 
     */  
    List<Article> findAll();  
      
    /** 
     * 按条件查询 
     * <br>------------------------------<br> 
     * @param criteriaUser 
     * @param skip 
     * @param limit 
     * @return 
     */  
    List<Article> find(Article criteriaArticle, int skip, int limit);  
      
    /** 
     * 根据条件查询出来后 在去修改 
     * <br>------------------------------<br> 
     * @param criteriaUser  查询条件 
     * @param updateUser    修改的值对象 
     * @return 
     */  
    Article findAndModify(Article criteriaArticle, Article updateArticle);  
      
    /** 
     * 查询出来后 删除 
     * <br>------------------------------<br> 
     * @param criteriaUser 
     * @return 
     */  
    Article findAndRemove(Article criteriaArticle);  
      
    
    Page<Article> getPage(int pageNo, int pageSize, Query query);
    
    /** 
     * count 
     * <br>------------------------------<br> 
     * @param criteriaUser 
     * @return 
     */  
    long count(Article criteriaArticle);
}
