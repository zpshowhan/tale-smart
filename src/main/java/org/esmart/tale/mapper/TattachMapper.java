package org.esmart.tale.mapper;

import java.util.List;

import org.esmart.tale.model.Tattach;

public interface TattachMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tattach record);

    int insertSelective(Tattach record);

    Tattach selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tattach record);

    int updateByPrimaryKey(Tattach record);
    
    List<Tattach> queryAll();
}