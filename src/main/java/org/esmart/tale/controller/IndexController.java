package org.esmart.tale.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.Archive;
import org.esmart.tale.dto.MetaDto;
import org.esmart.tale.dto.Types;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.jetag.Commons;
import org.esmart.tale.model.Tcomments;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.model.Tmetas;
import org.esmart.tale.service.CommentsService;
import org.esmart.tale.service.ContentsService;
import org.esmart.tale.service.MetasService;
import org.esmart.tale.service.SiteService;
import org.esmart.tale.utils.ResponseUtils;
import org.esmart.tale.utils.TaleConst;
import org.esmart.tale.utils.TaleUtils;
import org.esmart.tale.utils.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: IndexController 
* @Description: TODO 博客展示控制器 
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年11月3日 下午4:26:44
 */
@Controller
@RequestMapping("")
public class IndexController extends BaseController{

	private static Logger logger = Logger.getLogger(LinksController.class);
	@Resource
	private ContentsService contentsService;
	@Resource
	private MetasService metasService;
	@Resource
	private CommentsService commentsService;
	@Resource
	private SiteService siteService;
	
	@RequestMapping(value="/" , method=RequestMethod.GET  )
	public String index(ModelMap model,@RequestParam(value = "limit", defaultValue = "12") int limit){
		
		return this.index(model, 1, limit);
	}
	
	@RequestMapping(value = "/{pagename}", method = RequestMethod.GET)
    public String page(@PathVariable("pagename") String pagename, ModelMap model,HttpServletRequest request) {
		Tcontents contents = contentsService.bySlug(pagename);
		if(NumberUtils.isNumber(pagename)){
			
			contents = contentsService.byId(Integer.parseInt(pagename));
		}
        if (null == contents) {
            return this.render_404();
        }
        if (contents.getAllowComment()) {
        	String pcp=request.getParameter("cp");
        	int cp = StringUtils.isBlank(pcp)==true?1:Integer.valueOf(pcp);
        	model.addAttribute("cp", cp);
        }
        model.addAttribute("article", contents);
        Integer hits = cache.hget("page", "hits");
        hits = null == hits ? 1 : hits + 1;
        if (hits >= TaleConst.HIT_EXCEED) {
        	Tcontents temp = new Tcontents();
            temp.setCid(contents.getCid());
            temp.setHits(contents.getHits() + hits);
            contentsService.update(temp);
            cache.hset("page", "hits", 1);
        } else {
            cache.hset("page", "hits", hits);
        }
        return this.render("page");
    }
	
	@RequestMapping(value = "/page/{p}", method = RequestMethod.GET)
    public String index(ModelMap model, @PathVariable("p") int p, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        p = p < 0 || p > TaleConst.MAX_PAGE ? 1 : p;
        Map<String,Object> map=new HashMap<String,Object>();
        
		map.put("type", Types.ARTICLE);
		map.put("status", Types.PUBLISH);
		PageInfo<Tcontents> articles=contentsService.getArticles(map, p, limit);
		model.addAttribute("articles", articles);
        if (p > 1) {
            model.addAttribute("title", "第" + p + "页");
        }
        model.addAttribute("is_home", true);
        model.addAttribute("page_prefix", "/page");
        return this.render("index");
    }
	
	@RequestMapping(value = "/article/{cid}", method = RequestMethod.GET)
    public String post(ModelMap model, @PathVariable("cid") String cid,HttpServletRequest request) {
		Tcontents contents = contentsService.byId(Integer.parseInt(cid));
        if (null == contents) {
            return this.render_404();
        }
        model.addAttribute("article", contents);
        model.addAttribute("is_post", true);
        if (contents.getAllowComment()) {
        	String pcp=request.getParameter("cp");
        	int cp = StringUtils.isBlank(pcp)==true?1:Integer.valueOf(pcp);
        	model.addAttribute("cp", cp);
        }
        Integer hits = cache.hget("article", "hits");
        hits = null == hits ? 1 : hits + 1;
        if (hits >= TaleConst.HIT_EXCEED) {
        	Tcontents temp = new Tcontents();
            temp.setCid(contents.getCid());
            temp.setHits(contents.getHits() + hits);
            contentsService.update(temp);
            cache.hset("article", "hits", 1);
        } else {
            cache.hset("article", "hits", hits);
        }
        return this.render("post");
    }
	
