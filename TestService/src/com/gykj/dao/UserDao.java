package com.gykj.dao;

import com.gykj.pojo.User;


/**
 * 用户数据  持久层接口
 * @author xianyl
 *
 * @param <T>
 */
public interface UserDao extends BaseDao<User>{
	
		
	
	/**
	 * 根据token查找user
	 * @param token
	 * @return 
	 */
	public User findUserByToken(String token);

}
