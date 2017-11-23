package org.esmart.tale.utils.encrypt;

import java.security.MessageDigest;
import java.util.Arrays;

/** 
 * MD5加密
* @ClassName: MD5Coder 
* @Description: TODO 单向加密算法（不可逆） 
* @version 1.0 2017年8月17日 上午11:43:03 
*/
public class MD5Coder {

	private final static String MD5 = "MD5";
    private final static String ENCODE = "UTF-8";
	/**
	 * 
	*
	* @Title: getMD5 
	* @Description: TODO MD5加密
	* @param @param arg 明文
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
    public static String getMD5(String arg){
    	return getMD5(ENCODE, arg);
    }
    /**
     * 
    *
    * @Title: getMD5 
    * @Description: TODO MD5加密
    * @param @param charset 编码
    * @param @param args 明文
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
    public static String getMD5(String charset,String arg){
		try {
			// 
			MessageDigest md = MessageDigest.getInstance(MD5);
			//String.getBytes()默认使用的编码是ISO-8859-1
			md.update(arg.getBytes(charset));
			byte[] digest = md.digest();

			StringBuffer hexstr = new StringBuffer();
			String shaHex = "";
			for (int i = 0; i < digest.length; i++) {
				shaHex = Integer.toHexString(digest[i] & 0xFF);
				if (shaHex.length() < 2) {
					hexstr.append(0);
				}
				hexstr.append(shaHex);
			}
			return hexstr.toString();
		} catch (Exception e) {
			e.getStackTrace();
		}
		return null;
    	
    }
    public static void main(String[] args) {
		String content="00000000";
		String md52 = MD5Coder.getMD5(ENCODE,content);
		System.out.println("md52:　"+md52);
	}
}
