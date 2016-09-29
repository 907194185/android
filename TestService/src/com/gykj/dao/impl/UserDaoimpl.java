package com.gykj.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gykj.dao.UserDao;
import com.gykj.pojo.User;




/**
 * 用户 持久层实现类
 * 
 * @author xianyl
 * 
 * @param <T>
 */
@Repository(value = "userDao")
public class UserDaoimpl extends MongoDaoimpl<User> implements UserDao {
	

	@Override
	public User findUserByToken(String token) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("token", token);
		return find(params);
	}
	
	
	
}
