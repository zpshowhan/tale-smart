package org.esmart.tale.mapper;

import java.util.List;

import org.esmart.tale.model.Tlogs;

public interface TlogsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tlogs record);

    int insertSelective(Tlogs record);

    Tlogs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tlogs record);

    int updateByPrimaryKey(Tlogs record);
    
    List<Tlogs> selectAll();
}