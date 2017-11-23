package org.esmart.tale.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.MetaDto;
import org.esmart.tale.dto.Types;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.service.MetasService;
import org.esmart.tale.service.SiteService;
import org.esmart.tale.utils.TaleConst;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/category")
public class CategoryController extends BaseController{

	private static Logger logger = Logger.getLogger(CategoryController.class);
	
	@Resource
	private SiteService siteService;
	@Resource
	private MetasService metasService;
	
	@RequestMapping(value="" , method=RequestMethod.GET )
	public String index(HttpServletRequest request,ModelMap model){
		PageInfo<MetaDto> categories = siteService.getMetas(Types.RECENT_META, Types.CATEGORY, TaleConst.MAX_POSTS);
		PageInfo<MetaDto> tags = siteService.getMetas(Types.RECENT_META, Types.TAG, TaleConst.MAX_POSTS);
		model.addAttribute("categories", categories.getList());
		model.addAttribute("tags", tags.getList());
		
		return "admin/category";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody String saveCategory(@RequestParam String cname, @RequestParam Integer mid) {
       JSONObject json=new JSONObject();
		try {
            metasService.saveMeta(Types.CATEGORY, cname, mid);
            siteService.cleanCache(Types.C_STATISTICS);
        } catch (Exception e) {
            String msg = "分类保存失败";
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
		json.put("msg", "分类保存成功");
		return json.toString();
    }
	@RequestMapping(value = "/delete")
    public @ResponseBody String delete(@RequestParam int mid) {
		JSONObject json=new JSONObject();
		try {
            metasService.delete(mid);
            siteService.cleanCache(Types.C_STATISTICS);
        } catch (Exception e) {
            String msg = "删除失败";
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
    		json.put("msg", "删除成功");
    		return json.toString();
	}
}
