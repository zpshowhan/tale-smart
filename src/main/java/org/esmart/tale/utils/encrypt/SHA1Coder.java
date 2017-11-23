package org.esmart.tale.utils.encrypt;

import java.security.MessageDigest;
import java.util.Arrays;

/** 
 * SHA-1加密
* @ClassName: SHA1Coder 
* @Description: TODO 单向加密算法（不可逆） 
* @version 1.0 2017年8月17日 上午11:17:45 
*/
public class SHA1Coder {

	private final static String SHA1 = "SHA-1";
    private final static String ENCODE = "UTF-8";
	/**
	 * 
	*
	* @Title: getSHA1 
	* @Description: TODO  获取SHA-1加密后密文
	* @param @param args 
	* @param @return    设定文件 
	* @return String    返回类型 
	* @throws
	 */
    public static String getSHA1(String args){
    	
    	return getSHA1(ENCODE, args);
    }
    /**
     * 
    *
    * @Title: getSHA1 
    * @Description: TODO  获取SHA-1加密后密文
    * @param @param charset 编码
    * @param @param args 
    * @param @return    设定文件 
    * @return String    返回类型 
    * @throws
     */
	public static String getSHA1(String charset,String args){
		
		try {
			
			/*Arrays.sort(args);
			StringBuffer sb = new StringBuffer();
			// 字符串排序
			Arrays.sort(args);
			for (String a : args) {
				sb.append(a);
			}
			String str = sb.toString();*/
			// SHA1签名生成    使用getInstance("算法")来获得消息摘要,这里使用SHA-1的160位算法
			MessageDigest md = MessageDigest.getInstance(SHA1);
			//String.getBytes()默认使用的编码是ISO-8859-1
			md.update(args.getBytes(charset));
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
		String content="80980989080";
		System.out.println(SHA1Coder.getSHA1(ENCODE,content));
	}
}
