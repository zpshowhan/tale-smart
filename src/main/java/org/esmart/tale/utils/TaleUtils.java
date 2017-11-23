package org.esmart.tale.utils;

import org.apache.commons.lang.StringUtils;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.esmart.tale.controller.TuserContoller;
import org.esmart.tale.jetag.Commons;
import org.esmart.tale.jetag.Theme;
import org.esmart.tale.model.Tcontents;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.service.OptionsService;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Content;
import com.sun.syndication.feed.rss.Item;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.WireFeedOutput;
import com.vdurmont.emoji.EmojiParser;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Tale工具类
 * <p>
 * Created by biezhi on 2017/2/21.
 */
public class TaleUtils {

    /**
     * 一个月
     */
    private static final int     ONE_MONTH   = 30 * 24 * 60 * 60;
    private static final Random  R           = new Random();
    private static final long[]  HASH_PREFIX = {-1, 2, 0, 1, 7, 0, 9};
    
    public static final String CLASSPATH =TaleUtils.class.getClassLoader().getResource("").getPath();
    
    private static OptionsService optionsService=(OptionsService)ApplicationContextUtil.getBean("optionsService");
    /**
     * 匹配邮箱正则
     */
    private static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final Pattern SLUG_REGEX = Pattern.compile("^[A-Za-z0-9_-]{5,100}$", Pattern.CASE_INSENSITIVE);
    
