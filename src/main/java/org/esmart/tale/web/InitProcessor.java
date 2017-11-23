package org.esmart.tale.web;

import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.jetag.Commons;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * 
* @ClassName: InitProcessor 
* @Description: TODO  初始化主题
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年11月17日 上午11:01:11
 */
@Component
public class InitProcessor implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent arg0) {
		// TODO Auto-generated method stub
		BaseController.THEME = "themes/"  + Commons.site_option("site_theme");
		
		Commons.THEME="themes/"  + Commons.site_option("site_theme");
	}

}
