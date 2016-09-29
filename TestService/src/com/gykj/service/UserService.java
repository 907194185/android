package com.gykj.service;

import com.gykj.dao.UserDao;
import com.gykj.pojo.User;







/**
 * 用户  服务层接口
 * @author xianyl
 *
 * @param <T>
 */
public interface UserService extends BaseService<UserDao,User>{

	/**
	 * 注册   先在外面验证用户是否存在吧
	 * @param user
	 * @return  注册成功的账号，如果User为 null则注册失败
	 */
	public User register(User user);
	
	
	/**
	 * 登录
	 * @param user
	 * @return  登录成功的账号，如果User为 null则查询条件错误（账号或者密码错误）
	 */
	public User logIn(User user);
		
	
	/**
	 * 退出登录
	 * @param user
	 * @return 是否退出成功,如果无此用户数据或者更新用户token值失败，则为退出失败
	 */
	public boolean logOut(User user);


	/**
	 * 根据token查找user
	 * @param token
	 * @return 
	 */
	public User findUserByToken(String token);

}
