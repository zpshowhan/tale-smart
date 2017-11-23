package org.esmart.tale.controller;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.Types;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.model.Tmetas;
import org.esmart.tale.service.MetasService;
import org.esmart.tale.service.SiteService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin/links")
public class LinksController extends BaseController{

	private static Logger logger = Logger.getLogger(LinksController.class);
	@Resource
	private MetasService metasService;
	@Resource
	private SiteService siteService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public String index(ModelMap model) {
        List<Tmetas> metass = metasService.getMetas(Types.LINK);
        model.addAttribute("links", metass);
        return "admin/links";
    }
	@RequestMapping(value = "/save", method = RequestMethod.POST)
    public @ResponseBody String saveLink(@RequestParam String title, 
    		@RequestParam String url,
    		@RequestParam String logo, 
    		@RequestParam Integer mid,
    		@RequestParam(value = "sort", defaultValue = "0") int sort) {
		JSONObject json=new JSONObject();
        try {
        	Tmetas metas = new Tmetas();
            metas.setName(title);
            metas.setSlug(url);
            metas.setDescription(logo);
            metas.setSort(sort);
            metas.setType(Types.LINK);
            if (null != mid) {
                metas.setMid(mid);
                metasService.update(metas);
            } else {
                metasService.saveMeta(metas);
            }
            siteService.cleanCache(Types.C_STATISTICS);
        } catch (Exception e) {
            String msg = "友链保存失败";
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
		json.put("msg", "保存成功");
		return json.toString();
    }
	@RequestMapping(value = "delete")
    public @ResponseBody String delete(@RequestParam int mid) {
		JSONObject json=new JSONObject();
        try {
            metasService.delete(mid);
            siteService.cleanCache(Types.C_STATISTICS);
        } catch (Exception e) {
            String msg = "友链删除失败";
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
