package com.gykj.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import javax.persistence.Entity;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 实际用户缓存类
 * 用于记录实际用户的数据，进行权限验证等等的啊哦做
 * @author xianyl
 * 
 */
@Entity
public class UserCache extends Pojo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String name;// 用户名字 name + ig_name唯一
	private String ig_name;// 接口组名称，实际项目的标识
	
	private String token;// 用户登录状态的标识
		
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date login_time;//登录时间	
	
	private Map<String, Object> cache;// 实际用户的数据
	
	private String openid;// 微信id,这个不一定有	
	
	
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getIg_name() {
		return ig_name;
	}

	public void setIg_name(String ig_name) {
		this.ig_name = ig_name;
	}

	public Date getLogin_time() {
		return login_time;
	}

	public void setLogin_time(Date login_time) {
		this.login_time = login_time;
	}

	public Map<String, Object> getCache() {
		return cache;
	}

	public void setCache(Map<String, Object> cache) {
		this.cache = cache;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}
	


	
}