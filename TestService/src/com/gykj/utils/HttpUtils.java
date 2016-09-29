package com.gykj.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.log4j.Logger;

import com.gykj.Constant;





/**
 * 网络请求工具类
 * 
 * @author xianyl
 * 
 */
public class HttpUtils {

	static Logger logger = Logger.getLogger(HttpUtils.class);

	/**
	 * 获取请求配置参数，主要设置了请求超时
	 * @return
	 */
	public static RequestConfig getRequestConfig(){
		Builder configBuilder = RequestConfig.custom();
		configBuilder.setConnectTimeout(30000);//连接时间
		configBuilder.setSocketTimeout(30000);//读写数据时间
		return configBuilder.build();
	}



	/**
	 * 执行请求
	 * 
	 * @return
	 */
	public static String execute(String url) {
		// 执行请求
		String content = null;
		try {
			// 请求CloseableHttpResponse
			HttpResponse response = HttpClientBuilder.create()
					.setRetryHandler(new DefaultHttpRequestRetryHandler())// 可以设置重试参数,默认3次？
					.setDefaultRequestConfig(getRequestConfig())//设置配置参数
					.build()
					.execute(new HttpGet(url));
			// 如果请求成功	
			if (response.getStatusLine().getStatusCode() == 200) {
				content = getString(response.getEntity()).trim();
				logger.info("content=="+content);
				System.out.println(content);
			}
			logger.info("tttttttttttttttttttttttttttt==");
		} catch (Exception e) {
			logger.info("----"+e.getMessage());
			e.printStackTrace();
		}
		return content;
	}



	/**
	 * 解析数据
	 * 
	 * @param url
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static String getString(HttpEntity entity) {
		String content = null;
		if (entity == null) return content;
		try {
			InputStream inputStream = entity.getContent();
			if (inputStream == null) {
				return null;
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte[] buffer = new byte[10240];
			int len = -1;
			while ((len = inputStream.read(buffer)) != -1) {
				baos.write(buffer, 0, len);
			}
			baos.flush();
			baos.close();
			inputStream.close();
			content = new String(baos.toByteArray(), "gbk");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}


	/**
	 * 获取错误时需要跳转或重发的url
	 * @param code
	 * @param message
	 * @return
	 */
	public static StringBuilder getErrorUrl(Object code, String message) {
		return new StringBuilder("/error/dispose")
		.append("?").append(Constant.JSON_RESPONSE_FIELD_CODE).append("=").append(code)
		.append("&").append(Constant.JSON_RESPONSE_FIELD_MESSAGE).append("=").append(message);
	}

	/**
	 * 获取错误时需要跳转或重发的url
	 * @param code
	 * @param message
	 * @return
	 */
	public static StringBuilder getErrorUrlRedirect(Object code, String message) {
		try {
			message = URLEncoder.encode(message, "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		return new StringBuilder("/gykj/error/dispose")
		.append("?").append(Constant.JSON_RESPONSE_FIELD_CODE).append("=").append(code)
		.append("&").append(Constant.JSON_RESPONSE_FIELD_MESSAGE).append("=").append(message);
	}

}
