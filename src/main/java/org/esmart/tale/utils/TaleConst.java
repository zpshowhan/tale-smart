package org.esmart.tale.utils;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.esmart.tale.dto.PluginMenu;

/**
 * Tale 常量存储
 *
 * @author biezhi
 */
public class TaleConst {

    public static final String USER_IN_COOKIE = "S_L_ID";
    public static String AES_SALT = "0123456789abcdef";
    public static String LOGIN_SESSION_KEY = "login_user";
    public static boolean INSTALL = false;
    
    /**
     * 七牛配置
     */
    public static String QINIU_ACCESS_KEY=Endpoint.get("qiniu.accesskey");
    public static String QINIU_SECRET_KEY=Endpoint.get("qiniu.secretkey");
    public static String QINIU_DOMAIN=Endpoint.get("qiniu.domain");
    public static String QINIU_BUCKET=Endpoint.get("qiniu.bucket");

    /**
     * 最大页码
     */
    public static final int MAX_PAGE = 100;

    /**
     * 最大获取文章条数
     */
    public static final int MAX_POSTS = 9999;

    /**
     * 点击次数超过多少更新到数据库
     */
    public static final int HIT_EXCEED = 10;

    /**
     * 插件菜单
     */
    public static final List<PluginMenu> plugin_menus = new ArrayList<PluginMenu>(8);

    /**
     * 上传文件最大20M
     */
    public static Integer MAX_FILE_SIZE = 204800;

    /**
     * 要过滤的ip列表
     */
    public static final Set<String> BLOCK_IPS = new HashSet<>(16);

}