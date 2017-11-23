package org.esmart.tale.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.esmart.tale.mapper.ToptionsMapper;
import org.esmart.tale.model.Toptions;
import org.esmart.tale.service.OptionsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("optionsService")
@Transactional
public class OptionsServiceImpl implements OptionsService {

	@Resource
	private ToptionsMapper optionsMapper;
	
	@Override
	public void saveOptions(List<Toptions> options) {
		// TODO Auto-generated method stub
		options.forEach(option->{
			saveOption(option);
		});
	}

	@Override
	public void saveOption(Toptions option) {
		// TODO Auto-generated method stub
		Toptions old=new Toptions();
		old.setName(option.getName());
		int count = optionsMapper.selectCount(old);
		if (count == 0) {
			optionsMapper.insertSelective(option);
        } else {
        	optionsMapper.updateByPrimaryKeySelective(option);
        }
	}

	@Override
	public List<Toptions> getOptions() {
		// TODO Auto-generated method stub
		return optionsMapper.selectAll();
	}

	@Override
	public void deleteOption(String key) {
		// TODO Auto-generated method stub
		optionsMapper.deleteByPrimaryKey(key);
	}

	@Override
	public Toptions getOptionsOne(String key) {
		// TODO Auto-generated method stub
		return optionsMapper.selectByPrimaryKey(key);
	}

}
