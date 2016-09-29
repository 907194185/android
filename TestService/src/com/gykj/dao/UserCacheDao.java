package com.gykj.dao;

import com.gykj.pojo.UserCache;


/**
 * 用户缓存数据  持久层接口
 * @author xianyl
 *
 * @param <T>
 */
public interface UserCacheDao extends BaseDao<UserCache>{
	
	/**
	 * 根据ig_name跟name查找user
	 * @param token
	 * @return 
	 */
	public UserCache findUserByIgAndName(String ig_name, String name);	
	
	/**
	 * 根据token查找user
	 * @param token
	 * @return 
	 */
	public UserCache findUserByToken(String token);
	
	/**
	 * 根据token查找user
	 * @param token
	 * @return 
	 */
	public UserCache findUserByOpenid(String openid);
	
	public boolean loginOut(String openid);

}
