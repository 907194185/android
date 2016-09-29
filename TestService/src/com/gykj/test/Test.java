package com.gykj.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import sun.print.resources.serviceui;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gykj.utils.JsonUtils;

public class Test {
	public static void main(String[] args) {
		/*String  a = "{\"taskId\":\"5730214812e6930d408dfd74\",\"contentJson\":{\"message\":\"操作成功\",\"result\":[],\"totalResult\":0,\"code\":\"0000\",\"currentPage\":1,\"totalPage\":0},\"taskType\":1,\"datetime\":\"20160509133400\",\"clientId\":\"fsxfsh_01001\"}";
		System.out.println(UUID.randomUUID().toString());*/
		
		/*List<Map<String, Integer>> list = new ArrayList<Map<String,Integer>>();
		list.add(new HashMap<String, Integer>());
		for(Entry<String, Integer> entry : list.get(0).entrySet()){
			System.out.println(1111);
		}*/
		//System.out.println(UUID.randomUUID().toString());
		
		//Logger logger = Logger.getLogger(Test.class);
		//logger.info("liuyongllllllllllllllllll");
		List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("a", "null");
		map.put("b", null);
		list.add(map);
		System.out.println(JsonUtils.toJson(list));
	}

}
