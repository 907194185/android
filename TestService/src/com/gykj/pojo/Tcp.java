package com.gykj.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;



/**
 * tcp类
 * 
 * @author xianyl
 * 
 */
@Entity
public class Tcp extends Pojo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@NotEmpty(message="名字不能为空", groups={Save.class})
	private String name;// 账号,唯一,使用tcp设计时的client编号
	
	//@Pattern(regexp="\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", message="IP规则不对")
	@NotEmpty(message="IP不能为空", groups={Save.class})
	private String ip;
	
	//@Pattern(regexp="\\d{1,5}", message="PORT规则不对")
	@NotNull(message="PORT不能为空", groups={Save.class})
	private Integer port;
	
	private String description;// 说明
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	
}