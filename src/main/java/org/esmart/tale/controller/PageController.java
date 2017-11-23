package org.esmart.tale.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.LogActions;
import org.esmart.tale.dto.Types;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.jetag.Commons;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.model.Tlogs;
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
@RequestMapping("/admin/page")
public class PageController extends BaseController{

	private static Logger logger = Logger.getLogger(PageController.class);
	
	@Resource
	private ContentsService contentsService;
	@Resource
	private MetasService metasService;
	@Resource
	private SiteService siteService;
	@Resource
	private LogService logService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public String index(ModelMap model) {
		PageInfo<Tcontents> contentsPaginator = contentsService.getArticles("type", Types.PAGE, 1, TaleConst.MAX_POSTS);
        model.addAttribute("articles", contentsPaginator);
        return "admin/page_list";
    }
	@RequestMapping(value = "/new", method = RequestMethod.GET)
    public String newPage(ModelMap model) {
		model.addAttribute(Types.ATTACH_URL, Commons.site_option(Types.ATTACH_URL, Commons.site_url()));
        return "admin/page_edit";
    }
	
	@RequestMapping(value = "/{cid}", method = RequestMethod.GET)
    public String editPage(@PathVariable("cid") String cid, ModelMap model) {
		Tcontents contents = contentsService.byId(Integer.parseInt(cid));
        model.addAttribute("contents", contents);
        return "admin/page_edit";
    }
	
	@RequestMapping(value = "/publish", method = RequestMethod.POST)
    public @ResponseBody String publishPage(@RequestParam String title, @RequestParam String content,
    		@RequestParam String status, @RequestParam String slug,
    		@RequestParam Integer allow_comment, @RequestParam Integer allow_ping) {

		JSONObject json=new JSONObject();
        Tuser users = this.user();
        Tcontents contents = new Tcontents();
        contents.setTitle(title);
        contents.setContent(content);
        contents.setStatus(status);
        contents.setSlug(slug);
        contents.setType(Types.PAGE);
        if (null != allow_comment) {
            contents.setAllowComment(allow_comment == 1);
        }
        if (null != allow_ping) {
            contents.setAllowPing(allow_ping == 1);
        }
        contents.setAuthorId(users.getUid());

        try {
            contentsService.publish(contents);
            siteService.cleanCache(Types.C_STATISTICS);
        } catch (Exception e) {
            String msg = "页面发布失败";
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
        json.put("msg", "发布成功");
        
        return json.toString();
    }
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
    public @ResponseBody String modifyArticle(@RequestParam Integer cid, @RequestParam String title,
    		@RequestParam String content,
    		@RequestParam String status, @RequestParam String slug,
    		@RequestParam Integer allow_comment, @RequestParam Integer allow_ping) {

		JSONObject json=new JSONObject();
        Tuser users = this.user();
        Tcontents contents = new Tcontents();
        contents.setCid(cid);
        contents.setTitle(title);
        contents.setContent(content);
        contents.setStatus(status);
        contents.setSlug(slug);
        contents.setType(Types.PAGE);
        if (null != allow_comment) {
            contents.setAllowComment(allow_comment == 1);
        }
        if (null != allow_ping) {
            contents.setAllowPing(allow_ping == 1);
        }
        contents.setAuthorId(users.getUid());
        try {
            contentsService.updateArticle(contents);
        } catch (Exception e) {
            String msg = "页面编辑失败";
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
        json.put("msg", "页面编辑成功");
        
        return json.toString();
    }
	
	@RequestMapping(value = "/delete")
    public @ResponseBody String delete(@RequestParam int cid, ModelMap model,HttpServletRequest request) {
		JSONObject json=new JSONObject();
		try {
            contentsService.delete(cid);
            siteService.cleanCache(Types.C_STATISTICS);
            Tlogs log =new Tlogs();
			log.setAction(LogActions.DEL_PAGE);
			log.setAuthorId(this.getUid());
			log.setData(cid+" :删除页面");
			log.setIp(Tools.getIpAddr(request));
			logService.save(log);
        } catch (Exception e) {
            String msg = "页面删除失败";
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
        json.put("msg", "页面编辑成功");
        
        return json.toString();
    }
}
