package org.esmart.tale.mapper;

import java.util.List;

import org.esmart.tale.model.Toptions;

public interface ToptionsMapper {
    int deleteByPrimaryKey(String name);

    int insert(Toptions record);

    int insertSelective(Toptions record);

    Toptions selectByPrimaryKey(String name);

    int updateByPrimaryKeySelective(Toptions record);

    int updateByPrimaryKey(Toptions record);
    
    List<Toptions> selectAll();
    
    int selectCount(Toptions record);
}