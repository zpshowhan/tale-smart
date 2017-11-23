package org.esmart.tale.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.esmart.tale.controller.base.BaseController;
import org.esmart.tale.dto.LogActions;
import org.esmart.tale.dto.Types;
import org.esmart.tale.exception.TalException;
import org.esmart.tale.jetag.Commons;
import org.esmart.tale.model.Tattach;
import org.esmart.tale.model.Tlogs;
import org.esmart.tale.model.Toptions;
import org.esmart.tale.model.Tuser;
import org.esmart.tale.service.AttachService;
import org.esmart.tale.service.LogService;
import org.esmart.tale.service.OptionsService;
import org.esmart.tale.service.SiteService;
import org.esmart.tale.utils.ApplicationContextUtil;
import org.esmart.tale.utils.DateUtil;
import org.esmart.tale.utils.QiniuResult;
import org.esmart.tale.utils.QiniuUtil;
import org.esmart.tale.utils.TaleConst;
import org.esmart.tale.utils.TaleUtils;
import org.esmart.tale.utils.Tools;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.github.pagehelper.PageInfo;

import net.sf.json.JSONObject;

/**
 * 
* @ClassName: AttachController 
* @Description: TODO 文件控制器 
* @Company:esmart
* @author Thinkpad 
* @version 1.0 2017年11月3日 下午4:27:46
 */
@Controller
@RequestMapping("/admin/attach")
public class AttachController extends BaseController{

	private static Logger logger = Logger.getLogger(AttachController.class);
	
	@Resource
	private AttachService attachService;
	@Resource
	private LogService logService;
	@Resource
	private SiteService siteService;
	@Resource
	private OptionsService optionsService;
	
	@RequestMapping(value="" , method=RequestMethod.GET )
	public String index(HttpServletRequest request,ModelMap model,
			@RequestParam(value = "page", defaultValue = "1")int page,
			@RequestParam(value = "limit", defaultValue = "12")int limit){
		PageInfo<Tattach> pageat = attachService.getAttachs(page, limit);
		model.addAttribute("attachs", pageat);
		//获取项目域名+项目名
		String attachUrl="";
		Toptions option = optionsService.getOptionsOne("qiniu_isopen");
		if(option!=null){
			//1 代表启用七牛           0 代表不启用七牛
			if(option.getValue()!=null&&option.getValue().equals("0")){
				attachUrl=Commons.site_option(Types.ATTACH_URL, Commons.site_url());
			}
		}
		model.addAttribute(Types.ATTACH_URL, attachUrl);
		model.addAttribute("max_file_size", TaleConst.MAX_FILE_SIZE / 1024);
		
		return "admin/attach";
	}
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	@ResponseBody
    public String upload(HttpServletRequest request) {

		logger.info("UPLOAD DIR = "+ TaleUtils.UP_DIR);
		JSONObject json=new JSONObject();
        Tuser users = this.user();
        Integer uid = users.getUid();
        MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
        Iterator<String> iter = multiRequest.getFileNames();
        List<String> errorFiles = new ArrayList<>();
        Toptions option = optionsService.getOptionsOne("qiniu_isopen");
		
        try {
			
        	while(iter.hasNext()){
        		MultipartFile tempFile = multiRequest.getFile(iter.next());
        		//文件名
        		String fileName=tempFile.getOriginalFilename();
        		//判断文件类型
        		String ftype = TaleUtils.isImageByIn(tempFile.getInputStream()) ? Types.IMAGE : Types.FILE;
        		
         		try {
         			//项目下文件保存路径
         			String fkey = TaleUtils.getFileKey(fileName);
         			String filePath = TaleUtils.UP_DIR + fkey;
         			
         			//1 代表启用七牛           0 代表不启用七牛
         			if(option!=null&&
         					option.getValue()!=null&&
         						option.getValue().equals("0")){
         				File file = new File(filePath);
             			if(!file.getParentFile().exists())
             				file.getParentFile().mkdirs();
             			if(!file.exists())
             				file.createNewFile();
             			tempFile.transferTo(file);
         				
         			}else{
         				QiniuResult result = QiniuUtil.uploadByInputStream(tempFile.getInputStream());
         				fkey=result.getUrl();
         				logger.info("qinniu result key=> "+result.getKey());
         			}
         			
         			
         			Tattach att=new Tattach();
         			att.setAuthorId(uid);
         			att.setCreated(DateUtil.currentDateInt());
         			att.setFkey(fkey);
         			att.setFname(fileName);
         			att.setFtype(ftype);
         			attachService.save(att);
                    siteService.cleanCache(Types.C_STATISTICS);
				} catch (IOException e) {
					e.fillInStackTrace();
					logger.error(e.getMessage(), e);
					errorFiles.add(fileName);
				}
        	}
        	json.put("success", true);
        	json.put("msg", "上传成功");
		} catch (Exception e) {
			e.fillInStackTrace();
			logger.error(e.getMessage(), e);
			json.put("success", false);
			json.put("msg", e.getMessage());
		}
        json.put("errorFile", errorFiles);
        return json.toString();
    }
	
	@RequestMapping(value = "/delete")
	public @ResponseBody String deleteAtt(@RequestParam Integer id ,HttpServletRequest request){
		
		JSONObject json=new JSONObject();
		try {
			Tattach att = attachService.byId(id);
			if(null==att){
				json.put("success", false);
				json.put("msg", "附件不存在");
				return json.toString();
			}
			attachService.delete(id);
            siteService.cleanCache(Types.C_STATISTICS);
            String upDir = TaleUtils.UP_DIR+att.getFkey();
            File file =new File(upDir);
            if(file.exists()){
            	file.delete();
            }
            Tlogs log =new Tlogs();
            log.setAction(LogActions.LOGIN);
            log.setAuthorId(this.getUid());
            log.setData(att.getFkey());
            log.setIp(Tools.getIpAddr(request));
            logService.save(log);
		} catch (Exception e) {
			String msg = "附件删除失败";
            if (e instanceof TalException) msg = e.getMessage();
            else logger.error(msg, e);
            json.put("success", false);
			json.put("msg", msg);
            return json.toString();
		}
		json.put("success", true);
		json.put("msg", "删除成功");
		return json.toString();
	}
}
