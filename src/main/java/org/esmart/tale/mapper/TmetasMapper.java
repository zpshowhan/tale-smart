package org.esmart.tale.mapper;

import java.util.List;
import java.util.Map;

import org.esmart.tale.dto.MetaDto;
import org.esmart.tale.model.Tmetas;

public interface TmetasMapper {
    int deleteByPrimaryKey(Integer mid);

    int insert(Tmetas record);

    int insertSelective(Tmetas record);

    Tmetas selectByPrimaryKey(Integer mid);

    int updateByPrimaryKeySelective(Tmetas record);

    int updateByPrimaryKey(Tmetas record);
    
    List<Tmetas> selectByMap(Map<String,String> map);
    
    List<Tmetas> selectByType(String type);
    
    MetaDto selectMetaDtaByMap(Map<String,String> map);
    
    List<MetaDto> selectMetaDtasByMap(Map<String,String> map);
    
    List<MetaDto> randomMetaDtasByMap(Map<String,String> map);
}