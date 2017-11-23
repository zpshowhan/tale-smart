package org.esmart.tale.mapper;

import java.util.List;

import org.esmart.tale.model.Tcomments;

public interface TcommentsMapper {
    int deleteByPrimaryKey(Integer coid);

    int insert(Tcomments record);

    int insertSelective(Tcomments record);

    Tcomments selectByPrimaryKey(Integer coid);

    int updateByPrimaryKeySelective(Tcomments record);

    int updateByPrimaryKeyWithBLOBs(Tcomments record);

    int updateByPrimaryKey(Tcomments record);
    
    List<Tcomments> selectAll();
    /**
     * 根据不同的要求查询集合
    *
    * @Title: selectComments 
    * @Description: TODO(这里用一句话描述这个方法的作用) 
    * @param @param record
    * @param @return    设定文件 
    * @return List<Tcomments>    返回类型 
    * @throws
     */
    List<Tcomments> selectComments(Tcomments record);
}