	@RequestMapping(value = "/category/{keyword}", method = RequestMethod.GET)
    public String categories(ModelMap model,  @PathVariable("keyword") String keyword, 
    		@RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.categories(model, keyword, 1, limit);
    }

	@RequestMapping(value = "/category/{keyword}/{page}", method = RequestMethod.GET)
    public String categories(ModelMap model, @PathVariable("keyword") String keyword,
    		@PathVariable("page") int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        page = page < 0 || page > TaleConst.MAX_PAGE ? 1 : page;
        
        MetaDto metaDto = metasService.getMeta(Types.CATEGORY, keyword);
        if (null == metaDto) {
            return this.render_404();
        }

        PageInfo<Tcontents> contentsPaginator = contentsService.getArticles(metaDto.getMid(), page, limit);

        model.addAttribute("articles", contentsPaginator);
        model.addAttribute("meta", metaDto);
        model.addAttribute("type", "分类");
        model.addAttribute("keyword", keyword);
        model.addAttribute("is_category", true);
        model.addAttribute("page_prefix", "/category/" + keyword);

        return this.render("page-category");
    }
	
	@RequestMapping(value = "/tag/{name}", method = RequestMethod.GET)
    public String tags(ModelMap model, @PathVariable String name, @RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.tags(model, name, 1, limit);
    }
	
