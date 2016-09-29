package com.gykj.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.gykj.Constant;
import com.gykj.utils.DateTimeUtil;
import com.gykj.utils.FileUtils;





/**
 * 关于文件处理  服务类
 * @author xianyl
 *
 * @param <T>
 */
@Repository(value="fileService")
public class FileService{
	/**
	 * 获取存放文件的文件夹
	 * @param request
	 * @param dir
	 * @return
	 */
	public File getTargetDir(HttpServletRequest request, String dir){
		/*//首先获取web应用真实路径,并创建文件夹对象
		String basePath = request.getSession().getServletContext().getRealPath(Constant.FILE_DIR);
		if(!StringUtils.isEmpty(dir)) basePath += dir;
		File targetDir = new File(basePath);
		//如果文件夹不存在,则创建
		if(!FileUtils.exists(targetDir)){
			targetDir.mkdirs();
		}
		return targetDir;*/
		
		//首先获取web应用真实路径,并创建文件夹对象
		String basePath = request.getSession().getServletContext().getRealPath("/");
		//if(!StringUtils.isEmpty(dir)) basePath += dir;
		File targetDir0 = new File(basePath);
		File targetDir1 = new File(targetDir0.getParent());
		String path=targetDir1.getAbsolutePath()+"/fsxf/import/"+Constant.FILE_DIR+dir;
		File targetDir = new File(path);
		//如果文件夹不存在,则创建
		if(!FileUtils.exists(targetDir)){
			targetDir.mkdirs();
		}
		return targetDir;
	}
	
	
	/**
	 * 根据文件字段名获取文件对象
	 * @param request
	 * @param file_field 文件字段名,不传则为file
	 * @return
	 */
	public MultipartFile getMultipartFile(MultipartHttpServletRequest mrequest, String file_field){
		if(StringUtils.isEmpty(file_field)){
			file_field = "file";
		}
		//获取源文件对象
		MultipartFile srcFile = null;
		try {
			srcFile = mrequest.getFileMap().get(file_field);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return srcFile;
	}
	
	/**
	 * 保存文件 ，这是支持多文件的上存方式
	 * @param srcFile
	 * @param dir
	 * @return param 如果上传失败，它会为null
	 */
	public Map<String,Object> saveFile(MultipartFile srcFile, File dir) {
		Map<String,Object> param = null;//由保存的文件对象转换成的参数对象

		//如果保存成功，返回的信息
		if(srcFile == null || srcFile.getSize() == 0) return param;//没使用MultipartHttpServletRequest请求，这里会返回
		
		//获取文件内容，并转成md5，md5用做文件名字，最后创建保存的目标文件
		byte[] content = null;
		try {
			content = srcFile.getBytes();
		} catch (IOException e) {
			return param;//没有文件内容试，这里会返回
		}
		String MD5 = DigestUtils.md5DigestAsHex(content);
		//是否需要判断文件大小小于1M才存到数据库呢？
		
		//判断文件夹路径是否存在
		if(!FileUtils.exists(dir)) dir.mkdirs();
		
		//加上文件名,使用上传文件的原名
		File targetFile = new File(dir, FileUtils.rename(MD5, srcFile.getOriginalFilename()));
		//保存文件到目标文件
		try {
			FileCopyUtils.copy(content, targetFile);
		} catch (IOException e) {
			//文件保存错误，请检查服务器磁盘是否有足够空间或者路径是否正确
			return param;
		}
		//组装参数
		param = new HashMap<String, Object>();
		param.put("datetime", DateTimeUtil.getDate());		
		param.put("md5", MD5);
		param.put("size", targetFile.length());
		param.put("path", FileUtils.getRelativePathFormFile(dir, Constant.FILE_DIR));
		param.put("filename", targetFile.getName());
		//上存成功
		return param;
	}

	
	/**
	 * 下载文件
	 * @param response
	 * @param targetFileName 下载文件保存的文件名,如果没有,则用downLoadFile的文件名
	 * @param downLoadFile 需要下载的文件对象
	 */
	public void downloadFile(HttpServletResponse response, String targetFileName, File downLoadFile){
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			//保存文件的文件名
			if(StringUtils.isEmpty(targetFileName)){
				targetFileName = downLoadFile.getName();
			}			
			// 获取文件长度
			long fileLength = downLoadFile.length();
			//设置下载参数
			response.setContentType("application/octet-stream");			
			response.setHeader("Content-disposition", "attachment; filename=" + new String(targetFileName.getBytes("UTF-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			//下载数据
			bis = new BufferedInputStream(new FileInputStream(downLoadFile));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			bis.close();
			bos.close();
		} catch (Exception e) {
			if(bis != null){
				try {
					bis.close();
				} catch (IOException ex) {
				}
			}
			if(bos != null){
				try {
					bos.close();
				} catch (IOException ex) {
				}
			}
		}
	}
	
	
	
	public void writeResponse(HttpServletResponse response, String message) {
		response.setContentType("text/html;charset=UTF-8");
		try {
			response.getWriter().write(message);
			response.getWriter().close();
		} catch (IOException e) {
		}
	}
	
	

}
