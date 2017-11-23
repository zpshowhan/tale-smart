package org.esmart.tale.jetag;

import jetbrick.template.JetAnnotations.Functions;
import jetbrick.template.runtime.InterpretContext;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.esmart.tale.dto.Comment;
import org.esmart.tale.dto.MetaDto;
import org.esmart.tale.model.Tcomments;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.service.SiteService;
import org.esmart.tale.utils.ApplicationContextUtil;
import org.esmart.tale.utils.TaleConst;
import org.esmart.tale.utils.TaleUtils;

import com.github.pagehelper.PageInfo;

/**
 * 主题函数
 * <p>
 * Created by biezhi on 2017/2/28.
 */
@Functions
public final class Theme {

	private static SiteService siteService=(SiteService)ApplicationContextUtil.getBean("siteService");;

    public static final List EMPTY = new ArrayList(0);


    /**
     * 获取header keywords
     * @return
     */
    public static String meta_keywords(){
        InterpretContext ctx = InterpretContext.current();
        Object value = ctx.getValueStack().getValue("keywords");
        if(null != value){
            return value.toString();
        }
        return Commons.site_option("site_keywords");
    }

    /**
     * 获取header description
     * @return
     */
    public static String meta_description(){
        InterpretContext ctx = InterpretContext.current();
        Object value = ctx.getValueStack().getValue("description");
        if(null != value){
            return value.toString();
        }
        return Commons.site_option("site_description");
    }

    /**
     * header title
     * @return
     */
    public static String head_title(){
        InterpretContext ctx = InterpretContext.current();
        Object value = ctx.getValueStack().getValue("title");

        String p = "首页";
        if(null != value){
            p = value.toString();
        }
        return p + " - " + Commons.site_option("site_title", "Tale 博客");
    }

    /**
     * 返回文章链接地址
     *
     * @return
     */
    public static String permalink() {
    	Tcontents contents = current_article();
        return null != contents ? permalink(contents) : "";
    }

    /**
     * 返回文章链接地址
     *
     * @param contents
     * @return
     */
    public static String permalink(Tcontents contents) {
        return permalink(contents.getCid(), contents.getSlug());
    }

    /**
     * 返回文章链接地址
     *
     * @param cid
     * @param slug
     * @return
     */
    public static String permalink(Integer cid, String slug) {
        return Commons.site_url("/article/" + (StringUtils.isNotBlank(slug) ? slug : cid.toString()));
    }

    /**
     * 显示文章创建日期
     *
     * @param fmt
     * @return
     */
    public static String created(String fmt) {
    	Tcontents contents = current_article();
        if (null != contents) {
            return Commons.fmtdate(contents.getCreated(), fmt);
        }
        return "";
    }

    /**
     * 获取文章最后修改时间
     *
     * @param fmt
     * @return
     */
    public static String modified(String fmt) {
    	Tcontents contents = current_article();
        if (null != contents) {
            return Commons.fmtdate(contents.getModified(), fmt);
        }
        return "";
    }

    /**
     * 返回文章浏览数
     *
     * @return
     */
    public static Integer hits() {
    	Tcontents contents = current_article();
        return null != contents ? contents.getHits() : 0;
    }

    /**
     * 显示分类
     *
     * @return
     */
    public static String show_categories() throws UnsupportedEncodingException {
    	Tcontents contents = current_article();
        if (null != contents) {
            return show_categories(contents.getCategories());
        }
        return "";
    }

    /**
     * 显示分类
     *
     * @param categories
     * @return
     */
    public static String show_categories(String categories) throws UnsupportedEncodingException {
        if (StringUtils.isNotBlank(categories)) {
            String[] arr = categories.split(",");
            StringBuffer sbuf = new StringBuffer();
            for (String c : arr) {
                sbuf.append("<a href=\""+Commons.ctx()+"/category/"+ URLEncoder.encode(c, "UTF-8") + "\">" + c + "</a>");
            }
            return sbuf.toString();
        }
        return show_categories("默认分类");
    }

    /**
     * 显示标签
     *
     * @param split 每个标签之间的分隔符
     * @return
     */
    public static String show_tags(String split) throws UnsupportedEncodingException {
    	Tcontents contents = current_article();
        if (StringUtils.isNotBlank(contents.getTags())) {
            String[] arr = contents.getTags().split(",");
            StringBuffer sbuf = new StringBuffer();
            for (String c : arr) {
                sbuf.append(split).append("<a href=\"/tag/" + URLEncoder.encode(c, "UTF-8") + "\">" + c + "</a>");
            }
            return split.length() > 0 ? sbuf.substring(split.length() - 1) : sbuf.toString();
        }
        return "";
    }

