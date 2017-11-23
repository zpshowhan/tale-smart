package org.esmart.tale.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * 
 * 
* @ClassName: DatabaseBackup 
* @Description: TODO 数据库备份 
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年11月17日 下午12:47:16
 */
public class DatabaseBackup {

	/**
	 * Java代码实现MySQL数据库导出
	 * 原理java调用系统cmd命令实现导出
	*
	* @Title: exportDatabase 
	* @Description: TODO mysql数据库数据导出 
	* @param @param hostIP 服务器地址IP
	* @param @param userName 数据库所需要的用户名
	* @param @param password 数据库所需要的密码
	* @param @param savePath 数据库导出文件保存路径
	* @param @param fileName 数据库导出文件文件名
	* @param @param databaseName 要导出的数据库名
	* @param @return    设定文件 
	* @return boolean     返回true表示导出成功，否则返回false
	* @throws
	 */
	public static boolean exportDatabase(String hostIP, String userName, String password, String savePath, String fileName, String databaseName){
		
        File saveFile = new File(savePath);  
        if (!saveFile.exists()) {// 如果目录不存在  
            saveFile.mkdirs();// 创建文件夹  
        }  
        if(!savePath.endsWith(File.separator)){  
            savePath = savePath + File.separator;  
        }  
          
        PrintWriter printWriter = null;  
        BufferedReader bufferedReader = null;  
        try {  
            printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(savePath + fileName), "utf8"));  
            Process process = Runtime.getRuntime().exec(" mysqldump -h" + hostIP + " -u" + userName + " -p" + password + " --set-charset=UTF8 " + databaseName);  
            InputStreamReader inputStreamReader = new InputStreamReader(process.getInputStream(), "utf8");  
            bufferedReader = new BufferedReader(inputStreamReader);  
            String line;  
            while((line = bufferedReader.readLine())!= null){  
                printWriter.println(line);  
            }  
            printWriter.flush();  
            if(process.waitFor() == 0){//0 表示线程正常终止。  
                return true;  
            }  
        }catch (IOException e) {  
            e.printStackTrace();  
        }catch (InterruptedException e) {  
            e.printStackTrace();  
        } finally {  
            try {  
                if (bufferedReader != null) {  
                    bufferedReader.close();  
                }  
                if (printWriter != null) {  
                    printWriter.close();  
                }  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        return false;
		
	}
	public static void main(String[] args){  
        try {  
            if (exportDatabase("127.0.0.1", "root", "root", "D:/backup", "2014-10-14.sql", "tale")) {  
                System.out.println("数据库成功备份！！！");  
            } else {  
                System.out.println("数据库备份失败！！！");  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}
