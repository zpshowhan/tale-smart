package org.esmart.tale.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.mapper.TuserMapper;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.service.TuserService;
import org.esmart.tale.utils.Tools;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class TuserServiceImpl implements TuserService {

	@Resource
	private TuserMapper userMapper;
	
	public int deleteByPrimaryKey(Integer uid) {
		// TODO Auto-generated method stub
		return userMapper.deleteByPrimaryKey(uid);
	}

	public int insert(Tuser record) {
		// TODO Auto-generated method stub
		return userMapper.insert(record);
	}

	public int insertSelective(Tuser record) {
		// TODO Auto-generated method stub
		return userMapper.insertSelective(record);
	}

	public Tuser selectByPrimaryKey(Integer uid) {
		// TODO Auto-generated method stub
		return userMapper.selectByPrimaryKey(uid);
	}

	public int updateByPrimaryKeySelective(Tuser record) {
		// TODO Auto-generated method stub
		return userMapper.updateByPrimaryKeySelective(record);
	}

	public int updateByPrimaryKey(Tuser record) {
		// TODO Auto-generated method stub
		return userMapper.updateByPrimaryKey(record);
	}

	@Override
	public Tuser login(String username, String password) {
		// TODO Auto-generated method stub
		Tuser user=new Tuser();
		if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            throw new TalException("用户名和密码不能为空");
        }
		user.setUsername(username);
		List<Tuser> tusers = userMapper.selectUsersByParam(user);
		if(null==tusers||tusers.size()==0){
			throw new TalException("不存在该用户");
		}
        String pwd = Tools.md5(username, password);
        user.setPassword(pwd);
        tusers =userMapper.selectUsersByParam(user);
        if (null == tusers||tusers.size()==0) {
            throw new TalException("用户名或密码错误");
        }
		return tusers.get(0);
	}

}
