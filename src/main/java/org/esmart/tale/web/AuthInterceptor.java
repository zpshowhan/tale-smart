package org.esmart.tale.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.esmart.tale.dto.Types;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.utils.MapCache;
import org.esmart.tale.utils.TaleConst;
import org.esmart.tale.utils.TaleUtils;
import org.esmart.tale.utils.Tools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * mvc拦截器
 * @author Thinkpad
 *
 */
public class AuthInterceptor implements HandlerInterceptor {

	private static final Logger LOGGE = LoggerFactory.getLogger(AuthInterceptor.class);
	private List<String> excludeUrls;// 不需要拦截的资源
	
    public List<String> getExcludeUrls() {
        return excludeUrls;
    }
    public void setExcludeUrls(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls;
    }
    private MapCache cache = MapCache.single();
    
	//在业务处理器处理请求之前被调用
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		//获取访问路径
		String reqUri = request.getRequestURI();
		String ip = Tools.getIpAddr(request);
		//获取session
		HttpSession session = request.getSession();
		
		
		LOGGE.info("UserAgent: {}", request.getHeader("user-agent"));
        LOGGE.info("用户访问地址: {}, 来路地址: {}", reqUri, ip);
		
        //只拦截          /admin/***
        if(reqUri.indexOf("admin")!=-1){
        	
        	//1、如果开启了免登陆
        	Integer uid = TaleUtils.getCookieUid(request);
        	if(null != uid){
        		Tuser user = new Tuser();
        		user.setUid(uid);
        		session.setAttribute(TaleConst.LOGIN_SESSION_KEY, user);
        	}
        	
        	//2、先踢出不处理的地址
        	/*for (int i = 0; i < getExcludeUrls().size(); i++) {
        		
        		if(reqUri.indexOf(getExcludeUrls().get(i))!=-1){
        			
        			return true;
        		}
        	}*/
        	//3、当前没有登录用户
        	if(null==session.getAttribute(TaleConst.LOGIN_SESSION_KEY)){
        		outInfo(request, response);
        		
        		return false;
        	}
        }
		
        request.setAttribute("plugin_menus", TaleConst.plugin_menus);
		String method = request.getMethod();
        if(method.equals("GET")){
            String csrf_token = UUID.randomUUID().toString();
            // 默认存储30分钟
            cache.hset(Types.CSRF_TOKEN, csrf_token, reqUri, 30*60);
            request.setAttribute("_csrf_token", csrf_token);
        }
		return true;
		
	}

	//在业务处理器处理请求执行完成后,生成视图之前执行
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	//完全处理完请求后被调用,可用于清理资源等
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		String _csrf_token = request.getParameter("_csrf_token");
        if(StringUtils.isNotBlank(_csrf_token)){
            // 移除本次token
            cache.hdel(Types.CSRF_TOKEN, _csrf_token);
        }
	}

	private void outInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		StringBuilder builder = new StringBuilder();
		builder.append("<script type=\"text/javascript\" charset=\"UTF-8\">");
		// builder.append("alert(\"本系统需要您登录后才能使用\");");
		builder.append("window.top.location.href=\"");
		builder.append(request.getContextPath()+"/admin/login?login");
		builder.append("&addr=");
		builder.append(intoBase(request, response));
		builder.append("\";</script>");
		out.print(builder.toString());
		out.close();
	}
	private String intoBase(HttpServletRequest request, HttpServletResponse response){
		String basePath = request.getScheme() + "://"
				+ request.getServerName() + ":"
				+ request.getServerPort() + request.getRequestURI()
				+ "?" + request.getQueryString();
		return basePath;
	}
}
