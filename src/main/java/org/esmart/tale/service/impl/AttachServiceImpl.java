package org.esmart.tale.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.esmart.tale.mapper.TattachMapper;
import org.esmart.tale.model.Tattach;
import org.esmart.tale.service.AttachService;
import org.esmart.tale.utils.TaleConst;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("attachService")
@Transactional
public class AttachServiceImpl implements AttachService {

	@Resource
	private TattachMapper attachMapper;
	
	@Override
	public void save(Tattach attach) {
		// TODO Auto-generated method stub
		attachMapper.insert(attach);
	}

	@Override
	public Tattach byId(Integer id) {
		// TODO Auto-generated method stub
		if(id!=null){
			
			return attachMapper.selectByPrimaryKey(id);
		}
		return null;
	}

	@Override
	public void delete(Integer id) {
		// TODO Auto-generated method stub
		if(id!=null){
			
			attachMapper.deleteByPrimaryKey(id);
		}
		
	}

	@Override
	public PageInfo<Tattach> getAttachs(int page, int limit) {
		// TODO Auto-generated method stub
		if (page <= 0) {
            page = 1;
        }

        if (limit < 1 || limit > TaleConst.MAX_POSTS) {
            limit = 20;
        }
        //分页处理
        PageHelper.startPage(page, limit);
        List<Tattach> attas = attachMapper.queryAll();
        PageInfo<Tattach> pageInfo=new  PageInfo<Tattach>(attas);
        
		return pageInfo;
	}

}
