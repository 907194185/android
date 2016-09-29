package com.gykj.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * 基础类
 * 
 * @author xianyl
 * 
 */
@Entity
public class Pojo implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	protected String id;
	
	protected Integer flag = 0;// 标记号（-1：无效 0： 有效，默认有效


	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	

	
}