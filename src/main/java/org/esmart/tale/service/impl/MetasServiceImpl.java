package org.esmart.tale.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.esmart.tale.dto.MetaDto;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.mapper.TcontentsMapper;
import org.esmart.tale.mapper.TmetasMapper;
import org.esmart.tale.mapper.TrelationshipsMapper;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.model.Tmetas;
import org.esmart.tale.model.Trelationships;
import org.esmart.tale.service.MetasService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("metasService")
@Transactional
public class MetasServiceImpl implements MetasService {

	@Resource
	private TmetasMapper metasMapper;
	@Resource
	private TrelationshipsMapper relationshipsMapper;
	@Resource
	private TcontentsMapper contentsMapper ;
	
	@Override
	public MetaDto getMeta(String type, String name) {
		// TODO Auto-generated method stub
		Map<String ,String> map =new HashMap<String ,String>();
		map.put("type", type);
		map.put("name", name);
		/*List<Tmetas> metas = metasMapper.selectByMap(map);
		
		if(metas!=null&&metas.size()!=0){
			
			int count = relationshipsMapper.queryCountByMid(metas.get(0).getMid());
			MetaDto metad=new MetaDto(metas.get(0));
			metad.setCount(count);
			return metad;
		}*/
		return metasMapper.selectMetaDtaByMap(map);
	}

	@Override
	public List<Tmetas> getMetas(String types) {
		// TODO Auto-generated method stub
		
		return metasMapper.selectByType(types);
	}

	@Override
	public void saveMetas(Integer cid, String names, String type) {
		// TODO Auto-generated method stub

		String[] splitNames = names.split(",");
		List<String> nameList = Arrays.asList(splitNames);
		nameList.forEach(name->{
			this.saveOrUpdate(cid, name, type);
		});
		
	}
	private void saveOrUpdate(Integer cid, String name, String type) {
		Map<String,String> map=new HashMap<String,String>();
    	map.put("name", name);
    	map.put("type", type);
    	List<Tmetas> matas = metasMapper.selectByMap(map);
    	int mid = 0;
    	if(null==matas||matas.size()==0){
    		Tmetas meta=new Tmetas();
    		meta.setSlug(name);
            meta.setName(name);
            meta.setType(type);
            mid=metasMapper.insert(meta);
    	}else{
    		mid=matas.get(0).getMid();
    	}
    	if(mid!=0){
    		Trelationships relationship=new Trelationships();
    		int reCount = relationshipsMapper.queryCountByObj(relationship);
    		if(reCount==0){
    			Trelationships relationship2=new Trelationships();
    			relationship2.setCid(cid);
    			relationship2.setMid(mid);
    			relationshipsMapper.insert(relationship2);
    		}
    	}
	}
	@Override
	public void delete(int mid) {
		// TODO Auto-generated method stub
		Tmetas meta = metasMapper.selectByPrimaryKey(mid);
		if (null != meta) {
			String type = meta.getType();
            String name = meta.getName();
            metasMapper.deleteByPrimaryKey(mid);
            List<Tcontents> contents = contentsMapper.selectByShipId(mid);
            if(contents!=null){
            	
            	contents.forEach(content->{
            		if(content!=null){
            			boolean isUpdate = false;
            			Tcontents cont=new Tcontents();
            			if (type.equals("category")) {
            				cont.setCategories(reMeta(name, content.getCategories()));
                            isUpdate = true;
                        }
            			if (type.equals("tag")) {
            				cont.setTags(reMeta(name, content.getTags()));
                            isUpdate = true;
                        }
            			if(isUpdate){
            				contentsMapper.updateByPrimaryKeySelective(cont);
            			}
            			
            		}
            	});
            }
            Trelationships relationship=new Trelationships();
            relationship.setMid(mid);
            relationshipsMapper.deleteByPrimaryKey(relationship);
		}
	}

	@Override
	public void saveMeta(String type, String name, Integer mid) {
		// TODO Auto-generated method stub
		Map<String,String> map=new HashMap<String,String>();
    	map.put("name", name);
    	map.put("type", type);
		List<Tmetas> metas = metasMapper.selectByMap(map);
		
		if (null == metas||metas.size()<=0) {
            Tmetas meta = new Tmetas();
            if (null != mid) {
            	
            	meta.setMid(mid);
            	meta.setName(name);
            	metasMapper.updateByPrimaryKeySelective(meta);
            } else {
            	meta.setType(type);
            	meta.setName(name);
            	metasMapper.insert(meta);
            }
        }else{
        	throw new TalException("已经存在该项");
        }
		
	}

	@Override
	public void saveMeta(Tmetas metas) {
		// TODO Auto-generated method stub
		metasMapper.insert(metas);
	}

	@Override
	public void update(Tmetas metas) {
		// TODO Auto-generated method stub
		metasMapper.updateByPrimaryKeySelective(metas);

	}
	private String reMeta(String name, String metas) {
        String[] ms = metas.split( ",");
        StringBuffer sbuf = new StringBuffer();
        for (String m : ms) {
            if (!name.equals(m)) {
                sbuf.append(",").append(m);
            }
        }
        if (sbuf.length() > 0) {
            return sbuf.substring(1);
        }
        return "";
    }

}
