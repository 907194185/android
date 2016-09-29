package com.gykj.utils;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gykj.pojo.Param;


/***
 * 参数工具类，主要用于验证、过滤等等
 * @author xianyl
 *
 */
public class ParamUtils {
	
	
	/**
	 * 验证参数，fileds没有的，不验证
	 * @param params 参数列表
	 * @param fileds 参数规则
	 * @param isValidateNecessity  是否验证字段的必要值，一般情况如果是sava就要验证，其它不用验证
	 * @return 是否通过验证
	 */
	public static Boolean validateParam(List<Map<String, Object>> params, List<Param> fileds, Boolean isValidateNecessity){
		if(fileds == null || fileds.size()==0) return true;//如果没有验证字段，直接通过验证
		if(params == null || params.size()==0) return !isValidateNecessity;//如果没有参数，再判断是否验证字段的必要值，验证则不通过，不验证则通过
		if(!isValidateNecessity) return true;
		for(Map<String, Object> param : params){
			if(validateParam(param,fileds,isValidateNecessity))
				continue;
			return false;
		}
		return true;	
	}
	
	
	/**
	 * 验证参数，fileds没有的，不验证
	 * @param param 参数
	 * @param fileds 参数规则
	 * @param isValidateNecessity  是否验证字段的必要值，一般情况如果是sava就要验证，其它不用验证
	 * @return 是否通过验证
	 */
	public static Boolean validateParam(Map<String, Object> param, List<Param> fileds, Boolean isValidateNecessity){
		if(fileds == null || fileds.size()==0) return true;//如果没有验证字段，直接通过验证
		if(param == null || param.size()==0) return !isValidateNecessity;//如果没有参数，再判断是否验证字段的必要值，验证则不通过，不验证则通过
		
		Object value;
		String regular;
		String type;
		for(Param filed : fileds){
			value = param.get(filed.getName());
			System.out.println(value instanceof List);
			if(value == null){
				//如果需要验证非null
				if(isValidateNecessity && filed.getIsnecessity()) return false;				
				continue;//如果没有此参数，则继续下一个
			}
			
			type = filed.getType();
			//判断type是否Object或者Array
			if("Object".equals(type) && !(value instanceof Map)) return false;
			if("Array".equals(type) && !(value instanceof List)) return false;
			
			//验证规则
			if("String".equals(type)){
				regular = filed.getRegular();
				if(!StringUtils.isEmpty(regular) 
						&& !Pattern.matches(regular, ObjectUtils.toString(value))){
					return false;				
				}
			}
			
		}
		
		return true;	
	}
	
	

	/**
	 * 过滤参数
	 * @param param 参数
	 * @param fileds 保留的参数key
	 * @return 过滤后的参数
	 */
	public static List<Map<String, Object>> filterParam(List<Map<String, Object>> params, List<String> req_param){
		if(params == null || req_param == null || req_param.size()==0 || params.size()==0) return params;
		for(Map<String, Object> param : params){
			filterParam(param, req_param);
		}
		return params;	
	}
	
	
	/**
	 * 过滤参数
	 * @param param 参数
	 * @param fileds 保留的参数key
	 * @return 过滤后的参数
	 */
	public static Map<String, Object> filterParam(Map<String, Object> param, List<String> req_param){
		if(param == null || req_param == null || req_param.size()==0 || param.size()==0) return param;
		Iterator<Entry<String, Object>> iterator = param.entrySet().iterator();
		Entry<String, Object> entry = null;
		while (iterator.hasNext()) {
			entry = iterator.next();
			//如果请求参数不包含该参数
			if(!req_param.contains(entry.getKey())){
				iterator.remove();
			}
		}
		return param;	
	}
	
	
	/**
	 * 整理参数，接收到前端的参数，是这样的格式{"obj":[{},{}],"operation":{},"page":1,"pageSize":10,"order":{}}
	 * 1、处理operation，page，pageSize，order，如果前端没传，则设置默认值null
	 * 2、转换obj为List<Map<String, Object>>类型
	 * @param params  前端的参数
	 * @return  返回obj参数
	 */
	public static List<Map<String, Object>> tidyParam(Map<String, Object> params){
		List<Map<String,Object>> obj = null;
		if(params == null || params.size()==0) return obj;
		//处理obj
		obj = JsonUtils.toCollection(ObjectUtils.toString(params.get("obj"))
										, new TypeReference<List<Map<String,Object>>>(){});
		//处理operation
		Map<String,Object> operation =JsonUtils
										.toCollection(ObjectUtils.toString(params.get("operation"))
												, new TypeReference<Map<String,Object>>(){});
		//处理operation
		Map<String,Object> order =JsonUtils
										.toCollection(ObjectUtils.toString(params.get("order"))
												, new TypeReference<Map<String,Object>>(){});
		//处理分页的参数
		Object page = params.get("page");
		Object pageSize = params.get("pageSize");
		//整理参数，根据tcp交互文档 ，只留obj、operation、order、page、pageSize
		params.clear();
		params.put("obj", obj);
		params.put("operation", operation);
		params.put("order", order);
		params.put("page", page);
		params.put("pageSize", pageSize);
		return obj;	
	}
}
