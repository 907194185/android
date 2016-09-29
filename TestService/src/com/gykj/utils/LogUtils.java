package com.gykj.utils;

import org.slf4j.Logger;  
import org.slf4j.LoggerFactory;  
  
/**
 * 日志工具类，使用slf4j，为方便更换日志包
 * @author xianyl
 *
 */
public class LogUtils {  
	private static boolean isDebug = true;
	
    //使用slf4j创建日志对象
   private static Logger logger = LoggerFactory.getLogger(LogUtils.class);  
  
    public static void error(String message, Throwable throwable) {
        if(isDebug) logger.error(message, throwable);
    }  
    public static void debug(String message, Throwable throwable) {  
    	if(isDebug) logger.debug(message, throwable);
    }  
    public static void info(String message, Throwable throwable) {    	
    	if(isDebug) logger.info(message, throwable);  
    }  
}  
