package org.esmart.tale.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.Comment;
import org.esmart.tale.dto.Types;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.model.Tcomments;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.service.CommentsService;
import org.esmart.tale.service.SiteService;
import org.esmart.tale.utils.TaleUtils;
import org.esmart.tale.utils.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/comments")
public class CommentController extends BaseController{

	private static Logger logger = Logger.getLogger(CommentController.class);
	
	@Resource
	private CommentsService commentsService;
	@Resource
	private SiteService siteService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public String index(@RequestParam(value = "page", defaultValue = "1") int page,
    		@RequestParam(value = "limit", defaultValue = "15") int limit, ModelMap model) {
		Tuser users = this.user();
        PageInfo<Tcomments> commentsPaginator = commentsService.getTcomments(users.getUid(), page, limit);
        model.addAttribute("comments", commentsPaginator);
        return "admin/comment_list";
    }
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
    public @ResponseBody String delete(@RequestParam Integer coid) {
		JSONObject json=new JSONObject();
        try {
        	Tcomments comments = commentsService.byId(coid);
            if(null == comments){
            	json.put("success", false);
    			json.put("msg", "不存在该评论");
                return json.toString();
            }
            commentsService.delete(coid, comments.getCid());
            siteService.cleanCache(Types.C_STATISTICS);
        } catch (Exception e) {
            String msg = "评论删除失败";
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
		json.put("msg", "评论删除成功");
		return json.toString();
    }
	
	@RequestMapping(value = "/status", method = RequestMethod.POST)
    public @ResponseBody String delete(@RequestParam Integer coid, @RequestParam String status) {
		JSONObject json=new JSONObject();
		try {
        	Tcomments comments = new Tcomments();
            comments.setCoid(coid);
            comments.setStatus(status);
            commentsService.update(comments);
            siteService.cleanCache(Types.C_STATISTICS);
        } catch (Exception e) {
            String msg = "操作失败";
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
		json.put("msg", "操作成功");
		return json.toString();
    }
	
	@RequestMapping(value = "", method = RequestMethod.POST)
    public @ResponseBody String reply(@RequestParam Integer coid, 
    		@RequestParam String content, ModelMap model,HttpServletRequest request) {
		JSONObject json=new JSONObject();
		if(null == coid || StringUtils.isBlank(content)){
        	json.put("success", false);
			json.put("msg", "请输入完整后评论");
            return json.toString();
        }

        if(content.length() > 2000){
        	json.put("success", false);
			json.put("msg", "请输入2000个字符以内的回复");
            return json.toString();
        }
        Tcomments c = commentsService.byId(coid);
        if(null == c){
        	json.put("success", false);
			json.put("msg", "不存在该评论");
            return json.toString();
        }
        Tuser users = this.user();
        content = TaleUtils.cleanXSS(content);
        content = EmojiParser.parseToAliases(content);

        Tcomments comments = new Tcomments();
        comments.setAuthor(users.getUsername());
        comments.setAuthorId(users.getUid());
        comments.setCid(c.getCid());
        comments.setIp(Tools.getIpAddr(request));
        comments.setUrl(users.getHomeUrl());
        comments.setContent(content);
        if(StringUtils.isNotBlank(users.getEmail())){
            comments.setMail(users.getEmail());
        } else {
            comments.setMail("support@tale.me");
        }
        comments.setParent(coid);
        try {
            commentsService.saveComment(comments);
            siteService.cleanCache(Types.C_STATISTICS);
            json.put("success", true);
    		json.put("msg", "成功");
    		return json.toString();
        } catch (Exception e) {
            String msg = "回复失败";
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
