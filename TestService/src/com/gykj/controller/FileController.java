package com.gykj.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import sun.net.ftp.FtpClient;

import com.gykj.Constant;
import com.gykj.interceptor.CachePassport;
import com.gykj.pojo.InterfaceList;
import com.gykj.pojo.Log;
import com.gykj.service.impl.FileService;
import com.gykj.service.impl.InformationService;
import com.gykj.utils.JsonUtils;



/**
 * 关于文件处理的请求入口 控制器
 * @author xianyl
 *
 */
@RestController
@RequestMapping("/file")
public class FileController{
	@Resource
	private FileService fileService;
	@Resource
	private InformationService informationService;
	
	/**
	 * 文件上传,  form的enctype="multipart/form-data" 这个是上传文件必须的
	 * @param token
	 * @param request
	 * @return
	 */
	//http://192.168.1.140:8080/gykj/file/upload/{token}/{collection}/{import}?file=xxx.excel&token=xx&document=文件数据需要的表
	@CachePassport
	@RequestMapping(value="/upload/{token}/{collection}/{action}", method={RequestMethod.POST})
    public Callable<Map<String, Object>> upload(@PathVariable String token
    											,@PathVariable String collection
    											,@PathVariable String action
    											,String document,String username
    											,HttpServletRequest request) {
		// 先获取用户信息，在CacheAuthInterceptor有存入
		Object user = request.getAttribute("user");
		
		//贯通整个tcp任务的日志对象
		Log log = new Log();
		//获取接口清单
		InterfaceList interfaceList = informationService.getInterfaceList(collection, action, token, log);
	
		final List<Map<String,Object>> params = new ArrayList<Map<String,Object>>();
		//首先检查是否有使用该接口权限
		if(interfaceList != null){
			MultipartHttpServletRequest mrequest = null;
			try {
				mrequest = (MultipartHttpServletRequest)request;
				//获取当前服务的ip
				//String ip = ObjectUtils.getIpAddress(mrequest);
				//获取存放文件的文件夹
				File dir = fileService.getTargetDir(mrequest, "/uploadfile/"+username+"/");
				//获取file的文件
				Map<String, MultipartFile> fileMap = mrequest.getFileMap();
				Map<String,Object> param = null;//由保存的文件对象转换成的参数对象
				for(MultipartFile srcFile: fileMap.values()){
					//保存文件
					param = fileService.saveFile(srcFile, dir);	
					//param = fileService.saveFileToFTP("192.168.1.7",21,"fsxf","fsxf","file/org",srcFile,dir);
					if(param == null) continue;
					//再加接参数
					param.put("user", user);
					param.put("username", username);
					param.put("document", document);//这个是要插入那个表
					param.put("operation", action);
					param.put("flag", 0);
					param.put("display", srcFile.getOriginalFilename());
					param.put("state", "接受任务");
					params.add(param);
				}
				
			} catch (Exception e) {
				return new Callable<Map<String, Object>>(){
					@Override
					public Map<String, Object> call() throws Exception {
						Map<String,Object> param = new HashMap<String, Object>();
						param.put(Constant.JSON_RESPONSE_FIELD_CODE, Constant.RETURN_CODE_STRING_FAIL);
						param.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "上传失败，请检查是enctype='multipart/form-data'!");
						return param;
					}};
			}
		}
		
		//根据接收文件的数据做不同处理
		switch (params.size()) {
		case 0:
			//上传失败，直接返回
	        return new Callable<Map<String, Object>>(){
				@Override
				public Map<String, Object> call() throws Exception {
					Map<String,Object> param = new HashMap<String, Object>();
					param.put(Constant.JSON_RESPONSE_FIELD_CODE, Constant.RETURN_CODE_STRING_FAIL);
					param.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "上传失败，请检查token是否为空?是否有使用该接口权限?是否有上传文件内容?");
					return param;
				}};
		case 1:
			//只有1个文件
			Map<String,Object> param = new HashMap<String, Object>();
			param.put("obj", JsonUtils.toJson(params));
			param.put("taskType", 0);//不等待
			return informationService.requestTCP(interfaceList, param, log);
		default:
			//有N个文件
	        return new Callable<Map<String, Object>>(){
				@Override
				public Map<String, Object> call() throws Exception {
					Map<String,Object> param = new HashMap<String, Object>();
					param.put(Constant.JSON_RESPONSE_FIELD_CODE, Constant.RETURN_CODE_STRING_SUCCESS);
					param.put(Constant.JSON_RESPONSE_FIELD_MESSAGE, "上传成功");
					param.put("result", params);
					return param;
				}};
		}
		
	}//upload方法结束
	
	
	
	
	
}
