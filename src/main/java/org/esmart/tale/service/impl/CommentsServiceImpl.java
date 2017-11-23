package org.esmart.tale.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;

import org.esmart.tale.dto.Comment;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.mapper.TcommentsMapper;
import org.esmart.tale.mapper.TcontentsMapper;
import org.esmart.tale.model.Tcomments;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.service.CommentsService;
import org.esmart.tale.utils.DateUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("commentsService")
@Transactional
public class CommentsServiceImpl implements CommentsService {

	@Resource
	private TcommentsMapper commentsMapper;
	@Resource
	private TcontentsMapper contentsMapper;
	
	@Override
	public void saveComment(Tcomments comments) {
		// TODO Auto-generated method stub
		if (comments.getContent().length() < 5 || comments.getContent().length() > 2000) {
	            throw new TalException("评论字数在5-2000个字符");
	        }
        if (null == comments.getCid()) {
            throw new TalException("评论文章不能为空");
        }
        
        Tcontents content = contentsMapper.selectByPrimaryKey(comments.getCid());
        
        if(null==content){
        	throw new TalException("不存在的文章");
        }
        try {
            comments.setOwnerId(content.getAuthorId());
            comments.setCreated(DateUtil.currentDateInt());
            commentsMapper.insert(comments);
            //评论+1
            //这样做，因为要尽量减少修改的字段。
			Tcontents cont =new Tcontents();
			cont.setCid(content.getCid());
			cont.setCommentsNum(content.getCommentsNum() + 1);
            contentsMapper.updateByPrimaryKeySelective(cont);
            
        } catch (Exception e) {
            throw e;
        }
		
	}

	@Override
	public void delete(Integer coid, Integer cid) {
		// TODO Auto-generated method stub
		if (null == cid) {
            throw new TalException("主键不存在");
        }
		try {
			commentsMapper.deleteByPrimaryKey(coid);
			
			Tcontents content = contentsMapper.selectByPrimaryKey(coid);
			//评论-1
			//这样做，因为要尽量减少修改的字段。
			Tcontents cont =new Tcontents();
			cont.setCid(content.getCid());
			cont.setCommentsNum(content.getCommentsNum() - 1);
            contentsMapper.updateByPrimaryKeySelective(cont);
		} catch (Exception e) {
            throw e;
        }
	}

	@Override
	public PageInfo<Comment> getComments(Integer cid, int page, int limit) {
		// TODO Auto-generated method stub
		Tcomments comment =new Tcomments();
		comment.setCid(cid);
		//一级评论
		comment.setParent(0);
		//分页处理
		PageHelper.startPage(page, limit);
		List<Tcomments> comments = commentsMapper.selectComments(comment);
		PageInfo<Tcomments> pageTcomts=new PageInfo<Tcomments>(comments);
		
		//传输对像数据封装
		PageInfo<Comment> pageComts=new PageInfo<Comment>();
		
		if(null!=pageTcomts.getList()){
			List<Tcomments> listTcts = pageTcomts.getList();
			List<Comment> listCts=new ArrayList<Comment>(listTcts.size());
			listTcts.forEach(coment->{
				Comment com=new Comment(coment);
				List<Tcomments> children = new ArrayList<>();
				getChildren(children,coment.getCoid(),coment.getCid());
				com.setChildren(children);
				if(null==children||children.size()==0){
					com.setLevels(1);
				}
				listCts.add(com);
			});
			pageComts.setList(listCts);
			//不知道行不行
			pageComts=new PageInfo<Comment>(listCts);
		}
		return pageComts;
	}
	/**
     * 获取该评论下的追加评论
     *
     * @param coid
     * @return
     */
    private void getChildren(List<Tcomments> list, Integer coid,Integer cid ){
    	Tcomments comment =new Tcomments();
		comment.setCid(cid);
		//子级评论
		comment.setParent(coid);
		List<Tcomments> cms = commentsMapper.selectComments(comment);
		
        if (null != cms) {
            list.addAll(cms);
            cms.forEach(c -> getChildren(list, c.getCoid(),cid));
        }
    }
	@Override
	public PageInfo<Tcomments> getTcomments(int authorId, int page, int limit) {
		// TODO Auto-generated method stub
		Tcomments comment =new Tcomments();
		comment.setAuthorId(authorId);
		//分页处理
		PageHelper.startPage(page, limit);
		List<Tcomments> cms = commentsMapper.selectComments(comment);
		PageInfo<Tcomments> comments=new PageInfo<Tcomments>(cms);
		return comments;
	}

	@Override
	public Tcomments byId(Integer coid) {
		// TODO Auto-generated method stub
		if(null != coid){
			return commentsMapper.selectByPrimaryKey(coid);
		}
		return null;
	}

	@Override
	public void update(Tcomments comments) {
		// TODO Auto-generated method stub
		if(null != comments && null != comments.getCoid()){
			
			commentsMapper.updateByPrimaryKeySelective(comments);
		}
	}

}
