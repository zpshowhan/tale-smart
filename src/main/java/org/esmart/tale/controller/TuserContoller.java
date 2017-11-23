package org.esmart.tale.controller;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.service.TuserService;
import org.esmart.tale.utils.ResponseUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class TuserContoller extends BaseController{

	private static Logger logger = Logger.getLogger(TuserContoller.class);
	@Resource
	private TuserService userService;
	
	@RequestMapping("/show/{uid}")
	public String showUser(ModelMap model,@PathVariable int uid){
		
		Tuser user = userService.selectByPrimaryKey(uid);
		model.addAttribute("name", user.getUsername());
		return "index";
	}
	@RequestMapping("/save")
	public void saveUser(Tuser user,HttpServletResponse response){
		user.setCreated((int)(new Date().getTime()/1000));
		
		int inid = userService.insert(user);
		ResponseUtils.renderText(response, "添加的用户是："+inid);
	}
}
