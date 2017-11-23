package org.esmart.tale.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.esmart.tale.dto.Archive;
import org.esmart.tale.dto.BackResponse;
import org.esmart.tale.dto.Comment;
import org.esmart.tale.dto.JdbcConf;
import org.esmart.tale.dto.LogActions;
import org.esmart.tale.dto.MetaDto;
import org.esmart.tale.dto.Statistics;
import org.esmart.tale.dto.Types;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.jetag.Theme;
import org.esmart.tale.mapper.TattachMapper;
import org.esmart.tale.mapper.TcommentsMapper;
import org.esmart.tale.mapper.TcontentsMapper;
import org.esmart.tale.mapper.TlogsMapper;
import org.esmart.tale.mapper.TmetasMapper;
import org.esmart.tale.mapper.TuserMapper;
import org.esmart.tale.model.Tcomments;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.model.Tlogs;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.service.SiteService;
import org.esmart.tale.utils.DatabaseBackup;
import org.esmart.tale.utils.DateUtil;
import org.esmart.tale.utils.Endpoint;
import org.esmart.tale.utils.MapCache;
import org.esmart.tale.utils.TaleConst;
import org.esmart.tale.utils.TaleUtils;
import org.esmart.tale.utils.ZipUtils;
import org.esmart.tale.utils.encrypt.MD5Coder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service("siteService")
@Transactional
public class SiteServiceImpl implements SiteService {

	@Resource
	private TuserMapper userMapper;
	
	@Resource
	private TlogsMapper logsMapper;
	
	@Resource
	private TcommentsMapper commentsMapper;
	
	@Resource
	private TcontentsMapper contentsMapper;
	
	@Resource
	private TattachMapper attachMapper;
	
	@Resource
	private TmetasMapper metasMapper;
	
	public MapCache mapCache = new MapCache();
	
	
	@Override
	public void initSite(Tuser user, JdbcConf jdbcConf) {
		// TODO Auto-generated method stub
		String pwd = MD5Coder.getMD5(user.getUsername() + user.getPassword());
		user.setPassword(pwd);
		user.setScreenName(user.getUsername());
		user.setCreated(DateUtil.currentDateInt());
		int uid = userMapper.insert(user);
		try {
            Properties props = new Properties();
            String cp = JdbcConf.class.getClassLoader().getResource("").getPath();

            FileOutputStream fos = new FileOutputStream(cp + "jdbc.properties");

            props.setProperty("db_host", jdbcConf.getDb_host());
            props.setProperty("db_name", jdbcConf.getDb_name());
            props.setProperty("db_user", jdbcConf.getDb_user());
            props.setProperty("db_pass", jdbcConf.getDb_pass());
            props.store(fos, "update jdbc info.");
            fos.close();

            File lock = new File(cp + "install.lock");
            lock.createNewFile();
            Tlogs log =new Tlogs();
            log.setAction(LogActions.INIT_SITE);
            log.setAction("初始化站点，初始化数据库，创建用户");
            log.setIp("");
            log.setAuthorId(uid);
            logsMapper.insert(log);
            
        } catch (Exception e) {
            throw new TalException("初始化站点失败");
        }
	}

	@Override
	public PageInfo<Tcomments> recentComments(int limit) {
		// TODO Auto-generated method stub
		if (limit < 0 || limit > 10) {
            limit = 10;
        }
		PageHelper.startPage(1, limit);
		List<Tcomments> comments = commentsMapper.selectAll();
		PageInfo<Tcomments> page=new PageInfo<Tcomments>(comments);
		return page;
	}

	@Override
	public PageInfo<Tcontents> getContens(String type, int limit) {
		// TODO Auto-generated method stub
		if (limit < 0 || limit > 20) {
            limit = 10;
        }
		PageHelper.startPage(1, limit);
		//最新文章
		if(type.equals(Types.RECENT_ARTICLE)){
			Tcontents content =new Tcontents();
			content.setStatus(Types.PUBLISH);
			content.setType(Types.ARTICLE);
			List<Tcontents> contents = contentsMapper.queryByObj(content);
			PageInfo<Tcontents> page=new PageInfo<Tcontents>(contents);
			return page;
		}
		//随机文章
		if(type.equals(Types.RANDOM_ARTICLE)){
			Tcontents content =new Tcontents();
			content.setStatus(Types.PUBLISH);
			content.setType(Types.ARTICLE);
			List<Tcontents> contents = contentsMapper.queryByRandObj(content);
			PageInfo<Tcontents> page=new PageInfo<Tcontents>(contents);
			return page;
		}
		return null;
	}

