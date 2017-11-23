package org.esmart.tale.controller.base;


import org.esmart.tale.model.Tuser;
import org.esmart.tale.utils.MapCache;
import org.esmart.tale.utils.TaleUtils;

public abstract class BaseController {

	
	public static String THEME = "themes/default";
	 
	protected MapCache cache = MapCache.single();
	
	public String render(String viewName) {
        return THEME + "/" + viewName;
    }
	
    public String render_404() {
        return "/comm/error_404";
    }
    public Tuser user() {
        return TaleUtils.getLoginUser();
    }

    public Integer getUid(){
        return this.user().getUid();
    }

}
