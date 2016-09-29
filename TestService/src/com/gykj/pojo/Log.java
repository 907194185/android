package com.gykj.pojo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;



/**
 * 日志类
 * 
 * @author xianyl
 */
@Entity
public class Log implements Serializable {
	private static final long serialVersionUID = 1L;
	@Id
	private String id;
	@NotEmpty
	private String u_name;// 关联此日志的用户名
	@NotEmpty
	private String ig_name;//关联此日志的实际项目的命名空间名字
	@NotEmpty
	private String il_name;//关联此日志的接口清单标识
	@NotEmpty
	private String il_collection;//关联此日志的具体操作的数据
	@NotEmpty
	private String il_action;//关联此日志的具体操作
	@NotEmpty
	private String ts_name;//关联此日志的tcp名称
	
	private String content;//请求的内容
	
	
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date create_time;//请求的创建时间
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date ack_time;//请求的返回时间
	@NotNull
	private Boolean iscycle = false;//是否循环任务的日志，默认false
	
	@NotNull(message="标记号不能为空", groups={Save.class})
	private Integer flag;// 标记号（-1：无效，0：未返回请求，1：请求成功，2：请求不成功。）


	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getU_name() {
		return u_name;
	}

	public void setU_name(String u_name) {
		this.u_name = u_name;
	}

	public String getIg_name() {
		return ig_name;
	}

	public void setIg_name(String ig_name) {
		this.ig_name = ig_name;
	}

	public String getIl_name() {
		return il_name;
	}

	public void setIl_name(String il_name) {
		this.il_name = il_name;
	}

	public String getIl_collection() {
		return il_collection;
	}

	public void setIl_collection(String il_collection) {
		this.il_collection = il_collection;
	}

	public String getIl_action() {
		return il_action;
	}

	public void setIl_action(String il_action) {
		this.il_action = il_action;
	}

	public String getTs_name() {
		return ts_name;
	}

	public void setTs_name(String ts_name) {
		this.ts_name = ts_name;
	}
	

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}

	public Date getAck_time() {
		return ack_time;
	}

	public void setAck_time(Date ack_time) {
		this.ack_time = ack_time;
	}

	public Boolean getIscycle() {
		return iscycle;
	}

	public void setIscycle(Boolean iscycle) {
		this.iscycle = iscycle;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	

	
}