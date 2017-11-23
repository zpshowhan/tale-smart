package org.esmart.tale.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.esmart.tale.mapper.TlogsMapper;
import org.esmart.tale.model.Tlogs;
import org.esmart.tale.service.LogService;
import org.esmart.tale.utils.DateUtil;
import org.esmart.tale.utils.TaleConst;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("logService")
@Transactional
public class LogServiceImpl implements LogService {

	@Resource
	private TlogsMapper logsMapper;
	
	@Override
	public void save(Tlogs log) {
		// TODO Auto-generated method stub
		log.setCreated(DateUtil.currentDateInt());
		logsMapper.insert(log);
	}

	@Override
	public PageInfo<Tlogs> getLogs(int page, int limit) {
		// TODO Auto-generated method stub
		if (page <= 0) {
            page = 1;
        }

        if (limit < 1 || limit > TaleConst.MAX_POSTS) {
            limit = 10;
        }
        //分页处理
        PageHelper.startPage(page, limit);
        //分页查询
        List<Tlogs> logs=logsMapper.selectAll();
        
        return new PageInfo<Tlogs>(logs);
	}

}
