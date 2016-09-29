package com.gykj.pojo;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;



/**
 * 参数字段类
 * 
 * @author xianyl
 * name + type+ isnecessity 联合唯一
 */
@Entity
public class Param extends Pojo implements Serializable {
	private static final long serialVersionUID = 1L;
	
	
	@NotEmpty(message="参数名字不能为空", groups={Save.class})
	private String name;// 参数名字
	
	@NotEmpty(message="参数类型不能为空", groups={Save.class})
	private String type;//参数类型, String Integer Float Array File Object 选1
	
	@NotNull(message="参数是否必要的标识不能为空", groups={Save.class})
	private Boolean isnecessity;//参数是否必要
	
	private String regular;//参数正则
	
	private String description;// 说明
	
	private String platform;

	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsnecessity() {
		return isnecessity;
	}

	public void setIsnecessity(Boolean isnecessity) {
		this.isnecessity = isnecessity;
	}
	

	public String getRegular() {
		return regular;
	}

	public void setRegular(String regular) {
		this.regular = regular;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	

	
}