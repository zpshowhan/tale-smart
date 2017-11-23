package org.esmart.tale.dto;

import org.esmart.tale.model.Tmetas;

/**
 * 
* @ClassName: MetaDto 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年10月31日 下午2:39:09
 */
public class MetaDto extends Tmetas {

    /** 
	* @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	*/ 
	private static final long serialVersionUID = 1L;
	
	private int count;
    
    public MetaDto(Tmetas meta){
    	setMid(meta.getMid());
    	setName(meta.getName());
    	setParent(meta.getParent());
    	setDescription(meta.getDescription());
    	setSlug(meta.getSlug());
    	setSort(meta.getSort());
    	setType(meta.getType());
    }
    public MetaDto(){
    	super();
    	this.setMid(super.getMid());
    	this.setName(super.getName());
    	this.setParent(super.getParent());
    	this.setDescription(super.getDescription());
    	this.setSlug(super.getSlug());
    	this.setSort(super.getSort());
    	this.setType(super.getType());
    	this.count=count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
    
}
