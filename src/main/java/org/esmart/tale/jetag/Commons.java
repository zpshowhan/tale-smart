package org.esmart.tale.jetag;

import com.github.pagehelper.PageInfo;
import com.vdurmont.emoji.EmojiParser;

import jetbrick.template.JetAnnotations.Functions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.esmart.tale.model.Toptions;
import org.esmart.tale.service.OptionsService;
import org.esmart.tale.service.SiteService;
import org.esmart.tale.utils.ApplicationContextUtil;
import org.esmart.tale.utils.ContextHolderUtils;
import org.esmart.tale.utils.DateUtil;
import org.esmart.tale.utils.TaleConst;
import org.esmart.tale.utils.TaleUtils;
import org.esmart.tale.utils.Tools;

/**
 * 公共函数
 * <p>
 * Created by biezhi on 2017/2/21.
 */
@Functions
public final class Commons {

	
    private static SiteService siteService=(SiteService)ApplicationContextUtil.getBean("siteService");
	
    private static OptionsService optionsService=(OptionsService)ApplicationContextUtil.getBean("optionsService");
	
    private static final List EMPTY = new ArrayList(0);

    private static final Random rand = new Random();
    
    public static String THEME = "themes/default";
    
    private static final String TEMPLATES = "/templates/";

    /**
     * 
    *
    * @Title: ctx 
    * @Description: TODO 获取上下文 
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String ctx(){
    	
    	
    	return ContextHolderUtils.getRequest().getContextPath();
    }
    
    /**
     * 判断分页中是否有数据
     *
     * @param PageInfo
     * @return
     */
    public static boolean is_empty(PageInfo page) {
        return null == page || page.getList().size()==0||null==page.getList();
    }

    /**
     * 返回当前主题名称
     *
     * @return
     */
    public static String site_theme() {
        return site_option("site_theme", "default");
    }
    /**
     * 返回网站首页链接，如：http://tale.biezhi.me
     *
     * @return
     */
    public static String site_url() {
        return site_url("");
    }

    /**
     * 返回网站链接下的全址
     *
     * @param sub 后面追加的地址
     * @return
     */
    public static String site_url(String sub) {
        return site_option("site_url") + sub;
    }

    /**
     * 网站标题
     *
     * @return
     */
    public static String site_title() {
        return site_option("site_title");
    }

    /**
     * 网站配置项
     *
     * @param key
     * @return
     */
    public static String site_option(String key) {
        return site_option(key, "");
    }

    /**
     * 网站配置项
     *
     * @param key
     * @param defalutValue 默认值
     * @return
     */
    public static String site_option(String key, String defalutValue) {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        Toptions option = optionsService.getOptionsOne(key);
        return option==null||StringUtils.isBlank(option.getValue())?defalutValue:option.getValue();
    }

    /**
     * 返回站点设置的描述信息
     * @return
     */
    public static String site_description(){
        return site_option("site_description");
    }

    /**
     * 截取字符串
     *
     * @param str
     * @param len
     * @return
     */
    public static String substr(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len);
        }
        return str;
    }

    /**
     * 返回主题URL
     *
     * @return
     */
    public static String theme_url() {
        return Commons.site_url(TEMPLATES + THEME);
    }

    /**
     * 返回主题下的文件路径
     *
     * @param sub
     * @return
     */
    public static String theme_url(String sub) {
        return Commons.site_url(TEMPLATES + THEME + sub);
    }


    /**
     * 返回gravatar头像地址
     *
     * @param email
     * @return
     */
    public static String gravatar(String email) {
        String avatarUrl = "https://secure.gravatar.com/avatar";
        if (StringUtils.isBlank(email)) {
            return avatarUrl;
        }
        String hash = Tools.md5(email.trim().toLowerCase());
        return avatarUrl + "/" + hash;
    }

    /**
     * 格式化unix时间戳为日期
     *
     * @param unixTime
     * @return
     */
    public static String fmtdate(Integer unixTime) {
        return fmtdate(unixTime, "yyyy-MM-dd");
    }

    /**
     * 格式化日期
     * @param date
     * @param fmt
     * @return
     */
    public static String fmtdate(Date date, String fmt) {
    	SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		String timestr = sdf.format(date);
        return timestr;
    }

    /**
     * 格式化unix时间戳为日期
     *
     * @param unixTime
     * @param patten
     * @return
     */
    public static String fmtdate(Integer unixTime, String patten) {
        if (null != unixTime && StringUtils.isNotBlank(patten)) {
            return fmtdate(DateUtil.getTimeByInt(unixTime), patten);
        }
        return "";
    }

    /**
     * 获取随机数
     * @param max
     * @param str
     * @return
     */
    public static String random(int max, String str){
    	
        return (rand.nextInt(max - 1 + 1) + 1) + str;
    }

    /**
     * An :grinning:awesome :smiley:string &#128516;with a few :wink:emojis!
     *
     * 这种格式的字符转换为emoji表情
     *
     * @param value
     * @return
     */
    public static String emoji(String value){
        return EmojiParser.parseToUnicode(value);
    }

    /**
     * 获取文章第一张图片
     *
     * @return
     */
    public static String show_thumb(String content) {
        content = TaleUtils.mdToHtml(content);
        if (content.contains("<img")) {
            String img = "";
            String regEx_img = "<img.*src\\s*=\\s*(.*?)[^>]*?>";
            Pattern p_image = Pattern.compile(regEx_img, Pattern.CASE_INSENSITIVE);
            Matcher m_image = p_image.matcher(content);
            if (m_image.find()) {
                img = img + "," + m_image.group();
                // //匹配src
                Matcher m = Pattern.compile("src\\s*=\\s*\'?\"?(.*?)(\'|\"|>|\\s+)").matcher(img);
                if (m.find()) {
                    return m.group(1);
                }
            }
        }
        return "";
    }

}
