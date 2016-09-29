package com.gykj.service.impl;


import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.stereotype.Repository;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import com.gykj.Constant;
import com.gykj.dao.UserDao;
import com.gykj.pojo.User;
import com.gykj.service.UserService;





/**
 * 用户   服务层实现类,操作user，要把token删掉
 * @author xianyl
 *
 * @param <T>
 */
@Repository(value="userService")
public class UserServiceimpl extends BaseServiceimpl<UserDao,User> implements UserService{
	
	
	
	@Override
	public List<User> list(Map<String, Object> params, Integer pageNo,
			Integer pageSize) {
		params.remove(Constant.FIELD_TOKEN);
		return super.list(params, pageNo, pageSize);
	}
	
	@Override
	public Integer countRow(Map<String, Object> params) {
		params.remove(Constant.FIELD_TOKEN);
		return super.countRow(params);
	}
	@Override
	public Integer editBatch(Map<String, Object> params, Object... ids) {
		params.remove(Constant.FIELD_TOKEN);
		return super.editBatch(params, ids);
	}
	
	@Override
	public User insert(User user) {
		user.setToken(null);
		return super.insert(user);
	}
	@Override
	public User update(User user) {
		user.setToken(null);
		return super.update(user);
	}
	@Override
	public User delete(User user) {
		user.setToken(null);
		return super.delete(user);
	}
	
	
	@Override
	public User findUserByToken(String token) {
		return getDao().findUserByToken(token);
	}
	
	
	
	@Override
	public User register(User user) {
		try {
			//再把密码MD5一下
			String password_md5 = DigestUtils.md5DigestAsHex(user.getPassword().trim().getBytes());
			user.setPassword(password_md5);
			//注册就当登录吧?
			user.setToken(UUID.randomUUID().toString());
		} catch (Exception e) {
			return null;
		}
		//插入数据并且获取最新的数据
		return insert(user);
	}

	
	@Override
	public User logIn(User user) {
		try {
			//先把密码MD5一下
			String password_md5 = DigestUtils
					.md5DigestAsHex(user.getPassword()
							.trim().getBytes());
			
			//查找用户信息
			user = getDao().findModelByName(user.getName().trim());
			
			//检查密码是否正确
			if(StringUtils.isEmpty(password_md5) 
					|| !password_md5.equals(user.getPassword())){
				return null;
			}
		} catch (Exception e) {
			return null;//获取数据库的用户,如果有任何异常，置空user
		}
		
		//然后看看是否有token,如果没有,加上token ,使用uuid，
		if(StringUtils.isEmpty(user.getToken())){
			user.setToken(UUID.randomUUID().toString());
			return super.update(user);
		}
		return user;
	}

	



	@Override
	public boolean logOut(User user) {
		try {
			//获取数据库的用户,如果有任何异常，置空user
			user = getDao().findUserByToken(user.getToken().trim());
			if(user == null) return false;
			//更新token为空,token有效时间也置为空
			user.setToken(null);
		} catch (Exception e) {
			return false;
		}		
		//退出状态跟是否修改数据状态一致
		return super.update(user) != null;
	}

	





	




}
