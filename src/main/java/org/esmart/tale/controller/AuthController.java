package org.esmart.tale.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.LogActions;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.model.Tlogs;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.service.LogService;
import org.esmart.tale.service.TuserService;
import org.esmart.tale.utils.DateUtil;
import org.esmart.tale.utils.TaleConst;
import org.esmart.tale.utils.TaleUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;
/**
 * 
* @ClassName: AuthController 
* @Description: TODO 登录退出控制器 
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年11月3日 下午4:27:10
 */
@Controller
public class AuthController extends BaseController{

	private static Logger logger = Logger.getLogger(AuthController.class);
	@Resource
	private LogService logService;
	
	@Resource
	private TuserService userService;
	
	@RequestMapping(value="/admin/login" ,method=RequestMethod.GET)
	public String login(String addr,ModelMap model){
		if(null != this.user()){
			return "redirect:/admin/index "; 
		}
		if(StringUtils.isNotBlank(addr)){
			
			model.addAttribute("addr", addr);
		}
		return "admin/login";
	}
	
	@RequestMapping(value="/admin/login" ,method=RequestMethod.POST)
	@ResponseBody
	public String login(String username,String password, String remeber_me, String addr, HttpServletRequest request,HttpServletResponse response){
		
		HttpSession session = request.getSession();
		JSONObject json =new JSONObject();
		Integer error_count = cache.get("login_error_count");
		try {
			Tuser user = userService.login(username, password);
            session.setAttribute(TaleConst.LOGIN_SESSION_KEY, user);
            if (StringUtils.isNotBlank(remeber_me)) {
                TaleUtils.setCookie(response, user.getUid());
            }
           
            Tuser temp = new Tuser();
            temp.setUid(user.getUid());
            temp.setLogged(DateUtil.currentDateInt());
            userService.updateByPrimaryKeySelective(temp);
            Tlogs log =new Tlogs();
            log.setAction(LogActions.LOGIN);
            log.setAuthorId(user.getUid());
            log.setIp(request.getRemoteAddr());
            logService.save(log);
        } catch (Exception e) {
            error_count = null == error_count ? 1 : error_count + 1;
            if(null != error_count && error_count > 3){
            	json.put("success", false);
            	json.put("msg", "您输入密码已经错误超过3次，请10分钟后尝试");
                return json.toString();
            }
            cache.set("login_error_count", error_count, 10 * 60);
            String msg = "登录失败";
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
    	json.put("msg", "登录成功");
    	if(StringUtils.isNotBlank(addr)){
    		
    		json.put("addr", addr);
    	}
		return json.toString();
	}
	
}
