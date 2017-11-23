package org.esmart.tale.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.LogActions;
import org.esmart.tale.dto.Types;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.model.Tlogs;
import org.esmart.tale.model.Tmetas;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.service.ContentsService;
import org.esmart.tale.service.LogService;
import org.esmart.tale.service.MetasService;
import org.esmart.tale.service.SiteService;
import org.esmart.tale.utils.TaleConst;
import org.esmart.tale.utils.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("admin/article")
public class ArticleController extends BaseController{

	private static Logger logger = Logger.getLogger(ArticleController.class);
	
	@Resource
	private ContentsService contentsService;
	@Resource
	private MetasService metasService;
	@Resource
	private SiteService siteService;
	@Resource
	private LogService logService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
    		@RequestParam(value = "limit", defaultValue = "15") int limit, ModelMap model) {

        PageInfo<Tcontents> contentsPaginator = contentsService.getArticles("type", Types.ARTICLE, page, limit);
        model.addAttribute("articles", contentsPaginator);
        return "admin/article_list";
    }
	@RequestMapping(value = "/publish", method = RequestMethod.GET)
    public String newArticle(ModelMap model) {
        List<Tmetas> categories = metasService.getMetas(Types.CATEGORY);
        model.addAttribute("categories", categories);
        return "admin/article_edit";
    }
	@RequestMapping(value = "/{cid}", method = RequestMethod.GET)
    public String editArticle(@PathVariable("cid") String cid, ModelMap model) {
		Tcontents contents = contentsService.byId(Integer.parseInt(cid));
        model.addAttribute("contents", contents);
        List<Tmetas> categories = metasService.getMetas(Types.CATEGORY);
        model.addAttribute("categories", categories);
        model.addAttribute("active", "article");
        return "admin/article_edit";
    }
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
    public @ResponseBody String publishArticle(@RequestParam String title, @RequestParam String content,
    		@RequestParam String tags, @RequestParam String categories,
    		@RequestParam String status, @RequestParam String slug,
    		@RequestParam Boolean allow_comment, @RequestParam Boolean allow_ping, @RequestParam Boolean allow_feed) {

		JSONObject json=new JSONObject();
        Tuser users = this.user();
        Tcontents contents = new Tcontents();;
        contents.setTitle(title);
        contents.setContent(content);
        contents.setStatus(status);
        contents.setSlug(slug);
        contents.setType(Types.ARTICLE);
        if (null != allow_comment) {
            contents.setAllowComment(allow_comment);
        }
        if (null != allow_ping) {
            contents.setAllowPing(allow_ping);
        }
        if (null != allow_feed) {
            contents.setAllowFeed(allow_feed);
        }
        contents.setAuthorId(users.getUid());
        contents.setTags(tags);
        if (StringUtils.isBlank(categories)) {
            categories = "默认分类";
        }
        contents.setCategories(categories);

        try {
            contentsService.publish(contents);
            siteService.cleanCache(Types.C_STATISTICS);
        } catch (Exception e) {
            String msg = "文章发布失败";
            if (e instanceof TalException) {
                msg = e.getMessage();
            } else {
                logger.error(msg, e);
            }
            json.put("success", false);
            json.put("msg", msg);
            
            return json.toString();
        }
        
        json.put("success", true);
        json.put("msg", "页面发布成功");
        
        return json.toString();
    }
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
    public @ResponseBody String modifyArticle(@RequestParam Integer cid, @RequestParam String title,
    		@RequestParam String content,
    		@RequestParam String tags, @RequestParam String categories,
    		@RequestParam String status, @RequestParam String slug,
    		@RequestParam Boolean allow_comment, @RequestParam Boolean allow_ping, @RequestParam Boolean allow_feed) {

		JSONObject json=new JSONObject();
        Tuser users = this.user();
        Tcontents contents = new Tcontents();
        contents.setCid(cid);
        contents.setTitle(title);
        contents.setContent(content);
        contents.setStatus(status);
        contents.setSlug(slug);
        if (null != allow_comment) {
            contents.setAllowComment(allow_comment);
        }
        if (null != allow_ping) {
            contents.setAllowPing(allow_ping);
        }
        if (null != allow_feed) {
            contents.setAllowFeed(allow_feed);
        }
        contents.setAuthorId(users.getUid());
        contents.setTags(tags);
        contents.setCategories(categories);
        try {
            contentsService.updateArticle(contents);
        } catch (Exception e) {
            String msg = "文章编辑失败";
            if (e instanceof TalException) {
                msg = e.getMessage();
            } else {
                logger.error(msg, e);
            }
            json.put("success", false);
            json.put("msg", msg);
            
            return json.toString();
        }
        
        json.put("success", true);
        json.put("msg", "文章编辑成功");
        
        return json.toString();
    }
	@RequestMapping(value = "/delete")
    public @ResponseBody String delete(@RequestParam int cid,  ModelMap model,HttpServletRequest request) {
		JSONObject json=new JSONObject();
		try {
            contentsService.delete(cid);
            siteService.cleanCache(Types.C_STATISTICS);
            Tlogs log =new Tlogs();
			log.setAction(LogActions.DEL_ARTICLE);
			log.setAuthorId(this.getUid());
			log.setData(cid+" :删除文章");
			log.setIp(Tools.getIpAddr(request));
			logService.save(log);
        } catch (Exception e) {
            String msg = "文章删除失败";
            if (e instanceof TalException) {
                msg = e.getMessage();
            } else {
                logger.error(msg, e);
            }
            json.put("success", false);
            json.put("msg", msg);
            
            return json.toString();
        }
        
        json.put("success", true);
        json.put("msg", "文章删除成功");
        
        return json.toString();
    }
}
