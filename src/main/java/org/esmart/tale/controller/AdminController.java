package org.esmart.tale.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.BackResponse;
import org.esmart.tale.dto.LogActions;
import org.esmart.tale.dto.Statistics;
import org.esmart.tale.dto.Types;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.jetag.Commons;
import org.esmart.tale.model.Tcomments;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.model.Tlogs;
import org.esmart.tale.model.Toptions;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.service.LogService;
import org.esmart.tale.service.OptionsService;
import org.esmart.tale.service.SiteService;
import org.esmart.tale.service.TuserService;
import org.esmart.tale.utils.TaleConst;
import org.esmart.tale.utils.TaleUtils;
import org.esmart.tale.utils.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController{

	private static Logger logger = Logger.getLogger(AdminController.class);
	@Resource
	private SiteService siteService;
	@Resource
	private TuserService userService;
	@Resource
	private OptionsService optionsService;
	@Resource
	private LogService logService;
	
	@RequestMapping(value={"/index","/"} ,method=RequestMethod.GET)
	public String index(ModelMap model){
		List<Tcomments> comments = siteService.recentComments(5).getList();
        List<Tcontents> contents = siteService.getContens(Types.RECENT_ARTICLE, 5).getList();
        Statistics statistics = siteService.getStatistics();
        // 取最新的20条日志
        List<Tlogs> logs = logService.getLogs(1, 20).getList();
        model.addAttribute("comments", comments);
        model.addAttribute("articles", contents);
        model.addAttribute("statistics", statistics);
        model.addAttribute("logs", logs);
		return "admin/index";
	}
	
	@RequestMapping(value="/setting" ,method=RequestMethod.GET)
	public String setting(ModelMap model){
		
		List<Toptions> options = optionsService.getOptions();
		Map<String, String> mapOption= new HashMap<>();
		options.forEach(option ->{
			mapOption.put(option.getName(), option.getValue());
		});
		model.addAttribute("options", mapOption);
		// 读取主题
        String themesDir = TaleUtils.UP_DIR + "/templates/themes";
        File[] themesFile = new File(themesDir).listFiles();
        List<String> themems = new ArrayList<>(themesFile.length);
        for(File f : themesFile){
            if(f.isDirectory()){
                themems.add(f.getName());
            }
        }
        model.addAttribute("themes", themems);
		return "admin/setting";
	}
	
	@RequestMapping(value="/setting" ,method=RequestMethod.POST)
	public @ResponseBody String saveSetting(HttpServletRequest request,@RequestParam String site_theme){
		
		Map<String, String> map= new HashMap<>();
		JSONObject json=new JSONObject();
		try {
			
			Enumeration<String> names = request.getParameterNames();
			while (names.hasMoreElements()) {
				String head = (String) names.nextElement();
				String value = request.getParameter(head);
				Toptions option=new Toptions();
				option.setName(head);
				option.setValue(value);
				map.put(head, value);
				optionsService.saveOption(option);
			}
			if (StringUtils.isNotBlank(site_theme)) {
				BaseController.THEME = "themes/" + site_theme;
			}
			Tlogs log =new Tlogs();
			log.setAction(LogActions.SYS_SETTING);
			log.setAuthorId(this.getUid());
			log.setData(JSONObject.fromObject(map).toString());
			log.setIp(Tools.getIpAddr(request));
			logService.save(log);
			json.put("success", true);
			json.put("msg", "保存成功");
		}catch (Exception e) {
            String msg = "保存设置失败";
            if (e instanceof TalException) {
                msg = e.getMessage();
            } else {
            	logger.error(msg, e);
            }
            json.put("success", false);
    		json.put("msg", msg);
        }
		
		return json.toString();
	}
	
	@RequestMapping(value="/profile" ,method=RequestMethod.GET)
	public String profile(){
		
		return "admin/profile";
	}
	
	@RequestMapping(value = "/profile", method = RequestMethod.POST)
    public  @ResponseBody String saveProfile(@RequestParam String screen_name, 
    		@RequestParam String email, HttpServletRequest request) {
        Tuser users = this.user();
        JSONObject json=new JSONObject();
        json.put("success", false);
		json.put("msg", "保存失败");
        if (StringUtils.isNotBlank(screen_name) && StringUtils.isNotBlank(email)) {
        	Tuser temp = new Tuser();
            temp.setUid(users.getUid());
            temp.setScreenName(screen_name);
            temp.setEmail(email);
            userService.updateByPrimaryKeySelective(temp);
            Tlogs log =new Tlogs();
			log.setAction(LogActions.UP_INFO);
			log.setAuthorId(this.getUid());
			log.setData(JSONObject.fromObject(temp).toString());
			log.setIp(Tools.getIpAddr(request));
			logService.save(log);
			json.put("success", true);
			json.put("msg", "保存成功");
        }
        
        return json.toString();
    }
	
	@RequestMapping(value = "/password", method = RequestMethod.POST)
    public @ResponseBody String upPwd(@RequestParam String old_password, 
    		@RequestParam String password, HttpServletRequest request) {
		Tuser users = this.user();
        JSONObject json=new JSONObject();
        if (StringUtils.isBlank(old_password) || StringUtils.isBlank(password)) {
        	json.put("success", false);
    		json.put("msg", "请确认信息输入完整");
        	return json.toString();
        }

        if (!users.getPassword().equals(Tools.md5(users.getUsername() + old_password))) {
        	json.put("success", false);
    		json.put("msg", "旧密码错误");
        	return json.toString();
        }
        if (password.length() < 6 || password.length() > 14) {
        	json.put("success", false);
    		json.put("msg", "请输入6-14位密码");
        	return json.toString();
        }

        try {
        	Tuser temp = new Tuser();
            temp.setUid(users.getUid());
            String pwd = Tools.md5(users.getUsername() + password);
            temp.setPassword(pwd);
            userService.updateByPrimaryKeySelective(temp);
            Tlogs log =new Tlogs();
			log.setAction(LogActions.UP_PWD);
			log.setAuthorId(this.getUid());
			log.setData(null);
			log.setIp(Tools.getIpAddr(request));
			logService.save(log);
			json.put("success", true);
			json.put("msg", "保存成功");
			return json.toString();
        } catch (Exception e){
            String msg = "密码修改失败";
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
	
	@RequestMapping(value = "/backup", method = RequestMethod.POST)
    public @ResponseBody String backup(@RequestParam String bk_type, @RequestParam String bk_path,
    		HttpServletRequest request) {
		
		JSONObject json=new JSONObject();
        if (StringUtils.isBlank(bk_type)) {
        	
        	json.put("success", false);
    		json.put("msg", "请确认信息输入完整");
            return json.toString();
        }
        try {
            BackResponse backResponse = siteService.backup(bk_type, bk_path, "yyyyMMddHHmm");
            Tlogs log =new Tlogs();
			log.setAction(LogActions.SYS_BACKUP);
			log.setAuthorId(this.getUid());
			log.setData(null);
			log.setIp(Tools.getIpAddr(request));
			logService.save(log);
			json.put("success", true);
			json.put("msg", "备份成功");
			json.put("payload", JSONObject.fromObject(backResponse));
            return json.toString();
        } catch (Exception e){
            String msg = "备份失败";
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
	
	@RequestMapping(value = "/advanced", method = RequestMethod.GET)
    public  String advanced(ModelMap model){
        Map<String, String> map = new HashMap<>();
        List<Toptions> options = optionsService.getOptions();
        options.forEach(option ->{
			map.put(option.getName(), option.getValue());
		});
        model.addAttribute("options", map);
        return "admin/advanced";
    }
	
	@RequestMapping(value = "/advanced", method = RequestMethod.POST)
    public @ResponseBody String doAdvanced(@RequestParam String cache_key, @RequestParam String block_ips, @RequestParam String plugin_name){
        // 清除缓存
        if(StringUtils.isNotBlank(cache_key)){
            if(cache_key.equals("*")){
                cache.clean();
            } else {
                cache.del(cache_key);
            }
        }
        Toptions option=new Toptions();
        // 要过过滤的黑名单列表
        if(StringUtils.isNotBlank(block_ips)){
        	option.setName(Types.BLOCK_IPS);
        	option.setValue(block_ips);
        	optionsService.saveOption(option);
            TaleConst.BLOCK_IPS.addAll(Arrays.asList(StringUtils.split(block_ips, ",")));
        } else {
        	option.setName(Types.BLOCK_IPS);
        	option.setValue("");
        	optionsService.saveOption(option);
            TaleConst.BLOCK_IPS.clear();
        }
        // 处理卸载插件
        if(StringUtils.isNotBlank(plugin_name)){
            String key = "plugin_";
            // 卸载所有插件
            if(!"*".equals(plugin_name)){
                key = "plugin_" + plugin_name;
            } else {
            	option.setName(Types.ATTACH_URL);
            	option.setValue(Commons.site_url());
            	optionsService.saveOption(option);
            }
            optionsService.deleteOption(key);
        }
        JSONObject json=new JSONObject();
        json.put("success", true);
		json.put("msg", "成功");
        return json.toString();
    }
}
