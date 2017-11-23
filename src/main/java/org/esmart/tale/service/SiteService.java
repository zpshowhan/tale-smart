package org.esmart.tale.service;

import java.util.List;

import org.esmart.tale.dto.Archive;
import org.esmart.tale.dto.BackResponse;
import org.esmart.tale.dto.Comment;
import org.esmart.tale.dto.JdbcConf;
import org.esmart.tale.dto.MetaDto;
import org.esmart.tale.dto.Statistics;
import org.esmart.tale.model.Tcomments;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.model.Tuser;

import com.github.pagehelper.PageInfo;
/**
 * 
* @ClassName: SiteService 
* @Description: TODO 站点服务 
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年10月31日 下午3:56:17
 */
public interface SiteService {

	/**
	 * 
	*
	* @Title: initSite 
	* @Description: TODO 初始化站点 
	* @param @param user
	* @param @param jdbcConf    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void initSite(Tuser user, JdbcConf jdbcConf);
    /**
     * 最新收到的评论
     *
     * @param limit
     * @return
     */
	PageInfo<Tcomments> recentComments(int limit);

    /**
     * 根据类型获取文章列表
     *
     * @param type  最新,随机
     * @param limit 获取条数
     * @return
     */
    PageInfo<Tcontents> getContens(String type, int limit);

    /**
     * 获取后台统计数据
     *
     * @return
     */
    Statistics getStatistics();

    /**
     * 查询文章归档
     *
     * @return
     */
    List<Archive> getArchives();

    /**
     * 查询一条评论
     * @param coid
     * @return
     */
    Tcomments getComment(Integer coid);

    /**
     * 系统备份
     * @param bk_type
     * @param bk_path
     * @param fmt
     * @return
     */
    BackResponse backup(String bk_type, String bk_path, String fmt) throws Exception;

    /**
     * 获取分类/标签列表
     * @return
     */
    PageInfo<MetaDto> getMetas(String seachType, String type, int limit);

    /**
     * 清除缓存
     * @param key
     */
    void cleanCache(String key);

    /**
     * 获取相邻的文章
     *
     * @param type  上一篇:prev 下一篇:next
     * @param cid   当前文章id
     * @return
     */
    Tcontents getNhContent(String type, Integer cid);
    
    PageInfo<Comment> getComments(Integer cid, int page, int limit);
}
