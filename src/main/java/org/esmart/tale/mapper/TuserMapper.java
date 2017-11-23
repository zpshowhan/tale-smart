package org.esmart.tale.mapper;

import java.util.List;

import org.esmart.tale.model.Tuser;

public interface TuserMapper {
    int deleteByPrimaryKey(Integer uid);

    int insert(Tuser record);

    int insertSelective(Tuser record);

    Tuser selectByPrimaryKey(Integer uid);

    int updateByPrimaryKeySelective(Tuser record);

    int updateByPrimaryKey(Tuser record);
    
    List<Tuser> selectUsersByParam(Tuser record);
}