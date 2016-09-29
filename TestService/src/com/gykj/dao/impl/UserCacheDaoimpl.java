package com.gykj.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

import com.gykj.dao.UserCacheDao;
import com.gykj.pojo.UserCache;
import com.gykj.utils.BeanUtils;
import com.gykj.utils.MongodbUtil;




/**
 * 用户缓存 持久层实现类
 * 
 * @author xianyl
 * 
 * @param <T>
 */
@Repository(value = "userCacheDao")
public class UserCacheDaoimpl extends MongoDaoimpl<UserCache> implements UserCacheDao {
	
	
	@Override
	public UserCache update(UserCache userCache) {
		//查找条件
		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		hashMap.put("ig_name", userCache.getIg_name());
		hashMap.put("name", userCache.getName());		
		Query query = MongodbUtil.map2Query(hashMap);
		//更新的值
		Update update = MongodbUtil.map2Update(BeanUtils.beanToMap(userCache), field_id);
		//更新
		return super.updateInser(query, update) > 0 ? userCache : null;
	}

	@Override
	public UserCache findUserByToken(String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		return find(params);
	}

	@Override
	public UserCache findUserByIgAndName(String ig_name, String name) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", name);
		params.put("ig_name", ig_name);
		return find(params);
	}

	@Override
	public UserCache findUserByOpenid(String openid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openid", openid);
		return find(params);
	}

	@Override
	public boolean loginOut(String openid) {
		// TODO Auto-generated method stub
		UserCache userCache = findUserByOpenid(openid);
		if (userCache==null) {
			return false;
		}
		userCache.setOpenid("");
		update(userCache);
		return true;
	}
	
	
	
}
