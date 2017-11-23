package org.esmart.tale.mapper;

import java.util.List;

import org.esmart.tale.model.Trelationships;

public interface TrelationshipsMapper {
    int deleteByPrimaryKey(Trelationships key);

    int insert(Trelationships record);

    int insertSelective(Trelationships record);
    
    int queryCountByMid(int mid);
    
    int queryCountByObj(Trelationships record);
    
    List<Trelationships> queryObj(Trelationships record);
}