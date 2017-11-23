package org.esmart.tale.dto;

import java.io.Serializable;

/**
 * 
* @ClassName: BackResponse 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年10月31日 下午2:36:29
 */
public class BackResponse implements Serializable {

    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	private String attach_path;
    private String theme_path;
    private String sql_path;

    public String getAttach_path() {
        return attach_path;
    }

    public void setAttach_path(String attach_path) {
        this.attach_path = attach_path;
    }

    public String getTheme_path() {
        return theme_path;
    }

    public void setTheme_path(String theme_path) {
        this.theme_path = theme_path;
    }

    public String getSql_path() {
        return sql_path;
    }

    public void setSql_path(String sql_path) {
        this.sql_path = sql_path;
    }
}
