package org.esmart.tale.service;

import org.esmart.tale.model.Tattach;

import com.github.pagehelper.PageInfo;

public interface AttachService {

	public void save(Tattach attach);
	
	public Tattach byId(Integer id);
	
	public void delete(Integer id);
	
	public PageInfo<Tattach> getAttachs(int page ,int limit);
}
