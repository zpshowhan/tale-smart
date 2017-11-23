package org.esmart.tale.dto;


import java.util.List;

import org.esmart.tale.model.Tcomments;

/**
 * Created by biezhi on 2017/2/24.
 */
public class Comment extends Tcomments {

    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	private int levels;
    private List<Tcomments> children;

    public Comment(Tcomments comments) {
        setAuthor(comments.getAuthor());
        setMail(comments.getMail());
        setCoid(comments.getCoid());
        setAuthorId(comments.getAuthorId());
        setUrl(comments.getUrl());
        setCreated(comments.getCreated());
        setAgent(comments.getAgent());
        setIp(comments.getIp());
        setContent(comments.getContent());
        setOwnerId(comments.getOwnerId());
        setCid(comments.getCid());
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public List<Tcomments> getChildren() {
        return children;
    }

    public void setChildren(List<Tcomments> children) {
        this.children = children;
    }
}
