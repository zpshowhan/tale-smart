package org.esmart.tale.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.LogActions;
import org.esmart.tale.dto.ThemeDto;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.jetag.Commons;
import org.esmart.tale.model.Tlogs;
import org.esmart.tale.model.Toptions;
import org.esmart.tale.service.LogService;
import org.esmart.tale.service.OptionsService;
import org.esmart.tale.utils.TaleUtils;
import org.esmart.tale.utils.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;


@Controller
@RequestMapping("/admin/themes")
public class ThemeController extends BaseController{

	private static Logger logger = Logger.getLogger(ThemeController.class);
	@Resource
	private OptionsService optionsService;
	@Resource
	private LogService logService;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
    public String index(ModelMap model) {
        // 读取主题
        String         themesDir  = TaleUtils.UP_DIR + "/templates/themes";
        File[]         themesFile = new File(themesDir).listFiles();
        List<ThemeDto> themes     = new ArrayList<>(themesFile.length);
        for (File f : themesFile) {
            if (f.isDirectory()) {
                ThemeDto themeDto = new ThemeDto(f.getName());
                if (Files.exists(Paths.get(f.getPath() + "/setting.html"))) {
                    themeDto.setHasSetting(true);
                }
                themes.add(themeDto);
            }
        }
        model.addAttribute("current_theme", Commons.site_theme());
        model.addAttribute("themes", themes);
        return "admin/themes";
    }

	@RequestMapping(value = "active")
    public @ResponseBody String activeTheme(ModelMap model, @RequestParam String site_theme,HttpServletRequest request) {
		JSONObject json=new JSONObject();
		try {
        	Toptions option=new Toptions();
        	option.setName("site_theme");
        	option.setValue(site_theme);
            optionsService.saveOption(option);
            optionsService.deleteOption("theme_option_");

            BaseController.THEME = "themes/" + site_theme;

            String themePath = "/templates/themes/" + site_theme;
            
            Tlogs log =new Tlogs();
			log.setAction(LogActions.THEME_SETTING);
			log.setAuthorId(this.getUid());
			log.setData("变更主题为"+site_theme);
			log.setIp(Tools.getIpAddr(request));
			logService.save(log);
			
			json.put("success", true);
            json.put("msg", "成功");
            
            return json.toString();
        } catch (Exception e) {
            String msg = "主题启用失败";
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
