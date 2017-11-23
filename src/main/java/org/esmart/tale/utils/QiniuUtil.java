package org.esmart.tale.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import jetbrick.io.IoUtils;
import net.sf.json.JSONObject;

public class QiniuUtil {

	private static Logger logger = Logger.getLogger(QiniuUtil.class);
	//Zone对象
	/**
	 * Zone.zone0() =>华东
	 * Zone.zone1() =>华北
	 * Zone.zone2() =>华南
	 * Zone.zoneNa0() =>北美
	 */
	private static Zone zone= Zone.zone1();
	
	private static UploadManager uploadManager = new UploadManager(new Configuration(zone));
	
	private static String ak=TaleConst.QINIU_ACCESS_KEY;
	private static String sk=TaleConst.QINIU_SECRET_KEY;
	private static String bt=TaleConst.QINIU_BUCKET;
	
	/**
	 * 
	*
	* @Title: uploadByFile 
	* @Description: TODO 上传文件到七牛 
	* @param @param file
	* @param @return    设定文件 
	* @return QiniuResult    返回类型 
	* @throws
	 */
	public static QiniuResult uploadByFile(File file){
		
		
		return uploadByByte(IoUtils.toByteArray(file));
	}
	/**
	 * 
	*
	* @Title: uploadByByte 
	* @Description: TODO 上传数组到七牛
	* @param @param bte
	* @param @return    设定文件 
	* @return QiniuResult    返回类型 
	* @throws
	 */
	public static QiniuResult uploadByByte(byte[] bte){
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;
		Auth auth = Auth.create(ak, sk);
		String upToken = auth.uploadToken(bt);
		try {
		    Response response = uploadManager.put(bte, key, upToken);
		    //解析上传成功的结果
		    logger.info("qiniu Response body=> "+response.bodyString());
		    return QiniuResult.forBoject(response);
		} catch (QiniuException e) {
			e.getStackTrace();
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	/**
	 * 
	*
	* @Title: uploadByInputStream 
	* @Description: TODO 上传文件流到七牛 
	* @param @param inp
	* @param @return    设定文件 
	* @return QiniuResult    返回类型 
	* @throws
	 */
	public static QiniuResult uploadByInputStream(InputStream inp){
		
		//默认不指定key的情况下，以文件内容的hash值作为文件名
		String key = null;
		Auth auth = Auth.create(ak, sk);
		String upToken = auth.uploadToken(bt);
		try {
		    Response response = uploadManager.put(inp, key, upToken,null,null);
		    //解析上传成功的结果
		    logger.info("qiniu Response body=> "+response.bodyString());
		    return QiniuResult.forBoject(response);
		} catch (QiniuException e) {
			e.getStackTrace();
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	public static void main(String[] args) {
		File file=new File("D:\\test.JPG");
		System.out.println(JSONObject.fromObject(uploadByFile(file)).toString());
	}
	
}
