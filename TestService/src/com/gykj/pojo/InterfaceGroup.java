package com.gykj.pojo;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.mongodb.core.mapping.DBRef;



/**
 * 接口组类
 * 
 * @author xianyl
 */
@Entity
public class InterfaceGroup extends Pojo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@NotEmpty(message="名字不能为空", groups={Save.class})
	private String name;// 相当于实际项目的命名空间，唯一，建议对应相关的平台
	
	@NotNull(message="分组类型不能为空", groups={Save.class})
	private Integer type;//用于区分是按collectio分组还是实际项目分组的数据，0为collection分组，1为实际项目分组。实际项目分组提供与用户关联，collectio分组使用于配置用户分组的
	
	@DBRef
	@NotNull(message="接口清单列表不能为空", groups={Save.class})
	private List<InterfaceList> interfacelist;//接口清单列表
	

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<InterfaceList> getInterfacelist() {
		return interfacelist;
	}

	public void setInterfacelist(List<InterfaceList> interfacelist) {
		this.interfacelist = interfacelist;
	}


	

	
}