package com.gykj.controller;


import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.gykj.Constant;
import com.gykj.dao.UserCacheDao;
import com.gykj.interceptor.AuthPassport;
import com.gykj.pojo.InterfaceGroup;
import com.gykj.pojo.User;
import com.gykj.pojo.UserCache;
import com.gykj.service.UserService;
import com.gykj.utils.BeanUtils;
import com.gykj.utils.JsonUtils;




/**
 * 用户 控制器
 * @author xianyl
 *
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseControllerByCRUD<UserService, User>{	
	@Resource
	private UserCacheDao userCacheDao;
	

	@RequestMapping(value="/list", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> list(User user, Integer pageNo, Integer pageSize) {
		return super.list(BeanUtils.beanToMap(user), pageNo, pageSize);
	}
	

	@RequestMapping(value="/page", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> page(User user, Integer pageSize){
		return super.page(BeanUtils.beanToMap(user), pageSize);
	} 



	@RequestMapping(value="/info", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> info(String id){
		return super.info(id);
	} 
	
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/save", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> save(@RequestParam(required=false) String model) {
		User user = JsonUtils.toObject(model, baseService.getDao().getPojoClass());
		//验证对象，如果有问题则返回信息
		
		//检查一下数据是否已经存在数据
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("name", user.getName());
		User bean = baseService.find(params);
		if(bean != null){
			return getBaseData(Constant.RETURN_CODE_DATA_EXIST, "该账号已存在", bean);
		}
	
		
		//先把密码MD5一下
		if(!StringUtils.isEmpty(user.getPassword())){
			String password_md5 = DigestUtils.md5DigestAsHex(user.getPassword()
											.trim().getBytes());
			user.setPassword(password_md5);
		}	
		//再保存
		return super.save(user);
    } 
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/edit", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> edit(@RequestParam(required=false) String model) {
		User user = JsonUtils.toObject(model, baseService.getDao().getPojoClass());
		//验证对象，如果有问题则返回信息
	
		User bean = null;
		try {
			//检查一下数据是否已经存在数据
			bean = baseService.get(user.getId());
		} catch (Exception e) {
		}
	
		if(bean == null){
			return getBaseData(Constant.RETURN_CODE_FAIL, "该账号不存在", user);
		}
		//密码不变
		user.setPassword(null);
		return super.edit(user);
	}
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/delete", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> delete(User user) {
		return super.delete(user);
	}
	
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/delBatch", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> delBatch(@RequestParam("ids[]")Object[] ids) {
		//get的方式 delBatch?ids[]=16&ids[]=17
		return super.delBatch(ids);
	} 
	
	@AuthPassport(roles={"admin"})
	@RequestMapping(value="/editBatch", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> editBatch(@RequestParam("ids[]")Object[] ids, User user) {
		return super.editBatch(BeanUtils.beanToMap(user), ids);
	}
	
	
	//特殊接口	
	@RequestMapping(value="/changePassword", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> changePassword(User user, String oldpassword) {
		//先检查数据
		User bean = null;
		try {
			//检查一下数据是否已经存在数据
			bean = baseService.get(user.getId());
		} catch (Exception e) {
		}	
		if(bean == null){
			return getBaseData(Constant.RETURN_CODE_FAIL, "该账号不存在", bean);
		}
		
		if(StringUtils.isEmpty(user.getPassword())){
			return getBaseData(Constant.RETURN_CODE_FAIL, "请录入新密码", user);				
		}
		if(StringUtils.isEmpty(oldpassword)){
			return getBaseData(Constant.RETURN_CODE_FAIL, "请录入旧密码", user);				
		}
		//整理对象数据,要处理密码
		String password_md5 = DigestUtils.md5DigestAsHex(oldpassword.getBytes());
		if(!bean.getPassword().equals(password_md5)){
			return getBaseData(Constant.RETURN_CODE_FAIL, "旧密码不正确", user);
		}
		//把新密码md5
		user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
		//去掉token
		user.setToken(null);
		return super.edit(user);
	}
	
	
	
	
	@RequestMapping(value="/logOut", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> logOut(User user){
		int code = Constant.RETURN_CODE_FAIL;
		try {
			code = baseService.logOut(user) 
					? Constant.RETURN_CODE_SUCCESS 
							: Constant.RETURN_CODE_FAIL;
		} catch (Exception e) {
		}
		return getBaseData(code, null, null);
	}
	
	@RequestMapping(value="/logIn", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> logIn(User user){		
		int code = Constant.RETURN_CODE_FAIL;		
		//登录
		User bean = baseService.logIn(user);
		if(bean == null)
			return getBaseData(code, "登录失败,用户或者密码错误", null);
	
		//这里是正常登录成功的
		code =  Constant.RETURN_CODE_SUCCESS;
		//整理返回的数据
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("token", bean.getToken());
		map.put("id", bean.getId());
		map.put("name", bean.getName());
		map.put("role", bean.getRole());
		map.put("ig_name", bean.getIg_name());
		
		return getBaseData(code, "登录成功", map);
	} 
	
	@RequestMapping(value="/getUserByOpenid", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> getUserByOpenid(String openid){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("openid", openid);
		UserCache userCache = userCacheDao.findUserByOpenid(openid);
		Map<String, Object> pojo;
		if (userCache != null) {
			pojo = getBaseData(null, null, userCache);
		} else {
			pojo = getBaseData(Constant.RETURN_CODE_FAIL, null, null);
		}
		return pojo;
	}

	@RequestMapping(value="/editOpenid", method={RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> editOpenid(String openid){
		int code = Constant.RETURN_CODE_FAIL;
		try {
			code = userCacheDao.loginOut(openid) 
					? Constant.RETURN_CODE_SUCCESS 
							: Constant.RETURN_CODE_FAIL;
		} catch (Exception e) {
		}
		return getBaseData(code, null, null);
	}
	
}
