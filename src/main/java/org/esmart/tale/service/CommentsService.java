package org.esmart.tale.service;

import org.esmart.tale.dto.Comment;
import org.esmart.tale.model.Tcomments;

import com.github.pagehelper.PageInfo;

public interface CommentsService {

	void saveComment(Tcomments comments);
	
	/**
     * 删除评论，暂时没用
     * @param coid
     * @param cid
     * @throws Exception
     */
    void delete(Integer coid, Integer cid);
    /**
     * 获取文章下的评论
     * @param cid
     * @param page
     * @param limit
     * @return
     */
    PageInfo<Comment> getComments(Integer cid, int page, int limit);
    
    /**
     * 查询作者下的
     * 分页管理评论
     * @param take
     * @return
     */
    PageInfo<Tcomments> getTcomments(int authorId,int page, int limit);
    
    /**
     * 根据主键查询评论
     * @param coid
     * @return
     */
    Tcomments byId(Integer coid);
    
    /**
     * 更新评论状态
     * @param comments
     */
    void update(Tcomments comments);
}
