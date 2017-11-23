package org.esmart.tale.model;

import java.io.Serializable;

/**
 * 
* @ClassName: Trelationships 
* @Description: TODO 内容与元数据中间表 
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年10月31日 下午2:15:06
 */
public class Trelationships implements Serializable {
    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;

	private Integer cid;

    private Integer mid;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public Integer getMid() {
        return mid;
    }

    public void setMid(Integer mid) {
        this.mid = mid;
    }
}