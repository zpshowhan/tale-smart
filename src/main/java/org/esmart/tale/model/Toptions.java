package org.esmart.tale.model;

import java.io.Serializable;

/**
 * 
* @ClassName: Toptions 
* @Description: TODO  配置表
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年10月31日 下午2:08:05
 */
public class Toptions implements Serializable {
    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;

	private String name;

    private String value;

    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }
}