	@RequestMapping(value = "/tag/{name}/{page}", method = RequestMethod.GET)
    public String tags(ModelMap model, @PathVariable("name") String name, 
    		@PathVariable("page") int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {

        page = page < 0 || page > TaleConst.MAX_PAGE ? 1 : page;
        MetaDto metaDto = metasService.getMeta(Types.TAG, name);
        if (null == metaDto) {
            return this.render_404();
        }

        PageInfo<Tcontents> contentsPaginator = contentsService.getArticles(metaDto.getMid(), page, limit);
        model.addAttribute("articles", contentsPaginator);
        model.addAttribute("meta", metaDto);
        model.addAttribute("type", "标签");
        model.addAttribute("keyword", name);
        model.addAttribute("is_tag", true);
        model.addAttribute("page_prefix", "/tag/" + name);

        return this.render("page-category");
    }
	
	@RequestMapping(value = "/search/{keyword}", method = RequestMethod.GET)
    public String search(ModelMap model, @PathVariable("keyword") String keyword, 
    		@RequestParam(value = "limit", defaultValue = "12") int limit) {
        return this.search(model, keyword, 1, limit);
    }

	@RequestMapping(value = "/search/{keyword}/{page}", method = RequestMethod.GET)
    public String search(ModelMap model, @PathVariable("keyword") String keyword,
    		@PathVariable("page") int page, @RequestParam(value = "limit", defaultValue = "12") int limit) {

        page = page < 0 || page > TaleConst.MAX_PAGE ? 1 : page;
        
        Map<String,Object> map=new HashMap<>();
        map.put("type", Types.ARTICLE);
        map.put("status", Types.PUBLISH);
        map.put("like", keyword);
        
        PageInfo<Tcontents> articles = contentsService.getArticles(map, page, limit);
        model.addAttribute("articles", articles);

        model.addAttribute("type", "搜索");
        model.addAttribute("keyword", keyword);
        model.addAttribute("page_prefix", "/search/" + keyword);
        return this.render("page-category");
    }
	
	@RequestMapping(value = "/archives", method = RequestMethod.GET)
    public String archives(ModelMap model) {
		List<Archive> archives = siteService.getArchives();
        model.addAttribute("archives", archives);
        model.addAttribute("is_archive", true);
        return this.render("archives");
    }
	
	@RequestMapping(value = "/links", method = RequestMethod.GET)
    public String links(ModelMap model) {
        List<Tmetas> links = metasService.getMetas(Types.LINK);
        model.addAttribute("links", links);
        return this.render("links");
    }
	
	@RequestMapping(value = {"/feed", "/feed.xml"}, method = RequestMethod.GET)
    public void feed(HttpServletResponse response) {
		Map<String,Object> map=new HashMap<>();
        map.put("type", Types.ARTICLE);
        map.put("status", Types.PUBLISH);
        map.put("allowFeed", true);
        
        PageInfo<Tcontents> contentsPaginator = contentsService.getArticles(map, 1, TaleConst.MAX_POSTS);
        try {
            String xml = TaleUtils.getRssXml(contentsPaginator.getList());
            ResponseUtils.renderXml(response, xml);
        } catch (Exception e) {
            logger.error("生成RSS失败", e);
        }
    }
	
	@RequestMapping("/logout")
    public void logout(HttpSession session, HttpServletResponse response) {
        TaleUtils.logout(session, response);
    }
	@RequestMapping(value = "/comment", method = RequestMethod.POST)
    public @ResponseBody String comment(HttpServletRequest request, HttpServletResponse response,
    		@RequestParam Integer cid, @RequestParam Integer coid,
    		@RequestParam String author, @RequestParam String mail,
    		@RequestParam String url, @RequestParam String text, @RequestParam String _csrf_token) {
		
		JSONObject json=new JSONObject();
		String ref = request.getHeader("Referer");
        if (StringUtils.isBlank(ref) || StringUtils.isBlank(_csrf_token)) {
        	
        	json.put("success", false);
            json.put("msg", "非法请求");
            
            return json.toString();
        }

        if(!ref.startsWith(Commons.site_url())){
        	
        	json.put("success", false);
            json.put("msg", "非法评论来源");
            
            return json.toString();
        }

        String token = cache.hget(Types.CSRF_TOKEN, _csrf_token);
        //先去掉，不然评论不成功
        /*if (StringUtils.isBlank(token)) {
        	
        	json.put("success", false);
            json.put("msg", "非法请求");
            
            return json.toString();
        }*/

        if (null == cid || StringUtils.isBlank(author) || StringUtils.isBlank(mail) || StringUtils.isBlank(text)) {
            
        	json.put("success", false);
            json.put("msg", "请输入完整后评论");
            
            return json.toString();
        }

        if (author.length() > 50) {
        	
        	json.put("success", false);
            json.put("msg", "姓名过长");
            
            return json.toString();
        }

        if (!TaleUtils.isEmail(mail)) {
        	
        	json.put("success", false);
            json.put("msg", "请输入正确的邮箱格式");
            
            return json.toString();
        }

        if (StringUtils.isNotBlank(url) && !TaleUtils.isURL(url)) {
        	json.put("success", false);
            json.put("msg", "请输入正确的URL格式");
            
            return json.toString();
        }

        if (text.length() > 200) {
        	
        	json.put("success", false);
            json.put("msg", "请输入200个字符以内的评论");
            
            return json.toString();
        }

        String val = Tools.getIpAddr(request) + ":" + cid;
        Integer count = cache.hget(Types.COMMENTS_FREQUENCY, val);
        if (null != count && count > 0) {
        	json.put("success", false);
            json.put("msg", "您发表评论太快了，请过会再试");
            
            return json.toString();
        }

        author = TaleUtils.cleanXSS(author);
        text = TaleUtils.cleanXSS(text);

        author = EmojiParser.parseToAliases(author);
        text = EmojiParser.parseToAliases(text);

        Tcomments comments = new Tcomments();
        comments.setAuthor(author);
        comments.setCid(cid);
        comments.setIp(Tools.getIpAddr(request));
        comments.setUrl(url);
        comments.setContent(text);
        comments.setMail(mail);
        comments.setParent(coid);
        try {
            commentsService.saveComment(comments);
            TaleUtils.setCookie(response,"tale_remember_author",author, 7 * 24 * 60 * 60);
            TaleUtils.setCookie(response,"tale_remember_mail",mail, 7 * 24 * 60 * 60);
            if (StringUtils.isNotBlank(url)) {
            	TaleUtils.setCookie(response,"tale_remember_url", url, 7 * 24 * 60 * 60);
            }
            // 设置对每个文章1分钟可以评论一次
            cache.hset(Types.COMMENTS_FREQUENCY, val, 1, 60);
            siteService.cleanCache(Types.C_STATISTICS);
            json.put("success", true);
            json.put("msg", "成功");
            
            return json.toString();
        } catch (Exception e) {
            String msg = "评论发布失败";
            if (e instanceof TalException) {
                msg = e.getMessage();
            } else {
                logger.error(msg, e);
            }
            json.put("success", false);
            json.put("msg", msg);
            
            return json.toString();
        }
	}
}
