package org.esmart.tale.service;

import java.util.HashMap;
import java.util.Map;

import org.esmart.tale.model.Tcontents;

import com.github.pagehelper.PageInfo;

public interface ContentsService {

	/**
     * 根据id或slug获取文章
     *
     * @param id
     * @return
     */
	Tcontents byId(int id);
	
	Tcontents bySlug(String slug);
	/**
	 * 
	*
	* @Title: getArticles 
	* @Description: TODO  
	* @param @param type 表字段
	* @param @param obj 表字段值
	* @param @param page
	* @param @param limit
	* @param @return    设定文件 
	* @return PageInfo<Tcontents>    返回类型 
	* @throws
	 */
	PageInfo<Tcontents> getArticles(String type, Object obj,int page,int limit);
	
	PageInfo<Tcontents> getArticles(Map<String,Object> map,int page,int limit);
	
	/**
     * 发布文章
     * @param contents
     */
    void publish(Tcontents contents);
    /**
     * 编辑文章
     * @param contents
     */
    void updateArticle(Tcontents contents);
    
    /**
     * 自定义update
     * @param contents
     */
    void update(Tcontents contents);
    
    /**
     * 根据文章id删除
     * @param cid
     */
    void delete(int cid);
    
    /**
     * 查询分类/标签下的文章归档
     * @param mid
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Tcontents> getArticles(Integer mid, int page, int limit);
	
}
