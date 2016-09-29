package com.gykj.utils;
import java.text.SimpleDateFormat;  
import java.util.TimeZone;  

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.type.TypeReference;  
import com.fasterxml.jackson.databind.DeserializationFeature;  
import com.fasterxml.jackson.databind.ObjectMapper;  
import com.fasterxml.jackson.databind.SerializationFeature;  


/**
 * json工具类
 * @author xianyl
 *
 */
public class JsonUtils {  
	
    private static final ObjectMapper objectMapper;  
    static {  
        objectMapper = new ObjectMapper();  
        //去掉默认的时间戳格式  
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);  
        //设置为中国上海时区  
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));  
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);  
        //空值不序列化  
        objectMapper.setSerializationInclusion(Include.NON_NULL);
        //反序列化时，属性不存在的兼容处理  
        objectMapper.getDeserializationConfig().withoutFeatures(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  
        //序列化时，日期的统一格式  
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));  
  
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);  
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);  
        //单引号处理  
        objectMapper.configure(com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);  
    }  
  
    
    /**
     * json转对象
     * @param json
     * @param clazz
     * @return
     */
    public static <T> T toObject(String json, Class<T> clazz) {
    	if(StringUtils.isEmpty(json)) return null;
        try {  
            return objectMapper.readValue(json, clazz);  
        } catch (Exception e) {  
        	LogUtils.error("json转对象异常", e);
        } 
        return null;  
    }  
  
    
    /**
     * 对象转json
     * @param entity
     * @return
     */
    public static <T> String toJson(T entity) {
    	if(entity == null) return null;
        try {  
            return objectMapper.writeValueAsString(entity);  
        } catch (Exception e) {  
        	LogUtils.error("对象转json异常", e);
        } 
        return null;  
    }  
  
    /**
     * json转集合
     * @param json
     * @param typeReference
     * @return
     */
    public static <T> T toCollection(String json, TypeReference<T> typeReference) { 
    	if(StringUtils.isEmpty(json)) return null;
        try {  
            return objectMapper.readValue(json, typeReference);  
        } catch (Exception e) {  
        	LogUtils.error("json转集合异常", e);
        } 
        return null;  
    }  
  
}  