	@Override
	public Statistics getStatistics() {
		// TODO Auto-generated method stub
		Statistics statistics = mapCache.get(Types.C_STATISTICS);
		if (null != statistics) {
            return statistics;
        }
		statistics = new Statistics();
		Tcontents content =new Tcontents();
		content.setStatus(Types.PUBLISH);
		content.setType(Types.ARTICLE);
		int articles = contentsMapper.selectByObj(content);
		content.setType(Types.PAGE);
		int pages = contentsMapper.selectByObj(content);
		int comments = commentsMapper.selectAll().size();
		int attachs = attachMapper.queryAll().size();
		int links = metasMapper.selectByType(Types.LINK).size();
		int tags = metasMapper.selectByType(Types.TAG).size();
		int categories = metasMapper.selectByType(Types.CATEGORY).size();
		
		statistics.setArticles(articles);
        statistics.setPages(pages);
        statistics.setComments(comments);
        statistics.setAttachs(attachs);
        statistics.setLinks(links);
        statistics.setTags(tags);
        statistics.setCategories(categories);
        
        mapCache.set(Types.C_STATISTICS, statistics);
        
		return statistics;
	}

	@Override
	public List<Archive> getArchives() {
		// TODO Auto-generated method stub
		List<Archive> archives=new ArrayList<Archive>();
		List<HashMap<String,Object>> listMap = contentsMapper.selectMap();
		if(listMap!=null){
			
			listMap.forEach(map->{
				Archive arc=new Archive();
				String date_str = (String)map.get("date_str");
				arc.setDate_str(date_str);
				Date sd = DateUtil.dateFormat(date_str, "yyyy年MM月");
				arc.setDate(sd);
				int start = DateUtil.getTimeByDate(sd);
				
				Calendar calender = Calendar.getInstance();
                calender.setTime(sd);
                calender.add(Calendar.MONTH, 1);
                Date endSd = calender.getTime();

                int end = DateUtil.getTimeByDate(endSd) - 1;
                
                Map<String,Object> params =new HashMap<String,Object>();
                params.put("type", Types.ARTICLE);
                params.put("status", Types.PUBLISH);
                params.put("start", start);
                params.put("end", end);
                List<Tcontents> contents = contentsMapper.queryByMap(params);
                arc.setArticles(contents);
                archives.add(arc);
			});
		}
		return archives;
	}

	@Override
	public Tcomments getComment(Integer coid) {
		// TODO Auto-generated method stub
		return commentsMapper.selectByPrimaryKey(coid);
	}

