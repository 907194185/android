package com.gykj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javax.annotation.Resource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;


import com.gykj.dao.InterfaceGroupDao;
import com.gykj.dao.LogDao;
import com.gykj.dao.UserCacheDao;
import com.gykj.dao.UserDao;
import com.gykj.pojo.InterfaceGroup;
import com.gykj.pojo.InterfaceList;
import com.gykj.pojo.Log;
import com.gykj.pojo.User;
import com.gykj.tcp.TcpCallable;





/**
 * 平台  服务类，主要用于tcp服务对接
 * @author xianyl
 *
 * @param <T>
 */
@Repository(value="informationService")
public class InformationService{

	@Resource
	private InterfaceGroupDao interfaceGroupDao;	
	@Resource
	private LogDao logDao;
	@Resource
	private UserCacheDao userCacheDao;
	@Resource
	private UserDao userDao;
	
	
	/**
	 * 获取接口清单
	 * @param collection 
	 * @param action
	 * @param token
	 * @param log 这是请求日志对象，传参进去获取相关信息的
	 * @return
	 */
	public InterfaceList getInterfaceList(String collection,String action,String token, Log log){
		InterfaceList interfaceList = null;
		//根据token找用户
		User user = userDao.findUserByToken(token);
		if(user==null || StringUtils.isEmpty(user.getIg_name())) return interfaceList;//无用户,或者用户无接口权限
				
		//根据用户\collection\action找接口组
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", user.getIg_name());
		params.put("type", 1);
		InterfaceGroup interfaceGroup = interfaceGroupDao.find(params);
		if(interfaceGroup==null) return interfaceList;//无接口
		List<InterfaceList> interfaces = interfaceGroup.getInterfacelist();
		if(interfaces == null || interfaces.size() == 0) return interfaceList;//接口组无接口
		
		//遍历接口列表，挑选需要的接口
		for(InterfaceList i : interfaces){
			if(i == null) continue;
			if(collection.equals(i.getCollection()) && action.equals(i.getAction())){
				interfaceList = i;
				break;
			}
		}

		//先记录下日志需要的信息，后面继续补充信息 ，如果进行tcp 请求，则记录下
		if(log != null){
			log.setU_name(user.getName());
			log.setIg_name(user.getIg_name());
		}
		//返回 接口清单
		return interfaceList;
	}
	

	
	
	
	/**
	 * 请求tcp服务
	 * @param interfaceList
	 * @param param
	 * @param log
	 * @return
	 */
	public Callable<Map<String, Object>> requestTCP(InterfaceList interfaceList, Map<String, Object> param, Log log){
		return new TcpCallable(interfaceList,param, log, logDao, userCacheDao);
	}
	
	
	
	
	

	
	
	


}
