package com.gykj.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.DBRef;



/**
 * 接口清单类
 * 
 * @author xianyl
 */
@Entity
public class InterfaceList extends Pojo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="名字不能为空", groups={Save.class})
	private String name;// 无操作意义，只用于配置接口清单的标识，唯一，建议使用中文
	
	@NotEmpty(message="命名空间不能为空", groups={Save.class})
	private String collection;//操作具体数据的命名空间，对应数据库表，由数据平台提供
	
	@NotEmpty(message="具体动作不能为空", groups={Save.class})
	private String action;//操作数据的具体动作，例如crud，由数据平台提供
	
	@DBRef
	@NotNull(message="字段列表不能为空", groups={Save.class})
	private List<Param> all_param;//对应collection的所有字段，由数据平台提供
	
	private List<String> req_param;//请求参数，默认为空，则为无参，即无条件
	
	private List<String> ret_param;//返回参数，默认为空，则为无参，即返回所有数据
	
	@DBRef
	@NotNull(message="接口对应的tcp服务不能为空", groups={Save.class})
	private Tcp tcp;// 接口对应的tcp服务配置
	
	private String description;// 说明
	


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getCollection() {
		return collection;
	}

	public void setCollection(String collection) {
		this.collection = collection;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public List<Param> getAll_param() {
		return all_param;
	}

	public void setAll_param(List<Param> all_param) {
		this.all_param = all_param;
	}

	public List<String> getReq_param() {
		return req_param;
	}

	public void setReq_param(List<String> req_param) {
		this.req_param = req_param;
	}

	public List<String> getRet_param() {
		return ret_param;
	}

	public void setRet_param(List<String> ret_param) {
		this.ret_param = ret_param;
	}

	public Tcp getTcp() {
		return tcp;
	}

	public void setTcp(Tcp tcp) {
		this.tcp = tcp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	

	

	
}