package org.esmart.tale.dto;

import java.io.Serializable;

/**
 * 
* @ClassName: Statistics 
* @Description: TODO 后台统计对象 
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年10月31日 下午2:39:35
 */
public class Statistics implements Serializable {

    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	// 文章数
    private int articles;
    // 页面数
    private int pages;
    // 评论数
    private int comments;
    // 分类数
    private int categories;
    // 标签数
    private int tags;
    // 链接数
    private int links;
    // 附件数
    private int attachs;

    public int getArticles() {
        return articles;
    }

    public void setArticles(int articles) {
        this.articles = articles;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public int getLinks() {
        return links;
    }

    public void setLinks(int links) {
        this.links = links;
    }

    public int getAttachs() {
        return attachs;
    }

    public void setAttachs(int attachs) {
        this.attachs = attachs;
    }

    public int getCategories() {
        return categories;
    }

    public void setCategories(int categories) {
        this.categories = categories;
    }

    public int getTags() {
        return tags;
    }

    public void setTags(int tags) {
        this.tags = tags;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
