package org.esmart.tale.mapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esmart.tale.model.Tcontents;

public interface TcontentsMapper {
    int deleteByPrimaryKey(Integer cid);

    int insert(Tcontents record);

    int insertSelective(Tcontents record);

    Tcontents selectByPrimaryKey(Integer cid);

    int updateByPrimaryKeySelective(Tcontents record);

    int updateByPrimaryKeyWithBLOBs(Tcontents record);

    int updateByPrimaryKey(Tcontents record);
    
    Tcontents selectBySlug(String slug);
    
    List<Tcontents> selectByAuthorId(Integer id);
    
    int selectByObj(Tcontents record);
    
    List<Tcontents> queryByObj(Tcontents record);
    
    List<Tcontents> queryByRandObj(Tcontents record);
    
    List<Tcontents> selectByShipId(Integer id);
    
    List<HashMap<String,Object>> selectMap();
    
    List<Tcontents> queryByMap(Map map);
    
    Tcontents queryNHOne(Map map);
}