    /**
     * 显示文章浏览量
     * @return
     */
    public static String views(){
    	Tcontents contents = current_article();
        return null != contents ? contents.getHits().toString() : "0";
    }

    /**
     * 显示标签
     *
     * @return
     */
    public static String show_tags() throws UnsupportedEncodingException {
        return show_tags("");
    }

    /**
     * 显示文章内容，格式化markdown后的
     *
     * @return
     */
    public static String show_content() {
    	Tcontents contents = current_article();
        return null != contents ? article(contents.getContent()) : "";
    }

    /**
     * 获取文章摘要
     * @param len
     * @return
     */
    public static String excerpt(int len){
        return intro(len);
    }

    /**
     * 获取文章摘要
     * @param len
     * @return
     */
    public static String intro(int len) {
    	Tcontents contents = current_article();
        if (null != contents) {
            return intro(contents.getContent(), len);
        }
        return "";
    }

    /**
     * 截取文章摘要
     *
     * @param value 文章内容
     * @param len   要截取文字的个数
     * @return
     */
    public static String intro(String value, int len) {
        int pos = value.indexOf("<!--more-->");
        if (pos != -1) {
            String html = value.substring(0, pos);
            return TaleUtils.htmlToText(TaleUtils.mdToHtml(html));
        } else {
            String text = TaleUtils.htmlToText(TaleUtils.mdToHtml(value));
            if (text.length() > len) {
                return text.substring(0, len);
            }
            return text;
        }
    }

    /**
     * 显示文章内容，转换markdown为html
     *
     * @param value
     * @return
     */
    public static String article(String value) {
        if (StringUtils.isNotBlank(value)) {
            value = value.replace("<!--more-->", "\r\n");
            return TaleUtils.mdToHtml(value);
        }
        return "";
    }

    /**
     * 显示文章缩略图，顺序为：文章第一张图 -> 随机获取
     *
     * @return
     */
    public static String show_thumb(Tcontents contents) {
        if (null == contents) {
            return "";
        }
        String content = article(contents.getContent());
        String img = Commons.show_thumb(content);
        if (StringUtils.isNotBlank(img)) {
            return img;
        }
        int cid = contents.getCid();
        int size = cid % 20;
        size = size == 0 ? 1 : size;
        return "/templates/themes/default/static/img/rand/" + size + ".jpg";
    }

    /**
     * 获取当前文章的下一篇
     * @return
     */
    public static Tcontents article_next(){
    	Tcontents cur = current_article();
        return null != cur ? siteService.getNhContent("next", cur.getCid()) : null;
    }

    /**
     * 获取当前文章的上一篇
     *
     * @return
     */
    public static Tcontents article_prev(){
    	Tcontents cur = current_article();
        return null != cur ? siteService.getNhContent("prev", cur.getCid()) : null;
    }

    /**
     * 最新文章
     *
     * @param limit
     * @return
     */
    public static List<Tcontents> recent_articles(int limit) {
        if (null == siteService) {
            return EMPTY;
        }
        PageInfo<Tcontents> contens = siteService.getContens("recent_article", limit);
        return contens.getList();
    }

    /**
     * 随机获取文章
     * @param limit
     * @return
     */
    public static List<Tcontents> rand_articles(int limit) {
        if (null == siteService) {
            return EMPTY;
        }
        PageInfo<Tcontents> contens = siteService.getContens("random_article", limit);
        return contens.getList();
    }

    /**
     * 最新评论
     *
     * @param limit
     * @return
     */
    public static List<Tcomments> recent_comments(int limit) {
        if (null == siteService) {
            return EMPTY;
        }
        PageInfo<Tcomments> comments = siteService.recentComments(limit);
        return comments.getList();
    }

    /**
     * 获取分类列表
     *
     * @return
     */
    public static List<MetaDto> categries(int limit) {
        if (null == siteService) {
            return EMPTY;
        }
        PageInfo<MetaDto> metaDtos = siteService.getMetas("recent_meta", "category", limit);
        return metaDtos.getList();
    }

    /**
     * 随机获取limit个分类
     * @param limit
     * @return
     */
    public static List<MetaDto> rand_categries(int limit) {
        if (null == siteService) {
            return EMPTY;
        }
        PageInfo<MetaDto> metaDtos = siteService.getMetas("random_meta", "category", limit);
        return metaDtos.getList();
    }

