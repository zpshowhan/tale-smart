package org.esmart.tale.service;

import org.esmart.tale.model.Tuser;

public interface TuserService {

    public int deleteByPrimaryKey(Integer uid);

    public int insert(Tuser record);

    public int insertSelective(Tuser record);

    public Tuser selectByPrimaryKey(Integer uid);

    public int updateByPrimaryKeySelective(Tuser record);

    public int updateByPrimaryKey(Tuser record);
    
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public Tuser login(String username, String password);
    
}
