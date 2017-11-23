package org.esmart.tale.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;

import jetbrick.io.IoUtils;

public class AliyunUtil {

	private static Logger logger = Logger.getLogger(AliyunUtil.class);
	//阿里云访问地址
	private static String endpoint =Endpoint.get("endpoint");
	//资源访问地址
	private static String endpointUrl =Endpoint.get("endpointUrl");
	//accessKey
	private static String accessKeyId  =Endpoint.get("accessKeyId");
	//accessKeySecret
	private static String accessKeySecret  =Endpoint.get("accessKeySecret");
	//bucket
	private static String bucket  =Endpoint.get("bucketName");
	//默认过期时间
	private static String expirationDate  =Endpoint.get("expirationDate");
	
	public static OSSClient getInstance(){
		return new OSSClient(endpoint, accessKeyId,accessKeySecret);
	}
	
	public static AliyunResult uploadSingle(File file){
		String fileName=file.getName();
		return uploadSingle(IoUtils.toByteArray(file), fileName);
	}
	public static AliyunResult uploadSingle(InputStream inputStream ,String fileName){
		
		long start = System.currentTimeMillis();
		AliyunResult result =new AliyunResult();
		// 创建OSSClient实例
		OSSClient client = getInstance();
		result.setOldName(fileName);
		String key=UUID.randomUUID().toString()+"_"+fileName;
		try {
			// 上传文件
			client.putObject(bucket, key, inputStream);
			result.setCode(0);
			result.setMsg("上传成功！");
			
			result.setKey(key);
			String pointUrl=endpointUrl+"/"+key;
			//设置url的过期时间
			try {
				//设置过期时间
				 Date expiration = com.aliyun.oss.common.utils.DateUtil.parseIso8601Date(expirationDate);
				 URL url = client.generatePresignedUrl(bucket, key, expiration);
				 pointUrl=url.toString();
			} catch (ParseException e) {
				e.printStackTrace();
				logger.error(e.getMessage(), e);
			}
			result.setUrl(pointUrl);
		} catch (ClientException e) {
			result.setCode(-1);
			result.setMsg(e.getMessage());
			logger.error(e.getMessage(), e);
		} catch (OSSException e) {
			result.setCode(1);
			result.setMsg(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		// 关闭client
		client.shutdown();
		long end = System.currentTimeMillis();
		result.setConsum(end-start);
		return result;
	}
	public static AliyunResult uploadSingle(byte[] content ,String fileName){
		
		return uploadSingle(new ByteArrayInputStream(content), fileName);
	}
	
	public String pastDue(Date date,String key){
		
		// 创建OSSClient实例
		OSSClient client = getInstance();
		URL url = client.generatePresignedUrl(bucket, key, date);
		//关闭连接
		client.shutdown();
		
		return url.toString();
		
	}
	public static byte[] downLoadSingle(String key){
		// 创建OSSClient实例
		OSSClient client = getInstance();
		//得到下载对象
		OSSObject ossObject = client.getObject(bucket, key);
	    try {
	    	InputStream in = ossObject.getObjectContent();
	    	byte[] byteArray = IoUtils.toByteArray(in);
	    	in.close();
	    	return byteArray;
		} catch (Exception e) {
			e.getStackTrace();
			logger.error(e.getMessage(), e);
		}finally {
			client.shutdown();
		}
	    return null;
	}
	
	public static Boolean downLoadSingle(String key,String localPath){
		
		// 创建OSSClient实例
		OSSClient client = getInstance();
		try {
			client.getObject(new GetObjectRequest(bucket, key), new File(localPath));
			return true;
		} catch (ClientException e) {
			logger.error(e.getMessage(), e);
			return false;
		} catch (OSSException e) {
			logger.error(e.getMessage(), e);
			return false;
		}finally {
			client.shutdown();
		}
	}
	public static void main(String[] args) {
		File file =new File("D:\\test.JPG");
//		System.out.println(uploadSingle(file).toString());
		String key="79b11503-fdce-4672-9d7b-7a17963749c2_test.JPG";
//		downLoadSingle(key, "D:\\downloadtest.JPG");
		try {
			FileUtils.writeByteArrayToFile(new File("D:\\downloadtest2222.JPG"), downLoadSingle(key));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
