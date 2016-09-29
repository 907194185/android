package com.gykj.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.DBRef;

import com.fasterxml.jackson.annotation.JsonIgnore;


/**
 * 用户类
 * 
 * @author xianyl
 * 
 */
@Entity
public class User extends Pojo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	//@Size(min=6,max=12,message="账号长度必须6~12位")
	@NotEmpty(message="账号不能为空", groups={Save.class})
	private String name;// 账号,唯一
	
	//@JsonIgnore
	//@Size(min=6,max=20,message="密码长度必须6~20位")//@Pattern(regexp="\\S+", message="密码规则不对")
	@NotEmpty(message="密码不能为空", groups={Save.class})
	private String password;// 用户密码
	
	
	@NotEmpty(message="角色不能为空", groups={Save.class})
	private String role;// 角色名
	
	@JsonIgnore
	private String token;// 用户登录状态的标识
	private String ig_name;// 接口组名称
	
	@DBRef
	private List<Tcp> tcps;// 用户对应的tcp服务配置，可以多个，角色client有效
	


	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
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

	public List<Tcp> getTcps() {
		return tcps;
	}

	public void setTcps(List<Tcp> tcps) {
		this.tcps = tcps;
	}

	

	@Override
	public String toString() {
		return "User [id=" + getId() + ", name=" + name + ", password=" + password
				+ ", role=" + role + ", token=" + token + ", ig_name="
				+ ig_name + ", tcps=" + tcps + ", flag=" + getFlag() + "]";
	}


	
}