	@Override
	public BackResponse backup(String bk_type, String bk_path, String fmt) throws Exception {
		// TODO Auto-generated method stub
		BackResponse backResponse = new BackResponse();
		if (bk_type.equals("attach")) {
            if (StringUtils.isBlank(bk_path)) {
                throw new TalException("请输入备份文件存储路径");
            }
            if (!new File(bk_path).isDirectory()) {
                throw new TalException("请输入一个存在的目录");
            }
            String bkAttachDir = TaleUtils.UP_DIR+ "/upload";
            String bkThemesDir = TaleUtils.UP_DIR + "/templates/themes";

            String fname = DateUtil.dateFormat(new Date(), fmt) + "_" + UUID.randomUUID().toString() + ".zip";

            String attachPath = bk_path + "/" + "attachs_" + fname;
            String themesPath = bk_path + "/" + "themes_" + fname;

            ZipUtils.zipFolder(bkAttachDir, attachPath);
            ZipUtils.zipFolder(bkThemesDir, themesPath);

            backResponse.setAttach_path(attachPath);
            backResponse.setTheme_path(themesPath);
        }
		//导出sql 暂时不处理
        if (bk_type.equals("db")) {

            String bkAttachDir = TaleUtils.UP_DIR + "/upload/";
            if (!new File(bkAttachDir).isDirectory()) {
            	File bkf=new File(bkAttachDir);
            	if(!bkf.exists()){
            		bkf.mkdirs();
            	}
            }
            String sqlFileName = "tale_" + DateUtil.dateFormat(new Date(), fmt) + "_" + UUID.randomUUID().toString() + ".sql";
            String zipFile = sqlFileName.replace(".sql", ".zip");

           Boolean idb= DatabaseBackup.exportDatabase(Endpoint.getJDBC("jdbc.ip"), Endpoint.getJDBC("jdbc.username"), 
        		   Endpoint.getJDBC("jdbc.password"), bkAttachDir, sqlFileName, Endpoint.getJDBC("jdbc.table"));

            File sqlFile = new File(bkAttachDir + sqlFileName);
            
            String zip = bkAttachDir + zipFile;
            ZipUtils.zipFile(sqlFile.getPath(), zip);

            if (!sqlFile.exists()||!idb) {
                throw new TalException("数据库备份失败");
            }
            sqlFile.delete();

            backResponse.setSql_path(zipFile);

            // 10秒后删除备份文件
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    new File(zip).delete();
                }
            }, 10 * 1000);
        }
        return backResponse;
	}

	@Override
	public PageInfo<MetaDto> getMetas(String seachType, String type, int limit) {
		// TODO Auto-generated method stub
		if (StringUtils.isBlank(seachType) || StringUtils.isBlank(type)) {
            return new PageInfo<MetaDto>(new ArrayList<MetaDto>(0));
        }
		if (limit < 1 || limit > TaleConst.MAX_POSTS) {
            limit = 10;
        }
		Map<String ,String> map =new HashMap<String ,String>();
		map.put("type", type);
		// 获取最新的项目
        if (Types.RECENT_META.equals(seachType)) {
        	PageHelper.startPage(1, limit);
        	List<MetaDto> dtos = metasMapper.selectMetaDtasByMap(map);
        	PageInfo<MetaDto> pages= new PageInfo<>(dtos);
        	return pages;
        }
        // 随机获取项目
        if (Types.RANDOM_META.equals(seachType)) {
        	PageHelper.startPage(1, limit);
        	List<MetaDto> dtos = metasMapper.randomMetaDtasByMap(map);
        	PageInfo<MetaDto> pages= new PageInfo<>(dtos);
        	return pages;
        }
		return new PageInfo<MetaDto>(new ArrayList<MetaDto>(0));
	}

	@Override
	public void cleanCache(String key) {
		// TODO Auto-generated method stub
		if (StringUtils.isNotBlank(key)) {
            if ("*".equals(key)) {
                mapCache.clean();
            } else {
                mapCache.del(key);
            }
        }
	}

	@Override
	public Tcontents getNhContent(String type, Integer cid) {
		// TODO Auto-generated method stub
		Map<String ,Object> map =new HashMap<String ,Object>();
		map.put("type", "post");
		map.put("status", "publish");
		if(Types.NEXT.equals(type)){
			map.put("next", cid);
        }
        if(Types.PREV.equals(type)){
        	map.put("prev", cid);
        }
        
        return contentsMapper.queryNHOne(map);
	}

	@Override
	public PageInfo<Comment> getComments(Integer cid, int page, int limit) {
		// TODO Auto-generated method stub
		Tcomments comment =new Tcomments();
		comment.setCid(cid);
		//一级评论
		comment.setParent(0);
		//分页处理
		PageHelper.startPage(page, limit);
		List<Tcomments> comments = commentsMapper.selectComments(comment);
		PageInfo<Tcomments> pageTcomts=new PageInfo<Tcomments>(comments);
		
		//传输对像数据封装
		PageInfo<Comment> pageComts=new PageInfo<Comment>();
		
		if(null!=pageTcomts.getList()){
			List<Tcomments> listTcts = pageTcomts.getList();
			List<Comment> listCts=new ArrayList<Comment>(listTcts.size());
			listTcts.forEach(coment->{
				Comment com=new Comment(coment);
				List<Tcomments> children = new ArrayList<>();
				getChildren(children,coment.getCoid(),coment.getCid());
				com.setChildren(children);
				if(null==children||children.size()==0){
					com.setLevels(1);
				}
				listCts.add(com);
			});
			pageComts.setList(listCts);
			//不知道行不行
			pageComts=new PageInfo<Comment>(listCts);
		}
		return pageComts;
	}

	/**
     * 获取该评论下的追加评论
     *
     * @param coid
     * @return
     */
    private void getChildren(List<Tcomments> list, Integer coid,Integer cid ){
    	Tcomments comment =new Tcomments();
		comment.setCid(cid);
		//子级评论
		comment.setParent(coid);
		List<Tcomments> cms = commentsMapper.selectComments(comment);
		
        if (null != cms) {
            list.addAll(cms);
            cms.forEach(c -> getChildren(list, c.getCoid(),cid));
        }
    }

}