    /**
	 * 验证URL地址
	 * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean isURL(String url) {
		String regex = "(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
		return Pattern.matches(regex, url);
	}

    /**
     * 设置记住密码cookie
     *
     * @param response
     * @param uid
     */
    public static void setCookie(HttpServletResponse response, Integer uid) {
    	
    	Cookie cookie = new Cookie(TaleConst.USER_IN_COOKIE, null);
    	cookie.setPath("/");
    	cookie.setMaxAge(ONE_MONTH);
    	try {
            String val = Tools.enAes(uid.toString(), TaleConst.AES_SALT);
            boolean isSSL = Commons.site_url().startsWith("https");
            cookie.setValue(URLEncoder.encode(val, "utf-8"));
            cookie.setHttpOnly(isSSL);
            response.addCookie(cookie);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static void setCookie(HttpServletResponse response, String name,String value,int age) {
	    	
	    	Cookie cookie = new Cookie(name, null);
	    	cookie.setPath("/");
	    	cookie.setMaxAge(age);
	    	try {
	    		boolean isSSL = Commons.site_url().startsWith("https");
	            cookie.setValue(URLEncoder.encode(value, "utf-8"));
	            cookie.setHttpOnly(isSSL);
	            response.addCookie(cookie);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }
    /**
     * 移除密码cookie
     *
     * @param response
     * @param name
     */
    public static void removeCookie(HttpServletResponse response, String name){
    	
    	Cookie cookie = new Cookie(name, "");
        cookie.setMaxAge(0);
        cookie.setValue(null);
        response.addCookie(cookie);
    }
    /**
     * 返回当前登录用户
     *
     * @return
     */
    public static Tuser getLoginUser() {
    	HttpSession session = ContextHolderUtils.getSession();
    	Object o =session.getAttribute(TaleConst.LOGIN_SESSION_KEY);
    	
        if (null == o) {
            return null;
        }
        Tuser user = (Tuser)o;
        return user;
    }

    /**
     * 退出登录状态
     *
     * @param session
     * @param response
     */
    public static void logout(HttpSession session, HttpServletResponse response) {
        session.removeAttribute(TaleConst.LOGIN_SESSION_KEY);
        removeCookie(response,TaleConst.USER_IN_COOKIE);
        //这里需要再controller中进行
        try {
			response.sendRedirect(Commons.site_url());
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    /**
     * 
    *
    * @Title: getCookie 
    * @Description: TODO 获取cookie 
    * @param @param request
    * @param @param name
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String getCookie(HttpServletRequest request ,String name){
    	
    	String value = null;
    	Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					try {
						value = URLDecoder.decode(cookie.getValue(), "utf-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return value;
    }
    /**
     * 获取cookie中的用户id
     *
     * @param request
     * @return
     */
    public static Integer getCookieUid(HttpServletRequest request) {
        	
        String value = getCookie(request, TaleConst.LOGIN_SESSION_KEY);
        
        if (StringUtils.isNotBlank(value)) {
            try {
                String uid = Tools.deAes(value, TaleConst.AES_SALT);
                return StringUtils.isNotBlank(uid) ? Integer.valueOf(uid) : null;
            } catch (Exception e) {
            }
        }
        return null;
    }
    
    /**
     * 重新拼接字符串
     *
     * @param arr
     * @return
     */
    public static String rejoin(String[] arr) {
        if (null == arr) {
            return "";
        }
        if (arr.length == 1) {
            return "'" + arr[0] + "'";
        }
        String a = String.join("','", arr);
        a = a.substring(2) + "'";
        return a;
    }

    /**
     * markdown转换为html
     *
     * @param markdown
     * @return
     */
    public static String mdToHtml(String markdown) {
        if (StringUtils.isBlank(markdown)) {
            return "";
        }

        List<Extension> extensions = Arrays.asList(TablesExtension.create());
        Parser          parser     = Parser.builder().extensions(extensions).build();
        Node            document   = parser.parse(markdown);
        HtmlRenderer    renderer   = HtmlRenderer.builder().extensions(extensions).build();
        String          content    = renderer.render(document);
        content = EmojiParser.parseToUnicode(content);

        // 支持网易云音乐输出
        if (Boolean.parseBoolean(Endpoint.get("app.support_163_music")) && content.contains("[mp3:")) {
            content = content.replaceAll("\\[mp3:(\\d+)\\]", "<iframe frameborder=\"no\" border=\"0\" marginwidth=\"0\" marginheight=\"0\" width=350 height=106 src=\"//music.163.com/outchain/player?type=2&id=$1&auto=0&height=88\"></iframe>");
        }
        // 支持gist代码输出
        if ( Boolean.parseBoolean(Endpoint.get("app.support_gist")) && content.contains("https://gist.github.com/")) {
            content = content.replaceAll("&lt;script src=\"https://gist.github.com/(\\w+)/(\\w+)\\.js\">&lt;/script>", "<script src=\"https://gist.github.com/$1/$2\\.js\"></script>");
        }

        return content;
    }

    /**
     * 提取html中的文字
     *
     * @param html
     * @return
     */
    public static String htmlToText(String html) {
        if (StringUtils.isNotBlank(html)) {
            return html.replaceAll("(?s)<[^>]*>(\\s*<[^>]*>)*", " ");
        }
        return "";
    }

    /**
     * 判断文件是否是图片类型
     *
     * @param imageFile
     * @return
     */
    public static Boolean isImage(File imageFile) {
        if (!imageFile.exists()) {
            return false;
        }
        try {
            Image img = ImageIO.read(imageFile);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    public static Boolean isImageByIn(InputStream inp) {
        try {
            Image img = ImageIO.read(inp);
            if (img == null || img.getWidth(null) <= 0 || img.getHeight(null) <= 0) {
                return false;
            }
            return true;
        } catch (Exception e) {
            return false;
        }finally{
        	try {
				inp.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
    }
    public static String fileType(String contentType) {
    	
    	switch(contentType.toLowerCase()){
		    	case "image/gif":
		    	case "image/x-icon":
		    	case "image/jpeg":
		    	case "image/png":
		    	case "application/x-bmp":
		    		break;
		    	default:
		    		
		    		break;
    		
    	}
    	return "image";
    }

    /**
     * 判断是否是邮箱
     *
     * @param emailStr
     * @return
     */
    public static boolean isEmail(String emailStr) {
        Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(emailStr);
        return matcher.find();
    }

    /**
     * 判断是否是合法路径
     *
     * @param slug
     * @return
     */
    public static boolean isPath(String slug) {
        if (StringUtils.isNotBlank(slug)) {
            if (slug.contains("/") || slug.contains(" ") || slug.contains(".")) {
                return false;
            }
            Matcher matcher = SLUG_REGEX.matcher(slug);
            return matcher.find();
        }
        return false;
    }

    /**
     * 获取RSS输出
     *
     * @param articles
     * @return
     * @throws FeedException
     */
    public static String getRssXml(java.util.List<Tcontents> articles) throws FeedException {
        Channel channel = new Channel("rss_2.0");
        channel.setTitle(optionsService.getOptionsOne("site_title").getValue());
        channel.setLink(Commons.site_url());
        channel.setDescription(optionsService.getOptionsOne("site_description").getValue());
        channel.setLanguage("zh-CN");
        java.util.List<Item> items = new ArrayList<>();
        articles.forEach(post -> {
            Item item = new Item();
            item.setTitle(post.getTitle());
            Content content = new Content();
            String  value   = Theme.article(post.getContent());

            char[] xmlChar = value.toCharArray();
            for (int i = 0; i < xmlChar.length; ++i) {
                if (xmlChar[i] > 0xFFFD) {
                    //直接替换掉0xb
                    xmlChar[i] = ' ';
                } else if (xmlChar[i] < 0x20 && xmlChar[i] != 't' & xmlChar[i] != 'n' & xmlChar[i] != 'r') {
                    //直接替换掉0xb
                    xmlChar[i] = ' ';
                }
            }

            value = new String(xmlChar);

            content.setValue(value);
            item.setContent(content);
            item.setLink(Theme.permalink(post.getCid(), post.getSlug()));
            item.setPubDate(DateUtil.getTimeByInt(post.getCreated()));
            items.add(item);
        });
        channel.setItems(items);
        WireFeedOutput out = new WireFeedOutput();
        return out.outputString(channel);
    }

    /**
     * 替换HTML脚本
     *	XSS攻击解决方案
     * @param value
     * @return
     */
    public static String cleanXSS(String value) {
        //You'll need to remove the spaces from the html entities below
        value = value.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
        value = value.replaceAll("\\(", "&#40;").replaceAll("\\)", "&#41;");
        value = value.replaceAll("'", "&#39;");
        value = value.replaceAll("eval\\((.*)\\)", "");
        value = value.replaceAll("[\\\"\\\'][\\s]*javascript:(.*)[\\\"\\\']", "\"\"");
        value = value.replaceAll("script", "");
        return value;
    }

    /**
     * 获取某个范围内的随机数
     *
     * @param max 最大值
     * @param len 取多少个
     * @return
     */
    public static int[] random(int max, int len) {
        int values[] = new int[max];
        int temp1, temp2, temp3;
        for (int i = 0; i < values.length; i++) {
            values[i] = i + 1;
        }
        //随机交换values.length次
        for (int i = 0; i < values.length; i++) {
            temp1 = Math.abs(R.nextInt()) % (values.length - 1); //随机产生一个位置
            temp2 = Math.abs(R.nextInt()) % (values.length - 1); //随机产生另一个位置
            if (temp1 != temp2) {
                temp3 = values[temp1];
                values[temp1] = values[temp2];
                values[temp2] = temp3;
            }
        }
        return Arrays.copyOf(values, len);
    }


    public static final String UP_DIR = ApplicationContextUtil.getRootRealPath();

    public static String getFileKey(String name) {
        String prefix = "/upload/" + DateUtil.dateFormat(new Date(), "yyyy/MM");
        String dir    = UP_DIR + prefix;
        if (!Files.exists(Paths.get(dir))) {
            new File(dir).mkdirs();
        }
        return prefix + "/" + UUID.randomUUID().toString() + "." + fileSuffix(name);
    }
    
    public static String fileSuffix(String fileName){
    	String []str=fileName.split("\\.");
		String suffix=str[str.length-1].toLowerCase();
		return suffix;
    }
}
