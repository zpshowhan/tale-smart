package org.esmart.tale.service.impl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.mapper.TcontentsMapper;
import org.esmart.tale.mapper.TmetasMapper;
import org.esmart.tale.mapper.TrelationshipsMapper;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.model.Tmetas;
import org.esmart.tale.model.Trelationships;
import org.esmart.tale.service.ContentsService;
import org.esmart.tale.utils.DateUtil;
import org.esmart.tale.utils.TaleUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;

@Service("contentsService")
@Transactional
public class ContentsServiceImpl implements  ContentsService{

	@Resource
	private TcontentsMapper contentsMapper;
	@Resource
	private TmetasMapper metasMapper;
	@Resource
	private TrelationshipsMapper relationshipsMapper;
	
	@Override
	public Tcontents byId(int id) {
		// TODO Auto-generated method stub
		return contentsMapper.selectByPrimaryKey(id);
	}

	@Override
	public Tcontents bySlug(String slug) {
		// TODO Auto-generated method stub
		
		return contentsMapper.selectBySlug(slug);
	}

	@Override
	public PageInfo<Tcontents> getArticles(String type, Object obj, int page, int limit) {
		// TODO Auto-generated method stub
    	//未开发使用
		Map<String,Object> map=new HashMap<String,Object>();
		map.put(type, obj);
		//分页处理
        PageHelper.startPage(page, limit);
		List<Tcontents> queryByMap = contentsMapper.queryByMap(map);
		PageInfo<Tcontents> pages=new PageInfo<Tcontents>(queryByMap);
		return pages;
	}

	@Override
	public void publish(Tcontents contents) {
		// TODO Auto-generated method stub
		
		if (null == contents)
            throw new TalException("文章对象为空");
        if (StringUtils.isBlank(contents.getTitle()))
            throw new TalException("文章标题不能为空");
        if (StringUtils.isBlank(contents.getContent()))
            throw new TalException("文章内容不能为空");
        if (contents.getTitle().length() > 200)
            throw new TalException("文章标题过长");
        if (contents.getContent().length() > 10000)
            throw new TalException("文章内容过长");
        if (null == contents.getAuthorId())
            throw new TalException("请登录后发布文章");

        if (StringUtils.isNotBlank(contents.getSlug())) {
            if(contents.getSlug().length() < 5){
                throw new TalException("路径太短了");
            }
            if (!TaleUtils.isPath(contents.getSlug())) throw new TalException("您输入的路径不合法");

            Tcontents record=new Tcontents();
            record.setSlug(contents.getSlug());
            record.setType(contents.getType());
            int count = contentsMapper.selectByObj(record);
            
            if (count > 0) throw new TalException("该路径已经存在，请重新输入");
        }

        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        int time = DateUtil.currentDateInt();
        contents.setCreated(time);
        contents.setModified(time);

        String tags = contents.getTags();
        String categories = contents.getCategories();

        Integer cid = contentsMapper.insert(contents);
        
        String[] splitTag = tags.split(",");
        String[] splitCategorie = categories.split(",");
        List<String> tagList = Arrays.asList(splitTag);
        List<String> categorieList = Arrays.asList(splitCategorie);
        //tags 遍历
        tagList.forEach(tag->{
        	this.saveOrUpdate(cid, tag, "tag");
        });
        //categorys 遍历
        categorieList.forEach(cate->{
        	this.saveOrUpdate(cid, cate, "category");
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
	public void updateArticle(Tcontents contents) {
		// TODO Auto-generated method stub
		if (null == contents || null == contents.getCid()) {
            throw new TalException("文章对象不能为空");
        }
        if (StringUtils.isBlank(contents.getTitle())) {
            throw new TalException("文章标题不能为空");
        }
        if (StringUtils.isBlank(contents.getContent())) {
            throw new TalException("文章内容不能为空");
        }
        if (contents.getTitle().length() > 200) {
            throw new TalException("文章标题过长");
        }
        if (contents.getContent().length() > 10000) {
            throw new TalException("文章内容过长");
        }
        if (null == contents.getAuthorId()) {
            throw new TalException("请登录后发布文章");
        }
        int time = DateUtil.currentDateInt();
        contents.setModified(time);

        Integer cid = contents.getCid();

        contents.setContent(EmojiParser.parseToAliases(contents.getContent()));

        contentsMapper.updateByPrimaryKeySelective(contents);
        
        Trelationships relationship=new Trelationships();
        relationship.setCid(cid);
        relationshipsMapper.deleteByPrimaryKey(relationship);
        
        String tags = contents.getTags();
        String categories = contents.getCategories();
        
        String[] splitTag = tags.split(",");
        String[] splitCategorie = categories.split(",");
        List<String> tagList = Arrays.asList(splitTag);
        List<String> categorieList = Arrays.asList(splitCategorie);
        //tags 遍历
        tagList.forEach(tag->{
        	this.saveOrUpdate(cid, tag, "tag");
        });
        //categorys 遍历
        categorieList.forEach(cate->{
        	this.saveOrUpdate(cid, cate, "category");
        });
	}

	@Override
	public void update(Tcontents contents) {
		// TODO Auto-generated method stub
		contentsMapper.updateByPrimaryKeySelective(contents);
	}

	@Override
	public void delete(int cid) {
		// TODO Auto-generated method stub
		contentsMapper.deleteByPrimaryKey(cid);
		Trelationships relationship=new Trelationships();
        relationship.setCid(cid);
        relationshipsMapper.deleteByPrimaryKey(relationship);
	}

	@Override
	public PageInfo<Tcontents> getArticles(Integer mid, int page, int limit) {
		// TODO Auto-generated method stub
		PageHelper.startPage(page, limit);
		List<Tcontents> contents = contentsMapper.selectByShipId(mid);
		PageInfo<Tcontents> pages=new PageInfo<Tcontents>(contents);
		
		return pages;
	}

	@Override
	public PageInfo<Tcontents> getArticles(Map<String, Object> map, int page, int limit) {
		// TODO Auto-generated method stub
		//分页处理
        PageHelper.startPage(page, limit);
		List<Tcontents> queryByMap = contentsMapper.queryByMap(map);
		PageInfo<Tcontents> pages=new PageInfo<Tcontents>(queryByMap);
		return pages;
	}

}
