package com.gykj.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.gykj.dao.UserCacheDao;
import com.gykj.pojo.InterfaceList;
import com.gykj.pojo.UserCache;

public class InstitutionFilterUtils {

	public static int filter(InterfaceList interfaceList, Map<String, Object> param,UserCacheDao userCacheDao){
		if (interfaceList.getAction().equalsIgnoreCase("list") || interfaceList.getAction().equalsIgnoreCase("link")) {

			List<Map<String,Object>> param_obj = null; //界面传来的obj参数
			UserCache userCache = userCacheDao.findUserByToken(param.get("token").toString());
			List<Map<String, Object>> roles = (List<Map<String, Object>>) userCache.getCache().get("roles"); //用户角色
			//System.out.println("roles=="+roles);
			String roleName = "";
			for(Map<String, Object> role : roles){
				roleName = role.get("name").toString();
				break;
			}
			Map<String, Object> operation = null;
			//System.out.println("用户institution="+userCache.getCache().get("institution"));
			//登录用户的institution
			Map<String, Object> user_institution = (Map<String, Object>) userCache.getCache().get("institution");
			//System.out.println("用户i="+user_institution);
			String insName = user_institution.get("name")==null?"":(String) user_institution.get("name");
			//重新封装institution参数
			Map<String, Object> temp = user_institution;

			//界面传的参数 获取到institution  
			param_obj = JsonUtils.toCollection(ObjectUtils.toString(param.get("obj"))
					, new TypeReference<List<Map<String,Object>>>(){});
			boolean hasType = false;
			if("user".equals(interfaceList.getCollection())	||"organization".equals(interfaceList.getCollection())){

				//System.out.println("参数="+param_obj);
				boolean hasInstitution = false; //标记界面是否传入institution参数

				for(Map<String, Object> map : param_obj){
					//界面有传institution
					if (map.containsKey("institution")) {
						hasInstitution = true;
						temp.remove("name"); //不传name
						temp.remove("_id"); //不传_id
						Map<String, Object> param_institution = (Map<String, Object>) map.get("institution");
						String key = "";
						Object user_value = null;
						Object param_value = null;
						//循环institution里的key-value 和登录用户的institution逐一进行判断
						for(Entry<String, Object> entry : param_institution.entrySet()){
							key = entry.getKey();
							user_value = user_institution.get(key);
							param_value = param_institution.get(key);

							//判断到level 必须>=登录用户的level  否则无权限查询
							if (key.equalsIgnoreCase("level")) {
								if (param_value!=null && !param_value.equals("null") && param_value.toString().length()>0) {
									try {
										if (Integer.parseInt(user_value.toString())>Integer.parseInt(param_value.toString())) {
											//跳出 不能查询
											return 1;
										}else {
											temp.put("level", Integer.parseInt(param_value.toString()));
										}
									} catch (Exception e) {
										return 1;
									}
								}
							}else if (key.equalsIgnoreCase("type")) {
								if (param_value!=null && param_value.toString().trim().length()>0 && user_value!=null && !user_value.equals(param_value)) {
									hasType = true;
									if (roleName.equalsIgnoreCase("系统管理员") || roleName.equalsIgnoreCase("区级管理员")) {
										temp.put("type", param_value);
									}else {
										return 1;
									}
								}else if(param_value!=null && param_value.toString().trim().length()>0 && user_value==null){
									hasType = true;
									temp.put("type", param_value);
								}
							}else{
								if (user_value==null || user_value.equals("null") || user_value.toString().trim().length()<=0) {
									//if (param_value!=null && param_value.toString().trim().length()>0 && !user_value.equals(param_value)) {
									temp.put(key, param_value);
									//}
								}
							}
						}
						if ((roleName.equalsIgnoreCase("系统管理员") || roleName.equalsIgnoreCase("区级管理员")) && !hasType) {
							temp.remove("type");
						}
						map.put("institution", temp);
						//System.out.println("map="+map);
						break;
					}
				}

				//参数未传institution  使用用户institution

				if (!hasInstitution) {
					temp.remove("name"); //不传name
					temp.remove("_id");
					if (roleName.equalsIgnoreCase("系统管理员") || roleName.equalsIgnoreCase("区级管理员")) {
						temp.remove("type");
					}
					temp.put("level", Integer.parseInt(temp.get("level").toString()));
					if (param_obj.size()>0) {
						if ("institution".equals(interfaceList.getCollection())) {
							param_obj.remove(0);
							param_obj.add(temp);
						}else {
							param_obj.get(0).put("institution", temp);
						}
					}else {
						if ("institution".equals(interfaceList.getCollection())) {
							param_obj.add(temp);
						}else {
							Map<String, Object> userMap = new HashMap<String, Object>();
							userMap.put("institution", temp);
							param_obj.add(userMap);
						}
					}

				}

				//System.out.println("修改后："+param_obj);
				param.put("obj", JsonUtils.toJson(param_obj));

				if (param.containsKey("operation")) {
					operation = JsonUtils.toCollection(ObjectUtils.toString(param.get("operation")),new TypeReference<Map<String,Object>>(){});
					if ("institution".equals(interfaceList.getCollection())) {
						operation.put("level", "gte");
					}else {
						operation.put("institution.level", "gte");
					}
				}else {
					operation = new HashMap<String, Object>();
					if ("institution".equals(interfaceList.getCollection())) {
						operation.put("level", "gte");
					}else {
						operation.put("institution.level", "gte");
					}
				}
				param.put("operation", JsonUtils.toJson(operation));
			}else if("industrytype".equals(interfaceList.getCollection())){
				if (!roleName.equalsIgnoreCase("系统管理员") && !roleName.equalsIgnoreCase("区级管理员")) {
					Map<String, Object> name = new HashMap<String, Object>();
					name.put("name", user_institution.get("type"));
					param_obj.add(name);
					param.put("obj", JsonUtils.toJson(param_obj));
					//System.out.println("industrytype参数="+param_obj);
				}
			}else if("institution".equals(interfaceList.getCollection())){
				System.out.println("institution================start");
				String key = "";
				Object user_value = null;
				Object param_value = null;
				temp.remove("name"); //不传name
				temp.remove("_id");
				if (param_obj.size()>0) {

					if (!param_obj.get(0).isEmpty()) {
						temp = new HashMap<String, Object>();
						for(Entry<String, Object> entry : param_obj.get(0).entrySet()){
							key = entry.getKey();
							param_value = entry.getValue();
							//temp.put(key, param_value);
							if (key.equalsIgnoreCase("type") && param_value!=null && !param_value.equals("null") && param_value.toString().trim().length()>0) {
								if (!param_value.equals(temp.get("type")) || (roleName.equalsIgnoreCase("系统管理员") || roleName.equalsIgnoreCase("区级管理员"))) {
									temp.put("type", param_value);
									hasType = true;
								}else {
									return 1;
								}
							} 
							//System.out.println(key.equalsIgnoreCase("level") && param_value!=null && !param_value.equals("null") && param_value.toString().trim().length()>0);
							else if(key.equalsIgnoreCase("level") && param_value!=null && !param_value.equals("null") && param_value.toString().trim().length()>0) {
								try {
									System.out.println(Integer.parseInt(param_value.toString())==2 && Integer.parseInt(user_institution.get("level").toString())>=2);
									if (Integer.parseInt(user_institution.get("level").toString())>=2) {
										if (Integer.parseInt(param_value.toString())==2) {
											temp.put("area", user_institution.get("area"));
										}else if (Integer.parseInt(param_value.toString())==3 && Integer.parseInt(user_institution.get("level").toString())==3) {
											temp.put("town", user_institution.get("town"));
										}else if (Integer.parseInt(param_value.toString())==3 && Integer.parseInt(user_institution.get("level").toString())==4) {
											temp.put("town", user_institution.get("town"));
										}else if (Integer.parseInt(param_value.toString())==4 && Integer.parseInt(user_institution.get("level").toString())==4) {
											temp.put("name", insName);
										}
									}
									temp.put("level", Integer.parseInt(param_value.toString()));
								} catch (Exception e) {
									return 1;
								}
							}else {
								if (user_value==null || user_value.equals("null") || user_value.toString().trim().length()<=0) {
									temp.put(key, param_value);
								}
							}
						}
					}
					if ((roleName.equalsIgnoreCase("系统管理员") || roleName.equalsIgnoreCase("区级管理员")) && !hasType) {
						temp.remove("type");
					}
					param_obj.remove(0);
					param_obj.add(0, temp);
					param.put("obj", JsonUtils.toJson(param_obj));
					if (temp.containsKey("level") && !interfaceList.getAction().equalsIgnoreCase("link")) {
						if (!param.containsKey("operation")) {
							operation = new HashMap<String, Object>();
							operation.put("level", "gte");
							param.put("operation", JsonUtils.toJson(operation));
						}
						
					}

				}
				System.out.println("institution================end");
				System.out.println("ii-==="+param);
			}
		}
		System.out.println("XXXX:"+param);
		return 0;
	}




}
