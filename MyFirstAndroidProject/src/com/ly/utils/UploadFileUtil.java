package com.ly.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.util.Log;

import com.ly.po.FormFile;

public class UploadFileUtil {
	
	private static final String BOUNDARY = "--"+System.currentTimeMillis();  //分隔符
	
	public void upload(Map<String, Object> params, FormFile[] files, String uploadUrl) throws IOException{
		
		StringBuilder sb = new StringBuilder();
		int fileLength = 0;
		
		/**
		 * 拼接普通表单数据
		 */
		for(String key : params.keySet()){
			sb.append("--"+BOUNDARY+"\r\n");
			sb.append("Content-Disposition: form-data;name=" + key +"\r\n");
			sb.append("\r\n");
			sb.append(params.get(key) + "\r\n");
		}
		
		/**
		 * 上传文件头部拼接
		 */
		
		for(FormFile file : files){
			sb.append("--"+BOUNDARY+"\r\n");
			sb.append("Content-Disposition: form-data;name=" + file.getName() + "; filename=" + file.getFilename() + "\r\n");
			sb.append("Content-Type: "+file.getContentType() + "\r\n");
			sb.append("\r\n");
			fileLength += file.getFile().length();
		}
		
		byte[] headInfo = sb.toString().getBytes("utf-8");
		byte[] endInfo = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");
		
		URL url = new URL(uploadUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("post");
		conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
		conn.setRequestProperty("Content-Length", String.valueOf(headInfo.length + fileLength + endInfo.length));
		conn.setDoOutput(true);
		
		OutputStream out = conn.getOutputStream();
		out.write(headInfo);
		byte[] buf = new byte[1024];
		int len;
		InputStream in = null;
		for(FormFile file : files){
			in = new FileInputStream(file.getFile());
			while((len = in.read(buf)) != -1){
				out.write(buf, 0, len);
			}
		}
		
		out.write(endInfo);
		if (in != null) {
			in.close();
		}
		out.close();
		
		if (conn.getResponseCode() == 200) {
			Log.i("upload", "上传成功！！！");
		}
	}

}