    /**
     * 获取所有分类
     *
     * @return
     */
    public static List<MetaDto> categries() {
        return categries(TaleConst.MAX_POSTS);
    }

    /**
     * 获取标签列表
     *
     * @return
     */
        public static List<MetaDto> tags(int limit) {
        if (null == siteService) {
            return EMPTY;
        }
        PageInfo<MetaDto> metaDtos = siteService.getMetas("recent_meta", "tag", limit);
        return metaDtos.getList();
    }

    /**
     * 随机获取limit个标签
     * @param limit
     * @return
     */
    public static List<MetaDto> rand_tags(int limit) {
        if (null == siteService) {
            return EMPTY;
        }
        PageInfo<MetaDto> metaDtos = siteService.getMetas("random_meta", "tag", limit);
        return metaDtos.getList();
    }

    /**
     * 获取所有标签
     *
     * @return
     */
    public static List<MetaDto> tags() {
        return tags(TaleConst.MAX_POSTS);
    }

    /**
     * 获取评论at信息
     *
     * @param coid
     * @return
     */
    public static String comment_at(Integer coid) {
        if (null == siteService) {
            return "";
        }
        Tcomments comments = siteService.getComment(coid);
        if (null != comments) {
            return "<a href=\"#comment-" + coid + "\">@" + comments.getAuthor() + "</a>";
        }
        return "";
    }

    private static final String[] ICONS = {"bg-ico-book", "bg-ico-game", "bg-ico-note", "bg-ico-chat", "bg-ico-code", "bg-ico-image", "bg-ico-web", "bg-ico-link", "bg-ico-design", "bg-ico-lock"};

    /**
     * 显示文章图标
     *
     * @return
     */
    public static String show_icon() {
    	Tcontents contents = current_article();
        if (null != contents) {
            return show_icon(contents.getCid());
        }
        return show_icon(1);
    }

    /**
     * 显示文章图标
     *
     * @param cid
     * @return
     */
    public static String show_icon(int cid) {
        return ICONS[cid % ICONS.length];
    }

    /**
     * 显示文章标题
     *
     * @return
     */
    public static String title() {
        return title(current_article());
    }

    /**
     * 返回文章标题
     * @param contents
     * @return
     */
    public static String title(Tcontents contents) {
        return null != contents ? contents.getTitle() : Commons.site_title();
    }

    /**
     * 返回所有友链
     * @return
     */
    public static List<MetaDto> links(){
    	PageInfo<MetaDto> metaDtos =siteService.getMetas("recent_meta", "link", TaleConst.MAX_POSTS);
        List<MetaDto> links = metaDtos.getList();
        return links;
    }

    /**
     * 返回社交账号链接
     * @param socialtype
     * @return
     */
    public static String social_link(String socialtype) {
        String id = Commons.site_option("social_" + socialtype);
        switch (socialtype){
            case "github":
                return "https://github.com/" + id;
            case "weibo":
                return "http://weibo.com/" + id;
            case "twitter":
                return "https://twitter.com/" + id;
            case "zhihu":
                return "https://www.zhihu.com/people/" + id;
        }
        return "";
    }

    /**
     * 获取当前文章/页面的评论
     * @param limit
     * @return
     */
    public static PageInfo<Comment> comments(int limit){
        Tcontents contents = current_article();
        if(null == contents){
        	PageInfo pageIn=new PageInfo<>();
        	pageIn.setPages(0);
        	pageIn.setPageNum(limit);
            return pageIn;
        }
        InterpretContext ctx = InterpretContext.current();
        Object value = ctx.getValueStack().getValue("cp");
        int page = 1;
        if (null != value) {
            page = (int) value;
        }
        return siteService.getComments(contents.getCid(), page, limit);
    }

    /**
     * 获取当前上下文的文章对象
     *
     * @return
     */
    private static Tcontents current_article() {
        InterpretContext ctx = InterpretContext.current();
        Object value = ctx.getValueStack().getValue("article");
        if (null != value) {
            return (Tcontents) value;
        }
        return null;
    }

    /**
     * 显示评论
     *
     * @param noComment 评论为0的时候显示的文本
     * @param value     评论组装文本
     * @return
     */
    public static String comments_num(String noComment, String value){
        Tcontents contents = current_article();
        if(null == contents){
            return noComment;
        }
        return contents.getCommentsNum() > 0 ? String.format(value, contents.getCommentsNum()) : noComment;
    }

}
